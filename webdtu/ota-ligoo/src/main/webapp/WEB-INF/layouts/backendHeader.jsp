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
				<div class="head-nav-item"  style="width:100px;">
					<a href="${ctx}/otaTask/page" style="width:100px;" <c:if test="${currentMenu == 'task'}"> class="activenav" </c:if>>任务管理</a>
				</div>
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
