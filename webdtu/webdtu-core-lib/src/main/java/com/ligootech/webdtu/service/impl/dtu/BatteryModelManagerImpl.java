package com.ligootech.webdtu.service.impl.dtu;

import java.util.List;

import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.BatteryModel;
import com.ligootech.webdtu.repository.BatteryModelDao;
import com.ligootech.webdtu.service.dtu.BatteryModelManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service("batteryModelManager")
public class BatteryModelManagerImpl extends GenericManagerImpl<BatteryModel, Long>  implements BatteryModelManager{
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private BatteryModelDao batteryModelDao;

	@Autowired
	public void setBatteryModelDao(BatteryModelDao batteryModelDao) {
		super.dao = batteryModelDao;
		this.batteryModelDao = batteryModelDao;
	}
	@Override
	public List<String> getBatteryFacNames(Long userId,int isAdmin) {
		if (isAdmin == 0)
			return batteryModelDao.getBatteryFacNames(userId);
		else
			return batteryModelDao.getBatteryFacNamesAdmin();
	}

	@Override
	public Long insertBatteryModel(BatteryModel batteryModel){
		batteryModelDao.save(batteryModel);

		StringBuffer sql = new StringBuffer("SELECT MAX(id) FROM battery_model");
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
}
