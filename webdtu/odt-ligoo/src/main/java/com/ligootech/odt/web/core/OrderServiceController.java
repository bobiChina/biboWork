package com.ligootech.odt.web.core;

import com.ligootech.odt.web.util.ShiroUtil;
import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.entity.core.Order;
import com.ligootech.webdtu.service.dtu.DtuManager;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import com.ligootech.webdtu.service.order.FileManager;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.webdtu.service.order.OrderTrackManager;
import com.ligootech.webdtu.util.FileOperateUtil;
import com.ligootech.webdtu.util.LoggerUtil;
import com.ligootech.webdtu.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/18.
 */
@Controller
@RequestMapping("/orderService")
public class OrderServiceController {
    public static final int PAGE_SIZE = 15;

    protected final Log logger = LogFactory.getLog(this.getClass());

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

        return "order/orderList";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String forwardOrderTrackDetail(Model model, @RequestParam(value = "orderno", defaultValue = "") String orderno){
        /******************************************
         * 初始化订单
         ******************************************/
        Order order = orderManager.findInfoByOrderNo(orderno);

        model.addAttribute("order", order);
        if (order != null) {
            // 订单日志
            model.addAttribute("trackLogList", orderTrackManager.findOrderTrackByOrderNo(orderno));
            // 设备信息（系统配置）
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
        return "order/orderDetail";
    }

    @RequestMapping(value = "/listQuery", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String dtuList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                          @RequestParam(value = "orderno", defaultValue = "") String orderno,
                          HttpServletRequest req){

        Subject subject = SecurityUtils.getSubject();
        Boolean isOdtadmin = subject.hasRole("odtadmin");	//销售经理

        Integer count = orderManager.findOrderCount4Page(req);

        int pageCount = count /PAGE_SIZE;
        int mod = count%PAGE_SIZE;
        if (mod>0)
            pageCount += 1;
        if (pageCount ==0)
            pageCount += 1;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageCount", pageCount);
        map.put("result", orderManager.findOrderList4Page(req, PAGE_SIZE));
        map.put("pageNumber", pageNumber);
        JSONObject object = JSONObject.fromObject(map);
        return object.toString();
    }

    @RequestMapping(value = "/downloadOrderZip")
    public String downloadConfigFile(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "orderno", defaultValue = "") String orderno) throws Exception {
        String contentType = "application/octet-stream";

        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), request, logger);

        /******************************************************
         * 测试地址：http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201602230001&fileType=csf&deviceCode=BC52B&fileName=201512021441233283781051173&bmuNo=0
         *****************************************************/

        String errorMsg = "";
        /**********************************************
         * 空值判断
         *********************************************/
        if (orderno == null || "".equals(orderno)) {
            errorMsg = "订单编号不能为空";
        }

        if (!"".equals(errorMsg)){
            logger.debug("错误信息：" + errorMsg);
            FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
            return null;
        }
        String storeName = "";
        /**********************************************
         * 查找订单信息
         *********************************************/
        Order order = orderManager.findInfoByOrderNo(orderno);
        if (order == null) {
            logger.debug("错误信息：订单不存在");
            FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
            return null;
        }
        // 下载文件名 DD_LG201400001_南京XXX_60S_1T   orderObj.batteryModel.batteryNumber
        storeName = "DD_" + order.getOrderno() + "_" + order.getCorp().getCorpName() + "_" + order.getBatteryModel().getBatteryNumber() + "S_" + order.getQuantity() + "T.zip";;
        /**********************************************
         * 查找订单的设备信息
         *********************************************/
        List<Map<String, Object>> devices = orderManager.findOrderDeviceAll(orderno);
        if (null == devices || devices.size() == 0){
            logger.debug("错误信息：订单软件不存在");
            FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
            return null;
        }

        logger.debug("下载文件名：" + storeName);

        try {
            FileOperateUtil.download4Order(request, response, contentType, storeName, devices);
        } catch (Exception e) {
            FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
            e.printStackTrace();
        }

        return null;
    }

}
