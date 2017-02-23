package com.ligootech.webdtu.entity.core;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wly on 2015/10/27 9:55.
 */

@Entity
@Table(name = "tSnInfo")
public class SNInfo implements Serializable {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dtuUuid")
    private String dtuUuid;

    @Column(name = "sn")
    private String sn;

    @Column(name = "tempSn")
    private String tempSn;

    @Column(name = "orderno")
    private String orderno;

    @ManyToOne
    @JoinColumn(name = "corpId")
    private Corp corp;

    @ManyToOne
    @JoinColumn(name = "prodType")
    private ProductType prodType;

    @Column(name = "hwVersion")
    private String hwVersion;

    @Column(name = "swVersion")
    private String swVersion;

    @Column(name = "optUserid")
    private Long optUserid;

    @Column(name = "optTime")
    private Date optTime;

    @Column(name = "status")
    private int status;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "goodsCode")
    private String goodsCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTempSn() {
        return tempSn;
    }

    public void setTempSn(String tempSn) {
        this.tempSn = tempSn;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public Corp getCorp() {
        return corp;
    }

    public void setCorp(Corp corp) {
        this.corp = corp;
    }

    public ProductType getProdType() {
        return prodType;
    }

    public void setProdType(ProductType prodType) {
        this.prodType = prodType;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public void setSwVersion(String swVersion) {
        this.swVersion = swVersion;
    }

    public Long getOptUserid() {
        return optUserid;
    }

    public void setOptUserid(Long optUserid) {
        this.optUserid = optUserid;
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDtuUuid() {
        return dtuUuid;
    }

    public void setDtuUuid(String dtuUuid) {
        this.dtuUuid = dtuUuid;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
}
