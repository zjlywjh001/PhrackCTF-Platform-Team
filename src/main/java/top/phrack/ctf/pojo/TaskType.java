/**
 * 
 */
package top.phrack.ctf.pojo;


import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Jarvis
 * @date 2016年4月10日
 */
public class TaskType {
	
	private long id;
	
	private String title;

	private String stat;
	
	private String cate;
	
	private String catestyle;
	
	private boolean isnew;
	
	private long score;
	
	private String in;
	
	private long solvenum;
	
	private boolean solved;
	
	private String content;
	
	private boolean fin;
	
	private List<TaskHint> hints;
	
	private List<Attaches> attach;
	
	
	public long getid() {
		return id;
	}
	
	public void setid(long id) {
		this.id = id;
	}
	
	public String gettitle() {
		return title;
	}
	
	public void settitle(String title) {
		this.title = title;
	}
	
	public String getstat() {
		return stat;
	}
	
	public void setstat(String stat) {
		this.stat = stat;
	}
	
	public String getcate() {
		return cate;
	}
	
	public void setcate(String cate) {
		this.cate = cate;
	}
	
	public String getcatestyle() {
		return catestyle;
	}
	
	public void setcatestyle(String catestyle) {
		this.catestyle = catestyle;
	}
	
	public boolean getisnew() {
		return isnew;
	}
	
	public void setisnew(boolean isnew) {
		this.isnew = isnew;
	}
	public long getscore() {
		return score;
	}
	
	public void setscore(long score) {
		this.score = score;
	}
	public String getin() {
		return in;
	}
	
	public void setin(String in) {
		this.in = in;
	}
	public long getsolvenum() {
		return solvenum;
	}
	
	public void setsolvenum(long solvenum) {
		this.solvenum = solvenum;
	}
	public List<TaskHint> gethints() {
		return hints;
	}
	
	public void sethints(List<TaskHint> hints) {
		this.hints = hints;
	}
	
	public String getcontent(){
		return content;
	}

	public void setcontent(String content) {
		this.content = content;
	}
	public List<Attaches> getattach() {
		return attach;
	}
	
	public void setattach(ArrayList<Attaches> attach) {
		this.attach = attach;
	}
	
	public boolean getsolved() {
		return solved;
	}
	
	public void setsolved(boolean solved) {
		this.solved = solved;
	}
	
	public boolean getfin() {
		return fin;
	}
	
	public void setfin(boolean fin) {
		this.fin = fin;
	}
	
	
}
