package com.ligootech.pos.integration.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ligootech.pos.integration.BaseSeleniumTestCase;
import com.ligootech.pos.integration.Gui;
import com.ligootech.pos.integration.Gui.UserColumn;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * 系统安全控制的功能测试, 测试主要用户故事.
 * 
 * @author liand
 */
public class SecurityIT extends BaseSeleniumTestCase {

	/**
	 * 测试匿名用户访问系统时的行为.
	 */
	@Test
	public void checkAnonymous() {
		//访问退出登录页面,退出之前的登录
		selenium2.open("/logout");
		assertEquals("AppDot:登录页", selenium2.getTitle());

		//访问任意页面会跳转到登录界面
		selenium2.open("/account/users");
		assertEquals("AppDot:登录页", selenium2.getTitle());
	}

	/**
	 * 只有用户权限组的操作员访问系统时的受限行为.
	 */
	@Test
	public void checkUserPermission() {
		//访问退出登录页面,退出之前的登录
		selenium2.open("/logout");
		assertEquals("AppDot:登录页", selenium2.getTitle());

		//登录普通用户
		selenium2.type(By.name("username"), "user");
		selenium2.type(By.name("password"), "user");
		selenium2.click(By.id("submit_btn"));

		//校验用户权限组的操作单元格只有查看
		selenium2.click(By.linkText(Gui.MENU_SYS));
		selenium2.click(By.linkText(Gui.MENU_USER));
		assertEquals("", selenium2.getTable(By.id("contentTable"), 1, UserColumn.OPERATIONS.ordinal()));

		//强行访问无权限的url
		selenium2.open("/account/users/update/1");
		assertTrue(selenium2.getTitle().contains("403"));
		//重新退出
		selenium2.open("/logout");
	}
}
