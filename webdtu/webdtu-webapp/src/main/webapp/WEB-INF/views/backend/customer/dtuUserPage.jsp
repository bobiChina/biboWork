<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_dtu"/>
	<title>DTU管理</title>
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
    	.manage-input-text {
			 background: #E0E0E0;
			 padding-left: 5px;
			 width: 206px;
			 height: 22px;
			 line-height: 22px;
			 border: none;
			 margin-top: 2px;
		}



		#show_InfoDiv {
			font-size: 14px;
		}
    </style>

<script type="text/javascript">
var corpArr = [];
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

	 $(".showTip").inputTipsText();

	<c:forEach items="${corpList}" var="item" varStatus="status">
		corpArr.push({"corpid":"${item[0]}", "corpname": "${item[1]}"});
	</c:forEach>
     	 $('#searchCorp_add').combobox({
     		valueField: 'corpid',
     		textField: 'corpname',
     		data: corpArr,
     		onSelect: function(record){
     			$("#corpIdAdd").val("");
     			selCorp(record.corpid);
     		}
     	 });


	var userArr = [];
	<c:forEach items="${userList}" var="item" varStatus="status">
		userArr.push({"userid":"${item[0]}", "corpname": "${item[1]}"});
	</c:forEach>
	 $('#searchorderKey_li').combobox({
		valueField: 'userid',
		textField: 'corpname',
		data: userArr,
		onSelect: function(record){
			userIdchange(record.userid);
		},
		onLoadSuccess: function(){
			//替换样式
			$("#searchorder_dev").find(".combo").addClass("combobg");
            $("#searchorder_dev").find(".combo-text").addClass("combobg");
            //默认显示
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

function initOrdernoSel(){
	//重新加载下拉框内容
	 /**
	  $('#searchOrder_sn').combobox({
		url: '${ctx}/customer/getOrderBoxvalue?userId=' + $("#selUserId").val() ,
		valueField: 'orderno',
		textField: 'ordername',
		onSelect: function(record){
			toAddDtuDialog(record.orderno);
		}
	  });
	  **/
	$('#searchOrder_sn').empty();
	$("#searchOrder_sn").append("<option value='-1'>请选择</option>");
	$.post("${ctx}/customer/getOrderBoxvalue", {userId: $("#selUserId").val()},function(data){
		var rsList = eval('(' + data + ')');
		if(rsList.length > 0){
			$.each(rsList, function(index, item){
				$("#searchOrder_sn").append("<option value='" + item.orderno + "'>" + item.ordername + "</option>");
			});
		}
	});
}


function selCorp(corpId){
	$.get("${ctx}/customer/getCorpInfo", {corpId:corpId},function(data){
		var dataObj = eval('(' + data + ')');

		var corpObj = dataObj.corp;
		$("#corpPhone_add").html(corpObj.phone == null || corpObj.phone=="" ? "-" : corpObj.phone);

		$("#corpEmail_add").html(corpObj.email == null || corpObj.email=="" ? "-" : corpObj.email);

		$("#corpAddr_add").html(corpObj.addr == null || corpObj.addr=="" ? "-" : corpObj.addr);

		$("#linkPhone_add").html(corpObj.linkPhone == null || corpObj.linkPhone=="" ? "-" : corpObj.linkPhone);

		$("#linkEmail_add").html(corpObj.linkEmail == null || corpObj.linkEmail=="" ? "-" : corpObj.linkEmail);

		$("#linkMan_add").html(corpObj.linkMan == null || corpObj.linkMan=="" ? "-" : corpObj.linkMan);

		$("#corpIdAdd").val(corpObj.id);

	});

}

function updateUser(){
	var userpassStr = $.trim($("#userPassEdit").val());
	if (userpassStr == "" || userpassStr == "输入字母或者数字"){
		showDialogAlert("请填用户密码");
		return;
	}
	$.post("${ctx}/customer/updatePass", {userpass : userpassStr , userid : $("#selUserId").val()},function(data){
		$.messager.alert("系统提示", "操作成功", "info");
		findUserLi();
    });
}

function addUser(){
	var userNameStr = $.trim($("#userNameAdd").val());
	if (userNameStr == "" || userNameStr == "输入字母或者数字"){
		showDialogAlert("请填写用户账号");
		return;
	}
	var userpassStr = $.trim($("#userPassAdd").val()) ;
	if ( userpassStr == "" || userpassStr == "输入字母或者数字" ){
		showDialogAlert("请填写用户密码");
		return;
	}
	if ($("#corpIdAdd").val() == ""){
		showDialogAlert("请选择客户");
		return;
    }
	$.post("${ctx}/customer/addUser", {userpass: userpassStr, username: userNameStr, corpid:$("#corpIdAdd").val()},function(data){
		if(data == -1){

		}else{
			$("#selUserId").val(data);
            findUserLi();
		}
    });
}



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

function userIdchange(id){
	$("#selUserId").val(id);
	$("#userdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-leftactive");
		if($(this).attr("userLIData") == id){
			$(this).addClass("manage-leftactive");
		}
    });
    $("#edit_button").css("display", "none");

	$.get("${ctx}/customer/getuser", {userid:id},function(data){
		var dataObj = eval('(' + data + ')');

		var dtuObj = dataObj.dtuUser ;

		if(dtuObj.isAdmin == 0){
			$("#edit_button").css("display", "block");
		}

		var corpNameStr = dtuObj.corp.corpName;
		if(corpNameStr.length > 16){
			$("#corpName_show").html(corpNameStr.substring(0, 15) + "...");
			$("#corpName_edit").html(corpNameStr.substring(0, 15) + "...");
		}else{
			$("#corpName_show").html(corpNameStr);
			$("#corpName_edit").html(corpNameStr);
		}

		$("#corpPhone_show").html(dtuObj.corp.phone == null || dtuObj.corp.phone=="" ? "-" : dtuObj.corp.phone);
		$("#corpPhone_edit").html(dtuObj.corp.phone == null || dtuObj.corp.phone=="" ? "-" : dtuObj.corp.phone);

		$("#corpEmail_show").html(dtuObj.corp.email == null || dtuObj.corp.email=="" ? "-" : dtuObj.corp.email);
		$("#corpEmail_edit").html(dtuObj.corp.email == null || dtuObj.corp.email=="" ? "-" : dtuObj.corp.email);

		$("#corpAddr_show").html(dtuObj.corp.addr == null || dtuObj.corp.addr=="" ? "-" : dtuObj.corp.addr);
		$("#corpAddr_edit").html(dtuObj.corp.addr == null || dtuObj.corp.addr=="" ? "-" : dtuObj.corp.addr);

		$("#linkPhone_show").html(dtuObj.corp.linkPhone == null || dtuObj.corp.linkPhone=="" ? "-" : dtuObj.corp.linkPhone);
		$("#linkPhone_edit").html(dtuObj.corp.linkPhone == null || dtuObj.corp.linkPhone=="" ? "-" : dtuObj.corp.linkPhone);

		$("#linkEmail_show").html(dtuObj.corp.linkEmail == null || dtuObj.corp.linkEmail=="" ? "-" : dtuObj.corp.linkEmail);
		$("#linkEmail_edit").html(dtuObj.corp.linkEmail == null || dtuObj.corp.linkEmail=="" ? "-" : dtuObj.corp.linkEmail);

		$("#linkMan_show").html(dtuObj.corp.linkMan == null || dtuObj.corp.linkMan=="" ? "-" : dtuObj.corp.linkMan);
		$("#linkMan_edit").html(dtuObj.corp.linkMan == null || dtuObj.corp.linkMan=="" ? "-" : dtuObj.corp.linkMan);

		$("#show_opttime").html(dateFormatStr(dtuObj.optTime));
		$("#show_cjr").html(dtuObj.optUserName);

		$("#id").val(dtuObj.id);

		$("#userName_show").html(dtuObj.userName);
		$("#userName_edit").html(dtuObj.userName);
		if(dtuObj.isAdmin == 0){
        	$("#userPwd_show").html(dtuObj.userPass);
        	$("#userPassEdit").val(dtuObj.userPass);
        }else{
        	$("#userPwd_show").html("******");
            $("#userPassEdit").val("******");
        }

		$("#subUserTab").html("");
		//没有子账号的样式修改

        var dtuObj = dataObj.subList ;
        if(dtuObj.length > 0){
        	$("#subUser_show").css("display", "block");
        	$("#corpDivShow").addClass("border-bottom-style");
        	var subListStr = '';

        	for(var i=0; i< dtuObj.length; i++){
        		var sObj =  dtuObj[i];
        		subListStr += '<tr>'
					+ '<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>'
					+ '<td style="text-align:left; width:230px; border:0px; height:37px;">'
					+ '	<a href="javascript:void(0);" onclick="userIdchange(' + sObj[0] + ')">' + sObj[1] + '<a>'
					+ '</td>'
					+ '<td style="text-align:right; width:110px; border:0px;">用&nbsp;户&nbsp;密&nbsp;码：</td>'
					+ '<td style="text-align:left;border:0px;">' + sObj[2] + '</td>'
				+ '</tr>';
        	}
        	$("#subUserTab").html(subListStr);
        }else{
        	$("#subUser_show").css("display", "none");
        	$("#corpDivShow").removeClass("border-bottom-style");
        }
	});
	toBackPage();
	findCurUserDtu();
	initOrdernoSel();
	//还原搜索框的值
	//$('#searchorderKey_li').combobox('setValue', '');
}

function delSNFun(id, sn, uuid){
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
				$.post("${ctx}/customer/delUserDtu?rmd=" + new Date().getTime(), {ids: ids, userId:$("#selUserId").val()},function(data){
					var msg = "操作失败";
					if(data > 0){
						msg = "操作成功";
					}
					$.messager.alert("系统提示", msg, "info");
					findCurUserDtu();
				 });
			}
		});
	}

