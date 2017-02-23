package com.ligootech.webdtu.integration;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.test.selenium.Selenium2;
import org.springside.modules.test.selenium.WebDriverFactory;

/**
 * 使用Selenium的功能测试基类.
 * 
 * 在整个测试期间仅启动一次Selenium.
 * 
 * @author liand
 */
public class BaseSeleniumTestCase {

	protected static Selenium2 selenium2;

	private static final Logger logger = LoggerFactory.getLogger(BaseSeleniumTestCase.class);

	@BeforeClass
	public static void init() throws Exception {
		createSeleniumOnce();
		loginAsAdminIfNecessary();
	}

	/**
	 * 创建Selenium，仅创建一次.
	 */
	protected static void createSeleniumOnce() throws Exception {
		if (selenium2 == null) {
			//根据配置创建Selenium driver.
			String driverName = System.getProperty("selenium.driver");
			String baseUrl = System.getProperty("baseUrl");
			if (driverName.isEmpty() || baseUrl.isEmpty()) {
				logger.debug("driverName=={}, baseUrl=={}", driverName, baseUrl);
				throw new RuntimeException("driverName and baseUrl can not be null.");
			}

			WebDriver driver = WebDriverFactory.createDriver(driverName);
			selenium2 = new Selenium2(driver, System.getProperty("baseUrl"));
			selenium2.setStopAtShutdown();
		}
	}

	/**
	 * 登录管理员, 如果用户还没有登录.
	 */
	protected static void loginAsAdminIfNecessary() {
		selenium2.open("/mainMenu");
		System.out.println("title ====" + selenium2.getTitle());
		if ("AppDot:登录页".equals(selenium2.getTitle())) {
			System.out.println("============ 登陆页 ...");
			selenium2.type(By.name("username"), "admin");
			selenium2.type(By.name("password"), "admin");
			selenium2.check(By.name("rememberMe"));
			selenium2.click(By.id("submit_btn"));
			assertEquals("AppDot:首页", selenium2.getTitle());
			System.out.println("============ 首页 ...");
		}
	}

}
