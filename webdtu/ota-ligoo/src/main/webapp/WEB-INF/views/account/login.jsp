<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE HTML>
<!--[if lt IE 7 ]> <html lang="zh" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="zh" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="zh" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="zh" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="zh" class="no-js"> <!--<![endif]-->
<head>
	<title>登录页</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">

	<meta name="menu" content="login" />
	<link rel="stylesheet" href="${ctx}/static/styles/login.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/doc.css" />

	<script src="${ctx}/static/js/jquery-1.11.1.min.js"></script>
	<script src="${ctx}/static/js/respond.min.js"></script>
	<script src="${ctx}/static/js/dialog-plus.js"></script>
	<script src="${ctx}/static/js/history.js"></script>

	<script language="JavaScript">
		$(document).ready(function(e) {
			if (window.history && window.history.pushState) {
				$(window).on('popstate', function () {
					window.history.pushState('forward', null, '#');
					window.history.forward(1);
				});
			}
			window.history.pushState('forward', null, '#');
			window.history.forward(1);
		});
	</script>

<script>
	$(document).ready(function() {
		$("#username").focus();

		<%
			String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
			String errorMsg = (String) request.getAttribute("errorMsg");
			if (error != null) {
		%>
			showDialogAlert("<%=errorMsg %>");
		<%
			}
		%>
	});

	function login(){
		if($.trim($("#username").val()) == ""){
			showDialogAlert("请填写账号");
		}else if($.trim($("#password").val()) == ""){
			showDialogAlert("请填写密码");
		}else{
			document.getElementById("loginForm").submit();
		}
	}

	//执行键盘按键命令
	function keyDown(e){
		if(navigator.userAgent.indexOf("Firefox")>0){
			if (e.which == 13 ) //回车键是13
			{
				login();
			}
		}
	}

</script>
</head>
<body class="login_bg" id="login" onkeydown="keyDown(event);">
<form id="loginForm" action="${ctx}/login" method="post" class="form-signin" autocomplete="off">
<div class="login_form">
	<div class="login_form_item">
    	<label for="username"><i class="login_icon_user"></i>登录账号<input type="text" class="login_input" id="username" name="username" value="${username}" placeholder="用户名" onkeypress="javascrip:if(window.event.keyCode==13){login();}" /></label>
    </div>
	<div class="login_form_item">
    	<label for="password"><i class="login_icon_pass"></i>登录密码<input type="password" class="login_input" id="password" name="password" placeholder="密码" onkeypress="javascrip:if(window.event.keyCode==13){login();}" /></label>
    </div>
    <div class="login_form_btn">
    	<a class="login_btn" href="javascript:void(0);" onclick="login()">登录</a>
    </div>
</div>
</form>
</body>
</html>