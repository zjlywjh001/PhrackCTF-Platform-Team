/**
 * 
 */
package top.phrack.ctf.pojo;

/**
 * 提交列表的POJO类
 *
 * @author Jarvis
 * @date 2016年4月17日
 */
public class SubmissionList extends Submissions {
	
	private String taskname;
	
	private String user;
	
	private String teamname;
	
	public String gettaskname() {
		return taskname;
	}
	
	public void settaskname(String taskname) {
		this.taskname = taskname;
	}
	
	public String getuser(){
		return user;
	}
	
	public void setuser(String user) {
		this.user = user;
	}
	
	public String getteamname() {
		return teamname;
	}
	
	public void setteamname(String teamname) {
		this.teamname = teamname;
	}

}
