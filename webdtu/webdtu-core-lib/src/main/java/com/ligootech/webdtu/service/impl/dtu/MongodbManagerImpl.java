package com.ligootech.webdtu.service.impl.dtu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.entity.core.DtuMongo;
import com.ligootech.webdtu.entity.core.PageDtuMongo;
import com.ligootech.webdtu.repository.MongodbDao;
import com.ligootech.webdtu.service.dtu.MongodbManager;
import com.mongodb.DBObject;
@Service("mongodbManager")
public class MongodbManagerImpl implements MongodbManager {
	
	@Autowired 
	MongodbDao mongodbDao;
	
	@Override
	public void saveDtu(DtuMongo object) {
		mongodbDao.saveDtu(object);
	}

	@Override
	public PageDtuMongo getDtuPage(String uuid,int pageIndex,int pageSize){
		return mongodbDao.getDtuPage(uuid, pageIndex, pageSize);
	}

	@Override
	public List<DBObject> getTracksByUuid(String uuid, String beginDate,
			String endDate) {
		return mongodbDao.getTracksByUuid(uuid, beginDate, endDate);
	}

}
