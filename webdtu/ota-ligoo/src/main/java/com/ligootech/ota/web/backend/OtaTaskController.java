package com.ligootech.ota.web.backend;

import com.ligootech.ota.web.mqtt.MqttSendUtil;
import com.ligootech.ota.web.util.ShiroUtil;
import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.webdtu.util.EasyUiUtil;
import com.ligootech.webdtu.util.FileOperateUtil;
import com.ligootech.webdtu.util.S19ToBin;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19.
 */
// @SuppressWarnings("ALL")
@Controller
@RequestMapping("/otaTask")
public class OtaTaskController {
    @Autowired
    private OrderManager orderManager;

    /**
     * 任务列表界面
     * @param model
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String forwardMainPage(Model model){
        /*List<Object[]> userList ;
        model.addAttribute("userList", userList);*/
        //String fileName = new S19ToBin().operateS19File("20160927143739853012", true);

        List<String> uuids = new ArrayList<>();
        uuids.add("uuidtest1");
        uuids.add("uuidtest2");
        uuids.add("uuidtest3");
        uuids.add("uuidtest4");
        uuids.add("uuidtest5");
        /*new MqttSendUtil(uuids, "2016092901010101");
        new MqttSendUtil(uuids, "2016092901010101");
        new MqttSendUtil(uuids, "2016092901010101");*/
        return "task/otaTaskPage";
    }

    /**
     * 新增任务界面
     * @param model
     * @return
     */
    @RequiresRoles(logical = Logical.OR, value = {"admin", "admin"}) //多个角色 "或"关系示例
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public String forwardAddPage(Model model){

        model.addAttribute("orderList", orderManager.findOrder4OTA());
        return "task/otaTaskAddPage";
    }

    /**
     * 订单信息
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/findOrderInfo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderInfo(@RequestParam(value = "orderno", defaultValue = "") String orderno //订单编号
    ){
        Map<String, Object> map = orderManager.findCOrderByOrderNo(orderno);
        if (map == null) {
            return "-1";
        }
        map.put("order_sh", orderManager.findSHOrderList4OTA(orderno));
        String jsonStr = JSONObject.fromObject(map).toString();
        return jsonStr;
    }

    /**
     * 升级所选设备保存
     * @param orderno
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping(value = "/saveTaskDevice", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveTaskDevice(@RequestParam(value = "orderno", defaultValue = "") String orderno, //订单编号
                                 @RequestParam(value = "devices", defaultValue = "") String devices, //设备型号ID
                                 @RequestParam(value = "order_sw", defaultValue = "") String order_sw //软件订单号
    ){
        System.out.println(String.format("orderno=%s    devices=%s    order_sw=%s", orderno, devices, order_sw ));
        //orderno=LG201608090302    devices=387,    order_sw=LG-SH-2016080904
        Map<String, Object> map = orderManager.findCOrderByOrderNo(orderno);
        if (map == null) {
            return "-1";
        }
        map.put("order_sh", orderManager.findSHOrderList4OTA(orderno));
        String jsonStr = JSONObject.fromObject(map).toString();
        return jsonStr;
    }

    /**
     * 获取订单设备信息
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderno
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping(value = "/findOrderDevice", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderDevice(@RequestParam(value = "rows", defaultValue = "20") int rows, //每页数据量
                                     @RequestParam(value = "page", defaultValue = "1") int page,//开始页码位置
                                     @RequestParam(value = "sort", defaultValue = "") String sort,//排序字段
                                     @RequestParam(value = "order", defaultValue = "") String order, //升降序标识 DESC asc
                                     @RequestParam(value = "orderno", defaultValue = "") String orderno //订单编号
    ){
        EasyUiUtil.PageForData pageObj = orderManager.findOrderList4OTA(rows, page, sort, order, orderno);
        String jsonStr = JSONObject.fromObject(pageObj).toString();
        return jsonStr;
    }

    /**
     * 获取订单软件信息
     * @param orderno
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping(value = "/findOrderFile", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderFile(@RequestParam(value = "orderno", defaultValue = "") String orderno ){
        Map<String, String[]> map = orderManager.findFileByOrderNo(orderno);

        if (map != null && map.size()>0) {
            return JSONObject.fromObject(map).toString();
        }
        return "";
    }


}
