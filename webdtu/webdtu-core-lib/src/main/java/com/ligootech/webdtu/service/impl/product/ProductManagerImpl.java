package com.ligootech.webdtu.service.impl.product;

import com.ligootech.webdtu.entity.core.HardwareVersion;
import com.ligootech.webdtu.entity.core.ProductType;
import com.ligootech.webdtu.repository.HardwareVersionDao;
import com.ligootech.webdtu.repository.ProductTypeDao;
import com.ligootech.webdtu.service.product.ProductManager;
import com.ligootech.webdtu.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2015/11/16 11:30.
 */
@Service("productManager")
public class ProductManagerImpl implements ProductManager {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductTypeDao productTypeDao;

    @Autowired
    private HardwareVersionDao hardwareVersionDao;

    @Override
    public List<Object[]> findSimpleProductList() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT pt.id,pt.prod_typename,(SELECT COUNT(id) FROM t_hardware_version WHERE STATUS=1 AND prod_type_id=pt.id ) AS hwnum,is_mainframe FROM prod_type pt ORDER BY pt.is_mainframe DESC, pt.id DESC");


        List<Object[]> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] rs = new Object[]{
                        resultSet.getString("id"),
                        resultSet.getString("prod_typename"),
                        resultSet.getString("hwnum"),
                        resultSet.getString("is_mainframe")
                };
                return rs;
            }
        });

        return list;
    }

    @Override
    public List<HardwareVersion> findHwVwesion(Long productId) {
        return hardwareVersionDao.findHWList(productId);
    }

    @Override
    public int saveProduct(ProductType productType, Long optUserId) {
        String optType = "新增";
        /*if (productType.getId() != null || productType.getId() > 0) {
            optType = "修改";
        }*/
        productTypeDao.save(productType);
        //日志记录
        String content = optType + "产品型号>>产品型号ID:" + productType.getId() + "，产品名称：" + productType.getProdTypename();
        String log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'PRODUCT', NOW(), '" + content + "')";

        String[] sqlArr = {log};
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);
        return rs.length;

    }

    @Override
    public int saveHardwareVersion(HardwareVersion hardwareVersion, Long optUserid) {
        String optType = "新增";
        /*if (hardwareVersion.getId() != null || hardwareVersion.getId() > 0) {
            optType = "修改";
        }*/
        hardwareVersionDao.save(hardwareVersion);
        //日志记录
        String content = optType + "产品型号>>版本号ID:" + hardwareVersion.getId() + "，版本号：" + hardwareVersion.getVersion()
                + "，产品类型ID：" + hardwareVersion.getProductType().getId();
        String log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserid + ", 'PRODUCT', NOW(), '" + content + "')";

        String[] sqlArr = {log};
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);
        return rs.length;
    }

    @Override
    public int delProduct(Long productId, Long optUserid) {
        //判断有使用情况
        //版本号表
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(id) AS hwnum FROM t_hardware_version WHERE prod_type_id=" + productId);

        List<String> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("hwnum");
            }
        });
        String rsCheck = list.get(0);

        if (!"0".equals(rsCheck)){
            return -1;
        }
        //sn 或者scan 表
        sql = new StringBuffer();
        sql.append("SELECT SUM(ptnum) AS ptnum FROM (")
                .append("SELECT COUNT(id) AS ptnum FROM t_sn_info WHERE prod_type=" + productId)
                .append(" UNION " )
                .append("SELECT COUNT(id) AS ptnum FROM t_sn_scan WHERE prod_type=" + productId)
                .append(") sn");

        list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("ptnum");
            }
        });
        rsCheck = list.get(0);
        if (!"0".equals(rsCheck)){
            return -2;
        }

        ProductType pt = productTypeDao.findOne(productId);

        productTypeDao.delete(productId);
        //日志记录
        String content = "产品型号删除>>产品ID:" + pt.getId() + "，产品型号：" + pt.getProdTypename();
        String log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserid + ", 'PRODUCT', NOW(), '" + content + "')";

        String[] sqlArr = {log};
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);
        return rs.length;
    }

    @Override
    public int delHardwareVersion(Long hardwareVersionId, Long optUserid) {
        HardwareVersion hw = hardwareVersionDao.findOne(hardwareVersionId);
        hardwareVersionDao.delete(hardwareVersionId);
        //日志记录
        String content = "版本号删除>>版本号ID:" + hw.getId() + "，版本号：" + hw.getVersion() + "，产品类型ID："
                + hw.getProductType().getId() + "，产品类型名称：" + hw.getProductType().getProdTypename();
        String log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserid + ", 'PRODUCT', NOW(), '" + content + "')";

        String[] sqlArr = {log};
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);
        return rs.length;
    }

    @Override
    public boolean checkProdName(String prodName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) AS ct FROM prod_type WHERE prod_typename='");
        sql.append(StringUtil.null2String(prodName).trim()).append("'");

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        String ct = StringUtil.null2String(map.get("ct"));
        int num = StringUtil.StringToInt(ct);
        if (num > 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean checkHWversion(Long prodId, String version) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) AS ct FROM t_hardware_version WHERE prod_type_id=");
        sql.append(prodId).append(" and version='");
        sql.append(StringUtil.null2String(version).trim()).append("'");

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        String ct = StringUtil.null2String(map.get("ct"));
        int num = StringUtil.StringToInt(ct);
        if (num > 0){
            return true;
        }
        return false;
    }

    /**
     * 获取软件列表
     * @return
     */
    @Override
    public List<Object[]> findSimpleSWList() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id,sw_name, file_code, file_name, remarks, status, opt_user_id, opt_time FROM t_sw_info where status=1 ORDER BY id DESC");

        List<Object[]> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] rs = new Object[]{
                        resultSet.getString("id"),
                        resultSet.getString("sw_name")
                };
                return rs;
            }
        });

        return list;
    }

    /**
     * 获取软件对应的硬件设备型号
     * @param swId
     * @return
     */
    @Override
    public List<Object[]> findHWListBySwId(Long swId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id,prod_typename FROM prod_type WHERE id IN (SELECT hw_id FROM t_sw_hw WHERE STATUS=1 AND sw_id=").append(swId).append(") order by id desc");

        List<Object[]> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] rs = new Object[]{
                        resultSet.getString("id"),
                        resultSet.getString("prod_typename")
                };
                return rs;
            }
        });

        return list;
    }

    /**
     * 未使用的硬件型号
     * @return
     */
    @Override
    public List<Object[]> findHWListNoUse() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id,prod_typename FROM prod_type WHERE id not IN (SELECT hw_id FROM t_sw_hw WHERE STATUS=1)");

        List<Object[]> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] rs = new Object[]{
                        resultSet.getString("id"),
                        resultSet.getString("prod_typename")
                };
                return rs;
            }
        });

        return list;
    }

    /**
     * 获取软件版本信息
     * @param swId
     * @return
     */
    @Override
    public List<Object[]> findSwVersionListBySwId(Long swId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id,sw_id, sw_version, status, file_code, file_name, update_note, (select full_name from dtu_user where dtu_user.id=tsv.opt_user_id) as opt_user_name, DATE_FORMAT(add_time, '%Y-%m-%d %H:%i') AS add_time, DATE_FORMAT(del_time, '%Y-%m-%d %H:%i') AS del_time from t_sw_version tsv where status<99 and sw_id='").append(swId).append("' order by id desc");

        List<Object[]> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] rs = new Object[]{
                        resultSet.getString("id"),
                        resultSet.getString("sw_version"),
                        resultSet.getString("status"),
                        resultSet.getString("file_code"),
                        resultSet.getString("file_name"),
                        resultSet.getString("update_note"),
                        resultSet.getString("opt_user_name"),
                        resultSet.getString("add_time"),
                        resultSet.getString("del_time")
                };
                return rs;
            }
        });

        return list;
    }

    /**
     * 判断软件型号是否重复
     * @param id
     * @param swName
     * @return
     */
    @Override
    public boolean checkSwName(String id, String swName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(id) AS ct FROM t_sw_info WHERE status=1 and sw_name='").append(swName).append("' ");
        if (!"-1".equals(id)) {
            sql.append(" and id <>").append(id);
        }

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        String ct = StringUtil.null2String(map.get("ct"));
        if ("0".equals(ct)){
            return false;
        }
        return true;
    }

    /**
     * 判断软件版本号是否重复
     * @param swid
     * @param swVersion
     * @return
     */
    @Override
    public boolean checkSwVersion(String swid, String swVersion) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(id) AS ct FROM t_sw_version WHERE status<99 and sw_version='").append(swVersion).append("' and sw_id=").append(swid);

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        String ct = StringUtil.null2String(map.get("ct"));
        if ("0".equals(ct)){
            return false;
        }
        return true;
    }

    /**
     * 保存软件型号
     *
     * @param id
     * @param swName
     * @param hwStr
     * @param optUserId
     * @return
     */
    @Override
    public int saveSwInfo(String id, String swName, String hwStr, Long optUserId) {
        List<String> sqlList = new ArrayList<String>();

        if ("-1".equals(id)){
            sqlList.add("insert into t_sw_info (sw_name, status, opt_user_id, opt_time) " +
                    "values('" + swName + "','1','" + optUserId + "',now())");
        }else{
            sqlList.add("update t_sw_info set sw_name='" + swName + "', opt_user_id=" + optUserId + ", opt_time=now() where id=" + id);
            //删除原硬件型号
            sqlList.add("update t_sw_hw set status=2,del_time=now() WHERE sw_id=" + id);
        }

        //添加新硬件
        String[] idsArr = hwStr.split(",");
        for (int i = 0; i <idsArr.length ; i++) {
            String str = StringUtil.null2String(idsArr[i]).trim();
            if (!"".equals(str)){
                sqlList.add("insert into t_sw_hw (sw_id, hw_id, status, add_time) values('" + id + "','" + str + "','1',now())");
            }
        }

        if ("-1".equals(id)){
            //修改新增的硬件的id
            sqlList.add("update t_sw_hw set sw_id=(select max(id) from t_sw_info) where sw_id=-1 ");
        }

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        int result = StringUtil.StringToInt(id);
        if ("-1".equals(id)){
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT max(id) AS new_id FROM t_sw_info ");
            if (!"-1".equals(id)) {
                sql.append(" and id <>").append(id);
            }

            Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
            String new_id = StringUtil.null2String(map.get("new_id"));
            result = StringUtil.StringToInt(new_id);
        }
        return result;
    }

    /**
     * 保存软件版本信息
     * @param swId
     * @param swVersion
     * @param fileCode
     * @param updateNote
     * @param optUserId
     * @return
     */
    @Override
    public int saveSwVersion(String swId, String swVersion, String fileCode, String updateNote, Long optUserId) {
        List<String> sqlList = new ArrayList<String>();
        /*****************************************
         * 更新以前版本号为禁用状态
         ****************************************/
        sqlList.add("update t_sw_version set status=2,del_time=now(),opt_user_id=" + optUserId + " where status=1 and sw_id=" + swId);

        /*****************************************
         * 添加新版本号
         ****************************************/
        sqlList.add("insert into t_sw_version (sw_id, sw_version, status, file_code, update_note, opt_user_id, add_time) " +
                "values('" + swId + "','" + swVersion + "','1','" + fileCode + "','" + StringUtil.null2String(updateNote) + "','" + optUserId + "',now())");

        /*****************************************
         * 更新文件名称
         ****************************************/
        if (!"".equals(fileCode)) {
            sqlList.add("update t_sw_version tsw set " +
                    "tsw.file_name=(SELECT MAX(tfu.file_name) FROM t_file_upload tfu WHERE tfu.file_code='" + fileCode + "'), " +
                    "tsw.md5_code=(SELECT MAX(tfu.md5_code) FROM t_file_upload tfu WHERE tfu.file_code='" + fileCode + "') " +
                    "where tsw.file_code='" + fileCode + "'");
        }

        /*****************************************
         * 更新文件名称到软件型号表
         ****************************************/
        if (!"".equals(fileCode)) {
            sqlList.add("update t_sw_info set file_code='" + fileCode + "', file_name=(SELECT MAX(tfu.file_name) FROM t_file_upload tfu WHERE tfu.file_code='" + fileCode + "') where id=" + swId);
        }else{
            sqlList.add("update t_sw_info set file_code='', file_name='' where id=" + swId);
        }

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs.length;
    }

    /**
     * 获取软件信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findSwInfoBySwId(Long id) {
        String sql = "select * from t_sw_info where id=" + id;
        return jdbcTemplate.queryForMap(sql);
    }

    /**
     * 软件版本号删除
     * @param id
     * @param optUserid
     * @return
     */
    @Override
    public int delSWVersion(Long id, Long optUserid) {
        List<String> sqlList = new ArrayList<String>();
        Map<String, Object> swVersion = findSWVersionById(id);
        String status = StringUtil.null2String(swVersion.get("status"));
        if("1".equals(status)){//当前可用状态的
            sqlList.add("UPDATE t_sw_version SET STATUS=99,del_time=NOW() WHERE id=" + id);

            String sw_id = StringUtil.null2String(swVersion.get("sw_id"));
            Map<String, Object> map = jdbcTemplate.queryForMap("SELECT IFNULL(MAX(id), 0) as max_id FROM t_sw_version WHERE status=2 and sw_id='" + sw_id + "' AND id<>" + id);
            String max_id = StringUtil.null2String(map.get("max_id"));
            if ("0".equals(max_id)){//无记录
                sqlList.add("update t_sw_info set file_code='', file_name='' where id=" + sw_id);
            }else{
                Map<String, Object> nextSwVersion = findSWVersionById(Long.valueOf(max_id));

                sqlList.add("UPDATE t_sw_version SET STATUS=1 WHERE id=" + StringUtil.null2String(nextSwVersion.get("id")));
                sqlList.add("update t_sw_info set file_code='" + StringUtil.null2String(nextSwVersion.get("file_code")) + "', file_name='" + StringUtil.null2String(nextSwVersion.get("file_name")) + "' where id=" + sw_id);
            }
        }else {
            sqlList.add("UPDATE t_sw_version SET STATUS=99,del_time=NOW() WHERE id=" + id);
        }

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs.length;
    }

    /**
     * 软件版本删除
     * @param swId
     * @param optUserid
     * @return
     */
    @Override
    public int delSWInfo(Long swId, Long optUserid) {
        List<String> sqlList = new ArrayList<String>();
        sqlList.add("UPDATE t_sw_info SET STATUS=2,opt_time=NOW(),opt_user_id=" + optUserid + " WHERE id=" + swId);
        sqlList.add("UPDATE t_sw_hw SET STATUS=2,del_time=NOW() WHERE status=1 AND sw_id=" + swId);
        sqlList.add("UPDATE t_sw_version SET STATUS=99,del_time=NOW() WHERE sw_id=" + swId);

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs.length;
    }

    /**
     * 获取软件版本号信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findSWVersionById(Long id) {
        String sql = "select id,sw_id,sw_version,status,file_code,file_name,update_note,(SELECT full_name FROM dtu_user WHERE dtu_user.id=tsv.opt_user_id ) AS opt_user_name," +
                "DATE_FORMAT(add_time, '%Y-%m-%d %H:%i') AS add_time,DATE_FORMAT(del_time, '%Y-%m-%d %H:%i') AS del_time FROM t_sw_version tsv where id=" + id;

        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        return map;
    }
}
