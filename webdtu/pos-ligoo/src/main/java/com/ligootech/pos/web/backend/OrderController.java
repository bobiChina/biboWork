package com.ligootech.pos.web.backend;

import com.ligootech.webdtu.entity.core.*;
import com.ligootech.webdtu.repository.OptLogDao;
import com.ligootech.webdtu.service.order.OrderTrackManager;
import com.ligootech.webdtu.service.order.SNManager;
import com.ligootech.webdtu.service.dtu.*;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.webdtu.service.product.ProductManager;
import com.ligootech.pos.web.util.DateEditor;
import com.ligootech.pos.web.util.ShiroUtil;
import com.ligootech.webdtu.util.EasyUiUtil;
import com.ligootech.webdtu.util.LoggerUtil;
import com.ligootech.webdtu.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wly on 2015/10/15 15:47.
 */
@SuppressWarnings("JavaDoc")
@Transactional
@Controller
@RequestMapping("/order")
public class OrderController {
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    HttpServletRequest request;

    @Autowired
    OrderManager orderManager;

    @Autowired
    ProductManager productManager;

    @Autowired
    DtuUserManager dtuUserManager;

    @Autowired
    VehicleTypeManager vehicleTypeManager;

    @Autowired
    BatteryTypeManager batteryTypeManager;

    @Autowired
    VehicleModelManager vehicleModelManager;

    @Autowired
    BatteryModelManager batteryModelManager;

    @Autowired
    OptLogDao optLogDao;

    @Autowired
    SNManager snManager;

    @Autowired
    CorpManager corpManager;

    @Autowired
    private OrderTrackManager orderTrackManager;

    //@RequiresRoles("backend")   // @RequiresPermissions("users:view")  权限验证  角色验证
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String forwardOrderPage(Model model){

        Long userId = ShiroUtil.getCurrentUser().getId();

        /*
        Date date = new Date();
        model.addAttribute("init_time", DateEditor.TIMEFORMAT.format(date));
        model.addAttribute("init_timeMill", date.getTime());*/
        /**********************************************
         * 登录人信息
         *********************************************/
        DtuUser user = dtuUserManager.get(userId);
        model.addAttribute("user", user);
        model.addAttribute("init_date", DateEditor.DATEFORMAT.format(new Date()));
        /**********************************************
         * 客户信息
         *********************************************/
        List<Object[]> userList = corpManager.getCorpList();
        model.addAttribute("userList", userList );

        /**********************************************
         * 销售代表
         *********************************************/
        List<Object[]> salesList = dtuUserManager.findUserByType(1);
        model.addAttribute("salesList", salesList );

        /**********************************************
         * 技术代表
         *********************************************/
        List<Object[]> technicistList = dtuUserManager.findUserByType(2);
        model.addAttribute("technicistList", technicistList );

        /**********************************************
         * fw管理员
         *********************************************/
        List<Object[]> fwList = dtuUserManager.findUserByRole("fw");
        model.addAttribute("fwList", fwList );

        /**********************************************
         * sqa管理员
         *********************************************/
        List<Object[]> sqaList = dtuUserManager.findUserByRole("sqa");
        model.addAttribute("sqaList", sqaList );

        /**********************************************
         * 设备型号
         *********************************************/
        //主机
        List<String> device_BCU_list = orderManager.findDeviceByType("1_2");
        model.addAttribute("device_BCU_list", device_BCU_list );

        //一体机
        List<String> device_BYU_list = orderManager.findDeviceByType("3_4");
        model.addAttribute("device_BYU_list", device_BYU_list );

        //从机
        List<String> device_BMU_list = orderManager.findDeviceByType("5");
        model.addAttribute("device_BMU_list", device_BMU_list );

        //绝缘检测
        List<String> device_LDM_list = orderManager.findDeviceByType("6");
        model.addAttribute("device_LDM_list", device_LDM_list );

        //DTU模块
        List<String> device_DTU_list = orderManager.findDeviceByType("7");
        model.addAttribute("device_DTU_list", device_DTU_list );

        /**********************************************
         * 车辆类型和车辆型号
         *********************************************/
        model.addAttribute("vtList", vehicleTypeManager.getAll());
        model.addAttribute("bmList", batteryTypeManager.getAll());
        //删除无用的车辆型号
        vehicleModelManager.delVehicleModel();

        String toadd = request.getParameter("toadd");
        if (toadd != null && !"".equals(toadd)) {
            model.addAttribute("toadd", toadd);
            String userid = request.getParameter("userid");
            model.addAttribute("init_user", userid);
        }

        return "backend/order/orderPage";
    }

    //@RequestMapping(value = "/page", method = RequestMethod.GET)
    public String forwardOrderPage_bak20160616(Model model){

        Long userId = ShiroUtil.getCurrentUser().getId();
        /**********************************************
         * 订单列表
         *********************************************/
        List<String[]> list = orderManager.findSimpleOrderList(1);
        model.addAttribute("orderList", list);

        /*
        Date date = new Date();
        model.addAttribute("init_time", DateEditor.TIMEFORMAT.format(date));
        model.addAttribute("init_timeMill", date.getTime());*/
        /**********************************************
         * 登录人信息
         *********************************************/
        DtuUser user = dtuUserManager.get(userId);
        model.addAttribute("user", user);
        model.addAttribute("init_date", DateEditor.DATEFORMAT.format(new Date()));
        /**********************************************
         * 客户信息
         *********************************************/
        List<Object[]> userList = corpManager.getCorpList();
        model.addAttribute("userList", userList );

        /**********************************************
         * 销售代表
         *********************************************/
        List<Object[]> salesList = dtuUserManager.findUserByType(1);
        model.addAttribute("salesList", salesList );

        /**********************************************
         * 技术代表
         *********************************************/
        List<Object[]> technicistList = dtuUserManager.findUserByType(2);
        model.addAttribute("technicistList", technicistList );

        /**********************************************
         * fw管理员
         *********************************************/
        List<Object[]> fwList = dtuUserManager.findUserByRole("fw");
        model.addAttribute("fwList", fwList );

        /**********************************************
         * sqa管理员
         *********************************************/
        List<Object[]> sqaList = dtuUserManager.findUserByRole("sqa");
        model.addAttribute("sqaList", sqaList );

        /**********************************************
         * 设备型号
         *********************************************/
        //主机
        List<String> device_BCU_list = orderManager.findDeviceByType("1_2");
        model.addAttribute("device_BCU_list", device_BCU_list );

        //一体机
        List<String> device_BYU_list = orderManager.findDeviceByType("3_4");
        model.addAttribute("device_BYU_list", device_BYU_list );

        //从机
        List<String> device_BMU_list = orderManager.findDeviceByType("5");
        model.addAttribute("device_BMU_list", device_BMU_list );

        //绝缘检测
        List<String> device_LDM_list = orderManager.findDeviceByType("6");
        model.addAttribute("device_LDM_list", device_LDM_list );

        //DTU模块
        List<String> device_DTU_list = orderManager.findDeviceByType("7");
        model.addAttribute("device_DTU_list", device_DTU_list );

        /**********************************************
         * 车辆类型和车辆型号
         *********************************************/
        model.addAttribute("vtList", vehicleTypeManager.getAll());
        model.addAttribute("bmList", batteryTypeManager.getAll());
        //删除无用的车辆型号
        vehicleModelManager.delVehicleModel();

        /**********************************************
         * 初始化带入的参数
         *********************************************/
        String order_init = request.getParameter("order_init");
        if (order_init != null && !"".equals(order_init)) {
            model.addAttribute("orderShow", order_init);
        }

        String toadd = request.getParameter("toadd");
        if (toadd != null && !"".equals(toadd)) {
            model.addAttribute("toadd", toadd);
            String userid = request.getParameter("userid");
            model.addAttribute("init_user", userid);
        }

        return "backend/order/orderPage";
    }

