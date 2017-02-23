package com.ligootech.pos.web.account;

import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.ligootech.webdtu.entity.account.User;
import com.ligootech.webdtu.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Urls:
 * List   page        : GET  /account/users/
 * Create page        : GET  /account/users/create
 * Create action      : POST /account/users/save
 * Update page        : GET  /account/users/update/{id}
 * Update action      : POST /account/users/save/{id}
 * Delete action      : POST /account/users/delete/{id}
 * CheckLoginName ajax: GET  /account/users/checkLoginName?oldLoginName=a&loginName=b
 * 
 * @author liand
 *
 */
@Controller
@RequestMapping(value = "/account/users")
public class UserController {

	private AccountManager accountManager;

	@Autowired
	private RoleListEditor roleListEditor;

	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(List.class, "roleList", roleListEditor);
		b.setDisallowedFields("id");

	}

	@RequiresPermissions("users:view")
	@RequestMapping(value = { "list", "" })
	public String list(Model model) {
		List<User> users = accountManager.getAll();
		model.addAttribute("users", users);
		return "account/userList";
	}

	@RequiresPermissions("users:edit")
	@RequestMapping(value = "create")
	public String createForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("allRoles", accountManager.getAllRole());
		return "account/userForm";
	}

	@RequiresPermissions("users:edit")
	@RequestMapping(value = "save")
	public String save(User user, RedirectAttributes redirectAttributes) {
		accountManager.save(user);
		redirectAttributes.addFlashAttribute("message", "创建用户" + user.getLoginName() + "成功");
		return "redirect:/account/users/";
	}

	@RequiresPermissions("users:edit")
	@RequestMapping(value = "update/{id}")
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", accountManager.get(id));
		model.addAttribute("allRoles", accountManager.getAllRole());
		return "account/userForm";
	}

	@RequiresPermissions("users:edit")
	@RequestMapping(value = "save/{id}")
	public String update(@Valid @ModelAttribute("preloadUser") User user, RedirectAttributes redirectAttributes) {
		accountManager.save(user);
		redirectAttributes.addFlashAttribute("message", "修改用户" + user.getLoginName() + "成功");
		return "redirect:/account/users/";
	}

	@RequiresPermissions("users:edit")
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		accountManager.delete(id);
		redirectAttributes.addFlashAttribute("message", "删除用户成功");
		return "redirect:/account/users/";
	}

	@RequiresPermissions("users:edit")
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("oldLoginName") String oldLoginName,
			@RequestParam("loginName") String loginName) {
		if (loginName.equals(oldLoginName)) {
			return "true";
		} else if (accountManager.getUserByLoginName(loginName) == null) {
			return "true";
		}

		return "false";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return accountManager.get(id);
		}
		return null;
	}

	@Autowired
	@Qualifier("accountManager")
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}
