package com.ligootech.webdtu.service.impl.order;

import com.ligootech.webdtu.entity.core.*;
import com.ligootech.webdtu.entity.core.clientForm.*;
import com.ligootech.webdtu.repository.*;
import com.ligootech.webdtu.service.account.ShiroUser;
import com.ligootech.webdtu.service.order.SNManager;
import com.ligootech.webdtu.util.BhGenerator;
import com.ligootech.webdtu.util.SNCodeUtil;
import com.ligootech.webdtu.util.StringUtil;
import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wly on 2015/10/27 10:29.
 */
@SuppressWarnings("ALL")
@Service("snManager")
public class SNManagerImpl extends GenericManagerImpl<SNInfo, Long> implements SNManager {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DtuUserDao dtuUserDao;

    @Autowired
    private SNInfoDao snInfoDao;

    @Autowired
    private SNScanDao snScanDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    OrderDao orderDao;

    @Autowired
    HardwareVersionDao hardwareVersionDao;

    @Autowired
    ProductTypeDao productTypeDao;

    @Autowired
    SnModuleDao snModuleDao;

    @Override
    public List<Object[]> findUserByFullname(String fullName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id,full_name FROM dtu_user ");

        if (fullName == null || "".equals(fullName.trim())) {
            sql.append(" ORDER BY id LIMIT 10");
        }
        else{
            sql.append(" WHERE full_name LIKE '%" + fullName.trim() + "%' ORDER BY id");
        }

        javax.persistence.Query query = em.createNativeQuery(sql.toString());
        return query.getResultList();
    }

    @Override
    public int insertSn(Map<String, String> snMap) {
        //查询产品类型
        StringBuffer sql = new StringBuffer("select max(id) from prod_type ");
        sql.append(" where prod_typename='").append(snMap.get("prod_type")).append("'");
        Long prod_typeId = 0L;
        javax.persistence.Query qr = em.createNativeQuery(sql.toString());
        List rsList = qr.getResultList();
        if (null != rsList && rsList.size() > 0){
            java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
            if (bi != null) {
                prod_typeId = bi.longValue();
            }
        }

        if (prod_typeId > 0) {
            sql = new StringBuffer();
            sql.append("insert into t_sn_scan (uuid, sn, orderno, userid, prod_type, hw_version, sw_version, opt_userid, status, opt_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?, now())");
            javax.persistence.Query query = em.createNativeQuery(sql.toString()).setParameter(1, snMap.get("uuid"))
                    .setParameter(2, snMap.get("sn"))
                    .setParameter(3, snMap.get("orderno"))
                    .setParameter(4, snMap.get("userid"))
                    .setParameter(5, prod_typeId)
                    .setParameter(6, snMap.get("hw_version"))
                    .setParameter(7, snMap.get("sw_version"))
                    .setParameter(8, snMap.get("optuserid"))
                    .setParameter(9, 0);
            int rs = query.executeUpdate();

            return rs;
        }else{
            return -99;
        }
    }



