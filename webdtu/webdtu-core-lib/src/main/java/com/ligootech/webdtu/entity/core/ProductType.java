package com.ligootech.webdtu.entity.core;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wly on 2015/11/10 11:45.
 */
@Entity
@Table(name = "prod_type")
public class ProductType {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prodTypename")
    private String prodTypename;

    @ManyToOne
    @JoinColumn(name = "optUserid")
    private DtuUser optUser;

    @Column(name = "optTime")
    private Date optTime;

    @Column(name = "isMainframe")
    private int isMainframe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdTypename() {
        return prodTypename;
    }

    public void setProdTypename(String prodTypename) {
        this.prodTypename = prodTypename;
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

    public int getIsMainframe() {
        return isMainframe;
    }

    public void setIsMainframe(int isMainframe) {
        this.isMainframe = isMainframe;
    }
}
