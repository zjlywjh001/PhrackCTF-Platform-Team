/**
 * 
 */
package top.phrack.ctf.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;
import top.phrack.ctf.comparators.CompareDate;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.CategoryServices;
import top.phrack.ctf.models.services.ChallengeServices;
import top.phrack.ctf.models.services.ConfigServices;
import top.phrack.ctf.models.services.CountryServices;
import top.phrack.ctf.models.services.FileServices;
import top.phrack.ctf.models.services.HintServices;
import top.phrack.ctf.models.services.IPlogServices;
import top.phrack.ctf.models.services.NewsServices;
import top.phrack.ctf.models.services.OperateLogServices;
import top.phrack.ctf.models.services.RuleServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.BannedIps;
import top.phrack.ctf.pojo.CateProcess;
import top.phrack.ctf.pojo.Categories;
import top.phrack.ctf.pojo.Challenges;
import top.phrack.ctf.pojo.Config;
import top.phrack.ctf.pojo.Countries;
import top.phrack.ctf.pojo.Files;
import top.phrack.ctf.pojo.HintDisp;
import top.phrack.ctf.pojo.Hints;
import top.phrack.ctf.pojo.IpLogs;
import top.phrack.ctf.pojo.IpLogsList;
import top.phrack.ctf.pojo.News;
import top.phrack.ctf.pojo.OpLogDisp;
import top.phrack.ctf.pojo.Operatelog;
import top.phrack.ctf.pojo.Rules;
import top.phrack.ctf.pojo.SolveTable;
import top.phrack.ctf.pojo.SubmissionList;
import top.phrack.ctf.pojo.Submissions;
import top.phrack.ctf.pojo.TeamList;
import top.phrack.ctf.pojo.TeamMember;
import top.phrack.ctf.pojo.Teams;
import top.phrack.ctf.pojo.UserList;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.pojo.ChallengeInManager;
import top.phrack.ctf.utils.CommonUtils;
import top.phrack.ctf.utils.LogUtils;

/**
 * 后台主页
 *
 * @author Jarvis
 * @date 2016年4月15日
 */
@Controller
public class ManageController {
	private Logger log = LoggerFactory.getLogger(ManageController.class);
	
	
	@Autowired
	private HttpServletRequest request;
	@Resource 
	private UserServices userServices;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private SubmissionServices submissionServices;
	@Resource
	private NewsServices newsServices;
	@Resource 
	private IPlogServices ipLogServices;
	@Resource
	private RuleServices ruleServices;
	@Resource
	private CategoryServices categoryServices;
	@Resource 
	private OperateLogServices operateLogServices;
	@Resource
	private ChallengeServices challengeServices;
	@Resource
	private FileServices fileServices;
	@Resource
	private HintServices hintServices;
	@Resource
	private CountryServices countryServices;
	@Resource
	private TeamServices teamServices;
	@Resource
	private ConfigServices configServices;
	
	/**
	 * 控制面板主页
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/admin/manage",method={RequestMethod.GET})
	public ModelAndView Manage() throws Exception {
		ModelAndView mv = new ModelAndView("admin/manage");
		
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		/*显示News列表*/
		List<News> newslist = newsServices.selectAll();
		if (newslist!=null) {
			CompareDate c = new CompareDate();
			Collections.sort(newslist,c);
			mv.addObject("newslist", newslist);
		}

		/*显示rule内容*/
		Rules rule = ruleServices.getRulesById(Long.valueOf(1));
		mv.addObject("rulecontent", rule.getContent());
		
		/*显示Category列表*/
		List<Categories> cates = categoryServices.selectAllCategory();
		if (cates!=null) {
			mv.addObject("cates", cates);
		}
		
		/*显示Challenge列表*/
		List<Challenges> challs = challengeServices.getAllChallenges();
		List<Categories> allcate = cates;
		ArrayList<ChallengeInManager> challfordisp = new ArrayList<ChallengeInManager>();	
		if (challs!=null) {
			for (Challenges ch:challs) {
				ChallengeInManager cim = new ChallengeInManager();
				cim.setId(ch.getId());
				cim.setTitle(ch.getTitle());
				cim.setScore(ch.getScore());
				cim.setAvailable(ch.getAvailable());
				cim.setInvalidate(ch.getInvalidate());
				Categories cc = null;
				for (Categories ct:allcate) {
					if (ch.getCategoryid()-ct.getId()==0) {
						cc = ct;
						break;
					}
				}
				//categoryServices.selectById(ch.getCategoryid());
				cim.setcatename(cc.getName());
				cim.setcatemark(cc.getMark());
				challfordisp.add(cim);
			}
			mv.addObject("challengelist", challfordisp);
		}
		
		/*显示hint列表*/
		List<Hints> allhint = hintServices.getAllHints();
		ArrayList<HintDisp> hintdisps = new ArrayList<HintDisp>();
		if (allhint!=null) {
			for (Hints hi:allhint) {
				HintDisp hd = new HintDisp();
				hd.setid(hi.getId());
				hd.setcontent(hi.getContent());
				Challenges c = null;//challengeServices.getChallengeById(hi.getChallengeid());
				for (Challenges cls:challs) {
					if (hi.getChallengeid()-cls.getId()==0) {
						c = cls;
						break;
					}
				}
				hd.setchallname(c.getTitle());
				hd.setadddate(hi.getAddtime());
				hintdisps.add(hd);
			}
		}
		
		mv.addObject("hintsdisp",hintdisps);
		
		/*显示submission提交列表，后台主页限制显示50个*/
		List<Submissions> allsubs = submissionServices.getAllSubmissions();
		ArrayList<SubmissionList> sbl = new ArrayList<SubmissionList>();
		List<Users> alluser = userServices.getAllUsers();
		List<Teams> allteam = teamServices.getAllTeams();
		if (allsubs!=null) {
			long i=0;
			for (Submissions s:allsubs) {
				if (i>=50) {
					break;
				}
				SubmissionList sl = new SubmissionList();
				sl.setId(s.getId());
				for (Challenges cls:challs) {
					if (s.getChallengeId()-cls.getId()==0) {
						sl.settaskname(cls.getTitle());
						break;
					}
				}
				//sl.settaskname(challengeServices.getChallengeById(s.getChallengeId()).getTitle());
				Users subuserobj = null;
				for (Users u:alluser) {
					if (s.getUserid()-u.getId()==0) {
						sl.setuser(u.getUsername());
						subuserobj  = u;
						break;
					}
				}
				for (Teams tm:allteam) {
					if (subuserobj!=null && subuserobj.getTeamid()!=null 
							&& subuserobj.getTeamid().longValue()==tm.getId().longValue()) {
						sl.setteamname(tm.getName());
						break;
					}
				}
				//sl.setuser(userServices.getUserById(s.getUserid()).getUsername());
				sl.setSubmitTime(s.getSubmitTime());
				sl.setContent(s.getContent());
				sl.setCorrect(s.getCorrect());
				sbl.add(sl);
				i++;
			}
		}
		
		mv.addObject("submits", sbl);
		
		/*显示用户User列表，限制显示50个*/
		//List<Users> alluser = userServices.getAllUsers();
		List<Countries> allcountries = countryServices.SelectAllCountry();
		ArrayList<UserList> ulist = new ArrayList<UserList>();
		List<IpLogs> alllogs = ipLogServices.getAllOrderByUserId();
		if (alluser!=null) {
			long i=0;
			for (Users u:alluser) {
				if (i>=50) {
					break;
				}
				UserList user = new UserList();
				Countries usercon = null;
				for (Countries country:allcountries) {
					if (u.getCountryid()-country.getId()==0) {
						usercon = country;
						break;
					}
				}
				//Countries usercon = countryServices.getCountryById(u.getCountryid());
				user.setcountrycode(usercon.getCountrycode());
				user.setCountryid(u.getCountryid());
				user.setcountryname(usercon.getCountryname());
				user.setDescription(u.getDescription());
				user.setEmail(u.getEmail());
				user.setId(u.getId());
				
				//user.setips(ipLogServices.countByUserId(u.getId()));
				int index1 = -1;
				int index2 = -1;
				for (int j=0;j<alllogs.size();j++) {
					if (alllogs.get(j).getUserid()-u.getId()==0) {
						index1 = j;
						break;
					}
				}
				if (index1!=-1) {
					for (int j=index1;j<alllogs.size();j++) {
						if (alllogs.get(j).getUserid()-u.getId()!=0) {
							index2 = j;
							break;
						}
					}
					if (index2==-1) {
						index2 = alllogs.size();
					}
					user.setips(index2-index1);
				} else {
					user.setips(0);
				}
				user.setIsenabled(u.getIsenabled());
				user.setLastactive(u.getLastactive());
				user.setOrganization(u.getOrganization());
				user.setRegtime(u.getRegtime());
				user.setRole(u.getRole());
				user.setUsername(u.getUsername());
				ulist.add(user);
				i++;
			}
		}
		
