package com.ligootech.webdtu.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tools.zip.ZipFile;

/**
 * Created by wly on 2015/12/2 10:49.
 */
public class FileOperateUtil {
    private static final Log logger = LogFactory.getLog(FileOperateUtil.class);

    /** 表格文件 */
    public static final String FILE_TYPE_XLS = "xls,xlsx";
    /** 图片文件 */
    public static final String FILE_TYPE_IMG = "gif,jpg,jpeg,bmp,png";
    /** Word文档文件 */
    public static final String FILE_TYPE_DOC = "doc,docx";
    public static final String FILE_TYPE_OTHER = "html,htm,txt,ppt,zip,pdf,rar,doc,xls";
    /** 文件大小 */
    public static final long FILE_SIZE = 1*1024*1024l; // 1M
    public static final String ERROR_MSG = "errorMsg";

    public static final String REALNAME = "realName";
    public static final String OLD_REALNAME = "oldRealName";
    public static final String STORENAME = "storeName";
    public static final String SIZE = "size";
    public static final String SUFFIX = "suffix";
    public static final String CONTENTTYPE = "contentType";
    public static final String CREATETIME = "createTime";
    public static final String FILETYPE = "fileType";
    public static final String FILEID = "fileid";
    public static final String UPLOADDIR = "/var/uploadDir/";
    public static final String MD5CODE = "MD5CODE";

