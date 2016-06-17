/**
 * 
 */
package top.phrack.ctf.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import top.phrack.ctf.comparators.CompareDate;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.NewsServices;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.News;
import top.phrack.ctf.utils.CommonUtils;
import top.phrack.ctf.utils.MailUtil;

/**
 * Home控制器
 *
 * @author Jarvis
 * @date 2016年3月31日
 */
@Controller
public class HomeController {
	private Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Resource
	private NewsServices newsServices;
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

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/home")
	public ModelAndView home() throws Exception {
		
		ModelAndView mv = new ModelAndView("home");
		Subject currentUser = SecurityUtils.getSubject();
		CommonUtils.setControllerName(request, mv);
		CommonUtils.setUserInfo(currentUser, userServices, teamServices,submissionServices,mv);
		if (CommonUtils.CheckIpBanned(request, bannedIpServices)) {
		}
		List<News> newslist = newsServices.selectAll();
		if (newslist!=null) {
			CompareDate c = new CompareDate();
			Collections.sort(newslist,c);
		}
		
		mv.addObject("newslist", newslist);
		mv.setViewName("home");
		return mv;
	}
}