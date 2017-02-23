package com.ligootech.webdtu.repository;

/**
 * RoleDao的扩展行为interface.
 */
public interface RoleDaoCustom {

	/**
	 * 因为Role中没有建立与User的关联,因此需要以较低效率的方式进行删除User与Role的多对多中间表中的数据.
	 */
	void deleteWithReference(Long id);

}
