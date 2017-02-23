package com.ligootech.webdtu.repository;

import java.util.List;

import com.ligootech.webdtu.dto.MonthBatteryAlarmDto;



public interface MonthCountDao {
	public Integer getNewVehicleMonthCount(int isAdmin, Long userId,String startDate,String endDate);
	public Integer newVehicleRunningMilege(int isAdmin, Long userId,String startDate,String endDate);
	public float newVehicleRunningTime(int isAdmin, Long userId,String startDate,String endDate);
	public Integer alarmCountMonth(int isAdmin, Long userId, String startDate,String endDate);
	public List<String> alarmCountMonthByType(int isAdmin, Long userId, String startDate,String endDate);
	public List<Object[]> vehicleTypeMonthCount(int isAdmin,Long userId,String startDate,String endDate);
	public List<Object[]> batteryModelMonthCount(int isAdmin,Long userId,String startDate,String endDate);
	public List<MonthBatteryAlarmDto> batteryAlarmCount(int isAdmin,Long userId,String startDate,String endDate);
	public List<Object[]> vehicleRunningMonthCount(int isAdmin,Long userId,String startDate,String endDate);
	
}
