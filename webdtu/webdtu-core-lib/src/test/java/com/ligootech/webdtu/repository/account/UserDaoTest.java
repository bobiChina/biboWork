package com.ligootech.webdtu.repository.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.ligootech.webdtu.entity.account.User;
import com.ligootech.webdtu.entity.data.AccountData;
import com.ligootech.webdtu.repository.UserDao;
import org.appdot.test.dao.BaseDaoJpaTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserDao的测试用例, 测试ORM映射及特殊的DAO操作.
 * 
 * 默认在每个测试函数后进行回滚.
 * 
 * @author liand
 */
public class UserDaoTest extends BaseDaoJpaTestCase {

	@Autowired
	private UserDao entityDao;

	@Test
	//如果你需要真正插入数据库,将Rollback设为false
	//@Rollback(false) 
	public void crudEntityWithRole() {
		//新建并保存带权限组的用户
		User user = AccountData.getRandomUserWithOneRole();
		entityDao.save(user);
		flush();

		//获取用户
		user = entityDao.findOne(user.getId());
		assertEquals(1, user.getRoleList().size());

		//删除用户的权限组
		user.getRoleList().remove(0);
		entityDao.save(user);
		flush();

		user = entityDao.findOne(user.getId());
		assertEquals(0, user.getRoleList().size());

		//删除用户
		entityDao.delete(user.getId());
		flush();

		user = entityDao.findOne(user.getId());
		assertNull(user);
	}

	//期望抛出ConstraintViolationException的异常.
	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public void savenUserNotUnique() {
		User user = AccountData.getRandomUser();
		user.setLoginName("admin");
		entityDao.save(user);
		flush();
	}
}