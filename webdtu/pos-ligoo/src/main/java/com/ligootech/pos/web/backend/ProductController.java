package com.ligootech.pos.web.backend;

import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.entity.core.HardwareVersion;
import com.ligootech.webdtu.entity.core.ProductType;
import com.ligootech.webdtu.service.product.ProductManager;
import com.ligootech.pos.web.util.ShiroUtil;
import com.ligootech.webdtu.util.LoggerUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wly on 2015/11/16 11:04.
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private ProductManager productManager;

    /**
     * 硬件版本管理页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String forwardCustomer(Model model){

        List<Object[]> userList = productManager.findSimpleProductList();
        model.addAttribute("productList", userList);

        return "backend/product/productPage";
    }

    /**
     * 版本管理主页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String forwardMainPage(Model model){

        List<Object[]> userList = productManager.findSimpleProductList();
        model.addAttribute("productList", userList);

        return "backend/product/mainPage";
    }

    /**
     * 软件版本管理页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/swPage", method = RequestMethod.GET)
    public String forwardSwPage(Model model){

        List<Object[]> swList = productManager.findSimpleSWList();
        model.addAttribute("swList", swList);

        /*List<Object[]> hwList = productManager.findSimpleProductList();
        model.addAttribute("hwList", hwList);*/
        model.addAttribute("hwList", productManager.findHWListNoUse());

        return "backend/product/swPage";
    }

    /**
     * 重新获取左侧列表
     * @return
     */
    @RequestMapping(value = "/getProdLi", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findUserLiAjax(){
        //ShiroUtil.getCurrentUser().getId();

        List<Object[]> list = productManager.findSimpleProductList();
        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    @RequestMapping(value = "/getSwLi", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String getSwLi(){

        List<Object[]> list = productManager.findSimpleSWList();
        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    @RequestMapping(value = "/getBoxvalue", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findBoxvalue(){
        List<Object[]> list = productManager.findSimpleProductList();
        List<Map<String, String>> listRS = new ArrayList<Map<String, String>>();
        Object[] obj = null;
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            obj = list.get(i);
            map.put("prodid", obj[0].toString());
            map.put("prodname", obj[1].toString());
            listRS.add(map);
        }

        String rsStr = JSONArray.fromObject(listRS).toString();
        return rsStr;
    }

    /**
     * 获取软件版本信息列表下拉框
     * @return
     */
    @RequestMapping(value = "/findSWBoxvalue", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findSWBoxvalue(){
        List<Object[]> list = productManager.findSimpleSWList();
        List<Map<String, String>> listRS = new ArrayList<Map<String, String>>();
        Object[] obj = null;
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            obj = list.get(i);
            map.put("prodid", obj[0].toString());
            map.put("prodname", obj[1].toString());
            listRS.add(map);
        }

        String rsStr = JSONArray.fromObject(listRS).toString();
        return rsStr;
    }

    /**
     * 保存软件信息
     * @return
     */
    @RequestMapping(value = "/saveSwInfo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveSwInfo(@RequestParam(value = "id", defaultValue = "") String id,
                             @RequestParam(value = "sw_name", defaultValue = "") String swName,
                             @RequestParam(value = "hwStr", defaultValue = "") String hwStr,
                             HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        /**********************************************
         * 判断软件名称是否使用
         *********************************************/
        boolean checkSwName = productManager.checkSwName(id, swName);
        if (checkSwName){
            return "-1";
        }

        int rs = productManager.saveSwInfo(id, swName, hwStr, ShiroUtil.getCurrentUser().getId());

        if (rs > 0){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", rs);
            map.put("name", swName);
            return JSONObject.fromObject(map).toString();
        }
        return "-2";
    }

    /**
     * 保存软件版本号信息
     * @param swid
     * @param swVersion
     * @param file_code
     * @param updateNote
     * @param req
     * @return
     */
    @RequestMapping(value = "/saveSwVersion", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveSwVersion(@RequestParam(value = "swid", defaultValue = "") String swid,
                             @RequestParam(value = "swVersion", defaultValue = "") String swVersion,
                             @RequestParam(value = "file_code", defaultValue = "") String file_code,
                             @RequestParam(value = "updateNote", defaultValue = "") String updateNote,
                             HttpServletRequest req){
        /**********************************************
         * 日志记录传入的参数
         *********************************************/
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        LoggerUtil.logParameterInfo(ste.getClassName(), ste.getMethodName(), req, logger);

        if (",".equals(file_code)){
            file_code = ""; //防止验证文件时返回信息不完整
        }

        /**********************************************
         * 判断软件版本号是否已使用
         *********************************************/
        boolean checkSwName = productManager.checkSwVersion(swid, swVersion);
        if (checkSwName){
            return "-1";
        }

        int rs = productManager.saveSwVersion(swid, swVersion, file_code, updateNote, ShiroUtil.getCurrentUser().getId());

        if (rs > 0){
            /*Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", rs);
            //map.put("name", swName);
            return JSONObject.fromObject(map).toString();*/
            return rs + "";
        }
        return "-2";
    }
    /**
     * 获取软件产品信息
     * @param swId
     * @return
     */
    @RequestMapping(value = "/findSwInfo", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findSwInfo(@RequestParam(value = "swId") Long swId){

        /****************************
         * 软件基本信息
         ****************************/
        Map<String, Object> swMap = new HashMap<String, Object>();
                //productManager.findSwInfoBySwId(swId);

        /****************************
         * 版本信息
         ****************************/
        swMap.put("version", productManager.findSwVersionListBySwId(swId));

        /****************************
         * 硬件型号
         ****************************/
        swMap.put("isUseHw", productManager.findHWListBySwId(swId));

        /****************************
         * 未使用硬件型号
         ****************************/
        swMap.put("noUseHw", productManager.findHWListNoUse());

        return JSONObject.fromObject(swMap).toString();
    }

    /**
     * 获取产品对应的版本号
     * @param prodId
     * @return
     */
    @RequestMapping(value = "/getHWList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findHWList(@RequestParam(value = "prodId") Long prodId){

        List<HardwareVersion> list = productManager.findHwVwesion(prodId);
        String rsStr = JSONArray.fromObject(list).toString();
        return rsStr;
    }

    @RequestMapping(value = "/saveProd", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveProd(@RequestParam(value = "prodname", defaultValue = "") String prodname, @RequestParam(value = "prodType") int prodType){

        //判断产品类型是否存在
        boolean isUse = productManager.checkProdName(prodname);
        if(isUse){
            return "-1";
        }

        ProductType pt = new ProductType();
        pt.setOptTime(new Date());
        pt.setProdTypename(prodname);
        pt.setIsMainframe(prodType);
        DtuUser dtuUser = new DtuUser();
        dtuUser.setId(ShiroUtil.getCurrentUser().getId());
        pt.setOptUser(dtuUser);
        int rs = productManager.saveProduct(pt, ShiroUtil.getCurrentUser().getId());
        return pt.getId()+"";
    }

    @RequestMapping(value = "/saveHwVersion", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveProd(@RequestParam(value = "prodId") Long prodId, @RequestParam(value = "hwName") String hwName){

        //判断版本号是否存在
        boolean isUse = productManager.checkHWversion(prodId, hwName);
        if(isUse){
            return "-1";
        }

        ProductType pt = new ProductType();
        pt.setId(prodId);

        DtuUser dtuUser = new DtuUser();
        dtuUser.setId(ShiroUtil.getCurrentUser().getId());

        HardwareVersion hw = new HardwareVersion();
        hw.setVersion(hwName);
        hw.setOptUser(dtuUser);
        hw.setOptTime(new Date());
        hw.setStatus(1);
        hw.setProductType(pt);

        int rs = productManager.saveHardwareVersion(hw, ShiroUtil.getCurrentUser().getId());
        return hw.getId()+"";
    }

    @RequestMapping(value = "/delHWFun", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delHWFun(@RequestParam(value = "id") Long hardwareId){
        int rs = productManager.delHardwareVersion(hardwareId, ShiroUtil.getCurrentUser().getId());
        return "ok";
    }

    /**
     * 软件版本号删除
     * @param hardwareId
     * @return
     */
    @RequestMapping(value = "/delSWVersion", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delSWVersion(@RequestParam(value = "id") Long hardwareId){
        int rs = productManager.delSWVersion(hardwareId, ShiroUtil.getCurrentUser().getId());
        return "ok";
    }

    @RequestMapping(value = "/delProdFun", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delProdFun(@RequestParam(value = "id") Long prodId){
        //返回结果 -1 版本号表还有关联， -2 sn 或者 scan 表关联
        int rs = productManager.delProduct(prodId, ShiroUtil.getCurrentUser().getId());
        return rs + "";
    }

    /**
     * 软件删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delSWFun", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delSWFun(@RequestParam(value = "id") Long id){
        //返回结果 -1
        int rs = productManager.delSWInfo(id, ShiroUtil.getCurrentUser().getId());
        return rs + "";
    }

    @RequestMapping(value = "/showVersion", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String showVersion(@RequestParam(value = "id") Long id){
        Map<String, Object> map = productManager.findSWVersionById(id);
        return JSONObject.fromObject(map).toString();
    }

}
