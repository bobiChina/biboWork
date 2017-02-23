<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_permission"/>
	<title>权限管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/doc.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/easyui.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/css3buttons.css" />

	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<script src="${ctx}/static/js/jquery.easyui.min.js"></script>
	<script src="${ctx}/static/js/easyui-lang-zh_CN.js"></script>
	<script src="${ctx}/static/js/jquery.jdirk.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.js"></script>

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
    	.table-body select {
			background: #E0E0E0;
			color: #000;
			border: none;
			height: 30px;
			line-height: 30px;
			text-align: right;
			width: 190px;
		 }
        .manage-input-text {
			 background: #E0E0E0;
			 padding-left: 5px;
			 width: 186px;
			 height: 18px;
			 line-height: 18px;

			 border: none;
			 margin-top: 2px;
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
	var permissionStr = "";
	$("#userEditForm [name='permission']").each(function(index, ele){
		if($(this).is(':checked')){
			permissionStr += $(this).val() + ",";
		}
	});
	if($("#salesmanEdit").is(':checked')){
		$("#isSalesmanEdit").val("1");
	}else{
		$("#isSalesmanEdit").val("0");
	}
	if($("#technicistEdit").is(':checked')){
		$("#isTechnicistEdit").val("1");
	}else{
		$("#isTechnicistEdit").val("0");
	}

	if ($("#corpName").val() == ""){
		$.messager.alert("系统提示", "请填写公司名称", "warning");
		return;
	}
	if ($("#fullName").val() == ""){
		$.messager.alert("系统提示", "请填写用户名", "warning");
		return;
	}

	var emailEdit = $.trim($("#emailEdit").val());
    if (emailEdit.length >0){
    	var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/ ;
    	if(!reg.test(emailEdit)){
			$.messager.alert("系统提示", "邮箱格式不正确", "warning");
    		return;
    	}
    }
    if("" == permissionStr){
		$.messager.alert("系统提示", "请选择权限", "warning");
		return;
	}else{
		$("#permissionIdsEdit").val(permissionStr);
	}


	$.post("${ctx}/permission/updateUser?rmd=" + new Date().getTime(), $("#userEditForm").serialize(),function(data){
		$.messager.alert("系统提示", "操作成功", "info");
		//显示左侧页面
		findUserLi();
    });
}

function addUser(){
	var permissionStr = "";
	$("#userAddForm [name='permission']").each(function(index, ele){
		if($(this).is(':checked')){
			permissionStr += $(this).val() + ",";
		}
	});

	if ($("#userNameAdd").val() == "" || $("#userNameAdd").val() == "输入字母或者数字"){
		$.messager.alert("系统提示", "请填写用户账号", "warning");
		return;
	}
	if ($("#userPassAdd").val() == "" || $("#userPassAdd").val() == "输入字母或者数字"){
		$.messager.alert("系统提示", "请填写用户密码", "warning");
		return;
	}

	if ($("#fullNameAdd").val() == ""){
		$.messager.alert("系统提示", "请填写姓名", "warning");
		return;
	}
	var emailAdd = $.trim($("#emailAdd").val());
	if (emailAdd.length >0){
		var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/ ;
		if(!reg.test(emailAdd)){
			$.messager.alert("系统提示", "邮箱格式不正确", "warning");
			return;
		}
	}
	if($("#salesmanAdd").is(':checked')){
		$("#isSalesmanAdd").val("1");
	}else{
		$("#isSalesmanAdd").val("0");
	}
	if($("#technicistAdd").is(':checked')){
		$("#isTechnicistAdd").val("1");
	}else{
		$("#isTechnicistAdd").val("0");
	}

	if("" == permissionStr){
		$.messager.alert("系统提示", "请选择权限", "warning");
        return;
	}else{
		$("#permissionIdsAdd").val(permissionStr);
	}

	$.post("${ctx}/permission/addUser?rmd=" + new Date().getTime(), $("#userAddForm").serialize(),function(data){
		if(data == -1){
			$.messager.alert("系统提示", "用户账号已存在,重新填写", "warning");
			return;
		}else{
			$("#selUserId").val(data);
            findUserLi();
			$.messager.alert("系统提示", "操作成功", "info");
		}
    });

}

