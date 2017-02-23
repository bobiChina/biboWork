<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="reports" />
<title>概览</title>
<link type="text/css" rel="stylesheet" href="${ctx}/static/styles/bootstrap.min.css"></link>
<script src="${ctx}/static/js/bootstrap.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="${ctx}/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	
   
   
});
function daypickedFunc(){
	$("#btnday").attr("href","/count/day/"+$("#d11").val());
}
function monthpickedFunc(){
	$("#btnmonth").attr("href","/count/month/"+$("#d12").val());
}
</script>
</head>
<body>	

<div  class="container">
	<div class="panel panel-default">
	  <div class="panel-heading">日报表生产</div>
	  <div id="container" class="panel-body">
	    	日报：<input id="d11" type="text" value="${yesterday}" onFocus="WdatePicker({onpicked:daypickedFunc,maxDate:'%y-%M-{%d-1}'})"/>
	    	<a id="btnday" href="${ctx}/count/day/${yesterday}" target="_blank">生成</a>
	  </div>
	</div>
	<div class="panel panel-default">
	  <div class="panel-heading">月报表生成</div>
	  <div id="container" class="panel-body">
	    	月报：<input id="d12" type="text" value="${lastMonth}" onfocus="WdatePicker({onpicked:monthpickedFunc,dateFmt:'yyyy-MM',maxDate:'%y-{%M-1}'})" />
	    	<a id="btnmonth" href="${ctx}/count/month/${lastMonth}" target="_blank">生成</a>
	  </div>
	</div>	
</div>
</body>
</html>