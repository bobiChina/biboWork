package com.ligootech.pos.web.backend;

import com.ligootech.webdtu.entity.core.Corp;
import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.entity.core.Order;
import com.ligootech.webdtu.entity.core.SNInfo;
import com.ligootech.webdtu.service.dtu.CorpManager;
import com.ligootech.webdtu.service.dtu.DtuManager;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import com.ligootech.webdtu.service.order.SNManager;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.pos.web.util.DateEditor;
import com.ligootech.pos.web.util.ShiroUtil;
import com.ligootech.webdtu.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wly on 2015/11/7 9:43.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    DtuUserManager dtuUserManager;

    @Autowired
    CorpManager corpManager;

    @Autowired
    OrderManager orderManager;

    @Autowired
    DtuManager dtuManager;

    @Autowired
    SNManager snManager;

    /**
     * 跳转到客户管理页面
     * @param model
     * @return
     */

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String forwardCustomer(Model model){

        List<Object[]> userList = corpManager.getCorpList();
        model.addAttribute("userList", userList);
        DtuUser user = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
        model.addAttribute("user", user);
        return "backend/customer/customerPage";
    }

    /**
     * DTU管理页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/dtupage", method = RequestMethod.GET)
    public String forwardDtupage(Model model){

        List<Object[]> userList = dtuUserManager.findDtuUserList(2, ShiroUtil.getCurrentUser().getId());
        model.addAttribute("userList", userList);
        List<Object[]> corpList = corpManager.getCorpListNoUser();
        model.addAttribute("corpList", corpList);
        DtuUser user = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
        model.addAttribute("user", user);
        return "backend/customer/dtuUserPage";
    }

    /**
     * 重新获取左侧客户列表
     * @return
     */
    @RequestMapping(value = "/getCorpLi", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findCorpLiAjax(@RequestParam(value = "userId") Long userId){
        if(userId == null || userId == 0){
            userId =  ShiroUtil.getCurrentUser().getId();
        }

        List<Object[]> list = corpManager.getCorpList();
        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    @RequestMapping(value = "/getUserLi", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findUserLiAjax(@RequestParam(value = "userId") Long userId){
        if(userId == null || userId == 0){
            userId =  ShiroUtil.getCurrentUser().getId();
        }

        List<Object[]> list = dtuUserManager.findDtuUserList(2, ShiroUtil.getCurrentUser().getId());
        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    /**
     * 获取客户信息
     * @param userid
     * @return
     */
    @RequestMapping(value = "/getuser", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String vehicleGetByUuidAjax(@RequestParam(value = "userid") Long userid){

        DtuUser dtuUser = dtuUserManager.get(userid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dtuUser", dtuUser);

        List<Object[]> subList = dtuUserManager.findSubUserList(userid);

        map.put("subList", subList);

        return JSONObject.fromObject(map).toString();
    }

    @RequestMapping(value = "/getCorpBoxvalue", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findCorpBoxvalue(){
        List<Object[]> corpList = corpManager.getCorpListNoUser();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (corpList.size() > 0 ){
            for (int i = 0; i < corpList.size(); i++) {
                Object[] obj = corpList.get(i);
                if (obj != null && obj.length>1){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("corpid", obj[0]);
                    map.put("corpname", obj[1]);
                    list.add(map);
                }
            }
        }
        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    @RequestMapping(value = "/getOrderBoxvalue", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrdernoBoxvalue(@RequestParam(value = "userId") Long userId){
        List<String> orderList = orderManager.findOrderNoByUserId(userId);

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (orderList.size() > 0 ){
            for (int i = 0; i < orderList.size(); i++) {
                String str = orderList.get(i);
                if (!"".equals(str)){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("orderno", str);
                    map.put("ordername", str);
                    list.add(map);
                }
            }
        }
        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    @RequestMapping(value = "/getAllDtuByUserId", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findAllDtuByUserId(@RequestParam(value = "userId") Long userId){
        List<Object[]> dtuList = dtuManager.findDtuByUserId(userId);
        String rsStr = JSONArray.fromObject(dtuList).toString();
        return rsStr;
    }

    /**
     * 获取订单设备
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/getOrderSNList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findSNListByOrderNo(@RequestParam(value = "orderno") String orderno){
        List<SNInfo> list = snManager.findSNNoDtuByOrderNo(orderno);
        return JSONArray.fromObject(list).toString();
    }

    /**
     * 添加设备
     * @param snids
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sn2dtu", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String sn2DTU(@RequestParam(value = "orderno") String orderno, @RequestParam(value = "snids") String snids, @RequestParam(value = "userId") Long userId){
        int rs = snManager.sn2DTU(orderno, snids, userId, ShiroUtil.getCurrentUser().getId());
        return rs + "";
    }

    /**
     * 客户信息
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/getCorpInfo", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findCorpAjax(@RequestParam(value = "corpId") Long corpId){

        Map<String, Object> map = new HashMap<String, Object>();
        Corp corp = corpManager.get(corpId);
        map.put("corp", corp);

        if (null != corp){
            DtuUser dtuuser = dtuUserManager.get(StringUtil.StringToLong(StringUtil.null2String(corp.getOptUser())));
            if (dtuuser != null) {
                map.put("userName", dtuuser.getFullName());
            }
        }
        List<Object[]> accountList = corpManager.getCorpAccount(corpId);
        map.put("accountList", accountList);

        JSONObject jsonObject = JSONObject.fromObject(map);
        String rsStr = jsonObject.toString();
        return rsStr;
    }

    /**
     * 保存客户信息
     * @param corp
     * @return
     */
    @RequestMapping(value = "/saveCorp", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveCorp(Corp corp){

        if (corp.getId() == null) {
            corp.setOptTime(new Date());
            corp.setOptUser(ShiroUtil.getCurrentUser().getId());
        }
        /******************************
         * 添加判断客户名称是否重复
         *****************************/
        int checkCorpName = corpManager.checkCorpName(corp.getCorpName(), corp.getId());
        if(checkCorpName == -1){
            return "-1";
        }

        corpManager.saveCorp(corp, ShiroUtil.getCurrentUser().getId());
        return corp.getId() + "";
    }

    /**
     * 删除客户信息
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/delCorp", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delCorp(@RequestParam(value = "corpId") Long corpId){

        //有订单的不能删除
        int orderCount = orderManager.findCountByCorpId(String.valueOf(corpId));
        if (orderCount > 0){
            return "-1";
        }
        //有账号的不能删除
        List<Object[]> accountList = corpManager.getCorpAccount(corpId);
        if (accountList.size() > 0){
            return "-2";
        }

        corpManager.delCorp(corpId, ShiroUtil.getCurrentUser().getId());
        return "1";
    }

    @RequestMapping(value = "/delUserDtu", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delUserDtu(@RequestParam(value = "userId") Long userId, @RequestParam(value = "dtuId") Long dtuId){

       /* //有订单的不能删除
        List<Order> orderList = orderManager.findOrderList(corpId);
        if (orderList.size() > 0){
            return "-1";
        }
        //有账号的不能删除
        List<Object[]> accountList = corpManager.getCorpAccount(corpId);
        if (accountList.size() > 0){
            return "-2";
        }

        corpManager.delCorp(corpId, ShiroUtil.getCurrentUser().getId());*/



        return "1";
    }

    /**
     * 删除账号信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/delDtuUser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delDtuUser(@RequestParam(value = "userId") Long userId){

        //有DTU信息 不能删除
        long dtuNum = dtuManager.getDtuCountByUserid(userId);
        if (dtuNum > 0){
            return "-1";
        }

        dtuUserManager.delUser(userId, ShiroUtil.getCurrentUser().getId());
        return "1";
    }

    /**
     * 账号密码修改
     * @param userpass
     * @param userid
     * @return
     */
    @RequestMapping(value = "/updatePass", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String updatePass(@RequestParam(value = "userpass") String userpass, @RequestParam(value = "userid") Long userid){

        int rs = dtuUserManager.updatePass(userpass, userid, ShiroUtil.getCurrentUser().getId());

        return "1";
    }

    /**
     * 添加DTU用户
     * @param userpass
     * @param username
     * @param corpid
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String addUser(@RequestParam(value = "userpass") String userpass,
                          @RequestParam(value = "username") String username,
                          @RequestParam(value = "corpid") Long corpid){

        Corp corp = corpManager.get(corpid);

        DtuUser dtuUser = new DtuUser();
        dtuUser.setCorp(corp);
        dtuUser.setEmail(corp.getLinkEmail());
        dtuUser.setFullName(corp.getCorpName());
        dtuUser.setIsAdmin(0);
        dtuUser.setRelation(corp.getLinkPhone());
        dtuUser.setUserName(username);
        dtuUser.setUserPass(userpass);

        dtuUserManager.saveDtuUser(dtuUser, ShiroUtil.getCurrentUser().getId());

        return dtuUser.getId() + "";
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
    }
}
