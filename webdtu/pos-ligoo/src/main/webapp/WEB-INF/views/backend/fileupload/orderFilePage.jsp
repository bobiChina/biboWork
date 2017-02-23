<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_orderfile"/>
	<title>订单软件管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/doc.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/easyui.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/icon.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/css4easyui.css" />

	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>

	<script src="${ctx}/static/js/jquery.easyui.min.js"></script>
	<script src="${ctx}/static/js/easyui-lang-zh_CN.js"></script>
	<script src="${ctx}/static/js/jquery.jdirk.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.combo.js"></script>
    <script src="${ctx}/static/js/uploadify/jquery.uploadify.js"></script>
    <script src="${ctx}/static/js/jquery.euploadify.js"></script>

	<style>
    .manage-select {
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
		 width: 146px;
		 height: 18px;
		 line-height: 18px;
		 border: none;
		 margin-top: 2px;
	 }

	#show_button .l-btn {
		background: #E0E0E0;
		margin-right:20px;
	}


	#show_orderInfoDiv {
		font-size: 14px;
	}
	#show_orderInfoDiv2 {
		font-size: 14px;
	}

     </style>


<script type="text/javascript">
$(function(){
	//====== 用户信息tip
	$(".user-login-arrow").tipsbox({
		"tipsLeft":"-20px",
		"tipsTop":"20px"
	});

	$(".manage-page-left").mCustomScrollbar({
		//axis:"y",
		theme:"dark-3",
		advanced:{
			autoExpandHorizontalScroll:true
		},
	 	autoDraggerLength: true
	 });

	var orderArr = [];
	<c:forEach items="${orderList}" var="item" varStatus="status">
		orderArr.push({"orderno":"${item[1]}", "ordername": "${item[1]}"});
	</c:forEach>
    $('#searchorderKey_li').combobox({
		valueField: 'orderno',
		textField: 'ordername',
		data: orderArr,
    	onSelect: function(record){
			orderNoChange(record.orderno);
    	},
    	onLoadSuccess: function(){
    		$("#searchorder_dev").find(".combo").addClass("combobg");
            $("#searchorder_dev").find(".combo-text").addClass("combobg");
			//默认显示value的内容
			var p = $("input[name='searchorderKey_li']").closest("span.combo,div.combo-p,div.menu");//向上查找
			var tipObj = p.find(".combo-text");
			var defaultValue = tipObj.val();
			$(tipObj).on({
				focus:function(){
					if($(this).val() == defaultValue ){
						$(this).val("");
					}
				},
				blur:function(){
					if($(this).val() == ''){
						$(this).val(defaultValue);
					}
				}
			});
		 }
    });
});

function orderNoChange(id){
	if(id == "-1"){
		return;
	}
	$("#selOrderNo").val(id);
	//修改样式
	$("#orderdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-leftactive");
		if($(this).attr("orderLIData") == id){
			$(this).addClass("manage-leftactive");
		}
    });

	// 当前内容项改为默认第一项
	// $('#orderInfoShow').accordion('select',"基本信息");

	$.post("${ctx}/order/getOrderInfo?rmd=" + new Date().getTime(), {orderno:id},function(data){
		if(data != "-1"){
			var result = eval(data);
            var orderObj = result[0];
            var optUserName = result[1];
            $("#show_ldr").html(optUserName.username);
            $("#show_opttime").html(optUserName.opttime_edit);

            $("#show_orderno").html(orderObj.orderno);
            $("#show_salesman").html(orderObj.salesman);

            $("#show_corpName").html(orderObj.corp.corpName);
            $("#show_quantity").html(orderObj.quantity + "套");
            $("#edit_quantity").val(orderObj.quantity);
            $("#show_projectNote").html(orderObj.projectNote);
            $("#edit_projectNote").val(orderObj.projectNote);

            $("#show_typeName").html(orderObj.vehicleType.typeName);
            $("#show_modelName").html(orderObj.vehicleModel.modelName);

            $("#show_factoryName").html(orderObj.batteryModel.factoryName);
            $("#show_batteryType").html(orderObj.batteryModel.batteryType.typeName);
            $("#show_batteryNumber").html(orderObj.batteryModel.batteryNumber + "串");
            $("#show_capacity").html(orderObj.batteryModel.capacity + "AH");
		}
	});
	toBackPage();

}

