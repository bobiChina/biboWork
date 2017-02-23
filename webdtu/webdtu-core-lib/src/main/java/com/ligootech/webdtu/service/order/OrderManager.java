package com.ligootech.webdtu.service.order;

import com.ligootech.webdtu.entity.core.DtuUser;
import com.ligootech.webdtu.entity.core.Order;
import com.ligootech.webdtu.service.account.ShiroUser;
import com.ligootech.webdtu.util.EasyUiUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2015/10/15 14:34.
 */
public interface OrderManager {
    /**
     * 获取订单列表
     * @param userId
     * @return
     */
    public List<Order> findOrderList(Long userId);

    /**
     * 获取订单简化信息 <br>
     * type=0 全部 type=1 去除作废的订单
     * @param userId
     * @param type
     * @return
     */
    public List<Object[]> findSimpleOrderList(Long userId, int type);

    /**
     * 开发人员等搜索自己的订单
  * @param user
     * @return
     */
    public List<Object[]> findSimpleOrderListByTrack(ShiroUser user);

    /**
     * 获取单个订单信息
     * @param orderNo
     * @return
     */
    public Order findInfoByOrderNo(String orderNo);


    /**
     * 销售代表订单查询
     * @param orderNo
     * @param salesUserId
     * @return
     */
    public Order findInfoByOrderNo(String orderNo, Long salesUserId);

    /**
     * 获取订单对应的SN码、UUID信息
     * @param orderNo
     * @return
     */
    public List<Object[]> findSNListByOrderNo(String orderNo);

    /**
     * 订单的 保存、修改
     * @param order
     * @param opt_type
     * @param devices_code
     * @param devices_class
     * @param devices_bmu
     * @param devices_count
     * @param userId
     * @return
     */

    public int saveOrder(Order order, String opt_type, String devices_code, String devices_class, String devices_bmu, String devices_count, Long userId);

    /**
     * 售后订单保存
     * @param order
     * @param opt_type
     * @param devices_code
     * @param devices_class
     * @param devices_bmu
     * @param devices_count
     * @param userId
     * @return
     */
    public int saveSHOrder(Order order, String opt_type, String devices_code, String devices_class, String devices_bmu, String devices_count, Long userId);

    /**
     * 获取售后订单信息
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderNo
     * @return
     */
    public EasyUiUtil.PageForData findSHInfoList(int rows, int page, String sort, String order, String orderNo);

    /**
     * 售后信息
     * @param orderNo
     * @return
     */
    public List<Map<String, Object>> findSHInfoList(String orderNo);

    /**
     * 获取售后订单编号
     * @param orderNo
     * @return
     */
    public List<String> findSHOrderList(String orderNo);

    /**
     * 售后订单编号
     * @param orderNo
     * @return
     */
    public List<String> findSHOrderList4OTA(String orderNo);

    /**
     * 订单删除
     * @param orderId
     * @param optUserId
     * @return
     */
    public int delOrderById(Long orderId, Long optUserId);

    /**
     * 获取订单编号
     * @param orderno
     * @param startnum
     * @param endnum
     * @return
     */
    public List<String> findOrderNo(String orderno, int startnum, int endnum);

    /**
     * 客户所属订单编号
     * @param userId
     * @return
     */
    public List<String> findOrderNoByUserId(Long userId);

    /**
     * 获取订单的SN信息列表
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderNo
     * @return
     */
    public EasyUiUtil.PageForData findSNList(int rows, int page, String sort, String order, String orderNo);

    /**
     * 获取订单产品信息
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderNo
     * @return
     */
    public EasyUiUtil.PageForData getOrderProduct(int rows, int page, String sort, String order, String orderNo, String keywords);

    /**
     * 修改订单产品表状态
     * @param ids
     * @param orderno
     * @param status
     * @param user
     * @return
     */
    public int updateStatus(String ids, String orderno, int status, ShiroUser user);

    /**
     * 解绑
     * @param ids
     * @param orderno
     * @param type
     * @param user
     * @return
     */
    public int unbindOrderDevice(String ids, String orderno, String type, ShiroUser user);

    /**
     * 获取订单有效设备数量
     * @param orderno
     * @return
     */
    public int findSnCount4DelOrder(String orderno);

    /**
     * 删除设备软件时检查是否绑定设备
     * @param orderno
     * @param deviceClass
     * @return
     */
    public int findSnCount4DelDevice(String orderno, String deviceClass);

    /**
     * 订单软件删除
     * @param orderno
     * @param deviceClass
     * @param userId
     * @return
     */
    public int delOrderDevice(String orderno, String deviceClass, Long userId);

