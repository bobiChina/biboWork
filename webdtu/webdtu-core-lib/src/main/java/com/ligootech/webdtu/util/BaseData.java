package com.ligootech.webdtu.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/16.
 */
public class BaseData {
    public static final Map<String, String> deviceMap = new HashMap<String, String>();

    static{
        /*****************************
         * 程序解析为：BC52A
         ****************************/
        deviceMap.put("BC52A", "BC52A");
        deviceMap.put("BY3112A", "BC52A");
        deviceMap.put("BY3124A", "BC52A");
        deviceMap.put("BY3136A", "BC52A");
        deviceMap.put("BY3148A", "BC52A");
        deviceMap.put("BY3160A", "BC52A");

        /*****************************
         * 程序解析为：BC5XB
         ****************************/
        deviceMap.put("BC52B", "BC5XB");
        deviceMap.put("BY5212B", "BC5XB");
        deviceMap.put("BY5224B", "BC5XB");
        deviceMap.put("BY5236B", "BC5XB");
        deviceMap.put("BY5248B", "BC5XB");
        deviceMap.put("BY5212BT", "BC5XB");
        deviceMap.put("BY5224BT", "BC5XB");
        deviceMap.put("BY5236BT", "BC5XB");
        deviceMap.put("BC54B", "BC5XB");
        deviceMap.put("BY5412B", "BC5XB");
        deviceMap.put("BY5424B", "BC5XB");
        deviceMap.put("BY5436B", "BC5XB");
        deviceMap.put("BY5448B", "BC5XB");
        deviceMap.put("BY5412BT", "BC5XB");
        deviceMap.put("BY5424BT", "BC5XB");
        deviceMap.put("BY5436BT", "BC5XB");

        /*****************************
         * 程序解析可能为：BM51A 与 M51(2)XX 有共存
         ****************************/
        deviceMap.put("BM51A", "BM51A");
        deviceMap.put("BM5112A", "BM51A");
        deviceMap.put("BM5124A", "BM51A");
        deviceMap.put("BM5136A", "BM51A");
        deviceMap.put("BM5148A", "BM51A");
        deviceMap.put("BM5160A", "BM51A");
        deviceMap.put("BM5112AT", "BM51A");
        deviceMap.put("BM5124AT", "BM51A");
        deviceMap.put("BM5136AT", "BM51A");
        deviceMap.put("BM5148AT", "BM51A");

        /*****************************
         * 程序解析为：BM51B
         ****************************/
        deviceMap.put("BM51B", "BM51B");
        deviceMap.put("BM5124B", "BM51B");
        deviceMap.put("BM5136B", "BM51B");
        deviceMap.put("BM5148B", "BM51B");
        deviceMap.put("BM5160B", "BM51B");
        deviceMap.put("BM5124BT", "BM51B");
        deviceMap.put("BM5148BT", "BM51B");

        /*****************************
         * 程序解析为：LDM 2016年4月20日 14:23:08 修改为 BL51A
         ****************************/
        deviceMap.put("BL51A", "BL51A");

        /*****************************
         * 程序解析为：BL51B
         ****************************/
        deviceMap.put("BL51B", "BL51B");

        /*****************************
         * 程序解析为：M1216
         ****************************/
        deviceMap.put("M1216", "M1216");

        /*****************************
         * 程序解析为：M1112
         ****************************/
        deviceMap.put("M1112", "M1112");

        /*****************************
         * 程序解析为：BD51A
         ****************************/
        deviceMap.put("BD51A", "BD51A");

        /*****************************
         * 程序解析可能为：C11，BCU
         ****************************/
        deviceMap.put("C11", "C11");

        /*****************************
         * 程序解析可能为：C11B, BCU
         ****************************/
        deviceMap.put("C11B", "C11B");
    }

}
