package com.ligootech.webdtu.repository;

import java.util.List;

import com.ligootech.webdtu.dto.DayAlarmListDto;

public interface DtuCountDao {
	public List getDtuList(String uuid,int vehicleTypeId,int vehicleModelId,String facName,
			int chargeStatus,int alarmStatus,int sort,int pageNumber,int pageSize,Long userId,int isAdmin,String city);
	public Integer getDtuListCount(String uuid,int vehicleTypeId,int vehicleModelId,String facName,
			int chargeStatus,int alarmStatus,int sort,Long userId,int isAdmin,String city);
	/**
	 * 获取实时报警列表，列表页
	 * @param uuid
	 * @param isAdmin
	 * @param userId
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List getAlarmRealtime(String uuid,int isAdmin,Long userId,int pageNumber,int pageSize);
	/**
	 * 获取实时报警页报警数量count
	 * @param uuid
	 * @param isAdmin
	 * @param userId
	 * @return
	 */
	public Integer getAlarmRealtimeCount(String uuid,int isAdmin,Long userId);
	/**
	 * 日报中获取当日新增车辆
	 * @param isAdmin
	 * @param userId
	 * @param createDate
	 * @return
	 */
	public Integer getNewVehicleDayCount(int isAdmin,Long userId,String createDate);
	/**
	 * 日报中获取某位客户的车辆总数
	 * @param isAdmin
	 * @param userId
	 * @return
	 */
	public Integer getVehicleCount(int isAdmin,Long userId,String countDate);
	/**
	 * 日报中今日新增车辆运行里程
	 * @param isAdmin
	 * @param userId
	 * @return
	 */
	public float newVehicleRunningMilege(int isAdmin,Long userId,String countDate);

	/**
	 * 日报中获取客户的车辆运行总里程
	 * @param isAdmin
	 * @param userId
	 * @param countDate
	 * @return
	 */
	public float vehicleRunningMilegeAll(int isAdmin,Long userId,String countDate);
	/**
	 * 日报中今日新增车辆的运行时长
	 * @param isAdmin
	 * @param userId
	 * @param countDate
	 * @return
	 */
	public float newVehicleRunningTime(int isAdmin,Long userId,String countDate);
	/**
	 * 日报中客户的车辆运行总时长
	 * @param isAdmin
	 * @param userId
	 * @return
	 */
	public float vehicleRunningTimeAll(int isAdmin,Long userId,String countDate);
	/**
	 * 月报中客户的最大车辆运行总时长
	 * @param isAdmin
	 * @param userId
	 * @return
	 */
	public float maxRunningTimeAll(int isAdmin,Long userId,String firstDay,String lastDay);
	/**
	 * 日报中今日故障数
	 * @param isAdmin
	 * @param userId
	 * @param countDate
	 * @return
	 */
	public Integer alarmCountDay(int isAdmin,Long userId,String countDate);
	/**
	 * 日报中故障总数统计
	 * @param isAdmin
	 * @param userId
	 * @return
	 */
	public Integer alarmCountAll(int isAdmin,Long userId,String countDate);
	/**
	 * 日报获取今日上线新增车辆列表
	 * @param isAdmin
	 * @param userId
	 * @param createDate
	 * @return
	 */
	public List<Object[]> getNewVehicleListToday(int isAdmin,Long userId,String createDate);
	/**
	 * 日报，获取今日报警列表
	 * @param isAdmin
	 * @param userId
	 * @param createDate
	 * @return
	 */
	public List<DayAlarmListDto> getAlarmListToday(int isAdmin,Long userId,String createDate);

	/**
	 * 日报中获取今日车辆充电情况
	 * @param isAdmin
	 * @param userId
	 * @param today
	 * @return
	 */
	public List<Object[]> getVehicleChargeStatus(int isAdmin,Long userId,String today);
	/**
	 * 日报中获取今日车辆运行情况
	 */
	public List<Object[]> getVehicleRunningStatus(int isAdmin,Long userId,String today);
	/**
	 * 当日在线车辆数
	 */
	public Integer onlineVehicleToday(int isAdmin,Long userId,String today);
	/**
	 * 获取概览页最大单车运行时长
	 */
	public float getMaxRunningTime(int isAdmin,Long userId);
}
