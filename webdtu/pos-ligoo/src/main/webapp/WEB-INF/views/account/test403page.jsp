<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>添加用户</title>
<meta name="menu" content="test403page" />
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<script src="${ctx}/static/js/ligao.global.js"></script>
<script>
	$(document).ready(function() {
		$(".user-login-arrow").tipsbox({
			"tipsLeft":"-20px",
			"tipsTop":"20px"
		});
	});
</script>
</head>

<body>
	<div class="body-outer dclear clearfix">
		<div class="table-box page-manage-box-outer dclear clearfix">
			<div class="table-body page-table-body">
				<div class="history-save-table-head">
					<div class="history-save-table-head-outer">
						<div class="profile-head-top">
							系统提示
						</div>
					</div>
				</div>

				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner manage-page-inner clearfix" style="height:300px;font-size:18px; text-align:center;padding-top:20px;">
						权限不足，拒绝访问。
					</div>
				</div>
				
			</div>


			<div class="table-footer page-footer dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->
	
</body>
</html>