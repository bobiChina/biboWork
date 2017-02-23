<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_device"/>
	<title>生产管理</title>
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
	<script src="${ctx}/static/js/jeasyui.extensions.all.min.js"></script>
	<script src="${ctx}/static/js/order.js"></script>
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
     </style>

<script type="text/javascript">
	var showDelIconType = 0;
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
	var orderStrArr = [];
	<c:forEach items="${orderList}" var="item" varStatus="status">orderArr.push({"orderno":"${item[1]}", "ordername": "${item[1]}"}); orderStrArr.push("${item[1]}");</c:forEach>
    $('#searchorderKey_li').combobox({
		valueField: 'orderno',
		textField: 'ordername',
		data: orderArr,
    	onSelect: function(record){
			orderNoChange(record.orderno);
    	},
		onChange: function(newValue, oldValue){
			if(contains(orderStrArr, newValue)){
				orderNoChange(newValue);
			}
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

	//初始化三个panel
	$("#panel_base").panel({
		iconCls:'icon-book-go',
		collapsible:true,
		collapsed:false,
		onExpand:function(){show_base_info()},
		onCollapse:function(){show_device_info()}
	});
	$("#panel_download").panel({
		iconCls:'icon-book-go',
		tools:'#tt_sw_down',
		collapsible:true,
		collapsed:true,
		onExpand:function(){show_base_info()},
		onCollapse:function(){show_device_info()}
	});
	$("#panel_device").panel({
		iconCls:'icon-book-go',
		tools:'#tt_sn_module',
		collapsible:true,
		collapsed:true,
		onExpand:function(){show_device_info()},
		onCollapse:function(){show_base_info()}
	});
});
function formatStatus(val,row){
	return '<span style="color:#000000;">' + val + '</span>';
	// return '<span style="color:blue;">已组装</span>';
	/*if(val == "1"){
		return '<span style="color:blue;">已组装</span>';
	}
	else if(val == "2"){
		return '<span style="color:blue;">已组装</span>';
	}
	else if(val == "3"){
		return '<span style="color:blue;">已绑定</span>';
	}
	else if(val == "4"){
		return '<span style="color:blue;">已联调</span>';
	}
	else */

	/*if(val == "已出货"){
		return '<span style="color:gr;">已出货</span>';
	}
	else if(val == "未知"){
		return '<span style="color:red;">未知</span>';
	}
	else if(val == "已组装"){
		return '<span style="color:#000000;">已组装</span>';
	}
	else if(val == "已测试"){
		return '<span style="color: #000000;">已测试</span>';
	}
	else if(val == "已绑定"){
		return '<span style="color:#000000;">已绑定</span>';
	}
	else{
		return '<span style="color:blue;">' + val + '</span>';
	}*/
}

	function getSNInfo(){
		// url: '${ctx}/order/getOrderProduct?orderno='+ $("#selOrderNo").val() + '&rmd=' + new Date().getTime(),
		// EASYUI列表部分  LG201512230001   $("#selOrderNo").val()
		$('#dg').datagrid({
			rownumbers:true,
			singleSelect: false,
			toolbar:'#search_dg_order',
			url: '${ctx}/order/getOrderProduct?orderno='+ $("#selOrderNo").val() + '&rmd=' + new Date().getTime(),
			autoRowHeight:false,
			enableHeaderContextMenu:true,//表头右键
			enableRowContextMenu:false,//行右键
			pagination:true,
			pageSize:20,
			width:806,
			height:565
		 });

		if($("#dg_order_keyword").val() != "" ){
			$("#dg_order_keyword").val("");
			searchNormalFun("dg", {"m_sn": ""});
		}
	}
	//查询
	function searchDeviceInfo(){
		searchNormalFun("dg", {"m_sn": $("#dg_order_keyword").val()});
	}



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
	$("#a_download").attr("href", "javascript:void(0)");

	$.easyui.loading({ msg: "正在加载...", topMost: true });
	$.post("${ctx}/order/getOrderInfo?rmd=" + new Date().getTime(), {orderno:id},function(data){
		if(data != "-1"){

			$("#a_download").attr("href", "${ctx}/fileOperate/downloadOrderZip?orderno=" + id);//添加下载路径

			var result = eval("(" + data + ")");
			var orderObj = result.order ;

			$("#show_ldr").html(result.username);
			var show_order_time = result.opttime_edit;
			$("#show_opttime").html(show_order_time.substring(0, 10));

			$("#show_orderno").html(orderObj.orderno);
			$("#show_contractNo").html(orderObj.contractNo);
			$("#show_salesman").html(result.salesman_show);
			$("#show_technicalDelegate").html(result.technicist_show);

			$("#show_corpName").html(orderObj.corp.corpName);
			if(orderObj.orderno.indexOf("LG-SH-") == 0){
				$("#show_title").html("原始订单：");
				$("#show_quantity").html(orderObj.boundOrder);
			}else{
				$("#show_title").html("订单套数：");
				$("#show_quantity").html(orderObj.quantity + "套");
			}
			//$("#show_quantity").html(orderObj.quantity + "套");
			$("#show_projectNote").html(orderObj.projectNote);
			$("#show_orderNote").html(orderObj.orderNote);

			if(orderObj.vehicleType != null && orderObj.vehicleType != undefined){
				$("#show_typeName").html(orderObj.vehicleType.typeName);
			}else{
				$("#show_typeName").html("无");
			}
			if(orderObj.vehicleModel != null && orderObj.vehicleModel != undefined){
				$("#show_modelName").html(orderObj.vehicleModel.modelName);
			}else{
				$("#show_modelName").html("无");
			}

			$("#show_factoryName").html(orderObj.batteryModel.factoryName == "" ? "无" : orderObj.batteryModel.factoryName);
			$("#show_batteryType").html(orderObj.batteryModel.batteryType.typeName);
			$("#show_batteryNumber").html(orderObj.batteryModel.batteryNumber + "串");
			$("#show_capacity").html(orderObj.batteryModel.capacity + "AH");

			var order_deviceArr = result.order_device  ;
			var order_deviceFileArr = result.order_device_file  ;
			var add_device_str = "";
			var device_class = "";
			var device_code = "";
			var device_bum_no = "";

			//清除软件信息
			$("#show_device_tab_BCU").html("");
			$("#show_device_tab_BYU").html("");
			$("#show_device_tab_BMU").html("");
			$("#show_device_tab_LDM").html("");
			$("#show_device_tab_DTU").html("");
			$("#show_device_tab_LCD").html("");
			$("#show_device_tab_CS").html("");
			$("#show_device_tab_OTHER").html("");

			if(null != order_deviceArr && undefined != order_deviceArr && "" != order_deviceArr){
				var deviceObj = [];
				for(var i=0; i<order_deviceArr.length; i++){
					deviceObj = order_deviceArr[i];
					device_class = deviceObj[1];
					device_code = deviceObj[0];
					device_bum_no = deviceObj[2];
					/*****************************************************************************************************
					 * 浏览页显示订单软件
					 *****************************************************************************************************/
					var add_device_str = "<tr style='color:#000; background-color:#ECECF0;height:30px;'>" +
							"<td style=\"text-align:right; width:90px; border:0px; height:30px;\">设备类型：</td>" +
							"<td style=\"text-align:left; width:60px; border:0px; height:30px;\">" + device_class + "</td>" +
							"<td style=\"text-align:right; width:90px; border:0px; height:30px;\">设备型号：</td>" +
							"<td style=\"text-align:left; border:0px; height:30px;\" width=\"*\" >" + device_code ;
					if("BMU" == device_class){
						add_device_str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID编号：#" + device_bum_no  ;
					}
					add_device_str += "</td></tr>";
					if(device_class == "BCU" || device_class == "BYU" || device_class == "OTHER"){
						add_device_str += "<tr>" +
								"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">订单固件：</td>" +
								"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='3' >";
						if(undefined != order_deviceFileArr){
							fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_s19"];
							if(undefined != fileObj){
								add_device_str += "<div style='float: left;'><span title='" + fileObj[0] + "'>";
								if(fileObj[1] == "usetemplate"){
									add_device_str += "使用模版";
								}else{
									if(fileObj[0].length > 45){
										add_device_str += fileObj[0].substring(0, 45) + "...";
									}else{
										add_device_str += fileObj[0];
									}
								}

								add_device_str += "</span></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
										"</div></td></tr>";
							}else{
								add_device_str += "&nbsp;</td></tr>";
							}
						}else{
							add_device_str += "&nbsp;</td></tr>";
						}

						add_device_str += "<tr>" +
								"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">配置文件：</td>" +
								"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='3'>";
						if(undefined != order_deviceFileArr){
							fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_cfg"];
							if(undefined != fileObj){
								add_device_str += "<div style='float: left;'><span title='" + fileObj[0] + "'>";
								if(fileObj[0].length > 45){
									add_device_str += fileObj[0].substring(0, 45) + "...";
								}else{
									add_device_str += fileObj[0];
								}
								add_device_str += "</span></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
										"</div></td></tr>";
							}else{
								add_device_str += "&nbsp;</td></tr>";
							}
						}else{
							add_device_str += "&nbsp;</td></tr>";
						}

						add_device_str += "<tr>" +
								"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">规则文件：</td>" +
								"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='3'>";
						if(undefined != order_deviceFileArr){
							fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_csf"];
							if(undefined != fileObj){
								add_device_str += "<div style='float: left;'><span title='" + fileObj[0] + "'>";
								if(fileObj[0].length > 45){
									add_device_str += fileObj[0].substring(0, 45) + "...";
								}else{
									add_device_str += fileObj[0];
								}
								add_device_str += "</span></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
										"</div></td></tr>";
							}else{
								add_device_str += "&nbsp;</td></tr>";
							}
						}else{
							add_device_str += "&nbsp;</td></tr>";
						}

					}else if(device_class == "BMU" || device_class == "LDM"){
						add_device_str += "<tr>" +
								"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">订单固件：</td>" +
								"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='3'>";
						if(undefined != order_deviceFileArr){
							fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_s19"];
							if(undefined != fileObj){
								add_device_str += "<div style='float: left;'><span title='" + fileObj[0] + "'>";
								if(fileObj[1] == "usetemplate"){
									add_device_str += "使用模版";
								}else {
									if (fileObj[0].length > 45) {
										add_device_str += fileObj[0].substring(0, 45) + "...";
									} else {
										add_device_str += fileObj[0];
									}
								}
								add_device_str += "</span></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
										"</div></td></tr>";
							}else{
								add_device_str += "&nbsp;</td></tr>";
							}
						}else{
							add_device_str += "&nbsp;</td></tr>";
						}

						add_device_str += "<tr>" +
								"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">配置文件：</td>" +
								"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='3'>";
						if(undefined != order_deviceFileArr){
							fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_cfg"];
							if(undefined != fileObj){
								add_device_str += "<div style='float: left;'><span title='" + fileObj[0] + "'>";
								if(fileObj[0].length > 45){
									add_device_str += fileObj[0].substring(0, 45) + "...";
								}else{
									add_device_str += fileObj[0];
								}
								add_device_str += "</span></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
										"</div></td></tr>";
							}else{
								add_device_str += "&nbsp;</td></tr>";
							}
						}else{
							add_device_str += "&nbsp;</td></tr>";
						}
					}
					else if(device_class == "DTU" ){
						add_device_str += "<tr>" +
								"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">订单固件：</td>" +
								"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='3'>";
						if(undefined != order_deviceFileArr){
							fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_s19"];
							if(undefined != fileObj){
								add_device_str += "<div style='float: left;'><span title='" + fileObj[0] + "'>";
								if(fileObj[1] == "usetemplate"){
									add_device_str += "使用模版";
								}else {
									if (fileObj[0].length > 45) {
										add_device_str += fileObj[0].substring(0, 45) + "...";
									} else {
										add_device_str += fileObj[0];
									}
								}
								add_device_str += "</span></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
										"</div></td></tr>";
							}else{
								add_device_str += "&nbsp;</td></tr>";
							}
						}else{
							add_device_str += "&nbsp;</td></tr>";
						}
					}
					else if(device_class == "LCD" ){
						add_device_str += "<tr>" +
								"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">压缩文件：</td>" +
								"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='3'>";
						if(undefined != order_deviceFileArr){
							fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_zip"];
							if(undefined != fileObj){
								add_device_str += "<div style='float: left;'><span title='" + fileObj[0] + "'>";
								if(fileObj[1] == "usetemplate"){
									add_device_str += "使用模版";
								}else {
									if (fileObj[0].length > 45) {
										add_device_str += fileObj[0].substring(0, 45) + "...";
									} else {
										add_device_str += fileObj[0];
									}
								}
								add_device_str += "</span></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
										"</div></td></tr>";
							}else{
								add_device_str += "&nbsp;</td></tr>";
							}
						}else{
							add_device_str += "&nbsp;</td></tr>";
						}
					}
					$("#show_device_tab_" + deviceObj[1]).append(add_device_str);//对象不能被传递

					/*****************************************************************************************************
					 * 浏览页显示订单软件
					 *****************************************************************************************************/
				}
			}
		}
		$.easyui.loaded(true);
	});
	//已绑定设备信息
	getSNInfo();

}

