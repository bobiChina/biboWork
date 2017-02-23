package com.ligootech.webdtu.service.account;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created by wly on 2015/12/14 11:50.
 */
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;
    public Long id;
    public String loginName;
    public String name;
    public int isAdmin;

    public ShiroUser(Long id, String loginName, String name,int isAdmin) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getName() {
        return name;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "loginName");
    }

    /**
     * 重载equals,只比较loginName
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, "loginName");
    }
}