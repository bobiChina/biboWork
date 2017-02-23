package com.ligootech.webdtu.service.impl.dtu;

import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.VehicleType;
import com.ligootech.webdtu.repository.VehicleTypeDao;
import com.ligootech.webdtu.service.dtu.VehicleTypeManager;

@Service("vehicleTypeManager")
public class VehicleTypeManagerImpl extends GenericManagerImpl<VehicleType, Long> implements VehicleTypeManager {

	@Autowired
	private VehicleTypeDao vehicleTypeDao;
	@Autowired
	public void setVehicleTypeDao(VehicleTypeDao vehicleTypeDao) {
		super.dao = vehicleTypeDao;
		this.vehicleTypeDao = vehicleTypeDao;
	}
	
}
