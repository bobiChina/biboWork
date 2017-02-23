package com.ligootech.webdtu.service.impl.dtu;

import com.ligootech.webdtu.util.StringUtil;
import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.repository.DtuUserDao;
import com.ligootech.webdtu.service.dtu.DtuUserManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dtuUserManager")
public class DtuUserManagerImpl extends GenericManagerImpl<DtuUser, Long> implements DtuUserManager{
	private final String DTUADMIN = "dtuadmin";

	@Autowired
	private DtuUserDao dtuUserDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
    @Autowired
    public void setDtuUserDao(DtuUserDao dtuUserDao) {
        super.dao = dtuUserDao;
        this.dtuUserDao = dtuUserDao;
    }

	
	@Override
	public DtuUser findByUserName(String userName) {
		return dtuUserDao.getUserByName(userName);
	}

	@Override
	public DtuUser findByUserId(Long userId) {
		return dtuUserDao.findOne(userId);
	}

	@Override
	public DtuUser findLoginByUserName(String userName) {
		//判断是否为DTU管理员或者前台用户，不是这两种的不予登录
		DtuUser dtuUser = dtuUserDao.getUserByName(userName);
		if (dtuUser == null) {
			return null;
		}else if (dtuUser.getIsAdmin() < 2 ){// 0-普通用户 1-前台超级管理员
			return dtuUser;
		}else{//判断是否为DTU管理员
			List<String> rolesList = findUserRoles(dtuUser.getId());
			if (rolesList.contains(DTUADMIN)){
				return dtuUser;
			}
		}
		return null;
	}

	@Override
	public DtuUser findBackenLoginByUserName(String userName) {
		return dtuUserDao.findBackenLoginByUserName(userName);
	}

	@Override
	public List<Object[]> findDtuUserList(int isAdmin, Long userId) {
		// isAdmin 1-管理员查询所有 2-后台操作用户，查询类型为客户的用户 其他只能查询权限范围内的用户
		StringBuffer sql = new StringBuffer();
		//sql.append("SELECT * from (SELECT id,(SELECT corp_name FROM corp cp WHERE cp.id=dtu.corp_id ) as corpname,user_name,(SELECT COUNT(DISTINCT userid) FROM user_relation ur WHERE ur.STATUS=1 AND ur.headuserid=dtu.id) AS next_user FROM dtu_user dtu ");
		//前台客户显示客户账号
		sql.append("SELECT * from (SELECT id,user_name as corpname,user_name,(SELECT ifnull(max(opttype), 0) FROM user_relation ur WHERE ur.STATUS=1 AND ur.userid=dtu.id) AS opttype,(SELECT count(distinct id) FROM user_relation ur WHERE ur.STATUS=1 AND ur.headuserid=dtu.id) AS next_user FROM dtu_user dtu ");
		if (isAdmin ==1){
			sql.append(" where is_admin=0 ");
		} else if(isAdmin == 2){
			sql.append(" where is_admin=0 ");
		} else{
			sql.append(" where dtu.is_admin=0 and dtu.id in (SELECT userid FROM user_relation WHERE STATUS=1 and headuserid="+ userId + ")");
		}

		sql.append(") a ORDER BY CONVERT(corpname USING gbk)");
		javax.persistence.Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public List<Object[]> findBackendUserList() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,full_name as fullname,user_name FROM dtu_user dtu where is_admin=2 order by CONVERT(fullname USING gbk)");
		javax.persistence.Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();
	}

	@Override
	public Map<String, Integer> findNextUserCount() {
		return null;
	}

	@Override
	public List<Object[]> findLinkUserList(int isAdmin, Long corpId, Long optUserId) {
		//corp_linkman 表已删除
		/*StringBuffer sql = new StringBuffer();
		sql.append("SELECT linkman,linktel,linkemail,id FROM corp_linkman WHERE corp_id=" + corpId + " AND STATUS=1 ");
		if (isAdmin !=1)
			sql.append(" AND opt_userid="+ optUserId );

		sql.append(" ORDER BY id");
		javax.persistence.Query query = em.createNativeQuery(sql.toString());
		return query.getResultList();*/
		return null;
	}

