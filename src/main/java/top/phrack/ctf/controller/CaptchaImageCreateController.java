/**
 * 
 */
package top.phrack.ctf.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

/**
 * 验证码生成器
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
@Controller
public class CaptchaImageCreateController {
	
	private Producer captchaProducer = null;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired  
    public void setCaptchaProducer(Producer captchaProducer) {  
        this.captchaProducer = captchaProducer;  
    }  
	
	@ResponseBody
	@RequestMapping(value = "/captcha.jpg",method = {RequestMethod.GET})
	public byte[] generateCapcha() throws Exception{
		response.setDateHeader("Expires", 0);     
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");    
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");    
        response.setHeader("Pragma", "no-cache");    
        response.setContentType("image/jpg");    
        String capText = captchaProducer.createText();    
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);    
        BufferedImage bi = captchaProducer.createImage(capText);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", out); 

        return  out.toByteArray();
		
	}
}
