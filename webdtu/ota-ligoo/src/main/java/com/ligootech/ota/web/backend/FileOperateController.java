package com.ligootech.ota.web.backend;

import java.lang.reflect.Field;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ligootech.ota.web.util.ShiroUtil;
import com.ligootech.webdtu.entity.core.Corp;
import com.ligootech.webdtu.entity.core.Order;
import com.ligootech.webdtu.service.order.FileManager;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.webdtu.util.LoggerUtil;
import com.ligootech.webdtu.util.StringUtil;
import com.ligootech.webdtu.util.FileOperateUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by wly on 2015/12/2 11:46.
 */

@SuppressWarnings("Duplicates")
@Controller
@RequestMapping(value = "/fileOperate")
public class FileOperateController {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private FileManager fileManager;

    @Autowired
    private OrderManager orderManager;

    /**
     * 上传文件的位置
     * @author geloin
     * @date 2012-3-29 下午4:01:31
     * @return
     */
    @RequestMapping(value = "to_upload")
    public String toUpload() {
        /****************************************************************************
         * 上传测试地址;http://192.168.1.22:8080/pos-ligoo/fileOperate/to_upload
         ***************************************************************************/
        // return new ModelAndView("background/fileOperate/upload");
        return "fileOperate/upload";
    }
    @RequestMapping(value = "singleUploadFile")
    public String toUploadFile(HttpServletRequest request, Model model) throws Exception{
        String fileType = ServletRequestUtils.getStringParameter(request, "fileType");
        model.addAttribute("fileType_", StringUtil.null2String(fileType));

        String backmethod = ServletRequestUtils.getStringParameter(request, "backmethod");
        model.addAttribute("backmethod_", StringUtil.null2String(backmethod));
        return "fileOperate/uploadFile";
    }