	@Override
	public int updateDtuUserFullName(String fullName, Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("update dtu_user SET full_name=? where id=? ");
		javax.persistence.Query query = em.createNativeQuery(sql.toString()).setParameter(1, fullName).setParameter(2, userId);
		int rs = query.executeUpdate();
		return rs;
	}

	@Override
	public int addUserRelation(Long userId, String userName, Long optUserId, int type) {
		StringBuffer sql = new StringBuffer("INSERT INTO user_relation(userid, username, headuserid, opt_userid, opt_time,status, opttype) values(?, ?, ?, ?, NOW(), ?, ?)");
		Query query = em.createNativeQuery(sql.toString()).setParameter(1, userId)
				.setParameter(2, userName).setParameter(3, optUserId).setParameter(4, optUserId).setParameter(5, 1).setParameter(6, type);
		int rs = query.executeUpdate();
		return rs;
	}

	@Override
	public int delUserRelation(int isAdmin, Long userId, Long optUserId) {
		if (1 == isAdmin){//管理员删除用户并解除相关关系
			StringBuffer sql = new StringBuffer("update user_relation set status=?,opt_userid=? ,opt_time=NOW() where userid=? ");
			Query query = em.createNativeQuery(sql.toString()).setParameter(1, 0)
					.setParameter(2, optUserId).setParameter(3, userId);
			int rs = query.executeUpdate();

			//删除用户
			sql = new StringBuffer("delete from dtu_user where id=? ");
			Query delQuery = em.createNativeQuery(sql.toString()).setParameter(1, userId);
			int delRs = delQuery.executeUpdate();
			return delRs;
		}
		else{
			StringBuffer sql = new StringBuffer("update user_relation set status=?,opt_userid=? ,opt_time=NOW() where headuserid=? and userid=? ");
			Query query = em.createNativeQuery(sql.toString()).setParameter(1, 0)
					.setParameter(2, optUserId).setParameter(3, optUserId).setParameter(4, userId);
			int rs = query.executeUpdate();
			return rs;
		}
	}

	@Override
	public int addDtuUserConfig(Long userId, String dtuIds, Long optUserId, int isAdmin) {
		String[] dtuArr = dtuIds.split(",");
		StringBuffer dtuStr = new StringBuffer();
		String uuid = "";
		for (int i = 0; i < dtuArr.length; i++) {
			uuid = dtuArr[i] ;
			if ( uuid != null && !"".equals(uuid.trim())) {
				dtuStr.append("'").append(uuid.trim()).append("',");
			}
		}
		//先删除配置表中的数据  修改当前用户权限范围内的
		StringBuffer sql = new StringBuffer("UPDATE dtu_user_config SET STATUS=?,opttime=NOW(),optuserid=? WHERE userid=? AND STATUS=? and dtuid in (select distinct dtuid from (SELECT dtuid FROM dtu_user_config WHERE userid=? AND STATUS=?) a)");
		Query query_del = em.createNativeQuery(sql.toString()).setParameter(1, 0)
				.setParameter(2, optUserId).setParameter(3, userId).setParameter(4, 1).setParameter(5, optUserId).setParameter(6, 1);
		int rs_del = query_del.executeUpdate();

		//拼装重新分配的DTU
		int rs = -1;
		if (dtuStr.length() > 1){
			String dtuSql = dtuStr.substring(0, dtuStr.length()-1).toString();
			sql = new StringBuffer("INSERT INTO dtu_user_config(dtuid, userid, optuserid, opttime, STATUS) SELECT id, ?, ?, NOW(), 1 FROM dtu where uuid in (");
			sql.append(dtuSql).append(")");
			Query query_save = em.createNativeQuery(sql.toString()).setParameter(1, userId).setParameter(2, optUserId);
			rs = query_save.executeUpdate();
		}
		return rs;
	}

