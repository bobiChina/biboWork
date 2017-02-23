package com.ligootech.webdtu.entity.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "corp")
public class Corp {
	@Id
	@Column(name = "id", insertable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @Column(name = "corpName")
    private String corpName;
    
    @Column(name = "addr", nullable = true)
    private String addr;
    
    @Column(name = "phone", nullable = true)
    private String phone;
    
    @Column(name = "email", nullable = true)
    private String email;

	@Column(name = "linkPhone", nullable = true)
	private String linkPhone;

	@Column(name = "linkEmail", nullable = true)
	private String linkEmail;

	@Column(name = "linkMan", nullable = true)
	private String linkMan;

	@Column(name = "optUser")
	private Long optUser;

	@Column(name = "optTime")
	private Date optTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getLinkEmail() {
		return linkEmail;
	}

	public void setLinkEmail(String linkEmail) {
		this.linkEmail = linkEmail;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public Long getOptUser() {
		return optUser;
	}

	public void setOptUser(Long optUser) {
		this.optUser = optUser;
	}

	public Date getOptTime() {
		return optTime;
	}

	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}
}
