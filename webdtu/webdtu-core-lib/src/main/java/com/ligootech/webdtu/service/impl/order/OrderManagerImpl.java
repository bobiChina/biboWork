package com.ligootech.webdtu.service.impl.order;

import com.ligootech.webdtu.entity.core.Order;
import com.ligootech.webdtu.entity.core.OrderTrack;
import com.ligootech.webdtu.repository.OrderDao;
import com.ligootech.webdtu.service.account.ShiroUser;
import com.ligootech.webdtu.service.email.EmailManager;
import com.ligootech.webdtu.service.order.OrderManager;
import com.ligootech.webdtu.service.order.OrderTrackManager;
import com.ligootech.webdtu.util.BhGenerator;
import com.ligootech.webdtu.util.EasyUiUtil;
import com.ligootech.webdtu.util.StringUtil;
import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by wly on 2015/10/15 15:32.
 */
@SuppressWarnings("ALL")
@Service("orderManager")
public class OrderManagerImpl extends GenericManagerImpl<Order, Long> implements OrderManager {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderTrackManager orderTrackManager;

    @Autowired
    private EmailManager emailManager;

    @Override
    public List<Order> findOrderList(Long corpid) {
        return orderDao.findOrderByCorpid(corpid);
    }

    @Override
    public List<Object[]> findSimpleOrderList(Long corpId, int type) {

        if(type == 0){
            return orderDao.findSimpleOrderListAll();
        }else if(type == 1){
            return orderDao.findSimpleOrderList();
        }
        return orderDao.findSimpleOrderList();
    }

