/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;
import java.util.Map;

import top.phrack.ctf.pojo.Config;

/**
 * 系统设置信息
 *
 * @author Jarvis
 * @date 2016年5月23日
 */
public interface ConfigServices {
	
	public Map<String,String> getSysConfig();
	
	public List<Config> getAllConfigObjs();
	
	public int updateConfig(Config config);
	
}
