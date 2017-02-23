package com.ligootech.webdtu.entity.core;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wly on 2015/11/10 11:42.
 */
@Entity
@Table(name = "tHardwareVersion")
public class HardwareVersion {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prodTypeId")
    private ProductType productType;

    @Column(name = "version")
    private String version;

    @Column(name = "status")
    private int status;

    @ManyToOne
    @JoinColumn(name = "optUserid")
    private DtuUser optUser;

    @Column(name = "optTime")
    private Date optTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DtuUser getOptUser() {
        return optUser;
    }

    public void setOptUser(DtuUser optUser) {
        this.optUser = optUser;
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }
}
