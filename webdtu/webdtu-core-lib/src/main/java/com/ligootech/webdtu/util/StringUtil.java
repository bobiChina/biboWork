package com.ligootech.webdtu.util;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("ALL")
public class StringUtil {
	private static Logger logger = LoggerFactory.getLogger(StringUtil.class);//指定初始化位置
		
    public static boolean blnTextBox = true;
    public static final String CODING_REGION_C = "C";//编码区 C区间范围 [C0-CF, E0-EF]
    public static final String CODING_REGION_D = "D";//编码区 D区间范围 [D0-DF, E8-EF]
    public static final String S19_LOG_PATH = "/var/s19log.log";

    public static void main(String[] args) {
      //  System.out.println("00010001".substring(0, 8));
             // System.out.println("2013-05-07".substring(5,7));

    	//String str = "ja_va&java&&_&*javaeeeejavajjjj_javaq_weqwe";

    	//int rs = stringNumbers(str,"_",0);

    	//System.out.println(rs);

    	/*String phone = "-10000002";
    	System.out.println("--" + isPhoneNO(phone));*/
        System.out.println("C0---" + Integer.valueOf("C0", 16));
        System.out.println("CF---" + Integer.valueOf("CF", 16));
        System.out.println("E0---" + Integer.valueOf("E0", 16));
        System.out.println("E7---" + Integer.valueOf("E7", 16));
        System.out.println("D0---" + Integer.valueOf("D0", 16));
        System.out.println("DF---" + Integer.valueOf("DF", 16));
        System.out.println("E8---" + Integer.valueOf("E8", 16));
        System.out.println("EF---" + Integer.valueOf("EF", 16));
        /**
         C0---192
         CF---207
         E0---224
         E7---231

         D0---208
         DF---223
         E8---232
         EF---239
         */

        long l = 16564149l;

        long d = l - Integer.valueOf("FCBFB0", 16).longValue();
        System.out.println("d=" + d);

        // 页地址 * 16*1024 + 0X400000 + (页内地址 - 0X8000) - 0X700000   C08000

        long l1 = Integer.valueOf("C0", 16).longValue();
        long l2 = Integer.valueOf("8000", 16).longValue();
        long l3 = Integer.valueOf("400000", 16).longValue();
        long l4 = Integer.valueOf("8000", 16).longValue();
        long l5 = Integer.valueOf("700000", 16).longValue();

        long l6 = l1 * 16l*1024l + l3 + l2 - l4 - l5;

        System.out.println("l1=" + l1 + "  l2=" + l2 + "  l3=" + l3 + "  l4=" + l4 + "  l5=" + l5 + "  l6=" + l6);

        long l7 = Integer.valueOf("C0", 16).longValue() * 16l*1024l + Integer.valueOf("400000", 16).longValue() + Integer.valueOf("8000", 16).longValue() - Integer.valueOf("8000", 16).longValue() - Integer.valueOf("700000", 16).longValue();
        System.out.println(l7);



        //operateS19File("D:/qingping/BC52B.A.abs.s19");
        //operateS19File("D:/qingping/BC52B.abs(3).s19");

        System.out.println("--------------------------------------");
        // getLocalMac();
        Date date = new Date();
        String str = String.format("结果：%s-%s-%s-%f-%s\t日期：%tc\t\t年月日：%tF\t\t时分24小时制：%tR\t\t时分秒：%tT\t\t指定格式：%tF %tT", 2.5*5, "b", "c", 2.4*6.8, "d", date, date, date, date, date, date);
        System.out.println(str);
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
    public static void operateS19File(String inFilePath) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFilePath))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String lineStr;
        long existingNum = 0l;
        StringBuffer outBuff = new StringBuffer();
        String checkType = null;
        try {
            while((lineStr=br.readLine()) != null) {
                Map<String, Object> map = translationS19(lineStr, existingNum, checkType);
                if (map == null) {

                }else{
                    outBuff.append(null2String(map.get("data_str")));
                    existingNum = StringToLong(null2String(map.get("existing_num")));
                    checkType = null2String(map.get("coding_region"));
                }
            }
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
    public static Map<String, Object> translationS19(String lineStr, long existingNum, String s2_ppage) throws IOException, CodeErrorException, FileCodeErrorException {
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
        if ("S0".equalsIgnoreCase(typeStr) || "S2".equalsIgnoreCase(typeStr) || "S9".equalsIgnoreCase(typeStr)){
            if("S2".equalsIgnoreCase(typeStr)){
                addressStr = lineStr.substring(4, 10);
                dateStr = lineStr.substring(10, lineLength-2);
                count = count - 4l;
            }else{
                return null;
            }
        }else{
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
        String codeing_reginStr = addressStr.substring(0, 2);
        boolean codeCheck = checkRegion(codeing_reginStr, s2_ppage);
        if (!codeCheck){
            bo.write(("获取到的S2页地址为："+ codeing_reginStr + "，检查类型为：" + s2_ppage + "  不存在于指定区间！\n").getBytes());
            bo.flush();
            bo.close();
            throw new CodeErrorException("获取到的S2页地址为："+ codeing_reginStr + "，检查类型为：" + s2_ppage + "  不存在于指定区间！\n");
        }

        /*******************************
         * 校验S2页内地址区间范围
         * 0X8000 - 0XBFFF
         ******************************/
        codeing_reginStr = addressStr.substring(2);
        if (Integer.valueOf(codeing_reginStr, 16).longValue() > Integer.valueOf("BFFF", 16).longValue()
                || Integer.valueOf(codeing_reginStr, 16).longValue() < Integer.valueOf("8000", 16).longValue()){
            throw new CodeErrorException("获取到的S2页内地址为："+ codeing_reginStr + "， 不存在于指定区间【0X8000 - 0XBFFF】\n");
        }

        /******************************************************************
         * 地址计算公式：
         * 页地址 * 16*1024 + 0X400000 + (页内地址 - 0X8000) - 0X700000
         ******************************************************************/
        long addressStart = Integer.valueOf(addressStr.substring(0, 2), 16).longValue() * 16l*1024l
                + Integer.valueOf("400000", 16).longValue()
                + Integer.valueOf(addressStr.substring(2), 16).longValue() - Integer.valueOf("8000", 16).longValue()
                - Integer.valueOf("700000", 16).longValue();
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
        map.put("data_str", buff.toString());

        bo.flush();
        bo.close();

        return map;
    }






    /**
     * 将ISO字符转化为GBK字符
     *
     * @param str
     */
    public static String getGBKFromISO(String str) {
        try {
            if (str == null) str = "";
            byte[] buf = str.getBytes("iso-8859-1");
            byte[] buf2 = str.getBytes("gbk");
            if (!str.equals(new String(buf2, "gbk"))) {
                str = new String(buf, "gbk");
            }

            return str;
        }
        catch (UnsupportedEncodingException e) {
            logger.error("StringUtil类出现异常", e);
        }
        return str;
    }

    //从URL中将UTF8转换为GBK
    public static String getGBKFromURL(String s) {
        String ret = "null";
        try {
            ret = java.net.URLDecoder.decode(s, "utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return ret;
    }

    //从URL中将GBK转换为UTF8
    public static String getUTFFromURL(String s) {
        String ret = "null";
        try {
            ret = java.net.URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return ret;
    }

    public static String getGBKFromUTF(String str) {
        try {
            if (str == null) str = "";
            byte[] buf = str.getBytes("utf-8");
            byte[] buf2 = str.getBytes("gbk");
            if (!str.equals(new String(buf2, "gbk"))) {
                str = new String(buf, "gbk");
            }

            return str;
        }
        catch (UnsupportedEncodingException e) {
            logger.error("StringUtil类出现异常", e);
        }
        return str;
    }


    /**
     * 将列表转换为字符串输出
     *
     * @param arr
     * @param split
     */
    @SuppressWarnings("rawtypes")
	public static String getStrByArray(ArrayList arr, String split) {
        String outstr = "";
        for (int i = 0; i < arr.size(); i++) {
            outstr += arr.get(i) + split;
        }
        if (outstr.length() > 0) {
            outstr = outstr.substring(0, outstr.length() - 1);
        }
        return outstr;
    }

    /**
     * 将GBK字符转化为ISO字符
     *
     * @param str
     */
    public static String getISOFromGBK(String str) {
        try {
            if (str == null) str = "";
            byte[] buf = str.getBytes("gbk");
            byte[] buf2 = str.getBytes("iso-8859-1");
            if (!str.equals(new String(buf2, "iso-8859-1"))) {
                str = new String(buf, "iso-8859-1");
            }
        }
        catch (UnsupportedEncodingException e) {
            logger.error("StringUtil类出现异常", e);
        }
        return str;
    }

    /**
     * 将字符串handleStr中以pointStr以分隔的每个字符串存放在向量中返回
     *
     * @param handleStr
     * @param pointStr
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector explode(String handleStr, String pointStr) {
        Vector v = new Vector();
        int pos1, pos2;
        try {
            if (handleStr.length() > 0) {
                pos1 = handleStr.indexOf(pointStr);
                pos2 = 0;
                while (pos1 != -1) {
                    v.addElement(handleStr.substring(pos2, pos1));
                    pos2 = pos1 + pointStr.length();
                    pos1 = handleStr.indexOf(pointStr, pos2);
                }
                v.addElement(handleStr.substring(pos2));
            }
        }
        catch (Exception e) {
            logger.error("StringUtil类出现异常", e);
        }
        return v;
    }

    /**
     * 在字符串handleStr中的字符串pointStr以repStr代替
     *
     * @param handleStr
     * @param pointStr
     * @param repStr
     */
    public static String replace(String handleStr, String pointStr, String repStr) {
        String str = new String();
        int pos1, pos2;
        try {
            if (handleStr.length() > 0) {
                pos1 = handleStr.indexOf(pointStr);
                pos2 = 0;
                while (pos1 != -1) {
                    str += handleStr.substring(pos2, pos1);
                    str += repStr;
                    pos2 = pos1 + pointStr.length();
                    pos1 = handleStr.indexOf(pointStr, pos2);
                }
                str += handleStr.substring(pos2);
            }
        }
        catch (Exception e) {
            logger.error("StringUtil类出现异常", e);
        }
        return str;
    }

    /**
     * 设置blnTextBox的值
     *
     * @param blnAttrib
     */
    public static void setReturn(boolean blnAttrib) {
        blnTextBox = blnAttrib;
    }

    public static String getStringSplit(String allStr,String regex,int index){
		String returnStr="";
		if("".equals(allStr)||allStr==null||"null".equals(allStr)){
			returnStr="";
		}else{
			String str[] = allStr.split(regex);
			if(str.length>=index){
				returnStr=str[index];
			}
		}
		
		return returnStr;
	}

    /**
     * 将字符串转换为html字符串
     * @param handleStr
     */
    public static String htmlSpecialChars(String handleStr) {
        return htmlSpecialChars(handleStr, true);
    }

    /**
     * html字符串和字符串之间的互换。当seq为true时转换到html字符串
     *
     * @param handleStr
     * @param seq
     */
    public static String htmlSpecialChars(String handleStr, boolean seq) {
        String str = handleStr;

        if (seq) {
            str = replace(str, "&", "&amp;");
            str = replace(str, "\"", "&quot;");
            str = replace(str, "<", "&lt;");
            str = replace(str, ">", "&gt;");
        } else {
            str = replace(str, "&amp;", "&");
            str = replace(str, "&quot;", "\"");
            str = replace(str, "&lt;", "<");
            str = replace(str, "&gt;", ">");
        }

        if (!blnTextBox)
            if (seq)
                str = replace(str, "\n", "<br>");
            else
                str = replace(str, "<br>", "\n");

        return str;
    }

    /**
     * 将字符串中的"\n"以"<br>&nbsp;&nbsp;" 替换后返回
     *
     * @param handleStr
     */
    public static String returnChar2BR(String handleStr) {
        String str = handleStr;
        str = replace(str, "\n", "<br>&nbsp;&nbsp;");
        return str;
    }


    /**
     * 将字符串中的"\n"以"<br>&nbsp;&nbsp;" 替换后返回
     *
     * @param handleStr
     */
    public static String returnChar2BRno(String handleStr) {
        String str = handleStr;
        if (str != null) {
            str = replace(str, "\n", "<br>");
        } else {
            str = "";
        }
        return str;
    }

    /**
     * 将handler中的内容取出转换为字符串并将其每个以separator分割开后返回。
     *
     * @param handler
     * @param separator
     */
    @SuppressWarnings("rawtypes")
	public static String implode(Vector handler, String separator) {
        StringBuffer strbuf = new StringBuffer();
        try {
            if (!handler.isEmpty()) {
                int len = handler.size();
                for (int loopi = 0; loopi < len; loopi++) {
                    strbuf.append((String) handler.get(loopi));
                    if (loopi != len - 1)
                        strbuf.append(separator);
                }
            }
        }
        catch (Exception e) {
            logger.error("StringUtil类出现异常", e);
        }
        return strbuf.toString();
    }

    /**
     * Return appointed String from a String Vector
     * param1: String Vector
     * param2: appointed Index
     * param3: include Excel CSV process.
     */
    @SuppressWarnings("rawtypes")
	public static String getField(Vector vt, int i, boolean isExcel) {
        String str = "";
        try {
            str = (String) vt.get(i);
            if (str != null && str.length() > 2 && isExcel) {
                if (str.substring(0, 1).compareTo("\"") == 0) {
                    str = str.substring(1, str.length() - 1);
                    str = StringUtil.replace(str, "\"\"", "\"");
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            logger.error("StringUtil类出现异常", e);
            return "";
        }
        return str;
    }

    /**
     * 当字符串长度小于Len时，有字符InsChar填冲到str的左边或右边，当intDirect为0时为左，1时为右
     * param1: father string
     * param2: need fill in char
     * param3: 0 is left fill in
     * 1 is right fill in
     * param4: total string length after fill in char
     */
    public static String insStr(String str, String InsChar, int intDirect,
                                int Len) {
        int intLen = str.length();
        StringBuffer strBuffer = new StringBuffer(str);

        if (intLen < Len) {
            int inttmpLen = Len - intLen;
            for (int i = 0; i < inttmpLen; i++) {
                if (intDirect == 1) {
                    str = str.concat(InsChar);
                } else if (intDirect == 0) {
                    strBuffer.insert(0, InsChar);
                    str = strBuffer.toString();
                }
            }
        }
        return str;
    }

    /**
     * 返回在字符串str中，首次出现字符串divided的位置。若没有找到返回-1
     *
     * @param str
     * @param divided
     */
    public static int searchDiv(String str, String divided) {
        try {
            divided = divided.trim();
            int divpos = -1;

            if (str.length() > 0) {
                divpos = str.indexOf(divided);

                return divpos;
            } else
                return divpos;
        }
        catch (Exception e) {
            logger.error("StringUtil类出现异常", e);
            return -1;
        }
    }

    /**
     * 在字符串str中取首次出现startdiv到首次出现enddiv之间的字符串并返回，如果没有找到返回“”
     *
     * @param str
     * @param startdiv
     * @param enddiv
     */
    public static String extractStr(String str, String startdiv, String enddiv) {
        int startdivlen = startdiv.length();
        str = str.trim();

        int startpos = -1;
        int endpos = -1;

        startdiv = startdiv.trim();
        enddiv = enddiv.trim();
        startpos = searchDiv(str, startdiv);
        if (str.length() > 0) {
            if (startpos >= 0) {
                str = str.substring(startpos + startdivlen);
                str = str.trim();
            }
            endpos = searchDiv(str, enddiv);
            if (endpos == -1)
                return "";
            str = str.substring(0, endpos);
            str = str.trim();
        }
        return str;
    }

    /**
     * 返回一个不为空的字符串
     *
     * @param str
     */
    public static String isNull(String str) {
        return isNull(str, "&nbsp;");
    }

    /**
     * 返回一个不为空的字符串，当为空时返回def
     *
     * @param str
     * @param def
     */
    public static String isNull(String str, String def) {
        if (str == null)
            return def;
        else if (str.length() == 0)
            return def;
        else
            return str;
    }

    /**
     * 将字符串类型转换为整数类型
     *
     * @param str
     */
    public static int StringToInt(String str) {
        return StringToInt(str, 0);
    }

    /**
     * 将字符串类型转换为整数类型，出错时有def值返回
     *
     * @param str
     * @param def
     */
    public static int StringToInt(String str, int def) {
        int intRet = def;
        try {
            if (str == null || str.trim().equals(""))
                str = def + "";
            intRet = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            logger.error("StringUtil类出现异常", e);
            return def;
        }
        return intRet;
    }

    /**
     * 将字符串类型转换为浮点类型
     *
     * @param str
     */
    public static float StringToFloat(String str) {
        return StringToFloat(str, 0);
    }

    /**
     * 将字符串类型转换为浮点类型，出错时有def值返回
     * @param str
     * @param def
     */
    public static float StringToFloat(String str, float def) {
        float fRet = def;
        try {
            if (str == null || str.trim().equals(""))
                str = "0";
            fRet = Float.parseFloat(str);
        }
        catch (NumberFormatException e) {
            logger.error("StringUtil类出现异常", e);
            return def;
        }
        return fRet;
    }

    /**
     * 将字符串类型转换为双精度类型
     *
     * @param str
     */
    public static double StringToDouble(String str) {
        return StringToDouble(str, (double) 0);
    }

    /**
     * 将字符串类型转换为双精度类型，出错时有def值返回
     *
     * @param str
     * @param def
     */
    public static double StringToDouble(String str, double def) {
        double dRet = (double) def;
        try {
            if (str == null || str.trim().equals(""))
                str = "0";
            dRet = Double.parseDouble(str);
        }
        catch (NumberFormatException e) {
            logger.error("StringUtil类出现异常", e);
            return def;
        }
        return dRet;
    }

    /**
     * 将字符串类型转换为双精度类型
     *
     * @param str
     */
    public static long StringToLong(String str) {
        return StringToLong(str, (long) 0);
    }

    /**
     * 将字符串类型转换为双精度类型，出错时有def值返回
     *
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
            logger.error("StringUtil类出现异常", e);
        }
        return dRet;
    }

    /**
     * 将字符串类型转换为时间类型，出错时有def值返回
     *
     * @param str
     */
    public static String StringToDate(String str) {
        String intRet = DateUtil.date2str(new java.util.Date());
        try {
            if (str == null || str.trim().equals(""))
                str = intRet;
            intRet = str;
        }
        catch (NumberFormatException e) {
            logger.error("StringUtil类出现异常", e);
        }
        return intRet;
    }

    /**
     * 返回正确的日期格式
     *
     * @param str
     */
    public static String StringToStrDate(String str) {
        String init = "";
        if (str == null || str.trim().equals("")) {
            init = DateUtil.getDateStr(new Date());
        } else {
            init = str;
        }
        return init;
    }

    /**
     * 获得安全字符串，使得字符串不为空，并去掉前后的空格
     *
     * @param str
     */
    public static String getSafeString(String str) {
        if (str == null)
            return "";
        else
            return str.trim();
    }

    /**
     * 将字符串在指定的长度内显示，超出后以..代替
     *
     * @param str  in string
     * @param iLen specify length
     *             out string
     */
    public static String substr(String str, int iLen) {
        if (str == null)
            return "";
        if (iLen > 2) {
            if (str.length() > iLen - 2) {
                str = str.substring(0, iLen - 2) + "..";
            }

        }
        return str;
    }
    /**
     * 截取指定长度字符串
     * @param str
     * @param iLen
     * @return
     * @author WLY
     * @日期 2013-7-31 上午5:44:16
     */
    public static String substrByLen(String str, int iLen) {
        if (str == null)
            return "";
        int len = str.length();
        if (len >= iLen ) {           
            return str.substring(0, iLen);
        }
        return str;
    }

    public static String substr(String oldString, int length, String add) {
        int oldL = oldString.length();
        if (oldL < length) {
            for (int i = 0; i < (length - oldL); i++) oldString += add;
        } else if (oldL > length) oldString = oldString.substring(0, 8);
        return oldString;
    }

    /**
     * 将字符串转换为UTF-8
     *
     * @param str handle string
     *            str
     */
    public static String getJpString(String str) {
        if (str == null) {
            return null;
        }
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (java.io.UnsupportedEncodingException e) {
            logger.error("StringUtil类出现异常", e);
        }
        return null;
    }

    /**
     * 将字符串数组转换为UTF-8
     *
     * @param str handle string
     *            str[]
     */

    public static String[] getJpString(String[] str) {
        if (str == null) {
            return null;
        }
        String[] ret = new String[str.length];
        for (int i = 0; i < str.length; i++) {
            ret[i] = getJpString(str[i]);
        }
        return ret;
    }

    /**
     * 返回以UTF-8编码的URL
     *
     * @param str handle string
     *            str[]
     */

    public static String getUrlString(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            logger.error("StringUtil类出现异常", e);
        }
        return null;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void Obj2Map(Object obj, Map map) {
        if (map == null)
            map = new java.util.HashMap();
        PropertyDescriptor descriptors[] = BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(obj);
        for (int i = 0; i < descriptors.length; i++) {
            String name = descriptors[i].getName();
            try {
                if (descriptors[i].getReadMethod() != null) {
                    map.put(name, BeanUtilsBean.getInstance().getPropertyUtils().getProperty(obj, name));
                }
            }
            catch (Exception e) {
                logger.error("StringUtil类出现异常", e);
            }
        }
    }

    /**
     * 将对象转换了字符型
     *
     * @param s
     */
    public static String null2String(Object s) {
        return s == null || s.equals("null") || s.equals("NULL") ? "" : s.toString();
    }

    /**
     * 运行可执行文件
     *
     * @param cmd
     * @return String
     */
    public static synchronized boolean executeCmd(String cmd) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        }
        catch (Exception e) {
            logger.error("StringUtil类出现异常", e);
            return false;
        }
        process.destroy();
        return true;
    }


    /**
     * 将字符串数组转换为（'a','b'）的格式后返回，来方便数据库的操作
     *
     * @param names
     * @return String
     */
    public static String getStrsplit(String[] names) {
        if (names == null || names.length == 0) return "('')";
        String result = "(";
        for (int i = 0; i < names.length; i++) {
            if (i == names.length - 1) result = result + "'" + names[i] + "'";
            else
                result = result + "'" + names[i] + "',";
        }
        result = result + ")";
        return result;
    }

    /**
     * 将字符串数组转换为（'a','b'）的格式后返回，来方便数据库的操作
     *
     * @param names
     * @return String
     */
    @SuppressWarnings("rawtypes")
	public static String getStrsplit(List names) {
        if (names == null || names.size() == 0) return "('')";
        String result = "(";
        for (int i = 0; i < names.size(); i++) {
            if (i == names.size() - 1) result = result + "'" + (String) names.get(i) + "'";
            else
                result = result + "'" + (String) names.get(i) + "',";
        }
        result = result + ")";
        return result;
    }

    /**
     * 将整型数组转换为（1，2）的格式后返回，来方便数据库的操作
     *
     * @param ids
     * @return String
     */
    public static String getIdsplit(String[] ids) {
        if (ids == null || ids.length == 0) return "('')";
        String result = "(";
        for (int i = 0; i < ids.length; i++) {
            if (i == ids.length - 1) result = result + ids[i];
            else
                result = result + ids[i] + ",";
        }
        result = result + ")";
        return result;
    }

    /**
     * 将向量转换为（1，2）的格式后返回，来方便数据库的操作
     *
     * @param ids
     * @return String
     */
    @SuppressWarnings("rawtypes")
	public static String getIdsplit(ArrayList ids) {
        if (ids == null || ids.size() == 0) return "('')";
        String result = "(";
        for (int i = 0; i < ids.size(); i++) {
            if (i == ids.size() - 1) result = result + (String) ids.get(i);
            else
                result = result + (String) ids.get(i) + ",";
        }
        result = result + ")";
        return result;
    }

    /**
     * 将字符串数组转换为（'a','b'）的格式后返回，来方便数据库的操作
     *
     * @param names
     * @return String
     */
    @SuppressWarnings("rawtypes")
	public static String getStrByArr(ArrayList names, String split) {
        if (names == null || names.size() == 0) return "";
        String result = "";
        for (int i = 0; i < names.size(); i++) {
            if (i == names.size() - 1) result += names.get(i) + "";
            else
                result = names.get(i) + split;
        }
        return result;
    }

    /**
     * 将字符串数组转换为（'a','b'）的格式后返回，来方便数据库的操作
     *
     * @param names
     * @return String
     */
    public static String getStrByArr(String names[], String split) {
        if (names == null || names.length == 0) return "";
        String result = "";
        for (int i = 0; i < names.length; i++) {
            if (i == names.length - 1) result += names[i] + "";
            else
                result = names[i] + split;
        }
        return result;
    }

    /**
     * 将url转换为图片的HTML代码格式
     *
     * @param url
     * @return String
     */
    public static String toImage(String url) {
        return "<img src='" + url + "' border=0>";
    }

    /**
     * 将url转换为图片的HTML代码格式
     *
     * @param url
     * @return String
     */
    public static String toImage(String url, String attribute) {
        return "<img src='" + url + "' border=0 " + attribute + " >";
    }

    /**
     * 返回树形结构的字符串为├来分级
     *
     * @param level
     * @param flag
     * @return String
     */
    public static String getLevelFlag(int level, String flag) {
        String temp = "";
        for (int i = 0; i < level; i++) {
            temp = temp + flag;
        }
        return temp + "├";
    }


    public static String getValues(String str) {
        if (str == null || "".equals(str)) return "0";
        return str;
    }

    public static int getStrLen(String str) {
        if (str == null || "".equals(str)) return 0;
        return str.length();
    }

    public static String getParentID(String str) {
        if (str == null || "".equals(str) || str.length() == 4) return "0000";
        return str.substring(0, str.length() - 4);
    }

    //截取字符串
    public static String getSubStirng(String str, int start, int len) {
        if (str.length() >= len)
            return str.substring(start, len);
        else
            return "";
    }

    //获得字符串长度
    public static int getStringlen(String str) {
        return str.length();
    }
    
    
  //判断用户的 角色中是否有某个id
	/**
	 * @param roleList
	 * @param roleid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isRolein(List roleList, String roleid) {
		String role[] = new String[roleList.size()];// 所有的角色id
		for (int i = 0; i < roleList.size(); i++) {
			if (!StringUtil.null2String(roleList.get(i)).equals("")) {
				role[i] = StringUtil.null2String(roleList.get(i));

				if (roleid.equals(role[i])) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getStrFromStrs(String Strs,String regex,int index){
		if("".equals(Strs)||null==Strs){
			return "";
		}else{
			String Results[] = Strs.split(regex);
			if(Results.length>index){
				return Results[index];
			}else{
				return "";
			}
		}
	}
	/**
	 * @param ht 对象集合的Hashtable
	 * @param str 对象在Hashtable中的主键
	 * @return 返回对应主键的出现异常的时候值 默认返回&nbsp;
	 */
	
	@SuppressWarnings("rawtypes")
	public static String getTableStringFromHT(Hashtable ht, String str) {
		String returnStr = "";
		if (str == null || "".equals(str) || "".equals(str)
				|| ht.get(str) == null) {
			returnStr = "&nbsp;";
		} else {
			returnStr = (String) ht.get(str);
		}
		return returnStr;
	}
	/**
	 * @param ht 对象集合的Hashtable
	 * @param str 对象在Hashtable中的主键
	 * @param defaultStr 默认返回值
	 * @return 返回对应主键的出现异常的时候值 默认返回defaultStr;
	 */
	@SuppressWarnings("rawtypes")
	public static String getTableStringFromHT(Hashtable ht, String str, String defaultStr) {
		String returnStr = "";
		if (str == null || "".equals(str) || "".equals(str)
				|| ht.get(str) == null) {
			returnStr = defaultStr;
		} else {
			returnStr = (String) ht.get(str);
		}
		return returnStr;
	}
	
	/**
	 * @param ht 对象集合的Hashtable
	 * @param str 对象在Hashtable中的主键
	 * @return 返回对应主键的出现异常的时候值 默认返回""
	 */
	@SuppressWarnings("rawtypes")
	public static String getNoNullStringFromHT(Hashtable ht, String str) {
		String returnStr = "";
		if (str == null || "".equals(str) || "".equals(str)
				|| ht.get(str) == null) {
			returnStr = "";
		} else {
			returnStr = (String) ht.get(str);
		}
		return returnStr;
	}
	
	public static String getTableString(String str) {
		String returnStr = "&nbsp;";
		if (str != null && !"".equals(str) && !"null".equals(str)) {
			returnStr = str;
		}
		return returnStr;
	}
	
	
	/**
	 * 将编码转换成对应的名称以便前台显示
	 * @param
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String StrKeyToValue(Hashtable HT,String key,String regex,String returnRegex){
		String returnStr="";
		if(HT==null||key==null){
			returnStr="无";
		}else{
			String keys[] = key.split(regex);
			for(int i = 0;i<keys.length;i++){
				String Str =null2String((String)HT.get(keys[i]));
				if("".equals(returnStr)){
					returnStr =returnStr+("".equals(Str)?keys[i]:Str);
				}else{
					returnStr =returnStr+returnRegex+("".equals(Str)?keys[i]:Str);
				}
			}
		}
		return returnStr;
	}

	/**
	 * 取得某机构本级及上级机构
	 * @param
	 * @return len  机构上下级之间长度差
	 */
	public static String[] getParentUnit(String unitId,int len){
		String returnStr="";
		int UnitLen = unitId.length();
		for(int i=0;i<UnitLen/len;i++){
			if("".equals(returnStr)){
				returnStr=unitId.substring(0, UnitLen-len*i);
			}else{
				returnStr=returnStr+","+unitId.substring(0, UnitLen-len*i);
			}
		}
		return returnStr.split(",");
	}
	
	/**
	 * 获取指定位数的流水号,指定截取位数
	 * @param oldBh 原基数编号
	 * @param jqcd 截取长度
	 * @param lshcd 流水号长度
	 * @return
	 * @author WLY
	 * @日期 2013-4-13 下午6:05:46
	 */
	public static String getLsh(String oldBh,int jqcd,int lshcd){
		
		oldBh = null2String(oldBh);
		
		StringBuffer tempsb = new StringBuffer();
		
		if(!"".equals(oldBh)){
			
			if(jqcd<0 || lshcd<=0){
				return "";
			}
			
			if(oldBh.length()>jqcd){
				
				tempsb.append(oldBh.substring(0,jqcd));
				
				oldBh = oldBh.substring(jqcd);
				
				long lshLong = StringToLong(oldBh);
				
				lshLong = lshLong + 1;
				
				int len = StringUtil.null2String(lshLong).length();
				
				if(lshcd > len){
					
					for (int i = 0; i < lshcd - len; i++) {
			            tempsb.append("0");
			        }
					
					tempsb.append(lshLong);
					
				}else {//大于流水号长度返回空值
					return "";
				}
								
			}else{
				return "";
			}			
		}else{
			return "";
		}
		
		return tempsb.toString();
	}
	
	/**
	 * 判断 src是否包含  dst  常用于 页面判断
	 * @param src 
	 * @param dst
	 * @return  若包含则返回下标，否则返回-1
	 */
	public static int indexOf(String src,String dst){
		return src.indexOf(dst);
	}

	/**
	 * 字符串转换成数组
	 */
    public static String[] getsplitStrs(String names,String str) {
    	String[] result=null;
    	try{
    	names=null2String(names);
        result = names.split(str);
        
        return result;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    /**
	 * 字符串转换成数组
	 */
    public static String getstrsplitStrs(String names,String str,int index) {
    	String result=null;
    	try{
    	names=null2String(names);
    	String[] results = names.split(str);
    	if(results.length>=index)
    	{
    		result = results[index];
    	}
        return result;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * 获取一个字符串在另外一个字符串中的个数
     * @param str
     * @param tagStr
     * @param counter
     * @return
     * @author WLY
     * @日期 2014-2-12 下午7:48:41
     */
    public static int stringNumbers(String str,String tagStr,int counter)
	{
    	if("".equals(null2String(tagStr))){
    		return counter;
    	}
		if (str.indexOf(tagStr)==-1)
		{
			return counter;
		}
		else if(str.indexOf(tagStr) != -1)
		{
			counter++;			
			return stringNumbers(str.substring(str.indexOf(tagStr)+tagStr.length()), tagStr, counter);
		}
		return counter;
	}
    
    /**
     * 手机号码验证
     * @param mobiles
     * @return
     * @author WLY
     * @日期 2014-2-18 下午5:40:04
     */
    public static boolean isMobileNO(String mobiles){  
    	  
    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");    	  
    	Matcher m = p.matcher(mobiles);    	  
    	return m.matches();
    }
    /**
     * 电话验证
     * @param phone
     * @return
     * @author WLY
     * @日期 2014-2-18 下午6:01:30
     */
    public static boolean isPhoneNO(String phone){  
    
    	Pattern p = Pattern.compile("^(\\d{3,4}-)?\\d{7,8}$");    	  
    	Matcher m = p.matcher(phone);    	  
    	return m.matches();
    }
    /**
	 * 字节流转化为字符串
	 * @param in
	 * @param charset 文件的字符集 
	 * @return
	 * @author WLY
	 * @日期 2014-12-18 下午4:58:32
	 */
	public static String stream2String(InputStream in, String charset) { 
        StringBuffer sb = new StringBuffer(); 
        Reader r = null;
        try { 
            r = new InputStreamReader(in, charset); 
            int length = 0; 
            for (char[] c = new char[1024]; (length = r.read(c)) != -1;) { 
                    sb.append(c, 0, length); 
            } 
        } catch (UnsupportedEncodingException e) { 
                e.printStackTrace(); 
        } catch (FileNotFoundException e) { 
                e.printStackTrace(); 
        } catch (IOException e) { 
                e.printStackTrace(); 
        }finally{
        	if(r != null){
        		try{
        			r.close();	
        		}catch (Exception e) {
					e.printStackTrace();
				}
        	}
        }
        return sb.toString(); 
    }

    /**
     * 输出对象属性值
     * @param obj
     * @return
     */
    public static String toString(Object obj) {

        if(obj == null)
            return "null";

        StringBuffer sb = new StringBuffer();

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        sb.append(clazz.getName() + "{");
        try {
            Object param;
            for (Field field : fields) {
                field.setAccessible(true);
                param = field.get(obj);
                if (param instanceof Integer || param instanceof String
                        || param instanceof Double || param instanceof Float
                        || param instanceof Long || param instanceof Boolean )
                {
                    sb.append("\n  " + field.getName() + ":" + field.get(obj));
                }
                else if (param instanceof List || param instanceof Map || param instanceof Date ) {
                    sb.append("\n  " + field.getName() + ":" + field.get(obj).toString());
                }else
                {
                    sb.append("\n  " + field.getName() + ":" + toString(field.get(obj)) );
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        sb.append("\n}");

        return sb.toString();
    }

    /**
     * 对象转换为字符串
     * @param obj
     * @return
     */
    public static String toStringForDB(Object obj) {

        if(obj == null)
            return "null";

        StringBuffer sb = new StringBuffer();

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        sb.append(" {");
        try {
            Object param;
            for (Field field : fields) {
                field.setAccessible(true);
                param = field.get(obj);
                if (param instanceof Integer || param instanceof String
                        || param instanceof Double || param instanceof Float
                        || param instanceof Long || param instanceof Boolean )
                {
                    sb.append(" " + field.getName() + "：" + field.get(obj));
                }
                else if (param instanceof List || param instanceof Map || param instanceof Date ) {
                    sb.append(" " + field.getName() + "：" + field.get(obj).toString());
                }else
                {
                    sb.append(" " + field.getName() + "：" + toStringForDB(field.get(obj)) );
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        sb.append(" }");

        return sb.toString();
    }

    /**
     * 对应的十六进制字符串转化为明文的字母或者数字
     * @param str
     * @return
     */
    public static String hex2String(String str){
        int len = str.length();
        byte[] rs = new byte[len/2];
        String byteStr;
        for (int i = 0; i <rs.length; i++) {
            byteStr = str.substring(i * 2, (i * 2 + 2));
            rs[i] = (byte) Integer.parseInt(byteStr, 16);
        }
        return new String(rs);
    }

    /**
     * 订单号解析
     * @param str
     * @return
     */
    public static String hex2String4Order(String str){
        /*******************************************
         * 输入长度为16位，每四位一节，组成年月日和流水号
         * 前四位为年份的后两位数字，后四位为流水号
         * 生成明文时需补全四位年份，两位月份，两位日期和四位的流水号
         * 输出样式为：LG201603100101
         ******************************************/
        if (str == null || str.length() != 16) {
            return "";
        }

        StringBuffer rsSbf = new StringBuffer("LG");

        /******************************************************
         * 解析年份，因年份至少为两位数所以不需判断小于十的情况
         *****************************************************/
        rsSbf.append("20").append(Integer.parseInt(str.substring(0, 4), 16));
        /******************************************************
         * 解析月份
         *****************************************************/
        int month = Integer.parseInt(str.substring(4, 8), 16);
        rsSbf.append(completionStr(month, 2));

        /******************************************************
         * 解析日期
         *****************************************************/
        int day = Integer.parseInt(str.substring(8, 12), 16);
        rsSbf.append(completionStr(day, 2));

        /******************************************************
         * 解析流水号
         *****************************************************/
        int serial_number = Integer.parseInt(str.substring(12), 16);
        rsSbf.append(completionStr(serial_number, 4));

        return rsSbf.toString();
    }

    /**
     *  去掉指定字符串的开头和结尾的指定字符
     * @param stream 要处理的字符串
     * @param trimstr 要去掉的字符串
     * @param type 要去掉的部位 0-前后都去掉 1-去掉前部分 2-去掉后部分
     * @return
    **/
    public static String sideTrim(String stream, String trimstr, int type) {
        // null或者空字符串的时候不处理
        if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
            return stream;
        }
        // 结束位置
        int epos = 0;
        // 正规表达式
        String regpattern = "[" + trimstr + "]*+";
        Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
        StringBuffer buffer = new StringBuffer(stream).reverse();
        Matcher matcher = pattern.matcher(buffer);
        // 去掉结尾的指定字符
        if (type == 0 || type == 2 ){
            if (matcher.lookingAt()) {
                epos = matcher.end();
                stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
            }
        }

        // 去掉开头的指定字符 
        if (type == 0 || type == 1 ) {
            matcher = pattern.matcher(stream);
            if (matcher.lookingAt()) {
                epos = matcher.end();
                stream = stream.substring(epos);
            }
        }
        // 返回处理后的字符串
        return stream;
    }


    /**
     * 逐个转化为十六进制
     * @param s
     * @return
     */
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        //return "0x" + str;//0x表示十六进制
        return str;
    }

    /**
     * 指定长度转换
     * @param s
     * @param size
     * @return
     */
    public static String toHexString(String s, int size) {
        if ("".equals(null2String(s)) || size < 1) {
            return "";
        }
        String str = "";
        int len = s.length() / size ;

        for (int i = 0; i < len; i++) {
            int num = Integer.parseInt(s.substring((i * size), (i + 1) * size));
            String s4 = Integer.toHexString(num);
            str = str + s4;
        }
        return str;
    }

    /**
     * 订单编号转换
     * @param
     * @return
     */
    public static String order2HexString(String orderNo) {
        orderNo = null2String(orderNo).trim();
        if (orderNo.length() == 14 || orderNo.length() == 16) {
            /*******************************************
             * 去掉订单标识LG， 去掉年份前两位
             * 年份两位，月份两位，日期两位，流水号四位
             * 售后订单格式：LG-SH-2016022603
             ******************************************/
            StringBuffer str = new StringBuffer("(");

            if (orderNo.indexOf("LG-SH-") > -1){//售后订单
                str.append(completionStr(Integer.toHexString(StringToInt(orderNo.substring(8, 10))), 4))
                        .append(completionStr(Integer.toHexString(StringToInt(orderNo.substring(10, 12))), 4))
                        .append(completionStr(Integer.toHexString(StringToInt(orderNo.substring(12, 14))), 4))
                        .append(completionStr(Integer.toHexString(StringToInt(orderNo.substring(14))), 4));
            }else{
                str.append(completionStr(Integer.toHexString(StringToInt(orderNo.substring(4, 6))), 4))
                        .append(completionStr(Integer.toHexString(StringToInt(orderNo.substring(6, 8))), 4))
                        .append(completionStr(Integer.toHexString(StringToInt(orderNo.substring(8, 10))), 4))
                        .append(completionStr(Integer.toHexString(StringToInt(orderNo.substring(10))), 4));
            }

            return str.append("|0001000000000000)").toString().toUpperCase();
        }
        logger.debug("orderNo is error :" + orderNo);
        return orderNo;

    }

    /**
     * 补全字符串，不足的前面加0
     * @param obj
     * @param length
     * @return
     */
    public static String completionStr(Object obj, int length){
        String str = null2String(obj);
        str = String.format("%" + length + "s", str);
        str = str.replaceAll("\\s", "0");
        return str.toUpperCase();
    }

    /**
     * 数组转换
     * @param sqlList
     * @return
     */
    public static String[] getArrBylist(List<String> sqlList){
        if (sqlList == null || sqlList.size() == 0) {
            return null;
        }
        String[] sqlArr = new String[sqlList.size()];
        for (int i = 0; i < sqlList.size(); i++) {
            sqlArr[i] = sqlList.get(i);
        }
        return sqlArr;
    }

    public static void copyPropertiesExclude(Object from, Object to, String[] excludesArray) throws Exception {
        List<String> includesList = null;
        if(excludesArray != null && excludesArray.length > 0) {
            includesList = Arrays.asList(excludesArray); //构造列表对象
        } else {
            return;
        }
        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;
        for (int i = 0; i < fromMethods.length; i++) {
            fromMethod = fromMethods[i];
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("get"))
                continue;
            //排除列表检测  修改为排除部分属性
            String str = fromMethodName.substring(3);
            if(includesList.contains(str.substring(0,1).toLowerCase() + str.substring(1))) {
                continue;
            }
            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);
            if (toMethod == null)
                continue;
            Object value = fromMethod.invoke(from, new Object[0]);
            if(value == null)
                continue;
            //集合类判空处理
            if(value instanceof Collection) {
                Collection newValue = (Collection)value;
                if(newValue.size() <= 0)
                    continue;
            }
            toMethod.invoke(to, new Object[] {value});
        }
    }

    /**
     * 对象属性值复制，仅复制指定名称的属性值
     * @param from
     * @param to
     * @param includsArray
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static void copyPropertiesInclude(Object from, Object to, String[] includsArray) throws Exception {
        List<String> includesList = null;
        if(includsArray != null && includsArray.length > 0) {
            includesList = Arrays.asList(includsArray); //构造列表对象
        } else {
            return;
        }
        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;
        for (int i = 0; i < fromMethods.length; i++) {
            fromMethod = fromMethods[i];
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("get"))
                continue;
            //排除列表检测
            String str = fromMethodName.substring(3);
            if(!includesList.contains(str.substring(0,1).toLowerCase() + str.substring(1))) {
                continue;
            }
            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);
            if (toMethod == null)
                continue;
            Object value = fromMethod.invoke(from, new Object[0]);
            if(value == null)
                continue;
            //集合类判空处理
            if(value instanceof Collection) {
                Collection newValue = (Collection)value;
                if(newValue.size() <= 0)
                    continue;
            }
            toMethod.invoke(to, new Object[] {value});
        }
    }

    /**
     * 从方法数组中获取指定名称的方法
     *
     * @param methods
     * @param name
     * @return
     */
    public static Method findMethodByName(Method[] methods, String name) {
        for (int j = 0; j < methods.length; j++) {
            if (methods[j].getName().equals(name))
                return methods[j];
        }
        return null;
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
     * 拼接指定间隔符的字符串
     * @param join
     * @param strAry
     * @return
     */
    public static String join(String[] strAry, String join ){
        if (strAry == null || strAry.length == 0) {
            return "";
        }
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<strAry.length;i++){
            if(i==(strAry.length-1)){
                sb.append(strAry[i]);
            }else{
                sb.append(strAry[i]).append(join);
            }
        }

        return new String(sb);
    }


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return boolean
     * @author zer0
     * @version 1.0
     * @date 2015-2-19
     */
    public static boolean isBlank(String str){
        if(str==null){
            return true;
        }
        if(str.trim().length()<1){
            return true;
        }
        if(str.trim().equals("")){
            return true;
        }
        if(str.trim().toLowerCase().equals("null")){
            return true;
        }
        return false;
    }

    /**
     * 将字符串转换为byte数组
     *
     * @param string
     * @return byte[]
     * @author zer0
     * @version 1.0
     * @date 2015-3-3
     */
    public static byte[] stringToByte(String string) {
        if (string == null) {
            return new byte[0];
        }
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteOut);
        try {
            dos.writeUTF(string);
        } catch (IOException e) {
            return new byte[0];
        }
        return byteOut.toByteArray();
    }

    /**
     * 随机生成字符串
     * @param length
     * @return String
     * @date 2015-3-7
     */
    public static String generalRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取本机MAC
     */
    public static String getLocalMac() {
        //得到IP，输出PC-201309011313/122.206.73.83
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(ia);
        //获取网卡，获取地址
        byte[] mac = new byte[0];
        try {
            mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        //System.out.println("mac数组长度："+mac.length);
        StringBuffer sb = new StringBuffer("");
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            //System.out.println("每8位:"+str);
            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 随机生成一个mac地址
     * @return String
     * @author zer0
     * @version 1.0
     * @date  2015-7-7
     */
    public static String generalMacString(){
        String str = "ABCDEF0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int macGroupSize = 6;//mac地址长度为6组16进制
        int macOneGroup = 2;//一组有两个字符
        for (int i = 0; i < macGroupSize; i++) {
            for (int j = 0; j < macOneGroup; j++) {
                int number = random.nextInt(16);
                sb.append(str.charAt(number));
            }
            sb.append("-");
        }
        String macString = sb.toString().substring(0, sb.length()-1);
        return macString;
    }

    /**
     * 判断字符串是否为mac地址
     * @param str
     * @return boolean
     * @author zer0
     * @version 1.0
     * @date  2015-7-7
     */
    public static boolean isMacString(String str){
        String pattern = "^([A-Fa-f\\d]{2}[-:])([A-Fa-f\\d]{2}[-:]){4}([A-Fa-f\\d]{2})$";
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(str);
        Boolean isFind = mat.find();
        if (isFind) {
            return true;
        }else {
            return false;
        }
    }
}