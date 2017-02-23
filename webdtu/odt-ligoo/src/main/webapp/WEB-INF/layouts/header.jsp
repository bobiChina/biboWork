<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" language="java"%>
<%@ page import="com.ligootech.webdtu.service.account.ShiroUser" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<div class="head-outer">
		<div class="head-wrap" style="width:1010px;">
			<h1 class="logo dleft"><img src="${ctx}/static/images/logo.gif" alt="" /></h1>
			<div class="head-nav dleft" style="width:510px;">
				<shiro:hasAnyRoles name="odtadmin,odtview">
					<div class="head-nav-item"  style="width:100px;">
						<a href="${ctx}/orderTrack/list" style="width:100px;" <c:if test="${currentMenu == 'orderTrack'}"> class="activenav" </c:if>>订单列表</a>
					</div>
				</shiro:hasAnyRoles>
				<shiro:hasRole name="service_man">
				<div class="head-nav-item"  style="width:100px;">
					<a href="${ctx}/orderService/list" style="width:100px;" <c:if test="${currentMenu == 'orderService'}"> class="activenav" </c:if>>订单列表</a>
				</div>
				</shiro:hasRole>
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
						<a href="javascript:void(0)"  class="user-login-name" style="width:110px;" title="<shiro:principal property="name"/>" id="a_username"> <shiro:principal property="name"/></a>
						<div class="tips-box-js-outer">
							<div class="tips-box-js-head"></div>
							<div class="tips-box-js-con">
								<ul>
									<li><a href="${ctx}/userinfo">资料修改</a></li>
									<li><a href="${ctx}/password">密码修改</a></li>
									<li style="display:none;"><a href="${ctx}/overview/start">前台切换</a></li>
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