    @Override
    public int delSNById(Long id, Long optUserId) {

        SNInfo snInfo = snInfoDao.findOne(id);

        String[] sqlArr = new String[4];
        //修改sn状态 修改产品类型为默认值1，否则产品类型不能删除
        String str_sn = "UPDATE t_sn_info SET status=2,prod_type=null WHERE id=" + snInfo.getId();
        sqlArr[0] = str_sn;
        //解除dtu配置表的关联关系dtu_user_config
        //TODO 是否要删除DTU表数据？后期可会有UUID相同的情况？ 2015年12月15日17:29:41
        String str_dtu = "UPDATE dtu_user_config SET status=2,optuserid=" + optUserId + ",opttime=NOW() WHERE dtuid =(SELECT MAX(id) FROM dtu WHERE uuid='" + snInfo.getDtuUuid() + "')";
        sqlArr[1] = str_dtu;

        //  删除SN从属配置表信息
        String str_pcb = "UPDATE t_sn_pack SET STATUS=4 WHERE main_sn='" + StringUtil.null2String(snInfo.getTempSn()) + "' and status=1";
        sqlArr[2] = str_pcb;

        //日志记录
        String content = "SN删除>>订单编号:" + snInfo.getOrderno() + "，id:" + snInfo.getId() + "，SN码:" + snInfo.getSn() + "，UUID:" + snInfo.getUuid();
        String str_log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'ORDER', NOW(), '" + content + "')";
        sqlArr[3] = str_log;

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs.length;
    }

    @Override
    public int delSNById_scan(Long id, Long optUserId) {

        SNInfo snInfo = snInfoDao.findOne(id);

        String[] sqlArr = new String[5];
        //修改sn状态 修改产品类型为默认值1，否则产品类型不能删除
        String str_sn = "UPDATE t_sn_info SET status=2,prod_type=null WHERE id=" + snInfo.getId();
        sqlArr[0] = str_sn;
        //解除dtu配置表的关联关系dtu_user_config
        //TODO 是否要删除DTU表数据？后期可会有UUID相同的情况？ 2015年12月15日17:29:41
        String str_dtu = "UPDATE dtu_user_config SET status=2,optuserid=" + optUserId + ",opttime=NOW() WHERE dtuid =(SELECT MAX(id) FROM dtu WHERE uuid='" + snInfo.getDtuUuid() + "')";
        sqlArr[1] = str_dtu;

        //  恢复SN从属配置表信息
        String str_pcb = "UPDATE t_sn_pack SET status=0 WHERE main_sn='" + StringUtil.null2String(snInfo.getTempSn()) + "' and status=1";
        sqlArr[2] = str_pcb;


        //修改订单记录表信息
        sqlArr[3] = "UPDATE t_order_product SET status=2,p_sn='',mi_user_id='',mi_time=null WHERE orderno='" + snInfo.getOrderno() + "' AND m_sn='" + snInfo.getTempSn() + "' and status<99 ";

        //日志记录
        String content = "SN删除>>订单编号:" + snInfo.getOrderno() + "，id:" + snInfo.getId() + "，SN码:" + snInfo.getSn() + "，UUID:" + snInfo.getUuid();
        String str_log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'ORDER', NOW(), '" + content + "')";
        sqlArr[4] = str_log;

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs.length;
    }

    @Override
    public int delSNByIds(String ids, String orderno, ShiroUser optUser) {
        ArrayList<String> sqlList = new ArrayList<String>();
        String[] idsArr = ids.split(",");
        String idsSql = StringUtil.getStrsplit(idsArr);

        //SN表状态修改
        sqlList.add("UPDATE t_sn_info SET status=2 WHERE id in " + idsSql);

        //DTU用户配置表状态修改
        sqlList.add("UPDATE dtu_user_config SET status=2,optuserid=" + optUser.getId()
                + ",opttime=NOW() WHERE dtuid in (SELECT id FROM dtu WHERE uuid in (select distinct dtu_uuid from t_sn_info where id in " + idsSql + "))");

        //  修改PACK信息表数据状态
        sqlList.add("UPDATE t_sn_pack SET status=4 WHERE status=1 and main_sn in (select distinct temp_sn from t_sn_info where id in " + idsSql + " )");

        //获取SN码和UUID
        StringBuffer querySql = new StringBuffer("SELECT GROUP_CONCAT(sn) AS snall, GROUP_CONCAT(dtu_uuid) AS uuidall FROM t_sn_info WHERE id IN ");
        querySql.append(idsSql);
        Map<String, Object> map = jdbcTemplate.queryForMap(querySql.toString());

        //日志记录
        String content = "SN删除>>订单编号:" + orderno + "，id:" + ids + "，SN码:" + StringUtil.null2String(map.get("snall")) + "，UUID:" + StringUtil.null2String(map.get("uuidall"));
        sqlList.add("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUser.getId() + ", 'ORDER', NOW(), '" + content + "')");

        String[] sqlArr = new String[sqlList.size()];

        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);
        //返回修改状态结果条数
        return rs[0];
    }

    /**
     * 解除绑定
     * @param ids
     * @param orderno
     * @param optUser
     * @return
     */
    @Override
    public int unbind(String ids, String orderno, ShiroUser optUser) {
        /************************************************************
         * 解除产品SN与订单绑定关系，恢复PCBA版以前信息
         * 关联表：
         *      t_sn_pack 修改PCBA扫码信息状态0 为 2，并恢复当前状态为1 为 0
         *      t_tool_info 修改工装测试结果状态为2（不可用）
         *      t_sn_info 表状态为2
         *      dtu_user_config 表状态为2
         ***********************************************************/
        ArrayList<String> sqlList = new ArrayList<String>();
        String[] idsArr = ids.split(",");
        String idsSql = StringUtil.getStrsplit(idsArr);

        //SN表状态修改
        sqlList.add("UPDATE t_sn_info SET status=2 WHERE id in " + idsSql);

        //DTU用户配置表状态修改
        sqlList.add("UPDATE dtu_user_config SET status=2,optuserid=" + optUser.getId()
                + ",opttime=NOW() WHERE dtuid in (SELECT id FROM dtu WHERE uuid in (select distinct dtu_uuid from t_sn_info where id in " + idsSql + "))");

        //  修改PACK信息表数据状态 原来未使用的改为删除 原来已使用的不处理
        // sqlList.add("UPDATE t_sn_pack SET status=2 WHERE status=0 and main_sn in (select distinct temp_sn from t_sn_info where id in " + idsSql + " )");
        sqlList.add("UPDATE t_sn_pack SET status=0,module_sn='' WHERE status=1 and main_sn in (select distinct temp_sn from t_sn_info where id in " + idsSql + " )");

        //工装结果修改
        sqlList.add("UPDATE t_tool_info SET status=2 WHERE status=1 and sn in (select distinct temp_sn from t_sn_info where id in " + idsSql + " )");

        //获取SN码和UUID
        StringBuffer querySql = new StringBuffer("SELECT GROUP_CONCAT(sn) AS snall, GROUP_CONCAT(dtu_uuid) AS uuidall FROM t_sn_info WHERE id IN ");
        querySql.append(idsSql);
        Map<String, Object> map = jdbcTemplate.queryForMap(querySql.toString());

        //日志记录
        String content = "SN解除绑定>>订单编号:" + orderno + "，id:" + ids + "，SN码:" + StringUtil.null2String(map.get("snall")) + "，UUID:" + StringUtil.null2String(map.get("uuidall"));
        sqlList.add("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUser.getId() + ", 'ORDER', NOW(), '" + content + "')");

        String[] sqlArr = new String[sqlList.size()];

        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);
        //返回修改状态结果条数
        return rs[0];
    }

    @Override
    public int delScanById(Long scanid, Long optUserId) {
        SNScan snScan = snScanDao.findOne(scanid);

        String[] sqlArr = new String[2];
        //修改sn状态 修改产品类型为默认值1，否则产品类型不能删除
        String str_sn = "UPDATE t_sn_scan SET status=2,prod_type=null WHERE id=" + snScan.getId();
        sqlArr[0] = str_sn;

        //日志记录
        String content = "SNScan删除>>订单编号:" + snScan.getOrderno() + "，id:" + snScan.getId() + "，SN码:" + snScan.getSn() + "，UUID:" + snScan.getUuid();
        String str_log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'ORDER', NOW(), '" + content + "')";
        sqlArr[1] = str_log;

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs.length;
    }

    @Override
    public DtuUser findDtuUserByLogin(String userName, String userPass) {
        //查询userid
        StringBuffer sql = new StringBuffer("select max(id) from dtu_user ");
        sql.append(" where user_name='").append(userName).append("'");
        sql.append(" and user_pass='").append(userPass).append("'");

        javax.persistence.Query query = em.createNativeQuery(sql.toString());
        List rsList = query.getResultList();
        if (null != rsList && rsList.size() > 0){
            java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
            if (bi != null) {
                Long userid = bi.longValue();
                return dtuUserDao.findOne(userid);
            }
        }
        return null;
    }

    @Override
    public boolean findRepetitionFromScan(String uuid, String sn) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(id) FROM t_sn_scan WHERE STATUS=0 AND (uuid='" + uuid + "' OR sn='" + sn + "')");

        javax.persistence.Query query = em.createNativeQuery(sql.toString());
        List rsList = query.getResultList();
        if (null != rsList && rsList.size() > 0){
            java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
            if (bi != null) {
                Long count = bi.longValue();
                if (count > 0  ){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String findRepetitionFromSN(String sn) {
        //查询重复的SN码，并返回重复结果
        StringBuffer sql = new StringBuffer();
        //sql.append("SELECT COUNT(id) FROM t_sn_info WHERE STATUS=1 AND (dtu_uuid= RIGHT(REPLACE('" + uuid + "', '-', ''), 17) OR sn='" + sn + "')");
        sql.append("SELECT max(sn) as sncount FROM t_sn_info WHERE STATUS=1 AND sn='" + sn + "' ");

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        if (map != null) {
            Object rs = map.get("sncount");
            if (rs != null) {
                return rs.toString();
            }
        }


        /*String snSql =  StringUtil.getStrsplit(snList);
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT sn AS sn FROM t_sn_info WHERE STATUS=1 AND sn IN " + snSql)
                .append(" UNION ")
                .append(" SELECT pcb_sn AS sn FROM t_sn_pcb WHERE STATUS=1 AND pcb_sn IN " + snSql);

        List<String> list = jdbcTemplate.query(sqlBuf.toString(), new Object[]{}, new RowMapper(){
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("sn");
            }
        });
        StringBuffer rs = new StringBuffer();
        if (list != null && list.size() > 0){
            String rsStr = "";
            for (int i = 0; i < list.size(); i++) {
                rsStr = list.get(i);
                if (null != rsStr && rsStr.length()>0){
                    rs.append(rsStr).append("，");//使用中文逗号，防止json转换异常
                }
            }
        }

        if (rs.length() > 1){
            String rsStr = rs.toString();
            rsStr = rsStr.substring(0, rsStr.length()-1);//去除中文逗号
            return rsStr;
        }*/
        return null;
    }

    @Override
    public String findRepetitionFromUUID(String uuid, String sn) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT max(dtu_uuid) as uuid_count FROM t_sn_info WHERE STATUS=1 AND dtu_uuid= RIGHT(REPLACE('" + uuid + "', '-', ''), 17) ");

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        if (map != null) {
            Object rs = map.get("uuid_count");
            if (rs != null) {
                return rs.toString();
            }
        }
        return null;
    }

    @Override
    public int findRepetition(String uuid, String m_sn) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(0) as ct FROM t_sn_info WHERE STATUS=1 AND (dtu_uuid= RIGHT(REPLACE('" + uuid + "', '-', ''), 17) or temp_sn='" + m_sn + "') ");

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        if (map != null) {
           int count = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
           if (count > 0){
               return count;
           }
        }
        return -1;
    }

    @Override
    public boolean findPcbByMainSn(String main_sn) {
        StringBuffer sql = new StringBuffer("select count(id) as pcb_num from t_sn_pack WHERE status=0 AND main_sn='" + main_sn + "'");
        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());

        Object rs = map.get("pcb_num");
        if (rs != null) {
            int n = StringUtil.StringToInt(rs.toString(), 0);
            if (n > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean findPcbByMainSn(String main_sn, String orderNo) {
        if(findPcbByMainSn(main_sn)){
            return true;
        }

        StringBuffer sql = new StringBuffer("select count(id) as pcb_num from t_sn_pack WHERE status=1 AND main_sn=(SELECT MAX(temp_sn) AS sn FROM t_sn_info WHERE STATUS=1 AND orderno='" + orderNo + "' AND temp_sn='" + main_sn + "')");
        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());

        Object rs = map.get("pcb_num");
        if (rs != null) {
            int n = StringUtil.StringToInt(rs.toString(), 0);
            if (n > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Object[]> findSNListByOrderNo(String orderno) {
        return snInfoDao.findSNListByOrderNo(orderno);
    }

    @Override
    public List<SNInfo> findSNAllListByOrderNo(String orderno) {

        return snInfoDao.findSNAllListByOrderNo(orderno);
    }

    @Override
    public List<SNInfo> findSNNoDtuByOrderNo(String orderno) {
        return snInfoDao.findSNNoDtuByOrderNo(orderno);
    }

    @Override
    public List<SNScan> findScanAllListByOrderNo(String orderno) {
        return snScanDao.findSNAllListByOrderNo(orderno);
    }

    @Override
    public int setScan2DTU(String ids, String orderno, Long optUserId) {
        List<Order> orderList = orderDao.findOrderByOrderNo(orderno);
        Order order = null;
        if (orderList != null && orderList.size() > 0){
            order = orderList.get(0);
            if(null == order){
                return 0;
            }
        }else{
            return 0;
        }

        String[] idArr = ids.split(",");
        String str = "";
        StringBuffer idsSB = new StringBuffer("(");
        for (int i = 0; i < idArr.length; i++) {
            str = idArr[i];
            if (str != null && !"".equals(str)) {
                if (i == idArr.length - 1){
                    idsSB.append(str).append(")");
                }else{
                    idsSB.append(str).append(",");
                }
            }
        }

        String sql_uuid = "SELECT RIGHT(REPLACE(uuid, '-', ''), 17) as uuid from t_sn_scan WHERE id in " + idsSB.toString();

        String[] sqlArr = new String[6];
        //修改扫描表状态,改为已使用
        String sql_scan = "UPDATE t_sn_scan SET STATUS=1 WHERE id IN " + idsSB.toString();
        sqlArr[0] = sql_scan;

        //复制扫描表到SN表中
        String sql_sn = "INSERT INTO t_sn_info(sn,dtu_uuid,orderno,corpid,prod_type,hw_version,sw_version,STATUS,opt_userid,opt_time) " +
                "SELECT sn,RIGHT(REPLACE(uuid, '-', ''), 17) as uuid,orderno,userid,prod_type,hw_version,sw_version,1 AS STATUS, 1 AS opt_userid,NOW() AS opt_time FROM t_sn_scan WHERE id in " + idsSB.toString();
        sqlArr[1] = sql_sn;

        //修改订单状态为 已处理 1
        //String sql_order = "update t_order set status=1 where orderno='" + orderno + "'";// 订单状态定义已修改 wly 2016年4月14日 15:06:53
        //sqlArr[2] = sql_order;

        //添加DTU

        StringBuffer sql_dtu = new StringBuffer();
        sql_dtu.append("INSERT INTO dtu(alarm_status,balance_status,battery_recharge_cycles,battery_total_amp,battery_total_voltage,");
        sql_dtu.append("communi_status,insert_time,insulation_status,leak_elec,left_capacity,");
        sql_dtu.append("max_box_temper,max_single_box_id,max_single_string,max_single_voltage,max_temper_box_id,");
        sql_dtu.append("max_temper_string,min_box_temper,min_single_box_id,min_single_string,min_single_voltage,");
        sql_dtu.append("min_temper_box_id,min_temper_string,negative_resistance,online_status,");
        sql_dtu.append("out_charge,out_realease,out_stream,out_temper,positive_resistance,");
        sql_dtu.append("sim_card,soc,soc_high,soc_low,soh,");
        sql_dtu.append("temper_check_exeption,temper_diff_max,total_capacity,total_milege,total_resistance_value,");
        sql_dtu.append("total_voltage_high,total_voltage_low,uuid,voltage_check_exeption,voltage_diff_max,");
        sql_dtu.append("battery_model_id,dtu_user_id) ");
        sql_dtu.append("SELECT 0,0,0,0,0,");
        sql_dtu.append("0,NOW(),0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("13912345678,0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0, RIGHT(REPLACE(uuid, '-', ''), 17) as uuid,0,0,");
        sql_dtu.append(order.getBatteryModel().getId() + " AS battery_model_id,");
        sql_dtu.append(order.getCorp().getId() + " AS userid");
        sql_dtu.append(" FROM t_sn_scan WHERE id in ").append(idsSB.toString());
        sqlArr[2] = sql_dtu.toString();

        //添加车辆信息
        StringBuffer sql_vehicle = new StringBuffer();
        sql_vehicle.append("INSERT INTO vehicle(create_time,UUID,vehicle_number,dtu_id,dtu_user_id,vehicle_model_id,vehicle_type_id)");
        sql_vehicle.append(" SELECT NOW(), uuid,'', id,dtu_user_id, ");
        sql_vehicle.append(order.getVehicleModel().getId() + " AS vehicle_model_id,");
        sql_vehicle.append(order.getVehicleType().getId() + " AS vehicle_type_id");
        sql_vehicle.append(" FROM dtu ");
        sql_vehicle.append("WHERE uuid IN (" + sql_uuid + ") ");
        sqlArr[3] = sql_vehicle.toString();

        //分配到具体用户
        String sql_dtuuser = "INSERT INTO dtu_user_config(dtuid, userid, optuserid, opttime,STATUS) " +
                "SELECT id,dtu_user_id," + optUserId + " AS optuserid, NOW(), 1 FROM dtu WHERE uuid IN (" + sql_uuid + ") ";
        sqlArr[4] = sql_dtuuser;

        //日志记录
        String content = "SN绑定>>订单编号:" + orderno + "，扫描表ID：" + idsSB.toString();
        String log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'ORDER', NOW(), '" + content + "')";
        sqlArr[5] = log;

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs.length;
    }

    @Override
    public List<String> findHardwareVersion(String prodType) {
        StringBuffer sql = new StringBuffer("SELECT id,version FROM t_hardware_version WHERE status=1 AND prod_type_id=(SELECT MAX(id) FROM prod_type WHERE prod_typename='");
        sql.append(prodType).append("') ORDER BY version DESC");
        Object[] arg = new Object[]{};
        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("version");
            }
        });
        return list;
    }

    @Override
    public List<ProductType> findProdTypeList() {
        return productTypeDao.findAll();
    }

    @Override
    public Long findProdTypeByName(String prodName) {
        //查询产品类型
        StringBuffer sql = new StringBuffer("select max(id) from prod_type ");
        sql.append(" where prod_typename='").append(prodName).append("'");
        Long prod_typeId = 0L;
        javax.persistence.Query qr = em.createNativeQuery(sql.toString());
        List rsList = qr.getResultList();
        if (null != rsList && rsList.size() > 0){
            java.math.BigInteger bi = (java.math.BigInteger) rsList.get(0);
            if (bi != null) {
                return bi.longValue();
            }
        }
        return -8l;
    }

    @Override
    public int saveSnInfo(SNInfo obj) {
        List<Order> orderList = orderDao.findOrderByOrderNo(obj.getOrderno());
        Order order = null;
        if (orderList != null && orderList.size() > 0){
            order = orderList.get(0);
            if(null == order){
                obj.setId(-9l);
                return 0;
            }
        }else{
            return 0;
        }

        if ("00000000-0000-0000-0000-000000000000".equals(obj.getUuid())){
            obj.setDtuUuid("");
        }

        snInfoDao.save(obj);
        saveSNother(obj, order);
        return -99;
    }

    /**
     * 加锁防止取用SN时出现异常
     * @param obj
     * @return
     */
    @Override
    public synchronized int saveMiScanInfo(SNInfo obj) {

        if ("00000000-0000-0000-0000-000000000000".equals(obj.getUuid())){
            obj.setDtuUuid("");
        }
        /**********************************************
         * 生成产品SN
         *********************************************/
        String p_sql = "SELECT MAX(p_code) AS p_code, COUNT(0) AS ct FROM t_sn_product WHERE m_code='" + obj.getTempSn().substring(0, 3) + "'";
        Map<String, Object> p_map = jdbcTemplate.queryForMap(p_sql);
        int pcba_num = StringUtil.StringToInt(StringUtil.null2String(p_map.get("ct")));
        String p_code = StringUtil.null2String(p_map.get("p_code"));
        if ("".equals(p_code)){
            return -1;//无对应的产品SN编码
        }
        // p_code + "_" + SNCodeUtil.DATEFORMAT_MINI.format(new Date());
        String deviceMapKey = String.format("%s_%s", p_code, SNCodeUtil.DATEFORMAT_MINI.format(new Date()));
        Integer num = SNCodeUtil.deviceMap.get(deviceMapKey);
        if (num == null || num.intValue() == 0) {
            //此处不加status=1 考虑因素为SN不重复 本身不同订单同时生产时也会SN不连贯
            String sql = "SELECT COUNT(0) AS ct FROM t_sn_info WHERE SUBSTRING(sn, 1, 3)='" + p_code + "' and opt_time > DATE(NOW())";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql);
            int sn_num = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
            SNCodeUtil.deviceMap.put(deviceMapKey, sn_num);
            num = sn_num;
        }

        String p_sn = SNCodeUtil.getSerialNumber(num, p_code);
        SNCodeUtil.deviceMap.put(deviceMapKey, num + 1); //加一保持数量自增

        obj.setSn(p_sn);
        snInfoDao.save(obj);

        /**********************************************
         * 关联操作
         *********************************************/
        Long optUserId = obj.getOptUserid();

        List<String> sqlList = new ArrayList<String>();
        //TODO 是否删除原uuid所属客户的关联关系
        /**
         * 2015年12月22日11:41:10 修改
         //原SN码改为删除状态
         //解除原uuid的关联关系
         保留原操作痕迹 不修改原操作人和操作时间 wly 2016年7月12日 16:12:47
         **/
        String str_sn_update = "UPDATE t_sn_info SET status=2,prod_type=null WHERE temp_sn='" + obj.getTempSn() + "' and status=1 and id<>" + obj.getId();
        sqlList.add(str_sn_update);

        if (!"".equals(obj.getDtuUuid())){ // UUID合法时才会根据UUID更新状态 2015年12月30日15:00:58
            String str_uuid_update = "UPDATE t_sn_info SET status=2,prod_type=null WHERE dtu_uuid='" + obj.getDtuUuid() + "' and status=1 and id<>" + obj.getId();
            sqlList.add(str_uuid_update);
        }

        //原pcb板信息信息处理  默认为重新扫模块SN码，修改原先的扫码信息状态
        String sql_pack_update_main = "UPDATE t_sn_pack set STATUS=3 WHERE STATUS=1 AND main_sn='" + obj.getTempSn() + "'";
        sqlList.add(sql_pack_update_main);

        //绑定pack表PCB板信息
        String sql_pack_add = "UPDATE t_sn_pack set STATUS=1,module_sn='" + obj.getSn() + "' WHERE status=0 AND main_sn='" + obj.getTempSn() + "'";
        sqlList.add(sql_pack_add);

        String uuid = StringUtil.null2String(obj.getUuid());
        //修改订单产品表信息
        sqlList.add("UPDATE t_order_product SET status=3,uuid=RIGHT(REPLACE('" + uuid + "', '-', ''), 17),uuid_all='" + uuid + "',p_sn='" + obj.getSn() + "',sw_version='" + obj.getSwVersion() + "',mi_user_id='" + obj.getOptUserid() + "',mi_time=now() WHERE orderno='" + obj.getOrderno() + "' AND m_sn='" + obj.getTempSn() + "' and status<99 ");

        //日志记录
        String content = "SN绑定>>订单编号:" + obj.getOrderno() + "，UUID：" + obj.getUuid()
                + "，SN码：" + obj.getSn() + "，临时SN码：" + obj.getTempSn() + "，DTUUUID码：" + obj.getDtuUuid()
                + "，硬件版本号：" + obj.getHwVersion()  + "，软件版本号：" + obj.getSwVersion()  + "，产品型号ID：" + obj.getProdType().getId();
        String sql_log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'SNSCAN', NOW(), '" + content + "')";
        sqlList.add(sql_log);

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return Integer.valueOf(obj.getId() + "");
    }

    @Override
    public int updateGoodsCode(String pSN, String goodsCode) {
        String sql = "UPDATE t_sn_info SET goods_code=? WHERE sn=?";
        Object[] args = new Object[]{goodsCode, pSN};

        int rs = jdbcTemplate.update(sql, args );

        return rs;
    }

    @Override
    public int sn2DTU(String orderno, String snids, Long userId, Long optUserId) {
        List<Order> orderList = orderDao.findOrderByOrderNo(orderno);
        Order order = null;
        if (orderList != null && orderList.size() > 0){
            order = orderList.get(0);
            if(null == order){
                return 0;
            }
        }else{
            return 0;
        }

        DtuUser dtuUser = dtuUserDao.findOne(userId);

        String[] idArr = snids.split(",");
        String str = "";
        String idsSB = StringUtil.getIdsplit(idArr);

        String[] sqlArr = new String[4];
        //添加DTU
       StringBuffer sql_dtu = new StringBuffer();
        sql_dtu.append("INSERT INTO dtu(alarm_status,balance_status,battery_recharge_cycles,battery_total_amp,battery_total_voltage,");
        sql_dtu.append("communi_status,insert_time,insulation_status,leak_elec,left_capacity,");
        sql_dtu.append("max_box_temper,max_single_box_id,max_single_string,max_single_voltage,max_temper_box_id,");
        sql_dtu.append("max_temper_string,min_box_temper,min_single_box_id,min_single_string,min_single_voltage,");
        sql_dtu.append("min_temper_box_id,min_temper_string,negative_resistance,online_status,");
        sql_dtu.append("out_charge,out_realease,out_stream,out_temper,positive_resistance,");
        sql_dtu.append("sim_card,soc,soc_high,soc_low,soh,");
        sql_dtu.append("temper_check_exeption,temper_diff_max,total_capacity,total_milege,total_resistance_value,");
        sql_dtu.append("total_voltage_high,total_voltage_low,uuid,voltage_check_exeption,voltage_diff_max,");
        sql_dtu.append("battery_model_id,dtu_user_id) ");
        sql_dtu.append("SELECT 0,0,0,0,0,");
        sql_dtu.append("0,NOW(),0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("13912345678,0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0, dtu_uuid,0,0,");
        sql_dtu.append(order.getBatteryModel().getId() + " AS battery_model_id,");
        sql_dtu.append(userId + " AS userid");
        sql_dtu.append(" FROM t_sn_info WHERE status=1 and length(dtu_uuid)>1 and id in ").append(idsSB);
        // sql_dtu.append(" and dtu_uuid not in (select uuid from dtu)");//添加限制 已添加d过的DTU的不再添加 2015年12月25日16:05:15 页面中无法再选中 所以此处限制条件可不加
        sqlArr[0] = sql_dtu.toString();

        //添加车辆信息
        StringBuffer sql_vehicle = new StringBuffer();
        sql_vehicle.append("INSERT INTO vehicle(create_time,UUID,vehicle_number,dtu_id,dtu_user_id,vehicle_model_id,vehicle_type_id)");
        sql_vehicle.append(" SELECT NOW(), uuid,'', id,dtu_user_id, ");
        sql_vehicle.append(order.getVehicleModel().getId() + " AS vehicle_model_id,");
        sql_vehicle.append(order.getVehicleType().getId() + " AS vehicle_type_id");
        sql_vehicle.append(" FROM dtu ");
        sql_vehicle.append("WHERE uuid in (select dtu_uuid from t_sn_info where status=1 and length(dtu_uuid)>1 and id in " + idsSB.toString() + " ) ");
        sqlArr[1] = sql_vehicle.toString();

        //分配到具体用户
        String sql_dtuuser = "INSERT INTO dtu_user_config(dtuid, userid, optuserid, opttime,STATUS) " +
                "SELECT id,dtu_user_id," + optUserId + " AS optuserid, NOW(), 1 FROM dtu " +
                "WHERE uuid in (select dtu_uuid from t_sn_info where status=1 and id in " + idsSB.toString() + ")";
        sqlArr[2] = sql_dtuuser;

        String content = "用户添加设备>>用户ID:" + userId + "，" +
                "用户账号：" + dtuUser.getUserName() + "，" +
                "公司名称：" + dtuUser.getCorp().getCorpName() + "，" +
                "SN设备ID：" + snids + "，" +
                "UUID：" + getUuidBySnid(idsSB.toString()) + "，" +
                "订单编号：" + orderno + "。";
        String sql_log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'ORDER', NOW(), '" + content + "')";
        sqlArr[3] = sql_log;

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);
        return rs.length;
    }

    @Override
    public int savePCBSN(String main_sn, String pcb_sns, String optUserId) {
        //判断PCB板SN码是否可用
        String[] pcb_snArr = pcb_sns.trim().split(",");
        ArrayList<String> pcbSnList = new ArrayList<String>();
        if (pcb_snArr != null && pcb_snArr.length>0){
            String str = "";
            for (int i = 0; i <pcb_snArr.length; i++) {
                str = StringUtil.null2String(pcb_snArr[i]).trim();
                if (!"".equals(str)){
                    pcbSnList.add(str);
                }
            }
        }
        //pcba板允许为空 2015年12月30日13:28:36
        /*if (pcbSnList.size() == 0){
            return -4;
        }*/

        ArrayList<String> sqlList = new ArrayList<String>();
        //修改原临时SN码数据为作废
        sqlList.add("UPDATE t_sn_pack set status=2 WHERE status=0 AND main_sn='" + main_sn + "'");

        //添加新的PCB板SN信息
        if (pcbSnList.size() == 0){
            //PCBA板为空时 插入一条pcba字段为空 的数据
            sqlList.add("INSERT INTO t_sn_pack (main_sn, pcb_sn, status, opt_user_id, opt_time) VALUES('" + main_sn + "','', 0," + optUserId + ",NOW())");
        }else{
            for (int i = 0; i < pcbSnList.size(); i++) {
                sqlList.add("INSERT INTO t_sn_pack (main_sn, pcb_sn, status, opt_user_id, opt_time) VALUES('" + main_sn + "','" + StringUtil.null2String(pcbSnList.get(i)) + "', 0," + optUserId + ",NOW())");
            }
        }

        //修改本次扫码的pcba板类型
        String sqlPcbaName = "UPDATE t_sn_pack pack SET pack.pcb_name=(SELECT MAX(pcba.pcba_type) FROM t_sn_pcba pcba WHERE pcba.pcba_code=SUBSTRING(pack.pcb_sn,1,3)) where main_sn='" + main_sn + "' and status=0 ";
        sqlList.add(sqlPcbaName);
        //日志记录
        String content = "PCB板SN录入>>模块SN:" + main_sn + "，PCB板SN：" + pcb_sns ;               ;
        String log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'SNSCAN', NOW(), '" + content + "')";
        sqlList.add(log);

        //兼容老数据
        //"51-",        "BC52A",        "BC52B",        "BL51A",        "BL51B"
