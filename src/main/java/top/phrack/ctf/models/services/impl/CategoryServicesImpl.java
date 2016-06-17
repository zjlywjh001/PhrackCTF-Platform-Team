/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.CategoriesMapper;
import top.phrack.ctf.models.services.CategoryServices;
import top.phrack.ctf.pojo.Categories;

/**
 * 类别服务实现类
 *
 * @author Jarvis
 * @date 2016年4月10日
 */
@Service("CategoryServices")
public class CategoryServicesImpl implements CategoryServices{

	@Resource
	private CategoriesMapper categoriesMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.CategoryServices#selectAllCategory()
	 */
	public List<Categories> selectAllCategory() {

		return categoriesMapper.selectAll();
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.CategoryServices#selectById(java.lang.Long)
	 */
	public Categories selectById(Long id) {

		return categoriesMapper.selectByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.CategoryServices#insertNewCategory(top.phrack.ctf.pojo.Categories)
	 */
	@Override
	public int insertNewCategory(Categories cate) {

		return categoriesMapper.insert(cate);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.CategoryServices#deleteCategoryById(java.lang.Long)
	 */
	@Override
	public int deleteCategoryById(Long id) {

		return categoriesMapper.deleteByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.CategoryServices#updateCategory(top.phrack.ctf.pojo.Categories)
	 */
	@Override
	public int updateCategory(Categories cate) {

		return categoriesMapper.updateByPrimaryKey(cate);
	}
	
}
