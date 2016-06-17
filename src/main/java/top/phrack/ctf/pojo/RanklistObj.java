/**
 * 
 */
package top.phrack.ctf.pojo;

import java.util.Date;

/**
 * 用户排名对象
 *
 * @author Jarvis
 * @date 2016年4月12日
 */
public class RanklistObj {
	private Users userobj;
	
	private Date LastSummit;
	
	public Users getuserobj() {
		return userobj;
	}
	
	public void setuserobj(Users userobj) {
		this.userobj = userobj;
	}
	
	public Date getLastSummit() {
		return LastSummit;
	}
	
	public void setLastSummit(Date LastSummit) {
		this.LastSummit = LastSummit;
	}
	
	
}
