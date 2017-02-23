<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_product"/>
	<title>版本管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/easyui.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/icon.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/css4easyui.css" />
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<script src="${ctx}/static/js/jquery.easyui.min.js"></script>
	<script src="${ctx}/static/js/easyui-lang-zh_CN.js"></script>
	<script src="${ctx}/static/js/jquery.jdirk.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.all.min.js"></script>

	<style>
		.tabs-title {
			font-size: 14px;
		}
	.tabs-header, .tabs-scroller-left, .tabs-scroller-right, .tabs-tool, .tabs, .tabs-panels, .tabs li a.tabs-inner, .tabs li.tabs-selected a.tabs-inner, .tabs-header-bottom .tabs li.tabs-selected a.tabs-inner, .tabs-header-left .tabs li.tabs-selected a.tabs-inner, .tabs-header-right .tabs li.tabs-selected a.tabs-inner {
		/*border-color: #95B8E7;*/
		border-color: #E0E0E0;/* 边框颜色 */
	}
	.icon-standard-application-form{background:url('${ctx}/static/styles/icons/application-form.png') no-repeat center center;}

	#tab1.panel-body {
			overflow: hidden;
			border-top-width: 0;
			padding: 0;
	}
	#tab2.panel-body {
			overflow: hidden;
			border-top-width: 0;
			padding: 0;
	}
	.tabs-wrap {
		position: relative;
		left: 0;
		overflow: hidden;
		width: 100%;
		margin: 0;
		padding: 0;
		background-color: #E0E0E0;
	}
	.tabs {
		list-style-type: none;
		height: 26px;
		margin: 0px;
		padding: 0px;
		width: 5000px;

		border-width: 0;
	}

	.tabs-header {
		border-width: 0;
		position: relative;
		padding: 0;

		overflow: hidden;
	}
	.tabs-panels {
		margin: 0px;
		padding: 0px;
		border-width: 0;
		overflow: hidden;
		background-color: #E0E0E0;
	}

		.easyui-panel.manage-input-text {
			background: #E0E0E0;
			padding-left: 5px;
			width: 146px;
			height: 18px;
			line-height: 18px;
			border: none;
			margin-top: 2px;
		}
     </style>


<script type="text/javascript">
	$(function(){
		//====== 用户信息tip
		$(".user-login-arrow").tipsbox({
			"tipsLeft":"-20px",
			"tipsTop":"20px"
		});

		var t = $("#t").tabs({
			width: 1023,
			height: 740,
			lineHeight: 0,
			enableConextMenu: false
		});
	});




</script>
</head>
<body>
<input type="hidden" id="selProductId" name="selProductId" value="" />
	<div class="body-outer dclear clearfix" style="width:1023px;">
		<div id="t">
			<div id="tab1" data-options="title: '硬件版本',iniframe: true, href: '${ctx}/product/page', refreshable: false" style="background-color: #E0E0E0;"></div>
			<div id="tab2" data-options="title: '软件版本',iniframe: true, href: '${ctx}/product/swPage', refreshable: false"></div>
		</div>
	</div>
</body>
</html>