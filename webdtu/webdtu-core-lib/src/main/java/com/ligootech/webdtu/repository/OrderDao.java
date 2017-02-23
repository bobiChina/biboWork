package com.ligootech.webdtu.repository;

import com.ligootech.webdtu.entity.core.Order;
import org.appdot.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDao extends BaseJpaRepository<Order, Long>, JpaSpecificationExecutor<Order>{

	@Query("from Order")
	public List<Order> geOrderMaps();

	@Query("select id,orderno,status from Order order by id desc")
	public List<Object[]> findSimpleOrderListAll();

	@Query("select id,orderno,status from Order where status<99 order by id desc")
	public List<Object[]> findSimpleOrderList();

	@Query("from Order ode where ode.status<99 and ode.orderno=:orderno")
	public List<Order> findOrderByOrderNo(@Param("orderno") String orderno);

	@Query("from Order ode where ode.status<99 and ode.orderno=:orderno and ode.salesman=:salesman")
	public List<Order> findOrderByOrderNo(@Param("orderno") String orderno, @Param("salesman") String salesman);

	@Query("from Order ode where ode.status<99 and ode.corp.id=:corpid order by id desc ")
	public List<Order> findOrderByCorpid(@Param("corpid") Long corpid);

	@Query("from Order ode where ode.status<99 order by id desc ")
	public List<Order> findOrder4Page();

}
