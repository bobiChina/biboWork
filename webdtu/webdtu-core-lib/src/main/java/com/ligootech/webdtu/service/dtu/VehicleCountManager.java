package com.ligootech.webdtu.service.dtu;

import java.util.List;


public interface VehicleCountManager {
	public float totalRunningMilege(Long dtuId);
	public float totalRunningTime(Long dtuId);
	public int totalAlarmCount(Long dtuId);
	public String onlineStatus(Long dtuId);
	public String totalCapacityCurve(Long dtuId);
	public List getAlarmHistory(Long dtuId);
	public List vehicleChargeCycleList(Long dtuId);
	public String batteryAlarmCount(Long dtuId);
	public List overviewQuot(int isAdmin, Long userId);
}
