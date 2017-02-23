package com.ligootech.webdtu.util;
  
import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.File;
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.nio.MappedByteBuffer;  
import java.nio.channels.FileChannel;  
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  
public class MD5Util {  
    /** 
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合 
     */  
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    //1~9用数字表示，后面依次使用A、B、C，注意字母“I”、字母“O”、字母“q”不使用
    protected static char checkArr[] = {'0', '1', '2', '3', '4', '5', '6','7', '8', '9',
                                          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
                                          'L', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W',
                                          'X', 'Y'};
  
    protected static MessageDigest messagedigest = null;  
    static {  
        try {  
            messagedigest = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException nsaex) {  
            System.err.println(MD5Util.class.getName()  
                    + "初始化失败，MessageDigest不支持MD5Util。");  
            nsaex.printStackTrace();  
        }  
    }  
      
    /** 
     * 生成字符串的md5校验值 
     *  
     * @param s 
     * @return 
     */  
    public static String getMD5String(String s) {  
        return getMD5String(s.getBytes());  
    }  
      
    /** 
     * 判断字符串的md5校验码是否与一个已知的md5码相匹配 
     *  
     * @param password 要校验的字符串 
     * @param md5PwdStr 已知的md5校验码 
     * @return 
     */  
    public static boolean checkPassword(String password, String md5PwdStr) {  
        String s = getMD5String(password);  
        return s.equals(md5PwdStr);  
    }  
      
    /** 
     * 生成文件的md5校验值 
     *  
     * @param file 
     * @return 
     * @throws IOException 
     */  
    public static String getFileMD5String(File file) throws IOException {         
        InputStream fis;  
        fis = new FileInputStream(file);  
        byte[] buffer = new byte[1024];  
        int numRead = 0;  
        while ((numRead = fis.read(buffer)) > 0) {  
            messagedigest.update(buffer, 0, numRead);  
        }  
        fis.close();  
        return bufferToHex(messagedigest.digest());  
    }

