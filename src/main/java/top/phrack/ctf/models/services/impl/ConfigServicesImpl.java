/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.ConfigMapper;
import top.phrack.ctf.models.services.ConfigServices;
import top.phrack.ctf.pojo.Config;

/**
 * 系统设置服务实现类
 *
 * @author Jarvis
 * @date 2016年5月23日
 */
@Service("ConfigServices")
public class ConfigServicesImpl implements ConfigServices{

	
	@Resource
	private ConfigMapper configMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ConfigServices#getSysConfig()
	 */
	@Override
	public Map<String, String> getSysConfig() {

		Map<String, String> configs = new HashMap<String, String>();
		List<Config> allconf = configMapper.selectAllConfig();
		
		for (Config conf:allconf) {
			configs.put(conf.getVarname(), conf.getVarvalue());
		}
		return configs;
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ConfigServices#getAllConfigObjs()
	 */
	@Override
	public List<Config> getAllConfigObjs() {

		return configMapper.selectAllConfig();
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ConfigServices#updateConfig(top.phrack.ctf.pojo.Config)
	 */
	@Override
	public int updateConfig(Config config) {

		return configMapper.updateByPrimaryKey(config);
	}
	
}
