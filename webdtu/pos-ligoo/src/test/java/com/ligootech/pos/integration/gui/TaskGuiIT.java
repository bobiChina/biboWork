package com.ligootech.pos.integration.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ligootech.webdtu.entity.account.Task;
import com.ligootech.webdtu.entity.data.TaskData;
import com.ligootech.pos.integration.BaseSeleniumTestCase;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 任务管理的功能测试, 测试页面JavaScript及主要用户故事流程.
 * 
 * @author liand
 */
public class TaskGuiIT extends BaseSeleniumTestCase {

	/**
	 * 浏览用户列表.
	 */
	@Test
	public void viewTaskList() {
		selenium2.open("/tasks/");
		WebElement table = selenium2.findElement(By.id("contentTable"));
		assertEquals("How to write UnitTestCase right", selenium2.getTable(table, 0, 0));
	}

	/**
	 * 创建/更新/搜索/删除任务.
	 */
	@Test
	public void crudTask() {
		selenium2.open("/tasks/");

		// create
		selenium2.click(By.linkText("创建任务"));

		Task task = TaskData.randomTask();
		selenium2.type(By.id("task_title"), task.getTitle());
		selenium2.click(By.id("submit_btn"));

		assertTrue(selenium2.isTextPresent("创建任务成功"));

		// update
		selenium2.click(By.linkText(task.getTitle()));
		assertEquals(task.getTitle(), selenium2.getValue(By.id("task_title")));

		String newTitle = TaskData.randomTitle();
		selenium2.type(By.id("task_title"), newTitle);
		selenium2.click(By.id("submit_btn"));
		assertTrue(selenium2.isTextPresent("更新任务成功"));

		// search
		selenium2.type(By.name("search_LIKE_title"), newTitle);
		selenium2.click(By.id("search_btn"));
		assertEquals(newTitle, selenium2.getTable(By.id("contentTable"), 0, 0));

		// delete
		selenium2.click(By.linkText("删除"));
		assertTrue("没有成功消息", selenium2.isTextPresent("删除任务成功"));
	}

	@Test
	public void inputInValidateValue() {
		selenium2.open("/tasks/");
		selenium2.click(By.linkText("创建任务"));
		selenium2.click(By.id("submit_btn"));

		assertEquals("必选字段", selenium2.getText(By.xpath("//fieldset/div/div/span")));
	}

}
