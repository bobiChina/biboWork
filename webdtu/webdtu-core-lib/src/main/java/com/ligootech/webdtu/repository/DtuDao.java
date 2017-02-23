package com.ligootech.webdtu.repository;

import java.util.List;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ligootech.webdtu.entity.core.Dtu;

public interface DtuDao extends BaseJpaRepository<Dtu, Long>, JpaSpecificationExecutor<Dtu>{

	@Query("from Dtu")
	public List<Dtu> getDtuMaps();
	
	@Query("select lontitude,latitude,uuid from Dtu")
	public List<Dtu> getDtuLatLon();
	// 修改地图显示的车辆数据
	@Query("SELECT d.id,d.uuid,bm.factoryName,d.soc,d.onlineStatus,d.lontitude,d.latitude,v.vehicleNumber,d.chargeStatus,d.alarmStatus,d.simCard,vt.typeName FROM Dtu as d,BatteryModel as bm,Vehicle as v,VehicleType as vt where d.id=v.dtu.id and d.batteryModel.id = bm.id and v.vehicleType.id = vt.id and d.dtuUser.id=:userId and d.lontitude!=0 and d.latitude!=0")
	public List<Object[]> getDtuLocations(@Param("userId") Long userId);
	@Query("SELECT d.id,d.uuid,bm.factoryName,d.soc,d.onlineStatus,d.lontitude,d.latitude,v.vehicleNumber,d.chargeStatus,d.alarmStatus,d.simCard,vt.typeName FROM Dtu as d,BatteryModel as bm,Vehicle as v,VehicleType as vt where d.id=v.dtu.id and d.batteryModel.id = bm.id and v.vehicleType.id = vt.id and d.lontitude!=0 and d.latitude!=0")
	public List<Object[]> getDtuLocationsAdmin();
	@Query("SELECT ar.alarmType,d.uuid,ar.alarmStartTime ,bm.factoryName,d.onlineStatus FROM Dtu as d,AlarmRealtime as ar,Vehicle as v,BatteryModel as bm where d.id = ar.dtu.id and d.id = v.dtu.id and d.batteryModel.id = bm.id and v.dtuUser.id=:userId")
	public List<Object[]> getAlarmRealtime(@Param("userId") Long userId);
	
	@Query("SELECT ar.alarmType,d.uuid,ar.alarmStartTime ,bm.factoryName,d.onlineStatus FROM Dtu as d,AlarmRealtime as ar,Vehicle as v,BatteryModel as bm where d.id = ar.dtu.id and d.id = v.dtu.id and d.batteryModel.id = bm.id")
	public List<Object[]> getAlarmRealtime();	
	@Query("SELECT DISTINCT(d.city) from Dtu as d where d.dtuUser.id = :userId")
	public List<String> getCitys(@Param("userId") Long userId);
	@Query("SELECT DISTINCT(d.city) from Dtu as d")
	public List<String> getCitysAdmin();	
	@Query("SELECT MAX(ar.id) FROM Dtu as d,AlarmRealtime as ar where d.id = ar.dtu.id and d.dtuUser.id=:userId and ar.alarmStatus=1")
	public Integer getMaxAlarmId(@Param("userId") Long userId);
	@Query("SELECT count(*) FROM Dtu as d,AlarmRealtime as ar where d.id = ar.dtu.id and d.dtuUser.id=:userId and ar.alarmStatus=1 and ar.id>:alarmId")
	public Integer getNewAlarmRealtime(@Param("userId") Long userId,@Param("alarmId") Long alarmId);
	@Query("SELECT SUM (d.batteryModel.capacity*d.batteryModel.batteryNumber*d.batteryModel.batteryType.typeVoltage/1000) from Dtu as d where d.dtuUser.id = :userId")
	public Integer getTotalCapacity(@Param("userId") Long userId);
	@Query("SELECT SUM (d.batteryModel.capacity*d.batteryModel.batteryNumber*d.batteryModel.batteryType.typeVoltage/1000) from Dtu as d")
	public Integer getTotalCapacityAdmin();
}
