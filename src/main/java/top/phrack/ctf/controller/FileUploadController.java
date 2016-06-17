/**
 * 
 */
package top.phrack.ctf.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.sf.json.JSONObject;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.models.services.FileServices;
import top.phrack.ctf.models.services.IPlogServices;
import top.phrack.ctf.models.services.OperateLogServices;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Files;
import top.phrack.ctf.pojo.Users;
import top.phrack.ctf.utils.CommonUtils;
import top.phrack.ctf.utils.LogUtils;

/**
 * 后台文件上传的控制器
 *
 * @author Jarvis
 * @date 2016年4月16日
 */
@Controller
public class FileUploadController {
	
	private Logger log = LoggerFactory.getLogger(FileUploadController.class);
	private static String filePath="WEB-INF/upload/";
	
	@Autowired
	private HttpServletRequest request;
	@Resource 
	private UserServices userServices;
	@Resource
	private BannedIpServices bannedIpServices;
	@Resource
	private FileServices fileServices;
	@Resource 
	private IPlogServices ipLogServices;
	@Resource 
	private OperateLogServices operateLogServices;
	
	@ResponseBody
	@RequestMapping(value="/admin/fileUpload", method={RequestMethod.POST},produces="application/json;charset=utf-8")  
	public String UploadFile(@RequestParam("file_upload") CommonsMultipartFile[] files) throws IOException {
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
		
		String filename = null;
		for(CommonsMultipartFile myfile : files) {
			InputStream is = myfile.getInputStream();
			String filemd5 = null;
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.reset();
				byte[] bytes = new byte[8192];
				int numBytes;
				while ((numBytes = is.read(bytes)) != -1) {
					md5.update(bytes, 0, numBytes);
				}
				byte[] digest = md5.digest();
				filemd5 = new String(Hex.encode(digest));
			} catch (Exception e) {
				e.printStackTrace();
			}
			filename = myfile.getOriginalFilename()+"."+filemd5;
			String rootpath = System.getProperty("phrackCTF.root");
			String absolutepath = rootpath+filePath;
			File filetosave = new File(absolutepath,filename);
			if (!filetosave.exists()) {
				is = myfile.getInputStream();
				FileUtils.copyInputStreamToFile(is,filetosave);
			}
			Files afile = fileServices.getFileNotAttachedByName(filename);
			if (afile==null) {
				afile = new Files();
				afile.setAddby(userobj.getUsername());
				afile.setFilename(filename);
				afile.setMd5(filemd5);
				afile.setResindex("upload/"+filename);
				afile.setSize(myfile.getSize());
				fileServices.insertNewFileRecord(afile);
			}
			
			resp.put("fileid",String.valueOf(afile.getId()));
			resp.put("index",afile.getResindex());
			resp.put("filename", filename);
				
		}
		
		resp.put("errmsg", "File Uploaded successfully!!");
		CommonUtils.storeUserIpUsageInfo(request, userServices, ipLogServices, userobj.getEmail());
		LogUtils.recordOperateLog(request, userobj, operateLogServices, "Uploaded a file:"+filename);
		resp.put("err", "0");
		String result = JSONObject.fromObject(resp).toString();
		return result;
	}
}
