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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import top.phrack.ctf.comparators.CompareScore;
import top.phrack.ctf.comparators.CompareTeamScore;
import top.phrack.ctf.models.dao.ChallengesMapper;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.ChallengeServices;
import top.phrack.ctf.models.services.CountryServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.pojo.Challenges;
import top.phrack.ctf.pojo.Countries;
import top.phrack.ctf.pojo.RanklistObj;
import top.phrack.ctf.pojo.ScoreBoardObj;
import top.phrack.ctf.pojo.ScoreTrend;
import top.phrack.ctf.pojo.SolveList;
import top.phrack.ctf.pojo.Submissions;
import top.phrack.ctf.pojo.TeamRankObj;
import top.phrack.ctf.pojo.Teams;
import top.phrack.ctf.utils.CommonUtils;

/**
 * 排名页面的控制器
 *
 * @author Jarvis
 * @date 2016年4月12日
 */
@Controller
public class ScoreBoardController {
	private Logger log = LoggerFactory.getLogger(ScoreBoardController.class);
	
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
	private TeamServices teamServices;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/personalrank", method = RequestMethod.GET)
	public ModelAndView PersonalRank() throws Exception {
		ModelAndView mv = new ModelAndView("personalrank");
		CommonUtils.setControllerName(request, mv);
		Subject currentUser = SecurityUtils.getSubject();
		Users userobj=CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (userobj==null) {
			mv.addObject("thisuser","");
		} else {
			mv.addObject("thisuser",userobj.getUsername());
		}
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
		}
		
		Date currenttime= new Date();
		mv.addObject("updatetime", currenttime);
		ArrayList<ScoreBoardObj> rank = new ArrayList<ScoreBoardObj>();
		List<Users> userforrank = userServices.getUsersForRank();
		ArrayList<RanklistObj> ranklist = new ArrayList<RanklistObj>();
		List<Challenges> tasklist = challengeServices.getAllAvailChallenges();
		for (Users u:userforrank) {
			RanklistObj aobj = new RanklistObj();
			Submissions last = submissionServices.getLastCorrectSubmitByUserId(u.getId());
			if (last==null) {
				aobj.setLastSummit(new Date());
			} else {
				aobj.setLastSummit(last.getSubmitTime());
			}
			aobj.setuserobj(u);
			ranklist.add(aobj);
		}
		CompareScore c = new CompareScore();
		Collections.sort(ranklist,c);
		
		int count;
		count = 1;
		for (RanklistObj item:ranklist) {
			if (item.getuserobj().getScore()==0) {
				continue;
			}
			ScoreBoardObj sb = new ScoreBoardObj();
			sb.setrank(count++);
			sb.setusername(item.getuserobj().getUsername());
			sb.setuserid(item.getuserobj().getId());
			sb.setscore(item.getuserobj().getScore());
			Countries usercountry = countryServices.getCountryById(item.getuserobj().getCountryid());
			sb.setcountryname(usercountry.getCountryname());
			sb.setcountrycode(usercountry.getCountrycode());
			ArrayList<SolveList> sl = new ArrayList<SolveList>();
			for (Challenges ch:tasklist) {
				SolveList slitem = new SolveList();
				slitem.settaskid(ch.getId());
				slitem.settakstitle(ch.getTitle());
				if (submissionServices.getSolvedByUseridAndTaskId(item.getuserobj().getId(), ch.getId())>0) {
					slitem.setsolvestr("solved");
				} else {
					slitem.setsolvestr("unsolved");
				}
				sl.add(slitem);
			}
			sb.setsolvestat(sl);
			rank.add(sb);
		}
		
		mv.addObject("scorelist", rank);
		mv.setViewName("personalrank");
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/scoreboard", method = RequestMethod.GET)
	public ModelAndView ScoreBoard() throws Exception {
		ModelAndView mv = new ModelAndView("scoreboard");
		CommonUtils.setControllerName(request, mv);
		Subject currentUser = SecurityUtils.getSubject();
		Users userobj=CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (userobj==null || userobj.getTeamid()==null) {
			mv.addObject("thisteam","");
		}
		
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
		}
		
		Date currenttime= new Date();
		mv.addObject("updatetime", currenttime);
		ArrayList<ScoreBoardObj> rank = new ArrayList<ScoreBoardObj>();
		List<Users> userforrank = userServices.getUsersForRank();
		//ArrayList<RanklistObj> ranklist = new ArrayList<RanklistObj>();
		List<Challenges> tasklist = challengeServices.getAllAvailChallenges();
		
		List<Teams> teamlist = teamServices.getAllTeams();
		List<Submissions> subs = submissionServices.getAllCorrectOrderByTime();
		ArrayList<TeamRankObj> teamranklist = new ArrayList<TeamRankObj>();
		Teams thisteam = null;
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
			if (userobj!=null && userobj.getTeamid()!=null 
					&& tm.getId().longValue()==userobj.getTeamid().longValue()) {
				thisteam = tro;
			}
			
		}
		
		if (thisteam!=null) {
			mv.addObject("thisteam",thisteam.getId());
		}
		
		CompareTeamScore c = new CompareTeamScore();
		Collections.sort(teamranklist,c);
