package com.ligootech.webdtu.dto;

public class MonthBatteryAlarmDto {
	private String batteryName;
	private int leakElec;
	private int communiStatus;
	private int outTemper;
	private int outRealease;
	private int outCharge;
	private int socLow;
	private int socHigh;
	private int outStream;
	private int temperDiffMax;
	private int voltageDiffMax;
	private int voltageCheckExeption;
	private int temperCheckExeption;
	private int totalVoltageHigh;
	private int totalVoltageLow;
	public String getBatteryName() {
		return batteryName;
	}
	public void setBatteryName(String batteryName) {
		this.batteryName = batteryName;
	}
	public int getLeakElec() {
		return leakElec;
	}
	public void setLeakElec(int leakElec) {
		this.leakElec = leakElec;
	}
	public int getCommuniStatus() {
		return communiStatus;
	}
	public void setCommuniStatus(int communiStatus) {
		this.communiStatus = communiStatus;
	}
	public int getOutTemper() {
		return outTemper;
	}
	public void setOutTemper(int outTemper) {
		this.outTemper = outTemper;
	}
	public int getOutRealease() {
		return outRealease;
	}
	public void setOutRealease(int outRealease) {
		this.outRealease = outRealease;
	}
	public int getOutCharge() {
		return outCharge;
	}
	public void setOutCharge(int outCharge) {
		this.outCharge = outCharge;
	}
	public int getSocLow() {
		return socLow;
	}
	public void setSocLow(int socLow) {
		this.socLow = socLow;
	}
	public int getSocHigh() {
		return socHigh;
	}
	public void setSocHigh(int socHigh) {
		this.socHigh = socHigh;
	}
	public int getOutStream() {
		return outStream;
	}
	public void setOutStream(int outStream) {
		this.outStream = outStream;
	}
	public int getTemperDiffMax() {
		return temperDiffMax;
	}
	public void setTemperDiffMax(int temperDiffMax) {
		this.temperDiffMax = temperDiffMax;
	}
	public int getVoltageDiffMax() {
		return voltageDiffMax;
	}
	public void setVoltageDiffMax(int voltageDiffMax) {
		this.voltageDiffMax = voltageDiffMax;
	}
	public int getVoltageCheckExeption() {
		return voltageCheckExeption;
	}
	public void setVoltageCheckExeption(int voltageCheckExeption) {
		this.voltageCheckExeption = voltageCheckExeption;
	}
	public int getTemperCheckExeption() {
		return temperCheckExeption;
	}
	public void setTemperCheckExeption(int temperCheckExeption) {
		this.temperCheckExeption = temperCheckExeption;
	}
	public int getTotalVoltageHigh() {
		return totalVoltageHigh;
	}
	public void setTotalVoltageHigh(int totalVoltageHigh) {
		this.totalVoltageHigh = totalVoltageHigh;
	}
	public int getTotalVoltageLow() {
		return totalVoltageLow;
	}
	public void setTotalVoltageLow(int totalVoltageLow) {
		this.totalVoltageLow = totalVoltageLow;
	}
	
}
