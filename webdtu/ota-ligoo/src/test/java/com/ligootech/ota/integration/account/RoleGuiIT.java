package com.ligootech.ota.integration.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.entity.data.AccountData;
import com.ligootech.ota.integration.BaseSeleniumTestCase;
import com.ligootech.ota.integration.Gui;
import com.ligootech.ota.integration.Gui.RoleColumn;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springside.modules.utils.Collections3;

/**
 * 权限组管理的功能测试,测 试页面JavaScript及主要用户故事流程.
 * 
 * @author liand
 */
public class RoleGuiIT extends BaseSeleniumTestCase {

	/**
	 * 查看权限组列表.
	 */
	@Test
	public void viewRoleList() {
		selenium2.click(By.linkText(Gui.MENU_SYS));
		selenium2.click(By.linkText(Gui.MENU_ROLE));
		WebElement table = selenium2.findElement(By.xpath("//table[@id='contentTable']"));
		assertEquals("管理员", selenium2.getTable(table, 1, RoleColumn.NAME.ordinal()));
		assertEquals("查看用戶,修改用户,查看角色,修改角色", selenium2.getTable(table, 1, RoleColumn.PERMISSIONS.ordinal()));
	}

	/**
	 * 创建权限组.
	 */
	@Test
	public void createRole() {
		selenium2.open("/account/roles");
		selenium2.click(By.linkText("创建角色"));

		//生成测试数据
		Role role = AccountData.getRandomRoleWithPermissions();

		//输入数据
		selenium2.type(By.id("name"), role.getName());

		List<WebElement> checkBoxes = selenium2.findElements(By.name("permissionList"));
		for (String permission : role.getPermissionList()) {
			for (WebElement checkBox : checkBoxes) {
				if (permission.equals(selenium2.getValue(checkBox))) {
					selenium2.check(checkBox);
				}
			}
		}

		selenium2.click(By.id("submit"));

		//校验结果
		assertTrue(selenium2.isTextPresent("创建权限组" + role.getName() + "成功"));
		verifyRole(role);
	}

	private void verifyRole(Role role) {
		selenium2.click(By.linkText(Gui.MENU_ROLE));
		selenium2.click(By.id("editLink-" + role.getName()));
		assertEquals(role.getName(), selenium2.getValue(By.id("name")));

		List<WebElement> checkBoxes = selenium2.findElements(By.name("permissionList"));
		for (String permission : role.getPermissionList()) {
			for (WebElement checkBox : checkBoxes) {
				if (permission.equals(selenium2.getValue(checkBox))) {
					assertTrue(selenium2.isChecked(checkBox));
				}
			}
		}

		List<String> uncheckPermissionList = Collections3.subtract(AccountData.getDefaultPermissionList(),
				role.getPermissionList());
		for (String permission : uncheckPermissionList) {
			for (WebElement checkBox : checkBoxes) {
				if (permission.equals(selenium2.getValue(checkBox))) {
					assertFalse(selenium2.isChecked(checkBox));
				}
			}
		}
	}
}