//		
//		for (Users u:userforrank) {
//			RanklistObj aobj = new RanklistObj();
//			Submissions last = submissionServices.getLastCorrectSubmitByUserId(u.getId());
//			if (last==null) {
//				aobj.setLastSummit(new Date());
//			} else {
//				aobj.setLastSummit(last.getSubmitTime());
//			}
//			aobj.setuserobj(u);
//			ranklist.add(aobj);
//		}
//		CompareScore c = new CompareScore();
//		Collections.sort(ranklist,c);
		
		int count;
		count = 1;
		List<Countries> allcountry = countryServices.SelectAllCountry();
		List<Submissions> allcorrect = submissionServices.getAllCorrectAndOrderByUserId();
		for (TeamRankObj item:teamranklist) {
			if (item.getScore()==0) {
				continue;
			}
			ScoreBoardObj sb = new ScoreBoardObj();
			sb.setrank(count++);
			sb.setusername(item.getName());
			sb.setuserid(item.getId());
			sb.setscore(item.getScore());
			Countries teamcountry = null;//countryServices.getCountryById(item.getuserobj().getCountryid());
			for (Countries tmpcy:allcountry)
			{
				if (tmpcy.getId()-item.getCountryid()==0) {
					teamcountry = tmpcy;
					break;
				}
			}
			sb.setcountryname(teamcountry.getCountryname());
			sb.setcountrycode(teamcountry.getCountrycode());
			ArrayList<SolveList> sl = new ArrayList<SolveList>();
			for (Challenges ch:tasklist) {
				SolveList slitem = new SolveList();
				slitem.settaskid(ch.getId());
				slitem.settakstitle(ch.getTitle());
				slitem.setsolvestr("unsolved");
				for (Submissions cor:allcorrect) {
					Users userforsb = null;
					for (Users u:userforrank) {
						if (cor.getChallengeId().longValue()==ch.getId().longValue()
								&& cor.getUserid().longValue()==u.getId().longValue()) {
							userforsb = u;
							break;
						}
					}
					if (userforsb!=null && userforsb.getTeamid()!=null && userforsb.getTeamid().longValue()==item.getId().longValue() 
							&& cor.getChallengeId().longValue()==ch.getId().longValue()) {
						slitem.setsolvestr("solved");
						break;
					}
				}
				sl.add(slitem);
			}
			sb.setsolvestat(sl);
			rank.add(sb);
		}
		
		mv.addObject("scorelist", rank);
		mv.setViewName("scoreboard");
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="scoretrend.json",method = {RequestMethod.GET},produces = "application/json;charset=utf-8")
	public String GetJSONscoreboard() throws Exception {
		Map<String,Object> sbobj = new HashMap<String,Object>();
		List<Users> userforrank = userServices.getUsersForRank();
		ArrayList<TeamRankObj> teamranklist = new ArrayList<TeamRankObj>();
		List<Teams> teamlist = teamServices.getAllTeams();
		List<Submissions> subs = submissionServices.getAllCorrectOrderByTime();
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
		
		
		int count;
		count = 1;
		ArrayList<ScoreTrend> jsonsb = new ArrayList<ScoreTrend>();
		List<Submissions> allcorrect = submissionServices.getAllCorrectOrderByTime();
		long updatetime = new Date().getTime();
		sbobj.put("lastupdate", updatetime);
		List<Challenges> allchallenges = challengeServices.getAllChallenges();
		for (TeamRankObj item:teamranklist) {
			if (item.getScore()==0) {
				continue;
			}
			ScoreTrend sbitem = new ScoreTrend();
			sbitem.setusername(StringEscapeUtils.unescapeHtml(item.getName()));
			sbitem.setuserrank(count++);
			
			
			
			ArrayList<Submissions> usercorrect = new ArrayList<Submissions>();
			int sum;
			sum = 0;
			for (int i=0;i<allcorrect.size();i++) {
				Users userinteam = null;
				for (Users u:userforrank) {
					if (u.getTeamid()!=null && u.getTeamid().longValue()==item.getId().longValue() 
							&& allcorrect.get(i).getUserid().longValue()==u.getId().longValue()) {
						userinteam = u;
						break;
					}
				}
				
				if (userinteam!=null) {
					usercorrect.add(allcorrect.get(i));
				}
				
				
			}
			
			Collections.reverse(usercorrect);
			
			long[] scorearr = new long[usercorrect.size()];
			long[] taskarr = new long[usercorrect.size()];
			long[] datelist = new long[usercorrect.size()];
			
			for (int i=0;i<usercorrect.size();i++) {
				taskarr[i] = usercorrect.get(i).getChallengeId();
				//Challenges cls = challengeServices.getChallengeById(taskarr[i]);
				Challenges cls = null;
				for (Challenges cl:allchallenges) {
					if (cl.getId()-taskarr[i]==0) {
						cls = cl;
					}
				}
				sum += cls.getScore();
				scorearr[i] = sum;
				datelist[i]= usercorrect.get(i).getSubmitTime().getTime();
			}
			sbitem.settimepoint(datelist);
			sbitem.settaskarr(taskarr);
			if (usercorrect.size()>0) {
				scorearr[usercorrect.size()-1] = item.getScore();
			}
			sbitem.setscorearr(scorearr);
			
			jsonsb.add(sbitem);
		}
		
		sbobj.put("scorelist", jsonsb);
		String result = JSONObject.fromObject(sbobj).toString();
		
		return result;
		
	}
	
	
}