    /**使用GBK编码可以避免压缩中文文件名乱码*/
    public static final String CHINESE_CHARSET = "GBK";
    /**文件读取缓冲区大小*/
    public static final int CACHE_SIZE = 1024;
    /**
     * 将上传的文件进行重命名
     * @param name
     * @return
     */
    private static String rename(String name) {
        Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()));
        Long random = (long) (Math.random() * now);
        String fileName = now + "" + random;

        if (name.indexOf(".") != -1) {
            fileName += name.substring(name.lastIndexOf(".")).toLowerCase();//文件名统一为小写
        }
        return fileName;
    }

    /**
     * 压缩后的文件名
     * @param name
     * @return
     */
    private static String zipName(String name) {
        String prefix = "";
        if (name.indexOf(".") != -1) {
            prefix = name.substring(0, name.lastIndexOf("."));
        } else {
            prefix = name;
        }
        return prefix + ".zip";
    }

    /**
     * 上传文件 并压缩成zip文件
     * @param request
     * @param params
     * @param values
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> upload(HttpServletRequest request,
                                                   String[] params, Map<String, Object[]> values) throws Exception {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();

        /*String uploadDir = request.getSession().getServletContext()
                .getRealPath("/")
                + FileOperateUtil.UPLOADDIR;*/

        File file = new File(UPLOADDIR);

        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = null;
        String md5Code = null;
        int i = 0;
        for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet()
                .iterator(); it.hasNext(); i++) {

            Map.Entry<String, MultipartFile> entry = it.next();
            MultipartFile mFile = entry.getValue();
            if (mFile.isEmpty()){
                continue;
            }

            fileName = mFile.getOriginalFilename();

            String storeName = rename(fileName);

            String noZipName = UPLOADDIR + storeName;
            String zipName = zipName(noZipName);

            // 上传成为压缩文件
            ZipOutputStream outputStream = new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(zipName)));
            outputStream.putNextEntry(new ZipEntry(fileName));
            outputStream.setEncoding(CHINESE_CHARSET);

            FileCopyUtils.copy(mFile.getInputStream(), outputStream);

            //MD5校验码
            md5Code = MD5Util.getFileMD5String(new File(zipName));

            Map<String, Object> map = new HashMap<String, Object>();
            // 固定参数值对
            map.put(FileOperateUtil.OLD_REALNAME, fileName);
            map.put(FileOperateUtil.REALNAME, zipName(fileName));
            map.put(FileOperateUtil.STORENAME, zipName(storeName));
            map.put(FileOperateUtil.SIZE, new File(zipName).length());
            map.put(FileOperateUtil.SUFFIX, "zip");
            map.put(FileOperateUtil.CONTENTTYPE, "application/octet-stream");
            map.put(FileOperateUtil.CREATETIME, new Date());
            map.put(MD5CODE, md5Code);

            // 自定义参数值对
            for (String param : params) {
                map.put(param, values.get(param)[i]);
            }

            result.add(map);
        }
        return result;
    }

    /**
     * 保存单个文件
     * @param stream
     * @param filePath
     * @throws IOException
     */
    public static void saveFileFromInputStream(InputStream stream,String filePath) throws IOException
    {
        FileOutputStream fs=new FileOutputStream(filePath);
        byte[] buffer =new byte[1024*1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread=stream.read(buffer))!=-1)
        {
            bytesum+=byteread;
            fs.write(buffer,0,byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }

    /**
     * 单个文件上传,不压缩
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, Object> uploadFile(HttpServletRequest request, String fileType) throws Exception {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();

        File file = new File(UPLOADDIR);
        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = null;
        String extname = null;
        int i = 0;
        Map<String, Object> map = new HashMap<String, Object>();
        for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); i++) {
            Map.Entry<String, MultipartFile> entry = it.next();
            MultipartFile mFile = entry.getValue();
            if (mFile.isEmpty()){
                map.put(ERROR_MSG, "上传文件为空");
                return map;
            }

            fileName = mFile.getOriginalFilename();
            //判断文件格式是否符合要求,不符合要求的不予写入
            extname = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if ("xls".equals(fileType)){
                if (FILE_TYPE_XLS.indexOf(extname) == -1){
                    map.put(ERROR_MSG, "请选择扩展名为" + FILE_TYPE_XLS + "的文件进行上传！");
                    return map;
                }
            }else if ("img".equals(fileType)){
                if (FILE_TYPE_IMG.indexOf(extname) == -1){
                    map.put(ERROR_MSG, "请选择扩展名为" + FILE_TYPE_IMG + "的文件进行上传！");
                    return map;
                }
            }else{
                map.put(ERROR_MSG, "文件格式不正确");
                return map;
            }

            //检查文件大小
            if (mFile.getSize() > FILE_SIZE){
                map.put(ERROR_MSG, "文件大小超范围！请选择小于 " + FILE_SIZE/(1024*1024) + "M 的文件进行上传");
                return map;
            }

            String storeName = rename(fileName);
            String filePath = UPLOADDIR + storeName;
            // 上传
            saveFileFromInputStream(mFile.getInputStream(), filePath);

            // 固定参数值对
            map.put(FileOperateUtil.REALNAME, fileName);
            map.put(FileOperateUtil.STORENAME, storeName);
            map.put(FileOperateUtil.SIZE, mFile.getSize());
            map.put(FileOperateUtil.CONTENTTYPE, "application/octet-stream");
            map.put(FileOperateUtil.CREATETIME, new Date());

            return map;
        }
        return null;
    }

    /**
     * 下载
     * @param request
     * @param response
     * @param storeName
     * @param contentType
     * @param realName
     * @throws Exception
     */
    public static void download(HttpServletRequest request,
                                HttpServletResponse response, String storeName, String contentType,
                                String realName, String md5code) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        md5code = StringUtil.null2String(md5code);
        if (!"".equals(md5code)){
            md5code = "; md5code=" + md5code ;
        }

        /*String ctxPath = request.getSession().getServletContext()
                .getRealPath("/")
                + FileOperateUtil.UPLOADDIR;*/

        String downLoadPath = FileOperateUtil.UPLOADDIR + storeName;

        long fileLength = new File(downLoadPath).length();

        response.setContentType(contentType);
        //response.setHeader("Content-disposition", "attachment; filename=" + new String(realName.getBytes("utf-8"), "ISO8859-1") + md5code);
        response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(realName, "UTF-8") + md5code );
        response.setHeader("Content-Length", String.valueOf(fileLength));

        bis = new BufferedInputStream(new FileInputStream(downLoadPath));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }

    /**
     * 订单配置文件上传并压缩成ZIP文件
     * @param request
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> uploadOrderConfigFile(HttpServletRequest request) throws Exception{
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();

        File file = new File(UPLOADDIR);
        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = null;
        String md5Code = null;
        int i = 0;
        for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); i++) {
            Map.Entry<String, MultipartFile> entry = it.next();
            MultipartFile mFile = entry.getValue();
            if (mFile.isEmpty()){
                continue;
            }

            fileName = mFile.getOriginalFilename();
            String storeName = rename(fileName);
            String noZipName = UPLOADDIR + storeName;
            String zipName = zipName(noZipName);

            String fileType = storeName.substring(storeName.lastIndexOf(".") + 1);
            boolean boo = checkDevice4Order(mFile.getInputStream(), request, fileType);//验证固件
            if (!boo){
                Map<String, Object> map = new HashMap<String, Object>();

                // 固定参数值对
                map.put(FileOperateUtil.REALNAME, fileName);
                map.put(FileOperateUtil.ERROR_MSG, "s19_error");// 固件验证错误
                result.add(map);
                return result;
            }

            // 上传成为压缩文件
            ZipOutputStream outputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipName)));
            //outputStream.setEncoding(CHINESE_CHARSET);
            outputStream.setEncoding("UTF-8");
            outputStream.putNextEntry(new ZipEntry(fileName));

            FileCopyUtils.copy(mFile.getInputStream(), outputStream);
            outputStream.flush();

            //MD5校验码
            md5Code = MD5Util.getFileMD5String(new File(zipName));

            Map<String, Object> map = new HashMap<String, Object>();

            // 固定参数值对
            //map.put(FileOperateUtil.OLD_REALNAME, fileName);
            map.put(FileOperateUtil.REALNAME, fileName);
            map.put(FileOperateUtil.STORENAME, zipName(storeName));
            map.put(FileOperateUtil.FILEID, storeName.substring(0, storeName.lastIndexOf(".")));
            map.put(FileOperateUtil.FILETYPE, fileType);
            map.put(FileOperateUtil.SIZE, new File(zipName).length());
            map.put(FileOperateUtil.SUFFIX, "zip");
            map.put(FileOperateUtil.ERROR_MSG, "none");
            map.put("MD5CODE", md5Code);
            //map.put(FileOperateUtil.CONTENTTYPE, "application/octet-stream");
            //map.put(FileOperateUtil.CREATETIME, new Date());
            result.add(map);
        }
        return result;
    }

    /**
     * 验证订单固件文件
     * @param inputStream
     * @param request
     * @param fileType
     * @return 通过验证的返回true 不通过验证的返回false
     * @throws Exception
     */
    private static boolean checkDevice4Order(InputStream inputStream, HttpServletRequest request, String fileType) throws Exception{
        String orderno = ServletRequestUtils.getStringParameter(request, "orderno");
        String device_code = ServletRequestUtils.getStringParameter(request, "device_code");
        String device_class = ServletRequestUtils.getStringParameter(request, "device_class");

        /****************************************************
         * 售后订单不验证 sby 2016年5月27日 16:08:28
         * 还原验证 pyj 2016年6月2日 15:25:09
         * 售后订单从机不验证 sby 2016年10月14日 15:57:28
         ***************************************************/
        if (null != orderno && orderno.indexOf("LG-SH-") > -1 && "BMU".equals(device_class)){
            return true;
        }

        boolean boo_class = false;
        boo_class = "BCU".equals(device_class) || "BYU".equals(device_class) || "BMU".equals(device_class) ? true : false ;
        /*****************************************************
         * M1112、M1216、BY3124B 三种设备型号不做文件内容验证
         * 2016年4月20日 14:37:18 sby
         * 添加C11 C11B 不验证 sby 2016年5月31日 15:53:24
         *****************************************************/
        if ("M1112".equalsIgnoreCase(device_code) || "M1216".equalsIgnoreCase(device_code) || "BY3124B".equalsIgnoreCase(device_code)
                || "C11".equalsIgnoreCase(device_code) || "C11B".equalsIgnoreCase(device_code) ){
            return true;
        }

        if ("s19".equalsIgnoreCase(fileType) && boo_class){ //只做BCU BYU BMU 的固件验证
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String lineStr;
            Matcher matcher;

            //先简单验证前面的字符
            if (("BCU".equals(device_class) || "BYU".equals(device_class))){
                Pattern pattern_BCU = Pattern.compile("^S2[0-9A-F]{2}E38000[0-9A-F]{32}.+", Pattern.CASE_INSENSITIVE);//至少32位保证fwVersion和orderVersion
                while((lineStr=br.readLine()) != null) {
                    matcher = pattern_BCU.matcher(lineStr);
                    if (matcher.matches()) {
                        logger.debug("S19CHECK>>>scanInfo:" + lineStr);
                        /***********************************************************************************************************************
                         * S2开头 + 中间任意两位 + E38000 + 16位FW + 4位（年份后两位数字） + 4位月份 + 4位日期 + 4位流水号 + 设备型号 + 00
                         * 比较订单编号+设备型号
                         * 将参数中的订单编号和设备型号转化为十六进制的字符串+00，可一次性比较完成
                         * 不能加00结束符，因设备型号长度不一样，翻译出来的十六进制字符就不一样，字符长度不一样时不能做二选一匹配(中括号改为小括号可解决)
                         **********************************************************************************************************************/
                        String matchStr = "^S2[0-9A-F]{2}E38000[0-9A-F]{16}" + StringUtil.order2HexString(orderno) + formatDeviceCode(device_code) + ".+" ;
                        matchStr = matchStr.toUpperCase();
                        logger.debug("S19CHECK>>>orderno=" + orderno + "  device_code=" + device_code + "  device_class=" + device_class + " matchStr=" + matchStr);

                        Pattern pattern = Pattern.compile(matchStr, Pattern.CASE_INSENSITIVE);
                        //S21AE38000  0001 0000 002E 013F 00100002001000654243353241005F
                        matcher = pattern.matcher(lineStr);//去掉前缀和 fwVersion
                        logger.debug("S19CHECK>>>BCU_BYU_result:" + matcher.matches());
                        if (matcher.matches()){
                            return true;
                        }
                    }
                }
                logger.debug("BCU BYU not find : S2[0-9A-F]{2}E38000[0-9A-F]{32}.+");
                return false;
            }else if ( "BMU".equals(device_class)){
                Pattern pattern_BMU = Pattern.compile("^S1[0-9A-F]{2}C8[0|2]0.+", Pattern.CASE_INSENSITIVE);//包含C800 C820
                boolean orderMatch = false;
                boolean deviceMatch = false;
                while((lineStr=br.readLine()) != null ) {
                    matcher = pattern_BMU.matcher(lineStr);
                    if (matcher.matches()) {
                        logger.debug("S19CHECK>>>scanInfo:" + lineStr);
                        //判断是C800 C820 订单编号不用00结尾 设备型号需要用00结尾
                        //S113C80000020000001100170001000000000000F9
                        String matchOrder = "^S1[0-9A-F]{2}C800[0-9A-F]{16}" + StringUtil.order2HexString(orderno) + ".*";
                        matchOrder = matchOrder.toUpperCase();
                        logger.debug("S19CHECK>>>orderno=" + orderno + "  device_code=" + device_code + "  device_class=" + device_class + " matchStr=" + matchOrder);
                        Pattern pattern_BMU_order = Pattern.compile(matchOrder, Pattern.CASE_INSENSITIVE);
                        Matcher matcher_order = pattern_BMU_order.matcher(lineStr);
                        logger.debug("S19CHECK>>>BMU_order_result:" + matcher_order.matches());
                        if (matcher_order.matches()){
                            orderMatch = true;
                        }
                        else{
                            String matchDevice = "^S1[0-9A-F]{2}C820" + formatDeviceCode(device_code) + ".+";
                            matchDevice = matchDevice.toUpperCase();
                            logger.debug("S19CHECK>>>orderno=" + orderno + "  device_code=" + device_code + "  device_class=" + device_class + " matchStr=" + matchDevice);
                            Pattern pattern_BMU_device = Pattern.compile(matchDevice, Pattern.CASE_INSENSITIVE);
                            Matcher matcher_device = pattern_BMU_device.matcher(lineStr);
                            logger.debug("S19CHECK>>>BMU_device_result:" + matcher_device.matches());
                            if (matcher_device.matches()){
                                deviceMatch = true;
                            }
                        }
                        /*********************************************
                         * 订单版本号和设备版本号都验证通过则不再扫描
                         *********************************************/
                        if (orderMatch && deviceMatch){
                            return true;
                        }
                    }
                }
                logger.debug("BMU not find : ^S1[0-9A-F]{2}C8[0|2]0.+");
                return false;//扫描结束还未匹配上返回匹配不成功
            }
        }

        return true;
    }

    /**
     * 匹配新老设备型号,返回设备型号的十六进制字符串
     * @param newDeviceCode
     * @return
     */
    public static String formatDeviceCode( String newDeviceCode){
        Map<String, String> map = BaseData.deviceMap;

        String codeStr = map.get(newDeviceCode);
        String rsStr = "";

        if (codeStr == null) {
            rsStr = "error" ;
        }
        else if("BM51A".equals(codeStr)){
            rsStr = "(" + StringUtil.toHexString("BM51A") + "|" + StringUtil.toHexString("M51(2)XX") + ")";
        }
        else if("C11".equals(codeStr)){
            rsStr = "(" + StringUtil.toHexString("C11") + "|" + StringUtil.toHexString("BCU") + ")";
        }
        else if("C11B".equals(codeStr)){
            rsStr = "(" + StringUtil.toHexString("C11B") + "|" + StringUtil.toHexString("BCU") + ")";
        }else{
            rsStr = StringUtil.toHexString(codeStr);
        }
        //使用模版的固件时，订单版本号为0001000000000000
        return rsStr.toUpperCase();
    }


    public static void main(String[] arges){
        String match = "^2[A-Za-z0-9]{14}$";
        Pattern pattern_BMU_device = Pattern.compile(match, Pattern.CASE_INSENSITIVE);

        String str = "20L7CN8B3100q11";
        Matcher matcher_device = pattern_BMU_device.matcher(str);

        System.out.println(matcher_device.matches() + "   str=" + str);

    }

    /**
     * 订单软件打包下载
     * @param request
     * @param response
     * @param contentType
     * @param realName
     * @param devices
     * @throws Exception
     */
    public static void download4Order(HttpServletRequest request,
                                HttpServletResponse response, String contentType,
                                String realName, List<Map<String, Object>> devices) throws Exception {
        /***************************
         * 生成文件
         **************************/
        StringBuffer readmeSb = new StringBuffer();
        readmeSb.append("*******************************\r\n" +
                "* 设备型号与文件对照信息      *\r\n" +
                "*******************************\r\n\r\n");
        readmeSb.append("设备型号\t从机ID\t文件类型\t文件名称\r\n");
        Map<String, Object> map;
        Map<String, String> fileCodeMap = new HashMap<String, String>();
        String fileCode = "";
        for (int i = 0; i < devices.size(); i++) {
            map = devices.get(i);
            fileCode = StringUtil.null2String(map.get("file_code"));
            if (!"".equals(fileCode)){
                readmeSb.append(StringUtil.null2String(map.get("device_code")));

                if (StringUtil.null2String(map.get("device_code")).length() > 7){
                    readmeSb.append("\t");// BM5148BT 超出
                }else{
                    readmeSb.append("\t\t");
                }
                readmeSb.append(StringUtil.null2String(map.get("bmu_no"))).append("\t").append(StringUtil.null2String(map.get("file_type"))).append("\t\t").append(StringUtil.null2String(map.get("file_name"))).append("\r\n");

                fileCodeMap.put(fileCode, fileCode);
            }
        }

        String storeName = UPLOADDIR + BhGenerator.getBh() + ".zip";
        ZipOutputStream outputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(storeName, false))); //重新生成文件
       outputStream.setEncoding(CHINESE_CHARSET);

        Set<String> fileCodeSet = fileCodeMap.keySet();
        for (String str : fileCodeSet) {
            downloadZip4Order(str, outputStream);
        }
        outputStream.putNextEntry(new ZipEntry("Readme.txt"));
        outputStream.write(readmeSb.toString().getBytes(CHINESE_CHARSET));//中文处理
        outputStream.flush();
        outputStream.close();

        /***************************
         * 下载
         **************************/
        request.setCharacterEncoding("UTF-8");

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        long fileLength = new File(storeName).length();
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(realName, "UTF-8"));
        response.setHeader("Content-Length", String.valueOf(fileLength));

        bis = new BufferedInputStream(new FileInputStream(storeName));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }


    /**
     * 解压并合并下载所需的压缩包
     * @param fileCode 压缩文件ID
     */
    public static void downloadZip4Order(String fileCode, ZipOutputStream outputStream) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(UPLOADDIR + fileCode + ".zip", CHINESE_CHARSET);
            Enumeration<ZipEntry> zipEntries = zipFile.getEntries();
            ZipEntry entry;
            while (zipEntries.hasMoreElements()) {
                entry = (ZipEntry) zipEntries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                outputStream.putNextEntry(new ZipEntry(entry.getName()));
                InputStream fis = zipFile.getInputStream(entry);
                byte[] buffer = new byte[CACHE_SIZE];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, r);
                }
                fis.close();
                outputStream.flush();
            }
        } catch (IOException e) {
            logger.debug("系统找不到指定的文件。");
        }finally{
            try {
                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /***************************************
     * 原始压缩和解压代码
     **************************************/
    /**
     * 压缩文件
     * @param sourceFolder 压缩文件夹
     * @param zipFilePath 压缩文件输出路径
     */
    public static void zip(String sourceFolder, String zipFilePath) {
        OutputStream os = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        try {
            os = new FileOutputStream(zipFilePath);
            bos = new BufferedOutputStream(os);
            zos = new ZipOutputStream(bos);
            // 解决中文文件名乱码
            zos.setEncoding(CHINESE_CHARSET);
            File file = new File(sourceFolder);
            String basePath = null;
            if (file.isDirectory()) {//压缩文件夹
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (zos != null) {
                    zos.closeEntry();
                    zos.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归压缩文件
     * @param parentFile
     * @param basePath
     * @param zos
     * @throws Exception
     */
    private static void zipFile(File parentFile, String basePath, ZipOutputStream zos) throws Exception {
        File[] files = new File[0];
        if (parentFile.isDirectory()) {
            files = parentFile.listFiles();
        } else {
            files = new File[1];
            files[0] = parentFile;
        }
        String pathName;
        InputStream is;
        BufferedInputStream bis;
        byte[] cache = new byte[CACHE_SIZE];
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + File.separator;
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                is = new FileInputStream(file);
                bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                int nRead = 0;
                while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                    zos.write(cache, 0, nRead);
                }
                bis.close();
                is.close();
            }
        }
    }

    /**
     * 解压压缩包
     * @param zipFilePath 压缩文件路径
     * @param destDir 解压目录
     */
    public static void unZip(String zipFilePath, String destDir) {
        ZipFile zipFile = null;
        try {
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            zipFile = new ZipFile(zipFilePath, CHINESE_CHARSET);
            Enumeration<ZipEntry> zipEntries = zipFile.getEntries();
            File file, parentFile;
            ZipEntry entry;
            byte[] cache = new byte[CACHE_SIZE];
            while (zipEntries.hasMoreElements()) {
                entry = (ZipEntry) zipEntries.nextElement();
                if (entry.isDirectory()) {
                    new File(destDir + entry.getName()).mkdirs();
                    continue;
                }
                bis = new BufferedInputStream(zipFile.getInputStream(entry));
                file = new File(destDir + entry.getName());
                parentFile = file.getParentFile();
                if (parentFile != null && (!parentFile.exists())) {
                    parentFile.mkdirs();
                }
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, CACHE_SIZE);
                int readIndex = 0;
                while ((readIndex = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                    fos.write(cache, 0, readIndex);
                }
                bos.flush();
                bos.close();
                fos.close();
                bis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
