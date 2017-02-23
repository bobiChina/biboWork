package com.ligootech.webdtu.integration;

/**
 * 定义页面元素的常量.
 */
public class Gui {

	public static final String MENU_SYS = "系统管理";
	public static final String MENU_USER = "用户管理";
	public static final String MENU_ROLE = "角色管理";

	//定义表格内容，避免表格内容顺序变动引起case的大崩溃。
	public enum UserColumn {
		LOGIN_NAME, NAME, EMAIL, GROUPS, OPERATIONS
	}

	public enum RoleColumn {
		NAME, PERMISSIONS, OPERATIONS
	}
}
