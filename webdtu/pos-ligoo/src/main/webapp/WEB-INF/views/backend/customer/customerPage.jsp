<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_customer"/>
	<title>客户管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/easyui.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/icon.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/css4easyui.css" />

	<script src="${ctx}/static/js/jquery-1.11.1.min.js"></script>
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>

	<script src="${ctx}/static/js/jquery.easyui.min.js"></script>
	<script src="${ctx}/static/js/easyui-lang-zh_CN.js"></script>
	<script src="${ctx}/static/js/jquery.jdirk.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.all.min.js"></script>

		<style>
    	.combo .combo-text{
    		background:#E0E0E0;
    		font-size: 14px;
    		color:#000;
    		padding-top: 0px;
    	}
    	.combo-arrow{
    		background-color:#E0E0E0;
    	}
    	.combo,
        .combo-panel {
          background-color: #E0E0E0;
          color:#000;
          margin-top:0px;
        }

        .combobox-item,.combobox-group {
        	font-size: 14px;
        	padding: 3px;
        	padding-right: 0px;
        }
    	.combo{border-width:0px;}

    	.combobg{
    		background-color: #FFF !important;;
    	}

    	.manage-input-text {
			 background: #E0E0E0;
			 padding-left: 5px;
			 width: 206px;
			 height: 18px;
			 line-height: 18px;
			 border: none;
			 margin-top: 2px;
		 }

		.table-body td{ text-align: center; border: 1px solid #cccccc; color: #000;vertical-align: top; background-color:#FFF;height:37px;line-height: 37px;}
		.table-body th{ text-align: center; border: 1px solid #cccccc; color: #000; background-color: #ECECF0;height:37px;line-height: 37px; }
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

	 $(".showTip").inputTipsText();

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

	$("#searchorder_dev").find(".combo").addClass("combobg");
	$("#searchorder_dev").find(".combo-text").addClass("combobg");

});

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



		/**
		$("#userName_show").html("");
        $("#userPwd_show").html("");
		var accountList = dataObj.accountList;
		if(accountList.length > 0){
			var accountObj = accountList[0];
			$("#userName_show").html(accountObj[0]);
			$("#userPwd_show").html(accountObj[0]);
		}
		**/
	});
	toBackPage();
	getOrderInfo();
	//还原搜索框的值
    //$('#searchorderKey_li').combobox('setValue', '');
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
	$("#userInfoShow").css("display", "none");
	$("#userEditForm").css("display", "none");
	$("#userAddForm").css("display", "block");
	$("#userAddForm")[0].reset();
	$("#userIdAdd").val("");
}

function toBackPage(){
	$("#userInfoShow").css("display", "block");
	$("#userEditForm").css("display", "none");
	$("#userAddForm").css("display", "none");
	$("#userDtuAllDiv").css("display", "none");
}

