package com.ligootech.webdtu.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ligootech.webdtu.repository.DtuCountDao;
import org.springframework.stereotype.Component;

import com.ligootech.webdtu.dto.DayAlarmListDto;
import com.ligootech.webdtu.util.AlarmUtil;
@Component
public class DtuCountDaoImpl implements DtuCountDao {
	@PersistenceContext
	private EntityManager em;
	
//	@Override
//	public List getDtuList(int isAdmin, Long userId) {
//		Query query = em.createNativeQuery("SELECT d.id,d.uuid,d.sim_card,bm.factory_name,d.soc,d.alarm_status,d.online_status,vt.type_name,vm.model_name,d.total_capacity,d.battery_total_voltage,"+
//				"d.battery_total_amp,d.charge_status,d.battery_recharge_cycles,d.lontitude,d.latitude,d.insert_time "+
//				"from dtu as d,vehicle as v,battery_model as bm ,vehicle_type as vt,vehicle_model as vm "+
//				"where d.uuid = v.uuid and v.battery_model_id = bm.id and v.vehicle_type_id = vt.id and v.vehicle_model_id = vm.id and d.alarm_status = 0");
//		List result = query.getResultList();
//		return result;
//	}

	@Override
	public List getDtuList(String uuid, int vehicleTypeId,
			int vehicleModelId, String facName, int chargeStatus,
			int alarmStatus, int sort,int pageNumber,int pageSize, Long userId, int isAdmin,String city) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT d.id,d.uuid,d.sim_card,bm.factory_name,d.soc,d.alarm_status,d.online_status,vt.type_name,vm.model_name,d.total_capacity,d.battery_total_voltage,"+
						" d.battery_total_amp,d.charge_status,d.battery_recharge_cycles,d.lontitude," +
						"d.latitude,d.insert_time,d.dtu_user_id,v.vehicle_number,d.city,(unix_timestamp(NOW()) - unix_timestamp(insert_time)) as intervalTime," +
						"IFNULL((select max(t_sn.sn) from t_sn_info t_sn where t_sn.status=1 and length(t_sn.dtu_uuid)>1 and t_sn.dtu_uuid=d.uuid ),d.uuid) as sn "+
						" from dtu as d,vehicle as v,battery_model as bm ,vehicle_type as vt,vehicle_model as vm  "+
						" where d.uuid = v.uuid and d.battery_model_id = bm.id and v.vehicle_type_id = vt.id and v.vehicle_model_id = vm.id"
				);
		if(!uuid.trim().equals(""))
			sql.append(" and (d.uuid like '%"+uuid+"%'" + " or v.vehicle_number like '%"+uuid+"%')");
		if (vehicleTypeId != -1)
			sql.append(" and v.vehicle_type_id="+ vehicleTypeId);
		if (vehicleModelId != -1)
			sql.append(" and v.vehicle_model_id="+vehicleModelId);
		if (!facName.equals("-1"))
			sql.append(" and bm.factory_name='"+ facName+"'");
		
		if (chargeStatus != -1)
			sql.append(" and d.charge_status="+ chargeStatus);
		
		if (alarmStatus != -1)
			sql.append(" and d.alarm_status="+ alarmStatus);
		if (isAdmin !=1)
			// sql.append(" and d.dtu_user_id="+ userId); 修改为从配置表获取 wly 2015年9月17日16:24:22
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
		if (!city.equals("城市"))
			sql.append(" and d.city='"+ city+"'");	
		sql.append(" order by v.create_time asc,v.uuid asc ");
		sql.append(" limit " + (pageNumber - 1) * pageSize + "," + pageSize);
		Query query = em.createNativeQuery(sql.toString());

		return query.getResultList();
	}

	@Override
	public Integer getDtuListCount(String uuid, int vehicleTypeId,
			int vehicleModelId, String facName, int chargeStatus,
			int alarmStatus, int sort,Long userId, int isAdmin,String city) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT count(d.id) "+
						"from dtu as d,vehicle as v,battery_model as bm ,vehicle_type as vt,vehicle_model as vm "+
						"where d.uuid = v.uuid and d.battery_model_id = bm.id and v.vehicle_type_id = vt.id and v.vehicle_model_id = vm.id"
				);
		if(!uuid.trim().equals(""))
			sql.append(" and (d.uuid like '"+uuid+"'" + " or v.vehicle_number like '"+uuid+"')");
		if (vehicleTypeId != -1)
			sql.append(" and v.vehicle_type_id="+ vehicleTypeId);
		if (vehicleModelId != -1)
			sql.append(" and v.vehicle_model_id="+vehicleModelId);
		if (!facName.equals("-1"))
			sql.append(" and bm.factory_name='"+ facName+"'");
		
		if (chargeStatus != -1)
			sql.append(" and d.charge_status="+ chargeStatus);
		if (alarmStatus != -1)
			sql.append(" and d.alarm_status="+ alarmStatus);
		if (isAdmin != 1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and d.dtu_user_id="+ userId);
		if (!city.equals("城市"))
			sql.append(" and d.city='"+ city+"'");
		BigInteger query = (BigInteger)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else
			return query.intValue();
	}

	@Override
	public List getAlarmRealtime(String uuid, int isAdmin, Long userId,int pageNumber,int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT dtu.id,alarm_realtime.alarm_type,dtu.uuid,alarm_realtime.alarm_start_time ,battery_model.factory_name,dtu.online_status,dtu.lontitude,dtu.latitude,dtu.charge_status,vehicle.vehicle_number,dtu.city "+
		" FROM dtu,alarm_realtime,vehicle,battery_model where dtu.id = alarm_realtime.dtu_id and dtu.id = vehicle.dtu_id and dtu.battery_model_id = battery_model.id and alarm_realtime.alarm_status=1 "
				);
		if(!uuid.trim().equals(""))
			sql.append(" and (dtu.uuid='"+uuid+"'" + " or vehicle.vehicle_number='"+uuid+"')");
		if (isAdmin != 1)
			sql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and vehicle.dtu_user_id = "+ userId);
		sql.append(" order by alarm_realtime.alarm_start_time desc limit "+(pageNumber-1)*pageSize + ","+pageSize);
		Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public Integer getAlarmRealtimeCount(String uuid, int isAdmin,
			Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(dtu.uuid) "+
		"FROM dtu,alarm_realtime,vehicle,battery_model where dtu.id = alarm_realtime.dtu_id and dtu.id = vehicle.dtu_id and dtu.battery_model_id = battery_model.id and alarm_realtime.alarm_status=1 ");
		if(!uuid.trim().equals(""))
			sql.append(" and dtu.uuid='"+uuid+"'");
		if (isAdmin != 1)
			sql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and dtu.dtu_user_id="+ userId);
		BigInteger query = (BigInteger)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else
			return query.intValue();
	}

	@Override
	public Integer getNewVehicleDayCount(int isAdmin, Long userId,
			String createDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from vehicle as v where to_days(v.create_time) = to_days('"+ createDate+"')");
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
	public Integer getVehicleCount(int isAdmin, Long userId,String countDay) {
		StringBuffer sql = new StringBuffer();

		sql.append("select count(*) from vehicle v where TO_DAYS(create_time) <= TO_DAYS('"+countDay+"')");
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
	public float newVehicleRunningMilege(int isAdmin, Long userId,String countDate) {
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT SUM(rc.total_milege) from vehicle as v,running_cycle as rc where v.dtu_id = rc.dtu_id and to_days(rc.start_time) = to_days('"+countDate+"') ");
		if (isAdmin != 1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);*/
		sql.append("SELECT SUM(tlong) FROM " +
				"    (" +
				"    SELECT rc.dtu_id,SUM(rc.total_milege) AS tlong FROM running_cycle AS rc WHERE TO_DAYS(rc.end_time) = TO_DAYS('2015-03-20') GROUP BY rc.dtu_id " +
				"    ) tab_a ");
		if (isAdmin != 1)
			sql.append(" where dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");

		Double query = (Double)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else
			return query.floatValue();
	}

	@Override
	public float vehicleRunningMilegeAll(int isAdmin, Long userId,String countDate) {
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT SUM(rc.total_milege) from vehicle as v,running_cycle as rc where v.dtu_id = rc.dtu_id and TO_DAYS(rc.end_time) <= TO_DAYS('"+countDate+"')");
		if (isAdmin != 1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);*/

		sql.append("SELECT SUM(tlong) FROM " +
				"    (" +
				"    SELECT rc.dtu_id,SUM(rc.total_milege) AS tlong FROM running_cycle AS rc WHERE TO_DAYS(rc.end_time) <= TO_DAYS('2015-03-20') GROUP BY rc.dtu_id " +
				"    ) tab_a ");
		if (isAdmin != 1)
			sql.append(" where dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");

		Double query = (Double)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else
			return query.floatValue();
	}

	@Override
	public float newVehicleRunningTime(int isAdmin, Long userId,
			String countDate) {
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT sum(unix_timestamp(rc.end_time) - unix_timestamp(rc.start_time)) from vehicle as v,running_cycle as rc where v.dtu_id = rc.dtu_id and to_days(rc.start_time) = to_days('"+countDate+"') ");
		if (isAdmin != 1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);*/
		sql.append("SELECT SUM(tlong) FROM " +
				"    (" +
				"    SELECT rc.dtu_id,SUM(UNIX_TIMESTAMP(rc.end_time) - UNIX_TIMESTAMP(rc.start_time)) AS tlong FROM running_cycle AS rc WHERE TO_DAYS(rc.start_time) = TO_DAYS('" + countDate + "') GROUP BY rc.dtu_id " +
				"    ) tab_a ");
		if (isAdmin != 1)
			sql.append(" where dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");

		BigDecimal query = (BigDecimal)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else{
			float timeLength = query.floatValue()/3600;
			return timeLength;	
		}

	}

	@Override
	public float vehicleRunningTimeAll(int isAdmin, Long userId,String countDate) {
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT sum(unix_timestamp(rc.end_time) - unix_timestamp(rc.start_time)) from vehicle as v,running_cycle as rc where v.dtu_id = rc.dtu_id and TO_DAYS(rc.end_time) <= TO_DAYS('"+countDate+"')");
		if (isAdmin != 1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);*/
		sql.append("SELECT SUM(tlong) FROM " +
				"    (" +
				"    SELECT rc.dtu_id,SUM(UNIX_TIMESTAMP(rc.end_time) - UNIX_TIMESTAMP(rc.start_time)) AS tlong FROM running_cycle AS rc WHERE TO_DAYS(rc.end_time) <= TO_DAYS('" + countDate + "') GROUP BY rc.dtu_id " +
				"    ) tab_a ");
		if (isAdmin != 1)
			sql.append(" where dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");

		BigDecimal query = (BigDecimal)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else{
			float timeLength = query.floatValue()/3600;
			return timeLength;
		}
	}
//SELECT * from alarm_history as ah , dtu as d where (to_days(ah.alarm_start_time) = to_days('2014-08-06') or to_days(ah.alarm_end_time) = to_days('2014-08-06')) and d.id = ah.dtu_id
//SELECT * from alarm_realtime as ar ,dtu as d where ar.dtu_id = d.id	

	@Override
	public Integer alarmCountDay(int isAdmin, Long userId, String countDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) from alarm_history as ah , dtu as d where (to_days(ah.alarm_start_time) = to_days('"+countDate+"') or to_days(ah.alarm_end_time) = to_days('"+countDate+"')) and d.id = ah.dtu_id");
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
		sql.append("SELECT count(*) from alarm_realtime as ar ,dtu as d where ar.alarm_status=1 and ar.dtu_id = d.id and to_days(ar.alarm_start_time) = to_days('"+countDate+"')");
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
	public Integer alarmCountAll(int isAdmin, Long userId,String countDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) from alarm_history as ah , dtu as d where d.id = ah.dtu_id and TO_DAYS(ah.alarm_start_time) <= TO_DAYS('"+countDate+"')");
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
		sql.append("SELECT count(*) from alarm_realtime as ar ,dtu as d where ar.alarm_status=1 and ar.dtu_id = d.id  and TO_DAYS(ar.alarm_start_time) <= TO_DAYS('"+countDate+"')");
		if (isAdmin != 1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and d.dtu_user_id = "+ userId);
		BigInteger query2 = (BigInteger)em.createNativeQuery(sql.toString()).getSingleResult();
		int timeLength2=0;
		if (query2 == null)
			timeLength = 0;
		else
			timeLength2 = query2.intValue();
		
		return timeLength + timeLength2;
	}

	@Override
	public List<Object[]> getNewVehicleListToday(int isAdmin, Long userId,
			String createDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("select v.uuid,vt.type_name,v.vehicle_number,bm.factory_name,d.total_capacity,v.create_time,vm.model_name,bm.battery_type,bm.battery_number from vehicle as v, dtu as d,battery_model as bm,vehicle_type as vt,vehicle_model as vm"+
					" where v.dtu_id = d.id and d.battery_model_id = bm.id and v.vehicle_type_id = vt.id and v.vehicle_model_id=vm.id "+
					" and to_days(v.create_time) = to_days('"+ createDate+"')");

		if (isAdmin !=1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);
		Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public List<DayAlarmListDto> getAlarmListToday(int isAdmin, Long userId, String createDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("select v.uuid,vt.type_name,v.vehicle_number,bm.factory_name,ar.alarm_type,DATE_FORMAT(ar.alarm_start_time,'%Y-%m-%d %H:%i:%s'),d.lontitude,d.latitude,d.id from vehicle as v, dtu as d,battery_model as bm,vehicle_type as vt,alarm_realtime as ar"+
					" where v.dtu_id = d.id and d.battery_model_id = bm.id and v.vehicle_type_id = vt.id and ar.dtu_id = d.id and ar.alarm_status=1 and to_days(ar.alarm_start_time) <= to_days('"+createDate+"') ");//+
					//" and to_days(v.create_time) = to_days('"+ createDate+"')");

		if (isAdmin !=1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);
		sql.append(" order by ar.alarm_start_time desc");
		List<Object[]> query = (List<Object[]>)em.createNativeQuery(sql.toString()).getResultList();
		
		sql.setLength(0);
		sql.append("select v.uuid,vt.type_name,v.vehicle_number,bm.factory_name,ah.alarm_type,DATE_FORMAT(ah.alarm_start_time,'%Y-%m-%d %H:%i:%s'),DATE_FORMAT(ah.alarm_end_time,'%Y-%m-%d %H:%i:%s'),d.lontitude,d.latitude,d.id from vehicle as v, dtu as d,battery_model as bm,vehicle_type as vt,alarm_history as ah"+
				" where v.dtu_id = d.id and d.battery_model_id = bm.id and v.vehicle_type_id = vt.id and ah.dtu_id = d.id"+
				" and (to_days(ah.alarm_start_time) = to_days('"+ createDate+"')" + " or to_days(ah.alarm_end_time) = to_days('"+createDate+"'))");

		if (isAdmin !=1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);
		sql.append(" order by ah.alarm_start_time desc");
		List<Object[]> query2 = (List<Object[]>)em.createNativeQuery(sql.toString()).getResultList();
		
		List<DayAlarmListDto> results = new ArrayList<DayAlarmListDto>();
		for(int i=0;i<query.size();i++){
			Object[] item = query.get(i);
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			int alarmType = (Integer)item[4];
			dto.setAlarmType(alarmType);
			dto.setAlarmTypeName(AlarmUtil.getAlarmTypeName(alarmType));
			dto.setStartTime((String)item[5]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			results.add(dto);
		}
		for(int i=0;i<query2.size();i++){
			Object[] item = query2.get(i);
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType((Integer)item[4]);//解析alarmtype
			dto.setAlarmTypeName(AlarmUtil.getAlarmTypeName((Integer)item[4]));
			dto.setStartTime((String)item[5]);
			dto.setEndTime((String)item[6]);
			dto.setLontitude((float)item[7]);
			dto.setLatitude((float)item[8]);
			BigInteger dtuid = (BigInteger)item[9];
			dto.setDtuId(dtuid.longValue());
			results.add(dto);
		}
		
		return results;	
	}
/*
	private List<DayAlarmListDto> alarmParse(Object[] item){
		List<DayAlarmListDto> result = new ArrayList<DayAlarmListDto>();
		int alarmType = (Integer)item[4];
		String arr = (String)item[5];
		String[] alarmTimeArray = arr.split(",");
    	if ((alarmType&1) == 1){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("漏电");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(1);//解析alarmtype
			dto.setStartTime(alarmTimeArray[15]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&2) == 2){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("主从机通信失败");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(2);//解析alarmtype
			dto.setStartTime(alarmTimeArray[14]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&4) == 4){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("过温");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(3);//解析alarmtype
			dto.setStartTime(alarmTimeArray[13]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&8) == 8){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("过放");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(4);//解析alarmtype
			dto.setStartTime(alarmTimeArray[12]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&16) == 16){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("过充");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(5);//解析alarmtype
			dto.setStartTime(alarmTimeArray[11]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&32) == 32){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("SOC过低");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(6);//解析alarmtype
			dto.setStartTime(alarmTimeArray[10]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&64) == 64){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("SOC过高");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(7);//解析alarmtype
			dto.setStartTime(alarmTimeArray[9]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&128) == 128){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("过流");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(8);//解析alarmtype
			dto.setStartTime(alarmTimeArray[8]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&256) == 256){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("温差过大");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(9);//解析alarmtype
			dto.setStartTime(alarmTimeArray[7]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&512) == 512){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("压差过大");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(10);//解析alarmtype
			dto.setStartTime(alarmTimeArray[6]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&1024) == 1024){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("电压检测异常");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(11);//解析alarmtype
			dto.setStartTime(alarmTimeArray[5]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&2048) == 2048){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("温度检测异常");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(12);//解析alarmtype
			dto.setStartTime(alarmTimeArray[4]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&4096) == 4096){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("总压过高");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(13);//解析alarmtype
			dto.setStartTime(alarmTimeArray[3]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
    	if ((alarmType&8192) == 8192){
			DayAlarmListDto dto = new DayAlarmListDto();
			dto.setUuid((String)item[0]);
			dto.setAlarmTypeName("总压过低");
			dto.setTypeName((String)item[1]);
			dto.setVehicleNumber((String)item[2]);
			dto.setFactoryName((String)item[3]);
			dto.setAlarmType(14);//解析alarmtype
			dto.setStartTime(alarmTimeArray[2]);
			dto.setEndTime("--");
			dto.setLontitude((float)item[6]);
			dto.setLatitude((float)item[7]);
			BigInteger dtuid = (BigInteger)item[8];
			dto.setDtuId(dtuid.longValue());
			result.add(dto);
    	}
		return result;
	}
	
	public String getAlarmTypeName(int alarmId){
		if (alarmId == 1)
			return "漏电	";
		if (alarmId == 2)
			return "主从机通信失败";
		if (alarmId == 3)
			return "过温";
		if (alarmId == 4)
			return "过放";
		if (alarmId == 5)
			return "过充";
		if (alarmId == 6)
			return "SOC过低";
		if (alarmId == 7)
			return "SOC过高";
		if (alarmId == 8)
			return "过流";
		if (alarmId == 9)
			return "温差过大";
		if (alarmId == 10)
			return "压差过大";
		if (alarmId == 11)
			return "电压检测异常";
		if (alarmId == 12)
			return "温度检测异常";
		if (alarmId == 13)
			return "总压过高";
		if (alarmId == 14)
			return "总压过低";
		return "未知";
	}
	*/
	@Override
	public List<Object[]> getVehicleChargeStatus(int isAdmin, Long userId,
			String today) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cc.dtu_id,sum(unix_timestamp(cc.end_time) - unix_timestamp(cc.start_time)),v.vehicle_number,vt.type_name,bm.factory_name,d.lontitude,d.latitude  from charge_cycle as cc,vehicle as v,dtu as d,vehicle_type as vt,battery_model as bm"+
					" where cc.dtu_id = d.id and d.id = v.dtu_id and v.vehicle_type_id=vt.id and d.battery_model_id=bm.id and to_days(cc.end_time) = to_days('"+ today+"')");
		if (isAdmin != 1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);
		sql.append(" GROUP BY cc.dtu_id");
		Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public List<Object[]> getVehicleRunningStatus(int isAdmin, Long userId,
			String today) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT rc.dtu_id,sum(rc.total_milege),sum(unix_timestamp(rc.end_time) - unix_timestamp(rc.start_time)),v.vehicle_number,vt.type_name,bm.factory_name,d.lontitude,d.latitude from running_cycle as rc,vehicle as v,dtu as d,vehicle_type as vt,battery_model as bm"+
					" where rc.dtu_id = d.id and d.id = v.dtu_id and v.vehicle_type_id=vt.id and d.battery_model_id=bm.id and to_days(rc.end_time) = to_days('"+ today+"')");
		if (isAdmin !=1)
			sql.append(" and d.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);
		sql.append(" GROUP BY rc.dtu_id");
		Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public Integer onlineVehicleToday(int isAdmin, Long userId, String today) {
		/*StringBuffer sql = new StringBuffer();
		sql.append("SELECT rc.dtu_id as dtuid,count(rc.dtu_id) as vcount from running_cycle as rc,dtu as d,vehicle as v"+
				   " where rc.dtu_id = d.id and v.dtu_id = d.id and  to_days(rc.start_time) = TO_DAYS('"+today+"') OR TO_DAYS(rc.end_time) = TO_DAYS('"+today+"') ");
		if (isAdmin == 0)
			sql.append(" and v.dtu_user_id = "+ userId);
		sql.append(" GROUP BY rc.dtu_id");
		
		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT cc.dtu_id as dtuid,count(cc.dtu_id) as vcount from charge_cycle as cc,dtu as d,vehicle as v"+
				   " where cc.dtu_id = d.id and v.dtu_id = d.id and  to_days(cc.start_time) = TO_DAYS('"+today+"') OR TO_DAYS(cc.end_time) = TO_DAYS('"+today+"') ");
		if (isAdmin == 0)
			sql.append(" and v.dtu_user_id = "+ userId);
		sql2.append(" GROUP BY cc.dtu_id");

		String sqlUnion = "SELECT COUNT(DISTINCT uniontable.dtuid) from ("+sql+" union all "+sql2+") as uniontable";

		BigInteger query = (BigInteger)em.createNativeQuery(sqlUnion.toString()).getSingleResult();
		if (query == null)
			return 0;
		else
			return query.intValue();


		*/

		int rcNum = 0;
		int ccNum = 0;

		StringBuffer rcSql = null;
		if (isAdmin == 1){
			rcSql = new StringBuffer("SELECT COUNT(*) FROM (SELECT rc.dtu_id AS dtuid " +
					"  FROM " +
					"  running_cycle AS rc " +
					"  WHERE " +
					"  TO_DAYS(rc.start_time) = TO_DAYS('" + today + "') " +
					"  OR TO_DAYS(rc.end_time) = TO_DAYS('" + today + "') " +
					"  GROUP BY rc.dtu_id ) tab_a");
		}else{
			rcSql = new StringBuffer("  SELECT COUNT(DISTINCT tab_b.dtuid) FROM " +
					" dtu_user_config tab_b, " +
					"  (" +
					"  SELECT rc.dtu_id AS dtuid" +
					"  FROM" +
					"	running_cycle AS rc" +
					"  WHERE     " +
					"   TO_DAYS(rc.start_time) = TO_DAYS('" + today + "') " +
					"  OR TO_DAYS(rc.end_time) = TO_DAYS('" + today + "') " +
					"  GROUP BY rc.dtu_id " +
					"  ) tab_a" +
					" WHERE tab_a.dtuid=tab_b.dtuid AND tab_b.STATUS=1 AND tab_b.userid=" + userId);
		}

		BigInteger rcQuery = (BigInteger)em.createNativeQuery(rcSql.toString()).getSingleResult();
		if (rcQuery == null) {
			rcNum = 0;
		}else{
			rcNum = rcQuery.intValue();
		}

		StringBuffer ccSql = null;
		if (isAdmin == 1){
			ccSql = new StringBuffer("SELECT COUNT(*) FROM (SELECT " +
					" cc.dtu_id AS dtuid" +
					" FROM" +
					" charge_cycle AS cc" +
					"  WHERE" +
					"  TO_DAYS(cc.start_time) = TO_DAYS('" + today + "') " +
					"  OR TO_DAYS(cc.end_time) = TO_DAYS('" + today + "') " +
					"  GROUP BY cc.dtu_id ) tab_a");
		}else{
			ccSql = new StringBuffer("  SELECT COUNT(DISTINCT tab_b.dtuid) FROM " +
					" dtu_user_config tab_b, " +
					"  (" +
					"  SELECT cc.dtu_id AS dtuid" +
					"  FROM" +
					"  charge_cycle AS cc" +
					"  WHERE " +
					"  TO_DAYS(cc.start_time) = TO_DAYS('" + today + "') " +
					"  OR TO_DAYS(cc.end_time) = TO_DAYS('" + today + "') " +
					"  GROUP BY cc.dtu_id " +
					"  ) tab_a" +
					" WHERE tab_a.dtuid=tab_b.dtuid AND tab_b.STATUS=1 AND tab_b.userid=" + userId);
		}
		BigInteger ccQuery = (BigInteger)em.createNativeQuery(ccSql.toString()).getSingleResult();
		if (ccQuery == null) {
			ccNum = 0;
		}else{
			ccNum = ccQuery.intValue();
		}
		return rcNum+ccNum ;
	}

	@Override
	public float maxRunningTimeAll(int isAdmin, Long userId,String firstDay,String lastDay) {
		/*StringBuffer sql = new StringBuffer();
		sql.append("SELECT max(unix_timestamp(rc.end_time) - unix_timestamp(rc.start_time)) from vehicle as v,running_cycle as rc where v.dtu_id = rc.dtu_id");
		sql.append(" and TO_DAYS(rc.start_time) >= TO_DAYS('"+firstDay+"') and TO_DAYS(rc.end_time) <= TO_DAYS('"+lastDay+"')");///加入统计日期
		if (isAdmin != 1)
			sql.append(" and v.dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);*/
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" SELECT MAX(tlong) FROM ")
				.append("  ( ")
				.append("     SELECT MAX(UNIX_TIMESTAMP(rc.end_time) - UNIX_TIMESTAMP(rc.start_time)) AS tlong, dtu_id     ")
				.append("     FROM  ")
				.append("   running_cycle AS rc  ")
				.append(" WHERE TO_DAYS(rc.start_time) >= TO_DAYS('" + firstDay + "')  ")
				.append("   AND TO_DAYS(rc.end_time) <= TO_DAYS('" + lastDay + "')  ")
				.append("   GROUP BY dtu_id ")
				.append("   ) tab_a ");
		if (isAdmin != 1)
			sqlBuf.append(" where dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
		BigInteger query = (BigInteger)em.createNativeQuery(sqlBuf.toString()).getSingleResult();
		if (query == null)
			return 0;
		else{
			float timeLength = query.floatValue()/3600;
			return timeLength;
		}
	}

	@Override
	public float getMaxRunningTime(int isAdmin, Long userId) {
		StringBuffer sql = new StringBuffer();
		//sql.append("SELECT max(ss.sumtime) from (SELECT sum(cc.running_time_length) as sumtime FROM vehicle as v,charge_cycle as cc where cc.dtu_id=v.dtu_id");///加入统计日期
		sql.append("SELECT MAX(ss.sumtime) FROM " +
				"  (" +
				"  SELECT SUM(cc.running_time_length) AS sumtime ,cc.dtu_id " +
				"  FROM charge_cycle AS cc  GROUP BY cc.dtu_id " +
				"  ) ss ");
		if (isAdmin != 1)
			sql.append(" where dtu_id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " AND STATUS=1)");
			//sql.append(" and v.dtu_user_id = "+ userId);
		//sql.append("  GROUP BY cc.dtu_id) as ss");
		BigDecimal query = (BigDecimal)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else{
			float timeLength = query.floatValue()/3600;
			return timeLength;
		}
	}
}
