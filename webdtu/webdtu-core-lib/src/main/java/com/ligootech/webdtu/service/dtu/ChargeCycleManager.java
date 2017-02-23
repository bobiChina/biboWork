package com.ligootech.webdtu.service.dtu;

import java.util.List;

import org.appdot.service.GenericManager;

import com.ligootech.webdtu.entity.core.ChargeCycle;

public interface ChargeCycleManager extends GenericManager<ChargeCycle, Long>{
	public List getAllMilegeAndTimeLength(int isAdmin, Long userId);
}
