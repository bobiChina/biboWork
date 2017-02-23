package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.core.SNInfo;
import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wly on 2015/10/27 10:07.
 */
public interface SNInfoDao extends BaseJpaRepository<SNInfo, Long>, JpaSpecificationExecutor<SNInfo> {

    @Query("select id,orderno,status from SNInfo where status=1 and orderno = :orderno order by id")
    public List<Object[]> findSNListByOrderNo(@Param("orderno") String orderno);

    @Query(" from SNInfo where status=1 and orderno = :orderno order by id")
    public List<SNInfo> findSNAllListByOrderNo(@Param("orderno") String orderno);

    @Query(" from SNInfo where status=1 and sn = :sn order by id desc")
    public List<SNInfo> findSNAllListByPSN(@Param("sn") String sn);

    //uuid 为0的不能生成DTU
    @Query(" from SNInfo as sn where sn.dtuUuid not in (select d.uuid from Dtu as d ) and sn.uuid<>'00000000-0000-0000-0000-000000000000' and sn.status=1 and sn.prodType.isMainframe=1 and sn.orderno = :orderno  order by id")
    public List<SNInfo> findSNNoDtuByOrderNo(@Param("orderno") String orderno);

}
