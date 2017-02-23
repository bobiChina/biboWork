package com.ligootech.webdtu.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/8/8.
 */
public class SNCodeUtil {
    public static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat DATEFORMAT_MINI = new SimpleDateFormat("yyyyMMdd");
    public static Map<String, String> yearMap;
    public static Map<String, String> monthMap;
    public static Map<String, String> dayMap;
    public static Map<String, String> supplierMap;//供应商代码
    public static Map<String, Integer> deviceMap = new HashMap<String, Integer>();//设备型号计数器  键值对格式为  键：产品型号_年月日 值：从0开始

    static{
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date yesterdayDate = cal.getTime();
        String yesterdayStr = DATEFORMAT_MINI.format(yesterdayDate);
        deviceMap.put("11C_" + yesterdayStr, 103);
        deviceMap.put("104_" + yesterdayStr, 104);
        deviceMap.put("105_" + yesterdayStr, 105);
        deviceMap.put("106_" + yesterdayStr, 106);


        yearMap = new HashMap<String, String>();
        yearMap.put("2015", "5");
        yearMap.put("2016", "6");
        yearMap.put("2017", "7");
        yearMap.put("2018", "8");
        yearMap.put("2019", "9");
        yearMap.put("2020", "A");
        yearMap.put("2021", "B");
        yearMap.put("2022", "C");
        yearMap.put("2023", "D");
        yearMap.put("2024", "E");
        yearMap.put("2025", "F");

        monthMap = new HashMap<String, String>();
        monthMap.put("01", "1");
        monthMap.put("02", "2");
        monthMap.put("03", "3");
        monthMap.put("04", "4");
        monthMap.put("05", "5");
        monthMap.put("06", "6");
        monthMap.put("07", "7");
        monthMap.put("08", "8");
        monthMap.put("09", "9");
        monthMap.put("10", "A");
        monthMap.put("11", "B");
        monthMap.put("12", "C");

        dayMap = new HashMap<String, String>();
        dayMap.put("01", "1");
        dayMap.put("02", "2");
        dayMap.put("03", "3");
        dayMap.put("04", "4");
        dayMap.put("05", "5");
        dayMap.put("06", "6");
        dayMap.put("07", "7");
        dayMap.put("08", "8");
        dayMap.put("09", "9");
        dayMap.put("10", "A");
        dayMap.put("11", "B");
        dayMap.put("12", "C");
        dayMap.put("13", "D");
        dayMap.put("14", "E");
        dayMap.put("15", "F");
        dayMap.put("16", "G");
        dayMap.put("17", "H");
        dayMap.put("18", "I");
        dayMap.put("19", "J");
        dayMap.put("20", "K");
        dayMap.put("21", "L");
        dayMap.put("22", "M");
        dayMap.put("23", "N");
        dayMap.put("24", "O");
        dayMap.put("25", "P");
        dayMap.put("26", "Q");
        dayMap.put("27", "R");
        dayMap.put("28", "S");
        dayMap.put("29", "T");
        dayMap.put("30", "U");
        dayMap.put("31", "V");
    }

    /**
     * 格式化流水号，4个16位码，按顺序编号。起始号码是“0000“，延续至“FFFF”，总数65536。该字段在一天内不能重复
     * @param num
     * @return
     */
    public static String toHex(int num){
        num = num + 1;
        return StringUtil.completionStr(Integer.toHexString(num), 4).toUpperCase();
    }

    /**
     * 清除过时的设备型号标识
     * @return
     */
    public static void removeOutdated(){

        String todayStr = DATEFORMAT_MINI.format(new Date());
        Iterator it = deviceMap.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next().toString();
            if ( key.indexOf(todayStr) < 0){
                it.remove();
            }
        }
    }

    /**
     * 获取产品SN编码
     * @param num
     * @param productCode
     * @return
     */
    public static String getSerialNumber(int num, String productCode){

        if (num == 0){
            removeOutdated();
        }

        String date = DATEFORMAT_MINI.format(new Date());
        // yyyy-MM-dd
        StringBuffer rsBuff = new StringBuffer();
        //产品型号编码
        rsBuff.append(productCode);
        //年月日
        rsBuff.append(yearMap.get(date.substring(0, 4))).append(monthMap.get(date.substring(4, 6))).append(dayMap.get(date.substring(6)));
        //供应商 力高
        rsBuff.append("1");
        //流水号
        String bh = toHex(num);
        //校验码
        char checkCode = MD5Util.getLigooCRC(rsBuff.toString() + bh);
        rsBuff.append(checkCode).append(bh);

        return rsBuff.toString().toUpperCase();
    }

    public static void main(String[] args){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String str = DATEFORMAT_MINI.format(cal.getTime());
        System.out.println(str);

        String deviceMapKey = "21C" + "_" + SNCodeUtil.DATEFORMAT_MINI.format(new Date());
        String p_code = "21C";
        String str2 = String.format("%s_%s", p_code, SNCodeUtil.DATEFORMAT_MINI.format(new Date()));

        System.out.println(str2);


    }
}
