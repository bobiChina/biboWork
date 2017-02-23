package com.ligootech.webdtu.web.account;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springside.modules.utils.Collections3;


/**
 * 用于转换用户表单中复杂对象Role的checkbox的关联。
 * 
 * @author liand
 */
@Component
public class RoleListEditor extends PropertyEditorSupport {

	@Autowired
	private AccountManager accountManager;

	/**
	 * Back From Page
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String[] ids = StringUtils.split(text, ",");
		List<Role> roles = new ArrayList<Role>();
		for (String id : ids) {
			Role role = accountManager.getRole(Long.valueOf(id));
			roles.add(role);
		}
		setValue(roles);
	}

	/**
	 * Set to page
	 */
	@Override
	public String getAsText() {
		return Collections3.extractToString((List) getValue(), "id", ",");
	}
}
