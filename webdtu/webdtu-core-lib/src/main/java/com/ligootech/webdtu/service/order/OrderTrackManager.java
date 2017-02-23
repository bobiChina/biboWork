package com.ligootech.webdtu.service.order;

import com.ligootech.webdtu.entity.core.OrderTrack;
import com.ligootech.webdtu.util.EasyUiUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/12.
 */
public interface OrderTrackManager {

    /**
     * 获取订单跟踪日志
     * @param orderNo
     * @return
     */
    public List<OrderTrack> findTrackByOrderNo(String orderNo);

    /**
     * 保存跟踪日志
     * @param orderTrack
     * @return
     */
    public Long saveOrderTrack(OrderTrack orderTrack);

    /**
     * 订单列表信息
     * @param orderNo
     * @return
     */
    public EasyUiUtil.PageForData findEasyUiList(int rows, int page, String sort, String order, String orderNo);

    /**
     * 详细信息
     * @param id
     * @return
     */
    public OrderTrack findObjById(Long id);

    /**
     * 通过订单获取最新订单日志
     * @param orderNo
     * @return
     */
    public OrderTrack findNewTrackByOrderNo(String orderNo);

    /**
     * 删除
     * @param orderNo
     * @param id
     * @return
     */
    public int delOrderTrackById(String orderNo, Long id);

    /**
     * 获取公司名称
     * @return
     */
    public List<Object[]> findOrderCorp();

    /**
     * 获取订单销售代表
     * @return
     */
    public List<Object[]> findOrderSalesman();

    /**
     * 获取订单所有日志信息
     * @param orderNo
     * @return
     */
    public List<Map<String, Object>> findOrderTrackByOrderNo(String orderNo);

}
