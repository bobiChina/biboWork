package com.ligootech.webdtu.service.product;

import com.ligootech.webdtu.entity.core.HardwareVersion;
import com.ligootech.webdtu.entity.core.ProductType;

import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2015/11/16 11:15.
 */
public interface ProductManager {

    /**
     * 获取产品型号
     * @return
     */
    public List<Object[]> findSimpleProductList();

    /**
     * 获取硬件版本号
     * @param productId
     * @return
     */
    public List<HardwareVersion> findHwVwesion(Long productId);

    /**
     * 保存产品型号
      * @param productType
     * @param optUserid
     * @return
     */
    public int saveProduct(ProductType productType, Long optUserid);

    /**
     * 保存硬件版本号
     * @param hardwareVersion
     * @param optUserid
     * @return
     */
    public int saveHardwareVersion(HardwareVersion hardwareVersion, Long optUserid);

    /**
     * 删除产品型号
     * @param optUserid
     * @param productId
     * @return
     */
    public int delProduct(Long productId, Long optUserid);

    /**
     * 删除硬件版本号
     * @param optUserid
     * @param hardwareVersionId
     * @return
     */
    public int delHardwareVersion(Long hardwareVersionId, Long optUserid);

    /**
     * 检查产品型号是否存在
     * @param prodName
     * @return
     */
    public boolean checkProdName(String prodName);

    /**
     * 检查版本号
     * @param prodId
     * @param version
     * @return
     */
    public boolean checkHWversion(Long prodId, String version);

    /**
     * 获取软件列表
     * @return
     */
    public List<Object[]> findSimpleSWList();

    /**
     * 获取软件对应的硬件设备型号
     * @param swId
     * @return
     */
    public List<Object[]> findHWListBySwId(Long swId);

    /**
     * 未使用的硬件型号
     * @return
     */
    public List<Object[]> findHWListNoUse();

    /**
     * 获取软件版本信息
     * @param swId
     * @return
     */
    public List<Object[]> findSwVersionListBySwId(Long swId);

    /**
     * 判断软件型号是否重复
     * @param id
     * @param swName
     * @return
     */
    public boolean checkSwName(String id, String swName);

    /**
     * 判断软件版本号是否重复
     * @param swid
     * @param swVersion
     * @return
     */
    public boolean checkSwVersion(String swid, String swVersion);

    /**
     * 保存软件型号
     * @param id
     * @param swName
     * @param hwStr
     * @param optUserId
     * @return
     */
    public int saveSwInfo(String id, String swName, String hwStr, Long optUserId );

    /**
     * 保存软件版本信息
     * @param swId
     * @param swVersion
     * @param fileCode
     * @param updateNote
     * @param optUserId
     * @return
     */
    public int saveSwVersion(String swId, String swVersion, String fileCode,String updateNote, Long optUserId );

    /**
     * 获取软件信息
     * @param id
     * @return
     */
    public Map<String, Object> findSwInfoBySwId(Long id);

    /**
     * 软件版本号删除
     * @param swId
     * @param optUserid
     * @return
     */
    public int delSWVersion(Long swId, Long optUserid);

    /**
     * 软件版本删除
     * @param swId
     * @param optUserid
     * @return
     */
    public int delSWInfo(Long swId, Long optUserid);

    /**
     * 获取软件版本号信息
     * @param id
     * @return
     */
    public Map<String, Object> findSWVersionById(Long id);

}
