<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="config"/>
<title>配置</title>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/bootstrap.min.css"></link>
<script src="${ctx}/static/js/bootstrap.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
<script type="text/javascript">
$(function(){

	$("#updateUser").click(
			function(){
				$.post("${ctx}/user/updateUser", {id:$("#userId").val(),email:$("#useremail").val(),relation:$("#relation").val(),fullName:$("#fullName").val()},function(data){
					if (data == 1){
						alert("修改成功");
					}
		        });	
			});
	
	$("#vehicleType").change(
			function (){
				$("#vehicleModel").empty();
				$("#vehicleModel").append("<option value='-1'>请选择</option>");				
				if ($("#vehicleType").val() == -1)
					return;
				$("#vehicleTypeId").val($("#vehicleType").val());	
				$.get("${ctx}/vehicle/vehicleModel", {typeId:$("#vehicleType").val()},function(data){
					result = eval(data);
					$.each(result,function(index,item){
						$("#vehicleModel").append("<option value='"+item.id+"'>"+item.modelName+"</option>");
					});
					
					if ($("#modelId").val != -1){
						$("#vehicleModel").val($("#modelId").val());
						$("#vehicleModelId").val($("#modelId").val());
					}
						
				});
			});
	
	$("#vehicleModel").change(
			function (){
				$("#vehicleModelId").val($("#vehicleModel").val());
				$("#modelId").val($("#vehicleModel").val());
	})
	
	$("#uuidList").change(
		function (){
			
			$.get("${ctx}/vehicle/getuuid", {dtuId:$("#uuidList").val()},function(data){
				  var result = eval(data);
				  var dtu = result[0];
				  if (result.length == 1){ //车辆未录入
					  var dtu = result[0];
					  $("#id").val("");
					  $("#vehicleNumber").val("");
					  $("#vehicleType").val(-1);
					  $("#vehicleModel").val(-1);
					  //$("#modelId").val(-1);
					  $("#vehicleTypeId").val(-1);
					  $("#createTime").val("");
				  }else{
					  var vehicle = result[1];
					  $("#id").val(vehicle.id);
					  $("#vehicleNumber").val(vehicle.vehicleNumber);
					  $("#vehicleType").val(vehicle.vehicleType.id);
					  $("#modelId").val(vehicle.vehicleModel.id);
					  $("#vehicleType").trigger("change");
					  
					  $("#vehicleTypeId").val(vehicle.vehicleType.id);
					  $("#createTime").val((vehicle.createTime.year-100+2000)+"-"+ (vehicle.createTime.month+1) +"-"+ vehicle.createTime.date);
				  }
				  
				  $("#factoryName").html(dtu.batteryModel.factoryName);
				  if (dtu.batteryModel.batteryType ==1)
					 	 $("#batteryType").html("铁电池");
				  else if (dtu.batteryModel.batteryType ==2)
						 $("#batteryType").html("锂电池");
				  
				  $("#batteryNumber").html(dtu.batteryModel.batteryNumber);
				  $("#capacity").html(dtu.batteryModel.capacity);
				  $("#uuidtext").val(dtu.uuid);
				  $("#uuid").val(dtu.uuid);
				  $("#dtuId").val(dtu.id);
				  $("#dtuUserId").val(dtu.dtuUser.id); 
			});
		});
});

function saveVehicle(){
	if ($("#modelId").val() == -1){
		alert("请在左侧选择一个dtu");
		return;
	}
	if ($("#vehicleType").val() == -1){
		alert("请选择车辆类型");
		return;
	}
	
	if (($("#vehicleModel").val()== -1)||($("#vehicleModel").val()== null)){
		alert("请选择车辆型号");
		return;
	}
	
	$.post("${ctx}/vehicle/save", $("#vehicleForm").serialize(),function(data){
		alert("配置成功");
    });
}