    /**
     * 检查订单是否存在
     * @param orderNo
     * @return
     */
    public boolean checkOrder(String orderNo);

    /**
     * 获取设备型号
     * @param deviceType
     * @return
     */
    public List<String> findDeviceByType(String deviceType);

    /**
     * 获取订单所属设备信息
     * @param orderNo
     * @return
     */
    public List<Object[]> findDeviceByOrderNo(String orderNo);

    /**
     * 获取订单设备软件信息
     * @param orderNo
     * @return
     */
    public Map<String, List<String>> findDeviceFileByOrderNo(String orderNo);

    /**
     * 保存订单软件
     * @param req
     * @param user
     * @return
     */
    public int saveDeviceFile(HttpServletRequest req, ShiroUser user);

    /**
     * 获取订单设备信息，以及对应的设备文件
     * @param orderno
     * @return
     */
    public List<Map<String, Object>> findOrderDevice(String orderno);

    /**
     * 获取所有设备信息
     * @param orderno
     * @return
     */
    public List<Map<String, Object>> findOrderDeviceAll(String orderno);

    /**
     * 获取订单信息
     * @param req
     * @return
     */
    public List<Map<String, String>> findOrderList4Page(HttpServletRequest req, int pageSize);
    public List<Map<String, String>> findOrderList4Page(HttpServletRequest req, int pageSize, Long userId);

    /**
     * 获取订单数量
     * @param req
     * @return
     */
    public Integer findOrderCount4Page(HttpServletRequest req);
    public Integer findOrderCount4Page(HttpServletRequest req, Long userId);

    /**
     * 获取非删除订单信息 listType 1-软件管理订单 2-生产管理订单 （都包含新订单内容）
     * @param listType
     * @return
     */
    public List<String[]> findSimpleOrderList(int listType);

    /**
     * 获取设备信息（系统配置）
     * @param orderNo
     * @return
     */
    public List<String[]> findOrderDeviceList(String orderNo);

    /**
     * 获取客户订单
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderNo
     * @param corpId
     * @return
     */
    public EasyUiUtil.PageForData findEasyUiOrderList(int rows, int page, String sort, String order, String orderNo, String corpId);

    /**
     * 检查订单软件上传情况
     * @param orderNo
     * @return
     */
    public int findSoftware4Order(String orderNo);

    /**
     * 检查售后情况
     * @param orderNo
     * @return
     */
    public int findBound4Order(String orderNo);

    /**
     * 获取客户对应订单
     * @param corpId
     * @return
     */
    public int findCountByCorpId(String corpId);

    /**
     * 订单基本信息修改
     * @param order
     * @param userId
     * @return
     */
    public int updateOrderBase(Order order, Long userId);

    /**
     * 修改下单原因
     * @param orderNo
     * @param orderNote
     * @param userId
     * @return
     */
    public int updateOrderNote(String orderNo, String orderNote, String beforeSoftware, Long userId);

    /**
     * 修改系统配置
     * @param orderNo
     * @param devices_code
     * @param devices_class
     * @param devices_bmu
     * @param devices_count
     * @param userId
     * @return
     */
    public int updateOrderDevice(String orderNo, String devices_code, String devices_class, String devices_bmu, String devices_count, Long userId);

    /**
     * 获取模块对应的版本号, versiobType hw_version
     * @param orderno
     * @param versiobType
     * @return
     */
    public Map<String, String> findDeviceVersion(String orderno, String versiobType);


    /*****************************************
     * OTA相关
     ****************************************/

    /**
     * 获取OTA相关的订单编号
     * @return
     */
    public List<String> findOrder4OTA();

    /**
     * 获取订单信息
     * @param orderNo
     * @return
     */
    public Map<String, Object> findCOrderByOrderNo(String orderNo);


    /**
     * 获取订单待选设备
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderNo
     * @return
     */
    public EasyUiUtil.PageForData findOrderList4OTA(int rows, int page, String sort, String order, String orderNo);

    /**
     * 获取订单文件信息
     * @param orderNo
     * @return
     */
    public Map<String, String[]> findFileByOrderNo(String orderNo);

    /**
     * 获取基础配置检验信息
     * @param checkStr 检查对象
     * @param checkTpe 配置类型编码
     * @param isCheckConfigName 是否校验配置名 用于校验字段 true 校验“config_name”字段， false校验“config_value”字段
     * @return
     */
    public boolean checkBaseConfig(String checkStr, int checkTpe, boolean isCheckConfigName);

}
