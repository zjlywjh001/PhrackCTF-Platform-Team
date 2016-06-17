/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.RulesMapper;
import top.phrack.ctf.models.services.RuleServices;
import top.phrack.ctf.pojo.Rules;

/**
 * 规则页面实现方法
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
@Service("RuleServices")
public class RuleServicesImpl implements RuleServices{

	@Resource
	private RulesMapper rulesMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.RuleServices#getRulesById(long)
	 */
	public Rules getRulesById(long id) {
		return rulesMapper.selectByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.RuleServices#updateRule(top.phrack.ctf.pojo.Rules)
	 */
	@Override
	public int updateRule(Rules newRule) {

		return rulesMapper.updateByPrimaryKey(newRule);
	}

}
