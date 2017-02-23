<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>上传成功返回</title>
<script type="text/javascript">
        try
        {
            //返回上传文件的路径
			<c:if test="${not empty backmethod_}">
				window.parent.${backmethod_}('${result.storeName}', '${result.realName}', '${result.errorMsg}');
			</c:if>
			<c:if test="${empty backmethod_}">
				alert("回调方法缺失");
			</c:if>
			// window.close(); //已被重新定向
            //关闭窗口

        }
        catch(e)
        {
        	alert("回调方法错误");
        	//window.close();
            //关闭窗口
        }
    </script>
</head>
<body>
	返回对象：${result}<br>
	错误信息：${result.errorMsg} <br/>
	原文件名：${result.realName} <br/>
	新文件名：${result.storeName} <br/>
	文件大小：${result.size} <br/>
	上传时间：${result.createTime}
</body>
</html>