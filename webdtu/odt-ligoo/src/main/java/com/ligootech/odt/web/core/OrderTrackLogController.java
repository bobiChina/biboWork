package com.ligootech.odt.web.core;

import com.ligootech.odt.web.util.DateEditor;
import com.ligootech.odt.web.util.ShiroUtil;
import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.entity.core.Order;
import com.ligootech.webdtu.entity.core.OrderTrack;
import com.ligootech.webdtu.service.dtu.DtuManager;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.webdtu.service.order.OrderTrackManager;
import com.ligootech.webdtu.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * Created by Administrator on 2016/4/18.
 */
@Controller
@RequestMapping("/orderTrack")
public class OrderTrackLogController {
    public static final int PAGE_SIZE = 15;

    @Autowired
    private OrderTrackManager orderTrackManager;

    @Autowired
    DtuManager dtuManager;

    @Autowired
    DtuUserManager dtuUserManager;

    @Autowired
    OrderManager orderManager;

    /**
     * 查询列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String forwardOrderTrackList(Model model){
        Long userId = ShiroUtil.getCurrentUser().getId();
        /******************************************
         * 初始化客户
         ******************************************/
        List<Object[]> corpList = orderTrackManager.findOrderCorp();
        model.addAttribute("corpList", corpList);

        /******************************************
         * 初始化销售代表
         ******************************************/
        List<Object[]> salesmanList = orderTrackManager.findOrderSalesman();
        model.addAttribute("salesmanList", salesmanList);

        return "odt/orderTrackList";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String forwardOrderTrackDetail(Model model, @RequestParam(value = "orderno", defaultValue = "") String orderno){

        /******************************************
         * 初始化订单
         ******************************************/
        Order order = null;

        /******************************************
         * 判断是否具有权限
         *****************************************/
        Subject subject = SecurityUtils.getSubject();
        Boolean isOdtadmin = subject.hasRole("odtadmin");	//销售经理
        if (isOdtadmin){
            order = orderManager.findInfoByOrderNo(orderno);
        }else {
            order = orderManager.findInfoByOrderNo(orderno, ShiroUtil.getCurrentUser().getId());
        }
        model.addAttribute("order", order);
        if (order != null) {
           /* //设备信息
            List<Object[]> orderDeviceList = orderManager.findDeviceByOrderNo(orderno);
            model.addAttribute("orderDeviceList", orderDeviceList);

            //设备软件
            model.addAttribute("orderDeviceFile", orderManager.findDeviceFileByOrderNo(orderno));*/
            model.addAttribute("trackLogList", orderTrackManager.findOrderTrackByOrderNo(orderno));

            model.addAttribute("deviceList", orderManager.findOrderDeviceList(orderno));

            model.addAttribute("shList", orderManager.findSHInfoList(orderno));

            //对应设备的版本号
            model.addAttribute("hwVersionMap", orderManager.findDeviceVersion(orderno, "hw_version"));
            model.addAttribute("swVersionMap", orderManager.findDeviceVersion(orderno, "sw_version"));

            Long salesmanLong = -1l;
            try{
                salesmanLong = Long.valueOf(StringUtil.null2String(order.getSalesman()));
                DtuUser salesman = dtuUserManager.findByUserId(salesmanLong);
                if (salesman == null) {
                    model.addAttribute("salesman_show", "无");
                }else{
                    model.addAttribute("salesman_show", salesman.getFullName());
                }
            }catch (Exception e){
                //logger.debug("销售代表录入错误，不能转化为数字类型");
                model.addAttribute("salesman_show", "无");
            }
        }

        return "odt/orderTrackDetail";
    }

    @RequestMapping(value = "/listQuery", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String dtuList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                          @RequestParam(value = "orderno", defaultValue = "") String orderno,
                          HttpServletRequest req){

        Subject subject = SecurityUtils.getSubject();
        Boolean isOdtadmin = subject.hasRole("odtadmin");	//销售经理

        Integer count = 0;
        if (isOdtadmin){
            count = orderManager.findOrderCount4Page(req);
        }else{
            count = orderManager.findOrderCount4Page(req, ShiroUtil.getCurrentUser().getId());
        }
        int pageCount = count /PAGE_SIZE;
        int mod = count%PAGE_SIZE;
        if (mod>0)
            pageCount += 1;
        if (pageCount ==0)
            pageCount += 1;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageCount", pageCount);

        if (isOdtadmin){
            map.put("result", orderManager.findOrderList4Page(req, PAGE_SIZE));
        }else{
            map.put("result", orderManager.findOrderList4Page(req, PAGE_SIZE, ShiroUtil.getCurrentUser().getId()));
        }
        map.put("pageNumber", pageNumber);
        JSONObject object = JSONObject.fromObject(map);
        return object.toString();
    }

    /**
     * 设备信息
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/getOrderDevice", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderByOrderNo(@RequestParam(value = "orderno") String orderno){
        String rsStr = "-1";
        Map<String, Object> rsMap = new HashMap<>();
        //设备信息
        rsMap.put("order_device", orderManager.findDeviceByOrderNo(orderno));
        //设备软件
        rsMap.put("order_device_file", orderManager.findDeviceFileByOrderNo(orderno));

        rsStr = JSONObject.fromObject(rsMap).toString();

        return rsStr;
    }


}