    @RequestMapping(value = "/orderSoftwarePage", method = RequestMethod.GET)
    public String forwardOrderSoftwarePage(Model model){

        Long userId = ShiroUtil.getCurrentUser().getId();
        /**********************************************
         * 订单列表
         *********************************************/
        List<String[]> list = orderManager.findSimpleOrderList(1);
        model.addAttribute("orderList", list);

        /*
        Date date = new Date();
        model.addAttribute("init_time", DateEditor.TIMEFORMAT.format(date));
        model.addAttribute("init_timeMill", date.getTime());*/
        /**********************************************
         * 登录人信息
         *********************************************/
        DtuUser user = dtuUserManager.get(userId);
        model.addAttribute("user", user);
        model.addAttribute("init_date", DateEditor.DATEFORMAT.format(new Date()));
        /**********************************************
         * 客户信息
         *********************************************/
        List<Object[]> userList = corpManager.getCorpList();
        model.addAttribute("userList", userList );

        /**********************************************
         * 销售代表
         *********************************************/
        List<Object[]> salesList = dtuUserManager.findUserByType(1);
        model.addAttribute("salesList", salesList );

        /**********************************************
         * 技术代表
         *********************************************/
        List<Object[]> technicistList = dtuUserManager.findUserByType(2);
        model.addAttribute("technicistList", technicistList );

        /**********************************************
         * fw管理员
         *********************************************/
        List<Object[]> fwList = dtuUserManager.findUserByRole("fw");
        model.addAttribute("fwList", fwList );

        /**********************************************
         * sqa管理员
         *********************************************/
        List<Object[]> sqaList = dtuUserManager.findUserByRole("sqa");
        model.addAttribute("sqaList", sqaList );

        /**********************************************
         * 设备型号
         *********************************************/
        //主机
        List<String> device_BCU_list = orderManager.findDeviceByType("1_2");
        model.addAttribute("device_BCU_list", device_BCU_list );

        //一体机
        List<String> device_BYU_list = orderManager.findDeviceByType("3_4");
        model.addAttribute("device_BYU_list", device_BYU_list );

        //从机
        List<String> device_BMU_list = orderManager.findDeviceByType("5");
        model.addAttribute("device_BMU_list", device_BMU_list );

        //绝缘检测
        List<String> device_LDM_list = orderManager.findDeviceByType("6");
        model.addAttribute("device_LDM_list", device_LDM_list );

        //DTU模块
        List<String> device_DTU_list = orderManager.findDeviceByType("7");
        model.addAttribute("device_DTU_list", device_DTU_list );

        /**********************************************
         * 车辆类型和车辆型号
         *********************************************/
        model.addAttribute("vtList", vehicleTypeManager.getAll());
        model.addAttribute("bmList", batteryTypeManager.getAll());
        //删除无用的车辆型号
        vehicleModelManager.delVehicleModel();

        /**********************************************
         * 初始化带入的参数
         *********************************************/
        String order_init = request.getParameter("order_init");
        if (order_init != null && !"".equals(order_init)) {
            model.addAttribute("orderShow", order_init);
        }

        String toadd = request.getParameter("toadd");
        if (toadd != null && !"".equals(toadd)) {
            model.addAttribute("toadd", toadd);
            String userid = request.getParameter("userid");
            model.addAttribute("init_user", userid);
        }

        return "backend/order/orderSoftwarePage";
    }

    /**
     * 软件上传页面
     * @param model
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping(value = "/orderUploadFile", method = RequestMethod.GET)
    public String orderUploadFile(Model model){

        Long userId = ShiroUtil.getCurrentUser().getId();
        /**********************************************
         * 订单列表
         *********************************************/
        List<String[]> list = orderManager.findSimpleOrderList(1);
        model.addAttribute("orderList", list);

        /*
        Date date = new Date();
        model.addAttribute("init_time", DateEditor.TIMEFORMAT.format(date));
        model.addAttribute("init_timeMill", date.getTime());*/
        /**********************************************
         * 登录人信息
         *********************************************/
        DtuUser user = dtuUserManager.get(userId);
        model.addAttribute("user", user);
        model.addAttribute("init_date", DateEditor.DATEFORMAT.format(new Date()));
        /**********************************************
         * 客户信息
         *********************************************/
        List<Object[]> userList = corpManager.getCorpList();
        model.addAttribute("userList", userList );

        /**********************************************
         * 销售代表
         *********************************************/
        List<Object[]> salesList = dtuUserManager.findUserByType(1);
        model.addAttribute("salesList", salesList );

        /**********************************************
         * 技术代表
         *********************************************/
        List<Object[]> technicistList = dtuUserManager.findUserByType(2);
        model.addAttribute("technicistList", technicistList );

        /**********************************************
         * fw管理员
         *********************************************/
        List<Object[]> fwList = dtuUserManager.findUserByRole("fw");
        model.addAttribute("fwList", fwList );

        /**********************************************
         * sqa管理员
         *********************************************/
        List<Object[]> sqaList = dtuUserManager.findUserByRole("sqa");
        model.addAttribute("sqaList", sqaList );

        /**********************************************
         * 设备型号
         *********************************************/
        //主机
        List<String> device_BCU_list = orderManager.findDeviceByType("1_2");
        model.addAttribute("device_BCU_list", device_BCU_list );

        //一体机
        List<String> device_BYU_list = orderManager.findDeviceByType("3_4");
        model.addAttribute("device_BYU_list", device_BYU_list );

        //从机
        List<String> device_BMU_list = orderManager.findDeviceByType("5");
        model.addAttribute("device_BMU_list", device_BMU_list );

        //绝缘检测
        List<String> device_LDM_list = orderManager.findDeviceByType("6");
        model.addAttribute("device_LDM_list", device_LDM_list );

        //DTU模块
        List<String> device_DTU_list = orderManager.findDeviceByType("7");
        model.addAttribute("device_DTU_list", device_DTU_list );

        /**********************************************
         * 车辆类型和车辆型号
         *********************************************/
        model.addAttribute("vtList", vehicleTypeManager.getAll());
        model.addAttribute("bmList", batteryTypeManager.getAll());
        //删除无用的车辆型号
        vehicleModelManager.delVehicleModel();

        /**********************************************
         * 初始化带入的参数
         *********************************************/
        String order_init = request.getParameter("order_init");
        if (order_init != null && !"".equals(order_init)) {
            model.addAttribute("orderShow", order_init);
        }

        String toadd = request.getParameter("toadd");
        if (toadd != null && !"".equals(toadd)) {
            model.addAttribute("toadd", toadd);
            String userid = request.getParameter("userid");
            model.addAttribute("init_user", userid);
        }

