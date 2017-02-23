package com.ligootech.webdtu.service.order;

import com.ligootech.webdtu.entity.core.*;
import com.ligootech.webdtu.entity.core.clientForm.SystemJoint;
import com.ligootech.webdtu.entity.core.clientForm.ToolInfo;
import com.ligootech.webdtu.entity.core.clientForm.ToolInfoB;
import com.ligootech.webdtu.service.account.ShiroUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2015/10/27 10:27.
 */
public interface SNManager {

    /**
     * 或取指定订单的SN数量
     * @param orderId
     * @return
     */
    //public int findSnCountByOrderId(String orderId);

    /**
     * 通过姓名模糊查询用户信息
     * @param fullName
     * @return
     */
    public List<Object[]> findUserByFullname(String fullName);

    /**
     * 插入SN信息
     * @param snMap
     * @return
     */
    public int insertSn(Map<String, String> snMap);

    /**
     * 删除
     * @param id
     * @return
     */
    public int delSNById(Long id, Long optUserId);



    /**
     * 模块扫码客户端删除恢复PCBA板信息
     * @param id
     * @param optUserId
     * @return
     */
    public int delSNById_scan(Long id, Long optUserId);

    /**
     * 批量删除SN
     * @param ids
     * @param optUser
     * @return
     */
    public int delSNByIds(String ids, String orderno, ShiroUser optUser);

    /**
     * 解除绑定
     * @param ids
     * @param orderno
     * @param optUser
     * @return
     */
    public int unbind(String ids, String orderno, ShiroUser optUser);

    /**
     * 删除SN信息
     * @param scanid
     * @return
     */
    public int delScanById(Long scanid, Long optUserId);

    /**
     * 用户登录
     * @param userName
     * @param userPass
     * @return
     */
    public DtuUser findDtuUserByLogin(String userName, String userPass);

    /**
     * 扫码表判重
     * @param uuid
     * @param sn
     * @return
     */
    public boolean findRepetitionFromScan(String uuid, String sn);

    /**
     * SN表 SN码判重<br>
     * 查询重复的SN码，并返回重复结果,没有重复时返回null
     * @param sn
     * @return
     */
    public String findRepetitionFromSN(String sn);

    /**
     * SN表 UUID判重
     * @param uuid
     * @param sn
     * @return
     */
    public String findRepetitionFromUUID(String uuid, String sn);

    /**
     * 判断是否重复
     * @param uuid
     * @param m_sn
     * @return
     */
    public int findRepetition(String uuid, String m_sn);

    /**
     * 判断PCB扫码信息是否可用
     * @param main_sn
     * @return
     */
    public boolean findPcbByMainSn(String main_sn);

    /**
     * 订单和模块SN码相同时，可以不走解绑，重新流转到工装
     * @param main_sn
     * @param orderNo
     * @return
     */
    public boolean findPcbByMainSn(String main_sn, String orderNo);


    /**
     * 获取订单对应的设备信息
     * @param orderno
     * @return
     */
    public List<Object[]> findSNListByOrderNo(String orderno);

    /**
     * 获取SN详细信息
     * @param orderno
     * @return
     */
    public List<SNInfo> findSNAllListByOrderNo(String orderno);

    /**
     * 获取未分配的SN信息
     * @param orderno
     * @return
     */
    public List<SNInfo> findSNNoDtuByOrderNo(String orderno);

    /**
     * 获取扫描表信息
     * @param orderno
     * @return
     */
    public List<SNScan> findScanAllListByOrderNo(String orderno);


    /**
     * 设备绑定
     * @param ids
     * @param orderno
     * @param optUserId
     * @return
     */
    public int setScan2DTU(String ids, String orderno, Long optUserId);

    /**
     * 获取硬件版本号
     * @param prodType
     * @return
     */
    public List<String> findHardwareVersion(String prodType);

    /**
     * 获取产品类型
     * @return
     */
    public List<ProductType> findProdTypeList();

    /**
     * 验证产品类型是否存在
     * @return
     */
    public Long findProdTypeByName(String prodName);


    /**
     * 保存sn扫码信息
     * @param obj
     * @return
     */
    public int saveSnInfo(SNInfo obj);

    /**
     * 保存模块SN信息
     * @param obj
     * @return
     */
    public int saveMiScanInfo(SNInfo obj);

    /**
     * 保存物料编码
     * @param pSN
     * @param goodsCode
     * @return
     */
    public int updateGoodsCode(String pSN, String goodsCode);

    /**
     * 添加设备
     * @param orderno
     * @param snids
     * @param userId
     * @param optUserId
     * @return
     */
    public int sn2DTU(String orderno, String snids, Long userId, Long optUserId);

    /**
     * 保存PCB板SN码配置
     * @param main_sn
     * @param pcb_sns
     * @param optUserId
     * @return 保存的数量
     */
    public int savePCBSN(String main_sn, String pcb_sns, String optUserId);

    /**
     * PCBA扫码信息保存
     * @param main_sn
     * @param pcb_sns
     * @param orderno
     * @param hw_version
     * @param optUserId
     * @return
     */
    public int savePCBSN_B(String main_sn, String pcb_sns, String orderno, String hw_version, String optUserId);

    /**
     * 验证PCBA是否使用
     * @param pcb_sns
     * @param m_sn
     * @return
     */
    public int checkPcbaForBi(String m_sn, String pcb_sns);

