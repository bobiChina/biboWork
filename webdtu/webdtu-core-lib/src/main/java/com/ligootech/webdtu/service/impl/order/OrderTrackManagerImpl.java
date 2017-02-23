package com.ligootech.webdtu.service.impl.order;

import com.ligootech.webdtu.entity.core.OrderTrack;
import com.ligootech.webdtu.repository.OrderTrackDao;
import com.ligootech.webdtu.service.order.OrderTrackManager;
import com.ligootech.webdtu.util.EasyUiUtil;
import com.ligootech.webdtu.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/12.
 */
@Service("orderTrackManager")
public class OrderTrackManagerImpl implements OrderTrackManager {
    @Autowired
    private OrderTrackDao orderTrackDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 获取订单跟踪日志
     * @param orderNo
     * @return
     */
    @Override
    public List<OrderTrack> findTrackByOrderNo(String orderNo) {
        return orderTrackDao.findOrderTrackByOrderNo(orderNo);
    }

    /**
     * 保存跟踪日志
     * @param orderTrack
     * @return
     */
    @Override
    public Long saveOrderTrack(OrderTrack orderTrack) {
        try {
            OrderTrack orderTrackDB = findNewTrackByOrderNo(orderTrack.getOrderno());

            List<String> sqlList = new ArrayList<String>();
            if (orderTrackDB != null) {
                //修改其他日志为旧日志
                sqlList.add("UPDATE t_order_track SET is_new=0 WHERE id=" + orderTrackDB.getId());
            }

            if (orderTrack.getOrderStatus() == 11){//8- 齐套上传 11-非齐套上传
                //非齐套上传时不更新现有状态
            }else{
                //更新订单状态
                sqlList.add("UPDATE t_order SET status=" + orderTrack.getOrderStatus() + " WHERE orderno='" + orderTrack.getOrderno() + "' and status<99");
            }

            String[] sqlArr = new String[sqlList.size()];
            for (int i = 0; i < sqlList.size(); i++) {
                sqlArr[i] = sqlList.get(i);
            }

            if (sqlArr.length > 0){
                int[] rs = jdbcTemplate.batchUpdate(sqlArr);
            }

            orderTrackDao.save(orderTrack);//事务不能及时提交(开发机器卡造成)

            return orderTrack.getId();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 订单列表信息
     * @param rows
     * @param page
     * @param sort
     * @param order
     * @param orderNo @return
     */
    @Override
    public EasyUiUtil.PageForData findEasyUiList(int rows, int page, String sort, String order, String orderNo) {
        /**************************************************************************************************************************************************
         * 原始SQL脚本
         * SELECT id,orderno,action_name,
         CASE
         WHEN order_status=1 THEN '评审中'
         WHEN order_status=2 THEN '待接收'
         WHEN order_status=3 THEN '待开发'
         WHEN order_status=4 THEN '开发中'
         WHEN order_status=5 THEN '待测试'
         WHEN order_status=6 THEN '测试中'
         WHEN order_status=7 THEN '待上传'
         WHEN order_status=8 THEN '已齐套'
         WHEN order_status=9 THEN '暂停'
         WHEN order_status=10 THEN '取消'
         WHEN order_status=11 THEN '非齐套'
         END AS order_status,
         action_man,CONCAT(DATE_FORMAT(tot.action_date, '%Y-%m-%d'), ' ', am_pm) AS action_date,
         remarks,
         CASE
         WHEN tot.review_delivery IS NULL THEN '-'
         WHEN tot.order_status=8 THEN '-'
         WHEN tot.order_status=9 THEN '-'
         ELSE DATE_FORMAT(tot.review_delivery, '%Y-%m-%d')
         END AS review_delivery,
         DATE_FORMAT(tot.develop_delivery, '%Y-%m-%d') AS develop_delivery,
         DATE_FORMAT(tot.test_delivery, '%Y-%m-%d') AS test_delivery,(SELECT full_name FROM dtu_user du WHERE du.id=tot.opt_user_id) AS user_name,
         DATE_FORMAT(tot.opt_time, '%Y-%m-%d %H:%i') AS opttime, is_new
         FROM t_order_track tot WHERE tot.status=1 ORDER BY id DESC;
         **************************************************************************************************************************************************/

        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT id,orderno,action_name, ")
                .append(" CASE  ")
                .append(" WHEN order_status=1 THEN '评审中' ")
                .append(" WHEN order_status=2 THEN '待接收'  ")
                .append(" WHEN order_status=3 THEN '待开发' ")
                .append(" WHEN order_status=4 THEN '开发中' ")
                .append(" WHEN order_status=5 THEN '待测试' ")
                .append(" WHEN order_status=6 THEN '测试中' ")
                .append(" WHEN order_status=7 THEN '待上传' ")
                .append(" WHEN order_status=8 THEN '已齐套' ")
                .append(" WHEN order_status=9 THEN '暂停' ")
                .append(" WHEN order_status=10 THEN '取消' ")
                .append(" WHEN order_status=11 THEN '非齐套' ")
                .append(" END AS order_status, ")
                //.append(" action_man,CONCAT(DATE_FORMAT(tot.action_date, '%Y-%m-%d'), ' ', am_pm) AS action_date, ") 事件日期改为录入日期 sby 2016年5月27日 17:09:59
                .append(" action_man,DATE_FORMAT(tot.opt_time, '%Y-%m-%d %H:%i:%s') AS action_date, ")
                .append(" CASE  ")
                .append(" WHEN order_status=2 THEN concat(remarks, ' 开发工时',review_develop_duration,'H',' 测试工时',review_test_duration, 'H')  ")
                .append(" WHEN order_status=5 THEN concat(remarks, ' 实际开发工时',develop_duration,'H') ")
                .append(" WHEN order_status=7 THEN concat(remarks, ' 实际测试工时',test_duration,'H') ")
                .append(" else remarks ")
                .append(" END AS remarks, ")
                .append("  CASE " +
                        "         WHEN tot.review_delivery IS NULL THEN '-' " +
                        "         WHEN tot.order_status=8 THEN '-' " +
                        "         WHEN tot.order_status=9 THEN '-' " +
                        "         ELSE DATE_FORMAT(tot.review_delivery, '%Y-%m-%d') " +
                        "         END AS review_delivery, DATE_FORMAT(tot.develop_delivery, '%Y-%m-%d') AS develop_delivery,  ")
                .append("  DATE_FORMAT(tot.test_delivery, '%Y-%m-%d') AS test_delivery,(SELECT full_name FROM dtu_user du WHERE du.id=tot.opt_user_id) AS user_name, ")
                .append("  DATE_FORMAT(tot.opt_time, '%Y-%m-%d %H:%i') AS opttime, is_new  ")
                .append("    FROM t_order_track tot ");

        StringBuffer whereSql = new StringBuffer(" where tot.status=1 AND tot.orderno='" + orderNo + "' ");

        StringBuffer orderSql = new StringBuffer();
        if (!"".equals( StringUtil.null2String(sort).trim())){
            orderSql.append(" order by ").append(sort).append(" ").append(order);
        }else{// 默认排序为时间倒序，方便查询
            orderSql.append(" order by ").append(sort).append(" tot.id desc ");
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(EasyUiUtil.getPageSQL(sqlBuf.append(whereSql).append(orderSql).toString(), page, rows));

        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct from t_order_track tot ").append(whereSql.toString());
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));

        EasyUiUtil.PageForData pageObj = new EasyUiUtil.PageForData();
        pageObj.setRows(list);
        pageObj.setTotal(Long.parseLong(total));

        return pageObj;
    }

    /**
     * 订单日志详细信息
     * @param id
     * @return
     */
    @Override
    public OrderTrack findObjById(Long id) {
        return orderTrackDao.findOne(id);
    }

    /**
     * 通过订单获取最新订单日志
     * @param orderNo
     * @return
     */
    @Override
    public OrderTrack findNewTrackByOrderNo(String orderNo) {
        return orderTrackDao.findNewTrackByOrderNo(orderNo);
    }

    /**
     * 删除
     * @param orderNo
     * @param id
     * @return
     */
    @Override
    public int delOrderTrackById(String orderNo, Long id) {
        Map<String, Object> map = jdbcTemplate.queryForMap("select MAX(id) AS max_id from t_order_track where status=1 and orderno='" + orderNo + "' and is_new=0");
        Map<String, Object> mapStrtus = jdbcTemplate.queryForMap("select order_status from t_order_track where status=1 and orderno='" + orderNo + "' and is_new=0 AND order_status <>11 ORDER BY id DESC LIMIT 1");

        List<String> sqlList = new ArrayList<String>();
        sqlList.add("update t_order_track set status=2,opt_time=now() where id=" + id);

        //如果有上条记录则修改为最新
        if (map != null && map.get("max_id") != null) {
            sqlList.add("update t_order_track set is_new=1 where id=" + map.get("max_id"));
            sqlList.add("update t_order set status='" + mapStrtus.get("order_status") + "' where orderno='" + orderNo + "' and status < 99");
        }else{
            sqlList.add("update t_order set status=0 where orderno='" + orderNo + "' and status < 99");
        }
        String[] sqlArr = StringUtil.getArrBylist(sqlList);
        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs[0];
    }

    /**
     * 获取公司名称
     * @return
     */
    @Override
    public List<Object[]> findOrderCorp() {
        String sql = "SELECT id,corp_name FROM corp WHERE id IN (SELECT DISTINCT corp_id FROM t_order WHERE STATUS<99)";
        List<Object[]> list = jdbcTemplate.query(sql, new Object[]{}, new RowMapper(){
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] obj = new Object[2];
                obj[0] = StringUtil.null2String(resultSet.getString("id"));
                obj[1] = StringUtil.null2String(resultSet.getString("corp_name"));
                return obj;
            }
        });

