package com.ligootech.webdtu.service.impl.dtu;

import com.ligootech.webdtu.util.StringUtil;
import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.Corp;
import com.ligootech.webdtu.repository.CorpDao;
import com.ligootech.webdtu.service.dtu.CorpManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("corpManager")
public class CorpManagerImpl extends GenericManagerImpl<Corp, Long> implements CorpManager {

	@Autowired
	private CorpDao corpDao;

	@Autowired
	public void setCorpDao(CorpDao corpDao) {
		super.dao = corpDao;
		this.corpDao = corpDao;
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<Object[]> getCorpList() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.id,c.corp_name FROM corp c WHERE c.id>? ORDER BY CONVERT(c.corp_name USING gbk), id");

		Object[] arg = new Object[]{ 1 };
		List<Object[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("id"),
						resultSet.getString("corp_name")
				};
				return rs;
			}
		});
		return list;
	}

	@Override
	public List<Object[]> getCorpListNoUser() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.id,c.corp_name FROM corp c WHERE c.id not in (select distinct corp_id from dtu_user) ORDER BY CONVERT(c.corp_name USING gbk), id");

		Object[] arg = new Object[]{ };
		List<Object[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("id"),
						resultSet.getString("corp_name")
				};
				return rs;
			}
		});
		return list;
	}

	@Override
	public List<Object[]> getCorpAccount(Long corpId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT user_name, user_pass FROM dtu_user WHERE corp_id=? ORDER BY id");

		Object[] arg = new Object[]{corpId};
		List<Object[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				Object[] rs = new Object[]{
						resultSet.getString("user_name"),
						resultSet.getString("user_pass")
				};
				return rs;
			}
		});
		return list;
	}

	@Override
	public void saveCorp(Corp corp, Long optUserId) {
		String optType = "修改公司信息：";
		Long id = corp.getId();
		if (id == null || id.longValue() == 0) {
			optType = "新增公司信息：";
		}
		corpDao.save(corp);
		StringBuffer logContent = new StringBuffer();
		logContent.append(optType);
		logContent.append("公司ID：").append(corp.getId());
		logContent.append("，公司名称：").append(corp.getCorpName());
		logContent.append("，公司电话：").append(corp.getPhone());
		logContent.append("，公司邮箱：").append(corp.getEmail());
		logContent.append("，公司地址：").append(corp.getAddr());
		logContent.append("，联系人姓名：").append(corp.getLinkMan());
		logContent.append("，联系人电话：").append(corp.getLinkPhone());
		logContent.append("，联系人邮箱：。").append(corp.getLinkEmail());

		String sql = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'CORP', NOW(), '" + logContent.toString() + "')";
		int rs = jdbcTemplate.update(sql);
	}

	@Override
	public void delCorp(Long corpId, Long optUserId) {
		/******************************************
		 * 查询该删除公司的历史订单：（日志记录表）
		 * 先查询已删除订单中客户ID为该ID值
		 ******************************************/
		Corp corp = corpDao.findOne(corpId);
		List<String> sqlList = new ArrayList<String>();
		sqlList.add("DELETE FROM t_order WHERE STATUS=99 AND corp_id=" + corpId);
		sqlList.add("DELETE FROM corp WHERE id=" + corpId);
		//corpDao.delete(corpId);
		StringBuffer logContent = new StringBuffer();
		logContent.append("删除公司信息：公司ID：").append(corp.getId());
		logContent.append("，公司名称：").append(corp.getCorpName());
		logContent.append("，公司电话：").append(corp.getPhone());
		logContent.append("，公司邮箱：").append(corp.getEmail());
		logContent.append("，公司地址：").append(corp.getAddr());
		logContent.append("，联系人姓名：").append(corp.getLinkMan());
		logContent.append("，联系人电话：").append(corp.getLinkPhone());
		logContent.append("，联系人邮箱：。").append(corp.getLinkEmail());

		String sql = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'CORP', NOW(), '" + logContent.toString() + "')";
		sqlList.add(sql);

		String[] sqlArr = new String[sqlList.size()];
		for (int i = 0; i < sqlList.size(); i++) {
			sqlArr[i] = sqlList.get(i);
		}
		int[] rs = jdbcTemplate.batchUpdate(sqlArr);
	}

	/**
	 * 验证客户名称是否重复
	 * @param corpName
	 * @param id
	 * @return
	 */
	@Override
	public int checkCorpName(String corpName, Long id) {
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append("SELECT COUNT(*) AS ct FROM corp WHERE corp_name='").append(corpName).append("' ");
		if (id != null && id.longValue() > 0) {
			sqlBuff.append("AND id<>").append(id);
		}
		Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuff.toString());
		if (map == null) {
			return 0;
		}else{
			int rs = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
			if(rs > 0){
				return -1;
			}
		}
		return 0;
	}
}
