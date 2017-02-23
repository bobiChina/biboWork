package com.ligootech.webdtu.web.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ligootech.webdtu.entity.core.*;
import com.ligootech.webdtu.repository.OptLogDao;
import com.ligootech.webdtu.service.dtu.*;
import com.ligootech.webdtu.web.util.ShiroUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ligootech.webdtu.web.util.DateEditor;

/**
 * 整车页，车辆相关
 * @author Administrator
 *
 */
@Transactional
@Controller
@RequestMapping("/vehicle")
public class VehicleController {

	@Autowired
	private  HttpServletRequest request;

	@Autowired
	VehicleManager vehicleManager;

	@Autowired
	VehicleTypeManager vehicleTypeManager;
	
	@Autowired
	VehicleModelManager vehicleModelManager;
	
	@Autowired
	DtuManager dtuManager;

	@Autowired
	DtuUserManager dtuUserManager;

	@Autowired
	BatteryModelManager batteryModelManager;

	@Autowired
	OptLogDao optLogDao;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String forwardVehicleList(){
		return "";
	}
	
	@RequestMapping(value = "/listQuery", method = RequestMethod.POST)
	@ResponseBody
	public String vehicleList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "uuid") String uuid){
		return "";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String forwardVehicleCreate(){
		return "";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String vehicleEdit(@PathVariable(value = "id") int id){
		return "";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
public String vehicleCreate(Vehicle vehicle,Model model, @RequestParam(value = "factoryName") String factoryName,
								@RequestParam(value = "batteryType") int batteryType,
								@RequestParam(value = "batteryNumber") int batteryNumber,
								@RequestParam(value = "capacity") int capacity,
								@RequestParam(value = "batteryName") String batteryName,
								@RequestParam(value = "batteryId") Long batteryId){

		if((null != factoryName && !"".equals(factoryName.trim()))|| batteryId>0 ){//电池厂商不为空
			//封装电池信息
			BatteryModel batteryModel = new BatteryModel();
			BatteryModel oldBM = null;
			if (batteryId > 0){
				batteryModel.setId(batteryId);
				oldBM = batteryModelManager.get(batteryId);
			}
			batteryModel.setBatteryName(batteryName);
			batteryModel.setBatteryNumber(batteryNumber);
			BatteryType bt = new BatteryType();
			bt.setId(batteryType);
			batteryModel.setBatteryType(bt);
			batteryModel.setCapacity(capacity);
			batteryModel.setFactoryName(factoryName.trim());

			batteryModelManager.save(batteryModel);
			//添加日志记录
			if(null != oldBM){
				StringBuffer logStr = new StringBuffer("原电池信息：");
				logStr.append("id=").append(batteryId).append("  ");
				logStr.append("batteryName=").append(oldBM.getBatteryName()).append("  ");
				logStr.append("batteryNumber=").append(oldBM.getBatteryNumber()).append("  ");
				logStr.append("batteryType=").append(oldBM.getBatteryType().getId()).append("  ");
				logStr.append("capacity=").append(oldBM.getCapacity()).append("  ");
				logStr.append("factoryName=").append(oldBM.getFactoryName());
				int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "BatteryModel",logStr.toString() );
			}
		}

		if (vehicle.getId() == null)
			vehicle.setCreateTime(new Date());

		//添加车辆型号，并返回相应的ID值
		if (-9999 == vehicle.getVehicleModel().getId()){
			Long vechId = vehicleModelManager.insertVehicleModel(vehicle.getVehicleType().getId(), vehicle.getVehicleModel().getModelName());
			vehicle.getVehicleModel().setId(vechId);
			vehicle.getVehicleModel().setVehicleType(vehicle.getVehicleType());
		}

		//保存设备信息
		vehicleManager.save(vehicle);
		//删除未关联的车辆型号
		//int delRs = vehicleModelManager.delVehicleModel();

		//System.out.println(" BatteryModel batteryModel=" + batteryId + "---" + batteryModel.getBatteryName());
		//修改/添加电池信息
		/*if (null == batteryId || 0 == batteryId){

		}*/
		//batteryModel.setId(batteryId);
		//batteryModelManager.save(batteryModel);

		return "1";
	}

	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String vehicleDelete(@RequestParam(value = "id") Integer id){
		return "";
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String vehicleGetAjax(@RequestParam(value = "id") Long id){
		Vehicle vehicle = vehicleManager.get(id);
		JSONObject obj = JSONObject.fromObject(vehicle);

		return obj.toString();
	}
	
	@RequestMapping(value = "/getuuid", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String vehicleGetByUuidAjax(@RequestParam(value = "dtuId") Long dtuId,@RequestParam(value = "timestamp") Long timestamp){
		Dtu dtu = dtuManager.get(dtuId);
		Vehicle vehicle = vehicleManager.getVehicleByUuid(dtu.getUuid());
		JSONObject d = JSONObject.fromObject(dtu);
		JSONArray jsonResult = new JSONArray();
		jsonResult.add(d);

		Long upUserId = 1l;
		upUserId = dtuUserManager.getUpUserId(ShiroUtil.getCurrentUser().getId(),dtuId);
		if (upUserId == null) {
			upUserId = 1l;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		DtuUser dtuUser = dtuUserManager.get(upUserId);
		map.put("dtuuser", dtuUser);

		//sn码
		String snStr = dtuManager.getSNByUUID(dtu.getUuid());
		map.put("sn", snStr);

		JSONObject otherInfo = JSONObject.fromObject(map);
		jsonResult.add(otherInfo);

		if (vehicle != null){
			JSONObject v = JSONObject.fromObject(vehicle);
			jsonResult.add(v);
		}
		return jsonResult.toString();
	}
	
	@RequestMapping(value = "/vehicleModel", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String vehicleModel(@RequestParam(value = "typeId") Long typeId, @RequestParam(value = "pagetype") String pagetype){
		int isAdmin = ShiroUtil.getCurrentUser().getIsAdmin();//判断是否为admin admin用户 可看所有类型
		if ("order".equals(pagetype)){//在订单页面时取用的是所有车辆型号，订单录入人不具有DTU信息
			isAdmin = 1;
		}
		List<Object[]> result = vehicleModelManager.getVehicleModelByTypeId(typeId, isAdmin, ShiroUtil.getCurrentUser().getId());
		JSONArray arr = JSONArray.fromObject(result);
		return arr.toString();
	}
	
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理  
        binder.registerCustomEditor(Date.class, new DateEditor());

    }	
}
