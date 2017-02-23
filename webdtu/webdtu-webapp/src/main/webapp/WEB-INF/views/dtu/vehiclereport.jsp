<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<meta name="menu" content="login" />
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<script src="${ctx}/static/js/charts/echarts-all.js"></script>
	<title>整车报表</title>
</head>
<body>
	<div class="report-head">
		<div class="report-logo dleft">
			<img src="${ctx}/static/images/report-logo.gif" alt="" />
			<h2>整车报表 (${today})</h2>
		</div>

		<div class="report-user-info dright">
			<ul>
				<li>用户名:  ${vehicle.dtu.dtuUser.fullName}</li>
				<li>公&emsp;司:  ${vehicle.dtu.dtuUser.corp.corpName}</li>
				<li>电&emsp;话:  ${vehicle.dtu.dtuUser.corp.phone}</li>
				<li>邮&emsp;件:  ${vehicle.dtu.dtuUser.email}</li>
			</ul>
		</div>
	</div> <!-- //Report head end -->

	
	<div class="body-outer dclear clearfix">
		<div class="map-outer report-table-outer">
			<div class="map-box-head">
				<h3 class="dleft">整车信息</h3>
			</div>
			<div class="reportbox-wrap dclear">
				<table>
					<tr>
						<td class="report-td1 report-tdleft">UUID</td>
						<td class="report-td2">${vehicle.uuid}</td>
						<td class="report-td3">车辆类型</td>
						<td class="report-td4">${vehicle.vehicleType.typeName}</td>
						<td class="report-td5">电池厂商</td>
						<td class="report-td6 report-tdlast">${vehicle.dtu.batteryModel.factoryName}</td>
					</tr>

					<tr>
						<td class="report-td1 report-tdleft">车架号</td>
						<td class="report-td2">${vehicle.vehicleNumber}</td>
						<td class="report-td3">车辆型号</td>
						<td class="report-td4">${vehicle.vehicleModel.modelName}</td>
						<td class="report-td5">电池类型</td>
						<td class="report-td6 report-tdlast">
				    	${vehicle.dtu.batteryModel.batteryType.typeName}</td>
					</tr>

					<tr>
						<td class="report-td1 report-tdleft">SIM卡号</td>
						<td class="report-td2">${sim}</td>
						<td class="report-td3"></td>
						<td class="report-td4"></td>
						<td class="report-td5">电池串数</td>
						<td class="report-td6 report-tdlast">${vehicle.dtu.batteryModel.batteryNumber}</td>
					</tr>

					<tr class="report-table-footer">
						<td class="report-td1 report-tdleft">注册时间</td>
						<td class="report-td2"><fmt:formatDate value="${vehicle.createTime}" pattern="yyyy-MM-dd"/></td>
						<td class="report-td3"></td>
						<td class="report-td4"></td>
						<td class="report-td5">额定容量</td>
						<td class="report-td6 report-tdlast"><fmt:formatNumber value="${vehicle.dtu.batteryModel.capacity}" pattern="0.0"/> Ah</td>
					</tr>
				</table>
			</div>
			<div class="dianchi-footer dianchi-footer-position"></div>
		</div>
		
			<div class="guide-cart-box-outerwrap dclear clearfix">
			
			<div class="guide-cart-box guide-list-small clearfix dleft">
				<div class="guide-cart-box-head">总览</div>
				<div class="guide-cart-box-con">
					<ul>
						<li>总运行里程:    <fmt:formatNumber value="${totalRunningMilege}" pattern="0.00"/> km</li>
						<li>总运行时长:    <fmt:formatNumber value="${totalRunningTime}" pattern="0.0"/>h</li>
						<li>总充电次数:    ${fn:length(vehicleChargeList)}</li>
						<li>总故障次数:    ${totalAlarmCount}</li>
					</ul>
				</div>
				<div class="guide-cart-box-footer"></div>
			</div>
	
	
			<div class="guide-cart-box guide-list1 report-guide-chart clearfix dleft">
					<div class="guide-cart-box-head">充电/运行/离线时间饼状图</div>
					<div class="guide-cart-box-con" id="btou2">
						<script>
							var myChart2 = echarts.init(document.getElementById('btou2')); 
							option = {
							    tooltip : {
							        trigger: 'item',
							        formatter: "{a} <br/>{b} : {c} ({d}%)"
							    },
							    calculable : false,
							    series : [
							        {
							            name:'',
							            type:'pie',
							            radius : [50, 90],
							            data:${onlineStatus}
							        }
							    ]
							};
	
							myChart2.setOption(option); 
						</script>
	
					</div>
					<div class="guide-cart-box-footer"></div>
				</div>
	
			<div class="guide-cart-box guide-list-list4 clearfix dleft">
					<div class="guide-cart-box-head">故障类型分布饼状图</div>
					<div class="guide-cart-box-con" id="btou">
						
						<script>
							var myChart1 = echarts.init(document.getElementById('btou')); 
							option = {
							    tooltip : {
							        trigger: 'item',
							        formatter: "{a} <br/>{b} : {c} ({d}%)"
							    },
							    calculable : false,
							    series : [
							        {
							            name:'报警类型',
							            type:'pie',
							            radius : [50, 90],
							            data:${alarmTypeCount}
							        }
							    ]
							};
	
							myChart1.setOption(option); 
						</script>
	
					</div>
					<div class="guide-cart-box-footer"></div>
				</div>
			
		</div>
		<div class="map-outer dclear" style="display:none">
			<div class="map-box-head">
				<h3 class="dleft">电池总容量衰减曲线</h3>
			</div>
			<div class="reportbox-wrap dclear" id="reportMaonthlyGz" style="height: 400px;">
				<script>

					var myChart = echarts.init(document.getElementById('reportMaonthlyGz')); 
        
			        option = {
	
					    tooltip : {
					        trigger: 'axis'
					    },
					    calculable : true,
					    xAxis : [
					        {
					            type : 'category',
					            boundaryGap : false,
					            data : ${totalCapacityCurveXaxis}
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
					        {
					            name:'SOC',
					            type:'line',
					            smooth:true,
					            itemStyle: {normal: {areaStyle: {type: 'default'}}},
					            data:${totalCapacityCurve}
					        }
					    ]
					};
					                    
					                    

			        // 为echarts对象加载数据 
			        myChart.setOption(option); 
				</script>
			</div>
			<div class="overview-footer dclear"></div>
		</div>


		<div class="map-outer report-table-outer">
			<div class="map-box-head">
				<h3 class="dleft">故障历史</h3>
			</div>
			<div class="reportbox-wrap reportbox-table-wrap dclear">
				<table class="reportbox-table reportbox-table-bg">
					<thead>
						<tr>
							<th>故障类型</th>
							<th>发生时间</th>
							<th>恢复时间</th>
							<th>持续时间 (min)</th>
							 
						</tr>
					</thead>
					<tbody>
						<tr>
						  	<c:forEach var="item" items="${alarmHistory}">
							  <tr>
							  	<td>${item[0]}</td>
							  	<td><fmt:formatDate value="${item[1]}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<c:choose>
								   <c:when test="${item[4] == 'new'}">
								   		<td>-</td>
								   </c:when>
								   <c:otherwise>
								   		<td><fmt:formatDate value="${item[2]}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								   </c:otherwise>
								</c:choose>							  	
							  	<td><fmt:formatNumber value="${item[3]/60}" pattern="0"/></td>
							  	
							  </tr>
						  	</c:forEach>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="dianchi-footer dianchi-footer-position"></div>
		</div>


		<div class="map-outer report-table-outer">
			<div class="map-box-head">
				<h3 class="dleft">运行历史</h3>
			</div>
			<div class="reportbox-wrap reportbox-table-wrap dclear">
				<table class="reportbox-table reportbox-table-bg">
					<thead>
						<tr>
							<th>充电起始时间</th>
							<th>充电时长 (h)</th>
							<th>SOC初值 (%)</th>
							<th>SOC终值 (%)</th>
							<th>上次运行时长 (h)</th>
							<th>上次运行里程 (km)</th>
							<th>总里程 (km)</th>
						</tr>
					</thead>
					<tbody>
					  	<c:forEach var="item" items="${vehicleChargeList}">
						  <tr>
						  	<td><fmt:formatDate value="${item[0]}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						  	<td><fmt:formatNumber value="${item[2]/3600}" pattern="0.0"/></td>
						  	<td><fmt:formatNumber value="${item[3]}" pattern="0.0"/></td>
						  	<td><fmt:formatNumber value="${item[4]}" pattern="0.0"/></td>
						  	<td><fmt:formatNumber value="${item[7]/3600}" pattern="0.0"/></td>
						  	<td><fmt:formatNumber value="${item[6]}" pattern="0.00"/></td>
						  	<td><fmt:formatNumber value="${item[8]}" pattern="0.00"/></td>
						  </tr>
					  	</c:forEach>	
					</tbody>
				</table>
			</div>
			<div class="dianchi-footer dianchi-footer-position"></div>
		</div>
	</div> <!-- //body outer end -->
</body>


</html>