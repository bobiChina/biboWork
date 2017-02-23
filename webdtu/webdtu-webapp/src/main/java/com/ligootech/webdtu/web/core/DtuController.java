package com.ligootech.webdtu.web.core;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.ligootech.webdtu.entity.core.*;
import com.ligootech.webdtu.repository.OptLogDao;
import com.ligootech.webdtu.service.dtu.*;
import net.sf.json.JSONArray;
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
import com.ligootech.webdtu.web.util.ShiroUtil;
import com.mongodb.DBObject;

/**
 * dtu列表和详情页，包含地图位置，报警信息
 * @author Administrator
 *
 */
@SuppressWarnings("ALL")
@Transactional
@Controller
@RequestMapping("/dtu")
public class DtuController {
	
	public static final int PAGE_SIZE = 20;

	@Autowired
	private  HttpServletRequest request;

	@Autowired
	DtuManager dtuManager;
	
	@Autowired
	VehicleTypeManager vehicleTypeManager;

	@Autowired
	VehicleModelManager vehicleModelManager; 
	
	@Autowired
	BatteryModelManager batteryModelManager;

	@Autowired
	BatteryTypeManager batteryTypeManager;
	
	@Autowired
	BmuManager bmuManager;
	
	@Autowired
	DtuUserManager dtuUserManager;
	
	@Autowired
	MongodbManager mongodbManager;
	
	@Autowired
	VehicleManager vehicleManager;

	@Autowired
	CorpManager corpManager;

	@Autowired
	OptLogDao optLogDao;

    @RequestMapping(value = "/insertdtu", method = RequestMethod.GET)
    public String  insertdtu(Model model){
    	String [] dtus = {"01010814", "03010814", "14010814", "00130913", "01110714", "00070814", "23121213", "03291013", "13120914", "16121213", "33120914", "13010814", "10010814", "08010814", "02010814", "06010814", "28120914", "21120914", "27120914", "32120914", "37121213", "04291013", "23120914", "25120914", "15120914", "01280714", "05010814", "30120914", "18121213", "19121213", "01100614", "08120914", "07120914", "01300414", "31120914", "04120914", "00291013", "02120914", "11120914", "10120914", "29120914", "24120914", "19120914", "20120914", "29121213", "01120914", "36121213", "38121213", "18120914", "16120914", "05120914", "17120914", "14120914", "00120914", "06120914", "06121213", "10121213", "32121213", "31121213", "21121213", "26120914"};
    	System.out.println(dtus.length);
    	for(int i=0;i<dtus.length;i++){
    		String uuid = dtus[i];
    		Dtu dtu = new Dtu();
    		dtu.setUuid(uuid);
    		DtuUser user = new DtuUser();
    		user.setId(3l);
    		dtu.setDtuUser(user);
    		BatteryModel batteryModel = new BatteryModel();
    		batteryModel.setId(1l);
    		dtu.setBatteryModel(batteryModel);
    		dtu.setCity("未知");
    		dtuManager.save(dtu);
    	}
    	return "";
    }
    
