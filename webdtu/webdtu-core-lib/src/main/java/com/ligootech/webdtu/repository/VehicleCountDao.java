package com.ligootech.webdtu.repository;

import java.util.List;

public interface VehicleCountDao {
	public float totalRunningMilege(Long dtuId);
	public float totalRunningTime(Long dtuId);
	public int totalAlarmCount(Long dtuId);
	public String onlineStatus(Long dtuId);
	public List totalCapacityCurve(Long dtuId);
	public List getAlarmHistory(Long dtuId);
	public List vehicleChargeCycleList(Long dtuId);
	public List batteryAlarmCount(Long dtuId);
	public List overviewQuot(int isAdmin,Long userId);
}
