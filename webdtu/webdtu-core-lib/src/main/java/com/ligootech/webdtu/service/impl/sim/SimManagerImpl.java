package com.ligootech.webdtu.service.impl.sim;

import com.ligootech.webdtu.service.account.ShiroUser;
import com.ligootech.webdtu.service.sim.SimManager;
import com.ligootech.webdtu.util.EasyUiUtil;
import com.ligootech.webdtu.util.ExcelReaderUtil;
import com.ligootech.webdtu.util.FileOperateUtil;
import com.ligootech.webdtu.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wly on 2015/12/21 16:12.
 */
@Service("simManager")
public class SimManagerImpl implements SimManager {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public EasyUiUtil.PageForData findSIMList(int rows, int page, String sort, String order, String keywords) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id,ccid,sim,opt_user_name AS optname, status, DATE_FORMAT(opt_time, '%Y-%m-%d %H:%i:%s') AS opttime, '' AS sn FROM t_dtu_sim ");

        StringBuffer whereSql = new StringBuffer(" where status=1 ");
        if (!"".equals( StringUtil.null2String(keywords).trim())){
            whereSql.append(" and ccid like '%").append(keywords.trim()).append("%'").append(" or sim like '%").append(keywords.trim()).append("%' ");
        }

        StringBuffer orderSql = new StringBuffer();
        if (!"".equals( StringUtil.null2String(sort).trim())){
            orderSql.append(" order by ").append(sort).append(" ").append(order);
        }else{// 默认排序为状态放后面，时间放前面，方便查询
            orderSql.append(" order by ").append(sort).append(" opt_time desc ");
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList(EasyUiUtil.getPageSQL(sql.append(whereSql).append(orderSql).toString(), page, rows));

        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) as ct from t_dtu_sim ").append(whereSql.toString());
        Map<String, Object> mapCount = jdbcTemplate.queryForMap(countSql.toString());
        String total = StringUtil.null2String(mapCount.get("ct"));

        EasyUiUtil.PageForData pageObj = new EasyUiUtil.PageForData();
        pageObj.setRows(list);
        pageObj.setTotal(Long.parseLong(total));

        return pageObj;
    }

    @Override
    public int delSimCard(String ids, ShiroUser optUser) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE t_dtu_sim SET status=2,opt_user_id=").append(optUser.getId())
                .append(",opt_user_name='").append(optUser.getName()).append("',opt_time=now() where id in ");
        sql.append(StringUtil.getStrsplit(ids.split(",")));

        String[] sqlArr = {sql.toString()};

        int[] rs = jdbcTemplate.batchUpdate(sqlArr);

        return rs[0];
    }

    @Override
    public int saveSimCard(String fileName, ShiroUser optUser) throws Exception{
        //读取文件
        List<List<String>> rsList = ExcelReaderUtil.readExcel(FileOperateUtil.UPLOADDIR + fileName );
        ArrayList<String> sqlList = new ArrayList<String>();
        // INSERT INTO t_dtu_sim(ccid,sim,STATUS,opt_user_id,opt_user_name,opt_time)
        // VALUES(CONCAT('CCID', num),CONCAT('SIM',num),1, 4014, 'demo', NOW());
        if (rsList.size()>0){
            ArrayList<String> ccidList = new ArrayList<String>();
            String simCode = "";
            String ccidCode = "";
            for (int i = 0; i < rsList.size(); i++) {
                List<String> list = rsList.get(i);
                if (list != null && list.size() >1) {
                    ccidCode = StringUtil.null2String(list.get(0)).trim();
                    if (!"".equals(ccidCode)){
                        simCode = StringUtil.null2String(list.get(1)).trim();
                        ccidList.add(ccidCode);
                        sqlList.add("INSERT INTO t_dtu_sim(ccid,sim,STATUS,opt_user_id,opt_user_name,opt_time) VALUES('" + ccidCode + "','" + simCode + "',1, " + optUser.getId() + ", '" + optUser.getName() + "', NOW())");
                    }
                }
            }
            if (sqlList.size() > 0){
                //替换第一行, 状态修改范围限制为只能修改正常状态的数据
                StringBuffer sql = new StringBuffer();
                sql.append("UPDATE t_dtu_sim SET status=2,opt_user_id=").append(optUser.getId())
                        .append(",opt_user_name='").append(optUser.getName()).append("',opt_time=now() where status=1 and ccid in ");
                sql.append(StringUtil.getStrsplit(ccidList));
                sqlList.set(0, sql.toString());

                String[] sqlArr = new String[sqlList.size()];
                for (int i = 0; i < sqlList.size(); i++) {
                    sqlArr[i] = sqlList.get(i);
                }

                int[] rs = jdbcTemplate.batchUpdate(sqlArr);

                rsList.clear();
                sqlList.clear();
                ccidList.clear();
                sqlArr = null;

                return rs.length -1;
            }
        }

        return 0;
    }

}
