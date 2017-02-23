<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
	<meta name="menu" content="config"/>
	<title>管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/doc.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/css3buttons.css" />
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<style>
		.table-body select {
                background: #E0E0E0;
                color: #000;
                border: none;
                height: 22px;
                line-height: 28px;
                text-align: right;
             }

             .manage-input-text {
                 background: #E0E0E0;
                 padding-left: 5px;
                 width: 198px;
                 height: 18px;
                 line-height: 18px;

                 border: none;
                 margin-top: 2px;
             }
	</style>
<script type="text/javascript">
$(function(){
	$(".manage-page-left li").manageTab({
		"tabElement":".manage-page-item-wrap"
	});
	
	$(".manage-page-left").mCustomScrollbar({
		//axis:"y",
		theme:"dark-3",
		advanced:{   
			autoExpandHorizontalScroll:true   
		},
	 	autoDraggerLength: true
	 });
	//======
	$(".user-login-arrow").tipsbox({
		"tipsLeft":"-20px",
		"tipsTop":"20px"
	});
	$("#vehicleType").change(
			function (){
				$("#vehicleModel").empty();
				$("#vehicleModel").append("<option value='-1'>请选择</option>");				
				if ($("#vehicleType").val() == -1)
					return;
				$("#vehicleTypeId").val($("#vehicleType").val());	
				$.get("${ctx}/vehicle/vehicleModel", {typeId:$("#vehicleType").val() , pagetype:"config"},function(data){
					result = eval(data);
					$.each(result,function(index,item){
						$("#vehicleModel").append("<option value='"+item[0]+"'>"+item[1]+"</option>");
					});
					
					if ($("#modelId").val() != -1){
						$("#vehicleModel").val($("#modelId").val());
						$("#vehicleModel_show").html($("#vehicleModel").find("option:selected").text());//车型号 wly
						$("#vehicleModelId").val($("#modelId").val());
					}
					//最后添加addVType
					$("#vehicleModel").append("<option value='addVType'>添加车辆类型</option>");
				});
			});
	
	$("#vehicleModel").change(
			function (){
				$("#vehicleModelId").val($("#vehicleModel").val());
				$("#vehicleModel_show").html($("#vehicleModel").val());//车型号 wly
				$("#modelId").val($("#vehicleModel").val());
	})
});

function saveVehicle(){
	if ($("#seldtuid").val() == -1){
		showDialogAlert("请在左侧选择一个dtu");
		return;
	}
	if ($("#vehicleType").val() == -1){
		showDialogAlert("请选择车辆类型");
		return;
	}
	
	if (($("#vehicleModel").val()== -1)||($("#vehicleModel").val()== null)){
		showDialogAlert("请选择车辆型号");
		return;
	}
	if (($("#batteryType").val()== -1)||($("#batteryType").val()== null)){
		showDialogAlert("请选择电池类型");
		return;
	}
	//车辆型号text
	$("#vehicleModelName").val($("#vehicleModel").find("option:selected").text());
	//修改modelID
	$("#vehicleModelId").val($("#vehicleModel").val());
	//拼装电池名称
	autoBatteryName();

	$.post("${ctx}/vehicle/save", $("#vehicleForm").serialize(),function(data){
		//showDialogAlert("保存成功");
		//转到显示页面
		showDtuInfo();
    });
}

