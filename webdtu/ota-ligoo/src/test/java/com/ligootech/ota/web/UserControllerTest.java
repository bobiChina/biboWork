package com.ligootech.ota.web;

import com.ligootech.webdtu.entity.account.User;
import com.ligootech.webdtu.service.account.AccountManager;
import com.ligootech.ota.web.account.UserController;
import org.appdot.test.spring.BaseControllerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springside.modules.test.security.shiro.ShiroTestUtils;

public class UserControllerTest extends BaseControllerTestCase {

	private UserController userController;

	@Autowired
	private AccountManager accountManager;

	@Before
	public void setUp() {
		ShiroTestUtils.mockSubject("foo");
		userController = applicationContext.getBean(UserController.class);
	}

	@After
	public void tearDown() {
		ShiroTestUtils.clearSubject();
	}

	@Test
	public void testSaveUser() {
		User user = accountManager.get(-2L);
		user.setName("changed");
		userController.save(user, new RedirectAttributesModelMap());

	}
}