/*  不兼容老PCBA板 可以允许PCBA板信息为空 2015年12月30日13:32:56
        if (pcb_sns.indexOf("51-") > 0){
            sqlList.add("UPDATE t_sn_pack pack SET pack.pcb_name=SUBSTRING(pcb_sn,INSTR(pcb_sn,'51-'), 5) where main_sn='" + main_sn + "' and pcb_sn like '%51-%'");
        }
        if (pcb_sns.indexOf("BC52A") > 0){
            sqlList.add("UPDATE t_sn_pack pack SET pack.pcb_name='BC52A' where main_sn='" + main_sn + "' and pcb_sn like '%BC52A%' ");
        }
        if (pcb_sns.indexOf("BC52B") > 0){
            sqlList.add("UPDATE t_sn_pack pack SET pack.pcb_name='BC52B' where main_sn='" + main_sn + "' and pcb_sn like '%BC52B%' ");
        }
        if (pcb_sns.indexOf("BL51A") > 0){
            sqlList.add("UPDATE t_sn_pack pack SET pack.pcb_name='BL51A' where main_sn='" + main_sn + "' and pcb_sn like '%BL51A%' ");
        }
        if (pcb_sns.indexOf("BL51B") > 0){
            sqlList.add("UPDATE t_sn_pack pack SET pack.pcb_name='BL51B' where main_sn='" + main_sn + "' and pcb_sn like '%BL51B%' ");
        }*/

        String[] dbArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            dbArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(dbArr);
        if (rs.length > 0){
            return pcbSnList.size();
        }
        return -5;
    }

    @Override
    public int savePCBSN_B(String main_sn, String pcb_sns, String orderno, String hw_version, String optUserId) {
        //判断PCB板SN码是否可用
        String[] pcb_snArr = pcb_sns.trim().split(",");
        ArrayList<String> pcbSnList = new ArrayList<String>();
        if (pcb_snArr != null && pcb_snArr.length>0){
            String str = "";
            for (int i = 0; i <pcb_snArr.length; i++) {
                str = StringUtil.null2String(pcb_snArr[i]).trim();
                if (!"".equals(str)){
                    pcbSnList.add(str);
                }
            }
        }

        ArrayList<String> sqlList = new ArrayList<String>();
        //修改原临时SN码数据为作废
        sqlList.add("UPDATE t_sn_pack set status=2 WHERE status<2 AND main_sn='" + main_sn + "'");
        //取消原工装信息记录
        sqlList.add("UPDATE t_tool_info set status=2 WHERE status=1 AND sn='" + main_sn + "'");
        //取消原模块扫码信息
        sqlList.add("UPDATE t_sn_info set status=2 WHERE status=1 AND temp_sn='" + main_sn + "'");
        //取消原订单产品表记录重新添加
        sqlList.add("UPDATE t_order_product set status=99 WHERE status<99 AND m_sn='" + main_sn + "'");

        if (pcbSnList.size() > 0){ //现有库存可能出现未贴PCBA标签的板子
            //添加新的PCB板SN信息
            for (int i = 0; i < pcbSnList.size(); i++) {
                sqlList.add("INSERT INTO t_sn_pack (orderno, main_sn, pcb_sn, status, opt_user_id, opt_time) VALUES('" + orderno + "','" + main_sn + "','" + StringUtil.null2String(pcbSnList.get(i)) + "', 0," + optUserId + ",NOW())");
            }

            //修改本次扫码的pcba板类型
            String sqlPcbaName = "UPDATE t_sn_pack pack SET pack.pcb_name=(SELECT MAX(pcba.pcba_type) FROM t_sn_pcba pcba WHERE pcba.pcba_code=SUBSTRING(pack.pcb_sn,1,3)) where main_sn='" + main_sn + "' and status=0 ";
            sqlList.add(sqlPcbaName);
        }else{
            sqlList.add("INSERT INTO t_sn_pack (orderno, main_sn, pcb_sn, status, opt_user_id, opt_time) VALUES('" + orderno + "','" + main_sn + "','', 0," + optUserId + ",NOW())");
        }

        //添加到订单产品表中
        StringBuffer sqlBff = new StringBuffer();
        sqlBff.append("INSERT INTO t_order_product (orderno, m_sn, hw_version, bi_user_id, bi_time, status, device_code) ");
        sqlBff.append("SELECT '").append(orderno).append("' AS orderno,'").append(main_sn).append("' AS m_sn,'").append(hw_version)
                .append("' AS hw_version ,'").append(optUserId).append("' AS bi_user_id,NOW() AS bi_time,'1' AS STATUS, m_type FROM t_sn_module WHERE m_code='")
                .append(main_sn.substring(0, 3)).append("'");
        sqlList.add(sqlBff.toString());

        //日志记录
        String content = "PCB板SN录入>>订单编号：" + orderno + "，模块SN:" + main_sn + "，硬件版本号：" + hw_version + "，PCB板SN：" + pcb_sns ;               ;
        String log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'SNSCAN', NOW(), '" + content + "')";
        sqlList.add(log);

        String[] dbArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            dbArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(dbArr);
        if (rs.length > 0){
            return rs.length;
        }
        return -2;
    }

    @Override
    public int checkPcbaForBi(String m_sn, String pcb_sns) {
        //判断PCB板SN码是否可用
        String[] pcb_snArr = pcb_sns.trim().split(",");
        ArrayList<String> pcbSnList = new ArrayList<String>();
        if (pcb_snArr != null && pcb_snArr.length>0){
            String str = "";
            for (int i = 0; i <pcb_snArr.length; i++) {
                str = StringUtil.null2String(pcb_snArr[i]).trim();
                if (!"".equals(str)){
                    pcbSnList.add(str);
                }
            }
        }

        if (pcbSnList.size() == 0){
            return -1;
        }
        String idsSql = StringUtil.getStrsplit(pcbSnList);
        //已绑定的PCBA 订单编号不为空
        String sql = "SELECT COUNT(*) AS ct FROM t_sn_pack WHERE LENGTH(orderno)>10 and STATUS<2 AND pcb_sn IN" + idsSql;

        Map<String, Object> map = jdbcTemplate.queryForMap(sql);

        if (map != null) {
            int num = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
            if (num > 0){
                return num;
            }else{
                //判断已做模块SN解绑操作的PCBA 同一个模块集体上报才为有效
                String sql_pcba = "SELECT main_sn,COUNT(*) AS ct FROM t_sn_pack WHERE STATUS<2 AND pcb_sn IN " + idsSql + " GROUP BY main_sn";

                Object[] arg = new Object[]{ };
                List<String[]> list = jdbcTemplate.query(sql_pcba, arg, new RowMapper() {
                    @Override
                    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                        String[] arr = new String[2];
                        arr[0] = resultSet.getString("main_sn");
                        arr[1] = resultSet.getString("ct");
                        return arr;
                    }
                });

                if (list == null || list.size() == 0){
                    return 0;
                }else if(list.size()>1){
                    return list.size();// 不同属于一个模块的不能通过
                }else{
                    String[] arr = list.get(0);
                    int rsNum = StringUtil.StringToInt(arr[1]);
                    String db_mSN = StringUtil.null2String(arr[0]);
                    //模块SN不相同时 说明模块SN码被替换  不通过
                    if (!db_mSN.equalsIgnoreCase(m_sn)){
                        return rsNum;
                    }
                    //模块SN码相同， 数量不相等时也不予通过
                    if (rsNum != pcbSnList.size()){
                        return rsNum;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public String findNoPassPcbaMSn(String pcb_sns) {
        String sql = "SELECT GROUP_CONCAT(main_sn) AS m_sn FROM (SELECT main_sn FROM t_sn_pack WHERE STATUS<2 AND pcb_sn IN " + StringUtil.getStrsplit(pcb_sns.split(","))  + " GROUP BY main_sn) a";

        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        if (map != null && map.size()>0) {
            return StringUtil.null2String(map.get("m_sn"));
        }
        return "";
    }

    @Override
    public List<Map<String, String>> findPcbSn(String main_sn) {
        StringBuffer sql = new StringBuffer("SELECT id,pcb_sn,pcb_name FROM t_sn_pack WHERE STATUS=? AND main_sn=? ORDER BY id");
        Object[] arg = new Object[]{ 0, main_sn};
        List<Map<String, String>> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                map.put("pcb_sn", resultSet.getString("pcb_sn"));
                map.put("pcb_name", resultSet.getString("pcb_name"));
                return map;
            }
        });
        return list;
    }

    @Override
    public List<String> findUuidDB(String uuids) {
        List<String> rsList = new ArrayList<String>();

        String[] uuidArr = uuids.split(",");
        StringBuffer sql = new StringBuffer("SELECT distinct uuid FROM t_sn_info WHERE STATUS=1 AND UUID IN");
        sql.append(StringUtil.getStrsplit(uuidArr));

        Object[] arg = new Object[]{ };

        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("uuid");
            }
        });

        //简单判断 是否存在没有UUID入库的情况
        if (list.size() > 0 ){
            for (int i = 0; i < uuidArr.length; i++) {
                String str = StringUtil.null2String(uuidArr[i]).trim();
                if (!"".equals(str) && ! list.contains(str)){
                    // 数据库没有有效值时返回相关的UUID
                    rsList.add(str);
                }
            }
        }else{
            for (int i = 0; i < uuidArr.length; i++) {
                String str = StringUtil.null2String(uuidArr[i]).trim();
                if (!"".equals(str)){
                    // 数据库没有有效值时返回相关的UUID
                    rsList.add(str);
                }
            }
        }
        return rsList;
    }

    @Override
    public List<String> findSnDB(String sns) {
        List<String> rsList = new ArrayList<String>();
        String[] snArr = sns.split(",");
        StringBuffer sql = new StringBuffer("SELECT distinct sn FROM t_sn_info WHERE STATUS=1 AND sn IN");
        sql.append(StringUtil.getStrsplit(snArr));

        Object[] arg = new Object[]{ };

        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("sn");
            }
        });

        //简单判断 是否存在没有UUID入库的情况
        if (list.size() > 0 ){
            for (int i = 0; i < snArr.length; i++) {
                String str = StringUtil.null2String(snArr[i]).trim();
                if (!"".equals(str) && ! list.contains(str)){
                    //SN不为空 并且 数据库没有有效值时返回相关的SN
                    rsList.add(str);
                }
            }
        }else{//添加数据库无任何记录时的处理
            for (int i = 0; i < snArr.length; i++) {
                String str = StringUtil.null2String(snArr[i]).trim();
                if (!"".equals(str)){
                    // 数据库没有有效值时返回相关的UUID
                    rsList.add(str);
                }
            }
        }
        return rsList;
    }

    @Override
    public int checkModuleSNPcbaSN(String moduleSn, String pcba_sns) {

        moduleSn = StringUtil.null2String(moduleSn).trim();
        String moduleCode = moduleSn.length() > 3 ? moduleSn.substring(0, 3) : "";

        SnModule snModule = snModuleDao.findSnModuleByCode(moduleCode);
        if (null == snModule){
            return -11;//模块sn信息查找失败
        }

        if (!"".equals(pcba_sns)){
            String sql = "SELECT SUM(pcba_number) AS num, COUNT(*) AS ct FROM t_sn_module_pcba WHERE m_code='" + moduleCode + "'";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql);

            int db_num = StringUtil.StringToInt(StringUtil.null2String(map.get("num")));
            if (pcba_sns.endsWith(",")){
                pcba_sns = pcba_sns.substring(0, (pcba_sns.length() -1));
            }
            String[] pcba_sns_arr = pcba_sns.split(",");
            List<String> pcbaList = new ArrayList<String>();
            for (int i = 0; i <pcba_sns_arr.length; i++) {
                String str = StringUtil.null2String(pcba_sns_arr[i]).trim();
                if (!"".equals(str)){
                    pcbaList.add(str);
                }
            }

            int len = pcbaList.size() ;
            if (db_num != len){
                return -12; // 与规定数量不匹配
            }
        }else{
            return -12;
        }

        /* 暂时屏蔽 2015年12月30日13:27:23
        //提取pcba板前三个标识位
        ArrayList<String> pcbaSnList = new ArrayList<String>();
        String[] snArr = pcba_sns.split(",");

        for (int i = 0; i < snArr.length; i++) {
            String str = StringUtil.null2String(snArr[i]).trim();
            if (!"".equals(str)){
                pcbaSnList.add(str.substring(0, 3));
            }
        }

        //PCBA板总数量是否匹配
        int allSn = pcbaSnList.size();
        int allSn_db = snModule.getNumMain() + snModule.getNumCollection() + snModule.getNumBalance() + snModule.getNumThalposis() + snModule.getNumDtu();
        if (allSn != allSn_db){
            return -12;
        }

        //判断主板是否匹配
        StringBuffer sql_main_pcba = new StringBuffer();
        sql_main_pcba.append("SELECT COUNT(0) AS ct FROM t_sn_pcba WHERE pcba_code= '")
                .append(snModule.getPcbaCode()).append("' AND pcba_code IN ").append(StringUtil.getStrsplit(pcbaSnList));
        Map<String, Object> map = jdbcTemplate.queryForMap(sql_main_pcba.toString());
        //结果为1时才是正确结果
        int pcba_main = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
        if (1 != pcba_main){
            return -13; //主板不匹配
        }

        //判断数量是否匹配 1-主板 2-采集板 3-均衡板 4-温感板 5-DTU
        StringBuffer sql_sub_pcba = new StringBuffer();
        sql_sub_pcba.append("SELECT pcba_class,COUNT(pcba_class) AS ct FROM t_sn_pcba WHERE pcba_code IN ")
                .append(StringUtil.getStrsplit(pcbaSnList)).append(" GROUP BY pcba_class");

        Map<String, Integer> mapAll = new HashMap<String, Integer>();
        List<Map<String, Integer>> list = jdbcTemplate.query(sql_sub_pcba.toString(), new Object[]{}, new RowMapper(){
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Map<String, Integer> map_db = new HashMap<String, Integer>();
                map_db.put("PCBA_" + resultSet.getString("pcba_class"), resultSet.getInt("ct"));
                return map_db;
            }
        });
        //所有map结果放到一起
        if (list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                mapAll.putAll(list.get(i));
            }
        }

        if (snModule.getNumCollection() > 0){//采集板
            Integer num_db = mapAll.get("PCBA_2");
            if (num_db == null || snModule.getNumCollection() != num_db.intValue()){
                return -14;//采集板数量不正确
            }
        }

        if (snModule.getNumBalance() > 0){//均衡板
            Integer num_db = mapAll.get("PCBA_3");
            if (num_db == null || snModule.getNumBalance() != num_db.intValue()){
                return -15;//均衡板数量不正确
            }
        }

        if (snModule.getNumThalposis() > 0){//温感板
            Integer num_db = mapAll.get("PCBA_4");
            if (num_db == null || snModule.getNumThalposis() != num_db.intValue()){
                return -16;//温感板数量不正确
            }
        }

        if (snModule.getNumDtu() > 0){//DTU板
            Integer num_db = mapAll.get("PCBA_5");
            if (num_db == null || snModule.getNumDtu() != num_db.intValue()){
                return -17;//DTU板数量不正确
            }
        }
*/
        return 0;
    }

    @Override
    public int checkModuleSN(String moduleSn, String pcba_sn) {
        moduleSn = StringUtil.null2String(moduleSn).trim();
        String moduleCode = moduleSn.length() > 3 ? moduleSn.substring(0, 3) : "";
        SnModule snModule = snModuleDao.findSnModuleByCode(moduleCode);
        if (null == snModule){
            return -1;//模块sn信息查找失败
        }

        pcba_sn = StringUtil.null2String(pcba_sn).trim();
        //两种特殊情况  [BM51A    M51(2)XX ]  [BL51B LDM ]
        Map<String, String> map = new HashMap<String, String>();
        map.put("LDM", "BL51A");
        // BM5112A  BM5124A BM5136A BM5148A BM5160A BM5112AT BM5124AT BM5136AT BM5148AT
        map.put("M51(2)12", "BM5112A");
        map.put("M51(2)24", "BM5124A");
        map.put("M51(2)36", "BM5136A");
        map.put("M51(2)48", "BM5148A");
        map.put("M51(2)60", "BM5160A");
        map.put("M51(2)12T", "BM5112AT");
        map.put("M51(2)24T", "BM5124AT");
        map.put("M51(2)36T", "BM5136AT");
        map.put("M51(2)48T", "BM5148AT");

        String oldPcba = map.get(pcba_sn);

        if (null != oldPcba){ //属于特殊老PCBA板
            if (! oldPcba.equals(snModule.getType())) { //不匹配时
                return -2;//模块sn不匹配
            }
        }else{
            if (! pcba_sn.equals(snModule.getType())){ //不匹配时
                /**
                 * BL51B 分高压和低压两种情况
                 * BL51B(H) BL51B(L)
                 * sby 2016年6月3日 20:27:45
                 */
                if("BL51B".equals(pcba_sn)){
                    if (StringUtil.null2String(snModule.getType()).indexOf("BL51B") == 0){

                    }else{
                        return -2;//模块sn不匹配
                    }
                }else{
                    return -2;//模块sn不匹配
                }
            }
        }
        return 0;
    }

    @Override
    public int saveSystemJoint(SystemJoint obj) {

        if (obj == null) {
            return -1;
        }

        //分解模块信息
        List<SystemJointInfo> lists = obj.getLists();
        if (lists == null || lists.size() == 0) {
            return -2;
        }

        ArrayList<String> sqlList = new ArrayList<String>();
        String total_bh = BhGenerator.getBh();
        for (int i = 0; i < lists.size(); i++) {
            SystemJointInfo systemJointInfo = lists.get(i);

            String bh = BhGenerator.getBh();
            String uuid = StringUtil.null2String(systemJointInfo.getUuid());

            //修改原模块的状态为废弃  存在uuid全为0或者为空的情况
            //sqlList.add("update t_system_joint set status=2 where uuid='" + uuid + "' and status=1");
            //sqlList.add("update t_system_joint_detail set status=2 where uuid='" + uuid + "' and status=1");

            //模块数据保存脚本
            StringBuffer s_Sql = new StringBuffer("INSERT INTO t_system_joint (total_batch_no, batch_no,uuid,total_result,single_result,sn,m_type,m_name, status, opt_user_id, opt_time, orderno) ");
            s_Sql.append(" VALUES  ('").append(total_bh ).append("','").append(bh).append("','").append(uuid).append("',");
            s_Sql.append(" '").append(StringUtil.null2String(obj.getTotal_result())).append("','").append(StringUtil.null2String(systemJointInfo.getSingle_result())).append("',");
            s_Sql.append(" '").append(StringUtil.null2String(systemJointInfo.getSn())).append("','").append(StringUtil.null2String(systemJointInfo.getType())).append("',");
            s_Sql.append(" '").append(StringUtil.null2String(systemJointInfo.getName())).append("',");
            s_Sql.append(" '1','").append(obj.getUser_id()).append("',now(),'").append(obj.getOrderno()).append("')");

            sqlList.add(s_Sql.toString());

            //系统联调结果 单项结果通过的修改对应状态为已联调
            if("success".equals(systemJointInfo.getSingle_result())){
                //修改订单记录表信息
                sqlList.add("UPDATE t_order_product SET status=4 WHERE orderno='" + obj.getOrderno() + "' AND p_sn='" + systemJointInfo.getSn() + "' and status<99 ");
            }

            //明细数据保存脚本
            Map<String, String> map = systemJointInfo.getDetail();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()){
                java.util.Map.Entry e = (java.util.Map.Entry) iter.next();
                String key = StringUtil.null2String(e.getKey());
                String value = StringUtil.null2String(e.getValue());

                sqlList.add("insert into t_system_joint_detail (total_batch_no,batch_no, uuid, check_item, check_result, status, opt_time) values('" + total_bh + "','" + bh + "','" + uuid + "','" + key + "','" + value + "','1', now())");
            }
        }



        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return lists.size();
    }

    @Override
    public int saveToolInfo(ToolInfo obj) {
        ArrayList<String> sqlList = new ArrayList<String>();
        String batch_no = BhGenerator.getBh();
        String uuid = StringUtil.null2String(obj.getUuid()).toLowerCase();
        String sn = StringUtil.null2String(obj.getSn()).toUpperCase();
        String orderNo = StringUtil.null2String(obj.getOrder_no()).toUpperCase();

        //修改原UUIDhu的状态为废弃
        sqlList.add("update t_tool_info set status=2 where uuid='" + uuid + "' and status=1");
        //添按照SN码也废弃 wly 2016年4月14日 14:23:19
        sqlList.add("update t_tool_info set status=2 where sn='" + sn + "' and status=1");

        if("true".equals(obj.getTotal_result())){
            //修改订单产品表状态 2-已测试
            sqlList.add("UPDATE t_order_product SET status=2,uuid=RIGHT(REPLACE('" + uuid + "', '-', ''), 17),uuid_all='" + uuid + "' WHERE orderno='" + orderNo + "' AND m_sn='" + sn + "' and status<99 ");
        }

        //修改原模块扫码状态为不可用
        sqlList.add("UPDATE t_sn_info SET status=2 WHERE orderno='" + orderNo + "' AND temp_sn='" + sn + "' and status=1");
        sqlList.add("UPDATE t_sn_info SET status=2 WHERE uuid='" + uuid + "' and status=1");

        //插入主表新数据
        sqlList.add("insert into t_tool_info (batch_no, uuid, order_no, total_result, m_name, status, opt_user_id, opt_time, sn, bmu_id) values('" + batch_no + "','" + uuid
                + "','" + orderNo + "','" + StringUtil.null2String(obj.getTotal_result()) + "','"
                + StringUtil.null2String(obj.getName()) + "','1','" + StringUtil.null2String(obj.getUser_id()) + "', now(),'" + sn + "','" + StringUtil.null2String(obj.getBmu_id()) + "')");

        //插入结果数据
        Map<String, String> map = obj.getDetail();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()){
            java.util.Map.Entry e = (java.util.Map.Entry) iter.next();
            String key = StringUtil.null2String(e.getKey());
            String value = StringUtil.null2String(e.getValue());

            sqlList.add("insert into t_tool_detail (batch_no, uuid, check_item, check_result) values('" + batch_no + "','" + uuid + "','" + key + "','" + value + "')");
        }

        //插入所使用文件数据
        Map<String, String> mapFile = obj.getFiles();
        Iterator iterFile = mapFile.entrySet().iterator();
        while (iterFile.hasNext()){
            java.util.Map.Entry e = (java.util.Map.Entry) iterFile.next();
            String key = StringUtil.null2String(e.getKey());
            String value = StringUtil.null2String(e.getValue());

            sqlList.add("insert into t_tool_file (batch_no, uuid, file_item, file_code) values('" + batch_no + "','" + uuid + "','" + key + "','" + value + "')");
        }

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        //返回结果表插入记录
        return map.size();
    }

    @Override
    public int saveToolInfoB(ToolInfoB obj) {
        ArrayList<String> sqlList = new ArrayList<String>();
        String batch_no = BhGenerator.getBh();
        String uuid = StringUtil.null2String(obj.getUuid()).toLowerCase();
        String sn = StringUtil.null2String(obj.getSn()).toUpperCase();
        String orderNo = StringUtil.null2String(obj.getOrder_no()).toUpperCase();
        //修改原UUIDhu的状态为废弃
        sqlList.add("update t_tool_info set status=2 where uuid='" + uuid + "' and status=1");
        //添按照SN码也废弃
        sqlList.add("update t_tool_info set status=2 where sn='" + sn + "' and status=1");
        //模块扫码解绑
        sqlList.add("UPDATE t_sn_info SET status=2 WHERE temp_sn='" + sn + "' and status=1");
        sqlList.add("UPDATE t_sn_info SET status=2 WHERE uuid='" + uuid + "' and status=1");

        if("true".equals(obj.getTotal_result())){
            //修改订单产品表状态 2-已测试
            sqlList.add("UPDATE t_order_product SET status=2,sw_version='" + obj.getSoft_version() + "',uuid=RIGHT(REPLACE('" + uuid + "', '-', ''), 17),uuid_all='" + uuid + "' WHERE orderno='" + orderNo + "' AND m_sn='" + sn + "' and status<99 ");
        }

        //如果已有模块扫码 则PCBA 状态更新为未使用
        String sql = "SELECT COUNT(0) AS ct FROM t_sn_pack WHERE status=1 and main_sn='" + sn + "'";
        Map<String, Object> map_pcba = jdbcTemplate.queryForMap(sql);
        int pcba_num = StringUtil.StringToInt(StringUtil.null2String(map_pcba.get("ct")));
        if (pcba_num > 0){
            sqlList.add("UPDATE t_sn_pack SET status=0,module_sn='' WHERE status=1 and main_sn='" + sn + "'");
        }
        //系统联调数据由于实际操作过程中 测从机时 是一个主机带不同从机测试，上报的数据结果一直是的累加模式，因此不需做状态更改处理

        //插入主表新数据
        StringBuffer infoSbf = new StringBuffer();
        infoSbf.append("INSERT INTO t_tool_info (batch_no, uuid, sn, order_no, total_result, m_name, bmu_id, device_type, tool_id, soft_version, report_type, status, opt_user_id, opt_time) ");
        infoSbf.append("VALUES('").append(batch_no).append("','").append(uuid).append("','").append(sn).append("','").append(orderNo)
                .append("','").append(StringUtil.null2String(obj.getTotal_result())).append("','").append(StringUtil.null2String(obj.getName()))
                .append("','").append(StringUtil.null2String(obj.getBmu_id())).append("','").append(StringUtil.null2String(obj.getDevice_type()))
                .append("','").append(StringUtil.null2String(obj.getTool_id())).append("','").append(StringUtil.null2String(obj.getSoft_version()))
                .append("','").append(StringUtil.null2String(obj.getReport_type())).append("','1','").append(StringUtil.null2String(obj.getUser_id())).append("',NOW())");
        sqlList.add(infoSbf.toString());

        List<ToolDetail> lists = obj.getLists();
        if (lists != null && lists.size()>0) {
            for (int i = 0; i < lists.size(); i++) {
                ToolDetail check_item = lists.get(i);
                String batch_sub = BhGenerator.getBh();
                sqlList.add("insert into t_tool_detail (batch_no, batch_sub , uuid, check_item, check_result) values('" + batch_no + "','" + batch_sub + "','" + uuid + "','" + check_item.getCheck_item() + "','" + check_item.getRes() + "')");

                //插入结果数据
                Map<String, String> map = check_item.getDetail();
                if (null != map && map.size() >0 ){
                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()){
                        java.util.Map.Entry e = (java.util.Map.Entry) iter.next();
                        String key = StringUtil.null2String(e.getKey());
                        String value = StringUtil.null2String(e.getValue());

                        sqlList.add("insert into t_tool_details (batch_no, batch_sub, uuid, check_item, check_result) values('" + batch_no + "','" + batch_sub + "','" + uuid + "','" + key + "','" + value + "')");
                    }
                }
            }
        }

        //插入所使用文件数据
        Map<String, String> mapFile = obj.getFiles();
        Iterator iterFile = mapFile.entrySet().iterator();
        while (iterFile.hasNext()){
            java.util.Map.Entry e = (java.util.Map.Entry) iterFile.next();
            String key = StringUtil.null2String(e.getKey());
            String value = StringUtil.null2String(e.getValue());

            sqlList.add("insert into t_tool_file (batch_no, uuid, file_item, file_code) values('" + batch_no + "','" + uuid + "','" + key + "','" + value + "')");
        }

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        //返回 插入主表结果情况
        return rs[6];
    }

    @Override
    public boolean checkUserPermission(Long userId, String roleCode) {

        StringBuffer querySql = new StringBuffer("SELECT COUNT(*) AS ct FROM tw_user_role WHERE role_id=(SELECT MAX(id) FROM tw_role WHERE role_code='"
                + roleCode + "') AND user_id=");
        querySql.append(userId);
        Map<String, Object> map = jdbcTemplate.queryForMap(querySql.toString());

        int rs = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
        if (rs > 0){
            return true;
        }
        return false;
    }

    @Override
    public SnModule getSnModuleByCode(String snCode) {
        snCode = StringUtil.null2String(snCode);
        if (snCode.length() > 3){
            String moduleCode = snCode.length() > 3 ? snCode.substring(0, 3) : "";
            return snModuleDao.findSnModuleByCode(moduleCode);
        }
        return null;
    }

    @Override
    public int checkDeciveByOrderNo(SnModule snModule, String orderNo) {

        StringBuffer sql = new StringBuffer("SELECT COUNT(*) as device_num FROM t_order_device WHERE orderno='" + orderNo + "' AND device_code='" + snModule.getType() + "' AND status=1");
        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());

        Object rs = map.get("device_num");
        if (rs != null) {
            int device_num = StringUtil.StringToInt(rs.toString(), 0);
            if (device_num == 0){
                return -1;
            }
            /**********************************************
             * 设备类型：
             * 1- 主机（不带DTU）2- 主机（带DTU）
             * 3- 一体机（不带DTU）4- 一体机（带DTU）
             * 5- 从机 6- 绝缘检测(LDM) 7- 其他信息
             * 除独立的从机模块，其他设备型号从机都未0
             *********************************************/
            if (snModule.getDeviceType() == 5){
                /******************************************
                 * 查询从机个数
                 *****************************************/
                sql = new StringBuffer("SELECT COUNT(*) as bmu_num FROM (SELECT bmu_no FROM t_order_device WHERE orderno='" + orderNo + "' AND status=1 AND bmu_no>0 GROUP BY bmu_no) tab");
                Map<String, Object> bmu_map = jdbcTemplate.queryForMap(sql.toString());
                int bmu_num = StringUtil.StringToInt(StringUtil.null2String(bmu_map.get("bmu_num")), 0);
                return bmu_num;
            }else{
                return 0;
            }
        }
        return -1;
    }

    @Override
    public int checkDeciveByOrderNo(String deviceCode, String orderNo) {
        StringBuffer sqlBff = new StringBuffer();
        sqlBff.append("SELECT COUNT(*) AS ct FROM t_order_device WHERE orderno='")
                .append(orderNo).append("' AND STATUS=1 AND device_code='").append(deviceCode).append("'");
        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBff.toString());
        int num = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
        if (num > 0){
            return num;
        }
        return 0;
    }

    /**
     * 校验订单产品SN码
     * @param orderNo
     * @param productSn
     * @return
     */
    @Override
    public SnModule checkOrderProductSn(String orderNo, String productSn) {
        StringBuffer sql = new StringBuffer("SELECT MAX(temp_sn) AS temp_sn FROM t_sn_info WHERE STATUS=1 AND orderno='");
        sql.append(orderNo).append("' AND sn='").append(productSn).append("'");
        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        if (map == null) {
            return null;
        }
        String rs = StringUtil.null2String(map.get("temp_sn"));
        if (rs == null || "".equals(rs)) {
            return null;
        }
        String moduleCode = rs.length() > 3 ? rs.substring(0, 3) : "";

        SnModule snModule = snModuleDao.findSnModuleByCode(moduleCode);
        if (snModule == null) {
            return null;
        }
        //return snModule.getType();
        return snModule;
    }

    /**
     * 校验产品是否工装测试
     * @param orderNo
     * @param productSn
     * @return
     */
    @Override
    public int checkProductSnFromTool(String orderNo, String productSn) {
        StringBuffer sql = new StringBuffer("SELECT max(total_result) as total_result FROM t_tool_info WHERE STATUS=1 AND sn=(SELECT MAX(temp_sn) FROM t_sn_info WHERE STATUS=1 AND orderno='");
        sql.append(orderNo).append("' AND sn='").append(productSn).append("')");
        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        if (map == null) {
            return -2;
        }
        String rs = StringUtil.null2String(map.get("total_result"));
        if (rs == null || "".equals(rs)) {
            /*******************************************
             * 无记录时返回-2 测试不通过 -1 测试通过 1
             ******************************************/
            return -2;
        }else{
            if ("true".equals(rs)){
                /*******************************************
                 * 工装测试通过true 测试不通过false
                 ******************************************/
                return 1;
            }else {
                return -1;
            }
        }
    }

    @Override
    public int checkMSnFromTool(String orderNo, String mSN) {
        StringBuffer sql = new StringBuffer("SELECT max(total_result) as total_result FROM t_tool_info WHERE STATUS=1 AND sn='" + mSN + "' AND order_no='");
        sql.append(orderNo).append("' ");
        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        if (map == null) {
            return -2;
        }
        String rs = StringUtil.null2String(map.get("total_result"));
        if (rs == null || "".equals(rs)) {
            /*******************************************
             * 无记录时返回-2 测试不通过 -1 测试通过 1
             ******************************************/
            return -2;
        }else{
            if ("true".equals(rs)){
                /*******************************************
                 * 工装测试通过true 测试不通过false
                 ******************************************/
                return 1;
            }else {
                return -1;
            }
        }
    }

    @Override
    public int checkProductSn(String productSn) {
        /**************************************************
         * 开头三位匹配数据库已有数据
         **************************************************/
        productSn = StringUtil.null2String(productSn).trim();
        if (productSn.length() <12){
            return -1;
        }
        Map<String, Object> map = jdbcTemplate.queryForMap("SELECT COUNT(*) AS ct FROM t_sn_product WHERE p_code='" + productSn.substring(0, 3) + "'");
        if (map != null) {
            Object rs = map.get("ct");
            String str = StringUtil.null2String(rs);
            if ("1".equals(str)) {
                return 1;
            }
        }
        return -1;
    }

    @Override
    public int findToolDeviceCount(String orderNo, String device_name, int bmu_id) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) AS ct FROM t_tool_info WHERE total_result='true' AND STATUS=1 AND order_no='")
                .append(orderNo).append("' AND m_name='").append(device_name).append("' AND bmu_id='").append(bmu_id).append("'");

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        if (map != null) {
            String str = StringUtil.null2String(map.get("ct"));
            if (str != null && !"".equals(str)) {
                return StringUtil.StringToInt(str);
            }
        }
        return 0;
    }

    @Override
    public int findOrderDeviceCount(String orderNo, String device_name, int bmu_id) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT device_count AS ct FROM t_order_device WHERE STATUS=1 AND orderno='")
                .append(orderNo).append("' AND device_code='").append(device_name).append("' AND bmu_no='").append(bmu_id).append("'");

        Map<String, Object> map = null;
        try{
            map = jdbcTemplate.queryForMap(sql.toString());
        }catch (Exception e){
            logger.debug("findOrderDeviceCount无结果返回");
        }
        if (map != null) {
            String str = StringUtil.null2String(map.get("ct"));
            if (str != null && !"".equals(str)) {
                return StringUtil.StringToInt(str);
            }
        }
        return 0;
    }

    @Override
    public List<Map<String, String>> findPcbaBySN(String mSN) {
        //SELECT mp.pcba_type, mp.pcba_code, mp.pcba_name,sp.pcb_sn AS pcba_sn, mp.id AS px, sp.orderno
        //FROM t_sn_pack sp LEFT JOIN t_sn_module_pcba mp ON SUBSTRING(sp.pcb_sn, 1, 3)=mp.`pcba_code` WHERE sp.main_sn='21C5CN3A2200001' AND sp.STATUS<2 AND mp.`m_code`='21C' ORDER BY px;

        StringBuffer sql = new StringBuffer("SELECT mp.pcba_type, mp.pcba_code, mp.pcba_name,sp.pcb_sn AS pcba_sn, mp.id AS px, sp.orderno ");
        sql.append(" FROM t_sn_pack sp LEFT JOIN t_sn_module_pcba mp ON SUBSTRING(sp.pcb_sn, 1, 3)=mp.pcba_code ");
        sql.append(" WHERE sp.main_sn=? AND sp.STATUS<? AND mp.m_code=? ORDER BY px ");
        Object[] arg = new Object[]{mSN, 2, mSN.substring(0, 3)};
        List<Map<String, String>> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                map.put("pcba_type", resultSet.getString("pcba_type"));
                map.put("pcba_code", resultSet.getString("pcba_code"));
                map.put("pcba_name", resultSet.getString("pcba_name"));
                map.put("pcba_sn", resultSet.getString("pcba_sn"));
                map.put("orderno", resultSet.getString("orderno"));
                return map;
            }
        });
        return list;
    }

    @Override
    public List<Map<String, String>> findPcbaBySNCode(String mSN) {
        if (null != mSN && mSN.length() > 3){
            StringBuffer sql = new StringBuffer("SELECT m_type,m_code,pcba_type,pcba_code,pcba_name,pcba_number FROM t_sn_module_pcba WHERE m_code=?");
            Object[] arg = new Object[]{ mSN.substring(0, 3)};
            List<Map<String, String>> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

                @Override
                public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("m_type", resultSet.getString("m_type"));
                    map.put("m_code", resultSet.getString("m_code"));
                    map.put("pcba_type", resultSet.getString("pcba_type"));
                    map.put("pcba_code", resultSet.getString("pcba_code"));
                    map.put("pcba_name", resultSet.getString("pcba_name"));
                    map.put("pcba_number", resultSet.getString("pcba_number"));
                    return map;
                }
            });
            return list;
        }
        return null;
    }

    @Override
    public Map<String, String> findOrderPcbaBySN(String mSN) {
        // SELECT orderno, device_code,m_sn,hw_version FROM `t_order_product` WHERE STATUS<99 AND m_sn='2065CU3A2200003';
        //StringBuffer sql = new StringBuffer("SELECT orderno, device_code,m_sn,hw_version FROM t_order_product WHERE STATUS<? AND m_sn=?");
        //有可能已经解绑过了，此处状态有可能是99删除状态
        StringBuffer sql = new StringBuffer("SELECT orderno, device_code,m_sn,hw_version FROM t_order_product WHERE m_sn=? order by id desc");
        Object[] arg = new Object[]{mSN};
        List<Map<String, String>> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orderno", resultSet.getString("orderno"));
                map.put("device_code", resultSet.getString("device_code"));
                map.put("m_sn", resultSet.getString("m_sn"));
                map.put("hw_version", resultSet.getString("hw_version"));
                return map;
            }
        });
        if (list == null || list.size() == 0) {
            return null;
        }else{
            return list.get(0);
        }
    }

    @Override
    public boolean checkSNinOrder(String mSN, String orderNo) {
        String sql = "SELECT COUNT(*) AS ct FROM  t_order_product WHERE STATUS<99 AND orderno='" + orderNo + "' AND m_sn='" + mSN + "'";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);

        int db_num = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
        if (db_num > 0){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> findOrderProductForMiScann(String mSN, String orderNo) {
        /**
         * SELECT top.orderno, top.device_code, top.m_sn, top.hw_version, top.sw_version,top.uuid_all AS uuid,(SELECT CASE WHEN MAX(tti.bmu_id) > 0 THEN MAX(tti.bmu_id) ELSE 0 END FROM t_tool_info tti WHERE tti.total_result='true' AND tti.status=1 AND tti.sn=top.m_sn ) AS bmu_id
         FROM  t_order_product top WHERE STATUS<99 AND orderno='LG201512230001' AND m_sn='2065CU3A2200011' ;
         */
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT top.orderno, top.device_code, top.m_sn, top.hw_version, top.sw_version,top.uuid_all AS uuid,(SELECT CASE WHEN MAX(tti.bmu_id) > 0 THEN MAX(tti.bmu_id) ELSE 0 END FROM t_tool_info tti WHERE tti.total_result='true' AND tti.status=1 AND tti.sn=top.m_sn ) AS bmu_id ");
        sql.append("FROM  t_order_product top WHERE STATUS<99 AND orderno='").append(orderNo).append("' AND m_sn='").append(mSN).append("' ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public SNInfo findSNInfoById(Long snId) {
        return snInfoDao.findOne(snId);
    }

    @Override
    public SNInfo findSNInfoBySn(String pSN) {
        List<SNInfo> list = snInfoDao.findSNAllListByPSN(pSN);
        if (null != list && list.size() > 0){
            SNInfo obj = list.get(0);
            if (null != obj && null != obj.getSn()){
                return obj;
            }
        }

        return null;
    }

    @Override
    public int findSNCount(String orderno, String pSN) {
        String sql = "select count(0) as ct from t_sn_info where substring(sn, 1, 3)='" + pSN.substring(0, 3) + "' and orderno='" + orderno + "' and status=1";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        int sn_num = StringUtil.StringToInt(StringUtil.null2String(map.get("ct")));
        return sn_num;
    }


    @Override
    public int findOrdetrDeviceSum(String orderno, String deviceCode) {
        String sql = "SELECT IFNULL(SUM(device_count), -1) AS ct FROM t_order_device WHERE orderno='" + orderno + "' AND device_code='" + deviceCode + "'";

        Map<String, Object> mapCount = jdbcTemplate.queryForMap(sql);

        int deviceSum = StringUtil.StringToInt(StringUtil.null2String(mapCount.get("ct")));

        return deviceSum;
    }

    @Override
    public int findPCBADeviceSum(String orderno, String deviceCode) {
        String sql = "SELECT COUNT(*) AS ct FROM t_order_product WHERE STATUS<99 AND orderno='" + orderno + "' AND device_code='" + deviceCode + "'";

        Map<String, Object> mapCount = jdbcTemplate.queryForMap(sql);

        int deviceSum = StringUtil.StringToInt(StringUtil.null2String(mapCount.get("ct")));

        return deviceSum;
    }

    @Override
    public String findMSNByUUID(String uuid) {
        String sql = "SELECT m_sn FROM t_order_product WHERE status<99 AND uuid_all='" + uuid + "'  ORDER BY m_sn DESC";
        List<String> list = jdbcTemplate.query(sql, new Object[]{}, new RowMapper(){
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("m_sn");
            }
        });

        if (list == null || list.size() == 0) {
            return "";
        }else{
            return StringUtil.null2String(list.get(0));
        }
    }

    private String getUuidBySnid(String snids){
        //取前五千个字符
        StringBuffer sql = new StringBuffer("SELECT SUBSTRING(GROUP_CONCAT(DISTINCT dtu_uuid), 1, 5000) as rsstr FROM t_sn_info WHERE id in ");
        sql.append(snids);
        sql.append(" ORDER BY id");
        List<String> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("rsstr");
            }
        });

        if (list != null && list.size() > 0){
            String rsStr = list.get(0);
            if (null != rsStr && rsStr.length()>0){
                return rsStr;
            }
        }

        return "";
    }

    /**
     * 保存扫码关联信息
     * @param obj
     * @param order
     * @return
     */
    private int saveSNother(SNInfo obj, Order order){
        Long optUserId = obj.getOptUserid();

        List<String> sqlList = new ArrayList<String>();
        //TOTO 是否删除原uuid所属客户的关联关系
        /**
         * 2015年12月22日11:41:10 修改
        //原SN码改为删除状态
        //解除原uuid的关联关系
         保留原操作痕迹 不修改原操作人和操作时间 wly 2016年7月12日 16:12:47
         **/
       String str_sn_update = "UPDATE t_sn_info SET status=2,prod_type=null WHERE sn='" + obj.getSn() + "' and status=1 and id<>" + obj.getId();
       sqlList.add(str_sn_update);

        //出现模块SN码重复，此时操作为，原产品SN绑定的模块SN码清空 2016年1月13日16:40:51
        String str_temp_sn_update = "UPDATE t_sn_info SET temp_sn='' WHERE temp_sn='" + obj.getTempSn() + "' and id<>" + obj.getId();
        sqlList.add(str_temp_sn_update);

        if (!"".equals(obj.getDtuUuid())){ // UUID合法时才会根据UUID更新状态 2015年12月30日15:00:58
            String str_uuid_update = "UPDATE t_sn_info SET status=2,prod_type=null,opt_userid=" + obj.getOptUserid() + ",opt_time=now() WHERE dtu_uuid='" + obj.getDtuUuid() + "' and status=1 and id<>" + obj.getId();
            sqlList.add(str_uuid_update);
        }

        //还未生成DTU
        //String str_dtu = "UPDATE dtu_user_config SET status=2,optuserid=" + optUserId + ",opttime=NOW() WHERE dtuid =(SELECT MAX(id) FROM dtu WHERE uuid='" + snInfo.getDtuUuid() + "')";
        //sqlArr[1] = str_dtu;

        //修改订单状态为 已处理 1 订单状态定义已修改 wly 2016年4月14日 15:02:03
        //String sql_order = "update t_order set status=1 where orderno='" + obj.getOrderno() + "' and status=0";
        //sqlList.add(sql_order);
        //原pcb板信息信息处理  默认为重新扫模块SN码，修改原先的扫码信息状态
        String sql_pack_update_main = "UPDATE t_sn_pack set STATUS=3 WHERE STATUS=1 AND main_sn='" + obj.getTempSn() + "'";
        sqlList.add(sql_pack_update_main);
        String sql_pack_update_module = "UPDATE t_sn_pack set STATUS=3 WHERE STATUS=1 AND module_sn='" + obj.getSn() + "'";
        sqlList.add(sql_pack_update_module);

        //绑定pack表PCB板信息
        String sql_pack_add = "UPDATE t_sn_pack set STATUS=1,module_sn='" + obj.getSn() + "' WHERE STATUS=0 AND main_sn='" + obj.getTempSn() + "'";
        sqlList.add(sql_pack_add);

        String uuid = StringUtil.null2String(obj.getUuid());
        //修改订单记录表信息
        sqlList.add("UPDATE t_order_product SET status=3,uuid=RIGHT(REPLACE('" + uuid + "', '-', ''), 17),uuid_all='" + uuid + "',p_sn='" + obj.getSn() + "',sw_version='" + obj.getSwVersion() + "',mi_user_id='" + obj.getOptUserid() + "',mi_time=now() WHERE orderno='" + obj.getOrderno() + "' AND m_sn='" + obj.getTempSn() + "' and status<99 ");

        //日志记录
        String content = "SN绑定>>订单编号:" + obj.getOrderno() + "，UUID：" + obj.getUuid()
                + "，SN码：" + obj.getSn() + "，临时SN码：" + obj.getTempSn() + "，DTUUUID码：" + obj.getDtuUuid()
                + "，硬件版本号：" + obj.getHwVersion()  + "，软件版本号：" + obj.getSwVersion()  + "，产品型号ID：" + obj.getProdType().getId();
        String sql_log = "insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'SNSCAN', NOW(), '" + content + "')";
        sqlList.add(sql_log);

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs.length;
    }









    //添加DTU
       /* StringBuffer sql_dtu = new StringBuffer();
        sql_dtu.append("INSERT INTO dtu(alarm_status,balance_status,battery_recharge_cycles,battery_total_amp,battery_total_voltage,");
        sql_dtu.append("communi_status,insert_time,insulation_status,leak_elec,left_capacity,");
        sql_dtu.append("max_box_temper,max_single_box_id,max_single_string,max_single_voltage,max_temper_box_id,");
        sql_dtu.append("max_temper_string,min_box_temper,min_single_box_id,min_single_string,min_single_voltage,");
        sql_dtu.append("min_temper_box_id,min_temper_string,negative_resistance,online_status,");
        sql_dtu.append("out_charge,out_realease,out_stream,out_temper,positive_resistance,");
        sql_dtu.append("sim_card,soc,soc_high,soc_low,soh,");
        sql_dtu.append("temper_check_exeption,temper_diff_max,total_capacity,total_milege,total_resistance_value,");
        sql_dtu.append("total_voltage_high,total_voltage_low,uuid,voltage_check_exeption,voltage_diff_max,");
        sql_dtu.append("battery_model_id,dtu_user_id) ");
        sql_dtu.append("SELECT 0,0,0,0,0,");
        sql_dtu.append("0,NOW(),0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("13912345678,0,0,0,0,");
        sql_dtu.append("0,0,0,0,0,");
        sql_dtu.append("0,0, dtu_uuid,0,0,");
        sql_dtu.append(order.getBatteryModel().getId() + " AS battery_model_id,");
        sql_dtu.append(order.getDtuUser().getId() + " AS userid");
        sql_dtu.append(" FROM t_sn_info WHERE id =").append(obj.getId());
        sqlArr[1] = sql_dtu.toString();

        //添加车辆信息
        StringBuffer sql_vehicle = new StringBuffer();
        sql_vehicle.append("INSERT INTO vehicle(create_time,UUID,vehicle_number,dtu_id,dtu_user_id,vehicle_model_id,vehicle_type_id)");
        sql_vehicle.append(" SELECT NOW(), uuid,'', id,dtu_user_id, ");
        sql_vehicle.append(order.getVehicleModel().getId() + " AS vehicle_model_id,");
        sql_vehicle.append(order.getVehicleType().getId() + " AS vehicle_type_id");
        sql_vehicle.append(" FROM dtu ");
        sql_vehicle.append("WHERE uuid ='" + obj.getDtuUuid() + "'");
        sqlArr[2] = sql_vehicle.toString();

        //分配到具体用户
        String sql_dtuuser = "INSERT INTO dtu_user_config(dtuid, userid, optuserid, opttime,STATUS) " +
                "SELECT id,dtu_user_id," + optUserId + " AS optuserid, NOW(), 1 FROM dtu WHERE uuid = '" + obj.getDtuUuid() + "' ";
        sqlArr[3] = sql_dtuuser;

        */


}