//删除
function delSNFun(){
	var rows = $('#dg').datagrid('getSelections');
	var ids = "";
	for(var i=0; i<rows.length; i++){
		ids += rows[i].id + ",";
	}
	if("" == ids){
		$.messager.alert("系统提示", "请选择解除绑定项", "warning");
	}else{
		//获取已选择
		$.easyui.showDialog({
			title: "选择解绑类型",
			width: 330, height: 210,
			content:$("#selOrderType_dialog").html(),
			topMost: true,
			enableApplyButton: false,
			onSave: function (d) {
				var dataForm = d.form("getData");
				var selValue = dataForm.selOrderType ;
				if(selValue != null && selValue != ""){
					// 2- 产品SN解绑 1-模块SN解绑 0-全部解绑
					$.messager.confirm("解绑确认", "是否确认解除绑定？", function(cr){
						if(cr){
							$.easyui.loading({ msg: "正在操作...", topMost: true });
							$.post("${ctx}/order/unbindOrderDevice?rmd=" + new Date().getTime(), {ids: ids, orderno : $("#selOrderNo").val(), type: selValue },function(data){
								$.easyui.loaded(true);
								var msg = "操作成功";
								if(data != "ok"){
									msg = "操作失败";
								}
								$.messager.alert("系统提示", msg, "info");
								getSNInfo();
							});
						}
					});
				}else{
					$.messager.alert("系统提示", "请选择解绑类型", "warning");
					return false;
				}
			}
		});
	}
}
	function updateStatus(){
		var rows = $('#dg').datagrid('getSelections');
		var ids = "";
		for(var i=0; i<rows.length; i++){
			ids += rows[i].id + ",";
		}
		if("" == ids){
			$.messager.alert("系统提示", "请选择标注出货项", "warning");
		}else{
			$.messager.confirm("出货确认", "是否确认出货？", function(cr){
				if(cr){
					$.easyui.loading({ msg: "正在操作...", topMost: true });
					$.post("${ctx}/order/updateStatus?rmd=" + new Date().getTime(), {ids: ids, orderno : $("#selOrderNo").val()},function(data){
						$.easyui.loaded(true);
						var msg = "";
						if(data == "ok"){
							msg = "操作成功";
						}else if(data == "none"){
							msg = "请先选择标注出货项";
						}else{
							msg = "操作失败";
						}
						$.messager.alert("系统提示", msg, "info");
						getSNInfo();
					});
				}
			});
		}
	}