        return "backend/order/orderUploadFile";
    }

    @RequestMapping(value = "/device", method = RequestMethod.GET)
    public String forwardOrderDevice(Model model){
        Long userId = ShiroUtil.getCurrentUser().getId();
        List<String[]> list = orderManager.findSimpleOrderList(2);

        model.addAttribute("orderList", list);

        DtuUser user = dtuUserManager.get(userId);
        model.addAttribute("user", user);

        //order_init
        String order_init = request.getParameter("order_init");
        if (order_init != null && !"".equals(order_init)) {
            model.addAttribute("orderShow", order_init);
        }

        return "backend/order/orderDevicePage";
    }

    @RequestMapping(value = "/orderLog", method = RequestMethod.GET)
    public String orderLog(Model model){
        Long userId = ShiroUtil.getCurrentUser().getId();

        //用户角色
        String userrole = "";
        Subject subject = SecurityUtils.getSubject();
        Boolean isFw = subject.hasRole("fw");	//fw管理
        Boolean isSqa = subject.hasRole("sqa");	//sqa管理
        if (isFw){
            userrole = "fw";
        }else if(isSqa){
            userrole = "sqa";
        }
        model.addAttribute("userrole", userrole);

        /* 不限制 sby 2016年5月30日 17:32:03
        List<Object[]> list = null;

        if (isFw){
            list = orderManager.findSimpleOrderListByTrack(ShiroUtil.getCurrentUser());
        }else{
            list = orderManager.findSimpleOrderList(userId, 1);
        }*/
        List<String[]> list = orderManager.findSimpleOrderList(1);
        model.addAttribute("orderList", list);

        DtuUser user = dtuUserManager.get(userId);
        model.addAttribute("user", user);
        model.addAttribute("init_date", DateEditor.DATEFORMAT.format(new Date()));


        return "backend/order/orderLogPage";
    }

    /**
     * 设备绑定页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public String forwardOrdeConfig(Model model){
        List<Object[]> list = orderManager.findSimpleOrderList(ShiroUtil.getCurrentUser().getId(), 1);

        model.addAttribute("orderList", list);
        return "backend/order/orderConfig";
    }

    /**
     * 左侧列表
     * @return
     */
    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderList(@RequestParam(value = "type", defaultValue = "1") int type,
                                @RequestParam(value = "list_type", defaultValue = "1") int list_type){ //列表类型 默认1为软件管理列表  2 生产管理列表
        List<String[]> list = orderManager.findSimpleOrderList(list_type);

        return JSONArray.fromObject(list).toString();
    }

    @RequestMapping(value = "/getOrderListBoxvalue", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderListcombobox(@RequestParam(value = "type", defaultValue = "1") int type,
                                        @RequestParam(value = "list_type", defaultValue = "1") int list_type){
        List<String[]> list = orderManager.findSimpleOrderList(list_type);

        List<Map<String, String>> rs = new ArrayList<Map<String, String>>();
        if (list != null && list.size() > 0) {
            String str = "";

            for (int i = 0; i <list.size(); i++) {
                Object[] objArr = list.get(i);
                str = objArr[1].toString();
                if (str != null && !"".equals(str)) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("orderno", str);
                    map.put("ordername", str);
                    rs.add(map);
                }
            }
        }
        String rsStr = JSONArray.fromObject(rs).toString();

        return rsStr;
    }

    /**
     * 客户订单信息
     * @param corpid
     * @return
     */
    @RequestMapping(value = "/getOrderByCorpId", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderByUserId(@RequestParam(value = "corpid") Long corpid){
        List<Order> list = orderManager.findOrderList(corpid);

        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    /**
     * 获取订单设备
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/getOrderSNList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findSNListByOrderNo(@RequestParam(value = "orderno") String orderno, @RequestParam(value = "prodtypeid") Long prodtypeid){
        List<SNInfo> list = snManager.findSNAllListByOrderNo(orderno);
        return JSONArray.fromObject(list).toString();
    }

    /**
     * 获取订单对应的扫描信息
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/getOrderScanList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findScanListByOrderNo(@RequestParam(value = "orderno") String orderno){
        List<SNScan> list = snManager.findScanAllListByOrderNo(orderno);
        return JSONArray.fromObject(list).toString();
    }

    /**
     * SN解除绑定
     * @param ids
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/delSNFun", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delSNFun(@RequestParam(value = "ids", defaultValue = "") String ids,
                           @RequestParam(value = "orderno", defaultValue = "") String orderno){

        //int rs = snManager.delSNById(id, ShiroUtil.getCurrentUser().getId());//单个删除
        int rs = snManager.unbind(ids, orderno, ShiroUtil.getCurrentUser());
        if (rs > 0){
            return "ok";
        }
        return "failed";
    }

    /**
     * 解绑
     * @param ids
     * @param orderno
     * @param type
     * @return
     */
    @RequestMapping(value = "/unbindOrderDevice", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String unbindOrderDevice(@RequestParam(value = "ids", defaultValue = "") String ids,
                           @RequestParam(value = "orderno", defaultValue = "") String orderno,
                           @RequestParam(value = "type", defaultValue = "") String type){

        //int rs = snManager.delSNById(id, ShiroUtil.getCurrentUser().getId());//单个删除
        int rs = orderManager.unbindOrderDevice(ids, orderno, type, ShiroUtil.getCurrentUser());
        if (rs > -1){
            return "ok";
        }
        return "failed";
    }

    /**
     * 修改出货状态
     * @param ids
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String updateStatus(@RequestParam(value = "ids", defaultValue = "") String ids, @RequestParam(value = "orderno", defaultValue = "") String orderno){
        ids = StringUtil.null2String(ids).trim();
        if ("".equals(ids)){
            return "none";
        }

        int rs = orderManager.updateStatus(ids, orderno, 5, ShiroUtil.getCurrentUser());
        if (rs > 0){
            return "ok";
        }
        return "failed";
    }

    /**
     * 扫描信息表删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delScanFun", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delScanFun(@RequestParam(value = "id") Long id){
        //int rs = snManager.delScanById(id, ShiroUtil.getCurrentUser().getId());
        snManager.delScanById(id, ShiroUtil.getCurrentUser().getId());
        return "";
    }

    /**
     * 上传操作
     * @param req
     * @return
     */
    @RequestMapping(value = "/orderFileUpload", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String orderFileUpload( HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        int rs = orderManager.saveDeviceFile(req, ShiroUtil.getCurrentUser());

        if (rs < 0){
            return "error";
        }
        return "ok";
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
            Map<String, Object> rsMap = new HashMap<>();
            rsMap.put("order", order);
            //录单人姓名
            rsMap.put("username", order.getOptUserName());
            rsMap.put("opttime", DateEditor.DATEFORMAT.format(order.getOptTime()));
            rsMap.put("opttime_edit",DateEditor.TIMEFORMAT.format(order.getOptTime()));

            //销售代表 技术代表
            Long salesmanLong = -1l;
            try{
                salesmanLong = Long.valueOf(StringUtil.null2String(order.getSalesman()));
                DtuUser salesman = dtuUserManager.findByUserId(salesmanLong);
                if (salesman == null) {
                    rsMap.put("salesman_show", "无");
                }else{
                    rsMap.put("salesman_show", salesman.getFullName());
                }
            }catch (Exception e){
                logger.debug("销售代表录入错误，不能转化为数字类型");
                rsMap.put("salesman_show", "无");
            }

            Long technicistLong = -1l;
            try{
                technicistLong = Long.valueOf(StringUtil.null2String(order.getTechnicalDelegate()));
                DtuUser technicist = dtuUserManager.findByUserId(technicistLong);
                if (technicist == null) {
                    rsMap.put("technicist_show", "无");
                }else{
                    rsMap.put("technicist_show", technicist.getFullName());
                }
            }catch (Exception e){
                logger.debug("销售代表录入错误，不能转化为数字类型");
                rsMap.put("technicist_show", "无");
            }

            //设备信息
            rsMap.put("order_device", orderManager.findDeviceByOrderNo(orderno));
            //设备软件
            rsMap.put("order_device_file", orderManager.findDeviceFileByOrderNo(orderno));

            OrderTrack orderTrackDB = orderTrackManager.findNewTrackByOrderNo(orderno);
            if (orderTrackDB == null) {
                rsMap.put("order_track_delivery", "无");
            }else{
                Date date = orderTrackDB.getReviewDelivery();
                if (date == null) {
                    rsMap.put("order_track_delivery", "无");
                }else{
                    rsMap.put("order_track_delivery", DateEditor.DATEFORMAT.format(orderTrackDB.getReviewDelivery()));
                }
                rsMap.put("reviewDevelopDuration", orderTrackDB.getReviewDevelopDuration());//开发工时
                rsMap.put("reviewTestDuration", orderTrackDB.getReviewTestDuration());//测试工时
            }

            rsStr = JSONObject.fromObject(rsMap).toString();

            //JSONObject orderJson = JSONObject.fromObject(order);
            //JSONArray jsonResult = new JSONArray();
            //jsonResult.add(orderJson);
            //录单人姓名
            /*Map<String, String> userMap = new HashMap<String, String>();
            userMap.put("username", order.getOptUserName());
            userMap.put("opttime", DateEditor.DATEFORMAT.format(order.getOptTime()));
            userMap.put("opttime_edit",DateEditor.TIMEFORMAT.format(order.getOptTime()));
            JSONObject optUsername = JSONObject.fromObject(userMap);
            jsonResult.add(optUsername);

            rsStr = jsonResult.toString();*/
        }
        return rsStr;
    }

    @RequestMapping(value = "/findSHOrderInfo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findSHOrderInfo(@RequestParam(value = "orderno", defaultValue = "") String orderno){
        Order order = orderManager.findInfoByOrderNo(orderno);
        String rsStr = "-1";
        if (order != null) {
            Map<String, Object> rsMap = new HashMap<>();
            rsMap.put("order", order);
            //录单人姓名
            rsMap.put("username", order.getOptUserName());
            rsMap.put("opttime", DateEditor.DATEFORMAT.format(order.getOptTime()));
            rsMap.put("opttime_edit",DateEditor.TIMEFORMAT.format(order.getOptTime()));

            //销售代表 技术代表
            Long salesmanLong = -1l;
            try{
                salesmanLong = Long.valueOf(StringUtil.null2String(order.getSalesman()));
                DtuUser salesman = dtuUserManager.findByUserId(salesmanLong);
                if (salesman == null) {
                    rsMap.put("salesman_show", "无");
                }else{
                    rsMap.put("salesman_show", salesman.getFullName());
                }
            }catch (Exception e){
                logger.debug("销售代表录入错误，不能转化为数字类型");
                rsMap.put("salesman_show", "无");
            }

            Long technicistLong = -1l;
            try{
                technicistLong = Long.valueOf(StringUtil.null2String(order.getTechnicalDelegate()));
                DtuUser technicist = dtuUserManager.findByUserId(technicistLong);
                if (technicist == null) {
                    rsMap.put("technicist_show", "无");
                }else{
                    rsMap.put("technicist_show", technicist.getFullName());
                }
            }catch (Exception e){
                logger.debug("技术代表录入错误，不能转化为数字类型");
                rsMap.put("technicist_show", "无");
            }
            rsMap.put("order_sh", orderManager.findSHOrderList(order.getBoundOrder()));

            rsStr = JSONObject.fromObject(rsMap).toString();

            //JSONObject orderJson = JSONObject.fromObject(order);
            //JSONArray jsonResult = new JSONArray();
            //jsonResult.add(orderJson);
            //录单人姓名
            /*Map<String, String> userMap = new HashMap<String, String>();
            userMap.put("username", order.getOptUserName());
            userMap.put("opttime", DateEditor.DATEFORMAT.format(order.getOptTime()));
            userMap.put("opttime_edit",DateEditor.TIMEFORMAT.format(order.getOptTime()));
            JSONObject optUsername = JSONObject.fromObject(userMap);
            jsonResult.add(optUsername);

            rsStr = jsonResult.toString();*/
        }
        return rsStr;
    }

    /**
     * 售后订单查询
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/findOrderInfo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderInfo(@RequestParam(value = "orderno") String orderno){
        Order order = orderManager.findInfoByOrderNo(orderno);
        String rsStr = "-1";
        if (order != null) {
            Map<String, Object> rsMap = new HashMap<>();
            rsMap.put("order", order);
            //录单人姓名
            rsMap.put("username", order.getOptUserName());
            rsMap.put("opttime", DateEditor.DATEFORMAT.format(order.getOptTime()));
            rsMap.put("opttime_edit",DateEditor.TIMEFORMAT.format(order.getOptTime()));

            //销售代表 技术代表
            Long salesmanLong = -1l;
            try{
                salesmanLong = Long.valueOf(StringUtil.null2String(order.getSalesman()));
                DtuUser salesman = dtuUserManager.findByUserId(salesmanLong);
                if (salesman == null) {
                    rsMap.put("salesman_show", "无");
                }else{
                    rsMap.put("salesman_show", salesman.getFullName());
                }
            }catch (Exception e){
                logger.debug("销售代表录入错误，不能转化为数字类型");
                rsMap.put("salesman_show", "无");
            }

            Long technicistLong = -1l;
            try{
                technicistLong = Long.valueOf(StringUtil.null2String(order.getTechnicalDelegate()));
                DtuUser technicist = dtuUserManager.findByUserId(technicistLong);
                if (technicist == null) {
                    rsMap.put("technicist_show", "无");
                }else{
                    rsMap.put("technicist_show", technicist.getFullName());
                }
            }catch (Exception e){
                logger.debug("销售代表录入错误，不能转化为数字类型");
                rsMap.put("technicist_show", "无");
            }

            //设备信息
            rsMap.put("order_device", orderManager.findDeviceByOrderNo(orderno));
            rsMap.put("order_sh", orderManager.findSHOrderList(orderno));
            //设备软件
            //rsMap.put("order_device_file", orderManager.findDeviceFileByOrderNo(orderno));

            rsStr = JSONObject.fromObject(rsMap).toString();

        }
        return rsStr;
    }

    /**
     * 订单信息带售后情况
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/findInfoByOrderNo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findInfoByOrderNo(@RequestParam(value = "orderno") String orderno){
        Order order = orderManager.findInfoByOrderNo(orderno);
        String rsStr = "-1";
        if (order != null) {
            Map<String, Object> rsMap = new HashMap<>();
            rsMap.put("order", order);
            //录单人姓名
            rsMap.put("username", order.getOptUserName());
            rsMap.put("opttime", DateEditor.DATEFORMAT.format(order.getOptTime()));
            rsMap.put("opttime_edit",DateEditor.TIMEFORMAT.format(order.getOptTime()));

            //销售代表 技术代表
            Long salesmanLong = -1l;
            try{
                salesmanLong = Long.valueOf(StringUtil.null2String(order.getSalesman()));
                DtuUser salesman = dtuUserManager.findByUserId(salesmanLong);
                if (salesman == null) {
                    rsMap.put("salesman_show", "无");
                }else{
                    rsMap.put("salesman_show", salesman.getFullName());
                }
            }catch (Exception e){
                logger.debug("销售代表录入错误，不能转化为数字类型");
                rsMap.put("salesman_show", "无");
            }

            Long technicistLong = -1l;
            try{
                technicistLong = Long.valueOf(StringUtil.null2String(order.getTechnicalDelegate()));
                DtuUser technicist = dtuUserManager.findByUserId(technicistLong);
                if (technicist == null) {
                    rsMap.put("technicist_show", "无");
                }else{
                    rsMap.put("technicist_show", technicist.getFullName());
                }
            }catch (Exception e){
                logger.debug("销售代表录入错误，不能转化为数字类型");
                rsMap.put("technicist_show", "无");
            }

            //设备信息
            rsMap.put("order_device", orderManager.findDeviceByOrderNo(orderno));
            //设备软件
            rsMap.put("order_device_file", orderManager.findDeviceFileByOrderNo(orderno));
            //售后情况
            //rsMap.put("order_sh_list", orderTrackManager.findDeviceFileByOrderNo(orderno));

            rsStr = JSONObject.fromObject(rsMap).toString();
        }
        return rsStr;
    }

    /**
     * 检查订单编号是否存在
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/checkOrderNo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String checkOrderNo(@RequestParam(value = "orderno") String orderno){
        Order order = orderManager.findInfoByOrderNo(orderno);
        String rsStr = "-1";
        if (order != null) {
            return "isuse";
        }
        return rsStr;
    }

    /**
     * 搜索用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findUserByUserName(@RequestParam(value = "id", defaultValue = "0") Long userId){
        DtuUser dtuUser = dtuUserManager.findByUserId(userId);
        String rsStr = "-1";
        if (dtuUser != null) {
            JSONObject userJson = JSONObject.fromObject(dtuUser);
            rsStr = userJson.toString();
        }
        return rsStr;
    }

    /**
     * 订单删除（修改订单状态为作废）
     * @param orderid
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/delorder", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delOrderById(@RequestParam(value = "orderid") Long orderid, @RequestParam(value = "orderno") String orderNo){
        //判断是否有设备绑定，如有则不能删除
        int snCount = orderManager.findSnCount4DelOrder(orderNo);
        if (snCount > 0){
            return  "-1";
        }
        orderManager.delOrderById(orderid, ShiroUtil.getCurrentUser().getId());
        return "ok";
    }

    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String deleteOrder(@RequestParam(value = "orderid") Long orderid, @RequestParam(value = "orderno") String orderNo){
        Order order = orderManager.findInfoByOrderNo(orderNo);
        if (order.getOrderType() < 3){// 1-新订单 2-软件售后订单 3-硬件售后订单 硬件售后订单不检查软件上传情况
            //判断是否有软件上传，如有则不能删除
            int deviceCount = orderManager.findSoftware4Order(orderNo);
            if (deviceCount > 0){
                return  "-1";
            }
        }
        //判断是否有设备绑定，如有则不能删除
        int snCount = orderManager.findSnCount4DelOrder(orderNo);
        if (snCount > 0){
            return  "-2";
        }
        //已绑定售后订单
        int boundCount = orderManager.findBound4Order(orderNo);
        if (boundCount > 0){
            return  "-3";
        }
        orderManager.delOrderById(orderid, ShiroUtil.getCurrentUser().getId());
        return "ok";
    }

    /**
     * 检查是否有绑定的设备
     * @param orderNo
     * @param deviceClass
     * @return
     */
    @RequestMapping(value = "/checkSn4DelDevice", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String checkSn4DelDevice(@RequestParam(value = "orderno", defaultValue = "") String orderNo,
                                    @RequestParam(value = "device_class", defaultValue = "") String deviceClass ){
        //判断是否有设备绑定，如有则不能删除
        int snCount = orderManager.findSnCount4DelDevice(orderNo, deviceClass);
        return String.valueOf(snCount);
    }

    /**
     * 订单软件删除
     * @param orderNo
     * @param deviceClass
     * @return
     */
    @RequestMapping(value = "/delOrderDevice", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delOrderDevice(@RequestParam(value = "orderno", defaultValue = "") String orderNo,
                                    @RequestParam(value = "device_class", defaultValue = "") String deviceClass ){
        int rs = orderManager.delOrderDevice(orderNo, deviceClass, ShiroUtil.getCurrentUser().getId());
        return String.valueOf(rs);
    }

    /**
     * SN设备绑定
     * @param ids
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/saveSNScan", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveSNScan(@RequestParam(value = "ids") String ids, @RequestParam(value = "orderno") String orderNo){
        int rs = snManager.setScan2DTU(ids, orderNo, ShiroUtil.getCurrentUser().getId());
        return "ok";
    }

    /**
     * 订单信息保存
     * @param order
     * @return
     */
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveOrder( Order order,
                             @RequestParam(value = "opt_type", defaultValue = "edit") String opt_type,
                             @RequestParam(value = "devices_code", defaultValue = "") String devices_code,
                             @RequestParam(value = "devices_class", defaultValue = "") String devices_class,
                             @RequestParam(value = "devices_bmu", defaultValue = "") String devices_bmu,
                             @RequestParam(value = "devices_count", defaultValue = "") String devices_count
    ){
       //日志类型 新增或者修改
        //String logType = "UPDATE";
        Long optUserId = ShiroUtil.getCurrentUser().getId();

        //判断订单编号是否重复
        String orderNo = StringUtil.null2String(order.getOrderno()).trim();
        Order orderDB = orderManager.findInfoByOrderNo(orderNo);
        if (orderDB != null && order.getId() == null) {
            return "-1";
        }

        VehicleType vehicleType = order.getVehicleType();
        if (vehicleType != null) {
            String vehicleTypeId = StringUtil.null2String(vehicleType.getId());

            if("".equals(vehicleTypeId) || "-1".equals(vehicleTypeId)){
                order.setVehicleType(null);
            }
        }

        //判断车辆型号是否为新增
        VehicleModel vehicleModel = order.getVehicleModel();
        vehicleType = order.getVehicleType();
        if (vehicleType == null) {
            order.setVehicleModel(null);
        }else if (vehicleModel != null){
            String modelName = StringUtil.null2String(vehicleModel.getModelName());
            if(vehicleModel.getId() != null && -9999 == vehicleModel.getId() && vehicleType != null){
                Long vehicleModelId = vehicleModelManager.insertVehicleModel(vehicleType.getId(), vehicleModel.getModelName());
                order.getVehicleModel().setId(vehicleModelId);
            }else if("".equals(modelName) || "请选择".equals(modelName) || "-1".equals(modelName)){
                order.setVehicleModel(null);
            }
        }


        //电池信息是否修改或者新增
        BatteryModel batteryModel = order.getBatteryModel();
        if(batteryModel != null){
            if (batteryModel.getId() == null) {
                Long batteryModelId = batteryModelManager.insertBatteryModel(batteryModel);
                order.getBatteryModel().setId(batteryModelId);
            }else{
                batteryModelManager.save(batteryModel);
            }
        }

        if (order.getId() == null) {//新增的订单
            //logType = "ADD";
            order.setOptTime(new Date());
            order.setOptUser(optUserId);
            order.setOptUserName(ShiroUtil.getCurrentUser().getName());
        }
        if ("add".equals(opt_type)){// 新订单的创建
            //order.setOrderNote("新订单");
        }
        order.setShAddType(2);//手动添加
        orderManager.saveOrder(order, opt_type, devices_code, devices_class, devices_bmu, devices_count, optUserId);

        /*StringBuffer logStr = new StringBuffer();
        if("ADD".equals(logType)){
            logStr.append("订单添加>>");
        }else{
            logStr.append("订单修改>>");
        }
        *//*logStr.append("订单编号:").append(order.getOrderno())
                .append("，客户ID:").append(order.getCorp().getId())
                .append("，销售代表：").append(order.getSalesman());
               // .append("，电池ID：").append(order.getBatteryModel().getId())
               // .append("，电池类型ID：").append(order.getBatteryModel().getBatteryType().getId())
                //.append("，厂家名称：").append(order.getBatteryModel().getFactoryName())
                //.append("，电池串数：").append(order.getBatteryModel().getBatteryNumber())
                //.append("，电池容量：").append(order.getBatteryModel().getCapacity())
               // .append("，车辆类型ID：").append(order.getVehicleType().getId())
               // .append("，车辆型号ID：").append(order.getVehicleModel().getId())
               // .append("，车辆型号名称：").append(order.getVehicleModel().getModelName());*//*
        int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "ORDER", logStr.append(StringUtil.toStringForDB(order)).toString());
*/
        return order.getOrderno();
    }

    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String updateOrder( Order order){
       //日志类型 新增或者修改
        //String logType = "UPDATE";
        Long optUserId = ShiroUtil.getCurrentUser().getId();

        VehicleType vehicleType = order.getVehicleType();
        if (vehicleType != null) {
            String vehicleTypeId = StringUtil.null2String(vehicleType.getId());

            if("".equals(vehicleTypeId) || "-1".equals(vehicleTypeId)){
                order.setVehicleType(null);
            }
        }

        //判断车辆型号是否为新增
        VehicleModel vehicleModel = order.getVehicleModel();
        vehicleType = order.getVehicleType();
        if (vehicleType == null) {
            order.setVehicleModel(null);
        }else if (vehicleModel != null){
            String modelName = StringUtil.null2String(vehicleModel.getModelName());
            if(vehicleModel.getId() != null && -9999 == vehicleModel.getId() && vehicleType != null){
                Long vehicleModelId = vehicleModelManager.insertVehicleModel(vehicleType.getId(), vehicleModel.getModelName());
                order.getVehicleModel().setId(vehicleModelId);
            }else if("".equals(modelName) || "请选择".equals(modelName) || "-1".equals(modelName)){
                order.setVehicleModel(null);
            }
        }

        //电池信息是否修改或者新增
        BatteryModel batteryModel = order.getBatteryModel();
        if(batteryModel != null){
            if (batteryModel.getId() == null) {
                Long batteryModelId = batteryModelManager.insertBatteryModel(batteryModel);
                order.getBatteryModel().setId(batteryModelId);
            }else{
                batteryModelManager.save(batteryModel);
            }
        }
        int rs = orderManager.updateOrderBase(order, optUserId);
        // -1 订单套数小于已绑定设备数量
        return String.valueOf(rs);
    }

    /**
     * 售后订单信息保存
     * @param order
     * @return
     */
    @RequestMapping(value = "/saveSHOrder", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveSHOrder( Order order,
                             @RequestParam(value = "opt_type", defaultValue = "edit") String opt_type,
                             @RequestParam(value = "devices_code", defaultValue = "") String devices_code,
                             @RequestParam(value = "devices_class", defaultValue = "") String devices_class,
                             @RequestParam(value = "devices_bmu", defaultValue = "") String devices_bmu,
                             @RequestParam(value = "devices_count", defaultValue = "") String devices_count
    ){
       //日志类型 新增或者修改
        //String logType = "UPDATE";
        Long optUserId = ShiroUtil.getCurrentUser().getId();

        //判断订单编号是否重复
        String orderNo = StringUtil.null2String(order.getOrderno()).trim();
        Order orderDB = orderManager.findInfoByOrderNo(orderNo);
        if (orderDB != null && order.getId() == null) {
            return "-1";
        }
        Order orderOld = orderManager.findInfoByOrderNo(order.getBoundOrder());
        if (orderOld != null) {
            order.setCorp(orderOld.getCorp());
            order.setQuantity(0l);
            order.setProjectNote(orderOld.getProjectNote());
            order.setBatteryModel(orderOld.getBatteryModel());
            order.setContractNo(orderOld.getContractNo());
            order.setStatus(0);
            order.setSalesman(orderOld.getSalesman());
            order.setTechnicalDelegate(orderOld.getTechnicalDelegate());
            order.setVehicleType(orderOld.getVehicleType());
            order.setVehicleModel(orderOld.getVehicleModel());
            order.setOptUser(ShiroUtil.getCurrentUser().getId());
            order.setOptUserName(ShiroUtil.getCurrentUser().getName());
            order.setOptTime(new Date());
            order.setShAddType(1);
        }else{
            return "-2";// 原始订单异常
        }

        if (order.getId() == null) {//新增的订单
            //logType = "ADD";
            order.setOptTime(new Date());
            order.setOptUser(optUserId);
            order.setOptUserName(ShiroUtil.getCurrentUser().getName());
        }

        orderManager.saveSHOrder(order, opt_type, devices_code, devices_class, devices_bmu, devices_count, optUserId);

        return order.getOrderno();
    }

    @RequestMapping(value = "/saveUpdateOrderSh", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveUpdateOrderSh( @RequestParam(value = "orderno", defaultValue = "") String orderNo,
                             @RequestParam(value = "beforeSoftware", defaultValue = "") String beforeSoftware,
                             @RequestParam(value = "orderNote", defaultValue = "") String orderNote){
       int rs = orderManager.updateOrderNote(orderNo, orderNote, beforeSoftware, ShiroUtil.getCurrentUser().getId());
        // rs -1 硬件售后已有设备绑定
        return String.valueOf(rs);
    }

    /**
     * 修改系统配置
     * @param orderNo
     * @param devices_code
     * @param devices_class
     * @param devices_bmu
     * @param devices_count
     * @return
     */
    @RequestMapping(value = "/updateOrderModule", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String updateOrderModule(@RequestParam(value = "orderno", defaultValue = "") String orderNo,
                             @RequestParam(value = "devices_code", defaultValue = "") String devices_code,
                             @RequestParam(value = "devices_class", defaultValue = "") String devices_class,
                             @RequestParam(value = "devices_bmu", defaultValue = "") String devices_bmu,
                             @RequestParam(value = "devices_count", defaultValue = "") String devices_count
    ){

        int rs = orderManager.updateOrderDevice(orderNo, devices_code, devices_class, devices_bmu, devices_count, ShiroUtil.getCurrentUser().getId());
        // -1 已上传软件 -2 已有设备绑定
        return String.valueOf(rs);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
    }


    @RequestMapping(value = "/getTableData", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String getTableData(@RequestParam(value = "rows", defaultValue = "20") int rows, //每页数据量
                           @RequestParam(value = "page", defaultValue = "1") int page,//开始页码位置
                           @RequestParam(value = "sort", defaultValue = "") String sort,//排序字段
                           @RequestParam(value = "order", defaultValue = "") String order, //升降序标识 DESC asc
                           @RequestParam(value = "orderno", defaultValue = "") String orderno //订单编号
    ){
        EasyUiUtil.PageForData pageObj = orderManager.findSNList(rows, page, sort, order, orderno);
        String jsonStr = JSONObject.fromObject(pageObj).toString();
        return jsonStr;
    }

    /**
     * 订单产品数据
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/getOrderProduct", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String getOrderProduct(@RequestParam(value = "rows", defaultValue = "20") int rows, //每页数据量
                           @RequestParam(value = "page", defaultValue = "1") int page,//开始页码位置
                           @RequestParam(value = "sort", defaultValue = "") String sort,//排序字段
                           @RequestParam(value = "order", defaultValue = "") String order, //升降序标识 DESC asc
                           @RequestParam(value = "orderno", defaultValue = "") String orderno, //订单编号
                           @RequestParam(value = "m_sn", defaultValue = "") String m_sn //模块SN查询
    ){
        EasyUiUtil.PageForData pageObj = orderManager.getOrderProduct(rows, page, sort, order, orderno, m_sn);
        String jsonStr = JSONObject.fromObject(pageObj).toString();
        return jsonStr;
    }

    /**
     * 订单日志
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/getTrackData", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String getTrackData(@RequestParam(value = "rows", defaultValue = "20") int rows, //每页数据量
                           @RequestParam(value = "page", defaultValue = "1") int page,//开始页码位置
                           @RequestParam(value = "sort", defaultValue = "") String sort,//排序字段
                           @RequestParam(value = "order", defaultValue = "") String order, //升降序标识 DESC asc
                           @RequestParam(value = "orderno", defaultValue = "") String orderno //订单编号
    ){
        //前台显示时不显示翻页 所以在此默认一个较大数值
        rows = 9999;
        EasyUiUtil.PageForData pageObj = orderTrackManager.findEasyUiList(rows, page, sort, order, orderno);
        String jsonStr = JSONObject.fromObject(pageObj).toString();
        return jsonStr;
    }

    /**
     * 获取售后信息
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderno
     * @return
     */
    @RequestMapping(value = "/getSHData", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String getSHData(@RequestParam(value = "rows", defaultValue = "20") int rows, //每页数据量
                           @RequestParam(value = "page", defaultValue = "1") int page,//开始页码位置
                           @RequestParam(value = "sort", defaultValue = "") String sort,//排序字段
                           @RequestParam(value = "order", defaultValue = "") String order, //升降序标识 DESC asc
                           @RequestParam(value = "orderno", defaultValue = "") String orderno //订单编号
    ){
        //前台显示时不显示翻页 所以在此默认一个较大数值
        rows = 9999;
        EasyUiUtil.PageForData pageObj = orderManager.findSHInfoList(rows, page, sort, order, orderno);
        String jsonStr = JSONObject.fromObject(pageObj).toString();
        return jsonStr;
    }

    /**
     * 保存订单日志
     * @param orderTrack
     * @param isDiff
     * @param newDelivery
     * @return
     */
    @RequestMapping(value = "/saveTrack", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveTrack(OrderTrack orderTrack,
                            @RequestParam(value = "isDiff", defaultValue = "") String isDiff, @RequestParam(value = "newDelivery", defaultValue = "") String newDelivery){

        String amPm = orderTrack.getAmPm();
        OrderTrack orderTrackDB = orderTrackManager.findNewTrackByOrderNo(orderTrack.getOrderno());
        if (orderTrackDB == null) {
            orderTrack.setRemarks("申请人:" + orderTrack.getActionMan() + " " + orderTrack.getRemarks());
            orderTrack.setProposerMan(orderTrack.getActionMan());
            //评审开始时间
            if ("AM".equals(amPm)){
                orderTrack.setReviewStime(orderTrack.getActionDate());
            }else{
                Date d = orderTrack.getActionDate();
                d.setHours(12);
                orderTrack.setReviewStime(d);
            }
        }else{
            /**************************************************
             * “其他”时间添加相关人员，评审交期和各状态人员操作
             *************************************************/

            if (1 == orderTrack.getOrderStatus()){
                orderTrack.setRemarks("申请人:" + orderTrack.getActionMan() + " " + orderTrack.getRemarks());
                orderTrack.setProposerMan(orderTrack.getActionMan());
                //评审开始时间
                if ("AM".equals(amPm)){
                    orderTrack.setReviewStime(orderTrack.getActionDate());
                }else{
                    Date d = orderTrack.getActionDate();
                    d.setHours(12);
                    orderTrack.setReviewStime(d);
                }
            }
            else if (2 == orderTrack.getOrderStatus()){
                orderTrack.setRemarks("评审人:" + orderTrack.getActionMan() + " " + orderTrack.getRemarks());
                orderTrack.setReviewMan(orderTrack.getActionMan());
                //评审结束时间
                if ("AM".equals(amPm)){
                    orderTrack.setReviewEtime(orderTrack.getActionDate());
                }else{
                    Date d = orderTrack.getActionDate();
                    d.setHours(12);
                    orderTrack.setReviewEtime(d);
                }
                //补充
                orderTrack.setProposerMan(orderTrackDB.getProposerMan());
                orderTrack.setReviewStime(orderTrackDB.getReviewStime());
                if("其他".equals(orderTrack.getActionName())){
                    //补充时长
                    orderTrack.setReviewDevelopDuration(orderTrackDB.getReviewDevelopDuration());
                    orderTrack.setReviewTestDuration(orderTrackDB.getReviewTestDuration());
                }
            }
            else if (3 == orderTrack.getOrderStatus()){
                orderTrack.setRemarks("接收人:" + orderTrack.getActionMan() + " " + orderTrack.getRemarks());
                //补充
                orderTrack.setProposerMan(orderTrackDB.getProposerMan());
                orderTrack.setReviewStime(orderTrackDB.getReviewStime());
                orderTrack.setReviewEtime(orderTrackDB.getReviewEtime());
                orderTrack.setReviewMan(orderTrackDB.getReviewMan());
                orderTrack.setReviewDelivery(orderTrackDB.getReviewDelivery());

                //补充时长
                orderTrack.setReviewDevelopDuration(orderTrackDB.getReviewDevelopDuration());
                orderTrack.setReviewTestDuration(orderTrackDB.getReviewTestDuration());
            }
            else if (4 == orderTrack.getOrderStatus()){
                orderTrack.setRemarks("开发人:" + orderTrack.getActionMan() + " " + orderTrack.getRemarks());
                //开发人
                orderTrack.setDevelopMan(orderTrack.getActionMan());
                /*//开发交期
                orderTrack.setDevelopDelivery(orderTrack.getDevelopDelivery());*/
                //开发开始时间
                if ("AM".equals(amPm)){
                    orderTrack.setDevelopStime(orderTrack.getActionDate());
                }else{
                    Date d = orderTrack.getActionDate();
                    d.setHours(12);
                    orderTrack.setDevelopStime(d);
                }
                //补充
                orderTrack.setProposerMan(orderTrackDB.getProposerMan());
                orderTrack.setReviewStime(orderTrackDB.getReviewStime());
                orderTrack.setReviewEtime(orderTrackDB.getReviewEtime());
                orderTrack.setReviewMan(orderTrackDB.getReviewMan());
                orderTrack.setReviewDelivery(orderTrackDB.getReviewDelivery());
                //补充时长
                orderTrack.setReviewDevelopDuration(orderTrackDB.getReviewDevelopDuration());
                orderTrack.setReviewTestDuration(orderTrackDB.getReviewTestDuration());
            }
            else if (5 == orderTrack.getOrderStatus()){
                orderTrack.setRemarks("开发人:" + orderTrack.getActionMan() + " " + orderTrack.getRemarks());
                //开发人
                orderTrack.setDevelopMan(orderTrack.getActionMan());
                //开发结束时间
                if ("AM".equals(amPm)){
                    orderTrack.setDevelopEtime(orderTrack.getActionDate());
                }else{
                    Date d = orderTrack.getActionDate();
                    d.setHours(12);
                    orderTrack.setDevelopEtime(d);
                }
                //补充
                orderTrack.setProposerMan(orderTrackDB.getProposerMan());
                orderTrack.setReviewStime(orderTrackDB.getReviewStime());
                orderTrack.setReviewEtime(orderTrackDB.getReviewEtime());
                orderTrack.setReviewMan(orderTrackDB.getReviewMan());
                orderTrack.setReviewDelivery(orderTrackDB.getReviewDelivery());
                orderTrack.setDevelopDelivery(orderTrackDB.getDevelopDelivery());
                orderTrack.setDevelopStime(orderTrackDB.getDevelopStime());

                //补充时长
                orderTrack.setReviewDevelopDuration(orderTrackDB.getReviewDevelopDuration());
                orderTrack.setReviewTestDuration(orderTrackDB.getReviewTestDuration());
            }
            else if (6 == orderTrack.getOrderStatus()){
                orderTrack.setRemarks("测试人:" + orderTrack.getActionMan() + " " + orderTrack.getRemarks());
                //测试人
                orderTrack.setTestMan(orderTrack.getActionMan());
                /*//测试交期
                orderTrack.setTestDelivery(orderTrack.getTestDelivery());*/
                //测试开始时间
                if ("AM".equals(amPm)){
                    orderTrack.setTestStime(orderTrack.getActionDate());
                }else{
                    Date d = orderTrack.getActionDate();
                    d.setHours(12);
                    orderTrack.setTestStime(d);
                }
                //补充
                orderTrack.setProposerMan(orderTrackDB.getProposerMan());
                orderTrack.setReviewStime(orderTrackDB.getReviewStime());
                orderTrack.setReviewEtime(orderTrackDB.getReviewEtime());
                orderTrack.setReviewMan(orderTrackDB.getReviewMan());
                orderTrack.setReviewDelivery(orderTrackDB.getReviewDelivery());
                orderTrack.setDevelopDelivery(orderTrackDB.getDevelopDelivery());
                orderTrack.setDevelopMan(orderTrackDB.getDevelopMan());
                orderTrack.setDevelopStime(orderTrackDB.getDevelopStime());
                orderTrack.setDevelopEtime(orderTrackDB.getDevelopEtime());

                //补充时长
                orderTrack.setReviewDevelopDuration(orderTrackDB.getReviewDevelopDuration());
                orderTrack.setReviewTestDuration(orderTrackDB.getReviewTestDuration());
                orderTrack.setDevelopDuration(orderTrackDB.getDevelopDuration());
            }
            else if (7 == orderTrack.getOrderStatus()){
                orderTrack.setRemarks("测试人:" + orderTrack.getActionMan() + " " + orderTrack.getRemarks());
                //测试人
                orderTrack.setTestMan(orderTrack.getActionMan());

                //测试开始时间
                if ("AM".equals(amPm)){
                    orderTrack.setTestEtime(orderTrack.getActionDate());
                }else{
                    Date d = orderTrack.getActionDate();
                    d.setHours(12);
                    orderTrack.setTestEtime(d);
                }
                //补充
                orderTrack.setProposerMan(orderTrackDB.getProposerMan());
                orderTrack.setReviewStime(orderTrackDB.getReviewStime());
                orderTrack.setReviewEtime(orderTrackDB.getReviewEtime());
                orderTrack.setReviewMan(orderTrackDB.getReviewMan());
                orderTrack.setReviewDelivery(orderTrackDB.getReviewDelivery());
                orderTrack.setDevelopDelivery(orderTrackDB.getDevelopDelivery());
                orderTrack.setDevelopMan(orderTrackDB.getDevelopMan());
                orderTrack.setDevelopStime(orderTrackDB.getDevelopStime());
                orderTrack.setDevelopEtime(orderTrackDB.getDevelopEtime());
                orderTrack.setTestDelivery(orderTrackDB.getTestDelivery());
                orderTrack.setTestStime(orderTrackDB.getTestStime());

                //补充时长
                orderTrack.setReviewDevelopDuration(orderTrackDB.getReviewDevelopDuration());
                orderTrack.setReviewTestDuration(orderTrackDB.getReviewTestDuration());
                orderTrack.setDevelopDuration(orderTrackDB.getDevelopDuration());
            }
            else if (9 == orderTrack.getOrderStatus() || 10 == orderTrack.getOrderStatus()){
                //补充
                orderTrack.setProposerMan(orderTrackDB.getProposerMan());
                orderTrack.setReviewStime(orderTrackDB.getReviewStime());
                orderTrack.setReviewEtime(orderTrackDB.getReviewEtime());
                orderTrack.setReviewMan(orderTrackDB.getReviewMan());
                orderTrack.setReviewDelivery(orderTrackDB.getReviewDelivery());
                orderTrack.setDevelopDelivery(orderTrackDB.getDevelopDelivery());
                orderTrack.setDevelopMan(orderTrackDB.getDevelopMan());
                orderTrack.setDevelopStime(orderTrackDB.getDevelopStime());
                orderTrack.setDevelopEtime(orderTrackDB.getDevelopEtime());
                orderTrack.setTestDelivery(orderTrackDB.getTestDelivery());
                orderTrack.setTestStime(orderTrackDB.getTestStime());
                orderTrack.setTestEtime(orderTrackDB.getTestEtime());
                orderTrack.setTestMan(orderTrackDB.getTestMan());

                //补充时长
                orderTrack.setReviewDevelopDuration(orderTrackDB.getReviewDevelopDuration());
                orderTrack.setReviewTestDuration(orderTrackDB.getReviewTestDuration());
                orderTrack.setDevelopDuration(orderTrackDB.getDevelopDuration());
                orderTrack.setTestDuration(orderTrackDB.getTestDuration());
            }
        }

        if("true".equals(isDiff)){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date myDate = dateFormat.parse(newDelivery);
                orderTrack.setReviewDelivery(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        orderTrack.setOptTime(new Date());
        orderTrack.setOptUserId(ShiroUtil.getCurrentUser().getId());
        orderTrack.setIsNew(1);
        orderTrack.setStatus(1);

        orderTrackManager.saveOrderTrack(orderTrack);
        return "ok";
    }

    /**
     * 删除订单日志
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/delOrderTrack", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delOrderTrack(@RequestParam(value = "orderNo", defaultValue = "") String orderNo)
    {
        OrderTrack orderTrack = orderTrackManager.findNewTrackByOrderNo(orderNo);
        //Objects.requireNonNull(orderTrack);
        if (orderTrack != null) {
            orderTrackManager.delOrderTrackById(orderNo, orderTrack.getId());
        }
        return "ok";
    }

    //
    @RequestMapping(value = "/getOrderDataByCorp", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String getOrderDataByCorp(@RequestParam(value = "rows", defaultValue = "20") int rows, //每页数据量
                               @RequestParam(value = "page", defaultValue = "1") int page,//开始页码位置
                               @RequestParam(value = "sort", defaultValue = "") String sort,//排序字段
                               @RequestParam(value = "order", defaultValue = "") String order, //升降序标识 DESC asc
                               @RequestParam(value = "orderno", defaultValue = "") String orderno, //订单编号
                               @RequestParam(value = "corpid", defaultValue = "") String corpid //客户ID
    ){
        //前台显示时不显示翻页 所以在此默认一个较大数值
        rows = 9999;
        EasyUiUtil.PageForData pageObj = orderManager.findEasyUiOrderList(rows, page, sort, order, orderno, corpid);
        String jsonStr = JSONObject.fromObject(pageObj).toString();
        return jsonStr;
    }

}