	@Override
	public int delDtuUserConfig(Long userId, String uuid, Long optUserId) {
		StringBuffer sql = new StringBuffer("UPDATE dtu_user_config SET STATUS=?,opttime=NOW(),optuserid=? WHERE userid=? AND STATUS=? and dtuid=(SELECT MAX(id) FROM dtu WHERE UUID=? )");
		Query query_del = em.createNativeQuery(sql.toString()).setParameter(1, 2)
				.setParameter(2, optUserId).setParameter(3, userId).setParameter(4, 1).setParameter(5, uuid);
		int rs_del = query_del.executeUpdate();
		return 0;
	}

	@Override
	public Long getUpUserId(Long userId, Long dtuId) {
		StringBuffer sql = new StringBuffer("SELECT MAX(optuserid) FROM dtu_user_config WHERE STATUS=1 ");
		sql.append(" AND userid=").append(userId);
		sql.append(" AND dtuid=").append(dtuId);
		Query querySearch = em.createNativeQuery(sql.toString());
		List rsList = querySearch.getResultList();
		if (null != rsList && rsList.size() > 0){
			java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
			if (bi != null) {
				return bi.longValue();
			}
		}
		return null;
	}

	@Override
	public List<String> findUserRoles(Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT role_code FROM tw_role WHERE id IN (SELECT DISTINCT role_id FROM tw_user_role WHERE user_id=").append(userId).append(")");
		Query querySearch = em.createNativeQuery(sql.toString());
		return querySearch.getResultList();
	}

	@Override
	public List<String> findUserPermission(Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT permission FROM tw_role_permission WHERE role_id IN (SELECT DISTINCT role_id FROM tw_user_role WHERE user_id=").append(userId).append(")");
		Query querySearch = em.createNativeQuery(sql.toString());
		return querySearch.getResultList();
	}

	@Override
	public Long findDtuCountBUserId(Long userId) {
		StringBuffer sql = new StringBuffer("SELECT count(id) FROM dtu_user_config WHERE STATUS=1 ");
		sql.append(" AND userid=").append(userId);
		Query querySearch = em.createNativeQuery(sql.toString());
		List rsList = querySearch.getResultList();
		if (null != rsList && rsList.size() > 0){
			java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
			if (bi != null) {
				return bi.longValue();
			}
		}
		return null;
	}

	@Override
	public Long findOrderCountBUserId(Long userId) {
		StringBuffer sql = new StringBuffer("SELECT count(id) FROM t_order WHERE STATUS<99 ");// 订单定义已修改 wly 2016年4月14日 15:04:41
		sql.append(" AND userid=").append(userId);
		Query querySearch = em.createNativeQuery(sql.toString());
		List rsList = querySearch.getResultList();
		if (null != rsList && rsList.size() > 0){
			java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
			if (bi != null) {
				return bi.longValue();
			}
		}
		return null;
	}

	@Override
	public int updatePermission(DtuUser dtuUser, String permissionIds, Long optUserId) {

		ArrayList<String> sqlList = new ArrayList<String>();

		//删除原权限
		sqlList.add("DELETE FROM tw_user_role WHERE user_id=" + dtuUser.getId());

		//添加新权限
		String[] idsArr = permissionIds.split(",");
		for (int i = 0; i <idsArr.length ; i++) {
			String str = StringUtil.null2String(idsArr[i]).trim();
			if (!"".equals(str)){
				sqlList.add("insert into tw_user_role(user_id, role_id) values(" + dtuUser.getId() + "," + str + ")");
			}
		}
/*
		用户信息日志记录中已添加
		//添加日志记录
		String content = "修改权限>>用户ID：" + dtuUser.getId() + "，用户账号：" + dtuUser.getUserName() + "，用户名：" + dtuUser.getFullName() + "，新权限ID:" + permissionIds;
		String str_log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'PERMISSION', NOW(), '" + content + "')";
		sqlList.add(str_log);
*/

		String[] sqlArr = new String[sqlList.size()];
		for (int i = 0; i < sqlList.size(); i++) {
			sqlArr[i] = sqlList.get(i);
		}

		int[] rs = jdbcTemplate.batchUpdate(sqlArr);
		return rs.length;
	}