    /**
     * 上传文件
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "upload")
    public String upload(HttpServletRequest request, Model model) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        // 别名
        String[] alaises = ServletRequestUtils.getStringParameters(request, "alais");

        String[] params = new String[] { "alais" };
        Map<String, Object[]> values = new HashMap<String, Object[]>();
        values.put("alais", alaises);

        List<Map<String, Object>> result = FileOperateUtil.upload(request, params, values);

        //map.put("result", result);
        model.addAttribute("result", result);
        //return new ModelAndView("background/fileOperate/list", map);
        return "fileOperate/list";
    }

    /**
     * 单个文件上传且不压缩
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "uploadFile")
    @ResponseBody
    public String uploadOneFile(HttpServletRequest request, Model model) throws Exception {
        String fileType = ServletRequestUtils.getStringParameter(request, "fileType");

        Map<String, Object> result = FileOperateUtil.uploadFile(request, StringUtil.null2String(fileType));

        //model.addAttribute("result", result);

        //String backmethod = ServletRequestUtils.getStringParameter(request, "backmethod");
        //model.addAttribute("backmethod_", StringUtil.null2String(backmethod));

        String str = JSONObject.fromObject(result).toString();

        return str;
    }

    /**
     * 订单配置文件上传
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    //@RequestMapping(value = "uploadMoreFile",method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")  //火狐有406错误
    @RequestMapping(value = "uploadOrderConfigFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadOrderConfigFile(HttpServletRequest request, Model model) throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id");
        String device_code = StringUtil.null2String(ServletRequestUtils.getStringParameter(request, "device_code"));
        String device_type = StringUtil.null2String(ServletRequestUtils.getStringParameter(request, "device_type"));
        //String orderno = ServletRequestUtils.getStringParameter(request, "orderno");

        List<Map<String, Object>> resultMoreFile = FileOperateUtil.uploadOrderConfigFile(request);
        Map<String, Object> rs = resultMoreFile.get(0);
        rs.put("device_code", device_code);
        rs.put("device_type", device_type);
        if ("none".equals(rs.get(FileOperateUtil.ERROR_MSG))){
            //保存入库
            fileManager.saveUploadFile(rs, ShiroUtil.getCurrentUser().getId());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", id);
        result.put("status", true);
        result.put("message", rs.get(FileOperateUtil.ERROR_MSG));
        result.put("value", rs.get(FileOperateUtil.FILEID));
        String str = JSONObject.fromObject(result).toString();

        return str;
    }

    /**
     * 上传模版文件
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "uploadModuleConfigFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadModuleConfigFile(HttpServletRequest request, Model model) throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "id");
        //String orderno = ServletRequestUtils.getStringParameter(request, "orderno");

        List<Map<String, Object>> resultMoreFile = FileOperateUtil.uploadOrderConfigFile(request);
        Map<String, Object> rs = resultMoreFile.get(0);
        rs.put("device_code", "");
        rs.put("device_type", "0");
        if ("none".equals(rs.get(FileOperateUtil.ERROR_MSG))){
            //保存入库
            fileManager.saveUploadFile(rs, ShiroUtil.getCurrentUser().getId());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", id);
        result.put("status", true);
        result.put("message", rs.get(FileOperateUtil.ERROR_MSG));
        result.put("value", rs.get(FileOperateUtil.FILEID));
        String str = JSONObject.fromObject(result).toString();

        return str;
    }

    /**
     * 下载
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "download")
    public String download(HttpServletRequest request,
                           HttpServletResponse response) throws Exception {

        String storeName = ServletRequestUtils.getStringParameter(request, "id");
        String realName = "Java设计模式.zip";
        String contentType = "application/octet-stream";

        FileOperateUtil.download(request, response, storeName, contentType, storeName + ".zip", "");

        return null;
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "downloadById")
    public String downloadById(HttpServletRequest request,
                               HttpServletResponse response) {
        String contentType = "application/octet-stream";
        String storeName = "";
        try {
            String id = ServletRequestUtils.getStringParameter(request, "id");
            String deviceCode = ServletRequestUtils.getStringParameter(request, "device_code");
            String md5_code = "";
            if ("usetemplate".equals(id)){//使用模版固件
                //id = fileManager.getS19FileName(deviceCode);
                Map<String, String> fileMap = fileManager.getS19FileName(deviceCode);
                if (null == fileMap){
                    logger.debug("系统找不到模版固件文件>>>设备型号：" + deviceCode);
                    FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
                    return null;
                }else{
                    id = fileMap.get("file_code");
                    md5_code = fileMap.get("md5_code");
                }
            }
            storeName = id + ".zip";
            //String realName = id + ".zip";
            FileOperateUtil.download(request, response, storeName, contentType, storeName, md5_code);
        } catch (Exception e) {
            logger.debug("系统找不到指定的文件>>>" + storeName);
            try {
                FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 固件模版下载
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "downloadS19ByDeviceCode")
    public String downloadS19ByDeviceCode(HttpServletRequest request,
                               HttpServletResponse response) {
        String contentType = "application/octet-stream";
        String storeName = "";
        try {
            String id = ServletRequestUtils.getStringParameter(request, "id");
            storeName = id + ".zip";
            //String realName = id + ".zip";
            FileOperateUtil.download(request, response, storeName, contentType, storeName, "");
        } catch (Exception e) {
            logger.debug("系统找不到指定的文件>>>" + storeName);
            try {
                FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 订单配置文件下载
     * @param request
     * @param response
     * @param orderno
     * @param fileType
     * @param deviceCode
     * @param bmuNo
     * @param fileName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadConfigFile")
    public String downloadConfigFile(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "orderno", defaultValue = "") String orderno,
                                     @RequestParam(value = "fileType", defaultValue = "") String fileType,
                                     @RequestParam(value = "deviceCode", defaultValue = "") String deviceCode,
                                     @RequestParam(value = "bmuNo", defaultValue = "0") String bmuNo,//从机编号
                                     @RequestParam(value = "fileName", defaultValue = "") String fileName,
                                     @RequestParam(value = "isTestS19", defaultValue = "") String isTestS19
                                     ) throws Exception {
        String contentType = "application/octet-stream";

        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), request, logger);

        /******************************************************
         * 测试地址：http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201602230001&fileType=csf&deviceCode=BC52B&fileName=201512021441233283781051173&bmuNo=0&isTestS19=1
         * http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201606240901&fileType=s19&deviceCode=BM5148A&fileName=2&bmuNo=1&isTestS19=1
         * http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201602230001&fileType=s19&deviceCode=BC52A&fileName=&bmuNo=0&isTestS19=1
         *****************************************************/

        String errorMsg = "";
        /**********************************************
         * 空值判断
         *********************************************/
        if (orderno == null || "".equals(orderno)) {
            errorMsg = "订单编号不能为空";
        }

        if (deviceCode == null || "".equals(deviceCode)) {
            errorMsg = "设备型号不能为空";
        }

        if (fileType == null || "".equals(fileType)){
            errorMsg = "文件类型不能为空";
        }

        if (!"".equals(errorMsg)){
            logger.debug("错误信息：" + errorMsg);
            FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
            return null;
        }
        String storeName = "";
        boolean isTest = false;
        /**********************************************
         * 查找测试固件  isTestS19：1- 是  其他值为否
         *********************************************/
        if ("1".equals(isTestS19)){
            isTest = true;
        }

        /**********************************************
         * 查找文件名
         *********************************************/
        //storeName = fileManager.getFileNameByReq(orderno, fileType, deviceCode, bmuNo, isTest);
        Map<String, String> fileMap = fileManager.getFileNameByReq(orderno, fileType, deviceCode, bmuNo, isTest);
        String md5_code = "";

        if (null == fileMap ){
            logger.debug("错误信息：指定文件不存在");
            FileOperateUtil.download(request, response, "error.zip", contentType, "error.zip", "error");
            return null;
        }else{
            storeName = fileMap.get("file_code");
            md5_code = fileMap.get("md5_code");
        }

        /**********************************************
         * 判断是否存在本地文件
         *********************************************/
        if (!"".equals(fileName) && storeName.equals(fileName)){
            storeName = "existent";
        }
        storeName = storeName + ".zip";
        logger.debug("下载文件名：" + storeName);

        FileOperateUtil.download(request, response, storeName, contentType, storeName, md5_code);
        return null;
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