/**

	var d = dialog({
		title: '系统提示',
		content: '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>SN码:' + sn + '<br>UUID:' + uuid + '</div>',
		zIndex: 999999,
		button: [
				{
					value: '确定',
					callback: function () {
						$.post("${ctx}/customer/delUserDtu", {userId:$("#selUserId").val() , dtuId:id },function(data){
							if(data == "4"){
								showDialogAlert("操作成功");
							}else{
								showDialogAlert("操作失败");
							}
							//显示新DTU信息
							findCurUserDtu();
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
	**/
}

function formatStatus(val,row){
	if(val == "1"){
		return '<span style="color:blue;">正常</span>';
	}else if(val == "2"){
		return '<span style="color:red;">已删除</span>';
	}else{
		return '<span style="color:red;">未知</span>';
	}
}
function findCurUserDtu(){
	var id = $("#selUserId").val();
	// EASYUI列表部分
	$('#dg').datagrid({
		rownumbers:true,
		singleSelect: false,
		url: '${ctx}/customer/getTableDataByUserId?userId='+ $("#selUserId").val() + '&rmd=' + new Date().getTime(),
		autoRowHeight:false,
		pagination:true,
		pageSize:20,
		width:810
	 });

/**
	$("#dtulist_body").html("")
	$.post("${ctx}/customer/getAllDtuByUserId", {userId:id},function(data){
		var tr = "";
		var oldStr = "";
    	var result = eval(data);
		$.each(result, function(index, items){
			tr += "<tr style='position:relative;' onmouseover='showDelIcon_dtu(this, 1)' onmouseout='showDelIcon_dtu(this, 0)'>";
			tr += "<td style='width:40px;'>" + (index + 1) + "</td>";
			tr += "<td style='width:110px;'>" + (items[1]==null ? "" : items[1]) + "</td>";
			tr += "<td style='width:110px;'>" + (items[2]==null ? "" : items[2]) + "</td>";
			tr += "<td style='width:160px;'>" + (items[3]==null ? "" : items[3]) + "</td>";
			tr += "<td style='width:180px;'>" + (items[4]==null ? "" : items[4]) + "</td>";
			if(items[6] == "ligoo_send"){
				tr += "<td style='width:90px;'>力高</td>";
			}else{
				tr += "<td style='width:90px;'><a href='javascript:void(0)' onclick='userIdchange(" + items[5] + ")'>" + items[6] + "</a></td>";
			}
			tr += "<td style='width:130px;'>" + items[7];
			tr += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			tr += "<img onclick='delSNFun(" + items[0] + ",\"" + (items[3]==null || items[3]=="null" ? "" : items[3]) + "\",\"" + (items[4]==null || items[4]=="null" ? "" : items[4]) + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			tr += "</div>";
			tr += "</td>";
			tr += "</tr>";
			oldStr += (items[1]==null || items[1]=="null" ? "" : items[1]) + ",";
		});
		$("#dtulist_body").html(tr);
    });
**/
}


