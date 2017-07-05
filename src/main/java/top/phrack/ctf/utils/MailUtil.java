/**
 * 
 */
package top.phrack.ctf.utils;


import java.util.Arrays;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * 邮件发送工具类
 *
 * @author Jarvis
 * @date 2016年4月9日
 */
@Service
@EnableAsync
public class MailUtil {
	
	private Logger log = LoggerFactory.getLogger(MailUtil.class);

	@Async
	public void sendPasswordResetMail(String target,String reseturl,JavaMailSender javaMailSender) throws InterruptedException  {
		try {
			MimeMessage mimeMsg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, false, "utf-8");
			String content = (String) PropertyPlaceholder.getProperty("mail.template");
			content = content.replace("{reseturl}", reseturl);
			helper.setText(content, true);
			helper.setTo(new String[]{target});
			String title = (String) PropertyPlaceholder.getProperty("mail.title");
			helper.setSubject(title); 
			String authormail = (String) PropertyPlaceholder.getProperty("mail.sendfrom");
			String authorname = (String) PropertyPlaceholder.getProperty("mail.sendname");
			helper.setFrom(authormail,authorname);
			javaMailSender.send(mimeMsg);
			log.info("Password Reset Mail has been send to "+target);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Async
	public void mailTo(String[] targetuser,String title,String content,JavaMailSender javaMailSender) {//邮件功能
		try {
			MimeMessage mimeMsg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, false, "utf-8");
			helper.setText(content, true);
			helper.setBcc(targetuser);
			helper.setSubject(title);
			String authormail = (String) PropertyPlaceholder.getProperty("mail.sendfrom");
			String authorname = (String) PropertyPlaceholder.getProperty("mail.sendname");
			helper.setFrom(authormail,authorname);
			javaMailSender.send(mimeMsg);
			log.info("Mail send to "+Arrays.toString(targetuser));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
