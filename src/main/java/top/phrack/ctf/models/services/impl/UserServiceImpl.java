package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.UsersMapper;
import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Users;

@Service("UserServices")
public class UserServiceImpl implements UserServices{

	@Resource
	private UsersMapper usersMapper;
	
	public Users getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return usersMapper.selectByEmail(email);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.UserServices#insertNewUser(top.phrack.ctf.pojo.Users)
	 */
	public int insertNewUser(Users auser) {
		
		return usersMapper.insert(auser);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.UserServices#getUserByName(java.lang.String)
	 */
	public Users getUserByName(String username) {

		return usersMapper.selectByName(username);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.UserServices#updateUser(top.phrack.ctf.pojo.Users)
	 */
	public int updateUser(Users User) {
		
		return usersMapper.updateByPrimaryKey(User);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.UserServices#getUserById(java.lang.Long)
	 */
	public Users getUserById(Long id) {

		return usersMapper.selectByPrimaryKey(id);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.UserServices#getUsersForRank()
	 */
	public List<Users> getUsersForRank() {

		return usersMapper.selectUsersForRank();
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.UserServices#getAllUsers()
	 */
	@Override
	public List<Users> getAllUsers() {

		return usersMapper.SelectAll();
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.UserServices#deleteUserById(java.lang.Long)
	 */
	@Override
	public int deleteUserById(Long id) {

		return usersMapper.deleteByPrimaryKey(id);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.UserServices#getAllUsersByTeamId(java.lang.Long)
	 */
	@Override
	public List<Users> getAllUsersByTeamId(Long teamid) {

		return usersMapper.selectAllUsersInTeamByTeamId(teamid);
	}
	
}
