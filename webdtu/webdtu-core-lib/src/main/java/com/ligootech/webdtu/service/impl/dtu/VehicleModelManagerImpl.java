package com.ligootech.webdtu.service.impl.dtu;

import java.util.List;

import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.VehicleModel;
import com.ligootech.webdtu.repository.VehicleModelDao;
import com.ligootech.webdtu.service.dtu.VehicleModelManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service("vehicleModelManager")
public class VehicleModelManagerImpl extends GenericManagerImpl<VehicleModel, Long> implements VehicleModelManager {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private VehicleModelDao vehicleModelDao;
	@Autowired
	public void setVehicleModelDao(VehicleModelDao vehicleModelDao) {
		super.dao = vehicleModelDao;
		this.vehicleModelDao = vehicleModelDao;
	}
	
	@Override
	public List<Object[]> getVehicleModelByTypeId(Long typeId, int isAdmin, Long userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id, model_name FROM vehicle_model WHERE LENGTH(model_name)>1 AND type_id=" + typeId );
		if (isAdmin != 1)
			sql.append(" AND id IN (SELECT DISTINCT vehicle_model_id FROM vehicle AS v WHERE dtu_id IN (SELECT DISTINCT dtuid FROM dtu_user_config WHERE STATUS=1 AND userid=" + userId + "))" );
		sql.append(" order by id asc");
		Query query = em.createNativeQuery(sql.toString());
		List<Object[]> ar = (List<Object[]>)query.getResultList();
		return ar;
	}

	@Override
	public Long insertVehicleModel(Long typeId, String model_name) {
		StringBuffer sql = new StringBuffer("INSERT INTO vehicle_model(model_name, type_id) VALUES(?, ?)");
		Query queryInsert = em.createNativeQuery(sql.toString()).setParameter(1, model_name.trim()).setParameter(2, typeId);
		int rs = queryInsert.executeUpdate();
		if (rs > -1){
			sql = new StringBuffer("SELECT MAX(id) FROM vehicle_model");
			Query querySearch = em.createNativeQuery(sql.toString());
			List rsList = querySearch.getResultList();
			if (null != rsList && rsList.size() > 0){
				java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
				return bi.longValue();
			}
		}
		return null;
	}

	@Override
	public int delVehicleModel() {
		//删除无关的车辆型号 删除车辆信息表和订单表中都未使用的
		/*
		DELETE FROM vehicle_model WHERE id NOT IN (
			SELECT vehicle_model_id FROM
			(
					SELECT DISTINCT vehicle_model_id FROM vehicle
					UNION
					SELECT DISTINCT vehicle_model_id FROM t_order
			) t_ids
		)
		*/
		StringBuffer sql = new StringBuffer("DELETE FROM vehicle_model WHERE id NOT IN (");
		sql.append("SELECT vehicle_model_id FROM (");
		sql.append("SELECT DISTINCT vehicle_model_id FROM vehicle UNION SELECT DISTINCT vehicle_model_id FROM t_order ");
		sql.append(") t_ids");
		sql.append(")");
		Query queryDel = em.createNativeQuery(sql.toString());
		int rsDel = queryDel.executeUpdate();
		return rsDel;
	}
}
