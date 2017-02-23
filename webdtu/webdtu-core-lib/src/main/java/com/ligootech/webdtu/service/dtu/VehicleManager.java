package com.ligootech.webdtu.service.dtu;

import java.util.Date;
import java.util.List;

import org.appdot.service.GenericManager;

import com.ligootech.webdtu.entity.core.Vehicle;

public interface VehicleManager extends GenericManager<Vehicle, Long> {
	public List<Object[]> vehicleTypeCount(int isAdmin,Long userId);
	
	public List<Object[]> batteryFactoryCount(int isAdmin,Long userId);
	
	public List<Object[]> onlineStateCount(int isAdmin,Long userId);
	
	public Vehicle getVehicleByUuid(String uuid);
	public Integer getNewVehicleCount(Long userId,String createTime);
	public Integer getNewVehicleCount(Long userId,Date createTime);
	public List<Vehicle> getNewVehiclesMonth(int isAdmin,Long userId,Date beginDate,Date endDate);
}
