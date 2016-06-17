/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.TeamsMapper;
import top.phrack.ctf.models.services.TeamServices;
import top.phrack.ctf.pojo.Teams;

/**
 * 队伍查询服务的实现类
 *
 * @author Jarvis
 * @date 2016年4月21日
 */
@Service("TeamServices")
public class TeamServicesImpl implements TeamServices{

	
	@Resource 
	private TeamsMapper teamsMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.TeamServices#getTeamById(java.lang.Long)
	 */
	@Override
	public Teams getTeamById(Long id) {
		return teamsMapper.selectByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.TeamServices#createANewTeam(top.phrack.ctf.pojo.Teams)
	 */
	@Override
	public int createANewTeam(Teams ateam) {

		return teamsMapper.insert(ateam);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.TeamServices#getTeamByName(java.lang.String)
	 */
	@Override
	public Teams getTeamByName(String teamname) {

		return teamsMapper.selectByTeamName(teamname);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.TeamServices#getTeamByToken(java.lang.String)
	 */
	@Override
	public Teams getTeamByToken(String teamtoken) {

		return teamsMapper.selectByTeamToken(teamtoken);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.TeamServices#updateTeamInfo(top.phrack.ctf.pojo.Teams)
	 */
	@Override
	public int updateTeamInfo(Teams team) {

		return teamsMapper.updateByPrimaryKey(team);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.TeamServices#deleteTeamById(java.lang.Long)
	 */
	@Override
	public int deleteTeamById(Long id) {

		return teamsMapper.deleteByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.TeamServices#getAllTeams()
	 */
	@Override
	public List<Teams> getAllTeams() {

		return teamsMapper.selectAll();
	}
	

}
