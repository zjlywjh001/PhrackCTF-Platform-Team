/**
 * 
 */
package top.phrack.ctf.pojo;

import java.util.List;

/**
 * 分数页面展示用的对象
 *
 * @author Jarvis
 * @date 2016年4月12日
 */
public class ScoreBoardObj {
	
	private long rank;
	
	private long userid;
	
	private String username;
	
	private String countrycode;
	
	private String countryname;
	
	private List<SolveList> solvestat;
	
	private long score;
	
	public long getrank() {
		return rank;
	}
	
	public void setrank(long rank) {
		this.rank = rank;
	}
	
	public long getuserid() {
		return userid;
	}
	
	public void setuserid(long userid) {
		this.userid = userid;
	}
	
	public String getusername(){
		return username;
		
	}
	
	public void setusername(String username){
		this.username = username;
	}
	
	public String getcountrycode() {
		return countrycode;
	}
	
	public void setcountrycode(String countrycode) {
		this.countrycode = countrycode;
	}
	
	public String getcountryname() {
		return countryname;
	}
	
	public void setcountryname(String countryname) {
		this.countryname = countryname;
	}
	
	public List<SolveList> getsolvestat() {
		return solvestat;
	}
	
	public void setsolvestat(List<SolveList> solvestat) {
		this.solvestat = solvestat;
	}
	
	public long getscore(){
		return score;
	}
	
	public void setscore(long score) {
		this.score = score;
	}
}
