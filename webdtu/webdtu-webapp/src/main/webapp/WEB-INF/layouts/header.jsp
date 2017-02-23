<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" language="java"%>
<%@ page import="com.ligootech.webdtu.service.account.ShiroUser" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

	<div class="head-outer">
		<div class="head-wrap">
			<h1 class="logo dleft"><img src="${ctx}/static/images/logo.gif" alt="" /></h1>
			<div class="head-nav dleft">
				<div class="head-nav-item" style="width: 80px;">
					<a href="${ctx}/overview/start" <c:if test="${currentMenu == 'overview'}"> class="activenav" </c:if>>概览</a>
				</div>
				<div class="head-nav-item" style="width: 80px;">
					<a href="${ctx}/dtu/map" <c:if test="${currentMenu == 'map'}"> class="activenav" </c:if>>地图</a>
				</div>
				<div class="head-nav-item" style="width: 80px;">
					<a href="${ctx}/dtu/list" <c:if test="${currentMenu == 'dtus'}"> class="activenav" </c:if>>列表</a>
				</div>
				<div class="head-nav-item" style="width: 80px;">
					<a href="${ctx}/dtu/alarm" <c:if test="${currentMenu == 'alarm'}"> class="activenav" </c:if>>报警</a>
					<i id="alarmTips">New</i>
				</div>

				<div class="head-nav-item" style="width: 80px;">
					<a href="${ctx}/count/reports" <c:if test="${currentMenu == 'reports'}"> class="activenav" </c:if>>报表</a>
				</div>

				<div class="head-nav-item" style="width: 80px;">
					<a href="${ctx}/dtu/config" <c:if test="${currentMenu == 'config'}"> class="activenav" </c:if>>管理</a>
				</div>
			</div> <!-- //head nav end -->
			<style>
			#a_username{
                overflow:hidden;
                white-space:nowrap;
                text-overflow:ellipsis;
            }
			.user-login{ background: url('${ctx}/static/images/head-icon.gif') no-repeat 163px 30px; width: 210px; height: 81px; line-height: 65px; text-align: right;}
			</style>
			<div class="user-login dright">
				<div class="user-login-tips"><span class="dleft">欢迎您：</span>
					<div class="user-login-arrow dleft">
						<a href="javascript:void(0)"  class="user-login-name" style="width:110px;" title="<shiro:principal property="name"/>" id="a_username"><shiro:principal property="name"/></a>
						<div class="tips-box-js-outer">
							<div class="tips-box-js-head"></div>
							<div class="tips-box-js-con">
								<ul>
									<li><a href="${ctx}/userinfo">资料修改</a></li>
									<li><a href="${ctx}/password">密码修改</a></li>
									<li><a href="${ctx}/logout">注销</a></li>
								</ul>
							</div>
							<div class="tips-box-js-bootom"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div> <!-- //head end -->
	<input type="hidden" id="alarmId" value="0"/>
	<script type="text/javascript">
	$(function(){
		$("#alarmTips").css("display","none");
		<c:if test="${currentMenu != 'login'}"> 
		$.get("${ctx}/dtu/getMaxAlarmId", {},
			  function(data){
				$("#alarmId").val(data);
				setInterval(function(){
					$.get("${ctx}/dtu/getNewAlarmRealtime", { alarmId:$("#alarmId").val()},
							  function(data){
							    //alert("getNewAlarmRealtime: " + data);
							    //console.log(data);
							    if (data>0){
							    	$("#alarmTips").css("display","block");
							    	
							    }
					 });
				},30000);
		 });
		</c:if>
		
	});
	</script>