package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.core.BatteryType;
import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by wly on 2015/9/22 11:33.
 */
public interface BatteryTypeDao extends BaseJpaRepository<BatteryType, Long>, JpaSpecificationExecutor<BatteryType> {
}
