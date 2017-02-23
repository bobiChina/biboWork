package com.ligootech.webdtu.entity.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "dtuUser")
public class DtuUser {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "userName")
    private String userName;
    
    @Column(name = "userPass")
    private String userPass;
    
    @Column(name = "fullName")
    private String fullName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "relation")
    private String relation;
    
    @Column(name = "isAdmin")
    private int isAdmin;

	@Column(name = "isSalesman")
    private int isSalesman;

	@Column(name = "isTechnicist")
    private int isTechnicist;
    
    @ManyToOne
    @JoinColumn(name = "corpId")
    private Corp corp;

	@Column(name = "optUserId")
	private Long optUser;

	@Column(name = "optUserName")
	private String optUserName;

	@Column(name = "optTime")
	private Date optTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Corp getCorp() {
		return corp;
	}

	public void setCorp(Corp corp) {
		this.corp = corp;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Long getOptUser() {
		return optUser;
	}

	public void setOptUser(Long optUser) {
		this.optUser = optUser;
	}

	public String getOptUserName() {
		return optUserName;
	}

	public void setOptUserName(String optUserName) {
		this.optUserName = optUserName;
	}

	public Date getOptTime() {
		return optTime;
	}

	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}

	public int getIsSalesman() {
		return isSalesman;
	}

	public void setIsSalesman(int isSalesman) {
		this.isSalesman = isSalesman;
	}

	public int getIsTechnicist() {
		return isTechnicist;
	}

	public void setIsTechnicist(int isTechnicist) {
		this.isTechnicist = isTechnicist;
	}
}
