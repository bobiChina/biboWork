package com.ligootech.webdtu.web.account;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.ligootech.webdtu.entity.account.Permission;
import com.ligootech.webdtu.entity.account.Role;
import com.ligootech.webdtu.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/account/roles")
public class RoleController {

	@Autowired
	private AccountManager accountManager;

	@RequiresPermissions("roles:view")
	@RequestMapping(value = { "list", "" })
	public String list(Model model) {
		List<Role> roles = accountManager.getAllRole();
		model.addAttribute("roles", roles);
		return "account/roleList";
	}

	@RequiresPermissions("roles:edit")
	@RequestMapping(value = "create")
	public String createForm(Model model) {
		model.addAttribute("role", new Role());
		model.addAttribute("allPermissions", Permission.values());
		return "account/roleForm";
	}

	@RequiresPermissions("roles:edit")
	@RequestMapping(value = "save")
	public String save(Role role, RedirectAttributes redirectAttributes) {
		accountManager.saveRole(role);
		redirectAttributes.addFlashAttribute("message", "创建权限组" + role.getName() + "成功");
		return "redirect:/account/roles/";
	}

	@RequiresPermissions("roles:edit")
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		accountManager.deleteRole(id);
		redirectAttributes.addFlashAttribute("message", "删除权限组成功");
		return "redirect:/account/roles/";
	}
}
