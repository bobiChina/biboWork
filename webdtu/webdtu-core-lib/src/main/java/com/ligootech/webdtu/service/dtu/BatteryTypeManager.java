package com.ligootech.webdtu.service.dtu;

import com.ligootech.webdtu.entity.core.BatteryType;
import org.appdot.service.GenericManager;

import java.util.List;

/**
 * Created by wly on 2015/9/22 10:59.
 */
public interface BatteryTypeManager extends GenericManager<BatteryType, Long>

{
    List<BatteryType> getBatteryTypeList();
}
