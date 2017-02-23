<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title>用户管理</title>
<meta name="menu" content="SystemManagementMenu" />
</head>

<body>

	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>
			${message}
		</div>
	</c:if>

	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<caption>用户列表</caption>
		<thead>
			<tr>
				<th>登录名</th>
				<th>用户名</th>
				<th>邮箱</th>
				<th>权限组
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${users}" var="user">
				<tr>
					<shiro:hasPermission name="users:edit">
						<td><a href="${ctx}/account/users/update/${user.id}" id="editLink-${user.loginName}">${user.loginName}</a></td>
					</shiro:hasPermission>
					<shiro:lacksPermission name="users:edit">
						<td>${user.loginName}</td>
					</shiro:lacksPermission>
					<td>${user.name}</td>
					<td>${user.email}</td>
					<td>${user.roleNames}</td>
					<td><shiro:hasPermission name="users:edit">
							<a href="${ctx}/account/users/delete/${user.id}">删除</a>
						</shiro:hasPermission></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

		<shiro:hasPermission name="users:edit">
			<div><a class="btn btn-default btn-primary" href="${ctx}/account/users/create">创建用户</a></div>
		</shiro:hasPermission>

</body>
</html>
