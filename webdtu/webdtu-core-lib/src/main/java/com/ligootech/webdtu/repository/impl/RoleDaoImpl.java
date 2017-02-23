package com.ligootech.webdtu.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.entity.account.User;
import com.ligootech.webdtu.repository.RoleDaoCustom;
import org.springframework.stereotype.Component;


/**
 * RoleDao的扩展行为实现类.
 */
@Component
public class RoleDaoImpl implements RoleDaoCustom {

	private static final String QUERY_USER_BY_ROLEID = "select u from User u left join u.roleList g where g.id=?";

	@PersistenceContext
	private EntityManager em;

	@Override
	public void deleteWithReference(Long id) {
		//因為Role中沒有与User的关联，只能用笨办法，查询出拥有该角色的用户, 并删除该用户的角色.
		Role role = em.find(Role.class, id);
		List<User> users = em.createQuery(QUERY_USER_BY_ROLEID).setParameter(1, id).getResultList();
		for (User u : users) {
			u.getRoleList().remove(role);
		}
		em.remove(role);
	}

}
