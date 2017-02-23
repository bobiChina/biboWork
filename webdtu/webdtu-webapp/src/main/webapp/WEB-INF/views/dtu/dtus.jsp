<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="dtus"/>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
    <link rel="stylesheet" href="${ctx}/static/styles/doc.css" />

	<script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<link rel="stylesheet" href="${ctx}/static/styles/page.css" />
	<title>列表</title>
	<script type="text/javascript">
	var d;
    $(function(){
    	d = dialog();
		search();
		$("#vehicleType").change(
				function (){
					$("#vehicleModel").empty();
					$("#vehicleModel").append("<option value='-1'>车辆型号</option>");				
					if ($("#vehicleType").val() == -1){
						search();
						return;
					}
						
					search();	
					$.get("${ctx}/vehicle/vehicleModel", {typeId:$("#vehicleType").val()},function(data){
						result = eval(data);
						$.each(result,function(index,item){
							$("#vehicleModel").append("<option value='"+item.id+"'>"+item.modelName+"</option>");
						});
						  
					});
		});
		$("#vehicleModel").change(
				function (){
					search();	
		});
		$("#facName").change(
				function (){
					search();	
		});
		$("#chargeStatus").change(
				function (){
					search();	
		});
		$("#alarmStatus").change(
				function (){
					search();	
		});
		$("#vcity").change(
				function (){
					search();	
		});
		$("#input-box-submit").click(
			function (){
				$("#vehicleType").val(-1);
				$("#vehicleModel").val(-1);
				$("#facName").val(-1);
				$("#chargeStatus").val(-1);
				$("#alarmStatus").val(-1);
				search();
			}		
		);
	    
		$(".search-input-text").inputTipsText();
		$(".th-citys").tipsbox();
		//用户登陆tips
		$(".user-login-arrow").tipsbox({
			"tipsLeft":"-20px",
			"tipsTop":"20px"
		});
		setInterval(searchAjax,60000);
	});
	
	function search() {
		d.show();
		$(".ui-dialog-loading").css("margin-top", "0px");//等待条样式居中

		var uuid = "";
		if ($("#uuid").val() == "请输入UUID编号或车架号直接搜索")
			uuid = "";
		else 
			uuid = $("#uuid").val();
		var timestamp = new Date();
		$("#page").myPagination({
			currPage : 1,
			pageCount : 0,
			pageSize : 5,
			cssStyle:'yahoo',
			info: {
				first_on:true,
				last_on:true
			},
			ajax : {
				on : true, //开启状态
				callback : 'ajaxCallBack', //回调函数，注，此 ajaxCallBack 函数，必须定义在 $(function() {}); 外面
				url : '${ctx}/dtu/listQuery',//访问服务器地址
				dataType : 'json', //返回类型
				param : {
					on : true,
					page : 1,
					pageCountId : 'pageCount',
					uuid:uuid,
					timestamp:timestamp.getTime(),
					vehicleTypeId:$("#vehicleType").val(),
					vehicleModelId:$("#vehicleModel").val(),
					batteryModelId: $("#facName").val(),
					chargeStatus: $("#chargeStatus").val(),
					alarmStatus:$("#alarmStatus").val(),
					city:$("#vcity").val()
				}
			   //参数列表，其中on 必须开启，page 参数必须存在，其他的都是自定义参数，如果是多条件查询，可以序列化表单，然后增加 page参数
			}
		});
	}  
	
	function ajaxCallBack(data) {
		$("#hidPageNumber").val(data.pageNumber);
		var result = data.result;
		var startNumber = (data.pageNumber-1)*20;
		var insetViewData = ""; //视图数据
	    
		if(result != ""){
			$.each(result, function(index, items){
				insetViewData += createTR(startNumber+index+1,items);
				index += 1;
			});
	
		}else{
			insetViewData+= "<tr align='center'><td colspan='8' bgcolor='#FFFFFF' style='text-align:center;'><font color='red'>无记录...</font></td></tr>"
		}
		$("#container > tbody").html(insetViewData);
		$(".table-body").fixedHead({
			nums: 10
		});
		$(".table-body").setThead({
			"setColumnsWidth":[36,160,160,90,110,110,70,70,70,84]
		});//{"setColumnsWidth":[163,172,160,112,128,112,113]}
		// "setColumnsWidth":[36,160,160,90,110,70,70,68,82,100]

		d.close();
	}
	
	function createTR(index,obj) {
		var tr = "<tr>";
			tr += "<td>" + index + "</td>";
			tr += "<td><a target='_blank' href='${ctx}/dtu/get/"+obj.uuid+"'>" + obj.sn + "</a></td>";
			tr += "<td><a target='_blank' href='${ctx}/dtu/get/"+obj.uuid+"'>" + obj.vehicleNumber + "</a></td>";
			//tr += "<td>" + obj.simCard + "</td>";
			if(obj.city == undefined)
				tr += "<td>未知</td>";
			else
				tr += "<td>" + obj.city + "</td>";
			tr += "<td>"+obj.typeName+"</td>";
			//tr += "<td>"+obj.modelName+"</td>";
			tr += "<td>" + obj.facName + "</td>";
			tr += "<td>" + obj.soc + "%</td>";
			
			if(obj.chargeStatus == 0)
				tr += "<td>放电</td>";
			else if(obj.chargeStatus == 1)
				tr += "<td>充电</td>";
			else if(obj.chargeStatus == 2)
				tr += "<td>离线</td>";
					

				
			if(obj.alarmStatus == 1)
				tr += "<td><i class='overcharging-icon'></i></td>";
			else if(obj.alarmStatus == 0)
				tr += "<td></td>";
			else	
				tr += "<td>未知</td>";
				
				tr += "<td><a target='_blank' href='${ctx}/dtu/track/"+obj.uuid+"'>" + "查询" + "</a></td>";
			//tr += "<td><a target='_blank' href='${ctx}/dtu/get/"+obj.id+"'>查看</a></td>"
			tr += "</tr>";
		return tr;
	}
	
	function searchAjax() {
		var uuid = "";
		if ($("#uuid").val() == "请输入UUID编号或车架号直接搜索")
			uuid = "";
		else 
			uuid = $("#uuid").val();
		var timestamp = new Date();
		$("#page").myPagination({
			currPage :  parseInt($("#hidPageNumber").val()),
			pageCount : 99999,
			pageSize : 5,
			cssStyle:'yahoo',
			info: {
				first_on:true,
				last_on:true
			},
			ajax : {
				on : true, //开启状态
				callback : 'intervalCallBack', //回调函数，注，此 ajaxCallBack 函数，必须定义在 $(function() {}); 外面
				url : '${ctx}/dtu/listQuery',//访问服务器地址
				dataType : 'json', //返回类型
				param : {
					on : true,
					page : $("#hidPageNumber").val(),
					pageCountId : 'pageCount',
					uuid:uuid,
					timestamp:timestamp.getTime(),
					vehicleTypeId:$("#vehicleType").val(),
					vehicleModelId:$("#vehicleModel").val(),
					batteryModelId: $("#facName").val(),
					chargeStatus: $("#chargeStatus").val(),
					alarmStatus:$("#alarmStatus").val(),
					city:$("#vcity").val()
				}
			   //参数列表，其中on 必须开启，page 参数必须存在，其他的都是自定义参数，如果是多条件查询，可以序列化表单，然后增加 page参数
			}
		});
	}  	
	
	function intervalCallBack(data) {
		//console.log("num:"+data.pageNumber);
		$("#hidPageNumber").val(data.pageNumber);
		var result = data.result;
		var startNumber = (data.pageNumber-1)*20;
		var insetViewData = ""; //视图数据
	    
		if(result != ""){
			$.each(result, function(index, items){
				insetViewData += createTR(startNumber+index+1,items);
				index += 1;
			});
	
		}else{
			insetViewData+= "<tr align='center'><td colspan='8' bgcolor='#FFFFFF' style='text-align:center;'><font color='red'>无记录...</font></td></tr>"
		}
		$("#container > tbody").html(insetViewData);
		$(".table-body").fixedHead({
			nums: 10
		});
		$(".table-body").setThead({
			"setColumnsWidth":[36,160,160,90,110,110,70,70,70,84]
		});//{"setColumnsWidth":[163,172,160,112,128,112,113]}	"setColumnsWidth":[36,160,160,90,110,70,70,68,82,100]
	}	
	</script>	
	
	<style>
		.btn-sm {
			margin-top: 22px;
			margin-left: 60px;
		    padding: 5px 10px;
		    font-size: 16px;
		    line-height: 1.5;
		    border-radius: 3px;
		}
		.btn-success {
		    color: #fff;
		    background-color: #5cb85c;
		    border-color: #4cae4c;
		}
		.btn {
		    display: inline-block;
		    padding: 6px 12px;
		    margin-bottom: 0;
		    font-size: 14px;
		    font-weight: 400;
		    line-height: 1.42857143;
		    text-align: center;
		    white-space: nowrap;
		    vertical-align: middle;
		    cursor: pointer;
		    -webkit-user-select: none;
		    -moz-user-select: none;
		    -ms-user-select: none;
		    user-select: none;
		    background-image: none;
		    border: 1px solid transparent;
		    border-radius: 4px;
		}
	</style>
