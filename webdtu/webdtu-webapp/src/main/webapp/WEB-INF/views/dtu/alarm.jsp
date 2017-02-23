<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="alarm" />
<title>报警</title>
<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/ligao.global.js"></script>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/page.css"></link>

<script type="text/javascript">
$(function(){
	search();
	$("#input-box-submit").click(
			function (){
				search();
			}		
	);
	$(".user-login-arrow").tipsbox({
		"tipsLeft":"-20px",
		"tipsTop":"20px"
	});	

});

function search() {
	$("#page").myPagination({
		currPage : 1,
		pageCount : 10,
		pageSize : 5,
		cssStyle:'yahoo',
		info: {
			first_on:true,
			last_on:true
		},
		ajax : {
			on : true, //开启状态
			callback : 'ajaxCallBack', //回调函数，注，此 ajaxCallBack 函数，必须定义在 $(function() {}); 外面
			url : '${ctx}/dtu/alarmList',//访问服务器地址
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
	var result = data.result;
	var insetViewData = ""; //视图数据
	var startNumber = (data.pageNumber-1)*20;
	if(result != ""){
		$.each(result, function(index, items){
			insetViewData += createTR(items);
		});

	}else{
		insetViewData+= "<tr align='center'><td colspan='8' bgcolor='#FFFFFF' style='text-align:center;'><font color='red'>无记录...</font></td></tr>"
	}
	
	$("#container > tbody").html(insetViewData);
	$(".table-body").setThead({
		"setColumnsWidth":[195,195,195,81,81,108,105]
	});	
}
function getDateStr(obj){
	var year = obj.year-100+2000;
	var month = obj.month+1;
	if (month <10)
		month = "0"+month;
	var date = obj.date;
	if (date <10)
		date = "0"+date;
	var hours = obj.hours;
	if (hours <10)
		hours = "0"+hours;
	var minutes = obj.minutes;
	if (minutes<10)
		minutes = "0"+minutes;
	var seconds = obj.seconds;
	if (seconds<10)
		seconds = "0"+seconds;
	return year+"-"+month+"-"+date+" "+hours+":"+minutes+":"+seconds;
}
function createTR(obj) {
	var tr = "<tr>";
		//tr += "<td>" + (obj.alarmStartTime.year-100+2000)+"-"+ (obj.alarmStartTime.month+1) +"-"+ obj.alarmStartTime.date +" "+obj.alarmStartTime.hours+":"+obj.alarmStartTime.minutes+":"+obj.alarmStartTime.seconds+ "</td>";
		tr += "<td>" +getDateStr(obj.alarmStartTime) +"</td>";
		tr += "<td><a target='_blank' href='${ctx}/dtu/get/"+obj.uuid+"'>" + obj.uuid + "</a></td>";
		tr += "<td><a target='_blank' href='${ctx}/dtu/get/"+obj.uuid+"'>"+obj.vehicleNumber+"</a></td>";
		tr += "<td>"+obj.city+"</td>";
		tr += "<td>" + obj.factoryName + "</td>";
		if(obj.chargeStatus == 1)
			tr += "<td>充电</td>";
		else if(obj.chargeStatus == 0)
			tr += "<td>放电</td>";
		else if(obj.chargeStatus == 2)
			tr += "<td>离线</td>";
			
		if(obj.alarmType==1)
			tr +="<td>漏电</td>";
		else if(obj.alarmType==2)
			tr +="<td>主从机通信</td>";
		else if(obj.alarmType==3)
			tr +="<td>过温</td>";
		else if(obj.alarmType==4)
			tr +="<td>过放</td>";
		else if(obj.alarmType==5)
			tr +="<td>过充</td>";
		else if(obj.alarmType==6)
			tr +="<td>SOC过低</td>";
		else if(obj.alarmType==7)
			tr +="<td>SOC过高</td>";
		else if(obj.alarmType==8)
			tr +="<td>过流</td>";
		else if(obj.alarmType==9)
			tr +="<td>温差过大</td>";	
		else if(obj.alarmType==10)
			tr +="<td>压差过大</td>";
		else if(obj.alarmType==11)
			tr +="<td>电压异常</td>";
		else if(obj.alarmType==12)
			tr +="<td>温度异常</td>";
		else if(obj.alarmType==13)
			tr +="<td>总压过高</td>";	
		else if(obj.alarmType==14)
			tr +="<td>总压过低</td>";		
		tr += "</tr>";
	return tr;
}
</script>
</head>
<body>	

	<div class="body-outer dclear clearfix">
		<div class="form-box clearfix" style="display:none">
			<div class="input-box dleft">
				<input type="text" name="uuid" id ="uuid" class="search-input-text" value="" />
			</div>
			 <input id="input-box-submit" type="button" value="搜&nbsp;&nbsp;索" class="dleft"/>
		</div> <!-- //form end -->

		<div class="table-box dclear clearfix">
			<div class="table-body">
				<table id="container" class="dclear">
					<thead class="displavisibility">
						<tr>
						    <th>报警时间</th>
						    <th>UUID</th>
						    <th>车架号</th>
						    <th>城市</th>
						    <th>电池厂商</th>
						    <th>车辆状态</th>
						    <th>报警类型</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="table-footer dclear">
				<div id="page" style="float:right;margin-right:20px" class="yahoo">
				</div>
			</div>
		</div>
	</div> <!-- //body outer end -->

</body>
</html>