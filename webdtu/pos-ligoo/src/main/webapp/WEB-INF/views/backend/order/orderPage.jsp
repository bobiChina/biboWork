<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_order"/>
	<title>订单管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />
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
	<script src="${ctx}/static/js/order.js?rdm=1"></script>

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
		 width: 126px;
		 height: 18px;
		 line-height: 18px;
		 border: none;
		 margin-top: 2px;
	 }
	.manage-textarea-text {
		 background: #E0E0E0;
		 padding-left: 5px;
		 border: none;
	 }
	.manage-page-top{ width: 140px; height: 410px; overflow: hidden; background: #e0e0e0; font-size:14px; margin-top: 1px; margin-right: 2px; float: left; }
	.manage-page-top ul li{ height: 40px; line-height: 40px; display: block; width: 100%; text-align: center; border-bottom: 1px solid #cccccc; }
	.manage-page-top ul li.manage-lefthover{ background: #FFF;}
	.manage-page-top ul li.manage-leftactive{ background: #FFF;}
	.manage-page-top ul li.manage-nochange{ background: #F1EFEF;}

	.tooltip{
		max-width: 350px;
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
	var showDelIconType = 0;
	<shiro:hasRole name="admin">
	showDelIconType = 1;
	</shiro:hasRole>
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

	$("#edit_vehicleType_id").change(function (){
		$("#edit_vehicleModel").empty();
		$("#edit_vehicleModel").append("<option value=''>请选择</option>");
		if ($("#edit_vehicleType_id").val() == "")
			return;

		$.get("${ctx}/vehicle/vehicleModel?rmd=" + new Date().getTime(), {typeId:$("#edit_vehicleType_id").val() , pagetype:"order"},function(data){
			var result = eval(data);
			$.each(result,function(index,item){
				$("#edit_vehicleModel").append("<option value='"+item[0]+"'>"+item[1]+"</option>");

				if ($("#edit_vehicleModel_old").val() == item[0]){
					$("#edit_vehicleModel").val($("#edit_vehicleModel_old").val());
				}
			});
			//最后添加addVType
			$("#edit_vehicleModel").append("<option value='addVType'>添加车辆类型</option>");
		});
	});

	$("#add_vehicleType_id").change(function (){
		$("#add_vehicleModel").empty();
		$("#add_vehicleModel").append("<option value='-1'>请选择</option>");
		if ($("#add_vehicleType_id").val() == -1)
			return;

		$.get("${ctx}/vehicle/vehicleModel?rmd=" + new Date().getTime(), {typeId:$("#add_vehicleType_id").val() , pagetype:"order"},function(data){
			var result = eval(data);
			$.each(result,function(index,item){
				$("#add_vehicleModel").append("<option value='"+item[0]+"'>"+item[1]+"</option>");
			});
			//最后添加addVType
			$("#add_vehicleModel").append("<option value='addVType'>添加车辆类型</option>");
		});
	});
	$("#add_sw_vehicleType_id").change(function (){
		$("#add_sw_vehicleModel").empty();
		$("#add_sw_vehicleModel").append("<option value=''>请选择</option>");
		if ($("#add_sw_vehicleType_id").val() == "")
			return;

		$.get("${ctx}/vehicle/vehicleModel?rmd=" + new Date().getTime(), {typeId:$("#add_sw_vehicleType_id").val() , pagetype:"order"},function(data){
			var result = eval(data);
			$.each(result,function(index,item){
				$("#add_sw_vehicleModel").append("<option value='"+item[0]+"'>"+item[1]+"</option>");
			});
			//最后添加addVType
			$("#add_sw_vehicleModel").append("<option value='addVType'>添加车辆类型</option>");

		});
	});

	$("#add_sw_checkbox0").click(function(){
		if($(this).prop('checked')){
			toOrderSelPage();
			$("#add_sw_checkbox1").attr("checked",true);
		}
	});
	$("#add_sw_checkbox1").click(function(){
		if(!$(this).prop('checked')){
			toOrderAddSHPage(99);
			$("#add_sw_checkbox0").attr("checked",false);
		}
	});

	var corpArr = [];
	var corpNameMap = new Map();
	<c:forEach items="${userList}" var="item" varStatus="status">corpArr.push({"corpId":"${item[0]}", "corpName": "${item[1]}"});corpNameMap.put("key_${item[1]}", "${item[0]}");</c:forEach>
    $('#searchUserKey_li').combobox({
		valueField: 'corpId',
		textField: 'corpName',
		data: corpArr,
    	onSelect: function(record){
			userIdchange(record.corpId);
    	},
		onChange: function(newValue, oldValue){
			var corp_id = corpNameMap.get("key_" + newValue);
			if(corp_id != null && corp_id.length > 0){
				userIdchange(corp_id);
			}
		},
    	onLoadSuccess: function(){
    		$("#searchorder_dev").find(".combo").addClass("combobg");
            $("#searchorder_dev").find(".combo-text").addClass("combobg");
			//默认显示value的内容
			var p = $("input[name='searchUserKey_li']").closest("span.combo,div.combo-p,div.menu");//向上查找
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
		tools:'#tt_base',
		collapsible:false,
		collapsed:false,
		onExpand:function(){show_base_info()},
		onCollapse:function(){show_trackLog()}
	});
	$("#panel_software").panel({
		iconCls:'icon-book-go',
		tools:'#tt_module',
		collapsible:true,
		collapsed:false,
		onExpand:function(){show_base_info()},
		onCollapse:function(){show_trackLog()}
	});
	$("#panel_SH_Log").panel({
		iconCls:'icon-book-go',
		collapsible:true,
		collapsed:true,
		onExpand:function(){show_trackLog()},
		onCollapse:function(){show_base_info()}
	});
});

function show_trackLog(){
	$("#panel_base").panel("expand");
	$("#panel_software").panel("collapse");
	$("#panel_SH_Log").panel("expand");
}
function show_base_info(){
	$("#panel_base").panel("expand");
	$("#panel_software").panel("expand");
	$("#panel_SH_Log").panel("collapse");
}
/**********************
数组信息
**********************/
var bcuArr = [];
<c:forEach items="${device_BCU_list}" var="bcuItem">bcuArr.push("${bcuItem}");</c:forEach>

var byuArr = [];
<c:forEach items="${device_BYU_list}" var="byuItem">byuArr.push("${byuItem}");</c:forEach>

var bmuArr = [];
<c:forEach items="${device_BMU_list}" var="bmuItem">bmuArr.push("${bmuItem}");</c:forEach>

var ldmArr = [];
<c:forEach items="${device_LDM_list}" var="ldmItem">ldmArr.push("${ldmItem}");</c:forEach>

var dtuArr = [];
<c:forEach items="${device_DTU_list}" var="dtuItem">dtuArr.push("${dtuItem}");</c:forEach>
	var lcdArr = [];
	lcdArr.push("BS03512");
	lcdArr.push("BS03524");
	lcdArr.push("BS05712");
	lcdArr.push("BS05724");
	lcdArr.push("BS07012");
	lcdArr.push("BS07024");

	var csArr = [];
	/*csArr.push("FS100E2");
	csArr.push("FS300E2");
	csArr.push("FS500E2");
	csArr.push("FS600E2");
	csArr.push("FS100E2T");
	csArr.push("FS300E2T");
	csArr.push("FS500E2T");
	csArr.push("FS600E2T");
	csArr.push("DHAB S/18");
	csArr.push("DHAB S/24");
	csArr.push("DHAB S/45");*/

	function addOrder(optType){
		$.easyui.loading({ msg: "正在保存...", topMost: true });
		//订单类型
		$("#" + optType + "_orderType").val($("#page_orderType").val());

		var orderStr = $("#edit_orderno_").val();
		if(optType == 'add' || optType == "add_sw"){
			orderStr = $("#" + optType + "_orderno").val();
			if (orderStr == ""){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "订单编号不能为空", "warning");
				return;
			}
			if ($("#" + optType + "_checkOrderNo").val() == "0"){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "订单编号重复", "warning");
				return;
			}
			/******************************************************************
			 * 订单编号规则 LG 或 LG-SH- 开头，后面全为数字 长度为14或者16位
			 * 新订单只能是LG开头，加十二位数字，售后订单新增只能以LG-SH-开头
			 *****************************************************************/
			if(orderStr.length == 14 || orderStr.length == 16){
				if(optType == 'add'){
					if(orderStr.indexOf("LG")==0 && orderStr.length == 14){
						//检查后面是否全部为数字
						var str = orderStr.substring(2);
						var reg = new RegExp("^[0-9]*$");
						if(!reg.test(str)){
							$.easyui.loaded(true);
							$.messager.alert("系统提示", "订单编号格式不正确<br>正确格式：LG 开头后接12位数字", "warning");
							return;
						}
					}else{
						$.easyui.loaded(true);
						$.messager.alert("系统提示", "订单编号格式不正确<br>正确格式：LG 开头后接12位数字", "warning");
						return;
					}
				}else{
					if(orderStr.indexOf("LG-SH-")==0 && orderStr.length == 16){
						//检查后面是否全部为数字
						var str = orderStr.substring(6);
						var reg = new RegExp("^[0-9]*$");
						if(!reg.test(str)){
							$.easyui.loaded(true);
							$.messager.alert("系统提示", "订单编号格式不正确<br>正确格式：LG-SH- 开头后接10位数字", "warning");
							return;
						}
					}else{
						$.easyui.loaded(true);
						$.messager.alert("系统提示", "订单编号格式不正确<br>正确格式：LG-SH- 开头后接10位数字", "warning");
						return;
					}
				}
			}else{
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "订单编号长度应为14或16位字母加数字", "warning");
				return;
			}
		}else{
			if ($("#" + optType + "_id").val() == ""){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "请选择订单", "warning");
				return;
			}
		}
		var formStr = getFormStr();
		/********************** 设备信息 *******************************/
		//检查设备型号
		var device_code_sels = "";
		$("#" + formStr + " input[name='device_code_sel']").each(function(x, ele){
			device_code_sels += $(this).val() + ",";
		});
		$("#" + optType + "_devices_code").val(device_code_sels);

		var device_class_sels = "";
		$("#" + formStr + " input[name='device_class_sel']").each(function(x, ele){
			device_class_sels += $(this).val() + ",";
		});
		$("#" + optType + "_devices_class").val(device_class_sels);

		var device_bmu_sels = "";
		$("#" + formStr + " input[name='device_bmu_no']").each(function(x, ele){
			device_bmu_sels += $(this).val() + ",";
		});
		$("#" + optType + "_devices_bmu").val(device_bmu_sels);
		var device_count_sels = "";
		$("#" + formStr + " input[name='device_count']").each(function(x, ele){
			if($(this).val() == ""){
				device_count_sels += "0,";
			}else{
				device_count_sels += $(this).val() + ",";
			}
		});
		$("#" + optType + "_devices_count").val(device_count_sels);
		/********************** 设备信息 *******************************/

		var khmcStr = $('#' + optType + "_corp_id").val();
		if (khmcStr == undefined|| khmcStr == ""){
			$.easyui.loaded(true);
			$.messager.alert("系统提示", "请选择客户", "warning");
			return;
		}

		if ($("#" + optType + "_quantity").val() == ""){
			$.easyui.loaded(true);
			$.messager.alert("系统提示", "订单套数不能为空", "warning");
			return;
		}
		if ($("#" + optType + "_projectNote").val() == ""){
			$.easyui.loaded(true);
			$.messager.alert("系统提示", "项目描述不能为空", "warning");
			return;
		}
		/** (1)2016年1月12日13:49:14 屏蔽，许多订单无DTU信息
		 *  (2)售后订单 手工录入信息，允许车辆类型、车辆型号、电池厂商为非必填项
		 *	sby 2016年6月4日 15:47:09
		 * **/
		if(orderStr.indexOf("LG-SH-")==0 && orderStr.length == 16){

		}else{
			if ($("#" + optType + "_vehicleType_id").val() == "-1"){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "车辆类型不能为空", "warning");
				return;
			}

			if ($("#" + optType + "_vehicleModel").val() == "-1"){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "车辆型号不能为空", "warning");
				return;
			}
			if ($("#" + optType + "_factoryName").val() == ""){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "电池厂商不能为空", "warning");
				return;
			}
		}

		if ($("#" + optType + "_batteryModel_batteryType").val() == "-1"){
			$.easyui.loaded(true);
			$.messager.alert("系统提示", "电池类型不能为空", "warning");
			return;
		}

		if ($("#" + optType + "_batteryNumber").val() == ""){
			$.easyui.loaded(true);
			$.messager.alert("系统提示", "电池串数不能为空", "warning");
			return;
		}

		if ($("#" + optType + "_capacity").val() == ""){
			$.easyui.loaded(true);
			$.messager.alert("系统提示", "电池容量不能为空", "warning");
			return;
		}

		//车辆型号文本信息
		$("#" + optType + "_vehicleModel_modelName").val($("#" + optType + "_vehicleModel").find("option:selected").text());

		//下单原因不能为空，且不超过200字
		var orderNoteStr = $("#" + optType + "_orderNote").val();
		orderNoteStr = orderNoteStr.trim();
		if("" == orderNoteStr){
			$.easyui.loaded(true);
			$.messager.alert("系统提示", "下单原因不能为空", "warning");
			return;
		}else if(orderNoteStr.length > 200){
			$.easyui.loaded(true);
			$.messager.alert("系统提示", "下单原因不能超过两百字", "warning");
			return;
		}
		$.post(sys_rootpath + "/order/saveOrder?rmd=" + new Date().getTime(), $("#" + formStr).serialize(),function(data){
			$.easyui.loaded(true);
			if(data == "-1"){
				$.messager.alert("系统提示", "订单编号重复", "warning");
			}else{
				$.messager.alert("系统提示", "操作成功", "warning");
				toBackPage_new();
				searchInfo();
			}
		});
	}
