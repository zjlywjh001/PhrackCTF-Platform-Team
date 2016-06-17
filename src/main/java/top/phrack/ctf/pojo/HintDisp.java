/**
 * 
 */
package top.phrack.ctf.pojo;

import java.util.Date;

/**
 * 用于在后台展示用的Hint呈现类
 *
 * @author Jarvis
 * @date 2016年4月17日
 */
public class HintDisp {
	
	private long id;
	
	private String challname;
	
	private Date adddate;
	
	private String content;
	
	public long getid() {
		return id;
	}
	
	public void setid(long id){
		this.id = id;
	}
	
	public String getchallname() {
		return challname;
	
	}
	
	public void setchallname(String challname) {
		this.challname = challname;
	} 
	
	public Date getadddate(){
		return adddate;
	}
	
	public void setadddate(Date adddate) {
		this.adddate = adddate;
	}
	
	public String getcontent(){
		return content;
	}
	
	public void setcontent(String content) {
		this.content = content;
	} 
}
