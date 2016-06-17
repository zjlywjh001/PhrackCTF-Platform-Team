/**
 * 
 */
package top.phrack.ctf.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import top.phrack.ctf.models.services.OperateLogServices;
import top.phrack.ctf.pojo.Operatelog;
import top.phrack.ctf.pojo.Users;

/**
 * 后台日志记录工具类
 *
 * @author Jarvis
 * @date 2016年4月16日
 */
public class LogUtils {

	
	public static void recordOperateLog(HttpServletRequest request,Users userobj,OperateLogServices operatelogServices,String logcontent) {
		String userip = request.getRemoteAddr(); 
		Date current = new Date();
		Operatelog alog = new Operatelog();
		alog.setIpaddr(userip);
		alog.setOperatefunc(logcontent);
		alog.setOperatetime(current);
		alog.setOperatorid(userobj.getId());
		
		operatelogServices.insertNewlog(alog);
	}
}
