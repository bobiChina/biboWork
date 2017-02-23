<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="reports" />
<title>报表</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<script src="${ctx}/static/js/ligao.global.js"></script>
<script type="text/javascript">
$(function(){
	$(".calendar").calendar({
		dayreport:"${ctx}/count/day/",
		monthreport:"${ctx}/count/month/"
	});
	$(".user-login-arrow").tipsbox({
		"tipsLeft":"-20px",
		"tipsTop":"20px"
	});
});

</script>
</head>
<body>	
	<div class="body-outer dclear clearfix">
		
		<div class="calendar">

		</div>
	
	</div> <!-- //body outer end -->

</body>
</html>