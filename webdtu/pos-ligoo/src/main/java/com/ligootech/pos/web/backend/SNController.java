package com.ligootech.pos.web.backend;

import com.ligootech.webdtu.service.email.EmailManager;
import com.ligootech.webdtu.entity.core.*;
import com.ligootech.webdtu.entity.core.clientForm.*;
import com.ligootech.webdtu.service.order.SNManager;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.webdtu.util.LoggerUtil;
import com.ligootech.webdtu.util.SNCodeUtil;
import com.ligootech.webdtu.util.StringUtil;
import javassist.NotFoundException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wly on 2015/9/23 15:23.
 */
@Transactional
@Controller
@RequestMapping("/sn")
public class SNController {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    SNManager snManager;

    @Autowired
    EmailManager emailManager;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private OrderManager orderManager;

    @RequestMapping(value = "/getuser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findUsersByFullNameAjax(@RequestParam(value = "username") String username, HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        List<Object[]> dtuUsers =  snManager.findUserByFullname(username);
        String rsStr = JSONArray.fromObject(dtuUsers).toString();

        return rsStr;
    }

    @RequestMapping(value = "/userlogin", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findUsersByLoginAjax(@RequestParam(value = "username", defaultValue = "") String username,
                                       @RequestParam(value = "userpass", defaultValue = "") String userpass,
                                       @RequestParam(value = "logintype", defaultValue = "") String logintype,
                                       HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        /*
        module_sn_code_scanneri 模块SN扫码
        s 联调数据上报
        p PCBA板扫描
         */

        DtuUser user = snManager.findDtuUserByLogin(username, userpass);

        if(null != user){
            //判断权限
            String role_code = "";
            if ("module_sn_code_scanneri".equals(logintype)){//模块扫码
                role_code = "module_scanner";
            }
            else if("s".equals(logintype)){//系统联调
                role_code = "system_joint_user";
            }
            else if("p".equals(logintype)){//PCBA扫码
                role_code = "pcba_scanner";
            }
            else if("t".equals(logintype)){//工装
                role_code = "tooling_user";
            }

            boolean checkUserPermission = snManager.checkUserPermission(user.getId(), role_code);
            if (checkUserPermission){
                String str = putRSinfo("1" , user);
                return str;
            }
            return putRSinfo("-1" , "用户权限不足");
        }
        return putRSinfo("-1" , "用户不存在或密码错误");
    }

    @RequestMapping(value = "/getOrder", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderByOrderNo(@RequestParam(value = "orderno") String orderNo, HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        Order order = orderManager.findInfoByOrderNo(orderNo);
        String rsStr = "-1";
        if (order != null) {
            JSONObject json = JSONObject.fromObject(order);
            rsStr = json.toString();
        }
        return rsStr;
    }

    /**
     * 获取订单信息，带设备信息以及设备的文件信息
     * @param orderNo
     * @param req
     * @return
     */
    @RequestMapping(value = "/getOrder4SystemJoint", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String getOrder4SystemJoint(@RequestParam(value = "orderno") String orderNo, HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        Order order = orderManager.findInfoByOrderNo(orderNo);
        String rsStr = "-1";
        if (order != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            //JSONObject json = JSONObject.fromObject(order);
            //rsStr = json.toString();
            map.put("order", order);
            /**************************************
             * 查询订单对应的设备信息 以及软件编码
             **************************************/
            List<Map<String, Object>> list = orderManager.findOrderDevice(orderNo);
            map.put("devices", list);

            rsStr = JSONObject.fromObject(map).toString();
        }
        return rsStr;
    }

    @RequestMapping(value = "/delSNById", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delSNById(@RequestParam(value = "snid") Long snid, @RequestParam(value = "optuserid") String optuserid, HttpServletRequest req){

        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        int rs = snManager.delSNById_scan(snid, Long.parseLong(optuserid));

        return 1 + "";
    }

    /**
     * 获取订单编号
     * @param orderNo
     * @param startnum
     * @param endnum
     * @return
     */
    @RequestMapping(value = "/getOrderNo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findOrderOrderNo(@RequestParam(value = "orderno") String orderNo, @RequestParam(value = "startnum") int startnum,
                                   @RequestParam(value = "endnum") int endnum,
                                   HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

       Map<String, Object> map = new HashMap<String, Object>();
       map.put("startnum", startnum);
       map.put("endnum", endnum);
       if (orderNo == null) {
           orderNo = "";
       }
       List<String> result = orderManager.findOrderNo(orderNo, startnum, endnum);

        if (result == null){
            map.put("result", new ArrayList<String>());
            map.put("realnum", 0 );
        }
        else{
            if(startnum > 0){
                startnum = startnum -1;
            }
            map.put("realnum", result.size() );
            map.put("result", result);
        }
        JSONObject json = JSONObject.fromObject(map);
        String rsStr = "-1";
        rsStr = json.toString();
        return rsStr;
    }

    /**
     * 校验SN码是否在订单内
     * @param orderNo
     * @param productSn
     * @param req
     * @return
     */
    @RequestMapping(value = "/checkOrderProductSn", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String checkOrderProductSn(@RequestParam(value = "orderNo", defaultValue = "") String orderNo,
                                     @RequestParam(value = "productSn", defaultValue = "") String productSn,
                                   HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        orderNo = StringUtil.null2String(orderNo).trim();
        productSn = StringUtil.null2String(productSn).trim();
        if ("".equals(orderNo) || "".equals(productSn)){
            return putRSinfo("-1", "输入参数不能为空");
        }

        SnModule rs = snManager.checkOrderProductSn(orderNo, productSn);

        if (null != rs && !"".equals(rs) ){
            return putRSinfo("1", rs);
        }
        return putRSinfo("-2", "订单不包含该产品SN码");
    }

    /**
     * 检查是否走工装测试
     * @param orderNo
     * @param productSn
     * @param req
     * @return
     */
    @RequestMapping(value = "/checkProductSnFromTool", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String checkProductSnFromTool(@RequestParam(value = "orderNo", defaultValue = "") String orderNo,
                                     @RequestParam(value = "productSn", defaultValue = "") String productSn,
                                   HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        orderNo = StringUtil.null2String(orderNo).trim();
        productSn = StringUtil.null2String(productSn).trim();
        if ("".equals(orderNo) || "".equals(productSn)){
            return putRSinfo("-1", "输入参数不能为空");
        }

        /*******************************************
         * 无记录时返回-2 测试不通过 -1 测试通过 1
         ******************************************/
        int rs = snManager.checkProductSnFromTool(orderNo, productSn);
        if (rs == 1){
            return putRSinfo("1", "工装测试通过");
        }else if (rs == -1){
            return putRSinfo("-2", "工装测试不通过");
        }else if (rs == -2){
            return putRSinfo("-3", "未经过工装测试");
        }

        return putRSinfo("-99", "系统错误");
    }

    /**
     * 获取硬件版本号
     * @param prodtype
     * @return
     */
    @RequestMapping(value = "/getHardwareVersion", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findHardwareVersion(@RequestParam(value = "prodtype") String prodtype, HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        List<String> result = snManager.findHardwareVersion(prodtype);

        if (result == null){
            return "-1";
        }
        else{
            String str = JSONArray.fromObject(result).toString();
            return str;
        }
    }

    /**
     * 获取PCB板SN信息
     * @param main_sn
     * @return
     */
    @RequestMapping(value = "/getPcbSn", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findPcbSn(@RequestParam(value = "main_sn") String main_sn, HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);


        List<Map<String, String>> result = snManager.findPcbSn(main_sn);
        if (result == null || result.size() == 0){
            return "-1";
        }
        else{
            String str = JSONArray.fromObject(result).toString();
            return str;
        }
    }

    /**
     * 获取UUID的SN码信息
     * @param uuids
     * @return
     */
    @RequestMapping(value = "/getUuidSn", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
         @ResponseBody
         public String findUuidSn(@RequestParam(value = "uuids", defaultValue ="" ) String uuids,
                                  @RequestParam(value = "sns", defaultValue ="" ) String sns,
                                  HttpServletRequest req
    ) {

        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        if ("".equals(uuids.trim()) && "".equals(sns.trim())){
            String str = putRSinfo("-1", "输入参数不能都为空");
            return str;
        }
        //UUID转化为小写
        List<String> result_uuid = snManager.findUuidDB(uuids.toLowerCase());
        List<String> result_sn = snManager.findSnDB(sns);

        if (result_uuid.size() > 0 || result_sn.size() > 0){
            Map<String, List<String>> map = new HashMap<>();
            map.put("uuids", result_uuid);
            map.put("sns", result_sn);
            String str = putRSinfo("-2", map);
            return str;
        }

        //正常时也返回UUID和SN
        String[] uuidsArr = uuids.split(",");
        List<String> ok_uuid = new ArrayList<String>();
        for (int i = 0; i <uuidsArr.length ; i++) {
            String str = StringUtil.null2String(uuidsArr[i]);
            if (!"".equals(str)){
                ok_uuid.add(str);
            }
        }

        String[] snsArr = sns.split(",");
        List<String> ok_sn = new ArrayList<String>();
        for (int i = 0; i <snsArr.length ; i++) {
            String str = StringUtil.null2String(snsArr[i]);
            if (!"".equals(str)){
                ok_sn.add(str);
            }
        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("uuids", ok_uuid);
        map.put("sns", ok_sn);
        String str = putRSinfo("1", map);

        return str;
    }

    /**
     * 工装数据上报
     * @param json_data
     * @param req
     * @return
     */
    @RequestMapping(value = "/saveToolResult", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveToolResult(@RequestParam(value = "json_data", defaultValue ="" ) String json_data, HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        if (json_data == null || "".equals(json_data)) {
            return putRSinfo("-1", "工装上报数据不能为空");
        }

        JSONObject jsonObject = JSONObject.fromObject(json_data);
        //先定义好内部的对象
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("detail", Map.class);
        classMap.put("files", Map.class);

        ToolInfo obj = (ToolInfo) JSONObject.toBean(jsonObject, ToolInfo.class, classMap);

        if (obj == null) {
            return putRSinfo("-2", "工装上报数据格式不正确。上报数据为：" + json_data);
        }

        if ("".equals(StringUtil.null2String(obj.getUser_id()))) {
            return putRSinfo("-3", "上报人不能为空");
        }

        if ("".equals(StringUtil.null2String(obj.getUuid()))) {
            return putRSinfo("-4", "UUID不能为空"); //TODO 允许uuid为空 SN码不能为空 订单编号不能为空 2016年6月30日 17:13:14(未修改)
        }

        Map<String, String> map = obj.getDetail();

        if (map == null || map.size() == 0) {
            return putRSinfo("-5", "上报结果信息不能为空");
        }

        /**********************************************
         * 检查订单对应的设备型号是否匹配订单所属设备型号
         *********************************************/
        int device_num = snManager.checkDeciveByOrderNo(obj.getName(), obj.getOrder_no());
        if (device_num > 0){

        }else{
            String orderno = obj.getOrder_no();
            if (orderno.indexOf("LG-SH") > -1 || orderno.indexOf("lg-sh") > -1){

            }else{
                return putRSinfo("-6", "模块SN码与订单所属设备类型不匹配");
            }
        }
        /**********************************************
         * 检查UUID是否被使用，已被使用且未解绑的的UUID
         * sby 2016年9月18日 11:47:40
         *********************************************/
        String sn = StringUtil.null2String(obj.getSn());
        if ("".equals(sn)) {
            return putRSinfo("-7", "模块SN不能为空");
        }
        String m_sn = snManager.findMSNByUUID(StringUtil.null2String(obj.getUuid()));
        if (!"".equals(m_sn) && !sn.equals(m_sn)){
            return putRSinfo("-8", "UUID已绑定，请先解绑");
        }

        int rs = snManager.saveToolInfo(obj);

        if (rs > 0){
            return putRSinfo("1", "OK");
        }

        return putRSinfo("-99", "系统错误");
    }

    /**
     * B型工装数据上报
     * 加锁确保设备数量正确返回
     * @param json_data
     * @param req
     * @return
     */
    @RequestMapping(value = "/saveToolResult_B", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public synchronized String saveToolResult_B(@RequestParam(value = "json_data", defaultValue ="" ) String json_data, HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        if (json_data == null || "".equals(json_data)) {
            return putRSinfo("-1", "工装上报数据不能为空");
        }

        JSONObject jsonObject = JSONObject.fromObject(json_data);
        //先定义好内部的对象
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("lists", ToolDetail.class);
        classMap.put("detail", Map.class);
        classMap.put("files", Map.class);

        ToolInfoB obj = (ToolInfoB) JSONObject.toBean(jsonObject, ToolInfoB.class, classMap);

        if (obj == null) {
            return putRSinfo("-2", "工装上报数据格式不正确。上报数据为：" + json_data);
        }

        if ("".equals(StringUtil.null2String(obj.getUser_id()))) {
            return putRSinfo("-3", "上报人不能为空");
        }

        if ("".equals(StringUtil.null2String(obj.getUuid()))) {
            return putRSinfo("-4", "UUID不能为空");
        }
        if ("".equals(StringUtil.null2String(obj.getSn()))) {
            return putRSinfo("-5", "模块SN不能为空");
        }

        List<ToolDetail> lists = obj.getLists();
        if (lists == null || lists.size() == 0) {
            return putRSinfo("-6", "上报结果信息不能为空");
        }
        /**********************************************
         * 检查订单对应的设备型号是否匹配订单所属设备型号
         *********************************************/
        int device_num = snManager.checkDeciveByOrderNo(obj.getName(), obj.getOrder_no());
        if (device_num > 0){

        }else{
            String orderno = obj.getOrder_no();
            if (orderno.indexOf("LG-SH") > -1 || orderno.indexOf("lg-sh") > -1){

            }else{
                return putRSinfo("-8", "模块SN码与订单所属设备类型不匹配");
            }
        }
        /**************************************
         * 检查设备数量是否已达到规定数量
         **************************************/
        String orderNo = obj.getOrder_no();
        String device_name = obj.getName();
        int bmu_id = StringUtil.StringToInt(StringUtil.null2String(obj.getBmu_id()));

        int toolDeviceCount = snManager.findToolDeviceCount(orderNo, device_name, bmu_id);
        int orderDeviceCount = snManager.findOrderDeviceCount(orderNo, device_name, bmu_id);
        if (toolDeviceCount == orderDeviceCount || toolDeviceCount > orderDeviceCount ){
            String rsStr = "设备型号：" + device_name ;
            if (bmu_id > 0){
                rsStr += " 从机ID：" + bmu_id ;
            }
            rsStr += " 数量已达上线，不能添加";
            // return putRSinfo("-7", rsStr ); 数量不做限制
        }

        int rs = snManager.saveToolInfoB(obj);

        if (rs > 0){
            //返回当前型号设备的数量  有效的可用的
            Map<String, String> map = new HashMap<String, String>();
            int rsToolDeviceCount = snManager.findToolDeviceCount(orderNo, device_name, bmu_id);
            map.put("device_count", String.valueOf(rsToolDeviceCount));
            map.put("device_name", device_name);
            map.put("sn", StringUtil.null2String(obj.getSn()));
            return putRSinfo("1", map);
        }

        return putRSinfo("-99", "系统错误");
    }

    /**
     * 系统联调数据上报
     * @param json_data
     * @return
     */
    @RequestMapping(value = "/saveSystemJoint", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveSystemJoint(@RequestParam(value = "json_data", defaultValue ="" ) String json_data, HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        if (json_data == null || "".equals(json_data)) {
            return putRSinfo("-1", "联调上报数据不能为空");
        }

        JSONObject jsonObject = JSONObject.fromObject(json_data);
        //先定义好内部的对象
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("lists", SystemJointInfo.class);
        classMap.put("detail", Map.class);

        SystemJoint obj = (SystemJoint) JSONObject.toBean(jsonObject, SystemJoint.class, classMap);

        if (obj == null) {
            return putRSinfo("-2", "联调上报数据格式不正确。上报数据为：" + json_data);
        }

        if ("".equals(StringUtil.null2String(obj.getUser_id()))) {
            return putRSinfo("-3", "上报人不能为空");
        }

        if (null == obj.getLists() || obj.getLists().size() == 0){
            return putRSinfo("-4", "设备上报信息不能为空");
        }

        int rs = snManager.saveSystemJoint(obj);

        if (rs > 0){
            return putRSinfo("1", "OK");
        }

        return putRSinfo("-99", "系统错误");
    }

    /**
     * PCN板SN码保存
     * @param model
     * @param optuserid
     * @param main_sn
     * @param pcb_sns
     * @return
     */
    @RequestMapping(value = "/savePcbSn", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String savePCBSNAjax(Model model, @RequestParam(value = "optuserid") String optuserid,
                             @RequestParam(value = "main_sn") String main_sn,
                             @RequestParam(value = "pcb_sns", defaultValue = "") String pcb_sns,
                                HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        if (optuserid == null || "".equals(optuserid.trim())) {
            return putRSinfo("-1", "操作人不能为空");
        }

        if (main_sn == null || "".equals(main_sn.trim())) {
            return putRSinfo("-2", "模块SN码不能为空");
        }
        /**
         * 验证模块SN
         * 长度15，字母数字组合 2开头
         */
        String match = "^2[A-Za-z0-9]{14}$";
        Pattern pattern = Pattern.compile(match, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(main_sn);
        if (! matcher.matches()){
            return putRSinfo("-6", "模块sn信息编码不正确");
        }

      /*  PCBA板允许为空
      if (pcb_sns == null || "".equals(pcb_sns.trim())) {
            return putRSinfo("-3", "PCB板SN码不能为空");
        }*/

        int errorCode = snManager.checkModuleSNPcbaSN(main_sn, pcb_sns);
        if (errorCode == -11){
            return putRSinfo("-5", "模块sn信息不正确");
        }
        /*int errorCode = snManager.checkModuleSNPcbaSN(main_sn, pcb_sns);
        if (errorCode == -11){
            return putRSinfo("-5", "模块sn信息不正确");
        }
        if (errorCode == -12){
            return putRSinfo("-6", "PCBA板扫码总数不匹配");
        }
        else if (errorCode == -13){
            return putRSinfo("-7", "主板不匹配");
        }
        else if (errorCode == -14){
            return putRSinfo("-8", "采集板数量不正确");
        }
        else if (errorCode == -15){
            return putRSinfo("-9", "均衡板数量不正确");
        }
        else if (errorCode == -16){
            return putRSinfo("-10", "温感板数量不正确");
        }
        else if (errorCode == -17){
            return putRSinfo("-11", "DTU板数量不正确");
        }
       */


        int rs = snManager.savePCBSN(main_sn, pcb_sns, optuserid);

        if (rs == -4){
            return putRSinfo("-4", "PCB板SN码为空");
        }else if (rs == -5){
            return putRSinfo("-99", "保存失败");
        }

        return putRSinfo(rs + "", "OK");

    }

    /**
     * PCBA扫码信息保存
     * @param model
     * @param optuserid
     * @param main_sn
     * @param pcb_sns
     * @param req
     * @return
     */
    @RequestMapping(value = "/savePcbaSn_B", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String savePcbaSn_B(Model model, @RequestParam(value = "optuserid", defaultValue = "") String optuserid,
                             @RequestParam(value = "main_sn", defaultValue = "") String main_sn,
                             @RequestParam(value = "pcb_sns", defaultValue = "") String pcb_sns,
                             @RequestParam(value = "hw_version", defaultValue = "") String hw_ersion,
                             @RequestParam(value = "orderno", defaultValue = "") String orderno,
                                HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        optuserid = StringUtil.null2String(optuserid).trim();
        main_sn = StringUtil.null2String(main_sn).trim();
        orderno = StringUtil.null2String(orderno).trim();
        hw_ersion = StringUtil.null2String(hw_ersion).trim();
        pcb_sns = StringUtil.null2String(pcb_sns).trim();

        if ("".equals(optuserid) || "".equals(main_sn) || "".equals(orderno) || "".equals(hw_ersion))  {
            return putRSinfo("-1", "输入参数不能为空");
        }

        /**
         * 验证模块SN
         * 长度15，字母数字组合 2开头
         */
        String match = "^2[A-Za-z0-9]{14}$";
        Pattern pattern = Pattern.compile(match, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(main_sn);
        if (! matcher.matches()){
            return putRSinfo("-2", "模块sn信息编码不正确");
        }

        int errorCode = snManager.checkModuleSNPcbaSN(main_sn, pcb_sns);
        if (errorCode == -11){
            return putRSinfo("-3", "模块sn信息不正确");
        }else if (errorCode == -12){
            //2016年10月8日 17:06:11 sby 特殊配置用户可不用验证PCBA扫码数量
            boolean userP = orderManager.checkBaseConfig(optuserid, 3, true);
            if (! userP){
                return putRSinfo("-4", "PCBA板数量不正确");
            }
        }

        /**********************************************
         * 检查订单对应的设备型号是否匹配订单所属设备型号
         *********************************************/
        SnModule snModule = snManager.getSnModuleByCode(main_sn);
        if (null != snModule){
            int device_num = snManager.checkDeciveByOrderNo(snModule.getType(), orderno);
            if (device_num > 0) {

            }else{
                if (orderno.indexOf("LG-SH") > -1 || orderno.indexOf("lg-sh") > -1){

                }else{
                    return putRSinfo("-5", "模块SN码与订单所属设备类型不匹配");
                }
            }
        }
        //检查PCBA板使用情况
        if (!"".equals(pcb_sns)){
            int pcba_num = snManager.checkPcbaForBi(main_sn, pcb_sns);
            if (pcba_num > 0){
                String str = snManager.findNoPassPcbaMSn(pcb_sns);
                return putRSinfo("-6", "PCBA板与" + str + "已绑定");
            }
        }

        /* 2016年10月8日 17:11:37 sby 数量不做限制
        //订单允许数量
        int orderSum = snManager.findOrdetrDeviceSum(orderno, snModule.getType());
        //PCBA已扫描数量
        int pcbaSum = snManager.findPCBADeviceSum(orderno, snModule.getType());
        if (orderSum > pcbaSum){

        }else{
            return putRSinfo("-7", snModule.getType() + "扫码数量已达到上限");
        }*/


        int rs = snManager.savePCBSN_B(main_sn, pcb_sns, orderno, hw_ersion, optuserid);

        if (rs > 0){
            Map<String, String> map = new HashMap<String, String>();
            map.put("device_num", String.valueOf(snManager.findPCBADeviceSum(orderno, snModule.getType())));
            return putRSinfo("1", map);
        }else {
            return putRSinfo("-99", "系统错误，保存失败");
        }
    }


    /**
     * 保存SN码数据
     * @param model
     * @return
     */
    @RequestMapping(value = "/savesn", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveSNAjax(Model model, @RequestParam(value = "optuserid", defaultValue = "") String optuserid,
                             @RequestParam(value = "uuid", defaultValue = "") String uuid,
                             @RequestParam(value = "sn", defaultValue = "") String sn,
                             @RequestParam(value = "orderno", defaultValue = "") String orderno,
                             @RequestParam(value = "corpid", defaultValue = "") String corpid,
                             @RequestParam(value = "prod_type", defaultValue = "") String prod_type,
                             @RequestParam(value = "hw_version", defaultValue = "") String hw_version,
                             @RequestParam(value = "sw_version", defaultValue = "") String sw_version,
                             @RequestParam(value = "temp_sn", defaultValue = "") String temp_sn,
                             @RequestParam(value = "submit_type", defaultValue="0") int submit_type , //0 - 默认非强制提交 1-强制提交
                             @RequestParam(value = "pcba_type", defaultValue="") String pcba_type , //设备读取的主板型号
                             SNInfo snObj,
                             HttpServletRequest req
                             ){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);


        Map<String, String> map = new HashMap<String, String>();

        if (optuserid == null || "".equals(optuserid.trim())) {
            return putRSinfo("-1", "操作人不能为空");
        }
        if (orderno == null || "".equals(orderno.trim())) {
            return putRSinfo("-2", "订单编号不能为空");
        }
        map.put("optuserid", optuserid.trim());

        if (uuid == null || "".equals(uuid.trim())) {
            return putRSinfo("-3", "UUID不能为空");
        }
        map.put("uuid", uuid.trim());

        if (sn == null || "".equals(sn.trim())) {
            return putRSinfo("-4", "SN码不能为空");
        }
        map.put("sn", sn.trim());
        map.put("orderno", orderno);
        //map.put("userid", userid);

        if (prod_type == null || "".equals(prod_type.trim())) {
            return putRSinfo("-5", "产品类型不能为空");
        }

        if (temp_sn == null || "".equals(temp_sn.trim())) {
            return putRSinfo("-11", "模块SN码不能为空");
        }

        map.put("prod_type", prod_type.trim());
        map.put("hw_version", hw_version);
        map.put("sw_version", sw_version);

        //以下几种产品型号的UUID不做验证 2016年1月11日10:17:00
        //String[] unCheckArr = {"M1216", "BL51A", "M1112", "BD51A", "M512"};
        //新增不做UUID验证的型号 2016年1月21日17:27:31
        Map<String, String> unCheckMap = new HashMap<String, String>();
        // BM5112A  BM5124A BM5136A BM5148A BM5160A BM5112AT BM5124AT BM5136AT BM5148AT
        unCheckMap.put("LDM", "BL51A");
        unCheckMap.put("M51(2)12", "BM5112A");
        unCheckMap.put("M51(2)24", "BM5124A");
        unCheckMap.put("M51(2)36", "BM5136A");
        unCheckMap.put("M51(2)48", "BM5148A");
        unCheckMap.put("M51(2)60", "BM5160A");
        unCheckMap.put("M51(2)12T", "BM5112AT");
        unCheckMap.put("M51(2)24T", "BM5124AT");
        unCheckMap.put("M51(2)36T", "BM5136AT");
        unCheckMap.put("M51(2)48T", "BM5148AT");

        /**
         * 新程序也有无UUID的情况
         */
        unCheckMap.put("BM5112AT", "BM5112AT");
        unCheckMap.put("BM5124AT", "BM5124AT");
        unCheckMap.put("BM5136AT", "BM5136AT");
        unCheckMap.put("BM5148AT", "BM5148AT");

        unCheckMap.put("M1216", "M1216");
        unCheckMap.put("BL51A", "BL51A");
        unCheckMap.put("M1112", "M1112");
        unCheckMap.put("BD51A", "BD51A");
        unCheckMap.put("M512", "M512");

        uuid = uuid.toLowerCase(); //UUID转化为小写
        if (submit_type == 0){// 强制提交时不验证已绑定的SN码和UUID
            //SN码覆盖
            String isUseSN = snManager.findRepetitionFromSN(sn);
            if (isUseSN != null ){
                return putRSinfo("-7", "SN码已绑定");
            }

            String oldPcba = unCheckMap.get(pcba_type);
            if (null != oldPcba){

            }else{
                //非特殊型号的设备UUID不能全为0
                if ("00000000-0000-0000-0000-000000000000".equals(uuid)){
                    return putRSinfo("-3", "UUID不能为空");
                }
                String isUseUUID = snManager.findRepetitionFromUUID(uuid, sn);
                if (isUseUUID != null){
                    return putRSinfo("-10", "UUID已绑定");
                }
            }
        }else{
            //强制提交时也需要做UUID不为空的验证 2016年1月26日11:41:33
            String oldPcba = unCheckMap.get(pcba_type);
            if (null != oldPcba){

            }else{
                //非特殊型号的设备UUID不能全为0
                if ("00000000-0000-0000-0000-000000000000".equals(uuid)){
                    return putRSinfo("-3", "UUID不能为空");
                }
            }
        }

        Long prodId = snManager.findProdTypeByName(prod_type);
        if (prodId < 0){
            return putRSinfo("-8", "产品型号不存在");
        }

        int pcba_snCheck = snManager.checkModuleSN(temp_sn, pcba_type);
        if (pcba_snCheck == -1){
            return putRSinfo("-13", "模块SN码信息不存在");
        }else if(pcba_snCheck == -2){
            return putRSinfo("-14", "模块SN码信息与设备读取信息不匹配");
        }

       //2016年1月11日10:13:05 支持不走PCBA板扫描的情况 可能只在模块上贴模块SN码，而没有录入内置PCBA板信息
       // 2016年1月13日16:23:39 必须先扫PCBA，PCBA板可以为空
        boolean isNoPcb = snManager.findPcbByMainSn(temp_sn, orderno);
        if (! isNoPcb) {
            return putRSinfo("-12", "PCB板SN码信息不存在或已使用");
        }

        /****************************************
         * 添加产品SN验证
         * 长度12位，1开头，型号在产品SN范围内
         * lzj 2016年7月7日 09:43:54
         ****************************************/
        int p_rs = snManager.checkProductSn(sn);
        if(p_rs < 0){
            return putRSinfo("-15", "产品SN码信息不匹配");
        }

        Corp corp = new Corp();
        corp.setId(Long.parseLong(corpid));
        snObj.setCorp(corp);

        ProductType prodType = new ProductType();
        prodType.setId(prodId);
        snObj.setProdType(prodType);

        snObj.setHwVersion(hw_version);
        snObj.setSwVersion(sw_version);
        snObj.setOptUserid(Long.parseLong(optuserid));
        snObj.setOptTime(new Date());
        snObj.setStatus(1);
        snObj.setTempSn(temp_sn.trim());

        //填充dtuUuid
        String uuidStr = uuid.replace("-", "");
        if(uuidStr.length() > 17){
            snObj.setDtuUuid(uuidStr.substring(uuidStr.length()-17));
        }else{
            snObj.setDtuUuid(uuidStr);
        }

        Long rsSave = -99l;
        snManager.saveSnInfo(snObj);
        rsSave = snObj.getId();
       if (rsSave > 0){
           return putRSinfo("snid" , rsSave + "");
       }else if(rsSave == -9l){
           return putRSinfo("-9", "订单不存在");
       }
       return putRSinfo("-99", "保存失败");
    }

    /**
     * 返回信息
     * @param rscode
     * @param rsmsg
     * @return
     */
    private String putRSinfo(String rscode, Object rsmsg ){
        Map<String, Object> rsMap = new HashMap<String, Object>();//返回信息
        rsMap.put("rscode", rscode);
        rsMap.put("rsmsg", rsmsg);
        //System.out.println("rs==" + JSONObject.fromObject(rsMap).toString());
        String rs = JSONObject.fromObject(rsMap).toString();
        logger.debug("返回信息>>>" + rs);
        return rs;
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String forwardSNPage(Model model){
       model.addAttribute("prodList", snManager.findProdTypeList());

        return "sn/snPage";
    }

    /**
     * 获取模块信息，验证上传的模块sn和订单的关联是否有效，有效时返回设备型号以及从机数量
     * @param modelsn
     * @param orderno
     * @param req
     * @return
     */
    @RequestMapping(value = "/getModelBySN", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String getModelBySN(@RequestParam(value = "modelsn", defaultValue = "") String modelsn,
                               @RequestParam(value = "orderno", defaultValue = "") String orderno,
                               HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        /**********************************************
         * 空值判断
         *********************************************/
        if (orderno == null || "".equals(orderno.trim())) {
            return putRSinfo("-1", "订单编号不能为空");
        }
        if (modelsn == null || "".equals(modelsn.trim())) {
            return putRSinfo("-2", "模块SN不能为空");
        }

        /**********************************************
         * 检查订单是否存在
         *********************************************/
        boolean checkOrder = orderManager.checkOrder(orderno);
        if (!checkOrder){
            return putRSinfo("-3", "订单不存在或已删除");
        }

        /**********************************************
         * 检查PCBA是否有绑定
         *********************************************/
        boolean isNoPcb = snManager.findPcbByMainSn(modelsn, orderno);
        if (! isNoPcb) {
            return putRSinfo("-4", "PCB板SN码信息不存在或已使用");
        }

        /**********************************************
         * 获取模块SN对应的信息
         *********************************************/
        SnModule snModule = snManager.getSnModuleByCode(modelsn);
        if (snModule == null) {
            return putRSinfo("-5", "模块SN码信息不存在");
        }

        /**********************************************
         * 检查订单对应的设备型号是否匹配订单所属设备型号
         *********************************************/
        int bmu_num = snManager.checkDeciveByOrderNo(snModule, orderno);
        if (bmu_num < 0){
            if (orderno.indexOf("LG-SH") > -1 || orderno.indexOf("lg-sh") > -1){

            }else{
                return putRSinfo("-6", "模块SN码与订单所属设备类型不匹配");
            }
            //return putRSinfo("-6", "模块SN码与订单所属设备类型不匹配或订单软件未上传");
        }

        /**********************************************
         * 返回信息拼装
         *********************************************/
        Map<String, String> map = new HashMap<String, String>();
        map.put("devicename", snModule.getType());
        map.put("devicetype", snModule.getDeviceType() + ""); // 1- 主机（不带DTU）2- 主机（带DTU）3- 一体机（不带DTU）4- 一体机（带DTU） 5- 从机 6- 绝缘检测(LDM) 7- DTU
        map.put("bmunum", bmu_num + "");
        map.put("remarks", snModule.getRemarks());

        return putRSinfo("1", map);

    }


    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String sendEmail(@RequestParam(value = "msg", defaultValue = "") String msg,HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        //emailManager.sendMail("测试邮件", msg, new String[]{"wanglyue2008@126.com", "wanglyue@126.com"});
        msg = "\n" +
                "\n" +
                "    客户名称：北京普莱德新能源科技有限公司\n" +
                "    项目描述：FT-6116 158度\n" +
                "    订单套数：1\n" +
                "    订单编号：LG201610100101\n" +
                "    上传类型：齐套上传\n" +
                "\n" +
                "    设备型号：BC52A    文件名：LG201610100101_BC52A.s19\n" +
                "    设备型号：BC52A    文件名：LG201610100101_BC52A.csf\n" +
                "    设备型号：BC52A    文件名：LG201610100101_BC52A.cfg\n" +
                "    设备型号：BM5124AT    从机ID：1    文件名：LG201610100101_BM5124AT_M1.s19\n" +
                "    设备型号：BM5124AT    从机ID：1    文件名：LG201610100101_BM5124AT_M1.cfg\n" +
                "    设备型号：BM5124AT    从机ID：2    文件名：LG201610100101_BM5124AT_M2.s19\n" +
                "    设备型号：BM5124AT    从机ID：2    文件名：LG201610100101_BM5124AT_M2.cfg\n" +
                "    设备型号：BM5124AT    从机ID：3    文件名：LG201610100101_BM5124AT_M3.s19\n" +
                "    设备型号：BM5124AT    从机ID：3    文件名：LG201610100101_BM5124AT_M3.cfg\n" +
                "    设备型号：BM5124AT    从机ID：4    文件名：LG201610100101_BM5124AT_M4.s19\n" +
                "    设备型号：BM5124AT    从机ID：4    文件名：LG201610100101_BM5124AT_M4.cfg\n" +
                "    设备型号：BM5124AT    从机ID：5    文件名：LG201610100101_BM5124AT_M5.s19\n" +
                "    设备型号：BM5124AT    从机ID：5    文件名：LG201610100101_BM5124AT_M5.cfg\n" +
                "    设备型号：BM5124AT    从机ID：6    文件名：LG201610100101_BM5124AT_M6.s19\n" +
                "    设备型号：BM5124AT    从机ID：6    文件名：LG201610100101_BM5124AT_M6.cfg\n" +
                "    设备型号：BM5124AT    从机ID：7    文件名：LG201610100101_BM5124AT_M7.s19\n" +
                "    设备型号：BM5124AT    从机ID：7    文件名：LG201610100101_BM5124AT_M7.cfg\n" +
                "    设备型号：BM5124AT    从机ID：8    文件名：LG201610100101_BM5124AT_M8.s19\n" +
                "    设备型号：BM5124AT    从机ID：8    文件名：LG201610100101_BM5124AT_M8.cfg\n" +
                "    设备型号：BM5124AT    从机ID：9    文件名：LG201610100101_BM5124AT_M9.s19\n" +
                "    设备型号：BM5124AT    从机ID：9    文件名：LG201610100101_BM5124AT_M9.cfg\n" +
                "    设备型号：BM5124AT    从机ID：10    文件名：LG201610100101_BM5124AT_M10.s19\n" +
                "    设备型号：BM5124AT    从机ID：10    文件名：LG201610100101_BM5124AT_M10.cfg\n" +
                "    设备型号：BM5112AT    从机ID：11    文件名：LG201610100101_BM5112AT_M11.s19\n" +
                "    设备型号：BM5112AT    从机ID：11    文件名：LG201610100101_BM5112AT_M11.cfg\n" +
                "    设备型号：M1112    从机ID：12    文件名：LG201610100101_M1112_M12.s19\n" +
                "    设备型号：M1112    从机ID：12    文件名：LG201610100101_M1112_M12.cfg\n" +
                "    设备型号：BL51B(H)    文件名：LG201610100101_BL51B(H).s19\n" +
                "    设备型号：BL51B(H)    文件名：LG201610100101_BL51B(H).cfg\n" +
                "    下载地址：http://192.168.1.21:10020/odt-ligoo/orderService/detail?orderno=";

        emailManager.sendMail("测试邮件", msg, 99, 4030l);

        return putRSinfo("1", "邮件已发送");

    }

    /**
     * 获取PCBA板信息
     * 1-新扫码的模块SN 显示内容：硬件版本信息数组、设备类型、PCBA板详情（名称、型号、代码标识）
     * 2-已扫码模块SN   显示内容：硬件版本信息数组、设备类型、PCBA板详情（名称、型号、代码标识）、已选择硬件版本、已扫码PCBA板信息
     * 3-模块SN码不在订单内的返回错误提示
     * @param mSN
     * @param orderNo
     * @param req
     * @return
     */
    @RequestMapping(value = "/findPcbaInfo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findPcbaInfo(@RequestParam(value = "m_sn", defaultValue = "") String mSN, @RequestParam(value = "orderno", defaultValue = "") String orderNo,HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        mSN = StringUtil.null2String(mSN).trim();
        orderNo = StringUtil.null2String(orderNo).trim();

        if ("".equals(mSN) || "".equals(orderNo)){
            return putRSinfo("-1", "输入参数不能为空");
        }

        String deviceType = "";
        List<Map<String, String>> pcbaList = new ArrayList<Map<String, String>>();

        PcbaRes pcbaRes = new PcbaRes();
        pcbaRes.setOrderNo(orderNo);
        pcbaRes.setmSN(mSN);


        List<Map<String, String>> pcbaInfoList = snManager.findPcbaBySNCode(mSN);
        if (pcbaInfoList != null && pcbaInfoList.size()>0) {
            Map<String, String> pcbaMap = pcbaInfoList.get(0);
            deviceType = StringUtil.null2String(pcbaMap.get("m_type"));
            pcbaRes.setDeviceType(deviceType);

            /**********************************************
             * 检查订单对应的设备型号是否匹配订单所属设备型号
             *********************************************/
            int device_num = snManager.checkDeciveByOrderNo( deviceType, orderNo);
            if (device_num > 0){

            }else{
                if (orderNo.indexOf("LG-SH") > -1 || orderNo.indexOf("lg-sh") > -1){//硬件售后订单不用判断订单设备是否匹配

                }else {
                    return putRSinfo("-4", "模块SN码与订单所属设备类型不匹配");
                }
            }
            pcbaRes.setDeviceCount(snManager.findOrdetrDeviceSum(orderNo, deviceType));
            List<String> versionList = snManager.findHardwareVersion(deviceType);
            pcbaRes.setHwVersionList(versionList);

            for (int i = 0; i <pcbaInfoList.size(); i++) {
                Map<String, String> pcba_map = pcbaInfoList.get(i);
                int pcba_number = StringUtil.StringToInt(StringUtil.null2String(pcba_map.get("pcba_number")));
                for (int j = 0; j < pcba_number; j++) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("pcba_type", StringUtil.null2String(pcba_map.get("pcba_type")));
                    map.put("pcba_code", StringUtil.null2String(pcba_map.get("pcba_code")));
                    map.put("pcba_name", StringUtil.null2String(pcba_map.get("pcba_name")));
                    map.put("pcba_sn", "");
                    pcbaList.add(map);
                }
            }
        }else{
            return putRSinfo("-3", "模块SN无PCBA板关联信息");//模块SN码没有对照的PCBA板信息
        }
        pcbaRes.setPcbaList(pcbaList);

        //已解绑过的PCBA板信息
        //查询是否已有模块扫码记录
        List<Map<String, String>> packList = snManager.findPcbaBySN(mSN);
        if (packList == null || packList.size() == 0) {


        }else{
            //判断设备订单号是否相同
            Map<String, String> packMap = packList.get(0);
            String packOrderno = StringUtil.null2String(packMap.get("orderno")).trim();

            if ("".equals(packOrderno) || orderNo.equalsIgnoreCase(packOrderno)){
                //已解绑时，订单编号为空；同一个订单时也可重新操作
            }else{
                return putRSinfo("-2", "模块已绑定，须先解绑");
            }
            //匹配PCBA扫码数据
            List<Map<String, String>> newPcbaList = new ArrayList<Map<String, String>>();

            for (int i = 0; i < packList.size(); i++) {
                Map<String, String> pcba_map = packList.get(i);
                Map<String, String> map = new HashMap<String, String>();
                map.put("pcba_type", StringUtil.null2String(pcba_map.get("pcba_type")));
                map.put("pcba_code", StringUtil.null2String(pcba_map.get("pcba_code")));
                map.put("pcba_name", StringUtil.null2String(pcba_map.get("pcba_name")));
                map.put("pcba_sn", StringUtil.null2String(pcba_map.get("pcba_sn")));
                newPcbaList.add(map);
            }
            pcbaRes.setPcbaList(newPcbaList);
        }

        Map<String, String> map = snManager.findOrderPcbaBySN(mSN);
        if (map != null && map.size() > 0) {
            pcbaRes.setHwVersion(StringUtil.null2String(map.get("hw_version")));
        }

        return putRSinfo("1", pcbaRes);
    }

    /**
     * 获取模块SN信息
     * @param mSN
     * @param orderNo
     * @param req
     * @return
     */
    @RequestMapping(value = "/findMSNforMi", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findMSNforMi(@RequestParam(value = "m_sn", defaultValue = "") String mSN,
                               @RequestParam(value = "orderno", defaultValue = "") String orderNo,
                              // @RequestParam(value = "device_type", defaultValue = "") String device_type,
                               HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        mSN = StringUtil.null2String(mSN).trim();
        orderNo = StringUtil.null2String(orderNo).trim();

        //if ("".equals(mSN) || "".equals(orderNo) || "".equals(device_type)){
        if ("".equals(mSN) || "".equals(orderNo) ){
            return putRSinfo("-1", "输入参数不能为空");
        }

        /**********************************************
         * 检查订单对应的设备型号是否匹配订单所属设备型号
         *********************************************/
        SnModule snModule = snManager.getSnModuleByCode(mSN);
        String deviceType = "";
        if (snModule != null) {
            deviceType = snModule.getType();
        }
        /*if (!device_type.equals(deviceType)){ 从机需传入ID编号才能获取设备型号
            return putRSinfo("-2", "设备型号与模块条码不匹配");
        }*/

        if (snManager.checkSNinOrder(mSN, orderNo)){

        }else{
            if (orderNo.indexOf("LG-SH") > -1 || orderNo.indexOf("lg-sh") > -1){

            }else{
                return putRSinfo("-3", "模块SN码与订单不匹配");
            }
            //return putRSinfo("-3", "模块SN码与订单不匹配");
        }
        //获取返回信息
        Map<String, Object> map = snManager.findOrderProductForMiScann(mSN, orderNo);
        if (null == map || map.size() < 3){
            return putRSinfo("-4", "PCBA未扫码或已解绑");
        }

        //String db_uuid = StringUtil.null2String(map.get("uuid"));

        //判断工装情况
        /*******************************************
         * 无记录时返回-2 测试不通过 -1 测试通过 1
         ******************************************/
        int rs = snManager.checkMSnFromTool(orderNo, mSN);
        if (rs == 1){
            /*map.put("isNext", "true");
            //比较UUID
            if (! uuid.equalsIgnoreCase(db_uuid)){
                return putRSinfo("-4", "UUID不一致");
            }*/
        }else if (rs == -1){
            //map.put("isNext", "false");
            return putRSinfo("-5", "工装测试不通过");
        }else if (rs == -2){
            //TODO 兼顾前期A型工装
           /* //新增不做UUID验证的型号 2016年1月21日17:27:31
            Map<String, String> unCheckMap = new HashMap<String, String>();
            // BM5112A  BM5124A BM5136A BM5148A BM5160A BM5112AT BM5124AT BM5136AT BM5148AT
            unCheckMap.put("LDM", "BL51A");
            unCheckMap.put("M51(2)12", "BM5112A");
            unCheckMap.put("M51(2)24", "BM5124A");
            unCheckMap.put("M51(2)36", "BM5136A");
            unCheckMap.put("M51(2)48", "BM5148A");
            unCheckMap.put("M51(2)60", "BM5160A");
            unCheckMap.put("M51(2)12T", "BM5112AT");
            unCheckMap.put("M51(2)24T", "BM5124AT");
            unCheckMap.put("M51(2)36T", "BM5136AT");
            unCheckMap.put("M51(2)48T", "BM5148AT");
            *//**
             * 新程序也有无UUID的情况
             *//*
            unCheckMap.put("BM5112AT", "BM5112AT");
            unCheckMap.put("BM5124AT", "BM5124AT");
            unCheckMap.put("BM5136AT", "BM5136AT");
            unCheckMap.put("BM5148AT", "BM5148AT");

            unCheckMap.put("M1216", "M1216");
            unCheckMap.put("BL51A", "BL51A");
            unCheckMap.put("M1112", "M1112");
            unCheckMap.put("BD51A", "BD51A");
            unCheckMap.put("M512", "M512");

            String str = StringUtil.null2String(unCheckMap.get(deviceType));
            if ("".equals(str)) {
                return putRSinfo("-6", "工装未测试");
            }*/
        }
        return putRSinfo("1", map);
    }

    /**
     * 模块扫码信息保存
     * @param user_id
     * @param uuid
     * @param orderno
     * @param m_sn
     * @param device_type
     * @param hw_version
     * @param sw_version
     * @param submit_type
     * @param req
     * @return
     */
    @RequestMapping(value = "/saveMiScan", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveMiScan(@RequestParam(value = "user_id", defaultValue = "") String user_id,
                           @RequestParam(value = "uuid", defaultValue = "") String uuid,
                           @RequestParam(value = "orderno", defaultValue = "") String orderno,
                           @RequestParam(value = "m_sn", defaultValue = "") String m_sn,
                           @RequestParam(value = "device_type", defaultValue = "") String device_type, //设备读取的主板型号
                           @RequestParam(value = "hw_version", defaultValue = "") String hw_version,
                           @RequestParam(value = "sw_version", defaultValue = "") String sw_version,
                           @RequestParam(value = "submit_type", defaultValue="0") int submit_type , //0 - 默认非强制提交 1-强制提交
                           HttpServletRequest req
    ){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        /*SNInfo snObj_db = snManager.findSNInfoById(631l);
        String rs = JSONObject.fromObject(snObj_db).toString();*/

        if (user_id == null || "".equals(user_id.trim())
                || uuid == null || "".equals(uuid.trim())
                || orderno == null || "".equals(orderno.trim())
                || m_sn == null || "".equals(m_sn.trim())
                || device_type == null || "".equals(device_type.trim())
                || hw_version == null || "".equals(hw_version.trim())
                || sw_version == null || "".equals(sw_version.trim())
                ) {
            return putRSinfo("-1", "输入参数不能为空");
        }
        SNInfo snObj = new SNInfo();
        Order orderObj = orderManager.findInfoByOrderNo(orderno);
        if (orderObj == null) {
            return putRSinfo("-2", "订单不存在");
        }else {
            snObj.setOrderno(orderno);
            snObj.setCorp(orderObj.getCorp());
        }

        /**********************************************
         * 检查订单对应的设备型号是否匹配订单所属设备型号
         *********************************************/
        SnModule snModule = snManager.getSnModuleByCode(m_sn);
        String deviceType = "";
        if (snModule != null) {
            deviceType = snModule.getType();
        }
        if (!device_type.equals(deviceType)){
            return putRSinfo("-3", "设备型号与模块条码不匹配");
        }

        if (snManager.checkSNinOrder(m_sn, orderno)){

        }else{
            if (orderno.indexOf("LG-SH") > -1 || orderno.indexOf("lg-sh") > -1){

            }else{
                return putRSinfo("-4", "模块SN码与订单不匹配或未组装PCBA");
            }
        }

        //TODO 工装测试 规则尚需完善，暂不验证 sby

        //以下几种产品型号的UUID不做验证 2016年1月11日10:17:00
        //String[] unCheckArr = {"M1216", "BL51A", "M1112", "BD51A", "M512"};
        //新增不做UUID验证的型号 2016年1月21日17:27:31
        Map<String, String> unCheckMap = new HashMap<String, String>();
        // BM5112A  BM5124A BM5136A BM5148A BM5160A BM5112AT BM5124AT BM5136AT BM5148AT
        unCheckMap.put("LDM", "BL51A");
        unCheckMap.put("M51(2)12", "BM5112A");
        unCheckMap.put("M51(2)24", "BM5124A");
        unCheckMap.put("M51(2)36", "BM5136A");
        unCheckMap.put("M51(2)48", "BM5148A");
        unCheckMap.put("M51(2)60", "BM5160A");
        unCheckMap.put("M51(2)12T", "BM5112AT");
        unCheckMap.put("M51(2)24T", "BM5124AT");
        unCheckMap.put("M51(2)36T", "BM5136AT");
        unCheckMap.put("M51(2)48T", "BM5148AT");

        /**
         * 新程序也有无UUID的情况
         */
        unCheckMap.put("BM5112AT", "BM5112AT");
        unCheckMap.put("BM5124AT", "BM5124AT");
        unCheckMap.put("BM5136AT", "BM5136AT");
        unCheckMap.put("BM5148AT", "BM5148AT");

        unCheckMap.put("M1216", "M1216");
        unCheckMap.put("BL51A", "BL51A");
        unCheckMap.put("M1112", "M1112");
        unCheckMap.put("BD51A", "BD51A");
        unCheckMap.put("M512", "M512");

        uuid = uuid.toLowerCase(); //UUID转化为小写
        if (submit_type == 0){// 强制提交时不验证已绑定的SN码和UUID

            String oldPcba = unCheckMap.get(device_type);
            if (null != oldPcba){

            }else{
                //非特殊型号的设备UUID不能全为0
                if ("00000000-0000-0000-0000-000000000000".equals(uuid)){
                    return putRSinfo("-5", "UUID不能为空");
                }
                int isUseCount = snManager.findRepetition(uuid, m_sn);
                if (isUseCount > 0 ){
                    return putRSinfo("-6", "UUID或者模块SN码已绑定");
                }
            }
        }else{
            //强制提交时也需要做UUID不为空的验证 2016年1月26日11:41:33
            String oldPcba = unCheckMap.get(device_type);
            if (null != oldPcba){

            }else{
                //非特殊型号的设备UUID不能全为0
                if ("00000000-0000-0000-0000-000000000000".equals(uuid)){
                    return putRSinfo("-6", "UUID不能为空");
                }
            }
        }

        Long prodId = snManager.findProdTypeByName(device_type);
        if (prodId < 0){
            return putRSinfo("-7", "产品型号不存在");
        }

        ProductType prodType = new ProductType();
        prodType.setId(prodId);
        snObj.setProdType(prodType);

        snObj.setHwVersion(hw_version);
        snObj.setSwVersion(sw_version);
        snObj.setOptUserid(Long.parseLong(user_id));
        snObj.setOptTime(new Date());
        snObj.setStatus(1);
        snObj.setTempSn(m_sn.trim());
        snObj.setUuid(uuid.trim());

        //填充dtuUuid
        String uuidStr = uuid.replace("-", "");
        if(uuidStr.length() > 17){
            snObj.setDtuUuid(uuidStr.substring(uuidStr.length()-17));
        }else{
            snObj.setDtuUuid(uuidStr);
        }

        int rsSave = snManager.saveMiScanInfo(snObj);
        if (rsSave > 0){

            Map<String, String> rsMap = new HashMap<String, String>();
            rsMap.put("p_sn", snObj.getSn());                           //产品SN码
            rsMap.put("corp_name", orderObj.getCorp().getCorpName());   //客户名称
            rsMap.put("project_note", orderObj.getProjectNote());       //项目描述
            rsMap.put("device_class", snModule.getClassName());         //设备类型
            rsMap.put("device_info", snModule.getType());               //设备型号  BM5124B    不带从机ID和串数 sby 2016年8月9日 17:05:46  单个从机串数未记录
            rsMap.put("hw_version", snObj.getHwVersion());              //硬件版本号
            rsMap.put("sw_version", snObj.getSwVersion());              //软件版本号
            rsMap.put("orderno", orderno);                              //订单编号
            rsMap.put("device_count", String.valueOf(snManager.findSNCount(orderno, snObj.getSn())));//设备数量

            return putRSinfo("1" , rsMap);
        }else if(rsSave == -1){
            return putRSinfo("-8", "无对应的产品SN编码");
        }
        return putRSinfo("-99", "系统错误");
    }

    /**
     * 保存客户料号
     * @param goods_code
     * @param p_sn
     * @param req
     * @return
     */
    @RequestMapping(value = "/saveGoodsCode", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveGoodsCode(@RequestParam(value = "goods_code", defaultValue = "") String goods_code,
                           @RequestParam(value = "p_sn", defaultValue = "") String p_sn,
                           HttpServletRequest req
    ){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        if (null == goods_code || "".equals(goods_code.trim())
                || null == p_sn || "".equals(p_sn.trim())){
            return putRSinfo("-1", "输入参数不能为空");
        }
        SNInfo snObj = snManager.findSNInfoBySn(p_sn);
        if (null == snObj){
            return putRSinfo("-2", "产品SN码对应信息不存在");
        }

        int rs = snManager.updateGoodsCode(p_sn, goods_code);
        if (rs > 0){
            return putRSinfo("1", "操作成功");
        }
        return putRSinfo("-99", "系统错误");
    }

    /**
     * 产品SN获取打印信息
     * @param p_sn
     * @param req
     * @return
     */
    @RequestMapping(value = "/findMiInfoByPSN", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findMiInfoByPSN(@RequestParam(value = "p_sn", defaultValue = "") String p_sn,
                           HttpServletRequest req
    ){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        if (null == p_sn || "".equals(p_sn.trim())){
            return putRSinfo("-1", "输入参数不能为空");
        }

        SNInfo snObj = snManager.findSNInfoBySn(p_sn);
        if (null == snObj){
            return putRSinfo("-2", "产品SN码对应信息不存在");
        }
        Order orderObj = orderManager.findInfoByOrderNo(snObj.getOrderno());
        SnModule snModule = snManager.getSnModuleByCode(snObj.getTempSn());

        if (snModule == null || orderObj == null) {
            return putRSinfo("-3", "订单信息不存在");
        }

        Map<String, String> rsMap = new HashMap<String, String>();
        rsMap.put("p_sn", snObj.getSn());                           //产品SN码
        rsMap.put("corp_name", orderObj.getCorp().getCorpName());   //客户名称
        rsMap.put("project_note", orderObj.getProjectNote());       //项目描述
        rsMap.put("goods_code", snObj.getGoodsCode());              //客户料号
        rsMap.put("device_class", snModule.getClassName());         //设备类型
        rsMap.put("device_info", snModule.getType());               //设备型号  BM5124B    不带从机ID和串数 sby 2016年8月9日 17:05:46
        rsMap.put("hw_version", snObj.getHwVersion());              //硬件版本号
        rsMap.put("sw_version", snObj.getSwVersion());              //软件版本号
        rsMap.put("orderno", orderObj.getOrderno());                //订单编号
        rsMap.put("device_count", String.valueOf(snManager.findSNCount(orderObj.getOrderno(), snObj.getSn())));//设备数量

        return putRSinfo("1", rsMap);
    }



    @RequestMapping(value = "/testPSN", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String testPSN(@RequestParam(value = "p_code", defaultValue = "") String p_code,
                           HttpServletRequest req
    ){

        String deviceMapKey = p_code + "_" + SNCodeUtil.DATEFORMAT_MINI.format(new Date());
        Integer num = SNCodeUtil.deviceMap.get(deviceMapKey);
        if (num == null || num.intValue() == 0) {
            num = 0;
            SNCodeUtil.deviceMap.put(deviceMapKey, Integer.valueOf(0));
        }

        String p_sn = SNCodeUtil.getSerialNumber(SNCodeUtil.deviceMap.get(deviceMapKey), p_code);

        SNCodeUtil.deviceMap.put(deviceMapKey, Integer.valueOf(num + 1)); //加一保持数量自增

        for(String key : SNCodeUtil.deviceMap.keySet()){
            System.out.println(String.format("key=%s    value=%s", key, SNCodeUtil.deviceMap.get(key)));
        }
        System.out.println("-----------------------------------------------");

        return putRSinfo("1", p_sn);
    }
}