		mv.addObject("userlist",ulist);
		
		/**
		 * 显示团队列表
		 */
		List<Submissions> correctsubs = submissionServices.getAllCorrectOrderByTime();
		ArrayList<TeamList> tlist = new ArrayList<TeamList>();
		if (allteam!=null) {
			long i=0;
			for (Teams t:allteam) {
				if (i>=50) {
					break;
				}
				TeamList ateam = new TeamList(t);
				Countries teamcon = null;
				for (Countries country:allcountries) {
					if (t.getCountryid()-country.getId()==0) {
						teamcon = country;
						break;
					}
				}
				//Countries usercon = countryServices.getCountryById(u.getCountryid());
				ateam.setcountrycode(teamcon.getCountrycode());
				ateam.setcountryname(teamcon.getCountryname());
				
				//user.setips(ipLogServices.countByUserId(u.getId()));
				List<Users> teammembers = userServices.getAllUsersByTeamId(t.getId());
				Map<String,Long> iptable = new HashMap<String,Long>();
				long teamips = 0;
				for (Users member:teammembers) {
					if (member.getRole().equals("leader")){
						ateam.setcreator(member.getUsername());
					}
					int index1 = -1;
					int index2 = -1;
					for (int j=0;j<alllogs.size();j++) {
						if (alllogs.get(j).getUserid()-member.getId()==0) {
							index1 = j;
							break;
						}
					}
					if (index1!=-1) {
						for (int j=index1;j<alllogs.size();j++) {
							if (alllogs.get(j).getUserid()-member.getId()!=0) {
								index2 = j;
								break;
							}
						}
						if (index2==-1) {
							index2 = alllogs.size();
						}
						
						for (int k=index1;k<index2;k++) {
							if (!iptable.containsKey(alllogs.get(k).getIpaddr())) {
								iptable.put((String)alllogs.get(k).getIpaddr(), Long.valueOf(1));
							} else {
								long oldips = iptable.get(alllogs.get(k).getIpaddr()).longValue();
								iptable.put((String)alllogs.get(k).getIpaddr(), Long.valueOf(oldips+1));
							}
						}
						teamips = iptable.size();
						//teamips = teamips + index2-index1;
					} else {
						teamips = teamips + 0;
					}
				} 
				
				ateam.setips(teamips);
				
				for (Submissions sb:correctsubs) {
					Users subuserobj = null;
					for (Users u:alluser) {
						if (u.getId().longValue()==sb.getUserid().longValue()) {
							subuserobj = u;
							break;
						}
					}
					if (subuserobj!=null && subuserobj.getTeamid()!=null && subuserobj.getTeamid().longValue()==t.getId().longValue()) {
						ateam.setLastSubmit(sb.getSubmitTime());
						break;
					}
				}
				
				tlist.add(ateam);
				i++;
			}
		}
		
		mv.addObject("teamlist",tlist);
		
		/**
		 * 系统设置信息
		 */
		mv.addObject("max_teammembers",CommonUtils.getMaxTeamMembers(configServices));
		mv.addObject("compstart",CommonUtils.getStartDate(configServices));
		mv.addObject("compend",CommonUtils.getEndDate(configServices));
		
		/**
		 * IP地址黑名单
		 * 
		 */
		
		List<BannedIps> allbannedips = bannedIpServices.getAllIps();
		mv.addObject("blackips",allbannedips);
		
