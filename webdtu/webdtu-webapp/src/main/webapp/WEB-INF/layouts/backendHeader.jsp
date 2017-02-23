<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" language="java"%>
<%@ page import="com.ligootech.webdtu.service.account.ShiroUser" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<div class="head-outer">
		<div class="head-wrap" style="width:1010px;">
			<h1 class="logo dleft"><img src="${ctx}/static/images/logo.gif" alt="" /></h1>
			<div class="head-nav dleft" style="width:500px;">
				<div class="head-nav-item"  style="width:120px;">
					<a href="${ctx}/customer/dtupage" style="width:120px;" <c:if test="${currentMenu == 'backend_dtu'}"> class="activenav" </c:if>>DTU管理</a>
				</div>
				<div class="head-nav-item"  style="width:120px;">
					<a href="${ctx}/simcard/page" style="width:120px;" <c:if test="${currentMenu == 'backend_simcard'}"> class="activenav" </c:if>>SIM卡管理</a>
				</div>
				<div class="head-nav-item"  style="width:100px;">
					<a href="${ctx}/product/page" style="width:100px;display:none;" <c:if test="${currentMenu == 'backend_product'}"> class="activenav" </c:if>>版本管理</a>
				</div>
				<div class="head-nav-item"  style="width:100px;display:none;">
					<a href="${ctx}/permission/page" style="width:100px;" <c:if test="${currentMenu == 'backend_permission'}"> class="activenav" </c:if>>账号管理</a>
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
						<a href="javascript:void(0)"  class="user-login-name" style="width:80px;" title="<shiro:principal property="name"/>" id="a_username"> <shiro:principal property="name"/></a>
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
