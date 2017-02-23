package com.ligootech.webdtu.service.account;

import static org.junit.Assert.fail;

import com.ligootech.webdtu.repository.UserDao;
import com.ligootech.webdtu.service.ServiceException;
import com.ligootech.webdtu.service.impl.account.AccountManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springside.modules.test.security.shiro.ShiroTestUtils;


/**
 * SecurityEntityManager的测试用例, 测试Service层的业务逻辑.
 * 
 * @author liand
 */
public class AccountManagerMockTest {

	private AccountManagerImpl accountManager;
	@Mock
	private UserDao mockUserDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ShiroTestUtils.mockSubject("foo");

		accountManager = new AccountManagerImpl();
		accountManager.setUserDao(mockUserDao);
	}

	@After
	public void tearDown() {
		ShiroTestUtils.clearSubject();
	}

	@Test
	public void deleteUser() {
		//正常删除用户.
		accountManager.delete(-2L);

		//删除超级管理用户抛出异常.
		try {
			accountManager.delete(1L);
			fail("expected ServicExcepton not be thrown");
		} catch (ServiceException e) {
			//expected exception
		}
	}
}
