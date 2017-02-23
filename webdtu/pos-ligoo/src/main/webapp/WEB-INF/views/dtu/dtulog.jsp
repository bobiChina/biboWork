<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="login" />
<title>主机历史数据</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/doc.css" />

	<script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<link rel="stylesheet" href="${ctx}/static/styles/page.css" />
<script type="text/javascript">
var d;
$(function(){
	d = dialog();

	$(".table-body").fixedHead({
		//"setTableWidth":"2200",
		"headElement":".history-save-table-head",
		"cutTb":[1,1,1,1,1,1,1,1,1,1,1,1,3,3,3,3],
		"nums": 20
	});

	search();

});

function search() {
	d.show();
	$(".ui-dialog-loading").css("margin-top", "0px");//等待条样式居中
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
	$("html,body").animate({"scrollTop":0},300);
	insetViewData +=   "<tr class='trfirst'><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>"+
		"<td></td><td></td><td>从机</td><td>串号</td><td>电压(V)</td><td>从机</td><td>串号</td><td>电压(V)</td><td>从机</td><td>串号</td><td>温度(℃)</td><td>从机</td><td>串号</td><td>温度(℃)</td></tr>";

	if(result != ""){
		$.each(result, function(index, items){
			insetViewData += createTR(items);
		});

	}else{
		insetViewData+= "<tr align='center'><td colspan='8' bgcolor='#FFFFFF' style='text-align:center;'><font color='red'>无记录...</font></td></tr>"
	}
	
	$("#container > tbody").html(insetViewData);

	$(".body-outer").setScrollTable({
		trHeight : 				"41",
		"headScrollLeft"    	: "#mCSB_1_container",
		"headScrollElement" 	: "#mCSB_1_container"
	});



	$(".table-body").fixedHead({
		//"setTableWidth":"2200",
		"headElement":".history-save-table-head",
		"cutTb":[1,1,1,1,1,1,1,1,1,1,1,1,3,3,3,3],
		"nums": 20
	});
	


	$(".history-save-table-head-outer").mCustomScrollbar({
		axis:"x",
	 	autoDraggerLength: false,
	 	callbacks:{
	 		whileScrolling:function(){
	 			var _positionHeadLeft	= $("#mCSB_1_container").css("left");
				$(".history-table-con-inner").css({"left": _positionHeadLeft});
	 		}
	 	}
	 });

	 d.close();
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
		tr += "<td>" + getDateStr(obj.insertTime)+ "</td>";
		tr += "<td>" + parseFloat(obj.totalCapacity).toFixed(1) + "</td>";
		tr += "<td>" + parseFloat(obj.leftCapacity).toFixed(1) + "</td>";
		tr += "<td>" + parseFloat(obj.soc).toFixed(1) + "</td>";
		if(obj.chargingStatus == 1)
			tr += "<td>充电</td>";
		else if(obj.chargingStatus == 0)
			tr += "<td>放电</td>";
		else	
			tr += "<td>未知</td>";
			
		tr += "<td>" + parseFloat(obj.batteryTotalVoltage).toFixed(3) + "</td>";
		tr += "<td>" + parseFloat(obj.batteryTotalAmp).toFixed(1) + "</td>";
		tr += "<td>" + parseFloat(obj.positiveResistance).toFixed(1) + "</td>";
		tr += "<td>" + parseFloat(obj.negativeResistance).toFixed(1) + "</td>";
		tr += "<td>" + parseFloat(obj.totalResistanceValue).toFixed(1) + "</td>";
		tr += "<td>" + obj.insulationStatus + "</td>";
		tr += "<td>" + parseFloat(obj.soh).toFixed(1) + "</td>";
		tr += "<td>" + obj.maxSingleBoxId + "</td>";
		tr += "<td>" + obj.maxSingleString + "</td>";
		tr += "<td>" + parseFloat(obj.maxSingleVoltage).toFixed(3) + "</td>";
		tr += "<td>" + obj.minSingleBoxId + "</td>";
		tr += "<td>" + obj.minSingleString + "</td>";
		tr += "<td>" + parseFloat(obj.minSingleVoltage).toFixed(3) + "</td>";
		tr += "<td>" + obj.maxTemperBoxId + "</td>";
		tr += "<td>" + obj.maxTemperString + "</td>";
		tr += "<td>" + parseFloat(obj.maxBoxTemper).toFixed(1) + "</td>";
		tr += "<td>" + obj.minTemperBoxId + "</td>";
		tr += "<td>" + obj.minTemperString + "</td>";
		tr += "<td>" + parseFloat(obj.minBoxTemper).toFixed(1) + "</td>";
		tr += "</tr>";
	return tr;
}