	/**
	 * 获取dtu详情（bcu）
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/get/{uuid}", method = RequestMethod.GET)
    public String dtuGet(@PathVariable(value = "uuid") String uuid,Model model){
//    	Vehicle vehicle = vehicleManager.getVehicleByUuid(uuid);
//    	List<Bmu> bmus = bmuManager.getBmusByDtuId(vehicle.getDtu().getId());
//    	model.addAttribute("vehicle",vehicle);
//    	model.addAttribute("bmus",JSONArray.fromObject(bmus));
    	model.addAttribute("uuid", uuid);
    	model.addAttribute("dtuid", vehicleManager.getVehicleByUuid(uuid).getDtu().getId());
		model.addAttribute("sn", dtuManager.getSNByUUID(uuid));
		model.addAttribute("sim", dtuManager.getSIMByUUID(uuid));
    	return "dtu/dtu";
    }	
	/**
	 * 获取dtu详情（ajax）
	 * @param uuid
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/getAjax", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String dtuGetAjax(@RequestParam(value = "uuid") String uuid,@RequestParam(value = "timestamp") Long timestamp,Model model){
    	Vehicle vehicle = vehicleManager.getVehicleByUuid(uuid);
    	List<Bmu> bmus = bmuManager.getBmusByDtuId(vehicle.getDtu().getId());
		List<Bmu> bmus_rs = new ArrayList<Bmu>();
		//判断从机数量从dtu表中获取
		int bmuNum = dtuManager.getBmuNum(uuid);
		if (bmus != null ) {	//大于上传的从机数时，多余的从机不显示。屏蔽处理
			if (bmuNum == 0){
				//bmus.clear();
			}else { //以获取到的从机数为准 wly 2016年2月3日14:02:56
				for (int i = 0; i < bmus.size() ; i++) {
					if (i < bmuNum){
						bmus_rs.add(bmus.get(i));
					}
				}
			}
		}
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vehicle", vehicle);
        map.put("bmus", bmus_rs);
        JSONObject object = JSONObject.fromObject(map);

		return object.toString();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String dtuAjaxGet(@RequestParam(value = "id") Long id,Model model){
    	Dtu dtu = dtuManager.get(id);
    	JSONObject result = JSONObject.fromObject(dtu);
    	return result.toString();
    }	
    
    
    /**
     * 跳转至DTU列表页 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String forwardDtuList(Model model){
    	List<BatteryModel> bmList = batteryModelManager.getAll();
    	List<VehicleType> vtList = vehicleTypeManager.getAll();
    	List<String> facList = batteryModelManager.getBatteryFacNames(ShiroUtil.getCurrentUser().getId(),ShiroUtil.getCurrentUser().getIsAdmin());
    	List<String> citys = dtuManager.getCitys(ShiroUtil.getCurrentUser().getIsAdmin(),ShiroUtil.getCurrentUser().getId());

    	model.addAttribute("vtList",vtList );
    	model.addAttribute("bmList", bmList);
    	model.addAttribute("facList", facList);
    	model.addAttribute("citys", citys);
    	return "dtu/dtus";
    }

    /**
     * dtu 实时数据列表 ajax请求
     * @param pageNumber
     * @param uuid
     * @param vehicleTypeId
     * @param sort
     * @return
     */
    @RequestMapping(value = "/listQuery", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String dtuList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,@RequestParam(value = "uuid") String uuid,@RequestParam(value = "timestamp") Long timestamp,
    		@RequestParam(value = "vehicleTypeId") int vehicleTypeId,@RequestParam(value = "vehicleModelId") int vehicleModelId,@RequestParam(value = "batteryModelId") String facName,
    		@RequestParam(value = "chargeStatus") int chargeStatus,@RequestParam(value = "alarmStatus") int alarmStatus,@RequestParam(value = "city") String city,@RequestParam(value = "sort",defaultValue= "0") int sort){
    	List<Object[]> result = dtuManager.getDtuList(uuid.trim(), vehicleTypeId, vehicleModelId, facName, chargeStatus, alarmStatus, sort, pageNumber, PAGE_SIZE, ShiroUtil.getCurrentUser().getId(), ShiroUtil.getCurrentUser().getIsAdmin(), city);
    	Integer count = dtuManager.getDtuListCount(uuid.trim(), vehicleTypeId, vehicleModelId, facName, chargeStatus, alarmStatus, sort, ShiroUtil.getCurrentUser().getId(), ShiroUtil.getCurrentUser().getIsAdmin(), city);
    	JSONArray jsonResult = new JSONArray();
    	for (Object[] item :result){
    		JSONObject obj = new JSONObject();
    		obj.put("id", item[0]);
    		obj.put("uuid", item[1]);
    		obj.put("simCard", item[2]);
    		obj.put("facName", item[3]);
    		obj.put("soc", item[4]);
    		obj.put("alarmStatus", item[5]);
    		obj.put("onlineStatus", item[6]);
    		obj.put("typeName", item[7]);
    		obj.put("modelName", item[8]);
    		obj.put("totalCapacity", item[9]);
    		obj.put("batteryTotalVoltage", item[10]);
    		obj.put("batteryTotalAmp", item[11]);
    		obj.put("chargeStatus", item[12]);
    		obj.put("batteryRechargeCycles", item[13]);
    		obj.put("lontitude", item[14]);
    		obj.put("latitude", item[15]);
    		obj.put("insertTime", item[16]);
    		obj.put("vehicleNumber", item[18]);
    		obj.put("city", item[19]);
    		obj.put("intervalTime", item[20]);
    		obj.put("sn", item[21]);
    		jsonResult.add(obj);
    	}
    	int pageCount = count /PAGE_SIZE;
    	int mod = count%PAGE_SIZE;
    	if (mod>0)
    		pageCount += 1;
    	if (pageCount ==0)
    		pageCount += 1;
        Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageCount", pageCount);
		map.put("result", jsonResult);
		map.put("pageNumber", pageNumber);
        JSONObject object = JSONObject.fromObject(map);
    	return object.toString();
    }
    
