package com.ligootech.webdtu.service.dtu;

import java.util.List;

import com.ligootech.webdtu.dto.DayAlarmListDto;
import com.ligootech.webdtu.dto.DayOnlineVehicleStatusDto;

public interface CountManager {
	public Integer getNewVehicleDayCount(int isAdmin,Long userId,String createDate);
	public Integer getVehicleCount(int isAdmin, Long userId,String countDate);
	public float newVehicleRunningMilege(int isAdmin,Long userId,String countDate);
	public float vehicleRunningMilegeAll(int isAdmin,Long userId,String countDate);
	public float newVehicleRunningTime(int isAdmin,Long userId,String countDate);
	public float vehicleRunningTimeAll(int isAdmin,Long userId,String countDate);
	public float maxRunningTimeAll(int isAdmin, Long userId,String firstDay,String lastDay);
	public Integer alarmCountDay(int isAdmin,Long userId,String countDate);
	public Integer alarmCountAll(int isAdmin,Long userId,String countDate);
	public List<Object[]> getNewVehicleListToday(int isAdmin,Long userId,String createDate);
	public List<DayAlarmListDto> getAlarmListToday(int isAdmin,Long userId,String createDate);
	public List<DayOnlineVehicleStatusDto> getVehicleRunningStatus(int isAdmin,Long userId,String today);
	public Integer onlineVehicleToday(int isAdmin,Long userId,String today);
	public float getMaxRunningTime(int isAdmin, Long userId);
}
