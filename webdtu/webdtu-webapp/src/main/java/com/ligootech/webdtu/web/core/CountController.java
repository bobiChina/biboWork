package com.ligootech.webdtu.web.core;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ligootech.webdtu.entity.core.Dtu;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ligootech.webdtu.dto.DayAlarmListDto;
import com.ligootech.webdtu.dto.DayOnlineVehicleStatusDto;
import com.ligootech.webdtu.dto.MonthBatteryAlarmDto;
import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.entity.core.Vehicle;
import com.ligootech.webdtu.service.dtu.CountManager;
import com.ligootech.webdtu.service.dtu.DtuManager;
import com.ligootech.webdtu.service.dtu.DtuUserManager;
import com.ligootech.webdtu.service.dtu.MongodbManager;
import com.ligootech.webdtu.service.dtu.MonthCountManager;
import com.ligootech.webdtu.service.dtu.VehicleCountManager;
import com.ligootech.webdtu.service.dtu.VehicleManager;
import com.ligootech.webdtu.web.util.ShiroUtil;

@Controller
@RequestMapping("/count")
public class CountController {

	@Autowired
	MongodbManager mongodbManager;

	@Autowired
	DtuManager dtuManager;
	
	@Autowired
	DtuUserManager dtuUserManager;
	
	@Autowired
	VehicleManager vehicleManager;
	
	@Autowired
	CountManager countManager;
	
	@Autowired
	MonthCountManager monthCountManager;
	
	@Autowired
	VehicleCountManager vehicleCountManager; 
	
   
    
    @RequestMapping(value = "/day/{date}", method = RequestMethod.GET)
    public String dayCount(@PathVariable("date") String date, Model model){
    	String yesterday = getYesterday();
    	
    	int isAdmin = ShiroUtil.getCurrentUser().getIsAdmin();
    	Long userId = ShiroUtil.getCurrentUser().getId();
    	//获取当前用户
    	DtuUser dtuUser = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
    	//今日新增上线车辆
    	//int  newVehicleCount = countManager.getNewVehicleDayCount(isAdmin,userId, date);
    	//今日在线车辆数
    	//int onlineVehicleCount = countManager.onlineVehicleToday(isAdmin,userId, date);
    	//获取车辆总数
    	//int allVehicleCount = countManager.getVehicleCount(isAdmin,userId,date);
    	//离线车辆总数
    	//int offVehicleCount = allVehicleCount - onlineVehicleCount;
    	//今日新增车辆运行里程
    	//int newVehicleMilege = countManager.newVehicleRunningMilege(isAdmin,userId,date);
    	//客户所有车辆的运行总里程
    	//int allMilege = countManager.vehicleRunningMilegeAll(isAdmin,userId,date);
    	//今日新增车辆运行时长
    	//float newVehicleTime = countManager.newVehicleRunningTime(isAdmin,userId, date);
    	//客户所有车辆的运行时长
    	//float allTime = countManager.vehicleRunningTimeAll(isAdmin,userId, date);
    	//今日报警数
    	//int alarmToday = countManager.alarmCountDay(isAdmin,userId,date);
    	//所有车辆报警数
    	//int alarmAll = countManager.alarmCountAll(isAdmin,userId,date);
    	//获取今日新增车辆列表
    	List<Object[]> newVehicleList = countManager.getNewVehicleListToday(isAdmin,userId,date);
    	//获取今日车辆报警列表。
    	List<DayAlarmListDto> alarmListToday = countManager.getAlarmListToday(isAdmin,userId,date);
    	List<DayOnlineVehicleStatusDto> vehicleRunningStatus = countManager.getVehicleRunningStatus(isAdmin,userId, date);

    	model.addAttribute("dtuUser", dtuUser);
    	//model.addAttribute("newVehicleCount", newVehicleCount);
    	//model.addAttribute("allVehicleCount",allVehicleCount );
    	//model.addAttribute("newVehicleMilege", newVehicleMilege);
    	//model.addAttribute("allMilege",allMilege );
    	//model.addAttribute("newVehicleTime", newVehicleTime);
    	//model.addAttribute("allTime", allTime);
    	//model.addAttribute("alarmToday", alarmToday);
    	model.addAttribute("alarmToday", alarmListToday.size());
    	//model.addAttribute("alarmAll", alarmAll);
    	//model.addAttribute("onlineVehicleCount", onlineVehicleCount);
    	//model.addAttribute("offVehicleCount", offVehicleCount);
    	model.addAttribute("newVehicleList", newVehicleList);
    	model.addAttribute("alarmListToday", alarmListToday);
    	model.addAttribute("vehicleRunningStatus", vehicleRunningStatus);
    	model.addAttribute("today", date);
    	return "dtu/dayreport";
    }

