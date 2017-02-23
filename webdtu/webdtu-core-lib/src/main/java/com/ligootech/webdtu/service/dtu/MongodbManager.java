package com.ligootech.webdtu.service.dtu;

import java.util.List;

import com.ligootech.webdtu.entity.core.DtuMongo;
import com.ligootech.webdtu.entity.core.PageDtuMongo;
import com.mongodb.DBObject;

public interface MongodbManager {
	public void saveDtu(DtuMongo object);
	
	public PageDtuMongo getDtuPage(String uuid,int pageIndex,int pageSize);

	public List<DBObject>  getTracksByUuid(String uuid,String beginDate,String endDate);
}
