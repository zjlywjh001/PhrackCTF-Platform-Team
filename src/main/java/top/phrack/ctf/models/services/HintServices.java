/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.Hints;

/**
 * 服务类接口，对应字段hints
 *
 * @author Jarvis
 * @date 2016年4月11日
 */
public interface HintServices {
	public List<Hints> getHintsByTaskId(Long id);
	
	public int createNewHint(Hints hint);
	
	public List<Hints> getAllHints();
	
	public Hints getHintById(Long id);
	
	public int updateHint(Hints hint);
	
	public int deleteById(Long id);
}
