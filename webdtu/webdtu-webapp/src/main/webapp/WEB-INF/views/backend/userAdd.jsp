<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>添加用户</title>
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
	});
	
	function savePassword(){
		if ($("#newPass").val() != $("#newPassRe").val()){
			alert("密码不一致")
			return;
		}
		if ($("#newPass").val().replace(/(^\s+)|(\s+$)/g, "")==""){
			alert("密码不能为空");
			return;
		}
			
		$.post("${ctx}/user/updatePassword", {userId:$("#userId").val(),newPass:$("#newPass").val().replace(/(^\s+)|(\s+$)/g, ""),oldPass:$("#oldPass").val()},function(data){
			if (data =="2"){
				alert("原密码错误");
				return;
			}
				
			alert("密码修改成功");
			$("#newPass").val("");
			$("#oldPass").val("");
			$("#newPassRe").val("");
			
	    });
	}	
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
							添加用户
						</div>
					</div>
				</div>

				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner manage-page-inner clearfix">
						<form action="">
						<div class="profile-list-item">
							<div class="profile-list-item-inner">
								<div class="profile-list-item-list">
									<span>用户名:</span> <input type="text" id="userName" name="userName" />
								</div>

								<div class="profile-list-item-list">
									<span>密码:</span> <input type="text" id="userPass" name="userPass" />
								</div>

								<div class="profile-list-item-list">
									<span>确认密码:</span> <input type="password" id = "newPassRe" name="newPassRe" />
								</div>
								
								<div class="profile-list-item-list">
									<span>姓名:</span> <input type="text" id="fullName" name="fullName"/>
								</div>
								
								<div class="profile-list-item-list">
									<span>邮箱:</span> <input type="text" id="email" name="email" />
								</div>
								
								<div class="profile-list-item-list">
									<span>电话:</span> <input type="text" id="relation" name="relation" />
								</div>
								
								<div class="profile-list-item-list">
									<span>公司:</span> <select name="corp.id" class="form-control input-sm">
											<c:forEach var="corp" items="${corps}">
												<option value="${corp.id}">${corp.corpName}</option>
											</c:forEach>
										</select>
								</div>
							</div>
						</div>

						<div class="manage-page-submit-box password-page-submit-box" style="padding-bottom:50px;">
							<input type="button" value=" " onclick="savePassword()" class="manage-page-submit-button" />
						</div>

						</form>
					</div>
				</div>
				
			</div>


			<div class="table-footer page-footer dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->
	
</body>
</html>