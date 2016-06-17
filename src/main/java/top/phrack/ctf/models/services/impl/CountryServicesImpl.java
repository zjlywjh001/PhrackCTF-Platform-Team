/**
 * 
 */
package top.phrack.ctf.models.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import top.phrack.ctf.models.dao.CountriesMapper;
import top.phrack.ctf.models.services.CountryServices;
import top.phrack.ctf.pojo.Countries;

/**
 * 国家接口的实现
 *
 * @author Jarvis
 * @date 2016年4月8日
 */
@Service("CountryServices")
public class CountryServicesImpl implements CountryServices {

	@Resource
	private CountriesMapper countriesMapper;
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.CountryServices#SelectAllCountry()
	 */
	public List<Countries> SelectAllCountry() {
		return countriesMapper.SelectAll();
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.CountryServices#getCountryById(java.lang.Long)
	 */
	public Countries getCountryById(Long id) {

		return countriesMapper.selectByPrimaryKey(id);
	}
	/* (non-Javadoc)
	 * @see top.phrack.ctf.models.services.CountryServices#getCountryByCode(java.lang.String)
	 */
	@Override
	public Countries getCountryByCode(String code) {
		
		return countriesMapper.selectCountryByCode(code);
	}
	
}
