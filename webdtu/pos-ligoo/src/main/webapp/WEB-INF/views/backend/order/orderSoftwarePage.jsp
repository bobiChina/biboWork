<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_software"/>
	<title>软件管理</title>
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
	<script src="${ctx}/static/js/uploadify/jquery.uploadify.js"></script>
	<script src="${ctx}/static/js/My97DatePicker/WdatePicker.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.all.min.js"></script>
	<script src="${ctx}/static/js/jquery.euploadify.js"></script>
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
		 width: 126px;
		 height: 18px;
		 line-height: 18px;
		 border: none;
		 margin-top: 2px;
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
    </style>

<script type="text/javascript">
	var showDelIconType = 0;
	<shiro:hasRole name="admin">
	// showDelIconType = 1;
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

	var orderArr = [];
	var orderStrArr = [];
	<c:forEach items="${orderList}" var="item" varStatus="status">orderArr.push({"orderno":"${item[1]}", "ordername": "${item[1]}"});orderStrArr.push("${item[1]}");</c:forEach>
    $('#searchorderKey_li').combobox({
		valueField: 'orderno',
		textField: 'ordername',
		data: orderArr,
    	onSelect: function(record){
			//orderNoChange(record.orderno);
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
		collapsible:false,
		collapsed:false,
		onExpand:function(){show_base_info()},
		onCollapse:function(){show_trackLog()}
	});
	$("#panel_software").panel({
		iconCls:'icon-book-go',
		tools:'#tt_module',
		collapsible:true,
		collapsed:true,
		onExpand:function(){show_base_info()},
		onCollapse:function(){show_trackLog()}
	});
	$("#panel_trackLog").panel({
		iconCls:'icon-book-go',
		tools:'#tt_track',
		collapsible:true,
		collapsed:false,
		onExpand:function(){show_trackLog()},
		onCollapse:function(){show_base_info()}
	});
});

function show_trackLog(){
	$("#panel_base").panel("expand");
	$("#panel_software").panel("collapse");
	$("#panel_trackLog").panel("expand");
}
function show_base_info(){
	$("#panel_base").panel("expand");
	$("#panel_software").panel("expand");

	$("#panel_trackLog").panel("collapse");
}

