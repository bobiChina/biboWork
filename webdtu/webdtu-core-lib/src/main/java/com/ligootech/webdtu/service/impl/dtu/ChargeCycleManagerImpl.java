package com.ligootech.webdtu.service.impl.dtu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.ChargeCycle;
import com.ligootech.webdtu.repository.ChargeCycleDao;
import com.ligootech.webdtu.service.dtu.ChargeCycleManager;

@Service("chargeCycleManager")
public class ChargeCycleManagerImpl extends GenericManagerImpl<ChargeCycle, Long> implements ChargeCycleManager {
	@Autowired
	private ChargeCycleDao chargeCycleDao;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	public void setCorpDao(ChargeCycleDao chargeCycleDao) {
		super.dao = chargeCycleDao;
		this.chargeCycleDao = chargeCycleDao;
	}

	@Override
	public List getAllMilegeAndTimeLength(int isAdmin, Long userId) {
		//修改首页统计数据 总行驶里程、最大单车行驶里程、总运行时长  wly 2015年12月4日16:20:25
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT FORMAT(SUM(zxslc), 2) AS zxslc, FORMAT(SUM(zyxsc)/3600 , 2) AS zyxsc , FORMAT(MAX(zddclc), 2) AS zddclc, FORMAT(MAX(zddcyxsc)/3600, 2) AS zddcyxsc,dtu_id FROM ");
		sql.append(" (");
		sql.append(" SELECT SUM(cc.running_milege) AS zxslc,SUM(cc.running_time_length) AS zyxsc,MAX(cc.total_milege) AS zddclc,SUM(cc.running_time_length) zddcyxsc ,dtu_id FROM charge_cycle AS cc GROUP BY dtu_id ");
		sql.append(" ) tab_a ");
		if (isAdmin != 1){
			sql.append(" WHERE dtu_id IN (SELECT id FROM dtu WHERE dtu_user_id=" + userId + " ) ");
		}

		List<String[]> list = jdbcTemplate.query(sql.toString(), new Object[]{ }, new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				return new String[]{
						resultSet.getString("zxslc"),
						resultSet.getString("zyxsc"),
						resultSet.getString("zddclc"),
						resultSet.getString("zddcyxsc")
				};
			}
		});

		if (list != null && list.size() > 0){
			String[] rsArr = list.get(0);
			if(null != rsArr && rsArr.length >3 ){
				List<String> rsList = new ArrayList<String>();
				rsList.add(rsArr[0]);
				rsList.add(rsArr[1]);
				rsList.add(rsArr[2]);
				rsList.add(rsArr[3]);

				return rsList;
			}
		}
		List<String> rsList = new ArrayList<String>();
		rsList.add("0");
		rsList.add("0");
		rsList.add("0");
		rsList.add("0");

		return rsList;

		/*
		原先代码
		if (isAdmin ==1)
			return chargeCycleDao.getAllMilegeAndTimeLengthAdmin();
		else
			return chargeCycleDao.getAllMilegeAndTimeLength(userId);*/
	}

}
