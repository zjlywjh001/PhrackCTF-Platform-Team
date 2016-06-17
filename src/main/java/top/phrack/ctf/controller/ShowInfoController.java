/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.StatusMsg;
import top.phrack.ctf.utils.CommonUtils;

/**
 * 提示信息的页面
 *
 * @author Jarvis
 * @date 2016年4月12日
 */
@Controller
public class ShowInfoController {
	
	private Logger log = LoggerFactory.getLogger(ShowInfoController.class);
	
	@Resource 
	private UserServices userServices;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private SubmissionServices submissionServices;
	@Autowired
	private HttpServletRequest request;
	@Resource
	private TeamServices teamServices;
	
	
	@RequestMapping(value="/showinfo",method=RequestMethod.GET)
	public ModelAndView ShowInfo() throws Exception {
		ModelAndView mv = new ModelAndView("showinfo");
		ArrayList<StatusMsg> states = new ArrayList<StatusMsg>();
		String errid = request.getParameter("err");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
		}
		
		if (errid==null) {
			return new ModelAndView("redirect:/home");
		} else {
			if (errid.equals("-1")) {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Not Permitted!!");
				states.add(state);
				mv.addObject("stat",states);
			} else if (errid.equals("404")) {
				StatusMsg state = new StatusMsg();
				state.settype("warning");
				state.setmsg("Sorry, this page is not found.");
				states.add(state);
				mv.addObject("stat",states);
			} else if (errid.equals("400")) {
				StatusMsg state = new StatusMsg();
				state.settype("warning");
				state.setmsg("Sorry, Resource not found.");
				states.add(state);
				mv.addObject("stat",states);
			} else if (errid.equals("-99")) {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Your IP has been banned!!");
				states.add(state);
				mv.addObject("stat",states);
			} else if (errid.equals("100")){
				StatusMsg state = new StatusMsg();
				state.settype("warning");
				state.setmsg("File Storage is full!!");
				states.add(state);
				mv.addObject("stat",states);
			} else if (errid.equals("101")){
				StatusMsg state = new StatusMsg();
				state.settype("warning");
				state.setmsg("Admin can't use team function !!");
				states.add(state);
				mv.addObject("stat",states);
			} else if (errid.equals("102")){
				StatusMsg state = new StatusMsg();
				state.settype("warning");
				state.setmsg("You should join or create a team before take part in challenge !!");
				states.add(state);
				mv.addObject("stat",states);
			} else {
				return new ModelAndView("redirect:/home");
			}
		} 
		mv.setViewName("showinfo");
		return mv;
	}
}
