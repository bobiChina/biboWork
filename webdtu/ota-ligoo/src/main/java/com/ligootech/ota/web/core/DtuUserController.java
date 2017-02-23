package com.ligootech.ota.web.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.repository.DtuUserDao;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import com.ligootech.ota.web.util.ShiroUtil;

@Controller
@RequestMapping("")
public class DtuUserController {

	@Autowired
	DtuUserDao dtuUserDao;
	
	@Autowired
	DtuUserManager dtuUserManager;
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
//	public String forwardLogin(){
//		return "";
//	}
//	
//	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
//	public String userLogin(@RequestParam("userName") String userName,@RequestParam("password") String password,Model model){
//		
//		return "";
//	}
	
	@RequestMapping(value = "/password")
	public String forwardPassword(Model model){
    	DtuUser user = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
    	model.addAttribute("user", user);		
		return "account/password";
	}
	
	@RequestMapping(value = "/userinfo")
	public String forwardUserInfo(Model model){
    	DtuUser user = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
    	model.addAttribute("user", user);		
		return "account/userinfo";
	}
	
	@RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	public String updatePassword(@RequestParam(value = "userId") Long userId,@RequestParam(value = "oldPass") String oldPass,
			@RequestParam(value = "newPass") String newPass){
		DtuUser dtuUser = dtuUserDao.getit(userId);
		if(!dtuUser.getUserPass().equals(oldPass))
			return "2";
		
		dtuUser.setUserPass(newPass);
		dtuUserDao.saveAndFlush(dtuUser);
		return "1";
	}
	
	@RequestMapping(value = "/user/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public String updateUser(@RequestParam(value = "id") Long id,@RequestParam(value = "fullName") String fullName,
			@RequestParam(value = "email") String email,@RequestParam(value = "relation") String relation){
		DtuUser dtuUser = dtuUserDao.getit(id);
		dtuUser.setFullName(fullName);
		dtuUser.setEmail(email);
		dtuUser.setRelation(relation);
		
		dtuUserDao.saveAndFlush(dtuUser);
		return "1";
	}
	
}
