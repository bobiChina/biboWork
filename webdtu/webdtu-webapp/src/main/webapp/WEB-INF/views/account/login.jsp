<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>登录页</title>
<meta name="menu" content="login" />
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/doc.css" />
	<style>
    	body{background:#e0e0e0;}
    </style>
<script>
	$(document).ready(function() {
		$("#username").focus();
		$("#loginForm").validate();

		<c:if test="${not empty errorMsg}">
			showDialogAlert("${errorMsg}");
		</c:if>
	});
</script>

</head>

<body id="login">
	<div class="head-outer">
		<div class="head-wrap">
			<h1 class="logo dleft"><img src="${ctx}/static/images/logo.gif" alt="" /></h1>
			<div class="head-nav dleft">
				<div class="head-nav-item" style="width: 80px;">
					<a href="#">概览</a>
				</div>
				<div class="head-nav-item" style="width: 80px;">
					<a href="#">地图</a>
				</div>
				<div class="head-nav-item" style="width: 80px;">
					<a href="#">列表</a>
				</div>
				<div class="head-nav-item" style="width: 80px;">
					<a href="#">报警</a>
				</div>

				<div class="head-nav-item" style="width: 80px;">
					<a href="#">报表</a>
				</div>

				<div class="head-nav-item" style="width: 80px;">
					<a href="#">管理</a>
				</div>
			</div> <!-- //head nav end -->

			<div class="user-login-main dright">
				<div class="user-login-tips">欢迎您
					<div class="user-login-arrow">
						<a href="#"> <shiro:principal property="name"/></a>
						<div class="tips-box-js-outer">
							<div class="tips-box-js-head"></div>
							<div class="tips-box-js-con">
								<ul>
									<li><a href="#">资料修改</a></li>
									<li><a href="#">密码修改</a></li>
									<li><a href="#">注销</a></li>
								</ul>
							</div>
							<div class="tips-box-js-bootom"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div> <!-- //head end -->
	
	<div class="body-outer login-outer dclear clearfix">
		<div class="login-outer-wrap">
			<div class="login-outer-box">
				<h3><img src="${ctx}/static/images/login-box-title.png" alt="" /></h3>
				<form id="loginForm" action="${ctx}/login" method="post" class="form-signin" autocomplete="off">
					<div class="login-outer-box-wrap">
			<%
				String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
				if (error != null) {
			%>
			<div class="alert alert-danger fade in" style="color:#fff">
				<button class="close" data-dismiss="alert">×</button>
				登录失败，请重试.
			</div>
			<%
				}
			%>					
						<h4>登陆远程数据平台</h4>
						<p>请输入用户名或邮箱</p>
						<input type="text" id="username" name="username" value="${username}" class="login-outer-input"  required tabindex="1" />
						<p>请输入密码</p>
						<input type="password"  id="password" name="password" class="login-outer-input" required tabindex="2" />
						<div class="login-outer-tips">
							<span><input type="checkbox" name="rememberMe" value="true"/><label for="login-remember">30日内免登录</label></span>
						</div>						
						<div class="login-outer-submit">
							<input type="submit" id="submit_btn" value=" " class="login-outer-submit-button" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div> <!-- //body outer end -->
	
</body>
</html>