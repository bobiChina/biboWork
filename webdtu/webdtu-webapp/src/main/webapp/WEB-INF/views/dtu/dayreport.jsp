<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="count" />
<title>日报</title>
<meta name="menu" content="login" />
<title>月报</title>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<script src="${ctx}/static/js/charts/echarts-plain.js"></script>
<script type="text/javascript">
$(function(){
	getDayInfo();
});

function getDayInfo(){
	$.post("${ctx}/count/dayReportAjax", {date:"${today}"},function(data){
		//alert(data);
		var rsObj = eval('(' + data + ')');
		$("#vehicleCount").html(rsObj.newVehicleCount + '&nbsp;/&nbsp;' + rsObj.allVehicleCount);
		$("#alarm").html('${alarmToday}&nbsp;/&nbsp;' + rsObj.alarmAll);
		$("#vehicleTime").html(rsObj.newVehicleTime + '&nbsp;/&nbsp;' + rsObj.allTime + '&nbsp;h');
		$("#vehicleMilege").html(rsObj.newVehicleMilege + '&nbsp;/&nbsp;' + rsObj.allMilege + '&nbsp;Km');
    });
}


function preview(oper)
{
	if (oper < 10){
		bdhtml=window.document.body.innerHTML;//获取当前页的html代码
		sprnstr="<!--startprint"+oper+"-->";//设置打印开始区域
		eprnstr="<!--endprint"+oper+"-->";//设置打印结束区域
		prnhtml=bdhtml.substring(bdhtml.indexOf(sprnstr)+18); //从开始代码向后取html
		
		prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));//从结束代码向前取html
		window.document.body.innerHTML=prnhtml;
		window.print();
		window.document.body.innerHTML=bdhtml;
	} else {
		window.print();
	}
}
</script>
</head>
<body>	
	<div class="report-head">
		<div class="report-logo dleft">
			<img src="${ctx}/static/images/report-logo.gif" alt="" />
			<h2>日报表 (${today})</h2>
		</div>

		<div class="report-user-info dright">
			<ul>
				<li>用户名:  ${dtuUser.fullName}</li>
				<li>公&emsp;司:  ${dtuUser.corp.corpName}</li>
				<li>电&emsp;话:  ${dtuUser.corp.phone}</li>
				<li>邮&emsp;件:  ${dtuUser.email}</li>
			</ul>
		</div>
	</div> <!-- //Report head end -->

	<div class="body-outer dclear clearfix">
		<div class="table-box dclear clearfix">
			<div class="map-box-head">
				<h3 class="dleft">总览</h3>
			</div>

			<div class="overview-outer clearfix dclear">
				<div class="overview-content">
					<div class="overview-info overview-info-line dleft">
						<ul>
							<li><span class="overview-cart-title">今日新增上线车辆&nbsp;/&nbsp;车辆总数：</span><span class="overview-cart-total" id="vehicleCount"></span></li>
							<li><span class="overview-cart-title">今日故障数&nbsp;/&nbsp;故障次数总计：</span><span class="overview-cart-total" id="alarm"></span></li>
						</ul>
					</div>
					
					<div class="overview-info overview-dright dleft">
						<ul>
							<li><span class="overview-cart-title">今日新增运行时长&nbsp;/&nbsp;总时长：</span><span class="overview-cart-total" id="vehicleTime"></span></li>
							<li><span class="overview-cart-title">今日新增运行里程&nbsp;/&nbsp;总里程：</span><span class="overview-cart-total" id="vehicleMilege"></span></li>
						</ul>
					</div>	
				</div>
			</div>

			<div class="overview-footer dclear"></div>
		</div> <!-- //总览 -->
		
		<div class="map-outer report-table-outer">
			<div class="map-box-head">
				<h3 class="dleft">今日故障</h3>
			</div>
			<div class="reportbox-wrap reportbox-table-wrap dclear">
				<table class="reportbox-table reportbox-table-bg dclear">
					<thead>
						<tr>
							<th class="report-td-first"></th>
						    <th>UUID</th>
						    <th>车架号</th>
						    <th>车辆类型</th>
						    <th>电池厂商</th>
						    <th>故障类型</th>
						    <th>开始时间</th>
						    <th>修复时间</th>
						    
						</tr>
					</thead>
					<tbody>
					  	<c:forEach var="alarm" items="${alarmListToday}" varStatus="xh">
						  <tr>
						  	<td class="report-td-first">${xh.count}</td>
						  	<td>${alarm.uuid}</td>
						  	<td>${alarm.vehicleNumber}</td>
						  	<td>${alarm.typeName}</td>
						  	<td>${alarm.factoryName}</td>
						  	<td>${alarm.alarmTypeName}</td>
						  	<td>${alarm.startTime}</td>
						  	<td>${alarm.endTime}</td>
						  	
						  </tr>
					  	</c:forEach>					
					</tbody>
				</table>
			</div>
			<div class="dianchi-footer dianchi-footer-position"></div>
		</div>
		
		<div class="map-outer report-table-outer">
			<div class="map-box-head">
				<h3 class="dleft">今日新增</h3>
			</div>
			<div class="reportbox-wrap reportbox-table-wrap dclear">
				<table class="reportbox-table reportbox-table-bg dclear">
					<thead>
						<tr>
							<th class="report-td-first"></th>
						    <th>UUID</th>
						    <th>车架号</th>
						    <th>车辆类型</th>
						    <th>车辆型号</th>
						    <th>电池厂商</th>
						    <th>电池类型</th>
						    <th>电池串数</th>
						    <th>额定容量</th>
						</tr>
					</thead>
					<tbody>
					  	<c:forEach var="vechile" items="${newVehicleList}" varStatus="xh">
						  <tr>
						  	<td class="report-td-first">${xh.count}</td>
						  	<td>${vechile[0]}</td>
						  	<td>${vechile[2]}</td>
						    <td>${vechile[1]}</td>
						    <td>${vechile[6]}</td>
						    <td>${vechile[3]}</td>
						    <td>
						    <c:if test="${vechile[7] == 1}">
						    	钛酸锂
						    </c:if>
						    <c:if test="${vechile[7] == 2}">
						    	磷酸铁锂
						    </c:if>
						    <c:if test="${vechile[7] == 3}">
						    	锰酸锂
						    </c:if>
						    <c:if test="${vechile[7] == 4}">
						    	三元材料
						    </c:if>
						    <c:if test="${vechile[7] == 5}">
						    	铅酸电池
						    </c:if>
						    <c:if test="${vechile[7] == 6}">
						    	镍氢镍铬
						    </c:if>
						    <c:if test="${vechile[7] == 7}">
						    	锂电池
						    </c:if>						    						    						    						    						    
						    </td>
						    <td>${vechile[8]}</td>
						    <td><fmt:formatNumber value="${vechile[4]}" pattern="0.0"/></td>
						  </tr>
					  	</c:forEach>					
					</tbody>
				</table>
			</div>
			<div class="dianchi-footer dianchi-footer-position"></div>
		</div>		
	</div> <!-- //body outer end -->
	

<!--startprint1-->

<!--endprint1-->
</body>
</html>