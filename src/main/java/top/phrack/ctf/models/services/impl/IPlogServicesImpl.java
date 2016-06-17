/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.IpLogsMapper;
import top.phrack.ctf.models.services.IPlogServices;
import top.phrack.ctf.pojo.IpLogs;

/**
 *
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
@Service("IPlogServices")
public class IPlogServicesImpl implements IPlogServices{

	@Resource
	private IpLogsMapper ipLogsMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.IPlogServices#getLogsByAddrAndId(java.lang.String, long)
	 */
	public IpLogs getLogsByAddrAndId(String addr, long uid) {
		return ipLogsMapper.selectByAddrAndId(addr, uid);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.IPlogServices#insertNewLog(top.phrack.ctf.pojo.IpLogs)
	 */
	public int insertNewLog(IpLogs aLog) {
		return ipLogsMapper.insert(aLog);
		
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.IPlogServices#updateLogInfo(top.phrack.ctf.pojo.IpLogs)
	 */
	public int updateLogInfo(IpLogs newLog) {
		return ipLogsMapper.updateByPrimaryKey(newLog);
		
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.IPlogServices#countByUserId(java.lang.Long)
	 */
	@Override
	public long countByUserId(Long userid) {

		return ipLogsMapper.CountByUserId(userid);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.IPlogServices#getAllIpLogsByUserId(java.lang.Long)
	 */
	@Override
	public List<IpLogs> getAllIpLogsByUserId(Long userid) {

		return ipLogsMapper.selectByUserid(userid);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.IPlogServices#getAllOrderByUserId()
	 */
	@Override
	public List<IpLogs> getAllOrderByUserId() {

		return ipLogsMapper.selectAllOrderByUserId();
	}

}
