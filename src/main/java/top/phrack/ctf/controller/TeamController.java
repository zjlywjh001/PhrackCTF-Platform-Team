/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
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
import top.phrack.ctf.models.services.ConfigServices;
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
import top.phrack.ctf.pojo.Submissions;
import top.phrack.ctf.pojo.TeamMember;
import top.phrack.ctf.pojo.Teams;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.utils.CommonUtils;

/**
 * My Team页面Controller
 *
 * @author Jarvis
 * @date 2016年4月21日
 */
@Controller
public class TeamController {
	private Logger log = LoggerFactory.getLogger(TeamController.class);
	
	@Autowired
	private HttpServletRequest request;
	@Resource
	private UserServices userServices;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private TeamServices teamServices;
	@Resource
	private SubmissionServices submissionServices;
	@Resource 
	private IPlogServices ipLogServices;
	@Resource
	private CountryServices countryServices;
	@Resource
	private ChallengeServices challengeServices;
	@Resource
	private CategoryServices categoryServices;
	@Resource
	private ConfigServices configServices;
	
	@RequestMapping(value="/myteam",method = {RequestMethod.GET})
	public ModelAndView MyTeam() throws Exception {
		ModelAndView mv = new ModelAndView("myteam");
		
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		Users userobj = CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		if (currentUser.hasRole("admin")) {
			return new ModelAndView("redirect:/showinfo?err=101");
		}
		
		mv.addObject("thisuser",userobj);
		
		if (userobj.getTeamid()==null) {
			List<Countries> cts = countryServices.SelectAllCountry();
			mv.addObject("country",cts);
		}
		
		
		if (userobj.getTeamid()!=null) {
			Teams thisteam = teamServices.getTeamById(userobj.getTeamid());
			Countries teamcountry = countryServices.getCountryById(thisteam.getCountryid());
			mv.addObject("teamname",thisteam.getName());
			mv.addObject("countrycode",teamcountry.getCountrycode());
			mv.addObject("country",teamcountry.getCountryname());
			mv.addObject("teamscore",thisteam.getScore());
			mv.addObject("teamrank",CommonUtils.getTeamrank(thisteam,teamServices,userServices,submissionServices));
			List<Challenges> allchall = challengeServices.getAllAvailChallenges();
			long sum = 0;
			for (Challenges ch:allchall) {
				sum += ch.getScore();
			}
			mv.addObject("totalscore",sum);
			long allper = 0;
			if (sum!=0) {
				allper = Math.round(((double)thisteam.getScore())/((double)sum)*100);
			} 
			
			mv.addObject("totalpercent",allper);
			ArrayList<CateProcess> process = new ArrayList<CateProcess>();
			ArrayList<Submissions> passedtasks = new ArrayList<Submissions>();
			ArrayList<Submissions> usercorrect = new ArrayList<Submissions>();

			List<Submissions> allcorrect = submissionServices.getAllCorrectOrderByTime();
			List<Users> teammembers = userServices.getAllUsersByTeamId(thisteam.getId());

			if (teammembers!=null) {
				for (int i=0;i<allcorrect.size();i++) {
					Users userinteam = null;
					for (Users u:teammembers) {
						if (u.getTeamid()!=null && u.getTeamid().longValue()==thisteam.getId().longValue() 
								&& allcorrect.get(i).getUserid().longValue()==u.getId().longValue()) {
							userinteam = u;
							break;
						}
					}
					
					if (userinteam!=null) {
						usercorrect.add(allcorrect.get(i));
					}
					
					
				}
				/*for (Users u:teammembers) {
					List<Submissions> userpassedtasks = submissionServices.getCorrectSubmitByUserid(u.getId());
					passedtasks.addAll(userpassedtasks);
				}*/
				
				passedtasks.addAll(usercorrect);
				Collections.reverse(passedtasks);
			}
			
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
				tabitem.setpasstime(sub.getSubmitTime());
				tabitem.setsolvedby(userServices.getUserById(sub.getUserid()).getUsername());
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

			mv.addObject("teamstat",process);
			
			ArrayList<TeamMember> tm = new ArrayList<TeamMember>();
			if (teammembers!=null) {
				for (Users u:teammembers) {
					TeamMember atm = new TeamMember();
					atm.setId(u.getId());
					atm.setScore(u.getScore());
					atm.setUsername(u.getUsername());
					atm.setRole(u.getRole());
					atm.setsolved(submissionServices.getCorrectSubmitByUserid(u.getId()).size());
					tm.add(atm);
				}
			}
			mv.addObject("members",tm);
			
			
			mv.addObject("thisteam",thisteam);
		}
		
		List<Countries> cts = countryServices.SelectAllCountry();
		mv.addObject("countrylist",cts);
		
		mv.setViewName("myteam");
		return mv;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/addteam.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String AddTeam() throws Exception {
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
			resp.put("errmsg", "Session time out, please log in!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getTeamid()!=null) {
			resp.put("errmsg", "You are already in a team!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		
		
		String teamname = request.getParameter("teamname");
		String teamorgan = request.getParameter("organization");
		String countryid = request.getParameter("countryid");
		String teamdescript = request.getParameter("description");
		
		if (teamname==null || teamorgan == null || teamdescript==null || countryid==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (teamname.length()==0) {
			resp.put("errmsg", "Please input team name!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(countryid)||countryid.length()==0 || Long.valueOf(countryid)<1||Long.valueOf(countryid)>250) {
			resp.put("errmsg", "Invalid Country!!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		teamname = CommonUtils.XSSFilter(teamname);
		teamorgan = CommonUtils.XSSFilter(teamorgan);
		teamdescript = CommonUtils.XSSFilter(teamdescript);
		
		
		Teams checkexist = teamServices.getTeamByName(teamname);
		if (checkexist!=null) {
			resp.put("errmsg", "This team name is exist!!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (currentUser.hasRole("admin")){
			resp.put("errmsg", "Admin can't create team!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Date nowtime = new Date();
		Date compstart = CommonUtils.getStartDate(configServices);
		if (compstart!=null && nowtime.after(compstart)) {
			resp.put("errmsg", "The competition has started! Can't do this operation!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Teams ateam = new Teams();
		ateam.setName(teamname);
		ateam.setOrganization(teamorgan);
		ateam.setScore(userobj.getScore());
		ateam.setCountryid(Long.valueOf(countryid));
		String teamtoken = RandomStringUtils.randomAlphanumeric(128);
		ateam.setTeamtoken(teamtoken);
		ateam.setIsenabled(true);
		ateam.setCreatetime(new Date());
		ateam.setDescription(teamdescript);
		teamServices.createANewTeam(ateam);
		userobj.setTeamid(ateam.getId());
		userobj.setRole("leader");
		userServices.updateUser(userobj);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", "Team Create Successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/jointeam.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String JoinTeam() throws Exception {
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
			resp.put("errmsg", "Session time out, please log in!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getTeamid()!=null) {
			resp.put("errmsg", "You are already in a team!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String teamtoken = request.getParameter("token");
		
		if (teamtoken==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Teams thisteam = teamServices.getTeamByToken(teamtoken);
		if (thisteam==null) {
			resp.put("errmsg", "Invalid token!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (currentUser.hasRole("admin")){
			resp.put("errmsg", "Admin can't join team!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		List<Users> teamuser = userServices.getAllUsersByTeamId(thisteam.getId());
		if (teamuser.size()>=CommonUtils.getMaxTeamMembers(configServices)) {
			resp.put("errmsg", "This team is full!!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Date nowtime = new Date();
		Date compstart = CommonUtils.getStartDate(configServices);
		if (compstart!=null && nowtime.after(compstart)) {
			resp.put("errmsg", "The competition has started! Can't do this operation!");
			resp.put("err", "-7");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		userobj.setTeamid(thisteam.getId());
		userServices.updateUser(userobj);
		Long oldscore = thisteam.getScore();
		Long newscore = oldscore + userobj.getScore();
		thisteam.setScore(newscore);
		teamServices.updateTeamInfo(thisteam);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", "You joined in team "+thisteam.getName()+" successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value="/quitteam.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String QuitTeam() throws Exception {
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
			resp.put("errmsg", "Session time out, please log in!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getTeamid()==null) {
			resp.put("errmsg", "You are not in any team!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String teamid = request.getParameter("teamid");
		
		if (teamid==null || !StringUtils.isNumeric(teamid) || teamid.length()==0) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Teams thisteam = teamServices.getTeamById(Long.valueOf(teamid));
		if (thisteam==null) {
			resp.put("errmsg", "No such team with id: "+teamid);
			resp.put("err", "-7");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getTeamid()!=Long.valueOf(teamid)) {
			resp.put("errmsg", "You are not in team with id: "+teamid);
			resp.put("err", "-8");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getRole().equals("leader")) {
			resp.put("errmsg", "Team Creator can't quit team. Please use dismiss function.");
			resp.put("err", "-9");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Date nowtime = new Date();
		Date compstart = CommonUtils.getStartDate(configServices);
		if (compstart!=null && nowtime.after(compstart)) {
			resp.put("errmsg", "The competition has started! Can't do this operation!");
			resp.put("err", "-7");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}

		userobj.setTeamid(null);
		userobj.setRole("user");
		userServices.updateUser(userobj);
		Long oldscore = thisteam.getScore();
		Long newscore = oldscore - userobj.getScore();
		thisteam.setScore(newscore);
		teamServices.updateTeamInfo(thisteam);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", "You have quit team "+thisteam.getName()+" successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value="/saveteam.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String ModifyTeam() throws Exception {
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
			resp.put("errmsg", "Session time out, please log in!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("leader")) {
			resp.put("errmsg", "You are not team leader!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getTeamid()==null) {
			resp.put("errmsg", "You are not in any team!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String teamid = request.getParameter("id");
		String teamorgan = request.getParameter("organization");
		String countryid = request.getParameter("countryid");
		String teamdescript = request.getParameter("description");
		
		if (teamid==null || teamorgan == null || teamdescript==null || countryid==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(countryid)||countryid.length()==0 || Long.valueOf(countryid)<1||Long.valueOf(countryid)>250) {
			resp.put("errmsg", "Invalid Country!!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(teamid)||teamid.length()==0) {
			resp.put("errmsg", "Invalid Team Id!!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long teamidnum = Long.valueOf(teamid);
		teamorgan = CommonUtils.XSSFilter(teamorgan);
		teamdescript = CommonUtils.XSSFilter(teamdescript);
		
		
		Teams thisteam = teamServices.getTeamById(teamidnum);
		if (thisteam==null) {
			resp.put("errmsg", "Team with id: "+teamid+" do not exist!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thisteam.getId()!=userobj.getTeamid()) {
			resp.put("errmsg", "You are not leader of this team!!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}

		thisteam.setCountryid(Long.valueOf(countryid));
		thisteam.setOrganization(teamorgan);
		thisteam.setDescription(teamdescript);

		teamServices.updateTeamInfo(thisteam);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", "Team Info Updated Successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/resettoken.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String ResetTeamToken() throws Exception {
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
			resp.put("errmsg", "Session time out, please log in!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("leader")) {
			resp.put("errmsg", "You are not team leader!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getTeamid()==null) {
			resp.put("errmsg", "You are not in any team!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String teamid = request.getParameter("id");
		
		if (teamid==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(teamid)||teamid.length()==0) {
			resp.put("errmsg", "Invalid Team Id!!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long teamidnum = Long.valueOf(teamid);
		
		Teams thisteam = teamServices.getTeamById(teamidnum);
		if (thisteam==null) {
			resp.put("errmsg", "Team with id: "+teamid+" do not exist!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thisteam.getId()!=userobj.getTeamid()) {
			resp.put("errmsg", "You are not leader of this team!!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}

		String newtoken = RandomStringUtils.randomAlphanumeric(128);
		thisteam.setTeamtoken(newtoken);

		teamServices.updateTeamInfo(thisteam);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", "Token Reset Successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/kickmember.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String Kickmember() throws Exception {
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
			resp.put("errmsg", "Session time out, please log in!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("leader")) {
			resp.put("errmsg", "You are not team leader!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getTeamid()==null) {
			resp.put("errmsg", "You are not in any team!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String teamid = request.getParameter("teamid");
		String memberid = request.getParameter("memberid");
		
		if (teamid==null || memberid==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(teamid)||teamid.length()==0) {
			resp.put("errmsg", "Invalid Team Id!!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(memberid)||memberid.length()==0) {
			resp.put("errmsg", "Invalid Member Id!!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long teamidnum = Long.valueOf(teamid);
		Long memberidnum = Long.valueOf(memberid);
		
		Teams thisteam = teamServices.getTeamById(teamidnum);
		Users thismember = userServices.getUserById(memberidnum);
		if (thisteam==null) {
			resp.put("errmsg", "Team with id: "+teamid+" do not exist!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thisteam.getId()!=userobj.getTeamid()) {
			resp.put("errmsg", "You are not leader of this team!!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thismember==null) {
			resp.put("errmsg", "This member do not exist!!");
			resp.put("err", "-7");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thismember.getRole().equals("leader")||thismember.getId()==userobj.getId()) {
			resp.put("errmsg", "You can't kick yourself!!");
			resp.put("err", "-8");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thismember.getTeamid()!=thisteam.getId()) {
			resp.put("errmsg", "This user is not in this team!!");
			resp.put("err", "-9");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Date nowtime = new Date();
		Date compstart = CommonUtils.getStartDate(configServices);
		if (compstart!=null && nowtime.after(compstart)) {
			resp.put("errmsg", "The competition has started! Can't do this operation!");
			resp.put("err", "-10");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}

		thismember.setTeamid(null);
		Long oldscore = thisteam.getScore();
		Long newscore = oldscore - thismember.getScore();
		thisteam.setScore(newscore);
		teamServices.updateTeamInfo(thisteam);
		userServices.updateUser(thismember);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", thismember.getUsername()+" kicked out successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/dismissteam.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String Dismissteam() throws Exception {
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
			resp.put("errmsg", "Session time out, please log in!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("leader")) {
			resp.put("errmsg", "You are not team leader!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (userobj.getTeamid()==null) {
			resp.put("errmsg", "You are not in any team!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String teamid = request.getParameter("teamid");
		
		if (teamid==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(teamid)||teamid.length()==0) {
			resp.put("errmsg", "Invalid Team Id!!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long teamidnum = Long.valueOf(teamid);
		
		Teams thisteam = teamServices.getTeamById(teamidnum);
		if (thisteam==null) {
			resp.put("errmsg", "Team with id: "+teamid+" do not exist!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thisteam.getId()!=userobj.getTeamid()) {
			resp.put("errmsg", "You are not leader of this team!!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Date nowtime = new Date();
		Date compstart = CommonUtils.getStartDate(configServices);
		if (compstart!=null && nowtime.after(compstart)) {
			resp.put("errmsg", "The competition has started! Can't do this operation!");
			resp.put("err", "-7");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}

		List<Users> teamusers = userServices.getAllUsersByTeamId(teamidnum);
		for (Users u:teamusers) {
			u.setTeamid(null);
			u.setRole("user");
			userServices.updateUser(u);
		}
		
		teamServices.deleteTeamById(teamidnum);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		resp.put("errmsg", "Team dismissed successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
}
