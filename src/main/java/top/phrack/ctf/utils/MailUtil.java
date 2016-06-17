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
			helper.setText("<p><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;;\">This mail was sent automatically. Please DO NOT reply this mail!</span>"+
						"</p><p><br/></p><p><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;;\">Your password Reset link is:</span>"+
						"</p><p><a href=\""+reseturl+"\" _src=\""+reseturl+"\" style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; text-decoration: underline;\">"+
						"<span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;;\">"+reseturl+"</span></a></p><p><br/></p><p><span style=\"font-family: 微软雅黑,"+
						" &#39;Microsoft YaHei&#39;;\">With kind regards,<br style=\"font-family: arial;  line-height: 23.8px; white-space: normal; background-color: rgb(255, 255, 255);\"/>"+
						"------------------------------------------------------------<br style=\"font-family: arial;  line-height: 23.8px; white-space: normal; background-color: rgb(255, 255, 255)"+
						";\"/>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Phrack Team</span></p>", true);
			helper.setTo(new String[]{target});
			helper.setSubject("Phrack CTF Password Reset Email.");
			helper.setFrom("ctf@phrack.top","phrackCTF");
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
			helper.setFrom("ctf@phrack.top","phrackCTF");
			javaMailSender.send(mimeMsg);
			log.info("Mail send to "+Arrays.toString(targetuser));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
