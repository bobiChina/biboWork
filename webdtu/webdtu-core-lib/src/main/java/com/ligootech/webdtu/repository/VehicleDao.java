package com.ligootech.webdtu.repository;

import java.util.Date;
import java.util.List;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ligootech.webdtu.entity.core.Vehicle;

public interface VehicleDao extends BaseJpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle>{
	
	@Query("SELECT count(v.vehicleType.id),vt.typeName FROM Vehicle as v,VehicleType as vt where v.vehicleType.id = vt.id and v.dtuUser.id=:userId GROUP BY v.vehicleType.id")
	public List<Object[]> vehicleTypeCount(@Param("userId") Long userId);
	
	@Query("SELECT count(v.vehicleType.id),vt.typeName FROM Vehicle as v,VehicleType as vt where v.vehicleType.id = vt.id GROUP BY v.vehicleType.id")
	public List<Object[]> vehicleTypeCountAdmin();
	
	@Query("SELECT COUNT(d.batteryModel.id),bm.factoryName from Dtu as d,BatteryModel as bm WHERE d.batteryModel.id = bm.id GROUP BY d.batteryModel.id")
	public List<Object[]> batteryFactoryCountAdmin();
	@Query("SELECT COUNT(d.batteryModel.id),bm.factoryName from Dtu as d,BatteryModel as bm WHERE d.batteryModel.id = bm.id and d.dtuUser.id=:userId GROUP BY d.batteryModel.id")
	public List<Object[]> batteryFactoryCount(@Param("userId") Long userId);
	
	@Query("SELECT count(d.chargeStatus),d.chargeStatus from Dtu as d  GROUP BY d.chargeStatus")
	public List<Object[]> onlineStateCountAdmin();
	@Query("SELECT count(d.chargeStatus),d.chargeStatus from Dtu as d where d.dtuUser.id=:userId GROUP BY d.chargeStatus")
	public List<Object[]> onlineStateCount(@Param("userId") Long userId);
	
	@Query("from Vehicle as v where v.uuid = :uuid")
	public Vehicle getVehicleByUuid(@Param("uuid") String uuid);
	
	@Query("from Vehicle as v where v.dtuUser.id = :userId and v.createTime = :createTime")
	public Integer getNewVehicleCount(@Param("userId") Long userId,@Param("createTime") String createTime);
	
	@Query("from Vehicle as v where v.dtuUser.id = :userId and v.createTime = :createTime")
	public Integer getNewVehicleCount(@Param("userId") Long userId,@Param("createTime") Date createTime);

	@Query("from Vehicle as v where v.dtuUser.id = :userId and v.createTime >= :beginDate and v.createTime <= :endDate")
	public List<Vehicle> getNewVehiclesMonth(@Param("userId") Long userId,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);
	
	@Query("from Vehicle as v where v.createTime >= :beginDate and v.createTime <= :endDate")
	public List<Vehicle> getNewVehiclesMonthAdmin(@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);
}
