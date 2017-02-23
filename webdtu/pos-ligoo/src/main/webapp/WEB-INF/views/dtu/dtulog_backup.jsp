<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="dtus" />
<title>报警</title>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/bootstrap.min.css"></link>
<script src="${ctx}/static/js/bootstrap.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/page.css"></link>
<script type="text/javascript">
$(function(){
	search();
	
});

function search() {
	$("#page").myPagination({
		currPage : 1,
		pageCount : 10,
		pageSize : 5,
		ajax : {
			on : true, //开启状态
			callback : 'ajaxCallBack', //回调函数，注，此 ajaxCallBack 函数，必须定义在 $(function() {}); 外面
			url : '${ctx}/dtu/logAjax',//访问服务器地址
			dataType : 'json', //返回类型
			param : {
				on : true,
				page : 1,
				pageCountId : 'pageCount',
				uuid:$("#uuid").val()
			}
		}
	});
}  

function ajaxCallBack(data) {
	var result = data.list;
	var insetViewData = ""; //视图数据
    
	if(result != ""){
		$.each(result, function(index, items){
			insetViewData += createTR(items);
		});

	}else{
		insetViewData+= "<tr align='center'><td colspan='8' bgcolor='#FFFFFF' style='text-align:center;'><font color='red'>无记录...</font></td></tr>"
	}
	
	$("#container > tbody").html(insetViewData);
}

function createTR(obj) {
	var tr = "<tr>";
		tr += "<td>" + obj.totalCapacity + "</td>";
		tr += "<td>" + obj.totalCapacity + "</td>";
		if(obj.chargeStatus == 1)
			tr += "<td>在线</td>";
		else if(obj.chargeStatus == 0)
			tr += "<td>离线</td>";
		else	
			tr += "<td>未知</td>";
		if(obj.balanceStatus == 1)
			tr += "<td>开启</td>";
		else if(obj.balanceStatus == 0)
			tr += "<td>关闭</td>";
		else	
			tr += "<td>未知</td>";		
		tr += "<td>" + obj.batteryTotalVoltage + "</td>";
		tr += "<td>" + obj.batteryTotalAmp + "</td>";
		tr += "<td>" + obj.batteryRechargeCycles + "</td>";
		tr += "<td>" + obj.insulationStatus + "</td>";
		tr += "<td>" + obj.maxSingleVoltage + "</td>";
		tr += "<td>" + obj.minSingleVoltage + "</td>";
		tr += "<td>" + obj.maxBoxTemper + "</td>";
		tr += "<td>" + obj.minBoxTemper + "</td>";
		tr += "<td>" + (obj.insertTime.year-100+2000)+"-"+ (obj.insertTime.month+1) +"-"+ obj.insertTime.date +" "+obj.insertTime.hours+":"+obj.insertTime.minutes+":"+obj.insertTime.seconds+ "</td>";
		tr += "</tr>";
	return tr;
}
</script>
</head>
<body>	

<input type="hidden" id="uuid" value="${uuid}"/>

<div  class="container" >
	<div class="panel panel-default">
		<div class="panel-body ">
			<table id="container" class="table-condensed table-hover table-striped">
			  <thead>
				  <tr>
				    <th>总容量</th>
				    <th>剩余容量</th>
				    <th>充电状态</th>
				    <th>均衡开启状态</th>
				    <th>总电压</th>
				    <th>总电流</th>
				    <th>充电次数</th>
				    <th>绝缘状态</th>
				    <th>最高单体电压</th>
				    <th>最低单体电压</th>
				    <th>最高箱体温度</th>
				    <th>最低箱体温度</th>
				    <th>时间</th>
				  </tr>
			  <thead>
			  <tbody>
			  </tbody>
			</table>
			<div id="page"></div>  
		</div>
	</div>
</div>

</body>
</html>