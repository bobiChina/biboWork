<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_order"/>
	<title>订单管理</title>
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
	<script src="${ctx}/static/js/My97DatePicker/WdatePicker.js"></script>
	<script src="${ctx}/static/js/jquery.my97.js"></script>
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

	 $("#ceshidate").my97({
		minDate:'2015-12-11',
		isShowToday:true,
		maxDate:'2016-01-20',
		isShowClear:false ,
		qsEnabled:false ,
		dateFmt:'yyyy-MM-dd HH:mm:ss'
	 });

});


function showFun(){

	//alert($("#ceshidate2").val());
	//alert($("#ceshidate").combo("getValue"));

	//alert($("#euploadify1").val());
	var t = $("#euploadify4");
	var values = t.euploadify("getValues");
	alert("values=" + values);

	var t5 = $("#euploadify5");
    	var values5 = t5.euploadify("getValues");
    	alert("values5555=" + values5);

	//var obj = t.euploadify("queueDOM");
	//alert(obj);
	//alert(JSON.stringify(obj));
	//var obj2 = t.euploadify("uploadify");
	//alert(obj2);
	//alert(JSON.stringify(obj2));

	//var obj3 = t.euploadify("getUploadify");
	//alert(JSON.stringify(obj3));
}

function showValiFun(){

	var t = $("#euploadify4");
	var vailBoo = t.euploadify("validate");
	alert("values=" + JSON.stringify(vailBoo));

}
function showArrFun(){

	$(".euploadify-f").each(function(i, e){
		alert($(this).attr("id") + "====" + $(this).euploadify("getValues"));
	});

}

function addFileUploadFun(){
	var t = $("#euploadify6");

	t.euploadify({
		width: 600,
		multi: true,
		multiTemplate: 'bootstrap',
		auto: false,
		showStop: false,
		showCancel: true,
		tooltip: false,
		required: true,
		swf: '${ctx}/static/js/uploadify/uploadify.swf',
		uploader: '${ctx}/fileOperate/uploadOrderConfigFile' });
}

