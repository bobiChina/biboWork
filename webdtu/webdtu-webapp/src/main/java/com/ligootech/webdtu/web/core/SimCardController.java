package com.ligootech.webdtu.web.core;

import com.ligootech.webdtu.service.sim.SimManager;
import com.ligootech.webdtu.util.DateUtil;
import com.ligootech.webdtu.util.EasyUiUtil;
import com.ligootech.webdtu.util.StringUtil;
import com.ligootech.webdtu.web.util.ShiroUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by wly on 2015/12/18 13:58.
 */
@Controller
@RequestMapping("/simcard")
public class SimCardController {
    @Autowired
    private  HttpServletRequest request;

    @Autowired
    private SimManager simManager;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String dtuGet(Model model){

        return "simcard/simCardPage";
    }
    @RequestMapping(value = "/getTableData", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String findData(@RequestParam(value = "rows", defaultValue = "20") int rows, //每页数据量
                           @RequestParam(value = "page", defaultValue = "1") int page,//开始页码位置
                           @RequestParam(value = "sort", defaultValue = "") String sort,//排序字段
                           @RequestParam(value = "order", defaultValue = "") String order, //升降序标识 DESC asc
                           @RequestParam(value = "keywords", defaultValue = "") String keywords //升降序标识 DESC asc
    ){

        EasyUiUtil.PageForData pageObj = simManager.findSIMList(rows, page,sort,order, keywords);
        String jsonStr = JSONObject.fromObject(pageObj).toString();
        return jsonStr;
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delSimCard", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String delSimCard(@RequestParam(value = "ids", defaultValue = "") String ids ){
        if (null == ids || "".equals(ids.trim())){
            return "error";
        }
        int rs = simManager.delSimCard(ids, ShiroUtil.getCurrentUser());
        if (rs > 0){
            return "ok";
        }

        return "error";
    }

    @RequestMapping(value = "/saveSimCard", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String saveSimCard(@RequestParam(value = "filename", defaultValue = "") String filename ) throws Exception{
        if (null == filename || "".equals(filename.trim())){
            return "文件上传失败";
        }
        int rs = simManager.saveSimCard(filename, ShiroUtil.getCurrentUser());
        if (rs > 0){
            return "成功导入" + rs + "条";
        }

        return "error";
    }
}