		mv.setViewName("admin/manage");
		return mv;
		
	}
	
	/**
	 * 添加新闻的控制器
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/addnews",method={RequestMethod.GET})
	public ModelAndView AddNews() throws Exception {
		ModelAndView mv = new ModelAndView("admin/addnews");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		mv.setViewName("admin/addnews");
		return mv;
		
	}
	
	
	/**
	 * 添加新闻api
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/savenews.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String doPostNews() throws Exception {
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
		
		String newstitle = request.getParameter("title");
		String newscontent = request.getParameter("content");
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (newstitle==null || newscontent==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (newstitle.length()==0) {
			resp.put("errmsg", "Please input Title !!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		newstitle = CommonUtils.XSSFilter(newstitle);
		newscontent = CommonUtils.filterUserInputContent(newscontent);
		
		newsServices.createNews(newstitle, newscontent, new Date());
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Add news "+newstitle);
		resp.put("errmsg", "News added successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	/**
	 * 删除新闻
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/delnews",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String doDelNews() throws Exception {
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
		
		String id = request.getParameter("id");
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
			
			
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (id==null || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long newsid = Long.valueOf(id);
		News thisnews = newsServices.getNewsById(newsid);
		if (thisnews == null) {
			resp.put("errmsg", "News of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		newsServices.deleteNewsById(newsid);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Delete news title:"+thisnews.getTitle());
		resp.put("errmsg", "News deleted successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 编辑新闻页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/admin/editnews/{id}",method={RequestMethod.GET})
	public ModelAndView EditNews(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/editnews");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		News thisnews = newsServices.getNewsById(id);
		if (thisnews == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		
		mv.addObject("news",thisnews);
		mv.setViewName("admin/editnews");
		return mv;
	}
	
	
	/**
	 * 修改新闻的api
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/editnews/{id}",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String PostEditNews(@PathVariable long id) throws Exception {
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
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		
		
		}
		
		News thisnews = newsServices.getNewsById(id);
		String newtitle = request.getParameter("title");
		String newcontent = request.getParameter("content");
		
		if (thisnews == null) {
			resp.put("errmsg", "News of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (newtitle==null || newcontent==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (newtitle.length()==0) {
			resp.put("errmsg", "Title can't be empty !!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		newtitle = CommonUtils.XSSFilter(newtitle);
		newcontent = CommonUtils.filterUserInputContent(newcontent);
		
		thisnews.setTitle(newtitle);
		thisnews.setContent(newcontent);

		newsServices.updateNews(thisnews);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Modify news id:"+id);
		resp.put("errmsg", "News Updated successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
	}
	
	/**
	 * 保存rule的内容
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/saverule",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String SaveRule() throws Exception {
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
		
		String newrule = request.getParameter("content");
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (newrule==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Rules ruleobject = ruleServices.getRulesById(1);
		
		newrule = CommonUtils.filterUserInputContent(newrule);
		ruleobject.setContent(newrule);
		
		ruleServices.updateRule(ruleobject);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Modify Rule.");
		resp.put("errmsg", "Content Saved !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 添加赛题大类
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/addcate.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String doPostAddCate() throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String catename = request.getParameter("name");
		String catemark = request.getParameter("markid");
		
		if (catename==null || catemark==null || !StringUtils.isNumeric(catemark) || (Long.valueOf(catemark)<1) || (Long.valueOf(catemark)>6)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (catename.length()==0) {
			resp.put("errmsg", "Name can't be empty !!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		int markid = Integer.valueOf(catemark);
		Categories acate = new Categories();
		catename = CommonUtils.XSSFilter(catename);
		acate.setName(catename);
		switch (markid) {
		case 1:
			acate.setMark("default");
			break;
		case 2:
			acate.setMark("primary");
			break;
		case 3:
			acate.setMark("success");
			break;
		case 4:
			acate.setMark("info");
			break;
		case 5:
			acate.setMark("warning");
			break;
		case 6:
			acate.setMark("danger");
			break;
		default:assert(false);
		}
		
		categoryServices.insertNewCategory(acate);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Add category： "+catename);
		resp.put("errmsg", "Category added successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 编辑赛题大类
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/editcate.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String doPostEditCate() throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		String id = request.getParameter("id");
		String catename = request.getParameter("name");
		String catemark = request.getParameter("markid");
		
		if (catename==null || catemark==null || !StringUtils.isNumeric(catemark) 
				|| (Long.valueOf(catemark)<1) || (Long.valueOf(catemark)>6 || !StringUtils.isNumeric(id))) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (catename.length()==0) {
			resp.put("errmsg", "Name can't be empty !!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		long cateid = Long.valueOf(id);
		Categories acate = categoryServices.selectById(cateid);
		
		if (acate == null) {
			resp.put("errmsg", "Category of id:"+id+" do not exist!!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		int markid = Integer.valueOf(catemark);
		catename = CommonUtils.XSSFilter(catename);
		acate.setName(catename);
		switch (markid) {
		case 1:
			acate.setMark("default");
			break;
		case 2:
			acate.setMark("primary");
			break;
		case 3:
			acate.setMark("success");
			break;
		case 4:
			acate.setMark("info");
			break;
		case 5:
			acate.setMark("warning");
			break;
		case 6:
			acate.setMark("danger");
			break;
		default:assert(false);
		}
		
		categoryServices.updateCategory(acate);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Modify Category id:"+cateid);
		resp.put("errmsg", "Category Updated successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	
	/**
	 * 删除赛题分类
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/delcate",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String doDelCate() throws Exception {
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
		
		String id = request.getParameter("id");
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (id==null || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long cateid = Long.valueOf(id);
		Categories thiscate = categoryServices.selectById(cateid);
		if (thiscate == null) {
			resp.put("errmsg", "Category of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		List<Challenges> challs = challengeServices.getAllChallengesInCateById(cateid);
		for (Challenges ch:challs) {
			List<Submissions> correct = submissionServices.getAllCorrectSubmitByTaskId(ch.getId());
			for (Submissions csub:correct) {
				Users solveuser = userServices.getUserById(csub.getUserid());
				long oldscore = solveuser.getScore();
				long newscore = oldscore - ch.getScore();
				solveuser.setScore(newscore);
				userServices.updateUser(solveuser);
			}
		}
		
		categoryServices.deleteCategoryById(cateid);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Delete Category name:"+thiscate.getName());
		resp.put("errmsg", "Category deleted successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 添加赛题页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/addprob",method={RequestMethod.GET})
	public ModelAndView AddChallengePage() throws Exception {
		ModelAndView mv = new ModelAndView("admin/addprob");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		/*显示Category列表*/
		List<Categories> cates = categoryServices.selectAllCategory();
		if (cates!=null) {
			mv.addObject("allcates", cates);
		}
		
		mv.setViewName("admin/addprob");
		return mv;
		
	}
	
	
	/**
	 * 添加赛题api
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/addchallenge.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String AddChallenge() throws Exception {
		System.out.println("begin to handle challenge");
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
	
		String title = request.getParameter("title");
		String score = request.getParameter("score");
		String categoryid = request.getParameter("category");
		String description = request.getParameter("description");
		String attaches = request.getParameter("attaches");
		String flag = request.getParameter("flag");
		String isexposed = request.getParameter("isexposed");
		String available = request.getParameter("available");
		String invalidate = request.getParameter("invalidate");
		
		if (description==null){
			description = new String("");
		}
		
		if (title==null||title.length()==0){
			resp.put("errmsg", "Title can't be empty!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (score==null||!StringUtils.isNumeric(score)) {
			resp.put("errmsg", "Invalid Score !!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (categoryid==null||!StringUtils.isNumeric(categoryid)) {
			resp.put("errmsg", "Invalid Category!!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (flag==null || flag.length()==0) {
			resp.put("errmsg", "Flag can't be empty!!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (isexposed==null||(!isexposed.equalsIgnoreCase("true")&&!isexposed.equalsIgnoreCase("false"))) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (available==null || !CommonUtils.isValidDate(available)) {
			resp.put("errmsg", "Invalid Date!!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (invalidate==null || !CommonUtils.isValidDate(invalidate)) {
			resp.put("errmsg", "Invalid Date!!");
			resp.put("err", "-7");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (attaches==null) {
			attaches = new String("");
		}
		
		String[] attachlist = attaches.split("\\|");
		
		long[] attachids = new long[attachlist.length];
		for (int i=0;i<attachlist.length;i++){
			if (attachlist[i].length()==0) {
				attachids[i] = 0;
				continue;
			}
			if (!StringUtils.isNumeric(attachlist[i])|| fileServices.getFileById(Long.valueOf(attachlist[i]))==null) {
				resp.put("errmsg", "Invalid Attachments!!");
				resp.put("err", "-8");
				String result = JSONObject.fromObject(resp).toString();
				return result;
			}
			attachids[i] = Long.valueOf(attachlist[i]);
		}
		title = CommonUtils.XSSFilter(title);
		Long numscore = Long.valueOf(score);
		Long numcategoryid = Long.valueOf(categoryid);
		description = CommonUtils.filterUserInputContent(description);
		String flag_store = new SimpleHash("SHA-256",flag,CommonUtils.getFlagSalt()).toHex();
		Boolean exposed = (isexposed.equalsIgnoreCase("true"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date availfrom = null;
		Date availuntil = null;
		try {
			format.setLenient(false);
			availfrom = format.parse(available);
			availuntil = format.parse(invalidate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Challenges achall = new Challenges();
		achall.setTitle(title);
		achall.setScore(numscore);
		achall.setCategoryid(numcategoryid);
		achall.setDescription(description);
		achall.setExposed(exposed);
		achall.setFlag(flag_store);
		achall.setAvailable(availfrom);
		achall.setInvalidate(availuntil);
		challengeServices.createNewChallenge(achall);
		for (int i=0;i<attachids.length;i++){
			Files afile = fileServices.getFileById(attachids[i]);
			if (afile!=null) {
				afile.setChallengeid(achall.getId());
				fileServices.updateFileInfo(afile);
			}
		}

		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Add a new Challenge id:"+achall.getId());
		resp.put("errmsg", "Challenge added successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
	}
	
	
	/**
	 * 编辑赛题页面控制器
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/editprob/{id}",method={RequestMethod.GET})
	public ModelAndView EditChallenge(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/editprob");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		Challenges thischallenge = challengeServices.getChallengeById(id);
		if (thischallenge == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		
		mv.addObject("thischall",thischallenge);
		
		/*显示Category列表*/
		List<Categories> cates = categoryServices.selectAllCategory();
		if (cates!=null) {
			mv.addObject("allcates", cates);
		}
		
		/*这道题的所有文件*/
		List<Files> attaches = fileServices.getFilesByChallengeId(id);
		if (attaches!=null) {
			mv.addObject("attachlist", attaches);
		}
		
		
		
		mv.setViewName("admin/editprob");
		return mv;
	}
	
	/**
	 * 编辑赛题api
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/editprob/{id}",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String doPostEditChallenge(@PathVariable long id) throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Challenges thischallenge = challengeServices.getChallengeById(id);
		if (thischallenge == null) {
			resp.put("errmsg", "This challenges no longer exist!");
			resp.put("err", "-97");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
	
		String title = request.getParameter("title");
		String score = request.getParameter("score");
		String categoryid = request.getParameter("category");
		String description = request.getParameter("description");
		String attaches = request.getParameter("attaches");
		String flag = request.getParameter("flag");
		String isexposed = request.getParameter("isexposed");
		String available = request.getParameter("available");
		String invalidate = request.getParameter("invalidate");

		if (description==null){
			description = new String("");
		}
		
		if (title==null||title.length()==0){
			resp.put("errmsg", "Title can't be empty!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (score==null||!StringUtils.isNumeric(score)) {
			resp.put("errmsg", "Invalid Score !!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (categoryid==null||!StringUtils.isNumeric(categoryid)) {
			resp.put("errmsg", "Invalid Category!!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (flag==null) {
			flag = new String("");
		}
		
		if (isexposed==null||(!isexposed.equalsIgnoreCase("true")&&!isexposed.equalsIgnoreCase("false"))) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (available==null || !CommonUtils.isValidDate(available)) {
			resp.put("errmsg", "Invalid Date!!");
			resp.put("err", "-6");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (invalidate==null || !CommonUtils.isValidDate(invalidate)) {
			resp.put("errmsg", "Invalid Date!!");
			resp.put("err", "-7");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (attaches==null) {
			attaches = new String("");
		}
		
		String[] attachlist = attaches.split("\\|");
		
		long[] attachids = new long[attachlist.length];
		for (int i=0;i<attachlist.length;i++){
			if (attachlist[i].length()==0) {
				attachids[i] = 0;
				continue;
			}
			if (!StringUtils.isNumeric(attachlist[i])|| fileServices.getFileById(Long.valueOf(attachlist[i]))==null) {
				resp.put("errmsg", "Invalid Attachments!!");
				resp.put("err", "-8");
				String result = JSONObject.fromObject(resp).toString();
				return result;
			}
			attachids[i] = Long.valueOf(attachlist[i]);
		}
		title = CommonUtils.XSSFilter(title);
		Long numscore = Long.valueOf(score);
		Long numcategoryid = Long.valueOf(categoryid);
		description = CommonUtils.filterUserInputContent(description);
		if (flag.length()!=0) {
			String flag_store = new SimpleHash("SHA-256",flag,CommonUtils.getFlagSalt()).toHex();
			thischallenge.setFlag(flag_store);
		}
		Boolean exposed = (isexposed.equalsIgnoreCase("true"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date availfrom = null;
		Date availuntil = null;
		try {
			format.setLenient(false);
			availfrom = format.parse(available);
			availuntil = format.parse(invalidate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (numscore!=thischallenge.getScore()) {
			List<Submissions> correct = submissionServices.getAllCorrectSubmitByTaskId(id);
			for (Submissions csub:correct) {
				Users solveuser = userServices.getUserById(csub.getUserid());
				long oldscore = solveuser.getScore();
				long newscore = oldscore + (numscore-thischallenge.getScore());
				solveuser.setScore(newscore);
				userServices.updateUser(solveuser);
			}
		}
		thischallenge.setTitle(title);
		thischallenge.setScore(numscore);
		thischallenge.setCategoryid(numcategoryid);
		thischallenge.setDescription(description);
		thischallenge.setExposed(exposed);
		thischallenge.setAvailable(availfrom);
		thischallenge.setInvalidate(availuntil);
		challengeServices.updateChallenge(thischallenge);
		List<Files> file_list_old = fileServices.getFilesByChallengeId(id);
		long[] oldlist = new long[file_list_old.size()];
		int j=0;
		for (Files f:file_list_old) {
			if (!ArrayUtils.contains(attachids, f.getId())) {
				fileServices.deleteFileRecordById(f.getId());
			}
			oldlist[j++] = f.getId();
			
		}
		
		for (int i=0;i<attachids.length;i++) {
			if (!ArrayUtils.contains(oldlist, attachids[i])&&attachids[i]!=0) {
				Files afile = fileServices.getFileById(attachids[i]);
				if (afile!=null) {
					afile.setChallengeid(id);
					fileServices.updateFileInfo(afile);
				}
			}
		}

		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Update Challenge id:"+thischallenge.getId());
		resp.put("errmsg", "Challenge updated successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
	}
	
	
	/**
	 * 删除赛题
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/deltask",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String DelChallenge() throws Exception {
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
		
		String id = request.getParameter("id");
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (id==null || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long challid = Long.valueOf(id);
		Challenges thischall = challengeServices.getChallengeById(challid);
		if (thischall == null) {
			resp.put("errmsg", "Challenge of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		List<Submissions> correct = submissionServices.getAllCorrectSubmitByTaskId(challid);
		for (Submissions csub:correct) {
			Users solveuser = userServices.getUserById(csub.getUserid());
			long oldscore = solveuser.getScore();
			long newscore = oldscore - thischall.getScore();
			solveuser.setScore(newscore);
			userServices.updateUser(solveuser);
		}
		
		challengeServices.deleteChallengeById(challid);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Deleted Challenge name:"+thischall.getTitle());
		resp.put("errmsg", "Challenge deleted successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	
	/**
	 * 添加提示的控制器
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/addhint",method={RequestMethod.GET})
	public ModelAndView AddHint() throws Exception {
		ModelAndView mv = new ModelAndView("admin/addhint");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		List<Challenges> challs = challengeServices.getAllChallenges();
		mv.addObject("allchalls", challs);
		mv.setViewName("admin/addhint");
		return mv;
		
	}
	
	/**
	 * 添加hints的api
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/newhints.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String DoPostAddHints() throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String challid = request.getParameter("challengeid");
		String hintcontent = request.getParameter("hint");
		
		if (challid==null || hintcontent==null || challid.length()==0 || !StringUtils.isNumeric(challid)
			|| challengeServices.getChallengeById(Long.valueOf(challid))==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (hintcontent.length()==0) {
			resp.put("errmsg", "Hint content can't be empty !!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		
		
		hintcontent = CommonUtils.XSSFilter(hintcontent);
		
		Hints ahint = new Hints();
		ahint.setChallengeid(Long.valueOf(challid));
		ahint.setContent(hintcontent);
		ahint.setAddtime(new Date());
		
		hintServices.createNewHint(ahint);
		
		Challenges challengeobj = challengeServices.getChallengeById(Long.valueOf(challid));
		String newstitle = "New hint for "+challengeobj.getTitle();
		String newscontent = hintcontent;
		Date newsposttime = new Date();
		newsServices.createNews(newstitle, newscontent, newsposttime);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Add Hint for Challenge id:"+challid+". Hint Content:"+hintcontent);
		resp.put("errmsg", "Hint added successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	/**
	 * 修改提示的控制器
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/edithint/{id}",method={RequestMethod.GET})
	public ModelAndView EidtHint(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/edithint");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		Hints thishint = hintServices.getHintById(id);
		if (thishint == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		
		
		mv.addObject("thishint", thishint);
		
		List<Challenges> challs = challengeServices.getAllChallenges();
		mv.addObject("allchalls", challs);
		mv.setViewName("admin/edithint");
		return mv;
		
	}
	
	/**
	 * 修改提示的api
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/edithint/{id}",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String doPostEditHint(@PathVariable long id) throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		
		Hints thishint = hintServices.getHintById(id);
		if (thishint == null) {
			resp.put("errmsg", "This hint is not exist!!");
			resp.put("err", "-97");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		
		String challid = request.getParameter("challengeid");
		String hintcontent = request.getParameter("hint");
		
		if (challid==null || hintcontent==null || challid.length()==0 || !StringUtils.isNumeric(challid)
			|| challengeServices.getChallengeById(Long.valueOf(challid))==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (hintcontent.length()==0) {
			resp.put("errmsg", "Hint content can't be empty !!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		hintcontent = CommonUtils.XSSFilter(hintcontent);
		thishint.setChallengeid(Long.valueOf(challid));
		thishint.setContent(hintcontent);
		
		
		
		hintServices.updateHint(thishint);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Update Hints for Challenge id:"+challid+". Hint Content:"+hintcontent);
		resp.put("errmsg", "Hint Updated successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 删除提示
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/delhint",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String DelHint() throws Exception {
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
		
		String id = request.getParameter("id");
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (id==null || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long hintid = Long.valueOf(id);
		Hints thishint = hintServices.getHintById(hintid);
		if (thishint == null) {
			resp.put("errmsg", "Hint of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		hintServices.deleteById(hintid);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Deleted Hint content:"+thishint.getContent());
		resp.put("errmsg", "Hint deleted successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 修改提交状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/editsubmit",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String ModifySubmissionStatus() throws Exception {
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
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		String id = request.getParameter("id");
		String targetstatus = request.getParameter("newstat");
		
		
		if (id==null || !StringUtils.isNumeric(id) ||(!targetstatus.equals("0") && !targetstatus.equals("1"))) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long submitid = Long.valueOf(id);
		Submissions thissubmit = submissionServices.getSubmissionById(submitid);
		if (thissubmit == null) {
			resp.put("errmsg", "Submission of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		boolean state = targetstatus.equals("1");
		if (state) {
			Submissions correct = null;//submissionServices.getCorrectByUserIdAndTaskId(thissubmit.getUserid(), thissubmit.getChallengeId());
			Users uobject = userServices.getUserById(thissubmit.getUserid());
			if (uobject.getTeamid()!=null) {
				List<Users> teammembers = userServices.getAllUsersByTeamId(uobject.getTeamid());
				for (Users member:teammembers) {
					correct = submissionServices.getCorrectByUserIdAndTaskId(member.getId(), thissubmit.getChallengeId());
					if (correct!=null) {
						break;
					}
				}
			} else {
				correct = submissionServices.getCorrectByUserIdAndTaskId(thissubmit.getUserid(), thissubmit.getChallengeId());
			}
			if (correct!=null) {
				if (uobject.getTeamid()!=null) {
					resp.put("errmsg", "This team already have a correct submission of this challenge!!");
				} else {
					resp.put("errmsg", "This user already have a correct submission of this challenge!!");
				}
				resp.put("err", "-3");
				String result = JSONObject.fromObject(resp).toString();
				return result;
			} else {
				thissubmit.setCorrect(true);
				long addscore = challengeServices.getChallengeById(thissubmit.getChallengeId()).getScore();
				Users thisuser = userServices.getUserById(thissubmit.getUserid());
				long oldscore = thisuser.getScore();
				thisuser.setScore(oldscore+addscore);
				userServices.updateUser(thisuser);
				if (thisuser.getTeamid()!=null) {
					Teams userteam = teamServices.getTeamById(thisuser.getTeamid());
					oldscore = userteam.getScore().longValue();
					userteam.setScore(oldscore+addscore);
					teamServices.updateTeamInfo(userteam);
				}
				submissionServices.updateSubmission(thissubmit);
			}
		} else {
			thissubmit.setCorrect(false);
			long addscore = -challengeServices.getChallengeById(thissubmit.getChallengeId()).getScore();
			Users thisuser = userServices.getUserById(thissubmit.getUserid());
			long oldscore = thisuser.getScore();
			thisuser.setScore(oldscore+addscore);
			userServices.updateUser(thisuser);
			if (thisuser.getTeamid()!=null) {
				Teams userteam = teamServices.getTeamById(thisuser.getTeamid());
				oldscore = userteam.getScore().longValue();
				userteam.setScore(oldscore+addscore);
				teamServices.updateTeamInfo(userteam);
			}
			submissionServices.updateSubmission(thissubmit);
		}
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Update status of submission with id:"+thissubmit.getId()+" to "+(state?"correct":"incorrect"));
		resp.put("errmsg", "Submit updated successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 删除提交状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/delsubmit",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String deleteSubmissionStatus() throws Exception {
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
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String id = request.getParameter("id");
		
		if (id==null || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long submitid = Long.valueOf(id);
		Submissions thissubmit = submissionServices.getSubmissionById(submitid);
		if (thissubmit == null) {
			resp.put("errmsg", "Submission of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thissubmit.getCorrect()) {
			long addscore = -challengeServices.getChallengeById(thissubmit.getChallengeId()).getScore();
			Users thisuser = userServices.getUserById(thissubmit.getUserid());
			long oldscore = thisuser.getScore();
			thisuser.setScore(oldscore+addscore);
			userServices.updateUser(thisuser);
			if (thisuser.getTeamid()!=null) {
				Teams userteam = teamServices.getTeamById(thisuser.getTeamid());
				oldscore = userteam.getScore().longValue();
				userteam.setScore(oldscore+addscore);
				teamServices.updateTeamInfo(userteam);
			}
			submissionServices.deleteSubmission(submitid);
		} else {
			submissionServices.deleteSubmission(submitid);
		}
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Deleted submission with id:"+thissubmit.getId());
		resp.put("errmsg", "Submit deleted successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 后台修改用户页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/edituser/{id}",method={RequestMethod.GET})
	public ModelAndView EditUser(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/edituser");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		
		Users userobj = userServices.getUserById(id);
		if (userobj == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
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
		mv.addObject("currentuser",userobj);
		List<Countries> cts = countryServices.SelectAllCountry();
		mv.addObject("countrylist",cts);
		
		List<IpLogs> ips = ipLogServices.getAllIpLogsByUserId(userobj.getId());
		ArrayList<IpLogs> firstfiveip = new ArrayList<IpLogs>();
		for (int i=0;i<5;i++) {
			if (i>=ips.size()) {
				break;
			}
			firstfiveip.add(ips.get(i));
		}
		
		mv.addObject("ipused", firstfiveip);
		
		
		List<Submissions> usersubs = submissionServices.getAllSubmitByUserId(id);
		ArrayList<SubmissionList> sbl = new ArrayList<SubmissionList>();
		if (usersubs!=null) {
			for (int i=0;i<5;i++) {
				if (i>=usersubs.size()) {
					break;
				}
				Submissions s = usersubs.get(i);
				SubmissionList sl = new SubmissionList();
				sl.setId(s.getId());
				sl.settaskname(challengeServices.getChallengeById(s.getChallengeId()).getTitle());
				sl.setuser(userServices.getUserById(s.getUserid()).getUsername());
				sl.setSubmitTime(s.getSubmitTime());
				sl.setContent(s.getContent());
				sl.setCorrect(s.getCorrect());
				sbl.add(sl);
			}
		}
		
		mv.addObject("submits", sbl);
		
		
		mv.setViewName("admin/edituser");
		return mv;
	}
	
	/**
	 * 保存后台修改用户信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/saveuser/{id}",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String SaveUserinfo(@PathVariable long id) throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Users thisuser = userServices.getUserById(id);
		if (thisuser == null) {
			resp.put("errmsg", "This user does not exist!");
			resp.put("err", "-97");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String username = request.getParameter("username");
		String phone = request.getParameter("phone");
		String organize = request.getParameter("organize");
		String countryid = request.getParameter("country");
		String role = request.getParameter("role");
		String newscore = request.getParameter("score");
		String description = request.getParameter("description");
		String isenabled = request.getParameter("enable");
		
		if (username==null||role==null||isenabled==null||phone==null||organize==null||countryid==null||description==null
				||newscore==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		username = CommonUtils.XSSFilter(username);
		if (!username.equals(thisuser.getUsername())) {
			Users auser = userServices.getUserByName(username);
			if (auser!=null) {
				resp.put("errmsg", "Username has been taken!!");
				resp.put("err", "-6");
				String result = JSONObject.fromObject(resp).toString();
				return result;
			}
			thisuser.setUsername(username);
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
		description = CommonUtils.XSSFilter(description);
		
		if (!StringUtils.isNumeric(countryid)||Long.valueOf(countryid)<1||Integer.valueOf(countryid)>250) {
			resp.put("errmsg", "Invalid Countryid!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!isenabled.equalsIgnoreCase("true") && !isenabled.equalsIgnoreCase("false")) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!role.equals("admin") && !role.equals("user")) {
			resp.put("errmsg", "Invalid Role!!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(newscore)||newscore.length()==0) {
			resp.put("errmsg", "Invalid Score!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		long scorenum = Long.valueOf(newscore);
		thisuser.setPhone(phone);
		thisuser.setOrganization(organize);
		thisuser.setCountryid(Long.valueOf(countryid));
		thisuser.setRole(role);
		thisuser.setScore(Long.valueOf(newscore));
		thisuser.setDescription(description);
		thisuser.setIsenabled(isenabled.equals("true"));
		
		userServices.updateUser(thisuser);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Update Profile of user with id:"+thisuser.getId());
		resp.put("errmsg", "Userprofile update successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 后台重置用户密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/resetuserpass",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String ResetUserPassword() throws Exception {
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
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String id = request.getParameter("id");
		
		if (id==null || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long thisuserid = Long.valueOf(id);
		Users thisuser = userServices.getUserById(thisuserid);
		if (thisuser == null) {
			resp.put("errmsg", "User of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String newpssword = RandomStringUtils.randomAlphanumeric(8);
		String passhash = new SimpleHash("SHA-256",newpssword,thisuser.getSalt()).toHex();
		thisuser.setPassword(passhash);

		userServices.updateUser(thisuser);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Update password of user id="+thisuser.getId());
		resp.put("errmsg", "New password for "+thisuser.getUsername()+" is "+newpssword);
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	/**
	 * 删除用户(危险)
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/deluser",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String DeleteUser() throws Exception {
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
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String id = request.getParameter("id");
		
		if (id==null || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long thisuserid = Long.valueOf(id);
		Users thisuser = userServices.getUserById(thisuserid);
		if (thisuser == null) {
			resp.put("errmsg", "User of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}

		userServices.deleteUserById(thisuserid);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Deleted user of name: "+thisuser.getUsername());
		resp.put("errmsg", "User deleted successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	/**
	 * 后台查看用户IP界面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/userips/{id}",method={RequestMethod.GET})
	public ModelAndView UserIps(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/userips");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		
		Users userobj = userServices.getUserById(id);
		if (userobj == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		mv.addObject("currentuser", userobj);
		List<IpLogs> ips = ipLogServices.getAllIpLogsByUserId(userobj.getId());
		
		mv.addObject("ipused", ips);
		mv.setViewName("admin/userips");
		return mv;
	}
	
	/**
	 * 后台查看用户所有提交
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/usersubmit/{id}",method={RequestMethod.GET})
	public ModelAndView UserSubmissions(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/usersubmit");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		
		Users userobj = userServices.getUserById(id);
		if (userobj == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		mv.addObject("currentuser", userobj);
		
		List<Submissions> usersubs = submissionServices.getAllSubmitByUserId(id);
		ArrayList<SubmissionList> sbl = new ArrayList<SubmissionList>();
		if (usersubs!=null) {
			for (Submissions s:usersubs) {
				SubmissionList sl = new SubmissionList();
				sl.setId(s.getId());
				sl.settaskname(challengeServices.getChallengeById(s.getChallengeId()).getTitle());
				sl.setuser(userServices.getUserById(s.getUserid()).getUsername());
				sl.setSubmitTime(s.getSubmitTime());
				sl.setContent(s.getContent());
				sl.setCorrect(s.getCorrect());
				sbl.add(sl);
			}
		}
		
		mv.addObject("submits", sbl);
		mv.setViewName("admin/usersubmit");
		return mv;
	}
	
	/**
	 * 后台操作日志查看界面
	 * 
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="/admin/oplogs",method = {RequestMethod.GET})
	public ModelAndView OperationLogs() throws Exception {
		ModelAndView mv = new ModelAndView("admin/oplogs");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		List<Operatelog> alllogs = operateLogServices.getAllLogs();
		ArrayList<OpLogDisp> displist = new ArrayList<OpLogDisp>();
		if (alllogs!=null) {
			for (Operatelog log:alllogs) {
				OpLogDisp old = new OpLogDisp();
				old.setId(log.getId());
				old.setIpaddr(log.getIpaddr());
				old.setname(userServices.getUserById(log.getOperatorid()).getUsername());
				old.setOperatorid(log.getOperatorid());
				old.setOperatefunc(log.getOperatefunc());
				old.setOperatetime(log.getOperatetime());
				displist.add(old);
			}
		}
		
		mv.addObject("ops", displist);
		mv.setViewName("admin/oplogs");
		return mv;
		
	}
	

	/**
	 * 所有提交记录的列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/submitlist",method = {RequestMethod.GET})
	public ModelAndView AllSubmissions() throws Exception {
		ModelAndView mv = new ModelAndView("admin/submitlist");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		List<Submissions> allsubs = submissionServices.getAllSubmissions();
		ArrayList<SubmissionList> sbl = new ArrayList<SubmissionList>();
		List<Challenges> challs = challengeServices.getAllChallenges();
		List<Users> alluser = userServices.getAllUsers();
		List<Teams> allteam = teamServices.getAllTeams();
		if (allsubs!=null) {
			for (Submissions s:allsubs) {
				SubmissionList sl = new SubmissionList();
				sl.setId(s.getId());
				for (Challenges cls:challs) {
					if (s.getChallengeId()-cls.getId()==0) {
						sl.settaskname(cls.getTitle());
						break;
					}
				}
				//sl.settaskname(challengeServices.getChallengeById(s.getChallengeId()).getTitle());
				Users subuserobj = null;
				for (Users u:alluser) {
					if (s.getUserid()-u.getId()==0) {
						sl.setuser(u.getUsername());
						subuserobj  = u;
						break;
					}
				}
				for (Teams tm:allteam) {
					if (subuserobj!=null && subuserobj.getTeamid()!=null 
							&& subuserobj.getTeamid().longValue()==tm.getId().longValue()) {
						sl.setteamname(tm.getName());
						break;
					}
				}
				//sl.setuser(userServices.getUserById(s.getUserid()).getUsername());
				sl.setSubmitTime(s.getSubmitTime());
				sl.setContent(s.getContent());
				sl.setCorrect(s.getCorrect());
				sbl.add(sl);
			}
		}
		
		mv.addObject("submits", sbl);
		mv.setViewName("admin/submitlist");
		return mv;
		
	}
	
	
	/**
	 * 所有用户信息的列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/userlist",method = {RequestMethod.GET})
	public ModelAndView UserList() throws Exception {
		ModelAndView mv = new ModelAndView("admin/userlist");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		List<Users> alluser = userServices.getAllUsers();
		List<Countries> allcountries = countryServices.SelectAllCountry();
		List<IpLogs> alllogs = ipLogServices.getAllOrderByUserId();
		ArrayList<UserList> ulist = new ArrayList<UserList>();

		if (alluser!=null) {
			for (Users u:alluser) {
				UserList user = new UserList();
				Countries usercon = null;
				for (Countries country:allcountries) {
					if (u.getCountryid()-country.getId()==0) {
						usercon = country;
						break;
					}
				}
				//Countries usercon = countryServices.getCountryById(u.getCountryid());
				user.setcountrycode(usercon.getCountrycode());
				user.setCountryid(u.getCountryid());
				user.setcountryname(usercon.getCountryname());
				user.setDescription(u.getDescription());
				user.setEmail(u.getEmail());
				user.setId(u.getId());
				
				//user.setips(ipLogServices.countByUserId(u.getId()));
				int index1 = -1;
				int index2 = -1;
				for (int j=0;j<alllogs.size();j++) {
					if (alllogs.get(j).getUserid()-u.getId()==0) {
						index1 = j;
						break;
					}
				}
				if (index1!=-1) {
					for (int j=index1;j<alllogs.size();j++) {
						if (alllogs.get(j).getUserid()-u.getId()!=0) {
							index2 = j;
							break;
						}
					}
					if (index2==-1) {
						index2 = alllogs.size();
					}
					user.setips(index2-index1);
				} else {
					user.setips(0);
				}
				user.setIsenabled(u.getIsenabled());
				user.setLastactive(u.getLastactive());
				user.setOrganization(u.getOrganization());
				user.setRegtime(u.getRegtime());
				user.setRole(u.getRole());
				user.setUsername(u.getUsername());
				ulist.add(user);
			}
		}
		
		mv.addObject("userlist",ulist);
		mv.setViewName("admin/userlist");
		return mv;
		
	}
	
	/**
	 * 所有用户信息的列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/teamlist",method = {RequestMethod.GET})
	public ModelAndView ShowTeamList() throws Exception {
		ModelAndView mv = new ModelAndView("admin/teamlist");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		
		List<Users> alluser = userServices.getAllUsers();
		List<Countries> allcountries = countryServices.SelectAllCountry();
		List<IpLogs> alllogs = ipLogServices.getAllOrderByUserId();
		
		List<Submissions> correctsubs = submissionServices.getAllCorrectOrderByTime();
		ArrayList<TeamList> tlist = new ArrayList<TeamList>();
		List<Teams> allteam = teamServices.getAllTeams();

		if (allteam!=null) {
			for (Teams t:allteam) {
				TeamList ateam = new TeamList(t);
				Countries teamcon = null;
				for (Countries country:allcountries) {
					if (t.getCountryid()-country.getId()==0) {
						teamcon = country;
						break;
					}
				}
				//Countries usercon = countryServices.getCountryById(u.getCountryid());
				ateam.setcountrycode(teamcon.getCountrycode());
				ateam.setcountryname(teamcon.getCountryname());
				
				//user.setips(ipLogServices.countByUserId(u.getId()));
				List<Users> teammembers = userServices.getAllUsersByTeamId(t.getId());
				Map<String,Long> iptable = new HashMap<String,Long>();
				long teamips = 0;
				for (Users member:teammembers) {
					if (member.getRole().equals("leader")){
						ateam.setcreator(member.getUsername());
					}
					int index1 = -1;
					int index2 = -1;
					for (int j=0;j<alllogs.size();j++) {
						if (alllogs.get(j).getUserid()-member.getId()==0) {
							index1 = j;
							break;
						}
					}
					if (index1!=-1) {
						for (int j=index1;j<alllogs.size();j++) {
							if (alllogs.get(j).getUserid()-member.getId()!=0) {
								index2 = j;
								break;
							}
						}
						if (index2==-1) {
							index2 = alllogs.size();
						}
						
						for (int k=index1;k<index2;k++) {
							if (!iptable.containsKey(alllogs.get(k).getIpaddr())) {
								iptable.put((String)alllogs.get(k).getIpaddr(), Long.valueOf(1));
							} else {
								long oldips = iptable.get(alllogs.get(k).getIpaddr()).longValue();
								iptable.put((String)alllogs.get(k).getIpaddr(), Long.valueOf(oldips+1));
							}
						}
						teamips = iptable.size();
						//teamips = teamips + index2-index1;
					} else {
						teamips = teamips + 0;
					}
				} 
				
				ateam.setips(teamips);
				
				for (Submissions sb:correctsubs) {
					Users subuserobj = null;
					for (Users u:alluser) {
						if (u.getId().longValue()==sb.getUserid().longValue()) {
							subuserobj = u;
							break;
						}
					}
					if (subuserobj!=null && subuserobj.getTeamid()!=null && subuserobj.getTeamid().longValue()==t.getId().longValue()) {
						ateam.setLastSubmit(sb.getSubmitTime());
						break;
					}
				}
				
				tlist.add(ateam);
			}
		}
		
		mv.addObject("teamlist",tlist);
		
		mv.setViewName("admin/teamlist");
		return mv;
		
	}
	
	/**
	 * 后台修改用户页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/editteam/{id}",method={RequestMethod.GET})
	public ModelAndView EditTeam(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/editteam");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		
		Teams teamobj = teamServices.getTeamById(id);
		if (teamobj == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}

		Countries teamcountry = countryServices.getCountryById(teamobj.getCountryid());
		mv.addObject("teamname",teamobj.getName());
		mv.addObject("countrycode",teamcountry.getCountrycode());
		mv.addObject("country",teamcountry.getCountryname());
		mv.addObject("teamscore",teamobj.getScore());
		mv.addObject("teamrank",CommonUtils.getTeamrank(teamobj,teamServices,userServices,submissionServices));
		List<Challenges> allchall = challengeServices.getAllAvailChallenges();
		long sum = 0;
		for (Challenges ch:allchall) {
			sum += ch.getScore();
		}
		mv.addObject("totalscore",sum);
		long allper = 0;
		if (sum!=0) {
			allper = Math.round(((double)teamobj.getScore())/((double)sum)*100);
		} 
		
		mv.addObject("totalpercent",allper);
		ArrayList<CateProcess> process = new ArrayList<CateProcess>();
		ArrayList<Submissions> passedtasks = new ArrayList<Submissions>();
		ArrayList<Submissions> usercorrect = new ArrayList<Submissions>();

		List<Submissions> allcorrect = submissionServices.getAllCorrectOrderByTime();
		List<Users> teammembers = userServices.getAllUsersByTeamId(teamobj.getId());

		if (teammembers!=null) {
			for (int i=0;i<allcorrect.size();i++) {
				Users userinteam = null;
				for (Users u:teammembers) {
					if (u.getTeamid()!=null && u.getTeamid().longValue()==teamobj.getId().longValue() 
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
		
		
		mv.addObject("thisteam",teamobj);
		
		List<Countries> cts = countryServices.SelectAllCountry();
		mv.addObject("countrylist",cts);
		
		ArrayList<IpLogsList> firstfiveip = new ArrayList<IpLogsList>();
		ArrayList<IpLogs> teamlogs = new ArrayList<IpLogs>();
		for (Users member:teammembers) {
			List<IpLogs> ips = ipLogServices.getAllIpLogsByUserId(member.getId());
			teamlogs.addAll(ips);
		}
		
		for (int i=0;i<5;i++) {
			if (i>=teamlogs.size()) {
				break;
			}
			IpLogsList disip = new IpLogsList(teamlogs.get(i));
			for (Users u:teammembers) {
				if (u.getId().longValue()==disip.getUserid().longValue()) {
					disip.setusedby(u.getUsername());
					break;
				}

			}
			firstfiveip.add(disip);
		}
		mv.addObject("ipused", firstfiveip);
		
		
		List<Submissions> subs = submissionServices.getAllSubmissions();
		ArrayList<SubmissionList> sbl = new ArrayList<SubmissionList>();
		if (subs!=null) {
			for (Submissions sb:subs) {
				Users subuserobj = null;
				for (Users u:teammembers) {
					if (u.getId().longValue()==sb.getUserid().longValue()) {
						subuserobj = u;
						break;
					}
				}
				if (subuserobj!=null) {
					SubmissionList sl = new SubmissionList();
					sl.setId(sb.getId());
					sl.settaskname(challengeServices.getChallengeById(sb.getChallengeId()).getTitle());
					sl.setuser(userServices.getUserById(sb.getUserid()).getUsername());
					sl.setSubmitTime(sb.getSubmitTime());
					sl.setContent(sb.getContent());
					sl.setCorrect(sb.getCorrect());
					sl.setteamname(teamobj.getName());
					sbl.add(sl);
					if (sbl.size()>=5) {
						break;
					}
				}
			}
		}
		
		mv.addObject("submits", sbl);
		
		
		mv.setViewName("admin/editteam");
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="/admin/delmember.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String Delteammember() throws Exception {
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
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "No permission!");
			resp.put("err", "-98");
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
		
		if (thismember==null) {
			resp.put("errmsg", "This member do not exist!!");
			resp.put("err", "-7");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (thismember.getRole().equals("leader")) {
			resp.put("errmsg", "Team creator can't be kicked!");
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

		thismember.setTeamid(null);
		Long oldscore = thisteam.getScore();
		Long newscore = oldscore - thismember.getScore();
		thisteam.setScore(newscore);
		teamServices.updateTeamInfo(thisteam);
		userServices.updateUser(thismember);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Kick member with id: "+thismember.getId().longValue()+"of team with id: "+teamidnum.longValue());
		resp.put("errmsg", thismember.getUsername()+" kicked out successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/admin/saveteaminfo.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String SaveTeamInfo() throws Exception {
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
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "No Permission!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String teamid = request.getParameter("id");
		String teamname = request.getParameter("teamname");
		String teamorgan = request.getParameter("organization");
		String countryid = request.getParameter("countryid");
		String teamdescript = request.getParameter("description");
		String isenabled = request.getParameter("enable");
		
		if (teamid==null || teamname==null || teamorgan == null || teamdescript==null || countryid==null||isenabled==null) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!isenabled.equalsIgnoreCase("true") && !isenabled.equalsIgnoreCase("false")) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-8");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (teamname.length()==0) {
			resp.put("errmsg", "Team name can't be empty!!");
			resp.put("err", "-7");
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
		teamname = CommonUtils.XSSFilter(teamname);
		teamorgan = CommonUtils.XSSFilter(teamorgan);
		teamdescript = CommonUtils.XSSFilter(teamdescript);
		
		
		
		Teams thisteam = teamServices.getTeamById(teamidnum);
		if (thisteam==null) {
			resp.put("errmsg", "Team with id: "+teamid+" do not exist!!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		thisteam.setName(teamname);
		thisteam.setCountryid(Long.valueOf(countryid));
		thisteam.setOrganization(teamorgan);
		thisteam.setDescription(teamdescript);
		thisteam.setIsenabled(isenabled.equals("true"));

		teamServices.updateTeamInfo(thisteam);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Update teaminfo of team id="+teamidnum.longValue());
		resp.put("errmsg", "Team Info Updated Successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/admin/delteam.json",method = {RequestMethod.POST},produces="application/json;charset=utf-8")
	public String Deleteteam() throws Exception {
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
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "No Permission!!");
			resp.put("err", "-98");
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

		List<Users> teamusers = userServices.getAllUsersByTeamId(teamidnum);
		for (Users u:teamusers) {
			u.setTeamid(null);
			u.setRole("user");
			userServices.updateUser(u);
		}
		
		teamServices.deleteTeamById(teamidnum);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Delete team name "+thisteam.getName());
		resp.put("errmsg", "Team "+thisteam.getName()+" deleted successfully!!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	/**
	 * 后台查看队伍IP界面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/teamips/{id}",method={RequestMethod.GET})
	public ModelAndView TeamIps(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/teamips");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		
		//Users userobj = userServices.getUserById(id);
		Teams teamobj = teamServices.getTeamById(id);
		if (teamobj == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		
		mv.addObject("currentteam", teamobj);
		
		
		ArrayList<IpLogsList> allips = new ArrayList<IpLogsList>();
		ArrayList<IpLogs> teamlogs = new ArrayList<IpLogs>();
		List<Users> teammembers = userServices.getAllUsersByTeamId(teamobj.getId());
		for (Users member:teammembers) {
			List<IpLogs> ips = ipLogServices.getAllIpLogsByUserId(member.getId());
			teamlogs.addAll(ips);
		}
		
		for (int i=0;i<teamlogs.size();i++) {
			IpLogsList disip = new IpLogsList(teamlogs.get(i));
			for (Users u:teammembers) {
				if (u.getId().longValue()==disip.getUserid().longValue()) {
					disip.setusedby(u.getUsername());
					break;
				}

			}
			allips.add(disip);
		}
		mv.addObject("ipused", allips);
		mv.setViewName("admin/teamips");
		return mv;
	}
	
	
	/**
	 * 后台查看队伍的所有提交
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/teamsubmit/{id}",method={RequestMethod.GET})
	public ModelAndView TeamSubmissions(@PathVariable long id) throws Exception {
		ModelAndView mv = new ModelAndView("admin/teamsubmit");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
			return new ModelAndView("redirect:/showinfo?err=-99");
		}
		
		
		Teams teamobj = teamServices.getTeamById(id);//userServices.getUserById(id);
		if (teamobj == null) {
			return new ModelAndView("redirect:/showinfo?err=404");
		}
		mv.addObject("currentteam", teamobj);
		
		List<Users> teammembers = userServices.getAllUsersByTeamId(teamobj.getId());
		List<Submissions> subs = submissionServices.getAllSubmissions();
		ArrayList<SubmissionList> sbl = new ArrayList<SubmissionList>();
		if (subs!=null) {
			for (Submissions sb:subs) {
				Users subuserobj = null;
				for (Users u:teammembers) {
					if (u.getId().longValue()==sb.getUserid().longValue()) {
						subuserobj = u;
						break;
					}
				}
				if (subuserobj!=null) {
					SubmissionList sl = new SubmissionList();
					sl.setId(sb.getId());
					sl.settaskname(challengeServices.getChallengeById(sb.getChallengeId()).getTitle());
					sl.setuser(userServices.getUserById(sb.getUserid()).getUsername());
					sl.setSubmitTime(sb.getSubmitTime());
					sl.setContent(sb.getContent());
					sl.setCorrect(sb.getCorrect());
					sl.setteamname(teamobj.getName());
					sbl.add(sl);
				}
			}
		}
		
		mv.addObject("submits", sbl);
		mv.setViewName("admin/teamsubmit");
		return mv;
	}
	
	/**
	 * 保存系统设置
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/saveconfig.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String SaveSystemConfig() throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String maxteammember = request.getParameter("maxmem");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		
		
		if (maxteammember == null || maxteammember.length()==0) {
			resp.put("errmsg", "Max team members must be a number!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!StringUtils.isNumeric(maxteammember)) {
			resp.put("errmsg", "Invalid Parameter!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		
		if (starttime==null) {
			starttime = "";
		} 
		
		if (starttime.length()!=0 && !CommonUtils.isValidDate(starttime)) {
			resp.put("errmsg", "Invalid Date Format!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (endtime==null) {
			endtime = "";
		}
		
		if (endtime.length()!=0 && !CommonUtils.isValidDate(endtime)) {
			resp.put("errmsg", "Invalid Date Format!");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		long maxteammembernum = Long.valueOf(maxteammember).longValue();
		List<Config> sysconfig = configServices.getAllConfigObjs();
		
		for (Config conf:sysconfig) {
			if (conf.getVarname().equals("max_team_members")) {
				conf.setVarvalue(String.valueOf(maxteammembernum));
				configServices.updateConfig(conf);
			}
			if (conf.getVarname().equals("comp_start")) {
				conf.setVarvalue(starttime);
				configServices.updateConfig(conf);
			}
			if (conf.getVarname().equals("comp_end")) {
				conf.setVarvalue(endtime);
				configServices.updateConfig(conf);
			}
		}
		
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Modify system config.");
		resp.put("errmsg", "SystemConfig Updated successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 添加IP进黑名单
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/addblockip.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String AddBlockip() throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String ip_addr = request.getParameter("ip_addr");
		
		if (ip_addr==null) {
			resp.put("errmsg", "Please input ip_addr!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String ipaddrpattern = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
		Pattern pattern = Pattern.compile(ipaddrpattern);
		Matcher matcher = pattern.matcher(ip_addr);
		
		if (!matcher.find()) {
			resp.put("errmsg", "Invalid IP address format.");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		BannedIps exist = bannedIpServices.getRecordByIp(ip_addr);
		if (exist!=null) {
			resp.put("errmsg", "This ip address exists!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		BannedIps bi = new BannedIps();
		bi.setIpAddr(ip_addr);
		bannedIpServices.insertIpRecord(bi);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Add IP: "+ip_addr+" to blacklist.");
		resp.put("errmsg", "New ip added to blacklist!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	
	/**
	 * 编辑黑名单
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/editblockip.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String EditBlockip() throws Exception {
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
			resp.put("errmsg", "Please login!!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String id = request.getParameter("id");
		String ip_addr = request.getParameter("ip_addr");
		
		if (ip_addr==null) {
			resp.put("errmsg", "Please input ip_addr!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (id==null || id.length()==0 || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long numid = Long.valueOf(id);
		
		BannedIps thisrecord = bannedIpServices.getRecordById(numid);
		
		if (thisrecord==null) {
			resp.put("errmsg", "Error.This Record not exist!");
			resp.put("err", "-3");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		String ipaddrpattern = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
		Pattern pattern = Pattern.compile(ipaddrpattern);
		Matcher matcher = pattern.matcher(ip_addr);
		
		if (!matcher.find()) {
			resp.put("errmsg", "Invalid IP address format.");
			resp.put("err", "-4");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		BannedIps exist = bannedIpServices.getRecordByIp(ip_addr);
		if (exist!=null) {
			resp.put("errmsg", "This ip address exists!");
			resp.put("err", "-5");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		thisrecord.setIpAddr(ip_addr);
		bannedIpServices.updateRecord(thisrecord);
		
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Modify Banned ip record : "+thisrecord.getId());
		resp.put("errmsg", "IP Address updated successfully!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}
	
	/**
	 * 删除IP黑名单
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/delblockip.json",method={RequestMethod.POST},produces="application/json;charset=utf-8")
	public String DelCate() throws Exception {
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
		
		String id = request.getParameter("id");
		
		if (!currentUser.isAuthenticated()) {
			resp.put("errmsg", "Session time out!! Please login again !!");
			resp.put("err", "-98");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (!currentUser.hasRole("admin")) {
			resp.put("errmsg", "Not Permitted!!");
			resp.put("err", "-99");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		if (id==null || !StringUtils.isNumeric(id)) {
			resp.put("errmsg", "Invalid Parameter!!");
			resp.put("err", "-1");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		Long ipid = Long.valueOf(id);
		BannedIps ipobj = bannedIpServices.getRecordById(ipid);
		if (ipobj == null) {
			resp.put("errmsg", "Blacklist Record of id:"+id+" do not exist!!");
			resp.put("err", "-2");
			String result = JSONObject.fromObject(resp).toString();
			return result;
		}
		
		
		bannedIpServices.deleteRecordById(ipid);
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Delete IP :"+ipobj.getIpAddr()+"from black list.");
		resp.put("errmsg", "IP Address deleted from blacklist successfully !!");
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
		
	}

}
