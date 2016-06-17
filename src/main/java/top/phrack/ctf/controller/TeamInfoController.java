/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.CategoryServices;
import top.phrack.ctf.models.services.ChallengeServices;
import top.phrack.ctf.models.services.CountryServices;
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
 * 队伍公共信息页面
 * 
 *
 * @author Jarvis
 * @date 2016年5月20日
 */
@Controller
public class TeamInfoController {
	private Logger log = LoggerFactory.getLogger(TeamInfoController.class);
	
	
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
	private TeamServices teamServices;
	
	
	
	@RequestMapping(value = "/teaminfo/{id}", method = {RequestMethod.GET})
	public ModelAndView TeamInfo(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("teaminfo");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
		}
		
		
		Teams thisteam = teamServices.getTeamById(id);
		if (thisteam==null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
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
		mv.setViewName("teaminfo");
		return mv;
	}
	
	
}
