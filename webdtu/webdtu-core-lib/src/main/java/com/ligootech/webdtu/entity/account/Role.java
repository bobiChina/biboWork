package com.ligootech.webdtu.entity.account;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.appdot.model.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.collect.Lists;

/**
 * 角色.
 * 
 * @author liand
 */
@Entity
@Table(name = "tw_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity {

	private String name;

	private List<String> permissionList = Lists.newArrayList();

	public Role() {
	}

	public Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "tw_role_permission", joinColumns = { @JoinColumn(name = "role_id") })
	@Column(name = "permission")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}

	@Transient
	public String getPermissionNames() {
		List<String> permissionNameList = Lists.newArrayList();
		for (String permission : permissionList) {
			permissionNameList.add(Permission.parse(permission).displayName);
		}
		return StringUtils.join(permissionNameList, ",");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Role [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