function getTrackLog(){
	// EASYUI列表部分
	$('#dg').datagrid({
		rownumbers:true,
		singleSelect: true,
		url: '${ctx}/order/getTrackData?orderno='+ $("#selOrderNo").val() + '&rmd=' + new Date().getTime(),
		autoRowHeight:false,
		enableHeaderContextMenu:false,//表头右键
		enableRowContextMenu:false,//行右键
		onDblClickRow:function(rowIndex, rowData){
			/*$.easyui.showDialog({
				title: "更新内容",
				width: 400, height: 226,
				content:"<div style='padding:10px;font-size: 14px;'>" + rowData.remarks + "</div>" ,
				topMost: true,
				enableApplyButton: false,
				enableSaveButton:false
			});*/
		},
		/*pagination:true,
		pageSize:20,*/
		width:803,
		height:365
	});
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
	lcdArr.push("BS07024");

	var csArr = [];
	csArr.push("FS100E2");
	csArr.push("FS300E2");
	csArr.push("FS500E2");
	csArr.push("FS600E2");
	csArr.push("FS100E2T");
	csArr.push("FS300E2T");
	csArr.push("FS500E2T");
	csArr.push("FS600E2T");
	csArr.push("DHAB S/18");
	csArr.push("DHAB S/24");
	csArr.push("DHAB S/45");

</script>
</head>
<body>
<!-- 订单跟踪日志 -->
<div id="order_tracklog_div" style="display:none;">
	<div>
		<div style="float:left; width: 140px; height: 30px; line-height: 30px; font-size: 14px;color: #FFFFFF; background: #ABABAB;" align="center">事&nbsp;件</div>
		<div style="float:right; width: 544px; height: 30px; line-height: 30px; font-size: 14px;color: #FFFFFF; background: #ABABAB;" align="center">更新内容</div>
	</div>
	<div>
		<div class="manage-page-top">
			<ul id="action_nameArr">
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '1'); return false;" >评审接收</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '2'); return false;" >评审完成</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '3'); return false;" >研发接收</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '4'); return false;" >开始开发</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '5'); return false;" >开发完成</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '6'); return false;" >开始测试</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '7'); return false;" >测试完成</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '11'); return false;" style="color: #FF0000;" >其它</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '9'); return false;" style="color: #FF0000;" >暂停</a>
				</li>
				<li>
					<a href="javascript:void(0);" onclick="selAction(this, '10'); return false;" style="color: #FF0000;" >取消</a>
				</li>
			</ul>
		</div>
		<div style="float: right; margin-top: 1px; width: 544px; height: 422px; font-size: 14px; padding-top: 10px;" class="easyui-panel"  id="show_logHtml_div">
			<table class="dclear" style="width:540px;">
				<tr>
					<td style="text-align:center; border:0px; height:37px;font-size: 16px;" width="*">请选择事件!</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<div id="logHtml_1" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" id="orderStatus" value="1" />
	<input type="hidden" name="actionName" value="评审接收" />
	<table_ class="dclear" style="width:540px; ">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">申&nbsp;&nbsp;请&nbsp;人：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<select class="manage-select" id="actionMan" name="actionMan" class="form-control input-sm"  style="height:28px; width:205px;">
					<option value= "">无</option>
					<c:forEach items="${technicistList}" var="item">
						<option value= "${item[1]}">${item[1]}</option>
					</c:forEach>
				</select>
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_2" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" value="2" />
	<input type="hidden" name="actionName" value="评审完成" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">评&nbsp;&nbsp;审&nbsp;人：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<select class="manage-select" id="actionMan" name="actionMan" class="form-control input-sm"  style="height:28px; width:205px;">
					<option value= "">无</option>
					<c:forEach items="${fwList}" var="item">
						<option value= "${item[1]}">${item[1]}</option>
					</c:forEach>
				</select>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">评审交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="reviewDelivery" name="reviewDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">开发工时：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="manage-input-text" style="width:42px;" maxlength="3" id="reviewDevelopDuration" name="reviewDevelopDuration" value="" onKeyPress="if (event.keyCode<48 || event.keyCode>57) event.returnValue=false" /> H
				<lable style="margin-left: 10px;">测试工时：</lable><input type="text" class="manage-input-text" style="width:42px;" maxlength="3" id="reviewTestDuration" name="reviewTestDuration" value="" onKeyPress="if (event.keyCode<48 || event.keyCode>57) event.returnValue=false" /> H
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_3" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" value="3" />
	<input type="hidden" name="actionName" value="研发接收" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">接&nbsp;&nbsp;收&nbsp;人：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<select class="manage-select" id="actionMan" name="actionMan" class="form-control input-sm"  style="height:28px; width:205px;">
					<option value= "">无</option>
					<c:forEach items="${fwList}" var="item">
						<option value= "${item[1]}">${item[1]}</option>
					</c:forEach>
				</select>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">评审交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*" ><span class="show_psjq_td">&nbsp;</span></td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">影响交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<label><input type="checkbox" value="" id="isDiff" name="isDiff" /> 是</label>
				<input type="text" class="Wdate manage-input-text" style="width:165px;" maxlength="10" id="newDelivery" name="newDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_4" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" value="4" />
	<input type="hidden" name="actionName" value="开始开发" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">开发工时：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*" >
				<span class="show_reviewDevelopDuration">0</span> H
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">开&nbsp;&nbsp;发&nbsp;人：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="manage-input-text" style="width:202px;" maxlength="10" id="actionMan" name="actionMan" value="" />
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">评审交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*" ><span class="show_psjq_td">&nbsp;</span></td_>
		</tr_>
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">开发交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="developDelivery" name="developDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">影响交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<label><input type="checkbox" value="" id="isDiff" name="isDiff" /> 是</label>
				<input type="text" class="Wdate manage-input-text" style="width:165px;" maxlength="10" id="newDelivery" name="newDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_5" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" value="5" />
	<input type="hidden" name="actionName" value="开发完成" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">开发工时：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<span class="show_reviewDevelopDuration">0</span> H
				<lable style="margin-left: 50px;">实际工时：</lable><input type="text" class="manage-input-text" style="width:42px;" maxlength="3" id="developDuration" name="developDuration" value="" onKeyPress="if (event.keyCode<48 || event.keyCode>57) event.returnValue=false" /> H
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">开&nbsp;&nbsp;发&nbsp;人：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="manage-input-text" style="width:202px;" maxlength="10" id="actionMan" name="actionMan" value="" />
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">评审交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*" ><span class="show_psjq_td">&nbsp;</span></td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">影响交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<label><input type="checkbox" value="" id="isDiff" name="isDiff" /> 是</label>
				<input type="text" class="Wdate manage-input-text" style="width:165px;" maxlength="10" id="newDelivery" name="newDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_6" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" value="6" />
	<input type="hidden" name="actionName" value="开始测试" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">测试工时：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<span class="show_reviewTestDuration">0</span> H
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">测&nbsp;&nbsp;试&nbsp;人：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="manage-input-text" style="width:202px;" maxlength="10" id="actionMan" name="actionMan" value="" />
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">评审交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*" ><span class="show_psjq_td">&nbsp;</span></td_>
		</tr_>
		<tr_ id="testDelivery_tr" style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">测试交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="testDelivery" name="testDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">影响交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<label><input type="checkbox" value="" id="isDiff" name="isDiff" /> 是</label>
				<input type="text" class="Wdate manage-input-text" style="width:165px;" maxlength="10" id="newDelivery" name="newDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_7" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" value="7" />
	<input type="hidden" name="actionName" value="测试完成" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">测试工时：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<span class="show_reviewTestDuration">0</span> H
				<lable style="margin-left: 50px;">实际工时：</lable><input type="text" class="manage-input-text" style="width:42px;" maxlength="3" id="testDuration" name="testDuration" value="" onKeyPress="if (event.keyCode<48 || event.keyCode>57) event.returnValue=false" /> H
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">测&nbsp;&nbsp;试&nbsp;人：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="manage-input-text" style="width:202px;" maxlength="10" id="actionMan" name="actionMan" value="" />
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">评审交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*" ><span class="show_psjq_td">&nbsp;</span></td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">影响交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<label><input type="checkbox" value="" id="isDiff" name="isDiff" /> 是</label>
				<input type="text" class="Wdate manage-input-text" style="width:165px;" maxlength="10" id="newDelivery" name="newDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_9" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" value="9" />
	<input type="hidden" name="actionName" value="暂停" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_10" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="orderStatus" value="10" />
	<input type="hidden" name="actionName" value="取消" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<div id="logHtml_11" style="display: none;">
	<input type="hidden" class="hidden_orderno" name="orderno" value="" />
	<input type="hidden" name="actionName" value="其他" />
	<table_ class="dclear" style="width:540px;">
		<tr_ style="display: none;">
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">事件日期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="Wdate manage-input-text" style="width:202px;" maxlength="10" id="actionDate" name="actionDate" value="${init_date}" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '2015-01-01', maxDate: '${init_date}' })" readonly />&nbsp;&nbsp;
				<label><input type="radio" name="amPm" value="AM" checked /> AM</label>&nbsp;
				<label><input type="radio" name="amPm" value="PM"/> PM</label>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">订单状态：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<select id="orderStatus" name="orderStatus" class="manage-select" style="width: 206px;height: 28px; line-height: 28px;">
					<option value="1">评审中</option>
					<option value="2">待接收</option>
					<option value="3">待开发</option>
					<option value="4">开发中</option>
					<option value="5">待测试</option>
					<option value="6">测试中</option>
					<option value="7">待上传</option>
					<option value="8">已齐套</option>
				</select>
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">相关人员：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<input type="text" class="manage-input-text" style="width:202px;" maxlength="10" id="actionMan" name="actionMan" value="" />
			</td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">评审交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*" ><span class="show_psjq_td">&nbsp;</span></td_>
		</tr_>
		<tr_>
			<td_ style="text-align:right; width:120px; border:0px; height:37px;">影响交期：</td_>
			<td_ style="text-align:left; border:0px; height:37px;" width="*">
				<label><input type="checkbox" value="" id="isDiff" name="isDiff" /> 是</label>
				<input type="text" class="Wdate manage-input-text" style="width:165px;" maxlength="10" id="newDelivery" name="newDelivery" value="" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '${init_date}', maxDate: '2099-12-31' })" readonly />
			</td_>
		</tr_>
		<tr_>
			<td_ colspan="2" align="left" style="padding-left: 50px; padding-top: 5px;">
				<textarea name="remarks" id="remarks" style="width: 380px;height: 200px;"></textarea>
			</td_>
		</tr_>
	</table_>
