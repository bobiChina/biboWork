package com.ligootech.webdtu.entity.account;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Resource Base Access Control中的资源定义.
 * 
 * @author liand
 */
public enum Permission {

	USER_VIEW("users:view", "查看用戶"), USER_EDIT("users:edit", "修改用户"),

	ROLE_VIEW("roles:view", "查看角色"), ROLE_EDIT("roles:edit", "修改角色");

	private static Map<String, Permission> valueMap = Maps.newHashMap();

	public String value;
	public String displayName;

	static {
		for (Permission permission : Permission.values()) {
			valueMap.put(permission.value, permission);
		}
	}

	Permission(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public static Permission parse(String value) {
		return valueMap.get(value);
	}

	public String getValue() {
		return value;
	}

	public String getDisplayName() {
		return displayName;
	}
}