function checkFileType(){

}

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
							<div class="dright" style="width:24px;display:none;" onclick='toAddPage()' ><img src="${ctx}/static/images/add.png" style="width:20px;margin-top:12px;" title="添加订单" alt="添加订单" /></div>
						</div>
						<div class="manage-page-head-right dright" >
							<div class="dleft" style="width:670px;">	订单信息</div>
							<div class="dright" style="width:80px; margin-top:10px; display:none;" >
								<img src="${ctx}/static/images/edit.png" style="width:24px;cursor: pointer;" title="修改订单" alt="修改订单" onclick="toEditPage()" />
								<img src="${ctx}/static/images/dtuadd.png" style="width:24px;cursor: pointer; margin-left:8px;" title="设备绑定" alt="设备绑定" onclick="toScanPage()" />
							</div>
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
							<div class="manage-page-left" style="height:565px; margin-bottom: 0px !important;">
								<ul id="orderdata">
									<c:forEach items="${orderList}" var="item" varStatus="status">
										<li orderLIData="${item[1]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;' <c:if test="${item[2] < 2}"> onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' </c:if> >
											<a href="javascript:;" onclick="orderNoChange('${item[1]}')" <c:if test="${item[2] == 2}">title='已删除'</c:if>  ><c:if test="${item[2] == 2}"><font color='red'></c:if>${item[1]}<c:if test="${item[2] == 2}"></font></c:if></a>
											<div class="handle" style="position: absolute; right: 5px; margin-top: -30px; display: none;">
											<img onclick="delOrderFun('${item[0]}','${item[1]}', ${item[2]})" src="${ctx}/static/images/del.png" style="position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; ">
											</div>
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
							<div style="background:#E0E0E0; width:200px; height:50px;text-align:center;padding-top:5px;">
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="toAddPage()"><strong>&nbsp;&nbsp;创建新订单&nbsp;&nbsp;</strong></a>
							</div>
						</div>


						<div class="manage-page-right-backend dright easyui-accordion" id="orderInfoShow" style="display:block; background:#FFF;margin-top:57px; height:650px;width:810px;" >
							<div id="show_orderInfoDiv" class="manage-page-item" style="width:810px;" title="订单概况" data-options="iconCls:'icon-ok',selected:true,tools:[{ iconCls:'icon-edit',
                                                                                                                                                 handler:function(){
                                                                                                                                                     toEditPage();
                                                                                                                                                 }
                                                                                                                                             }]">
                                <div style="width:810px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">基本信息</b></div>
								<div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;margin-top:1px; margin-bottom:10px;">
									<!-- 文件上传 -->
									<table class="dclear" style="width:810px;" >
										<tr>
											<td style="text-align:right; width:120px; border:0px; height:37px;">启用时间：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;"><input type="text" id="ceshidate" class="easyui-my97" value="2016-01-16 00:00:01" style="width:180px;" /></td>
											<td style="text-align:right; width:120px; border:0px;">日期时间：</td>
											<td style="text-align:left; border:0px;"><input type="text" class="easyui-my97" id="ceshidate2" datefmt="yyyy-MM-dd HH:mm:ss" style="width:180px;" isShowClear="true" /></td>
										</tr>

										<tr>
											<td style="text-align:right; width:120px; border:0px; height:37px;">操作：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;" colspan="3" >
											<input type="button" onclick="showFun()" value="显示" />
											<input type="button" onclick="showValiFun()" value="验证" />
											<input type="button" onclick="showArrFun()" value="显示多个" />
											<input type="button" onclick="addFileUploadFun()" value="添加" />

											</td>

										</tr>
										<tr>
											<td style="text-align:right; width:120px; border:0px; height:37px;">操作：</td>
											<td>

											</td>
										</tr>
                                    </table>

										<hr />
										<input id="euploadify4" name="euploadify4" class="easyui-euploadify" type="text" style="margin-left:180px;" data-options="
											   width: 600,
											   //height: 400,
											   multi: true,
											   multiTemplate: 'bootstrap',
											   auto: false,
											   showStop: false,
											   showCancel: true,
											   tooltip: false,
											   required: true,
											   swf: '${ctx}/static/js/uploadify/uploadify.swf',
											   uploader: '${ctx}/fileOperate/uploadOrderConfigFile'" />
										<hr />
										<input id="euploadify5" name="euploadify4" class="easyui-euploadify" type="text" style="margin-left:180px;" data-options="
                                        											   width: 600,
                                        											   //height: 400,
                                        											   multi: true,
                                        											   multiTemplate: 'bootstrap',
                                        											   auto: false,
                                        											   showStop: false,
                                        											   showCancel: true,
                                        											   tooltip: false,
                                        											   required: true,
                                        											   swf: '${ctx}/static/js/uploadify/uploadify.swf',
                                        											   uploader: '${ctx}/fileOperate/uploadOrderConfigFile'" />
                                        										<hr />
                                        <input id="euploadify6" name="euploadify6" type="text" style="margin-left:180px;"  />
                                                                                										<hr />
										<lable id='show_info_upload' style="margin-left:80px;"></lable>

									<!-- 文件上传 -->
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
							<div class="page-table-body" align="center" style="padding:0px;" title="设备信息" data-options="iconCls:'icon-ok',tools:[{ iconCls:'icon-hamburg-busy',
																																							  handler:function(){
																																								  delSNFun();
																																							  }
																																						  }]">
								<table id="dg" style="width:810px;height:585px;">
									<thead>
										<tr>
											<th data-options="field:'id',width:50,align:'center', halign:'center', checkbox:true"></th>
											<th data-options="field:'prodtype',width:70,align:'center', halign:'center', sortable:true">产品型号</th>
											<th data-options="field:'dtu_uuid',width:120,align:'center', halign:'center', sortable:true">UUID</th>
											<th data-options="field:'sn',width:120,align:'center', halign:'center', sortable:true">产品SN</th>
											<th data-options="field:'temp_sn',width:120,align:'center', halign:'center', sortable:true">模块SN</th>
											<th data-options="field:'hw_version',width:65,align:'center', halign:'center', sortable:true">版本号</th>
											<th data-options="field:'userid',width:65,align:'center', halign:'center'">操作人</th>
											<th data-options="field:'opttime',width:145,align:'center', halign:'center', sortable:true">操作时间</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
						<shiro:hasRole name="admin">
						<div class="manage-page-right-backend" id="scanInfoShow" style="display:none;">
							<div class="manage-page-item" style="width:810px;">
								<div style="height:37px; width:810px;">
									<div class="table-body2" style=" width:810px;">
										<table class="dclear" style=" width:810px;">
												<tr class="trHead">
													<th style="width:150px;">产品型号</th>
													<th style="width:150px;">SN码</th>
													<th style="width:150px;">UUID</th>
													<th style="width:150px;">硬件版本号</th>
													<th style="width:150px;">软件版本号</th>
													<th style="width:60px;"></th>
												</tr>
										</table>
									</div>
								</div>
								<div class="manage-page-left" style="margin-top:-5px; height:380px; width:810px; background: #FFF;">
										<table class="dclear" style=" width:810px;">
											<tbody id="dtu_body_scan_add" valign="top">
												<tr>
													<td style="width:150px;text-align: center; border: 1px solid #cccccc; height:32px;">
                                        				<select class="manage-select" name="prod_add" style="width:135px; background: #E0E0E0; color: #000; border: none; height: 28px; line-height: 28px; text-align: right;margin-top:2px;">
                                        				<c:forEach items="${prodList}" var="prodItem" varStatus="status">
                                        					<option value=\"${prodItem[0]}\">${prodItem[1]}</option>
                                        				</c:forEach>
                                        				</select>
                                        			</td>
                                        			<td style="width:150px;text-align: center; border: 1px solid #cccccc;">
                                        				<input type="text" name="sn_add" style="width:135px; " class="manage-input-text" maxlength="20" />
                                        			</td>
													<td style="width:150px;text-align: center; border: 1px solid #cccccc;">
														<input type="text" name="uuid_add" style="width:135px; " class="manage-input-text" maxlength="36" />
													</td>
                                        			<td style="width:150px;text-align: center; border: 1px solid #cccccc;">
                                        				<input type="text" name="hwversion_add" style="width:135px; " class="manage-input-text" maxlength="20" onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
                                        			</td>
                                        			<td style="width:150px;text-align: center; border: 1px solid #cccccc;">
                                        				<input type="text" name="swversion_add" style="width:135px; " class="manage-input-text" maxlength="20" onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
                                        			</td>
                                        			<td style="width:60px;text-align: center; border: 1px solid #cccccc; line-height:40px;">
                                                		<div class='handle' style='position: absolute; right: 20px; margin-top: 10px;'>
                                                			<img onclick='deltr(this)' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>
                                                		</div>
                                        			</td>
                                        		</tr>
											</tbody>
										</table>
									<div style="margin-top:5px; width:810px; text-align:center;">
										<a href="javascript:void(0)" class="button" style="margin-right:20px;"  onclick="addRowScan()"><strong>添加设备</strong></a>
									</div>
								</div>
							</div>
						</div>
						</shiro:hasRole>
						<div class="manage-page-right-backend dright" >
							<form id="orderEditForm" action="javascript:;" style="display: none;height:632px;margin-top:57px;">
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

								<div class="manage-page-item" style="border-bottom:0px;width:810px; background:#FFF;">
									<div class="manage-page-item-inner clearfix" style="width:810px; background:#FFF;">
										<div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;background:#FFF;">
											<div style="padding-left:20px; width:810px; text-align:left; margin-top:15px; height:25px;"><b>基本信息</b></div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;" id="edit_orderno"></td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">客户名称：</td>
													<td style="text-align:left; border:0px; height:37px;" colspan="3" >
														<input type="text" id="searchUserKey_edit" name="searchUserKey_edit" style="width:411px;height:35px;" value="输入客户名称搜索" /><font color="red" style="margin-left:6px;">*</font>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">订货数量：</td>
                                                    <td style="text-align:left; width:170px; border:0px; height:37px;">
                                                    	<input type="text" class="manage-input-text" id="edit_quantity" name="quantity" value="" maxlength="6" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('edit');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
                                                    </td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">项目描述：</td>
													<td style="text-align:left; border:0px; height:37px;" colspan="3" >
														<input type="text" class="manage-input-text" style="width:406px;" id="edit_projectNote" name="projectNote" value="" maxlength="100" /><font color="red" style="margin-left:6px;">*</font>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="edit_salesman" name="salesman" value="" maxlength="10" />
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;" id="edit_ldr"> </td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">创建时间：</td>
													<td style="text-align:left; border:0px; height:37px;" id="edit_opttime"> </td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc; padding-bottom:5px;background:#FFF;">
											<div style="padding-left:20px; width:810px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>车辆信息</b></div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<select class="manage-select" id="edit_vehicleType_id" name="vehicleType.id" class="form-control input-sm"  style="height:28px; width:150px;">
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
													<td style="text-align:left; border:0px; height:37px;">
														<select class="manage-select" id="edit_vehicleModel" name="vehicleModel.id" class="form-control input-sm" style="height:28px; width:150px;" onchange="addCartype('edit')">
															<option value= "-1">请选择 </option>
															<option value= "addVType">添加车辆型号</option>
														</select><font color="red" style="margin-left:6px;">*</font></td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="edit_factoryName" name="batteryModel.factoryName" value="" maxlength="30" onkeyup="autoBatteryName();" /><font color="red" style="margin-left:6px;">*</font>
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<select class="manage-select" id="edit_batteryModel_batteryType" name="batteryModel.batteryType.id" class="form-control input-sm"  style="height:28px; width:150px;" onchange="autoBatteryName()">
															<option value= "-1">请选择</option>
															<c:forEach items="${bmList}" var="bmItem">
																<option value= "${bmItem.id}">${bmItem.typeName}</option>
															</c:forEach>
														</select><font color="red" style="margin-left:6px;">*</font>
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
													<td style="text-align:left; width:170px; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="edit_batteryNumber" name="batteryModel.batteryNumber" value="" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('edit');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
													</td>
													<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" class="manage-input-text" id="edit_capacity" name="batteryModel.capacity" value="" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('edit');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
													</td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right-backend dright" style="margin-top: 20px; width:810px; text-align:center;">
											<input type="button" value=" " onclick="return updateOrder('edit')" class="manage-page-submit-button2">
										</div>
									</div>
								</div>
							</form>
						</div>

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
														<c:out value="${init_time}" />
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