function userIdchange(id){
	$("#selUserId").val(id);
	$("#userdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-leftactive");
		if($(this).attr("userLIData") == id){
			$(this).addClass("manage-leftactive");
		}
    });

	$.get("${ctx}/permission/getuser?rmd=" + new Date().getTime(), {userid:id},function(data){
		var result = eval('(' + data + ')');
		var userObj = result.userObj;
		$("#userName").val(userObj.userName);
		$("#userNameText").html(userObj.userName);
		$("#userName_show").html(userObj.userName);
		$("#userPwd_show").html(userObj.userPass);
		$("#userPwd_show").html(userObj.userPass);
		$("#userPassEdit").val(userObj.userPass);

		$("#isAdminEdit").val(userObj.isAdmin);
		$("#fullNameEdit").val(userObj.fullName);
		$("#fullName_show").html(userObj.fullName);
		$("#relation_show").html(userObj.relation);
		$("#relationEdit").val(userObj.relation);
		$("#email_show").html(userObj.email);
		$("#emailEdit").val(userObj.email);

	    $("#id").val(userObj.id);
		/********************************
		 * 添加用户职务
		 *******************************/
			//清除原复选框信息
		$("[name='zhiwu']").removeAttr("checked");
		if(userObj.isSalesman == "1"){
			$("#salesman_show").prop("checked",true);
			$("#salesmanEdit").prop("checked",true);
			$("#isSalesmanEdit").val("1");
		}else{
			$("#isSalesmanEdit").val("0");
		}
		if(userObj.isTechnicist == "1"){
			$("#technicist_show").prop("checked",true);
			$("#technicistEdit").prop("checked",true);
			$("#isTechnicistEdit").val("1");
		}else{
			$("#isTechnicistEdit").val("0");
		}

	    //原账户信息
	    var accountInfoStr = "账号：" + userObj.userName +  "，密码：" + userObj.userPass ;
		$("#accountInfo").val(accountInfoStr);
	    //原联系人信息
	    var linkInfoStr = "联系人：" + userObj.fullName + "，电话：" + userObj.relation + "，邮箱：" + userObj.email  ;
	    $("#linkInfo").val(linkInfoStr);

	    //权限信息
	    //清除原复选框信息
	    $("[name='permission']").removeAttr("checked");

	    var permissionList = result.permissionList ;
	    var permissionObj;
	    if(permissionList.length > 0){
			for(var x = 0; x < permissionList.length; x ++ ){
				permissionObj = permissionList[x] ;
				$("#show_permission_" + permissionObj.id).prop("checked",true);
				$("#edit_permission_" + permissionObj.id).prop("checked",true);
			}
	    }
	});
	toBackPage();
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
	$("#userAddForm [name='permission']").removeAttr("checked");
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


