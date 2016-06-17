/**
 * 
 */
package top.phrack.ctf.pojo;


/**
 * 用于显示的Hint POJO类
 *
 * @author Jarvis
 * @date 2016年4月11日
 */
public class TaskHint {
	
	private long label;
	
	private String content;
	
	public long getlabel(){
		return label;
	}
	
	public void setlabel(long label) {
		this.label = label;
	}
	
	public String getcontent(){
		return content;
	}
	
	public void setcontent(String content) {
		this.content = content;
	}

}