        return list;
    }

    /**
     * 获取订单销售代表
     * @return
     */
    @Override
    public List<Object[]> findOrderSalesman() {
        String sql = "SELECT id,full_name FROM dtu_user WHERE id IN (SELECT DISTINCT salesman FROM t_order WHERE STATUS<99)";
        List<Object[]> list = jdbcTemplate.query(sql, new Object[]{}, new RowMapper(){
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Object[] obj = new Object[2];
                obj[0] = StringUtil.null2String(resultSet.getString("id"));
                obj[1] = StringUtil.null2String(resultSet.getString("full_name"));
                return obj;
            }
        });

        return list;
    }

    /**
     * 获取订单所有日志信息
     *
     * @param orderNo
     * @return
     */
    @Override
    public List<Map<String, Object>> findOrderTrackByOrderNo(String orderNo) {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT id,orderno,action_name, ")
                .append(" CASE  ")
                .append(" WHEN order_status=1 THEN '评审中' ")
                .append(" WHEN order_status=2 THEN '待接收'  ")
                .append(" WHEN order_status=3 THEN '待开发' ")
                .append(" WHEN order_status=4 THEN '开发中' ")
                .append(" WHEN order_status=5 THEN '待测试' ")
                .append(" WHEN order_status=6 THEN '测试中' ")
                .append(" WHEN order_status=7 THEN '待上传' ")
                .append(" WHEN order_status=8 THEN '已齐套' ")
                .append(" WHEN order_status=9 THEN '暂停' ")
                .append(" WHEN order_status=10 THEN '取消' ")
                .append(" WHEN order_status=11 THEN '非齐套' ")
                .append(" END AS order_status, ")
                //.append(" action_man,CONCAT(DATE_FORMAT(tot.action_date, '%Y-%m-%d'), ' ', am_pm) AS action_date, ") 事件日期改为录入日期 sby 2016年5月27日 17:12:13
                .append(" action_man,DATE_FORMAT(opt_time, '%Y-%m-%d %H:%i:%s') AS action_date, ")
                .append(" CASE  ")
                .append(" WHEN order_status=2 THEN concat(remarks, ' 开发工时',review_develop_duration,'H',' 测试工时',review_test_duration, 'H')  ")
                .append(" WHEN order_status=5 THEN concat(remarks, ' 实际开发工时',develop_duration,'H') ")
                .append(" WHEN order_status=7 THEN concat(remarks, ' 实际测试工时',test_duration,'H') ")
                .append(" ELSE remarks ")
                .append(" END AS remarks, ")
                .append("  CASE " +
                        "         WHEN tot.review_delivery IS NULL THEN '-' " +
                        "         WHEN tot.order_status=8 THEN '-' " +
                        "         WHEN tot.order_status=9 THEN '-' " +
                        "         ELSE DATE_FORMAT(tot.review_delivery, '%Y-%m-%d') " +
                        "         END AS review_delivery, DATE_FORMAT(tot.develop_delivery, '%Y-%m-%d') AS develop_delivery ")
                //.append("  DATE_FORMAT(tot.test_delivery, '%Y-%m-%d') AS test_delivery,(SELECT full_name FROM dtu_user du WHERE du.id=tot.opt_user_id) AS user_name, ")
                //.append("  DATE_FORMAT(tot.opt_time, '%Y-%m-%d %H:%i') AS opttime, is_new  ")
                .append("    FROM t_order_track tot ");

        sqlBuf.append(" where tot.status=1 AND tot.orderno='" + orderNo + "' ").append(" order by tot.id asc ");

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlBuf.toString());
        return list;
    }


}
