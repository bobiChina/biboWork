<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>资料修改</title>
<meta name="menu" content="password" />
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/style/jquery.mCustomScrollbar.min.css" />
	<script src="${ctx}/static/js/ligao.global.js"></script>
<script>
	$(document).ready(function() {
		$(".user-login-arrow").tipsbox({
			"tipsLeft":"-20px",
			"tipsTop":"20px"
		});
		
		$("#updateUser").click(
				function(){
					$.post("${ctx}/user/updateUser", {id:$("#userId").val(),email:$("#useremail").val(),relation:$("#relation").val(),fullName:$("#fullName").val()},function(data){
						if (data == 1){
							alert("修改成功");
						}
			        });	
		});		
	});
	
</script>
</head>

<body>
<div class="body-outer dclear clearfix">
	<input id="userId" type="hidden" value="${user.id}"/>
		<div class="table-box page-manage-box-outer dclear clearfix">
			<div class="table-body page-table-body">
				<div class="history-save-table-head">
					<div class="history-save-table-head-outer">
						<div class="profile-head-top">
							用户资料修改
						</div>
					</div>
				</div>

				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner manage-page-inner clearfix">
						<form action="">
						<div class="profile-list-item">
							<div class="profile-list-item-inner">
								<h3>公司信息</h3>
								<div class="profile-list-item-list">
									<span>名称:</span> <input type="text" value="${user.corp.corpName}" readonly="readonly"/>
								</div>

								<div class="profile-list-item-list">
									<span>地址:</span> <input type="text" value="${user.corp.addr}" readonly="readonly"/>
								</div>


								<div class="profile-list-item-list">
									<span>电话:</span> <input type="text" value="${user.corp.phone}" readonly="readonly"/>
								</div>


								<div class="profile-list-item-list">
									<span>邮箱:</span> <input type="text" value="${user.corp.email}" readonly="readonly"/>
								</div>
							</div>
						</div>

						<div class="profile-list-item">
							<div class="profile-list-item-inner">
								<h3>个人信息</h3>
								<div class="profile-list-item-list">
									<span>姓名:</span> <input type="text" id = "fullName" value="${user.fullName}" />
								</div>

								<div class="profile-list-item-list">
									<span>电话:</span> <input type="text" id = "relation" value="${user.relation}"/>
								</div>


								<div class="profile-list-item-list">
									<span>邮箱:</span> <input type="text" id = "useremail" value="${user.email}"/>
								</div>

							</div>
						</div>

						<div class="manage-page-submit-box profile-page-submit-box">
							<input type="button" id="updateUser" value=" " class="manage-page-submit-button" />
						</div>

						</form>
					</div>
				</div>
				
			</div>
		</div>
	</div> <!-- //body outer end -->
	
</body>
</html>