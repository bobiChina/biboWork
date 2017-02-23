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
		tr += "<td>" + obj.bmu[${bmuId}].batteryNumber + "</td>";
		tr += "<td>" + obj.bmu[${bmuId}].singleVoltageList + "</td>";
		tr += "<td>" + obj.bmu[${bmuId}].temperSensorNumber + "</td>";
		tr += "<td>" + obj.bmu[${bmuId}].boxTemperList + "</td>";
		tr += "<td>" + obj.bmu[${bmuId}].balanceAmpNumber + "</td>";
		tr += "<td>" + obj.bmu[${bmuId}].balanceAmpList + "</td>";
		if(obj.bmu[${bmuId}].hotStatus == 1)
			tr += "<td>开启</td>";
		else if(obj.bmu[${bmuId}].hotStatus == 0)
			tr += "<td>关闭</td>";
		else	
			tr += "<td>未知</td>";
		if(obj.bmu[${bmuId}].fanStatus == 1)
			tr += "<td>开启</td>";
		else if(obj.bmu[${bmuId}].fanStatus == 0)
			tr += "<td>关闭</td>";
		else	
			tr += "<td>未知</td>";		
		tr += "<td>" + (obj.insertTime.year-100+2000)+"-"+ (obj.insertTime.month+1) +"-"+ obj.insertTime.date +" "+obj.insertTime.hours+":"+obj.insertTime.minutes+":"+obj.insertTime.seconds+ "</td>";
		tr += "</tr>";
	return tr;
}
</script>
</head>
<body>	

<input type="hidden" id="uuid" value="${uuid}"/>
<input type="hidden" id="bmuId" value="${bmuId}"/>
<div  class="container" >
	<div class="panel panel-default">
		<div class="panel-body ">
			<table id="container" class="table-condensed table-hover table-striped">
			  <thead>
				  <tr>
				    <th>电池数目</th>
				    <th>单体电池电压列表</th>
				    <th>温感数目</th>
				    <th>箱体温度列表</th>
				    <th>均衡电流数目</th>
				    <th>均衡电流列表</th>
				    <th>加热开启状态</th>
				    <th>风扇开启状态</th>
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