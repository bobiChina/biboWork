package com.ligootech.webdtu.service.impl.order;

import com.ligootech.webdtu.service.order.FileManager;
import com.ligootech.webdtu.util.FileOperateUtil;
import com.ligootech.webdtu.util.LoggerUtil;
import com.ligootech.webdtu.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wly on 2016/1/22 11:40.
 */
@Service("fileManager")
public class FileManagerImpl implements FileManager {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int saveUploadFile(Map<String, Object> map, Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO t_file_upload (file_code, file_name, md5_code, store_name, file_size, file_type,device_code,device_type, opt_user, opt_time) ");
        sql.append(" VALUES('").append(StringUtil.null2String(map.get(FileOperateUtil.FILEID))).append("',");
        sql.append(" '").append(StringUtil.null2String(map.get(FileOperateUtil.REALNAME))).append("',");
        sql.append(" '").append(StringUtil.null2String(map.get(FileOperateUtil.MD5CODE))).append("',");
        sql.append(" '").append(StringUtil.null2String(map.get(FileOperateUtil.STORENAME))).append("',");
        sql.append(" '").append(StringUtil.null2String(map.get(FileOperateUtil.SIZE))).append("',");
        sql.append(" '").append(StringUtil.null2String(map.get(FileOperateUtil.FILETYPE))).append("',");
        sql.append(" '").append(StringUtil.null2String(map.get("device_code"))).append("',");
        sql.append(" '").append(StringUtil.null2String(map.get("device_type"))).append("',");
        sql.append(" '").append(userId).append("', NOW())");

        jdbcTemplate.execute(sql.toString());

        return 0;
    }

    @Override
    public Map<String, String> getFileNameByReq(String orderNo, String fileType, String deviceCode, String bmuNo, boolean isTest) {
        StringBuffer sqlBuf = new StringBuffer();
        if (isTest){//找配置文件
            sqlBuf.append(" SELECT MAX(config_value) as file_code FROM t_base_config ")
                    .append(" WHERE STATUS = 1 and config_type=1 ") //类型为测试固件
                    .append("   AND config_name = '").append(deviceCode).append("' ");
        }else{
            sqlBuf.append(" SELECT  ")
                    .append("   MAX(file_code) AS file_code ")
                    .append(" FROM ")
                    .append("   t_order_file  ")
                    .append(" WHERE STATUS = 1  ")
                    .append("   AND start_time < NOW()  ")
                    .append("   AND orderno = '").append(orderNo).append("' ")
                    .append("   AND device_code = '").append(deviceCode).append("' ")
                    .append("   AND file_type = '").append(fileType).append("' ")
                    .append("   AND bmu_no =").append(bmuNo);
        }

        Map<String, Object> mapCount = jdbcTemplate.queryForMap(sqlBuf.toString());
        String file_code = null;
        if (mapCount != null) {
            file_code = StringUtil.null2String(mapCount.get("file_code"));
            if ("usetemplate".equals(file_code)){//使用模板
                logger.debug("工装下载文件使用模版>>>>设备型号：" + deviceCode);
                return getS19FileName(deviceCode);
            }
        }

        return getFileByFileCode(file_code);
    }

    /**
     * 获取模版固件
     *
     * @param deviceCode
     * @return
     */
    @Override
    public Map<String, String> getS19FileName(String deviceCode) {
        /*************************************
         * 从 prod_type 表找对应的ID
         * 通过prod_type 表获取的ID，找 t_sw_hw 对应的软件型号ID
         * t_sw_info 通过ID查询对i用那个的文件信息
         ************************************/
        String sql = "SELECT MAX(file_code) AS file_code FROM t_sw_info WHERE status=1 AND id=(SELECT IFNULL(sw_id, 0) FROM t_sw_hw WHERE status=1 AND hw_id=(SELECT IFNULL(MAX(id),0) FROM prod_type WHERE prod_typename='" + deviceCode + "'))";

        Map<String, Object> mapCount = jdbcTemplate.queryForMap(sql);

        return getFileByFileCode(StringUtil.null2String(mapCount.get("file_code")));
    }

    /**
     * 获取对应的文件ID和MD5编码
     * @param fileCode
     * @return
     */
    private Map<String, String> getFileByFileCode(String fileCode){
        if (fileCode == null || "".equals(fileCode)) {
            return null;
        }
        String sql = "SELECT file_code,md5_code FROM t_file_upload WHERE file_code='" + fileCode + "'";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        if (map == null) {
            return null;
        }else{
            String file_code = StringUtil.null2String(map.get("file_code"));
            if ("".equals(file_code)){
                return null;
            }
            Map<String, String> rs = new HashMap<String, String>();
            rs.put("file_code", file_code);
            rs.put("md5_code", StringUtil.null2String(map.get("md5_code")));
            return rs;
        }
    }

}
