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

	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
    <link rel="stylesheet" href="${ctx}/static/styles/doc.css" />

	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/charts/echarts-plain.js"></script>
	<script src="${ctx}/static/js/highcharts.js" type="text/javascript" charset="UTF-8"></script>
<script type="text/javascript">
var d;

$(function(){
	d = dialog();
	search();
	setInterval(function(){
		search();
	},60000);
});

function search() {
	d.show();
	var timestamp = new Date();
	$.get("${ctx}/dtu/getAjax", { uuid: $("#dtuUuid").val(),timestamp:timestamp.getTime()}, function(data){
		var result = eval("(" + data + ")");
		var vehicle = result.vehicle;
		if (vehicle.dtu.chargeStatus == 1)
			$("#chargeStatus").html("充电");
		else if(vehicle.dtu.chargeStatus == 2)
			$("#chargeStatus").html("离线");
		else if (vehicle.dtu.chargeStatus == 0)
			$("#chargeStatus").html("放电");
		
		if (vehicle.dtu.alarmStatus == 1)
			$("#alarmStatus").html("报警");
		else if(vehicle.dtu.alarmStatus == 0)
			$("#alarmStatus").html("正常");
		if (vehicle.dtu.balanceStatus == 1)
			$("#balanceStatus").html("开启");
		else if(vehicle.dtu.balanceStatus == 0)
			$("#balanceStatus").html("关闭");
		
		$("#vehicleNumber").html(vehicle.vehicleNumber);
		//$("#simCard").html(vehicle.dtu.simCard);
		$("#dtuCity").html(vehicle.dtu.city);
		$("#typeName").html(vehicle.vehicleType.typeName);
		$("#modelName").html(vehicle.vehicleModel.modelName);
		$("#factoryName").html(vehicle.dtu.batteryModel.factoryName);
		$("#batteryType").html(vehicle.dtu.batteryModel.batteryType.typeName);
		$("#batteryNumber").html(vehicle.dtu.batteryModel.batteryNumber);
		$("#totalCapacity").html(parseFloat(vehicle.dtu.totalCapacity).toFixed(1));
		$("#batteryTotalVoltage").html(parseFloat(vehicle.dtu.batteryTotalVoltage).toFixed(3));
		$("#capacity").html(parseFloat(vehicle.dtu.batteryModel.capacity).toFixed(1));
		$("#leftCapacity").html(parseFloat(vehicle.dtu.leftCapacity).toFixed(1));
		$("#batteryTotalAmp").html(parseFloat(vehicle.dtu.batteryTotalAmp).toFixed(1));
		$("#soc").html(parseFloat(vehicle.dtu.soc).toFixed(1));
		$("#soh").html(parseFloat(vehicle.dtu.soh).toFixed(1));
		if(vehicle.dtu.insulationStatus == 1)
			$("#insulationStatus").html("是");
		else if (vehicle.dtu.insulationStatus == 0)
			$("#insulationStatus").html("否");
		$("#maxSingleVoltage").html(parseFloat(vehicle.dtu.maxSingleVoltage).toFixed(3));
		$("#totalResistanceValue").html(parseFloat(vehicle.dtu.totalResistanceValue).toFixed(1));
		$("#minSingleVoltage").html(parseFloat(vehicle.dtu.minSingleVoltage).toFixed(3));
		$("#positiveResistance").html(parseFloat(vehicle.dtu.positiveResistance).toFixed(1));
		$("#maxBoxTemper").html(parseFloat(vehicle.dtu.maxBoxTemper).toFixed(1));
		$("#negativeResistance").html(parseFloat(vehicle.dtu.negativeResistance).toFixed(1));
		$("#latitude").html(parseFloat(vehicle.dtu.latitude).toFixed(3));
		$("#lontitude").html(parseFloat(vehicle.dtu.lontitude).toFixed(3));
		$("#minBoxTemper").html(parseFloat(vehicle.dtu.minBoxTemper).toFixed(1));
		$("#bmuspan").empty();
		$("#bmudiv").empty();
		var bmus = result.bmus;
		$.each(bmus,function(index,item){
			if (index == 0){
				$("#bmuspan").append("<span class='readtime-active'><a id='bmua"+index+"' href='javascript:bmuclick("+index+");'>从机"+(index+1)+"</a></span>");
				
				if (item.hotStatus==1)
					hotStatus = "开启";
				else if (item.hotStatus==0)
					hotStatus = "关闭";
				else
					hotStatus = "未知";
				if (item.fanStatus==1)
					fanStatus = "开启";
				else if (item.fanStatus==0)
					fanStatus = "关闭";
				else 
					fanStatus = "未知";
				$("#bmudiv").append("<div class='reportbox-cart-readtime-item' style='display:block'>"+
								"<div class='reportbox-cart-head-tips clearfix'>"+
								"<p>风扇状态："+fanStatus+"</p>"+
								"<p class='add-hot-active'>加热状态："+hotStatus+"</p>"+
								"<p class='dright'><a target='_blank' href='${ctx}/dtu/bmulog/${uuid}/"+index+"'>历史数据</a></p>"+
							"</div>"+
							"<h4 class='dclear'>单体电池电压/温感温度柱状图</h4>"+
							"<div class='reportbox-cart-readtime-box-inner' id='vlist"+index+"' style='height: 300px;'>"+
							
							"</div>"+
							"<h4>温感温度柱状图</h4>"+
							"<div class='reportbox-cart-readtime-box-inner' id='tlist"+index+"' style='height: 300px;'>"+
						
							"</div>"+
						"</div>"
				);
			}else{
				$("#bmuspan").append("<span><a id='bmua"+index+"' href='javascript:bmuclick("+index+");'>从机"+(index+1)+"</a></span>");
				
				$("#bmudiv").append("<div class='reportbox-cart-readtime-item'>"+
								"<div class='reportbox-cart-head-tips clearfix'>"+
								"<p>风扇状态："+fanStatus+"</p>"+
								"<p class='add-hot-active'>加热状态："+hotStatus+"</p>"+
								"<p class='dright'><a target='_blank' href='${ctx}/dtu/bmulog/${uuid}/"+index+"'>历史数据</a></p>"+
							"</div>"+
							"<h4 class='dclear'>单体电池电压/温感温度柱状图</h4>"+
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
		$("#bmua"+$("#bmuindex").val()).trigger("click");
		
	});
	d.close();
}

function bmuclick(index){
	$("#bmuindex").val(index);
}

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
	<input type="hidden" id="dtuUuid" value="${uuid}"/>
	<input type="hidden" id="bmuindex" value="0"/>
	<div class="body-outer dclear clearfix">
		<h2 class="preport-head-title">整车详情</h2>
		<div class="map-outer report-table-outer">
			<div class="map-box-head">
				<h3 class="report-car-title dleft">总览</h3>
				<span class="report-car dright"><a target='_blank' href="${ctx}/count/vehicle/${dtuid}">整车报表</a></span>
			</div>

			<div class="reportbox-wrap clearfix dclear">
				<div class="overview-content report-overview">
					<div class="report-overview-list box-border-right dleft">
						车辆状态： <span id="chargeStatus"></span>
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
						报警状态：	<span id="alarmStatus"></span>
						<c:if test="${vehicle.dtu.alarmStatus == 1}">
							报警
						</c:if>	
						<c:if test="${vehicle.dtu.alarmStatus == 0}">
							正常
						</c:if>							
					</div>

					<div class="report-overview-list dleft">
						均衡：<span id="balanceStatus"></span>
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
					<span style="display:none;"><a target='_blank' href="${ctx}/dtu/track/${uuid}">轨迹查询</a></span>
				</div>
				<div class="map-report-cartinfo-con" style="height:433px;">
					<table>
						<tr>
							<td class="map-report-table-title">U&nbsp;U&nbsp;I&nbsp;D</td>
							<td>${uuid}</td>
						</tr>
						<tr>
							<td class="map-report-table-title">S&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;N</td>
							<td>${sn}</td>
						</tr>
						<tr>
							<td class="map-report-table-title">车&nbsp;架&nbsp;&nbsp;号</td>
							<td><span id="vehicleNumber"></span></td>
						</tr>

						<tr>
							<td class="map-report-table-title">SIM卡号</td>
							<td><span id="simCard" style="display:none;"></span>${sim}</td>
						</tr>
						
						<tr>
							<td class="map-report-table-title">城&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;市</td>
							<td><span id="dtuCity"></span></td>
						</tr>
						<tr>
							<td class="map-report-table-title">车辆类型</td>
							<td><span id="typeName"></span></td>
						</tr>
						<tr>
							<td class="map-report-table-title">车辆型号</td>
							<td><span id="modelName"></span></td>
						</tr>
						<tr>
							<td class="map-report-table-title">电池厂商</td>
							<td><span id="factoryName"></span></td>
						</tr>
						<tr>
							<td class="map-report-table-title">电池类型</td>
							<td><span id="batteryType"></span>
							</td>
						</tr>
						<tr>
							<td class="map-report-table-title">电池串数</td>
							<td><span id="batteryNumber"></span></td>
						</tr>
						<tr>
							<td class="map-report-table-title">额定容量</td>
							<td><span id="capacity"></span> Ah</td>
						</tr>

					</table>
				</div>
				<div class="map-report-cartinfo-footer"></div>
			</div>

			<div class="map-report-cart-readtimedata dright">
				<div class="map-report-cart-readtimedata-head">
					<h3>主机实时数据</h3>
					<span><a target='_blank' href="${ctx}/dtu/log/${uuid}">历史数据</a></span>
				</div>
				<div class="map-report-cart-readtimedata-con" style="height:433px;">
					<table>
						<tr>
							<td class="map-report-cart-readtimedata-title">电池容量</td>
							<td><span id="totalCapacity"></span>${vehicle.dtu.totalCapacity} Ah</td>
							<td class="map-report-cart-readtimedata-title">总电压</td>
							<td><span id="batteryTotalVoltage"></span> V</td>
						</tr>

						<tr>
							<td class="map-report-cart-readtimedata-title">剩余容量</td>
							<td><span id="leftCapacity"></span> Ah</td>
							<td class="map-report-cart-readtimedata-title">电流</td>
							<td><span id="batteryTotalAmp"></span> A</td>
						</tr>


						<tr>
							<td class="map-report-cart-readtimedata-title">SOC</td>
							<td><span id="soc"></span>%</td>
							<td class="map-report-cart-readtimedata-title">SOH</td>
							<td><span id="soh"></span>%</td>
						</tr>

						<tr>
							<td class="map-report-cart-readtimedata-title">绝缘状态</td>
							<td><span id="insulationStatus"></span>
							</td>
							<td class="map-report-cart-readtimedata-title">最高单体电压</td>
							<td><span id="maxSingleVoltage"></span> V</td>
						</tr>


						<tr>
							<td class="map-report-cart-readtimedata-title">总绝缘阻值</td>
							<td><span id="totalResistanceValue"></span> kΩ</td>
							<td class="map-report-cart-readtimedata-title">最低单体电压</td>
							<td><span id="minSingleVoltage"></span> V</td>
						</tr>


						<tr>
							<td class="map-report-cart-readtimedata-title">绝缘正极阻值</td>
							<td><span id="positiveResistance"></span> kΩ</td>
							<td class="map-report-cart-readtimedata-title">最高温感温度</td>
							<td><span id="maxBoxTemper"></span> ℃</td>
						</tr>

						<tr>
							<td class="map-report-cart-readtimedata-title">绝缘负极阻值</td>
							<td><span id="negativeResistance"></span> kΩ</td>
							<td class="map-report-cart-readtimedata-title">最低温感温度</td>
							<td><span id="minBoxTemper"></span> ℃</td>
						</tr>


						<tr>
							<td class="map-report-cart-readtimedata-title">经度</td>
							<td><span id="lontitude"></span></td>
							<td class="map-report-cart-readtimedata-title">纬度</td>
							<td><span id="latitude"></span></td>
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