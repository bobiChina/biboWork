package com.ligootech.webdtu.entity.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bmu")
public class Bmu {
    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "bmuNumber")
    private int bmuNumber;
    
    @Column(name = "batteryNumber")
    private int batteryNumber;
    
    @Column(name = "singleVoltageList")
    private String singleVoltageList;
    
    @Column(name = "temperSensorNumber")
    private int temperSensorNumber;
    
    @Column(name = "boxTemperList")
    private String boxTemperList;
    
    @Column(name = "balanceAmpNumber")
    private int balanceAmpNumber;
    
    @Column(name = "balanceAmpList")
    private String balanceAmpList;
    
    @Column(name = "hotStatus")
    private int hotStatus;
    
    @Column(name = "fanStatus")
    private int fanStatus;
    
    @ManyToOne
    @JoinColumn(name = "dtuId")
    private Dtu dtu;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public int getBmuNumber() {
		return bmuNumber;
	}

	public void setBmuNumber(int bmuNumber) {
		this.bmuNumber = bmuNumber;
	}

	public int getBatteryNumber() {
		return batteryNumber;
	}

	public void setBatteryNumber(int batteryNumber) {
		this.batteryNumber = batteryNumber;
	}

	public String getSingleVoltageList() {
		return singleVoltageList;
	}

	public void setSingleVoltageList(String singleVoltageList) {
		this.singleVoltageList = singleVoltageList;
	}

	public int getTemperSensorNumber() {
		return temperSensorNumber;
	}

	public void setTemperSensorNumber(int temperSensorNumber) {
		this.temperSensorNumber = temperSensorNumber;
	}

	public String getBoxTemperList() {
		return boxTemperList;
	}

	public void setBoxTemperList(String boxTemperList) {
		this.boxTemperList = boxTemperList;
	}

	public int getBalanceAmpNumber() {
		return balanceAmpNumber;
	}

	public void setBalanceAmpNumber(int balanceAmpNumber) {
		this.balanceAmpNumber = balanceAmpNumber;
	}

	public String getBalanceAmpList() {
		return balanceAmpList;
	}

	public void setBalanceAmpList(String balanceAmpList) {
		this.balanceAmpList = balanceAmpList;
	}

	public int getHotStatus() {
		return hotStatus;
	}

	public void setHotStatus(int hotStatus) {
		this.hotStatus = hotStatus;
	}

	public int getFanStatus() {
		return fanStatus;
	}

	public void setFanStatus(int fanStatus) {
		this.fanStatus = fanStatus;
	}

	public Dtu getDtu() {
		return dtu;
	}

	public void setDtu(Dtu dtu) {
		this.dtu = dtu;
	}
    
}
