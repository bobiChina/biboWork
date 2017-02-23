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
<script>
	$(document).ready(function() {
		$("#username").focus();
		$("#loginForm").validate();
	});
</script>
</head>

<body id="login">
	<div style="width:320px;margin:0 auto;">
		<form id="loginForm" action="${ctx}/login" method="post" class="form-signin" autocomplete="off">
			<h2 class="form-signin-heading">登录</h2>
			<%
				String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
				if (error != null) {
			%>
			<div class="alert alert-danger fade in">
				<button class="close" data-dismiss="alert">×</button>
				登录失败，请重试.
			</div>
			<%
				}
			%>
			<input type="text" id="username" name="username" value="${username}" class="form-control" placeholder="用户名" required tabindex="1" /> 
			<br/>
			<input type="password" id="password" name="password" class="form-control" required placeholder="密码" tabindex="2" /> 
			<br/>
			<input id="submit_btn" class="btn btn-lg btn-primary btn-block" type="submit" value="登录" tabindex="4" /> 
		</form>
	</div>
</body>
</html>