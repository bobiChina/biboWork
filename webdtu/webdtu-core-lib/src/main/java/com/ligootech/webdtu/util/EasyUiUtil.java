package com.ligootech.webdtu.util;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wly on 2015/12/8 16:12.
 */
public class EasyUiUtil {



    /**
     * 去除排序字样，方便查询总数
     * @param sql
     * @return
     */
    public static String removeOrders(String sql) {
        sql = sql.trim();
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }



    /**
     * LIMIT 筛选
     * @param sql 原始SQL语句
     * @param page 起始页码
     * @param rows 需要获取的行数
     * @return 数据库特定的定位行集SQL语句
     */
    public static String getPageSQL(String sql, int page, int rows) {
        if (page < 1){
            page = 1;
        }
        String pageSql = sql + " limit " + (page - 1)*rows + "," + rows;
        return pageSql;
    }

    /**
     * 获取总数的SQL脚本，不适用于嵌套结果集的查询,以及限制条件中有其他表查询的情况
     * @param sql
     * @return
     */
    public static String getCountSql(String sql) {

        String regex = "(^select)(.*)(from.*)";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        boolean rs = m.find();
        if (rs == false || m.groupCount() != 3) {
            return "";
        } else {
            //return m.group(1) + " count(*) as ct "+sql.substring(sql.lastIndexOf("from"));
            return m.group(1) + " count(*) as ct " + m.group(3);
        }

    }

    public static void main(String[] args) {
        //String sql = "SELECT (SELECT * from tableName) id,(select id from t) a, name from (select * from dtu) as ddd where 1=1 and a=b order by a desc";
        String sql = "SELECT (SELECT * from tableName) id,(select id from t) a, name from tableName where 1=1 and a=(select max(id) from dtu where dtuid=1) order by a desc";
        System.out.println(sql);
        System.out.println(removeOrders(getCountSql(sql)));
    }

    /**
     * 封装成easyui用到的对象
     */
    public static class PageForData implements Serializable{
        private static final long serialVersionUID = -1373760761710848811L;
        private Long total;
        private List<Map<String, Object>> rows;

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public List<Map<String, Object>> getRows() {
            return rows;
        }

        public void setRows(List<Map<String, Object>> rows) {
            this.rows = rows;
        }
    }






















    /**
     * 封装成map数组
     * @param con
     * @param sqlBuffer
     * @param objects
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getListMapStrStr(Connection con, String sqlBuffer, Object[] objects) throws Exception{
        List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = con.prepareStatement(sqlBuffer);
        if(objects!=null && objects.length>0){
            int i=1;
            for (Object object : objects) {
                if(object == null){
                    ps.setNull(i++, Types.INTEGER);
                }else{
                    ps.setObject(i++, object);
                }
            }
        }
        rs = ps.executeQuery();

        ResultSetMetaData rsmd = rs.getMetaData();
        int colCnt = rsmd.getColumnCount();
        while(rs.next()){
            Map<String, String> map = new Hashtable<String, String>();
            for(int col = 1; col <= colCnt; col++){
                String value = rs.getString(col);
                if(value != null){
                    map.put(rsmd.getColumnName(col).toLowerCase(), value);
                }
            }
            retList.add(map);
        }
        return retList;
    }















    /**
     * 翻页数据组装
     * @param sqlCode
     * @param start
     * @param limit
     * @return
     */
    public static String getPageSql(String sqlCode,int start,int limit)
    {
        if(start < 1){
            start = 1;
        }
        if(limit <= 0){
            limit = 10;
        }
        sqlCode  =
                "select * from ( \n"+
                        "     select row_.*, rownum rownum_ from ( \n"+
                        "		"+sqlCode+ "\n"+
                        "     ) row_ where rownum <= '"+(start * limit)+"' \n"+
                        ") where rownum_ >  '"+ ((start-1) * limit) +"' \n";

        return sqlCode;
    }
    /**
     * 向前台输送列表数据(分页)
     * @param response
     * @param count
     * @param list
     * @throws Exception
     * @author WLY
     * @日期 2014-3-25 下午4:06:17
     */
    public static void sendListData(HttpServletResponse response, String count,List<?> list) throws Exception {

        Map<String, Object> jsonObj = new HashMap<String, Object>();
        jsonObj.put("rows", list);
        jsonObj.put("total", count);
        response.setContentType("application/x-json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        /*String jsonStr = JSONObject.fromObject(jsonObj).toString();
        out.print(jsonStr);
        out.close();*/
    }
    /**
     * 向前台输送列表数据(不分页)
     * @param response
     * @param list
     * @throws Exception
     * @author WLY
     * @日期 2015-4-25 下午9:09:59
     */
    public static void sendListData(HttpServletResponse response, List<?> list ) throws Exception{
        if(null == list || list.size() == 0){
            response.setContentType("application/x-json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("[]");
            out.close();
        }else{
          /*  String jsonTreeStr = JSONArray.fromObject(list).toString();
            response.setContentType("application/x-json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonTreeStr);
            out.close();*/
        }
    }
    /**
     * 向前台输信息
     * @param response
     * @param msg
     * @throws Exception
     * @author WLY
     * @日期 2015-4-25 下午9:11:00
     */
    public static void sendMsgData(HttpServletResponse response, String msg ) throws Exception{
        response.setContentType("application/x-json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(msg);
        out.close();
    }
}
