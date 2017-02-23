package com.ligootech.webdtu.entity.core.clientForm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/1.
 */
public class PcbaRes implements Serializable {

    private String orderNo;              //订单编号
    private String mSN;                  //模块SN
    private String deviceType;          //设备型号
    private String hwVersion;           //硬件版本号
    private int deviceCount;              //规定设备数量
    private List<String> hwVersionList;         //硬件版本可选项
    private List<Map<String, String>> pcbaList;    //pcba信息，包括 PCBA编码(pcbaCode)、PCBA型号(pcbaType)、PCBA中文名称(pcbaName)、PCBA板SN码(pcbaSN)

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getmSN() {
        return mSN;
    }

    public void setmSN(String mSN) {
        this.mSN = mSN;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public List<String> getHwVersionList() {
        return hwVersionList;
    }

    public void setHwVersionList(List<String> hwVersionList) {
        this.hwVersionList = hwVersionList;
    }

    public List<Map<String, String>> getPcbaList() {
        return pcbaList;
    }

    public void setPcbaList(List<Map<String, String>> pcbaList) {
        this.pcbaList = pcbaList;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }
}
