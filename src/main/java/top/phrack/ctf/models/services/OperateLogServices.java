/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.Operatelog;

/**
 * 后台操作日志的接口
 *
 * @author Jarvis
 * @date 2016年4月16日
 */
public interface OperateLogServices {
	public int insertNewlog(Operatelog alog);
	
	public List<Operatelog> getAllLogs();
	
	
}
