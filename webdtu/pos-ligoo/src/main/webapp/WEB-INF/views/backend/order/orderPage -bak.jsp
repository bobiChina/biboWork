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

	$("#edit_vehicleType_id").change(function (){
		$("#edit_vehicleModel").empty();
		$("#edit_vehicleModel").append("<option value='-1'>请选择</option>");
		if ($("#edit_vehicleType_id").val() == -1)
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

	//$("#orderAddForm").validate({debug:true});
	//显示订单添加的时间  2015-11-11 09:38:44
	var myDate = new Date();
    myDate.setTime(<c:out value="${init_timeMill}" />);
	window.setInterval(function(){
		myDate.setSeconds(myDate.getSeconds() + 1);
		var dateStr = myDate.getFullYear() + "-" + myDate.getMonth() + "-" + myDate.getDate() + " " + myDate.getHours() + ":" + myDate.getMinutes() + ":" + myDate.getSeconds();
		$("#showInitTime").html(dateStr);
	}, 1000);

	var dataArr = [];
	<c:forEach items="${userList}" var="userItem" varStatus="status">
		dataArr.push({"username":"${userItem[0]}", "corpname": "${userItem[1]}"});
	</c:forEach>

	$('#searchUserKey_add').combobox({
		valueField: 'username',
		textField: 'corpname',
		data: dataArr,
    	onSelect: function(record){
    		findUserInfo("add", record.username );
    	},
		onLoadSuccess: function(){
			//默认显示value的内容
			var p = $("input[name='corp.id']").closest("span.combo,div.combo-p,div.menu");//向上查找
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

    $('#searchUserKey_edit').combobox({
		valueField: 'username',
		textField: 'corpname',
		data: dataArr,
    	onSelect: function(record){
    		findUserInfo("edit", record.username );
    		$('#edit_corp_id').val(record.username);
    	},
		onLoadSuccess: function(){
			//默认显示value的内容
			var p = $("input[name='searchUserKey_edit']").closest("span.combo,div.combo-p,div.menu");//向上查找
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



/**
	var prodArr = [];
	prodArr.push({"prodid":"0", "prodname": "产品信息"});
	<c:forEach items="${prodList}" var="prodItem" varStatus="status">
		prodArr.push({"prodid":"${prodItem[0]}", "prodname": "${prodItem[1]}"});
    </c:forEach>
    $('#prodHeadTab').combobox({
		valueField: 'prodid',
		textField: 'prodname',
		data: prodArr,
    	onSelect: function(record){
			getSNInfo(record.prodid);
    	}
    });

	$("#prodHeadTab_th").find(".combo").addClass("combobgprodHead");
	$("#prodHeadTab_th").find(".combo-text").addClass("combobgprodHead");
**/

});
function formatStatus(val,row){
	if(val == "1"){
		return '<span style="color:blue;">正常</span>';
	}else if(val == "2"){
		return '<span style="color:red;">已删除</span>';
	}else{
		return '<span style="color:red;">未知</span>';
	}
}

function getSNInfo(){
	// EASYUI列表部分
	$('#dg').datagrid({
		rownumbers:true,
		singleSelect: false,
		url: '${ctx}/order/getTableData?orderno='+ $("#selOrderNo").val() + '&rmd=' + new Date().getTime(),
		autoRowHeight:false,
		pagination:true,
		pageSize:20,
		width:810
	 });
}

function addCartype(opttype){
	if($("#" + opttype + "_vehicleModel").val() == "addVType"){
		$("#" + opttype + "_vehicleModel").val("-1");
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
		var returnStr = $.trim(this.returnValue);
		if(returnStr == ""){
			return;
		}
		var rs = false;
        $("#" + opttype + "_vehicleModel option").each(function(index, el){
			if(returnStr == $(el).text()){
				rs = true;
				$(el).attr("selected", true);//默认选中
			}
        });

        if(rs){
			//showDialogAlert("车辆型号已存在");
        }
        else{
			$("#" + opttype + "_vehicleModel").append("<option value='-9999'>"+ returnStr +"</option>");
			$("#" + opttype + "_vehicleModel").val("-9999");
        }
    });
    d.width(250);
    d.show();
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
    //清除原信息
    cleanUserInfo();
    cleanOtherInfo();

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

            //修改页信息
            $("#edit_ldr").html(optUserName.username);
			$("#edit_opttime").html(optUserName.opttime_edit);

			$("#edit_orderno").html(orderObj.orderno);
			$("#edit_salesman").val(orderObj.salesman);
			$('#searchUserKey_edit').combobox('setValue', orderObj.corp.id);
			$('#edit_corp_id').val(orderObj.corp.id);

			//$("#edit_typeName").html(orderObj.vehicleType.typeName);
			$("#edit_vehicleType").val(orderObj.vehicleType.id);
			$("#edit_modelName").html(orderObj.vehicleModel.modelName);

			$("#edit_factoryName").val(orderObj.batteryModel.factoryName);
			//$("#edit_batteryType").html(orderObj.batteryModel.batteryType.typeName);
			$("#edit_batteryNumber").val(orderObj.batteryModel.batteryNumber);
			$("#edit_capacity").val(orderObj.batteryModel.capacity);
			//隐藏域初始化
			$("#edit_id").val(orderObj.id);
			$("#edit_orderno_").val(orderObj.orderno);
			$("#edit_optUser").val(orderObj.optUser);
			$("#edit_optTime_").val(optUserName.opttime_edit);
			$("#edit_batteryModel_id").val(orderObj.batteryModel.id);
			$("#edit_vehicleModel_batteryName").val(orderObj.batteryModel.batteryName);
			$("#edit_batteryModel_batteryType").val(orderObj.batteryModel.batteryType.id);
			$("#edit_dtuUser_id").val(orderObj.corp.id);
			$("#edit_status").val(orderObj.status);
			$("#edit_optUserName").val(orderObj.optUserName);
			$("#edit_vehicleModel").val(orderObj.vehicleModel.id);
			$("#edit_vehicleModel_old").val(orderObj.vehicleModel.id);
			$("#edit_vehicleModel_vehicleType_id").val(orderObj.vehicleModel.vehicleType.id);
			$("#edit_vehicleType_id").val(orderObj.vehicleType.id);
			$("#edit_vehicleType_id").change();
			//搜索条件置空
			//$('#searchUserKey_add').combobox('setValue', '');
            //$('#searchUserKey_edit').combobox('setValue', '');
		}
	});
	//已绑定设备信息
	getSNInfo();
	toBackPage();
	cleanOtherInfo();
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

function findOrderLi(){
	var selId = $("#selOrderNo").val();
	$("#orderdata").html("");
	$.post("${ctx}/order/getOrderList?rmd=" + new Date().getTime(), {type:1},function(data){
		var li = "";
		var result = eval(data);
		$.each(result, function(index, items){
			li += "<li orderLIData='" + items[1] + "' style='position:relative;'";
			if( items[2] < 2){
			 	li += " onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' ";
			}
			if(selId == ""){
				if(index == 0){
					li += " class='manage-leftactive' ";
					orderNoChange(items[1]);
				}
			}else{
				if(selId == items[1]){
					li += " class='manage-leftactive' ";
                    orderNoChange(items[1]);
				}
			}
			li += " >";
			if( items[2] == 2){
				li += "<a href='javascript:;' onclick='orderNoChange(\"" + items[1] + "\")' title='已删除'><font color='red'>" + items[1] + "</font>" ;
			}else{
				li += "<a href='javascript:;' onclick='orderNoChange(\"" + items[1] + "\")'>" + items[1] ;
			}
			li += "</a>";
			li += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			li += "<img onclick='delOrderFun(" + items[0] + ",\"" + items[1] + "\", " + items[2] + ")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			li += "</div>";
			li += "</li>";
		});
		$("#orderdata").html(li);
	});
}

function toEditPage(){
	$("#orderInfoShow").css("display", "none");
	$("#userAddForm").css("display", "none");
	$("#orderAddForm").css("display", "none");
	$("#orderEditForm").css("display", "block");
	$("#userAddType").val("");
}

function toAddPage(){
	$("#orderInfoShow").css("display", "none");
	$("#orderEditForm").css("display", "none");
	$("#userAddForm").css("display", "none");
	$("#orderAddForm").css("display", "block");
	$("#orderAddForm")[0].reset();
	$("#userIdAdd").val("");
	$("#userAddType").val("");


}

function toBackPage(){
	$("#orderInfoShow").css("display", "block");
	$("#orderEditForm").css("display", "none");
	$("#orderAddForm").css("display", "none");
	$("#userAddForm").css("display", "none");
	$("#userAddType").val("");
}

//清除编辑页面用户信息数据
function cleanUserInfo(){
	$("#edit_dtuUser_id").val("");
	$("#edit_corpName").html("");
	$("#edit_addr").html("");
	$("#edit_userName").html("");
	$("#edit_corpphone").html("");
	$("#edit_corpemail").html("");
	$("#edit_fullName").html("");
	$("#edit_relation").html("");
	$("#edit_email").html("");

	$("#add_dtuUser_id").val("");
	$("#add_corpName").html("");
	$("#add_addr").html("");
	$("#add_userName").html("");
	$("#add_corpphone").html("");
	$("#add_corpemail").html("");
	$("#add_fullName").html("");
	$("#add_relation").html("");
	$("#add_email").html("");

}

function cleanOtherInfo(){
	$("#add_dtuUser_id").val("");
	$("#add_corpName").html("");
	$("#add_addr").html("");
	$("#add_userName").html("");
	$("#add_corpphone").html("");
	$("#add_corpemail").html("");
	$("#add_fullName").html("");
	$("#add_relation").html("");
	$("#add_email").html("");

	$("#add_orderno").val("");
	$("#add_salesman").val("");
	$('#searchUserKey_add').combobox('setValue', '');
	$('#searchUserKey_edit').combobox('setValue', '');
	$("#add_batteryModel_batteryName").val("");
	$("#add_vehicleType_id").val("-1");
	$("#add_vehicleModel").empty();
	$("#add_vehicleModel").append("<option value='-1'>请选择</option>");
	$("#add_batteryModel_batteryType").val("-1");
	$("#add_factoryName").val("");
	$("#add_batteryNumber").val("");
	$("#add_capacity").val("");
	$("#add_checkOrderNo").val("0");
}
var dialogObj ;
function findUserInfo(opttype, keywords){
	/**
	//清除原修改页信息
	cleanUserInfo();
	$.post("${ctx}/order/getUserInfo?rmd=" + new Date().getTime(), {userName : keywords},function(data){
		if(data != "-1"){
			var dtuUser = eval('(' + data + ')');
			$("#" + opttype + "_dtuUser_id").val(dtuUser.id);
			$("#" + opttype + "_corpName").html(dtuUser.corp.corpName);
			$("#" + opttype + "_addr").html(dtuUser.corp.addr);
			$("#" + opttype + "_userName").html(dtuUser.userName);
			$("#" + opttype + "_corpphone").html(dtuUser.corp.phone);
			$("#" + opttype + "_corpemail").html(dtuUser.corp.email);
			$("#" + opttype + "_fullName").html(dtuUser.fullName);
			$("#" + opttype + "_relation").html(dtuUser.relation);
			$("#" + opttype + "_email").html(dtuUser.email);
		}else{
			//showDialogAlert("用户不存在");
			dialogObj = dialog({
				title: '提示',
				width: 280,
				zIndex: 999999,
				align: 'center',
				content: '<div style="width:99%; text-align: center; font-size: 18px;">' + keywords + ' 账号对应用户不存在</div><div style="width:99%; text-align: center; font-size: 12px; margin-top:15px;"><a href="javascript:void(0)" onclick="closeTip(\'' + opttype + '\')" style="font-size: 12px;color:#3388FF; ">添加新用户</a></div>',
				okValue: '确定',
				ok: function () {
					this.close();
					this.remove();
				}
			});
			dialogObj.show();
		}
	});
	**/
}

function closeTip(type){
	dialogObj.close();
	dialogObj.remove();

	if(type == "edit"){
		$("#orderEditForm").css("display", "none");
	}else{
		$("#orderAddForm").css("display", "none");
	}
	$("#userAddForm")[0].reset();
    $("#userIdAdd").val("");
    $("#userAddForm").css("display", "block");
    $("#userAddType").val(type);

}


//组装电池名称
function autoBatteryName(opttype){
	var batteryTypeStr = $("#" + opttype + "_batteryModel_batteryType").find("option:selected").text() + "-";
	if (($("#" + opttype + "_batteryModel_batteryType").val()== -1)||($("#" + opttype + "_batteryModel_batteryType").val()== null)){
		batteryTypeStr = "";
    }
    var str = $("#" + opttype + "_factoryName").val() + "-" + batteryTypeStr
    	 + $("#" + opttype + "_batteryNumber").val() + "S-" + $("#" + opttype + "_capacity").val() + "AH";
	$("#" + opttype + "_batteryModel_batteryName").val(str);

}

function updateOrder(optType){

	if(optType == 'add'){
		if ($("#" + optType + "_orderno").val() == ""){
			$.messager.alert("系统提示", "订单编号不能为空", "warning");
			return;
        }
        if ($("#" + optType + "_checkOrderNo").val() == "0"){
			$.messager.alert("系统提示", "订单编号重复", "warning");
			return;
		}
	}else{
		if ($("#" + optType + "_id").val() == ""){
			$.messager.alert("系统提示", "请选择订单", "warning");
			return;
		}
	}
	var khmcStr = $('#searchUserKey_' + optType).combobox('getValue');
	if (khmcStr == undefined|| khmcStr == ""){
		$.messager.alert("系统提示", "客户不能为空", "warning");
		return;
	}
	if(optType == 'edit'){//修改客户
		$("#edit_corp_id").val(khmcStr);
	}
	if ($("#" + optType + "_quantity").val() == ""){
		$.messager.alert("系统提示", "订货数量不能为空", "warning");
		return;
	}
	if ($("#" + optType + "_projectNote").val() == ""){
		$.messager.alert("系统提示", "项目描述不能为空", "warning");
		return;
	}
	/** 2016年1月12日13:49:14 屏蔽，许多订单无DTU信息
	if ($("#" + optType + "_vehicleType_id").val() == "-1"){
		$.messager.alert("系统提示", "车辆类型不能为空", "warning");
		return;
	}

	if ($("#" + optType + "_vehicleModel").val() == "-1"){
		$.messager.alert("系统提示", "车辆型号不能为空", "warning");
		return;
	}
	if ($("#" + optType + "_batteryModel_batteryType").val() == "-1"){
		$.messager.alert("系统提示", "电池类型不能为空", "warning");
		return;
	}
	if ($("#" + optType + "_factoryName").val() == ""){
		$.messager.alert("系统提示", "电池厂商不能为空", "warning");
		return;
	}

	if ($("#" + optType + "_batteryNumber").val() == ""){
		$.messager.alert("系统提示", "电池串数不能为空", "warning");
		return;
	}

	if ($("#" + optType + "_capacity").val() == ""){
		$.messager.alert("系统提示", "电池容量不能为空", "warning");
		return;
	}
**/
	var formStr = "orderEditForm";
	if(optType == 'add'){
		formStr = "orderAddForm";
	}
	//车辆型号文本信息
	$("#" + optType + "_vehicleModel_modelName").val($("#" + optType + "_vehicleModel").find("option:selected").text());

	$.post("${ctx}/order/save?rmd=" + new Date().getTime(), $("#" + formStr).serialize(),function(data){
		$.messager.alert("系统提示", "操作成功", "warning");
		if(optType == 'add'){
			$("#selOrderNo").val(data);
			//转到显示页面
			findOrderLi();
			//重新加载下拉框内容
			 $('#searchorderKey_li').combobox({
				url: '${ctx}/order/getOrderListBoxvalue?type=1&rmd=' + new Date().getTime(),
				valueField: 'orderno',
				textField: 'ordername',
				onSelect: function(record){
					orderNoChange(record.orderno);
				}
			 });
		}else{
			orderNoChange(data)
		}
    });
}

function checkOrderNo(){
	window.setTimeout(function(){
		$.post("${ctx}/order/checkOrderNo?rmd=" + new Date().getTime(), { orderno:$("#add_orderno").val()},function(data){
			if(data == "isuse"){
				$("#add_checkOrderNo").val("0");
			}else{
				$("#add_checkOrderNo").val("1");
			}
		});
	}, 500);
}


function delOrderFun(id, orderno, status){
	$.messager.confirm("删除确认", "是否确认删除订单？<br>编号：" + orderno , function(cr){
		if(cr){
			$.post("${ctx}/order/delorder?rmd=" + new Date().getTime(), {orderid : id, orderno : orderno},function(data){
				var msg = "操作失败";
				if(data == "ok"){
					msg = "操作成功";
					//操作成功才会刷新左侧
					$("#selOrderNo").val("");
                    findOrderLi();
				}else if(data == "-1"){
					msg = "订单已有设备绑定，不能删除";
				}
				$.messager.alert("系统提示", msg, "info");

			 });
		}
	});
/**
	var d = dialog({
		title: '系统提示',
		content: '<div style="text-align:center;">是否确认删除订单？<br>编号：' + orderno + "</div>",
		zIndex: 999999,
		button: [
				{
					value: '确定',
					callback: function () {
						$.post("${ctx}/order/delorder?rmd=" + new Date().getTime(), {orderid : id, orderno : orderno},function(data){
							$("#selOrderNo").val("");
							findOrderLi();
						 });
					},
					autofocus: true
				},
				{
					value: '取消',
					callback: function () {
					}
				}
			]
	});
	d.width(250);
	d.showModal();
**/
}


function addUser(){
	if ($("#userNameAdd").val() == ""){
		showDialogAlert("请填写用户账号");
		return;
	}
	if ($("#userPassAdd").val() == ""){
		showDialogAlert("请填写用户密码");
		return;
	}
	if ($("#fullNameAdd").val() == ""){
			showDialogAlert("请填写用户名");
			return;
		}

	if ($("#corpcorpNameAdd").val() == ""){
		showDialogAlert("请填写公司名称");
		return;
	}

	$.post("${ctx}/dtu/addUser?rmd=" + new Date().getTime(), $("#userAddForm").serialize(),function(data){
		if(data == -1){
			showDialogAlert("用户账号已存在,重新填写");
			return;
		}else{
			var opttype = $("#userAddType").val();
			$("#searchUserKey_" + opttype).val($("#userNameAdd").val());

			$("#userAddForm").css("display", "none");
			if(opttype == "edit"){
				$("#orderEditForm").css("display", "block");
			}else{
				$("#orderAddForm").css("display", "block");
			}
			findUserInfo(opttype);
			$("#userAddType").val("");
		}
    });
}

function searchInfoByNname(){
	var userName = $("#userNameAdd").val();
	if(userName == ""){
		showDialogAlert("用户账号不能为空");
		$("#userAddForm")[0].reset();//清空
		$("#userIdAdd").val("");
		return ;
	}
	$("#userAddForm")[0].reset();//清空
	$("#userIdAdd").val("");

	$.post("${ctx}/dtu/searchInfoByNname?rmd=" + new Date().getTime(), {userName : userName},function(data){
		if(data == ""){
			$("#userNameAdd").val(userName);
			showDialogAlert("该账号不存在");
			$("#userIdAdd").val("");
			return;
		}else{
			var userObj = eval("(" + data + ")");
			$("#userIdAdd").val(userObj.id);
			$("#userNameAdd").val(userObj.userName);
			$("#userPassAdd").val("******");

			$("#fullNameAdd").val(userObj.fullName);
			$("#relationAdd").val(userObj.relation);
			$("#emailAdd").val(userObj.email);

			$("#corpcorpNameAdd").val(userObj.corp.corpName);

			$("#corpphoneAdd").val(userObj.corp.phone);

			$("#corpemailAdd").val(userObj.corp.email);

			$("#corpaddrAdd").val(userObj.corp.addr);

		}
	});
}


//-------------------------------------------------------------------------
<shiro:hasRole name="admin">
function toScanPage(){
	$.post("${ctx}/order/getOrderScanList?rmd=" + new Date().getTime(), {orderno:$("#selOrderNo").val()},function(data){
		var tr = "";
		var result = eval(data);
		<shiro:hasRole name="admin">
			//清除编辑框内容
			$("#dtu_body_scan_add").html("");
		</shiro:hasRole>

		$.each(result, function(index, items){
			tr += "<tr style='position:relative;' onmouseover='showEditIcon(this, 1)' onmouseout='showEditIcon(this, 0)'>";
			tr += "<td style='width:32px;border:1px solid #cccccc;'><input type='checkbox' name='selScanObj' value='" + items.id + "' /></td>";
			tr += "<td style='width:90px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.prodType.prodTypename + "</td>";
			tr += "<td style='width:125px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.sn + "</td>";
			tr += "<td style='width:295px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.uuid + "</td>";
			tr += "<td style='width:90px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.hwVersion + "</td>";
			tr += "<td style='width:122px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.swVersion ;
			tr += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			tr += "<img onclick='delScanFun(\"" + items.id + "\",\"" + items.uuid + "\",\"" + items.sn + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			tr += "</div>";
			tr += "</td>";
			tr += "</tr>";
		});
		$("#dtu_body_scan").html(tr);
		toScanAllPage();
	});
}

function isRepeat(arr){
	var hash = {};
	for(var i in arr) {
		if(arr[i] != null && arr[i] != ""){
			if(hash[arr[i]])
				return true;
			hash[arr[i]] = true;
		}
	}
	return false;
}

function addRowScan(){
	var rowLen = $("#dtu_body_scan_add").find("select[name='prod_add']").length;
	if(rowLen > 9){
		showDialogAlert("每次最多添加10行");
		return;
	}
	var oldSelValue = $('#dtu_body_scan_add tr:last').find("select").eq(0).val();
	$("#dtu_body_scan_add").append($('#dtu_body_scan_add tr:last').clone());

	$('#dtu_body_scan_add tr:last').find("select").eq(0).val(oldSelValue);
}

function deltr(obj){
	var rowLen = $("#dtu_body_scan_add").find("select[name='prod_add']").length;
	if(rowLen == 1){
		showDialogAlert("至少保留一行");
		return;
	}
	var tr = $(obj).parent().parent().parent();
	tr.remove();
}
</shiro:hasRole>

function delScanFun(id, uuid, sn){
	var d = dialog({
		title: '系统提示',
		content: '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>SN码:' + sn + '<br>UUID:' + uuid + '</div>',
		zIndex: 999999,
		button: [
				{
					value: '确定',
					callback: function () {
						$.post("${ctx}/order/delScanFun?rmd=" + new Date().getTime(), {id:id},function(data){
							//显示右侧信息
							$.post("${ctx}/order/getOrderScanList?rmd=" + new Date().getTime(), {orderno:$("#selOrderNo").val()},function(data){
								var tr = "";
								var result = eval(data);
								if(result.length == 0){
									$("#dtu_body_scan").html("<tr><td colspan='6'>无待确认设备</td></tr>");
									return;
								}
								$.each(result, function(index, items){
									tr += "<tr style='position:relative;' onmouseover='showEditIcon(this, 1)' onmouseout='showEditIcon(this, 0)'>";
									tr += "<td style='width:32px;border:1px solid #cccccc;'><input type='checkbox' name='selScanObj' value='" + items.id + "' /></td>";
									tr += "<td style='width:125px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.sn + "</td>";
									tr += "<td style='width:295px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.uuid + "</td>";
									tr += "<td style='width:90px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.prodType.prodTypename + "</td>";
									tr += "<td style='width:90px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.hwVersion + "</td>";
									tr += "<td style='width:122px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.swVersion ;
									tr += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
									tr += "<img onclick='delScanFun(\"" + items.id + "\",\"" + items.uuid + "\",\"" + items.sn + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
									tr += "</div>";
									tr += "</td>";
									tr += "</tr>";
								});
								$("#dtu_body_scan").html(tr);
                            });
						});
					}
				},
				{
					value: '取消',
					callback: function () {
					},
					autofocus: true
				}
			]
	});
	d.width(250);
	d.showModal();
}

function delSNFun_bak(id, uuid, sn){
	var d = dialog({
		title: '系统提示',
		content: '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>SN码:' + sn + '<br>UUID:' + uuid + '</div>',
		zIndex: 999999,
		button: [
				{
					value: '确定',
					callback: function () {
						$.post("${ctx}/order/delSNFun?rmd=" + new Date().getTime(), {id:id},function(data){
							//显示右侧信息
							getSNInfo();
						});
					}
				},
				{
					value: '取消',
					callback: function () {
					},
					autofocus: true
				}
			]
	});
	d.width(250);
	d.showModal();
}

function selAllScan(obj){
	if($(obj).is(":checked")){
		$("#dtu_body_scan").find("input").each(function(i, em){
			if($(this).attr("id") == "selAll"){

			}else{
				$(this).prop('checked' , true);
			}
		});
	}else{
		$("#dtu_body_scan").find("input").each(function(i, em){
			if($(this).attr("id") == "selAll"){

			}else{
				$(this).prop('checked' , false);
			}
		});
	}


}
//绑定SN
function saveSNScan(){
	var selStr = "";
	$("#dtu_body_scan").find("input").each(function(i, em){
		if($(this).attr("id") == "selAll"){

        }else{
        	if($(this).prop('checked')){
				selStr += $(this).val() + ",";
			}
        }
	});

	if(selStr == ""){
		showDialogAlert("请选择扫码信息");
        return;
	}else{
		$.post("${ctx}/order/saveSNScan?rmd=" + new Date().getTime(), {ids:selStr, orderno:$("#selOrderNo").val()},function(data){
			//显示右侧信息
			getSNInfo();
		});
	}
}
<shiro:hasRole name="admin">
function toScanAllPage(){
	//删除其他的
	$("#dtu_body_scan_add").find("select[name='prod_add']").each(function(index, elm){
		if(index > 0 ){
			$(this).parent().parent().remove();
		}else{
			$(this).parent().parent().find("input").each(function(i, ele){
				$(ele).val("");
			});
		}
	});

	toBackPage();
	var elem = document.getElementById('scanInfoShow');
	var d = dialog({
	title: '添加设备',
	width:765,
	height:450,
	content: elem ,
	zIndex: 999999,
	padding: 3,
	button: [
			{
				value: '确定',
				callback: function () {
					var rowLen = $("#dtu_body_scan_add").find("tr").length;
					if(rowLen > 0){//有自己添加的信息
						var snArr = [];
						$("#dtu_body_scan_add").find("input[name='sn_add']").each(function(i, ele){
							snArr.push($.trim($(this).val()));
						});
						if(isRepeat(snArr)){
							showDialogAlert("SN码不能重复");
							return false;
						}
						var uuidArr = [];
						var uuid_boolean = false;
						$("#dtu_body_scan_add").find("input[name='uuid_add']").each(function(i, ele){
							if($.trim($(this).val()) == ""){
								uuid_boolean = true;
							}
							uuidArr.push($.trim($(this).val()));
						});
						if(uuid_boolean){
							showDialogAlert("UUID不能为空");
							return false;
						}

						if(isRepeat(uuidArr)){
							showDialogAlert("UUID不能重复");
							return false;
						}

						/**
						var sn_boolean = false;
						var sn_str = "";
						$("#dtu_body_scan_add").find("input[name='sn_add']").each(function(i, ele){
							if($.trim($(this).val()) == ""){
								sn_boolean = true;
							}
							sn_str += ","
						});
						if(sn_boolean){
							showDialogAlert("SN码不能为空");
							return false;
						}
						var uuid_boolean = false;
						$("#dtu_body_scan_add").find("input[name='uuid_add']").each(function(i, ele){
							if($.trim($(this).val()) == ""){
								uuid_boolean = true;
							}
						});
						if(uuid_boolean){
							showDialogAlert("UUID不能为空");
							return false;
						}

						var hwversion_boolean = false;
						$("#dtu_body_scan_add").find("input[name='hwversion_add']").each(function(i, ele){
							if($.trim($(this).val()) == ""){
								hwversion_boolean = true;
							}
						});
						if(hwversion_boolean){
							showDialogAlert("硬件版本号不能为空");
							return false;
						}
						var swversion_boolean = false;
						$("#dtu_body_scan_add").find("input[name='swversion_add']").each(function(i, ele){
							if($.trim($(this).val()) == ""){
								swversion_boolean = true;
							}
						});
						if(swversion_boolean){
							showDialogAlert("软件版本号不能为空");
							return false;
						}
						**/
					}
				},
				autofocus: true
			},
			{
				value: '取消',
				callback: function () {
				}
			}
		]
	});
	d.width(815);
	d.showModal();
}
</shiro:hasRole>

//-------------------------------------------------------------------------
//删除
function delSNFun(){
	var rows = $('#dg').datagrid('getSelections');
	var ids = "";
	for(var i=0; i<rows.length; i++){
		ids += rows[i].id + ",";
	}
	if("" == ids){
		$.messager.alert("系统提示", "请选择删除项", "warning");
	}else{
		$.messager.confirm("删除确认", "是否确认删除？", function(cr){
			if(cr){
				$.post("${ctx}/order/delSNFun?rmd=" + new Date().getTime(), {ids: ids, orderno : $("#selOrderNo").val()},function(data){
					var msg = "操作成功";
					if(data != "ok"){
						msg = "操作失败";
					}
					$.messager.alert("系统提示", msg, "info");
					getSNInfo();
				 });
			}
		});
	}
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


						<div class="manage-page-right-backend dright easyui-accordion" id="orderInfoShow" style="display:none; background:#FFF;margin-top:57px; height:650px;width:810px;" >
							<div id="show_orderInfoDiv" class="manage-page-item" style="width:810px;" title="订单概况" data-options="iconCls:'icon-ok',selected:true,tools:[{ iconCls:'icon-edit',
                                                                                                                                                 handler:function(){
                                                                                                                                                     toEditPage();
                                                                                                                                                 }
                                                                                                                                             }]">
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
	<c:if test="${not empty orderShow}">
    <script type="text/javascript">
    $(function(){
    	orderNoChange("${orderShow}");
    });
    </script>
    </c:if>
    <c:if test="${not empty toadd}">
    <script type="text/javascript">
    $(function(){
    	toAddPage();
		$('#searchUserKey_add').combobox('setValue', '${init_user}');

    });
    </script>
    </c:if>
</body>
</html>