function showbumData(){
	var index_bum = $("#selbum").val();
	if(index_bum > -1){
		window.location.href = "${ctx}/dtu/bmulog/${uuid}/" + index_bum ;
	}
}
</script>
</head>
<body>	

<input type="hidden" id="uuid" value="${uuid}"/>


	<div class="body-outer dclear clearfix">
		
		<div class="body-history-save-headtips">
			<div class="dleft" style="margin-left:5px;">
			<span class="btn btn-success btn-sm" style="background-color: #C7C5C6; color:#000;">主机历史数据</span>
            		&nbsp;
            		<c:forEach var="bum" items="${bmuList}" varStatus="statusCount" >
            		<c:if test="${statusCount.count == 1}">
            		<a href="${ctx}/dtu/bmulog/${uuid}/0" class="btn btn-success btn-sm" style="background-color: #DBDBE7; color:#000">从机历史数据</a>
            		</c:if>
            		</c:forEach>
			</div>
			<div class="body-history-headtips-outer dright">
				<span>UUID: ${uuid}</span> <span>车架号: ${vehicleNumber}</span>
			</div>
		</div><!--  //body history save hdeadtips-->


		<div class="table-box dclear clearfix">
			<div class="table-body" style="min-height: 580px;">
			<style>
				.history-table-con-inner table td{ min-width: 140px;}
			</style>
				<div class="history-save-table-head">
					<div class="history-save-table-head-outer">
						<div class="history-save-table-head-inner" style="white-spacing:nowrap;">
							<span style="display:inline-block;">时间</span>
							<span style="display:inline-block;">总容量(Ah)</span>
							<span style="display:inline-block;">剩余容量(Ah)</span>
							<span style="display:inline-block;">SOC(%)</span>
							<span style="display:inline-block;">电池状态</span>
							<span style="display:inline-block;">总电压(V)</span>
							<span style="display:inline-block;">电流(A)</span>
							<span style="display:inline-block;">绝缘正极电阻(kΩ)</span>
							<span style="display:inline-block;">绝缘负极电阻(kΩ)</span>
							<span style="display:inline-block;">总绝缘阻值(kΩ)</span>
							<span style="display:inline-block;">绝缘状态</span>
							<span style="display:inline-block;">SOH(%)</span>
							<span style="display:inline-block;">最高电压单体</span>
							<span style="display:inline-block;">最低电压单体</span>
							<span style="display:inline-block;">最高温度(℃)</span>
							<span style="display:inline-block;">最低温度(℃)</span>
						</div>
					</div>
				</div>

				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner">
						<table id="container" class="dclear" style="margin-left:0px;">
							<tbody>
				 			  <tr class="trfirst">
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>从机</td>
									<td>串号</td>
									<td>电压(V)</td>
									<td>从机</td>
									<td>串号</td>
									<td>电压(V)</td>
									<td>从机</td>
									<td>串号</td>
									<td>温度(℃)</td>
									<td>从机</td>
									<td>串号</td>
									<td>温度(℃)</td>
						 	  </tr>
							</tbody>
						</table>
						
					</div>
				</div>
			</div>
			<div class="table-footer dclear">
				<div id="page" style="float:right;margin-right:20px" class="yahoo">
				</div>
			</div> 
		</div>
	</div> <!-- //body outer end -->
	

</body>
</html>