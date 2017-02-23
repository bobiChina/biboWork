package com.ligootech.webdtu.service.dtu;

import java.util.List;
import java.util.Map;

import com.ligootech.webdtu.util.EasyUiUtil;
import org.appdot.service.GenericManager;

import com.ligootech.webdtu.entity.core.Dtu;

public interface DtuManager extends GenericManager<Dtu, Long>{

	public List<Dtu> getDtuMaps();
	
	public List<Dtu> getDtuLatLon();
	
	public List<Object[]> getDtuLocations(int isAdmin,Long userId);
	
	public List getPageDtu(int pageNumber, int pageSize);
	
	public List<Object[]> getDtuList(String uuid,int vehicleTypeId,int vehicleModelId,String facName,
			int chargeStatus,int alarmStatus,int sort,int pageNumber,int pageSize,Long userId,int isAdmin,String city);
	
	public Integer getDtuListCount(String uuid,int vehicleTypeId,int vehicleModelId,String facName,
			int chargeStatus,int alarmStatus,int sort,Long userId,int isAdmin,String city);	
	
	public List<Dtu> getAllDtu(int isAdmin,Long userId);
	
	public List getAlarmRealtime(String uuid,int isAdmin,Long userId,int pageNumber,int pageSize);
	
	public Integer getAlarmRealtimeCount(String uuid,int isAdmin,Long userId);
	
	public List<String> getCitys(int isAdmin,Long userId);
	
	public Integer getMaxAlarmId(Long userId);
	
	public Integer getNewAlarmRealtime(Long userId,Long alarmId);
	
	public Integer getTotalCapacity(int isAdmin, Long userId);

	public List<Object[]> getDtuSnList(int isAdmin,Long userId);

	/**
	 * 获取操作用户范围内指定用户的DTU信息
	 * @param isAdmin
	 * @param userid
	 * @param optUserId
	 * @return
	 */
	public List<Object[]> getManagerDtulist(int isAdmin, Long userid, Long optUserId);

	/**
	 * 查询操作用户所有的DTU信息，并附带查询条件<br>
	 * isUse 0-未分配（所属用户只有一个） 1-以分配 （所属用户为多个）<br>
	 * uuid 当前操作用户权限内UUID模糊查询
	 * @param isAdmin
	 * @param isUse
	 * @param uuid
	 * @param userId
	 * @param optUserId
	 * @return
	 */
	public List<Object[]> getManagerAllDtulist(int isAdmin, int isUse, String uuid,Long userId, Long optUserId);

	/**
	 * 获取用户DTU数量
	 * @param userid
	 * @return
	 */
	public long getDtuCountByUserid(Long userid);

	/**
	 * 获取用户dtu信息
	 * @param userId
	 * @return
	 */
	public List<Object[]> findDtuByUserId(Long userId);


	public List<Map<String, Object>> findDtuByUserIdMap(Long userId);

	/**
	 * 删除用户DTU设备
	 * @param userId
	 * @param dtuid
	 * @param optUserId
	 * @return
	 */
	public int deluserDtu(Long userId, Long dtuid, Long optUserId);

	/**
	 * 多DTU删除
	 * @param userId
	 * @param dtuids
	 * @param optUserId
	 * @return
	 */
	public int deluserDtu(String userId, String dtuids, Long optUserId);

	/**
	 * 获取sn码
	 * @param uuid
	 * @return
	 */
	public String getSNByUUID(String uuid);

	/**
	 * 获取SIM卡号
	 * @param uuid
	 * @return
	 */
	public String getSIMByUUID(String uuid);

	/**
	 * 获取从机数量
	 * @param uuid
	 * @return
	 */
	public int getBmuNum(String uuid);

	/**
	 * 获取用户DTU列表
	 * @param rows
	 * @param page
	 * @param sort
	 * @param order
	 * @param userId
	 * @return
	 */
	public EasyUiUtil.PageForData findDTUByUserId(int rows, int page, String sort, String order, String userId);
}