	@Override
	public List<Map<String, String>> findUserPermissionName(Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,name,role_code FROM tw_role WHERE id IN (SELECT role_id FROM tw_user_role WHERE user_id=? ) order by id asc");

		Object[] arg = new Object[]{userId};
		List<Map<String, String>> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", resultSet.getString("id"));
				map.put("name", resultSet.getString("name"));
				map.put("role_code", resultSet.getString("role_code"));
				return map;
			}
		});

		return list;
	}

	@Override
	public int updatePass(String userpass, Long userId, Long optUserId) {

		DtuUser dtuUser = dtuUserDao.findOne(userId);

		String[] sqlArr = new String[2];
		//修改密码
		String update_sql = "update dtu_user set user_pass='" + userpass + "' WHERE id=" + userId;
		sqlArr[0] = update_sql;

		//日志记录
		String content = "修改密码>>用户ID:" + userId + "，原密码：" + dtuUser.getUserPass() + "，新密码：" + userpass + "。";
		String str_log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'USERINFO', NOW(), '" + content + "')";
		sqlArr[1] = str_log;

		int[] rs = jdbcTemplate.batchUpdate(sqlArr);
		return rs.length;
	}

	@Override
	public void saveDtuUser(DtuUser dtuUser, Long optUserId) {
		String optType = "修改用户：";
		if (dtuUser.getId() == null || dtuUser.getId().longValue() == 0){
			optType = "新增用户：";
		}

		dtuUserDao.save(dtuUser);

		StringBuffer logContent = new StringBuffer();
		logContent.append(optType);
		logContent.append("用户ID：").append(dtuUser.getId());
		logContent.append("，用户姓名：").append(dtuUser.getFullName());
		logContent.append("，用户账号：").append(dtuUser.getUserName());
		logContent.append("，用户密码：").append(dtuUser.getUserPass());
		logContent.append("，电话：").append(dtuUser.getRelation());
		logContent.append("，邮箱：").append(dtuUser.getEmail());
		logContent.append("，公司ID：").append(dtuUser.getCorp().getId());
		logContent.append("，公司名称：").append(dtuUser.getCorp().getCorpName());
		logContent.append("，公司电话：").append(dtuUser.getCorp().getPhone());
		logContent.append("，公司邮箱：").append(dtuUser.getCorp().getEmail());
		logContent.append("，公司地址：").append(dtuUser.getCorp().getAddr());

		String sql = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'USERINFO', NOW(), '" + logContent.toString() + "')";
		int rs = jdbcTemplate.update(sql);
	}

	@Override
	public int delUser(Long userId, Long optUserId) {

		DtuUser dtuUser = dtuUserDao.findOne(userId);
		String[] sqlArr = new String[4];
		//删除权限表
		String relation_sql = "DELETE FROM user_relation WHERE headuserid=" + userId + " OR userid="  + userId;
		sqlArr[0] = relation_sql;

		//删除角色表
		String role_sql = "DELETE FROM tw_user_role WHERE user_id=" + userId;
		sqlArr[1] = role_sql;

		//删除用户表
		String dtuuser_sql = "update dtu_user set user_name='',is_admin=3 WHERE id=" + userId;
		sqlArr[2] = dtuuser_sql;

		StringBuffer logContent = new StringBuffer();
		logContent.append("删除用户：用户ID：").append(dtuUser.getId());
		logContent.append("，用户姓名：").append(dtuUser.getFullName());
		logContent.append("，用户账号：").append(dtuUser.getUserName());
		logContent.append("，用户密码：").append(dtuUser.getUserPass());
		logContent.append("，电话：").append(dtuUser.getRelation());
		logContent.append("，邮箱：").append(dtuUser.getEmail());
		logContent.append("，公司ID：").append(dtuUser.getCorp().getId());
		logContent.append("，公司名称：").append(dtuUser.getCorp().getCorpName());
		logContent.append("，公司电话：").append(dtuUser.getCorp().getPhone());
		logContent.append("，公司邮箱：").append(dtuUser.getCorp().getEmail());
		logContent.append("，公司地址：").append(dtuUser.getCorp().getAddr());

		String log_sql = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'USERINFO', NOW(), '" + logContent.toString() + "')";
		sqlArr[3] = log_sql;

		int[] rs = jdbcTemplate.batchUpdate(sqlArr);

		return rs.length;
	}

	@Override
	public int delUser4OTA(Long userId, Long optUserId) {
		DtuUser dtuUser = dtuUserDao.findOne(userId);
		String[] sqlArr = new String[3];

		//删除角色表
		String role_sql = "DELETE FROM tw_user_role WHERE user_id=" + userId;
		sqlArr[0] = role_sql;

		//删除用户表
		String dtuuser_sql = "update dtu_user set user_name='',is_admin=3 WHERE id=" + userId;
		sqlArr[1] = dtuuser_sql;

		StringBuffer logContent = new StringBuffer();
		logContent.append("删除用户：用户ID：").append(dtuUser.getId());
		logContent.append("，用户姓名：").append(dtuUser.getFullName());
		logContent.append("，用户账号：").append(dtuUser.getUserName());
		logContent.append("，用户密码：").append(dtuUser.getUserPass());
		logContent.append("，电话：").append(dtuUser.getRelation());
		logContent.append("，邮箱：").append(dtuUser.getEmail());
		logContent.append("，公司ID：").append(dtuUser.getCorp().getId());
		logContent.append("，公司名称：").append(dtuUser.getCorp().getCorpName());
		logContent.append("，公司电话：").append(dtuUser.getCorp().getPhone());
		logContent.append("，公司邮箱：").append(dtuUser.getCorp().getEmail());
		logContent.append("，公司地址：").append(dtuUser.getCorp().getAddr());

		String log_sql = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'USERINFO', NOW(), '" + logContent.toString() + "')";
		sqlArr[2] = log_sql;

		int[] rs = jdbcTemplate.batchUpdate(sqlArr);

		return rs.length;
	}

	@Override
	public List<Object[]> findSubUserList(Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,user_name,user_pass FROM dtu_user WHERE id IN(SELECT DISTINCT userid FROM user_relation WHERE STATUS=? AND headuserid=?)");

		Object[] arg = new Object[]{ 1, userId };
		List<Object[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("id"),
						resultSet.getString("user_name"),
						resultSet.getString("user_pass")
				};
				return rs;
			}
		});
		return list;
	}

	@Override
	public List<Object[]> findBackendRoleList() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,name AS role_name,role_code FROM  tw_role WHERE role_type=1 ORDER BY id");

		Object[] arg = new Object[]{};
		List<Object[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("id"),
						resultSet.getString("role_name"),
						resultSet.getString("role_code")
				};
				return rs;
			}
		});
		return list;
	}

	/**
	 * 获取不同人员类型用户
	 *
	 * @param type
	 * @return
	 */
	@Override
	public List<Object[]> findUserByType(int type) {
		StringBuffer sql = new StringBuffer();
		if (type == 2){
			sql.append("SELECT id,full_name FROM dtu_user WHERE is_admin=2 AND is_technicist=1");
		}else {
			sql.append("SELECT id,full_name FROM dtu_user WHERE is_admin=2 AND is_salesman=1");
		}

		Object[] arg = new Object[]{};
		List<Object[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("id"),
						resultSet.getString("full_name")
				};
				return rs;
			}
		});
		return list;
	}

	/**
	 * 获取角色用户
	 * @param role
	 * @return
	 */
	@Override
	public List<Object[]> findUserByRole(String role) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,full_name FROM dtu_user WHERE is_admin=2 ");
		sql.append("AND id IN (SELECT DISTINCT user_id FROM tw_user_role WHERE role_id=(SELECT id FROM tw_role WHERE role_code='")
				.append(role).append("'))");
		Object[] arg = new Object[]{};
		List<Object[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("id"),
						resultSet.getString("full_name")
				};
				return rs;
			}
		});
		return list;
	}


}
