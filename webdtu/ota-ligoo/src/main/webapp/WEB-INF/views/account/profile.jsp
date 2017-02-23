<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>资料修改</title>

<script>
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#name").focus();
		//为inputForm注册validate函数
		$("#inputForm").validate();
	});
</script>
</head>

<body>
<div class="col-sm-5">
	<form id="inputForm" action="${ctx}/profile" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${user.id}" />
		<fieldset>
			<legend>资料修改</legend>
			<div class="form-group">
				<label for="name" class="control-label">用户名:</label> <input type="text" id="name" name="name" value="${user.name}"
					class="form-control input-md required" />
			</div>
			<div class="form-group">
				<label for="plainPassword" class="control-label">密码:</label> <input type="password" id="plainPassword"
					name="plainPassword" class="form-control input-md" placeholder="...不修改密码无需填写" />

			</div>
			<div class="form-group">
				<label for="confirmPassword" class="control-label">确认密码:</label> <input type="password" id="confirmPassword"
					name="confirmPassword" class="form-control input-md" equalTo="#plainPassword" />
			</div>
			<div class="form-group">
				<input id="submit_btn" class="btn btn-default btn-primary" type="submit" value="提交" />&nbsp; <input id="cancel_btn"
					class="btn btn-default" type="button" value="返回" onclick="history.back()" />
			</div>
		</fieldset>
	</form>
</div>
</body>
</html>
