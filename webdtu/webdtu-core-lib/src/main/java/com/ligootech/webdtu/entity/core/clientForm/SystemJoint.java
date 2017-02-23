package com.ligootech.webdtu.entity.core.clientForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wly on 2016/1/6 9:38.
 */
public class SystemJoint implements Serializable {
    private String user_id;
    private String total_result;
    private String orderno;

    private List<SystemJointInfo> lists = new ArrayList<SystemJointInfo>();

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTotal_result() {
        return total_result;
    }

    public void setTotal_result(String total_result) {
        this.total_result = total_result;
    }

    public List<SystemJointInfo> getLists() {
        return lists;
    }

    public void setLists(List<SystemJointInfo> lists) {
        this.lists = lists;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