</script>
</head>
<body>
<input type="hidden" id="addDeviceType" name="addDeviceType" value="" />
<input type="hidden" id="page_orderType" name="orderType" value="1"/>
<input type="hidden" id="page_shAddType" name="shAddType" value="1"/>
<div id="test_dialog" style="display:none;">
	<table class="dclear" style="width:400px;margin-top:40px;font-size:14px; margin-left:35px;" >
		<tr>
			<td style="text-align:right; width:90px; border:0px; height:37px;">设备类型：</td>
			<td style="text-align:left; width:210px; border:0px; height:37px;">
				<select class="manage-select" id="add_device_class_p" name="add_device_class_p" onchange="changeDeviceSel(this)" class="form-control input-sm"  style="height:28px; width:207px;">
					<option value= "-1">请选择</option>
					<option value= "BCU">BCU</option>
					<option value= "BYU">BYU</option>
					<option value= "BMU">BMU</option>
					<option value= "LDM">LDM</option>
					<option value= "DTU">DTU</option>
					<option value= "LCD">LCD</option>
					<%--<option value= "CS">CS</option>--%>
					<option value= "OTHER">其他</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="text-align:right; width:90px; border:0px; height:37px;">设备型号：</td>
			<td style="text-align:left; width:210px; border:0px; height:37px;">
				<select class="manage-select" id="add_device_code_p" name="add_device_code_p" class="form-control input-sm"  style="height:28px; width:207px;">
					<option value= "">请选择</option>
				</select>
			</td>
		</tr>
		<tr style="display:none;">
			<td style="text-align:right; width:90px; border:0px; height:37px;">ID编号：</td>
			<td style="text-align:left; width:210px; border:0px; height:37px;"><span></span>
				<input type="hidden" id="add_device_bmu_p" name="add_device_bmu_p" value="" />
			</td>
		</tr>
		<tr style="display: none;">
			<td style="text-align:right; width:90px; border:0px; height:37px;">数量：</td>
			<td style="text-align:left; width:210px; border:0px; height:37px;">
				<input type="text" class="manage-input-text" style="width:202px;" id="add_device_count" name="add_device_count" value="0" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,''); " onafterpaste="this.value=this.value.replace(/\D/g,'')" />
			</td>
		</tr>
	</table>
</div>
<div id="selOrderType_dialog" style="display:none;">
	<table class="dclear" style="width:210px;margin-top:10px;font-size:14px; margin-left:35px;" >
		<tr>
			<td style="text-align:left; border:0px; height:37px; padding-left: 70px;" width="*"><label><input type="radio" id="selType1_radio1" name="selOrderType" value="1" /> 新订单</label></td>
		</tr>
		<tr>
			<td style="text-align:left; border:0px; height:37px; padding-left: 70px;" width="*"><label><input type="radio" id="selType1_radio2" name="selOrderType" value="2" /> 软件售后订单</label></td>
		</tr>
		<tr>
			<td style="text-align:left; border:0px; height:37px; padding-left: 70px;" width="*"><label><input type="radio" id="selType1_radio3" name="selOrderType" value="3" /> 硬件售后订单</label></td>
		</tr>
	</table>
