/**
 * 
 */
package top.phrack.ctf.pojo;

/**
 * 每个用户的每个题目状态
 *
 * @author Jarvis
 * @date 2016年4月12日
 */
public class SolveList {
	
	private long taskid;
	
	private String solvestr;
	
	private String tasktitle;
	
	public long gettaskid() {
		return taskid;
	}
	
	public void settaskid(long taskid) {
		this.taskid = taskid;
	}
	
	public String getsolvestr(){
		return solvestr;
	}
	
	public void setsolvestr(String solvestr) {
		this.solvestr = solvestr;
	}
	
	public String gettasktitle(){
		return tasktitle;
	}
	
	public void settakstitle(String tasktitle){
		this.tasktitle = tasktitle;
	}
}
