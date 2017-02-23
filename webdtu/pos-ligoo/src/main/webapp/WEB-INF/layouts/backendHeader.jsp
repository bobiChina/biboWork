<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" language="java"%>
<%@ page import="com.ligootech.webdtu.service.account.ShiroUser" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<div class="head-outer">
		<div class="head-wrap" style="width:1010px;">
			<h1 class="logo dleft"><img src="${ctx}/static/images/logo.gif" alt="" /></h1>
			<div class="head-nav dleft" style="width:510px;">
				<shiro:hasAnyRoles name="admin,order">
				<div class="head-nav-item"  style="width:100px;">
					<a href="${ctx}/order/page" style="width:100px;" <c:if test="${currentMenu == 'backend_order'}"> class="activenav" </c:if>>订单管理</a>
				</div>
				</shiro:hasAnyRoles>
				<shiro:hasAnyRoles name="admin,software,fw,sqa">
					<div class="head-nav-item"  style="width:100px;"><shiro:hasRole name="admin"><a href="${ctx}/order/orderSoftwarePage" style="width:100px;" <c:if test="${currentMenu == 'backend_software'}"> class="activenav" </c:if>>软件管理</a></shiro:hasRole>
					<shiro:lacksRole name="admin"><shiro:hasRole name="software"><a href="${ctx}/order/orderSoftwarePage" style="width:100px;" <c:if test="${currentMenu == 'backend_software'}"> class="activenav" </c:if>>软件管理</a></shiro:hasRole>
					<shiro:lacksRole name="software"><a href="${ctx}/order/orderLog" style="width:100px;" <c:if test="${currentMenu == 'backend_orderlog'}"> class="activenav" </c:if>>软件管理</a></shiro:lacksRole></shiro:lacksRole>
					</div>
				</shiro:hasAnyRoles>
				<shiro:hasAnyRoles name="admin,equipment">
				<div class="head-nav-item"  style="width:100px;">
					<a href="${ctx}/order/device" style="width:100px;" <c:if test="${currentMenu == 'backend_device'}"> class="activenav" </c:if>>生产管理</a>
				</div>
				</shiro:hasAnyRoles>
				<style>
					.tips-box-prod{
						position: absolute;
						top: 20px; left: 0;
						width: 100px;
						height: 82px;
						display: none;
						font-size: 14px;
						background: #e0e0e0;;
						z-index: 999999999;}
					.tips-box-prod ul li{ height: 40px; line-height: 40px; display: block; width: 100%; text-align: center; border-bottom: 1px solid #cccccc; cursor: pointer;}
					.tips-box-prod ul li.prod-lefthover{ background: #FFF;}
					.tips-box-prod ul li a{ font-size: 14px;height: 40px; line-height: 40px;}
				</style>
				<shiro:hasAnyRoles name="admin,product">
				<div class="head-nav-item" style="width:100px;" id="head-nav-item-id">
					<c:choose>
						<c:when test="${currentMenu == 'backend_product'}">
							<a href="${ctx}/product/page" style="width:100px;" class="activenav" >产品管理</a>
						</c:when>
						<c:when test="${currentMenu == 'backend_swPage'}">
							<a href="${ctx}/product/swPage" style="width:100px;" class="activenav">产品管理</a>
						</c:when>
						<c:otherwise>
							<a href="${ctx}/product/page" style="width:100px;" >产品管理</a>
						</c:otherwise>
					</c:choose>
					<div class="tips-box-prod clear">
						<ul>
							<li onmouseover='$(this).addClass("prod-lefthover")' onmouseout='$(this).removeClass("prod-lefthover")'><a href="${ctx}/product/page" style="width:100px;">硬件版本</a></li>
							<li onmouseover='$(this).addClass("prod-lefthover")' onmouseout='$(this).removeClass("prod-lefthover")'><a href="${ctx}/product/swPage" style="width:100px;">软件版本</a></li>
						</ul>
					</div>
				</div>
				</shiro:hasAnyRoles>
                <shiro:hasRole name="admin">
				<div class="head-nav-item"  style="width:100px;">
					<a href="${ctx}/permission/page" style="width:100px;" <c:if test="${currentMenu == 'backend_permission'}"> class="activenav" </c:if>>权限管理</a>
				</div>
				</shiro:hasRole >
			</div> <!-- //head nav end -->
			<style>
				.tips-box-js-outer{ position: absolute; top: 20px; left: 0; width: 118px; display: none; z-index: 999999999;}
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
						<a href="javascript:void(0)"  class="user-login-name" style="width:110px;" title="<shiro:principal property="name"/>" id="a_username"> <shiro:principal property="name"/></a>
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
<style>
	.handle{
		width:100px;
		position: absolute;
		right: 1px;
		margin-top: 70px;
		display: none;
		background-color:#ADABAC;
		font-size: 18px;
	}
</style>
<script language="JavaScript">
	$(document).ready(function(e) {
		if (window.history && window.history.pushState) {
			$(window).on('popstate', function () {
				window.history.pushState('forward', null, '#');
				window.history.forward(1);
			});
		}
		window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
		window.history.forward(1);
	});

</script>
	<script>
		$(function(){
			$("#head-nav-item-id").tipsbox4Prod();
		});
		(function($){
			/**提示 Tips**/
			$.fn.tipsbox4Prod= function(options){
				var defaults = {
					"tipsLeft":"0px",
					"tipsTop":"66px"
				}
				var options = $.extend(options, defaults);

				var _tipsElement = $(this);
				var _tipsElementBox = _tipsElement.find(".tips-box-prod");
				var _timeId;
				if(_tipsElementBox.length > 0){
					_tipsElement.on({
						"mouseenter": function(e){
							_tipsElementBox.css({
								"left":options.tipsLeft,
								"top":options.tipsTop
							});
							_tipsElementBox.fadeIn();
							$(".tips-box-prod").not(_tipsElementBox).css({
								"display":"none"
							});
						},

						"mouseleave":function(e){
							_tipsElementBox.fadeOut();
						}

					});

					_tipsElementBox.on({
						"mouseleave":function(e){
						}
					});
				}
			};
		})(jQuery);
		/* function showHeadMenu(obj, type){
			if(type == 1){
				$(obj).find('.handle').show();
			}else{
				$(obj).find('.handle').hide();
			}
		}

		$(function(){
			$(".head-nav-item").tipsbox({
				"tipsLeft":"-20px",
				"tipsTop":"20px"
			});
		})
		 //====== 用户信息tip
		 $(".user-login-arrow").tipsbox({
		 "tipsLeft":"-20px",
		 "tipsTop":"20px"
		 });


		 <div class="tips-box-js-outer">
				<div class="tips-box-js-con">
				<ul>
				<li><a href="${ctx}/userinfo">资料修改</a></li>
		<li><a href="${ctx}/password">密码修改</a></li>
		<li style="display:none;"><a href="${ctx}/overview/start">前台切换</a></li>
		<li><a href="${ctx}/logout">注销</a></li>
		</ul>
		</div>
		</div>*/

	</script>
