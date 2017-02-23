<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="monthcount" />
<title>月报</title>
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
            data: [${vehicleType}]
        }]
    });

    $('#container2').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: '电池型号统计'
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
            data: [${batteryModel}]
        }]
    });
    
    
    $('#container3').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: '故障类型分类统计'
        },
        credits: {
            enabled: false 
        },    
        xAxis: {
            categories: [
                '漏电',
                '主从机通信',
                '过温',
                '过放',
                '过充',
                'SOC过低',
                'SOC过高',
                '过流',
                '温差过大',
                '压差过大',
                '电压检测异常',
                '温度检测异常',
                '总压过高',
                '总压过低'
            ]
        },
        yAxis: {
            min: 0,
            title: {
                text: '次数'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tbody><tr><td style="color:{series.color};padding:0">{series.name}: </td><td style="padding:0"><b>{point.y}次</b></td></tr></tbody>',
            footerFormat:'</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: ${batteryAlarmCount}
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
	<h3 style="text-align:center">力高DTU信息管理系统—月报表</h3>
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
			<div  class="col-md-3" style="height:120px"></div>
			<div class="col-md-6">
				<ul style="list-style-type:none">
					<li style="margin-bottom:10px">本月新增上线车辆&nbsp;/&nbsp;车辆总数：<span class="label label-primary">${newVehicleCount}&nbsp;/&nbsp;${allVehicleCount}</span>&nbsp;辆</li>
					<li style="margin-bottom:10px">本月车辆新增运行里程&nbsp;/&nbsp;总里程：<span class="label label-primary">${newVehicleMilege}&nbsp;/&nbsp;${allMilege}</span>&nbsp;公里</li>
					<li style="margin-bottom:10px">本月车辆新增运行时长&nbsp;/&nbsp;总运行时长：<span class="label label-primary"><fmt:formatNumber value="${newVehicleTime}" pattern="0.0"/>&nbsp;/&nbsp;<fmt:formatNumber value="${allTime}" pattern="0.0"/></span>&nbsp;小时</li>
					<li style="margin-bottom:10px">本月故障数&nbsp;/&nbsp;故障次数总计：<span class="label label-primary">${alarmMonth}&nbsp;/&nbsp;${alarmAll}</span>&nbsp;次</li>
				</ul>
			</div>
			<div class="col-md-3" style="height:120px"></div>			
		</div>
	</div>	
	
	<div class="panel panel-primary">
		<div class="panel-heading">本月生产统计</div>
		<div class="panel-body ">	
			<div id="container" class="col-md-6">
			
			</div>
			
			<div id="container2" class="col-md-6">
			
			</div>
		</div>
	</div>		
	
	<div class="panel panel-primary">
		<div class="panel-heading">本月故障统计</div>
		<div class="panel-body ">	
			<div id="container3">
			</div>
		</div>
	</div>		
	
	<div class="panel panel-primary">
		<div class="panel-heading">本月车辆运行状态统计</div>
		<div class="panel-body ">	
			<table class="table table-hover table-striped">
			  <thead>
				  <tr>
				    <th>车辆类型</th>
				    <th>车架号</th>
				    <th>电池厂商</th>
				    <th>充电次数</th>
				    <th>放电次数</th>
				    <th>运行时长</th>
				    <th>运行里程</th>
				  </tr>
			  <thead>
			  <tbody>
			  	<c:forEach var="item" items="${vehicleRunningMonthCount}">
				  <tr>
				  	<td>${item[4]}</td>
				  	<td>${item[3]}</td>
				  	<td>${item[5]}</td>
				  	<td>${item[9]}</td>
				  	<td>${item[8]}</td>
				  	<td><fmt:formatNumber value="${item[2]/3600}" pattern="0.0"/>小时</td>
				  	<td><fmt:formatNumber value="${item[1]}" pattern="0.0"/>公里</td>
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