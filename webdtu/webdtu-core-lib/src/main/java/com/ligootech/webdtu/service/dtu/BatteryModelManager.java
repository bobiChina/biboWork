package com.ligootech.webdtu.service.dtu;

import java.util.List;

import org.appdot.service.GenericManager;

import com.ligootech.webdtu.entity.core.BatteryModel;

public interface BatteryModelManager extends GenericManager<BatteryModel, Long>{
	public List<String> getBatteryFacNames(Long userId,int isAdmin);

	/**
	 * 插入信息并返回ID值
	 * @param batteryModel
	 * @return
	 */
	public Long insertBatteryModel(BatteryModel batteryModel);
}