function toEditPage(){
	$("#userInfoShow").css("display", "none");
	$("#userAddForm").css("display", "none");
	$("#userEditForm").css("display", "block");
}

function toDtuAllPage(){
	$("#userInfoShow").css("display", "none");
	$("#userDtuAllDiv").css("display", "block");
}

function toAddPage(){
	if(corpArr.length > 0 ){
		$("#userInfoShow").css("display", "none");
		$("#userEditForm").css("display", "none");
		$("#userAddForm").css("display", "block");
		$("#corpIdAdd").val("");
		$("#corpPhone_add").html("-");
		$("#corpEmail_add").html("-");
		$("#corpAddr_add").html("-");
		$("#linkPhone_add").html("-");
		$("#linkEmail_add").html("-");
		$("#linkMan_add").html("-");
		$('#searchCorp_add').combobox('setValue', '');
	}else{
		showDialogAlert("未找到没有账号的客户,请先添加客户");
	}
}

function toBackPage(){
	$("#userInfoShow").css("display", "block");
	$("#userEditForm").css("display", "none");
	$("#userAddForm").css("display", "none");
	$("#userDtuAllDiv").css("display", "none");
	$("#orderInfoShow").css("display", "none");
}

function showUserInfo(){
	userIdchange($("#selUserId").val());
}


