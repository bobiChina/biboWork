package com.ligootech.webdtu.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ligootech.webdtu.repository.VehicleCountDao;
import org.springframework.stereotype.Component;

import com.ligootech.webdtu.dto.MonthBatteryAlarmDto;

@Component
public class VehicleCountDaoImpl implements VehicleCountDao {
	@PersistenceContext
	private EntityManager em;

	@Override
	public float totalRunningMilege(Long dtuId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT total_milege from dtu where id = "+ dtuId);

		Double query = Double.valueOf(em.createNativeQuery(sql.toString()).getSingleResult().toString());
		if (query == null)
			return 0;
		else{
			float timeLength = query.floatValue();
			return timeLength;	
		}
	}

	@Override
	public float totalRunningTime(Long dtuId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sum(unix_timestamp(rc.end_time) - unix_timestamp(rc.start_time)) from running_cycle as rc where rc.dtu_id="+ dtuId );

		BigDecimal query = (BigDecimal)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else{
			float timeLength = query.floatValue()/3600;
			return timeLength;	
		}
	}

	@Override
	public int totalAlarmCount(Long dtuId) {
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) from alarm_realtime where alarm_status=1 and dtu_id = "+ dtuId );
		Query query = em.createNativeQuery(sql.toString());
		BigInteger ar = (BigInteger)query.getSingleResult();
		if (ar != null)
			result = result + ar.intValue();
		sql.setLength(0);
		sql.append("SELECT count(*) from alarm_history where dtu_id = "+ dtuId);
		BigInteger query2 = (BigInteger)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query2 != null){
			result += query2.intValue();
		}		
		return result;
	}
	
	private int alarmCountParse(int alarmType){
		int result = 0;
	  	if ((alarmType&1) == 1){
    		result += 1;
    	}
    	if ((alarmType&2) == 2){
    		result += 1;
    	}
    	if ((alarmType&4) == 4){
    		result += 1;
    	}
    	if ((alarmType&8) == 8){
    		result += 1;
    	}
    	if ((alarmType&16) == 16){
    		result += 1;
    	}
    	if ((alarmType&32) == 32){
    		result += 1;
    	}
    	if ((alarmType&64) == 64){
    		result += 1;
    	}
    	if ((alarmType&128) == 128){
    		result += 1;
    	}
    	if ((alarmType&256) == 256){
    		result += 1;
    	}
    	if ((alarmType&512) == 512){
    		result += 1;
    	}
    	if ((alarmType&1024) == 1024){
    		result += 1;
    	}
    	if ((alarmType&2048) == 2048){
    		result += 1;
    	}
    	if ((alarmType&4096) == 4096){
    		result += 1;
    	}
    	if ((alarmType&8192) == 8192){
    		result += 1;
    	}
    	return result;
		
	}

	@Override
	public String onlineStatus(Long dtuId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select (SELECT sum(unix_timestamp(end_time) - unix_timestamp(start_time)) from running_cycle where dtu_id ="+dtuId+") as c1,(SELECT sum(unix_timestamp(end_time) - unix_timestamp(start_time)) from charge_cycle where dtu_id = "+dtuId+") as c2,create_time from vehicle where dtu_id = "+ dtuId );

		List<Object[]> query = (List<Object[]>)em.createNativeQuery(sql.toString()).getResultList();
		if (query.size() != 0){
			BigDecimal runningTime = (BigDecimal)query.get(0)[0];
			BigDecimal chargeTime = (BigDecimal)query.get(0)[1];
			Timestamp createTime = (Timestamp)query.get(0)[2];
			long createTime1 = (new Date()).getTime() - createTime.getTime();
			float createTime2 = createTime1/(1000*60*60);
			float rtime;
			if (runningTime == null)
				rtime=0;
			else
			    rtime = runningTime.floatValue()/3600;
			float ctime;
			if (chargeTime == null)
				ctime=0;
			else
				ctime = chargeTime.floatValue()/3600;
			
			DecimalFormat df = new DecimalFormat();
			df.applyPattern("0.0");
			
			StringBuffer sb = new StringBuffer();
			sb.append("[{value:");
			sb.append(new BigDecimal(rtime).setScale(1, BigDecimal.ROUND_FLOOR));
			sb.append(",name:'运行'},{value:");
			sb.append(new BigDecimal(ctime).setScale(1, BigDecimal.ROUND_FLOOR));
			sb.append(",name:'充电'},{value:");
			sb.append(new BigDecimal((createTime2-rtime-ctime)).setScale(1, BigDecimal.ROUND_FLOOR));
			sb.append(",name:'离线'}]");
			return sb.toString() ;
		}

		return "";
	}

	@Override
	public List totalCapacityCurve(Long dtuId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT total_capacity FROM charge_cycle WHERE dtu_id = "+ dtuId );
		Query query = em.createNativeQuery(sql.toString());
		List ar = (List<Object[]>)query.getResultList();
		return ar;
	}

	@Override
	public List getAlarmHistory(Long dtuId) {
		StringBuffer sql = new StringBuffer();
		//添加实时报警记录 wly 2015年9月11日13:30:33
		//sql.append("SELECT alarm_type,alarm_start_time,alarm_end_time,unix_timestamp(alarm_end_time) - unix_timestamp(alarm_start_time) FROM alarm_history where dtu_id = "+ dtuId + " order by alarm_start_time desc" );
		//修改 alarm_status=1 1--发生报警 0-修复报警 wly 2016年1月30日09:35:16
		sql.append("SELECT alarm_type,alarm_start_time,alarm_end_time,UNIX_TIMESTAMP(alarm_end_time) - UNIX_TIMESTAMP(alarm_start_time) AS diff, 'old' AS bs FROM alarm_history WHERE dtu_id = " + dtuId);
		sql.append(" UNION ");
		sql.append("SELECT alarm_type,alarm_start_time,SYSDATE() AS alarm_end_time,UNIX_TIMESTAMP(SYSDATE()) - UNIX_TIMESTAMP(alarm_start_time) AS diff, 'new' AS bs FROM alarm_realtime WHERE alarm_start_time>0 AND alarm_status=1 AND dtu_id = " + dtuId);
		sql.append(" ORDER BY alarm_start_time DESC,bs ASC");
		Query query = em.createNativeQuery(sql.toString());
		List<Object[]> ar = (List<Object[]>)query.getResultList();
		return ar;
	}

	@Override
	public List vehicleChargeCycleList(Long dtuId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT start_time,end_time,unix_timestamp(end_time) - unix_timestamp(start_time),start_soc,end_soc,end_soc-start_soc,running_milege,running_time_length,total_milege from charge_cycle where dtu_id ="+ dtuId +" order by start_time desc");
		Query query = em.createNativeQuery(sql.toString());
		List<Object[]> ar = (List<Object[]>)query.getResultList();
		return ar;
	}
	
	@Override
	public List batteryAlarmCount(Long dtuId) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select aresult.tname, SUM(aresult.acount) FROM");
		sql.append(" (select count(ar.id) as acount,at.type_name as tname from alarm_type as at,alarm_realtime as ar where ar.alarm_type = at.id and ar.alarm_status=1 and ar.dtu_id = "+dtuId+"  group by ar.alarm_type");
		sql.append(" UNION ALL");
		sql.append(" select count(ah.id) as acount,at.type_name  as tname from alarm_type as at,alarm_history as ah where ah.alarm_type = at.id  and ah.dtu_id = "+dtuId+"  group by ah.alarm_type) as aresult");
		sql.append(" GROUP BY aresult.tname");
		Query query = em.createNativeQuery(sql.toString());
		List<Object[]> ar = (List<Object[]>)query.getResultList();
		return ar;
	}	
	private void alarmParse2(MonthBatteryAlarmDto dto,int alarmType){
		if (alarmType == 1)
			dto.setLeakElec(dto.getLeakElec() + 1);
		if (alarmType == 2)
			dto.setCommuniStatus(dto.getCommuniStatus() + 1);
		if (alarmType == 3)
			dto.setOutTemper(dto.getOutTemper() + 1);
		if (alarmType == 4)
			dto.setOutRealease(dto.getOutRealease()+ 1);
		if (alarmType == 5)
			dto.setOutCharge(dto.getOutCharge()+ 1);
		
		if (alarmType == 6)
			dto.setSocLow(dto.getSocLow()+ 1);
		if (alarmType == 7)
			dto.setSocHigh(dto.getSocHigh()+ 1);
		if (alarmType == 8)
			dto.setOutStream(dto.getOutStream()+ 1);
		if (alarmType == 9)
			dto.setTemperDiffMax(dto.getTemperDiffMax()+ 1);
		if (alarmType == 10)
			dto.setVoltageDiffMax(dto.getVoltageDiffMax()+ 1);
		if (alarmType == 11)
			dto.setVoltageCheckExeption(dto.getVoltageCheckExeption()+ 1);
		if (alarmType == 12)
			dto.setTemperCheckExeption(dto.getTemperCheckExeption()+ 1);
		if (alarmType == 13)
			dto.setTotalVoltageHigh(dto.getTotalVoltageHigh()+ 1);
		if (alarmType == 14)
			dto.setTotalVoltageLow(dto.getTotalVoltageLow()+ 1);
		
	}

	@Override
	public List overviewQuot(int isAdmin, Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ifnull(vm.milege,0) from vehicle_type as vt left JOIN (");
		sql.append("SELECT v.vehicle_type_id as vtid,SUM(cc.running_milege) as milege from charge_cycle as cc,dtu as d,vehicle as v ");
		sql.append(" where cc.dtu_id = d.id and d.id = v.dtu_id");
		if (isAdmin != 1)
			sql.append(" and d.dtu_user_id =" + userId);
		sql.append(" GROUP BY v.vehicle_type_id");
		sql.append(") as vm ON vt.id = vm.vtid ORDER BY vt.id");
		
		Query query = em.createNativeQuery(sql.toString());
		List<Object[]> ar = (List<Object[]>)query.getResultList();
		return ar;
	}
}
