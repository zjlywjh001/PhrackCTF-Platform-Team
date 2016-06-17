/**
 * 
 */
package top.phrack.ctf.controller;

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
import org.springframework.web.servlet.ModelAndView;

import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.RuleServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Rules;
import top.phrack.ctf.utils.CommonUtils;

/**
 * 规则页面
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
@Controller
public class RuleController {
	private Logger log = LoggerFactory.getLogger(RuleController.class);
	
	@Resource
	private RuleServices ruleServices;
	@Resource 
	private UserServices userServices;
	@Autowired
	private HttpServletRequest request;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private SubmissionServices submissionServices;
	@Resource
	private TeamServices teamServices;
	
	@RequestMapping(value = "/rules",method=RequestMethod.GET)
	public ModelAndView showrules() throws Exception{
		ModelAndView mv = new ModelAndView("rules");
		CommonUtils.setControllerName(request, mv);
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
			currentUser.logout();
		}
		Rules ruleentity = ruleServices.getRulesById(1);
		mv.addObject("rule",ruleentity);
		mv.setViewName("rules");
		return mv;
		
	}
}