function delUserFun(id, name){
	$.messager.confirm("删除确认", '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>用户号:' + name + '</div>', function(cr){
		if(cr){
			$.post("${ctx}/permission/delUser?rmd=" + new Date().getTime(), {userId : id},function(data){
				$.messager.alert("系统提示", "操作成功", "info");
				$("#selUserId").val("");
				findUserLi();
			});
		}
	});

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
	$.get("${ctx}/permission/getUserLi?rmd=" + new Date().getTime(), {},function(data){
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

</script>
</head>
<body>	
<input type="hidden" id="selUserId" name="selUserId" value="" />
<input type="hidden" id="userDtu_old" name="userDtu_old" value="" />
<div class="body-outer dclear clearfix" style="width:1020px;">
		<div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
			<div class="table-body page-table-body" style="width:1020px;">
				<!-- ---------------------------head start -------------------------- -->
				<div class="history-save-table-head-backend">
					<div class="history-save-table-head-outer" >
						<div class="manage-page-head-left dleft">
							<div class="dleft" style="width:150px; " onclick='findUserLi(-999)'><a href="javascript:void(0)" style="color:#FFF; font-size:14px;">用户列表</a></div>
							<div class="dright" style="width:24px; display:none;" onclick='toAddPage()' ><img src="${ctx}/static/images/add.png" style="width:20px;margin-top:12px;" title="添加客户" alt="添加客户" /></div>
						</div>
						<div class="manage-page-head-right dright" >
							<div class="dleft" style="width:670px;">	用户信息</div>
							<div class="dright" style="width:50px; margin-top:10px;display:none; ">
								<img src="${ctx}/static/images/edit.png" style="width:24px;cursor: pointer;" title="修改客户信息" alt="修改客户信息" onclick="toEditPage()" />
							</div>
						</div>
					</div>
				</div>
				<!-- ---------------------------head end -------------------------- -->

				<div class="historty-table-con-outer-backend dclear">
					<div class="history-table-con-inner manage-page-inner clearfix" style="margin-bottom: 0px !important;">
						<div class="dleft">
							<div style="background:#E0E0E0; width:200px; height:37px;text-align:center; padding-top:8px; margin-left:0px;border-bottom: 1px solid #cccccc;" id="searchorder_dev">
								<input id="searchorderKey_li" name="searchorderKey_li" style="width:160px; height:30px;" value="输入用户姓名搜索" />
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
								<a href="javascript:void(0)" class="pill button" style="padding-bottom:5px !important;"  onclick="toAddPage()"><strong>&nbsp;&nbsp;创建新用户&nbsp;&nbsp;</strong></a>
							</div>
						</div>
						<!-- ---------------------------left end -------------------------- -->

						<div class="manage-page-right-backend dright" id="userInfoShow" style="margin-bottom: 0px !important; height:630px; background:#FFF; display:none;">
							<div class="manage-page-item">
								<div style="margin-left:190px; width:620px; text-align:left; margin-top:15px;padding-bottom:5px;height:25px;"><b>账户信息：</b>
								 	<a href="javascript:void(0)" class="button" style="margin-left:488px;"  onclick="toEditPage()"><strong>编辑</strong></a>
								</div>
								<table class="dclear" style="width:710px;" >
									<tr >
										<td style="text-align:right; width:280px; height:37px; border:0px;">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
										<td style="text-align:left; border:0px;" id="userName_show"></td>
									</tr>
									<tr>
										<td style="text-align:right; width:280px; height:37px; border:0px;">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
										<td style="text-align:left; border:0px;" id="userPwd_show"></td>
									</tr>
								</table>
							</div>
							<div class="manage-page-item">
								<div style="margin-left:190px; width:300px; text-align:left; margin-top:15px;margin-bottom:5px;"><b>用户信息：</b></div>
								<table class="dclear" style="width:710px;" >
									<tr >
										<td style="text-align:right; width:280px; height:37px; border:0px;">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
										<td style="text-align:left; border:0px;" id="fullName_show"></td>
									</tr>
									<tr>
										<td style="text-align:right; width:280px; height:37px; border:0px;">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
										<td style="text-align:left; border:0px;" id="relation_show" ></td>
									</tr>
									<tr>
										<td style="text-align:right; width:280px; height:37px; border:0px;">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</td>
										<td style="text-align:left; border:0px;" id="email_show"></td>
									</tr>
									<tr>
										<td style="text-align:right; width:280px; height:37px; border:0px;">职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务：</td>
										<td style="text-align:left; border:0px;">
											<label>销售代表 <input type="checkbox" id="salesman_show" name="zhiwu" value="0" disabled="disabled" /></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<label>技术代表 <input type="checkbox" id="technicist_show" name="zhiwu" value="0" disabled="disabled" /></label>
										</td>
									</tr>
								</table>
							</div>
							<div class="manage-page-item">
								<div style="margin-left:190px; width:300px; text-align:left; margin-top:15px;margin-bottom:5px;"><b>权限分配：</b></div>
								<table class="dclear" style="width:620px;margin-left:190px;" id="show_permission_table">
									<colgroup>
										<col width="150">
										<col width="140">
										<col width="160">
										<col width="*">
									</colgroup>
									<tr >
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_2" name="permission" value="2" disabled="disabled" /> POPBE管理 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_7" name="permission" value="7" disabled="disabled" /> BDCBE管理 </label>
										</td>
										<td style="text-align:left; border:0px;" width="*" colspan="2" >
											&nbsp;
										</td>
									</tr>
									<tr >
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_4" name="permission" value="4" disabled="disabled" /> 订单管理 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_17" name="permission" value="17" disabled="disabled" /> 软件管理 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_11" name="permission" value="11" disabled="disabled" /> 生产管理 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_5" name="permission" value="5" disabled="disabled" /> 产品管理 </label>
										</td>
									</tr>
									<tr >
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_13" name="permission" value="13" disabled="disabled" /> 软件开发 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_14" name="permission" value="14" disabled="disabled" /> 软件测试 </label>
										</td>
										<td style="text-align:left; border:0px;" width="*" colspan="2" >
											&nbsp;
										</td>
									</tr>
									<tr >

										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_8" name="permission" value="8" disabled="disabled" /> BIScanne操作员 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_10" name="permission" value="10" disabled="disabled" /> BATS操作员 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_6" name="permission" value="6" disabled="disabled" /> MIScanner操作员 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_9" name="permission" value="9" disabled="disabled" /> CCSpider操作员 </label>
										</td>
									</tr>
									<tr >
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_12" name="permission" value="12" disabled="disabled" /> 销售经理 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_15" name="permission" value="15" disabled="disabled" /> 销售代表 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_16" name="permission" value="16" disabled="disabled" /> 服务代表 </label>
										</td>
										<td style="text-align:left; border:0px;" >
											<label style="margin-right:25px;"><input type="checkbox" id="show_permission_18" name="permission" value="18" disabled="disabled" /> 订单设备管理 </label>
										</td>
									</tr>
								</table>
							</div>
						</div>
						<form id="userEditForm" action="javascript:;" style="display: none;">
							<input type="hidden" id="corpId" name="corp.id" value="1"/>
							<input type="hidden" id="userName" name="userName" value=""/>
							<input type="hidden" id="id" name="id" value="" />
							<input type="hidden" id="isAdminEdit" name="isAdmin" value="" />
							<input type="hidden" id="linkInfo" name="linkInfo" value=""/>
							<input type="hidden" id="accountInfo" name="accountInfo" value=""/>
							<input type="hidden" id="permissionIdsEdit" name="permissionIds" value=""/>
							<input type="hidden" id="isTechnicistEdit" name="isTechnicist" value="0"/>
							<input type="hidden" id="isSalesmanEdit" name="isSalesman" value="0"/>

							<div class="manage-page-right-backend dright" style="height:630px; background:#FFF;">
								<div class="manage-page-item">
									<div style="margin-left:190px; width:300px; text-align:left; margin-top:15px;padding-bottom:5px;padding-top:4px;height:21px;"><b>账户信息：</b></div>
									<table class="dclear" style="width:710px;" >
										<tr >
											<td style="text-align:right; width:280px; height:37px; border:0px;">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
											<td style="text-align:left; border:0px;" id="userNameText"></td>
										</tr>
										<tr>
											<td style="text-align:right; width:280px; height:37px; border:0px;">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="userPassEdit" name="userPass" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="10" value="输入字母或者数字" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g,''))" /><font color="red">*</font></td>
										</tr>
									</table>
								</div>
								<div class="manage-page-item">
									<div style="margin-left:190px; width:300px; text-align:left; margin-top:15px;margin-bottom:5px;"><b>用户信息：</b></div>
									<table class="dclear" style="width:710px;" >
										<tr >
											<td style="text-align:right; width:280px; height:37px; border:0px;">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="fullNameEdit" name="fullName" class="manage-input-text" maxlength="10" /><font color="red">*</font></td>
										</tr>
										<tr>
											<td style="text-align:right; width:280px; height:37px; border:0px;">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="relationEdit" name="relation" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" /></td>
										</tr>
										<tr>
											<td style="text-align:right; width:280px; height:37px; border:0px;">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="emailEdit" name="email" class="manage-input-text" maxlength="100" /></td>
										</tr>
										<tr>
											<td style="text-align:center; height:37px; border:0px;" colspan="2">
												<label>销售代表 <input type="checkbox" id="salesmanEdit" name="zhiwu" value="0" /></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<label>技术代表 <input type="checkbox" id="technicistEdit" name="zhiwu" value="0" /></label>
											</td>
										</tr>
									</table>
								</div>
								<div class="manage-page-item">
									<div style="margin-left:190px; width:300px; text-align:left; margin-top:15px;margin-bottom:5px;"><b>权限分配：</b></div>
									<table class="dclear" style="width:620px;margin-left:190px;" >
										<colgroup>
											<col width="150">
											<col width="140">
											<col width="160">
											<col width="*">
										</colgroup>
										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_2" name="permission" value="2" /> POPBE管理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_7" name="permission" value="7" /> BDCBE管理员 </label>
											</td>
											<td style="text-align:left; border:0px;" colspan="2" >
												&nbsp;
											</td>
										</tr>
										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_4" name="permission" value="4" /> 订单管理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_17" name="permission" value="17" /> 软件管理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_11" name="permission" value="11" /> 生产管理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_5" name="permission" value="5" /> 产品管理 </label>
											</td>
										</tr>
										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_13" name="permission" value="13" /> 软件开发 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_14" name="permission" value="14" /> 软件测试 </label>
											</td>
											<td style="text-align:left; border:0px;" colspan="2" >
												&nbsp;
											</td>
										</tr>
										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_8" name="permission" value="8" /> BIScanne操作员 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_10" name="permission" value="10" /> BATS操作员 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_6" name="permission" value="6" /> MIScanner操作员 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_9" name="permission" value="9" /> CCSpider操作员 </label>
											</td>
										</tr>

										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_12" name="permission" value="12" /> 销售经理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_15" name="permission" value="15" /> 销售代表 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_16" name="permission" value="16" /> 服务代表 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="edit_permission_18" name="permission" value="18" /> 订单设备管理 </label>
											</td>
										</tr>
									</table>
								</div>
								<div style="width: 810px; margin-top: 10px; background: #FFF;" align="center" >
									<input type="button" value=" " onclick="return updateUser()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>

						<form id="userAddForm" action="javascript:;" style="display: none;">
							<input type="hidden" id="userIdAdd" name="userIdAdd" value=""/>
							<input type="hidden" id="isAdminAdd" name="isAdmin" value="2"/>
							<input type="hidden" id="corpIdAdd" name="corp.id" value="1"/>
							<input type="hidden" id="permissionIdsAdd" name="permissionIds" value=""/>
							<input type="hidden" id="isTechnicistAdd" name="isTechnicist" value="0"/>
							<input type="hidden" id="isSalesmanAdd" name="isSalesman" value="0"/>
							<div class="manage-page-right-backend dright" style="height:630px; background:#FFF;">
								<div class="manage-page-item">
									<div style="margin-left:190px; width:300px; text-align:left;margin-top:15px;padding-bottom:5px;padding-top:4px;height:21px;"><b>账户信息：</b></div>
									<table class="dclear" style="width:710px;" >
										<tr >
											<td style="text-align:right; width:280px; height:37px; border:0px;">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="userNameAdd" name="userName" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="30" value="输入字母或者数字" /><font color="red">*</font></td>
										</tr>
										<tr>
											<td style="text-align:right; width:280px; height:37px; border:0px;">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="userPassAdd" name="userPass" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="10" value="输入字母或者数字" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g,''))" /><font color="red">*</font></td>
										</tr>
									</table>
								</div>
								<div class="manage-page-item">
									<div style="margin-left:190px; width:300px; text-align:left; margin-top:15px;margin-bottom:5px;"><b>用户信息：</b></div>
									<table class="dclear" style="width:710px;" >
										<tr >
											<td style="text-align:right; width:280px; height:37px; border:0px;">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="fullNameAdd" name="fullName" class="manage-input-text" maxlength="10" /><font color="red">*</font></td>
										</tr>
										<tr>
											<td style="text-align:right; width:280px; height:37px; border:0px;">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="relationAdd" name="relation" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" /></td>
										</tr>
										<tr>
											<td style="text-align:right; width:280px; height:37px; border:0px;">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</td>
											<td style="text-align:left; border:0px;" ><input type="text" id="emailAdd" name="email" class="manage-input-text" maxlength="100" /></td>
										</tr>
										<tr>
											<td style="text-align:center; height:37px; border:0px;" colspan="2">
												<label>销售代表 <input type="checkbox" id="salesmanAdd" name="zhiwu" value="0" /></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<label>技术代表 <input type="checkbox" id="technicistAdd" name="zhiwu" value="0" /></label>
											</td>
										</tr>
									</table>
								</div>
								<div class="manage-page-item">
									<div style="margin-left:190px; width:300px; text-align:left; margin-top:15px;margin-bottom:5px;"><b>权限分配：</b></div>
									<table class="dclear" style="width:620px;margin-left:190px;" >
										<colgroup>
											<col width="150">
											<col width="140">
											<col width="160">
											<col width="*">
										</colgroup>
										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_2" name="permission" value="2" /> POPBE管理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_7" name="permission" value="7" /> BDCBE管理员 </label>
											</td>
											<td style="text-align:left; border:0px;" colspan="2" >
												&nbsp;
											</td>
										</tr>
										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_4" name="permission" value="4" /> 订单管理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_17" name="permission" value="17" /> 软件管理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_11" name="permission" value="11" /> 生产管理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_5" name="permission" value="5" /> 产品管理 </label>
											</td>
										</tr>
										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_13" name="permission" value="13" /> 软件开发 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_14" name="permission" value="14" /> 软件测试 </label>
											</td>
											<td style="text-align:left; border:0px;" colspan="2" >
												&nbsp;
											</td>
										</tr>
										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_8" name="permission" value="8" /> BIScanne操作员 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_10" name="permission" value="10" /> BATS操作员 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_6" name="permission" value="6" /> MIScanner操作员 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_9" name="permission" value="9" /> CCSpider操作员 </label>
											</td>
										</tr>

										<tr >
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_12" name="permission" value="12" /> 销售经理 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_15" name="permission" value="15" /> 销售代表 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_16" name="permission" value="16" /> 服务代表 </label>
											</td>
											<td style="text-align:left; border:0px;" >
												<label style="margin-right:25px;"><input type="checkbox" id="add_permission_18" name="permission" value="18" /> 订单设备管理 </label>
											</td>
										</tr>
									</table>
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