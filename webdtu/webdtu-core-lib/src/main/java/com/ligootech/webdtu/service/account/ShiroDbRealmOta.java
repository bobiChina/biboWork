package com.ligootech.webdtu.service.account;

import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 自实现用户与权限查询.
 * 密码用明文存储，因此使用默认 的SimpleCredentialsMatcher.
 */
public class ShiroDbRealmOta extends AuthorizingRealm {

	private AccountManager accountManager;
	private DtuUserManager dtuUserManager;


	@Autowired
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
	@Autowired
	public void setDtuUserManager(DtuUserManager dtuUserManager) {
		this.dtuUserManager = dtuUserManager;
	}
	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		DtuUser user = dtuUserManager.findBackenLoginByUserName(token.getUsername());
		if (user != null) {
			return new SimpleAuthenticationInfo(new ShiroUser(new Long(user.getId()), user.getUserName(), user.getFullName(),user.getIsAdmin()),
				user.getUserPass(), getName());
		}		
//		User user = accountManager.getUserByLoginName(token.getUsername());
//		if (user != null) {
//			return new SimpleAuthenticationInfo(new ShiroUser(new Long(user.getId()), user.getLoginName(), user.getName()),
//					user.getPassword(), getName());
//		}
		
		return null;

	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName()).iterator().next();
		if (shiroUser != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			//添加角色
			List<String> rolesList = dtuUserManager.findUserRoles(shiroUser.id);
			if (rolesList != null && rolesList.size()>0) {
				info.addRoles(rolesList);
			}
			//添加权限
			List<String> permissionList = dtuUserManager.findUserRoles(shiroUser.id);
			if (permissionList != null && permissionList.size()>0) {
				info.addRoles(permissionList);
			}
			return info;

			/*for (Role role : user.getRoleList()) {
				//基于Permission的权限信息
				info.addStringPermissions(role.getPermissionList());
			}*/

		}
		return null;
	}

	/**
	 * 更新用户授权信息缓存.
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

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
/*	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		public Long id;
		public String loginName;
		public String name;
		public int isAdmin;

		public ShiroUser(Long id, String loginName, String name,int isAdmin) {
			this.id = id;
			this.loginName = loginName;
			this.name = name;
			this.isAdmin = isAdmin;
		}

		public Long getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public int getIsAdmin() {
			return isAdmin;
		}

		public void setIsAdmin(int isAdmin) {
			this.isAdmin = isAdmin;
		}

		*//**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 *//*
		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this);
		}

		*//**
		 * 重载equals,只计算loginName;
		 *//*
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this, "loginName");
		}

		*//**
		 * 重载equals,只比较loginName
		 *//*
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj, "loginName");
		}
	}*/
}
