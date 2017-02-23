<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="dtus"/>
<title>dtu列表</title>
<script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/page.css"></link>

<script type="text/javascript">
$(function(){
	search();
	$("#vehicleType").change(
			function (){
				$("#vehicleModel").empty();
				$("#vehicleModel").append("<option value='-1'>请选择</option>");				
				if ($("#vehicleType").val() == -1)
					return;
					
				$.get("${ctx}/vehicle/vehicleModel", {typeId:$("#vehicleType").val()},function(data){
					result = eval(data);
					$.each(result,function(index,item){
						$("#vehicleModel").append("<option value='"+item.id+"'>"+item.modelName+"</option>");
					});
					  
				});
			});
	
	$("#btnQuery").click(
		function (){
			search();
		}		
	);
    
   
});

function search() {
	
	$("#page").myPagination({
		currPage : 1,
		pageCount : 10,
		pageSize : 5,
		ajax : {
			on : true, //开启状态
			callback : 'ajaxCallBack', //回调函数，注，此 ajaxCallBack 函数，必须定义在 $(function() {}); 外面
			url : '${ctx}/dtu/listQuery',//访问服务器地址
			dataType : 'json', //返回类型
			param : {
				on : true,
				page : 1,
				pageCountId : 'pageCount',
				uuid:$("#uuid").val(),
				vehicleTypeId:$("#vehicleType").val(),
				vehicleModelId:$("#vehicleModel").val(),
				batteryModelId: $("#facName").val(),
				chargeStatus: $("#chargeStatus").val(),
				alarmStatus:$("#alarmStatus").val()
				
			}
		   //参数列表，其中on 必须开启，page 参数必须存在，其他的都是自定义参数，如果是多条件查询，可以序列化表单，然后增加 page参数
		}
	});
}  

function ajaxCallBack(data) {
	
	var result = data.result;
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
		tr += "<td>" + obj.id + "</td>";
		tr += "<td>" + obj.uuid + "</td>";
		tr += "<td>" + obj.simCard + "</td>";
		tr += "<td>" + obj.facName + "</td>";
		tr += "<td>" + obj.soc + "</td>";
		if(obj.alarmStatus == 1)
			tr += "<td>报警</td>";
		else if(obj.alarmStatus == 0)
			tr += "<td>正常</td>";
		else	
			tr += "<td>未知</td>";
			
		if(obj.onlineStatus == 1)
			tr += "<td>在线</td>";
		else if(obj.onlineStatus == 0)
			tr += "<td>离线</td>";
		else	
			tr += "<td>未知</td>";			
		tr += "<td><a target='_blank' href='${ctx}/dtu/get/"+obj.id+"'>查看</a></td>"
		tr += "</tr>";
	return tr;
}
</script>
<style type="text/css">

</style>
</head>
<body>	
<div id = "clause" class="container">
<div class="panel panel-default ">
		<div class="panel-body ">
		  <div class="form-inline" role="form">
				<div class="col-md-3">
					  <div class="form-group">
					                     车辆类型 
					      <select id="vehicleType" name="vehicleType" class="form-control input-sm " style="width:160px">
						        <option value="-1">请选择</option>   
						        <c:forEach items="${vtList}" var="item">
						     	   <option value="${item.id}">${item.typeName}</option>   
						        </c:forEach>
					      </select>
				      </div>
			      </div>
			      <div class="col-md-3">
				  	      车辆型号&nbsp;
				      <select id="vehicleModel" name="vehicleModel" class="form-control input-sm" style="width:160px">
				        <option value="-1">请选择</option>    
				      </select> 
			      </div>   
			      <div class="col-md-3">
				             电池厂商
				      <select id="facName" name="facName" class="form-control input-sm" style="width:160px">
				        <option value="-1">请选择</option>   
				        <c:forEach items="${bmList}" var="item">
				     	   <option value="${item.id}">${item.factoryName}</option>   
				        </c:forEach>  
				      </select> 
			      </div>
		      	  <div class="col-md-3">
				   	     充电状态
				      <select id="chargeStatus" name="chargeStatus" class="form-control input-sm" style="width:160px">
				        <option value="-1">请选择</option> 
				        <option value="1">充电</option>   
				        <option value="0">放电</option>   
				      </select>    
			      </div>	
		   </div>   
		   </div>
		   <div class="panel-body" style="margin-top:-25px">
		   <div class="form-inline" role="form" style="padding-top:10px">
			      <div class="col-md-3">
						报警状态
				      <select id="alarmStatus" name="alarmStatus" class="form-control input-sm" style="width:160px">
				        <option value="-1">请选择</option>    
				        <option value="1">报警</option>   
				        <option value="0">正常</option>   
				      </select> 
			      </div>
			      <div class="col-md-3">
			    	  UUID编号
			    	  <input type="text" name="uuid" id ="uuid" class="form-control input-sm" style="width:160px"/>
			      </div>
			      <div class="col-md-3" >
						<input id="btnQuery" type="button" value="查询" class="btn btn-success btn-sm"/>
			      </div>	      
		    </div>
    	</div>  
    </div> 
</div>
<div  class="container" >
	<div class="panel panel-default">
		<div class="panel-body ">
			<table id="container" class="table table-hover table-striped">
			  <thead>
				  <tr>
				    <th>行号</th>
				    <th>UUID</th>
				    <th>sim卡号</th>
				    <th>电池厂商</th>
				    <th>soc</th>
				    <th>报警状态</th>
				    <th>在线/离线</th>
				    <th>详细</th>
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