package com.ligootech.ota.integration.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.entity.account.User;
import com.ligootech.webdtu.entity.data.AccountData;
import com.ligootech.ota.integration.BaseSeleniumTestCase;
import com.ligootech.ota.integration.Gui;
import com.ligootech.ota.integration.Gui.UserColumn;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springside.modules.utils.Collections3;

/**
 * 用户管理的功能测试, 测试页面JavaScript及主要用户故事流程.
 * 
 * @author liand
 */
public class UserGuiIT extends BaseSeleniumTestCase {

	/**
	 * 查看用户列表.
	 */
	@Test
	public void viewUserList() {
		selenium2.click(By.linkText(Gui.MENU_SYS));
		selenium2.click(By.linkText(Gui.MENU_USER));
		WebElement table = selenium2.findElement(By.id("contentTable"));
		assertTrue("用户表应该有数据", selenium2.getTable(table, 1, UserColumn.LOGIN_NAME.ordinal()).length() > 0);
		assertTrue("用户表应该有数据", selenium2.getTable(table, 1, UserColumn.NAME.ordinal()).length() > 0);
	}

	/**
	 * 创建用户.
	 */
	@Test
	public void createUser() {
		//打开新增用户页面
		selenium2.click(By.linkText(Gui.MENU_USER));
		selenium2.click(By.linkText("创建用户"));

		//生成待输入的测试用户数据
		final User user = AccountData.getRandomUserWithOneRole();

		//输入数据
		selenium2.type(By.id("loginName"), user.getLoginName());
		selenium2.type(By.id("name"), user.getName());
		selenium2.type(By.id("password"), user.getPassword());
		selenium2.type(By.id("passwordConfirm"), user.getPassword());
		List<WebElement> checkBoxes = selenium2.findElements(By.name("roleList"));
		for (Role role : user.getRoleList()) {
			for (WebElement checkBox : checkBoxes) {
				if (String.valueOf(role.getId()).equals(selenium2.getValue(checkBox))) {
					selenium2.check(checkBox);
				}
			}

		}
		selenium2.click(By.id("submit_btn"));
		selenium2.waitForVisible(By.id("contentTable"), 10);
		//校验结果
		assertTrue(selenium2.isTextPresent("创建用户" + user.getLoginName() + "成功"));
		verifyUser(user);
	}

	/**
	 * 校验用户数据的工具函数.
	 */
	private void verifyUser(User user) {

		selenium2.click(By.id("editLink-" + user.getLoginName()));

		assertEquals(user.getLoginName(), selenium2.getValue(By.id("loginName")));
		assertEquals(user.getName(), selenium2.getValue(By.id("name")));

		List<WebElement> checkBoxes = selenium2.findElements(By.name("roleList"));
		for (Role role : user.getRoleList()) {
			for (WebElement checkBox : checkBoxes) {
				if (String.valueOf(role.getId()).equals(selenium2.getValue(checkBox))) {
					assertTrue(selenium2.isChecked(checkBox));
				}
			}
		}

		List<Role> uncheckRoleList = Collections3.subtract(AccountData.getDefaultRoleList(), user.getRoleList());
		for (Role role : uncheckRoleList) {
			for (WebElement checkBox : checkBoxes) {
				if (String.valueOf(role.getId()).equals(selenium2.getValue(checkBox))) {
					assertFalse(selenium2.isChecked(checkBox));
				}
			}

		}
	}

	/**
	 * 创建用户时的输入校验测试. 
	 */
	@Test
	public void inputInValidateUser() {
		//		selenium2.click(By.linkText(Gui.MENU_SYS));
		selenium2.waitForVisible(By.partialLinkText(Gui.MENU_USER), 10);
		System.out.println("========================================" + By.partialLinkText(Gui.MENU_USER));
		selenium2.click(By.partialLinkText(Gui.MENU_USER));
		selenium2.click(By.linkText("创建用户"));

		selenium2.type(By.id("loginName"), "admin");
		selenium2.type(By.id("name"), "");
		selenium2.type(By.id("password"), "a");
		selenium2.type(By.id("passwordConfirm"), "abc");
		selenium2.type(By.id("email"), "abc");

		selenium2.click(By.id("submit_btn"));

		assertEquals("用户登录名已存在", selenium2.getText(By.xpath("//*[@id=\"inputForm\"]/fieldset/div[2]/div/span")));
		assertEquals("必选字段", selenium2.getText(By.xpath("//fieldset/div[3]/div/span")));
		assertEquals("输入与上面相同的密码", selenium2.getText(By.xpath("//fieldset/div[5]/div/span")));
		assertEquals("请输入正确格式的电子邮件", selenium2.getText(By.xpath("//fieldset/div[6]/div/span")));
	}

}
