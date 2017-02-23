package com.ligootech.ota.web;

import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.service.account.AccountManager;
import com.ligootech.ota.web.account.RoleDetailController;
import org.appdot.test.spring.BaseControllerTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springside.modules.test.security.shiro.ShiroTestUtils;

/**
 * 
 */

/**
 * @author Lian
 *
 */
public class RoleDetailControllerTest extends BaseControllerTestCase {

	@Autowired
	private RoleDetailController c = null;

	@Autowired
	private AccountManager accountManager;

	@Before
	public void setUp() {
		ShiroTestUtils.mockSubject("foo");
	}

	@After
	public void tearDown() {
		ShiroTestUtils.clearSubject();
	}

	@Test
	public void testSaveRole() {
		logger.debug("test save role");
		Role role = applicationContext.getBean(AccountManager.class).getRole(-2L);
		logger.debug("old role is [" + role + "]");
		role.setName("changedByMe");
		c.save(role, new RedirectAttributesModelMap());

		Role newRole = accountManager.getRole(-2L);
		logger.debug("updated role == [" + newRole + "]");
		Assert.assertEquals("newRole name is changedByMe", "changedByMe", newRole.getName());
	}
}
