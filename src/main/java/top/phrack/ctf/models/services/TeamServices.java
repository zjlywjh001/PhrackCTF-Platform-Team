/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.Teams;

/**
 * 管理参赛队伍
 *
 * @author Jarvis
 * @date 2016年4月21日
 */
public interface TeamServices {
	public Teams getTeamById(Long id);
	
	public int createANewTeam(Teams ateam);
	
	public Teams getTeamByName(String teamname);
	
	public Teams getTeamByToken(String teamtoken);
	
	public int updateTeamInfo(Teams team);
	
	public int deleteTeamById(Long id);
	
	public List<Teams> getAllTeams();
}