    /**
     * 开发人员
     *
     * @param user
     * @return
     */
    @Override
    public List<Object[]> findSimpleOrderListByTrack(ShiroUser user) {
        // select id,orderno,status from Order order by id desc
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id,orderno,STATUS AS st FROM t_order WHERE STATUS<99 AND orderno IN (SELECT DISTINCT orderno FROM t_order_track WHERE STATUS=1 AND action_man='" + user.getName() + "') ORDER BY STATUS ASC, id DESC");

        Object[] arg = new Object[]{};
        List<Object[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] rs =  new Object[3];
                rs[0] = resultSet.getString("id");
                rs[1] = resultSet.getString("orderno");
                rs[2] = resultSet.getString("st");
                return rs;
            }
        });
        return list;
    }

    @Override
    public Order findInfoByOrderNo(String orderNo) {
        List<Order> list = orderDao.findOrderByOrderNo(orderNo);
        if(null != list && list.size() > 0){
            Order order = list.get(0);
            if (order != null) {
                return order;
            }
        }
        return null;
    }

    /**
     * 销售代表订单查询
     *
     * @param orderNo
     * @param salesUserId
     * @return
     */
    @Override
    public Order findInfoByOrderNo(String orderNo, Long salesUserId) {
        List<Order> list = orderDao.findOrderByOrderNo(orderNo, String.valueOf(salesUserId));
        if(null != list && list.size() > 0){
            Order order = list.get(0);
            if (order != null) {
                return order;
            }
        }
        return null;
    }

    @Override
    public List<Object[]> findSNListByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public int saveOrder(Order order, String opt_type, String devices_code, String devices_class, String devices_bmu, String devices_count, Long userId) {
        /************************************************************************
         * 保存订单信息
         ***********************************************************************/
        orderDao.save(order);

        List<String> sqlList = new ArrayList<>();
        StringBuffer logSql = new StringBuffer();
        logSql.append("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(").append(userId).append(", 'ORDER', NOW(), '");

        /***********************************************************************
         * 订单设备型号保存
         * 新增订单直接添加设备型号，修改订单需比较设备型号是否被修改，
         * 若有修改则原设备型号状态改为删除，订单文件表同步修改订单的文件状态
         **********************************************************************/
        String[] device_codeArr = devices_code.split(",");
        String[] device_classArr = devices_class.split(",");
        String[] device_bmuArr = devices_bmu.split(",");

        List<String> deviceList = new ArrayList<String>();
        for (int i = 0; i <device_codeArr.length; i++) {
            String deviceStr = StringUtil.null2String(device_codeArr[i]).trim();
            if (!"".equals(deviceStr) && !deviceList.contains(deviceStr) ){
                deviceList.add(deviceStr + "_" + device_classArr[i] + "_" + device_bmuArr[i]);
            }
        }
        /**********************************************************************
         *  数量根据订单类型不同会有不同取值
         *  1.新订单取套数值，2.软件售后订单为0，3.硬件售后订单取用具体填写的值
         *  2016年6月15日 11:37:01
         *********************************************************************/
        String[] devices_countArr = null;
        String orderNo = order.getOrderno();
        if (order.getOrderType() == 2){                         //软件售后订单
            devices_countArr = new String[deviceList.size()];
            for (int i = 0; i <deviceList.size(); i++) {
                devices_countArr[i] = "0";
            }
        }else if (order.getOrderType() == 3){                   //硬件售后订单
            devices_countArr = devices_count.split(",");
        }else {                                                 //新订单
            devices_countArr = new String[deviceList.size()];
            for (int i = 0; i <deviceList.size(); i++) {
                devices_countArr[i] = String.valueOf(order.getQuantity());
            }
        }

        /***************************************************
         * 添加设备数量 device_count 2016年6月14日 17:24:01
         **************************************************/
        if ("add".equals(opt_type) || "add_sw".equals(opt_type) || "add_hw".equals(opt_type)){
            logSql.append("订单添加>>");
            for (int i = 0; i <deviceList.size(); i++) {
                String countStr = StringUtil.null2String(devices_countArr[i]).trim();
                if("".equals(countStr)){
                    countStr = "0";
                }
                sqlList.add("insert into t_order_device (orderno, device_code, device_class, bmu_no, device_type, status, opt_user_id, opt_time, device_count) " +
                        "values('" + order.getOrderno() + "','" + device_codeArr[i] + "','" + device_classArr[i] + "','" + device_bmuArr[i] + "','0','1','" + userId + "',now()," + countStr + ")");
            }
        }else{
            logSql.append("订单修改>>");

            List<String> dbList = findDeviceForDiff(order.getOrderno());
            if (deviceList.size() > 0){
                sqlList.add("UPDATE t_order_device SET status=2,opt_user_id=" + userId + ",opt_time=now() WHERE status=1 AND orderno='" + order.getOrderno() + "'");
                for (int i = 0; i <deviceList.size(); i++) {
                    String countStr = StringUtil.null2String(devices_countArr[i]).trim();
                    if("".equals(countStr)){
                        countStr = "0";
                    }
                    sqlList.add("insert into t_order_device (orderno, device_code, device_class, bmu_no, device_type, status, opt_user_id, opt_time, device_count) " +
                            "values('" + order.getOrderno() + "','" + device_codeArr[i] + "','" + device_classArr[i] + "','" + device_bmuArr[i] + "','0','1','" + userId + "',now(), " + countStr + ")");
                }
                //比较两次设备型号是否一致
                if (dbList.containsAll(deviceList) && deviceList.containsAll(dbList)){

                }else{
                    sqlList.add("UPDATE t_order_file SET status=3,opt_user_id=" + userId + ",del_time=now() WHERE status=1 AND orderno='" + order.getOrderno() + "'");
                }
            }else{
                sqlList.add("UPDATE t_order_device SET status=2,opt_user_id=" + userId + ",opt_time=now() WHERE status=1 AND orderno='" + order.getOrderno() + "'");
                sqlList.add("UPDATE t_order_file SET status=3,opt_user_id=" + userId + ",del_time=now() WHERE status=1 AND orderno='" + order.getOrderno() + "'");
            }
        }

        //修改设备类型 由于添加 LCD 和其他 故限制为('BCU', 'BYU', 'BMU', 'LDM', 'DTU')五种类型
        sqlList.add("UPDATE t_order_device t_a SET t_a.device_type=(SELECT MAX(device_type) FROM t_sn_module WHERE m_type=t_a.device_code) WHERE t_a.device_type=0 and t_a.status=1 and t_a.device_class in ('BCU', 'BYU', 'BMU', 'LDM', 'DTU')");

        //复制软件信息  硬件售后订单需拉取软件信息  手动添加的售后都不同步软件信息
       /* String bfOrder = StringUtil.null2String(order.getBeforeSoftware()).trim();
        if (!"".equals(bfOrder) && order.getOrderType() == 3){
            StringBuffer sBuf = new StringBuffer();
            sBuf.append("INSERT INTO t_order_file (batch_no, orderno, device_code, bmu_no, file_type, file_code, file_name, md5_code,start_time, STATUS, opt_user_id, opt_time) ");
            sBuf.append("SELECT batch_no, '").append(order.getOrderno()).append("', IFNULL(device_code, ''), IFNULL(bmu_no, 0), IFNULL(file_type, ''), IFNULL(file_code, ''), IFNULL(file_name, ''), ");
            sBuf.append("IFNULL(md5_code, ''), NOW(), STATUS, '").append(order.getOptUser()).append("', NOW() FROM t_order_file WHERE STATUS=1 AND orderno='").append(bfOrder).append("'");

            sqlList.add(sBuf.toString());
        }*/

        //添加操作日志记录
        logSql.append(StringUtil.toStringForDB(order).toString()).append("')");
        sqlList.add(logSql.toString());

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] sqlRs = jdbcTemplate.batchUpdate(sqlArr);

        return 0;
    }

    @Override
    public int saveSHOrder(Order order, String opt_type, String devices_code, String devices_class, String devices_bmu, String devices_count, Long userId) {
        /************************************************************************
         * 保存订单信息
         ***********************************************************************/
        orderDao.save(order);

        List<String> sqlList = new ArrayList<>();
        StringBuffer logSql = new StringBuffer();
        logSql.append("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(").append(userId).append(", 'ORDER', NOW(), '");

        /***********************************************************************
         * 订单设备型号保存
         * 新增订单直接添加设备型号，修改订单需比较设备型号是否被修改，
         * 若有修改则原设备型号状态改为删除，订单文件表同步修改订单的文件状态
         **********************************************************************/
        String[] device_codeArr = devices_code.split(",");
        String[] device_classArr = devices_class.split(",");
        String[] device_bmuArr = devices_bmu.split(",");

        List<String> deviceList = new ArrayList<String>();
        for (int i = 0; i <device_codeArr.length; i++) {
            String deviceStr = StringUtil.null2String(device_codeArr[i]).trim();
            if (!"".equals(deviceStr) && !deviceList.contains(deviceStr) ){
                deviceList.add(deviceStr + "_" + device_classArr[i] + "_" + device_bmuArr[i]);
            }
        }
        /**********************************************************************
         *  数量根据订单类型不同会有不同取值
         *  1.新订单取套数值，2.软件售后订单为0，3.硬件售后订单取用具体填写的值
         *  2016年6月15日 11:37:01
         *********************************************************************/
        String[] devices_countArr = null;
        String orderNo = order.getOrderno();
        if (order.getOrderType() == 2){                         //软件售后订单
            devices_countArr = new String[deviceList.size()];
            for (int i = 0; i <deviceList.size(); i++) {
                devices_countArr[i] = "0";
            }
        }else if (order.getOrderType() == 3){                   //硬件售后订单
            devices_countArr = devices_count.split(",");
        }else {                                                 //新订单
            devices_countArr = new String[deviceList.size()];
            for (int i = 0; i <deviceList.size(); i++) {
                devices_countArr[i] = String.valueOf(order.getQuantity());
            }
        }

        /***************************************************
         * 添加设备数量 device_count 2016年6月14日 17:24:01
         **************************************************/
        //if ("add".equals(opt_type) || "add_sh".equals(opt_type)){
            logSql.append("订单添加>>");
            for (int i = 0; i <deviceList.size(); i++) {
                String countStr = StringUtil.null2String(devices_countArr[i]).trim();
                if("".equals(countStr)){
                    countStr = "0";
                }
                sqlList.add("insert into t_order_device (orderno, device_code, device_class, bmu_no, device_type, status, opt_user_id, opt_time, device_count) " +
                        "values('" + order.getOrderno() + "','" + device_codeArr[i] + "','" + device_classArr[i] + "','" + device_bmuArr[i] + "','0','1','" + userId + "',now()," + countStr + ")");
            }
        //}

        //修改设备类型 由于添加 LCD 和其他 故限制为('BCU', 'BYU', 'BMU', 'LDM', 'DTU')五种类型
        sqlList.add("UPDATE t_order_device t_a SET t_a.device_type=(SELECT MAX(device_type) FROM t_sn_module WHERE m_type=t_a.device_code) WHERE t_a.device_type=0 and t_a.status=1 and t_a.device_class in ('BCU', 'BYU', 'BMU', 'LDM', 'DTU')");

        //复制软件信息 硬件售后订单需拉取软件信息
        String bfOrder = StringUtil.null2String(order.getBeforeSoftware()).trim();
        if (!"".equals(bfOrder) && order.getOrderType() == 3){
            StringBuffer sBuf = new StringBuffer();
            sBuf.append("INSERT INTO t_order_file (batch_no, orderno, device_code, bmu_no, file_type, file_code, file_name, md5_code,start_time, STATUS, opt_user_id, opt_time) ");
            sBuf.append("SELECT batch_no, '").append(order.getOrderno()).append("', IFNULL(device_code, ''), IFNULL(bmu_no, 0), IFNULL(file_type, ''), IFNULL(file_code, ''), IFNULL(file_name, ''), ");
            sBuf.append("IFNULL(md5_code, ''), NOW(), STATUS, '").append(order.getOptUser()).append("', NOW() FROM t_order_file WHERE STATUS=1 AND orderno='").append(bfOrder).append("'");

            sqlList.add(sBuf.toString());
        }

        //添加操作日志记录
        logSql.append(StringUtil.toStringForDB(order).toString()).append("')");
        sqlList.add(logSql.toString());

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] sqlRs = jdbcTemplate.batchUpdate(sqlArr);

        return 0;
    }

    @Override
    public EasyUiUtil.PageForData findSHInfoList(int rows, int page, String sort, String order, String orderNo) {

        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT orderno, order_type, DATE_FORMAT(opt_time, '%Y-%m-%d %H:%i') AS opt_time, order_note FROM t_order ");

        StringBuffer whereSql = new StringBuffer(" WHERE STATUS<99 AND bound_order='").append(orderNo).append("' ");

        StringBuffer orderSql = new StringBuffer();
        if (!"".equals( StringUtil.null2String(sort).trim())){
            orderSql.append(" order by ").append(sort).append(" ").append(order);
        }else{// 默认排序为ID顺序
            orderSql.append(" order by id ");
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(EasyUiUtil.getPageSQL(sqlBuf.append(whereSql).append(orderSql).toString(), page, rows));

        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct from t_order tot ").append(whereSql.toString());
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));

        EasyUiUtil.PageForData pageObj = new EasyUiUtil.PageForData();
        pageObj.setRows(list);
        pageObj.setTotal(Long.parseLong(total));

        return pageObj;
    }

    @Override
    public List<Map<String, Object>> findSHInfoList(String orderNo) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT orderno, CASE WHEN order_type=2 THEN '软件售后' WHEN order_type=3 THEN '硬件售后' ELSE '' END AS order_type, DATE_FORMAT(opt_time, '%Y-%m-%d %H:%i') AS opt_time, order_note FROM t_order ");
        sqlBuf.append(" WHERE STATUS<99 AND bound_order='").append(orderNo).append("' order by id ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlBuf.toString());
        return list;
    }

    @Override
    public List<String> findSHOrderList(String orderNo) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT orderno,id FROM t_order ");
        sqlBuf.append(" WHERE STATUS<99 and order_type=2 AND bound_order='").append(orderNo).append("' order by id ");

        Object[] arg = new Object[]{};
        List<String> list = jdbcTemplate.query(sqlBuf.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("orderno");
            }
        });
        return list;
    }

    @Override
    public List<String> findSHOrderList4OTA(String orderNo) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT orderno,id FROM c_order ");
        sqlBuf.append(" WHERE STATUS<99 and order_type=2 AND bound_order=? order by id ");

        Object[] arg = new Object[]{orderNo};
        List<String> list = jdbcTemplate.query(sqlBuf.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("orderno");
            }
        });
        return list;
    }


    /**
     * 获取指定订单的设备型号
     * @param orderNo
     * @return
     */
    public List<String> findDeviceForDiff(String orderNo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT CONCAT(IFNULL(device_code, ''), '_', IFNULL(device_class, ''), '_', IFNULL(bmu_no, 0)) AS keystr FROM t_order_device WHERE status=1 AND orderno='").append(orderNo).append("' ");

        Object[] arg = new Object[]{};
        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("keystr");
            }
        });
        return list;
    }

    @Override
    public int delOrderById(Long orderId, Long optUserId) {

        Order order = orderDao.findOne(orderId);
        List<String> sqlList = new ArrayList<>();

        //修改t_order状态 99 --删除状态
        //String str_order = "update t_order set status=2,opt_userid='" + optUserId + "',opt_time=NOW() where id=" + orderId;
        //修改为直接物理删除，有订单时，关联的客户删除不掉，因此在这修改客户 2015年11月16日17:22:18
        sqlList.add("update t_order set status=99,opt_userid='" + optUserId + "',opt_time=NOW() where id=" + orderId);

        //修改扫描表状态为作废 修改产品类型为默认值1，否则产品类型不能删除
        //String str_scan = "UPDATE t_sn_scan SET status=2,prod_type=1 WHERE orderno='" + order.getOrderno() + "'";
        //sqlArr[1] = str_scan;

        //删除订单设备型号表信息 和 订单文件
        sqlList.add("UPDATE t_order_device SET status=2,opt_user_id=" + optUserId + ",opt_time=now() WHERE status=1 AND orderno='" + order.getOrderno() + "'");
        sqlList.add("UPDATE t_order_file SET status=3,opt_user_id=" + optUserId + ",del_time=now() WHERE status=1 AND orderno='" + order.getOrderno() + "'");

        //删除订单日志 2016年5月20日 20:31:07
        sqlList.add("UPDATE t_order_track SET STATUS=2 WHERE  orderno='" + order.getOrderno() + "'");
        //日志记录
        String content = "订单删除>>" + StringUtil.toStringForDB(order);
        sqlList.add("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(" + optUserId + ", 'ORDER', NOW(), '" + content + "')");

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);
        return rs.length;
    }

    @Override
    public List<String> findOrderNo(String orderno, int startnum, int endnum) {
        if (startnum < 0 || startnum > endnum){
            return null;
        }
        if (startnum > 0){//MySQL limit用法是从0开始
            startnum = startnum - 1;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select id,orderno from t_order where status<99 ");
        sql.append(" and orderno like '" + orderno.trim() + "%'");
        sql.append(" order by id desc limit  ?, ?");
        Object[] arg = new Object[]{startnum, endnum - startnum };
        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("orderno");
            }
        });
        return list;
    }

    @Override
    public List<String> findOrderNoByUserId(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select id,orderno from t_order where status<99 ");
        sql.append(" AND corp_id=(SELECT MAX(corp_id) FROM dtu_user WHERE id=? ) ORDER BY opt_time DESC");
        Object[] arg = new Object[]{userId };
        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("orderno");
            }
        });
        return list;
    }

    @Override
    public EasyUiUtil.PageForData findSNList(int rows, int page, String sort, String order, String orderNo) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT tsi.id, tsi.dtu_uuid, tsi.sn, tsi.temp_sn,(SELECT MAX(prod_typename) FROM prod_type WHERE id=tsi.prod_type) AS prodtype, ");
        sql.append(" tsi.hw_version, tsi.sw_version, tsi.uuid, (SELECT MAX(full_name) FROM dtu_user WHERE id=tsi.`opt_userid`) AS userid, ");
        sql.append(" DATE_FORMAT(tsi.opt_time, '%Y-%m-%d %H:%i:%s') AS opttime ");
        sql.append(" FROM t_sn_info tsi ");

        StringBuffer whereSql = new StringBuffer(" where tsi.status=1 AND tsi.orderno='" + orderNo + "' ");

        StringBuffer orderSql = new StringBuffer();
        if (!"".equals( StringUtil.null2String(sort).trim())){
            orderSql.append(" order by ").append(sort).append(" ").append(order);
        }else{// 默认排序为时间倒序，方便查询
            orderSql.append(" order by ").append(sort).append(" tsi.opt_time desc ");
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(EasyUiUtil.getPageSQL(sql.append(whereSql).append(orderSql).toString(), page, rows));

        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct from t_sn_info tsi ").append(whereSql.toString());
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));

        EasyUiUtil.PageForData pageObj = new EasyUiUtil.PageForData();
        pageObj.setRows(list);
        pageObj.setTotal(Long.parseLong(total));

        return pageObj;
    }

    @Override
    public EasyUiUtil.PageForData getOrderProduct(int rows, int page, String sort, String order, String orderNo, String keywords) {
        keywords = StringUtil.null2String(keywords).trim();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,device_code,m_sn,hw_version,p_sn,uuid,sw_version, ");
        sql.append(" case when status=1 then '已组装' ");
        sql.append(" when status=2 then '已测试' ");
        sql.append(" when status=3 then '已打印' ");
        sql.append(" when status=4 then '已联调' ");
        sql.append(" when status=5 then '已出货' ");
        sql.append(" else '未知' end as status ");
        sql.append(" FROM t_order_product ");

        StringBuffer whereSql = new StringBuffer(" where status<99 AND orderno='" + orderNo + "' ");
        if(!"".equals(keywords)){
            whereSql.append(" and m_sn like '%").append(keywords).append("%'");
        }

        StringBuffer orderSql = new StringBuffer();
        if (!"".equals( StringUtil.null2String(sort).trim())){
            orderSql.append(" order by ").append(sort).append(" ").append(order);
        }else{
            orderSql.append(" order by id desc ");
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(EasyUiUtil.getPageSQL(sql.append(whereSql).append(orderSql).toString(), page, rows));

        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct from t_order_product ").append(whereSql.toString());
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));

        EasyUiUtil.PageForData pageObj = new EasyUiUtil.PageForData();
        pageObj.setRows(list);
        pageObj.setTotal(Long.parseLong(total));

        return pageObj;
    }

    @Override
    public int updateStatus(String ids, String orderno, int status, ShiroUser user) {
        ids = StringUtil.null2String(ids).trim();
        if ("".equals(ids)){
            return -1;
        }

        List<String> sqlList = new ArrayList<String>();
        sqlList.add("update t_order_product set status=" + status + "  where id in " + StringUtil.getIdsplit(ids.split(",")));

        //日志记录
        StringBuffer logSql = new StringBuffer();
        logSql.append("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(").append(user.getId()).append(", 'ORDER', NOW(), '");
        logSql.append("修改订单出货信息>>订单编号：").append(orderno);
        //添加操作日志记录
        logSql.append("  产品ID：").append(ids).append("')");
        sqlList.add(logSql.toString());

        String[] sqlArr = new String[sqlList.size()];

        for (int i = 0; i <sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs[0];
    }

    @Override
    public int unbindOrderDevice(String ids, String orderno, String type, ShiroUser user) {
        Object[] arg = new Object[]{};
        List<String> list = jdbcTemplate.query("select m_sn from t_order_product where id in " + StringUtil.getIdsplit(ids.split(",")) , arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("m_sn");
            }
        });

        String mSNSql = StringUtil.getStrsplit(list);

        List<String> sqlList = new ArrayList<String>();
        // type  0-全部解绑 1-模块SN解绑 2-产品SN解绑
        if("0".equals(type)){
            //订单产品表修改状态为99
            sqlList.add("UPDATE t_order_product SET status=99 WHERE status<99 and m_sn IN " + mSNSql);

            //PCBA扫码状态为删除
            sqlList.add("UPDATE t_sn_pack SET status=2 WHERE status<2 and main_sn IN " + mSNSql);
            //工装状态修改
            sqlList.add("UPDATE t_tool_info SET status=2 WHERE status=1 and sn IN " + mSNSql);
            //模块扫码状态修改
            sqlList.add("UPDATE t_sn_info SET status=2 WHERE status=1 and temp_sn IN " + mSNSql);

            //系统联调信息状态修改
            sqlList.add("UPDATE t_system_joint SET status=2 WHERE status=1 and sn IN (select p_sn from t_order_product where id in " + StringUtil.getIdsplit(ids.split(",")) + ")");

        }
        else if ("1".equals(type)){
            //订单产品表修改状态为99
            sqlList.add("UPDATE t_order_product SET status=99 WHERE status<99 and m_sn IN " + mSNSql);

            //PCBA扫码解除绑定， 状态为0， 订单编号清除
            sqlList.add("UPDATE t_sn_pack SET status=0,module_sn='',orderno='' WHERE status<2 and main_sn IN " + mSNSql);
            //工装状态修改
            sqlList.add("UPDATE t_tool_info SET status=2 WHERE status=1 and sn IN " + mSNSql);
            //模块扫码状态修改
            sqlList.add("UPDATE t_sn_info SET status=2 WHERE status=1 and temp_sn IN " + mSNSql);

            //系统联调信息状态修改
            sqlList.add("UPDATE t_system_joint SET status=2 WHERE status=1 and sn IN (select p_sn from t_order_product where id in " + StringUtil.getIdsplit(ids.split(",")) + ")");

        }else if ("2".equals(type)){
            //订单产品表修改状态
            sqlList.add("UPDATE t_order_product SET status=2,p_sn='' WHERE status in (3, 4, 5) and m_sn IN " + mSNSql);// 1-已组装、2-已测试、3-已打印、4-已联调、5-已出货

            //工装状态修改
            sqlList.add("UPDATE t_tool_info SET status=2 WHERE status=1 and sn IN " + mSNSql);
            //模块扫码状态修改
            sqlList.add("UPDATE t_sn_info SET status=2 WHERE status=1 and temp_sn IN " + mSNSql);
            //PCBA状态修改
            sqlList.add("UPDATE t_sn_pack SET status=0,module_sn='' WHERE status=1 and main_sn IN " + mSNSql);
            //系统联调信息状态修改
            sqlList.add("UPDATE t_system_joint SET status=2 WHERE status=1 and sn IN (select p_sn from t_order_product where id in " + StringUtil.getIdsplit(ids.split(",")) + ")");
        }

        //日志记录
        StringBuffer logSql = new StringBuffer();
        logSql.append("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(").append(user.getId()).append(", 'ORDER', NOW(), '");
        logSql.append("订单设备解绑>>订单编号：").append(orderno);
        logSql.append(" 解绑类型：").append(type);
        logSql.append("  产品ID：").append(ids)
            .append("  模块SN码：").append(mSNSql.replace("'", "").replace("(", "").replace(")",""))
            .append("')");
        sqlList.add(logSql.toString());


        String[] sqlArr = new String[sqlList.size()];

        for (int i = 0; i <sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs[0];
    }

    @Override
    public int findSnCount4DelOrder(String orderno) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) as ct from t_sn_info tsi ").append(" where tsi.status=1 AND tsi.orderno='" + orderno + "' ");
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(sql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));
        return Integer.parseInt(total);
    }

    @Override
    public int findSnCount4DelDevice(String orderno, String deviceClass) {
        /*
        SELECT COUNT(*) AS ct FROM t_sn_info tsi WHERE tsi.status=1 AND tsi.orderno='LG201605050103'
        AND tsi.`prod_type` IN (SELECT id FROM prod_type WHERE prod_typename IN (SELECT device_code FROM t_order_device WHERE orderno='LG201605050103' AND device_class='BCU'));
        */
        /*StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) AS ct FROM t_sn_info tsi WHERE tsi.status=1 AND tsi.orderno='").append(orderno).append("' ");
        sql.append("AND tsi.prod_type IN (SELECT id FROM prod_type WHERE prod_typename IN (SELECT device_code FROM t_order_device WHERE orderno='").append(orderno).append("' AND device_class='").append(deviceClass).append("'))");
        */
        StringBuffer sql = new StringBuffer();                                                 //1-已组装、2-已测试、3-已打印、4-已联调和 5-已出货 5 种
        sql.append("SELECT COUNT(*) AS ct FROM t_order_product tsi WHERE tsi.status IN (2, 3, 4)  AND tsi.orderno='").append(orderno).append("' ");
        sql.append("AND tsi.device_code IN (SELECT distinct device_code FROM t_order_device WHERE orderno='").append(orderno).append("' AND device_class='").append(deviceClass).append("')");

        Map<String, Object> mapCount = jdbcTemplate.queryForMap(sql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));
        return Integer.parseInt(total);
    }

    @Override
    public int delOrderDevice(String orderno, String deviceClass, Long userId) {
        /**
         * SELECT * FROM t_order_file WHERE STATUS=1 AND orderno='LG201605050103' AND device_code IN (SELECT DISTINCT device_code FROM t_order_device WHERE orderno='LG201605050103' AND device_class='BCU');
         UPDATE t_order_file SET STATUS=2,del_time=NOW(),opt_user_id=11 WHERE STATUS=1 AND orderno='LG201605050103' AND device_code IN (SELECT DISTINCT device_code FROM t_order_device WHERE orderno='LG201605050103' AND device_class='BCU');
         */
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE t_order_file SET STATUS=2,del_time=NOW(),opt_user_id=")
                .append(userId).append(" WHERE STATUS=1 AND orderno='")
                .append(orderno).append("' AND device_code IN (SELECT DISTINCT device_code FROM t_order_device WHERE orderno='")
                .append(orderno).append("' AND device_class='").append(deviceClass).append("')");
        int rs = jdbcTemplate.update(sql.toString());
        return rs;
    }

    @Override
    public boolean checkOrder(String orderNo) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) as ct from t_order where status<99 and orderno='" + orderNo + "' ");
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(sql.toString());
        int total = StringUtil.StringToInt(StringUtil.null2String(mapCount.get("ct")), 0);

        if (total > 0){
            return true;
        }
        return false;
    }

    @Override
    public List<String> findDeviceByType(String deviceType) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT distinct m_type FROM t_sn_module WHERE remarks NOT LIKE '%退市%' ");
        if ("1_2".equals(deviceType)){
            sql.append(" AND device_type in (1, 2)");
        }
        else if ("3_4".equals(deviceType)){
            sql.append(" AND device_type in (3, 4)");
        }
        else{
            sql.append(" AND device_type = " + deviceType);
        }
        sql.append(" ORDER BY id DESC");
        Object[] arg = new Object[]{};
        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper() {

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("m_type");
            }
        });
        return list;
    }

    @Override
    public List<Object[]> findDeviceByOrderNo(String orderNo) {
        // device_code, device_class, bmu_no
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select device_code, device_class, bmu_no,device_type,device_count,id from t_order_device WHERE status=1 AND orderno='" + orderNo + "' order by id");
        Object[] arg = new Object[]{};
        List<Object[]> list = jdbcTemplate.query(sqlBuf.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] rsObj = new Object[5];
                rsObj[0] = resultSet.getString("device_code");
                rsObj[1] = resultSet.getString("device_class");
                rsObj[2] = resultSet.getString("bmu_no");
                rsObj[3] = resultSet.getString("device_type");
                rsObj[4] = resultSet.getString("device_count");
                return rsObj;
            }
        });

        if (list.size() > 0){

        }
        return list;
    }

    /**
     * 获取订单设备软件信息
     *
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, List<String>> findDeviceFileByOrderNo(String orderNo) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        StringBuffer sqlBuf = new StringBuffer("SELECT tof.device_code, tof.bmu_no, tof.file_type, tof.file_name, tof.file_code,(SELECT du.full_name FROM dtu_user du WHERE du.id=tof.opt_user_id) AS user_name, " +
                "DATE_FORMAT(tof.opt_time, '%Y-%m-%d %H:%i') AS opt_time FROM t_order_file tof ");
        sqlBuf.append(" WHERE tof.status=1 AND tof.orderno='").append(orderNo).append("'");
        Object[] arg = new Object[]{};
        List<Map<String, List<String>>> listOrderFile = jdbcTemplate.query(sqlBuf.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                List<String> fileInfo = new ArrayList<>();
                fileInfo.add(StringUtil.null2String(resultSet.getString("file_name")));
                fileInfo.add(StringUtil.null2String(resultSet.getString("file_code")));
                fileInfo.add(StringUtil.null2String(resultSet.getString("user_name")));
                fileInfo.add(StringUtil.null2String(resultSet.getString("opt_time")));

                Map<String, List<String>> map = new HashMap<String, List<String>>();
                String keyStr = resultSet.getString("device_code") + "_" + resultSet.getString("bmu_no") + "_" + resultSet.getString("file_type");
                map.put(keyStr, fileInfo);
                return map;
            }
        });

        if (listOrderFile != null && listOrderFile.size()>0) {
            for (int i = 0; i < listOrderFile.size(); i++) {
                map.putAll(listOrderFile.get(i));
            }
        }
        return map;
    }

    /**
     * 保存订单软件
     * @param req
     * @param user
     * @return
     */
    @Override
    public int saveDeviceFile(HttpServletRequest req, ShiroUser user) {
        List<String> sqlList = new ArrayList<String>();
        String orderno = StringUtil.null2String(req.getParameter("orderno"));
        Order order = findInfoByOrderNo(orderno);

        //查询原有的软件程序
        String oldFileSql = "select CONCAT(device_code,bmu_no,file_type) AS mapkey,file_code from t_order_file where status=1 AND orderno='" + orderno + "'";
        List<String[]> oldDeviceList = jdbcTemplate.query(oldFileSql, new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                String[] arr = {
                        resultSet.getString("mapkey"),
                        resultSet.getString("file_code")
                };
                return arr;
            }
        });

        Map<String, String> deviceMap = new HashMap<String, String>();
        if (oldDeviceList != null && oldDeviceList.size()>0) {
            for (int i = 0; i < oldDeviceList.size(); i++) {
                String[] arr = oldDeviceList.get(i);
                deviceMap.put(arr[0], arr[1]);
            }
        }

        Long userId = user.getId();
        String bh = BhGenerator.getBh();

        //清除原订单文件信息 1-正常 2-替换 3-删除 改为逐条修改 sby 2016年9月14日 16:10:30
        //sqlList.add("UPDATE t_order_file SET status=2,opt_user_id=" + userId + ",del_time=now() WHERE status=1 AND orderno='" + orderno + "' ");

        Enumeration<String> keys = req.getParameterNames();
        String keyStr = "";
        String valueStr = "";
        StringBuffer sqlSbuf ;
        String[] deviceInfoArr;
        while(keys.hasMoreElements()) {
            keyStr = keys.nextElement();
            if (keyStr.endsWith("_devicefile")){
                valueStr = StringUtil.null2String(req.getParameter(keyStr)).replace(",", "");//检查file_code，已存在的不重新添加
                deviceInfoArr = keyStr.split("_");// BCU_BC52B_0_s19_devicefile
                if (deviceInfoArr.length == 5 && !"".equals(valueStr)){
                    //剔除未修改的
                    String strKey = deviceMap.get(deviceInfoArr[1].replace("-L", "(").replace("-R", ")") + deviceInfoArr[2] + deviceInfoArr[3]);
                    if (strKey != null && valueStr.equals(strKey) ) {

                    }else{
                        //清除原订单文件信息 1-正常 2-替换 3-删除
                        sqlList.add("UPDATE t_order_file SET status=2,opt_user_id=" + userId + ",del_time=now() WHERE status=1 AND orderno='" + orderno + "' and device_code='" + deviceInfoArr[1].replace("-L", "(").replace("-R", ")") + "' and bmu_no='" + deviceInfoArr[2] + "' and file_type='" + deviceInfoArr[3] + "' and batch_no<>'" + bh + "'");

                        sqlSbuf = new StringBuffer("INSERT INTO t_order_file (batch_no, orderno, device_code, bmu_no, file_type, file_code, start_time, status, opt_user_id, opt_time) ");
                        sqlSbuf.append(" VALUES('").append(bh).append("',")
                                .append("'").append(orderno).append("',")
                                .append("'").append(deviceInfoArr[1]).append("',")
                                .append("'").append(deviceInfoArr[2]).append("',")
                                .append("'").append(deviceInfoArr[3]).append("',")
                                .append("'").append(valueStr).append("',")
                                .append("NOW(),'1','" + userId + "',NOW())");
                        sqlList.add(sqlSbuf.toString());
                    }
                }
            }
        }

         //修改文件名
        sqlList.add("UPDATE t_order_file tof SET " +
                "tof.file_name=(SELECT MAX(tfu.file_name) FROM t_file_upload tfu WHERE tfu.file_code = tof.file_code )," +
                "tof.md5_code=(SELECT MAX(tfu.md5_code) FROM t_file_upload tfu WHERE tfu.file_code = tof.file_code )" +
                " WHERE tof.file_name IS NULL AND STATUS=1");
        //修改设备型号有小括号的情况
        sqlList.add("UPDATE t_order_file SET device_code=REPLACE(REPLACE(device_code, '-R', ')'), '-L', '(') WHERE status=1 and orderno='" + orderno + "'");

        String[] sqlArr = new String[sqlList.size()];

        for (int i = 0; i <sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        if (rs[0] < 0){//批量操作不成功时
            return -1;
        }

        String uploadType = StringUtil.null2String(req.getParameter("uploadType"));// 1- 齐套上传 2-非齐套上传
        StringBuffer msgBuff = new StringBuffer("\n\n");//拼装邮件内容
        StringBuffer titleBuff = new StringBuffer();//拼装邮件标题
        msgBuff.append("    客户名称：").append(order.getCorp().getCorpName()).append("\n");
        msgBuff.append("    项目描述：").append(order.getProjectNote()).append("\n");
        msgBuff.append("    订单套数：").append(order.getQuantity()).append("\n");
        msgBuff.append("    订单编号：").append(orderno).append("\n");
        titleBuff.append("订单").append(orderno);
        if ("1".equals(uploadType)){
            msgBuff.append("    上传类型：齐套上传\n\n");
            titleBuff.append("软件齐套上传");
        }else{
            msgBuff.append("    上传类型：非齐套上传\n\n");
            titleBuff.append("软件非齐套上传");
        }

        String uploadNumSql = "SELECT COUNT(*) AS ct FROM (SELECT batch_no FROM t_order_file WHERE orderno='" + orderno + "' GROUP BY batch_no) a";
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(uploadNumSql);
        String totalStr = StringUtil.null2String(mapCount.get("ct"));
        int total = StringUtil.StringToInt(totalStr);
        if ( total > 1){
            titleBuff.append("，软件上传次数【").append(total).append("】");
        }

        //已上传订单软件信息
        /**
         * SELECT tf.id, (SELECT MAX(td.device_type) FROM t_order_device td WHERE td.device_code=tf.device_code AND td.status=1 AND td.orderno='LG201606270201'  ) AS device_type , CASE
         WHEN tf.file_code ='usetemplate' THEN CONCAT('设备型号：', tf.device_code, '    从机ID：', tf.bmu_no, '    文件名：使用模版')
         ELSE CONCAT('设备型号：', tf.device_code, '    从机ID：', tf.bmu_no, '    文件名：', tf.file_name)
         END AS msg FROM t_order_file tf WHERE tf.status=1 AND tf.orderno='LG201606270201' ORDER BY device_type ASC, tf.bmu_no ASC, msg DESC, tf.id ASC ;
         */
        /*StringBuffer uploadSql = new StringBuffer();
        uploadSql.append(" SELECT tf.id, (SELECT MAX(td.device_type) FROM t_order_device td WHERE td.device_code=tf.device_code AND td.status=1 AND td.orderno='").append(orderno).append("'  ) AS device_type ,CASE ");
        uploadSql.append(" WHEN tf.file_code ='usetemplate' THEN CONCAT('设备型号：', tf.device_code, '    从机ID：', tf.bmu_no, '    文件名：使用模版')  ");
        uploadSql.append(" ELSE CONCAT('设备型号：', tf.device_code, '    从机ID：', tf.bmu_no, '    文件名：', tf.file_name)  ");
        uploadSql.append(" END AS msg FROM t_order_file tf WHERE tf.status=1 AND tf.orderno='").append(orderno).append("' AND tf.batch_no='").append(bh).append("' ORDER BY device_type ASC, tf.bmu_no ASC, msg DESC, tf.id ASC ");
*/
        /**
         * SELECT id,CASE WHEN device_type='0' THEN '99' ELSE device_type END AS device_type,
         CASE WHEN device_type='5' THEN
         CASE WHEN a.file_code ='usetemplate' THEN CONCAT('设备型号：', a.device_code, '    从机ID：', a.bmu_no, '    文件名：使用模版')
         ELSE CONCAT('设备型号：', a.device_code, '    从机ID：', a.bmu_no, '    文件名：', a.file_name)
         END
         ELSE
         CASE WHEN a.file_code ='usetemplate' THEN CONCAT('设备型号：', a.device_code, '    文件名：使用模版')
         ELSE CONCAT('设备型号：', a.device_code, '    文件名：', a.file_name)
         END
         END AS msg FROM (
         SELECT tf.id, (SELECT MAX(td.device_type) FROM t_order_device td WHERE td.device_code=tf.device_code AND td.status=1 AND td.orderno='LG201606270201'  ) AS device_type ,
         tf.file_code,tf.device_code,tf.bmu_no,tf.file_name
         FROM t_order_file tf WHERE tf.status=1 AND tf.orderno='LG201606270201' AND tf.`batch_no`='20160728172956754000'
         ) a ORDER BY a.device_type ASC, a.bmu_no ASC, msg DESC, a.id ASC
         */
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT id,CASE WHEN device_type='0' THEN '99' ELSE device_type END AS device_type_show, ")
                .append("         CASE WHEN device_type='5' THEN  ")
                .append(" 		CASE WHEN a.file_code ='usetemplate' THEN CONCAT('设备型号：', a.device_code, '    从机ID：', a.bmu_no, '    文件名：使用模版') ")
                .append(" 		ELSE CONCAT('设备型号：', a.device_code, '    从机ID：', a.bmu_no, '    文件名：', a.file_name) ")
                .append(" 		END  ")
                .append("         ELSE ")
                .append(" 		CASE WHEN a.file_code ='usetemplate' THEN CONCAT('设备型号：', a.device_code, '    文件名：使用模版') ")
                .append(" 		ELSE CONCAT('设备型号：', a.device_code, '    文件名：', a.file_name) ")
                .append(" 		END          ")
                .append("         END AS msg FROM ( ")
                .append("          SELECT tf.id, (SELECT MAX(td.device_type) FROM t_order_device td WHERE td.device_code=tf.device_code AND td.status=1 AND td.orderno='").append(orderno).append("'  ) AS device_type ,  ")
                .append("          tf.file_code,tf.device_code,tf.bmu_no,tf.file_name ")
                .append("          FROM t_order_file tf WHERE tf.status=1 AND tf.orderno='").append(orderno).append("' AND tf.batch_no='").append(bh).append("' ")
                .append("          ) a ORDER BY device_type_show ASC, a.bmu_no ASC, msg DESC, a.id ASC ");


        Object[] arg = new Object[]{};
        List<String> deviceList = jdbcTemplate.query(sqlBuf.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("msg");
            }
        });
        if (deviceList != null && deviceList.size() > 0) {
            for (int i = 0; i < deviceList.size(); i++) {
                msgBuff.append("    ").append(deviceList.get(i)).append("\n");
            }

            msgBuff.append("\n\n");
            msgBuff.append("    ").append("下载地址：http://192.168.1.21:10020/odt-ligoo/orderService/detail?orderno=").append(orderno);

            if (order.getOrderType() == 1){
                emailManager.sendMail(titleBuff.toString(), msgBuff.toString(), 1, userId);
            }else {
                emailManager.sendMail(titleBuff.toString(), msgBuff.toString(), 2, userId);
            }
        }

        //保存订单日志信息
        OrderTrack orderTrackDB = orderTrackManager.findNewTrackByOrderNo(orderno);
        OrderTrack orderTrack = new OrderTrack();
        Date date = new Date();

        orderTrack.setOrderno(orderno);

        orderTrack.setActionMan(user.getName());
        if ("1".equals(uploadType)){
            orderTrack.setOrderStatus(8);
            orderTrack.setActionName("已齐套上传");
            orderTrack.setRemarks("软件上传人:" + user.getName() + "  已齐套上传");
        }else{
            orderTrack.setOrderStatus(11);
            orderTrack.setActionName("非齐套上传");
            orderTrack.setRemarks("软件上传人:" + user.getName() + "  非齐套上传");
        }
        orderTrack.setActionDate(date);
        if(date.getHours() > 12){
            orderTrack.setAmPm("PM");
        }else{
            orderTrack.setAmPm("AM");
        }
        orderTrack.setStatus(1);
        orderTrack.setOptTime(new Date());
        orderTrack.setOptUserId(userId);
        orderTrack.setIsNew(1);
        if (orderTrackDB != null) {
            orderTrack.setProposerMan(orderTrackDB.getProposerMan());
            orderTrack.setReviewStime(orderTrackDB.getReviewStime());
            orderTrack.setReviewEtime(orderTrackDB.getReviewEtime());
            orderTrack.setReviewMan(orderTrackDB.getReviewMan());
            orderTrack.setReviewDelivery(orderTrackDB.getReviewDelivery());
            orderTrack.setDevelopDelivery(orderTrackDB.getDevelopDelivery());
            orderTrack.setDevelopMan(orderTrackDB.getDevelopMan());
            orderTrack.setDevelopStime(orderTrackDB.getDevelopStime());
            orderTrack.setDevelopEtime(orderTrackDB.getDevelopEtime());
            orderTrack.setTestDelivery(orderTrackDB.getTestDelivery());
            orderTrack.setTestStime(orderTrackDB.getTestStime());
            orderTrack.setTestEtime(orderTrackDB.getTestEtime());
            orderTrack.setTestMan(orderTrackDB.getTestMan());
            orderTrack.setReviewDevelopDuration(orderTrackDB.getReviewDevelopDuration());
            orderTrack.setReviewTestDuration(orderTrackDB.getReviewTestDuration());
            orderTrack.setDevelopDuration(orderTrackDB.getDevelopDuration());
            orderTrack.setTestDuration(orderTrackDB.getTestDuration());
        }

        Long rsTrack = orderTrackManager.saveOrderTrack(orderTrack);

        return rs.length;
    }

    /**
     * 获取订单设备信息，以及对应的设备文件
     * @param orderno
     * @return
     */
    @Override
    public List<Map<String, Object>> findOrderDevice(String orderno) {
        String sql = "SELECT device_code,device_class,device_type,bmu_no,device_count FROM t_order_device WHERE status=1 AND orderno='" + orderno + "' ORDER BY id";

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        if (list != null) {
            String device_code;
            String bmu_no;
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);

                StringBuffer sqlBuf = new StringBuffer();
                sqlBuf.append("SELECT ");
                sqlBuf.append("CASE WHEN 'usetemplate'=tof.file_code ");
                sqlBuf.append("THEN ");
                sqlBuf.append("(SELECT IFNULL(MAX(tsi.file_code), '') FROM t_sw_info tsi WHERE tsi.status=1 AND tsi.id=(SELECT IFNULL(sw_id, 0) FROM t_sw_hw WHERE STATUS=1 AND hw_id=(SELECT IFNULL(MAX(id),0) FROM prod_type WHERE prod_typename=tof.device_code))) ");
                sqlBuf.append("ELSE tof.file_code ");
                sqlBuf.append("END AS file_code,");
                sqlBuf.append("CASE WHEN 'usetemplate'=tof.file_code ");
                sqlBuf.append("THEN ");
                sqlBuf.append("(SELECT IFNULL(MAX(tsi.file_name), '') FROM t_sw_info tsi WHERE tsi.status=1 AND tsi.id=(SELECT IFNULL(sw_id, 0) FROM t_sw_hw WHERE STATUS=1 AND hw_id=(SELECT IFNULL(MAX(id),0) FROM prod_type WHERE prod_typename=tof.device_code))) ");
                sqlBuf.append("ELSE tof.file_name ");
                sqlBuf.append("END AS file_name,");
                sqlBuf.append("tof.file_type ");
                sqlBuf.append("FROM t_order_file tof WHERE tof.status=1 AND tof.orderno='").append(orderno).append("' AND tof.device_code='")
                        .append(StringUtil.null2String(map.get("device_code"))).append("' AND tof.bmu_no=").append(StringUtil.null2String(map.get(("bmu_no"))));

                map.put("files", jdbcTemplate.queryForList(sqlBuf.toString()));
            }
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> findOrderDeviceAll(String orderno) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("SELECT tof.device_code,tof.bmu_no, file_type,");
        sqlBuf.append("CASE WHEN 'usetemplate'=tof.file_code ");
        sqlBuf.append("THEN ");
        sqlBuf.append("(SELECT IFNULL(MAX(tsi.file_code), '') FROM t_sw_info tsi WHERE tsi.status=1 AND tsi.id=(SELECT IFNULL(sw_id, 0) FROM t_sw_hw WHERE STATUS=1 AND hw_id=(SELECT IFNULL(MAX(id),0) FROM prod_type WHERE prod_typename=tof.device_code))) ");
        sqlBuf.append("ELSE tof.file_code ");
        sqlBuf.append("END AS file_code,");
        sqlBuf.append("CASE WHEN 'usetemplate'=tof.file_code ");
        sqlBuf.append("THEN ");
        sqlBuf.append("(SELECT IFNULL(MAX(tsi.file_name), '') FROM t_sw_info tsi WHERE tsi.status=1 AND tsi.id=(SELECT IFNULL(sw_id, 0) FROM t_sw_hw WHERE STATUS=1 AND hw_id=(SELECT IFNULL(MAX(id),0) FROM prod_type WHERE prod_typename=tof.device_code))) ");
        sqlBuf.append("ELSE tof.file_name ");
        sqlBuf.append("END AS file_name,(SELECT CASE WHEN device_type='0' THEN '99' ELSE device_type END AS device_type FROM t_sn_module WHERE m_type=tof.device_code) AS device_type ");
        sqlBuf.append("FROM t_order_file tof WHERE tof.status=1 AND tof.orderno='").append(orderno).append("' order by device_type,bmu_no, file_type DESC ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlBuf.toString());

        return list;
    }

    /**
     * 获取订单信息
     * @param req
     * @return
     */
    @Override
    public List<Map<String, String>> findOrderList4Page(HttpServletRequest req, int pageSize) {
        try {
            Integer pageNumber = ServletRequestUtils.getIntParameter(req, "page");
            int page = 0;
            if (pageNumber.intValue() < 1){
                page = 1;
            }else{
                page = pageNumber.intValue();
            }
            int sNumber = (page - 1)*pageSize ;
            int eNumber = pageSize ;

            StringBuffer sqlBuff = new StringBuffer();
            /*sqlBuff.append("SELECT tab.id,tab.orderno,tab.contract_no,(SELECT corp.corp_name FROM corp WHERE corp.id=tab.corp_id ) AS corp_name,tab.salesman,tab.quantity, tab.STATUS");
            sqlBuff.append(",IFNULL((SELECT DATE_FORMAT(MAX(tot.review_delivery), '%Y-%m-%d') AS review_delivery FROM t_order_track tot WHERE tot.status=1 and tot.is_new=1 AND tot.orderno=tab.orderno), '') AS review_delivery ");
            sqlBuff.append(" FROM t_order tab ");*/

            sqlBuff.append("SELECT tob.`id`,tob.`orderno`,tob.`contract_no`,(SELECT cp.`corp_name` FROM corp cp WHERE cp.`id`=tob.`corp_id` ) AS corp_name," +
                    "IFNULL((SELECT full_name FROM dtu_user dtu WHERE dtu.`id`=tob.`salesman` ), '无') AS salesman,tob.`quantity`, tob.`STATUS` ");
            sqlBuff.append(",IFNULL((SELECT MAX(DATE_FORMAT(tot.`review_delivery`, '%Y-%m-%d')) FROM t_order_track tot WHERE tot.`status`=1 AND tot.`is_new`=1 AND tot.`orderno`=tob.`orderno`), '') AS review_delivery, ");
            sqlBuff.append(" CASE  ")
                    .append(" WHEN tob.`STATUS`=8 THEN 1 ")
                    .append(" WHEN tob.`STATUS`=10 THEN 1 ")
                    .append(" ELSE 0 END AS px ");
            sqlBuff.append(" FROM t_order tob ");

            StringBuffer whereBuff = new StringBuffer(" WHERE tob.STATUS<99 and SUBSTR(REPLACE(REPLACE(tob.orderno, 'LG-SH-', ''), 'LG', ''), 1, 8) > '20160531' and tob.order_type < 3 ");

            String orderno = StringUtil.null2String(ServletRequestUtils.getStringParameter(req, "orderno")).trim();
            if(!"".equals(orderno)){
                whereBuff.append(" and (tob.orderno like '%").append(orderno).append("%' or contract_no like '%").append(orderno).append("%')");
            }

            String corp_id = ServletRequestUtils.getStringParameter(req, "corp_id");
            if(!"-1".equals(corp_id)){
                whereBuff.append(" and tob.corp_id=").append(corp_id);
            }

            String salesman = ServletRequestUtils.getStringParameter(req, "salesman");
            if(!"-1".equals(salesman)){
                whereBuff.append(" and tob.salesman='").append(salesman).append("'");
            }

            String orderstatus = ServletRequestUtils.getStringParameter(req, "orderstatus");
            if(!"-1".equals(orderstatus)){
                whereBuff.append(" and tob.status='").append(orderstatus).append("'");
            }

            String order_type = ServletRequestUtils.getStringParameter(req, "order_type");
            if(!"-1".equals(order_type)){
                whereBuff.append(" and tob.orderno like '").append(order_type).append("%'");
            }

            String endSql = " ORDER BY px asc, tob.`STATUS` asc,tob.id DESC LIMIT " + sNumber + "," + pageSize;
            if(!"-1".equals(orderstatus)){ // 状态查询时按照时间倒序排列  sby 2016年7月14日 17:20:09
                endSql = " ORDER BY tob.id DESC LIMIT " + sNumber + "," + pageSize;
            }

            List<Map<String, String>> list = jdbcTemplate.query(sqlBuff.append(whereBuff).append(endSql).toString(), new Object[]{}, new RowMapper(){

                @Override
                public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", StringUtil.null2String(resultSet.getString("id")));
                    map.put("orderno", StringUtil.null2String(resultSet.getString("orderno")));
                    map.put("contract_no", StringUtil.null2String(resultSet.getString("contract_no")));
                    map.put("corp_name", StringUtil.null2String(resultSet.getString("corp_name")));
                    map.put("salesman", StringUtil.null2String(resultSet.getString("salesman")));
                    map.put("quantity", StringUtil.null2String(resultSet.getString("quantity")));
                    map.put("status", StringUtil.null2String(resultSet.getString("STATUS")));
                    map.put("review_delivery", StringUtil.null2String(resultSet.getString("review_delivery")));
                    return map;
                }
            });
            return list;
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, String>> findOrderList4Page(HttpServletRequest req, int pageSize, Long userId) {
        try {
            Integer pageNumber = ServletRequestUtils.getIntParameter(req, "page");
            int page = 0;
            if (pageNumber.intValue() < 1){
                page = 1;
            }else{
                page = pageNumber.intValue();
            }
            int sNumber = (page - 1)*pageSize ;
            int eNumber = pageSize ;

            StringBuffer sqlBuff = new StringBuffer();

            sqlBuff.append("SELECT tob.`id`,tob.`orderno`,tob.`contract_no`,(SELECT cp.`corp_name` FROM corp cp WHERE cp.`id`=tob.`corp_id` ) AS corp_name," +
                    "IFNULL((SELECT full_name FROM dtu_user dtu WHERE dtu.id=tob.`salesman` ), '无') AS salesman,tob.`quantity`, tob.`STATUS` ");
            sqlBuff.append(",IFNULL((SELECT MAX(DATE_FORMAT(tot.`review_delivery`, '%Y-%m-%d')) FROM t_order_track tot WHERE tot.`status`=1 AND tot.`is_new`=1 AND tot.`orderno`=tob.`orderno`), '') AS review_delivery, ");
            sqlBuff.append(" CASE  ")
                    .append(" WHEN tob.`STATUS`=8 THEN 1 ")
                    .append(" WHEN tob.`STATUS`=10 THEN 1 ")
                    .append(" ELSE 0 END AS px ");
            sqlBuff.append(" FROM t_order tob ");

            StringBuffer whereBuff = new StringBuffer(" WHERE tob.STATUS<99 and SUBSTR(REPLACE(REPLACE(tob.orderno, 'LG-SH-', ''), 'LG', ''), 1, 8) > '20160531' and tob.order_type < 3 and tob.salesman='" + userId + "'");

            String orderno = StringUtil.null2String(ServletRequestUtils.getStringParameter(req, "orderno")).trim();
            if(!"".equals(orderno)){
                whereBuff.append(" and (tob.orderno like '%").append(orderno).append("%' or contract_no like '%").append(orderno).append("%')");
            }

            String corp_id = ServletRequestUtils.getStringParameter(req, "corp_id");
            if(!"-1".equals(corp_id)){
                whereBuff.append(" and tob.corp_id=").append(corp_id);
            }

            String salesman = ServletRequestUtils.getStringParameter(req, "salesman");
            if(!"-1".equals(salesman)){
                whereBuff.append(" and tob.salesman='").append(salesman).append("'");
            }

            String orderstatus = ServletRequestUtils.getStringParameter(req, "orderstatus");
            if(!"-1".equals(orderstatus)){
                whereBuff.append(" and tob.status='").append(orderstatus).append("'");
            }

            String order_type = ServletRequestUtils.getStringParameter(req, "order_type");
            if(!"-1".equals(order_type)){
                whereBuff.append(" and tob.orderno like '").append(order_type).append("%'");
            }

            String endSql = " ORDER BY px asc, tob.`STATUS` asc,tob.id DESC LIMIT " + sNumber + "," + pageSize;
            if(!"-1".equals(orderstatus)){ // 状态查询时按照时间倒序排列  sby 2016年7月14日 17:20:09
                endSql = " ORDER BY tob.id DESC LIMIT " + sNumber + "," + pageSize;
            }

            List<Map<String, String>> list = jdbcTemplate.query(sqlBuff.append(whereBuff).append(endSql).toString(), new Object[]{}, new RowMapper(){

                @Override
                public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", StringUtil.null2String(resultSet.getString("id")));
                    map.put("orderno", StringUtil.null2String(resultSet.getString("orderno")));
                    map.put("contract_no", StringUtil.null2String(resultSet.getString("contract_no")));
                    map.put("corp_name", StringUtil.null2String(resultSet.getString("corp_name")));
                    map.put("salesman", StringUtil.null2String(resultSet.getString("salesman")));
                    map.put("quantity", StringUtil.null2String(resultSet.getString("quantity")));
                    map.put("status", StringUtil.null2String(resultSet.getString("STATUS")));
                    map.put("review_delivery", StringUtil.null2String(resultSet.getString("review_delivery")));
                    return map;
                }
            });
            return list;
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取订单数量
     * @param req
     * @return
     */
    @Override
    public Integer findOrderCount4Page(HttpServletRequest req) {
        StringBuffer sqlBuff = new StringBuffer();
        sqlBuff.append("SELECT count(*) as ct FROM t_order tab  ");
        sqlBuff.append(" WHERE tab.status<99 and SUBSTR(REPLACE(REPLACE(tab.orderno, 'LG-SH-', ''), 'LG', ''), 1, 8) > '20160531' and tab.order_type < 3 ");

        try {
            String orderno = StringUtil.null2String(ServletRequestUtils.getStringParameter(req, "orderno")).trim();
            if(!"".equals(orderno)){
                sqlBuff.append(" and (tab.orderno like '%").append(orderno).append("%' or contract_no like '%").append(orderno).append("%')");
            }

            String corp_id = ServletRequestUtils.getStringParameter(req, "corp_id");
            if(!"-1".equals(corp_id)){
                sqlBuff.append(" and tab.corp_id=").append(corp_id);
            }

            String salesman = ServletRequestUtils.getStringParameter(req, "salesman");
            if(!"-1".equals(salesman)){
                sqlBuff.append(" and tab.salesman='").append(salesman).append("'");
            }

            String orderstatus = ServletRequestUtils.getStringParameter(req, "orderstatus");
            if(!"-1".equals(orderstatus)){
                sqlBuff.append(" and tab.status='").append(orderstatus).append("'");
            }

            String order_type = ServletRequestUtils.getStringParameter(req, "order_type");
            if(!"-1".equals(order_type)){
                sqlBuff.append(" and tab.orderno like '").append(order_type).append("%'");
            }

        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }

        //StringBuffer whereBuff = new StringBuffer(" WHERE tab.status<99 ");

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuff.toString());
        if (map == null) {
            return 0;
        }else{
            Object ct = map.get("ct");
            if (ct != null){
                return StringUtil.StringToInt(StringUtil.null2String(ct));
            }
        }
        return 0;
    }

    @Override
    public Integer findOrderCount4Page(HttpServletRequest req, Long userId) {
        StringBuffer sqlBuff = new StringBuffer();
        sqlBuff.append("SELECT count(*) as ct FROM t_order tab  ");
        sqlBuff.append(" WHERE tab.status<99 and SUBSTR(REPLACE(REPLACE(tab.orderno, 'LG-SH-', ''), 'LG', ''), 1, 8) > '20160531' and tab.order_type < 3 and tab.salesman='" + userId + "'");

        try {
            String orderno = StringUtil.null2String(ServletRequestUtils.getStringParameter(req, "orderno")).trim();
            if(!"".equals(orderno)){
                sqlBuff.append(" and (tab.orderno like '%").append(orderno).append("%' or contract_no like '%").append(orderno).append("%')");
            }

            String corp_id = ServletRequestUtils.getStringParameter(req, "corp_id");
            if(!"-1".equals(corp_id)){
                sqlBuff.append(" and tab.corp_id=").append(corp_id);
            }

            String salesman = ServletRequestUtils.getStringParameter(req, "salesman");
            if(!"-1".equals(salesman)){
                sqlBuff.append(" and tab.salesman='").append(salesman).append("'");
            }

            String orderstatus = ServletRequestUtils.getStringParameter(req, "orderstatus");
            if(!"-1".equals(orderstatus)){
                sqlBuff.append(" and tab.status='").append(orderstatus).append("'");
            }

            String order_type = ServletRequestUtils.getStringParameter(req, "order_type");
            if(!"-1".equals(order_type)){
                sqlBuff.append(" and tab.orderno like '").append(order_type).append("%'");
            }
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }

        //StringBuffer whereBuff = new StringBuffer(" WHERE tab.status<99 ");

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuff.toString());
        if (map == null) {
            return 0;
        }else{
            Object ct = map.get("ct");
            if (ct != null){
                return StringUtil.StringToInt(StringUtil.null2String(ct));
            }
        }
        return 0;
    }

    /**
     * 获取非删除订单信息 listType 1-软件管理订单 2-生产管理订单 （都包含新订单内容）
     * @param listType
     * @return
     */
    @Override
    public List<String[]> findSimpleOrderList(int listType) {
        String sql = "SELECT id,orderno,STATUS AS st, order_type FROM t_order WHERE STATUS<99 AND order_type IN (1, 2) ORDER BY id DESC";
        if (listType == 2){
            sql = "SELECT id,orderno,STATUS AS st, order_type FROM t_order WHERE STATUS<99 AND order_type IN (1, 3) ORDER BY id DESC";
            //新订单只有齐套和非齐套已上传软件的订单显示到生产管理界面 sby pop1.3
            // 靳春林
            /*sql = "SELECT ta.id,ta.orderno,ta.STATUS AS st, ta.order_type FROM t_order ta " +
                    "LEFT JOIN ( SELECT orderno,COUNT(*) AS ct FROM t_order_file WHERE STATUS=1 GROUP BY orderno) tf ON tf.orderno=ta.orderno WHERE ((tf.ct>0 AND ta.order_type=1) OR ta.order_type=3) AND ta.STATUS<99 AND ta.order_type IN (1, 3) ORDER BY id DESC";
            */
        }
        List<String[]> list = jdbcTemplate.query(sql, new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                String[] obj = new String[4];
                obj[0] = StringUtil.null2String(resultSet.getString("id"));
                obj[1] = StringUtil.null2String(resultSet.getString("orderno"));
                obj[2] = StringUtil.null2String(resultSet.getString("st"));
                obj[3] = StringUtil.null2String(resultSet.getString("order_type"));
                return obj;
            }
        });

        return list;
    }

    /**
     * 获取设备信息（系统配置）
     *
     * @param orderNo
     * @return
     */
    @Override
    public List<String[]> findOrderDeviceList(String orderNo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT tod.id,tod.device_code,tod.device_class,CASE WHEN tod.bmu_no=0 THEN '-' ELSE CONCAT(tod.bmu_no, '#') END AS bmu_no,");
        sql.append("CASE WHEN SUBSTR(tod.orderno, 1, 6)='LG-SH-' THEN '-' ELSE tod.device_count END AS device_count,");
        sql.append("(SELECT d.full_name FROM dtu_user d WHERE d.id=tod.opt_user_id ) AS optuser, DATE_FORMAT(tod.opt_time, '%Y-%m-%d %H:%i') AS opttime ");
        sql.append("FROM t_order_device tod WHERE tod.status=1 AND tod.orderno='").append(orderNo).append("' ORDER BY tod.id ASC;");

        List<String[]> list = jdbcTemplate.query(sql.toString(), new Object[]{}, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                String[] obj = new String[6];
                obj[0] = StringUtil.null2String(resultSet.getString("device_class"));
                obj[1] = StringUtil.null2String(resultSet.getString("device_code"));
                obj[2] = StringUtil.null2String(resultSet.getString("bmu_no"));
                obj[3] = StringUtil.null2String(resultSet.getString("device_count"));
                obj[4] = StringUtil.null2String(resultSet.getString("optuser"));
                obj[5] = StringUtil.null2String(resultSet.getString("opttime"));
                return obj;
            }
        });

        return list;
    }

    @Override
    public EasyUiUtil.PageForData findEasyUiOrderList(int rows, int page, String sort, String order, String orderNo, String corpId) {
        /****************************************************************************************
         * 原始SQL脚本
         SELECT id,quantity,project_note,opt_user_name,opt_time FROM t_order WHERE corp_id=12;
         ****************************************************************************************/

        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT id,orderno,quantity,project_note,order_type,opt_user_name,DATE_FORMAT(opt_time, '%Y-%m-%d %H:%i') AS opttime  FROM t_order tot ");
        StringBuffer whereSql = new StringBuffer(" where tot.status <99 and tot.corp_id=").append(corpId);
        if (!"".equals( StringUtil.null2String(orderNo).trim())) {
            whereSql.append(" AND tot.orderno like '%" + orderNo.trim() + "%' ");
        }
        StringBuffer orderSql = new StringBuffer();
        if (!"".equals( StringUtil.null2String(sort).trim())){
            orderSql.append(" order by ").append(sort).append(" ").append(order);
        }else{// 默认排序为按状态排序
            orderSql.append(" order by tot.status asc, tot.id desc ");
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(EasyUiUtil.getPageSQL(sqlBuf.append(whereSql).append(orderSql).toString(), page, rows));

        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct from t_order tot ").append(whereSql.toString());
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));

        EasyUiUtil.PageForData pageObj = new EasyUiUtil.PageForData();
        pageObj.setRows(list);
        pageObj.setTotal(Long.parseLong(total));

        return pageObj;
    }

    @Override
    public int findSoftware4Order(String orderNo) {
        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct FROM t_order_file WHERE STATUS=1 AND orderno='").append(orderNo).append("'");
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));
        return Integer.valueOf(total);
    }

    @Override
    public int findBound4Order(String orderNo) {
        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct from t_order WHERE STATUS<99 AND bound_order='").append(orderNo).append("'");
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));
        return Integer.valueOf(total);
    }

    @Override
    public int findCountByCorpId(String corpId) {
        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct from t_order WHERE STATUS<99 AND corp_id=").append(corpId);
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));
        return Integer.valueOf(total);
    }

    /**
     * 判断订单套数  如果有设备型号已绑定数量大于新套数则不能执行修改操作，返回false
     * @param orderNo
     * @param quantity
     * @return
     */
    private boolean checkDeviceCount(String orderNo, Long quantity){
        /***
         * 查询修改后的数量情况
         */
        Map<String, Integer> mapCount = new HashMap<String, Integer>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT device_code FROM t_order_device WHERE status=1 AND orderno='").append(orderNo).append("' ");
        Object[] arg = new Object[]{};
        List<String> deviceList = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("device_code");
            }
        });

        if (deviceList == null || deviceList.size() == 0) {
            //原订单未添加系统配置
            return true;
        }

        //新订单套数情况
        for (int i = 0; i < deviceList.size(); i++) {
            String deviceCode = deviceList.get(i);
            Object o = mapCount.get(deviceCode);
            if (o == null) {
                mapCount.put(deviceCode, quantity.intValue());
            }else{
                mapCount.put(deviceCode, quantity.intValue() + StringUtil.StringToInt(StringUtil.null2String(o)));
            }
        }

        //已绑定设备情况
        sql = new StringBuffer();
        sql.append("SELECT (SELECT prod_typename FROM prod_type pt WHERE pt.id=tsi.prod_type ) AS device_code, COUNT(*) AS ct FROM t_sn_info tsi WHERE STATUS=1 AND orderno='")
               .append(orderNo).append("' GROUP BY prod_type");
        List<String[]> snList = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                String[] arr = new String[2];
                arr[0] = resultSet.getString("device_code");
                arr[1] = resultSet.getString("ct");
                return arr;
            }
        });

        if (snList == null || snList.size() == 0) {
            //订单未绑定任何设备
            return true;
        }

        /******************************************
         * 已绑定的设备大于新修改的设备时，不能修改
         *****************************************/
        for (int i = 0; i < snList.size(); i++) {
            String[] dbArr = snList.get(i);
            int dbCount = StringUtil.StringToInt(dbArr[1]);
            Integer newCount = mapCount.get(dbArr[0]);
            if (dbCount > newCount.intValue()){
                return false;
            }
        }

        return true;
    }

    @Override
    public int updateOrderBase(Order order, Long userId) {
        orderDao.save(order);

        List<String> sqlList = new ArrayList<String>();
        StringBuffer logSql = new StringBuffer();
        logSql.append("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(").append(userId).append(", 'ORDER', NOW(), '");
        logSql.append("修改订单基本信息>>");
        //添加操作日志记录
        logSql.append(StringUtil.toStringForDB(order).toString()).append("')");
        sqlList.add(logSql.toString());

        if(order.getOrderType() == 1){
            //判断套数情况，不能修改后的套数小于已绑定模块的套数
            Long qu = order.getQuantity();
            if(checkDeviceCount(order.getOrderno(), qu)){
                String sql = "UPDATE t_order_device SET device_count=" + qu.longValue() + " WHERE STATUS=1 AND orderno='" + order.getOrderno() + "'";
                sqlList.add(sql);
            }else{
                return -1; //订单套数不能小于已绑定设备数量
            }
        }
        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] sqlRs = jdbcTemplate.batchUpdate(sqlArr);

        return sqlRs.length;
    }

    @Override
    public int updateOrderNote(String orderNo, String orderNote, String beforeSoftware, Long userId) {
        List<String> sqlList = new ArrayList<String>();
        Order order = findInfoByOrderNo(orderNo);
        beforeSoftware = StringUtil.null2String(beforeSoftware).trim();
        /*******************************************************
         * 引用的硬件售后订单，已有设备绑定时不能修改前版程序
         * 硬件售后无设备绑定时，前版程序有变动时修改为新的程序
         ******************************************************/
        if (order.getOrderType() == 3 && order.getShAddType() == 1){
            String db_beforeSoftware = StringUtil.null2String(order.getBeforeSoftware()).trim();
            if (! beforeSoftware.equals(db_beforeSoftware)){
                /********************************************
                 * 已有设备绑定的订单，不能修改
                 *******************************************/
                int snCount = findSnCount4DelOrder(orderNo);
                if(snCount > 0){
                    return -1;
                }else{ //修改软件信息
                    sqlList.add("UPDATE t_order_file SET status=3,opt_user_id=" + userId + ",del_time=now() WHERE status=1 AND orderno='" + orderNo + "'");
                    StringBuffer sBuf = new StringBuffer();
                    sBuf.append("INSERT INTO t_order_file (batch_no, orderno, device_code, bmu_no, file_type, file_code, file_name, md5_code,start_time, STATUS, opt_user_id, opt_time) ");
                    sBuf.append("SELECT batch_no, '").append(orderNo).append("', IFNULL(device_code, ''), IFNULL(bmu_no, 0), IFNULL(file_type, ''), IFNULL(file_code, ''), IFNULL(file_name, ''), ");
                    sBuf.append("IFNULL(md5_code, ''), NOW(), STATUS, '").append(userId).append("', NOW() FROM t_order_file WHERE STATUS=1 AND orderno='").append(beforeSoftware).append("'");

                    sqlList.add(sBuf.toString());
                }
            }
        }

        StringBuffer sql = new StringBuffer();
        sql.append("update t_order set order_note='").append(StringUtil.null2String(orderNote).trim()).append("',before_software='")
                .append(beforeSoftware).append("' where status<99 and orderno='").append(orderNo).append("'");
        sqlList.add(sql.toString());

        StringBuffer logSql = new StringBuffer();
        logSql.append("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(").append(userId).append(", 'ORDER', NOW(), '");
        //添加操作日志记录
        logSql.append("下单原因修改》》订单编号：").append(orderNo).append("，前版程序:").append(beforeSoftware).append("，下单原因：").append(orderNote).append("')");
        sqlList.add(logSql.toString());

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }
        int[] sqlRs = jdbcTemplate.batchUpdate(sqlArr);
        return sqlRs.length;
    }

    @Override
    public int updateOrderDevice(String orderNo, String devices_code, String devices_class, String devices_bmu, String devices_count, Long userId) {
        Order order = findInfoByOrderNo(orderNo);
        List<String> sqlList = new ArrayList<String>();

        /*********************************************
         * 新订单和手动添加的软件售后订单
         * 检查软件上传情况,有软件上传的不可修改
         * 前台已屏蔽引用的软件售后订单不能修改系统配置
         *********************************************/
        if (order.getOrderType() == 1 || order.getOrderType() == 2){
            int swCount = findSoftware4Order(orderNo);
            if(swCount > 0){
                return -1;
            }
        }

        /********************************************
         * 已有设备绑定的订单，不能修改
         *******************************************/
        int snCount = findSnCount4DelOrder(orderNo);
        if(snCount > 0){
            return -2;
        }

        String[] device_codeArr = devices_code.split(",");
        String[] device_classArr = devices_class.split(",");
        String[] device_bmuArr = devices_bmu.split(",");
        String[] devices_countArr = devices_count.split(",");

        List<String> deviceList = new ArrayList<String>();
        for (int i = 0; i <device_codeArr.length; i++) {
            String deviceStr = StringUtil.null2String(device_codeArr[i]).trim();
            if (!"".equals(deviceStr) && !deviceList.contains(deviceStr) ){
                deviceList.add(deviceStr + "_" + device_classArr[i] + "_" + device_bmuArr[i]);
            }
        }
        List<String> dbList = findDeviceForDiff(orderNo);
        if (deviceList.size() > 0){
            sqlList.add("UPDATE t_order_device SET status=2,opt_user_id=" + userId + ",opt_time=now() WHERE status=1 AND orderno='" + orderNo + "'");
            //修改数量
            StringBuffer sqlInsert = null;
            for (int i = 0; i <deviceList.size(); i++) {
                sqlInsert = new StringBuffer();
                sqlInsert.append("insert into t_order_device (orderno, device_code, device_class, bmu_no, device_type, status, opt_user_id, opt_time, device_count) ");
                sqlInsert.append("values('").append(orderNo).append("','" ).append(device_codeArr[i]).append("','" ).append(device_classArr[i]).append("','" )
                        .append(device_bmuArr[i]).append("','0','1','").append(userId);
                sqlInsert.append("',now(), ");

                if(order.getOrderType() == 1){
                    sqlInsert.append(order.getQuantity()).append(")");
                }else{
                    String countStr = StringUtil.null2String(devices_countArr[i]).trim();
                    if("".equals(countStr)){
                        countStr = "0";
                    }
                    sqlInsert.append(countStr).append(")");
                }
                sqlList.add(sqlInsert.toString());
            }
            //比较两次设备型号是否一致
            if (dbList.containsAll(deviceList) && deviceList.containsAll(dbList)){

            }else{
                sqlList.add("UPDATE t_order_file SET status=3,opt_user_id=" + userId + ",del_time=now() WHERE status=1 AND orderno='" + orderNo + "'");
            }
        }else{
            sqlList.add("UPDATE t_order_device SET status=2,opt_user_id=" + userId + ",opt_time=now() WHERE status=1 AND orderno='" + orderNo + "'");
            sqlList.add("UPDATE t_order_file SET status=3,opt_user_id=" + userId + ",del_time=now() WHERE status=1 AND orderno='" + orderNo + "'");
        }
        //修改设备类型 由于添加 LCD 和其他 故限制为('BCU', 'BYU', 'BMU', 'LDM', 'DTU')五种类型
        sqlList.add("UPDATE t_order_device t_a SET t_a.device_type=(SELECT MAX(device_type) FROM t_sn_module WHERE m_type=t_a.device_code) WHERE t_a.device_type=0 and t_a.status=1 and t_a.device_class in ('BCU', 'BYU', 'BMU', 'LDM', 'DTU')");

        StringBuffer logSql = new StringBuffer();
        logSql.append("insert into t_operation_log(opt_userid, opt_type, opt_time, content) values(").append(userId).append(", 'ORDER', NOW(), '");
        logSql.append("修改订单系统配置>>设备型号：").append(devices_code).append(" 数量：").append(devices_count).append("')");
        sqlList.add(logSql.toString());

        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size() ; i++) {
            sqlArr[i] = sqlList.get(i);
        }

        int[] sqlRs = jdbcTemplate.batchUpdate(sqlArr);
        return sqlRs.length;
    }

    @Override
    public Map<String, String> findDeviceVersion(String orderno, String versiobType) {
        /**
         * SELECT device_code,GROUP_CONCAT(hw_version) AS device_version FROM (
         SELECT device_code,hw_version FROM t_order_product WHERE orderno='LG201512230001' AND STATUS<99 GROUP BY device_code, hw_version
         ) a GROUP BY device_code;
         */
        String sql = "SELECT tab_a.device_code,GROUP_CONCAT(tab_a." + versiobType + ") AS device_version FROM ( " +
                " SELECT device_code," + versiobType + " FROM t_order_product WHERE orderno='" + orderno + "' AND STATUS<99 GROUP BY device_code, " + versiobType +
                " ) tab_a GROUP BY tab_a.device_code";

        Object[] arg = new Object[]{};
        List<String[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                String[] arr = new String[2];
                arr[0] = resultSet.getString("device_code");
                arr[1] = resultSet.getString("device_version");
                return arr;
            }
        });

        if (list != null && list.size()>0) {
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < list.size() ; i++) {
                String[] arr = list.get(i);
                map.put(arr[0], arr[1]);
            }
            return map;
        }
        return null;
    }

    @Override
    public List<String> findOrder4OTA() {
        String sql = "SELECT orderno FROM c_order WHERE order_type=1 AND status<99 ORDER BY orderno DESC";
        Object[] arg = new Object[]{};
        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return StringUtil.null2String(resultSet.getString("orderno"));
            }
        });
        return list;
    }

    @Override
    public Map<String, Object> findCOrderByOrderNo(String orderNo) {
        String sql = "SELECT * FROM c_order WHERE orderno='" + orderNo + "' and status<99 ORDER BY orderno DESC";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list != null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public EasyUiUtil.PageForData findOrderList4OTA(int rows, int page, String sort, String order, String orderNo) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT id,`device_code`,`uuid`,`p_sn`,vehicle_number,`city`,`hw_version`,`sw_version`,`status`,hours,insert_time,uuid_show FROM( ")
                .append(" SELECT c.id,c.`device_code`,t.`uuid`,c.`p_sn`,c.vehicle_number,t.`city`,t.`hw_version`,t.`sw_version`,t.`status`, t.`insert_time`,c.uuid AS uuid_show, ")
                .append(" CASE WHEN t.status=3 THEN TIMESTAMPDIFF(HOUR, DATE_FORMAT(t.`insert_time`, '%Y-%m-%d %H:%i'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i')) ELSE 0 END AS hours FROM c_order_product c LEFT JOIN t_bms_info t ON c.`uuid_all`=t.`uuid` ")
                .append(" WHERE c.orderno IN (SELECT orderno FROM c_order WHERE (c_order.`bound_order`='").append(orderNo).append("' AND c_order.`order_type`=3) OR c_order.orderno='").append(orderNo).append("')")
                .append(" ) a ");

        StringBuffer whereSql = new StringBuffer(" WHERE a.insert_time IS NOT NULL ");
        whereSql.append(" AND a.uuid NOT IN (SELECT DISTINCT ttd.`uuid` FROM t_task_device ttd WHERE ttd.`status`<3 AND ttd.task_no IN (SELECT task_no FROM t_task_info WHERE orderno='").append(orderNo).append("'))");

        StringBuffer orderSql = new StringBuffer();
        if (!"".equals( StringUtil.null2String(sort).trim())){
            orderSql.append(" order by ").append(sort).append(" ").append(order);
        }else{// 默认排序为ID顺序
            orderSql.append(" order by hours, id desc");
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlBuf.append(whereSql).append(orderSql).toString());

        /*StringBuffer countSql = new StringBuffer();
        countSql.append("SELECT COUNT(*) AS ct FROM(SELECT t.`insert_time` FROM c_order_product c LEFT JOIN t_bms_info t ON c.`uuid_all`=t.`uuid` WHERE orderno='LG201608090302') a ").append(whereSql.toString());
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));*/

        EasyUiUtil.PageForData pageObj = new EasyUiUtil.PageForData();
        pageObj.setRows(list);
        //pageObj.setTotal(Long.parseLong(total));
        pageObj.setTotal(0L);

        return pageObj;
    }

    @Override
    public Map<String, String[]> findFileByOrderNo(String orderNo) {
        String sql = "SELECT file_type,file_code,file_name FROM c_order_file WHERE orderno=?";
        Object[] arg = new Object[]{orderNo};
        List<String[]> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                String[] arr = new String[3] ;
                arr[0] = StringUtil.null2String(resultSet.getString("file_type"));
                arr[1] = StringUtil.null2String(resultSet.getString("file_code"));
                arr[2] = StringUtil.null2String(resultSet.getString("file_name"));
                return arr;
            }
        });

        if (list != null && list.size() > 0){
            Map<String, String[]> map = new HashMap<String, String[]>();
            for (int i = 0; i < list.size(); i++) {
                String[] arr = list.get(i);
                if (!"".equals(arr[0])){
                    map.put(arr[0], new String[]{arr[1], arr[2]});
                }
            }
            if (map.size() > 0){
                return map;
            }
        }
        return null;
    }

    @Override
    public boolean checkBaseConfig(String checkStr, int checkTpe, boolean isCheckConfigName) {
        StringBuffer sqlBuf = new StringBuffer("SELECT COUNT(*) AS ct FROM t_base_config WHERE status=1");
        sqlBuf.append(" and config_type=").append(checkTpe);
        if (isCheckConfigName){
            sqlBuf.append(" and config_name='").append(checkStr).append("' ");
        }else {
            sqlBuf.append(" and config_value='").append(checkStr).append("' ");
        }

        Map<String, Object> mapCount = jdbcTemplate.queryForMap(sqlBuf.toString());
        int count = StringUtil.StringToInt(StringUtil.null2String(mapCount.get("ct")));
        if (count > 0){
            return true;
        }
        return false;
    }
}
