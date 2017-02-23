package com.ligootech.webdtu.web.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ligootech.webdtu.util.StringUtil;
import com.ligootech.webdtu.util.FileOperateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by wly on 2015/12/2 11:46.
 */

@Controller
@RequestMapping(value = "/fileOperate")
public class FileOperateController {
    /**
     * 到上传文件的位置
     *
     * @author geloin
     * @date 2012-3-29 下午4:01:31
     * @return
     */
    @RequestMapping(value = "to_upload")
    public String toUpload() {
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

        List<Map<String, Object>> result = FileOperateUtil.upload(request,params, values);

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
    public String uploadOneFile(HttpServletRequest request, Model model) throws Exception {
        String fileType = ServletRequestUtils.getStringParameter(request, "fileType");

        Map<String, Object> result = FileOperateUtil.uploadFile(request, StringUtil.null2String(fileType));

        model.addAttribute("result", result);

        String backmethod = ServletRequestUtils.getStringParameter(request, "backmethod");
        model.addAttribute("backmethod_", StringUtil.null2String(backmethod));

        return "fileOperate/listRS";
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

        String storeName = "2015120213221310397194331717.zip";
        String realName = "Java设计模式.zip";
        String contentType = "application/octet-stream";

        FileOperateUtil.download(request, response, storeName, contentType, realName, "");

        return null;
    }


    @RequestMapping(value = "/downloadConfigFile")
    public String downloadConfigFile(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "orderno") String orderno,
                                     @RequestParam(value = "fileType") String fileType,
                                     Model model) throws Exception {
        String storeName = "2015120213221310397194331717.zip";
        String realName = "Java设计模式.zip";

        if (orderno == null) {
            model.addAttribute("errorMsg", "订单编号不能为空");
            return "fileOperate/uploadError";
        }

        if (fileType == null){
            model.addAttribute("errorMsg", "文件类型不能为空");
            return "fileOperate/uploadError";
        }else if ("s19".equalsIgnoreCase(fileType)){
            storeName = "201512021441235078204971076.zip";
            realName = storeName;
        }else if ("cfg".equalsIgnoreCase(fileType)){
            storeName = "201512021441239418336435832.zip";
            realName = storeName;
        }else if ("csf".equalsIgnoreCase(fileType)){
            storeName = "201512021441233283781051173.zip";
            realName = storeName;
        }else{
            model.addAttribute("errorMsg", "文件类型不正确");
            return "fileOperate/uploadError";
        }
        String contentType = "application/octet-stream";

        FileOperateUtil.download(request, response, storeName, contentType, realName, "");

        return null;
    }
}
