/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.IPlogServices;
import top.phrack.ctf.models.services.OperateLogServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Countries;
import top.phrack.ctf.pojo.News;
import top.phrack.ctf.pojo.UserList;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.utils.CommonUtils;
import top.phrack.ctf.utils.LogUtils;
import top.phrack.ctf.utils.MailUtil;

/**
 * 后台邮件功能控制器
 *
 * @author Jarvis
 * @date 2016年4月19日
 */
@Controller
public class MailController {
	private Logger log = LoggerFactory.getLogger(MailController.class);
	
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MailUtil mailUtil;
	@Autowired
	private JavaMailSender javaMailSender;
	@Resource 
	private UserServices userServices;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private SubmissionServices submissionServices;
	@Resource 
	private IPlogServices ipLogServices;
	@Resource 
	private OperateLogServices operateLogServices;
	@Resource
	private TeamServices teamServices;
	
	@RequestMapping(value="admin/mails",method = {RequestMethod.GET})
	public ModelAndView Mails() throws Exception {
		ModelAndView mv = new ModelAndView("admin/mails");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		String uid = request.getParameter("target");
		Users touser= null;
		if (uid!=null && uid.length()>0 && StringUtils.isNumeric(uid) && (touser = userServices.getUserById(Long.valueOf(uid)))!=null) {
			mv.addObject("target", touser.getEmail());
		} else  if (uid!=null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		
		mv.setViewName("admin/mails");
		return mv;
	}
	
	@RequestMapping(value="admin/maillist",method = {RequestMethod.GET})
	public ModelAndView MailList() throws Exception {
		ModelAndView mv = new ModelAndView("admin/maillist");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		/*显示用户User列表*/
		List<Users> alluser = userServices.getAllUsers();
		if (alluser!=null) {
			mv.addObject("userlist",alluser);
		}
		
		mv.setViewName("admin/maillist");
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="admin/maillist",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String doSendMail() throws Exception {
		Map<String,String> resp = new HashMap<String,String>();
		Subject currentUser = SecurityUtils.getSubject();
		Users userobj = userServices.getUserByEmail((String)currentUser.getPrincipal());
		assert(userobj!=null);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) { //如果ip被ban了自动踢出去
			currentUser.logout();
			resp.put("errmsg", "Your IP has been banned!!");
			resp.put("err", "-100");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		
		
		}
		
		String receivers = request.getParameter("maillist");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		if (receivers == null) {
			resp.put("errmsg","Receivers can't be empty!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (title == null) {
			title = new String("Untitled");
		}
		
		if (content==null) {
			content = new String("");
		}
		
		String[] targets = receivers.split(",");
		System.out.println(Arrays.toString(targets));
		
		String emalPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern pattern = Pattern.compile(emalPattern);
		for (int i = 0;i<targets.length;i++) {
			Matcher matcher = pattern.matcher(targets[i]);
			if (!matcher.find()) {
				resp.put("errmsg","Invalid email format!!");
				resp.put("err", "-2");
				String result = JSONObject.fromObject(resp).toString();
				return result;
			}
		}
		content = CommonUtils.filterUserInputContent(content);
		
		mailUtil.mailTo(targets, title, content, javaMailSender);
		
		String loginfo = "Send mail to:"+Arrays.toString(targets);
		if (loginfo.length()>128){
			loginfo = loginfo.substring(0, 128);
		}
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, loginfo);
		resp.put("errmsg", "Mail send successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
	}
}
