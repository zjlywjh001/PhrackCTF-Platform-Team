/**
 * 
 */
package top.phrack.ctf.models.services;

import top.phrack.ctf.pojo.Passreset;

/**
 * 密码重置服务
 *
 * @author Jarvis
 * @date 2016年4月9日
 */
public interface PassResetServices {
	public int insertNewtoken(Passreset passreset);
	
	public Passreset getResetRecordBytoken(String token);
	
	public int updateResetRecord(Passreset passreset);
}
