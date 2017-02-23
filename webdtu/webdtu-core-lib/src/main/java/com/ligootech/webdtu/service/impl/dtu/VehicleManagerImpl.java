package com.ligootech.webdtu.service.impl.dtu;

import java.util.Date;
import java.util.List;

import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.Vehicle;
import com.ligootech.webdtu.repository.VehicleDao;
import com.ligootech.webdtu.service.dtu.VehicleManager;

@Service("vehicleManager")
public class VehicleManagerImpl extends GenericManagerImpl<Vehicle, Long> implements VehicleManager {
	@Autowired
	private VehicleDao vehicleDao;
	@Autowired
	public void setVehicleDao(VehicleDao vehicleDao) {
		super.dao = vehicleDao;
		this.vehicleDao = vehicleDao;
	}

	@Override
	public List<Object[]> vehicleTypeCount(int isAdmin,Long userId) {
		if (isAdmin == 1)
			return vehicleDao.vehicleTypeCountAdmin();
		else 
			return vehicleDao.vehicleTypeCount(userId);
	}

	@Override
	public List<Object[]> batteryFactoryCount(int isAdmin,Long userId) {
		if (isAdmin == 1)
			return vehicleDao.batteryFactoryCountAdmin();
		else
			return vehicleDao.batteryFactoryCount(userId);
		
	}

	@Override
	public List<Object[]> onlineStateCount(int isAdmin,Long userId) {
		if (isAdmin == 1)
			return vehicleDao.onlineStateCountAdmin();
		else
			return vehicleDao.onlineStateCount(userId);
	}

	@Override
	public Vehicle getVehicleByUuid(String uuid) {
		return vehicleDao.getVehicleByUuid(uuid);
	}

	@Override
	public Integer getNewVehicleCount(Long userId, String createTime) {
		return vehicleDao.getNewVehicleCount(userId, createTime);
	}

	@Override
	public Integer getNewVehicleCount(Long userId, Date createTime) {
		return vehicleDao.getNewVehicleCount(userId, createTime);
	}

	@Override
	public List<Vehicle> getNewVehiclesMonth(int isAdmin,Long userId, Date beginDate,
			Date endDate) {
		if (isAdmin == 1)
			return vehicleDao.getNewVehiclesMonthAdmin(beginDate, endDate);
		else
			return vehicleDao.getNewVehiclesMonth(userId, beginDate, endDate);
	}

	
}
