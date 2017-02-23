package com.ligootech.webdtu.repository;

import java.util.List;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ligootech.webdtu.entity.core.VehicleModel;

public interface VehicleModelDao extends BaseJpaRepository<VehicleModel, Long>, JpaSpecificationExecutor<VehicleModel>{

	@Query("from VehicleModel as vm where vm.vehicleType.id = :typeId")
	public List<VehicleModel> getVehicleModelByTypeId(@Param("typeId") Long typeId);
}
