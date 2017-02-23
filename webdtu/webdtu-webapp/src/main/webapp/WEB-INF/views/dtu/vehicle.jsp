<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>客户门店</title>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/bootstrap.min.css"></link>
<script src="${ctx}/static/js/bootstrap.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/js/highcharts.js" type="text/javascript" charset="UTF-8"></script>
<script type="text/javascript">
$(function(){

  
   
});
</script>
</head>
<body>	

<div id="container" style="width:800px;">
	<c:forEach items="${alarmHistory}" var="item">
	${item[0]}
	<br/>
	${item[1]}
	<br/>
	</c:forEach>
</div>


</body>
</html>