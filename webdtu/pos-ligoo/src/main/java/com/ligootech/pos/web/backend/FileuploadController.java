package com.ligootech.pos.web.backend;

import com.ligootech.pos.web.util.DateEditor;
import com.ligootech.pos.web.util.ShiroUtil;
import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import com.ligootech.webdtu.service.order.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by wly on 2016/2/26 9:41.
 */
@Controller
@RequestMapping("/fileupload")
public class FileuploadController {
    @Autowired
    OrderManager orderManager;

    @Autowired
    DtuUserManager dtuUserManager;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String forwardOrdFilepage(Model model, HttpServletRequest request){
        Long userId = ShiroUtil.getCurrentUser().getId();
        List<Object[]> list = orderManager.findSimpleOrderList(userId, 1);

        model.addAttribute("orderList", list);

        DtuUser user = dtuUserManager.get(userId);
        model.addAttribute("user", user);

        return "backend/fileupload/orderFilePage";
    }
}
