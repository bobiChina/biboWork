package com.ligootech.webdtu.service.impl.dtu;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.dto.DayAlarmListDto;
import com.ligootech.webdtu.dto.DayOnlineVehicleStatusDto;
import com.ligootech.webdtu.repository.DtuCountDao;
import com.ligootech.webdtu.service.dtu.CountManager;
@Service("countManager")
public class CountManagerImpl implements CountManager {

	@Autowired
	DtuCountDao dtuCountDao;
	
	@Override
	public Integer getNewVehicleDayCount(int isAdmin, Long userId,
			String createDate) {
		return dtuCountDao.getNewVehicleDayCount(isAdmin, userId, createDate);
	}

	@Override
	public Integer getVehicleCount(int isAdmin, Long userId,String countDate) {
		return dtuCountDao.getVehicleCount(isAdmin, userId,countDate);
	}

	@Override
	public float newVehicleRunningMilege(int isAdmin, Long userId,
			String countDate) {
		return dtuCountDao.newVehicleRunningMilege(isAdmin, userId, countDate);
	}

	@Override
	public float vehicleRunningMilegeAll(int isAdmin, Long userId,String countDate) {
		return dtuCountDao.vehicleRunningMilegeAll(isAdmin, userId,countDate);
	}

	@Override
	public float newVehicleRunningTime(int isAdmin, Long userId,
			String countDate) {
		return dtuCountDao.newVehicleRunningTime(isAdmin, userId, countDate);
	}

	@Override
	public float vehicleRunningTimeAll(int isAdmin, Long userId,String countDate) {
		return dtuCountDao.vehicleRunningTimeAll(isAdmin, userId,countDate);
	}

	@Override
	public Integer alarmCountDay(int isAdmin, Long userId, String countDate) {
		return dtuCountDao.alarmCountDay(isAdmin, userId, countDate);
	}

	@Override
	public Integer alarmCountAll(int isAdmin, Long userId,String countDate) {
		return dtuCountDao.alarmCountAll(isAdmin, userId, countDate);
	}
	
	@Override
	public List<Object[]> getNewVehicleListToday(int isAdmin, Long userId,
			String createDate) {
		return dtuCountDao.getNewVehicleListToday(isAdmin, userId, createDate);
	}

	@Override
	public List<DayAlarmListDto> getAlarmListToday(int isAdmin, Long userId,
			String createDate) {
		return dtuCountDao.getAlarmListToday(isAdmin, userId, createDate);
	}

	@Override
	public List<DayOnlineVehicleStatusDto> getVehicleRunningStatus(int isAdmin,
			Long userId, String today) {
		List<Object[]> chargeResult = dtuCountDao.getVehicleChargeStatus(isAdmin, userId, today);
		List<Object[]> runningResult = dtuCountDao.getVehicleRunningStatus(isAdmin, userId, today);
		Map<Long,DayOnlineVehicleStatusDto> result = new HashMap<Long, DayOnlineVehicleStatusDto>();
		for (int i=0;i<chargeResult.size();i++){
			Object[] item = chargeResult.get(i);
			Long dtuId = ((BigInteger)item[0]).longValue();
			if (result.get(dtuId) == null){
				DayOnlineVehicleStatusDto dtoValue = new DayOnlineVehicleStatusDto();
				dtoValue.setDtuId(dtuId);
				BigDecimal chargeTime = (BigDecimal)item[1];
				dtoValue.setChargeTime(chargeTime.floatValue());
				dtoValue.setVehicleNumber((String)item[2]);
				dtoValue.setVehicleTypeName((String)item[3]);
				dtoValue.setFactoryName((String)item[4]);
				dtoValue.setLontitude((Float)item[5]);
				dtoValue.setLatitude((Float)item[6]);
				result.put(dtuId, dtoValue);
			}else{
				DayOnlineVehicleStatusDto dtoValue = result.get(dtuId);
				BigDecimal chargeTime = (BigDecimal)item[1];
				dtoValue.setChargeTime(dtoValue.getChargeTime() + chargeTime.floatValue());
			}
		}
		for (int i=0;i<runningResult.size();i++){
			Object[] item = runningResult.get(i);
			Long dtuId = ((BigInteger)item[0]).longValue();
			if (result.get(dtuId) == null){
				DayOnlineVehicleStatusDto dtoValue = new DayOnlineVehicleStatusDto();
				dtoValue.setDtuId(dtuId);
				Double runningMilege = (Double)item[1];
				dtoValue.setRunningMilege(runningMilege.floatValue());
				BigDecimal runningTime = (BigDecimal)item[2];
				dtoValue.setRunningTime(runningTime.floatValue());
				dtoValue.setVehicleNumber((String)item[3]);
				dtoValue.setVehicleTypeName((String)item[4]);
				dtoValue.setFactoryName((String)item[5]);
				dtoValue.setLontitude((Float)item[6]);
				dtoValue.setLatitude((Float)item[7]);				
				result.put(dtuId, dtoValue);
			}else{
				DayOnlineVehicleStatusDto dtoValue = result.get(dtuId);
				Double runningMilege = (Double)item[1];
				dtoValue.setRunningMilege(dtoValue.getRunningMilege() + runningMilege.floatValue());
				BigDecimal runningTime = (BigDecimal)item[2];
				dtoValue.setRunningTime(dtoValue.getRunningTime() + runningTime.floatValue());				
			}
		}
		
		List<DayAlarmListDto> alarmList = dtuCountDao.getAlarmListToday(isAdmin, userId, today);
		for(int i=0;i<alarmList.size();i++){
			DayAlarmListDto item = alarmList.get(i);
			Long dtuId = item.getDtuId();
			if (result.get(dtuId) == null){
				DayOnlineVehicleStatusDto dtoValue = new DayOnlineVehicleStatusDto();
				dtoValue.setDtuId(dtuId);
				dtoValue.setAlarmCount(1);
				dtoValue.setVehicleNumber(item.getVehicleNumber());
				dtoValue.setVehicleTypeName(item.getTypeName());
				dtoValue.setFactoryName(item.getFactoryName());
				dtoValue.setLontitude(item.getLontitude());
				dtoValue.setLatitude(item.getLatitude());				
				result.put(dtuId, dtoValue);
			}else{
				DayOnlineVehicleStatusDto dtoValue = result.get(dtuId);
				dtoValue.setAlarmCount(dtoValue.getAlarmCount() + 1);
			}
		}
		
		List<DayOnlineVehicleStatusDto> list = new ArrayList(result.values());
		return list;
	}

	@Override
	public Integer onlineVehicleToday(int isAdmin, Long userId, String today) {
		return dtuCountDao.onlineVehicleToday(isAdmin,userId,today);
	}

	@Override
	public float maxRunningTimeAll(int isAdmin, Long userId,String firstDay,String lastDay) {
		return dtuCountDao.maxRunningTimeAll(isAdmin, userId,firstDay,lastDay);
	}

	@Override
	public float getMaxRunningTime(int isAdmin, Long userId) {
		return dtuCountDao.getMaxRunningTime(isAdmin, userId);
	}
}
