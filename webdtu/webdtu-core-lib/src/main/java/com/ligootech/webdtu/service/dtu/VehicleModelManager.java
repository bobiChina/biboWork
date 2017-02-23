package com.ligootech.webdtu.service.dtu;

import java.util.List;

import org.appdot.service.GenericManager;

import com.ligootech.webdtu.entity.core.VehicleModel;

public interface VehicleModelManager extends GenericManager<VehicleModel, Long>{
	/**
	 * 车辆型号
	 * @param typeId
	 * @param isAdmin
	 * @param userId
	 * @return
	 */
	public List<Object[]> getVehicleModelByTypeId(Long typeId, int isAdmin, Long userId);

	/**
	 * 插入新增的车辆型号
	 * @param typeId
	 * @param model_name
	 * @return
	 */
	public Long insertVehicleModel(Long typeId, String model_name);

	/**
	 * 删除没有关联的车辆型号
	 * @return
	 */
	public int delVehicleModel();
}
