package com.ligootech.pos.web.util;

public class AlarmUtil {
	public static String getAlarmTypeName(int alarmId){
		if (alarmId == 1)
			return "漏电	";
		if (alarmId == 2)
			return "主从机通信失败";
		if (alarmId == 3)
			return "过温";
		if (alarmId == 4)
			return "过放";
		if (alarmId == 5)
			return "过充";
		if (alarmId == 6)
			return "SOC过低";
		if (alarmId == 7)
			return "SOC过高";
		if (alarmId == 8)
			return "过流";
		if (alarmId == 9)
			return "温差过大";
		if (alarmId == 10)
			return "压差过大";
		if (alarmId == 11)
			return "电压检测异常";
		if (alarmId == 12)
			return "温度检测异常";
		if (alarmId == 13)
			return "总压过高";
		if (alarmId == 14)
			return "总压过低";
		return "未知";
	}
}
