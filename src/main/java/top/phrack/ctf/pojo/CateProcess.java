/**
 * 
 */
package top.phrack.ctf.pojo;

/**
 * 按分类统计的进度
 *
 * @author Jarvis
 * @date 2016年4月14日
 */
public class CateProcess {
	
	private String name;
	
	private long proc;
	
	private long total;
	
	private double percent;
	
	private String style;
	
	private double percentall;
	
	public String getname() {
		return name;
	}
	
	public void setname(String name) {
		this.name = name;
	}
	
	public long getproc() {
		return proc;
	}
	
	public void setproc(long proc){
		this.proc = proc;
	}
	
	public long gettotal(){
		return total;
	}
	
	public void settotal(long total) {
		this.total = total;
	}
	
	public double getpercent() {
		return percent;
	}
	
	public void setpercent(double percent) {
		this.percent = percent;
	}
	
	public String getstyle() {
		return style;
	}
	
	public void setstyle(String style) {
		this.style = style;
	}
	
	public double getpercentall() {
		return percentall;
	} 
	
	public void setpercentall(double percentall) {
		this.percentall = percentall;
	}
	
	
}
