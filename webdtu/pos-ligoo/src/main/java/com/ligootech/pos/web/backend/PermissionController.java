package com.ligootech.pos.web.backend;

import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.repository.OptLogDao;
import com.ligootech.webdtu.service.dtu.CorpManager;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import com.ligootech.pos.web.util.ShiroUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2015/11/7 9:43.
 */
@SuppressWarnings("ALL")
@Transactional
@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    DtuUserManager dtuUserManager;

    @Autowired
    CorpManager corpManager;

    @Autowired
    OptLogDao optLogDao;

    /**
     * 跳转到客户管理页面
     * @param model
     * @return
     */

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String forwardCustomer(Model model){

        List<Object[]> userList = dtuUserManager.findBackendUserList();
        model.addAttribute("userList", userList);
        DtuUser user = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
        model.addAttribute("user", user);
        //初始化用户角色
        List<Object[]> roleList = dtuUserManager.findBackendRoleList();
        model.addAttribute("roleList", roleList);

        return "backend/permission/permissionPage";
    }

    /**
     * 重新获取左侧客户列表
     * @return
     */
    @RequestMapping(value = "/getUserLi", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findUserLiAjax(){

        List<Object[]> list = dtuUserManager.findBackendUserList();
        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    /**
     * 添加用户
     * @param dtuUser
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String addUserAjax(
            DtuUser dtuUser, @RequestParam(value = "permissionIds", defaultValue = "") String permissionIds){

        //判断登录账号是否存在，已存在的不能添加
        DtuUser dtuUserDB = dtuUserManager.findByUserName(dtuUser.getUserName());
        if(null != dtuUserDB){
            return "-1";
        }

        DtuUser optUser = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
        dtuUser.setOptUser(optUser.getId());
        dtuUser.setOptUserName(optUser.getUserName());
        dtuUser.setOptTime(new Date());

        dtuUserManager.save(dtuUser);
        //dtuUserManager.addUserRelation(dtuUser.getId(), dtuUser.getFullName(), ShiroUtil.getCurrentUser().getId());//添加关联关系

        StringBuffer logStr = new StringBuffer();
        logStr.append("添加后台用户>>ID:").append(dtuUser.getId())
                .append("用户账号：").append(dtuUser.getUserName())
                .append("，用户名：").append(dtuUser.getFullName())
                .append("，密码：").append(dtuUser.getUserPass())
                .append("，联系电话：").append(dtuUser.getRelation())
                .append("，权限ID：").append(permissionIds);

        int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "AddUserInfo", logStr.toString());

        int permissionInt = dtuUserManager.updatePermission(dtuUser, permissionIds, ShiroUtil.getCurrentUser().getId());

        return "" + dtuUser.getId();
    }

    /**
     * 用户信息修改
     * @param accountInfo
     * @param linkInfo
     * @param dtuUser
     * @return
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String updateUserAjax(
            @RequestParam(value = "accountInfo") String accountInfo,
            @RequestParam(value = "linkInfo") String linkInfo,
            DtuUser dtuUser,
            @RequestParam(value = "permissionIds", defaultValue = "") String permissionIds	){
        DtuUser oldUser = dtuUserManager.get(dtuUser.getId());

        dtuUser.setOptTime(oldUser.getOptTime());
        dtuUser.setOptUser(oldUser.getOptUser());
        dtuUser.setOptUserName(oldUser.getOptUserName());

        dtuUserManager.save(dtuUser);
        StringBuffer logStr = new StringBuffer();
        logStr.append("后台用户修改>>原账户信息：").append(accountInfo).append("。原联系人信息：" + linkInfo + "，本次权限ID:" + permissionIds);
        int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "UpdateUserInfo", logStr.toString());
        int permissionInt = dtuUserManager.updatePermission(dtuUser, permissionIds, ShiroUtil.getCurrentUser().getId());
        return "";
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delUserAjax(
            @RequestParam(value = "userId") Long userId){

        int rs = dtuUserManager.delUser(userId, ShiroUtil.getCurrentUser().getId());

        return rs + "";
    }

    /**
     * 获取客户信息
     * @param userid
     * @return
     */
    @RequestMapping(value = "/getuser", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String vehicleGetByUuidAjax(@RequestParam(value = "userid") Long userid){
        Map<String, Object> map = new HashMap<String, Object>();

        DtuUser dtuUser = dtuUserManager.get(userid);

        map.put("userObj", dtuUser);
        //权限信息
        List<Map<String, String>> mapList = dtuUserManager.findUserPermissionName(userid);

        map.put("permissionList", mapList);

        String str = JSONObject.fromObject(map).toString();
        return str;
    }
}
