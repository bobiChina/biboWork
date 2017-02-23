package com.ligootech.webdtu.service.impl.dtu;

import com.ligootech.webdtu.entity.core.BatteryType;
import com.ligootech.webdtu.repository.BatteryTypeDao;
import com.ligootech.webdtu.service.dtu.BatteryTypeManager;
import org.appdot.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wly on 2015/9/22 11:15.
 */
@Service("batteryTypeManager")
public class BatteryTypeManagerImpl extends GenericManagerImpl<BatteryType, Long> implements BatteryTypeManager {

    @Autowired
    private BatteryTypeDao batteryTypeDao;
    @Autowired
    public void setBatteryTypeDao(BatteryTypeDao batteryTypeDao) {
        super.dao = batteryTypeDao;
        this.batteryTypeDao = batteryTypeDao;
    }

    @Override
    public List<BatteryType> getBatteryTypeList() {
        return null;
    }

}
