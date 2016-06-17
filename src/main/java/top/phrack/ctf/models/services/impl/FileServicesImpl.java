/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.FilesMapper;
import top.phrack.ctf.models.services.FileServices;
import top.phrack.ctf.pojo.Files;

/**
 * Files字段实现类
 *
 * @author Jarvis
 * @date 2016年4月11日
 */
@Service("FileServices")
public class FileServicesImpl implements FileServices {

	@Resource
	private FilesMapper filesMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.FileServices#getFilesByChallengeId(java.lang.Long)
	 */
	public List<Files> getFilesByChallengeId(Long id) {

		return filesMapper.SelectFilesByTaskId(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.FileServices#insertNewFileRecord(top.phrack.ctf.pojo.Files)
	 */
	@Override
	public int insertNewFileRecord(Files afile) {

		return filesMapper.insert(afile);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.FileServices#getFileNotAttachedByName(java.lang.String)
	 */
	@Override
	public Files getFileNotAttachedByName(String name) {
		
		return filesMapper.selectAFilesNotattachedByName(name);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.FileServices#getFileById(java.lang.Long)
	 */
	@Override
	public Files getFileById(Long id) {

		return filesMapper.selectByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.FileServices#updateFileInfo(top.phrack.ctf.pojo.Files)
	 */
	@Override
	public int updateFileInfo(Files file) {

		return filesMapper.updateByPrimaryKey(file);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.FileServices#deleteFileRecord(top.phrack.ctf.pojo.Files)
	 */
	@Override
	public int deleteFileRecordById(Long id) {

		return filesMapper.deleteByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.FileServices#AllFiles()
	 */
	@Override
	public List<Files> getAllFiles() {

		return filesMapper.SelectAll();
	}
	
}
