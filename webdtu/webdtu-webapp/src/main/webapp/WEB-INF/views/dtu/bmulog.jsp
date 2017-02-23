<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="login" />
	<title>从机历史数据</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
    <link rel="stylesheet" href="${ctx}/static/styles/doc.css" />

	<script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<link rel="stylesheet" href="${ctx}/static/styles/page.css" />
	<style>
	select {
            background: #C7C5C6;
            color: #000;
            border: none;
            height: 22px;
            line-height: 28px;
            text-align: right;
         }
	</style>
<script type="text/javascript">
var d;

$(function(){
	d = dialog();
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
	//insetViewData+= "<tr><td>电池数目</td><td>单体电池电压列表</td><td>温感数目</td><td>箱体温度列表</td><td>均衡电流数目</td><td>均衡电流列表</td>"+
    //"<td>加热开启状态</td><td>风扇开启状态</td><td>时间</td></tr>";
	 /* if(result != ""){
		$.each(result, function(index, items){
			insetViewData += createTR(items);
		});

	}else{
		insetViewData += "<tr class='trfirst'><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>";
		insetViewData+= "<tr align='center'><td colspan='8' bgcolor='#FFFFFF' style='text-align:center;'><font color='red'>无记录...</font></td></tr>"
	}  */
	
	$("#container > tbody").html(insetViewData);
	$(".body-outer").setScrollTable({
		"headScrollLeft"    	: "#mCSB_1_container",
		"headScrollElement" 	: "#mCSB_1_container"
	});


/* 	$(".table-body").fixedHead({
		//"setTableWidth":"1320",
		"headElement":".history-save-table-head",
		//"cutTb":[5,1,12,2,1],
		"nums": 20
	}); */
	
	$(".table-body").createTable({
		headElement:".history-save-table-head",
		nums: 20,
		data: result,
		index: ${bmuId}
	});

	$(".history-save-table-head-inner").css("width", (parseInt($("#container").css("width")) + 0) +"px");
	
	
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
	if ( obj.bmu[${bmuId}]== undefined)
		return;
	var tr = "<tr>";
		tr += "<td>" + getDateStr(obj.insertTime)+ "</td>";
		tr += "<td>" + obj.bmu[${bmuId}].singleVoltageList == undefined || obj.bmu[${bmuId}].singleVoltageList == null ? "0" : obj.bmu[${bmuId}].singleVoltageList + "</td>";
		tr += "<td>" + obj.bmu[${bmuId}].boxTemperList + "</td>";
		tr += "<td>均衡状态</td>";
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
<input type="hidden" id="bmuId" value="${bmuId}"/>

	<div class="body-outer dclear clearfix">
		<div class="body-history-save-headtips">
			<div class="dleft" style="margin-left:5px;">
			<a href="${ctx}/dtu/log/${uuid}" class="btn btn-success btn-sm" style="background-color: #E0E0E0; color:#000;">主机历史数据</a>
            		&nbsp;
            		<span class="btn btn-success btn-sm" style="background-color: #C7C5C6; color:#000">从机<select id="selbum" name="selbum" onchange="showbumData()">

			<c:forEach begin="1" end="${bmuNum}" varStatus="statusCount" >
				<option value='<c:out value="${statusCount.count -1}"/>' <c:if test="${statusCount.count -1 == bmuId }"> selected </c:if> ><c:out value="${statusCount.count}"/></option>
			</c:forEach>
			</select>历史数据</span>
			</div>
			<div class="body-history-headtips-outer dright">
				<span>UUID: ${uuid}</span> <span>车架号: ${vehicleNumber}</span> <span class="spanlast">从机号: #${bmuId+1}</span>    
			</div>
		</div><!--  //body history save hdeadtips-->

		<div class="table-box dclear clearfix">
			<div class="table-body" style="min-height: 580px;">
				<style>
					.history-table-con-inner table td{ min-width: 140px;}
				</style>
				<div class="history-save-table-head">
					<div class="history-save-table-head-outer">
						<div class="history-save-table-head-inner">
							<span>时间</span>
							<span>单体电压(V)</span>
							<span>温感温度 (℃)</span>
							<span>均衡状态</span>
							<span>均衡电流(A)</span>
							<span>加热</span>
							<span style="width:141px !important; margin-left:-5px">风扇</span>
						</div>
					</div>
				</div>

				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner">
						<table id="container" class="dclear" style="margin-left:1px;">
							<tbody>
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