    /**
     * 改造文件MD5验证码
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = inputStream.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        //fis.close();
        return bufferToHex(messagedigest.digest());
    }
  
    /** 
     * JDK1.4中不支持以MappedByteBuffer类型为参数update方法，并且网上有讨论要慎用MappedByteBuffer， 
     * 原因是当使用 FileChannel.map 方法时，MappedByteBuffer 已经在系统内占用了一个句柄， 
     * 而使用 FileChannel.close 方法是无法释放这个句柄的，且FileChannel有没有提供类似 unmap 的方法， 
     * 因此会出现无法删除文件的情况。 
     *  
     * 不推荐使用 
     *  
     * @param file 
     * @return 
     * @throws IOException 
     */  
    public static String getFileMD5String_old(File file) throws IOException {  
        FileInputStream in = new FileInputStream(file);  
        FileChannel ch = in.getChannel();  
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,  
                file.length());  
        messagedigest.update(byteBuffer);  
        return bufferToHex(messagedigest.digest());  
    }  
  
    public static String getMD5String(byte[] bytes) {  
        messagedigest.update(bytes);  
        return bufferToHex(messagedigest.digest());  
    }  
  
    private static String bufferToHex(byte bytes[]) {  
        return bufferToHex(bytes, 0, bytes.length);  
    }  
  
    private static String bufferToHex(byte bytes[], int m, int n) {  
        StringBuffer stringbuffer = new StringBuffer(2 * n);  
        int k = m + n;  
        for (int l = m; l < k; l++) {  
            appendHexPair(bytes[l], stringbuffer);  
        }  
        return stringbuffer.toString();  
    }  
  
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同   
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换   
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    }  
      
    /***************************
     * shiro MD5加密
     ***************************/
    /**
     * 只要这个salt不被泄露出去，原则上加密之后是无法被解密的
     * @param str
     * @param salt
     * @return
     */
    public static String shiroMD5(String str,String salt){
        return new Md5Hash(str,salt).toString();
    }

    public static String ligooShiroMD5(String str){
        return shiroMD5(str, "popLigoo");
    }

    /**
     * 先用shiro的MD5加密算法加密，再用CRC循环冗余校验法算出校验码
     * @param str
     * @return
     */
    public static char getLigooCRC(String str){
        String md5 = ligooShiroMD5(str);
        short crc = crc16(md5.getBytes());

        int index = Math.abs(crc % 32);
        char rs = checkArr[index];
        return rs;
    }

    /**
     * java实现 循环冗余校验（CRC）算法         http://ask.csdn.net/questions/171325
     crcjava算法循环冗余校验
     算法的要求是如下：
     ① 装一个16 位寄存器，所有数位均为1。
     ② 取被校验串的一个字节与16 位寄存器的高位字节进行“异或（^）”运算。运算结果放入这个16 位寄存器。
     ③ 把这个16 寄存器向右移一位。
     ④ 若向右（标记位）移出的数位是1，则生成多项式1010 0000 0000 0001 和这个寄存器进行“异或”运算；若向右移出的数位是0，则返回③。
     ⑤ 重复③和④，直至移出8 位。
     ⑥ 取被校验串的下一个字节
     ⑦ 重复③~⑥，直至被校验串的所有字节均与16 位寄存器进行“异或（^）”运算，并移位 8 次。
     ⑧ 这个16 位寄存器的内容即2 字节CRC 错误校验码。
     校验码按照先高字节后低字节的顺序存放。
     * @param data
     * @return
     */
    public static short crc16(byte[] data) {
        short crc = (short) 0xFFFF;
        short dxs = (short) 0xA001;
        byte tc;
        byte sbit;
        for (int i = 0; i < data.length; i++) {
            tc = (byte) ((crc & 0xff00) >> 8);
            crc = (short) (tc ^ data[i]);
            for (int r = 0; r < 8; r++) {
                sbit = (byte) (crc & 0x01);
                crc = (short) (crc >> 1);
                if (sbit != 0)
                    crc = (short) (crc ^ dxs);
            }
        }

        /*
        System.out.println(bytesToHexString(new byte[] {
                (byte) ((crc & 0xff00) >> 8), (byte) (crc & 0xff) }));
        */
        return crc;
    }

    //将字节数组按16进制输出
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);

            if (stringBuilder.length() != 0) {
                stringBuilder.append(",");
            }
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }






    //=========================================================================================================
    public static void main_bak(String[] args) throws IOException {
        long begin = System.currentTimeMillis();

        String filePath = "D:/var/uploadDir/201603011712539655696561790.zip";
        //filePath = "D:/var/uploadDir/201603011657155663496650323.zip";
        filePath = "D:/var/uploadDir/201603011712539655696561791.zip";
        filePath = "D:/var/uploadDir/201603011656505578262097706.zip";
        filePath = "D:/var/uploadDir/201604051535108801972776025.zip";
        filePath = "D:/var/uploadDir/2016040515503619480778471610.zip";
        filePath = "D:/var/uploadDir/201604051552599728524833544.zip";
        File file = new File(filePath);
        String md5 = getFileMD5String(file);

//      String md5 = getMD5String("a");     66a342ee992ed9bd7e6ed5c8b648c52f
        //                                  66a342ee992ed9bd7e6ed5c8b648c52f
        //                                  66a342ee992ed9bd7e6ed5c8b648c52f
        //                                  eb4e4f6397a117888d1df6698cf88a98

        //D:/var/uploadDir/201603011656505578262097706.zip	md5:8e56cd79e88ee6b853a519f91aa0ce3d time:0s
        //D:/var/uploadDir/201604051535108801972776025.zip	md5:ce1a85d7956c8a2876294177f6786cc5 time:0s
        //D:/var/uploadDir/2016040515503619480778471610.zip	md5:747feb1f53b2d39f8e2f166de97d082b time:0s
        //D:/var/uploadDir/201604051552599728524833544.zip	md5:d5003a822de9367c7bdb9f878c9d4fb8 time:0s


        long end = System.currentTimeMillis();
        System.out.println(filePath + "\tmd5:" + md5 + " time:" + ((end - begin) / 1000) + "s");

    }

    public static void main(String[] args){
        //String str1 = shiroMD5("jack", "ligooweb");
        //String str2 = shiroMD5("jack", "ligoopos");
        //System.out.println(str1 + "---" + str2);
        //9d22fd8b99e21b86288f8997b3c1fe5d---
        //9d22fd8b99e21b86288f8997b3c1fe5d---7fd0df6081d0b3de05b3349d1e44b984
        //7fd0df6081d0b3de05b3349d1e44b984
//        System.out.println("CRC=" + crc16("7fd0df6081d0b3de".getBytes()));
//        System.out.println("CRC=" + crc16("10M61610".getBytes()));
//        short st = crc16("10M61610".getBytes());
//        System.out.println(Math.abs(st%32));

        System.out.println(ligooShiroMD5("uuid"));
        System.out.println(ligooShiroMD5("uuid"));
    }

}  