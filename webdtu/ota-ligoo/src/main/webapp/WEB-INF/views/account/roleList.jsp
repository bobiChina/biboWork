<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title>帐号管理</title>
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
		<caption>角色列表</caption>
		<thead>
			<tr>
				<th>名称</th>
				<th>授权</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${roles}" var="role">
				<tr>
					<td>${role.name}</td>
					<td>${role.permissionNames}</td>
					<td><shiro:hasPermission name="roles:edit">
							<a href="${ctx}/account/roles/update/${role.id}" id="editLink-${role.name}">修改</a>
							<a href="${ctx}/account/roles/delete/${role.id}">删除</a>
						</shiro:hasPermission></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<shiro:hasPermission name="roles:edit">
		<div><a class="btn btn-default btn-primary" href="${ctx}/account/roles/create">创建角色</a></div>
	</shiro:hasPermission>
</body>