    /**
     * 获取PCBA验证不通过时对应的模块SN和PCBA板SN信息
     * @param pcb_sns
     * @return
     */
    public String findNoPassPcbaMSn(String pcb_sns);

    /**
     * 获取PCB板对应的sn信息
     * @param main_sn
     * @return
     */
    public List<Map<String, String>> findPcbSn(String main_sn);

    /**
     * 获取UUID对应的数据库信息
     * @param uuids
     * @return
     */
    public List<String> findUuidDB(String uuids);

    /**
     * 获取SN数据库信息
     * @param sns
     * @return
     */
    public List<String> findSnDB(String sns);

    /**
     * 校验模块sn与pcbaSN
     * @param moduleSn
     * @param pcba_sns
     * @return
     */
    public int checkModuleSNPcbaSN(String moduleSn, String pcba_sns);

    /**
     * 校验模块SN标识的主板型号与设备读取的主板型号是否匹配
     * @param moduleSn
     * @param pcba_sn
     * @return
     */
    public int checkModuleSN(String moduleSn, String pcba_sn);


    /**
     * 系统联调数据上报
     * @param obj
     * @return
     */
    public int saveSystemJoint(SystemJoint obj);

    /**
     * 工装数据上报
     * @param obj
     * @return
     */
    public int saveToolInfo(ToolInfo obj);

    /**
     * B型工装数据保存
     * @param obj
     * @return
     */
    public int saveToolInfoB(ToolInfoB obj);

    /**
     * 判断用户角色
     * @param userId
     * @param roleCode
     * @return
     */
    public boolean checkUserPermission(Long userId, String roleCode);

    /**
     * 模块SN码获取设备相关信息
     * @param snCode
     * @return
     */
    public SnModule getSnModuleByCode(String snCode);

    /**
     * 检查设备型号在订单中是否存在
     * <br>存在则返回从机数,主机、一体机默认为0，小于0的为订单中不存在该设备型号
     * @param snModule
     * @param orderNo
     * @return
     */
    public int checkDeciveByOrderNo(SnModule snModule, String orderNo);

    /**
     * 检查设备型号在订单中是否存在
     * <br>存在则返回从机数,主机、一体机默认为0，小于0的为订单中不存在该设备型号
     * @param deviceCode
     * @param orderNo
     * @return
     */
    public int checkDeciveByOrderNo(String deviceCode, String orderNo);

    /**
     * 校验订单产品SN码
     * @param orderNo
     * @param productSn
     * @return
     */
    public SnModule checkOrderProductSn(String orderNo, String productSn);

    /**
     * 校验产品是否工装测试
     * @param orderNo
     * @param productSn
     * @return
     */
    public int checkProductSnFromTool(String orderNo, String productSn);

    /**
     * 检查工装信息
     * @param orderNo
     * @param mSN
     * @return
     */
    public int checkMSnFromTool(String orderNo, String mSN);

    /**
     * 检查产品SN是否有效，前三位在编码范围内
     * @param productSn
     * @return
     */
    public int checkProductSn(String productSn);

    /**
     * 获取设备工装数量
     * @param orderNo
     * @param device_name
     * @param bmu_id
     * @return
     */
    public int findToolDeviceCount(String orderNo, String device_name, int bmu_id);

    /**
     * 获取订单设备数量
     * @param orderNo
     * @param device_name
     * @param bmu_id
     * @return
     */
    public int findOrderDeviceCount(String orderNo, String device_name, int bmu_id);

    /**
     * 模块SN获取PCBA板扫码信息
     * @param mSN
     * @return
     */
    public List<Map<String, String>> findPcbaBySN(String mSN);

    /**
     * 模块SN前三位获取模块结构信息
     * @param mSN
     * @return
     */
    public List<Map<String, String>> findPcbaBySNCode(String mSN);

    /**
     * 模块SN码获取已扫码PCBA信息
     * @param mSN
     * @return
     */
    public Map<String, String> findOrderPcbaBySN(String mSN);

    /**
     * 检查模块SN是否在订单内
     * @param mSN
     * @param orderNo
     * @return
     */
    public boolean checkSNinOrder(String mSN, String orderNo);

    /**
     * 模块扫码获取模块SN信息
     * @param mSN
     * @param orderNo
     * @return
     */
    public Map<String, Object> findOrderProductForMiScann(String mSN, String orderNo);

    /**
     * 获取模块扫码信息
     * @param snId
     * @return
     */
    public SNInfo findSNInfoById(Long snId);

    /**
     * 获取模块扫码信息
     * @param pSN
     * @return
     */
    public SNInfo findSNInfoBySn(String pSN);

    /**
     * 获取订单中有效的产品SN数量
     * @param orderno
     * @param pSN
     * @return
     */
    public int findSNCount(String orderno, String pSN);

    /**
     * 获取订单中指定设备型号的总共数量
     * @param orderno
     * @param deviceCode
     * @return
     */
    public int findOrdetrDeviceSum(String orderno, String deviceCode);

    /**
     * 获取PCBA已扫码数量
     * @param orderno
     * @param deviceCode
     * @return
     */
    public int findPCBADeviceSum(String orderno, String deviceCode);

    /**
     * 获取模块SN
     * @param uuid
     * @return
     */
    public String findMSNByUUID(String uuid);
}
