/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.NewsMapper;
import top.phrack.ctf.models.services.NewsServices;
import top.phrack.ctf.pojo.News;

/**
 *
 *
 * @author Jarvis
 * @date 2016年3月31日
 */
@Service("NewsServices")
public class NewsServiceImpl implements NewsServices{

	@Resource
	private NewsMapper newsMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.NewsServices#getNewsById(long)
	 */
	public News getNewsById(long id) {
		return newsMapper.selectByPrimaryKey(id);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.NewsServices#createNews(java.lang.String, java.lang.String, java.util.Date)
	 */
	public int createNews(String title, String content, Date posttime) {
		// TODO Auto-generated method stub
		News aNews = new News();
		aNews.setTitle(title);
		aNews.setContent(content);
		aNews.setPosttime(posttime);
		return newsMapper.insert(aNews);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.NewsServices#selectAll()
	 */
	public List<News> selectAll() {
		// TODO Auto-generated method stub
		return newsMapper.selectAll();
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.NewsServices#updateNews(long, java.lang.String, java.lang.String)
	 */
	public int updateNews(News newNews) {
		// TODO Auto-generated method stub
		return newsMapper.updateByPrimaryKey(newNews);
	}

	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.NewsServices#deleteNewsById(long)
	 */
	public int deleteNewsById(long id) {
		// TODO Auto-generated method stub
		return newsMapper.deleteByPrimaryKey(id);
	}
	
}
