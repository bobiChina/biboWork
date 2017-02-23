package com.ligootech.webdtu.service.impl.dtu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ligootech.webdtu.repository.VehicleCountDao;
import com.ligootech.webdtu.service.dtu.VehicleCountManager;
import com.ligootech.webdtu.util.AlarmUtil;
@Service("vehicleCountManager")
public class VehicleCountManagerImpl implements VehicleCountManager {
	@Autowired
	VehicleCountDao vehicleCountDao;

	@Override
	public float totalRunningMilege(Long dtuId) {
		return vehicleCountDao.totalRunningMilege(dtuId);
	}

	@Override
	public float totalRunningTime(Long dtuId) {
		return vehicleCountDao.totalRunningTime(dtuId);
	}

	@Override
	public int totalAlarmCount(Long dtuId) {
		return vehicleCountDao.totalAlarmCount(dtuId);
	}

	@Override
	public String onlineStatus(Long dtuId) {
		return vehicleCountDao.onlineStatus(dtuId);
	}

	@Override
	public String totalCapacityCurve(Long dtuId) {
		List result = vehicleCountDao.totalCapacityCurve(dtuId);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0;i<result.size();i++){
			float totalCapacity =(float)result.get(i);
			sb.append(totalCapacity);
			if(i!=(result.size()-1))
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public List getAlarmHistory(Long dtuId) {
		List<Object[]> result = vehicleCountDao.getAlarmHistory(dtuId);
		for (Object[] item:result){
			item[0] = AlarmUtil.getAlarmTypeName((Integer)item[0]);
		}
		return result;
	}

	@Override
	public List vehicleChargeCycleList(Long dtuId) {
		return vehicleCountDao.vehicleChargeCycleList(dtuId);
	}

	@Override
	public String batteryAlarmCount(Long dtuId){
		List<Object[]> result = (List<Object[]>)vehicleCountDao.batteryAlarmCount(dtuId);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0;i<result.size();i++){
			Object[] item = result.get(i);
			sb.append("{value:"+item[1]+",");
			sb.append("name:'"+item[0]+"'}");
			if ((result.size()-1) != i)
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public List overviewQuot(int isAdmin, Long userId) {
		List<Double> result = (List<Double>)vehicleCountDao.overviewQuot(isAdmin, userId);//[2.0, 4.9]
		
		Double oilQuot = 0.0;
		Double elecQuot = 0.0;
		oilQuot += result.get(0) * 2.4;
		oilQuot += result.get(1) * 0.8;
		oilQuot += result.get(2) * 0.6;
		oilQuot += result.get(3) * 1.2;
		elecQuot += result.get(0) * 0.6;
		elecQuot += result.get(1) * 0.2;
		elecQuot += result.get(2) * 0.15;
		elecQuot += result.get(3) * 0.3;
		DecimalFormat df = new DecimalFormat("#.00");
		String powerString = "["+df.format(oilQuot/10000)+","+df.format(elecQuot/10000)+"]";
		
		float oilCo2=0;
		float elecCo2=0;
		oilCo2 += result.get(0)*1/1000;
		oilCo2 += result.get(1)*0.4/1000;
		oilCo2 += result.get(2)*0.25/1000;
		oilCo2 += result.get(3)*0.5/1000;
		String co2String = "["+df.format(oilCo2)+","+elecCo2+"]";

		//String treeString = "["+elecCo2+","+df.format(oilCo2*2.8)+"]"; 调换位置 wly 2015年9月11日15:53:12
		String treeString = "["+df.format(oilCo2*2.8)+","+ elecCo2+"]";
		
		StringBuffer sb = new StringBuffer();
		List<String> quotResult = new ArrayList<String>();
		quotResult.add(powerString);
		quotResult.add(co2String);
		quotResult.add(treeString);
		return quotResult;
	}
	
}