</div>
<!-- 订单跟踪日志 -->

<input type="hidden" id="addDeviceType" name="addDeviceType" value="" />
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
					<option value= "CS">CS</option>
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

<input type="hidden" id="selOrderNo" name="selOrderNo" value="" />
<input type="hidden" id="userDtu_old" name="userDtu_old" value="" />
	<div class="body-outer dclear clearfix" style="width:1020px;">
		<div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
			<div class="page-table-body" style="width:1020px;">
				<!-- ---------------------------head start -------------------------- -->
				<div class="history-save-table-head-backend">
					<div class="history-save-table-head-outer">
						<div class="manage-page-head-left dleft">
							<div class="dleft" style="width:150px;" onclick='findOrderLi(1)'><a href="javascript:void(0)" style="color:#FFF; font-size:14px;">订单列表</a></div>
							<div class="dright" style="width:24px;display:none;" onclick='toAddPage()' ><img src="${ctx}/static/images/add.png" style="width:20px;margin-top:12px;" title="添加订单" alt="添加订单" /></div>
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
										<li orderLIData="${item[1]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;' onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' >
											<a href="javascript:void(0);" onclick="orderNoChange('${item[1]}'); return false;" >${item[1]}</a>
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
											<textarea rows="3" style="width: 670px; border: 0;"  name="show_orderNote"></textarea>
										</td>
									</tr>
								</table>
							</div>
							<div id="tt_module">
								<a href="javascript:void(0)" class="icon-cloud-upload" onclick="javascript:toUpLoadPage()"></a>
							</div>
							<div class="easyui-panel" id="panel_software" title="系统配置" style="width:805px;padding:1px;font-size:14px; height:315px;" >
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
							<div class="easyui-panel" id="panel_trackLog" title="订单日志" style="width:805px;padding:0;font-size:14px; height:380px;" >
								<table id="dg">
									<thead>
									<tr>
										<th data-options="field:'action_date',width:149,align:'center', halign:'center', sortable:true">事件日期</th>
										<th data-options="field:'action_name',width:80,align:'center', halign:'center', sortable:true">事件</th>
										<th data-options="field:'order_status',width:70,align:'center', halign:'center', sortable:true">订单状态</th>
										<th data-options="field:'review_delivery',width:85,align:'center', halign:'center', sortable:true">预计交期</th>
										<th data-options="field:'remarks',width:361,halign:'center',align:'left', halign:'center', tooltip: true">更新内容</th>
									</tr>
									</thead>
								</table>
							</div>
							<div id="tt_track">
								<a href="javascript:void(0)" class="icon-add" onclick="javascript:show_tracklog()"></a>
								<a href="javascript:void(0)" class="icon-remove" onclick="javascript:del_tracklog()"></a>
							</div>
						</div>
						<form id="orderSWUploadForm" class="easyui-form" method="post" action="${ctx}/order/orderFileUpload" data-options="novalidate:true" style="display: none; height:655px;"  align="center">
							<input type="hidden" id="upload_orderno" name="orderno" value="" />
							<div class="easyui-panel" align="center" style="background:#FFF;margin-top:57px; height:650px;width:810px; font-size:14px;" >
                            	<div style="float: left;width:190px; text-align:left; height:40px; line-height: 40px; ">
									<b style="margin-left:20px;">软件上传</b>
								</div>
								<div style="float: right; width:590px; text-align:right; height:40px; line-height: 40px; " id="div_label_uploadType">
									<label style=""><input type="radio" name="uploadType" id="uploadType_1" value="1" checked onclick="changBcuLcd()" />&nbsp;齐套上传 </label>
									<label style="margin-right:40px;" ><input type="radio" name="uploadType" id="uploadType_2" value="2" onclick="changBcuLcd()" />&nbsp;非齐套上传 </label>
								</div>
								<table class="dclear" style="width:790px; " >
									<tbody id="upload_device_tab_BCU" style="width:790px; ">
									</tbody>
								</table>
								<table class="dclear" style="width:790px; " >
									<tbody id="upload_device_tab_BYU" style="width:790px; ">
									</tbody>
								</table>
								<table class="dclear" style="width:790px; " >
									<tbody id="upload_device_tab_BMU" style="width:790px; ">
									</tbody>
								</table>
								<table class="dclear" style="width:790px; " >
									<tbody id="upload_device_tab_LDM" style="width:790px; ">
									</tbody>
								</table>
								<table class="dclear" style="width:790px; " >
									<tbody id="upload_device_tab_DTU" style="width:790px; ">
									</tbody>
								</table>
								<table class="dclear" style="width:790px; " >
									<tbody id="upload_device_tab_LCD" style="width:790px; ">
									</tbody>
								</table>
								<table class="dclear" style="width:790px; " >
									<tbody id="upload_device_tab_OTHER" style="width:790px; ">
									</tbody>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="manage-page-right-backend dright" style="margin-top: 20px; width:800px; text-align:center; background: #FFF;">
									<input type="button" value=" " onclick="submitForm()"  class="manage-page-submit-button2">
								</div>
                            </div>
						</form>
					</div>
				</div>
			</div>
			<div class="page-footer-backend dclear">
			</div>
		</div>
		<form id="hiddenForm" class="easyui-form" method="post" action="${ctx}/order/checkSn4DelDevice" data-options="novalidate:true" style="display: none;">
			<input id="hiddenForm_orderno" name="orderno" value="orderno" type="hidden" />
			<input id="hiddenForm_deviceClass" name="device_class" value="device_class" type="hidden" />
		</form>
	</div> <!-- //body outer end -->
	<script type="text/javascript">
		function initUploadOrderFile(idStr, init_value, init_name, device_type){
			idStr = idStr.replace("(", "-L").replace(")", "-R");// 替换左右括号
			/**************************************
			 * 初始化信息 2016年5月16日 15:08:50
			 * var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
			 *************************************/
			var idsArr = idStr.split("_");
			var suffix = idsArr[3];
			var prdfix = idsArr[0] + "_" + idsArr[1] + "_" + idsArr[2];
			if(undefined != $("#" +idStr) && $("#" +idStr) != null){
				if(init_value == "usetemplate"){
					init_name = "使用模版";
				}
				if(null != init_value && undefined != init_value){
					var show_name = "";
					if("s19" == suffix){
						show_name = "订单固件";
					}else if("cfg" == suffix){
						show_name = "配置文件";
					}
					else if("csf" == suffix){
						show_name = "规则文件";
					}
					else if("zip" == suffix){
						show_name = "压缩文件";
					}
					/**
					 *  显示的时候为 文件名称 后加重新上传 ，点击重新上传时恢复到未上传<tr>状态，从机需考虑使用模板按钮
					 *  usetemplate
					 */
					var add_device_str = "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">" + show_name + "：</td>" +
							"<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr.substring(0, (idStr.length -3)) + "td\">" + init_name +
							"<input id=\"" + idStr + "_" + device_type + "\" name=\"" + idStr + "_devicefile\" value=\"" + init_value + "\" type=\"hidden\" /></td>" +
							"<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
							"<label><input type='checkbox' id=\"" + idStr + "_checkbox\" name=\"checkbox_change\" value=\"" + prdfix + "\" onclick=\"changeUploadAgain(this,'" + prdfix + "')\" /> 重新上传</label>" +
							"&nbsp;&nbsp;<label><input type='checkbox' id=\"" + idStr + "_checkbox_del\" name=\"checkbox_change_del\" value=\"" + prdfix + "\" onclick=\"delDeviceFile(this,'" + prdfix + "')\" /> 软件删除</label></td>";

					/**
					 * 查询该模块是否已存在,按前缀查找
					 */
					var isUse = false;
					$("input[value='" + prdfix + "'][type='checkbox']").each(function(i, ele){
						isUse = true;
					});
					if(isUse){
						add_device_str = "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">" + show_name + "：</td>" +
								"<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr.substring(0, (idStr.length -3)) + "td\">" + init_name +
								"<input id=\"" + idStr + "_" + device_type + "\" name=\"" + idStr + "_devicefile\" value=\"" + init_value + "\" type=\"hidden\" /></td>" +
								"<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td>";
					}
					var trObj = $("#" + idStr).parents("tr");
					$("#" +idStr).euploadify("destroy");
					trObj.html("");
					trObj.html(add_device_str);
				}
			}
		}

		function delDeviceFile(obj, prdfix){
			// 取消验证设备是否绑定的限制 sby 2016年10月14日 13:52:28
			$.messager.confirm("软件删除确认", "同时删除所属模块所有软件<br>是否确定删除？", function(cr){
				if(cr){
					$('#hiddenForm').form('submit',{
						url: sys_rootpath + '/order/delOrderDevice?ram=' + new Date().getMilliseconds() ,
						onSubmit:function(param){
							$.easyui.loading({ msg: "正在提交...", topMost: true });
							$("#hiddenForm_orderno").val($("#selOrderNo").val());
							var arr = prdfix.split("_");
							$("#hiddenForm_deviceClass").val(arr[0]);
							return true; // 返回false终止表单提交
						},
						success: function(data){
							$.easyui.loaded(true);
							//重新加载订单信息
							if(data > -1){
								$.messager.alert("系统提示", "操作成功", "warning");
								orderNoChange($("#selOrderNo").val());
							}else{
								$.messager.alert("系统提示", "操作失败", "warning");
								$(obj).attr("checked",false);
							}
						}
					});
				}else{
					$(obj).attr("checked",false);
				}
			});
			/*
			//检查设备绑定情况  checkSn4DelDevice
			$('#hiddenForm').form('submit',{
				url: sys_rootpath + '/order/checkSn4DelDevice?ram=' + new Date().getMilliseconds() ,
				onSubmit:function(param){
					$.easyui.loading({ msg: "正在查询...", topMost: true });
					$("#hiddenForm_orderno").val($("#selOrderNo").val());
					var arr = prdfix.split("_");
					$("#hiddenForm_deviceClass").val(arr[0]);
					return true; // 返回false终止表单提交
				},
				success: function(data){
					$.easyui.loaded(true);
					if(data > 0){
						$(obj).attr("checked",false);
						$.messager.alert("系统提示", "该型号已有设备绑定,需先解绑设备", "warning");
						return ;
					}else{
						$.messager.confirm("软件删除确认", "同时删除所属模块所有软件<br>是否确定删除？", function(cr){
							if(cr){
								$('#hiddenForm').form('submit',{
									url: sys_rootpath + '/order/delOrderDevice?ram=' + new Date().getMilliseconds() ,
									onSubmit:function(param){
										$.easyui.loading({ msg: "正在提交...", topMost: true });
										$("#hiddenForm_orderno").val($("#selOrderNo").val());
										var arr = prdfix.split("_");
										$("#hiddenForm_deviceClass").val(arr[0]);
										return true; // 返回false终止表单提交
									},
									success: function(data){
										$.easyui.loaded(true);
										//重新加载订单信息
										if(data > -1){
											$.messager.alert("系统提示", "操作成功", "warning");
											orderNoChange($("#selOrderNo").val());
										}else{
											$.messager.alert("系统提示", "操作失败", "warning");
											$(obj).attr("checked",false);
										}
									}
								});
							}else{
								$(obj).attr("checked",false);
							}
						});
					}
				}
			});
			*/
		}

		function changeUploadAgain(obj, prdfix){
			// 取消验证设备是否绑定的限制 sby 2016年10月14日 13:52:28
			$.messager.confirm("软件重新上传确认", "是否重新上传？", function(cr){
				if(cr){
					$.easyui.loading({ msg: "正在初始化...", topMost: true });
					var orderNo = $("#selOrderNo").val();
					var deviceObj = [];
					$("input[name^='" + prdfix + "'][type='hidden']").each(function(i, ele){
						var str = $(this).attr("id");
						if(undefined != str && null != str){
							deviceObj = $(this).attr("id").split("_");
							showUploadDevice4Again(deviceObj[0], deviceObj[1], deviceObj[2], deviceObj[4], deviceObj[3]);
						}
					});
					$.easyui.loaded(true);
				}else{
					$(obj).attr("checked",false);
				}
			});

			/*
			//检查设备绑定情况  checkSn4DelDevice
			$('#hiddenForm').form('submit',{
				url: sys_rootpath + '/order/checkSn4DelDevice?ram=' + new Date().getMilliseconds() ,
				onSubmit:function(param){
					$.easyui.loading({ msg: "正在查询...", topMost: true });
					$("#hiddenForm_orderno").val($("#selOrderNo").val());
					var arr = prdfix.split("_");
					$("#hiddenForm_deviceClass").val(arr[0]);
					return true; // 返回false终止表单提交
				},
				success: function(data){
					$.easyui.loaded(true);
					if(data > 0){
						$(obj).attr("checked",false);
						$.messager.alert("系统提示", "该型号已有设备绑定,需先解绑设备", "warning");
						return ;
					}else{
						$.messager.confirm("软件重新上传确认", "是否重新上传？", function(cr){
							if(cr){
								$.easyui.loading({ msg: "正在初始化...", topMost: true });
								var orderNo = $("#selOrderNo").val();
								var deviceObj = [];
								$("input[name^='" + prdfix + "'][type='hidden']").each(function(i, ele){
									var str = $(this).attr("id");
									if(undefined != str && null != str){
										deviceObj = $(this).attr("id").split("_");
										showUploadDevice4Again(deviceObj[0], deviceObj[1], deviceObj[2], deviceObj[4], deviceObj[3]);
									}
								});
								$.easyui.loaded(true);
							}else{
								$(obj).attr("checked",false);
							}
						});
					}
				}
			});
			*/
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
			//清除上传项数据
			$("#show_device_tab_BCU").html("");
			$("#show_device_tab_BYU").html("");
			$("#show_device_tab_BMU").html("");
			$("#show_device_tab_LDM").html("");
			$("#show_device_tab_DTU").html("");
			$("#show_device_tab_LCD").html("");
			$("#show_device_tab_CS").html("");
			$("#show_device_tab_OTHER").html("");
			/**
			 * eupload需先销毁才能清除
			 */
			$(".euploadify-f").each(function(i, e){
				$("#" + $(this).attr("id")).euploadify("destroy");
			});
			$("#upload_device_tab_BCU").html("");
			$("#upload_device_tab_BYU").html("");
			$("#upload_device_tab_BMU").html("");
			$("#upload_device_tab_LDM").html("");
			$("#upload_device_tab_DTU").html("");
			$("#upload_device_tab_LCD").html("");
			$("#upload_device_tab_OTHER").html("");

			$("#upload_device_tab_BCU").css("display", "block");
			$("#upload_device_tab_BYU").css("display", "block");
			$("#upload_device_tab_LCD").css("display", "block");

			$('input').removeAttr('checked');
			$($('#uploadType_1').eq(0)).prop('checked',false);
			$($('#uploadType_1').eq(0)).prop('checked',true);
			//售后订单不显示齐套非齐套 sby 2016年10月10日 17:08:55
			if(id.indexOf("LG-SH-")==0 && id.length == 16){
				$("#div_label_uploadType").css("display", "none");
			}else{
				$("#div_label_uploadType").css("display", "block");
			}
			//$("#uploadType_1").attr("checked",true);
			//$("#uploadType_2").attr("checked",false);

			//$.easyui.loading({ msg: "正在加载...", locale: "#orderInfoShow" });
			$.post(sys_rootpath + "/order/getOrderInfo?rmd=" + new Date().getTime(), {orderno:id},function(data){
				if(data != "-1"){
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
					$("#show_projectNote").html(orderObj.projectNote);
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

					//隐藏域初始化
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

							//显示页面
							//文件上传
							showUploadDeviceTR(deviceObj[1], deviceObj[0], deviceObj[2], deviceObj[3]);
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
							//add_device_str += device_count + "</td></tr>";
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
										initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_s19" ,fileObj[1], fileObj[0], deviceObj[3]);
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
										initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_cfg" ,fileObj[1], fileObj[0], deviceObj[3]);
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
										initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_csf" ,fileObj[1], fileObj[0], deviceObj[3]);
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
											initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_s19" ,fileObj[1], "", deviceObj[3]);
										}else {
											if (fileObj[0].length > 45) {
												add_device_str += fileObj[0].substring(0, 45) + "...";
											} else {
												add_device_str += fileObj[0];
											}
											initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_s19" ,fileObj[1], fileObj[0], deviceObj[3]);
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
										initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_cfg" ,fileObj[1], fileObj[0], deviceObj[3]);
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
											initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_s19" ,fileObj[1], "", deviceObj[3]);
										}else {
											if (fileObj[0].length > 45) {
												add_device_str += fileObj[0].substring(0, 45) + "...";
											} else {
												add_device_str += fileObj[0];
											}
											initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_s19" ,fileObj[1], fileObj[0], deviceObj[3]);
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
											initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_zip" ,fileObj[1], "", deviceObj[3]);
										}else {
											if (fileObj[0].length > 45) {
												add_device_str += fileObj[0].substring(0, 45) + "...";
											} else {
												add_device_str += fileObj[0];
											}
											initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_zip" ,fileObj[1], fileObj[0], deviceObj[3]);
										}
										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
												"</div></td></tr>";
									}else{
										add_device_str += "&nbsp;</td></tr>";
									}
								}else{
									add_device_str += "&nbsp;</td></tr>";
								}
								// sby LCD也可使用模版
								/*if(undefined != order_deviceFileArr){
									fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_zip"];
									if(undefined != fileObj){
										add_device_str += "<div style='float: left;'><a href='" + sys_rootpath + "/fileOperate/downloadById?id=" + fileObj[1] + "&device_code=" + device_code + "' title='" + fileObj[0] + "'>";

										if (fileObj[0].length > 45) {
											add_device_str += fileObj[0].substring(0, 45) + "...";
										} else {
											add_device_str += fileObj[0];
										}
										initUploadOrderFile(device_class + "_" + device_code + "_" + device_bum_no + "_zip" ,fileObj[1], fileObj[0], deviceObj[3]);

										add_device_str += "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
												"</div></td></tr>";
									}else{
										add_device_str += "&nbsp;</td></tr>";
									}
								}else{
									add_device_str += "&nbsp;</td></tr>";
								}*/
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
					/*****************************
					 * 日志记录初始化
					 *****************************/
					var orderStatus = parseInt(orderObj.status);

					$("#order_tracklog_div").find("li").each(function(indexNum, ele){
						$(this).removeClass("manage-nochange");
						$(this).removeClass("manage-leftactive");
						if(orderStatus == 9 || orderStatus == 10){//暂停和取消不限制订单日志动作（sby 2016年4月18日 13:36:06）

						}else if(orderStatus == 8){//已完成状态
							if(indexNum < 7){
								$(this).addClass("manage-nochange");
							}
						}else{
							if(indexNum == orderStatus || indexNum > 6 ){

							}else{
								$(this).addClass("manage-nochange");
							}
						}
					});
					$(".hidden_orderno").each(function(i, e){
						$(this).val(id);
					});
					$(".show_psjq_td").each(function(i, e){
						$(this).text(result.order_track_delivery);
					});
					$(".show_reviewDevelopDuration").each(function(i, e){
						$(this).text(result.reviewDevelopDuration);
					});
					$(".show_reviewTestDuration").each(function(i, e){
						$(this).text(result.reviewTestDuration);
					});
				}
				//$.easyui.loaded(true); IE8兼容性问题
				//$.easyui.loaded("#orderInfoShow");
			});
			toBackPage();
			getTrackLog();
		}

		/**
		 * 软件上传表单
		 */
		function submitForm(){
			var msgStr = "保存操作会覆盖原先上传的软件<br>是否确认保存订单软件？";
			var rs = true;
			var idsArr = [];
			// var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_" + "s19";
			var noUploadMap = new Map();
			var uploadMap = new Map();

			/********************************************
			 * 1）BCU BYU BMU LDM DTU 单类上传
			 * 2）BCU (C11/C11B) 上传时可以没有csf文件
			 * 3）软件变更，覆盖上传
			 *******************************************/
			var key_up = "";
			var value_up = "";
			$(".euploadify-f").each(function(i, e){
				key_up = $(this).attr("id");
				value_up = $(this).euploadify("getValues");
				idsArr.push("#" + key_up);
				/**********************************************************
				 * OTHER 类型不做整体性验证，即可以单独上传
				 * sby 2016年6月15日 19:35:43
				 *********************************************************/
				if( key_up.indexOf("OTHER") == 0 ){

				}else{
					if("" == value_up || "," == value_up){
						/*************************************************************
						 * 添加绝缘检测配置文件不做限制 sby 2016年5月18日 20:21:33
						 * 若有绝缘检测，配置文件必须上传 会议讨论 2016年6月23日 11:39:45
						 *************************************************************/
						if("BCU_C11_0_csf" == key_up || "BCU_C11B_0_csf" == key_up ){
							//可不上传csf文件
						}
						/*else if( key_up.indexOf("LDM") == 0 && key_up.endsWith("_cfg")){
							//绝缘检测可不上传配置文件 LG201605050103_BL51B.cfg
						}*/
						else{
							rs = false;
							noUploadMap.put(key_up.substring(0, 3), "1");//存设备类型
						}
					}else{
						uploadMap.put(key_up.substring(0, 3), key_up.substring(0, 3));//存设备类型
					}
				}
			});
			$(idsArr.join(',')).euploadify("validate");

			/**
			 * 单独一个LCD时，判断不准确
			 * if(idsArr.length == 0){
    		 *    $.messager.alert("系统提示", "无上传项", "warning");
    		 *    return ;
    		 * }
			 */

			var selOrderNoStr = $("#selOrderNo").val();
			/**
			 * 售后订单不能单独上传
			 * 会议讨论 2016年6月23日 13:37:32
			 */
			/*if(selOrderNoStr.indexOf("LG-SH-")==0 && selOrderNoStr.length == 16){//售后订单可单独上传


			}else{*/
				/****************************************************
				 * 查询使用模版的情况 tq 2016年4月25日 17:38:57
				 * 发现使用模版时也验证是否有其他文件未上传
				 ****************************************************/
				$("input[value='usetemplate'][type='hidden']").each(function(i, ele){
					uploadMap.put($(this).attr("id").substring(0, 3), $(this).attr("id").substring(0, 3));//存设备类型
				});

				if(uploadMap.size() == 0){
					$.messager.alert("系统提示", "无软件上传", "warning");
					return;
				}

				/***************************************
				 * 循环上传项，找出未上传的设备类型分类
				 ***************************************/
				var checkDeviceClass = true;
				uploadMap.each(function(key,value,index){
					if("1" == noUploadMap.get(key)){
						checkDeviceClass = false;
					}
				});

				if(!checkDeviceClass){
					$.messager.alert("系统提示", "请确保单一设备类型完整上传", "warning");
					return;
				}
				if(selOrderNoStr.indexOf("LG-SH-")==0 && selOrderNoStr.length == 16){

				}else{
					if(!rs){
						msgStr = "上传项有未上传的文件<br>是否确认继续上传保存操作？";

						//齐套软件上传不允许有未上传的情况出现 sby 2016年9月10日 10:24:49
						var uploadTypeStr = $("#orderSWUploadForm").form("getData").uploadType;
						if(uploadTypeStr == 1){
							$.messager.alert("系统提示", "齐套上传请确保所有软件上传完成", "warning");
							return;
						}
					}
				}

			//}
			$.messager.confirm("软件上传保存确认", msgStr, function(cr){
				if(cr){
					$('#orderSWUploadForm').form('submit',{
						url: sys_rootpath + '/order/orderFileUpload?ram=' + new Date().getMilliseconds(),
						onSubmit:function(param){
							$.easyui.loading({ msg: "正在保存...", topMost: true });
							$("#upload_orderno").val($("#selOrderNo").val());
							return true; // 返回false终止表单提交
						},
						success: function(data){
							$.easyui.loaded(true);
							orderNoChange($("#selOrderNo").val());
							if(data == "ok"){
								$.messager.alert("系统提示", "操作成功", "info");
							}else{
								$.messager.alert("系统提示", "操作失败，请联系管理员", "info");
							}
						}
					});
				}
			});
		}

		function changBcuLcd(){
			var uploadTypeStr = $("#orderSWUploadForm").form("getData").uploadType;
			if(uploadTypeStr == 2){
				$("#upload_device_tab_BCU").css("display", "none");
				$("#upload_device_tab_BYU").css("display", "none");
				$("#upload_device_tab_LCD").css("display", "none");
			}
			else{
				$("#upload_device_tab_BCU").css("display", "block");
				$("#upload_device_tab_BYU").css("display", "block");
				$("#upload_device_tab_LCD").css("display", "block");
			}
		}

	</script>
	</body>
	</html>