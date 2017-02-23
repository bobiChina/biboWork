package com.ligootech.webdtu.service.impl.dtu;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.repository.DtuUserDao;
import com.ligootech.webdtu.util.EasyUiUtil;
import com.ligootech.webdtu.util.StringUtil;
import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.ligootech.webdtu.entity.core.Dtu;
import com.ligootech.webdtu.repository.DtuCountDao;
import com.ligootech.webdtu.repository.DtuDao;
import com.ligootech.webdtu.service.dtu.DtuManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SuppressWarnings("ALL")
@Service("dtuManager")
public class DtuManagerImpl extends GenericManagerImpl<Dtu, Long>  implements DtuManager {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DtuDao dtuDao;

	@Autowired
	private DtuUserDao dtuUserDao;
	
	@Autowired
	private DtuCountDao dtuCountDao;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDtuDao(DtuDao dtuDao) {
		super.dao = dtuDao;
		this.dtuDao = dtuDao;
	}

	@Override
	public List<Dtu> getDtuMaps() {
		return dtuDao.getDtuMaps();
	}

	@Override
	public List<Dtu> getDtuLatLon() {
		return dtuDao.getDtuLatLon();
	}

	@Override
	public List<Object[]> getDtuLocations(int isAdmin,Long userId){
		//修改地图显示车辆信息 2016年1月14日14:03:57 从配置表获取

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT d.id,d.uuid,bm.factory_name,d.soc,d.online_status,d.lontitude,d.latitude,v.vehicle_number,d.charge_status,d.alarm_status,d.sim_card,vt.type_name ");
		sql.append(" FROM dtu AS d,battery_model AS bm,vehicle AS v,vehicle_type AS vt ");
		sql.append(" WHERE d.id=v.dtu_id AND d.battery_model_id = bm.id AND v.vehicle_type_id = vt.id AND d.lontitude!=0 AND d.latitude!=0");

		if (isAdmin == 1){
			//return dtuDao.getDtuLocationsAdmin();
		}
		else{
			sql.append(" AND d.id in (SELECT DISTINCT dtuid FROM dtu_user_config WHERE STATUS=1 AND userid=" + userId + " )" );
		}
		List<Object[]> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("id"),
						resultSet.getString("uuid"),
						resultSet.getString("factory_name"),
						resultSet.getString("soc"),
						resultSet.getString("online_status"),
						resultSet.getString("lontitude"),
						resultSet.getString("latitude"),
						resultSet.getString("vehicle_number"),
						resultSet.getString("charge_status"),
						resultSet.getString("alarm_status"),
						resultSet.getString("sim_card"),
						resultSet.getString("type_name")
				};
				return rs;
			}
		});
		return list;
			//return dtuDao.getDtuLocations(userId);
	}

	@Override
	public List getPageDtu(int pageNumber, int pageSize){
		return null;
	}

	@Override
	public List getDtuList(String uuid, int vehicleTypeId,
			int vehicleModelId, String facName, int chargeStatus,
			int alarmStatus, int sort,int pageNumber,int pageSize, Long userId, int isAdmin,String city) {
		return dtuCountDao.getDtuList(uuid, vehicleTypeId, vehicleModelId, facName, chargeStatus, alarmStatus, sort,pageNumber,pageSize, userId, isAdmin,city);
	}

	@Override
	public Integer getDtuListCount(String uuid, int vehicleTypeId,
			int vehicleModelId, String facName, int chargeStatus,
			int alarmStatus, int sort, Long userId, int isAdmin,String city) {
		return dtuCountDao.getDtuListCount(uuid, vehicleTypeId, vehicleModelId, facName, chargeStatus, alarmStatus, sort, userId, isAdmin, city);
	}

	@Override
	public List<Dtu> getAllDtu(int isAdmin, Long userId) {
		if (isAdmin == 1)
			return this.getAll();
        Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
        filters.put("dtuUser.id", new SearchFilter("dtuUser.id", Operator.EQ, userId));
		Specification<Dtu> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				Dtu.class);
		return dtuDao.findAll(spec);
	}

	@Override
	public List getAlarmRealtime(String uuid, int isAdmin, Long userId,
			int pageNumber, int pageSize) {
		return dtuCountDao.getAlarmRealtime(uuid, isAdmin, userId, pageNumber, pageSize);
	}

	@Override
	public Integer getAlarmRealtimeCount(String uuid, int isAdmin, Long userId) {
		return dtuCountDao.getAlarmRealtimeCount(uuid, isAdmin, userId);
	}

	@Override
	public List<String> getCitys(int isAdmin,Long userId) {
		if (isAdmin ==1)
			return dtuDao.getCitysAdmin();
		else
			return dtuDao.getCitys(userId);
	}
	
	public Integer getMaxAlarmId(Long userId){
		try{
			int result = dtuDao.getMaxAlarmId(userId);
            return result;
		}
		catch(Exception  e){
			return 0;
		}
		
		
	}
	
	public Integer getNewAlarmRealtime(Long userId,Long alarmId){
		return dtuDao.getNewAlarmRealtime(userId, alarmId);
	}

	@Override
	public Integer getTotalCapacity(int isAdmin, Long userId) {
		try{
			if (isAdmin == 1)
				return dtuDao.getTotalCapacityAdmin();
			else
				return dtuDao.getTotalCapacity(userId);
			}
		catch(Exception  e){
			return 0;
		}
	}

	/**
	 * 获取设备管理uuid或者sn码
	 * wly
	 * @param isAdmin
	 * @param userId
	 * @return
	 */
	@Override
	public List<Object[]> getDtuSnList(int isAdmin, Long userId) {
		//SELECT dtu.`uuid`,sn.`sn`,IFNULL(sn.`sn`, dtu.`uuid`) AS ss FROM dtu dtu LEFT JOIN t_sn_info sn ON dtu.`uuid`=sn.`dtu_uuid`
		//WHERE dtu.`id` IN (SELECT dtuid FROM dtu_user_config c WHERE c.`userid`=4014 )

		StringBuffer sql = new StringBuffer();
		//不取SN信息
		/*
		sql.append("SELECT UUID,UUID AS sn,id FROM dtu");
		if (isAdmin !=1)
			sql.append(" where id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + ")");
		*/
		//添加sn状态为正常状态的
		sql.append("SELECT dtu.uuid,IFNULL(sn.sn, dtu.uuid) AS sn,dtu.id FROM dtu dtu LEFT JOIN t_sn_info sn ON dtu.uuid=sn.dtu_uuid and sn.status=1 ");
		if (isAdmin !=1)
			sql.append(" where dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " and status=1)");

		sql.append(" ORDER BY sn");
		javax.persistence.Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public List<Object[]> getManagerDtulist(int isAdmin, Long userId, Long optUserId) {
		StringBuffer sql = new StringBuffer();
		/**
		 * SELECT id,UUID,
		 (SELECT vehicle_number FROM vehicle vh WHERE vh.uuid =dtu.uuid) AS vehicle_number,
		 (SELECT type_name FROM vehicle_type WHERE id=( SELECT vehicle_type_id FROM vehicle vh WHERE vh.uuid =dtu.uuid)) AS vehicle_type_name,
		 (SELECT factory_name FROM battery_model WHERE id=battery_model_id) AS factory_name
		 FROM dtu dtu WHERE id IN (SELECT dtuid FROM dtu_user_config WHERE userid=4014);
		 */
		sql.append("SELECT id,");
		sql.append("IFNULL((SELECT MAX(sn) FROM t_sn_info t_sn WHERE t_sn.status=1 and length(t_sn.dtu_uuid)>1 AND t_sn.dtu_uuid=dtu.UUID),dtu.UUID) AS sn,");
		sql.append("(SELECT vehicle_number FROM vehicle vh WHERE vh.uuid =dtu.uuid) AS vehicle_number,");
		sql.append("(SELECT type_name FROM vehicle_type WHERE id=( SELECT vehicle_type_id FROM vehicle vh WHERE vh.uuid =dtu.uuid)) AS vehicle_type_name,");
		sql.append("(SELECT factory_name FROM battery_model WHERE id=dtu.battery_model_id) AS factory_name,dtu.uuid ");
		sql.append(" FROM dtu dtu ");
		sql.append(" where dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ userId + " and status=1)");

		if (isAdmin != 1)
			sql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ optUserId + " and status=1)");

		sql.append(" ORDER BY sn");
		javax.persistence.Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public List<Object[]> getManagerAllDtulist(int isAdmin, int isUse, String uuid,Long userId, Long optUserId) {
		/*StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,UUID,");
		sql.append("(SELECT vehicle_number FROM vehicle vh WHERE vh.uuid =dtu.uuid) AS vehicle_number,");
		sql.append("(SELECT type_name FROM vehicle_type WHERE id=( SELECT vehicle_type_id FROM vehicle vh WHERE vh.uuid =dtu.uuid)) AS vehicle_type_name,");
		sql.append("(SELECT factory_name FROM battery_model WHERE id=battery_model_id) AS factory_name, ");
		sql.append("(SELECT COUNT(dtuid) FROM dtu_user_config duc WHERE duc.dtuid=dtu.id AND duc.userid=" + userId + " and status=1) AS isuse ");
		sql.append(" FROM dtu dtu ");
		//去除已分配的
		sql.append(" where 1=1 ");
		if (isAdmin != 1)
			sql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config WHERE userid="+ optUserId + " and status=1)");

		if (uuid != null && !"".equals(uuid.trim())) {//加上已选中的
			sql.append(" and (dtu.uuid like '%" + uuid.trim() + "%' " +
					"or dtu.uuid in (SELECT uuid FROM vehicle WHERE vehicle_number like '%" + uuid.trim() + "%' )" +
					"or dtu.id in (SELECT distinct dtuid FROM dtu_user_config WHERE status=1 and userid=" + userId + ")" +
					")");
		}

		if(0 == isUse){
			sql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config where status=1 GROUP BY dtuid HAVING COUNT(dtuid)=1)");
		}else if(1 == isUse){
			sql.append(" and dtu.id in (SELECT dtuid FROM dtu_user_config where status=1 GROUP BY dtuid HAVING COUNT(dtuid)>1)");
		}
		sql.append(" ORDER BY isuse desc, uuid asc");
		System.out.println("分配DTU：" + sql.toString());*/

		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" SELECT id, ")
				.append(" IFNULL((SELECT MAX(sn) FROM t_sn_info t_sn WHERE t_sn.status=1 and length(t_sn.dtu_uuid)>1 AND t_sn.dtu_uuid=tab_a.uuid), tab_a.uuid) AS sn, ")
				.append(" (SELECT vehicle_number FROM vehicle vh WHERE vh.uuid =tab_a.uuid) AS vehicle_number, ")
				.append(" (SELECT type_name FROM vehicle_type WHERE id=( SELECT vehicle_type_id FROM vehicle vh WHERE vh.uuid =tab_a.uuid)) AS vehicle_type_name, ")
				.append(" (SELECT factory_name FROM battery_model WHERE id=tab_a.battery_model_id) AS factory_name,  ")
				.append(" isuse, uuid  ")
				.append(" FROM ")
				.append(" (  ")
				.append(" SELECT id,UUID,battery_model_id, ")
				.append(" (SELECT COUNT(dtuid) FROM dtu_user_config duc WHERE duc.dtuid=dtu.id AND duc.userid=" + userId + " AND STATUS=1) AS isuse, ")
				.append(" (SELECT COUNT(dtuid) FROM dtu_user_config duc WHERE duc.dtuid=dtu.id AND STATUS=1) AS nouse     ")
				.append(" FROM dtu dtu  ")
				.append(" WHERE 1=1  ");
		if (isAdmin != 1)
			sqlBuf.append(" AND dtu.id IN (SELECT dtuid FROM dtu_user_config WHERE userid=" + optUserId + " AND STATUS=1)  ");

		if (uuid != null && !"".equals(uuid.trim())) {//加上已选中的
			sqlBuf.append(" and (dtu.uuid like '%" + uuid.trim() + "%' ");
			sqlBuf.append(" or dtu.uuid in (SELECT uuid FROM vehicle WHERE vehicle_number like '%" + uuid.trim() + "%' )");
			sqlBuf.append(" or dtu.id in (SELECT distinct dtuid FROM dtu_user_config WHERE status=1 and userid=" + userId + ")" +
					")");
		}
		sqlBuf.append(" ) tab_a WHERE isuse=1 OR nouse=1  ORDER BY isuse DESC, sn ASC ");
		javax.persistence.Query query = em.createNativeQuery(sqlBuf.toString());
		return query.getResultList();
	}

	@Override
	public long getDtuCountByUserid(Long userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(0) FROM dtu WHERE dtu_user_id=" + userid + " OR id IN (SELECT dtuid FROM dtu_user_config duc WHERE duc.userid=" + userid + " AND duc.STATUS=1)");

		BigInteger query = (BigInteger)em.createNativeQuery(sql.toString()).getSingleResult();
		if (query == null)
			return 0;
		else
			return query.longValue();
	}

	@Override
	public List<Object[]> findDtuByUserId(Long userId) {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" SELECT tab_a.dtuid, ")
				.append(" (SELECT pt.prod_typename FROM prod_type pt WHERE pt.id=sn.prod_type ) AS prod_name, ")
				.append(" sn.hw_version, ")
				.append(" sn.sn, ")
				.append(" tab_a.uuid, ")
				.append(" tab_a.optuserid,IFNULL((SELECT du.user_name FROM dtu_user du WHERE du.is_admin=0 AND du.id=tab_a.optuserid ), 'ligoo_send') AS user_name, ")
				.append(" tab_a.opttimestr ")
				.append("   FROM  ")
				.append(" ( ")
				.append(" SELECT dtuid,(SELECT d.uuid FROM dtu d WHERE d.id=duc.dtuid ) AS UUID ,DATE_FORMAT(MAX(opttime), '%Y-%m-%d') AS opttimestr,MAX(optuserid) AS optuserid  ")
				.append(" FROM dtu_user_config duc WHERE duc.userid=? AND duc.status=1 GROUP BY duc.dtuid ")
				.append(" ) AS tab_a ")
				.append(" LEFT JOIN t_sn_info sn ON tab_a.uuid=sn.dtu_uuid AND sn.status=1 WHERE LENGTH(tab_a.uuid)>1 ORDER BY tab_a.opttimestr DESC, tab_a.dtuid DESC ");

		Object[] arg = new Object[]{ userId };
		List<Object[]> list = jdbcTemplate.query(sqlBuf.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("dtuid"),
						resultSet.getString("prod_name"),
						resultSet.getString("hw_version"),
						resultSet.getString("sn"),
						resultSet.getString("uuid"),
						resultSet.getString("optuserid"),
						resultSet.getString("user_name"),
						resultSet.getString("opttimestr")
				};
				return rs;
			}
		});
		return list;
	}

	@Override
	public List<Map<String, Object>> findDtuByUserIdMap(Long userId) {

		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" SELECT tab_a.dtuid, ")
				.append(" (SELECT pt.prod_typename FROM prod_type pt WHERE pt.id=sn.prod_type ) AS prod_name, ")
				.append(" sn.hw_version, ")
				.append(" sn.sn, ")
				.append(" tab_a.uuid, ")
				.append(" tab_a.optuserid,IFNULL((SELECT du.user_name FROM dtu_user du WHERE du.is_admin=0 AND du.id=tab_a.optuserid ), 'ligoo_send') AS user_name, ")
				.append(" tab_a.opttimestr ")
				.append("   FROM  ")
				.append(" ( ")
				.append(" SELECT dtuid,(SELECT d.uuid FROM dtu d WHERE d.id=duc.dtuid ) AS UUID ,DATE_FORMAT(MAX(opttime), '%Y-%m-%d') AS opttimestr,MAX(optuserid) AS optuserid  ")
				.append(" FROM dtu_user_config duc WHERE duc.userid=? AND duc.status=? GROUP BY duc.dtuid ")
				.append(" ) AS tab_a ")
				.append(" LEFT JOIN t_sn_info sn ON tab_a.uuid=sn.dtu_uuid AND sn.status=? WHERE LENGTH(tab_a.uuid)>1 ORDER BY tab_a.opttimestr DESC, tab_a.dtuid DESC ");

		Object[] arg = new Object[]{ userId, 1, 1 };

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlBuf.toString(), arg);



		return list;
	}

	@Override
	public int deluserDtu(Long userId, Long dtuid, Long optUserId) {
		String[] sqlArr = new String[4];
		//修改dtu_user_config 1-正常  2-删除
		String sql_config = "UPDATE dtu_user_config SET STATUS=2,optuserid=" + optUserId + ",opttime=NOW() WHERE dtuid=" + dtuid + " AND userid=" + userId ;
		sqlArr[0] = sql_config ;
		//修改dtu 防止主键关联异常
		String sql_dtu = "UPDATE dtu SET dtu_user_id=1 WHERE id=" + dtuid + " AND dtu_user_id=" + userId;
		sqlArr[1] = sql_dtu ;
		//修改 vehicle
		String sql_vehicle = "UPDATE vehicle SET dtu_user_id=1 WHERE dtu_id=" + dtuid + " AND dtu_user_id=" + userId;
		sqlArr[2] = sql_vehicle ;

		DtuUser dtuUser = dtuUserDao.findOne(userId);
		Dtu dtu = dtuDao.findOne(dtuid);
		if (dtu != null && dtuUser != null) {
			StringBuffer logContent = new StringBuffer();
			logContent.append("删除DTU用户关联：用户ID：").append(userId);
			logContent.append("，用户账号：").append(dtuUser.getUserName());
			logContent.append("，公司名称：").append(dtuUser.getCorp().getCorpName());
			logContent.append("，DTUID：").append(dtuid);
			logContent.append("，UUID：").append(dtu.getUuid());

			String log_sql = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'USERINFO', NOW(), '" + logContent.toString() + "')";
			sqlArr[3] = log_sql;
		}else {
			StringBuffer logContent = new StringBuffer();
			logContent.append("删除DTU用户关联：用户ID：").append(userId);
			logContent.append("，DTUID：").append(dtuid);

			String log_sql = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'USERINFO', NOW(), '" + logContent.toString() + "')";
			sqlArr[3] = log_sql;
		}

		int[] rs = jdbcTemplate.batchUpdate(sqlArr);

		return rs.length;
	}

	@Override
	public int deluserDtu(String userId, String dtuids, Long optUserId) {

		String[] idsArr = dtuids.split(",");
		String idsSql = StringUtil.getStrsplit(idsArr);

		List<String> sqlList = new ArrayList<String>();
		//修改dtu_user_config 1-正常  2-删除
		String sql_config = "UPDATE dtu_user_config SET STATUS=2,optuserid=" + optUserId + ",opttime=NOW() WHERE dtuid in " + idsSql + " AND userid=" + userId ;
		sqlList.add(sql_config);

		//修改dtu 防止主键关联异常
		String sql_dtu = "UPDATE dtu SET dtu_user_id=1 WHERE id in " + idsSql + " AND dtu_user_id=" + userId;
		sqlList.add(sql_dtu);

		//修改 vehicle
		String sql_vehicle = "UPDATE vehicle SET dtu_user_id=1 WHERE dtu_id in " + idsSql + " AND dtu_user_id=" + userId;
		sqlList.add(sql_vehicle);

		StringBuffer logContent = new StringBuffer();
		logContent.append("删除DTU用户关联：用户ID：").append(userId);
		logContent.append("，DTUID：").append(dtuids);

		String log_sql = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'USERINFO', NOW(), '" + logContent.toString() + "')";
		sqlList.add(log_sql);

		String[] sqlArr = new String[sqlList.size()];
		for (int i = 0; i < sqlList.size(); i++) {
			sqlArr[i] = sqlList.get(i);
		}

		int[] rs = jdbcTemplate.batchUpdate(sqlArr);
		return rs[0];//返回修改用户设备配置表状态的行数
	}

	@Override
	public String getSNByUUID(String uuid) {
		StringBuffer sql = new StringBuffer("SELECT IFNULL(MAX(sn), '未配置') AS sn FROM t_sn_info WHERE STATUS=1 and length(dtu_uuid)>1 ");
		sql.append(" AND dtu_uuid='").append(uuid).append("'");
		Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
		if (map != null && map.size()>0) {
			Object str = map.get("sn");
			if (str != null) {
				return str.toString();
			}
		}
		return "未配置";
	}

	@Override
	public String getSIMByUUID(String uuid) {
		//DTU上报的数据为CCID的后十一位
		StringBuffer sql = new StringBuffer("SELECT IFNULL(MAX(sim), '未知') AS sim FROM t_dtu_sim WHERE STATUS=1 ");
		sql.append(" AND RIGHT(ccid, 11)=(SELECT IFNULL(sim_card, '未知') FROM dtu WHERE UUID='").append(uuid).append("')");
		Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
		if (map != null && map.size()>0) {
			Object str = map.get("sim");
			if (str != null) {
				return str.toString();
			}
		}
		return "无";
	}

	@Override
	public int getBmuNum(String uuid) {
		StringBuffer sql = new StringBuffer("SELECT MAX(bmu_num) as bmu_num FROM dtu ");
		sql.append(" WHERE uuid='").append(uuid).append("'");
		Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());

		if (map != null && map.size()>0) {
			Object str = map.get("bmu_num");
			if (str != null) {
				return StringUtil.StringToInt(str.toString());
			}
		}
		return 0;
	}

	@Override
	public EasyUiUtil.PageForData findDTUByUserId(int rows, int page, String sort, String order, String userId) {


		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tab_a.dtuid as id, ")
				.append(" (SELECT pt.prod_typename FROM prod_type pt WHERE pt.id=sn.prod_type ) AS prod_name, ")
				.append(" sn.hw_version as hw_version, ")
				.append(" sn.sn as sn, ")
				.append(" tab_a.uuid as uuid, ")
				.append(" tab_a.optuserid,IFNULL((SELECT du.user_name FROM dtu_user du WHERE du.is_admin=0 AND du.id=tab_a.optuserid ), '力高') AS user_name, ")
				.append(" tab_a.opttimestr ")
				.append("   FROM  ")
				.append(" ( ")
				.append(" SELECT dtuid,(SELECT d.uuid FROM dtu d WHERE d.id=duc.dtuid ) AS UUID ,DATE_FORMAT(MAX(opttime), '%Y-%m-%d') AS opttimestr,MAX(optuserid) AS optuserid  ")
				.append(" FROM dtu_user_config duc WHERE duc.userid=" + userId + " AND duc.status=1 GROUP BY duc.dtuid ")
				.append(" ) AS tab_a ")
				.append(" LEFT JOIN t_sn_info sn ON tab_a.uuid=sn.dtu_uuid AND sn.status=1 WHERE LENGTH(tab_a.uuid)>1 ");

		StringBuffer whereSql = new StringBuffer(" ");
		//ORDER BY tab_a.opttimestr DESC, tab_a.dtuid DESC
		StringBuffer orderSql = new StringBuffer();
		if (!"".equals( StringUtil.null2String(sort).trim())){
			orderSql.append(" order by ").append(sort).append(" ").append(order);
		}else{// 默认排序为时间倒序，方便查询
			orderSql.append(" order by tab_a.opttimestr DESC, tab_a.dtuid DESC  ");
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(EasyUiUtil.getPageSQL(sql.append(whereSql).append(orderSql).toString(), page, rows));

		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT COUNT(*) AS ct FROM (SELECT count(duc.dtuid) as ct FROM dtu_user_config duc WHERE duc.userid=" + userId + " AND duc.status=1 GROUP BY duc.dtuid ) t ").append(whereSql.toString());
		Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
		String total = StringUtil.null2String(mapCount.get("ct"));

		EasyUiUtil.PageForData pageObj = new EasyUiUtil.PageForData();
		pageObj.setRows(list);
		pageObj.setTotal(Long.parseLong(total));

		return pageObj;
	}

}