    /**
     * 查询dtu实时定位数据（经纬度），并展示在百度地图之上
     * @param model
     * @return
     */
    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String dtuMap(Model model){
    	//List<Dtu> result = dtuManager.getDtuMaps();
    	List<Object[]> result = dtuManager.getDtuLocations(ShiroUtil.getCurrentUser().getIsAdmin(), ShiroUtil.getCurrentUser().getId());
    	
    	List<Object[]> filterResult = new ArrayList<Object[]>();
    	for (int i=0;i<result.size();i++){
    		Object[] item = result.get(i);
    		float lontitude = Float.parseFloat(item[5].toString());
    		float latitude = Float.parseFloat(item[6].toString());
    		if ((lontitude == 0)&&(latitude == 0))
    			continue;
    		if (lontitude<latitude)
    			continue;
    		filterResult.add(item);
    	}
    	
    	int loopCount = 0;
    	if (filterResult.size()%100 >0){
    		loopCount = filterResult.size()/100 + 1;
    	}else{
    		loopCount = filterResult.size()/100;
    	}
    	JSONArray geoArr = new JSONArray();
    	for(int i=0;i<loopCount;i++){
    		if (i == (loopCount -1)){
    			StringBuffer sb = new StringBuffer();
        		for(int j=i*100;j<filterResult.size();j++){
            		Object[] item = filterResult.get(j);
            		sb.append(item[5]+","+item[6]);
            		if (j != (filterResult.size()-1))
            			sb.append(";");
        		}
        		String geoResult = geocoder(sb.toString());
            	JSONObject jobj = JSONObject.fromObject(geoResult);
            	JSONArray arr = (JSONArray)jobj.get("result");
            	geoArr.addAll(arr);
    		}else{
    			StringBuffer sb = new StringBuffer();
        		for(int j=i*100;j<(i+1)*100;j++){
            		Object[] item = filterResult.get(j);
            		sb.append(item[5]+","+item[6]);
            		if (j != ((i+1)*100-1))
            			sb.append(";");
        		}
        		String geoResult = geocoder(sb.toString());
            	JSONObject jobj = JSONObject.fromObject(geoResult);
            	JSONArray arr = (JSONArray)jobj.get("result");
            	geoArr.addAll(arr);
    		}    		
    	}
    	
    	JSONArray jsonResult = new JSONArray();
    	for (int i=0;i<filterResult.size();i++){
    		Object[] item = filterResult.get(i);
    		JSONObject loc = (JSONObject)geoArr.get(i);
    		JSONObject obj = new JSONObject();
    		obj.put("id", item[0]);
    		obj.put("uuid", item[1]);
    		obj.put("facName", item[2]);
    		obj.put("soc", item[3]);
    		obj.put("online", item[4]);
    		obj.put("lontitude", loc.get("x"));
    		obj.put("latitude", loc.get("y"));
    		obj.put("vehicleNumber", item[7]);
    		obj.put("chargeStatus", item[8]);
    		obj.put("alarmStatus", item[9]);
    		obj.put("simCard", item[10]);
    		obj.put("typeName", item[11]);
    		jsonResult.add(obj);
    	}
    	
    	model.addAttribute("dtuList", jsonResult);
    	return "dtu/map";
    }
    public String geocoder(String loc){
        //String uriAPI = "http://api.map.baidu.com/geoconv/v1/?coords="+loc+"&ak=Uvehft4QYLK1W5KbRDzMY7q2";
    	String uriAPI = "http://api.map.baidu.com/geoconv/v1/?coords="+loc+"&ak=CF9649ad8e4abb469672be973645361c";
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
                    String strResult = EntityUtils.toString(httpResponse.getEntity() , "utf-8");
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
    /**
	 * 跳转至用户配置页
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
	public String forwardConfig(Model model){
		//List<Dtu> dtus = dtuManager.getAllDtu(ShiroUtil.getCurrentUser().getIsAdmin(),ShiroUtil.getCurrentUser().getId());
		List<Object[]> dtusns = dtuManager.getDtuSnList(ShiroUtil.getCurrentUser().getIsAdmin(), ShiroUtil.getCurrentUser().getId());
    	model.addAttribute("dtus", dtusns);
    	model.addAttribute("vtList", vehicleTypeManager.getAll());
		model.addAttribute("bmList", batteryTypeManager.getAll());
    	DtuUser user = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
    	model.addAttribute("user", user);

		//删除无用的车辆星型号
		vehicleModelManager.delVehicleModel();

		return "dtu/config";
	}

	/**
	 * 跳转到客户管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public String forwardCustomer(Model model){
		int isAdmin = ShiroUtil.getCurrentUser().getIsAdmin();
		if (isAdmin == 1){
			isAdmin = 1;
		}else{
			isAdmin = 0;
		}

		List<Object[]> userList = dtuUserManager.findDtuUserList(isAdmin, ShiroUtil.getCurrentUser().getId());
		model.addAttribute("userList", userList);
		model.addAttribute("corps", corpManager.getAll());
		DtuUser user = dtuUserManager.get(ShiroUtil.getCurrentUser().getId());
		model.addAttribute("user", user);
		return "dtu/customer";
	}

	/**
	 * 获取客户信息
	 * @param userid
	 * @return
	 */
	@RequestMapping(value = "/getuser", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String vehicleGetByUuidAjax(@RequestParam(value = "userid") Long userid){

		DtuUser dtuUser = dtuUserManager.get(userid);
		JSONObject d = JSONObject.fromObject(dtuUser);
		JSONArray jsonResult = new JSONArray();
		jsonResult.add(d);

		List<Object[]> list = dtuUserManager.findLinkUserList(ShiroUtil.getCurrentUser().getIsAdmin(), dtuUser.getCorp().getId(), ShiroUtil.getCurrentUser().getId());
		if(null != list && list.size()>0){
			Object[] linkObj = list.get(0);
			if (linkObj != null) {
				jsonResult.add(linkObj);
			}
		}
		return jsonResult.toString();
	}

	/**
	 * 获取指定用户DTU信息
	 * @return
	 */
	@RequestMapping(value = "/getUserDtuList", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String findUserDtuListAjax(@RequestParam(value = "userid") Long userid){
		List<Object[]> list = dtuManager.getManagerDtulist(ShiroUtil.getCurrentUser().getIsAdmin(), userid, ShiroUtil.getCurrentUser().getId());
		String rsStr = JSONArray.fromObject(list).toString();
		//System.out.println("rsStr=" + rsStr);
		return rsStr;
	}

	@RequestMapping(value = "/getUserDtuAll", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String getUserDtuAll(@RequestParam(value = "userid") Long userid, @RequestParam(value = "uuid") String uuid, @RequestParam(value = "isUse") int isUse){

		List<Object[]> list = dtuManager.getManagerAllDtulist(ShiroUtil.getCurrentUser().getIsAdmin(), isUse, uuid, userid, ShiroUtil.getCurrentUser().getId());
		String rsStr = JSONArray.fromObject(list).toString();
		//System.out.println("rsStr=" + rsStr);
		return rsStr;
	}

	/**
	 * 重新获取左侧客户列表
	 * @return
	 */
	@RequestMapping(value = "/getUserLi", method = RequestMethod.GET,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String findUserLiAjax(@RequestParam(value = "userId") Long userId, @RequestParam(value = "usertype") int usertype){
		int isAdmin = ShiroUtil.getCurrentUser().getIsAdmin();
		if(userId == null || userId == 0){
			userId =  ShiroUtil.getCurrentUser().getId();
		}else{
			isAdmin = 0;
		}
		if(isAdmin == 1){
			usertype = 1;
		}

		List<Object[]> list = dtuUserManager.findDtuUserList(usertype, userId);
		String rsStr = JSONArray.fromObject(list).toString();

		return rsStr;
	}

	/**
	 * 用户信息修改
	 * @param accountInfo
	 * @param linkInfo
	 * @param corpOldInfo
	 * @param dtuUser
	 * @return
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String updateUserAjax(
			@RequestParam(value = "accountInfo") String accountInfo,
			@RequestParam(value = "linkInfo") String linkInfo,
			@RequestParam(value = "corpOldInfo") String corpOldInfo,
			DtuUser dtuUser	){
		// @RequestParam(value = "fullNameOldInfo") String fullNameOldInfo,

		Corp corp = dtuUser.getCorp();
		corpManager.save(corp);//修改公司信息
		//dtuUserManager.updateDtuUserFullName(fullName, userId);//修改用户名
		//保留原创建人 创建时间信息
		DtuUser oldDtuUser = dtuUserManager.get(dtuUser.getId());
		dtuUser.setOptUserName(oldDtuUser.getOptUserName());
		dtuUser.setOptTime(oldDtuUser.getOptTime());
		dtuUser.setOptUser(oldDtuUser.getOptUser());

		dtuUserManager.save(dtuUser);
		StringBuffer logStr = new StringBuffer();
		logStr.append("原公司信息>>").append(corpOldInfo).append("。").append("原账户信息：").append(accountInfo).append("。原联系人信息：" + linkInfo);
		int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "UpdateUserInfo", logStr.toString());

		//System.out.println("rsStr=" + rsStr);
		return "";
	}

	/**
	 * 添加用户
	 * @param dtuUser
	 * @return
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String addUserAjax(
			@RequestParam(value = "userIdAdd") Long userIdAdd,
			DtuUser dtuUser){
		Long userId = ShiroUtil.getCurrentUser().getId();
		if (userIdAdd != null && userIdAdd > 0) {
			if (userId.longValue() == userIdAdd.longValue()){
				return "isme";
			}
			dtuUserManager.addUserRelation(userIdAdd, dtuUser.getFullName(), userId, 0);//添加关联关系
			int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "AddUserInfo", "关联新用户>>用户ID：" + userIdAdd);
			return "" + userIdAdd;
		}
		//判断登录账号是否存在，已存在的不能添加
		DtuUser dtuUserDB = dtuUserManager.findByUserName(dtuUser.getUserName());
		if(null != dtuUserDB){
			if (dtuUserDB.getIsAdmin() > 0){//非前台客户不能添加
				return "backendUser";
			}
			if (userId.longValue() == dtuUserDB.getId()){
				return "isme";
			}
			//已存在的不再添加
			List<Object[]> list = dtuUserManager.findDtuUserList(0, userId);
			for (int i = 0; i <list.size(); i++) {
				Object[] obj = list.get(i);
				if (obj != null) {
					long id = Long.parseLong(obj[0].toString());
					if (id == dtuUserDB.getId()){
						return "having";
					}
				}
			}


			String dtuJson = JSONObject.fromObject(dtuUserDB).toString();
			return dtuJson;
		}
		Corp corp = dtuUser.getCorp();
		corp.setOptUser(userId);
		corp.setOptTime(new Date());
		corpManager.save(corp);

		dtuUser.setCorp(corp);
		dtuUser.setOptTime(new Date());
		dtuUser.setOptUser(userId);
		dtuUser.setOptUserName(ShiroUtil.getCurrentUser().getName());
		dtuUserManager.save(dtuUser);
		dtuUserManager.addUserRelation(dtuUser.getId(), dtuUser.getFullName(), userId, 1);//添加关联关系

		StringBuffer logStr = new StringBuffer();
		logStr.append("添加公司信息>>ID:").append(corp.getId())
				.append("，名称:").append(corp.getCorpName())
				.append("，电话：").append(corp.getPhone())
				.append("，邮箱：").append(corp.getEmail())
				.append("，地址：").append(corp.getAddr()).append("。")
				.append("用户账号：").append(dtuUser.getUserName())
				.append("，用户名：").append(dtuUser.getFullName())
				.append("，密码：").append(dtuUser.getUserPass())
				.append("，联系电话：").append(dtuUser.getRelation());

		int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "AddUserInfo", logStr.toString());

		return "" + dtuUser.getId();
	}

	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/delUser", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String delUserAjax(
			@RequestParam(value = "userId") Long userId){
		//判断是否绑定DTU，有绑定的账号不能删除 前台已经判断没有自己分发的设备时才可以到后台
		/*Long dtuNum = dtuUserManager.findDtuCountBUserId(userId);
		if(dtuNum > 0){
			return "haveDtu";
		}*/
		/*Long orderNum = dtuUserManager.findOrderCountBUserId(userId);
		if(orderNum > 0){
			return "haveOrder";
		}*/

		DtuUser dtuUser = dtuUserManager.get(userId);
		int isAdmin = 0;//只解除关联关系
		dtuUserManager.delUserRelation(isAdmin, userId, ShiroUtil.getCurrentUser().getId());
		//只解除关联关系
		StringBuffer logStr = new StringBuffer();
		logStr.append("删除用户信息>>用户账号：").append(dtuUser.getUserName())
				.append("，联系人：").append(dtuUser.getFullName()).append("，密码：").append(dtuUser.getUserPass())
				.append("，联系电话：").append(dtuUser.getRelation());
		int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "DelUserInfo", logStr.toString());
		return "";
	}

	/**
	 * 保存用户DTU关联配置
	 * @param userId
	 * @param seldtus
	 * @param userDtu_old
	 * @return
	 */
	@RequestMapping(value = "/saveUserDtu", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String saveUserDtu(
			@RequestParam(value = "userId") Long userId, @RequestParam(value = "seldtus") String seldtus, @RequestParam(value = "userDtu_old") String userDtu_old ){

		int rs = dtuUserManager.addDtuUserConfig(userId, seldtus, ShiroUtil.getCurrentUser().getId(), ShiroUtil.getCurrentUser().getIsAdmin());

		StringBuffer logStr = new StringBuffer();
		logStr.append("修改用户DTU配置信息>>用户ID：").append(userId)	.append("，原DTU信息：").append(userDtu_old).append("，新DTU信息：").append(seldtus);
		int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "SaveUserDTU", logStr.toString());

		return "" + rs;
	}

	/**
	 * 删除单个记录
	 * @param userId
	 * @param selUuid
	 * @return
	 */
	@RequestMapping(value = "/delUserDtu", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String delUserDtu(@RequestParam(value = "userId") Long userId, @RequestParam(value = "selUuid") String selUuid){

		int rs = dtuUserManager.delDtuUserConfig(userId, selUuid, ShiroUtil.getCurrentUser().getId());
		StringBuffer logStr = new StringBuffer();
		logStr.append("删除用户单个DTU配置信息>>用户ID：").append(userId)	.append("，被删除DTU信息_UUID=").append(selUuid);
		int logRs = optLogDao.optLog(ShiroUtil.getCurrentUser().getId(), "SaveUserDTU", logStr.toString());

		return "" + rs;
	}

	@RequestMapping(value = "/searchInfoByNname", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String searchInfoByNname(
			@RequestParam(value = "userName") String userName){
		DtuUser dtuUser = dtuUserManager.findByUserName(userName);

		if(null != dtuUser){
			String str = JSONObject.fromObject(dtuUser).toString();
			return str;
		}
		return "";
	}

    /**
     * 查询dtu实时报警信息list
     * @param model
     * @return
     */
    @RequestMapping(value = "/alarm", method = RequestMethod.GET)
    public String forwordDtuAlarm(Model model){
//    	List<Object[]> result = dtuManager.getAlarmRealtime(uuid, isAdmin, userId, pageNumber, pageSize);
    	
    	return "dtu/alarm";
    }
    /**
     * 获取报警列表，ajax
     * @param model
     * @return
     */
    @RequestMapping(value = "/alarmList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
    @ResponseBody
    public String dtuAlarmList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,@RequestParam(value = "uuid") String uuid,
    		Model model){
    	List<Object[]> result = dtuManager.getAlarmRealtime(uuid.trim(), ShiroUtil.getCurrentUser().getIsAdmin(),ShiroUtil.getCurrentUser().getId(), pageNumber, PAGE_SIZE);
    	Integer count = dtuManager.getAlarmRealtimeCount(uuid.trim(), ShiroUtil.getCurrentUser().getIsAdmin(),ShiroUtil.getCurrentUser().getId());
    	JSONArray jsonResult = new JSONArray();
    	for (Object[] item :result){
    		JSONObject obj = new JSONObject();
    		obj.put("id", item[0]);
    		obj.put("alarmType", item[1]);
    		obj.put("uuid", item[2]);
    		obj.put("alarmStartTime", item[3]);
    		obj.put("factoryName", item[4]);
    		obj.put("onlineStatus", item[5]);
    		obj.put("lontitude", item[6]);
    		obj.put("latitude", item[7]);
    		obj.put("chargeStatus", item[8]);
    		obj.put("vehicleNumber", item[9]);
    		obj.put("city", item[10]);
    		jsonResult.add(obj);
    	}
    	int pageCount = count /PAGE_SIZE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageCount", pageCount+1);
        map.put("pageNumber", pageNumber);
        map.put("result", jsonResult);
        JSONObject object = JSONObject.fromObject(map);
    	return object.toString();
    }
    
    @RequestMapping(value = "/log/{uuid}", method = RequestMethod.GET)
    public String forwordDtuLog(@PathVariable("uuid") String uuid,Model model){
    	Vehicle v = vehicleManager.getVehicleByUuid(uuid);
    	model.addAttribute("uuid", uuid);
    	model.addAttribute("vehicleNumber", v.getVehicleNumber());
		/*List<Bmu> bmus = bmuManager.getBmusByDtuId(v.getDtu().getId()); //添加从机选项 wly 2015年9月16日11:18:29
		System.out.println("bmus==" + bmus.size() + "\t" + bmus);
		model.addAttribute("bmuList", bmus);*/
		int bmuNum = dtuManager.getBmuNum(uuid);//从机数量从dtu表获取 wly 2016年1月19日10:30:45
		model.addAttribute("bmuNum", bmuNum);
    	return "dtu/dtulog";
    }
    
    @RequestMapping(value = "/logAjax")
    @ResponseBody
    public String dtuLog(@RequestParam("uuid") String uuid,@RequestParam("page") int pageIndex,Model model){
		PageDtuMongo page =  mongodbManager.getDtuPage(uuid, pageIndex, 30);
    	JSONObject obj = JSONObject.fromObject(page);
		String rsStr = obj.toString();
		return rsStr;
    }
    
    @RequestMapping(value = "/bmulog/{uuid}/{bmuId}", method = RequestMethod.GET)
    public String forwordBmuLog(@PathVariable("uuid") String uuid,@PathVariable("bmuId") int bmuId,Model model){
    	Vehicle v = vehicleManager.getVehicleByUuid(uuid);
    	model.addAttribute("uuid", uuid);
    	model.addAttribute("bmuId", bmuId);
    	model.addAttribute("vehicleNumber", v.getVehicleNumber());
		/*List<Bmu> bmus = bmuManager.getBmusByDtuId(v.getDtu().getId());
		model.addAttribute("bmuList", bmus);*/
		int bmuNum = dtuManager.getBmuNum(uuid); //从机数量从dtu表获取 wly 2016年1月19日10:30:45
		model.addAttribute("bmuNum", bmuNum);
    	return "dtu/bmulog";
    }
    @RequestMapping(value = "/getMaxAlarmId", method = RequestMethod.GET)
    @ResponseBody
    public String getMaxAlarmId(Model model){
    	Integer result = dtuManager.getMaxAlarmId(ShiroUtil.getCurrentUser().getId());
    	return result.toString();
    }
    @RequestMapping(value = "/getNewAlarmRealtime", method = RequestMethod.GET)
    @ResponseBody
    public String getNewAlarmRealtime(@RequestParam("alarmId") Long alarmId,Model model){
    	Integer result = dtuManager.getNewAlarmRealtime(ShiroUtil.getCurrentUser().getId(), alarmId);
    	return result.toString();
    } 
    
    @RequestMapping(value = "/track/{uuid}", method = RequestMethod.GET)
    public String forwordTrack(@PathVariable("uuid") String uuid,Model model){
    	Vehicle vehicle = vehicleManager.getVehicleByUuid(uuid);
    	model.addAttribute("uuid", uuid);
    	model.addAttribute("vehicleNumber", vehicle.getVehicleNumber());
    	model.addAttribute("vehicleType", vehicle.getVehicleType().getTypeName());
    	return "dtu/track";
    }
    
    @RequestMapping(value = "/getTracks")
    @ResponseBody
    public String getTracksAjax(@RequestParam("uuid") String uuid,@RequestParam("startDT") String startDT,@RequestParam("endDT") String endDT,Model model){
    	List<DBObject> res = mongodbManager.getTracksByUuid(uuid, startDT+":00", endDT+":00");
    	List<DBObject> filterResult = new ArrayList<DBObject>();
    	for (int i=0;i<res.size();i++){
    		DBObject item = res.get(i);
    		float lontitude = Float.parseFloat(item.get("lontitude").toString());
    		float latitude = Float.parseFloat(item.get("latitude").toString());
    		if ((lontitude == 0)&&(latitude == 0))
    			continue;
    		if (lontitude<latitude)
    			continue;
    		filterResult.add(item);
    	}
    	
    	JSONArray result = JSONArray.fromObject(filterResult);
    	
    	int loopCount = 0;
    	if (result.size()%100 >0){
    		loopCount = result.size()/100 + 1;
    	}else{
    		loopCount = result.size()/100;
    	}
    	JSONArray geoArr = new JSONArray();
    	for(int i=0;i<loopCount;i++){
    		if (i == (loopCount -1)){
    			StringBuffer sb = new StringBuffer();
        		for(int j=i*100;j<result.size();j++){
        			JSONObject item = (JSONObject)result.get(j);
            		sb.append(item.get("lontitude")+","+item.get("latitude"));
            		if (j != (result.size()-1))
            			sb.append(";");
        		}
        		String geoResult = geocoder(sb.toString());
            	JSONObject jobj = JSONObject.fromObject(geoResult);
            	JSONArray arr = (JSONArray)jobj.get("result");
            	geoArr.addAll(arr);
    		}else{
    			StringBuffer sb = new StringBuffer();
        		for(int j=i*100;j<(i+1)*100;j++){
        			JSONObject item = (JSONObject)result.get(j);
            		sb.append(item.get("lontitude")+","+item.get("latitude"));
            		if (j != ((i+1)*100-1))
            			sb.append(";");
        		}
        		String geoResult = geocoder(sb.toString());
            	JSONObject jobj = JSONObject.fromObject(geoResult);
            	JSONArray arr = (JSONArray)jobj.get("result");
            	geoArr.addAll(arr);
    		}    		
    	}    	
    	
    	for (int i=0;i<geoArr.size();i++){
    		JSONObject item = (JSONObject)result.get(i);
    		JSONObject geo = (JSONObject)geoArr.get(i);
    		geo.put("insertTime", item.get("insertTime"));
    	}
    	return geoArr.toString();
    }
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理  
        binder.registerCustomEditor(Date.class, new DateEditor());
    }
    
}