	@RequestMapping(value = "/dayReportAjax", method = RequestMethod.POST)
	@ResponseBody
	public String findDayReportAjax(@RequestParam("date") String date){
		int isAdmin = ShiroUtil.getCurrentUser().getIsAdmin();
		Long userId = ShiroUtil.getCurrentUser().getId();
		//获取当前用户
		DtuUser dtuUser = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
		Map<String, String> map = new HashMap<String, String>();
		//今日新增上线车辆
		int  newVehicleCount = countManager.getNewVehicleDayCount(isAdmin, userId, date);
		map.put("newVehicleCount", newVehicleCount + "");
		//今日在线车辆数
		int onlineVehicleCount = countManager.onlineVehicleToday(isAdmin, userId, date);
		map.put("onlineVehicleCount", onlineVehicleCount + "");
		//获取车辆总数
		int allVehicleCount = countManager.getVehicleCount(isAdmin, userId, date);
		map.put("allVehicleCount",allVehicleCount + "");
		//离线车辆总数
		int offVehicleCount = allVehicleCount - onlineVehicleCount;
		map.put("offVehicleCount", offVehicleCount + "");

		DecimalFormat fnum = new DecimalFormat("###,###,###,###.0");
		//今日新增车辆运行里程
		float newVehicleMilege = countManager.newVehicleRunningMilege(isAdmin,userId,date);
		if (newVehicleMilege == 0){
			map.put("newVehicleMilege", "0");
		}else{
			map.put("newVehicleMilege", fnum.format(newVehicleMilege));
		}
		//客户所有车辆的运行总里程
		float allMilege = countManager.vehicleRunningMilegeAll(isAdmin, userId, date);
		map.put("allMilege", fnum.format(allMilege));
		//今日新增车辆运行时长
		float newVehicleTime = countManager.newVehicleRunningTime(isAdmin,userId, date);
		if (newVehicleTime == 0){
			map.put("newVehicleTime", "0");
		}else{
			map.put("newVehicleTime", fnum.format(newVehicleTime) );
		}
		//客户所有车辆的运行时长
		float allTime = countManager.vehicleRunningTimeAll(isAdmin,userId, date);
		map.put("allTime", fnum.format(allTime) );
		//今日报警数
		int alarmToday = countManager.alarmCountDay(isAdmin,userId,date);
		//所有车辆报警数
		int alarmAll = countManager.alarmCountAll(isAdmin, userId, date);
		map.put("alarmAll", alarmAll + "");

		return JSONObject.fromObject(map).toString();
	}

