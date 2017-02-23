<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>资源上传</title>
</head>
<body>
<form enctype="multipart/form-data" id="form1" name="form1" action="<c:url value="/fileOperate/uploadFile" />" method="post">
	<input type="hidden" name="fileType" id="fileType" value="${fileType_ }" />
	<input type="hidden" name="backmethod" id="backmethod" value="${backmethod_ }" />
	<input type="file" name="upfile" id="upfile" /> <br />
	<input type="submit" id="submitBtu" name="submitBtu" value="上传" />
</form>
</body>

