/**
 * 
 */
package top.phrack.ctf.csrf;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * 过滤CSRF请求
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
public class CSRFRequestDataValueProcessor implements RequestDataValueProcessor{

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processAction(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
	 */
	public String processAction(HttpServletRequest request, String action, String httpMethod) {
		return action;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processFormFieldValue(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {

		return value;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.RequestDataValueProcessor#getExtraHiddenFields(javax.servlet.http.HttpServletRequest)
	 */
	public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
		Map<String,String> hiddenFields = new HashMap<String,String>();
		hiddenFields.put(CSRFTokenManager.CSRF_PARAM_NAME, CSRFTokenManager.createNewTokenForSession(request.getSession()));
		return hiddenFields;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processUrl(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	public String processUrl(HttpServletRequest request, String url) {
		System.out.println(url);
		return url;
	}

}
