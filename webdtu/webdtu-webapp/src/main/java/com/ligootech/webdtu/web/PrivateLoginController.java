package com.ligootech.webdtu.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，

 * 真正登录的POST请求由Filter完成,
 * 
 * @author liand
 */
@Controller
@RequestMapping("/private/login*")
public class PrivateLoginController {

	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		return "account/login_backend";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, @RequestParam(FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM) String password, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
		} catch ( UnknownAccountException uae ) {
			model.addAttribute("errorMsg", "账号不存在");
		} catch ( IncorrectCredentialsException ice ) {
			model.addAttribute("errorMsg", "密码不正确");
			token.clear();
		} catch (LockedAccountException lae ) {
			System.out.println("账号锁定");
		} catch (ExcessiveAttemptsException eae ) {
			System.out.println("登录失败次数过多");
		}finally {
			token.clear();
			currentUser.logout();
			return "account/login_backend";
		}
	}

	@RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
	public String findUnauthorizedPage(Model model) {
		System.out.println("##########权限不足###########");
		/*Subject subject = SecurityUtils.getSubject();
		Boolean isBackend = subject.hasRole("admin");//判断是否为后台用户,如果是后台用户，则跳转到后台页面
		Boolean isProduct = subject.hasRole("product");//产品管理权限
		Boolean isOrder = subject.hasRole("order");//订单管理权限
		Boolean isCustomer = subject.hasRole("customer");//客户管理权限(包括订单管理)
		if (isBackend || isProduct || isOrder || isCustomer){
			model.addAttribute("init_menu", "backend_403");
		}*/
		return "account/unauthorized";
	}

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String findsuccessPage() {
		Subject subject = SecurityUtils.getSubject();
		Boolean backendAdmin = subject.hasRole("dtuadmin");//DTU后台管理员
		if (backendAdmin){
			return "redirect:/customer/dtupage";
		}
		return "redirect:/overview/start";
	}

	@RequestMapping(value = "/loginOpt", method = RequestMethod.POST)
	public String loginOpt(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, @RequestParam(FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM) String password, Model model) {
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
		} catch ( UnknownAccountException uae ) {
			model.addAttribute("errorMsg", "账号不存在");
			token.clear();
			currentUser.logout();
			return "account/login_backend";
		} catch ( IncorrectCredentialsException ice ) {
			model.addAttribute("errorMsg", "密码不正确");
			token.clear();
			currentUser.logout();
			return "account/login_backend";
		} catch (LockedAccountException lae ) {
			System.out.println("账号锁定");
			token.clear();
			currentUser.logout();
			return "account/login_backend";
		} catch (ExcessiveAttemptsException eae ) {
			System.out.println("登录失败次数过多");
			token.clear();
			currentUser.logout();
			return "account/login_backend";
		}
		Boolean backendAdmin = currentUser.hasRole("dtuadmin");//DTU后台管理员
		if (backendAdmin){
			return "redirect:/customer/dtupage";
		}
		model.addAttribute("errorMsg", "权限不足");
		token.clear();
		currentUser.logout();
		return "account/login_backend";

	}

	@RequestMapping(value = "/test403page", method = RequestMethod.GET)
	public String findTest403Page() {
		return "account/test403page";
	}
}
