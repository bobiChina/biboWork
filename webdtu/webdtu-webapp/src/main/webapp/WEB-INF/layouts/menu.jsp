<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://struts-menu.sf.net/tag-el" prefix="menu" %>


<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="container">
<ul class="nav nav-pills" role="tablist">
  <li role="presentation" <c:if test="${currentMenu == 'overview'}"> class="active"</c:if>><a href="${ctx}/overview/start">概览</a></li>
  <li role="presentation" <c:if test="${currentMenu == 'map'}"> class="active"</c:if>><a href="${ctx}/dtu/map">地图</a></li>
  <li role="presentation" <c:if test="${currentMenu == 'dtus'}"> class="active"</c:if>><a href="${ctx}/dtu/list">DTU</a></li>
  <li role="presentation" <c:if test="${currentMenu == 'alarm'}"> class="active"</c:if>><a href="${ctx}/dtu/alarm">报警</a></li>
  <li role="presentation" <c:if test="${currentMenu == 'reports'}"> class="active"</c:if>><a href="${ctx}/count/reports">报表</a></li>
  <!-- 
  <li role="presentation" <c:if test="${currentMenu == 'count'}"> class="active"</c:if>><a href="${ctx}/count/day">日报</a></li>
  <li role="presentation" <c:if test="${currentMenu == 'monthcount'}"> class="active"</c:if>><a href="${ctx}/count/month">月报</a></li>
   -->
  <li role="presentation" <c:if test="${currentMenu == 'config'}"> class="active"</c:if>><a href="${ctx}/dtu/config">配置</a></li>
  <li role="presentation" <c:if test="${currentMenu == 'dtuuser'}"> class="active"</c:if>><a href="${ctx}/manager/user">用户</a></li>
  <li role="presentation" <c:if test="${currentMenu == 'dtumanage'}"> class="active"</c:if>><a href="${ctx}/manager/dtu">DTU管理</a></li>
</ul>
</div>