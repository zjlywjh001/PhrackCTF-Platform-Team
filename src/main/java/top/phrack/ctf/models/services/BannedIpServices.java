/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.BannedIps;

/**
 * 
 * 被禁止登录的IP查询服务
 * 
 * @author Jarvis
 * @date 2016年4月10日
 */
public interface BannedIpServices {
	
	public BannedIps getRecordByIp(String ip);
	
	public List<BannedIps> getAllIps();
	
	public int deleteRecordById(Long id);
	
	public int updateRecord(BannedIps ip);
	
	public int insertIpRecord(BannedIps newip);
	
	public BannedIps getRecordById(Long id);
	
}
