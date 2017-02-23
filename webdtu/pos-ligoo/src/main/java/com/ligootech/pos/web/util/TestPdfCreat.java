package com.ligootech.pos.web.util;

/**
 * Created by wly on 2016/2/19 15:00.
 */
public class TestPdfCreat {

    public static void main(String[] arges){
        System.out.println("中文输出");
        String str = "{" +
                "\"user_id\": \"4044\"," +
                "\"order_no\": \"LG201606270201\"," +
                "\"tool_id\": \"1\"," +
                "\"total_result\": \"false\"," +
                "\"uuid\": \"6C0BC2FD960C45C9BD107DD705B1A560\"," +
                "\"name\": \"BC52B\"," +
                "\"devicetype\": \"1\"," +
                "\"bmu_id\": \"0\"," +
                "\"sn\": \"2115CN3A2200005\"," +
                "\"soft_version\": \"1.0.2.0/2001.3.2.3\"," +
                "\"report_type\": \"0\"," +
                "\"lists\": [{" +
                "\"check_item\": \"Supply_24\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"open_24V_DET\": \"\"," +
                "\"open_24V_1\": \"\"," +
                "\"open_24V_2\": \"\"," +
                "\"open_24V_3\": \"\"," +
                "\"open_5V_HALL\": \"\"," +
                "\"close_24V_DET\": \"\"," +
                "\"close_24V_1\": \"\"," +
                "\"close_24V_2\": \"\"," +
                "\"close_24V_3\": \"\"," +
                "\"close_5V_HALL\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Supply_BAT\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BAT_OUT_DET\": \"12165\"," +
                "\"BAT_24V_1_DET\": \"12165\"," +
                "\"BAT_24V_2_DET\": \"12165\"," +
                "\"BAT_24V_3_DET\": \"12165\"," +
                "\"BAT_5V_HALL\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Supply_KEY\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                "\"OPEN_24V_1_DET\": \"12165\"," +
                "\"OPEN_24V_2_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"OPEN_5V_HALL\": \"12165\"," +
                "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                "\"CLOSE_24V_1_DET\": \"12165\"," +
                "\"CLOSE_24V_2_DET\": \"12165\"," +
                "\"CLOSE_24V_3_DET\": \"12165\"," +
                "\"CLOSE_5V_HALL\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Supply_CHR\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                "\"OPEN_24V_1_DET\": \"12165\"," +
                "\"OPEN_24V_2_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"OPEN_5V_HALL\": \"12165\"," +
                "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                "\"CLOSE_24V_1_DET\": \"12165\"," +
                "\"CLOSE_24V_2_DET\": \"12165\"," +
                "\"CLOSE_24V_3_DET\": \"12165\"," +
                "\"CLOSE_5V_HALL\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Supply_SIG1\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                "\"OPEN_24V_1_DET\": \"12165\"," +
                "\"OPEN_24V_2_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"OPEN_5V_HALL\": \"12165\"," +
                "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                "\"CLOSE_24V_1_DET\": \"12165\"," +
                "\"CLOSE_24V_2_DET\": \"12165\"," +
                "\"CLOSE_24V_3_DET\": \"12165\"," +
                "\"CLOSE_5V_HALL\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Supply_CP\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                "\"OPEN_24V_1_DET\": \"12165\"," +
                "\"OPEN_24V_2_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"OPEN_5V_HALL\": \"12165\"," +
                "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                "\"CLOSE_24V_1_DET\": \"12165\"," +
                "\"CLOSE_24V_2_DET\": \"12165\"," +
                "\"CLOSE_24V_3_DET\": \"12165\"," +
                "\"CLOSE_5V_HALL\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Supply_LATCH\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                "\"OPEN_24V_1_DET\": \"12165\"," +
                "\"OPEN_24V_2_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"OPEN_5V_HALL\": \"12165\"," +
                "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                "\"CLOSE_24V_1_DET\": \"12165\"," +
                "\"CLOSE_24V_2_DET\": \"12165\"," +
                "\"CLOSE_24V_3_DET\": \"12165\"," +
                "\"CLOSE_5V_HALL\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Consum_9V\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CHAR_24V_OUT_DET\": \"12165\"," +
                "\"CHAR_24V_DET\": \"12165\"," +
                "\"CHAR_BATTERY_V\": \"12165\"," +
                "\"CHAR_CURRENT\": \"12165\"," +
                "\"CHAR_CONSUM\": \"12165\"," +
                "\"BAT_BAT_OUT_DET\": \"12165\"," +
                "\"BAT_24V_DET\": \"12165\"," +
                "\"BAT_BATTERY_V\": \"12165\"," +
                "\"BAT_DUT_CURRENT\": \"12165\"," +
                "\"BAT_CONSUM\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Consum_12V\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CHAR_24V_OUT_DET\": \"12165\"," +
                "\"CHAR_24V_DET\": \"12165\"," +
                "\"CHAR_BATTERY_V\": \"12165\"," +
                "\"CHAR_CURRENT\": \"12165\"," +
                "\"CHAR_CONSUM\": \"12165\"," +
                "\"BAT_BAT_OUT_DET\": \"12165\"," +
                "\"BAT_24V_DET\": \"12165\"," +
                "\"BAT_BATTERY_V\": \"12165\"," +
                "\"BAT_DUT_CURRENT\": \"12165\"," +
                "\"BAT_CONSUM\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Consum_36V\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CHAR_24V_OUT_DET\": \"12165\"," +
                "\"CHAR_24V_DET\": \"12165\"," +
                "\"CHAR_BATTERY_V\": \"12165\"," +
                "\"CHAR_CURRENT\": \"12165\"," +
                "\"CHAR_CONSUM\": \"12165\"," +
                "\"BAT_BAT_OUT_DET\": \"12165\"," +
                "\"BAT_24V_DET\": \"12165\"," +
                "\"BAT_BATTERY_V\": \"12165\"," +
                "\"BAT_DUT_CURRENT\": \"12165\"," +
                "\"BAT_CONSUM\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"Consum_48V\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CHAR_24V_OUT_DET\": \"12165\"," +
                "\"CHAR_24V_DET\": \"12165\"," +
                "\"CHAR_BATTERY_V\": \"12165\"," +
                "\"CHAR_CURRENT\": \"12165\"," +
                "\"CHAR_CONSUM\": \"12165\"," +
                "\"BAT_BAT_OUT_DET\": \"12165\"," +
                "\"BAT_24V_DET\": \"12165\"," +
                "\"BAT_BATTERY_V\": \"12165\"," +
                "\"BAT_DUT_CURRENT\": \"12165\"," +
                "\"BAT_CONSUM\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"USB\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"RS485\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"CAN1\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"CAN2\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"CAN3\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"NTC\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"NTC1\": \"12165\"," +
                "\"NTC2\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"CC\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"NTC1\": \"12165\"," +
                "\"NTC2\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_1000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_1500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_2000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_2500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_3000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_3500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_4000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_A_4500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_1000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_1500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_2000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_2500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_3000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_3500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_4000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HALL_B_4500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CURRENT_1\": \"12165\"," +
                "\"HALL_C1\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_50\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_100\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_200\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_300\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_400\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_500\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_600\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_700\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"HV_800\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"HV_TOOLING\": \"12165\"," +
                "\"PCH\": \"12165\"," +
                "\"PRE\": \"12165\"," +
                "\"BAT\": \"12165\"," +
                "\"INSUHV_ON_ON\": \"12165\"," +
                "\"INSUHV_OFF_ON\": \"12165\"," +
                "\"INSUHV_ON_OFF\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RELAY_1\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_RLY_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"CLOSE_RLY_DET\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RELAY_2\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_RLY_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"CLOSE_RLY_DET\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RELAY_3\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_RLY_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"CLOSE_RLY_DET\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RELAY_4\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_RLY_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"CLOSE_RLY_DET\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RELAY_5\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_RLY_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"CLOSE_RLY_DET\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RELAY_6\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_RLY_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"CLOSE_RLY_DET\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RELAY_7\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_RLY_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"CLOSE_RLY_DET\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RELAY_8\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_RLY_DET\": \"12165\"," +
                "\"OPEN_24V_3_DET\": \"12165\"," +
                "\"CLOSE_RLY_DET\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"IO\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"LOSE_POWER\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"SD_CARD\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"CP PWM\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"DUTY\": \"12165\"," +
                "\"CLOSE_CHR_EN_CP_V\": \"12165\"," +
                "\"OPEN_CHR_EN_CP_V\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RTC_TIME\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"RTC\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"RTC_ALARM\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"DTU\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"SIGNAL\": \"12165\"," +
                "\"CCID\": \"12165\"," +
                "\"IMEI\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"DO\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": null" +
                "}, {" +
                "\"check_item\": \"BMU_CON_9V\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"TOOL_BM51_DET\": \"12165\"," +
                "\"DUT_CURRENT\": \"12165\"," +
                "\"CONSUM\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_CON_12V\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"TOOL_BM51_DET\": \"12165\"," +
                "\"DUT_CURRENT\": \"12165\"," +
                "\"CONSUM\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_CON_36V\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"TOOL_BM51_DET\": \"12165\"," +
                "\"DUT_CURRENT\": \"12165\"," +
                "\"CONSUM\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_CON_48V\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"TOOL_BM51_DET\": \"12165\"," +
                "\"DUT_CURRENT\": \"12165\"," +
                "\"CONSUM\": \"12165\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_VOL_1000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_VOL_2000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_VOL_3000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_VOL_4000\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_VOL_4200\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(-25)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(-5)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(0)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(20)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(30)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(40)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(50)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(65)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(75)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_T(80)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_EXTEND_T(-40)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CHANNEL_1\": \"118460\"," +
                "\"CHANNEL_2\": \"118460\"," +
                "\"CHANNEL_3\": \"118460\"," +
                "\"CHANNEL_4\": \"118460\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_EXTEND_T(25)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CHANNEL_1\": \"118460\"," +
                "\"CHANNEL_2\": \"118460\"," +
                "\"CHANNEL_3\": \"118460\"," +
                "\"CHANNEL_4\": \"118460\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_EXTEND_T(55)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CHANNEL_1\": \"118460\"," +
                "\"CHANNEL_2\": \"118460\"," +
                "\"CHANNEL_3\": \"118460\"," +
                "\"CHANNEL_4\": \"118460\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_EXTEND_T(80)\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"CHANNEL_1\": \"118460\"," +
                "\"CHANNEL_2\": \"118460\"," +
                "\"CHANNEL_3\": \"118460\"," +
                "\"CHANNEL_4\": \"118460\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_1\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_2\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_3\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_4\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_5\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_6\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_7\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_8\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_9\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_10\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_11\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_OFF_12\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_1\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_2\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_3\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_4\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_5\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_6\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_7\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_8\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_9\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_10\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_11\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_BAL_ON_12\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"BOARD1\": \"12106\"," +
                "\"BOARD2\": \"12106\"," +
                "\"BOARD3\": \"12106\"," +
                "\"BOARD4\": \"12106\"," +
                "\"BOARD5\": \"12106\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_RELAY1\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_PTC_DET\": \"118460\"," +
                "\"OPEN_24V_BM51_DET\": \"118460\"," +
                "\"CLOSE_PTC_DET\": \"118460\"" +
                "}" +
                "}, {" +
                "\"check_item\": \"BMU_RELAY2\"," +
                "\"res\": \"unchecked\"," +
                "\"detail\": {" +
                "\"OPEN_FAN_DET\": \"118460\"," +
                "\"OPEN_24V_BM51_DET\": \"118460\"," +
                "\"CLOSE_FAN_DET\": \"118460\"" +
                "}" +
                "}]," +
                "\"files\": {" +
                "\"s19_test\": \"2015090211111\"," +
                "\"s19_order\": \"2015090211111\"," +
                "\"cfg\": \"2015090211111\"," +
                "\"csf\": \"2015090211111\"" +
                "}" +
                "}";
    }
}
