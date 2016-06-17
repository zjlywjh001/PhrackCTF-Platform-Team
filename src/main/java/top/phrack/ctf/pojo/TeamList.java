/**
 * 
 */
package top.phrack.ctf.pojo;

import java.util.Date;

/**
 * 后台team列表
 *
 * @author Jarvis
 * @date 2016年5月23日
 */
public class TeamList extends Teams{

	public TeamList(Teams initteam) {
		this.setCountryid(initteam.getCountryid());
		this.setCreatetime(initteam.getCreatetime());
		this.setDescription(initteam.getDescription());
		this.setId(initteam.getId());
		this.setIsenabled(initteam.getIsenabled());
		this.setName(initteam.getName());
		this.setOrganization(initteam.getOrganization());
		this.setScore(initteam.getScore());
		this.setTeamtoken(initteam.getTeamtoken());
	}
	
	private Date LastSubmit;
	
	private String creator;
	
	private long ips;
	
	private String countrycode;
	
	private String countryname;
	
	public Date getLastSubmit() {
		return LastSubmit;
	}
	
	public void setLastSubmit(Date LastSubmit) {
		this.LastSubmit = LastSubmit;
	}
	
	public String getcreator() {
		return creator;
	}
	
	public void setcreator(String creator) {
		this.creator = creator;
	}
	
	public long getips() {
		return ips;
	}
	
	public void setips(long ips) {
		this.ips = ips;
	}
	
	public String getcountrycode(){
		return countrycode;
	}
	
	public void setcountrycode(String countrycode){
		this.countrycode = countrycode;
	}
	
	public String getcountryname() {
		return countryname;
	}
	
	public void setcountryname(String countryname) {
		this.countryname = countryname;
	}
}
