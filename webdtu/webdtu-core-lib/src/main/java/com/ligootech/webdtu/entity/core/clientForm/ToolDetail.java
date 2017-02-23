package com.ligootech.webdtu.entity.core.clientForm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wly on 2016/7/12 16:35.
 */
public class ToolDetail implements Serializable {
    private String check_item;
    private String res;
    private Map<String, String> detail = new HashMap<String, String>();

    public String getCheck_item() {
        return check_item;
    }

    public void setCheck_item(String check_item) {
        this.check_item = check_item;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public Map<String, String> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, String> detail) {
        this.detail = detail;
    }
}
