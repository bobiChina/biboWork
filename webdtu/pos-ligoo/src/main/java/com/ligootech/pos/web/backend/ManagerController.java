package com.ligootech.pos.web.backend;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ligootech.webdtu.entity.core.Dtu;
import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.service.dtu.BatteryModelManager;
import com.ligootech.webdtu.service.dtu.CorpManager;
import com.ligootech.webdtu.service.dtu.DtuManager;
import com.ligootech.webdtu.service.dtu.DtuUserManager;

@Controller
@RequestMapping("/manager")

public class ManagerController {
	@Autowired
	DtuUserManager dtuUserManager;
	
	@Autowired
	DtuManager dtuManager;
	
	@Autowired
	CorpManager corpManager;
	
	@Autowired
	BatteryModelManager batteryModelManager;
	
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String users(Model model){
    	List<DtuUser> result = dtuUserManager.getAll();
    	model.addAttribute("userList", result);
    	return "backend/user";
    }
    
    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String forwardUserAdd(Model model){
    	model.addAttribute("corps",corpManager.getAll());
    	return "backend/userAdd";
    }
    
    @RequestMapping(value = "/user/addpost", method = RequestMethod.POST)
    public String userAdd(DtuUser dtuUser,Model model){
    	dtuUserManager.save(dtuUser);
    	return "redirect:/manager/user";
    }
    
    @RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.GET)
    public String userDelete(@PathVariable(value = "userId") Long userId, Model model){
    	dtuUserManager.delete(userId);
    	return "redirect:/manager/user";
    }
    
    @RequestMapping(value = "/dtu", method = RequestMethod.GET)
    public String forwardDtu(Model model){
    	model.addAttribute("dtuList", dtuManager.getAll());
    	return "backend/dtu";
    }  
    
    @RequestMapping(value = "/dtu/add", method = RequestMethod.GET)
    public String forwardDtuAdd(Model model){
    	model.addAttribute("userList",dtuUserManager.getAll());
    	model.addAttribute("batteryList",batteryModelManager.getAll());
    	return "backend/dtuAdd";
    }
    
    @RequestMapping(value = "/dtu/addpost", method = RequestMethod.POST)
    public String dtuAdd(Dtu dtu,Model model){
    	dtu.setInsertTime(new Date());
    	dtu.setCity("未知");
    	dtu.setLatitude(0);
    	dtu.setLontitude(0);
    	dtu.setChargeStatus(2);
    	dtu.setOnlineStatus(0);
    	dtuManager.save(dtu);
    	return "redirect:/manager/dtu";
    }
    
    @RequestMapping(value = "/dtu/delete/{dtuId}", method = RequestMethod.GET)
    public String dtuDelete(@PathVariable(value = "dtuId") Long dtuId, Model model){
    	dtuManager.delete(dtuId);
    	return "redirect:/manager/dtu";
    }
}