</head>
<body>

	<div class="body-outer dclear clearfix">
		<div class="form-box clearfix">
			<div class="input-box dleft" >
				<input type="text"  name="uuid" id ="uuid" class="search-input-text" value="请输入UUID编号或车架号直接搜索"/>
			</div>
			<input type="button" class="dleft" id="input-box-submit" value="搜&nbsp;&nbsp;索" />
			<a href="${ctx}/dtu/map" class="btn btn-success btn-sm" style="display: none;">地&nbsp;&nbsp;图</a>
		</div> <!-- //form end -->
		<input type="hidden" id="hidPageNumber" value="1"/>
		
		<div class="table-box dclear clearfix" >
			<div class="table-body" style="min-height:487px;">
				<table id="container" class="dclear">
					<thead class="displavisibility">
						<tr>
							<th>序号</th>
							<th>SN/UUID</th>
							<th>车架号</th>
							<th>
								<select id="vcity" name="vcity">
							        <option value="城市">城市</option>   
							        <c:forEach items="${citys}" var="item">
							        	<c:if test="${fn:length(item)>'3'}"><option value="${item}">${fn:substring(item, 0, 3)}...</option></c:if>
							        	<c:if test="${fn:length(item)<'4'}"><option value="${item}">${item}</option></c:if>
							        </c:forEach>
								</select>	
							</th>
							<th class="vehicle-type">
								<select id="vehicleType" name="vehicleType">
							        <option value="-1">车辆类型</option>   
							        <c:forEach items="${vtList}" var="item">
							        	<c:if test="${item.typeName != '其他'}">
							     	   	<c:if test="${fn:length(item.typeName)>'3'}"><option value="${item.id}">${fn:substring(item.typeName, 0, 3)}...</option></c:if>
							     	   	<c:if test="${fn:length(item.typeName)<'4'}"><option value="${item.id}">${item.typeName}</option></c:if>
							     	   </c:if>
							        </c:forEach>
							        <c:forEach items="${vtList}" var="item">
										<c:if test="${item.typeName == '其他'}">
										<option value= "${item.id}">${item.typeName}</option>
										</c:if>
									</c:forEach>
								</select>
							</th>
							<th class="battery-models">
								<select id="facName" name="facName">
							        <option value="-1">电池厂商</option>   
							        <c:forEach items="${facList}" var="item">
							     	   <c:if test="${fn:length(item)>'3'}"><option value="${item}">${fn:substring(item, 0, 3)}...</option></c:if>
							     	   <c:if test="${fn:length(item)<'4'}"><option value="${item}">${item}</option></c:if>
							        </c:forEach>  
								</select>
							</th>
							<th>SOC</th>
							<th class="vehicle-state">
								<select id="chargeStatus" name="chargeStatus">
							        <option value="-1">状态</option> 
							        <option value="1">充电</option>   
							        <option value="0">放电</option> 
							        <option value="2">离线</option> 
								</select>
							</th>
							<th class="to-alarm">
								<select id="alarmStatus" name="alarmStatus">
							        <option value="-1">报警</option>    
							        <option value="1">报警</option>   
							        <option value="0">正常</option>  
								</select>
							</th>
							<th>轨迹查询</th>
							<th class="vehicle-models" style="display:none">
								<select id="vehicleModel" name="vehicleModel">
									<option value="-1">车辆型号</option>    
								</select>
							</th>							
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

<script>
	$(function(){



	})
</script>


</body>

</html>