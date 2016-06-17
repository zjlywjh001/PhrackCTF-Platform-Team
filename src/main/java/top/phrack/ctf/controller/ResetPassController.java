/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;
import top.phrack.ctf.models.services.IPlogServices;
import top.phrack.ctf.models.services.PassResetServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Passreset;
import top.phrack.ctf.pojo.StatusMsg;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.utils.CommonUtils;
import top.phrack.ctf.utils.MailUtil;

/**
 * 重置密码的controller
 *
 * @author Jarvis
 * @date 2016年4月10日
 */
@Controller
public class ResetPassController {
	private Logger log = LoggerFactory.getLogger(ResetPassController.class);
	
	@Autowired
	private HttpServletRequest request;
	@Resource 
	private UserServices userServices;
	@Resource
	private PassResetServices passResetServices;
	@Resource 
	private IPlogServices ipLogServices;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private MailUtil mailUtil;
	@Resource
	private SubmissionServices submissionServices;
	@Resource
	private TeamServices teamServices;
	
	/**
	 * 这个是重设密码的方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resetpass", method = RequestMethod.GET)
	public ModelAndView Resetpass() throws Exception {
		ModelAndView mv = new ModelAndView("resetpass");
		Subject currentUser = SecurityUtils.getSubject();
		ArrayList<StatusMsg> states = new ArrayList<StatusMsg>();
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		CommonUtils.setControllerName(request, mv);
		String token = request.getParameter("token");
		
		if (token==null){
			mv.setViewName("resetpass");
			if (currentUser.isAuthenticated()||currentUser.isRemembered())
				return new ModelAndView("redirect:/home");
		} else {
			Passreset pr = passResetServices.getResetRecordBytoken(token);
			Date currenttime = new Date();
			if (pr==null || pr.getUsed() || currenttime.after(pr.getExpireson()) ) {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Password Reset Faild! Please check your url.");
				states.add(state);
				mv.addObject("stat",states);
				mv.setViewName("showinfo");
			} else {
				mv.addObject("token",token);
				mv.setViewName("setpass");
			}
		}
		
		
		return mv;
	}
	
	@RequestMapping(value="/resetpass", method = RequestMethod.POST)
	public ModelAndView RequestReset() throws Exception {
		ModelAndView mv = new ModelAndView("setpass");
		Subject currentUser = SecurityUtils.getSubject();
		ArrayList<StatusMsg> states = new ArrayList<StatusMsg>();
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		CommonUtils.setControllerName(request, mv);
		
		
		String contextpath = request.getContextPath();
		int serverport = request.getServerPort();
		String basePath = null;
		if (serverport==80||serverport==443) {
			basePath = request.getScheme() + "://"
					+ request.getServerName() + contextpath + "/";
		} else {
			basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + serverport
				+ contextpath + "/";
		}
		
		
		String email = request.getParameter("email");
		String captcha = request.getParameter("captcha");
		String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (captcha==null || !captcha.equalsIgnoreCase(kaptchaCode))
		{
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Wrong captcha!");
			states.add(state);
			mv.addObject("stat",states);
			mv.setViewName("resetpass");
		} else {
			if (email==null) {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Invalid Parameter!");
				states.add(state);
				mv.addObject("stat",states);
				mv.setViewName("resetpass");
			} else {
				String emalPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
				Pattern pattern = Pattern.compile(emalPattern);
				Matcher matcher = pattern.matcher(email);
				if (!matcher.find()) {
					StatusMsg state = new StatusMsg();
					state.settype("danger");
					state.setmsg("Invalid email format!!");
					states.add(state);
					mv.addObject("stat",states);
					mv.setViewName("resetpass");
				} else {
					Users userobj = userServices.getUserByEmail(email);
					if (userobj==null) {
						StatusMsg state = new StatusMsg();
						state.settype("danger");
						state.setmsg("User does not exist!!");
						states.add(state);
						mv.addObject("stat",states);
						mv.setViewName("resetpass");
					} else {
						Date currenttime = new Date();
						Passreset pr = new Passreset();
						pr.setCreatetime(currenttime);
						pr.setExpireson(new Date(currenttime.getTime()+24*3600*1000));
						String resettoken = UUID.randomUUID().toString();
						pr.setResettoken(resettoken);
						pr.setUsed(false);
						pr.setUserid(userobj.getId());
						String reseturl = basePath+"resetpass?token="+resettoken;
						passResetServices.insertNewtoken(pr);
						mailUtil.sendPasswordResetMail(email, reseturl,javaMailSender);
						StatusMsg state = new StatusMsg();
						state.settype("info");
						state.setmsg("A password reset email has been sent to your mailbox. Please check your inbox and continue your password reset process.");
						states.add(state);
						mv.addObject("stat",states);
						mv.setViewName("showinfo");
						mv.addObject("nextpage","home");
					}
				}	
			}
		}
		
		return mv;
	}
	
	/**
	 * 链接重置密码
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resetpass/{token}", method = RequestMethod.POST)
	public ModelAndView SetPass(@PathVariable String token) throws Exception {
		ModelAndView mv = new ModelAndView("setpass");
		Subject currentUser = SecurityUtils.getSubject();
		ArrayList<StatusMsg> states = new ArrayList<StatusMsg>();
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		CommonUtils.setControllerName(request, mv);

		
		if (token==null) {
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Password Reset Faild! Please check your url.");
			states.add(state);
			mv.addObject("stat",states);
			mv.setViewName("showinfo");
			mv.addObject("nextpage","home");
		} else {
			Passreset pr = passResetServices.getResetRecordBytoken(token);
			Date currenttime = new Date();
			if (pr==null || pr.getUsed() || currenttime.after(pr.getExpireson()) ) {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Password Reset Faild! This token is invalid.");
				states.add(state);
				mv.addObject("stat",states);
				mv.setViewName("showinfo");
				mv.addObject("nextpage","home");
			} else {
				String captchanum = request.getParameter("captcha");
				String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY); 
				if (captchanum==null || !captchanum.equalsIgnoreCase(kaptchaCode))
				{
					StatusMsg state = new StatusMsg();
					state.settype("danger");
					state.setmsg("Wrong captcha!");
					states.add(state);
					mv.addObject("stat",states);
					mv.setViewName("setpass");
					return mv;
				}
				String newpass1 = request.getParameter("newpass");
				String newpass2 = request.getParameter("newpass2");
				if (!newpass1.equals(newpass2)) {
					StatusMsg state = new StatusMsg();
					state.settype("danger");
					state.setmsg("Password not match!");
					states.add(state);
					mv.addObject("stat",states);
					mv.setViewName("setpass");
					return mv;
				} else {
					if (newpass1.length()==0||newpass2.length()==0)
					{
						StatusMsg state = new StatusMsg();
						state.settype("danger");
						state.setmsg("Please input new password!");
						states.add(state);
						mv.addObject("stat",states);
						mv.setViewName("setpass");
					} else {
						pr.setUsed(true);
						pr.setUsedtime(currenttime);
						passResetServices.updateResetRecord(pr);
						Users userobj = userServices.getUserById(pr.getUserid());
						userobj.setPassword(new SimpleHash("SHA-256",newpass1,userobj.getSalt()).toHex());
						userServices.updateUser(userobj);
						CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
						StatusMsg state = new StatusMsg();
						state.settype("success");
						state.setmsg("Password Reset successfully!");
						states.add(state);
						currentUser.logout();
						mv.addObject("stat",states);
						mv.setViewName("showinfo");
						mv.addObject("nextpage","login");
					}
				}
			}
		}
		
		mv.addObject("token",token);
		return mv;
	}
}
