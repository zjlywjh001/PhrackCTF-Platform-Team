/**
 * 
 */
package top.phrack.ctf.models.services;

import top.phrack.ctf.pojo.Rules;

/**
 * 规则页面数据接口
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
public interface RuleServices {
	public Rules getRulesById(long id);
	
	public int updateRule(Rules newRule);
	
}
