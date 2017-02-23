package com.ligootech.webdtu.service.dtu;

import java.util.List;

import com.ligootech.webdtu.dto.MonthBatteryAlarmDto;


public interface MonthCountManager {
	public Integer getNewVehicleMonthCount(int isAdmin, Long userId,String startDate,String endDate);
	public Integer newVehicleRunningMilege(int isAdmin, Long userId,String startDate,String endDate);
	public float newVehicleRunningTime(int isAdmin, Long userId,String startDate,String endDate);
	public Integer alarmCountMonth(int isAdmin, Long userId, String startDate,String endDate);
	public String vehicleTypeMonthCount(int isAdmin,Long userId,String startDate,String endDate);
	public String batteryModelMonthCount(int isAdmin,Long userId,String startDate,String endDate);
	public List<MonthBatteryAlarmDto> batteryAlarmCount(int isAdmin, Long userId, String startDate,String endDate);
	public List vehicleRunningMonthCount(int isAdmin, Long userId, String startDate,String endDate);
	public List<String> alarmCountMonthByType(int isAdmin, Long userId,
			String startDate, String endDate);
}
