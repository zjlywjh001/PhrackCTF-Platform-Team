/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.ArrayList;
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
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.utils.CommonUtils;

/**
 * 用户资料显示页面
 *
 * @author Jarvis
 * @date 2016年4月14日
 */
@Controller
public class ProfileController {
	
	private Logger log = LoggerFactory.getLogger(ProfileController.class);
	

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
	
	@RequestMapping(value = "/profile/{id}",method = {RequestMethod.GET})
	public ModelAndView Profile(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("profile");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
		}
		
		Users userobj = userServices.getUserById(id);
		if (userobj==null || userobj.getRole().equals("admin")) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		Countries usercountry = countryServices.getCountryById(userobj.getCountryid());
		mv.addObject("username",userobj.getUsername());
		mv.addObject("countrycode",usercountry.getCountrycode());
		mv.addObject("userdec",userobj.getDescription());
		mv.addObject("country",usercountry.getCountryname());
		mv.addObject("userscore",userobj.getScore());
		mv.addObject("userrank",CommonUtils.getUserrank(userobj,userServices,submissionServices));
		mv.addObject("organize",userobj.getOrganization());
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
		mv.setViewName("profile");
		return mv;
	}
}
