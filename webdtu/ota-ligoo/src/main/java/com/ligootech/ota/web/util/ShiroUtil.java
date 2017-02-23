package com.ligootech.ota.web.util;

import org.apache.shiro.SecurityUtils;

import com.ligootech.webdtu.service.account.ShiroUser;

public class ShiroUtil {
    public static ShiroUser getCurrentUser() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user;
    }
}
