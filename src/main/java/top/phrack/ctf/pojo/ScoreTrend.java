/**
 * 
 */
package top.phrack.ctf.pojo;

/**
 * 趋势图的POJO类
 *
 * @author Jarvis
 * @date 2016年4月13日
 */
public class ScoreTrend {
	
	private long id;
	
	private String countrycode;
	
	private String countryname;
	
	private String[] tasktitlearr;
	
	private String username;
	
	private long userrank;
	
	private long[] timepoint;
	
	private long[] taskarr;
	
	private long[] scorearr;
	
	public long[] gettaskarr() {
		return taskarr;
	}
	
	public void settaskarr(long[] taskarr) {
		this.taskarr = taskarr;
	}
	
	public long[] getscorearr() {
		return scorearr;
	}
	
	public void setscorearr(long[] scorearr) {
		this.scorearr = scorearr;
	}
	
	public long[] gettimepoint() {
		return timepoint;
	}
	
	public void settimepoint(long[] timepoint) {
		this.timepoint = timepoint;
	}
	
	public String getusername() {
		return username;
	}
	
	public void setusername(String username) {
		this.username = username;
	}
	
	public long getuserrank(){
		return userrank;
	}
	
	public void setuserrank(long userrank){
		this.userrank = userrank;
	}
}
