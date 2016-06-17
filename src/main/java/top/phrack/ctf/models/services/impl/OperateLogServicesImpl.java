/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.OperatelogMapper;
import top.phrack.ctf.models.services.OperateLogServices;
import top.phrack.ctf.pojo.Operatelog;

/**
 * 后台操作日志服务的实现类
 *
 * @author Jarvis
 * @date 2016年4月16日
 */
@Service("OperateLogServices")
public class OperateLogServicesImpl implements OperateLogServices{
	
	@Resource
	private OperatelogMapper operatelogMapper;

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.OperateLogServices#insertNewlog(top.phrack.ctf.pojo.Operatelog)
	 */
	@Override
	public int insertNewlog(Operatelog alog) {

		return operatelogMapper.insert(alog);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.OperateLogServices#getAllLogs()
	 */
	@Override
	public List<Operatelog> getAllLogs() {

		return operatelogMapper.selectAll();
	}
	
	
}
