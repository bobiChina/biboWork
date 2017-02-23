<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="login"/>
<title>整车详情</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/style/jquery.mCustomScrollbar.min.css" />
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/charts/echarts-plain.js"></script>
	<script src="${ctx}/static/js/highcharts.js" type="text/javascript" charset="UTF-8"></script>
<script type="text/javascript">
$(function(){

	
	var bmus = ${bmus};
	
	$.each(bmus,function(index,item){
		if (index == 0){
			$("#bmuspan").append("<span class='readtime-active'><a href='javascript:;'>从机"+(index+1)+"</a></span>");
			
			if (item.hotStatus==1)
				hotStatus = "开启";
			else
				hotStatus = "关闭";
			
			if (item.fanStatus==1)
				fanStatus = "开启";
			else
				fanStatus = "关闭";
			
			$("#bmudiv").append("<div class='reportbox-cart-readtime-item' style='display:block'>"+
							"<div class='reportbox-cart-head-tips clearfix'>"+
							"<p>风扇状态："+fanStatus+"</p>"+
							"<p class='add-hot-active'>加热状态："+hotStatus+"</p>"+
							"<p class='dright'><a target='_blank' href='${ctx}/dtu/bmulog/${vehicle.dtu.uuid}/"+index+"'>历史数据</a></p>"+
						"</div>"+
						"<h4 class='dclear'>单体电池电压/均衡电流柱状图</h4>"+
						"<div class='reportbox-cart-readtime-box-inner' id='vlist"+index+"' style='height: 300px;'>"+
						
						"</div>"+
						"<h4>温感温度柱状图</h4>"+
						"<div class='reportbox-cart-readtime-box-inner' id='tlist"+index+"' style='height: 300px;'>"+
					
						"</div>"+
					"</div>"
			);
		}else{
			$("#bmuspan").append("<span><a href='javascript:;'>从机"+(index+1)+"</a></span>");
			
			$("#bmudiv").append("<div class='reportbox-cart-readtime-item'>"+
							"<div class='reportbox-cart-head-tips clearfix'>"+
							"<p>风扇状态："+fanStatus+"</p>"+
							"<p class='add-hot-active'>加热状态："+hotStatus+"</p>"+
							"<p class='dright'><a target='_blank' href='${ctx}/dtu/bmulog/${vehicle.dtu.uuid}/"+index+"'>历史数据</a></p>"+
						"</div>"+
						"<h4 class='dclear'>单体电池电压/均衡电流柱状图</h4>"+
						"<div class='reportbox-cart-readtime-box-inner' id='vlist"+index+"' style='width:960px;height: 300px;'>"+
						
						"</div>"+
						"<h4>温感温度柱状图</h4>"+
						"<div class='reportbox-cart-readtime-box-inner' id='tlist"+index+"' style='width:960px;height:300px;'>"+
					
						"</div>"+
					"</div>"
			);
		}

		
		var vlist = item.singleVoltageList.split(',');
		var vlistIndex=new Array()
		$.each(vlist,function(index,item){
			vlist[index] = parseFloat(vlist[index]);
			vlistIndex[index] = index + 1;
		});
		var tlist = item.boxTemperList.split(',');
		var tlistIndex=new Array()
		$.each(tlist,function(index,item){
			tlist[index] = parseFloat(tlist[index]);
			tlistIndex[index] = index + 1;
		});		
		
		var chart = new Highcharts.Chart({
	        chart: {
	        	renderTo: 'vlist'+index, 
	            type: 'column',
	            animation: false,
	            margin: [ 50, 50, 100, 80],
	            events: {
	                load: applyGraphGradient
	            }
	        },
	        title: {
	            text: '从机'+(index+1)+'单体电池电压柱状图'
	        },
	        credits: {
	            enabled: false 
	        },    	        
	        xAxis: {
	            categories: vlistIndex,
	        },
	        plotOptions: {
	            bar: {
	            	cropThreshold:3420
	                
	            }
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '电压值 (单位：伏)'
	            }
	        },
	        legend: {
	            enabled: false
	        },
	        tooltip: {
	            pointFormat: '电压值: <b>{point.y:.3f} V</b>',
	        },
	        plotOptions:{
	        	column:{
	        		animation:false
	        	}
	        },
	        series: [{
	            name: 'vlist',
	            data: vlist
	        }]
	    });

		var chart2 = new Highcharts.Chart({
	        chart: {
	        	renderTo: 'tlist'+index, 
	        	animation: false,
	            type: 'column',
	            margin: [ 50, 50, 100, 80],
	            events: {
	                load: applyGraphGradient2
	            }
	        },
	        title: {
	            text: '从机'+(index+1)+'箱体温度柱状图'
	        },
	        credits: {
	            enabled: false 
	        },    	        
	        xAxis: {
	            categories: tlistIndex,

	        },
	        yAxis: {
	            
	            title: {
	                text: '温度 (单位：摄氏度)'
	            }
	        },
	        legend: {
	            enabled: false
	        },
	        tooltip: {
	            pointFormat: '温度值: <b>{point.y:.1f} ℃</b>',
	        },
	        plotOptions:{
	        	column:{
	        		animation:false
	        	}
	        },
	        series: [{
	            name: 'tlist',
	            data: tlist
	        }]
	    });
	});
	
	$(".reportbox-cart-head-readtime-top").navTab({
		"toItem":".reportbox-cart-readtime-itemwrap",
		"itemList":".reportbox-cart-readtime-item"
	});
	
	setInterval(function(){
		window.location.reload();
	},1000000);
});

