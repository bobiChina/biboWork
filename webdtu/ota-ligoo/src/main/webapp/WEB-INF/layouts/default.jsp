<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!--[if lt IE 7 ]> <html lang="zh" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="zh" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="zh" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="zh" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="zh" class="no-js"> <!--<![endif]-->
  <head>
	  <!–[if lte IE 8]>
	  <meta http-equiv="X-UA-Compatible" content="IE=8" />
	  <![endif]–>
	  <!–[if IE 9]>
	  <meta http-equiv="X-UA-Compatible" content="IE=9" />
	  <![endif]–>
	  <!--[if (gt IE 9)|!(IE)]><!--> <meta http-equiv="X-UA-Compatible" content="IE=9" /> <!--<![endif]-->
	  <title><decorator:title /></title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
	<t:assets />
	<decorator:head />
  </head>

<body <decorator:getProperty property="body.id" writeEntireProperty="true"/>
	<decorator:getProperty property="body.class" writeEntireProperty="true"/>>
	<c:set var="currentMenu" scope="request">
		<decorator:getProperty property="meta.menu" />
	</c:set>
	<c:set var="currentMenu2" value="${fn:substring(currentMenu, 0, 7)}" />
<c:choose>
   <c:when test="${currentMenu == 'login'}">
   </c:when>
   <%--<c:when test="${currentMenu2 == 'backend'}">
   		<%@ include file="/WEB-INF/layouts/backendHeader.jsp"%>
   </c:when>
   <c:otherwise>
   		<%@ include file="/WEB-INF/layouts/header.jsp"%>
   </c:otherwise>--%>
	<c:otherwise>
		<%@ include file="/WEB-INF/layouts/backendHeader.jsp"%>
	</c:otherwise>
</c:choose>

<div class="container-fluid">	

	<div class="row-fluid">
			<decorator:body />
	</div>
	<div class="row-fluid" style="height:60px;margin-top:20px">
		<c:choose>
           <c:when test="${currentMenu == 'login'}">
           </c:when>
           <c:otherwise>
           		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
           </c:otherwise>
        </c:choose>
	</div>	
</div>	
	<%=(request.getAttribute("scripts") != null) ? request.getAttribute("scripts") : ""%>
</body>
</html>