package com.ligootech.webdtu.entity.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dtu")
public class Dtu {

    @Id
    @Column(name = "id", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "uuid")
    private String uuid;
    
    @Column(name = "onlineStatus")
    private int onlineStatus;
    
    @Column(name = "lontitude")
    private float lontitude;
    
    @Column(name = "latitude")
    private float latitude;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "soc")
    private float soc;
    
    @Column(name = "balanceStatus")
    private int balanceStatus;
    
    @Column(name = "leakElec")
    private int leakElec;
    
    @Column(name = "communiStatus")
    private int communiStatus;
    
    @Column(name = "outTemper")
    private int outTemper;
    
    @Column(name = "outRealease")
    private int outRealease;
    
    @Column(name = "outCharge")
    private int outCharge;
    
    @Column(name = "socLow")
    private int socLow;
    
    @Column(name = "socHigh")
    private int socHigh;
    
    @Column(name = "outStream")
    private int outStream;
    
    @Column(name = "temperDiffMax")
    private int temperDiffMax;
    
    @Column(name = "voltageDiffMax")
    private int voltageDiffMax;
    
    @Column(name = "voltageCheckExeption")
    private int voltageCheckExeption;
    
    @Column(name = "temperCheckExeption")
    private int temperCheckExeption;
    
    @Column(name = "totalVoltageHigh")
    private int totalVoltageHigh;
    
    @Column(name = "totalVoltageLow")
    private int totalVoltageLow;
    
    @Column(name = "totalCapacity")
    private int totalCapacity;
    
    @Column(name = "leftCapacity")
    private int leftCapacity;
    
    @Column(name = "batteryTotalVoltage")
    private float batteryTotalVoltage;
    
    @Column(name = "batteryTotalAmp")
    private float batteryTotalAmp;
    
    @Column(name = "batteryRechargeCycles")
    private int batteryRechargeCycles;
    
    @Column(name = "positiveResistance")
    private float positiveResistance;
    
    @Column(name = "negativeResistance")
    private float negativeResistance;
    
    @Column(name = "totalResistanceValue")
    private float totalResistanceValue;
    
    @Column(name = "insulationStatus")
    private int insulationStatus;
    
    @Column(name = "soh")
    private float soh;
    
    @Column(name = "maxSingleVoltage")
    private float maxSingleVoltage;
    
    @Column(name = "minSingleVoltage")
    private float minSingleVoltage;
    
    @Column(name = "maxBoxTemper")
    private float maxBoxTemper;
    
    @Column(name = "minBoxTemper")
    private float minBoxTemper;
    
    @Column(name = "maxSingleBoxId")
    private int maxSingleBoxId;
    
    @Column(name = "maxSingleString")
    private int maxSingleString;
    
    @Column(name = "minSingleBoxId")
    private int minSingleBoxId;
    
    @Column(name = "minSingleString")
    private int minSingleString;
    
    @Column(name = "maxTemperBoxId")
    private int maxTemperBoxId;
    
    @Column(name = "maxTemperString")
    private int maxTemperString;
    
    @Column(name = "minTemperBoxId")
    private int minTemperBoxId;
    
    @Column(name = "minTemperString")
    private int minTemperString;
    
    @Column(name = "insertTime")
    private Date insertTime;

    @Column(name = "simCard")
    private String simCard;
    
    @Column(name = "alarmStatus")
    private int alarmStatus;
    
    @Column(name = "chargeStatus")
    private int chargeStatus;
    
    @Column(name = "totalMilege")
    private int totalMilege;
    
	@ManyToOne
    @JoinColumn(name = "dtuUserId")
    private DtuUser dtuUser;
	
    @ManyToOne
    @JoinColumn(name = "batteryModelId")
    private BatteryModel batteryModel;

	@Column(name = "bmuNum")
	private int bmuNum;

	public int getBmuNum() {
		return bmuNum;
	}

	public void setBmuNum(int bmuNum) {
		this.bmuNum = bmuNum;
	}

	public int getTotalMilege() {
		return totalMilege;
	}

	public void setTotalMilege(int totalMilege) {
		this.totalMilege = totalMilege;
	}

	public BatteryModel getBatteryModel() {
		return batteryModel;
	}

	public void setBatteryModel(BatteryModel batteryModel) {
		this.batteryModel = batteryModel;
	}

	public int getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(int alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
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

	public int getBatteryRechargeCycles() {
		return batteryRechargeCycles;
	}

	public void setBatteryRechargeCycles(int batteryRechargeCycles) {
		this.batteryRechargeCycles = batteryRechargeCycles;
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

	public DtuUser getDtuUser() {
		return dtuUser;
	}

	public void setDtuUser(DtuUser dtuUser) {
		this.dtuUser = dtuUser;
	}
    
    public String getSimCard() {
		return simCard;
	}

	public void setSimCard(String simCard) {
		this.simCard = simCard;
	}

	public int getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(int chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}    
    
}
