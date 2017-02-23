package com.ligootech.webdtu.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/3.
 */
public class S19ToBin_L {
    public static final String CODING_REGION_C = "C";//编码区 C区间范围 [C0-CF, E0-EF]
    public static final String CODING_REGION_D = "D";//编码区 D区间范围 [D0-DF, E8-EF]
    public static String S19_LOG_PATH = "f:\\2s19log.log";
    private static StringBuilder jsonDataBuilder = new StringBuilder();
    public static void main(String[] args) {
        /*if (args.length != 2) {
            System.out.println("用法：java S19ToBin [M|S] 输入文件路径");
            System.out.println("M - 主机固件；S - 从机固件。");
            return;
        }*/
        //S19_LOG_PATH = args[1].substring(0, args[1].lastIndexOf(".")) + ".log";
        System.out.println("转换开始 ");
        //operateS19File(args[1], args[0].equalsIgnoreCase("M"));
        //operateS19File("D:/qingping/BC52B.abs(3).s19");

        operateS19File("f:/BC52B.A.abs(1).s19", true);
    }

    /**
     * 检查S2记录页地址区间范围
     * @param checkStr
     * @param checkType
     * @return
     */
    public static boolean checkRegion(String checkStr, String checkType ){
        /************************************
         * 编码区 C区间范围 [C0-CF, E0-EF]
         * 编码区 D区间范围 [D0-DF, E8-EF]
         * C0---192 CF---207  E0---224   E7---231
         * D0---208 DF---223  E8---232   EF---239
         ************************************/

        int checkNum = Integer.valueOf(checkStr, 16).intValue();
        if (CODING_REGION_C.equalsIgnoreCase(checkType)){
            if (checkNum == 192 || checkNum == 207 || checkNum == 224 || checkNum == 231 ){
                return true;
            }else if (checkNum > 192 && checkNum < 207){
                return true;
            }else if (checkNum > 224 && checkNum < 231){
                return true;
            }
        }else if (CODING_REGION_D.equalsIgnoreCase(checkType)){
            if (checkNum == 208 || checkNum == 223 || checkNum == 232 || checkNum == 239 ){
                return true;
            }else if (checkNum > 208 && checkNum < 223){
                return true;
            }else if (checkNum > 232 && checkNum < 239){
                return true;
            }
        }
        return false;
    }

    /**
     * 读写S19文件
     * @param inFilePath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void operateS19File(String inFilePath, boolean isBcu) {
        BufferedReader br = null;
        BufferedOutputStream bo_json = null;

        try {
            bo_json = new BufferedOutputStream(new FileOutputStream(inFilePath.substring(0, inFilePath.lastIndexOf(".")) + ".json", false));
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFilePath))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String lineStr;
        long existingNum = 0l;
        StringBuffer outBuff = new StringBuffer();
        String checkType = null;


        try {
            bo_json.write("[".getBytes());
            while((lineStr=br.readLine()) != null) {
                Map<String, Object> map = translationS19(lineStr, existingNum, checkType, bo_json, isBcu);
                if (map == null) {

                }else{
                    outBuff.append(null2String(map.get("data_str")));
                    existingNum = StringToLong(null2String(map.get("existing_num")));
                    checkType = null2String(map.get("coding_region"));
                }
            }

            String dataStr = jsonDataBuilder.toString();
            int len = dataStr.length();
            //bo.write(("2.dataStr = " + dataStr +  " len = " + len + "\n").getBytes());
            if (len % 16 != 0) {
                int fill = (len / 16 + 1) * 16 - len;
                for (int i = 0; i < fill; i++) {
                    dataStr = dataStr + "F";
                }
            }

            bo_json.write((", \"data\":\"" + dataStr + "\"}]").getBytes());
            jsonDataBuilder.setLength(0);

            bo_json.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CodeErrorException e) {
            e.printStackTrace();
        } catch (FileCodeErrorException e) {
            e.printStackTrace();
        }finally {
            /**
             * 释放流
             */
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /********************
         * 写出文件
         *******************/
        BufferedOutputStream bo = null;
        BufferedOutputStream bo_1 = null;

