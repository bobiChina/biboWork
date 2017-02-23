package com.ligootech.webdtu.entity.core.clientForm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wly on 2016/1/16 14:09.
 */
public class ToolInfo implements Serializable {
    private String user_id;
    private String order_no;
    private String total_result;
    private String uuid;
    private String name;
    private int bmu_id;
    private String sn;
    private Map<String, String> detail = new HashMap<String, String>();
    private Map<String, String> files = new HashMap<String, String>();

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getTotal_result() {
        return total_result;
    }

    public void setTotal_result(String total_result) {
        this.total_result = total_result;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, String> detail) {
        this.detail = detail;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getBmu_id() {
        return bmu_id;
    }

    public void setBmu_id(int bmu_id) {
        this.bmu_id = bmu_id;
    }
}
