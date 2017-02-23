package com.ligootech.webdtu.entity.core;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tOrder")
public class Order {

	@Id
	@Column(name = "id", insertable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "orderno")
    private String orderno;

	@ManyToOne
	@JoinColumn(name = "corpId", nullable = true)
	private Corp corp;

    @Column(name = "quantity", nullable = true)
    private Long quantity;

	@Column(name = "projectNote", nullable = true)
    private String projectNote;

	@Column(name = "contractNo", nullable = true)
    private String contractNo;

	@Column(name = "technicalDelegate", nullable = true)
    private String technicalDelegate;

	@Column(name = "salesman", nullable = true)
    private String salesman;

    @Column(name = "status")
    private int status;

    @Column(name = "optUserid")
    private Long optUser;

	@Column(name = "optUserName")
    private String optUserName;

    @Column(name = "optTime")
    private Date optTime;

    @ManyToOne
    @JoinColumn(name = "batteryModelId", nullable = true)
    private BatteryModel batteryModel;

	@ManyToOne
	@JoinColumn(name = "vehicleTypeId", nullable = true)
	private VehicleType vehicleType;

	@ManyToOne
	@JoinColumn(name = "vehicleModelId", nullable = true)
	private VehicleModel vehicleModel;

	@Column(name = "orderType", nullable = true)
	private int orderType;

	@Column(name = "boundOrder", nullable = true)
	private String boundOrder;

	@Column(name = "shAddType", nullable = true)
	private int shAddType;

	@Column(name = "orderNote", nullable = true)
	private String orderNote;

	@Column(name = "beforeSoftware", nullable = true)
	private String beforeSoftware;

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

	public Corp getCorp() {
		return corp;
	}

	public void setCorp(Corp corp) {
		this.corp = corp;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public BatteryModel getBatteryModel() {
		return batteryModel;
	}

	public void setBatteryModel(BatteryModel batteryModel) {
		this.batteryModel = batteryModel;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public VehicleModel getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(VehicleModel vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getProjectNote() {
		return projectNote;
	}

	public void setProjectNote(String projectNote) {
		this.projectNote = projectNote;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getTechnicalDelegate() {
		return technicalDelegate;
	}

	public void setTechnicalDelegate(String technicalDelegate) {
		this.technicalDelegate = technicalDelegate;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getBoundOrder() {
		return boundOrder;
	}

	public void setBoundOrder(String boundOrder) {
		this.boundOrder = boundOrder;
	}

	public int getShAddType() {
		return shAddType;
	}

	public void setShAddType(int shAddType) {
		this.shAddType = shAddType;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	public String getBeforeSoftware() {
		return beforeSoftware;
	}

	public void setBeforeSoftware(String beforeSoftware) {
		this.beforeSoftware = beforeSoftware;
	}
}
