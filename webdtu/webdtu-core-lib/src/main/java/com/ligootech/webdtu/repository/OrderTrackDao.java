package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.core.OrderTrack;
import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public interface OrderTrackDao extends BaseJpaRepository<OrderTrack, Long>, JpaSpecificationExecutor<OrderTrack> {

    @Query(" from OrderTrack ot where ot.status=1 and ot.orderno=:orderno order by id desc")
    public List<OrderTrack> findOrderTrackByOrderNo(@Param("orderno") String orderno);

    @Query(" from OrderTrack ot where ot.status=1 and ot.orderno=:orderno and isNew=1")
    public OrderTrack findNewTrackByOrderNo(@Param("orderno") String orderno);
}