function showEditIcon(obj, type){
	if(type == 1){
		$(obj).find('.handle').show();
	}else{
		$(obj).find('.handle').hide();
	}
}

function showDelIcon(obj, type){
	$("#orderdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-lefthover")
	});

	if(type == 1){
		$(obj).find('.handle').show();
		$(obj).addClass("manage-lefthover");
	}else{
		$(obj).find('.handle').hide();
	}
}

function showDelIcon_dtu(obj, type){
	if(type == 1){
		$(obj).find('.handle').show();
	}else{
		$(obj).find('.handle').hide();
	}
}


function toEditPage(){
	$("#orderInfoShow").css("display", "none");
	$("#orderAddForm").css("display", "none");
	$("#orderEditForm").css("display", "block");
}

function toAddPage(){
	$("#orderInfoShow").css("display", "none");
	$("#orderEditForm").css("display", "none");
	$("#orderAddForm").css("display", "block");
	$("#orderAddForm")[0].reset();
	$("#userIdAdd").val("");
	$("#userAddType").val("");


}

function toBackPage(){
	$("#orderInfoShow").css("display", "block");
	$("#orderEditForm").css("display", "none");
	$("#orderAddForm").css("display", "none");
	$("#userAddType").val("");
}

alert(1);
</script>
</head>
<body>
<input type="hidden" id="selOrderNo" name="selOrderNo" value="" />
<input type="hidden" id="userDtu_old" name="userDtu_old" value="" />
<div class="body-outer dclear clearfix" style="width:1020px;">
		<div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
			<div class="page-table-body" style="width:1020px;">
				<!-- ---------------------------head start -------------------------- -->
				<div class="history-save-table-head-backend">
					<div class="history-save-table-head-outer">
						<div class="manage-page-head-left dleft">
							<div class="dleft" style="width:150px;" onclick='findOrderLi(-999)'><a href="javascript:void(0)" style="color:#FFF; font-size:14px;">订单列表</a></div>
						</div>
						<div class="manage-page-head-right dright" >
							<div class="dleft" style="width:670px;">	订单信息</div>
						</div>
					</div>
				</div>
				<!-- ---------------------------head end -------------------------- -->
				<div class="historty-table-con-outer-backend dclear">
					<div class="manage-page-inner clearfix" style="width:1010px;">
						<div class="dleft" style="background:#E0E0E0;">
							<div style="background:#E0E0E0; width:197px; height:37px;text-align:center; margin-top:67px; margin-left:3px;border-bottom: 1px solid #cccccc;" id="searchorder_dev">
								<input id="searchorderKey_li" name="searchorderKey_li" style="width:160px; height:30px;padding:6px 12px;" value="输入订单编号搜索" />
							</div>
							<div class="manage-page-left" style="height:596px; margin-bottom: 0px !important;">
								<ul id="orderdata">
									<c:forEach items="${orderList}" var="item" varStatus="status">
										<li orderLIData="${item[1]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if>>
											<a href="javascript:;" onclick="orderNoChange('${item[1]}')" <c:if test="${item[2] == 2}">title='已删除'</c:if>  ><c:if test="${item[2] == 2}"><font color='red'></c:if>${item[1]}<c:if test="${item[2] == 2}"></font></c:if></a>
											<c:if test="${status.count == 1}">
											<c:if test="${empty orderShow}">
											 <script type="text/javascript">
												$(function (){
													orderNoChange('${item[1]}');
												});
											 </script>
											 </c:if>
											 </c:if>
										 </li>
									</c:forEach>
								</ul>
							</div>
						</div>
						<div class="manage-page-right-backend dright easyui-accordion" id="orderInfoShow" style="display:none; background:#FFF;margin-top:57px; height:627px;width:810px;" >
							<div id="show_orderInfoDiv" class="manage-page-item" style="width:810px;" title="订单概况" data-options="iconCls:'icon-ok',selected:true">
                                <div style="width:810px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">基本信息</b></div>
								<div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;margin-top:1px; margin-bottom:10px;">
									<table class="dclear" style="width:810px;" >
										<tr>
											<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;" id="show_orderno"></td>
											<td style="text-align:right; width:90px; border:0px;">客户名称：</td>
											<td style="text-align:left; border:0px;" colspan="3" id="show_corpName"></td>
										</tr>
										<tr>
											<td style="text-align:right; width:90px; border:0px; height:37px;">订货数量：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;" id="show_quantity"></td>
											<td style="text-align:right; width:90px; border:0px;">项目描述：</td>
											<td style="text-align:left; border:0px;" colspan="3" id="show_projectNote"></td>
										</tr>
										<tr>
											<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
											<td style="text-align:left; border:0px; height:37px;" id="show_salesman"> </td>
											<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;" id="show_ldr"> </td>
											<td style="text-align:right; width:90px; border:0px; height:37px;">创建时间：</td>
											<td style="text-align:left; border:0px; height:37px;" id="show_opttime"> </td>
										</tr>
									</table>
								</div>
								<div style="width:810px; text-align:left; height:20px; margin-top:8px;"><b style="margin-left:20px;">车辆信息</b></div>
								<div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;margin-top:20px;">
									<table class="dclear" style="width:810px;" >
										<tr>
											<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;" id="show_typeName"> </td>
											<td style="text-align:right; width:90px; border:0px; height:37px;">车辆型号：</td>
											<td style="text-align:left; border:0px; height:37px;" id="show_modelName"> </td>
											<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
											<td style="text-align:left; border:0px; height:37px;" id="show_factoryName"> </td>
										</tr>
										<tr>
											<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;" id="show_batteryType"> </td>
											<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;" id="show_batteryNumber"> </td>
											<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
											<td style="text-align:left; border:0px; height:37px;" id="show_capacity"> </td>
										</tr>
									</table>
								</div>
							</div>
							<div class="page-table-body" align="center" style="padding:0px;" title="文件信息" data-options="iconCls:'icon-ok',tools:[{ iconCls:'icon-edit',
																																											   handler:function(){
																																												   toEditPage();
																																											   }
																																										   }]">

								文件

							</div>
						</div>

						<form id="orderEditForm" action="javascript:;" style="display: none;margin-top:57px;">
							<input type="hidden" id="edit_id" name="id" value="" />
							<input type="hidden" id="edit_orderno_" name="orderno" value="" />
							<input type="hidden" id="edit_optTime_" name="optTime" value=""/>
							<input type="hidden" id="edit_optUser" name="optUser" value=""/>
							<input type="hidden" id="edit_status" name="status" value=""/>
							<input type="hidden" id="edit_optUserName" name="optUserName" value=""/>

							<input type="hidden" id="edit_batteryModel_id" name="batteryModel.id" value=""/>
							<input type="hidden" id="edit_batteryModel_batteryName" name="batteryModel.batteryName" value=""/>
							<input type="hidden" id="edit_vehicleModel_vehicleType_id" name="vehicleModel.vehicleType.id" value=""/>
							<input type="hidden" id="edit_vehicleModel_modelName" name="vehicleModel.modelName" value=""/>
							<input type="hidden" id="edit_vehicleModel_old" name="edit_vehicleModel_old" value=""/>
							<input type="hidden" id="edit_checkOrderNo" name="edit_checkOrderNo" value="1"/>
							<input type="hidden" id="edit_corp_id" name="corp.id" value=""/>
							<div class="easyui-panel" style="border-bottom:0px;width:810px; background:#FFF;height:565px;" align="center" >
									<div class="easyui-panel" title="主控模块" style="width:785px;padding:1px;"
																				data-options="iconCls:'icon-save',tools:'#main_tt'">
										<ul>
											<li>
											<div class="dleft">
											设备型号：<select id="main_device" name="main_device">
												<option value="BC52B">BC52B</option>
												<option value="BC54B">BC54B</option>
											</select>
											</div>
											<div class="dright">
											<input id="euploadify5" name="euploadify4" class="easyui-euploadify" type="text" style="margin-left:180px;" data-options="
																																   width: 450,
																																   //height: 400,
																																   multi: false,
																																   multiTemplate: 'bootstrap',
																																   auto: true,
																																   showStop: false,
																																   showCancel: true,
																																   tooltip: false,
																																   required: true,
																																   formData: {orderno:'LG20160227001'},
																																   fileSizeLimit: '10MB',
																																   fileTypeExts: '*.cfg; *.s19; *.csf',
																																   swf: '${ctx}/static/js/uploadify/uploadify.swf',
																																   uploader: '${ctx}/fileOperate/uploadOrderConfigFile'" />
											</div>
											</li>
										</ul>
									</div>
									<div id="main_tt">
											<a href="javascript:void(0)" class="icon-add" onclick="javascript:alert('add')"></a>
											<a href="javascript:void(0)" class="icon-hamburg-busy" style="margin-right:10px;" onclick="javascript:alert('help')"></a>
									</div>

									<div class="easyui-panel" title="从控模块" style="width:785px;height:270px;padding:1px;"
												data-options="iconCls:'icon-save',tools:'#bmu_tt'">
											<p style="font-size:14px">jQuery EasyUI framework helps you build your web pages easily.</p>
											<ul>
												<li>easyui is a collection of user-interface plugin based on jQuery.</li>
												<li>easyui provides essential functionality for building modem, interactive, javascript applications.</li>
												<li>using easyui you don't need to write many javascript code, you usually defines user-interface by writing some HTML markup.</li>
												<li>complete framework for HTML5 web page.</li>
												<li>easyui save your time and scales while developing your products.</li>
												<li>easyui is very easy but powerful.</li>
											</ul>
									</div>
									<div id="bmu_tt">
											<a href="javascript:void(0)" class="icon-add" onclick="javascript:alert('add')"></a>
											<a href="javascript:void(0)" class="icon-hamburg-busy" style="margin-right:10px;" onclick="javascript:alert('help')"></a>
									</div>

									<div class="easyui-panel" title="绝缘检测及DTU模块" style="width:785px;height:120px;padding:1px;;"
												data-options="iconCls:'icon-save',tools:'#LDM_tt'">
											<p style="font-size:14px">jQuery EasyUI framework helps you build your web pages easily.</p>
											<ul>
												<li>easyui is a collection of user-interface plugin based on jQuery.</li>
												<li>easyui provides essential functionality for building modem, interactive, javascript applications.</li>
												<li>using easyui you don't need to write many javascript code, you usually defines user-interface by writing some HTML markup.</li>
												<li>complete framework for HTML5 web page.</li>
												<li>easyui save your time and scales while developing your products.</li>
												<li>easyui is very easy but powerful.</li>
											</ul>
									</div>
									<div id="LDM_tt">
											<a href="javascript:void(0)" class="icon-add" onclick="javascript:alert('add')"></a>
											<a href="javascript:void(0)" class="icon-hamburg-busy" style="margin-right:10px;" onclick="javascript:alert('help')"></a>
									</div>
							</div>
							<div class="manage-page-right-backend dright" style="margin-top: 5px; width:810px; text-align:center;">
								<input type="button" value=" " onclick="return updateOrder('edit')" class="manage-page-submit-button2">
							</div>
						</form>

						<div class="manage-page-right-backend dright">
							<form id="orderAddForm" action="javascript:;" style="display: none; height:632px;">
								<input type="hidden" id="add_id" name="id" value="" />

								<input type="hidden" id="add_dtuUser_id" name="dtuUser.id" value=""/>
								<input type="hidden" id="add_optTime_" name="optTime" value="1900-01-01 23:59:59"/>
								<input type="hidden" id="add_optUser" name="optUser" value="10"/>
								<input type="hidden" id="add_status" name="status" value="0"/>

								<input type="hidden" id="add_batteryModel_id" name="batteryModel.id" value=""/>
								<input type="hidden" id="add_batteryModel_batteryName" name="batteryModel.batteryName" value=""/>
								<input type="hidden" id="add_vehicleModel_vehicleType_id" name="vehicleModel.vehicleType.id" value=""/>
								<input type="hidden" id="add_vehicleModel_modelName" name="vehicleModel.modelName" value=""/>
								<input type="hidden" id="add_checkOrderNo" name="add_checkOrderNo" value="0"/>

								<div class="manage-page-item" style="border-bottom:0px;width:810px; background:#FFF; margin-top:57px;">
									<div class="manage-page-item-inner clearfix" style="width:810px;">
										<div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;">
											<div style="padding-left:20px; width:810px; text-align:left; margin-top:15px; height:25px;"><b>基本信息</b></div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="add_orderno" name="orderno" value="" maxlength="20" onkeyup="checkOrderNo();" /><font color="red" style="margin-left:6px;">*</font>
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">客户名称：</td>
													<td style="text-align:left; border:0px; height:37px;" colspan="3" class="testli">
														<div class="dclear" style="width:99%;">
														<input type="text" id="searchUserKey_add" name="corp.id" style="width:411px;height:30px;" value="输入客户名称搜索" /><font color="red" style="margin-left:6px;">*</font>
														</div>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">订货数量：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="add_quantity" name="quantity" value="" maxlength="6" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('edit');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">项目描述：</td>
													<td style="text-align:left; border:0px; height:37px;" colspan="3" >
														<input type="text" class="manage-input-text" style="width:406px;" id="add_projectNote" name="projectNote" value="" maxlength="100" /><font color="red" style="margin-left:6px;">*</font>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="add_salesman" name="salesman" value="" maxlength="10" />
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<c:out value="${user.fullName}" />
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">创建时间：</td>
													<td style="text-align:left; border:0px; height:37px;" id="showInitTime">

													</td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc; padding-bottom:5px;">
										 <div style="padding-left:20px; width:810px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>车辆信息</b></div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<select class="manage-select" id="add_vehicleType_id" name="vehicleType.id" class="form-control input-sm"  style="height:28px; width:150px;">
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
														</select><font color="red" style="margin-left:6px;">*</font>
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">车辆型号：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<select class="manage-select" id="add_vehicleModel" name="vehicleModel.id" class="form-control input-sm" style="height:28px; width:150px;" onchange="addCartype('add')">
															<option value= "-1">请选择 </option>
															<option value= "addVType">添加车辆型号</option>
														</select><font color="red" style="margin-left:6px;">*</font></td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="add_factoryName" name="batteryModel.factoryName" value="" maxlength="30" onkeyup="autoBatteryName();" /><font color="red" style="margin-left:6px;">*</font>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<select class="manage-select" id="add_batteryModel_batteryType" name="batteryModel.batteryType.id" class="form-control input-sm"  style="height:28px; width:150px;" onchange="autoBatteryName()">
															<option value= "-1">请选择</option>
															<c:forEach items="${bmList}" var="bmItem">
																<option value= "${bmItem.id}">${bmItem.typeName}</option>
															</c:forEach>
														</select><font color="red" style="margin-left:6px;">*</font>
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="add_batteryNumber" name="batteryModel.batteryNumber" value="" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('add');" onafterpaste="this.value=this.value.replace(/\D/g,'')" /><font color="red" style="margin-left:6px;">*</font>
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" class="manage-input-text" maxlength="10" id="add_capacity" name="batteryModel.capacity" value="" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('add');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
													</td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right-backend dright" style="margin-top: 20px; width:810px; text-align:center;">
											<input type="button" value=" " onclick="return updateOrder('add')" class="manage-page-submit-button2">
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<div class="page-footer-backend dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->

</body>
</html>