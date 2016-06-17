/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.CategoryServices;
import top.phrack.ctf.models.services.ChallengeServices;
import top.phrack.ctf.models.services.CountryServices;
import top.phrack.ctf.models.services.IPlogServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.CateProcess;
import top.phrack.ctf.pojo.Categories;
import top.phrack.ctf.pojo.Challenges;
import top.phrack.ctf.pojo.Countries;
import top.phrack.ctf.pojo.SolveTable;
import top.phrack.ctf.pojo.StatusMsg;
import top.phrack.ctf.pojo.Submissions;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.utils.CommonUtils;

/**
 * 个人面板控制器
 *
 * @author Jarvis
 * @date 2016年4月14日
 */
@Controller
public class DashboardController {
	
	private Logger log = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private HttpServletRequest request;
	@Resource 
	private UserServices userServices;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private SubmissionServices submissionServices;
	@Resource
	private CountryServices countryServices;
	@Resource
	private ChallengeServices challengeServices;
	@Resource
	private CategoryServices categoryServices;
	@Resource
	private IPlogServices ipLogServices;
	@Resource
	private TeamServices teamServices;
	
	
	@RequestMapping(value = "/dashboard", method = {RequestMethod.GET} )
	public ModelAndView DashBoard() throws Exception {
		ModelAndView mv = new ModelAndView("dashboard");
		
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		Users userobj = CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		Countries usercountry = countryServices.getCountryById(userobj.getCountryid());
		mv.addObject("username",userobj.getUsername());
		mv.addObject("userid", userobj.getId());
		mv.addObject("countrycode",usercountry.getCountrycode());
		mv.addObject("userdec",userobj.getDescription());
		mv.addObject("country",usercountry.getCountryname());
		mv.addObject("userscore",userobj.getScore());
		mv.addObject("userrank",CommonUtils.getUserrank(userobj,userServices,submissionServices));
		List<Challenges> allchall = challengeServices.getAllAvailChallenges();
		long sum = 0;
		for (Challenges ch:allchall) {
			sum += ch.getScore();
		}
		mv.addObject("totalscore",sum);
		long allper = 0;
		if (sum!=0) {
			allper = Math.round(((double)userobj.getScore())/((double)sum)*100);
		} 
		
		mv.addObject("totalpercent",allper);
		ArrayList<CateProcess> process = new ArrayList<CateProcess>();
		List<Submissions> passedtasks = submissionServices.getCorrectSubmitByUserid(userobj.getId());
		Map<Categories,Long> statcate = new HashMap<Categories,Long>();
		ArrayList<SolveTable> tab = new ArrayList<SolveTable>();
		for (Submissions sub:passedtasks) {
			Challenges challobj = challengeServices.getChallengeById(sub.getChallengeId());
			Categories subcate = categoryServices.selectById(challobj.getCategoryid());
			SolveTable tabitem = new SolveTable();
			tabitem.setid(challobj.getId());
			tabitem.settitle(challobj.getTitle());
			tabitem.setscore(challobj.getScore());
			tabitem.setcatename(subcate.getName());
			tabitem.setcatemark(subcate.getMark());
			tabitem.setpasstime(submissionServices.getCorrectByUserIdAndTaskId(userobj.getId(), challobj.getId()).getSubmitTime());
			if (statcate.containsKey(subcate)) {
				Long cs = statcate.get(subcate);
				cs += challobj.getScore();
				statcate.put(subcate, cs);
			} else {
				statcate.put(subcate, challobj.getScore());
			}
			tab.add(tabitem);
		}
		
		mv.addObject("passtask",tab);
		
		for (Challenges ch:allchall) {
			Categories challcate = categoryServices.selectById(ch.getCategoryid());
			if (statcate.containsKey(challcate)) {
				continue;
			} else {
				statcate.put(challcate, Long.valueOf(0));
			}
		}
		for (Map.Entry<Categories,Long> stc:statcate.entrySet()) {
			CateProcess cp = new CateProcess();
			Categories cate = stc.getKey();
			long catescore = stc.getValue();
			long catetotal = challengeServices.getCateScoreByCateId(cate.getId());
			cp.setname(cate.getName());
			cp.setproc(catescore);
			cp.settotal(catetotal);
			cp.setstyle(cate.getMark());
			if (catetotal!=0) {
				cp.setpercent(((double)catescore)/((double)catetotal)*100);
			} else {
				cp.setpercent(0);
			}
			if (sum!=0) {
				cp.setpercentall(((double)catescore)/((double)sum)*100);
			} else {
				cp.setpercentall(0);
			}
			
			process.add(cp);

		}

		mv.addObject("userstat",process);
		mv.setViewName("dashboard");
		mv.addObject("currentuser",userobj);
		List<Countries> cts = countryServices.SelectAllCountry();
		mv.addObject("countrylist",cts);
		return mv;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/saveprofile.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String SaveProfile() throws Exception {
		Map<String,String> resp = new HashMap<String,String>();
		String phone = request.getParameter("phone");
		String organize = request.getParameter("organize");
		String countryid = request.getParameter("country");
		String description = request.getParameter("description");

		Subject currentUser = SecurityUtils.getSubject();
		Users userobj = userServices.getUserByEmail((String)currentUser.getPrincipal());
		assert(userobj!=null);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) { //如果ip被ban了自动踢出去
			currentUser.logout();
			return null;
		}
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (phone==null||organize==null||countryid==null||description==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (phone!=null&&phone.length()>0) {
			phone = CommonUtils.XSSFilter(phone);
			Pattern p = Pattern.compile("^(\\+){0,1}\\d+$");
			Matcher matcher = p.matcher(phone);
			if (!matcher.find()) {
				resp.put("errmsg", "Invalid phone format!!");
				resp.put("err", "-7");
				String result = JSONObject.fromObject(resp).toString();
				return result;
			}
		} else {
			phone = "";
		}
		
		organize = CommonUtils.XSSFilter(organize);
		countryid = CommonUtils.XSSFilter(countryid);
		description = CommonUtils.XSSFilter(description);
		
		if (!StringUtils.isNumeric(countryid)||Long.valueOf(countryid)<1||Integer.valueOf(countryid)>250) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		userobj.setPhone(phone);
		userobj.setOrganization(organize);
		userobj.setCountryid(Long.valueOf(countryid));
		userobj.setDescription(description);
		
		
		userServices.updateUser(userobj);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", "Profile update successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/changepass.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String ChangePass() throws Exception {
		Map<String,String> resp = new HashMap<String,String>();
		String oldpass = request.getParameter("oldpass");
		String newpass = request.getParameter("newpass");
		String repeatnewpass = request.getParameter("confirm");
		String captcha = request.getParameter("captcha");

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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (captcha==null || !captcha.equalsIgnoreCase(kaptchaCode))
		{
			resp.put("errmsg", "Wrong Captcha!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (oldpass==null||newpass==null||repeatnewpass==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String passforcomp = new SimpleHash("SHA-256",oldpass,userobj.getSalt()).toHex();
		if (!userobj.getPassword().equals(passforcomp)) {
			resp.put("errmsg", "Original password incorrect!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!newpass.equals(repeatnewpass)) {
			resp.put("errmsg", "Password not match!!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (newpass.length()==0||repeatnewpass.length()==0l) {
			resp.put("errmsg", "Please input new password!!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String newpassdigest = new SimpleHash("SHA-256",newpass,userobj.getSalt()).toHex();
		
		userobj.setPassword(newpassdigest);
		
		
		userServices.updateUser(userobj);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", "New password set successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
}