function uuidchange(id){
	$("#seldtuid").val(id);
	var timestamp = new Date();
	$.get("${ctx}/vehicle/getuuid", {dtuId:id,timestamp:timestamp.getTime()},function(data){
		  var result = eval(data);
		  var dtu = result[0];
		  var upUser = result[1];
		  var remarkStr = upUser.corp.corpName + "-" + upUser.fullName;
		  $("#remark_show").html(remarkStr);
		  $("#remark_show_edit").html(remarkStr);
		  if (result.length == 2){ //车辆未录入
			  var dtu = result[0];
			  $("#id").val("");
			  $("#vehicleNumber").val("");
			  $("#vehicleNumber_show").html("");//wly
			  $("#vehicleType").val(-1);
			  $("#vehicleType_show").html('未填写');//车型 wly
			  $("#vehicleModel").val(-1);
			  $("#vehicleModel_show").html('未填写');//车型号 wly
			  //$("#modelId").val(-1);
			  $("#vehicleTypeId").val(-1);
			  $("#createTime").val("");
		  }else{
			  var vehicle = result[2];
			  $("#id").val(vehicle.id);
			  $("#vehicleNumber").val(vehicle.vehicleNumber);
			  $("#vehicleNumber_show").html(vehicle.vehicleNumber == null || vehicle.vehicleNumber == undefined || vehicle.vehicleNumber == "" ? "-" : vehicle.vehicleNumber );//wly
			  $("#vehicleType").val(vehicle.vehicleType.id);
			  //获取下拉框文本信息
			  $("#vehicleType_show").html($("#vehicleType").find("option:selected").text());//车型 wly
			  $("#modelId").val(vehicle.vehicleModel.id);
			  $("#vehicleType").trigger("change");
			  
			  $("#vehicleTypeId").val(vehicle.vehicleType.id);
			  $("#createTime").val((vehicle.createTime.year-100+2000)+"-"+ (vehicle.createTime.month+1) +"-"+ vehicle.createTime.date);
		  }
		  
		  $("#factoryName").val(dtu.batteryModel.factoryName);
		  $("#factoryName_show").html(dtu.batteryModel.factoryName);//wly
		  $("#batteryName").val(dtu.batteryModel.batteryName);
		  $("#batteryName_show").html(dtu.batteryModel.batteryName);//wly
		  $("#batteryType").val(dtu.batteryModel.batteryType.id);
		  $("#batteryType_show").html(dtu.batteryModel.batteryType.typeName);//wly
		  $("#batteryNumber").val(dtu.batteryModel.batteryNumber);
		  $("#batteryNumber_show").html(dtu.batteryModel.batteryNumber);//wly
		  $("#capacity").val(dtu.batteryModel.capacity);
		  $("#capacity_show").html(dtu.batteryModel.capacity + "AH");//wly
		  $("#batteryId").val(dtu.batteryModel.id);//wly
		  $("#uuidtext").html(dtu.uuid);
		  $("#uuidtext_show").html(dtu.uuid);
		  $("#uuid").val(dtu.uuid);
		  $("#dtuId").val(dtu.id);
		  $("#dtuUserId").val(dtu.dtuUser.id);
	});
	toBackPage();
}

function toEditPage(){
	$("#dtuInfoShow").css("display", "none");
	$("#vehicleForm").css("display", "block");
}

function toBackPage(){
	$("#dtuInfoShow").css("display", "block");
	$("#vehicleForm").css("display", "none");
}

function showDtuInfo(){
	uuidchange($("#seldtuid").val());
}

function addCartype(){
	if($("#vehicleModel").val() == "addVType"){
		$("#vehicleModel").val("-1");
	}else{
		return;
	}

	var d = dialog({
        title: '添加车辆型号',
        content: '<input id="property-cartype" type="text" value="" style="width: 240px;" />',
        okValue: '确定',
        ok: function () {
            var value = $('#property-cartype').val();
            this.close(value);
            this.remove();
        }
    });
    d.addEventListener('close', function () {
        //showDialogAlert(this.returnValue);
		var returnStr = $.trim(this.returnValue);
		if(returnStr == ""){
			return;
		}
		var rs = false;
        $("#vehicleModel option").each(function(index, el){
			if(returnStr == $(el).text()){
				rs = true;
				$(el).attr("selected", true);//默认选中
			}
        });

        if(rs){
			//showDialogAlert("车辆型号已存在");
        }
        else{
			$("#vehicleModel").append("<option value='-9999'>"+ returnStr +"</option>");
			$("#vehicleModel").val("-9999");
        }
    });
    d.width(250);
    d.show();
}

