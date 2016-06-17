/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.HintsMapper;
import top.phrack.ctf.models.services.HintServices;
import top.phrack.ctf.pojo.Hints;

/**
 * Hints表段服务实现类
 *
 * @author Jarvis
 * @date 2016年4月11日
 */
@Service("HintServices")
public class HintServicesImpl implements HintServices {

	@Resource
	private HintsMapper hintsMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.HintServices#getHintsByTaskId(java.lang.Long)
	 */
	public List<Hints> getHintsByTaskId(Long id) {
		
		return hintsMapper.selectHintsByTaskId(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.HintServices#createNewHint(top.phrack.ctf.pojo.Hints)
	 */
	@Override
	public int createNewHint(Hints hint) {

		return hintsMapper.insert(hint);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.HintServices#getAllHints()
	 */
	@Override
	public List<Hints> getAllHints() {

		return hintsMapper.selectAll();
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.HintServices#getHintById(java.lang.Long)
	 */
	@Override
	public Hints getHintById(Long id) {

		return hintsMapper.selectByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.HintServices#updateHint(top.phrack.ctf.pojo.Hints)
	 */
	@Override
	public int updateHint(Hints hint) {
		
		return hintsMapper.updateByPrimaryKey(hint);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.HintServices#deleteById(java.lang.Long)
	 */
	@Override
	public int deleteById(Long id) {
		
		return hintsMapper.deleteByPrimaryKey(id);
	}

}
