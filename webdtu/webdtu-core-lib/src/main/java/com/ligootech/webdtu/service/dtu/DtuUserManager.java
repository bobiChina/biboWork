package com.ligootech.webdtu.service.dtu;

import org.appdot.service.GenericManager;

import com.ligootech.webdtu.entity.core.DtuUser;

import java.util.List;
import java.util.Map;

public interface DtuUserManager extends GenericManager<DtuUser, Long>{

	public DtuUser findByUserName(String userName);

	public DtuUser findByUserId(Long userId);

	/**
	 * 登录用户信息
	 * @param userName
	 * @return
	 */
	public DtuUser findLoginByUserName(String userName);

	/**
	 * 获取后台用户
	 * @param userName
	 * @return
	 */
	public DtuUser findBackenLoginByUserName(String userName);

	/**
	 * 获取用户下属客户
	 * @param userId
	 * @param isAdmin
	 * @return
	 */
	public List<Object[]> findDtuUserList(int isAdmin, Long userId );


	/**
	 * 获取后台用户
	 * @return
	 */
	public List<Object[]> findBackendUserList();

	/**
	 * 获取下级用户数量，以用户账号为主键
	 * @return
	 */
	public Map<String, Integer> findNextUserCount();

	/**
	 * 获取联系人信息, corpId 公司ID，optUserId 操作人ID
	 * @param isAdmin
	 * @param corpId
	 * @param optUserId
	 * @return
	 */
	public List<Object[]> findLinkUserList(int isAdmin, Long corpId, Long optUserId );

	/**
	 * 修改用户名
	 * @param fullName
	 * @param userId
	 * @return
	 */
	public int updateDtuUserFullName(String fullName, Long userId);

	/**
	 * 添加关联关系,type 0-关联用户， 1-创建用户
	 * @param userId
	 * @param userName
	 * @param optUserId
	 * @param type
	 * @return
	 */
	public int addUserRelation(Long userId, String userName, Long optUserId, int type);

	/**
	 * 删除用户,以及关联用户，admin用户为删除用户，并解除该用户的关联
	 * @param isAdmin
	 * @param userId
	 * @param optUserId
	 * @return
	 */
	public int delUserRelation(int isAdmin, Long userId, Long optUserId);

	/**
	 * 添加DTU关联关系
	 * @param userId
	 * @param dtuIds
	 * @param optUserId
	 * @param isAdmin
	 * @return
	 */
	public int addDtuUserConfig(Long userId, String dtuIds, Long optUserId, int isAdmin);

	/**
	 * 删除DTUuser关联关系
	 * @param userId
	 * @param uuid
	 * @param optUserId
	 * @return
	 */
	public int delDtuUserConfig(Long userId, String uuid, Long optUserId);

	/**
	 * 获取上级用户ID
	 * @param userId
	 * @param dtuId
	 * @return
	 */
	public Long getUpUserId(Long userId, Long dtuId);

	/**
	 * 获取用户角色
	 * @param userId
	 * @return
	 */
	public List<String> findUserRoles(Long userId);

	/**
	 * 获取用户权限
	 * @param userId
	 * @return
	 */
	public List<String> findUserPermission(Long userId);

	/**
	 * 获取用户设备数量
	 * @param userId
	 * @return
	 */
	public Long findDtuCountBUserId(Long userId);

	/**
	 * 获取用户订单数量
	 * @param userId
	 * @return
	 */
	public Long findOrderCountBUserId(Long userId);

	/**
	 * 更新后台用户权限信息
	 * @param dtuUser
	 * @param permissionIds
	 * @return
	 */
	public int updatePermission(DtuUser dtuUser, String permissionIds, Long optUserId);

	/**
	 * 获取用户权限名称
	 * @param userId
	 * @return
	 */
	public List<Map<String, String>> findUserPermissionName(Long userId);

	/**
	 * 修改用户密码
	 * @param userpass
	 * @param userId
	 * @param optUserId
	 * @return
	 */
	public int updatePass(String userpass, Long userId, Long optUserId);

	/**
	 * 用户信息保存
	 * @param dtuUser
	 * @param optUserId
	 */
	public void saveDtuUser(DtuUser dtuUser, Long optUserId);

	/**
	 * 删除用户账号
	 * @param userId
	 * @param optUserId
	 * @return
	 */
	public int delUser(Long userId, Long optUserId);

	/**
	 * OTA删除用户
	 * @param userId
	 * @param optUserId
	 * @return
	 */
	public int delUser4OTA(Long userId, Long optUserId);

	/**
	 * 获取子用户
	 * @param userId
	 * @return
	 */
	public List<Object[]> findSubUserList(Long userId);

	/**
	 * 获取后台用户角色
	 * @return
	 */
	public List<Object[]> findBackendRoleList();

	/**
	 * 获取不同人员类型用户  type=1 销售代表 type=2 技术代表
	 * @param type
	 * @return
	 */
	public List<Object[]> findUserByType(int type);

	/**
	 * 获取角色用户
	 * @param role
	 * @return
	 */
	public List<Object[]> findUserByRole(String role);

}
