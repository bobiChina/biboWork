<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="dtus"/>
<title>dtu</title>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/bootstrap.min.css"></link>
<script src="${ctx}/static/js/bootstrap.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/highcharts.js" type="text/javascript" charset="UTF-8"></script>
<script type="text/javascript">
$(function(){

	var bmus = ${bmus};
	$.each(bmus,function(index,item){
		//console.log(item.singleVoltageList);
		$("#bmus").append("<a target='_blank' href='${ctx}/dtu/bmulog/${dtu.uuid}/"+index+"'>历史记录</a><div class='panel panel-primary' id='vlist"+index+"'></div>");
		$("#bmus").append("<div class='panel panel-primary' id='tlist"+index+"'></div>");
		var vlist = item.singleVoltageList.split(',');
		$.each(vlist,function(index,item){
			vlist[index] = parseInt(vlist[index]);
		});
		var tlist = item.boxTemperList.split(',');
		$.each(tlist,function(index,item){
			tlist[index] = parseInt(tlist[index]);
		});		
		var chart = new Highcharts.Chart({
	        chart: {
	        	renderTo: 'vlist'+index, 
	            type: 'column',
	            animation: false,
	            margin: [ 50, 50, 100, 80]
	        },
	        title: {
	            text: '从机'+(index+1)+'单体电池电压柱状图'
	        },
	        credits: {
	            enabled: false 
	        },    	        
	        xAxis: {
	            categories: [

	            ],

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
	            pointFormat: '电压值: <b>{point.y:.1f} 伏</b>',
	        },
	        series: [{
	            name: 'vlist',
	            data: vlist,
	            dataLabels: {
	                enabled: true,
	                rotation: -90,
	                color: '#FFFFFF',
	                align: 'right',
	                x: 4,
	                y: 10,
	                style: {
	                    fontSize: '13px',
	                    fontFamily: 'Verdana, sans-serif',
	                    textShadow: '0 0 3px black'
	                }
	            }	
	        }]
	    });

		var chart2 = new Highcharts.Chart({
	        chart: {
	        	renderTo: 'tlist'+index, 
	            type: 'column',
	            animation: false,
	            margin: [ 50, 50, 100, 80]
	        },
	        title: {
	            text: '从机'+(index+1)+'箱体温度柱状图'
	        },
	        credits: {
	            enabled: false 
	        },    	        
	        xAxis: {
	            categories: [

	            ],

	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '温度 (单位：摄氏度)'
	            }
	        },
	        legend: {
	            enabled: false
	        },
	        tooltip: {
	            pointFormat: '温度值: <b>{point.y:.1f} 伏</b>',
	        },
	        series: [{
	            name: 'tlist',
	            data: tlist,
	            dataLabels: {
	                enabled: true,
	                rotation: -90,
	                color: '#FFFFFF',
	                align: 'right',
	                x: 4,
	                y: 10,
	                style: {
	                    fontSize: '13px',
	                    fontFamily: 'Verdana, sans-serif',
	                    textShadow: '0 0 3px black'
	                }
	            }	            
	        }]
	    });
	})
   
});
</script>
</head>
<body>	

<div  class="container" >
	
	<div class="panel panel-primary">
		<div class="panel-heading">主机信息</div>
		<div class="panel-body ">
		<table id="container" class="table table-hover table-striped">
		<tr><td><a href="${ctx}/count/vehicle/${dtu.id}" target='_blank'>整车报表</a></td><td><a href="${ctx}/dtu/log/${dtu.uuid}" target='_blank'>历史记录</a></td></tr>
		<tr><td>uuid：${dtu.uuid}</td>
		<td>最高单体电压：${dtu.maxSingleVoltage}</td></tr>
		<tr><td>soc:${dtu.soc}</td><td>最低单体电压：${dtu.minSingleVoltage}</td></tr>
		<tr><td>总压：${dtu.batteryTotalVoltage}</td><td>最高箱体温度：${dtu.maxBoxTemper}</td></tr>
		<tr><td>电流：${dtu.batteryTotalAmp}</td><td>最低箱体温度：${dtu.minBoxTemper}</td></tr>
		<tr><td>报警状态：<c:if test="${dtu.alarmStatus==1}">报警</c:if><c:if test="${dtu.alarmStatus==0}">正常</c:if></td>
		<td>充放电状态：<c:if test="${dtu.chargeStatus==1}">充电</c:if><c:if test="${dtu.chargeStatus==0}">放电</c:if></td></tr>
		<tr><td>充电次数：${dtu.batteryRechargeCycles}</td><td>总容量：${dtu.totalCapacity}</td></tr>
	</table>
	</div>
	</div>
</div>
<div id="bmus" class="container">
</div>
  

</body>
</html>