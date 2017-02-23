/**
 * 
 */
package com.ligootech.webdtu.service.account;

import java.util.List;

import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.entity.account.User;
import org.appdot.service.GenericManager;

/**
 * 用户和角色的业务接口定义
 * 
 * @author Lian
 *
 */
public interface AccountManager extends GenericManager<User, Long> {

	/**
	 * 删除用户角色
	 * 
	 * @param id	角色主键
	 */
	void deleteRole(Long id);

	/**
	 * 保存或修改用户角色
	 * 
	 * @param entity	用户角色对象
	 */
	void saveRole(Role entity);

	/**
	 * 获取所有的角色
	 * 
	 * @return	所有角色对象
	 */
	List<Role> getAllRole();

	/**
	 * 获取单个角色
	 * 
	 * @param id	角色主键
	 * @return	角色实体
	 */
	Role getRole(Long id);

	/**
	 * 根据登录名获取用户实体对象, 并对用户的延迟加载关联进行初始化.
	 * 
	 * @param loginName	用户登录名
	 * @return	用户对象
	 */
	User getUserByLoginName(String loginName);

}
