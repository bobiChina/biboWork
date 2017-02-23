package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.account.User;
import org.appdot.repository.BaseJpaRepository;


/**
 * 用户对象的Dao interface.
 * 
 * @author liand
 */
public interface UserDao extends BaseJpaRepository<User, Long> {

	User findByLoginName(String loginName);
}
