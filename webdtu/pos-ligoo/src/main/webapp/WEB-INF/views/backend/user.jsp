<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="dtus"/>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<link rel="stylesheet" href="${ctx}/static/styles/page.css" />
	<title>用户管理</title>
	<script type="text/javascript">
	$(function(){
		
		$("#input-box-add").click(
				function (){
					window.location.href="${ctx}/manager/user/add";
				}		
			);
	    
		$(".search-input-text").inputTipsText();
		$(".th-citys").tipsbox();
		//用户登陆tips
		$(".user-login-arrow").tipsbox({
			"tipsLeft":"-20px",
			"tipsTop":"20px"
		});
		//setInterval(searchAjax,60000);
		
		/* $(".table-body").setThead({
			"setColumnsWidth":[46,100,100,160,160,260,90]
		});//{"setColumnsWidth":[163,172,160,112,128,112,113]}	 */
	});
	
	
	</script>	
	<style>
		.btn-sm {
		    padding: 5px 10px;
		    font-size: 12px;
		    line-height: 1.5;
		    border-radius: 3px;
		}
		.btn-success {
		    color: #fff;
		    background-color: #5cb85c;
		    border-color: #4cae4c;
		}
		.btn {
		    display: inline-block;
		    padding: 6px 12px;
		    margin-bottom: 0;
		    font-size: 14px;
		    font-weight: 400;
		    line-height: 1.42857143;
		    text-align: center;
		    white-space: nowrap;
		    vertical-align: middle;
		    cursor: pointer;
		    -webkit-user-select: none;
		    -moz-user-select: none;
		    -ms-user-select: none;
		    user-select: none;
		    background-image: none;
		    border: 1px solid transparent;
		    border-radius: 4px;
		}
	</style>
</head>
<body>

	<div class="body-outer dclear clearfix">
		<div class="form-box clearfix">
			<!-- <div class="input-box dleft">
				<input type="text"  name="uuid" id ="uuid" class="search-input-text" value="请输入UUID编号或车架号直接搜索"/>
			</div>
			<input type="button" class="dleft" id="input-box-submit" value="搜&nbsp;&nbsp;索" /> 
			<input type="button" class="dleft" id="input-box-add" value="添&nbsp;&nbsp;加" /> -->
			<a href="${ctx}/manager/user/add" class="btn btn-success btn-sm">添加</a>
		</div> <!-- //form end -->
		<input type="hidden" id="hidPageNumber" value="1"/>
		
		<div class="table-box dclear clearfix">
			<div class="table-body">
				<table id="container" class="dclear">
					<thead class="displavisibility">
						<tr>
							<th width="46" style="text-align: center;">序号</th>
							<th width="100" style="text-align: center;">用户名</th>
						    <th width="100" style="text-align: center;">姓名</th>
						    <th width="160" style="text-align: center;">email</th>
						    <th width="160" style="text-align: center;">联系电话</th>
						    <th width="260" style="text-align: center;">所属公司</th>
						    <th width="100" style="text-align: center;">删除</th>
					</thead>
					<tbody>
					<c:forEach var="user" items="${userList}" varStatus="status" >
				  		<tr>
				  			<td width="46"><c:out value="${status.count}"/></td>
				  			<td width="100">${user.userName}</td>
				  			<td width="100">${user.fullName}</td>
				  			<td width="160">${user.email}</td>
				  			<td width="160">${user.relation}</td>
				  			<td width="260">${user.corp.corpName}</td>
				  			<td width="100"><a href="${ctx}/manager/user/delete/${user.id}">删除</a></td>
				  		</tr>
				  	</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="table-footer dclear">
				<div id="page" style="float:right;margin-right:20px" class="yahoo">
				</div>
			</div>
			
		</div>
	</div> <!-- //body outer end -->

<script>
	$(function(){



	})
</script>
</body>
</html>





