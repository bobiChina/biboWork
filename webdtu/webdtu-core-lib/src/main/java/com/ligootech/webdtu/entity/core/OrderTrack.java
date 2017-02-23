package com.ligootech.webdtu.entity.core;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tOrderTrack")
public class OrderTrack {

	@Id
	@Column(name = "id", insertable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "orderno")
    private String orderno;

	@Column(name = "actionName")
    private String actionName;

	@Column(name = "actionMan")
    private String actionMan;

	@Column(name = "orderStatus")
	private int orderStatus;

	@Column(name = "actionDate")
    private Date actionDate;

	@Column(name = "amPm")
	private String amPm;

	@Column(name = "status")
	private int status;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "reviewDelivery")
	private Date reviewDelivery;

	@Column(name = "developDelivery")
	private Date developDelivery;

	@Column(name = "testDelivery")
	private Date testDelivery;

	@Column(name = "optUserId")
	private Long optUserId;

	@Column(name = "optTime")
	private Date optTime;

	@Column(name = "isNew")
	private int isNew;

	/*********************************
	 * 新加段部分
	 *********************************/
	@Column(name = "proposerMan")
	private String proposerMan;//申请人

	@Column(name = "reviewDevelopDuration")
	private int reviewDevelopDuration;

	@Column(name = "reviewTestDuration")
	private int reviewTestDuration;

	@Column(name = "reviewMan")
	private String reviewMan;

	@Column(name = "developMan")
	private String developMan;

	@Column(name = "testMan")
	private String testMan;

	@Column(name = "reviewStime")
	private Date reviewStime;

	@Column(name = "reviewEtime")
	private Date reviewEtime;

	@Column(name = "developStime")
	private Date developStime;

	@Column(name = "developEtime")
	private Date developEtime;

	@Column(name = "developDuration")
	private int developDuration;

	@Column(name = "testStime")
	private Date testStime;

	@Column(name = "testEtime")
	private Date testEtime;

	@Column(name = "testDuration")
	private int testDuration;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionMan() {
		return actionMan;
	}

	public void setActionMan(String actionMan) {
		this.actionMan = actionMan;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getAmPm() {
		return amPm;
	}

	public void setAmPm(String amPm) {
		this.amPm = amPm;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getReviewDelivery() {
		return reviewDelivery;
	}

	public void setReviewDelivery(Date reviewDelivery) {
		this.reviewDelivery = reviewDelivery;
	}

	public Date getDevelopDelivery() {
		return developDelivery;
	}

	public void setDevelopDelivery(Date developDelivery) {
		this.developDelivery = developDelivery;
	}

	public Date getTestDelivery() {
		return testDelivery;
	}

	public void setTestDelivery(Date testDelivery) {
		this.testDelivery = testDelivery;
	}

	public Long getOptUserId() {
		return optUserId;
	}

	public void setOptUserId(Long optUserId) {
		this.optUserId = optUserId;
	}

	public Date getOptTime() {
		return optTime;
	}

	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public String getReviewMan() {
		return reviewMan;
	}

	public void setReviewMan(String reviewMan) {
		this.reviewMan = reviewMan;
	}

	public String getDevelopMan() {
		return developMan;
	}

	public void setDevelopMan(String developMan) {
		this.developMan = developMan;
	}

	public String getTestMan() {
		return testMan;
	}

	public void setTestMan(String testMan) {
		this.testMan = testMan;
	}

	public Date getReviewStime() {
		return reviewStime;
	}

	public void setReviewStime(Date reviewStime) {
		this.reviewStime = reviewStime;
	}

	public Date getReviewEtime() {
		return reviewEtime;
	}

	public void setReviewEtime(Date reviewEtime) {
		this.reviewEtime = reviewEtime;
	}

	public Date getDevelopStime() {
		return developStime;
	}

	public void setDevelopStime(Date developStime) {
		this.developStime = developStime;
	}

	public Date getDevelopEtime() {
		return developEtime;
	}

	public void setDevelopEtime(Date developEtime) {
		this.developEtime = developEtime;
	}

	public Date getTestStime() {
		return testStime;
	}

	public void setTestStime(Date testStime) {
		this.testStime = testStime;
	}

	public Date getTestEtime() {
		return testEtime;
	}

	public void setTestEtime(Date testEtime) {
		this.testEtime = testEtime;
	}

	public String getProposerMan() {
		return proposerMan;
	}

	public void setProposerMan(String proposerMan) {
		this.proposerMan = proposerMan;
	}

	public int getReviewDevelopDuration() {
		return reviewDevelopDuration;
	}

	public void setReviewDevelopDuration(int reviewDevelopDuration) {
		this.reviewDevelopDuration = reviewDevelopDuration;
	}

	public int getReviewTestDuration() {
		return reviewTestDuration;
	}

	public void setReviewTestDuration(int reviewTestDuration) {
		this.reviewTestDuration = reviewTestDuration;
	}

	public int getDevelopDuration() {
		return developDuration;
	}

	public void setDevelopDuration(int developDuration) {
		this.developDuration = developDuration;
	}

	public int getTestDuration() {
		return testDuration;
	}

	public void setTestDuration(int testDuration) {
		this.testDuration = testDuration;
	}
}
