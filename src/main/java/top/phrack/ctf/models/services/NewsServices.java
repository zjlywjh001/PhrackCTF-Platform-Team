/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.Date;
import java.util.List;

import top.phrack.ctf.pojo.News;

/**
 * 新闻的数据库访问操作
 *
 * @author Jarvis
 * @date 2016年3月31日
 */
public interface NewsServices {
	public News getNewsById(long id); //查询新闻
	
	public int createNews(String title,String content,Date posttime);
	
	public List<News> selectAll();
	
	public int updateNews(News newNews);
	
	public int deleteNewsById(long id);
	
	
}
