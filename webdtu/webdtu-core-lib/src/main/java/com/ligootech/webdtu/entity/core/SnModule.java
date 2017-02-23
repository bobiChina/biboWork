package com.ligootech.webdtu.entity.core;

import javax.persistence.*;

/**
 * 模块SN码信息表
 */
@Entity
@Table(name = "tSnModule")
public class SnModule {

	@Id
	@Column(name = "id", insertable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "mType")
    private String type;

    @Column(name = "mGoods")
    private Long goods;

	@Column(name = "mCode")
    private String code;

	@Column(name = "numMain")
    private int numMain;

    @Column(name = "numDtu")
    private int numDtu;

	@Column(name = "numCollection")
    private int numCollection;

	@Column(name = "numBalance")
    private int numBalance;

	@Column(name = "numThalposis")
    private int numThalposis;

	@Column(name = "pcbaCode")
    private String pcbaCode;

	@Column(name = "deviceType")
    private int deviceType;

	@Column(name = "className")
    private String className;

	@Column(name = "remarks")
    private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getGoods() {
		return goods;
	}

	public void setGoods(Long goods) {
		this.goods = goods;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getNumMain() {
		return numMain;
	}

	public void setNumMain(int numMain) {
		this.numMain = numMain;
	}

	public int getNumDtu() {
		return numDtu;
	}

	public void setNumDtu(int numDtu) {
		this.numDtu = numDtu;
	}

	public int getNumCollection() {
		return numCollection;
	}

	public void setNumCollection(int numCollection) {
		this.numCollection = numCollection;
	}

	public int getNumBalance() {
		return numBalance;
	}

	public void setNumBalance(int numBalance) {
		this.numBalance = numBalance;
	}

	public int getNumThalposis() {
		return numThalposis;
	}

	public void setNumThalposis(int numThalposis) {
		this.numThalposis = numThalposis;
	}

	public String getPcbaCode() {
		return pcbaCode;
	}

	public void setPcbaCode(String pcbaCode) {
		this.pcbaCode = pcbaCode;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
