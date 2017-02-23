<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="count" />
<title>日报</title>
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
            text: '车辆类型统计'
        },
        credits: {
            enabled: false 
        },            
        tooltip: {
    	    pointFormat: '<b>{point.y}%</b>辆'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '<b>{point.name}</b>: {point.y}辆'
                }
            }
        },
        series: [{
            type: 'pie',
            name: '',
            data: [['在线',${onlineVehicleCount}],['离线',${offVehicleCount}]]
        }]
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
	<h3 style="text-align:center">力高DTU信息管理系统—日报表</h3>
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
				    <td>名称：${dtuUser.corp.corpName}</td>
				    <td></td>
				    <td>姓名：${dtuUser.userName}</td>
				  </tr>
				  <tr>
				  	<td></td>
				    <td>地址：${dtuUser.corp.addr}</td>
				    <td></td>
				    <td>邮件：${dtuUser.email}</td>
				  </tr>
				  <tr>
				  	<td></td>
				    <td>联系方式：${dtuUser.corp.phone}</td>
				    <td></td>
				    <td>联系方式：${dtuUser.relation}</td>
				  </tr>				  				  
			  </tbody>
			</table>
		</div>
	</div>	
	
	<div class="panel panel-primary">
		<div class="panel-heading">运行状态评估</div>
		<div class="panel-body ">
			<div class="col-md-6">
				<ul style="list-style-type:none">
					<li style="margin-bottom:10px">今日新增上线车辆&nbsp;/&nbsp;车辆总数：<span class="label label-primary">${newVehicleCount}&nbsp;/&nbsp;${allVehicleCount}</span>&nbsp;辆</li>
					<li style="margin-bottom:10px">当日车辆新增运行里程&nbsp;/&nbsp;总里程：<span class="label label-primary">${newVehicleMilege}&nbsp;/&nbsp;${allMilege}</span>&nbsp;公里</li>
					<li style="margin-bottom:10px">当日车辆新增运行时长&nbsp;/&nbsp;总运行时长：<span class="label label-primary"><fmt:formatNumber value="${newVehicleTime}" pattern="0.0"/>&nbsp;/&nbsp;<fmt:formatNumber value="${allTime}" pattern="0.0"/></span>&nbsp;小时</li>
					<li style="margin-bottom:10px">今日故障数&nbsp;/&nbsp;故障次数总计：<span class="label label-primary">${alarmToday}&nbsp;/&nbsp;${alarmAll}</span>&nbsp;次</li>
				</ul>
			</div>
			<div id="container" class="col-md-6" style="height:150px">
			
			</div>			
		</div>
	</div>	
	
	<div class="panel panel-primary">
		<div class="panel-heading">今日新增上线车辆</div>
		<div class="panel-body ">	
			<table class="table table-hover table-striped">
			  <thead>
				  <tr>
				    <th>UUID</th>
				    <th>车辆类型</th>
				    <th>车架号</th>
				    <th>电池厂商</th>
				    <th>电池容量</th>
				    <th>上线时间</th>
				  </tr>
			  <thead>
			  <tbody>
			  	<c:forEach var="vechile" items="${newVehicleList}">
				  <tr>
				  	<td>${vechile[0]}</td>
				    <td>${vechile[1]}</td>
				    <td>${vechile[2]}</td>
				    <td>${vechile[3]}</td>
				    <td>${vechile[4]}</td>
				    <td><fmt:formatDate value="${vechile[5]}" pattern="yyyy-MM-dd HH:mm"/></td>
				  </tr>
			  	</c:forEach>			  
			  </tbody>
			</table>
		</div>
	</div>		
	
	<div class="panel panel-primary">
		<div class="panel-heading">今日故障列表</div>
		<div class="panel-body ">	
			<table class="table table-hover table-striped">
			  <thead>
				  <tr>
				    <th>UUID</th>
				    <th>车辆类型</th>
				    <th>车架号</th>
				    <th>电池厂商</th>
				    <th>故障类型</th>
				    <th>开始时间</th>
				    <th>结束时间</th>
				  </tr>
			  <thead>
			  <tbody>
			  	<c:forEach var="alarm" items="${alarmListToday}">
				  <tr>
				  	<td>${alarm.uuid}</td>
				  	<td>${alarm.typeName}</td>
				  	<td>${alarm.vehicleNumber}</td>
				  	<td>${alarm.factoryName}</td>
				  	<td>${alarm.alarmTypeName}</td>
				  	<td>${alarm.startTime}</td>
				  	<td>${alarm.endTime}</td>
				  </tr>
			  	</c:forEach>			  
			  </tbody>
			</table>
		</div>
	</div>		
	
	<div class="panel panel-primary">
		<div class="panel-heading">今日在线车辆运行状态统计</div>
		<div class="panel-body ">	
			<table class="table table-hover table-striped">
			  <thead>
				  <tr>
				    <th>车辆类型</th>
				    <th>车架号</th>
				    <th>电池厂商</th>
				    <th>运行时间</th>
				    <th>充电时间</th>
				    <th>运行里程</th>
				    <th>故障次数</th>
				  </tr>
			  <thead>
			  <tbody>
			  	<c:forEach var="item" items="${vehicleRunningStatus}">
				  <tr>
				  	<td>${item.vehicleTypeName}</td>
				  	<td>${item.vehicleNumber}</td>
				  	<td>${item.factoryName}</td>
				  	<td><fmt:formatNumber value="${item.runningTime/3600}" pattern="0.0"/>小时</td>
				  	<td><fmt:formatNumber value="${item.chargeTime/3600}" pattern="0.0"/>小时</td>
				  	<td><fmt:formatNumber value="${item.runningMilege}" pattern="0.0"/>公里</td>
				  	<td>${item.alarmCount}</td>
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