</div>
<input type="hidden" id="selOrderNo" name="selOrderNo" value="" />
<input type="hidden" id="selUserId" name="selUserId" value="" />
<input type="hidden" id="userDtu_old" name="userDtu_old" value="" />
	<div class="body-outer dclear clearfix" style="width:1020px;">
		<div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
			<div class="page-table-body" style="width:1020px;">
				<!-- ---------------------------head start -------------------------- -->
				<div class="history-save-table-head-backend">
					<div class="history-save-table-head-outer">
						<div class="manage-page-head-left dleft">
							<div class="dleft" style="width:150px;" onclick='findUserLi(-999)'><a href="javascript:void(0)" style="color:#FFF; font-size:14px; ">客户列表</a></div>
							<div class="dright" style="width:24px;display:none;" onclick='toAddPage()' ><img src="${ctx}/static/images/add.png" style="width:20px;margin-top:12px;" title="添加订单" alt="添加订单" /></div>
						</div>
						<div class="manage-page-head-right dright" >
							<div class="dleft" style="width:670px;" id="show_head_title">	详细信息</div>
						</div>
					</div>
				</div>
				<!-- ---------------------------head end -------------------------- -->
				<div class="historty-table-con-outer-backend dclear">
					<div class="manage-page-inner clearfix" style="width:1010px;">
						<div class="dleft" style="background:#E0E0E0;">
							<div style="background:#E0E0E0; width:197px; height:37px;text-align:center; margin-top:67px; margin-left:3px;border-bottom: 1px solid #cccccc;" id="searchorder_dev">
								<input id="searchUserKey_li" name="searchUserKey_li" style="width:160px; height:30px;padding:6px 12px;" value="输入客户名称搜索" />
							</div>
							<div class="manage-page-left" style="height:565px; margin-bottom: 0px !important;">
								<ul id="userdata">
									<c:forEach items="${userList}" var="item" varStatus="status">
										<li userLIData="${item[0]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;' onmouseover='showCorpDelIcon(this, 1)' onmouseout='showCorpDelIcon(this, 0)'>
											<a href="javascript:;" title="${item[1]}" onclick="userIdchange(${item[0]})">
												<c:if test="${fn:length(item[1])>'9'}">${fn:substring(item[1], 0, 8)}...</c:if>
												<c:if test="${fn:length(item[1])<'10'}">${item[1]}</c:if>
											</a>
											<div class="handle" style="position: absolute; right: 5px; margin-top: -30px; display: none;">
												<img onclick="delUserFun('${item[0]}','${item[1]}')" src="${ctx}/static/images/del.png" style="position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; ">
											</div>
											<c:if test="${status.count == 1}">
												<script type="text/javascript">
													$(function (){
														userIdchange(${item[0]});
													});
												</script>
											</c:if>
										</li>
									</c:forEach>
								</ul>
							</div>
							<div style="background:#E0E0E0; width:200px; height:50px;text-align:center;padding-top:5px;">
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="toAddUserPage()"><strong>&nbsp;&nbsp;创建新客户&nbsp;&nbsp;</strong></a>
							</div>
						</div>
						<div class="easyui-panel" id="orderInfoShow" style="display:none; background:#FFF;margin-top:57px; height:660px;width:810px; font-size:14px; overflow: hidden;" align="right"  >
							<div class="easyui-panel" id="panel_base" title="订单信息" style="width:805px;height:270px;padding:1px;font-size:14px"	>
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
										</td>
									</tr>
								</table>
							</div>
							<div id="tt_base">
								<a href="javascript:void(0)" class="icon-edit" onclick="toUpdatePage()"></a>
							</div>
							<div id="tt_module">
								<a href="javascript:void(0)" class="icon-edit" onclick="toUpdateModule()"></a>
							</div>
							<div class="easyui-panel" id="panel_software" title="系统配置" style="width:805px;padding:1px;font-size:14px; height:320px;" >
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
							<div class="easyui-panel" id="panel_SH_Log" title="售后信息" style="width:805px;padding:1px;font-size:14px;height: 420px;padding: 0;" >
								<table id="dg_order_sh">
									<thead>
									<tr>
										<th data-options="field:'orderno',width:140,align:'center', halign:'center', sortable:true">订单编号</th>
										<th data-options="field:'order_type',width:70,align:'center', halign:'center', sortable:true, formatter:formatOrderType">类型</th>
										<th data-options="field:'opt_time',width:140,align:'center', halign:'center', sortable:true">申请时间</th>
										<th data-options="field:'order_note',width:390,halign:'center',align:'left', halign:'center', tooltip: true">下单原因</th>
									</tr>
									</thead>
								</table>
							</div>
						</div>
						<form id="orderSelSHForm" class="easyui-form" action="javascript:;" style="display: none;height:632px;margin-top:57px;">
							<input type="hidden" id="sel_id" name="id" value=""/>
							<input type="hidden" id="sel_devices_code" name="devices_code" value=""/>
							<input type="hidden" id="sel_devices_class" name="devices_class" value=""/>
							<input type="hidden" id="sel_devices_bmu" name="devices_bmu" value=""/>
							<input type="hidden" id="sel_devices_count" name="devices_count" value=""/>
							<input type="hidden" id="sel_orderType" name="orderType" value="2"/>
							<input type="hidden" id="sel_quantity" name="quantity" value="0"/>
							<input type="hidden" id="sel_boundOrder" name="boundOrder" value="" />
							<input type="hidden" id="sel_shAddType" name="shAddType" value="1"/>
							<div class="easyui-panel" style="background:#FFF;margin-top:0px; height:655px;width:810px; font-size:14px;" >
								<div style="width:790px; text-align:left; height:40px; margin-top:12px;">
									<label style=" margin-left: 20px;"><input type="checkbox" id="add_sw_checkbox1" value="0" />&nbsp;引用原始订单</label>&nbsp;&nbsp;&nbsp;
									<input id="searchSHOrder" name="searchSHOrder" style="width:160px; height:30px;padding:6px 12px;" value="输入订单编号搜索" />
									<label style="margin-left: 30px;">引用前版程序：</label>
									<select class="manage-select" id="sel_beforeSoftware" name="beforeSoftware" class="form-control input-sm"  style="height:28px; width:207px;">
										<option value="">请选择</option>
									</select>
								</div>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:1px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">订单信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*">
											<input type="text" class="manage-input-text" style="width:202px;" id="add_sw_sel_orderno" name="orderno" value="" maxlength="16" onkeyup="checkOrderNo('add');" /><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;"><c:out value="${user.fullName}" /></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创建日期：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;"><c:out value="${init_date}" /></td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px;">客户名称：</td>
										<td style="text-align:left; border:0px;" id="show_sel_corpName" width="*"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_sel_salesman"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">合同编号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_sel_contractNo"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px;">项目描述：</td>
										<td style="text-align:left; border:0px;" width="*" id="show_sel_projectNote"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">技术代表：</td>
										<td style="text-align:left; width:150px; border:0px;height:37px;" id="show_sel_technicalDelegate"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;" >原始订单：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_sel_order_init"> </td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">车辆信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
										<td style="text-align:left; border:0px; height:37px;" id="show_sel_typeName"  width="*"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆型号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_sel_modelName"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_sel_batteryNumber"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
										<td style="text-align:left; border:0px; height:37px;" id="show_sel_factoryName" width="*"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_sel_batteryType"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
										<td style="text-align:left;  width:150px; border:0px; height:37px;" id="show_sel_capacity"> </td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">下单原因</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:center; width:790px; border:0px; height:37px;">
											<textarea rows="3" class="manage-textarea-text" style="width: 750px;" id="sel_orderNote" name="orderNote"></textarea>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">系统配置</b></div>
								<div class="easyui-panel scroll-class" style="background:#FFF;margin-top:0px; height:165px;width:800px; font-size:14px; border:0;" >
									<table class="dclear" width="100%" id="show_sel_device_tab">
										<tbody id="show_sel_device_tab_BCU">
										</tbody>
										<tbody id="show_sel_device_tab_BYU">
										</tbody>
										<tbody id="show_sel_device_tab_BMU">
										</tbody>
										<tbody id="show_sel_device_tab_LDM">
										</tbody>
										<tbody id="show_sel_device_tab_DTU">
										</tbody>
										<tbody id="show_sel_device_tab_LCD">
										</tbody>
										<tbody id="show_sel_device_tab_CS">
										</tbody>
										<tbody id="show_sel_device_tab_OTHER">
										</tbody>
									</table>
								</div>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="manage-page-right-backend dright" style="margin-top: 8px; width:810px; text-align:center; background: #FFF;">
									<input type="button" value=" " onclick="return saveSelOrderSh('add')" class="manage-page-submit-button2" />
								</div>
							</div>
						</form>
						<form id="orderUpdateSHForm" class="easyui-form" action="javascript:;" style="display: none;height:632px;margin-top:57px;">
							<input type="hidden" id="update_orderno" name="orderno" value=""/>
							<div class="easyui-panel" style="background:#FFF;margin-top:0px; height:655px;width:810px; font-size:14px;" >
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:1px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">订单信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" id="show_update_orderno"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_update_ldr"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创建日期：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_update_opttime"></td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px;">客户名称：</td>
										<td style="text-align:left; border:0px;" id="show_update_corpName" width="*"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_update_salesman"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">合同编号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_update_contractNo"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px;">项目描述：</td>
										<td style="text-align:left; border:0px;" width="*" id="show_update_projectNote"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">技术代表：</td>
										<td style="text-align:left; width:150px; border:0px;height:37px;" id="show_update_technicalDelegate"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;" >原始订单：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_update_order_init"> </td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">车辆信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
										<td style="text-align:left; border:0px; height:37px;" id="show_update_typeName"  width="*"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆型号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_update_modelName"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_update_batteryNumber"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
										<td style="text-align:left; border:0px; height:37px;" id="show_update_factoryName" width="*"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="show_update_batteryType"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
										<td style="text-align:left;  width:150px; border:0px; height:37px;" id="show_update_capacity"> </td>
									</tr>
								</table>
								<table class="dclear" style="width:790px;">
									<tr>
										<td style="text-align:right; width:120px; border:0px; height:37px;">引用前版程序：</td>
										<td style="text-align:left; border:0px; height:37px;"  width="*" >
											<select id="update_beforeSoftware" name="beforeSoftware">
											</select>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">下单原因</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:center; width:790px; border:0px; height:37px;">
											<textarea rows="6" class="manage-textarea-text" style="width: 750px;" id="show_update_orderNote" name="orderNote"></textarea>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="manage-page-right-backend dright" style="margin-top: 8px; width:810px; text-align:center; background: #FFF;">
									<input type="button" value=" " onclick="return saveUpdateOrderSh()" class="manage-page-submit-button2" />
								</div>
							</div>
						</form>
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
							<input type="hidden" id="edit_orderType" name="orderType" value="1"/>
							<input type="hidden" id="edit_shAddType" name="shAddType" value="2"/>

							<div class="easyui-panel" style="background:#FFF;margin-top:0px; height:655px;width:810px; font-size:14px;" >
                            	<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">订单信息</b></div>
                            	<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" id="edit_orderno"> </td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="edit_ldr"></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创建日期：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" id="edit_opttime"></td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">客户名称：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" id="show_edit_corp_id">&nbsp;</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="edit_salesman" name="salesman" class="form-control input-sm"  style="height:28px; width:132px;">
												<option value= "-1">无</option>
												<c:forEach items="${salesList}" var="item">
														<option value= "${item[0]}">${item[1]}</option>
												</c:forEach>
											</select>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">合同编号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;" >
											<input type="text" class="manage-input-text" id="edit_contractNo" name="contractNo" value="" maxlength="18" />
										</td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">项目描述：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" >
											<input type="text" class="manage-input-text" style="width:202px;" id="edit_projectNote" name="projectNote" value="" maxlength="100" /><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">技术代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="edit_technicalDelegate" name="technicalDelegate" class="form-control input-sm"  style="height:28px; width:132px;">
												<option value= "-1">无</option>
												<c:forEach items="${technicistList}" var="item">
													<option value= "${item[0]}">${item[1]}</option>
												</c:forEach>
											</select>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;" ><span id="edit_quantity_title">订单套数：</span><span style="display: none;" id="edit_boundOrder_title">原始订单：</span></td>
										<td style="text-align:left; width:150px; border:0px; height:37px;"><input type="text" class="manage-input-text" id="edit_quantity" name="quantity" value="" maxlength="6" onkeyup="this.value=this.value.replace(/\D/g,''); " onafterpaste="this.value=this.value.replace(/\D/g,'')"  />
											<input type="text" class="manage-input-text" style="display: none;" id="edit_boundOrder" name="boundOrder" value="" maxlength="16" />
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">车辆信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*">
											<select class="manage-select" id="edit_vehicleType_id" name="vehicleType.id" class="form-control input-sm"  style="height:28px; width:207px;">
												<option value= "">请选择</option>
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
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="edit_vehicleModel" name="vehicleModel.id" class="form-control input-sm" style="height:28px; width:132px;" onchange="addCartype('edit')">
												<option value= "-1">请选择 </option>
												<option value= "addVType">添加车辆型号</option>
											</select><font color="red" style="margin-left:6px;">*</font></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="edit_batteryNumber" name="batteryModel.batteryNumber" value="" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('add');" onafterpaste="this.value=this.value.replace(/\D/g,'')" /><font color="red" style="margin-left:6px;">*</font>
										</td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
										<td style="text-align:left; border:0px; height:37px;"  width="*">
											<input type="text" class="manage-input-text" style="width:202px;" id="edit_factoryName" name="batteryModel.factoryName" value="" maxlength="30" onkeyup="autoBatteryName();" /><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="edit_batteryModel_batteryType" name="batteryModel.batteryType.id" class="form-control input-sm"  style="height:28px; width:132px;" onchange="autoBatteryName()">
												<option value= "-1">请选择</option>
												<c:forEach items="${bmList}" var="bmItem">
													<option value= "${bmItem.id}">${bmItem.typeName}</option>
												</c:forEach>
											</select><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="edit_capacity" name="batteryModel.capacity" value="" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('add');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
										</td>
									</tr>
								</table>
								<table class="dclear" style="width:790px;" id="show_boundOrderInfo">
									<tr>
										<td style="text-align:right; width:120px; border:0px; height:37px;">引用前版程序：</td>
										<td style="text-align:left; border:0px; height:37px;"  width="*" >
											<input type="text" class="manage-input-text" style="width: 170px;" id="edit_beforeSoftware" name="beforeSoftware" value="" maxlength="16"  />
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">下单原因</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:center; width:790px; border:0px; height:37px;">
											<textarea rows="6" class="manage-textarea-text" style="width: 750px;" id="edit_orderNote" name="orderNote"></textarea>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="manage-page-right-backend dright" style="margin-top: 20px; width:810px; text-align:center; background: #FFF;">
									<input type="button" value=" " onclick="return updateOrderBaseInfo('edit')" class="manage-page-submit-button2">
								</div>
							</div>
						</form>
						<form id="orderEditModuleForm" action="javascript:;" style="display: none;height:632px;margin-top:57px;">
							<input type="hidden" id="edit_module_id" name="id" value="" />
							<input type="hidden" id="edit_module_orderno" name="orderno" value="" />
							<input type="hidden" id="edit_module_devices_code" name="devices_code" value=""/>
							<input type="hidden" id="edit_module_devices_class" name="devices_class" value=""/>
							<input type="hidden" id="edit_module_devices_bmu" name="devices_bmu" value=""/>
							<input type="hidden" id="edit_module_devices_count" name="devices_count" value=""/>
							<div class="easyui-panel" style="background:#FFF;margin-top:0px; height:655px;width:810px; font-size:14px;" >
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">系统配置</b></div>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="easyui-panel scroll-class" style="background:#FFF;margin-top:0px; height:500px;width:800px; font-size:14px; border:0;" >
									<table class="dclear" style="width:750px;" id="edit_module_device_tab">
										<tbody id="edit_module_device_tab_BCU">
										</tbody>
										<tbody id="edit_module_device_tab_BYU">
										</tbody>
										<tbody id="edit_module_device_tab_BMU">
										</tbody>
										<tbody id="edit_module_device_tab_LDM">
										</tbody>
										<tbody id="edit_module_device_tab_DTU">
										</tbody>
										<tbody id="edit_module_device_tab_LCD">
										</tbody>
										<tbody id="edit_module_device_tab_CS">
										</tbody>
										<tbody id="edit_module_device_tab_OTHER">
										</tbody>
									</table>
									<div style="padding:10px; margin-left:10px;">
										<span class="icon-add" style="cursor: pointer;" onclick="show_device_add('edit_module')">&nbsp;&nbsp;&nbsp;&nbsp;</span>
									</div>
								</div>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="manage-page-right-backend dright" style="margin-top: 20px; width:810px; text-align:center; background: #FFF;">
									<input type="button" value=" " onclick="return updateOrderModule()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>

						<form id="orderAddForm" action="javascript:;" style="display: none; height:632px; margin-top: 57px;">
							<input type="hidden" id="add_id" name="id" value="" />
							<input type="hidden" id="add_optTime_" name="optTime" value="1900-01-01 23:59:59"/>
							<input type="hidden" id="add_optUser" name="optUser" value="10"/>
							<input type="hidden" id="add_status" name="status" value="0"/>
							<input type="hidden" id="add_corp_id" name="corp.id" value=""/>
							<input type="hidden" id="add_batteryModel_id" name="batteryModel.id" value=""/>
							<input type="hidden" id="add_batteryModel_batteryName" name="batteryModel.batteryName" value=""/>
							<input type="hidden" id="add_vehicleModel_vehicleType_id" name="vehicleModel.vehicleType.id" value=""/>
							<input type="hidden" id="add_vehicleModel_modelName" name="vehicleModel.modelName" value=""/>
							<input type="hidden" id="add_checkOrderNo" name="add_checkOrderNo" value="0"/>
							<input type="hidden" id="add_opt_type" name="opt_type" value="add"/>
							<input type="hidden" id="add_devices_code" name="devices_code" value=""/>
							<input type="hidden" id="add_devices_class" name="devices_class" value=""/>
							<input type="hidden" id="add_devices_bmu" name="devices_bmu" value=""/>
							<input type="hidden" id="add_devices_count" name="devices_count" value=""/>
							<input type="hidden" id="add_boundOrder" name="boundOrder" value=""/>
							<input type="hidden" id="add_orderType" name="orderType" value="1"/>
							<input type="hidden" id="add_shAddType" name="shAddType" value="2"/>

							<div class="easyui-panel" style="background:#FFF;margin-top:0px; height:655px;width:810px; font-size:14px;" >
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">订单信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*">
											<input type="text" class="manage-input-text" style="width:202px;" id="add_orderno" name="orderno" value="" maxlength="16" onkeyup="checkOrderNo('add');" /><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;"><c:out value="${user.fullName}" /></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创建日期：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;"><c:out value="${init_date}" /></td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">客户名称：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" id="show_add_corp_id">&nbsp;</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="add_salesman" name="salesman" class="form-control input-sm"  style="height:28px; width:132px;">
												<option value= "-1">无</option>
												<c:forEach items="${salesList}" var="item">
													<option value= "${item[0]}">${item[1]}</option>
												</c:forEach>
											</select>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">合同编号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="add_contractNo" name="contractNo" value="" maxlength="18" />
										</td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">项目描述：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" >
											<input type="text" class="manage-input-text" style="width:202px;" id="add_projectNote" name="projectNote" value="" maxlength="100" /><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">技术代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="add_technicalDelegate" name="technicalDelegate" class="form-control input-sm"  style="height:28px; width:132px;">
												<option value= "-1">无</option>
												<c:forEach items="${technicistList}" var="item">
													<option value= "${item[0]}">${item[1]}</option>
												</c:forEach>
											</select>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">订单套数：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="add_quantity" name="quantity" value="" maxlength="6" onkeyup="this.value=this.value.replace(/\D/g,'');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">车辆信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*">
											<select class="manage-select" id="add_vehicleType_id" name="vehicleType.id" class="form-control input-sm"  style="height:28px; width:207px;">
												<option value= "">请选择</option>
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
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="add_vehicleModel" name="vehicleModel.id" class="form-control input-sm" style="height:28px; width:132px;" onchange="addCartype('add')">
												<option value= "-1">请选择 </option>
												<option value= "addVType">添加车辆型号</option>
											</select><font color="red" style="margin-left:6px;">*</font></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="add_batteryNumber" name="batteryModel.batteryNumber" value="" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('add');" onafterpaste="this.value=this.value.replace(/\D/g,'')" /><font color="red" style="margin-left:6px;">*</font>
										</td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
										<td style="text-align:left; border:0px; height:37px;"  width="*">
											<input type="text" class="manage-input-text" style="width:202px;" id="add_factoryName" name="batteryModel.factoryName" value="" maxlength="30" onkeyup="autoBatteryName();" /><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="add_batteryModel_batteryType" name="batteryModel.batteryType.id" class="form-control input-sm"  style="height:28px; width:132px;" onchange="autoBatteryName()">
												<option value= "-1">请选择</option>
												<c:forEach items="${bmList}" var="bmItem">
													<option value= "${bmItem.id}">${bmItem.typeName}</option>
												</c:forEach>
											</select><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="add_capacity" name="batteryModel.capacity" value="" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('add');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">下单原因</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:center; width:790px; border:0px; height:37px;">
											<textarea rows="3" class="manage-textarea-text" style="width: 750px;" id="add_orderNote" name="orderNote">新订单</textarea>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">系统配置</b></div>
								<div class="easyui-panel scroll-class" style="background:#FFF;margin-top:0px; height:220px;width:800px; font-size:14px; border:0;" >
									<table class="dclear" style="width:750px;" id="add_device_tab">
										<tbody id="add_device_tab_BCU">
										</tbody>
										<tbody id="add_device_tab_BYU">
										</tbody>
										<tbody id="add_device_tab_BMU">
										</tbody>
										<tbody id="add_device_tab_LDM">
										</tbody>
										<tbody id="add_device_tab_DTU">
										</tbody>
										<tbody id="add_device_tab_LCD">
										</tbody>
										<tbody id="add_device_tab_CS">
										</tbody>
										<tbody id="add_device_tab_OTHER">
										</tbody>
									</table>
									<div style="padding:10px; margin-left:10px;">
										<span class="icon-add" style="cursor: pointer;" onclick="show_device_add('add')">&nbsp;&nbsp;&nbsp;&nbsp;</span>
									</div>
								</div>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="manage-page-right-backend dright" style="margin-top: 8px; width:810px; text-align:center; background: #FFF;">
									<input type="button" value=" " onclick="return addOrder('add')" class="manage-page-submit-button2" />
								</div>
							</div>
						</form>
						<form id="orderAddSWForm" action="javascript:;" style="display: none; height:632px; margin-top: 57px;">
							<input type="hidden" id="add_sw_id" name="id" value="" />
							<input type="hidden" id="add_sw_optTime_" name="optTime" value="1900-01-01 23:59:59"/>
							<input type="hidden" id="add_sw_optUser" name="optUser" value="10"/>
							<input type="hidden" id="add_sw_status" name="status" value="0"/>
							<input type="hidden" id="add_sw_corp_id" name="corp.id" value=""/>
							<input type="hidden" id="add_sw_batteryModel_id" name="batteryModel.id" value=""/>
							<input type="hidden" id="add_sw_batteryModel_batteryName" name="batteryModel.batteryName" value=""/>
							<input type="hidden" id="add_sw_vehicleModel_vehicleType_id" name="vehicleModel.vehicleType.id" value=""/>
							<input type="hidden" id="add_sw_vehicleModel_modelName" name="vehicleModel.modelName" value=""/>
							<input type="hidden" id="add_sw_checkOrderNo" name="add_sw_checkOrderNo" value="0"/>
							<input type="hidden" id="add_sw_devices_code" name="devices_code" value=""/>
							<input type="hidden" id="add_sw_devices_class" name="devices_class" value=""/>
							<input type="hidden" id="add_sw_devices_bmu" name="devices_bmu" value=""/>
							<input type="hidden" id="add_sw_devices_count" name="devices_count" value=""/>
							<input type="hidden" id="add_sw_orderType" name="orderType" value="2"/>
							<input type="hidden" id="add_sw_quantity" name="quantity" value="0"/>
							<input type="hidden" id="add_sw_shAddType" name="shAddType" value="2"/>
							<input type="hidden" id="add_sw_opt_type" name="opt_type" value="add_sw"/>

							<div class="easyui-panel" style="background:#FFF;margin-top:0px; height:655px;width:810px; font-size:14px;" >
								<div style="width:790px; text-align:left; height:23px; line-height: 23px; margin-top:12px;">
									<label style=" margin-left: 20px;"><input type="checkbox" id="add_sw_checkbox0" value="0" />&nbsp;引用原始订单</label>&nbsp;&nbsp;&nbsp;
									<input id="searchSHOrder_1" name="searchSHOrder_1" class="manage-input-text" style="width:160px; " value="" disabled readonly />
									<label style="margin-left: 23px;">引用前版程序：</label><input id="add_sw_beforeSoftware" name="beforeSoftware" class="manage-input-text" style="width:160px; " value="" maxlength="16" />
								</div>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:18px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">订单信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">订单编号：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*">
											<input type="text" class="manage-input-text" style="width:202px;" id="add_sw_orderno" name="orderno" value="" maxlength="16" onkeyup="checkOrderNo('add_sw');" /><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;"><c:out value="${user.fullName}" /></td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">创建日期：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;"><c:out value="${init_date}" /></td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">客户名称：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" id="show_add_sw_corp_id">&nbsp;</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">销售代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="add_sw_salesman" name="salesman" class="form-control input-sm"  style="height:28px; width:132px;">
												<option value= "-1">无</option>
												<c:forEach items="${salesList}" var="item">
													<option value= "${item[0]}">${item[1]}</option>
												</c:forEach>
											</select>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">合同编号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="add_sw_contractNo" name="contractNo" value="" maxlength="18" />
										</td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">项目描述：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*" >
											<input type="text" class="manage-input-text" style="width:202px;" id="add_sw_projectNote" name="projectNote" value="" maxlength="100" /><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">技术代表：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="add_sw_technicalDelegate" name="technicalDelegate" class="form-control input-sm"  style="height:28px; width:132px;">
												<option value= "-1">无</option>
												<c:forEach items="${technicistList}" var="item">
													<option value= "${item[0]}">${item[1]}</option>
												</c:forEach>
											</select>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">原始订单：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="add_sw_boundOrder" name="boundOrder" value="" maxlength="16" />
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">车辆信息</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆类型：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*">
											<select class="manage-select" id="add_sw_vehicleType_id" name="vehicleType.id" class="form-control input-sm"  style="height:28px; width:207px;">
												<option value= "">请选择</option>
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
										<td style="text-align:right; width:90px; border:0px; height:37px;">车辆型号：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="add_sw_vehicleModel" name="vehicleModel.id" class="form-control input-sm" style="height:28px; width:132px;" onchange="addCartype('add_sw')">
												<option value= "">请选择 </option>
											</select>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池串数：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="add_sw_batteryNumber" name="batteryModel.batteryNumber" value="" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('add');" onafterpaste="this.value=this.value.replace(/\D/g,'')" /><font color="red" style="margin-left:6px;">*</font>
										</td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池厂商：</td>
										<td style="text-align:left; border:0px; height:37px;"  width="*">
											<input type="text" class="manage-input-text" style="width:202px;" id="add_sw_factoryName" name="batteryModel.factoryName" value="" maxlength="30" onkeyup="autoBatteryName();" />
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池类型：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<select class="manage-select" id="add_sw_batteryModel_batteryType" name="batteryModel.batteryType.id" class="form-control input-sm"  style="height:28px; width:132px;" onchange="autoBatteryName()">
												<option value= "-1">请选择</option>
												<c:forEach items="${bmList}" var="bmItem">
													<option value= "${bmItem.id}">${bmItem.typeName}</option>
												</c:forEach>
											</select><font color="red" style="margin-left:6px;">*</font>
										</td>
										<td style="text-align:right; width:90px; border:0px; height:37px;">电池容量：</td>
										<td style="text-align:left; width:150px; border:0px; height:37px;">
											<input type="text" class="manage-input-text" id="add_sw_capacity" name="batteryModel.capacity" value="" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,''); autoBatteryName('add');" onafterpaste="this.value=this.value.replace(/\D/g,'')"  /><font color="red" style="margin-left:6px;">*</font>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">下单原因</b></div>
								<table class="dclear" style="width:790px;" >
									<tr>
										<td style="text-align:center; width:790px; border:0px; height:37px;">
											<textarea rows="3" class="manage-textarea-text" style="width: 750px;" id="add_sw_orderNote" name="orderNote"></textarea>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div style="width:790px; text-align:left; height:20px; margin-top:8px;">	<b style="margin-left:20px;margin-top:20px;">系统配置</b></div>
								<div class="easyui-panel scroll-class" style="background:#FFF;margin-top:0px; height:165px;width:800px; font-size:14px; border:0;" >
									<table class="dclear" style="width:750px;" id="add_sw_device_tab">
										<tbody id="add_sw_device_tab_BCU">
										</tbody>
										<tbody id="add_sw_device_tab_BYU">
										</tbody>
										<tbody id="add_sw_device_tab_BMU">
										</tbody>
										<tbody id="add_sw_device_tab_LDM">
										</tbody>
										<tbody id="add_sw_device_tab_DTU">
										</tbody>
										<tbody id="add_sw_device_tab_LCD">
										</tbody>
										<tbody id="add_sw_device_tab_CS">
										</tbody>
										<tbody id="add_sw_device_tab_OTHER">
										</tbody>
									</table>
									<div style="padding:10px; margin-left:10px;">
										<span class="icon-add" style="cursor: pointer;" onclick="show_device_add('add_sw')">&nbsp;&nbsp;&nbsp;&nbsp;</span>
									</div>
								</div>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="manage-page-right-backend dright" style="margin-top: 8px; width:810px; text-align:center; background: #FFF;">
									<input type="button" value=" " onclick="return addOrder('add_sw')" class="manage-page-submit-button2" />
								</div>
							</div>
						</form>

						<!-- ================================================客户管理部分==================================================================================== -->
						<div class="easyui-panel" id="userInfoShow" style="display:none; background:#FFF;margin-top:1px; margin-top:57px;height:655px;width:810px; font-size:14px;overflow: hidden;" align="right" >
							<div class="easyui-panel" title="基本信息" style="width:805px;height:200px;padding:1px;font-size:14px;"	 data-options="iconCls:'icon-book-go',tools:'#tt_base_user'">
								<table class="dclear" style="width:800px;">
									<tr>
										<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
										<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpName_show"> </td>
										<td style="text-align:right; width:110px; border:0px; height:37px;">联系人姓名：</td>
										<td style="text-align:left; border:0px; height:37px;" id="linkMan_show"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
										<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpPhone_show"> </td>
										<td style="text-align:right; width:110px; border:0px; height:37px;">联系人电话：</td>
										<td style="text-align:left; border:0px; height:37px;" id="linkPhone_show"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
										<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpEmail_show"> </td>
										<td style="text-align:right; width:110px; border:0px; height:37px;">联系人邮箱：</td>
										<td style="text-align:left; border:0px; height:37px;" id="linkEmail_show"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
										<td style="text-align:left; border:0px; height:37px;" id="corpAddr_show" colspan="3"> </td>
									</tr>
									<tr>
										<td style="text-align:right; width:110px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
										<td style="text-align:left; width:230px; border:0px; height:37px;" id="optUserName_show"> </td>
										<td style="text-align:right; width:110px; border:0px; height:37px;">创&nbsp;建&nbsp;时&nbsp;间：</td>
										<td style="text-align:left; border:0px; height:37px;" id="optTime_show"> </td>
									</tr>
									<tr style="display:none;">
										<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
										<td style="text-align:left; width:230px; border:0px; height:37px;" id="userName_show"></td>
										<td style="text-align:right; width:110px; border:0px;">用&nbsp;户&nbsp;&nbsp;密&nbsp;码：</td>
										<td style="text-align:left;border:0px;" id="userPwd_show"></td>
									</tr>
								</table>
							</div>
							<div id="tt_base_user">
								<a href="javascript:void(0)" class="icon-edit" onclick="toEditUserPage()"></a>
							</div>
							<div class="easyui-panel" title="订单信息" style="width:805px;padding:1px;font-size:14px;height: 420px;padding: 0;" data-options="iconCls:'icon-book-go',tools:'#tt_module_user',collapsible:false">
								<table id="dg_order" >
									<thead>
									<tr>
										<th data-options="field:'orderno',width:140,align:'center', halign:'center', sortable:true, formatter:formatOrderInfo">订单编号</th>
										<th data-options="field:'project_note',width:202,align:'left', halign:'center', sortable:true">项目描述</th>
										<th data-options="field:'quantity',width:50,align:'center', halign:'center', sortable:true">套数</th>
										<th data-options="field:'order_type',width:70,align:'center', halign:'center', sortable:true, formatter:formatOrderType">类型</th>
										<th data-options="field:'opt_user_name',width:100,align:'center', halign:'center', sortable:true">录单人</th>
										<th data-options="field:'opttime',width:130,halign:'center',align:'center', halign:'center', sortable:true">录单日期</th>
										<th data-options="field:'id',width:45,halign:'center',align:'center', halign:'center', formatter:formatDel">操作</th>
									</tr>
									</thead>
								</table>
								<div id="search_dg_order" style="height:38px; line-height: 38px; padding-left: 10px; font-size: 14px;">
									订单编号：<input type="text" class="manage-input-text" name="dg_order_keyword" id="dg_order_keyword" style="width: 170px;" />
									<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="searchInfo()">查询</a>
								</div>
							</div>
							<div id="tt_module_user">
								<a href="javascript:void(0)" class="icon-add" onclick="javascript:show_sel_order()"></a>
							</div>
						</div>

						<form id="userEditForm" action="javascript:;" style="display: none;">
							<input type="hidden" id="id" name="id" value="" />
							<input type="hidden" id="optUser" name="optUser" value=""/>
							<input type="hidden" id="optTime" name="optTime" value=""/>
							<div class="manage-page-right-backend dright" style="background:#FFF; margin-top:57px; height: 655px;">
								<div class="manage-page-item" style="width:810px;">
									<div class="manage-page-item-inner clearfix" style="width:810px;">
										<div class="manage-page-right-backend dright" style="padding-bottom:5px;">
											<div style="width:810px; text-align:left; margin-top:15px;height:25px;">
												<b style="margin-left:20px;">客户信息</b>
											</div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpNameEdit" name="corpName" class="manage-input-text" maxlength="20" /><font color="red">*</font>
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人姓名：</td>
													<td style="text-align:left; border:0px; height:37px;" >
														<input type="text" id="linkManEdit" name="linkMan" class="manage-input-text" maxlength="10" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpPhoneEdit" name="phone" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人电话：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="linkPhoneEdit" name="linkPhone" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpEmailEdit" name="email" class="manage-input-text" maxlength="50" />
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人邮箱：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="linkEmailEdit" name="linkEmail" class="manage-input-text" maxlength="100" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
													<td style="text-align:left; border:0px; height:37px;" colspan="3">
														<input type="text" id="corpAddrEdit" name="addr" class="manage-input-text" maxlength="100" style="width:466px;" />
													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
								<div style="width: 810px; margin-top: 10px;background: #FFF;" align="center" >
									<input type="button" value=" " onclick="return updateUser()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>

						<form id="userAddForm" action="javascript:;" style="display: none;">
							<input type="hidden" id="corpIdAdd" name="id" value=""/>
							<div class="manage-page-right-backend dright" style=" background:#FFF; margin-top:57px;height: 655px;">
								<div class="manage-page-item" style="width:810px;">
									<div class="manage-page-item-inner clearfix" style="width:810px;">
										<div class="manage-page-right-backend dright" style="padding-bottom:5px;">
											<div style="width:810px; text-align:left; margin-top:15px;height:25px;">
												<b style="margin-left:20px;">客户信息</b>
											</div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpcorpNameAdd" name="corpName" class="manage-input-text" maxlength="20" /><font color="red">*</font>
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人姓名：</td>
													<td style="text-align:left; border:0px; height:37px;" >
														<input type="text" id="linkManAdd" name="linkMan" class="manage-input-text" maxlength="10" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpphoneAdd" name="phone" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人电话：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="linkPhoneAdd" name="linkPhone" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpemailAdd" name="email" class="manage-input-text" maxlength="50" />
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人邮箱：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="linkEmailAdd" name="linkEmail" class="manage-input-text" maxlength="100" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
													<td style="text-align:left; border:0px; height:37px;" colspan="3">
														<input type="text" id="corpaddrAdd" name="addr" class="manage-input-text" maxlength="100" style="width:466px;" />
													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
								<div style="width: 810px; margin-top: 10px; background: #FFF;" align="center" >
									<input type="button" value=" " onclick="return addUser()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="page-footer-backend dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->

	<script type="text/javascript">
		//=============================================================================================
		function dateFormatStr(obj){
			if(obj == null || obj == undefined || obj == ""){
				return "";
			}
			var fmt = "";
			fmt += (parseInt(obj.year) + 1900) + "-";
			var monthNum = (parseInt(obj.month) + 1);
			if(monthNum < 10){
				fmt += "0" + monthNum + "-";
			}else{
				fmt += monthNum + "-";
			}
			fmt += obj.date + " ";
			fmt += obj.hours + ":";
			fmt += obj.minutes + ":";
			fmt += obj.seconds;
			return fmt;
		}

		function findUserLi(selNextId){
			var userId = 0;
			if(null == selNextId || undefined == selNextId){

			}else if(selNextId == -999){
				$("#selUserId").val("");//清空
			}
			else{
				userId = selNextId ;
				$("#selUserId").val("");//清空
			}
			var selId = $("#selUserId").val();
			$("#userdata").html("");
			$.get("${ctx}/customer/getCorpLi?rmd=" + new Date().getTime(), {userId: userId, usertype:0},function(data){
				var li = "";
				var result = eval(data);
				$.each(result, function(index, items){
					li += "<li userLIData='" + items[0] + "' style='position:relative;' onmouseover='showCorpDelIcon(this, 1)' onmouseout='showCorpDelIcon(this, 0)' ";
					if(selId == ""){
						if(index == 0){
							li += " class='manage-leftactive' ";
							userIdchange(items[0]);
						}
					}else{
						if(selId == items[0]){
							li += " class='manage-leftactive' ";
							userIdchange(items[0]);
						}
					}
					li += " >";
					li += "<a href='javascript:;' onclick='userIdchange(" + items[0] + ")' title='" + items[1] + "'>";
					if( items[1].length > 9){
						li += items[1].substring(0, 8) + "...";
					}else{
						li += items[1];
					}
					li += "</a>";
					li += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
					li += "<img onclick='delUserFun(" + items[0] + ", \"" + items[1] + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
					li += "</div>";
					li += "</li>";
				});
				$("#userdata").html(li);
			});
		}

		function showCorpDelIcon(obj, type){
			$("#userdata").find("li").each(function(indexNum, ele){
				$(this).removeClass("manage-lefthover")
			});
			if(type == 1){
				$(obj).addClass("manage-lefthover");
			}
			if(showDelIconType == 1){
				if(type == 1){
					$(obj).find('.handle').show();
				}else{
					$(obj).find('.handle').hide();
				}
			}
		}
		function updateUser(){
			if ($("#corpName").val() == ""){
				$.messager.alert("系统提示", "请填写公司名称", "warning");
				return;
			}
			var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/ ;
			var corpMail = $.trim($("#corpEmailEdit").val());
			if (corpMail.length >0){
				if(!reg.test(corpMail)){
					$.messager.alert("系统提示", "邮箱格式不正确", "warning");
					return;
				}
			}
			var linkEmailEdit = $.trim($("#linkEmailEdit").val());
			if (linkEmailEdit.length >0){
				if(!reg.test(linkEmailEdit)){
					$.messager.alert("系统提示", "邮箱格式不正确", "warning");
					return;
				}
			}
			$.post("${ctx}/customer/saveCorp?rmd=" + new Date().getTime(), $("#userEditForm").serialize(),function(data){
				if(data == "-1"){
					$.messager.alert("系统提示", "客户名称重复", "info");
				}else{
					$.messager.alert("系统提示", "操作成功", "info");
					findUserLi();
				}
			});
		}
		function addUser(){
			if ($("#corpcorpNameAdd").val() == ""){
				$.messager.alert("系统提示", "请填写公司名称", "warning");
				return;
			}
			var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/ ;
			var corpMail = $.trim($("#corpemailAdd").val());
			if (corpMail.length >0){
				if(!reg.test(corpMail)){
					$.messager.alert("系统提示", "邮箱格式不正确", "warning");
					return;
				}
			}
			var linkEmailAdd = $.trim($("#linkEmailAdd").val());
			if (linkEmailAdd.length >0){
				if(!reg.test(linkEmailAdd)){
					$.messager.alert("系统提示", "邮箱格式不正确", "warning");s
					return;
				}
			}
			$.post("${ctx}/customer/saveCorp?rmd=" + new Date().getTime(), $("#userAddForm").serialize(),function(data){
				if(data == "-1"){
					$.messager.alert("系统提示", "客户名称重复", "info");
				}else{
					$.messager.alert("系统提示", "操作成功", "info");
					$("#selUserId").val(data);
					findUserLi();
				}
			});
		}
		function delUserFun(id, corpName){
			$.messager.confirm("删除确认", "是否确认删除客户<br>" + corpName + " ？", function(cr){
				if(cr){
					$.post("${ctx}/customer/delCorp?rmd=" + new Date().getTime(), {corpId : id},function(data){
						if(data == "-1"){
							$.messager.alert("系统提示", "用户存在关联订单，不能删除", "warning");
						}else if(data == "-2"){
							$.messager.alert("系统提示", "用户存在关联账号，不能删除", "warning");
						}else if(data == "1"){
							$.messager.alert("系统提示", "操作成功", "info");
							$("#selUserId").val("");
							findUserLi();
						}else{
							findUserLi();
						}
					});
				}
			});
		}

		function userIdchange(id){
			$("#show_head_title").html("客户详细信息");
			$("#selUserId").val(id);
			$("#userdata").find("li").each(function(indexNum, ele){
				$(this).removeClass("manage-leftactive");
				if($(this).attr("userLIData") == id){
					$(this).addClass("manage-leftactive");
				}
			});
			$.get("${ctx}/customer/getCorpInfo?rmd=" + new Date().getTime(), {corpId:id},function(data){
				var dataObj = eval('(' + data + ')');
				var corpObj = dataObj.corp;
				var corpNameStr = corpObj.corpName;
				$("#corpNameEdit").val(corpNameStr);
				if(corpNameStr.length > 16){
					$("#corpName_show").html(corpNameStr.substring(0, 15) + "...");
				}else{
					$("#corpName_show").html(corpNameStr);
				}
				/***********************
				 * 订单初始化部分
				 **********************/
				$("#add_corp_id").val(corpObj.id);
				$("#add_sw_corp_id").val(corpObj.id);
				$("#show_add_corp_id").html(corpNameStr);
				$("#show_edit_corp_id").html(corpNameStr);
				$("#show_add_sw_corp_id").html(corpNameStr);
				$("#corpPhoneEdit").val(corpObj.phone);
				$("#corpPhone_show").html(corpObj.phone == null || corpObj.phone=="" ? "-" : corpObj.phone);
				$("#corpEmailEdit").val(corpObj.email);
				$("#corpEmail_show").html(corpObj.email == null || corpObj.email=="" ? "-" : corpObj.email);
				$("#corpAddrEdit").val(corpObj.addr);
				$("#corpAddr_show").html(corpObj.addr == null || corpObj.addr=="" ? "-" : corpObj.addr);
				$("#linkPhoneEdit").val(corpObj.linkPhone);
				$("#linkPhone_show").html(corpObj.linkPhone == null || corpObj.linkPhone=="" ? "-" : corpObj.linkPhone);
				$("#linkEmailEdit").val(corpObj.linkEmail);
				$("#linkEmail_show").html(corpObj.linkEmail == null || corpObj.linkEmail=="" ? "-" : corpObj.linkEmail);
				$("#linkManEdit").val(corpObj.linkMan);
				$("#linkMan_show").html(corpObj.linkMan == null || corpObj.linkMan=="" ? "-" : corpObj.linkMan);
				$("#id").val(corpObj.id);
				$("#optUser").val(corpObj.optUser);
				$("#optTime").val(dateFormatStr(corpObj.optTime));
				$("#optUserName_show").html(dataObj.userName);
				$("#optTime_show").html(dateFormatStr(corpObj.optTime));
			});
			toBackPage_new();
			getOrderForCorp();
		}
		//查询
		function searchInfo(){
			searchNormalFun("dg_order", {"orderno": $("#dg_order_keyword").val()});
		}

		function getOrderForCorp(){
			// EASYUI列表部分
			$('#dg_order').datagrid({
				rownumbers:true,
				toolbar:'#search_dg_order',
				singleSelect: true,
				url: '${ctx}/order/getOrderDataByCorp?corpid=' + $("#selUserId").val() + '&rmd=' + new Date().getTime(),
				autoRowHeight:false,
				enableHeaderContextMenu:false,//表头右键
				enableRowContextMenu:false,//行右键
				onLoadSuccess: function(data){
					initSelSH(data.rows);
				},
				/*pagination:true,
				 pageSize:20,*/
				width:803,
				height:405
			});
			if($("#dg_order_keyword").val() != "" ){
				$("#dg_order_keyword").val("");
				searchNormalFun("dg_order", {"orderno": $("#dg_order_keyword").val()});
			}
		}

		function formatDel(val,row){
			return '<img src="${ctx}/static/images/del.png" style="cursor: pointer; width:20px;" title="删除订单" alt="删除订单" onclick="deleteOrder(\'' + row.id + '\',\'' + row.orderno + '\')" />';
		}

		function formatOrderInfo(val,row){
			return '<a href="javascript:void(0)" onclick="orderNoChange(\'' + row.orderno + '\')" >' + row.orderno + '</a>';
		}
		function formatOrderType(val,row){
			if(val == 1){
				return "新订单";
			}else if(val == 2){
				return "软件售后";
			}else if(val == 3){
				return "硬件售后";
			}else{
				return "&nbsp;";
			}
		}
		function deleteOrder(id, orderno){
			$.messager.confirm("删除确认", "是否确认删除订单？<br>编号：" + orderno , function(cr){
				if(cr){
					$.post(sys_rootpath + "/order/deleteOrder?rmd=" + new Date().getTime(), {orderid : id, orderno : orderno},function(data){
						var msg = "操作失败";
						if(data == "ok"){
							msg = "操作成功";
							getOrderForCorp();
						}else if(data == "-3"){
							msg = "订单已绑定售后订单，不能删除";
						}else if(data == "-1"){
							msg = "订单已有软件上传，不能删除";
						}else if(data == "-2"){
							msg = "订单已有设备绑定，不能删除";
						}
						$.messager.alert("系统提示", msg, "info");
					});
				}
			});
		}

		function toBackPage_new(){
			$("#orderInfoShow").css("display", "none");
			$("#orderEditForm").css("display", "none");
			$("#orderAddForm").css("display", "none");
			$("#orderAddSWForm").css("display", "none");
			$("#orderSelSHForm").css("display", "none");
			$("#orderEditModuleForm").css("display", "none");
			$("#orderUpdateSHForm").css("display", "none");

			$("#userAddForm").css("display", "none");
			$("#userEditForm").css("display", "none");
			$("#userInfoShow").css("display", "block");
		}
		function toAddUserPage(){
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#userAddForm").css("display", "block");
		}
		function toEditUserPage(){
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#userEditForm").css("display", "block");
		}
		function toOrderInfoPage(){
			$("#show_head_title").html("订单详细信息");
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#orderInfoShow").css("display", "block");
		}
		function toOrderAddPage(){
			$("#show_head_title").html("添加新订单");
			$("#addDeviceType").val("add");
			$("#page_orderType").val(1);
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#orderAddForm").css("display", "block");
			$("#orderAddForm")[0].reset();
			$("#add_device_tab_BCU").html("");
			$("#add_device_tab_BYU").html("");
			$("#add_device_tab_BMU").html("");
			$("#add_device_tab_LDM").html("");
			$("#add_device_tab_DTU").html("");
			$("#add_device_tab_LCD").html("");
			$("#add_device_tab_CS").html("");
			$("#add_device_tab_OTHER").html("");
		}
		function toOrderAddSHPage(orderType){
			$("#addDeviceType").val("add_sw");
			if(orderType == 2){
				$("#page_orderType").val(orderType);
				$("#show_head_title").html("添加软件售后订单");
			}else if(orderType == 3){
				$("#page_orderType").val(orderType);
				$("#show_head_title").html("添加硬件售后订单");
			}
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#orderAddSWForm").css("display", "block");
			$("#orderAddSWForm")[0].reset();
			$("#add_sw_device_tab_BCU").html("");
			$("#add_sw_device_tab_BYU").html("");
			$("#add_sw_device_tab_BMU").html("");
			$("#add_sw_device_tab_LDM").html("");
			$("#add_sw_device_tab_DTU").html("");
			$("#add_sw_device_tab_LCD").html("");
			$("#add_sw_device_tab_CS").html("");
			$("#add_sw_device_tab_OTHER").html("");
		}

		function toOrderEditPage(){
			$("#addDeviceType").val("edit");
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#orderEditForm").css("display", "block");
			$("#show_head_title").html("订单基本信息修改");
		}
		function toUpdateSHOrderPage(){
			$("#addDeviceType").val("edit");
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#orderUpdateSHForm").css("display", "block");
			$("#orderUpdateSHForm")[0].reset();
			$("#show_head_title").html("售后订单信息修改");
		}

		function toOrderSelPage(){
			$("#sel_id").val("");
			$("#show_head_title").html("添加售后订单");
			$("#addDeviceType").val("add_sel");
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#orderSelSHForm").css("display", "block");
			$("#orderSelSHForm")[0].reset();
			cleanSelOrder();
			$('#searchSHOrder').combobox("setValue", "");
			$("#show_sel_device_tab_BCU").html("");
			$("#show_sel_device_tab_BYU").html("");
			$("#show_sel_device_tab_BMU").html("");
			$("#show_sel_device_tab_LDM").html("");
			$("#show_sel_device_tab_DTU").html("");
			$("#show_sel_device_tab_LCD").html("");
			$("#show_sel_device_tab_CS").html("");
			$("#show_sel_device_tab_OTHER").html("");
			$("#sel_beforeSoftware").empty();
			$("#sel_beforeSoftware").append("<option value=''>请选择</option>");
		}

		function toOrderModulePage(){
			$("#addDeviceType").val("edit_module");
			$("#show_head_title").html("系统配置信息修改");
			toBackPage_new();
			$("#userInfoShow").css("display", "none");
			$("#orderEditModuleForm").css("display", "block");
		}
		//=============================================================================================
		function orderNoChange(id){
			if(id == "-1"){
				return;
			}
			$("#selOrderNo").val(id);

			//清除原信息
			cleanOrderInfo();
			//$.easyui.loading({ msg: "正在加载...", topMost: true });
			$.post("${ctx}/order/findInfoByOrderNo?rmd=" + new Date().getTime(), {orderno:id},function(data){
				if(data != "-1"){
					var result = eval("(" + data + ")");
					var orderObj = result.order ;

					$("#page_orderType").val(orderObj.orderType);
					$("#page_shAddType").val(orderObj.shAddType);//引用的订单不能修改系统配置部分 硬件可修改数量
					if(orderObj.orderType == 1){
						$("#show_head_title").html("新订单详细信息");
					}else if(orderObj.orderType == 2){
						$("#show_head_title").html("软件售后订单详细信息");
					}else{
						$("#show_head_title").html("硬件售后订单详细信息");
					}

					$("#show_ldr").html(result.username);
					var show_order_time = result.opttime_edit;
					$("#show_opttime").html(show_order_time.substring(0, 10));

					$("#show_orderno").html(orderObj.orderno);
					/**
					 * 系统配置修改页面
					 */
					$("#edit_module_id").val(orderObj.id);
					$("#edit_module_orderno").val(orderObj.orderno);

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

					$("#edit_quantity").val(orderObj.quantity);
					$("#show_projectNote").html(orderObj.projectNote);
					$("#edit_projectNote").val(orderObj.projectNote);
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
					$("#show_orderNote").html(orderObj.orderNote);
					$("#edit_orderNote").val(orderObj.orderNote);
					$("#edit_beforeSoftware").val(orderObj.beforeSoftware);
					//新订单隐藏 引用前版程序
					if(orderObj.orderType == 1){
						$("#show_boundOrderInfo").css("display", "none");
						$("#edit_boundOrder_title").css("display", "none");
						$("#edit_boundOrder").css("display", "none");
						$("#edit_quantity_title").css("display", "block");
						$("#edit_quantity").css("display", "block");
					}else{
						$("#show_boundOrderInfo").css("display", "block");
						$("#edit_boundOrder_title").css("display", "block");
						$("#edit_boundOrder").css("display", "block");
						$("#edit_quantity_title").css("display", "none");
						$("#edit_quantity").css("display", "none");
					}

					//修改页信息
					$("#edit_ldr").html(result.username);
					$("#edit_opttime").html(show_order_time.substring(0, 10));

					$("#edit_orderno").html(orderObj.orderno);
					$("#edit_contractNo").val(orderObj.contractNo);
					$("#edit_salesman").val(orderObj.salesman);
					$("#edit_technicalDelegate").val(orderObj.technicalDelegate);
					//$('#searchUserKey_edit').combobox('setValue', orderObj.corp.id);
					$('#edit_corp_id').val(orderObj.corp.id);

					$("#edit_factoryName").val(orderObj.batteryModel.factoryName);
					//$("#edit_batteryType").html(orderObj.batteryModel.batteryType.typeName);
					$("#edit_batteryNumber").val(orderObj.batteryModel.batteryNumber);
					$("#edit_capacity").val(orderObj.batteryModel.capacity);

					//隐藏域初始化
					$("#edit_id").val(orderObj.id);
					$("#edit_orderno_").val(orderObj.orderno);
					$("#edit_optUser").val(orderObj.optUser);
					$("#edit_optTime_").val(result.opttime_edit);
					$("#edit_batteryModel_id").val(orderObj.batteryModel.id);
					//$("#edit_vehicleModel_batteryName").val(orderObj.batteryModel.batteryName);
					$("#edit_batteryModel_batteryType").val(orderObj.batteryModel.batteryType.id);
					$("#edit_status").val(orderObj.status);
					$("#edit_optUserName").val(orderObj.optUserName);
					$("#edit_orderType").val(orderObj.orderType);
					$("#edit_shAddType").val(orderObj.shAddType);
					$("#edit_boundOrder").val(orderObj.boundOrder);

					if(orderObj.vehicleType != null && orderObj.vehicleType != undefined){
						$("#edit_vehicleType_id").val(orderObj.vehicleType.id);
					}else{
						$("#edit_vehicleType_id").val("");
					}
					if(orderObj.vehicleModel != null && orderObj.vehicleModel != undefined){
						$("#edit_vehicleModel").val(orderObj.vehicleModel.id);
						$("#edit_vehicleModel_old").val(orderObj.vehicleModel.id);
						$("#edit_vehicleModel_vehicleType_id").val(orderObj.vehicleModel.vehicleType.id);
					}else{
						$("#edit_vehicleModel").val("");
						$("#edit_vehicleModel_old").val("");
						$("#edit_vehicleModel_vehicleType_id").val("");
					}
					$("#edit_vehicleType_id").change();

					var order_deviceArr = result.order_device  ;
					var order_deviceFileArr = result.order_device_file  ;
					var add_device_str = "";
					var device_class = "";
					var device_code = "";
					var device_bum_no = "";
					var device_count = "";

					if(null != order_deviceArr && undefined != order_deviceArr && "" != order_deviceArr){
						var deviceObj = [];
						for(var i=0; i<order_deviceArr.length; i++){
							deviceObj = order_deviceArr[i];
							device_class = deviceObj[1];
							device_code = deviceObj[0];
							device_bum_no = deviceObj[2];
							device_count = deviceObj[4];

							if(orderObj.orderType == 1){//新订单和硬件订单
								$("#edit_module_device_tab_" + deviceObj[1]).append(addDeviceTR(deviceObj[1], deviceObj[0], deviceObj[2], "edit", device_count));
							}else{
								if(orderObj.shAddType == 2){
									$("#edit_module_device_tab_" + deviceObj[1]).append(addDeviceTR(deviceObj[1], deviceObj[0], deviceObj[2], "edit", device_count));
								}else{
									$("#edit_module_device_tab_" + deviceObj[1]).append(addDeviceTR(deviceObj[1], deviceObj[0], deviceObj[2], "sw", device_count));
								}
							}
							/*****************************************************************************************************
							 * 浏览页显示订单软件
							 *****************************************************************************************************/
							var add_device_str = "<tr style='color:#000; background-color:#ECECF0;height:30px;'>" +
									"<td style=\"text-align:right; width:90px; border:0px; height:30px;\">设备类型：</td>" +
									"<td style=\"text-align:left; width:60px; border:0px; height:30px;\">" + device_class + "</td>" +
									"<td style=\"text-align:right; width:90px; border:0px; height:30px;\">设备型号：</td>" +
									"<td style=\"text-align:left; width:80px; border:0px; height:30px;\">" + device_code + "</td>";
							if("BMU" == device_class){
								add_device_str += "<td style=\"text-align:right; width:90px; border:0px; height:30px;\">ID编号：</td>";
								//add_device_str += "<td style=\"text-align:left; border:0px; height:30px;\" width=\"*\" >#" + device_bum_no + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数量：";
								//不显示数量
								add_device_str += "<td style=\"text-align:left; border:0px; height:30px;\" width=\"*\" >#" + device_bum_no ;
							}else{
								//add_device_str += "<td style=\"text-align:right; width:90px; border:0px; height:30px;\">数量：</td><td style=\"text-align:left; border:0px; height:30px;\" width=\"*\" >";
								//不显示数量
								add_device_str += "<td style=\"text-align:right; width:90px; border:0px; height:30px;\">&nbsp;</td><td style=\"text-align:left; border:0px; height:30px;\" width=\"*\" >";
							}
							add_device_str += "</td></tr>";

							if(device_class == "BCU" || device_class == "BYU" || device_class == "OTHER"){
								add_device_str += "<tr>" +
										"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">订单固件：</td>" +
										"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='5' >";
								if(undefined != order_deviceFileArr){
									fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_s19"];
									if(undefined != fileObj){
										add_device_str += "<div style='float: left;'><a href='" + sys_rootpath + "/fileOperate/downloadById?id=" + fileObj[1] + "&device_code=" + device_code + "' title='" + fileObj[0] + "'>";
										if(fileObj[0].length > 45){
											add_device_str += fileObj[0].substring(0, 45) + "...";
										}else{
											add_device_str += fileObj[0];
										}

										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
												"</div></td></tr>";
									}else{
										add_device_str += "&nbsp;</td></tr>";
									}
								}else{
									add_device_str += "&nbsp;</td></tr>";
								}

								add_device_str += "<tr>" +
										"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">配置文件：</td>" +
										"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='5'>";
								if(undefined != order_deviceFileArr){
									fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_cfg"];
									if(undefined != fileObj){
										add_device_str += "<div style='float: left;'><a href='" + sys_rootpath + "/fileOperate/downloadById?id=" + fileObj[1] + "' title='" + fileObj[0] + "'>";
										if(fileObj[0].length > 45){
											add_device_str += fileObj[0].substring(0, 45) + "...";
										}else{
											add_device_str += fileObj[0];
										}
										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
												"</div></td></tr>";
									}else{
										add_device_str += "&nbsp;</td></tr>";
									}
								}else{
									add_device_str += "&nbsp;</td></tr>";
								}

								add_device_str += "<tr>" +
										"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">规则文件：</td>" +
										"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='5'>";
								if(undefined != order_deviceFileArr){
									fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_csf"];
									if(undefined != fileObj){
										add_device_str += "<div style='float: left;'><a href='" + sys_rootpath + "/fileOperate/downloadById?id=" + fileObj[1] + "' title='" + fileObj[0] + "'>";
										if(fileObj[0].length > 45){
											add_device_str += fileObj[0].substring(0, 45) + "...";
										}else{
											add_device_str += fileObj[0];
										}
										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
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
										"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='5'>";
								if(undefined != order_deviceFileArr){
									fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_s19"];
									if(undefined != fileObj){
										add_device_str += "<div style='float: left;'><a href='" + sys_rootpath + "/fileOperate/downloadById?id=" + fileObj[1] + "&device_code=" + device_code + "' title='" + fileObj[0] + "'>";
										if(fileObj[1] == "usetemplate"){
											add_device_str += "使用模版";
										}else {
											if (fileObj[0].length > 45) {
												add_device_str += fileObj[0].substring(0, 45) + "...";
											} else {
												add_device_str += fileObj[0];
											}
										}
										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
												"</div></td></tr>";
									}else{
										add_device_str += "&nbsp;</td></tr>";
									}
								}else{
									add_device_str += "&nbsp;</td></tr>";
								}
								add_device_str += "<tr>" +
										"<td style=\"text-align:right; width:90px; border:0px; height:37px;\">配置文件：</td>" +
										"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='5'>";
								if(undefined != order_deviceFileArr){
									fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_cfg"];
									if(undefined != fileObj){
										add_device_str += "<div style='float: left;'><a href='" + sys_rootpath + "/fileOperate/downloadById?id=" + fileObj[1] + "' title='" + fileObj[0] + "'>";
										if(fileObj[0].length > 45){
											add_device_str += fileObj[0].substring(0, 45) + "...";
										}else{
											add_device_str += fileObj[0];
										}
										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
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
										"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='5'>";
								if(undefined != order_deviceFileArr){
									fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_s19"];
									if(undefined != fileObj){
										add_device_str += "<div style='float: left;'><a href='" + sys_rootpath + "/fileOperate/downloadById?id=" + fileObj[1] + "&device_code=" + device_code + "' title='" + fileObj[0] + "'>";
										if(fileObj[1] == "usetemplate"){
											add_device_str += "使用模版";
										}else {
											if (fileObj[0].length > 45) {
												add_device_str += fileObj[0].substring(0, 45) + "...";
											} else {
												add_device_str += fileObj[0];
											}
										}
										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
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
										"<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='5'>";
								if(undefined != order_deviceFileArr){
									fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_zip"];
									if(undefined != fileObj){
										add_device_str += "<div style='float: left;'><a href='" + sys_rootpath + "/fileOperate/downloadById?id=" + fileObj[1] + "&device_code=" + device_code + "' title='" + fileObj[0] + "'>";
										if(fileObj[1] == "usetemplate"){
											add_device_str += "使用模版";
										}else {
											if (fileObj[0].length > 45) {
												add_device_str += fileObj[0].substring(0, 45) + "...";
											} else {
												add_device_str += fileObj[0];
											}
										}
										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
												"</div></td></tr>";
									}else{
										add_device_str += "&nbsp;</td></tr>";
									}
								}else{
									add_device_str += "&nbsp;</td></tr>";
								}
							}
							else if(device_class == "CS" ){
								//无上传文件
								add_device_str += "<tr><td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" colspan='6'>&nbsp;</td></tr>";
							}
							$("#show_device_tab_" + deviceObj[1]).append(add_device_str);//对象不能被传递

							/*****************************************************************************************************
							 * 浏览页显示订单软件
							 *****************************************************************************************************/
						}
					}
				}
			});
			toOrderInfoPage();
			//售后信息
			getSHData(id);
		}

		function cleanOrderInfo(){
			$("#show_orderNote").val("");
			$("#add_orderno").val("");
			$("#add_salesman").val("");
			$("#add_batteryModel_batteryName").val("");
			$("#add_vehicleType_id").val("");
			$("#add_vehicleModel").empty();
			$("#add_vehicleModel").append("<option value=''>请选择</option>");
			$("#add_batteryModel_batteryType").val("");
			$("#add_factoryName").val("");
			$("#add_batteryNumber").val("");
			$("#add_capacity").val("");
			$("#add_checkOrderNo").val("0");

			//清除设备信息
			$("#edit_module_device_tab_BCU").html("");
			$("#edit_module_device_tab_BYU").html("");
			$("#edit_module_device_tab_BMU").html("");
			$("#edit_module_device_tab_LDM").html("");
			$("#edit_module_device_tab_DTU").html("");
			$("#edit_module_device_tab_LCD").html("");
			$("#edit_module_device_tab_CS").html("");
			$("#edit_module_device_tab_OTHER").html("");

			$("#add_device_tab_BCU").html("");
			$("#add_device_tab_BYU").html("");
			$("#add_device_tab_BMU").html("");
			$("#add_device_tab_LDM").html("");
			$("#add_device_tab_DTU").html("");
			$("#add_device_tab_LCD").html("");
			$("#add_device_tab_CS").html("");
			$("#add_device_tab_OTHER").html("");

			$("#show_device_tab_BCU").html("");
			$("#show_device_tab_BYU").html("");
			$("#show_device_tab_BMU").html("");
			$("#show_device_tab_LDM").html("");
			$("#show_device_tab_DTU").html("");
			$("#show_device_tab_LCD").html("");
			$("#show_device_tab_CS").html("");
			$("#show_device_tab_OTHER").html("");
		}

		function getSHData(orderNo){
			// EASYUI列表部分
			$('#dg_order_sh').datagrid({
				rownumbers:true,
				singleSelect: true,
				url: '${ctx}/order/getSHData?orderno=' + orderNo + '&rmd=' + new Date().getTime(),
				autoRowHeight:false,
				enableHeaderContextMenu:false,//表头右键
				enableRowContextMenu:false,//行右键
				/*pagination:true,
				 pageSize:20,*/
				width:803,
				height:400
			});
		}

		function show_sel_order(){
			//获取已选择
			$.easyui.showDialog({
				title: "选择订单类型",
				width: 330, height: 210,
				content:$("#selOrderType_dialog").html(),
				topMost: true,
				enableApplyButton: false,
				onSave: function (d) {
					var dataForm = d.form("getData");
					var selValue = dataForm.selOrderType ;
					if(selValue != null && selValue != ""){
						// 1- 新订单 2-软件售后订单 3-硬件售后订单
						if(selValue == 1){
							toOrderAddPage();
						}else if(selValue == 2){
							toOrderAddSHPage(2);
						}else if(selValue == 3){
							toOrderAddSHPage(3);
						}
					}else{
						$.messager.alert("系统提示", "请选择订单类型", "warning");
						return false;
					}
				}
			});
		}

		//初始化售后订单下拉选项
		function initSelSH(arr){
			var corpOrderArr = [];
			var corpOrderArrr = [];
			var str = "";
			for(var i=0; i< arr.length; i++){
				str = arr[i].orderno ;
				if(str.indexOf("LG-SH-") < 0){
					corpOrderArr.push({"orderKey": str, "orderValue": str});
					corpOrderArrr.push(arr[i].orderno);
				}
			}
			$('#searchSHOrder').combobox({
				valueField: 'orderKey',
				textField: 'orderValue',
				data: corpOrderArr,
				onSelect: function(record){
					//orderNoSel(record.orderKey);
				},
				onChange: function(newValue, oldValue){
					if(contains(corpOrderArrr, newValue)){
						orderNoSel(newValue);
					}
				}
			});
		}

		//清除售后关联订单信息
		function cleanSelOrder(){
			$("#show_sel_device_tab_BCU").html("");
			$("#show_sel_device_tab_BYU").html("");
			$("#show_sel_device_tab_BMU").html("");
			$("#show_sel_device_tab_LDM").html("");
			$("#show_sel_device_tab_DTU").html("");
			$("#show_sel_device_tab_LCD").html("");
			$("#show_sel_device_tab_CS").html("");
			$("#show_sel_device_tab_OTHER").html("");
			$("#show_sel_order_init").html("");
			$("#sel_boundOrder").val("");
			$("#show_sel_contractNo").html("");
			$("#show_sel_salesman").html("");
			$("#show_sel_technicalDelegate").html("");
			$("#show_sel_corpName").html("");
			$("#show_sel_projectNote").html("");
			$("#show_sel_typeName").html("");
			$("#show_sel_modelName").html("");
			$("#show_sel_factoryName").html("");
			$("#show_sel_batteryType").html("");
			$("#show_sel_batteryNumber").html("");
			$("#show_sel_capacity").html("");

			$("#sel_beforeSoftware").empty();
			$("#sel_beforeSoftware").append("<option value=''>请选择</option>");
		}
		function cleanUpdateSelOrder(){
			$("#show_sel_order_init").html("");
			$("#sel_boundOrder").val("");
			$("#show_sel_contractNo").html("");
			$("#show_sel_salesman").html("");
			$("#show_sel_technicalDelegate").html("");
			$("#show_sel_corpName").html("");
			$("#show_sel_projectNote").html("");
			$("#show_sel_typeName").html("");
			$("#show_sel_modelName").html("");
			$("#show_sel_factoryName").html("");
			$("#show_sel_batteryType").html("");
			$("#show_sel_batteryNumber").html("");
			$("#show_sel_capacity").html("");

			$("#update_beforeSoftware").empty();
		}
		function orderNoSel(orderNo){
			cleanSelOrder();
			$("#sel_boundOrder").val("");
			if(orderNo == "-1"){
				return;
			}
			$.post("${ctx}/order/findOrderInfo?rmd=" + new Date().getTime(), {orderno:orderNo},function(data){
				if(data != "-1"){
					var result = eval("(" + data + ")");
					var orderObj = result.order ;

					$("#show_sel_order_init").html(orderObj.orderno);
					$("#sel_boundOrder").val(orderObj.orderno);
					$("#show_sel_contractNo").html(orderObj.contractNo);
					$("#show_sel_salesman").html(result.salesman_show);
					$("#show_sel_technicalDelegate").html(result.technicist_show);

					$("#show_sel_corpName").html(orderObj.corp.corpName);
					$("#show_sel_projectNote").html(orderObj.projectNote);

					if(orderObj.vehicleType != null && orderObj.vehicleType != undefined){
						$("#show_sel_typeName").html(orderObj.vehicleType.typeName);
					}else{
						$("#show_sel_typeName").html("无");
					}
					if(orderObj.vehicleModel != null && orderObj.vehicleModel != undefined){
						$("#show_sel_modelName").html(orderObj.vehicleModel.modelName);
					}else{
						$("#show_sel_modelName").html("无");
					}

					$("#show_sel_factoryName").html(orderObj.batteryModel.factoryName == "" ? "无" : orderObj.batteryModel.factoryName);
					$("#show_sel_batteryType").html(orderObj.batteryModel.batteryType.typeName);
					$("#show_sel_batteryNumber").html(orderObj.batteryModel.batteryNumber + "串");
					$("#show_sel_capacity").html(orderObj.batteryModel.capacity + "AH");

					var order_deviceArr = result.order_device  ;
					if(!String.isNullOrEmpty(order_deviceArr)){
						var deviceObj = [];
						for(var i=0; i<order_deviceArr.length; i++){
							deviceObj = order_deviceArr[i];
							$("#show_sel_device_tab_" + deviceObj[1]).append(addDeviceTR(deviceObj[1], deviceObj[0], deviceObj[2], "sw", deviceObj[4]));
						}
					}
					var order_shArr = result.order_sh ;
					$("#sel_beforeSoftware").empty();
					$("#sel_beforeSoftware").append("<option value='" + orderNo + "'>" + orderNo + "</option>");
					if(!String.isNullOrEmpty(order_shArr)){
						for(var i=0; i<order_shArr.length; i++){
							$("#sel_beforeSoftware").append("<option value='" + order_shArr[i] + "'>" + order_shArr[i] + "</option>");
						}
					}
				}
			});
		}

		function saveSelOrderSh(type){
			var orderStr = $("#orderSelSHForm #add_sw_sel_orderno").val();
			if(orderStr.length == 14 || orderStr.length == 16){
				if(orderStr.indexOf("LG-SH-")==0 && orderStr.length == 16){
					//检查后面是否全部为数字
					var str = orderStr.substring(6);
					var reg = new RegExp("^[0-9]*$");
					if(!reg.test(str)){
						$.easyui.loaded(true);
						$.messager.alert("系统提示", "订单编号格式不正确<br>正确格式：LG-SH- 开头后接10位数字", "warning");
						return;
					}
				}else{
					$.easyui.loaded(true);
					$.messager.alert("系统提示", "订单编号格式不正确<br>正确格式：LG-SH- 开头后接10位数字", "warning");
					return;
				}
			}else{
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "订单编号长度应为16位字母加数字", "warning");
				return;
			}
			$("#orderSelSHForm #sel_orderType").val($("#page_orderType").val());

			var orderno = $("#orderSelSHForm #sel_boundOrder").val();
			if("" == orderno ){
				$.messager.alert("系统提示", "请选择原始订单", "warning");
				return ;
			}
			var orderNoteStr = $("#orderSelSHForm #sel_orderNote").val();
			orderNoteStr = orderNoteStr.trim();
			if("" == orderNoteStr){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "下单原因不能为空", "warning");
				return;
			}else if(orderNoteStr.length > 200){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "下单原因不能超过两百字", "warning");
				return;
			}

			var formStr = "orderSelSHForm";
			var optType = "sel";
			/********************** 设备信息 *******************************/
			//检查设备型号
			var device_code_sels = "";
			$("#" + formStr + " input[name='device_code_sel']").each(function(x, ele){
				device_code_sels += $(this).val() + ",";
			});
			$("#" + optType + "_devices_code").val(device_code_sels);

			var device_class_sels = "";
			$("#" + formStr + " input[name='device_class_sel']").each(function(x, ele){
				device_class_sels += $(this).val() + ",";
			});
			$("#" + optType + "_devices_class").val(device_class_sels);

			var device_bmu_sels = "";
			$("#" + formStr + " input[name='device_bmu_no']").each(function(x, ele){
				device_bmu_sels += $(this).val() + ",";
			});
			$("#" + optType + "_devices_bmu").val(device_bmu_sels);
			var device_count_sels = "";
			$("#" + formStr + " input[name='device_count']").each(function(x, ele){
				if($(this).val() == ""){
					device_count_sels += "0,";
				}else{
					device_count_sels += $(this).val() + ",";
				}
			});
			$("#" + optType + "_devices_count").val(device_count_sels);
			/********************** 设备信息 *******************************/

			$.post(sys_rootpath + "/order/saveSHOrder?rmd=" + new Date().getTime(), $("#" + formStr).serialize(),function(data){
				$.easyui.loaded(true);
				if(data == "-1"){
					$.messager.alert("系统提示", "订单编号重复", "warning");
				}else if(data == "-2"){
					$.messager.alert("系统提示", "关联订单异常", "warning");
				}else{
					$.messager.alert("系统提示", "操作成功", "warning");
					toBackPage_new();
					searchInfo();
				}
			});
		}
		function orderSHUpdateInit(){
			cleanUpdateSelOrder();
			var orderNo = $("#selOrderNo").val();
			$.post("${ctx}/order/findSHOrderInfo?rmd=" + new Date().getTime(), {orderno:orderNo},function(data){
				if(data != "-1"){
					var result = eval("(" + data + ")");
					var orderObj = result.order ;

					$("#show_update_ldr").html(result.username);
					$("#show_update_opttime").html(result.opttime);
					$("#show_update_order_init").html(orderObj.boundOrder);
					$("#show_update_orderno").html(orderObj.orderno);
					$("#update_orderno").val(orderObj.orderno);
					$("#show_update_contractNo").html(orderObj.contractNo);
					$("#show_update_salesman").html(result.salesman_show);
					$("#show_update_technicalDelegate").html(result.technicist_show);
					$("#show_update_corpName").html(orderObj.corp.corpName);
					$("#show_update_projectNote").html(orderObj.projectNote);
					$("#show_update_orderNote").val(orderObj.orderNote);

					if(orderObj.vehicleType != null && orderObj.vehicleType != undefined){
						$("#show_update_typeName").html(orderObj.vehicleType.typeName);
					}else{
						$("#show_update_typeName").html("无");
					}
					if(orderObj.vehicleModel != null && orderObj.vehicleModel != undefined){
						$("#show_update_modelName").html(orderObj.vehicleModel.modelName);
					}else{
						$("#show_update_modelName").html("无");
					}

					$("#show_update_factoryName").html(orderObj.batteryModel.factoryName == "" ? "无" : orderObj.batteryModel.factoryName);
					$("#show_update_batteryType").html(orderObj.batteryModel.batteryType.typeName);
					$("#show_update_batteryNumber").html(orderObj.batteryModel.batteryNumber + "串");
					$("#show_update_capacity").html(orderObj.batteryModel.capacity + "AH");
					var order_shArr = result.order_sh ;
					$("#update_beforeSoftware").empty();
					$("#update_beforeSoftware").append("<option value='" + orderObj.boundOrder + "'>" + orderObj.boundOrder + "</option>");
					if(!String.isNullOrEmpty(order_shArr)){
						for(var i=0; i<order_shArr.length; i++){
							if(order_shArr[i] != orderNo){ //去除本身
								$("#update_beforeSoftware").append("<option value='" + order_shArr[i] + "'>" + order_shArr[i] + "</option>");
							}
						}
					}
					$("#update_beforeSoftware").val(orderObj.beforeSoftware);
				}
			});
		}

		function toUpdateModule(){
			var shAddType = $("#page_shAddType").val();
			var selOrderNo = $("#selOrderNo").val();
			var orderType = $("#page_orderType").val();
			/**
			 * 软件售后订单，引用方式添加的不能修改系统配置
			 * 硬件售后订单可修改数量
			 **/
			if(orderType == 2 && shAddType == 1){
				$.messager.alert("系统提示", "引用的软件售后订单不能修改系统配置", "warning");
				return;
			}
			toOrderModulePage();
		}
		function toUpdatePage(){
			var shAddType = $("#page_shAddType").val();
			var selOrderNo = $("#selOrderNo").val();
			var orderType = $("#page_orderType").val();
			if(shAddType == 1){
				toUpdateSHOrderPage();
				orderSHUpdateInit();
			}else{
				toOrderEditPage();
			}
		}

		function updateOrderModule(){
			/********************** 设备信息 *******************************/
			//检查设备型号
			var device_code_sels = "";
			$("#orderEditModuleForm input[name='device_code_sel']").each(function(x, ele){
				device_code_sels += $(this).val() + ",";
			});
			$("#edit_module_devices_code").val(device_code_sels);
			var device_class_sels = "";
			$("#orderEditModuleForm input[name='device_class_sel']").each(function(x, ele){
				device_class_sels += $(this).val() + ",";
			});
			$("#edit_module_devices_class").val(device_class_sels);
			var device_bmu_sels = "";
			$("#orderEditModuleForm input[name='device_bmu_no']").each(function(x, ele){
				device_bmu_sels += $(this).val() + ",";
			});
			$("#edit_module_devices_bmu").val(device_bmu_sels);
			var device_count_sels = "";
			$("#orderEditModuleForm input[name='device_count']").each(function(x, ele){
				if($(this).val() == ""){
					device_count_sels += "0,";
				}else{
					device_count_sels += $(this).val() + ",";
				}
			});
			$("#edit_module_devices_count").val(device_count_sels);
			/********************** 设备信息 *******************************/

			$.post(sys_rootpath + "/order/updateOrderModule?rmd=" + new Date().getTime(), $("#orderEditModuleForm").serialize(),function(data){
				$.easyui.loaded(true);
				if(data > 0){
					$.messager.alert("系统提示", "操作成功", "warning");
					orderNoChange($("#edit_module_orderno").val());
				}else if (data == "-1"){
					$.messager.alert("系统提示", "订单已上传软件,不可修改系统配置", "warning");
					orderNoChange($("#edit_module_orderno").val());
				}else if (data == "-2"){
					$.messager.alert("系统提示", "订单已绑定设备,不可修改系统配置", "warning");
					orderNoChange($("#edit_module_orderno").val());
				}else{
					$.messager.alert("系统提示", "操作失败", "warning");
					orderNoChange($("#edit_module_orderno").val());
				}
			});
		}

		function updateOrderBaseInfo(optType){
			$.easyui.loading({ msg: "正在保存...", topMost: true });
			var orderStr = $("#edit_orderno_").val();
			var formStr = getFormStr();
			if ($("#" + optType + "_quantity").val() == ""){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "订单套数不能为空", "warning");
				return;
			}
			if ($("#" + optType + "_projectNote").val() == ""){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "项目描述不能为空", "warning");
				return;
			}
			/** (1)2016年1月12日13:49:14 屏蔽，许多订单无DTU信息
			 *  (2)售后订单 手工录入信息，允许车辆类型、车辆型号、电池厂商为非必填项
			 *	sby 2016年6月4日 15:47:09
			 * **/
			if(orderStr.indexOf("LG-SH-")==0 && orderStr.length == 16){

			}else{
				if ($("#" + optType + "_vehicleType_id").val() == "-1"){
					$.easyui.loaded(true);
					$.messager.alert("系统提示", "车辆类型不能为空", "warning");
					return;
				}

				if ($("#" + optType + "_vehicleModel").val() == "-1"){
					$.easyui.loaded(true);
					$.messager.alert("系统提示", "车辆型号不能为空", "warning");
					return;
				}
				if ($("#" + optType + "_factoryName").val() == ""){
					$.easyui.loaded(true);
					$.messager.alert("系统提示", "电池厂商不能为空", "warning");
					return;
				}
			}

			if ($("#" + optType + "_batteryModel_batteryType").val() == "-1"){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "电池类型不能为空", "warning");
				return;
			}

			if ($("#" + optType + "_batteryNumber").val() == ""){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "电池串数不能为空", "warning");
				return;
			}

			if ($("#" + optType + "_capacity").val() == ""){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "电池容量不能为空", "warning");
				return;
			}

			//车辆型号文本信息
			$("#" + optType + "_vehicleModel_modelName").val($("#" + optType + "_vehicleModel").find("option:selected").text());

			//下单原因不能为空，且不超过200字
			var orderNoteStr = $("#" + optType + "_orderNote").val();
			orderNoteStr = orderNoteStr.trim();
			if("" == orderNoteStr){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "下单原因不能为空", "warning");
				return;
			}else if(orderNoteStr.length > 200){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "下单原因不能超过两百字", "warning");
				return;
			}

			$.post(sys_rootpath + "/order/updateOrder?rmd=" + new Date().getTime(), $("#" + formStr).serialize(),function(data){
				$.easyui.loaded(true);
				if(data == "-1"){
					$.messager.alert("系统提示", "订单套数小于已绑定设备数量", "warning");
				}else{
					$.messager.alert("系统提示", "操作成功", "warning");
					orderNoChange($("#edit_module_orderno").val());
				}
			});
		}

		function saveUpdateOrderSh(){
			var orderNoteStr = $("#show_update_orderNote").val();
			orderNoteStr = orderNoteStr.trim();
			if("" == orderNoteStr){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "下单原因不能为空", "warning");
				return;
			}else if(orderNoteStr.length > 200){
				$.easyui.loaded(true);
				$.messager.alert("系统提示", "下单原因不能超过两百字", "warning");
				return;
			}

			$.post(sys_rootpath + "/order/saveUpdateOrderSh?rmd=" + new Date().getTime(), $("#orderUpdateSHForm").serialize(),function(data){
				$.easyui.loaded(true);
				if(data == "-1"){
					$.messager.alert("系统提示", "已绑定设备，不能修改引用前版程序", "warning");
				}else{
					$.messager.alert("系统提示", "操作成功", "warning");
					orderNoChange($("#update_orderno").val());
				}
			});
		}
	</script>
	</body>
	</html>