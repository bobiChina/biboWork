package com.ligootech.webdtu.entity.core;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BmuMongo {
    private int bmuNumber;
    
    private int batteryNumber;
    
    private String singleVoltageList;
    
    private int temperSensorNumber;
    
    private String boxTemperList;
    
    private int balanceAmpNumber;
    
    private String balanceAmpList;
    
    private int hotStatus;
    
    private int fanStatus;

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
    
}
