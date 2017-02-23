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
.manage-input-text {
	 background: #E0E0E0;
	 padding-left: 5px;
	 width: 206px;
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
});

function updateUser(){
	if ($("#fullName").val() == ""){
		showDialogAlert("请填写用户名");
		return;
	}

	if ($("#corpName").val() == ""){
		showDialogAlert("请填写公司名称");
		return;
	}

	$.post("${ctx}/dtu/updateUser", $("#userEditForm").serialize(),function(data){
		//showDialogAlert("保存成功");
		//转到显示页面
		findUserLi();
    });
}

function addUser(){
	if ($("#userNameAdd").val() == "" || $("#userNameAdd").val() == "输入字母或者数字"){
		showDialogAlert("请填写用户账号");
		return;
	}
	if ($("#userPassAdd").val() == "" || $("#userPassAdd").val() == "输入字母或者数字"){
		showDialogAlert("请填写用户密码");
		return;
	}
	$("#fullNameAdd").val($("#userNameAdd").val());
	if ($("#fullNameAdd").val() == ""){
			showDialogAlert("请填写用户名");
			return;
		}

	if ($("#corpcorpNameAdd").val() == ""){
		showDialogAlert("请填写公司名称");
		return;
	}

	$.post("${ctx}/dtu/addUser", $("#userAddForm").serialize(),function(data){
		if(data == "-1"){
			showDialogAlert("用户账号已存在,重新填写");
			return;
		}else if(data == "isme"){
			showDialogAlert("不能添加自己为子客户");
            findUserLi();
		}else if(data == "backendUser"){
			showDialogAlert("后台用户不能添加");
            findUserLi();
		}
		else if(window.parseInt(data) > 1){//添加成功时
			$("#selUserId").val(data);
            findUserLi();
		}else{//返回添加的对象，弹出提示框，是否继续添加
			var userObj = eval('(' + data + ')');
			if(userObj.userName == null || userObj.userName == undefined || userObj.userName == ""){
				showDialogAlert("操作失败");
			}else{
				//赋值
				$("#userName_dialog").html(userObj.userName);
				$("#userPwd_dialog").html("******");
				$("#corpName_dialog").html(userObj.corp.corpName);
				$("#corpPhone_dialog").html(userObj.corp.phone == null || userObj.corp.phone=="" ? "-" : userObj.corp.phone);
				$("#corpEmail_dialog").html(userObj.corp.email == null || userObj.corp.email=="" ? "-" : userObj.corp.email);
				$("#corpAddr_dialog").html(userObj.corp.addr == null || userObj.corp.addr=="" ? "-" : userObj.corp.addr);
				var elem = document.getElementById('userInfoDialog');
                var dAdd = dialog({
                	title: '添加已有客户',
                	height:310,
                	content: elem ,
                	zIndex: 999999,
                	padding: 3,
                	button: [
                			{
                				value: '继续添加',
                				callback: function () {
                					$("#userIdAdd").val(userObj.id);
									$.post("${ctx}/dtu/addUser", $("#userAddForm").serialize(),function(dataSub){
										if(dataSub == "-1"){
											showDialogAlert("用户账号已存在,重新填写");
											return;
										}else if(dataSub == "isme"){
											showDialogAlert("不能添加自己为子客户");
											findUserLi();
										}else{
											$("#selUserId").val(dataSub);
											findUserLi();
										}
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
                dAdd.width(760);
                dAdd.showModal();
			}
		}
    });
}

function userIdchange(id, type){
	$("#selUserId").val(id);
	$("#userdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-leftactive");
		if($(this).attr("userLIData") == id){
			$(this).addClass("manage-leftactive");
		}
    });
	if(type == 1){
		$("#edit_button").css("display", "block");
	}else{
		$("#edit_button").css("display", "none");
		<c:if test="${user.isAdmin == 1}">
			$("#edit_button").css("display", "block");
		</c:if>
	}
	$.get("${ctx}/dtu/getuser", {userid:id},function(data){
		var result = eval(data);
		var userObj = result[0];
		$("#userName").val(userObj.userName);
		$("#userNameText").html(userObj.userName);
		$("#userName_show").html(userObj.userName);
		if(type == 1){
			$("#userPwd_show").html(userObj.userPass);
		}
		else{
			$("#userPwd_show").html("******");
			<c:if test="${user.isAdmin == 1}">
				$("#userPwd_show").html(userObj.userPass);
			</c:if>
		}
		$("#userPassEdit").val(userObj.userPass);

		$("#isAdminEdit").val(userObj.isAdmin);
		$("#fullNameEdit").val(userObj.fullName);


		$("#relationEdit").val(userObj.relation);

		$("#emailEdit").val(userObj.email);

		$("#corpNameEdit").val(userObj.corp.corpName);
		$("#corpName_show").html(userObj.corp.corpName);

		$("#corpPhoneEdit").val(userObj.corp.phone);
		$("#corpPhone_show").html(userObj.corp.phone == null || userObj.corp.phone=="" ? "-" : userObj.corp.phone);

		$("#corpEmailEdit").val(userObj.corp.email);
		$("#corpEmail_show").html(userObj.corp.email == null || userObj.corp.email=="" ? "-" : userObj.corp.email);

		$("#corpAddrEdit").val(userObj.corp.addr);
		$("#corpAddr_show").html(userObj.corp.addr == null || userObj.corp.addr=="" ? "-" : userObj.corp.addr);

		$("#corpId").val(userObj.corp.id);
		$("#id").val(userObj.id);

		//原公司信息
		var oldStr = "公司ID：" + userObj.corp.id + "，名称：" + userObj.corp.corpName + ",电话：" + userObj.corp.phone + "，邮箱：" + userObj.corp.email + "，地址：" + userObj.corp.addr;
		$("#corpOldInfo").val(oldStr);

		//原账户信息
		var accountInfoStr = "账号：" + userObj.userName +  "，密码：" + userObj.userPass ;
		$("#accountInfo").val(accountInfoStr);
		//原联系人信息
		var linkInfoStr = "联系人：" + userObj.fullName + "，电话：" + userObj.relation + "，邮箱：" + userObj.email  ;
		$("#linkInfo").val(linkInfoStr);

	    //清除原来信息
	    $("#dtuList_body").html("");
	    findCurUserDtu(id);
	});
	toBackPage();
}

function findCurUserDtu(id){
	$("#userDtu_old").val("");
	$("#dtuList_body").html("")
	$.get("${ctx}/dtu/getUserDtuList", {userid:id},function(data){
		var tr = "";
		var oldStr = "";
    	var result = eval(data);
		$.each(result, function(index, items){
			tr += "<tr style='position:relative;' onmouseover='showDelIcon_dtu(this, 1)' onmouseout='showDelIcon_dtu(this, 0)'>";
			tr += "<td style='width:50px;'>" + (index + 1) + "</td>";
			tr += "<td style='width:170px;' nowrap='nowrap'>" + (items[1]==null || items[1]=="null" ? "" : items[1]) + "</td>";
			tr += "<td style='width:170px;' nowrap='nowrap'>" + (items[2]==null ? "" : items[2]) + "</td>";
			tr += "<td style='width:155px;' nowrap='nowrap'>" + (items[3]==null ? "" : items[3]) + "</td>";
			tr += "<td style='width:210px;' nowrap='nowrap'>" + (items[4]==null ? "" : items[4]) ;
			tr += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			tr += "<img onclick='delDtuConfigFun(\"" + (items[1]==null || items[1]=="null" ? "" : items[1]) + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			tr += "</div>";
			tr += "</td>";
			tr += "</tr>";
			oldStr += (items[1]==null || items[1]=="null" ? "" : items[1]) + ",";
		});
		$("#dtuList_body").html(tr);
		//原用户所属DTU
		$("#userDtu_old").val(oldStr);
    });

    findUserDtuAll();
}


function findUserDtuAll(){

	var id = $("#selUserId").val();
	var uuid = $.trim($("#searchuuid").val());
	if(uuid == "输入UUID或车架号搜索"){
		uuid = "";
	}

	$.get("${ctx}/dtu/getUserDtuAll", {userid:id, uuid:uuid , isUse:99 },function(data){
		var tr = "";
    	var result = eval(data);
		$.each(result, function(index, items){
			tr += "<tr>";
			if(0 == items[5]){
				tr += "<td style='width:50px;border: 1px solid #cccccc;'><input type='checkbox' name='selDtuObj' value='" + items[1] + "' /></td>";
			}else{
				tr += "<td style='width:50px;border: 1px solid #cccccc;'><input type='checkbox' name='selDtuObj' value='" + items[1] + "' checked /></td>";
			}
			tr += "<td style='width:170px;border: 1px solid #cccccc;' nowrap='nowrap'>" + (items[1]==null || items[1]=="null" ? "" : items[1]) + "</td>";
			tr += "<td style='width:170px;border: 1px solid #cccccc;' nowrap='nowrap'>" + (items[2]==null ? "" : items[2]) + "</td>";
			tr += "<td style='width:155px;border: 1px solid #cccccc;' nowrap='nowrap'>" + (items[3]==null ? "" : items[3]) + "</td>";
			tr += "<td style='width:210px;border: 1px solid #cccccc;' nowrap='nowrap'>" + (items[4]==null ? "" : items[4]) + "</td>";
			tr += "</tr>";
		});
		$("#dtuList_body_all").html(tr);
    });
}

function toEditPage(){
	$("#userInfoShow").css("display", "none");
	$("#userAddForm").css("display", "none");
	$("#userEditForm").css("display", "block");
}

function toDtuAllPage(){
	//$("#userInfoShow").css("display", "none");
	//$("#userDtuAllDiv").css("display", "block");
	toBackPage();
	var elem = document.getElementById('userDtuAllDiv');

	var d = dialog({
	title: '添加设备',
	height:500,
	content: elem ,
	zIndex: 999999,
	padding: 3,
	button: [
			{
				value: '确定',
				callback: function () {
					saveUserDtu();
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
d.width(760);
d.showModal();

}

function toAddPage(){
	$("#userInfoShow").css("display", "none");
	$("#userEditForm").css("display", "none");
	$("#userDtuAllDiv").css("display", "none");
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

function delUserFun(id, corpName){
	if($("#dtuList_body").html() != ""){
		showDialogAlert("用户已绑定设备，不能删除");
		return;
	}
	var d = dialog({
		title: '系统提示',
		content: '<div style="text-align:center;width:98%">是否确认删除客户<br>' + corpName + ' ？</div>',
		zIndex: 999998,
		button: [
				{
					value: '确定',
					callback: function () {
						$.post("${ctx}/dtu/delUser", {userId : id},function(data){
							if(data == "haveDtu"){
								showDialogAlert("用户已绑定设备，不能删除");
							}else{
								$("#selUserId").val("");
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
		$(obj).addClass("manage-lefthover");
		if($(obj).attr("userLIData") == $("#selUserId").val()){//当前行为选中行时出现删除按钮
			$(obj).find('.handle').show();
		}
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
	$.get("${ctx}/dtu/getUserLi", {userId: userId, usertype:0},function(data){
		var li = "";
		var result = eval(data);
		$.each(result, function(index, items){
			li += "<li userLIData='" + items[0] + "' style='position:relative;' onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)'";
			if(selId == ""){
				if(index == 0){
					li += " class='manage-leftactive' ";
					userIdchange(items[0], items[3]);
				}
			}else{
				if(selId == items[0]){
					li += " class='manage-leftactive' ";
                    userIdchange(items[0], items[3]);
				}
			}
			li += " >";
			li += "<a href='javascript:;' onclick='userIdchange(" + items[0] + ", " + items[3] + ")'>" + items[1] ;
			li += "</a>";
      	   <c:if test="${user.isAdmin == 1}">
			if(items[3] > 0){
				li += "&nbsp;&nbsp;<font color='red' style='cursor:pointer;' title='子客户数' onclick='findUserLi(" + items[0] + ")'>(" + items[4] + ")</font>";
			}
			</c:if>
			li += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			li += "<img onclick='delUserFun(" + items[0] + ",\"" + items[1] + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			li += "</div>";
			li += "</li>";
		});
		$("#userdata").html(li);
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

	$.post("${ctx}/dtu/searchInfoByNname", {userName : userName},function(data){
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


function saveUserDtu(){
	if($("#selUserId").val() == ""){
		showDialogAlert("未选择客户");
		return;
	}

	var selDtus = "";
	$("#dtuList_body_all").find("input").each(function(i, em){
		if($(this).prop('checked')){
			selDtus += $(this).val() + ","
		}
	});
	$.post("${ctx}/dtu/saveUserDtu", {userId: $("#selUserId").val(), seldtus : selDtus, userDtu_old : $("#userDtu_old").val()},function(data){
		findCurUserDtu($("#selUserId").val());
		toBackPage();
	});
}

function delDtuConfigFun(selUuid){
	var d = dialog({
    		title: '系统提示',
    		content: '是否删除该用户所属设备？',
    		zIndex: 999999,
    		button: [
    				{
    					value: '确定',
    					callback: function () {
    						$.post("${ctx}/dtu/delUserDtu", {userId: $("#selUserId").val(), selUuid : selUuid },function(data){
                            		findCurUserDtu($("#selUserId").val());
                            		toBackPage();
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

</script>
</head>
<body>	
<input type="hidden" id="selUserId" name="selUserId" value="" />
<input type="hidden" id="userDtu_old" name="userDtu_old" value="" />
<div class="body-outer dclear clearfix">
	<div class="form-box clearfix" style="padding-left:5px;">
		<a href="${ctx}/dtu/config" class="btn btn-success btn-sm" style="background-color: #DBDBE7; color:#000;">设备管理</a>
		&nbsp;
		<a href="javascript:;" class="btn btn-success btn-sm" style="background-color: #C7C5C6; color:#000">客户管理</a>
	</div>
		<div class="table-box page-manage-box-outer dclear clearfix">
			<div class="table-body page-table-body">
				<div class="history-save-table-head">
					<div class="history-save-table-head-outer">
						<div class="manage-page-head-left dleft" style="cursor: pointer; width:230px;">
							<div class="dleft" style="width:180px; " onclick='findUserLi(-999)'><a href="javascript:void(0)" style="color:#FFF; font-size:14px;">客户账号</a></div>
							<div class="dright" style="width:24px;display:none;" onclick='toAddPage()' ><img src="${ctx}/static/images/add.png" style="width:20px;margin-top:12px;" title="添加客户" alt="添加客户" /></div>
						</div>

						<div class="manage-page-head-right dright" style="color:#FFF; font-size:14px;width:710px;">
							<div class="dleft" style="width:630px;">	客户信息</div>
							<div class="dright" style="width:80px; margin-top:10px;display:none;">
								<img src="${ctx}/static/images/edit.png" style="width:24px;cursor: pointer;" title="修改客户信息" alt="修改客户信息" onclick="toEditPage()" />
								<img src="${ctx}/static/images/dtuadd.png" style="width:24px;cursor: pointer; margin-left:8px;" title="添加设备" alt="添加设备" onclick="toDtuAllPage()" />
							</div>
						</div>
					</div>
				</div>
				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner manage-page-inner clearfix" style="margin-bottom: 0px !important;">
						<div class="dleft">
							<div class="manage-page-left" style="height:621px;margin-bottom: 0px !important; width:200px;">
								<ul id="userdata">
									<c:forEach items="${userList}" var="item" varStatus="status">
										<li userLIData="${item[0]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;' onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' onclick="">
											<a href="javascript:;" onclick="userIdchange(${item[0]}, ${item[3]})">${item[1]}</a><c:if test="${user.isAdmin == 1}"><c:if test="${item[4] > 0 }">&nbsp;&nbsp;<font color='red' style='cursor:pointer;' title="子客户数" onclick='findUserLi(${item[0]})'>(${item[4]})</font></c:if></c:if>
											<div class="handle" style="position: absolute; right: 5px; margin-top: -30px; display: none;">
											<img onclick="delUserFun('${item[0]}','${item[1]}')" src="${ctx}/static/images/del.png" style="position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; ">
											</div>
											<c:if test="${status.count == 1}">
											 <script type="text/javascript">
												$(function (){
													userIdchange(${item[0]}, ${item[3]});
												});
											 </script>
											 </c:if>
										 </li>
									</c:forEach>
								</ul>
							</div>
							<div style="background:#E0E0E0; width:200px; height:50px;text-align:center;padding-top:5px;">
								<a href="javascript:void(0)" class="pill button" style="padding-bottom:5px !important;"  onclick="toAddPage()"><strong>&nbsp;&nbsp;创建新客户&nbsp;&nbsp;</strong></a>
							</div>
						</div>
						<div class="manage-page-right dright" id="userInfoShow" style="margin-bottom: 0px !important;">
							<div class="manage-page-item">
								<div class="manage-page-item-inner clearfix">
									<div class="manage-page-right dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;">
										<div style="width:760px; text-align:left; margin-top:15px;height:25px;">
											<b style="margin-left:20px;">账户信息</b>
											<div class="dright" style="padding-right:20px;" id="edit_button">
												<a href="javascript:void(0)" class="button" onclick="toEditPage()"><strong>编辑</strong></a>
											</div>
										</div>
										<table class="dclear" style="width:760px;" >
											<tr>
												<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
												<td style="text-align:left; width:230px; border:0px; height:37px;" id="userName_show"></td>
												<td style="text-align:right; width:110px; border:0px;">用户密码：</td>
												<td style="text-align:left;border:0px;" id="userPwd_show"></td>
											</tr>

										</table>
									</div>
									<div class="manage-page-right dright" style="width:760px;">
										<div style="padding-left:20px; width:710px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>基本信息</b></div>
										<table class="dclear" style="width:760px;" >
											<tr>
												<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
												<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpName_show"> </td>
												<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
                                                <td style="text-align:left; border:0px; height:37px;" id="corpPhone_show"> </td>
											</tr>
											<tr>
												<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
                                                <td style="text-align:left; width:230px; border:0px; height:37px;" id="corpAddr_show"> </td>
												<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
												<td style="text-align:left; border:0px; height:37px;" id="corpEmail_show"> </td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							<div style="width:760px; text-align:left; height:30px; margin-top:8px;">
							<b style="margin-left:20px;margin-top:20px;">设备信息</b>
								<a href="javascript:void(0)" class="button" style="margin-left:600px;"  onclick="toDtuAllPage()"><strong>添加设备</strong></a>
							</div>
							<div style="margin-top:1px; width:760px;">
								<div class="table-body2" >
									<table class="dclear" >
										<tr class="trHead">
											<th style="width:50px;">序号</th>
											<th style="width:170px;">UUID</th>
											<th style="width:170px;">车架号</th>
											<th style="width:155px;">车辆类型</th>
											<th style="width:210px;" nowrap="nowrap">电池厂商</th>
										</tr>
									</table>
								</div>
							</div>
							<div class="manage-page-left dleft" style="margin-top:0px; margin-bottom: 0px !important;height:280px; width:760px; background-color:#FFF;">
								<div class="table-body2" >
									<table class="dclear" >
										<tbody id="dtuList_body" valign="top">
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<!-- 添加设备 -->
						<div class="manage-page-right dright" id="userDtuAllDiv" style="display:none; background-color:#FFF;">
							<div class="manage-page-item">
								<div class="manage-page-item-inner clearfix" style="padding-top: 1px;padding-bottom:1px;background-color:#FFF;">
									<div class="manage-page-list" style="width:710px;text-align:left; height:38px;line-height: 38px; padding-left:25px;">
											<input type="text" id="searchuuid" name="searchuuid" class="manage-input-text showTip" maxlength="10" value="输入UUID或车架号搜索" style="width:205px;" />
											<a href="javascript:void(0)" class="l-btn" onclick="findUserDtuAll()"><span class="l-btn-left"><span class="l-btn-text">&nbsp;查 询&nbsp;</span></span></a>&nbsp;
									</div>
								</div>
								<div style="margin-top:5px; height:45px; width:760px;">
									<div class="table-body2" >
										<table class="dclear" >
												<tr class="trHead">
													<th style="width:50px;">&nbsp;</th>
													<th style="width:170px;">UUID</th>
													<th style="width:170px;">车架号</th>
													<th style="width:155px;">车辆类型</th>
													<th style="width:210px;" nowrap="nowrap">电池厂商</th>
												</tr>
										</table>
									</div>
								</div>
								<div class="manage-page-left dleft" style="margin-top:-5px; height:390px; width:760px;background-color:#FFF;">
									<div class="table-body2" >
										<table class="dclear" >
											<tbody id="dtuList_body_all" valign="top">
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						<!-- 添加设备 -->

						<form id="userEditForm" action="javascript:;" style="display: none;">
							<input type="hidden" id="corpId" name="corp.id" value="-1"/>
							<input type="hidden" id="userName" name="userName" value=""/>
							<input type="hidden" id="id" name="id" value="" />
							<input type="hidden" id="isAdminEdit" name="isAdmin" value="" />
							<input type="hidden" id="corpOldInfo" name="corpOldInfo" value=""/>
							<input type="hidden" id="linkInfo" name="linkInfo" value=""/>
							<input type="hidden" id="accountInfo" name="accountInfo" value=""/>

							<input type="hidden" id="fullNameEdit" name="fullName" class="manage-input-text" maxlength="10" />
							<input type="hidden" id="relationEdit" name="relation" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
							<input type="hidden" id="emailEdit" name="email" class="manage-input-text" maxlength="100" />

							<div class="manage-page-right dright">
								<div class="manage-page-item">
									<div class="manage-page-item-inner clearfix">
										<div class="manage-page-right dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;width:760px;">
											<div style="width:760px; text-align:left; margin-top:15px;height:25px;">
												<b style="margin-left:20px;">账户信息</b>
											</div>
											<table class="dclear" style="width:760px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;" id="userNameText"></td>
													<td style="text-align:right; width:110px; border:0px;">用户密码：</td>
													<td style="text-align:left;border:0px;">
														<input type="text" id="userPassEdit" name="userPass" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="10" value="输入字母或者数字" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g,''))" /><font color="red">*</font>
													</td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right dright">
											<div style="padding-left:20px; width:710px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>基本信息</b></div>
											<table class="dclear" style="width:760px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpNameEdit" name="corp.corpName" class="manage-input-text" maxlength="20" /><font color="red">*</font>
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="corpPhoneEdit" name="corp.phone" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpAddrEdit" name="corp.addr" class="manage-input-text" maxlength="100" />
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="corpEmailEdit" name="corp.email" class="manage-input-text" maxlength="50" />
													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
								<div style="width: 760px; margin-top: 10px;" align="center" >
									<input type="button" value=" " onclick="return updateUser()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>

						<form id="userAddForm" action="javascript:;" style="display: none;">
							<input type="hidden" id="userIdAdd" name="userIdAdd" value=""/>
							<input type="hidden" id="isAdminAdd" name="isAdmin" value="0"/>
							<input type="hidden" id="corpIdAdd" name="corp.id" value=""/>

							<input type="hidden" id="fullNameAdd" name="fullName" class="manage-input-text" maxlength="10" />
							<input type="hidden" id="relationAdd" name="relation" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
							<input type="hidden" id="emailAdd" name="email" class="manage-input-text" maxlength="100" />

							<div class="manage-page-right dright">
								<div class="manage-page-item" >
									<div class="manage-page-item-inner clearfix">
										<div class="manage-page-right dright" style="border-bottom:1px solid #ccc;padding-bottom:5px;">
											<div style="width:760px; text-align:left; margin-top:15px;height:25px;">
												<b style="margin-left:20px;">账户信息</b>
											</div>
											<table class="dclear" style="width:760px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="userNameAdd" name="userName" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="10" value="输入字母或者数字" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g,''))" /><font color="red">*</font>
													</td>
													<td style="text-align:right; width:110px; border:0px;">用户密码：</td>
													<td style="text-align:left;border:0px;">
														<input type="text" id="userPassAdd" name="userPass" style="color:#9E9E9E;" class="manage-input-text showTip" maxlength="10" value="输入字母或者数字" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text', clipboardData.getData('text').replace(/[^\d]/g,''))" /><font color="red">*</font>
													</td>
												</tr>
											</table>
										</div>
										<div class="manage-page-right dright" style="width:760px;">
											<div style="padding-left:20px; width:760px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>基本信息</b></div>
											<table class="dclear" style="width:760px;" >
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpcorpNameAdd" name="corp.corpName" class="manage-input-text" maxlength="20" /><font color="red">*</font>
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="corpphoneAdd" name="corp.phone" class="manage-input-text" maxlength="13" onKeyPress="if(event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
													</td>
												</tr>
												<tr>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
													<td style="text-align:left; width:230px; border:0px; height:37px;">
														<input type="text" id="corpaddrAdd" name="corp.addr" class="manage-input-text" maxlength="100" />
													</td>
													<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
													<td style="text-align:left; border:0px; height:37px;">
														<input type="text" id="corpemailAdd" name="corp.email" class="manage-input-text" maxlength="50" />
													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
								<div style="width: 760px; margin-top: 10px;" align="center" >
									<input type="button" value=" " onclick="return addUser()" class="manage-page-submit-button2">
								</div>
							</div>
						</form>
						<div class="manage-page-right" id="userInfoDialog" style="margin-bottom: 0px !important; display:none;">
							<div class="manage-page-item">
								<div class="manage-page-item-inner clearfix">
									<div class="manage-page-right" style="border-bottom:1px solid #ccc;padding-bottom:5px;">
										<div style="width:760px; text-align:left; margin-top:15px;height:25px;">
											<b style="margin-left:20px;">账户信息</b>
										</div>
										<table class="dclear" style="width:760px;" >
											<tr>
												<td style="text-align:right; width:110px; border:0px; height:37px;">用户账号：</td>
												<td style="text-align:left; width:230px; border:0px; height:37px;" id="userName_dialog"></td>
												<td style="text-align:right; width:110px; border:0px;">用户密码：</td>
												<td style="text-align:left;border:0px;" id="userPwd_dialog"></td>
											</tr>

										</table>
									</div>
									<div class="manage-page-right" style="width:760px;">
										<div style="padding-left:20px; width:710px; text-align:left; margin-top:15px;margin-bottom:10px;"><b>基本信息</b></div>
										<table class="dclear" style="width:760px;" >
											<tr>
												<td style="text-align:right; width:110px; border:0px; height:37px;">公司名称：</td>
												<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpName_dialog"> </td>
												<td style="text-align:right; width:110px; border:0px; height:37px;">公司电话：</td>
												<td style="text-align:left; border:0px; height:37px;" id="corpPhone_dialog"> </td>
											</tr>
											<tr>
												<td style="text-align:right; width:110px; border:0px; height:37px;">公司地址：</td>
												<td style="text-align:left; width:230px; border:0px; height:37px;" id="corpAddr_dialog"> </td>
												<td style="text-align:right; width:110px; border:0px; height:37px;">公司邮箱：</td>
												<td style="text-align:left; border:0px; height:37px;" id="corpEmail_dialog"> </td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							<div class="manage-page-right" style="width:760px;">
								<div style="padding-left:20px; width:710px; text-align:left; margin-top:15px;">
									<font color="red">注：用户账号已使用，是否确认添加？点击“继续添加”完成添加操作。</font>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="table-footer page-footer dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->
</body>
</html>