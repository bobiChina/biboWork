package com.ligootech.webdtu.dto;

public class MonthVehicleRunningDto {
	private Long dtuId;
	private float totalMilege;
	private float runningTime;
	private String vehicleNumber;
	private String vehicleType;
	private String factoryName;
	private float lontitude;
	private float latitude;
	private float runningCycle;
	private int chargeCycle;
	private int alarmCount;
	
	public Long getDtuId() {
		return dtuId;
	}
	public void setDtuId(Long dtuId) {
		this.dtuId = dtuId;
	}
	public float getTotalMilege() {
		return totalMilege;
	}
	public void setTotalMilege(float totalMilege) {
		this.totalMilege = totalMilege;
	}
	public float getRunningTime() {
		return runningTime;
	}
	public void setRunningTime(float runningTime) {
		this.runningTime = runningTime;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
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
	public float getRunningCycle() {
		return runningCycle;
	}
	public void setRunningCycle(float runningCycle) {
		this.runningCycle = runningCycle;
	}
	public int getChargeCycle() {
		return chargeCycle;
	}
	public void setChargeCycle(int chargeCycle) {
		this.chargeCycle = chargeCycle;
	}
	public int getAlarmCount() {
		return alarmCount;
	}
	public void setAlarmCount(int alarmCount) {
		this.alarmCount = alarmCount;
	}
	
}
