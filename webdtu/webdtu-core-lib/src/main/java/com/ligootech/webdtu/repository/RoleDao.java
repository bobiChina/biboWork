package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.account.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 权限组（角色）对象的Dao interface.
 * 
 * @author liand
 */

public interface RoleDao extends PagingAndSortingRepository<Role, Long>, RoleDaoCustom {
}
