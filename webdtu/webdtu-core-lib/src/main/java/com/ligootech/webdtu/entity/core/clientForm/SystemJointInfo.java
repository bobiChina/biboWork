package com.ligootech.webdtu.entity.core.clientForm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wly on 2016/1/6 9:42.
 */
public class SystemJointInfo implements Serializable {
    private String uuid;
    private String single_result;
    private String sn;
    private String type;
    private String name;
    private Map<String, String> detail = new HashMap<String, String>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSingle_result() {
        return single_result;
    }

    public void setSingle_result(String single_result) {
        this.single_result = single_result;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
