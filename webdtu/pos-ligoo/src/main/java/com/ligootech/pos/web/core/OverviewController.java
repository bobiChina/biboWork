package com.ligootech.pos.web.core;

import java.io.IOException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ligootech.webdtu.entity.core.Dtu;
import com.ligootech.webdtu.service.dtu.ChargeCycleManager;
import com.ligootech.webdtu.service.dtu.CountManager;
import com.ligootech.webdtu.service.dtu.DtuManager;
import com.ligootech.webdtu.service.dtu.VehicleCountManager;
import com.ligootech.webdtu.service.dtu.VehicleManager;
import com.ligootech.pos.web.util.ShiroUtil;

/**
 * 概览页
 * @author Administrator
 *
 */

@Controller
@RequestMapping("/overview")
public class OverviewController {
	
	@Autowired
	VehicleManager vehicleManager;
	@Autowired
	CountManager countManager;
	@Autowired
	ChargeCycleManager chargeCycleManager;
	@Autowired
	DtuManager dtuManager;
	@Autowired
	VehicleCountManager vehicleCountManager;
	/**
	 * 获取当前用户的5个统计图形
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public String overview(Model model){
    	int isAdmin = ShiroUtil.getCurrentUser().getIsAdmin();
    	Long userId = ShiroUtil.getCurrentUser().getId();
    	
    	int allVehicleCount = countManager.getVehicleCount(isAdmin,userId,"2099-12-12");
    	List allMilegeAndTimeLength = chargeCycleManager.getAllMilegeAndTimeLength(isAdmin, userId);
    	Float maxRunningTime = countManager.getMaxRunningTime(isAdmin, userId);
    	Integer totalCapacity = dtuManager.getTotalCapacity(isAdmin, userId);
    	
    	
		List<Object[]> result = vehicleManager.vehicleTypeCount(isAdmin,userId);
		List<Object[]> result1 = vehicleManager.batteryFactoryCount(isAdmin,userId);
		List<Object[]> result2 = vehicleManager.onlineStateCount(isAdmin,userId);
//		StringBuffer vTypeStr = new StringBuffer("");
//		for(int i = 0;i<result.size();i++){
//			Object[] item = result.get(i);
//			vTypeStr.append("['").append(item[1]).append("',").append(item[0]).append("]");
//			if ((result.size()-1) != i)
//				vTypeStr.append(",");
//		}
		
		StringBuffer vTypeStr = new StringBuffer();
		vTypeStr.append("[");
		for(int i=0;i<result.size();i++){
			Object[] item = result.get(i);
			vTypeStr.append("{value:"+item[0]+",");
			vTypeStr.append("name:'"+item[1]+"'}");
			if ((result.size()-1) != i)
				vTypeStr.append(",");
		}
		vTypeStr.append("]");
//		StringBuffer bFacStr = new StringBuffer("");
//		for(int i = 0;i<result1.size();i++){
//			Object[] item = result1.get(i);
//			bFacStr.append("['").append(item[1]).append("',").append(item[0]).append("]");
//			if ((result1.size()-1) != i)
//				bFacStr.append(",");
//		}
		
		
		StringBuffer bFacStr = new StringBuffer();
		bFacStr.append("[");
		for(int i=0;i<result1.size();i++){
			Object[] item = result1.get(i);
			bFacStr.append("{value:"+item[1]+",");
			bFacStr.append("name:'"+item[0]+"'}");
			if ((result1.size()-1) != i)
				bFacStr.append(",");
		}
		bFacStr.append("]");		
		
		StringBuffer oState = new StringBuffer("");
		oState.append("[");
		for(int i = 0;i<result2.size();i++){
			Object[] item = result2.get(i);
			if ((Integer)item[1] == 1){
				oState.append("{value:"+item[0]+",");
				oState.append("name:'充电'}");
			}
			else if((Integer)item[1] == 0){
				oState.append("{value:"+item[0]+",");
				oState.append("name:'放电'}");
			}else{
				oState.append("{value:"+item[0]+",");
				oState.append("name:'离线'}");	
			}
			
			if ((result2.size()-1) != i)
				oState.append(",");
		}
		oState.append("]");		
		List overviewQuot = vehicleCountManager.overviewQuot(isAdmin, userId);
//		StringBuffer sb = new StringBuffer();
//		sb.append("[");
//		for(int i=0;i<result1.size();i++){
//			Object[] item = result.get(i);
//			sb.append("{value:"+item[1]+",");
//			sb.append("name:'"+item[0]+"'}");
//			if ((result.size()-1) != i)
//				sb.append(",");
//		}
//		sb.append("]");	
		
		model.addAttribute("vehicleType",vTypeStr);
		model.addAttribute("batteryFac", bFacStr);
		model.addAttribute("onlineStatus", oState);
		
		model.addAttribute("allVehicleCount", allVehicleCount);
		model.addAttribute("allMilegeAndTimeLength", allMilegeAndTimeLength.get(0));
		model.addAttribute("maxRunningTime", maxRunningTime);
		model.addAttribute("totalCapacity", totalCapacity);
		model.addAttribute("overviewQuot", overviewQuot);
		return "dtu/overview";
	}
	
	@RequestMapping(value = "/geo", method = RequestMethod.GET)
	public String geotest(Model model){
        List<Dtu> result = dtuManager.getAll();
        for (int i = 0; i < result.size(); i++) {
			Dtu dtu = result.get(i);
			Float lon = dtu.getLontitude();
			float lat = dtu.getLatitude();
			String city;
			if ((lon==0)&&(lat==0)){
				city="未知";
			}else{
				JSONObject jobj = JSONObject.fromObject(geocoder(dtu.getLatitude()+","+dtu.getLontitude()));
				city = JSONObject.fromObject(JSONObject.fromObject(jobj.get("result").toString()).get("addressComponent").toString()).get("city").toString();
			}
			dtu.setCity(city);
			dtuManager.save(dtu);
		}		
		return "";
	}
    public String geocoder(String loc){
        String uriAPI = "http://api.map.baidu.com/geocoder/v2/?ak=Uvehft4QYLK1W5KbRDzMY7q2&location="+loc+"&output=json&pois=0";
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
