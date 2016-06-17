/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.IpLogs;

/**
 * 用户操作日志
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
public interface IPlogServices {
	public IpLogs getLogsByAddrAndId(String addr,long uid);
	
	public int insertNewLog(IpLogs aLog);
	
	public int updateLogInfo(IpLogs newLog);
	
	public long countByUserId(Long userid);
	
	public List<IpLogs> getAllIpLogsByUserId(Long userid);
	
	public List<IpLogs> getAllOrderByUserId();
}
