package top.phrack.ctf.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.IPlogServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.BannedIps;
import top.phrack.ctf.pojo.StatusMsg;
import top.phrack.ctf.utils.CommonUtils;


@Controller
public class LoginController {
	private Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Resource
	private SubmissionServices submissionServices;
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public ModelAndView doGetlogin() throws Exception{
		ModelAndView mv = new ModelAndView("login");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setUserInfo(currentUser, userServices,teamServices, submissionServices,mv);
		CommonUtils.setControllerName(request, mv);
		if (currentUser.isAuthenticated())
		{
			return new ModelAndView("redirect:/home");
		}
		mv.setViewName("login");
		
		return mv;
		
	}
	
	@RequestMapping(value = "/logout")
	public String doLogout() throws Exception{
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		
		return "redirect:/home";
		
	}
	
	@Autowired
	private HttpServletRequest request;
	@Resource 
	private UserServices userServices;
	@Resource 
	private IPlogServices ipLogServices;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private TeamServices teamServices;

	
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public ModelAndView doPostlogin() throws Exception{
		ModelAndView mv = new ModelAndView("login");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		ArrayList<StatusMsg> states = new ArrayList<StatusMsg>();
		String username = request.getParameter("email");
		String password = request.getParameter("password");
		String captchanum = request.getParameter("captcha");
		String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (captchanum==null||!captchanum.equalsIgnoreCase(kaptchaCode))
		{
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Wrong captcha!");
			states.add(state);
			mv.addObject("stat",states);
			mv.setViewName("login");
			return mv;
		}
		if (username==null||username.length()==0)
		{
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Please input username!");
			states.add(state);
			mv.addObject("stat",states);
			mv.setViewName("login");
			return mv;
		}
		if (password==null||password.length()==0)
		{
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Please input password!");
			states.add(state);
			mv.addObject("stat",states);
			mv.setViewName("login");
			return mv;
		}
		//System.out.println("csrf_token:"+request.getParameter("pctf_csrf_token"));
		UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());
		String userip = request.getRemoteAddr(); 
		BannedIps ip = bannedIpServices.getRecordByIp(userip);
		if (ip!=null) {
			currentUser.logout();
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("You can't login!!May be your IP has been banned.");
			states.add(state);
			mv.addObject("stat",states);
			mv.setViewName("showinfo");
			return mv;
		}
		try {
			if (!currentUser.isAuthenticated()) {//使用shiro来验证
				token.setRememberMe(true);
				currentUser.login(token);//验证角色和权限
			}
			CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, username);
			CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
			StatusMsg state = new StatusMsg();
			state.settype("success");
			state.setmsg("Log in Successfully!!");
			states.add(state);
		} catch (Exception e) {
			log.error(e.getMessage());
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Username/Password incorrect!!");
			states.add(state);
			
		}
		
		mv.addObject("stat",states);
		mv.setViewName("login");
		return mv;
		
	}
	
	
}
