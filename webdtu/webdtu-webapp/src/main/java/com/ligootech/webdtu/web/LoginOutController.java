package com.ligootech.webdtu.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/logout")
public class LoginOutController {

	@RequestMapping(method = RequestMethod.GET)
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		Boolean backendAdmin = subject.hasRole("dtuadmin");//DTU后台管理员
		subject.logout();
		if (backendAdmin){
			return "redirect:private/login";
		}
		return "redirect:/login";
	}

}
