<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
  <meta name="menu" content="login"/>
  <title>订单详情</title>
  <link rel="stylesheet" href="${ctx}/static/styles/css.css" />
  <link rel="stylesheet" href="${ctx}/static/style/jquery.mCustomScrollbar.min.css" />

  <script src="${ctx}/static/js/ligao.global.js"></script>

  <script type="text/javascript">
    $(function(){
      <c:if test="${empty order}">
        alert("订单获取失败，或者权限不足");
      </c:if>
      <c:if test="${not empty order}">
      //orderNoChange();
      </c:if>
    });
  </script>

</head>
<body>
<input type="hidden" id="dtuUuid" value="${uuid}"/>
<input type="hidden" id="bmuindex" value="0"/>
<div class="body-outer dclear clearfix">
  <h2 class="preport-head-title">订单详情</h2>
  <div class="map-outer report-box-cart-outer clearfix">
    <div class="order-info dleft">
      <div class="order-info-head dleft">
        <h3 >基本信息</h3>
      </div>
      <div class="order-info-con" align="center">
        <table>
          <tr>
            <td class="map-report-table-title">合同编号：</td>
            <td>${order.contractNo}</td>
          </tr>
          <tr>
            <td class="map-report-table-title">订单编号：</td>
            <td>${order.orderno}</td>
          </tr>
          <tr>
            <td class="map-report-table-title">客户名称：</td>
            <td>${order.corp.corpName}</td>
          </tr>
          <tr>
            <td class="map-report-table-title">项目描述：</td>
            <td>${order.projectNote}</td>
          </tr>
          <c:if test="${order.orderType == 1}">
            <tr>
              <td class="map-report-table-title">订货数量：</td>
              <td>${order.quantity} 套</td>
            </tr>
          </c:if>
          <c:if test="${order.orderType > 1}">
            <tr>
              <td class="map-report-table-title">原始订单：</td>
              <td><a target='_blank' href='${ctx}/orderService/detail?orderno=${order.boundOrder}'>${order.boundOrder}</a></td>
            </tr>
          </c:if>
          <tr>
            <td class="map-report-table-title">销售代表：</td>
            <td>${salesman_show}</td>
          </tr>
        </table>
      </div>
      <div class="order-info-footer"></div>
    </div>

    <div class="order-info dright">
      <div class="order-info-head">
        <h3>车辆信息</h3>
      </div>
      <div class="order-info-con">
        <table>
          <tr>
            <td class="map-report-table-title">车辆类型：</td>
            <td>${order.vehicleType.typeName}</td>
          </tr>
          <tr>
            <td class="map-report-table-title">车辆型号：</td>
            <td>${order.vehicleModel.modelName}</td>
          </tr>
          <tr>
            <td class="map-report-table-title">电池厂商：</td>
            <td>${order.batteryModel.factoryName}</td>
          </tr>
          <tr>
            <td class="map-report-table-title">电池类型：</td>
            <td>${order.batteryModel.batteryType.typeName}</td>
          </tr>
          <tr>
            <td class="map-report-table-title">电池串数：</td>
            <td>${order.batteryModel.batteryNumber} 串</td>
          </tr>
          <tr>
            <td class="map-report-table-title">电池容量：</td>
            <td>${order.batteryModel.capacity} AH</td>
          </tr>
        </table>
      </div>
      <div class="order-info-footer-right"></div>
    </div>
  </div>

  <div class="map-outer report-cart-info dclear">
    <div class="map-box-head">
      <h3 class="dleft">系统配置</h3>
      <a href="${ctx}/orderService/downloadOrderZip?orderno=${order.orderno}" class="dright" style="height: 52px; line-height:52px;margin-right: 10px; ">
        <img src="${ctx}/static/images/downloads.png" width="50" style="margin-top: 7px;" title="软件下载" alt="软件下载" />
      </a>
    </div>
    <div class="reportbox-wrap reportbox-table-wrap dclear">
      <div class="reportbox-cart-head-readtime-top">
        <table class="reportbox-table reportbox-table-bg" style="margin-bottom: 8px;">
          <thead>
          <th style="width: 50px; text-align: center;">序号</th>
          <th style="width: 100px;">设备类型</th>
          <th style="width: 100px;">设备型号</th>
          <th style="width: 100px;">硬件版本</th>
          <th style="width: 100px;">软件版本</th>
          <th style="width: 100px;">ID编号</th>
          <th style="width: 100px;">订单数量</th>
          <th style="width: 120px;">管理员</th>
          <th width="*">更新日期</th>
          </thead>
          <tbody>
          <c:forEach items="${deviceList}" var="deviceObj" varStatus="status">
            <tr>
              <td style="width: 50px; background: #FFF;">${status.count}</td>
              <td style="width: 100px; background: #FFF;">${deviceObj[0]}</td>
              <td style="width: 100px; background: #FFF;">${deviceObj[1]}</td>
              <c:if test="${deviceObj[0] == 'BCU' || deviceObj[0] == 'BYU'}">
                <td style="width: 100px; background: #FFF;">
                  <c:if test="${empty hwVersionMap[deviceObj[1]]}">-</c:if>
                  <c:if test="${not empty hwVersionMap[deviceObj[1]]}">${hwVersionMap[deviceObj[1]]}</c:if>
                </td>
              </c:if>
              <c:if test="${deviceObj[0] != 'BCU' && deviceObj[0] != 'BYU'}">
                <td style="width: 100px; background: #FFF;">-</td>
              </c:if>

              <c:if test="${deviceObj[0] == 'BCU' || deviceObj[0] == 'BYU'}">
                <td style="width: 100px; background: #FFF;">
                  <c:if test="${empty swVersionMap[deviceObj[1]]}">-</c:if>
                  <c:if test="${not empty swVersionMap[deviceObj[1]]}">${swVersionMap[deviceObj[1]]}</c:if>
                </td>
              </c:if>
              <c:if test="${deviceObj[0] != 'BCU' && deviceObj[0] != 'BYU'}">
                <td style="width: 100px; background: #FFF;">-</td>
              </c:if>
              <td style="width: 100px; background: #FFF;">${deviceObj[2]}</td>
              <td style="width: 100px; background: #FFF;">${deviceObj[3]}</td>
              <td style="width: 120px; background: #FFF;">${deviceObj[4]}</td>
              <td width="*" style="text-align: center; background: #FFF;">${deviceObj[5]}</td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
    <div class="dianchi-footer dianchi-footer-position cart-readtime-footer"></div>
  </div>

  <div class="map-outer report-cart-info dclear">
    <div class="map-box-head">
      <h3 class="dleft">订单日志</h3>
    </div>
    <div class="reportbox-wrap reportbox-table-wrap dclear">
      <div class="reportbox-cart-head-readtime-top">
        <table class="reportbox-table reportbox-table-bg" style="margin-bottom: 8px;">
          <thead>
          <th style="width: 114px;">事件日期</th>
          <th style="width: 114px;">事件</th>
          <th style="width: 114px;">订单状态</th>
          <th style="width: 114px;">预计交期</th>
          <th width="*">更新内容</th>
          </thead>
           <tbody>
           <c:forEach items="${trackLogList}" var="trackLog">
             <tr>
               <td style="width: 149px; background: #FFF;">${trackLog.action_date}</td>
               <td style="width: 114px; background: #FFF;">${trackLog.action_name}</td>
               <td style="width: 114px; background: #FFF;">${trackLog.order_status}</td>
               <td style="width: 114px; background: #FFF;">${trackLog.review_delivery}</td>
               <td width="*" style="text-align: left;padding-left: 5px; background: #FFF;">${trackLog.remarks}</td>
             </tr>
           </c:forEach>
           </tbody>
        </table>
      </div>
    </div>
    <div class="dianchi-footer dianchi-footer-position cart-readtime-footer"></div>
  </div>
  <c:if test="${order.orderType == 1}">
    <div class="map-outer report-cart-info dclear">
      <div class="map-box-head">
        <h3 class="dleft">售后服务</h3>
      </div>
      <div class="reportbox-wrap reportbox-table-wrap dclear">
        <div class="reportbox-cart-head-readtime-top">
          <table class="reportbox-table reportbox-table-bg" style="margin-bottom: 8px;">
            <thead>
            <th style="width: 50px;">序号</th>
            <th style="width: 140px;">售后订单编号</th>
            <th style="width: 114px;">售后类型</th>
            <th style="width: 134px;">申请日期</th>
            <th width="*">更改原因</th>
            </thead>
            <tbody>
            <c:forEach items="${shList}" var="shObj" varStatus="status">
              <tr>
                <td style="width: 50px; background: #FFF;">${status.count}</td>
                <td style="width: 140px; background: #FFF;"><a target='_blank' href='${ctx}/orderService/detail?orderno=${shObj.orderno}'>${shObj.orderno}</a></td>
                <td style="width: 114px; background: #FFF;">${shObj.order_type}</td>
                <td style="width: 134px; background: #FFF;">${shObj.opt_time}</td>
                <td width="*" style="text-align: left;padding-left: 5px; background: #FFF;">${shObj.order_note}</td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
      <div class="dianchi-footer dianchi-footer-position cart-readtime-footer"></div>
    </div>
  </c:if>

    </div> <!-- //body outer end -->


</body>
</html>