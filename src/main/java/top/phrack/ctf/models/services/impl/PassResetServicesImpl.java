/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.PassresetMapper;
import top.phrack.ctf.models.services.PassResetServices;
import top.phrack.ctf.pojo.Passreset;

/**
 * 密码重置服务的实现类
 *
 * @author Jarvis
 * @date 2016年4月9日
 */
@Service("PassResetServices")
public class PassResetServicesImpl implements PassResetServices {

	@Resource
	private PassresetMapper passresetMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.PassResetServices#insertNewtoken(top.phrack.ctf.pojo.Passreset)
	 */
	public int insertNewtoken(Passreset passreset) {
		
		return passresetMapper.insert(passreset);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.PassResetServices#getResetRecordBytoken(java.lang.String)
	 */
	public Passreset getResetRecordBytoken(String token) {

		return passresetMapper.selectBytoken(token);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.PassResetServices#updateResetRecord(top.phrack.ctf.pojo.Passreset)
	 */
	public int updateResetRecord(Passreset passreset) {

		return passresetMapper.updateByPrimaryKey(passreset);
	}
	
}
