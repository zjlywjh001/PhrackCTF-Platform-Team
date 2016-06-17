/**
 * 
 */
package top.phrack.ctf.pojo;

import java.util.Date;

/**
 * 用户信息页面通过的赛题列表对象
 *
 * @author Jarvis
 * @date 2016年4月14日
 */
public class SolveTable {
	
	private long id;
	
	private String title;
	
	private String catemark;
	
	private String catename;
	
	private long score;
	
	private Date passtime;
	
	private String solvedby;
	
	public long getid() {
		return id;
	}
	
	public void setid(long id) {
		this.id = id;
	}
	public String gettitle(){
		return title;
	}
	
	public void settitle(String title) {
		this.title = title;
	}
	
	public String getcatemark(){
		return catemark;
	}
	
	public void setcatemark(String catemark) {
		this.catemark = catemark;
	}
	
	public long getscore(){
		return score;
	}
	
	public void setscore(long score) {
		this.score = score;
	}
	
	public String getcatename(){
		return catename;
	}
	
	public void setcatename(String catename) {
		this.catename = catename;
	}
	
	public String getsolvedby(){
		return solvedby;
	}
	
	public void setsolvedby(String solvedby) {
		this.solvedby = solvedby;
	}
	
	public Date getpasstime(){
		return passtime;
	}
	
	public void setpasstime(Date passtime) {
		this.passtime = passtime;
	}
	
	
}
