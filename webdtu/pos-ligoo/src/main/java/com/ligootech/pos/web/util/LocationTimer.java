package com.ligootech.pos.web.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ligootech.webdtu.entity.core.Dtu;
import com.ligootech.webdtu.service.dtu.DtuManager;

/*
屏蔽定时任务
@Component
@Lazy(false)
*/
public class LocationTimer{
	@Autowired
	DtuManager dtuManager;
	
    @Scheduled(cron = "0 0 10,16 * * ?")//"0 0 2 * * ?" 每晚2点触发
    public void exejob() {
        System.out.println(new Date() + " ：任务执行中");
        List<Dtu> result = dtuManager.getAll();
        for (int i = 0; i < result.size(); i++) {
			Dtu dtu = result.get(i);
			Float lon = dtu.getLontitude();
			float lat = dtu.getLatitude();
			String city;
			if ((lon==0)&&(lat==0)){
				city="未知";
				//continue;
			}else{
				JSONObject jobj = JSONObject.fromObject(geocoder(dtu.getLatitude()+","+dtu.getLontitude()));
				city = JSONObject.fromObject(JSONObject.fromObject(jobj.get("result").toString()).get("addressComponent").toString()).get("city").toString();
			}
			dtu.setCity(city);
			dtuManager.save(dtu);
		}
        
    } 
    
    public String geocoder(String loc){
        String uriAPI = "http://api.map.baidu.com/geocoder/v2/?ak=CF9649ad8e4abb469672be973645361c&location="+loc+"&output=json&pois=0";
        HttpParams httpParameters = new BasicHttpParams(); 
        int timeoutConnection = 30000; 
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection); 
        int timeoutSocket = 30000; 
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);       
        HttpGet httpRequest = new HttpGet(uriAPI); 
        HttpResponse httpResponse;
                try {
                        httpResponse = new DefaultHttpClient(httpParameters).execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode() == 200)  
                { 
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    return strResult;
                }                       
                } catch (ClientProtocolException e) {
                        e.printStackTrace();
                        return "[]";
                } catch (IOException e) {
                        e.printStackTrace();
                        return "[]";
                } 
                return "[]";            
    }
}
