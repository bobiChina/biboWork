package com.ligootech.webdtu.web.backend;

import com.ligootech.webdtu.entity.core.Corp;
import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.entity.core.Order;
import com.ligootech.webdtu.entity.core.SNInfo;
import com.ligootech.webdtu.service.dtu.CorpManager;
import com.ligootech.webdtu.service.dtu.DtuManager;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import com.ligootech.webdtu.service.order.SNManager;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.webdtu.util.EasyUiUtil;
import com.ligootech.webdtu.web.util.DateEditor;
import com.ligootech.webdtu.web.util.ShiroUtil;
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
     * 用户DTU数据
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getTableDataByUserId", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findDataByUserId(@RequestParam(value = "rows", defaultValue = "20") int rows, //每页数据量
                           @RequestParam(value = "page", defaultValue = "1") int page,//开始页码位置
                           @RequestParam(value = "sort", defaultValue = "") String sort,//排序字段
                           @RequestParam(value = "order", defaultValue = "") String order, //升降序标识 DESC asc
                           @RequestParam(value = "userId", defaultValue = "") String userId //用户ID
    ){
        EasyUiUtil.PageForData pageObj = dtuManager.findDTUByUserId(rows, page, sort, order, userId);
        String jsonStr = JSONObject.fromObject(pageObj).toString();
        return jsonStr;
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
            DtuUser dtuuser = dtuUserManager.get(corp.getOptUser());
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
        List<Order> orderList = orderManager.findOrderList(corpId);
        if (orderList.size() > 0){
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

    /**
     * 删除用户DTU
     * @param userId
     * @param dtuId
     * @return
     */
    @RequestMapping(value = "/delUserDtu", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delUserDtu(@RequestParam(value = "userId", defaultValue="") String userId,
                             @RequestParam(value = "ids", defaultValue = "") String ids){
        //int rs = dtuManager.deluserDtu(userId, dtuId, ShiroUtil.getCurrentUser().getId());//单个删除
        int rs = dtuManager.deluserDtu(userId, ids, ShiroUtil.getCurrentUser().getId());
        return rs + "";
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
        dtuUser.setOptTime(new Date());
        dtuUser.setOptUser(ShiroUtil.getCurrentUser().getId());
        dtuUser.setOptUserName(ShiroUtil.getCurrentUser().getName());

        dtuUserManager.saveDtuUser(dtuUser, ShiroUtil.getCurrentUser().getId());

        return dtuUser.getId() + "";
    }

    /**
     * 订单信息
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/getOrderInfo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderByOrderNo(@RequestParam(value = "orderno") String orderno){
        Order order = orderManager.findInfoByOrderNo(orderno);
        String rsStr = "-1";
        if (order != null) {
            JSONObject orderJson = JSONObject.fromObject(order);
            JSONArray jsonResult = new JSONArray();
            jsonResult.add(orderJson);
            //录单人姓名
            Map<String, String> userMap = new HashMap<String, String>();
            userMap.put("username", order.getOptUserName());
            userMap.put("opttime", DateEditor.DATEFORMAT.format(order.getOptTime()));
            userMap.put("opttime_edit",DateEditor.TIMEFORMAT.format(order.getOptTime()));
            JSONObject optUsername = JSONObject.fromObject(userMap);
            jsonResult.add(optUsername);

            rsStr = jsonResult.toString();
        }
        return rsStr;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
    }
}
