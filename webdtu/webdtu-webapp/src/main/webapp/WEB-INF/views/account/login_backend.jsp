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
<script>
	$(document).ready(function() {
		$("#username").focus();
		<c:if test="${not empty errorMsg}">
			showDialogAlert("${errorMsg}");
		</c:if>

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

<body id="login" class="sys_bg" style="*overflow:hidden;" onkeydown="keyDown(event);">
<div class="ligoo">
	<form id="loginForm" name="loginForm" action="${ctx}/private/login/loginOpt" method="post" class="form-signin" autocomplete="off">
		<input type="checkbox" name="rememberMe" value="true" style="display:none;" />
	    <div class="l_top">
	        <div class="left">
	           <div class="logo"></div>
	           <div class="text"><span>远程数据平台管理系统</span></div>
	       </div>
	       <div class="help" style="display:none;"></div>
	    </div>

	    <div class="mass">
	        <div class="cont">
	           <div class="comp_name">
	           		<img id="groupImg" src="${ctx}/static/images/ligoo_login.png" style="visibility: visible; width:174px;"/>
	           </div>
	           <div class="l_box">
	               <div class="term">
	                  <input type="text" id="username" name="username" value="${username}" placeholder="用户名" onkeypress="javascrip:if(window.event.keyCode==13){login();}" />
	                  <div class="icon2"></div>
	               </div>
	               <div class="term" style="border-bottom:none;">
	                  <input type="password"  id="password" name="password" value="" placeholder="密码" onkeypress="javascrip:if(window.event.keyCode==13){login();}"  />
	                  <div class="icon3"></div>
	               </div>
	           </div>
	           <div class="l_btn" onclick="login()">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</div>
	       </div>
	    </div>
	    <div class="footer">
	        <div class="text"><p><a href="http://www.ligoo.cn" target="_blank">安徽力高新能源技术有限公司版权所有</a>   |   ©2010-2014 LIGOO ALL RIGHTS RESERVED.   |   皖 ICP 10014803号</P></div>
	    </div>
    </form>
</div>
	 <!-- //body outer end -->
	
</body>
</html>