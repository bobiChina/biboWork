package com.ligootech.webdtu.service.impl.dtu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.dto.MonthBatteryAlarmDto;
import com.ligootech.webdtu.repository.MonthCountDao;
import com.ligootech.webdtu.repository.VehicleDao;
import com.ligootech.webdtu.service.dtu.MonthCountManager;
@Service("monthCountManager")
public class MonthCountManagerImpl implements MonthCountManager {
	@Autowired
	MonthCountDao monthCountDao;
	@Autowired
	VehicleDao vehicleDao;
	
	@Override
	public Integer getNewVehicleMonthCount(int isAdmin, Long userId,
			String startDate, String endDate) {
		return monthCountDao.getNewVehicleMonthCount(isAdmin, userId, startDate, endDate);
	}

	@Override
	public Integer newVehicleRunningMilege(int isAdmin, Long userId,
			String startDate, String endDate) {
		return monthCountDao.newVehicleRunningMilege(isAdmin, userId, startDate, endDate);
	}

	@Override
	public float newVehicleRunningTime(int isAdmin, Long userId,
			String startDate, String endDate) {
		return monthCountDao.newVehicleRunningTime(isAdmin, userId, startDate, endDate);
	}

	@Override
	public Integer alarmCountMonth(int isAdmin, Long userId, String startDate,
			String endDate) {
		return monthCountDao.alarmCountMonth(isAdmin, userId, startDate, endDate);
	}

	@Override
	public String vehicleTypeMonthCount(int isAdmin, Long userId,
			String startDate, String endDate) {
		List<Object[]> result = monthCountDao.vehicleTypeMonthCount(isAdmin, userId, startDate, endDate);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0;i<result.size();i++){
			Object[] item = result.get(i);
			sb.append("{value:"+item[1]+",");
			sb.append("name:'"+item[0]+"'}");
			if ((result.size()-1) != i)
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String batteryModelMonthCount(int isAdmin, Long userId,
			String startDate, String endDate) {
		List<Object[]> result = monthCountDao.batteryModelMonthCount(isAdmin, userId, startDate, endDate);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0;i<result.size();i++){
			Object[] item = result.get(i);
			sb.append("{value:"+item[1]+",");
			sb.append("name:'"+item[0]+"'}");
			if ((result.size()-1) != i)
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public List<MonthBatteryAlarmDto> batteryAlarmCount(int isAdmin, Long userId, String startDate,
			String endDate) {
		return monthCountDao.batteryAlarmCount(isAdmin, userId, startDate, endDate);
	}

	@Override
	public List vehicleRunningMonthCount(int isAdmin, Long userId,
			String startDate, String endDate) {
		List result = monthCountDao.vehicleRunningMonthCount(isAdmin, userId, startDate, endDate);
		return result;
	}

	@Override
	public List<String> alarmCountMonthByType(int isAdmin, Long userId,
			String startDate, String endDate) {
		return monthCountDao.alarmCountMonthByType(isAdmin, userId, startDate, endDate);
	}
	
}
