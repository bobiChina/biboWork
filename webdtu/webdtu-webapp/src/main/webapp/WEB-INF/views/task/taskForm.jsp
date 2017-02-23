<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>任务管理</title>

<script>
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#task_title").focus();
		//为inputForm注册validate函数
		$("#inputForm").validate();
	});
</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/tasks/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${task.id}" />
		<fieldset>
			<legend>
				<small>管理任务</small>
			</legend>
			<div class="form-group">
				<label for="task_title" class="control-label">任务名称:</label> 
				<input type="text" id="task_title" name="title"
					value="${task.title}" class="input-lg required form-control" minlength="3" />

			</div>
			<div class="form-group">
				<label for="description" class="control-label">任务描述:</label>
				<textarea id="description" name="description" class="input-lg form-control">${task.description}</textarea>
			</div>
			<div class="btn-group">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交" />&nbsp; 
				<input id="cancel_btn"
					class="btn btn-default" type="button" value="返回" onclick="history.back()" />
			</div>
		</fieldset>
	</form>
</body>
</html>
