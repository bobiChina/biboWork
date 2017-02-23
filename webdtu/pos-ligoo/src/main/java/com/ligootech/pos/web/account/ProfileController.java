package com.ligootech.pos.web.account;

import javax.validation.Valid;

import com.ligootech.webdtu.service.account.ShiroUser;
import org.apache.shiro.SecurityUtils;
import com.ligootech.webdtu.entity.account.User;
import com.ligootech.webdtu.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户修改自己资料的Controller.
 * 
 * @author liand
 */
@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

	@Autowired
	private AccountManager accountManager;

	@RequestMapping(method = RequestMethod.GET)
	public String updateForm(Model model) {
		Long id = getCurrentUserId();
		model.addAttribute("user", accountManager.get(id));
		return "account/profile";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadUser") User user) {
		accountManager.save(user);
		updateCurrentUserName(user.getName());
		return "redirect:/";
	}

	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return accountManager.get(id);
		}
		return null;
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	/**
	 * 更新Shiro中当前用户的用户名.
	 */
	private void updateCurrentUserName(String userName) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.name = userName;
	}
}
