/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.Categories;

/**
 * 题目类型的Services
 *
 * @author Jarvis
 * @date 2016年4月10日
 */

public interface CategoryServices {
	
	public List<Categories> selectAllCategory();
	
	public Categories selectById(Long id);

	public int insertNewCategory(Categories cate);
	
	public int deleteCategoryById(Long id);
	
	public int updateCategory(Categories cate);
	
}