function savePassword(){
	if ($("#newPass").val() != $("#newPassRe").val()){
		alert("密码不一致")
		return;
	}
	if ($("#newPass").val()==""){
		alert("密码不能为空");
		return;
	}
		
	$.post("${ctx}/user/updatePassword", {userId:$("#userId").val(),newPass:$("#newPass").val(),oldPass:$("#oldPass").val()},function(data){
		if (data =="2"){
			alert("原密码错误");
			return;
		}
			
		alert("密码修改成功");
		$("#newPass").val("");
		$("#oldPass").val("");
		$("#newPassRe").val("");
		
    });
}
</script>
</head>
<body>	
<div  class="container">
	<ul class="nav nav-tabs" role="tablist">
	  <li class="active"><a href="#vehicleconfig" role="tab" data-toggle="tab">车辆配置</a></li>
	  <li><a href="#userconfig" role="tab" data-toggle="tab">修改密码</a></li>
	  <li><a href="#passwordconfig" role="tab" data-toggle="tab">用户信息</a></li>
	</ul>
	
	<div class="tab-content">
	    <div class="tab-pane active" id="vehicleconfig">
	    	<div class="row" >
	    		<div class="col-md-1">	
	    		</div>
				<div class="col-md-3" style="padding-top:20px">	
					<select id="uuidList" size="12" class="form-control">  
						<c:forEach items="${dtus}" var="item">
							<option value= "${item.id}">${item.uuid}</option>
						</c:forEach>
					</select>  
				</div>


				<div class="col-md-4" style="padding-top:20px">	
					<ul class="list-group">
							<li class="list-group-item">电池信息</li>
							<li class="list-group-item">厂&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;商：<span id="factoryName"></span></li>
							<li class="list-group-item">电池类型：<span id="batteryType"></span></li>
							<li class="list-group-item">电池串数：<span id="batteryNumber"></span></li>
							<li class="list-group-item">电池容量：<span id="capacity"></span></li>
										
					</ul>
	    		</div>	
	    		<div class="col-md-3" style="padding-top:20px">
					<form id="vehicleForm" action="post">
						<input type="hidden" id="id" name="id"/>
						<input type="hidden" id="vehicleTypeId" name="vehicleType.id"/>
						<input type="hidden" id="vehicleModelId" name="vehicleModel.id"/>
						<input type="hidden" id="createTime" name="createTime"/>
						<input type="hidden" id="dtuUserId" name="dtuUser.id"/>
						<input type="hidden" id="uuid" name="uuid"/>
						<input type="hidden" id="dtuId" name="dtu.id"/>
						<input type="hidden" id="modelId" value="-1"/>		
						<input id="id" type="hidden"/>
						
						<ul style="list-style-type:none">
							<li style="margin-bottom:20px">
								<div class="input-group">
								  <span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;UUID：</span>
								  <input type="text" id="uuidtext" class="form-control input-sm" readonly/>
								</div>
							</li>
							<li style="margin-bottom:20px">
								<div class="input-group">
								    <span class="input-group-addon">车辆类型：</span>
									<select id="vehicleType" class="form-control input-sm">  
										<option value= "-1">请选择</option>  
										<c:forEach items="${vtList}" var="item">
											<option value= "${item.id}">${item.typeName}</option>
										</c:forEach>
									</select> 
								</div>
							</li>												
							<li style="margin-bottom:20px">
								<div class="input-group">
								    <span class="input-group-addon">车辆型号：</span>
									<select id="vehicleModel" class="form-control input-sm">  
										<option value= "-1">请选择 </option>  
									</select>  
								</div>
							</li>	
							<li style="margin-bottom:20px">
								<div class="input-group">
								    <span class="input-group-addon">&nbsp;&nbsp;&nbsp;车架号：</span>
									<input type="text" id="vehicleNumber" name="vehicleNumber" class="form-control input-sm"/>
								</div>
							</li>	
							<li style="text-align:center"><input type="button" value="保存" onclick="return saveVehicle();" class="btn btn-success btn-sm"/></li>	
						</ul>
					</form>
				</div>	  
				<div class="col-md-1">	
	    		</div>
	   		</div>
	    </div>
		<div class="tab-pane" id="userconfig"  style="padding-top:20px">
			<div class="col-md-4">	
	    	</div>
	    	<div class="panel panel-default col-md-4">
				<div class="panel-body ">
					<form>
					<ul style="list-style-type:none">
						<li style="margin-bottom:20px">
							<div class="input-group">
							  <span class="input-group-addon">原密码：</span>
							  <input type="password" id = "oldPass" class="form-control input-sm"/>
							</div>
						</li>
						<li style="margin-bottom:20px">
							<div class="input-group">
							  <span class="input-group-addon">新密码：</span>
							  <input type="password" id = "newPass" name="newPass" class="form-control input-sm" />
							</div>
						</li>
						<li style="margin-bottom:20px">
							<div class="input-group">
							  <span class="input-group-addon">新密码：</span>
							  <input type="password" id = "newPassRe" class="form-control input-sm"/>
							</div>
						</li>
						<li style="text-align:center"><input type="button" value="修改" onclick="savePassword()" class="btn btn-success btn-sm"/></li>
					</ul>
					</form>		
				</div>	
	    	</div>
			<div class="col-md-4">	
	    	</div>	    		    		
		</div>
	    <div class="tab-pane" id="passwordconfig" style="padding-top:20px">

			<div class="col-md-2">	
   			</div>
	    	<div class="panel panel-default col-md-8">
				<div class="panel-body ">	    	
					<div class="col-md-5">	
							<ul class="list-group">
								<li class="list-group-item">公司名称：<span  id = "corpName">${user.corp.corpName}</span></li>
								<li class="list-group-item">联系电话：<span  id = "phone">${user.corp.phone}</span></li>
								<li class="list-group-item">电子邮件：<span  id = "email">${user.corp.email}</span></li>
								<li class="list-group-item">联系方式：<span  id = "addr">${user.corp.addr}</span></li>
							</ul>							
	    			</div>
			    	<div class="col-md-5">	
							<ul style="list-style-type:none">
								<li style="margin-bottom:20px">
									<div class="input-group">
									  <span class="input-group-addon">用户姓名：</span>
									  <input type="text" id = "fullName" value="${user.fullName}"/>
									</div>
								</li>							
								<li style="margin-bottom:20px">
									<div class="input-group">
									  <span class="input-group-addon">电子邮件：</span>
									  <input type="text" id = "useremail" value="${user.email}"/>
									</div>
								</li>	
								<li style="margin-bottom:20px">
									<div class="input-group">
									  <span class="input-group-addon">联系电话：</span>
									  <input type="text" id = "relation" value="${user.relation}"/>
									</div>
								</li>	
								<input id="userId" type="hidden" value="${user.id}"/>
								<li style="text-align:center"><input type="button" id="updateUser" value="保存"  class="btn btn-success btn-sm"/></li>
							</ul>	    	
			
			    	</div>
			    	<div class="col-md-4">	
	    			</div>
		    	</div>
	    	</div>

			<div class="col-md-2">	
   			</div>

		</div>	    
		</div>
	</div>	
</div>


</body>
</html>