function delUserFun(id, corpName){
	var d = dialog({
		title: '系统提示',
		content: '<div style="text-align:center;width:98%">是否确认删除账号<br>' + corpName + ' ？</div>',
		zIndex: 999998,
		button: [
				{
					value: '确定',
					callback: function () {
						$.post("${ctx}/customer/delDtuUser", {userId : id},function(data){
							if(data == "-1"){
								showDialogAlert("已绑定DTU设备，不能删除");
							}else if(data == "1"){
								showDialogAlert("操作成功");
								$("#selUserId").val("");
								findUserLi();
								//重新加载下拉框内容
								  $('#searchCorp_add').combobox({
									url: '${ctx}/customer/getCorpBoxvalue',
									valueField: 'corpid',
                                    textField: 'corpname',
									onSelect: function(record){
										$("#corpIdAdd").val("");
                                        selCorp(record.corpid);
									}
								  });
							}else{
								findUserLi();
							}
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
}

function showDelIcon(obj, type){
	$("#userdata").find("li").each(function(indexNum, ele){
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
	$.get("${ctx}/customer/getUserLi", {userId: userId, usertype:0},function(data){
		var li = "";
		var result = eval(data);
		$.each(result, function(index, items){
			li += "<li userLIData='" + items[0] + "' style='position:relative;' onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)'";
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

//订单已绑定的设备信息
function toAddDtuDialog(orderno){
	if(null == orderno || undefined == orderno){
		cleanOrderInfo();
		toScanAllPage();
		return;
	}if("-1" == orderno){
		cleanOrderInfo();
		return;
	}
	getOrderInfo(orderno);
	getOrderSnInfo(orderno);
}
function getOrderInfo(orderno){
	$.post("${ctx}/customer/getOrderInfo", {orderno:orderno},function(data){
		if(data != "-1"){
			var result = eval(data);
			var orderObj = result[0];
			var optUserName = result[1];
			$("#show_ldr").html(optUserName.username);
			$("#show_opttime_order").html(optUserName.opttime_edit);

			$("#show_salesman").html(orderObj.salesman);
			$("#show_corpName").html(orderObj.corp.corpName);

			$("#show_typeName").html(orderObj.vehicleType.typeName);
			$("#show_modelName").html(orderObj.vehicleModel.modelName);

			$("#show_factoryName").html(orderObj.batteryModel.factoryName);
			$("#show_batteryType").html(orderObj.batteryModel.batteryType.typeName);
			$("#show_batteryNumber").html(orderObj.batteryModel.batteryNumber + "串");
			$("#show_capacity").html(orderObj.batteryModel.capacity + "AH");
		}else{
			$("#show_ldr").html("");
			$("#show_opttime_order").html("");
			$("#show_salesman").html("");
			$("#show_corpName").html("");
			$("#show_typeName").html("");
			$("#show_modelName").html("");
			$("#show_factoryName").html("");
			$("#show_batteryType").html("");
			$("#show_batteryNumber").html("");
			$("#show_capacity").html("");
		}
	});
}

function cleanOrderInfo(){
	$("#show_ldr").html("");
	$("#show_opttime_order").html("");
	$("#show_salesman").html("");
	$("#show_corpName").html("");
	$("#show_typeName").html("");
	$("#show_modelName").html("");
	$("#show_factoryName").html("");
	$("#show_batteryType").html("");
	$("#show_batteryNumber").html("");
	$("#show_capacity").html("");
	$("#searchOrder_sn").val("-1");

	$("#dtu_body_sn").html("");
}

function getOrderSnInfo(orderno){
	$("#dtu_body_sn").html("");
    	$.post("${ctx}/customer/getOrderSNList", {orderno:orderno, prodtypeid:""},function(data){
    		var tr = "";
    		var result = eval(data);
    		if(result.length == 0){
    			$("#dtu_body_sn").html("<tr><td colspan='6'>无绑定设备或已全部添加到用户</td></tr>");
    			return;
    		}
    		$.each(result, function(index, items){
    			tr += "<tr>";
    			tr += "<td style='width:32px;border:1px solid #cccccc;'><input type='checkbox' name='selScanObj' value='" + items.id + "' /></td>";
    			tr += "<td style='width:90px;border:1px solid #cccccc;;' nowrap='nowrap'>" + items.prodType.prodTypename + "</td>";
    			tr += "<td style='width:125px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.sn + "</td>";
    			tr += "<td style='width:295px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.uuid + "</td>";
    			tr += "<td style='width:90px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.hwVersion + "</td>";
    			tr += "<td style='width:122px;border:1px solid #cccccc;' nowrap='nowrap'>" + items.swVersion + "</td>";
    			tr += "</tr>";
    		});
    		$("#dtu_body_sn").html(tr);
    	});
}

function selAllScan(obj){
	if($(obj).is(":checked")){
		$("#dtu_body_sn").find("input").each(function(i, em){
			if($(this).attr("id") == "selAll"){

			}else{
				$(this).prop('checked' , true);
			}
		});
	}else{
		$("#dtu_body_sn").find("input").each(function(i, em){
			if($(this).attr("id") == "selAll"){

			}else{
				$(this).prop('checked' , false);
			}
		});
	}
}

function toScanAllPage(){
	var elem = document.getElementById('orderInfoShow');
	var d = dialog({
	title: '添加设备',
	width:765,
	height:590,
	content: elem ,
	zIndex: 999999,
	padding: 3,
	button: [
			{
				value: '确定',
				callback: function () {
					var selStr = "";
					$("#dtu_body_sn").find("input").each(function(i, em){
						if($(this).attr("id") == "selAll"){

						}else{
							if($(this).prop('checked')){
								selStr += $(this).val() + ",";
							}
						}
					});
					if(selStr == ""){
						showDialogAlert("请选择设备");
						return false;
					}else{
						$.post("${ctx}/customer/sn2dtu", {orderno: $("#searchOrder_sn").val(), snids:selStr , userId: $("#selUserId").val()},function(data){
							$.messager.alert("系统提示", "操作成功", "info");
							userIdchange($("#selUserId").val());
						});
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


</script>
</head>
<body>	
<input type="hidden" id="selUserId" name="selUserId" value="" />
<input type="hidden" id="userDtu_old" name="userDtu_old" value="" />
<div class="body-outer dclear clearfix" style="width:1020px;">
		<div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
			<div class="page-table-body" style="width:1020px;">
				<!-- ---------------------------head start -------------------------- -->
				<div class="history-save-table-head-backend">
					<div class="history-save-table-head-outer" >
						<div class="manage-page-head-left dleft">
							<div class="dleft" style="width:150px; " onclick='findUserLi(-999)'><a href="javascript:void(0)" style="color:#FFF; font-size:14px;">账号列表</a></div>
							<div class="dright" style="width:24px; display:none;" onclick='toAddPage()' ><img src="${ctx}/static/images/add.png" style="width:20px;margin-top:12px;" title="添加客户" alt="添加客户" /></div>
						</div>
						<div class="manage-page-head-right dright" >
							<div class="dleft" style="width:670px;">	客户信息</div>
							<div class="dright" style="width:50px; margin-top:10px;display:none; ">
								<img src="${ctx}/static/images/edit.png" style="width:24px;cursor: pointer;" title="修改客户信息" alt="修改客户信息" onclick="toEditPage()" />
							</div>
						</div>
					</div>
				</div>
				<!-- ---------------------------head end -------------------------- -->
				<div class="historty-table-con-outer-backend dclear">
					<div class="manage-page-inner clearfix" style="margin-bottom: 0px !important; background:#E0E0E0;">
						<div class="dleft">
							<div style="background:#E0E0E0; width:200px; height:37px;text-align:center; margin-top:67px; margin-left:0px;border-bottom: 1px solid #cccccc;" id="searchorder_dev">
								<input id="searchorderKey_li" name="searchorderKey_li" style="width:160px; height:30px;" value="输入账号搜索" />
							</div>
							<div class="manage-page-left" style="width:200px!important;height:540px;margin-bottom: 0px !important;">
								<ul id="userdata">
									<c:forEach items="${userList}" var="item" varStatus="status">
										<li userLIData="${item[0]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;' onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' onclick="">
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
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="toAddPage()"><strong>&nbsp;&nbsp;创建新账号&nbsp;&nbsp;</strong></a>
							</div>
						</div>
						<!-- ---------------------------left end -------------------------- -->
						<div class="manage-page-right-backend dright easyui-accordion" id="userInfoShow" style="display:none; background:#FFF; margin-top:57px;height:626px;width:810px;">
							<div id="show_InfoDiv" class="manage-page-item" style="width:810px;" title="账号详情" data-options="iconCls:'icon-ok',selected:true,tools:[{ iconCls:'icon-edit',
                                                                                                                                                                                 handler:function(){
                                                                                                                                                                                     toEditPage();
                                                                                                                                                                                 }
                                                                                                                                                                             }]">
								<div style="width:810px; text-align:left; height:20px; margin-top:15px;">	<b style="margin-left:20px;margin-top:20px;">账号信息</b></div>
                                <div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;margin-top:1px; margin-bottom:10px;">
									<table class="dclear" style="width:810px;" >
										<tr>
											<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
											<td style="text-align:left; width:230px; border:0px; height:37px;" id="userName_show"></td>
											<td style="text-align:right; width:110px; border:0px;">用&nbsp;户&nbsp;密&nbsp;码：</td>
											<td style="text-align:left;border:0px;" id="userPwd_show"></td>
										</tr>
										<tr>
											<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;&nbsp;建&nbsp;人：</td>
											<td style="text-align:left; width:170px; border:0px; height:37px;" id="show_cjr"> </td>
											<td style="text-align:right; width:90px; border:0px; height:37px;">创&nbsp;建&nbsp;时&nbsp;间：</td>
											<td style="text-align:left; border:0px; height:37px;" id="show_opttime"> </td>
										</tr>
									</table>
								</div>
								<div class="manage-page-right-backend dright" id="corpDivShow">
									<div style="padding-left:20px; width:790px; text-align:left; margin-top:5px;margin-bottom:10px;"><b>客户信息</b></div>
									<table class="dclear" style="width:810px;" >
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
									</table>
								</div>
								<div class="manage-page-right-backend dright" id="subUser_show">
									<div style="padding-left:20px; width:790px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>子账号信息</b></div>
									<table class="dclear" style="width:810px;" id="subUserTab">
										<tr>
											<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
											<td style="text-align:left; width:230px; border:0px; height:37px;">
												<a href="javascript:void(0);" onclick="userIdchange(4035)">adadasd<a>
											</td>
											<td style="text-align:right; width:110px; border:0px;">用&nbsp;户&nbsp;密&nbsp;码：</td>
											<td style="text-align:left;border:0px;">xxxx</td>
										</tr>
									</table>
								</div>
							</div>

							<div class="page-table-body" align="center" style="padding:0px;" title="设备信息" data-options="iconCls:'icon-ok',tools:[{ iconCls:'icon-add',
																																								  handler:function(){
																																									  toAddDtuDialog();
																																								  }
																																							  },
																																							  { iconCls:'icon-hamburg-busy',
																																							  		handler:function(){
																																							  	 		delSNFun();
																																							  		}
																																							  }]">
								<table id="dg" style="width:810px;height:564px;">
									<thead>
										<tr>
											<th data-options="field:'id',width:50,align:'center', halign:'center', checkbox:true"></th>
											<th data-options="field:'prod_name',width:100,align:'center', halign:'center', sortable:true">产品信息</th>
											<th data-options="field:'hw_version',width:100,align:'center', halign:'center', sortable:true">硬件版本号</th>
											<th data-options="field:'sn',width:160,align:'center', halign:'center', sortable:true">产品SN码</th>
											<th data-options="field:'uuid',width:160,align:'center', halign:'center', sortable:true">UUID</th>
											<th data-options="field:'user_name',width:100,align:'center', halign:'center', sortable:true">设备来源</th>
											<th data-options="field:'opttimestr',width:100,align:'center', halign:'center', sortable:true">添加日期</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
						<form id="userEditForm" action="javascript:;" style="display: none; margin-top:57px;">
							<input type="hidden" id="id" name="id" value="" />
							<div class="manage-page-right-backend dright" style="background:#FFF; height:630px;">
								<div class="manage-page-item" style="width:810px;">
									<div class="manage-page-item-inner clearfix" style="width:810px;">
										<div class="manage-page-right-backend dright" style="padding-bottom:5px;">
											<div style="width:810px; text-align:left; margin-top:15px;height:25px;">
												<b style="margin-left:20px;">账号信息</b>
											</div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;" id="userName_edit"></td>
													<td style="text-align:right; width:110px; border:0px;">用&nbsp;户&nbsp;密&nbsp;码：</td>
													<td style="text-align:left;border:0px;">
														<input type="text" id="userPassEdit" name="userPass" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="10" value="输入字母或者数字" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g,''))" /><font color="red">*</font>
													</td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right-backend dright">
											<div style="padding-left:20px; width:810px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>客户信息</b></div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpName_edit"> </td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人姓名：</td>
													<td style="text-align:left; border:0px; height:37px;" id="linkMan_edit"> </td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpPhone_edit"> </td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人电话：</td>
													<td style="text-align:left; border:0px; height:37px;" id="linkPhone_edit"> </td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpEmail_edit"> </td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人邮箱：</td>
													<td style="text-align:left; border:0px; height:37px;" id="linkEmail_edit"> </td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
													<td style="text-align:left; border:0px; height:37px;" id="corpAddr_edit" colspan="3"> </td>
												</tr>
											</table>
										</div>
									</div>
								</div>
								<div style="width: 810px; margin-top: 10px;" align="center" >
									<input type="button" value=" " onclick="return updateUser()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>
						<form id="userAddForm" action="javascript:;" style="display: none;margin-top:57px;">
							<input type="hidden" id="id" name="id" value="" />
							<input type="hidden" id="corpIdAdd" name="corp.id" value="" />
							<div class="manage-page-right-backend dright" style="background:#FFF; height:630px;">
								<div class="manage-page-item" style="width:810px;">
									<div class="manage-page-item-inner clearfix" style="width:810px;">
										<div class="manage-page-right-backend dright" style="padding-bottom:5px;">
											<div style="width:810px; text-align:left; margin-top:15px;height:25px;">
												<b style="margin-left:20px;">账号信息</b>
											</div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="userNameAdd" name="userName" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="10" value="输入字母或者数字" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g,''))" /><font color="red">*</font>
													</td>
													<td style="text-align:right; width:110px; border:0px;">用&nbsp;户&nbsp;密&nbsp;码：</td>
													<td style="text-align:left;border:0px;">
														<input type="text" id="userPassAdd" name="userPass" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="10" value="输入字母或者数字" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g,''))" /><font color="red">*</font>
													</td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right-backend dright">
											<div style="padding-left:20px; width:810px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>客户信息</b></div>
											<table class="dclear" style="width:810px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="searchCorp_add" name="searchCorp_add" class="form-control input-sm" style="width:210px;height:35px;" />
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人姓名：</td>
													<td style="text-align:left; border:0px; height:37px;" id="linkMan_add"> </td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpPhone_add"> </td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人电话：</td>
													<td style="text-align:left; border:0px; height:37px;" id="linkPhone_add"> </td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpEmail_add"> </td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">联系人邮箱：</td>
													<td style="text-align:left; border:0px; height:37px;" id="linkEmail_add"> </td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
													<td style="text-align:left; border:0px; height:37px;" id="corpAddr_add" colspan="3"> </td>
												</tr>
											</table>
										</div>
									</div>
								</div>
								<div style="width: 810px; margin-top: 10px;" align="center" >
									<input type="button" value=" " onclick="return addUser()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>
						<div class="manage-page-right-backend dright" id="orderInfoShow">
							<div class="manage-page-item" style="width:810px;">
								<div class="manage-page-item-inner clearfix" style="width:810px;">
									<div class="manage-page-right-backend dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;">
										<div style="width:810px; text-align:left; margin-top:5px;height:25px;">
											<b style="margin-left:20px;">订单信息</b>
										</div>
										<table class="dclear" style="width:810px;" >
											<tr>
												<td style="text-align:right; width:90px; border:0px; height:30px;">订单编号：</td>
												<td style="text-align:left; width:170px; border:0px; height:30px;">
													<select id="searchOrder_sn" name="searchOrder_sn" style="width:160px;" onchange="toAddDtuDialog(this.value)">
														<option value="-1">请选择</option>
													</select>
												</td>
												<td style="text-align:right; width:90px; border:0px;">客户名称：</td>
												<td style="text-align:left; border:0px;" colspan="3" id="show_corpName"></td>
											</tr>
											<tr>
												<td style="text-align:right; width:90px; border:0px; height:30px;">销售代表：</td>
												<td style="text-align:left; border:0px; height:30px;" id="show_salesman"> </td>
												<td style="text-align:right; width:90px; border:0px; height:30px;">创&nbsp;&nbsp;建&nbsp;人：</td>
												<td style="text-align:left; width:170px; border:0px; height:30px;" id="show_ldr"> </td>
												<td style="text-align:right; width:90px; border:0px; height:30px;">创建时间：</td>
												<td style="text-align:left; border:0px; height:30px;" id="show_opttime_order"> </td>
											</tr>
										</table>
									</div>
									<div class="manage-page-right-backend dright">
										<div style="padding-left:20px; width:810px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>车辆信息</b></div>
										<table class="dclear" style="width:810px;" >
											<tr>
												<td style="text-align:right; width:90px; border:0px; height:30px;">车辆类型：</td>
												<td style="text-align:left; width:170px; border:0px; height:30px;" id="show_typeName"> </td>
												<td style="text-align:right; width:90px; border:0px; height:30px;">车辆型号：</td>
												<td style="text-align:left; border:0px; height:30px;" id="show_modelName"> </td>
												<td style="text-align:right; width:90px; border:0px; height:30px;">电池厂商：</td>
												<td style="text-align:left; border:0px; height:30px;" id="show_factoryName"> </td>
											</tr>
											<tr>
												<td style="text-align:right; width:90px; border:0px; height:30px;">电池类型：</td>
												<td style="text-align:left; width:170px; border:0px; height:30px;" id="show_batteryType"> </td>
												<td style="text-align:right; width:90px; border:0px; height:30px;">电池串数：</td>
												<td style="text-align:left; width:170px; border:0px; height:30px;" id="show_batteryNumber"> </td>
												<td style="text-align:right; width:90px; border:0px; height:30px;">电池容量：</td>
												<td style="text-align:left; border:0px; height:30px;" id="show_capacity"> </td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							<div style="width:810px; text-align:left; height:28px; padding-top:12px;">
							<b style="margin-left:20px; margin-top:8px !important;">设备信息</b>
							</div>
							<div style="margin-top:0px; height:37px; width:810px;">
								<div class="table-body2" style=" width:810px;">
									<table class="dclear" style=" width:810px;">
											<tr class="trHead">
												<th style="width:32px;"><input type="checkbox" value="1" name="selAll" id="selAll" onclick="selAllScan(this)" /></th>
												<th style="width:90px;" id="prodHeadTab_th">产品信息</th>
												<th style="width:125px;">SN码</th>
												<th style="width:295px;">UUID</th>
												<th style="width:90px;">硬件版本号</th>
												<th style="width:122px;">软件版本号</th>
											</tr>
									</table>
								</div>
							</div>
							<div class="manage-page-left dleft" style="margin-top:0px; height:287px; width:810px; background: #FFF;">
								<div class="table-body2" style=" width:810px;">
									<table class="dclear" style=" width:810px;">
										<tbody id="dtu_body_sn" valign="top">
										</tbody>
									</table>
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
</body>
</html>