function show_device_info(){
	$("#panel_base").panel("collapse");
	$("#panel_download").panel("collapse");
	$("#panel_device").panel("expand");
}
function show_base_info(){
	$("#panel_base").panel("expand");
	$("#panel_download").panel("expand");

	$("#panel_device").panel("collapse");
}


</script>
</head>
<body>
<div id="selOrderType_dialog" style="display:none;">
	<table class="dclear" style="width:210px;margin-top:10px;font-size:14px; margin-left:35px;" >
		<tr>
			<td style="text-align:left; border:0px; height:37px; padding-left: 70px;" width="*"><label><input type="radio" id="selType1_radio1" name="selOrderType" value="2" /> 产品SN码解绑</label></td>
		</tr>
		<tr>
			<td style="text-align:left; border:0px; height:37px; padding-left: 70px;" width="*"><label><input type="radio" id="selType1_radio2" name="selOrderType" value="1" /> 模块SN码解绑</label></td>
		</tr>
		<tr>
			<td style="text-align:left; border:0px; height:37px; padding-left: 70px;" width="*"><label><input type="radio" id="selType1_radio3" name="selOrderType" value="0" /> 全部解绑</label></td>
		</tr>
	</table>
</div>

<input type="hidden" id="selOrderNo" name="selOrderNo" value="" />
<input type="hidden" id="userDtu_old" name="userDtu_old" value="" />
<div class="body-outer dclear clearfix" style="width:1020px;">
		<div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
			<div class="page-table-body" style="width:1020px;">
				<!-- ---------------------------head start -------------------------- -->
				<div class="history-save-table-head-backend">
					<div class="history-save-table-head-outer">
						<div class="manage-page-head-left dleft">
							<div class="dleft" style="width:150px;color:#FFF; font-size:14px;" onclick='findOrderLi(2)'>订单列表</div>
						</div>
						<div class="manage-page-head-right dright" >
							<div class="dleft" style="width:670px;">	详细信息</div>
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
							<div class="manage-page-left" style="height:610px;">
								<ul id="orderdata">
									<c:forEach items="${orderList}" var="item" varStatus="status">
										<li orderLIData="${item[1]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;'onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)'>
											<a href="javascript:;" onclick="orderNoChange('${item[1]}')" <c:if test="${item[2] == 2}">title='已删除'</c:if>  >${item[1]}</a>
											<div class="handle" style="position: absolute; right: 5px; margin-top: -30px; display: none;">
												<img onclick="delOrderFun('${item[0]}','${item[1]}', ${item[2]}, 1)" src="${ctx}/static/images/del.png" style="position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; ">
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
						</div>
						<!-- 操作按钮 -->
						<div id="tt_sn_module">
							<a href="javascript:void(0)" class="icon-unlock" onclick="delSNFun()" title="解除绑定"></a>
							<a href="javascript:void(0)" class="icon-map" onclick="updateStatus()" title="标记出货"></a>
						</div>
						<div id="tt_sw_down">
							<a id="a_download" href="javascript:void(0)" class="icon-cloud-download" title="软件下载"></a>
						</div>
						<!-- 操作按钮 -->
						<div class="easyui-panel" id="orderInfoShow" style="background:#FFF; height:670px;width:810px; margin-top:57px;font-size:14px;overflow: hidden;" align="right" >
                        	<div class="easyui-panel" id="panel_base" title="订单信息" style="width:805px;height:285px;padding:1px;font-size:14px">
								<table class="dclear" style="width:785px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" id="show_orderno"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_ldr"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创建时间：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_opttime"></td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px;">客户名称：</td>
										<td style="text-align:left; border:0px;" id="show_corpName" width="*"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_salesman"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">合同编号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_contractNo"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px;">项目描述：</td>
										<td style="text-align:left; border:0px;" width="*" id="show_projectNote"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">技术代表：</td>
										<td style="text-align:left; width:150px; border:0px;height:37px;" id="show_technicalDelegate"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;" id="show_title">订单套数：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_quantity"> </td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<table class="dclear" style="width:785px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
										<td style="text-align:left; border:0px; height:37px;" id="show_typeName"  width="*"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆型号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_modelName"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_batteryNumber"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
										<td style="text-align:left; border:0px; height:37px;" id="show_factoryName" width="*"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_batteryType"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
										<td style="text-align:left;  width:150px; border:0px; height:37px;" id="show_capacity"> </td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<table class="dclear" style="width:785px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">下单原因：</td>
										<td style="text-align:left; border:0px; height:37px;"  width="*" colspan="5" id="show_orderNote">
											<textarea rows="3" style="width: 670px; border: 0;"  name="show_orderNote"></textarea>
										</td>
									</tr>
								</table>
							</div>
							<div style="background:#FFF; width: 800px; height: 1px;"></div>
							<div class="easyui-panel" id="panel_download" title="系统配置" style="width:805px;height:330px;padding:1px;font-size:14px;">
								<table class="dclear" width="100%" id="show_device_tab">
									<tbody id="show_device_tab_BCU">
									</tbody>
									<tbody id="show_device_tab_BYU">
									</tbody>
									<tbody id="show_device_tab_BMU">
									</tbody>
									<tbody id="show_device_tab_LDM">
									</tbody>
									<tbody id="show_device_tab_DTU">
									</tbody>
									<tbody id="show_device_tab_LCD">
									</tbody>
									<tbody id="show_device_tab_CS">
									</tbody>
									<tbody id="show_device_tab_OTHER">
									</tbody>
								</table>
							</div>
							<div style="background:#FFF; width: 805px; height: 1px;"></div>
							<div class="easyui-panel" id="panel_device" title="设备管理" style="padding:0;font-size:14px; height:600px; width: 810px;"	>
								<table id="dg">
									<thead>
									<tr>
										<th data-options="field:'id',width:50,align:'center', halign:'center', checkbox:true"></th>
										<th data-options="field:'device_code',width:80,align:'center', halign:'center', sortable:true">设备型号</th>
										<th data-options="field:'m_sn',width:140,align:'center', halign:'center', sortable:true">模块SN码</th>
										<th data-options="field:'hw_version',width:70,align:'center', halign:'center', sortable:true">硬件版本</th>
										<th data-options="field:'uuid',width:160,align:'center', halign:'center', sortable:true">UUID</th>
										<th data-options="field:'p_sn',width:125 ,align:'center', halign:'center', sortable:true">产品SN码</th>
										<th data-options="field:'sw_version',width:75,align:'center', halign:'center'">软件版本</th>
										<th data-options="field:'status',width:60,align:'center', halign:'center', sortable:true, formatter:formatStatus">状态</th>
									</tr>
									</thead>
								</table>
								<div id="search_dg_order" style="height:38px; line-height: 38px; padding-left: 10px; font-size: 14px;">
									模块SN码：<input type="text" class="manage-input-text" name="dg_order_keyword" id="dg_order_keyword" style="width: 170px;" />
									<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="searchDeviceInfo()">查询</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="page-footer-backend dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->
	<%--
	<c:if test="${not empty orderShow}">
    <script type="text/javascript">
   $(function(){
    	orderNoChange("${orderShow}");
    });
    </script>
    </c:if>
    --%>
</body>
</html>