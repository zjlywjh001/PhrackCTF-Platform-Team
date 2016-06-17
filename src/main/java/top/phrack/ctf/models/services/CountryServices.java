/**
 * 
 */
package top.phrack.ctf.models.services;

import java.util.List;

import top.phrack.ctf.pojo.Countries;

/**
 * 国家接口
 *
 * @author Jarvis
 * @date 2016年4月8日
 */

public interface CountryServices {
	public List<Countries> SelectAllCountry(); 
	
	public Countries getCountryById(Long id);
	
	public Countries getCountryByCode(String code);
}
