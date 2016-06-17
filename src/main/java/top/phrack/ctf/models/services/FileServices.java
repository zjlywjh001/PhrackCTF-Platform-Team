/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.Files;

/**
 * 对应数据库Files字段
 * 
 * @author Jarvis
 * @date 2016年4月11日
 */
public interface FileServices {
	public List<Files> getFilesByChallengeId(Long id);
	
	public int insertNewFileRecord(Files afile);
	
	public Files getFileNotAttachedByName(String name);
	
	public Files getFileById(Long id);
	
	public int updateFileInfo(Files file);
	
	public int deleteFileRecordById(Long id);
	
	public List<Files> getAllFiles();
}
