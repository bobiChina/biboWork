package com.ligootech.pos.web;

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
 * @author liand
 */
@Controller
@RequestMapping("/login*")
public class LoginController {

	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		Subject subject = SecurityUtils.getSubject();
		boolean boo = subject.isAuthenticated(); // 已通过验证的
		if(boo){
			return "redirect:/login/success";
		}

		return "account/login";
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
		} catch (LockedAccountException lae ) {
			System.out.println("账号锁定");
		} catch (ExcessiveAttemptsException eae ) {
			System.out.println("登录失败次数过多");
		}finally {
			token.clear();
			return "account/login";
		}
	}

	@RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
	public String findUnauthorizedPage(Model model) {
		System.out.println("##########权限不足###########");
		Subject subject = SecurityUtils.getSubject();
		Boolean isBackend = subject.hasRole("admin");		//判断是否为后台用户,如果是后台用户，则跳转到后台页面
		Boolean isProduct = subject.hasRole("product");	//产品管理权限
		Boolean isOrder = subject.hasRole("order");			//订单管理权限
		Boolean isCustomer = subject.hasRole("customer");	//客户管理权限(包括订单管理)
		Boolean isEquipment = subject.hasRole("equipment");	//设备管理
		if (isBackend || isProduct || isOrder || isCustomer || isEquipment){
			model.addAttribute("init_menu", "backend_403");
		}
		return "account/unauthorized";
	}

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String findsuccessPage() {
		Subject subject = SecurityUtils.getSubject();
		Boolean isAdmin = subject.hasRole("admin");		//判断是否为后台用户,如果是后台用户，则跳转到后台页面
		Boolean isProduct = subject.hasRole("product");	//产品管理权限（现改为硬件管理员）
		Boolean isOrder = subject.hasRole("order");			//订单管理权限
		Boolean isCustomer = subject.hasRole("customer");	//客户管理权限(包括订单管理)
		Boolean isEquipment = subject.hasRole("equipment");	//设备管理
		Boolean isFw = subject.hasRole("fw");	//fw管理
		Boolean isSqa = subject.hasRole("sqa");	//sqa管理
		Boolean isSoftware = subject.hasRole("software");	//软件管理

		/**
		 * 调整默认显示页面
		 */
		if (isAdmin){
			return "redirect:/order/page";
		}
		else if(isOrder){
			return "redirect:/order/page";
		}
		else if(isSoftware){
			return "redirect:/order/orderSoftwarePage";
		}
		else if(isEquipment){
			return "redirect:/order/device";
		}
		else if(isFw){
			return "redirect:/order/orderLog";
		}
		else if(isSqa){
			return "redirect:/order/orderLog";
		}
		else if(isProduct){
			return "redirect:/product/page";
		}
		else{
			return "account/test403page";
		}
	}

	@RequestMapping(value = "/test403page", method = RequestMethod.GET)
	public String findTest403Page() {
		return "account/test403page";
	}
}
