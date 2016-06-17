/**
 * 
 */
package top.phrack.ctf.pojo;

/**
 * 团队成员的类
 *
 * @author Jarvis
 * @date 2016年4月23日
 */
public class TeamMember extends Users{

	private long solved;
	
	public long getsolved(){
		return solved;
	}
	
	public void setsolved(long solved){
		this.solved = solved;
	}
}
