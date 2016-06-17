/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

import top.phrack.ctf.comparators.CompareScore;
import top.phrack.ctf.comparators.CompareTeamScore;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.CategoryServices;
import top.phrack.ctf.models.services.ChallengeServices;
import top.phrack.ctf.models.services.CountryServices;
import top.phrack.ctf.models.services.FileServices;
import top.phrack.ctf.models.services.HintServices;
import top.phrack.ctf.models.services.IPlogServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Challenges;
import top.phrack.ctf.pojo.Countries;
import top.phrack.ctf.pojo.CountryRank;
import top.phrack.ctf.pojo.RanklistObj;
import top.phrack.ctf.pojo.ScoreBoardObj;
import top.phrack.ctf.pojo.Submissions;
import top.phrack.ctf.pojo.TeamRankObj;
import top.phrack.ctf.pojo.Teams;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.utils.CommonUtils;

/**
 * 
 * 国家页面的控制器
 *
 * @author Jarvis
 * @date 2016年4月14日
 */
@Controller
public class CountryController {
	
	private Logger log = LoggerFactory.getLogger(ChallengeController.class);
	
	@Resource 
	private UserServices userServices;
	@Autowired
	private HttpServletRequest request;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private SubmissionServices submissionServices;
	@Resource
	private CountryServices countryServices;
	@Resource
	private TeamServices teamServices;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/country/{code}",method = {RequestMethod.GET})
	public ModelAndView CountryView(@PathVariable String code) throws Exception {
		ModelAndView mv = new ModelAndView("country");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices,teamServices, submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
		}
		
		Countries countryobj = countryServices.getCountryByCode(code);
		if (countryobj==null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		mv.addObject("countryname",countryobj.getCountryname());
		mv.addObject("countrycode", countryobj.getCountrycode());
		List<Users> userforrank = userServices.getUsersForRank();
		List<Teams> teamlist = teamServices.getAllTeams();
		List<Submissions> subs = submissionServices.getAllCorrectOrderByTime();
		ArrayList<TeamRankObj> teamranklist = new ArrayList<TeamRankObj>();
		for (Teams tm:teamlist) {
			if (!tm.getIsenabled()) {
				continue;
			}
			TeamRankObj tro = new TeamRankObj(tm);
			for (Submissions sb:subs) {
				Users subuserobj = null;
				for (Users u:userforrank) {
					if (u.getId().longValue()==sb.getUserid().longValue()) {
						subuserobj = u;
						break;
					}
				}
				if (subuserobj!=null && subuserobj.getTeamid()!=null && subuserobj.getTeamid().longValue()==tm.getId().longValue()) {
					tro.setLastSummit(sb.getSubmitTime());
					break;
				}
			}
			if (tro.getLastSummit()==null) {
				tro.setLastSummit(new Date());
			}
			teamranklist.add(tro);
			
		}
		
		CompareTeamScore c = new CompareTeamScore();
		Collections.sort(teamranklist,c);
		
		
		ArrayList<CountryRank> crl = new ArrayList<CountryRank>();
		
		int rank;
		rank = 0;
		for (TeamRankObj ro:teamranklist) {
			rank++;
			CountryRank cr = new CountryRank();
			if (ro.getCountryid().longValue()==countryobj.getId().longValue() && ro.getScore()!=0) {
				cr.setid(ro.getId());
				cr.setname(ro.getName());
				cr.setrank(rank);
				cr.setscore(ro.getScore());
				crl.add(cr);
			}
		}
		
		mv.addObject("country_users", crl);

		mv.setViewName("country");
		return mv;
	}
}
