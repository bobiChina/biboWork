package com.ligootech.webdtu.entity.core;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DtuMongo {
	@Id
    private ObjectId _id;
    
    private String uuid;
    
//    private int onlineStatus;
    
    private float lontitude;
    
    private float latitude;
    
    private float soc;
    
    private int balanceStatus;
    
    private int leakElec;
    
    private int communiStatus;
    
    private int outTemper;
    
    private int outRelease;
    
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
    
    private int totalCapacity;
    
    private int leftCapacity;
    
    private float batteryTotalVoltage;
    
    private float batteryTotalAmp;
    
    private int batterRechargeCycles;
    
    private float positiveResistance;
    
    private float negativeResistance;
    
    private float totalResistanceValue;
    
    private int insulationStatus;
    
    private float soh;
    
    private float maxSingleVoltage;
    
    private float minSingleVoltage;
    
    private float maxBoxTemper;
    
    private float minBoxTemper;
    
    private int maxSingleBoxId;
    
    private int maxSingleString;
    
    private int minSingleBoxId;
    
    private int minSingleString;
    
    private int maxTemperBoxId;
    
    private int maxTemperString;
    
    private int minTemperBoxId;
    
    private int minTemperString;
    
    private Date insertTime;
    
//    private Date time;

    private String simCard;
    
    private int alarmStatus;
    
    private int chargingStatus;
    
    private int altitude;
    
    private int bcuCount;
    
    private int bmuCount;
    
    private int direction;
    
    private int speed;
    
    
    
    private List<BmuMongo> bmu;



	public List<BmuMongo> getBmu() {
		return bmu;
	}

	public void setBmu(List<BmuMongo> bmu) {
		this.bmu = bmu;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public float getSoc() {
		return soc;
	}

	public void setSoc(float soc) {
		this.soc = soc;
	}

	public int getBalanceStatus() {
		return balanceStatus;
	}

	public void setBalanceStatus(int balanceStatus) {
		this.balanceStatus = balanceStatus;
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


	public int getOutRelease() {
		return outRelease;
	}

	public void setOutRelease(int outRelease) {
		this.outRelease = outRelease;
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

	public int getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(int totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public int getLeftCapacity() {
		return leftCapacity;
	}

	public void setLeftCapacity(int leftCapacity) {
		this.leftCapacity = leftCapacity;
	}

	public float getBatteryTotalVoltage() {
		return batteryTotalVoltage;
	}

	public void setBatteryTotalVoltage(float batteryTotalVoltage) {
		this.batteryTotalVoltage = batteryTotalVoltage;
	}

	public float getBatteryTotalAmp() {
		return batteryTotalAmp;
	}

	public void setBatteryTotalAmp(float batteryTotalAmp) {
		this.batteryTotalAmp = batteryTotalAmp;
	}

	
	public float getPositiveResistance() {
		return positiveResistance;
	}

	public void setPositiveResistance(float positiveResistance) {
		this.positiveResistance = positiveResistance;
	}

	public float getNegativeResistance() {
		return negativeResistance;
	}

	public void setNegativeResistance(float negativeResistance) {
		this.negativeResistance = negativeResistance;
	}

	public float getTotalResistanceValue() {
		return totalResistanceValue;
	}

	public void setTotalResistanceValue(float totalResistanceValue) {
		this.totalResistanceValue = totalResistanceValue;
	}

	public int getInsulationStatus() {
		return insulationStatus;
	}

	public void setInsulationStatus(int insulationStatus) {
		this.insulationStatus = insulationStatus;
	}

	public float getSoh() {
		return soh;
	}

	public void setSoh(float soh) {
		this.soh = soh;
	}

	public float getMaxSingleVoltage() {
		return maxSingleVoltage;
	}

	public void setMaxSingleVoltage(float maxSingleVoltage) {
		this.maxSingleVoltage = maxSingleVoltage;
	}

	public float getMinSingleVoltage() {
		return minSingleVoltage;
	}

	public void setMinSingleVoltage(float minSingleVoltage) {
		this.minSingleVoltage = minSingleVoltage;
	}

	public float getMaxBoxTemper() {
		return maxBoxTemper;
	}

	public void setMaxBoxTemper(float maxBoxTemper) {
		this.maxBoxTemper = maxBoxTemper;
	}

	public float getMinBoxTemper() {
		return minBoxTemper;
	}

	public void setMinBoxTemper(float minBoxTemper) {
		this.minBoxTemper = minBoxTemper;
	}

	public int getMaxSingleBoxId() {
		return maxSingleBoxId;
	}

	public void setMaxSingleBoxId(int maxSingleBoxId) {
		this.maxSingleBoxId = maxSingleBoxId;
	}

	public int getMaxSingleString() {
		return maxSingleString;
	}

	public void setMaxSingleString(int maxSingleString) {
		this.maxSingleString = maxSingleString;
	}

	public int getMinSingleBoxId() {
		return minSingleBoxId;
	}

	public void setMinSingleBoxId(int minSingleBoxId) {
		this.minSingleBoxId = minSingleBoxId;
	}

	public int getMinSingleString() {
		return minSingleString;
	}

	public void setMinSingleString(int minSingleString) {
		this.minSingleString = minSingleString;
	}

	public int getMaxTemperBoxId() {
		return maxTemperBoxId;
	}

	public void setMaxTemperBoxId(int maxTemperBoxId) {
		this.maxTemperBoxId = maxTemperBoxId;
	}

	public int getMaxTemperString() {
		return maxTemperString;
	}

	public void setMaxTemperString(int maxTemperString) {
		this.maxTemperString = maxTemperString;
	}

	public int getMinTemperBoxId() {
		return minTemperBoxId;
	}

	public void setMinTemperBoxId(int minTemperBoxId) {
		this.minTemperBoxId = minTemperBoxId;
	}

	public int getMinTemperString() {
		return minTemperString;
	}

	public void setMinTemperString(int minTemperString) {
		this.minTemperString = minTemperString;
	}


	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public String getSimCard() {
		return simCard;
	}

	public void setSimCard(String simCard) {
		this.simCard = simCard;
	}

	public int getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(int alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public int getBatterRechargeCycles() {
		return batterRechargeCycles;
	}

	public void setBatterRechargeCycles(int batterRechargeCycles) {
		this.batterRechargeCycles = batterRechargeCycles;
	}

	public int getChargingStatus() {
		return chargingStatus;
	}

	public void setChargingStatus(int chargingStatus) {
		this.chargingStatus = chargingStatus;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getBcuCount() {
		return bcuCount;
	}

	public void setBcuCount(int bcuCount) {
		this.bcuCount = bcuCount;
	}

	public int getBmuCount() {
		return bmuCount;
	}

	public void setBmuCount(int bmuCount) {
		this.bmuCount = bmuCount;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


    
}
