package com.ligootech.webdtu.entity.core.clientForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2016/7/12 16:35.
 */
public class ToolInfoB implements Serializable {

    private String user_id;
    private String order_no;
    private String total_result;
    private String uuid;
    private String name;
    private int bmu_id;
    private String sn;
    private int device_type;
    private int tool_id;
    private String soft_version;
    private int report_type;

    private List<ToolDetail> lists = new ArrayList<ToolDetail>();
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

    public int getBmu_id() {
        return bmu_id;
    }

    public void setBmu_id(int bmu_id) {
        this.bmu_id = bmu_id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getDevice_type() {
        return device_type;
    }

    public void setDevice_type(int device_type) {
        this.device_type = device_type;
    }

    public int getTool_id() {
        return tool_id;
    }

    public void setTool_id(int tool_id) {
        this.tool_id = tool_id;
    }

    public String getSoft_version() {
        return soft_version;
    }

    public void setSoft_version(String soft_version) {
        this.soft_version = soft_version;
    }

    public int getReport_type() {
        return report_type;
    }

    public void setReport_type(int report_type) {
        this.report_type = report_type;
    }

    public List<ToolDetail> getLists() {
        return lists;
    }

    public void setLists(List<ToolDetail> lists) {
        this.lists = lists;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }
}