function applyGraphGradient() {
    
    // Options
    //var threshold = 3.3,
        colorAbove = '#ff0000',
        colorBelow = '#ffff00';
        
    // internal
    var someSeries = this.series[0];
    
    $.each(someSeries.points, function(){
        if (this.y > 3.8)
        {
           this.graphic.attr('fill', colorAbove);
        }
        else if (this.y <2.4)
        {
            //this.graphic.attr('fill', colorAbove );
        	this.graphic.attr('fill', colorBelow);
        }
    });
    
    delete someSeries.pointAttr.hover.fill;
    delete someSeries.pointAttr[''].fill; 
}
function applyGraphGradient2() {
    
    // Options
    //var threshold = 3.3,
        colorAbove = '#FF8247',
        colorBelow = '#4B0082';
        
    // internal
    var someSeries = this.series[0];
    
    $.each(someSeries.points, function(){
        if (this.y > 51)
        {
           this.graphic.attr('fill', colorAbove);
        }
        else if (this.y <0)
        {
            //this.graphic.attr('fill', colorAbove );
        	this.graphic.attr('fill', colorBelow);
        }
    });
    
    delete someSeries.pointAttr.hover.fill;
    delete someSeries.pointAttr[''].fill; 
}
</script>
</head>
<body>	

	<div class="body-outer dclear clearfix">
		<h2 class="preport-head-title">整车详情</h2>
		<div class="map-outer report-table-outer">
			<div class="map-box-head">
				<h3 class="report-car-title dleft">总览</h3>
				<span class="report-car dright"><a target='_blank' href="${ctx}/count/vehicle/${vehicle.dtu.id}">整车报表</a></span>
			</div>

			<div class="reportbox-wrap clearfix dclear">
				<div class="overview-content report-overview">
					<div class="report-overview-list box-border-right dleft">
						车辆状态： 	
						<c:if test="${vehicle.dtu.chargeStatus == 1}">
							充电
						</c:if>
						<c:if test="${vehicle.dtu.chargeStatus == 0}">
							放电
						</c:if>
						<c:if test="${vehicle.dtu.chargeStatus == 2}">
							离线
						</c:if>												
					</div>

					<div class="report-overview-list box-border-right dleft">
						报警状态：	
						<c:if test="${vehicle.dtu.alarmStatus == 1}">
							报警
						</c:if>	
						<c:if test="${vehicle.dtu.alarmStatus == 0}">
							正常
						</c:if>							
					</div>

					<div class="report-overview-list dleft">
						均衡：
						<c:if test="${vehicle.dtu.balanceStatus == 1}">
							开启
						</c:if>	
						<c:if test="${vehicle.dtu.balanceStatus == 0}">
							关闭
						</c:if>							
					</div>
				</div>
			</div>
			<div class="dianchi-footer dianchi-footer-position"></div>
		</div> <!-- //总览 -->
		
		<div class="map-outer report-box-cart-outer clearfix">
			<div class="map-report-cart-info dleft">
				<div class="map-report-cartinfo-head">
					<h3>车辆信息</h3>
				</div>
				<div class="map-report-cartinfo-con">
					<table>
						<tr>
							<td class="map-report-table-title">UUID</td>
							<td>${vehicle.uuid}</td>
						</tr>

						<tr>
							<td class="map-report-table-title">车架号</td>
							<td>${vehicle.vehicleNumber}</td>
						</tr>


						<tr>
							<td class="map-report-table-title">城市</td>
							<td>${vehicle.dtu.city}</td>
						</tr>


						<tr>
							<td class="map-report-table-title">车辆类型</td>
							<td>${vehicle.vehicleType.typeName}</td>
						</tr>


						<tr>
							<td class="map-report-table-title">车辆型号</td>
							<td>${vehicle.vehicleModel.modelName}</td>
						</tr>


						<tr>
							<td class="map-report-table-title">电池厂商</td>
							<td>${vehicle.dtu.batteryModel.factoryName}</td>
						</tr>


						<tr>
							<td class="map-report-table-title">电池类型</td>
							<td>
								<c:if test="${vehicle.dtu.batteryModel.batteryType == 1}">
									铁电池
								</c:if>	
								<c:if test="${vehicle.dtu.batteryModel.batteryType == 2}">
									锂电池
								</c:if>									
							</td>
						</tr>

						<tr>
							<td class="map-report-table-title">电池串数</td>
							<td>${vehicle.dtu.batteryModel.batteryNumber}</td>
						</tr>

						<tr>
							<td class="map-report-table-title">额定容量</td>
							<td>${vehicle.dtu.batteryModel.capacity}</td>
						</tr>

					</table>
				</div>
				<div class="map-report-cartinfo-footer"></div>
			</div>

			<div class="map-report-cart-readtimedata dright">
				<div class="map-report-cart-readtimedata-head">
					<h3>主机实时数据</h3>
					<span><a target='_blank' href="${ctx}/dtu/log/${vehicle.dtu.uuid}">历史数据</a></span>
				</div>
				<div class="map-report-cart-readtimedata-con">
					<table>
						<tr>
							<td class="map-report-cart-readtimedata-title">电池容量</td>
							<td>${vehicle.dtu.totalCapacity} AH</td>
							<td class="map-report-cart-readtimedata-title">总电压</td>
							<td>${vehicle.dtu.batteryTotalVoltage} V</td>
						</tr>

						<tr>
							<td class="map-report-cart-readtimedata-title">剩余容量</td>
							<td>${vehicle.dtu.leftCapacity} AH</td>
							<td class="map-report-cart-readtimedata-title">电流</td>
							<td>${vehicle.dtu.batteryTotalAmp} A</td>
						</tr>


						<tr>
							<td class="map-report-cart-readtimedata-title">SOC</td>
							<td>${vehicle.dtu.soc}%</td>
							<td class="map-report-cart-readtimedata-title">SOH</td>
							<td>${vehicle.dtu.soh}%</td>
						</tr>

						<tr>
							<td class="map-report-cart-readtimedata-title">绝缘状态</td>
							<td>
								<c:if test="${vehicle.dtu.insulationStatus == 1}">
									是
								</c:if>	
								<c:if test="${vehicle.dtu.insulationStatus == 0}">
									否
								</c:if>	
							</td>
							<td class="map-report-cart-readtimedata-title">最高单体电压</td>
							<td>${vehicle.dtu.maxSingleVoltage} V</td>
						</tr>


						<tr>
							<td class="map-report-cart-readtimedata-title">总绝缘阻值</td>
							<td>${vehicle.dtu.totalResistanceValue} Ω</td>
							<td class="map-report-cart-readtimedata-title">最低单体电压</td>
							<td>${vehicle.dtu.minSingleVoltage} V</td>
						</tr>


						<tr>
							<td class="map-report-cart-readtimedata-title">绝缘正极阻值</td>
							<td>${vehicle.dtu.positiveResistance} Ω</td>
							<td class="map-report-cart-readtimedata-title">最高温感温度</td>
							<td>${vehicle.dtu.maxBoxTemper} ℃</td>
						</tr>

						<tr>
							<td class="map-report-cart-readtimedata-title">绝缘负极阻值</td>
							<td>${vehicle.dtu.negativeResistance} Ω</td>
							<td class="map-report-cart-readtimedata-title">最低温感温度</td>
							<td>${vehicle.dtu.minBoxTemper} ℃</td>
						</tr>


						<tr>
							<td class="map-report-cart-readtimedata-title"></td>
							<td></td>
							<td class="map-report-cart-readtimedata-title"></td>
							<td></td>
						</tr>

						<tr>
							<td class="map-report-cart-readtimedata-title"></td>
							<td></td>
							<td class="map-report-cart-readtimedata-title"></td>
							<td></td>
						</tr>

					</table>
				</div>
				<div class="map-report-cart-readtimedata-head-footer"></div>
			</div>
		</div>




		<div class="map-outer report-cart-info dclear">
			<div class="map-box-head">
				<h3 class="dleft">从机实时数据</h3>
			</div>
			<div class="reportbox-wrap reportbox-table-wrap dclear">
				<div class="reportbox-cart-head-readtime-top" id="bmuspan">
				</div>
				<div class="reportbox-cart-readtime-itemwrap" id="bmudiv">
				</div>
			</div>
			<div class="dianchi-footer dianchi-footer-position cart-readtime-footer"></div>
		</div>
	</div> <!-- //body outer end -->
  

</body>
</html>