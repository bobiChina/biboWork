package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.core.SNScan;
import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wly on 2015/10/27 10:07.
 */
public interface SNScanDao extends BaseJpaRepository<SNScan, Long>, JpaSpecificationExecutor<SNScan> {

    @Query("select id,orderno,status from SNScan where status=1 and orderno = :orderno order by id")
    public List<Object[]> findSNListByOrderNo(@Param("orderno") String orderno);

    /**
     * 未使用状态的扫描记录
     * @param orderno
     * @return
     */
    @Query(" from SNScan where status=0 and orderno = :orderno order by id")
    public List<SNScan> findSNAllListByOrderNo(@Param("orderno") String orderno);

}