        try {
            bo = new BufferedOutputStream(new FileOutputStream(inFilePath.substring(0, inFilePath.lastIndexOf(".")) + ".bin", false));
            bo_1 = new BufferedOutputStream(new FileOutputStream(inFilePath.substring(0, inFilePath.lastIndexOf(".")) + ".txt", false));

            bo.write(hexStringToByte(outBuff.toString()));
            bo_1.write(outBuff.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bo.flush();
                bo.close();

                bo_1.flush();
                bo_1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 写入器
        /*FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            fos = new FileOutputStream(inFilePath.substring(0, inFilePath.lastIndexOf(".")) + ".bin", false);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(hexStringToByte(outBuff.toString()));
            oos.flush();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            oos.close();
            fos.close();
        }*/
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 解析S19行数据
     * @param lineStr
     * @param existingNum
     * @param s2_ppage
     * @return
     * @throws IOException
     * @throws CodeErrorException
     * @throws FileCodeErrorException
     */
    public static Map<String, Object> translationS19(String lineStr, long existingNum, String s2_ppage, OutputStream jsonOutputStream, boolean isBcu) throws IOException, CodeErrorException, FileCodeErrorException {
        int lineLength = lineStr.length();
        Map<String, Object> map = new HashMap<String, Object>();

        BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(S19_LOG_PATH, true));

        bo.write(("原始扫描数据：" + lineStr + "\n").getBytes());
        bo.flush();
        bo.close();

        /**
         * ------------------------------------------------------
         *  S	Type	ByteCount	Address     Data	Checksum
         * ------------------------------------------------------
         * S1	Data	16-bit Address
         * Yes	This record contains data that starts at the 16-bit address field.
         * This record is typically used for 8-bit microcontrollers, such as AVR, PIC, 8051, 68xx, 6502, 80xx, Z80.
         * The number of bytes of data contained in this record is "Byte Count Field" minus 3, which is 2 bytes for "16-bit Address Field" and 1 byte for "Checksum Field".

         * S2	Data	24-bit Address
         * Yes	This record contains data that starts at a 24-bit address.
         * The number of bytes of data contained in this record is "Byte Count Field" minus 4, which is 3 bytes for "24-bit Address Field" and 1 byte for "Checksum Field".

         * S3	Data	32-bit Address
         * Yes	This record contains data that starts at a 32-bit address.
         * This record is typically used for 32-bit microcontrollers, such as ARM and 680x0.
         * The number of bytes of data contained in this record is "Byte Count Field" minus 5, which is 4 bytes for "32-bit Address Field" and 1 byte for "Checksum Field".
         */
        String typeStr = lineStr.substring(0, 2);
        String countStr = lineStr.substring(2, 4);
        String addressStr = "0" ;
        String dateStr = "" ;
        String checksumStr ;

        long count = Integer.valueOf(countStr, 16).longValue();
        /*********************************
         * 只存在S0 S2 类型的文件数据 S9
         * xqp 2016年5月11日 10:27:34
         ********************************/
        if ("S0".equalsIgnoreCase(typeStr) || "S2".equalsIgnoreCase(typeStr) || "S9".equalsIgnoreCase(typeStr) || isBcu) {
            if("S2".equalsIgnoreCase(typeStr)){
                addressStr = lineStr.substring(4, 10);
                dateStr = lineStr.substring(10, lineLength-2);
                count = count - 4l;
            }else{
                return null;
            }
        } else if ("S1".equalsIgnoreCase(typeStr) || !isBcu) {
            if("S1".equalsIgnoreCase(typeStr)){
                addressStr = lineStr.substring(4, 8);
                dateStr = lineStr.substring(8, lineLength-2);
                count = count - 4l;
            }else{
                return null;
            }
        } else {
            bo = new BufferedOutputStream(new FileOutputStream(S19_LOG_PATH, true));
            bo.write(("文件中存在不被允许的数据类型：" + typeStr + "\n").getBytes());
            bo.flush();
            bo.close();
            throw new FileCodeErrorException("文件中存在不被允许的数据类型：" + typeStr);
        }

        /*if ("S1".equalsIgnoreCase(typeStr)){
            addressStr = lineStr.substring(4, 8);
            dateStr = lineStr.substring(8, lineLength-2);
            count = count - 3l;
        }else if("S2".equalsIgnoreCase(typeStr)){
            addressStr = lineStr.substring(4, 10);
            dateStr = lineStr.substring(10, lineLength-2);
            count = count - 4l;
        }else if("S3".equalsIgnoreCase(typeStr)){
            addressStr = lineStr.substring(4, 12);
            dateStr = lineStr.substring(12, lineLength-2);
            count = count - 5l;
        }else{
            return null;
        }*/

        bo = new BufferedOutputStream(new FileOutputStream(S19_LOG_PATH, true));
        /*******************************
         * 初始化S2页地址校验类型
         ******************************/
        if (existingNum == 0l){
            //logger.debug("起始位置未填充");
            /****************************************************************************************************
             * 起始位置确认S2记录页地址所处区间
             ****************************************************************************************************/
            if (s2_ppage == null && "S2".equalsIgnoreCase(typeStr)) {
                String initCodeStr = addressStr.substring(0, 2);
                int initCodeNum = Integer.valueOf(initCodeStr, 16).intValue();
                if (initCodeNum == 192 || initCodeNum == 207 || initCodeNum == 224 || initCodeNum == 231 ){
                    s2_ppage = CODING_REGION_C;
                }else if (initCodeNum > 192 && initCodeNum < 207){
                    s2_ppage = CODING_REGION_C;
                }else if (initCodeNum > 224 && initCodeNum < 231){
                    s2_ppage = CODING_REGION_C;
                }
                else if (initCodeNum == 208 || initCodeNum == 223 || initCodeNum == 232 || initCodeNum == 239 ){
                    s2_ppage = CODING_REGION_D;
                }else if (initCodeNum > 208 && initCodeNum < 223){
                    s2_ppage = CODING_REGION_D;
                }else if (initCodeNum > 232 && initCodeNum < 239){
                    s2_ppage = CODING_REGION_D;
                }else{
                    bo.write(("初始化>>>获取到的S2页地址为：" + initCodeStr + "  不存在于指定区间！\n").getBytes());
                    bo.flush();
                    bo.close();
                    throw new CodeErrorException("初始化>>>获取到的S2页地址为："+ initCodeStr + "  不存在于指定区间！");
                }
            }
        }
        /******************************
         * 添加S2页地址校验类型
         ******************************/
        map.put("coding_region", s2_ppage);
        /*******************************
         * 校验S2页地址区间范围
         ******************************/
        long addressStart = 0;
        if (isBcu) {
            String codeing_reginStr = addressStr.substring(0, 2);
            boolean codeCheck = checkRegion(codeing_reginStr, s2_ppage);
            if (!codeCheck) {
                bo.write(("获取到的S2页地址为：" + codeing_reginStr + "，检查类型为：" + s2_ppage + "  不存在于指定区间！\n").getBytes());
                bo.flush();
                bo.close();
                throw new CodeErrorException("获取到的S2页地址为：" + codeing_reginStr + "，检查类型为：" + s2_ppage + "  不存在于指定区间！\n");
            }

            /*******************************
             * 校验S2页内地址区间范围
             * 0X8000 - 0XBFFF
             ******************************/
            codeing_reginStr = addressStr.substring(2);
            if (Integer.valueOf(codeing_reginStr, 16).longValue() > Integer.valueOf("BFFF", 16).longValue()
                    || Integer.valueOf(codeing_reginStr, 16).longValue() < Integer.valueOf("8000", 16).longValue()) {
                throw new CodeErrorException("获取到的S2页内地址为：" + codeing_reginStr + "， 不存在于指定区间【0X8000 - 0XBFFF】\n");
            }

            /******************************************************************
             * 地址计算公式：
             * 页地址 * 16*1024 + 0X400000 + (页内地址 - 0X8000) - 0X700000
             ******************************************************************/
            addressStart = Integer.valueOf(addressStr.substring(0, 2), 16).longValue() * 16l * 1024l
                    + Integer.valueOf("400000", 16).longValue()
                    + Integer.valueOf(addressStr.substring(2), 16).longValue() - Integer.valueOf("8000", 16).longValue();
        } else {
            addressStart = Integer.valueOf(addressStr, 16).longValue();
            if (addressStart < 0x1080 || (addressStart > 0x13FF && addressStart < 0x1900)) {
                bo.write(("获取到的S1地址为：" + addressStart +  "  不存在于指定区间！\n").getBytes());
                bo.flush();
                bo.close();
                throw new CodeErrorException("获取到的S1页地址为：" + addressStart + "  不存在于指定区间！\n");
            }
        }
        /***********************************************
         * 本次起始位置加上本次DATA的长度,为下次计算准备
         ***********************************************/
        map.put("existing_num", addressStart + count );
        /*******************
         * 不足时前面需补空
         ******************/

        StringBuffer buff = new StringBuffer();
        if (existingNum == 0l){
            //logger.debug("起始位置未填充");
            //System.out.println("起始位置未填充");
            bo.write("起始位置开始填充\n".getBytes());
            jsonOutputStream.write(("{\"start_addr\":"+ addressStart).getBytes());
            if (addressStart == 0l){

            }else{
                long diff = addressStart - 1l ;
                bo.write(("首次补充的长度为：" + diff + "  本次开始位置：" + addressStart + "\n").getBytes());
                for (int i = 0; i < diff; i++) {
                    buff.append("FF");
                }
            }
        }else if (existingNum < addressStart-1l){
            long diff = addressStart - 1l - existingNum ;
            //logger.debug("本次补充的长度为：" + diff);
            //System.out.println("本次补充的长度为：" + diff);
            bo.write(("本次补充的长度为：" + diff + "  上次结束位置：" + existingNum + "  本次开始位置：" + addressStart + "\n").getBytes());
            String dataStr = jsonDataBuilder.toString();
            int len = dataStr.length();
            bo.write(("dataStr = " + dataStr +  " len = " + len + "\n").getBytes());
            if (len % 16 != 0) {
                int fill = (len / 16 + 1) * 16 - len;
                bo.write(("after-fill = " + fill + "@" + (addressStart + len / 2 - 1) + "(" + addressStart + " + " + (len / 2 - 1) + ")" + "\n").getBytes());
                for (int i = 0; i < fill; i++) {
                    dataStr = dataStr + "F";
                }
            }
            jsonOutputStream.write((", \"data\":\"" + dataStr + "\"}, \n").getBytes());
            jsonDataBuilder.setLength(0);

            if (addressStart % 8 != 0) {
                long fill = (addressStart / 16 + 1) * 16 - addressStart;
                bo.write(("pre-fill = " + fill + "@" + addressStart + "\n").getBytes());
                addressStart = addressStart - fill / 2;
                for (int i = 0; i < fill; i++) {
                    jsonDataBuilder.append("F");
                }
            }
            jsonOutputStream.write(("{\"start_addr\":"+ addressStart).getBytes());
            for (int i = 0; i < diff; i++) {
                buff.append("FF");
            }
        }else{
            //logger.debug("未填充");
            //System.out.println("未填充");

            bo.write(("未填充, 起始字节数为：" + addressStart + "\n").getBytes());
        }
        buff.append(dateStr);
        //logger.debug("输出data内容：" + buff.toString());
        //System.out.println("输出data内容：" + buff.toString());
        bo.write(("输出data内容：" + buff.toString() + "\n").getBytes());

        jsonDataBuilder.append(dateStr);
        map.put("data_str", buff.toString());

        bo.flush();
        bo.close();

        return map;
    }

    /**
     * 自定义异常部分
     */
    static class CodeErrorException extends Exception
    {
        public CodeErrorException(String msg)
        {
            super(msg);
        }
    }

    static class FileCodeErrorException extends Exception
    {
        public FileCodeErrorException(String msg)
        {
            super(msg);
        }
    }

    /**
     * 将对象转换了字符型
     * @param s
     */
    public static String null2String(Object s) {
        return s == null || s.equals("null") || s.equals("NULL") ? "" : s.toString();
    }
    /**
     * 将字符串类型转换为双精度类型
     * @param str
     */
    public static long StringToLong(String str) {
        return StringToLong(str, (long) 0);
    }

    /**
     * 将字符串类型转换为双精度类型，出错时有def值返回
     * @param str
     * @param def
     */
    public static long StringToLong(String str, long def) {
        long dRet = (long) def;
        try {
            if (str == null || str.trim().equals(""))
                str = "0";
            dRet = Long.parseLong(str);
        }
        catch (NumberFormatException e) {
            System.out.println("StringUtil类出现异常:双精度类型转换错误");
        }
        return dRet;
    }
}
