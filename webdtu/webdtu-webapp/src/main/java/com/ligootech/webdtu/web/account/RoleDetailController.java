package com.ligootech.webdtu.web.account;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.ligootech.webdtu.entity.account.Permission;
import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.service.account.AccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/account/roles/")
public class RoleDetailController {

	private static Logger logger = LoggerFactory.getLogger(RoleDetailController.class);

	@Autowired
	private AccountManager accountManager;

	@RequiresPermissions("roles:edit")
	@RequestMapping(value = "update/{id}")
	public String updateForm(Model model) {
		logger.debug("update model == {}", model);
		model.addAttribute("allPermissions", Permission.values());
		return "account/roleForm";
	}

	@RequiresPermissions("roles:edit")
	@RequestMapping(value = "save/{id}")
	public String save(@ModelAttribute("role") Role role, RedirectAttributes redirectAttributes) {
		logger.debug("role == {}", role);
		accountManager.saveRole(role);
		redirectAttributes.addFlashAttribute("message", "修改权限组" + role.getName() + "成功");
		return "redirect:/account/roles/";
	}

	@ModelAttribute("role")
	public Role getRole(@PathVariable("id") Long id) {
		return accountManager.getRole(id);
	}
}
