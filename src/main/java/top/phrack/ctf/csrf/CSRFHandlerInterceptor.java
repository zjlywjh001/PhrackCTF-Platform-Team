/**
 * 
 */
package top.phrack.ctf.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Request预处理器，先过滤csrf然后才允许访问对应controller
 *
 * @author Jarvis
 * @date 2016年4月7日
 */
public class CSRFHandlerInterceptor implements HandlerInterceptor{

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (!request.getMethod().equalsIgnoreCase("POST")) {
			return true;
		} else {
			if (request.getServletPath().equals("/admin/fileUpload")){  //排除文件上传的拦截
				return true;
			}
			HttpSession session = request.getSession();
			String sessionToken = CSRFTokenManager.getCurrentSessionToken(session);
			if (sessionToken==null) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF Token Expired!!");
				return false;
			}
			String requestToken = CSRFTokenManager.getTokenFromRequest(request);
			CSRFTokenManager.createNewTokenForSession(request.getSession());
			if (requestToken!=null && sessionToken.equals(requestToken)) {
				return true;
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing CSRF value !!");
				return false;
			}
		}
		//return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