	@RequestMapping(value = "/montherReportAjax", method = RequestMethod.POST)
	@ResponseBody
	public String findMontherReportAjax(@RequestParam("date") String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date month;
		try {
			month = sdf.parse(date + "-01");
		} catch (ParseException e) {
			e.printStackTrace();
			return "-1";
		}

		String firstDay = getFirstDay(month);
		String lastDay = getLastDay(month);
		DecimalFormat fnum = new DecimalFormat("###,###,###,###.0");

		int isAdmin = ShiroUtil.getCurrentUser().getIsAdmin();
		Long userId = ShiroUtil.getCurrentUser().getId();
		//获取当前用户
		DtuUser dtuUser = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
		Map<String, String> map = new HashMap<String, String>();

		//本月新增上线车辆
		int newVehicleCount = monthCountManager.getNewVehicleMonthCount(isAdmin,userId,firstDay,lastDay);
		map.put("newVehicleCount", newVehicleCount + "");
		//获取车辆总数
		int allVehicleCount = countManager.getVehicleCount(isAdmin,userId,lastDay);
		map.put("allVehicleCount", allVehicleCount + "");
		//本月报警次数
		int alarmMonth = monthCountManager.alarmCountMonth(isAdmin,userId,firstDay,lastDay);
		map.put("alarmMonth", alarmMonth + "");
		//所有车辆报警数
		int alarmAll = countManager.alarmCountAll(isAdmin,userId,lastDay);
		map.put("alarmAll", alarmAll + "");
		//本月新增车辆运行时长
		float newVehicleTime = monthCountManager.newVehicleRunningTime(isAdmin,userId,firstDay,lastDay);
		if (newVehicleTime == 0){
			map.put("newVehicleTime", "0");
		}else{
			map.put("newVehicleTime", fnum.format(newVehicleTime));
		}
		//客户所有车辆的运行时长
		float allTime = countManager.vehicleRunningTimeAll(isAdmin,userId,lastDay);
		if (allTime == 0){
			map.put("allTime", "0");
		}else{
			map.put("allTime", fnum.format(allTime));
		}

		//最大单车运行时长
		float maxTime = countManager.maxRunningTimeAll(isAdmin, userId,firstDay,lastDay);
		if (maxTime == 0){
			map.put("maxTime", "0");
		}else{
			map.put("maxTime", fnum.format(maxTime));
		}

		return JSONObject.fromObject(map).toString();
	}

    
    @RequestMapping(value = "/month/{date}", method = RequestMethod.GET)
    public String monthCount(@PathVariable("date") String date,Model model){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date month;
		try {
			month = sdf.parse(date + "-01");
		} catch (ParseException e) {
			e.printStackTrace();
			return "dtu/dayreport";
		}
		
		
    	String firstDay = getFirstDay(month);
    	String lastDay = getLastDay(month);
    	Date firstDay_d=null;
    	Date lastDay_d=null;
    	try {
			firstDay_d = sdf.parse(firstDay);
			lastDay_d = sdf.parse(lastDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	
//    	Calendar cal = Calendar.getInstance();
//	    GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
//	    Calendar calendar1 = Calendar.getInstance();
//	    calendar1.setTime(new Date());
//
//	    calendar1.add(Calendar.MONTH, -1);
//	    Date theDate = calendar1.getTime();
//	    gcLast.setTime(theDate);
//	    gcLast.set(Calendar.DAY_OF_MONTH, 1);
//	    
//	    Date beginDate = gcLast.getTime(); 
//    	
//    	Calendar calendar = Calendar.getInstance();  
//    	int month = calendar.get(Calendar.MONTH);
//    	calendar.set(Calendar.MONTH, month-1);
//    	calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
//    	Date endDate = calendar.getTime();  
//    	
    	
    	
    	int isAdmin = ShiroUtil.getCurrentUser().getIsAdmin();
    	Long userId = ShiroUtil.getCurrentUser().getId();
    	//获取当前用户
    	DtuUser dtuUser = dtuUserManager.get(userId);
    	//本月新增上线车辆
    	//int newVehicleCount = monthCountManager.getNewVehicleMonthCount(isAdmin,userId,firstDay,lastDay);
    	
    	List<Vehicle> newVehicleList = vehicleManager.getNewVehiclesMonth(isAdmin,userId, firstDay_d,lastDay_d);
    	//获取车辆总数
    	//int allVehicleCount = countManager.getVehicleCount(isAdmin,userId,lastDay);
    	//本月新增运行里程
    	//int newVehicleMilege = monthCountManager.newVehicleRunningMilege(isAdmin,userId,firstDay,lastDay);
    	//客户所有车辆的运行总里程
    	//float allMilege = countManager.vehicleRunningMilegeAll(isAdmin,userId,lastDay);
    	//本月新增车辆运行时长
    	//float newVehicleTime = monthCountManager.newVehicleRunningTime(isAdmin,userId,firstDay,lastDay);
    	//客户所有车辆的运行时长
    	//float allTime = countManager.vehicleRunningTimeAll(isAdmin,userId,lastDay);
    	//最大单车运行时长
    	//float maxTime = countManager.maxRunningTimeAll(isAdmin, userId,firstDay,lastDay);
    	//本月报警次数
    	//int alarmMonth = monthCountManager.alarmCountMonth(isAdmin,userId,firstDay,lastDay);
    	//所有车辆报警数
    	//int alarmAll = countManager.alarmCountAll(isAdmin,userId,lastDay);
    	//本月生产车辆类型统计
    	String vehicleType = monthCountManager.vehicleTypeMonthCount(isAdmin,userId,firstDay,lastDay);
    	//本月生产车辆电池型号统计
    	String batteryModel = monthCountManager.batteryModelMonthCount(isAdmin,userId,firstDay,lastDay);
    	//电池厂商故障分类统计
    	List<MonthBatteryAlarmDto> batteryAlarmCount = monthCountManager.batteryAlarmCount(isAdmin,userId, firstDay, lastDay);
    	//本月故障柱状图
    	List<String> alarmCountMonthByType = monthCountManager.alarmCountMonthByType(isAdmin,userId, firstDay, lastDay);
    	/*List vehicleRunningMonthCount = monthCountManager.vehicleRunningMonthCount(isAdmin,userId, firstDay,lastDay);
    	for(Object item:vehicleRunningMonthCount){
    		Object[] timeLengh = (Object[])item;
    		timeLengh[2] = ((BigDecimal)timeLengh[2]).floatValue();
    	} 页面无显示 wly */
    	
    	JSONArray batteryAlarmCountJsonResult = new JSONArray();
    	for(MonthBatteryAlarmDto item:batteryAlarmCount){
    		JSONObject obj = new JSONObject();
    		obj.put("name", item.getBatteryName());
    		int[] alarmdata = new int[14];
    		alarmdata[0] = item.getLeakElec();
    		alarmdata[1] = item.getCommuniStatus();
    		alarmdata[2] = item.getOutTemper();
    		alarmdata[3] = item.getOutRealease();
    		alarmdata[4] = item.getOutCharge();
    		alarmdata[5] = item.getSocLow();
    		alarmdata[6] = item.getSocHigh();
    		alarmdata[7] = item.getOutStream();
    		alarmdata[8] = item.getTemperDiffMax();
    		alarmdata[9] = item.getVoltageDiffMax();
    		alarmdata[10] = item.getVoltageCheckExeption();
    		alarmdata[11] = item.getTemperCheckExeption();
    		alarmdata[12] = item.getTotalVoltageHigh();
    		alarmdata[13] = item.getTotalVoltageLow();
    		obj.put("data", alarmdata);
    		batteryAlarmCountJsonResult.add(obj);
    	}
    	
    	model.addAttribute("dtuUser", dtuUser);
    	//model.addAttribute("newVehicleCount", newVehicleCount);
    	model.addAttribute("newVehicleList", newVehicleList);
    	//model.addAttribute("allVehicleCount", allVehicleCount);
    	//model.addAttribute("newVehicleMilege", newVehicleMilege);

		/*DecimalFormat fnum = new DecimalFormat("###,###,###,###.0");
		if (allMilege == 0){
			model.addAttribute("allMilege", "0");
		}else{
			model.addAttribute("allMilege", fnum.format(allMilege));
		}*/
    	//model.addAttribute("newVehicleTime", newVehicleTime);
    	//model.addAttribute("allTime", allTime);
    	//model.addAttribute("maxTime", maxTime);
    	//model.addAttribute("alarmMonth", alarmMonth);
    	//model.addAttribute("alarmAll", alarmAll);
    	model.addAttribute("vehicleType", vehicleType);
    	model.addAttribute("batteryModel",batteryModel );
    	model.addAttribute("batteryAlarmCount", JSONArray.fromObject(batteryAlarmCountJsonResult).toString());
    	//model.addAttribute("vehicleRunningMonthCount", vehicleRunningMonthCount);
    	model.addAttribute("alarmCountMonthByType", alarmCountMonthByType);
    	model.addAttribute("lastmonth", date);
    	return "dtu/monthreport";
    }
    
    @RequestMapping(value = "/vehicle/{dtuId}", method = RequestMethod.GET)
    public String vehicleCount(@PathVariable(value = "dtuId") Long dtuId,Model model){
		Dtu dtu = dtuManager.get(dtuId);//共用一个DTU对象 wly 2016年2月15日16:57:02
		String uuid = dtu.getUuid();
    	//获取公司，登录用户，车辆基本信息。
    	Vehicle vehicle = vehicleManager.getVehicleByUuid(uuid);
    	//车辆总运行里程
    	float totalRunningMilege = vehicleCountManager.totalRunningMilege(dtuId);
    	//车辆总运行时长
    	float totalRunningTime = vehicleCountManager.totalRunningTime(dtuId);
    	//车辆故障总数
		int totalAlarmCount = vehicleCountManager.totalAlarmCount(dtuId);
    	//运行状态统计
		String onlineStatus = vehicleCountManager.onlineStatus(dtuId);
    	//总容量变化曲线
    	String totalCapacityCurve = vehicleCountManager.totalCapacityCurve(dtuId);
    	//历史故障表
		List alarmHistory = vehicleCountManager.getAlarmHistory(dtuId);
    	//历史充电表
    	List vehicleChargeList = vehicleCountManager.vehicleChargeCycleList(dtuId);
    	//故障类型分布饼图
    	String alarmTypeCount = vehicleCountManager.batteryAlarmCount(dtuId);
    	
    	for(Object item:vehicleChargeList){
    		Object[] timeLengh = (Object[])item;
    		timeLengh[2] = ((BigInteger) timeLengh[2]).floatValue();
		}
		model.addAttribute("vehicle", vehicle);
    	model.addAttribute("totalRunningMilege", totalRunningMilege);
		model.addAttribute("totalRunningTime", totalRunningTime);
    	model.addAttribute("totalAlarmCount", totalAlarmCount);
    	model.addAttribute("onlineStatus", onlineStatus);
    	model.addAttribute("totalCapacityCurve", totalCapacityCurve);
    	String[] totalCapacityCurveXaxis = totalCapacityCurve.split(",");
		StringBuffer sb = new StringBuffer();
		sb.append("[");
    	for (int i = 0;i<totalCapacityCurveXaxis.length;i++){
			sb.append("''");
			if(i!=(totalCapacityCurveXaxis.length-1))
				sb.append(",");
    	}
    	sb.append("]");
    	model.addAttribute("totalCapacityCurveXaxis", sb.toString());
    	model.addAttribute("alarmHistory", alarmHistory);
		model.addAttribute("vehicleChargeList", vehicleChargeList);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format( new Date());
		model.addAttribute("today", today);
		model.addAttribute("alarmTypeCount", alarmTypeCount);

		//获取新的SIM卡号 2016年2月15日16:57:46
		model.addAttribute("sim", dtuManager.getSIMByUUID(uuid));

    	return "dtu/vehiclereport";
    }
    
    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public String forwardReports(Model model){
    	model.addAttribute("yesterday", getYesterday());
    	model.addAttribute("lastMonth", getLastMonth());
    	return "dtu/reports";
    }
    
    public String getYesterday(){
    	Calendar   cal   =   Calendar.getInstance();
    	cal.add(Calendar.DATE,   -1);
    	String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
    	return yesterday;
    }
    
    public String getLastMonth(){
    	Calendar   cal   =   Calendar.getInstance();
    	cal.add(Calendar.MONTH,  -1);
    	String lastMonth = new SimpleDateFormat( "yyyy-MM").format(cal.getTime());
    	return lastMonth;
    }
    
    private static String getFirstDay(Date date) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();
        
        //第一天
        calendar.setTime(theDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(calendar.getTime());
        return day_first;
    }
    
    private static String getLastDay(Date date) {
    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         Date theDate = calendar.getTime();
         
         calendar.setTime(theDate);
         calendar.add(Calendar.MONTH, 1);    //加一个月
         calendar.set(Calendar.DATE, 1);        //设置为该月第一天
         calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
         String day_last = df.format(calendar.getTime());
         return day_last;
    }    
}
