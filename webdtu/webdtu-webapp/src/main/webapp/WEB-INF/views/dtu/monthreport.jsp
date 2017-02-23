<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="login" />
<title>月报</title>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<script src="${ctx}/static/js/charts/echarts-all.js"></script>
<script type="text/javascript">
$(function(){
   getMonthInfo();
});

function getMonthInfo(){
	$.post("${ctx}/count/montherReportAjax", {date:"${lastmonth}"},function(data){
		if(data == "-1"){

		}else{
			var rsObj = eval('(' + data + ')');
			$("#vehicleCount_show").html(rsObj.newVehicleCount + '&nbsp;/&nbsp;' + rsObj.allVehicleCount);
			$("#alarmMonth_show").html(rsObj.alarmMonth + '&nbsp;/&nbsp;' + rsObj.alarmAll);
			$("#vehicleTime_show").html(rsObj.newVehicleTime + '&nbsp;/&nbsp;' + rsObj.allTime + '&nbsp;小时');
			$("#maxTime_show").html(rsObj.maxTime + '小时');
		}
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
			<h2>月报表 (${lastmonth})</h2>
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
							<li><span class="overview-cart-title">本月新增上线车辆&nbsp;/&nbsp;车辆总数：</span><span class="overview-cart-total" id="vehicleCount_show"></span></li>
							<li><span class="overview-cart-title">本月故障数&nbsp;/&nbsp;故障次数总计：</span><span class="overview-cart-total" id="alarmMonth_show"></span></li>
						</ul>
					</div>
					
					<div class="overview-info overview-dright dleft">
						<ul>
							<li><span class="overview-cart-title">本月车辆新增运行时长&nbsp;/&nbsp;总时长：</span><span class="overview-cart-total" id="vehicleTime_show"></span></li>
							<li><span class="overview-cart-title">最大单车运行时长：</span><span class="overview-cart-total" id="maxTime_show"></span></li>
						</ul>
					</div>	
				</div>
			</div>

			<div class="overview-footer dclear"></div>
		</div> <!-- //总览 -->
		
		<div class="map-outer dclear">
			<div class="map-box-head">
				<h3 class="dleft">本月故障</h3>
			</div>
			<div class="reportbox-wrap dclear" id="reportMaonthlyGz" style="height: 400px;">
				<script>

					var myChart = echarts.init(document.getElementById('reportMaonthlyGz')); 
        
			        option = {
			        	calculable:false,
					    tooltip : {
					        trigger: 'axis'
					    },
					    legend: {
					        data:['本月实时故障','本月历史故障数']
					    },
					    xAxis : [
					        {	
					            type : 'category',
					            axisLabel: {
			                        show: true,
			                        interval: '0',
			                        textStyle:{
			                        	color: '#555',
				                    	fontSize:8
				                    }
			                    },
					            data : ${alarmCountMonthByType[0]}
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
					       
					        {
					            name:'本月实时故障',
					            type:'bar',
					            data:${alarmCountMonthByType[1]}
					        },
					        {
					            name:'本月历史故障数',
					            type:'bar',
					            data:${alarmCountMonthByType[2]}
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
				<h3 class="dleft">本月新增</h3>
			</div>
			<div class="reportbox-wrap reportbox-table-wrap dclear">
				<div class="report-monthly-add-chert clearfix">
					<div class="report-monthly-list report-monthly-line" id="btou2" style="height:250px">
					<script>
						var myChart2 = echarts.init(document.getElementById('btou2')); 
						option = {
			        	    title : {
			        	        text: '车辆型号',
			        	        x:'center'
			        	    },	
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
						            data:${vehicleType}
						        }
						    ]
						};

						myChart2.setOption(option); 
					</script>
					</div>
					<div class="report-monthly-list" id="btou" style="height:250px">
					<script>
						var myChart1 = echarts.init(document.getElementById('btou')); 
						option = {
			        	    title : {
			        	        text: '电池类型',
			        	        x:'center'
			        	    },								
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
						            data:${batteryModel}
						        }
						    ]
						};

						myChart1.setOption(option); 
					</script>
					</div>
				</div>


				<table class="reportbox-table reportbox-table-bg dclear">
					<thead>
						<tr>
							<th class="report-td-first"></th>
							<th>时间</th>
							<th>UUID</th>
							<th>车架号</th>
							<th>车辆类型</th>
							<th>车辆型号</th>
							<th>电池厂商</th>
							<th>电池类型</th>
							<th>电池串数</th>
							<th>额定容量（AH)</th>
						</tr>
					</thead>
					<tbody>
					  	<c:forEach var="item" items="${newVehicleList}" varStatus="xh">
							<tr>
								<td class="report-td-first">${xh.count}</td>
								<td><fmt:formatDate value="${item.createTime}" type="date" dateStyle="short" pattern="yyyy-MM-dd"/></td>
								<td>${item.uuid}</td>
								<td>${item.vehicleNumber}</td>
								<td>${item.vehicleType.typeName}</td>
								<td>${item.vehicleModel.modelName}</td>
								<td>${item.dtu.batteryModel.factoryName}</td>
								<td>${item.dtu.batteryModel.batteryType.typeName}</td>
								<td>${item.dtu.batteryModel.batteryNumber}</td>
								<td><fmt:formatNumber value="${item.dtu.batteryModel.capacity}" pattern="0.0"/></td>
							</tr>
					  	</c:forEach>					

					</tbody>
				</table>
			</div>
			<div class="dianchi-footer dianchi-footer-position"></div>
		</div>
	</div> <!-- //body outer end -->
<!--endprint1-->
</body>
</html>