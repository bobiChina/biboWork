package com.ligootech.webdtu.dto;

public class DayAlarmListDto {
	private Long dtuId;
	private String uuid;
	private String typeName;
	private String vehicleNumber;
	private String factoryName;
	private int alarmType;
	private String alarmTypeName;
	private String startTime;
	private String endTime;
	private float lontitude;
	private float latitude;
	
	public Long getDtuId() {
		return dtuId;
	}
	public void setDtuId(Long dtuId) {
		this.dtuId = dtuId;
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public int getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmTypeName() {
		return alarmTypeName;
	}
	public void setAlarmTypeName(String alarmTypeName) {
		this.alarmTypeName = alarmTypeName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
