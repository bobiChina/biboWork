package com.ligootech.webdtu.entity.data;

import java.util.List;

import com.ligootech.webdtu.entity.account.Permission;
import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.entity.account.User;
import org.springside.modules.test.data.RandomData;

import com.google.common.collect.Lists;

/**
 * Account相关实体测试数据生成.
 * 
 * @author liand
 */
public class AccountData {

	public static final String DEFAULT_PASSWORD = "123456";

	private static List<Role> defaultRoleList = null;

	private static List<String> defaultPermissionList = null;

	public static User getRandomUser() {
		String userName = RandomData.randomName("User");

		User user = new User();
		user.setLoginName(userName);
		user.setName(userName);
		user.setPassword(DEFAULT_PASSWORD);
		user.setEmail(userName + "@appdot.org.cn");

		return user;
	}

	public static User getRandomUserWithOneRole() {
		User user = getRandomUser();
		user.getRoleList().add(getRandomDefaultRole());
		return user;
	}

	public static Role getRandomRole() {
		Role role = new Role();
		role.setName(RandomData.randomName("Role"));
		return role;
	}

	public static Role getRandomRoleWithPermissions() {
		Role role = getRandomRole();
		role.getPermissionList().addAll(getRandomDefaultPermissionList());
		return role;
	}

	public static List<Role> getDefaultRoleList() {
		if (defaultRoleList == null) {
			defaultRoleList = Lists.newArrayList();
			defaultRoleList.add(new Role(-1L, "管理员"));
			defaultRoleList.add(new Role(-2L, "用户"));
		}
		return defaultRoleList;
	}

	public static Role getRandomDefaultRole() {
		return RandomData.randomOne(getDefaultRoleList());
	}

	public static List<String> getDefaultPermissionList() {
		if (defaultPermissionList == null) {
			defaultPermissionList = Lists.newArrayList();
			for (Permission permission : Permission.values()) {
				defaultPermissionList.add(permission.value);
			}
		}
		return defaultPermissionList;
	}

	public static List<String> getRandomDefaultPermissionList() {
		return RandomData.randomSome(getDefaultPermissionList());
	}
}