function autoBatteryName(){
	var batteryTypeStr = $("#batteryType").find("option:selected").text() + "-";
	if (($("#batteryType").val()== -1)||($("#batteryType").val()== null)){
		batteryTypeStr = "";
    }
    var str = $("#factoryName").val() + "-" + batteryTypeStr
    	 + $("#batteryNumber").val() + "S-" + $("#capacity").val() + "AH";
	$("#batteryName").val(str);
}

</script>
</head>
<body>	
<input type="hidden" id="seldtuid" name="seldtuid" value="-1" />
<div class="body-outer dclear clearfix">
	<div class="form-box clearfix" style="padding-left:5px;">
		<a href="javascript:;" class="btn btn-success btn-sm" style="background-color: #C7C5C6; color:#000">设备管理</a>
		&nbsp;
		<a href="${ctx}/dtu/customer" class="btn btn-success btn-sm" style="background-color: #DBDBE7; color:#000;">客户管理</a>
	</div>
		<div class="table-box page-manage-box-outer dclear clearfix">
			<div class="table-body page-table-body">
				<div class="history-save-table-head">
					<div class="history-save-table-head-outer">
						<div class="manage-page-head-left dleft">
							DTU列表
						</div>
						<div class="manage-page-head-right dright">
							整车信息配置
						</div>
					</div>
				</div>
				<style type="text/css">
					.table-body select{ width: 202px;}
				</style>
				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner manage-page-inner clearfix">
						<div class="manage-page-left dleft" style="height:600px;">
							<ul>
								<c:forEach items="${dtus}" var="item" varStatus="status">
									<li onclick="uuidchange(${item[2]})" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> ><a href="javascript:;" >${item[1]}</a></li>
									 <c:if test="${status.count == 1}">
									 <script type="text/javascript">
									 	$(function (){
									 		uuidchange(${item[2]});
									 	});
									 </script>
									 </c:if>
								</c:forEach>							
							</ul>
						</div>
						<div class="manage-page-right dright" id="dtuInfoShow">
							<div class="manage-page-item">
								<div class="manage-page-item-inner clearfix">
									<div class="manage-page-right dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;">
										<div style="width:760px; text-align:left; margin-top:15px;height:25px;">
											<b style="margin-left:20px;">车辆信息</b>
											<div class="dright" style="padding-right:20px;">
												<a href="javascript:void(0)" class="button" onclick="toEditPage()"><strong>编辑</strong></a>
											</div>
										</div>
										<table class="dclear" style="width:760px;" >
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">UUID：</td>
												<td style="text-align:left; border:0px; height:37px;" id="uuidtext_show"></td>
											</tr>
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">车辆类型：</td>
												<td style="text-align:left; border:0px; height:37px;" id="vehicleType_show"></td>
											</tr>
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">车辆型号：</td>
												<td style="text-align:left; border:0px; height:37px;" id="vehicleModel_show"></td>
											</tr>
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">车&nbsp;架&nbsp;&nbsp;号：</td>
												<td style="text-align:left; border:0px; height:37px;" id="vehicleNumber_show"></td>
											</tr>
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">来源备注：</td>
												<td style="text-align:left; border:0px; height:37px;" id="remark_show"></td>
											</tr>
										</table>
									</div>
									<div class="manage-page-right dright" style="width:760px;">
										<div style="padding-left:20px; width:710px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>电池信息</b></div>
										<table class="dclear" style="width:760px;" >
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">电池厂商：</td>
												<td style="text-align:left; border:0px; height:37px;" id="factoryName_show"> </td>
											</tr>
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">电池类型：</td>
												<td style="text-align:left; border:0px; height:37px;" id="batteryType_show"> </td>
											</tr>
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">电池串数：</td>
												<td style="text-align:left; border:0px; height:37px;" id="batteryNumber_show"> </td>
											</tr>
											<tr>
												<td style="text-align:right; width:310px; border:0px; height:37px;">电池容量：</td>
												<td style="text-align:left; border:0px; height:37px;" id="capacity_show"> </td>
											</tr>
											<tr style="display:none;">
												<td style="text-align:right; width:310px; border:0px; height:37px;">电池名称：</td>
												<td style="text-align:left; border:0px; height:37px;" id="batteryName_show"> </td>
											</tr>
										</table>
									</div>
								</div>
							</div>
						</div>
						<form id="vehicleForm" action="javascript:;" style="display: none;">
							<input type="hidden" id="id" name="id"/>
							<input type="hidden" id="vehicleTypeId" name="vehicleType.id"/>
							<input type="hidden" id="vehicleModelId" name="vehicleModel.id"/>
							<input type="hidden" id="vehicleModelName" name="vehicleModel.modelName"/>
							<input type="hidden" id="createTime" name="createTime"/>
							<input type="hidden" id="dtuUserId" name="dtuUser.id"/>
							<input type="hidden" id="uuid" name="uuid"/>
							<input type="hidden" id="dtuId" name="dtu.id"/>
							<input type="hidden" id="modelId" value="-1"/>		
							<input type="hidden" id="batteryId" name="batteryId" value="-1"/>
							<input id="id" type="hidden"/>
							<div class="manage-page-right dright">
								<div class="manage-page-item">
									<div class="manage-page-item-inner clearfix">
										<div class="manage-page-right dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;">
											<div style="width:760px; text-align:left; margin-top:15px;height:25px;">
												<b style="margin-left:20px;">车辆信息</b>
											</div>
											<table class="dclear" style="width:760px;" >
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">UUID：</td>
													<td style="text-align:left; border:0px; height:37px;" id="uuidtext"></td>
												</tr>
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">车辆类型：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<select id="vehicleType" class="form-control input-sm"  style="height:28px">
															<option value= "-1">请选择</option>
															<c:forEach items="${vtList}" var="item">
																<c:if test="${item.typeName != '其他'}">
																<option value= "${item.id}">${item.typeName}</option>
																</c:if>
															</c:forEach>
															<c:forEach items="${vtList}" var="item">
																<c:if test="${item.typeName == '其他'}">
																<option value= "${item.id}">${item.typeName}</option>
																</c:if>
															</c:forEach>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">车辆型号：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<select id="vehicleModel" class="form-control input-sm" style="height:28px" onchange="addCartype()">
															<option value= "-1">请选择 </option>
															<option value= "addVType">添加车辆型号</option>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">车&nbsp;架&nbsp;&nbsp;号：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="vehicleNumber" name="vehicleNumber" class="manage-input-text" maxlength="25" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">来源备注：</td>
													<td style="text-align:left; border:0px; height:37px;" id="remark_show_edit"></td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right dright" style="width:760px;">
											<div style="padding-left:20px; width:710px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>电池信息</b></div>
											<table class="dclear" style="width:760px;" >
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">电池厂商：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="factoryName" name="factoryName" value="" class="manage-input-text" maxlength="30" onkeyup="autoBatteryName();" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">电池类型：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<select name="batteryType" id="batteryType" class="form-control input-sm"  style="height:28px" onchange="autoBatteryName()">
															<option value= "-1">请选择</option>
															<c:forEach items="${bmList}" var="bmItem">
																<option value= "${bmItem.id}">${bmItem.typeName}</option>
															</c:forEach>
														</select>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">电池串数：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="batteryNumber" name="batteryNumber" value="" class="manage-input-text" onkeyup="value=value.replace(/[^\d\.]/g,'');autoBatteryName();" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:310px; border:0px; height:37px;">电池容量：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="capacity" name="capacity" value="" class="manage-input-text" onkeyup="value=value.replace(/[^\d\.]/g,'');autoBatteryName();" />
													</td>
												</tr>
												<tr style="display:none;">
													<td style="text-align:right; width:310px; border:0px; height:37px;">电池名称：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="batteryName" name="batteryName" value="" class="manage-input-text2" />
													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
								<div style="width: 100%; margin-top: 10px;">
									<input type="button" value=" " onclick="return saveVehicle()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>

			<div class="table-footer page-footer dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->


</body>
</html>