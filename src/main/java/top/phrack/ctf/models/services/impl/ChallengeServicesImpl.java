/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.ChallengesMapper;
import top.phrack.ctf.models.services.ChallengeServices;
import top.phrack.ctf.models.services.ConfigServices;
import top.phrack.ctf.pojo.Challenges;
import top.phrack.ctf.utils.CommonUtils;

/**
 * 赛题的服务实现类
 *
 * @author Jarvis
 * @date 2016年4月10日
 */
@Service("ChallengeServices")
public class ChallengeServicesImpl implements ChallengeServices{

	@Resource
	private ChallengesMapper challengesMapper;
	@Resource
	private ConfigServices configServices;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ChallengeServices#getAllChallenges()
	 */
	public List<Challenges> getAllAvailChallenges() {
		
		List<Challenges> allchallenges = challengesMapper.selectAll();
		ArrayList<Challenges> availchalls = new ArrayList<Challenges>();
		Date currenttime = new Date();
		for (Challenges ch:allchallenges) {
			if (ch.getExposed() && currenttime.after(ch.getAvailable())){
				Date globalstart = CommonUtils.getStartDate(configServices);
				if (globalstart!=null && currenttime.before(globalstart)) {
					continue;
				}
				availchalls.add(ch);
			}
		}
		return availchalls;
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ChallengeServices#updateChallenge(top.phrack.ctf.pojo.Challenges)
	 */
	public int updateChallenge(Challenges challenge) {

		return challengesMapper.updateByPrimaryKey(challenge);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ChallengeServices#getChallengeById(java.lang.Long)
	 */
	public Challenges getChallengeById(Long id) {

		return challengesMapper.selectByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ChallengeServices#getChallengeByCateId(java.lang.Long)
	 */
	@Override
	public Long getCateScoreByCateId(Long cateid) {
		
		List<Challenges> catechalls = challengesMapper.selectChallengesByCateId(cateid);
		long score = 0;
		Date currenttime = new Date();
		for (Challenges ch:catechalls) {
			if (ch.getExposed() && currenttime.after(ch.getAvailable())){
				score += ch.getScore();
			}
		}
		return score;
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ChallengeServices#getAllChallenges()
	 */
	@Override
	public List<Challenges> getAllChallenges() {
		
		return challengesMapper.selectAll();
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ChallengeServices#createNewChallenge(top.phrack.ctf.pojo.Challenges)
	 */
	@Override
	public int createNewChallenge(Challenges challenge) {

		return challengesMapper.insert(challenge);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ChallengeServices#getAllChallengesInCateById(java.lang.Long)
	 */
	@Override
	public List<Challenges> getAllChallengesInCateById(Long id) {

		return challengesMapper.selectByCategoryId(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.ChallengeServices#deleteChallengeById(java.lang.Long)
	 */
	@Override
	public int deleteChallengeById(Long id) {

		return challengesMapper.deleteByPrimaryKey(id);
	}

}
