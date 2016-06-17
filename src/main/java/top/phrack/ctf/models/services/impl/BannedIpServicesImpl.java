/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.BannedIpsMapper;
import top.phrack.ctf.models.services.BannedIpServices;
import top.phrack.ctf.pojo.BannedIps;

/**
 * 禁止登录IP查询服务类的实现
 *
 * @author Jarvis
 * @date 2016年4月10日
 */
@Service("BannedIpServices")
public class BannedIpServicesImpl implements BannedIpServices{

	@Resource
	private BannedIpsMapper bannedIpsMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.BannedIpServices#getRecordByIp(java.lang.String)
	 */
	public BannedIps getRecordByIp(String ip) {

		return bannedIpsMapper.selectByIpAddr(ip);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.BannedIpServices#deleteRecord(top.phrack.ctf.pojo.BannedIps)
	 */
	@Override
	public int deleteRecordById(Long id) {

		return bannedIpsMapper.deleteByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.BannedIpServices#updateRecord(top.phrack.ctf.pojo.BannedIps)
	 */
	@Override
	public int updateRecord(BannedIps ip) {

		return bannedIpsMapper.updateByPrimaryKey(ip);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.BannedIpServices#insertIpRecord(top.phrack.ctf.pojo.BannedIps)
	 */
	@Override
	public int insertIpRecord(BannedIps newip) {

		return bannedIpsMapper.insert(newip);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.BannedIpServices#getAllIps()
	 */
	@Override
	public List<BannedIps> getAllIps() {

		return bannedIpsMapper.selectAll();
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.BannedIpServices#getRecordById(java.lang.Long)
	 */
	@Override
	public BannedIps getRecordById(Long id) {

		return bannedIpsMapper.selectByPrimaryKey(id);
	}
	
}
