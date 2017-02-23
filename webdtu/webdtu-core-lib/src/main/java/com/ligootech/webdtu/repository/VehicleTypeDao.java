package com.ligootech.webdtu.repository;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ligootech.webdtu.entity.core.VehicleType;

public interface VehicleTypeDao extends BaseJpaRepository<VehicleType, Long>, JpaSpecificationExecutor<VehicleType>{

}
