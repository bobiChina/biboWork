package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.core.HardwareVersion;
import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wly on 2015/11/10 11:55.
 */
public interface HardwareVersionDao extends BaseJpaRepository<HardwareVersion, Long>, JpaSpecificationExecutor<HardwareVersion> {

    @Query(" from HardwareVersion hwv where hwv.status=1 and hwv.productType.id=:prodId order by id desc")
    public List<HardwareVersion> findHWList(@Param("prodId") Long prodId);

}
