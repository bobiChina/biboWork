package com.ligootech.ota.web;

import com.ligootech.ota.web.util.ShiroUtil;
import com.ligootech.webdtu.service.account.ShiroUser;
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
 * 权限不足处理
 * 
 * @author liand
 */
@Controller
@RequestMapping("/unauthorized*")
public class UnauthorizedController {

	@RequestMapping(method = RequestMethod.GET)
	public String findUnauthorizedPage(Model model) {
		Subject subject = SecurityUtils.getSubject();
		ShiroUser user = (ShiroUser) subject.getPrincipal();
		System.out.println("##########权限不足###########登录账号：" + user.getLoginName() + " 用户名：" + user.getLoginName() );
		Boolean isBackend = subject.hasRole("admin");		//判断是否为后台用户,如果是后台用户，则跳转到后台页面
		Boolean isProduct = subject.hasRole("product");	//产品管理权限
		Boolean isOrder = subject.hasRole("order");			//订单管理权限
		Boolean isCustomer = subject.hasRole("customer");	//客户管理权限(包括订单管理)
		if (isBackend || isProduct || isOrder || isCustomer){
			model.addAttribute("init_menu", "backend_403");
		}
		return "account/unauthorized";
	}
}
