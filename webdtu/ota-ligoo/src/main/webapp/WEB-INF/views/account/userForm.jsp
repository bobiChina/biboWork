<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="appdot" uri="http://www.appdot.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title>用户管理</title>
<meta name="menu" content="SystemManagementMenu">
<script>
	$(document)
			.ready(
					function() {

						//聚焦第一个输入框
						$("#loginName").focus();

						//为inputForm注册validate函数
						$("#inputForm")
								.validate(
										{
											rules : {
												loginName : {
													remote : "${ctx}/account/users/checkLoginName?oldLoginName="
															+ encodeURIComponent('${user.loginName}')
												},
												name : "required",
												password : "required",

												roleList : "required"
											},
											messages : {
												loginName : {
													remote : "用户登录名已存在"
												},
												passwordConfirm : {
													equalTo : "输入与上面相同的密码"
												}
											},
										});
					});
</script>
</head>

<body>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/account/users/save/${user.id}" method="post"
		class="form-horizontal">
		<input type="hidden" name="id" value="${user.id}" />
		<fieldset class="prepend-top">

			<legend>
				<small>管理用户</small>
			</legend>

			<div id="messageBox" class="alert alert-danger" style="display: none">输入有误，请先更正。</div>

			<div class="form-group">
				<appdot:label key="user.loginName" styleClass="control-label" value="登录名:" />
				<input type="text" id="loginName" name="loginName"
					size="50" value="${user.loginName}" class="form-control" />
				<form:errors path="loginName" cssClass="danger help-inline" />
			</div>
			<div class="form-group">
				<appdot:label styleClass="control-label" key="user.name" value="用户名:" />
				<input type="text" id="name" name="name" size="50" value="${user.name}" class="form-control" />
				<form:errors path="name" cssClass="danger help-inline" />
			</div>
			<div class="form-group">
				<label for="password" class="control-label">密码:</label> 
				<input type="password" id="password" name="password"
					size="50" value="${user.password}" class="form-control" />
			</div>
			<div class="form-group">
				<label for="passwordConfirm" class="control-label">确认密码:</label> 
				<input type="password" id="passwordConfirm"
					name="passwordConfirm" size="50" value="${user.password}" equalTo="#password" class="form-control"/>
			</div>
			<div class="form-group">
				<label for="email" class="control-label">邮箱:</label>
				<div class="controls">
					<input type="text" id="email" name="email" size="50" value="${user.email}" class="email" />
				</div>
			</div>
			<div class="form-group">
				<label for="roleList" class="control-label">角色:</label>
				<appdot:checkboxes labelCssClass="checkbox-inline" cssClass="checkbox" path="roleList" items="${allRoles}"
					itemLabel="name" itemValue="id" />
			</div>
		</fieldset>
		<div class="form-group">
			<input id="submit_btn" class="btn btn-default btn-primary" type="submit" value="提交" />&nbsp; <input id="cancel"
				class="btn btn-default" type="button" value="返回" onclick="history.back()" />
		</div>
	</form:form>
</body>
</html>
