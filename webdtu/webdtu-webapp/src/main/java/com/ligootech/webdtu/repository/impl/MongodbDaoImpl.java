package com.ligootech.webdtu.repository.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.ligootech.webdtu.repository.MongodbDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ligootech.webdtu.entity.core.DtuMongo;
import com.ligootech.webdtu.entity.core.PageDtuMongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
@Repository("mongodbDao")
public class MongodbDaoImpl implements MongodbDao {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void saveDtu(DtuMongo object){
		mongoTemplate.insert(object);
	}

	@Override
	public PageDtuMongo getDtuPage(String uuid,int pageIndex,int pageSize) {
		Query query = new Query(Criteria.where("uuid").is(uuid));
		
		
		long count = mongoTemplate.count(query, DtuMongo.class);
		long pageCount = count/pageSize;
		if (pageCount%pageSize >0)
			pageCount += 1;
		query.skip((pageIndex-1)*pageSize);
		query.limit(pageSize);
		query.with(new Sort(new Order(Direction.DESC, "_id")));  
		List<DtuMongo> result = mongoTemplate.find(query, DtuMongo.class);
		PageDtuMongo pdm = new PageDtuMongo();
		if (pageCount ==0)
			pageCount +=1;
		pdm.setPageCount(pageCount);
		pdm.setPageIndex(pageIndex);
		pdm.setList(result);
		return pdm;
	}

	public static void main(String [] args){
		long count = 1603;
		long pageSize = 20;
		long pageCount = count/20;
		long pageCount2 = count%20;
		System.out.print("");
	}

	@Override
	public List<DBObject> getTracksByUuid(String uuid,String beginDate,String endDate) {
		DBCollection dbc = mongoTemplate.getCollection("dtuMongo");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DBObject query = new BasicDBObject();//查询条件
		try {
			query.put("uuid", uuid);
			query.put("insertTime", new BasicDBObject("$gt",sdf.parse(beginDate)).append("$lt", sdf.parse(endDate)));
			query.put("lontitude", new BasicDBObject("$gt",0));
			query.put("latitude", new BasicDBObject("$gt",0));
			//query.put("insertTime", new BasicDBObject("$lt",sdf.parse(endDate)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		DBObject field = new BasicDBObject();//要查的哪些字段 
        field.put("lontitude", true); 
        field.put("latitude", true); 
        field.put("insertTime", true); 
        List<DBObject> result = dbc.find(query,field).sort(new BasicDBObject("_id", 1)).toArray();
        
		return result;
	}

}
