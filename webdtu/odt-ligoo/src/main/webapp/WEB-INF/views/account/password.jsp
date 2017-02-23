<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>密码修改</title>
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
							修改密码
						</div>
					</div>
				</div>

				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner manage-page-inner clearfix">
						<form action="">
						<div class="profile-list-item">
							<div class="profile-list-item-inner">
								<div class="profile-list-item-list">
									<span>旧密码:</span> <input type="password" id = "oldPass" />
								</div>

								<div class="profile-list-item-list">
									<span>新密码:</span> <input type="password" id = "newPass" name="newPass" />
								</div>


								<div class="profile-list-item-list">
									<span>确认密码:</span> <input type="password" id = "newPassRe"/>
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