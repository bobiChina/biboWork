<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="dtu"/>
<title>整车报表</title>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/bootstrap.min.css"></link>
<script src="${ctx}/static/js/bootstrap.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/highcharts.js" type="text/javascript" charset="UTF-8"></script>
<script type="text/javascript">
$(function(){
    $('#container').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: '运行状况统计'
        },
        credits: {
            enabled: false 
        },            
        tooltip: {
    	    pointFormat: '<b>{point.y}</b>小时'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '<b>{point.name}</b>: {point.y}小时'
                }
            }
        },
        series: [{
            type: 'pie',
            name: '',
            data: ${onlineStatus}
        }]
    });
    
    $('#container1').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: '总容量变化曲线'
        },
        credits: {
            enabled: false 
        },            
        yAxis: {
            title: {
                text: '总容量'
            }
        },
        tooltip: {
            enabled: false,
            formatter: function() {
                return '<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y +'°C';
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: true
            }
        },
        series: [{
            name: '${vehicle.vehicleNumber}',
            data: ${totalCapacityCurve}}]
    });
});

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
<div  class="container">
<input id="btnPrint" type="button" value="打印" onclick="onclick=preview(1)" class="btn btn-success btn-sm"/>
</div>	
<!--startprint1-->
<div  class="container">
	<h3 style="text-align:center">力高DTU信息管理系统—整车报表</h3>
	<h4 style="text-align:left">建档时间：<fmt:formatDate value="${vehicle.createTime}" pattern="yyyy-MM-dd"/></h4>
	<h4 style="text-align:left">归档时间：<fmt:formatDate value="${today}" pattern="yyyy-MM-dd"/></h4>
	<div class="panel panel-primary">
		<div class="panel-heading">基本信息</div>
		<div class="panel-body ">
			<table class="table table-hover table-striped">
			  <thead>
				  <tr>
				    <th style="width:10%">公司信息</th>
				    <th style="width:40%"></th>
				    <th style="width:10%">登录信息</th>
				    <th style="width:40%"></th>
				  </tr>
			  <thead>
			  <tbody>
				  <tr>
				  	<td></td>
				    <td>名称：${vehicle.dtu.dtuUser.corp.corpName}</td>
				    <td></td>
				    <td>姓名：${vehicle.dtu.dtuUser.userName}</td>
				  </tr>
				  <tr>
				  	<td></td>
				    <td>地址：${vehicle.dtu.dtuUser.corp.addr}</td>
				    <td></td>
				    <td>邮件：${vehicle.dtu.dtuUser.email}</td>
				  </tr>
				  <tr>
				  	<td></td>
				    <td>联系方式：${vehicle.dtu.dtuUser.corp.phone}</td>
				    <td></td>
				    <td>联系方式：${vehicle.dtu.dtuUser.relation}</td>
				  </tr>				  				  
			  </tbody>
			</table>
		</div>
	</div>	

<div class="panel panel-primary">
		<div class="panel-heading">整车信息</div>
		<div class="panel-body ">
			<table class="table table-hover table-striped">
			  <tbody>
				  <tr>
				  	<td></td>
				    <td>车辆类型：${vehicle.vehicleType.typeName}</td>
				    <td></td>
				    <td>电池厂商：${vehicle.dtu.batteryModel.factoryName}</td>
				  </tr>
				  <tr>
				  	<td></td>
				    <td>车架号：${vehicle.vehicleNumber}</td>
				    <td></td>
				    <td>电池类型：
				    	<c:if test="${vehicle.dtu.batteryModel.batteryType == 0 }">
				    		锂电池
				    	</c:if>	
				    	<c:if test="${vehicle.dtu.batteryModel.batteryType == 1 }">
				    		铁电池
				    	</c:if>					    	
				    </td>
				  </tr>
				  <tr>
				  	<td></td>
				    <td>UUID：${vehicle.uuid}</td>
				    <td></td>
				    <td>电池容量：${vehicle.dtu.batteryModel.capacity}</td>
				  </tr>				  				  
			  </tbody>
			</table>
		</div>
	</div>	
	<div class="panel panel-primary">
		<div class="panel-heading">运行状态评估</div>
		<div class="panel-body ">
			<div class="col-md-6">
				<ul style="list-style-type:none;margin-top:30px">
					<li style="margin-bottom:10px">车辆运行总里程：<span class="label label-primary">${totalRunningMilege}</span>&nbsp;公里</li>
					<li style="margin-bottom:10px">车辆运行总时长：<span class="label label-primary"><fmt:formatNumber value="${totalRunningTime}" pattern="0.0"/></span>&nbsp;小时</li>
					<li style="margin-bottom:10px">故障总数：<span class="label label-primary">${totalAlarmCount}</span>&nbsp;次</li>
				</ul>
			</div>
			<div class="col-md-6" style="height:180px" id="container"></div>			
		</div>
	</div>	
	
	<div class="panel panel-primary">
		<div class="panel-heading">总容量变化曲线</div>
		<div class="panel-body ">	
			<div id="container1">
			
			</div>
		</div>
	</div>		
	
	<div class="panel panel-primary">
		<div class="panel-heading">历史故障表</div>
		<div class="panel-body ">	
			<table class="table table-hover table-striped">
			  <thead>
				  <tr>
				    <th>故障类型</th>
				    <th>发生时间</th>
				    <th>恢复时间</th>
				    <th>持续时间</th>
				  </tr>
			  <thead>
			  <tbody>
			  	<c:forEach var="item" items="${alarmHistory}">
				  <tr>
				  	<td>${item[0]}</td>
				  	<td><fmt:formatDate value="${item[1]}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				  	<td><fmt:formatDate value="${item[2]}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				  	<td><fmt:formatNumber value="${item[3]/3600}" pattern="0.0"/>小时</td>
				  </tr>
			  	</c:forEach>			  
			  </tbody>
			</table>
		</div>
	</div>		
	
	<div class="panel panel-primary">
		<div class="panel-heading">车辆运维表</div>
		<div class="panel-body ">	
			<table class="table table-hover table-striped">
			  <thead>
				  <tr>
				    <th>充电开始时间</th>
				    <th>充电结束时间</th>
				    <th>充电时长</th>
				    <th>SOC起始值</th>
				    <th>SOC结束值</th>
				    <th>新增SOC</th>
				    <th>上次运行时长</th>
				    <th>上次运行里程</th>
				    <th>总里程</th>
				  </tr>
			  <thead>
			  <tbody>
			  	<c:forEach var="item" items="${vehicleChargeList}">
				  <tr>
				  	<td><fmt:formatDate value="${item[0]}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				  	<td><fmt:formatDate value="${item[1]}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				  	<td><fmt:formatNumber value="${item[2]/3600}" pattern="0.0"/>小时</td>
				  	<td><fmt:formatNumber value="${item[3]}" pattern="0.0"/></td>
				  	<td><fmt:formatNumber value="${item[4]}" pattern="0.0"/></td>
				  	<td><fmt:formatNumber value="${item[5]}" pattern="0.0"/></td>
				  	<td><fmt:formatNumber value="${item[7]/60}" pattern="0.0"/>小时</td>
				  	<td><fmt:formatNumber value="${item[6]}" pattern="0.0"/></td>
				  	<td><fmt:formatNumber value="${item[8]}" pattern="0.0"/></td>
				  </tr>
			  	</c:forEach>			  
			  </tbody>
			</table>
		</div>
	</div>	
</div>
<!--endprint1-->
</body>
</html>