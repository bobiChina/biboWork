package com.ligootech.webdtu.dto;

public class DayOnlineVehicleStatusDto {
	private Long dtuId;
	private String vehicleNumber;
	private String vehicleTypeName;
	private String factoryName;
	private float lontitude;
	private float latitude;
	private float runningTime=0;
	private float chargeTime=0;
	private float runningMilege=0;
	private int alarmCount=0;
	
	public Long getDtuId() {
		return dtuId;
	}
	public void setDtuId(Long dtuId) {
		this.dtuId = dtuId;
	}
	public String getVehicleTypeName() {
		return vehicleTypeName;
	}
	public void setVehicleTypeName(String vehicleTypeName) {
		this.vehicleTypeName = vehicleTypeName;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public float getLontitude() {
		return lontitude;
	}
	public void setLontitude(float lontitude) {
		this.lontitude = lontitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getRunningTime() {
		return runningTime;
	}
	public void setRunningTime(float runningTime) {
		this.runningTime = runningTime;
	}
	public float getChargeTime() {
		return chargeTime;
	}
	public void setChargeTime(float chargeTime) {
		this.chargeTime = chargeTime;
	}
	public float getRunningMilege() {
		return runningMilege;
	}
	public void setRunningMilege(float runningMilege) {
		this.runningMilege = runningMilege;
	}
	public int getAlarmCount() {
		return alarmCount;
	}
	public void setAlarmCount(int alarmCount) {
		this.alarmCount = alarmCount;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	
}
