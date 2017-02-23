<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>添加dtu</title>
<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
<link href="${ctx}/static/styles/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript">
$(function(){

   
    
   
});
</script>
</head>
<body>	

<div id="container" style="width:300px;"  class="container">
	<form action="${ctx}/manager/dtu/addpost" method="post">
		<ul class="list-group">
			<li class="list-group-item">uuid：<input type="text" name="uuid" class="form-control input-sm"/></li>
			<li class="list-group-item">所属用户：<select name="dtuUser.id" class="form-control input-sm">
				<c:forEach var="user" items="${userList}">
					<option value="${user.id}">${user.fullName}</option>
				</c:forEach>
			</select></li>
			<li class="list-group-item">电池型号：<select name="batteryModel.id" class="form-control input-sm">
				<c:forEach var="battery" items="${batteryList}">
					<option value="${battery.id}">${battery.batteryName}</option>
				</c:forEach>
			</select></li>			
		</ul>
		<input type="submit" value="保存" class="btn btn-success btn-sm"/>
	</form>
</div>

</body>
</html>