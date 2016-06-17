package top.phrack.ctf.realm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import top.phrack.ctf.models.services.UserServices;
import top.phrack.ctf.pojo.Users;

public class shiroDbRealm extends AuthorizingRealm {
	private Logger log = LoggerFactory.getLogger(shiroDbRealm.class);
	//private static final String ALGORITHM = "SHA256";

	@Autowired
	private UserServices userServices;
	
	public shiroDbRealm() {
		super();
	}
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Set<String> roleNames = new HashSet<String>();
		Users user = userServices.getUserByEmail(principals.toString());
	    roleNames.add(user.getRole());//添加角色。对应到index.jsp
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		Users user = userServices.getUserByEmail(token.getUsername());

		if (user!=null && user.getIsenabled()) {
			SimpleAuthenticationInfo ai= new SimpleAuthenticationInfo(user.getEmail(),user.getPassword(),ByteSource.Util.bytes(user.getSalt()),getName());
			return ai;
		} else {
			throw new AuthenticationException();
		}
		//return null;
	}
	
	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}


	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

}
