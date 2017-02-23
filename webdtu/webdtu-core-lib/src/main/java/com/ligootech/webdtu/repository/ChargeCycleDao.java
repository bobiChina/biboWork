package com.ligootech.webdtu.repository;

import java.util.List;

import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ligootech.webdtu.entity.core.ChargeCycle;

public interface ChargeCycleDao extends BaseJpaRepository<ChargeCycle, Long>, JpaSpecificationExecutor<ChargeCycle>{
	@Query("select sum(cc.runningMilege),sum(cc.runningTimeLength),max(cc.totalMilege),max(cc.runningTimeLength) from ChargeCycle as cc where cc.dtu.dtuUser.id = :userId")
	public List<Object[]> getAllMilegeAndTimeLength(@Param("userId") Long userId);
	
	@Query("select sum(cc.runningMilege),sum(cc.runningTimeLength),max(cc.totalMilege),max(cc.runningTimeLength) from ChargeCycle as cc")
	public List<Object[]> getAllMilegeAndTimeLengthAdmin();	
	
	//SELECT max(ss.sumtime) from (SELECT sum(cc.running_time_length) as sumtime FROM charge_cycle as cc GROUP BY cc.dtu_id) as ss
	
//	@Query("from ChargeCycle as cc where cc.id = :userId")
//	public List getAllMilegeAndTimeLength(@Param("userId") Long userId);
	
//	@Query("from ChargeCycle as cc")
//	public List<ChargeCycle> getAllMilegeAndTimeLengthAdmin();		
}
