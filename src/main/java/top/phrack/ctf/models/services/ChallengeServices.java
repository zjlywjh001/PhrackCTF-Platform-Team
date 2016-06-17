/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.Challenges;

/**
 * 所有赛题的服务接口
 *
 * @author Jarvis
 * @date 2016年4月10日
 */
public interface ChallengeServices {

	public List<Challenges> getAllAvailChallenges();
	
	public int updateChallenge(Challenges challenge);
	
	public Challenges getChallengeById(Long id);
	
	public Long getCateScoreByCateId(Long cateid);
	
	public List<Challenges> getAllChallenges();
	
	public int createNewChallenge(Challenges challenge);
	
	public List<Challenges> getAllChallengesInCateById(Long id);
	
	public int deleteChallengeById(Long id);
}
