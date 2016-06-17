/**
 * 
 */
package top.phrack.ctf.pojo;

/**
 * 国家页面的排行榜
 *
 * @author Jarvis
 * @date 2016年4月14日
 */
public class CountryRank {
	
	private long id;
	
	private String name;
	
	private long rank;
	
	private long score;
	
	public long getid() {
		return id;
	}
	
	public void setid(long id) {
		this.id = id;
	}
	
	public String getname(){
		return name;
	}
	
	public void setname(String name) {
		this.name = name;
	}
	
	public long getrank() {
		return rank;
	}
	
	public void setrank(long rank) {
		this.rank = rank;
	}
	
	public long getscore() {
		return score;
	}
	
	public void setscore(long score) {
		this.score = score;
	}
}
