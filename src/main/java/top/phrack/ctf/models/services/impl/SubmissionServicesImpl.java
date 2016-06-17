/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.SubmissionsMapper;
import top.phrack.ctf.models.services.SubmissionServices;
import top.phrack.ctf.pojo.Submissions;

/**
 * 对应表submissions服务实现类
 * 
 * @author Jarvis
 * @date 2016年4月11日
 */
@Service("SubmissionServices")
public class SubmissionServicesImpl implements SubmissionServices {

	
	@Resource
	private SubmissionsMapper submissionsMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getAllCorrectSubmitByTaskId(java.lang.Long)
	 */
	public List<Submissions> getAllCorrectSubmitByTaskId(Long taskid) {

		return submissionsMapper.getCorrectSubmitBytaskId(taskid);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getAllSubmitByUserId(java.lang.Long)
	 */
	public List<Submissions> getAllSubmitByUserId(Long userid) {

		return submissionsMapper.getAllSubmitByUserId(userid);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#insertNewSubmission(top.phrack.ctf.pojo.Submissions)
	 */
	public int insertNewSubmission(Submissions aSubmission) {

		return submissionsMapper.insert(aSubmission);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getLastCorrectSubmitByUserId(java.lang.Long)
	 */
	public Submissions getLastCorrectSubmitByUserId(Long userid) {

		return submissionsMapper.selectLastCorrectSubmitByUserId(userid);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getSolvedByUseridAndTaskId(java.lang.Long, java.lang.Long)
	 */
	public Long getSolvedByUseridAndTaskId(Long userid, Long taskid) {

		return submissionsMapper.countSolvedByUseridAndTaskId(userid, taskid);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getLastSubmitByUserId(java.lang.Long)
	 */
	public Submissions getLastSubmitByUserId(Long userid) {

		return submissionsMapper.selectLastSubmitByUserId(userid);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getCorrectSubmitByUserid(java.lang.Long)
	 */
	@Override
	public List<Submissions> getCorrectSubmitByUserid(Long userid) {

		return submissionsMapper.getCorrectSubmitByUserid(userid);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getCorrectByUserIdAndTaskId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Submissions getCorrectByUserIdAndTaskId(Long userid, Long taskid) {

		return submissionsMapper.selectCorrectByUseridAndTaskId(userid, taskid);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getAllSubmissions()
	 */
	@Override
	public List<Submissions> getAllSubmissions() {

		return submissionsMapper.selectAll();
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getSubmissionById(java.lang.Long)
	 */
	@Override
	public Submissions getSubmissionById(Long id) {

		return submissionsMapper.selectByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#updateSubmission(top.phrack.ctf.pojo.Submissions)
	 */
	@Override
	public int updateSubmission(Submissions newsub) {

		return submissionsMapper.updateByPrimaryKey(newsub);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#deleteSubmission(java.lang.Long)
	 */
	@Override
	public int deleteSubmission(Long id) {

		return submissionsMapper.deleteByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getsubmitTimesByUserIdAndTaskId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Long getsubmitTimesByUserIdAndTaskId(Long userid, Long taskid) {

		return submissionsMapper.countSubmitByUserIdAndTaskId(userid, taskid);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getAllCorrectAndOrderByUserId()
	 */
	@Override
	public List<Submissions> getAllCorrectAndOrderByUserId() {

		return submissionsMapper.SelectCorrectOrderByUserId();
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.SubmissionServices#getAllCorrectOrderByTime()
	 */
	@Override
	public List<Submissions> getAllCorrectOrderByTime() {

		return submissionsMapper.SelectCorrectOrderByTime();
	}
	
	
}
