package com.ligootech.webdtu.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ligootech.webdtu.repository.MonthCountDao;
import org.springframework.stereotype.Component;

import com.ligootech.webdtu.dto.MonthBatteryAlarmDto;

@Component
public class MonthCountDaoImpl implements MonthCountDao {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Integer getNewVehicleMonthCount(int isAdmin, Long userId,
			String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from vehicle as v where to_days(v.create_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(v.create_time) <= TO_DAYS('"+endDate+"')");
		if (isAdmin != 1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and dtu_user_id = "+ userId);
		BigInteger query = (BigInteger)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else
			return query.intValue();
	}
	
	@Override
	public Integer newVehicleRunningMilege(int isAdmin, Long userId,String startDate,String endDate) {
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT SUM(rc.total_milege) from vehicle as v,running_cycle as rc where v.dtu_id = rc.dtu_id and to_days(v.create_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(v.create_time) <= TO_DAYS('"+endDate+"')");
		if (isAdmin != 1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);*/
		sql.append("SELECT SUM(tlong) FROM " +
				"    (" +
				"    SELECT rc.dtu_id,SUM(total_milege) AS tlong FROM running_cycle AS rc WHERE TO_DAYS(rc.end_time) <= TO_DAYS('" + endDate + "') and TO_DAYS(rc.start_time) >= TO_DAYS('" + startDate + "') GROUP BY rc.dtu_id " +
				"    ) tab_a ");
		if (isAdmin != 1)
			sql.append(" where dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1) ");

		Double query = (Double)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else
			return query.intValue();
	}
	
	@Override
	public float newVehicleRunningTime(int isAdmin, Long userId,
			String startDate,String endDate) {
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT sum(unix_timestamp(rc.end_time) - unix_timestamp(rc.start_time)) from vehicle as v,running_cycle as rc where v.dtu_id = rc.dtu_id and to_days(v.create_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(v.create_time) <= TO_DAYS('"+endDate+"')");
		sql.append(" and TO_DAYS(rc.end_time) <= TO_DAYS('"+endDate+"')");
		if (isAdmin == 0)
			sql.append(" and v.dtu_user_id = "+ userId);*/

		sql.append("SELECT SUM(tlong) FROM " +
				"    (" +
				"    SELECT rc.dtu_id,SUM(UNIX_TIMESTAMP(rc.end_time) - UNIX_TIMESTAMP(rc.start_time)) AS tlong FROM running_cycle AS rc WHERE TO_DAYS(rc.end_time) <= TO_DAYS('" + endDate + "') and TO_DAYS(rc.start_time) >= TO_DAYS('" + startDate + "') GROUP BY rc.dtu_id " +
				"    ) tab_a ");
		if (isAdmin != 1)
			sql.append(" where dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1) ");

		BigDecimal query = (BigDecimal)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else{
			float timeLength = query.floatValue()/3600;
			return timeLength;	
		}
	}
	
	@Override
	public Integer alarmCountMonth(int isAdmin, Long userId, String startDate,String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) from alarm_history as ah , dtu as d where ((to_days(ah.alarm_start_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(ah.alarm_start_time) <= TO_DAYS('"+endDate+"')) or (to_days(ah.alarm_end_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(ah.alarm_end_time) <= TO_DAYS('"+endDate+"'))) and d.id = ah.dtu_id");
		if (isAdmin != 1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and d.dtu_user_id = "+ userId);
		BigInteger query = (BigInteger)em.createNativeQuery(sql.toString()).getSingleResult();
		int timeLength;
		if (query == null)
			timeLength = 0;
		else
			timeLength = query.intValue();
		
		sql.setLength(0);
		sql.append("SELECT count(*) from alarm_realtime as ar ,dtu as d where ar.alarm_status=1 and ar.dtu_id = d.id and ((to_days(ar.alarm_start_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(ar.alarm_start_time) <= TO_DAYS('"+endDate+"')))");//今日实时报警应该不在日报中，因为实时报警为当日，而日报查询的报警为往日的日报。
		//sql.append(" and alarm_realtime.alarm_start_time <= '"+endDate+"' and alarm_realtime.alarm_start_time>='"+startDate+"')");
		if (isAdmin != 1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and d.dtu_user_id = "+ userId);
		BigInteger query2 = (BigInteger)em.createNativeQuery(sql.toString()).getSingleResult();
		int timeLength2;
		if (query2 == null)
			timeLength2 = 0;
		else
			timeLength2 = query2.intValue();	
		return timeLength + timeLength2;
	}

	@Override
	public List<Object[]> vehicleTypeMonthCount(int isAdmin, Long userId,
			String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT vt.type_name,count(vt.id) from vehicle as v,vehicle_type as vt"+
					" where v.vehicle_type_id = vt.id AND to_days(v.create_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(v.create_time) <= TO_DAYS('"+endDate+"')");
		if (isAdmin !=1 )
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);
		sql.append(" GROUP BY v.vehicle_type_id");
		Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public List<Object[]> batteryModelMonthCount(int isAdmin, Long userId,
			String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT bm.factory_name,count(v.id) from vehicle as v,dtu as d,battery_model as bm "+
					" where v.dtu_id = d.id and d.battery_model_id = bm.id AND to_days(v.create_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(v.create_time) <= TO_DAYS('"+endDate+"')");
		if (isAdmin !=1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);
		sql.append(" GROUP BY d.battery_model_id");
		Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public List<MonthBatteryAlarmDto> batteryAlarmCount(int isAdmin, Long userId, String startDate,
			String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("select bm.factory_name,ar.alarm_type from dtu as d,battery_model as bm,alarm_realtime as ar"+
					" where  d.battery_model_id = bm.id and ar.dtu_id = d.id and ar.alarm_status=1 " +
					" and to_days(ar.alarm_start_time) >= TO_DAYS('"+startDate +"') and TO_DAYS(ar.alarm_start_time) <= TO_DAYS('"+ endDate + "')");

		if (isAdmin !=1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and d.dtu_user_id = "+ userId);
		List<Object[]> query = (List<Object[]>)em.createNativeQuery(sql.toString()).getResultList();
		
		sql.setLength(0);
		sql.append("select bm.factory_name,ah.alarm_type from dtu as d,battery_model as bm,alarm_history as ah"+
				" where d.battery_model_id = bm.id and ah.dtu_id = d.id"+ 
				" and ((TO_DAYS(ah.alarm_start_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(ah.alarm_start_time) <= TO_DAYS('"+endDate+"')) OR " +
				" (TO_DAYS(ah.alarm_end_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(ah.alarm_end_time) <= TO_DAYS('"+endDate+"')))"
				);

		if (isAdmin !=1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and d.dtu_user_id = "+ userId);
		List<Object[]> query2 = (List<Object[]>)em.createNativeQuery(sql.toString()).getResultList();
		Map<String,MonthBatteryAlarmDto> map = new HashMap<String, MonthBatteryAlarmDto>();
		for(int i=0;i<query.size();i++){
			Object[] item = query.get(i);
			
			MonthBatteryAlarmDto dto = map.get((String)item[0]);
			if (dto == null){
				dto = new MonthBatteryAlarmDto();
				dto.setBatteryName((String)item[0]);
				map.put((String)item[0], dto);
			}
			alarmParse2(dto, (Integer)item[1]);
		}
		for(int i=0;i<query2.size();i++){
			Object[] item = query2.get(i);
			MonthBatteryAlarmDto dto = map.get((String)item[0]);
			if (dto == null){
				dto = new MonthBatteryAlarmDto();
				dto.setBatteryName((String)item[0]);
				map.put((String)item[0], dto);
			}
			alarmParse2(dto, (Integer)item[1]);
		}
		List<MonthBatteryAlarmDto> results = new ArrayList(map.values());
		return results;
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
	/*
	private MonthBatteryAlarmDto alarmParse(Object[] item,MonthBatteryAlarmDto dto,String startDate,String endDate){
		int result = 0;
		int alarmType = (Integer)item[1];
		String arr = (String)item[2];
		String[] alarmTimeArray = arr.split(",");
    	if ((alarmType&1) == 1){
    		if (ifInMonth(alarmTimeArray[15], startDate, endDate))
    			dto.setLeakElec(dto.getLeakElec() + 1);;
    	}
    	if ((alarmType&2) == 2){
    		if (ifInMonth(alarmTimeArray[14], startDate, endDate))
    			dto.setCommuniStatus(dto.getCommuniStatus() + 1);
    	}
    	if ((alarmType&4) == 4){
    		if (ifInMonth(alarmTimeArray[13], startDate, endDate))
    			dto.setOutTemper(dto.getOutTemper() + 1);
    	}
    	if ((alarmType&8) == 8){
    		if (ifInMonth(alarmTimeArray[12], startDate, endDate))
    			dto.setOutRealease(dto.getOutRealease()+ 1);
    	}
    	if ((alarmType&16) == 16){
    		if (ifInMonth(alarmTimeArray[11], startDate, endDate))
    			dto.setOutCharge(dto.getOutCharge()+ 1);
    	}
    	if ((alarmType&32) == 32){
    		if (ifInMonth(alarmTimeArray[10], startDate, endDate))
    			dto.setSocLow(dto.getSocLow()+ 1);
    	}
    	if ((alarmType&64) == 64){
    		if (ifInMonth(alarmTimeArray[9], startDate, endDate))
    			dto.setSocHigh(dto.getSocHigh()+ 1);
    	}
    	if ((alarmType&128) == 128){
    		if (ifInMonth(alarmTimeArray[8], startDate, endDate))
    			dto.setOutStream(dto.getOutStream()+ 1);
    	}
    	if ((alarmType&256) == 256){
    		if (ifInMonth(alarmTimeArray[7], startDate, endDate))
    			dto.setTemperDiffMax(dto.getTemperDiffMax()+ 1);
    	}
    	if ((alarmType&512) == 512){
    		if (ifInMonth(alarmTimeArray[6], startDate, endDate))
    			dto.setVoltageDiffMax(dto.getVoltageDiffMax()+ 1);
    	}
    	if ((alarmType&1024) == 1024){
    		if (ifInMonth(alarmTimeArray[5], startDate, endDate))
    			dto.setVoltageCheckExeption(dto.getVoltageCheckExeption()+ 1);
    	}
    	if ((alarmType&2048) == 2048){
    		if (ifInMonth(alarmTimeArray[4], startDate, endDate))
    			dto.setTemperCheckExeption(dto.getTemperCheckExeption()+ 1);
    	}
    	if ((alarmType&4096) == 4096){
    		if (ifInMonth(alarmTimeArray[3], startDate, endDate))
    			dto.setTotalVoltageHigh(dto.getTotalVoltageHigh()+ 1);
    	}
    	if ((alarmType&8192) == 8192){
    		if (ifInMonth(alarmTimeArray[3], startDate, endDate))
    			dto.setTotalVoltageLow(dto.getTotalVoltageLow()+ 1);
    	}
		return dto;
	}
	
	private boolean ifInMonth(String date,String startDate,String endDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date vdate = sdf.parse(date);
			Date vstartDate = sdf.parse(startDate);
			Date vendDate = sdf.parse(endDate);
			if (vdate.after(vstartDate)&&vdate.before(vendDate))
				return true;
		} catch (ParseException e) {

		}
		
		return false;
	}*/

	@Override
	public List<Object[]> vehicleRunningMonthCount(int isAdmin, Long userId,
			String startDate, String endDate) {
		/*StringBuffer subSql = new StringBuffer();
		subSql.append("(SELECT charge_cycle.dtu_id, count(charge_cycle.id) as vcount from charge_cycle ,dtu "+
					" where charge_cycle.dtu_id = dtu.id and TO_DAYS(end_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(end_time) <= TO_DAYS('"+endDate+"')");
		if (isAdmin !=1)
			subSql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//subSql.append(" and dtu.dtu_user_id = "+ userId);
		subSql.append(" GROUP BY charge_cycle.dtu_id) as cc");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT rc.dtu_id,sum(rc.total_milege),sum(unix_timestamp(rc.end_time) - unix_timestamp(rc.start_time)),v.vehicle_number,vt.type_name,bm.factory_name,d.lontitude,d.latitude ,count(rc.dtu_id),cc.vcount "+
					" from running_cycle as rc,vehicle as v,dtu as d,vehicle_type as vt,battery_model as bm, "+
					subSql.toString()+
					" where rc.dtu_id = d.id and d.id = v.dtu_id and v.vehicle_type_id=vt.id and d.battery_model_id=bm.id and cc.dtu_id = rc.dtu_id and TO_DAYS(end_time) >= TO_DAYS('"+startDate+"') and TO_DAYS(end_time) <= TO_DAYS('"+endDate+"')");
		if (isAdmin !=1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and d.dtu_user_id = "+ userId);
		sql.append(" GROUP BY rc.dtu_id");*/

		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" SELECT  ")
				.append("   rc.dtu_id, ")
				.append("   rc.total_milege, ")
				.append("   rc.tlong, ")
				.append("   v.vehicle_number, ")
				.append("   vt.type_name, ")
				.append("   bm.factory_name, ")
				.append("   d.lontitude, ")
				.append("   d.latitude, ")
				.append("   rc.dtu_count, ")
				.append("   cc.vcount  ")
				.append(" FROM   ")
				.append("   vehicle AS v, ")
				.append("   dtu AS d, ")
				.append("   vehicle_type AS vt, ")
				.append("   battery_model AS bm, ")
				.append("   (SELECT  ")
				.append("     charge_cycle.dtu_id, ")
				.append("     COUNT(charge_cycle.id) AS vcount  ")
				.append("   FROM ")
				.append("     charge_cycle ")
				.append("   WHERE TO_DAYS(end_time) >= TO_DAYS('" + startDate + "')  ")
				.append("     AND TO_DAYS(end_time) <= TO_DAYS('2015-05-31')      ")
				.append("   GROUP BY charge_cycle.dtu_id) AS cc , ")
				.append("   ( SELECT dtu_id, ")
				.append(" 	SUM(total_milege) AS total_milege, ")
				.append(" 	SUM( ")
				.append(" 	UNIX_TIMESTAMP(end_time) - UNIX_TIMESTAMP(start_time) ")
				.append(" 	) AS tlong,COUNT(dtu_id) AS dtu_count FROM running_cycle   ")
				.append(" 	WHERE TO_DAYS(end_time) >= TO_DAYS('" + startDate + "')  ")
				.append(" 	AND TO_DAYS(end_time) <= TO_DAYS('" + endDate + "')  ")
				.append(" 	GROUP BY dtu_id 	 ")
				.append(" 	) AS rc ")
				.append(" WHERE rc.dtu_id = d.id  ")
				.append("   AND d.id = v.dtu_id  ")
				.append("   AND v.vehicle_type_id = vt.id  ")
				.append("   AND d.battery_model_id = bm.id  ")
				.append("   AND cc.dtu_id = rc.dtu_id  ");

		if (isAdmin != -1){
			sqlBuf.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
		}

		Query query = em.createNativeQuery(sqlBuf.toString());
		return query.getResultList();
	}

	@Override
	public List<String> alarmCountMonthByType(int isAdmin, Long userId,
											  String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT at.type_name,count(ar.id) from (");
		sql.append("SELECT alarm_realtime.* from alarm_realtime,dtu where alarm_realtime.alarm_status=1 and alarm_realtime.dtu_id = dtu.id");
		if (isAdmin != 1)
			sql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and dtu.dtu_user_id ="+userId);
		sql.append(" and alarm_realtime.alarm_start_time <= '"+endDate+"' and alarm_realtime.alarm_start_time>='"+startDate+"')");
		sql.append(" as ar RIGHT JOIN alarm_type as at on ar.alarm_type = at.id");
		sql.append(" GROUP BY at.type_name ORDER BY `at`.id");
		//SELECT at.type_name,at.id,count(ar.id) from alarm_realtime as ar RIGHT JOIN alarm_type as at on ar.alarm_type = at.id GROUP BY at.type_name ORDER BY `at`.id
		//SELECT at.type_name,at.id,count(ah.id) from alarm_history as ah RIGHT JOIN alarm_type as at on ah.alarm_type = at.id GROUP BY at.type_name ORDER BY `at`.id
		Query query = em.createNativeQuery(sql.toString());
		List<Object[]> result1 = query.getResultList();
		
		sql.setLength(0);
		sql.append("SELECT at.type_name,count(ar.id) from (");
		sql.append("SELECT alarm_history.* from alarm_history,dtu where alarm_history.dtu_id = dtu.id");
		if (isAdmin != 1)
			sql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and dtu.dtu_user_id ="+userId);
		sql.append(" and ((TO_DAYS(alarm_history.alarm_start_time) <= TO_DAYS('"+endDate+"') and TO_DAYS(alarm_history.alarm_start_time)>=TO_DAYS('"+startDate+"'))");
		sql.append(" or (TO_DAYS(alarm_history.alarm_end_time) <= TO_DAYS('"+endDate+"') and TO_DAYS(alarm_history.alarm_end_time)>=TO_DAYS('"+startDate+"'))))");
		sql.append(" as ar RIGHT JOIN alarm_type as at on ar.alarm_type = at.id");
		sql.append(" GROUP BY at.type_name ORDER BY `at`.id");
		Query query2 = em.createNativeQuery(sql.toString());
		List<Object[]> result2 = query2.getResultList();		
		
		List<String> result = new ArrayList<String>();
		
		StringBuffer sbType = new StringBuffer();
		sbType.append("[");
		StringBuffer sbRealtime = new StringBuffer();
		sbRealtime.append("[");
		
		for (int i=0;i<result1.size();i++){
			Object[] item = result1.get(i);
			sbType.append("'"+item[0]+"'");
			sbRealtime.append(item[1]);
			if (i != (result1.size()-1)){
				sbType.append(",");
				sbRealtime.append(",");
			}
				
		}
		sbType.append("]");
		sbRealtime.append("]");
		
		StringBuffer sbHistory = new StringBuffer();
		sbHistory.append("[");	
		for (int i=0;i<result2.size();i++){
			Object[] item = result2.get(i);
			sbHistory.append(item[1]);
			if (i != (result2.size()-1)){
				sbHistory.append(",");
			}
		}
		sbHistory.append("]");
		result.add(sbType.toString());
		result.add(sbRealtime.toString());
		result.add(sbHistory.toString());
		return result;
	}
	
}
