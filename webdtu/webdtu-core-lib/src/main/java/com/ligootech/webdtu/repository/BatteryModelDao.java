package com.ligootech.webdtu.repository;

import java.util.List;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ligootech.webdtu.entity.core.BatteryModel;

public interface BatteryModelDao extends BaseJpaRepository<BatteryModel, Long>, JpaSpecificationExecutor<BatteryModel>{
	@Query("select distinct(bm.factoryName) from BatteryModel as bm")
	public List<String> getBatteryFacNamesAdmin();
	@Query("select distinct(bm.factoryName) from BatteryModel as bm,Dtu as d where d.batteryModel.id = bm.id and d.dtuUser.id = :userId")
	public List<String> getBatteryFacNames(@Param("userId") Long userId);
}
