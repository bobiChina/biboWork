package com.ligootech.webdtu.util;

import java.text.SimpleDateFormat;

/**
 * 时间格式
 * @author WLY
 * @日期 2013-5-24 下午3:38:43
 */
public class SimpleDateFormatConfig {
	
	private final SimpleDateFormat MMddYYYY_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final SimpleDateFormat MMddYYYY_HHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
	private final SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
	private final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat yyyyMMdd8 = new SimpleDateFormat("yyyyMMdd");
	private final SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
	private final SimpleDateFormat yyyyMM_CN = new SimpleDateFormat("yyyy年MM月");
	private final SimpleDateFormat yyyyMMddE = new SimpleDateFormat("yyyy年MM月dd日E");
	private final SimpleDateFormat yyyyMMddStr = new SimpleDateFormat("yyyy年MM月dd日");
	private final SimpleDateFormat yyyyMMddEStr = new SimpleDateFormat("yyyy-MM-dd E");
	private final SimpleDateFormat YYYYMMdd_HHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    
    private static SimpleDateFormatConfig simpleDateFormatConfig = new SimpleDateFormatConfig();
    
    private SimpleDateFormatConfig(){
    	
    }
    
    public static synchronized SimpleDateFormatConfig getInstance(){
    	return simpleDateFormatConfig;
    }

	public SimpleDateFormat getMMddYYYY_HHmmss() {
		return MMddYYYY_HHmmss;
	}

	public SimpleDateFormat getMMddYYYY_HHmm() {
		return MMddYYYY_HHmm;
	}

	public SimpleDateFormat getHHmmss() {
		return HHmmss;
	}

	public SimpleDateFormat getYyyy() {
		return yyyy;
	}

	public SimpleDateFormat getYyyyMMdd() {
		return yyyyMMdd;
	}

	public SimpleDateFormat getYyyyMMdd8() {
		return yyyyMMdd8;
	}

	public SimpleDateFormat getYyyyMM() {
		return yyyyMM;
	}

	public SimpleDateFormat getYyyyMM_CN() {
		return yyyyMM_CN;
	}

	public SimpleDateFormat getYyyyMMddE() {
		return yyyyMMddE;
	}

	public SimpleDateFormat getYyyyMMddStr() {
		return yyyyMMddStr;
	}

	public SimpleDateFormat getYyyyMMddEStr() {
		return yyyyMMddEStr;
	}

	public SimpleDateFormat getYYYYMMdd_HHmm() {
		return YYYYMMdd_HHmm;
	}

}
