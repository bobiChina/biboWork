<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="appdot" uri="http://www.appdot.org/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
	<title>权限管理</title>
	<meta name="menu" content="SystemManagementMenu" >
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
	<form:form id="inputForm" modelAttribute="role" action="${ctx}/account/roles/save/${role.id}" method="post"
		class="form-horizontal">
		<input type="hidden" name="id" value="${role.id}" />
		<fieldset class="prepend-top">
			<legend>
				<small>管理角色</small>
			</legend>
			<div class="form-group">
				<label for="name" class="control-label">名称:</label>
				<div class="controls">
					<input type="text" id="name" name="name" size="50" class="required" value="${role.name}" />
					<form:errors path="name" cssClass="error help-inline" />
				</div>
			</div>
			<div class="form-group">
				<label for="permissionList" class="control-label">权限列表:</label>
				<div class="controls">
					<appdot:checkboxes labelCssClass="checkbox-inline" cssClass="checkbox" cssErrorClass="error help-inline" path="permissionList" items="${allPermissions}" itemLabel="displayName" itemValue="value" />
				</div>
			</div>
		</fieldset>
		<div class="form-group">
			<input id="submit" class="btn btn-default btn-primary" type="submit" value="提交" />&nbsp; 
			<input id="cancel" class="btn btn-default" type="button" value="返回" onclick="history.back()" />
		</div>
	</form:form>
</body>

