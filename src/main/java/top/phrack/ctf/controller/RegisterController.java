/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.CountryServices;
import top.phrack.ctf.models.services.PassResetServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Countries;
import top.phrack.ctf.pojo.Passreset;
import top.phrack.ctf.pojo.StatusMsg;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.utils.CommonUtils;
import top.phrack.ctf.utils.MailUtil;

/**
 * 注册页面控制器
 *
 * @author Jarvis
 * @date 2016年4月8日
 */
@Controller
public class RegisterController {
	private Logger log = LoggerFactory.getLogger(RegisterController.class);
	

	@Resource 
	private UserServices userServices;
	@Resource
	private CountryServices countryServices;
	@Resource
	private PassResetServices passResetServices;
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MailUtil mailUtil;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private SubmissionServices submissionServices;
	@Resource
	private TeamServices teamServices;
	
	@RequestMapping(value = "/register",method = RequestMethod.GET)
	public ModelAndView doGetRegister() throws Exception {
		ModelAndView mv = new ModelAndView("register");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		CommonUtils.setControllerName(request, mv);
		
		if (currentUser.isAuthenticated()||currentUser.isRemembered())
		{
			return new ModelAndView("redirect:/home");
		}
		List<Countries> cts = countryServices.SelectAllCountry();
		mv.addObject("country",cts);
		mv.setViewName("register");
		return mv;
	}
	
	@RequestMapping(value="register",method = RequestMethod.POST)
	public ModelAndView doPostRegister() throws Exception {
		ModelAndView mv = new ModelAndView("register");
		int errorcounter;
		List<Countries> cts = countryServices.SelectAllCountry();
		mv.addObject("country",cts);
		ArrayList<StatusMsg> states = new ArrayList<StatusMsg>();
		Subject currentUser = SecurityUtils.getSubject();
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
		
		
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
		}
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String organize = request.getParameter("organize");
		String phone = request.getParameter("phone");
		String countryid = request.getParameter("countryid");
		String captchanum = request.getParameter("captcha");
		
		String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (captchanum==null || !captchanum.equalsIgnoreCase(kaptchaCode))
		{
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Wrong captcha!");
			states.add(state);
			mv.addObject("stat",states);
			mv.setViewName("register");
			return mv;
		}
		
		if (email==null||username==null) {
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Invalid Parameter!");
			states.add(state);
			mv.addObject("stat",states);
			mv.setViewName("register");
			return mv;
		}
		
		email = CommonUtils.XSSFilter(email).toLowerCase();
		username = CommonUtils.XSSFilter(username);
		if (organize==null) {
			organize = "";
		} else {
			organize = CommonUtils.XSSFilter(organize);
		}
		
		errorcounter = 0;
		
		if (phone!=null&&phone.length()>0) {
			Pattern p = Pattern.compile("^(\\+){0,1}\\d+$");
			Matcher matcher = p.matcher(phone);
			if (!matcher.find()) {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Invalid phone format!!");
				states.add(state);
				mv.addObject("stat",states);
				errorcounter ++;
			}
		} else {
			phone = "";
		}
		
		if (email.equals("")) {
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Please input your e-mail!!");
			states.add(state);
			mv.addObject("stat",states);
			errorcounter ++;
		} else {
			String emalPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
			Pattern pattern = Pattern.compile(emalPattern);
			Matcher matcher = pattern.matcher(email);
			if (!matcher.find()) {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Invalid mail format!!");
				states.add(state);
				mv.addObject("stat",states);
				errorcounter ++;
			} else {
				email = CommonUtils.XSSFilter(email);
				if (userServices.getUserByEmail(email)!=null)
				{
					StatusMsg state = new StatusMsg();
					state.settype("danger");
					state.setmsg("This email has been used!!");
					states.add(state);
					mv.addObject("stat",states);
					errorcounter ++;
				}
			}
		}
		
		if (username == null || username.equals("")) {
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Please input your username!!");
			states.add(state);
			mv.addObject("stat",states);
			errorcounter ++;
		} else {
			if (userServices.getUserByName(username)!=null)
			{
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("This username has been used!!");
				states.add(state);
				mv.addObject("stat",states);
				errorcounter ++;
			}
		}
		
		if (countryid==null){
			StatusMsg state = new StatusMsg();
			state.settype("danger");
			state.setmsg("Please select your country/location!!");
			states.add(state);
			mv.addObject("stat",states);
			errorcounter ++;
		} else {
			if (!StringUtils.isNumeric(countryid)) {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Invalid Country!");
				states.add(state);
				mv.addObject("stat",states);
				errorcounter ++;
			} else {
				if (Integer.valueOf(countryid)<1||Integer.valueOf(countryid)>250) {
					StatusMsg state = new StatusMsg();
					state.settype("danger");
					state.setmsg("Invalid Country!");
					states.add(state);
					mv.addObject("stat",states);
					errorcounter ++;
				}
			}
		}
		
		if (errorcounter == 0) {
			Users aUser = new Users();
			String salt = RandomStringUtils.randomAlphanumeric(64);
			String randompass = RandomStringUtils.randomAlphanumeric(10);
			Date currenttime = new Date();
			aUser.setUsername(username);
			aUser.setEmail(email);
			aUser.setPassword(new SimpleHash("SHA-256",randompass,salt).toHex());
			aUser.setPhone(phone);
			aUser.setOrganization(organize);
			aUser.setCountryid(Long.valueOf(countryid));
			aUser.setIsenabled(true);
			aUser.setScore(Long.valueOf(0));
			aUser.setSalt(salt);
			aUser.setRegtime(currenttime);
			aUser.setRole("user");
			int result = userServices.insertNewUser(aUser);
			if (result!=0) {
				StatusMsg state = new StatusMsg();
				state.settype("success");
				state.setmsg("A password reset e-mail has been sent to your email address "+email+". Please check your inbox and verify your registration.");
				states.add(state);
				mv.addObject("stat",states);
				Passreset pr = new Passreset();
				pr.setCreatetime(currenttime);
				pr.setExpireson(new Date(currenttime.getTime()+24*3600*1000));
				String resettoken = UUID.randomUUID().toString();
				pr.setResettoken(resettoken);
				pr.setUsed(false);
				aUser = userServices.getUserByEmail(email);
				pr.setUserid(aUser.getId());
				String reseturl = basePath+"resetpass?token="+resettoken;
				passResetServices.insertNewtoken(pr);
				mailUtil.sendPasswordResetMail(email, reseturl,javaMailSender);
				mv.addObject("nextpage","login");
				mv.setViewName("showinfo");
				return mv;
			} else {
				StatusMsg state = new StatusMsg();
				state.settype("danger");
				state.setmsg("Unknown Error!Please try later.");
				states.add(state);
				mv.addObject("stat",states);
			}
		}
		
		
		mv.setViewName("register");
		return mv;
	}
}