function showUserInfo(){
	userIdchange($("#selUserId").val());
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

function showDelIcon(obj, type){
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

function showDelIcon_dtu(obj, type){
	if(showDelIconType == 1){
		if(type == 1){
			$(obj).find('.handle').show();
		}else{
			$(obj).find('.handle').hide();
		}
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
	$.get("${ctx}/customer/getCorpLi?rmd=" + new Date().getTime(), {userId: userId, usertype:0},function(data){
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

//订单信息
function getOrderInfo(){
    	$("#order_body").html("");
    	$.post("${ctx}/order/getOrderByCorpId?rmd=" + new Date().getTime(), {corpid:$("#selUserId").val()},function(data){
			var tr = "";
			var result = eval(data);
			if(result.length == 0){
				$("#order_body").html("<tr><td >无订单信息</td></tr>");
				return;
			}
			tr += "<tr style='color:#000; background-color:#ECECF0; height:37px;'>";
			tr += "<th style='width:35px; '>&nbsp;</th>";
			tr += "<th style='' nowrap='nowrap' width='*'>订单编号</th>";
			tr += "<th style='width:123px;' nowrap='nowrap'>车辆型号</th>";
			tr += "<th style='width:123px; ' nowrap='nowrap'>电池类型</th>";
			tr += "<th style='width:120px;' nowrap='nowrap'>电池串数</th>";
			tr += "<th style='width:120px;' nowrap='nowrap'>录&nbsp;单&nbsp;人</th>";
			tr += "<th style='width:120px;' nowrap='nowrap'>录单日期</th>";
			tr += "</tr>";
			$.each(result, function(index, items){
				tr += "<tr>";
				tr += "<td style='width:35px;'>" + (index + 1) + "</td>";
				tr += "<td  width='*' style='' nowrap='nowrap'><a href=\"${ctx}/order/device?order_init=" + items.orderno + "\">" + items.orderno + "</a></td>";
				tr += "<td style='width:123px;' nowrap='nowrap'>" + items.vehicleType.typeName + "</td>";
				tr += "<td style='width:123px;' nowrap='nowrap'>" + items.batteryModel.batteryType.typeName + "</td>";
				tr += "<td style='width:120px;' nowrap='nowrap'>" + items.batteryModel.batteryNumber + "</td>";
				tr += "<td style='width:120px;' nowrap='nowrap'>" + items.optUserName + "</td>";
				tr += "<td style='width:120px;' nowrap='nowrap'>" + (parseInt(items.optTime.year) + 1900) + "-" + (parseInt(items.optTime.month) + 1) + "-" + items.optTime.date + "</td>";
				tr += "</tr>";
			});

			$("#order_body").html(tr);
	});
}

function toAddOrderPage(){
	window.location.href="${ctx}/order/page?toadd=toadd&userid=" + $("#selUserId").val();
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
							<div class="dleft" style="width:150px; " onclick='findUserLi(-999)'><a href="javascript:void(0)" style="color:#FFF; font-size:14px;">客户列表</a></div>
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
					<div class="manage-page-inner clearfix" style="width:1010px;">
						<div class="dleft">
							<div style="background:#E0E0E0; width:200px; height:37px;text-align:center; margin-top:57px; padding-top:8px; margin-left:0px;border-bottom: 1px solid #cccccc;" id="searchorder_dev">
								<input id="searchorderKey_li" name="searchorderKey_li" style="width:160px; height:30px;" value="输入客户名称搜索" />
							</div>
							<div class="manage-page-left" style="width:200px!important;height:537px;margin-bottom: 0px !important;">
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
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="toAddPage()"><strong>&nbsp;&nbsp;创建新客户&nbsp;&nbsp;</strong></a>
							</div>
						</div>
						<!-- ---------------------------left end --------------  border-bottom:1px solid #ccc; ------------ -->
						<div class="easyui-panel" id="userInfoShow" style="display:none; background:#FFF;margin-top:1px; margin-top:57px;height:655px;width:810px; font-size:14px;overflow: hidden;" align="right" >
							<div class="easyui-panel" title="基本信息" style="width:805px;height:200px;padding:1px;font-size:14px;"	 data-options="iconCls:'icon-book-go',tools:'#tt_base'">
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
							<div id="tt_base">
								<a href="javascript:void(0)" class="icon-edit" onclick="toEditPage()"></a>
							</div>
							<div class="easyui-panel" title="订单信息" style="width:805px;padding:1px;font-size:14px;height: 382px;" data-options="iconCls:'icon-book-go',tools:'#tt_module',collapsible:false">
								<table class="dclear" width="100%">
									<tbody id="order_body" valign="top" class="table-body" style="font-size: 14px;">
									</tbody>
								</table>
							</div>
							<div id="tt_module">
								<a href="javascript:void(0)" class="icon-add" onclick="javascript:toAddOrderPage()"></a>
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
														<input type="text" id="corpAddrEdit" name="addr" class="manage-input-text" maxlength="100" style="width:546px;" />
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
														<input type="text" id="corpaddrAdd" name="addr" class="manage-input-text" maxlength="100" style="width:546px;" />
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
</body>
</html>