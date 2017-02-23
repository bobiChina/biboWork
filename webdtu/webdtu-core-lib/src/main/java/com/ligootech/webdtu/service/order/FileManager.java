package com.ligootech.webdtu.service.order;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by wly on 2016/1/22 11:36.
 */
public interface FileManager {
    /**
     * 上传文件入库记录
     * @param map
     * @param userId
     * @return
     */
    public int saveUploadFile(Map<String, Object> map, Long userId);

    /**
     * 获取文件名
     * @param orderNo
     * @param fileType
     * @param deviceCode
     * @param bmuNo
     * @param isTest
     * @return
     */
    public  Map<String, String> getFileNameByReq(String orderNo, String fileType, String deviceCode, String bmuNo, boolean isTest);

    /**
     * 获取模版固件
     * @param deviceCode
     * @return
     */
    public Map<String, String> getS19FileName(String deviceCode);

}
