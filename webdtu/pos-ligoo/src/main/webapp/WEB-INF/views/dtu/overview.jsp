<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta name="menu" content="overview" />
<title>概览</title>
<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
<script src="${ctx}/static/js/charts/echarts-all.js"></script>
<script src="${ctx}/static/js/ligao.global.js"></script>
<script type="text/javascript">
$(function(){
	$(".user-login-arrow").tipsbox({
		"tipsLeft":"-20px",
		"tipsTop":"20px"
	});
});
</script>
</head>
<body>	
	<div class="body-outer dclear clearfix">
		<div class="table-box dclear clearfix">
			<div class="map-box-head">
				<h3 class="dleft">总览</h3>
			</div>


			<div class="overview-outer clearfix dclear">
				<div class="overview-content">
					<div class="overview-head">
						<h3><span class="overview-cart-title">车辆总数</span><span class="overview-cart-total">${allVehicleCount}辆</span></h3>
						<h3 class="overview-dright"><span class="overview-cart-title">总装车容量</span><span class="overview-cart-total">
							<c:if test="${empty totalCapacity}">
								0
							</c:if>
							<c:if test="${!empty totalCapacity}">
								<fmt:formatNumber value="${totalCapacity}" pattern="0.0"/>
							</c:if>	KWh		
						</span></h3>
					</div>
					<div class="overview-info overview-info-line dleft">
						<ul>
							<li><span class="overview-cart-title">总行驶里程（km）</span><span class="overview-cart-total">
							<c:if test="${empty allMilegeAndTimeLength[0]}">
								0
							</c:if>
							<c:if test="${!empty allMilegeAndTimeLength[0]}">
								<fmt:formatNumber value="${allMilegeAndTimeLength[0]}" pattern="0.00"/>
							</c:if>
							 </span></li>
							<li><span class="overview-cart-title">总运行时长（h）</span><span class="overview-cart-total">
							<c:if test="${empty allMilegeAndTimeLength[1]}">
								0
							</c:if>
							<c:if test="${!empty allMilegeAndTimeLength[1]}">
								<fmt:formatNumber value="${allMilegeAndTimeLength[1]/3600}" pattern="0.0"/>
							</c:if>							
							</span></li>
						</ul>
					</div>
					
					<div class="overview-info overview-dright dleft">
						<ul>
							<li><span class="overview-cart-title">最大单车行驶里程（km）</span><span class="overview-cart-total">
								<c:if test="${empty allMilegeAndTimeLength[2]}">
									0
								</c:if>
								<c:if test="${!empty allMilegeAndTimeLength[2]}">
									<fmt:formatNumber value="${allMilegeAndTimeLength[2]}" pattern="0.00"/>
								</c:if>								
							 </span></li>
							<li><span class="overview-cart-title">最大单车运行时长（h）</span><span class="overview-cart-total">
								<c:if test="${empty maxRunningTime}">
									0
								</c:if>
								<c:if test="${!empty maxRunningTime}">
									<fmt:formatNumber value="${maxRunningTime}" pattern="0.0"/>
								</c:if>								
							</span></li>
						</ul>
					</div>	
				</div>
			</div>

			<div class="overview-footer dclear"></div>
		</div> <!-- //总览 -->


		<div class="guide-cart-outer">
			<div class="guide-cart-box guide-list1 clearfix dleft">
				<div class="guide-cart-box-head">动力费用对比（万元）</div>
				<div class="guide-cart-box-con" id="btou3">
					<script>
						var myChart1 = echarts.init(document.getElementById('btou3')); 
						option = {
						    tooltip : {
						        trigger: 'axis'
						    },
						    calculable : false,
						    xAxis : [
						        {
						            type : 'category',
						            data : ['燃油车','新能源车']
						        }
						    ],
						    color:['#ff7f50', '#87cefa'],
						    yAxis : [
						        {
						            type : 'value'
						        }
						    ],
						    
						    series : [
						        {
						            name:'费用',
						            type:'bar',
						            data:${overviewQuot[0]},
						        }
						    ]
						};

						myChart1.setOption(option);
					</script>	
				</div>
				<div class="guide-cart-box-footer"></div>
			</div>


			<div class="guide-cart-box guide-list1 clearfix dleft">
				<div class="guide-cart-box-head">CO<font style="font-size:8px">2</font>排放量对比（吨）</div>
				<div class="guide-cart-box-con" id="btou4"> 
					<script>
						var myChart2 = echarts.init(document.getElementById('btou4')); 
						option = {
						    tooltip : {
						        trigger: 'axis'
						    },
						    calculable : false,
						    xAxis : [
						        {
						            type : 'category',
						            data : ['燃油车','新能源车']
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value'
						        }
						    ],
						    color:['#ff7f50', '#87cefa'],
						    series : [
						        {
						            name:'排放量',
						            type:'bar',
						            data:${overviewQuot[1]}
						        }			        
						    ]
						};

						myChart2.setOption(option);
					</script>
				</div>
				<div class="guide-cart-box-footer"></div>
			</div>
				

			<div class="guide-cart-box guide-list1 clearfix dleft">
				<div class="guide-cart-box-head">砍伐树木消耗对比（棵）</div>
				<div class="guide-cart-box-con" id="btou5">
					<script>
						var myChart3 = echarts.init(document.getElementById('btou5')); 
						option = {
						    tooltip : {
						        trigger: 'axis'
						    },
						    calculable : false,
						    xAxis : [
						        {
						            type : 'category',
						            data : ['燃油车','新能源车']
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value'
						        }
						    ],
						    color:['#ff7f50', '#87cefa'],
						    series : [
						        {
						            name:'减少砍伐',
						            type:'bar',
						            data:${overviewQuot[2]}
						        }			        
						    ]
						};

						myChart3.setOption(option);
					</script>
				</div>
				<div class="guide-cart-box-footer"></div>
			</div>

			
			<div class="guide-cart-box guide-list2 clearfix dleft">
				<div class="guide-cart-box-head">车辆类型饼状图</div>
				<div class="guide-cart-box-con" id="btou"> 
					<script>
						var myChart4 = echarts.init(document.getElementById('btou')); 
						option = {
						    tooltip : {
						        trigger: 'item',
						        formatter: "{a} <br/>{b} : {c} ({d}%)"
						    },
						    calculable : false,
						    series : [
						        {
						            name:'车辆类型',
						            type:'pie',
						            radius : ['50px', '90px'],
						            data:${vehicleType}
						        }
						    ]
						};

						myChart4.setOption(option); 
					</script>
				</div>
				<div class="guide-cart-box-footer"></div>
			</div>


			<div class="guide-cart-box guide-list3 clearfix dleft">
				<div class="guide-cart-box-head">车辆状态饼状图</div>
				<div class="guide-cart-box-con" id="btou2"> 
					<script>
						var myChart5 = echarts.init(document.getElementById('btou2')); 
						option = {
						    tooltip : {
						        trigger: 'item',
						        formatter: "{a} <br/>{b} : {c} ({d}%)"
						    },
						    calculable : false,
						    series : [
						        {
						            name:'状态',
						            type:'pie',
						            radius : ['50px', '90px'],
						            data:${onlineStatus}
						        }
						    ]
						};

						myChart5.setOption(option); 
					</script>
				</div>
				<div class="guide-cart-box-footer"></div>
			</div>

		</div>

	</div> <!-- //body outer end -->
</body>
</html>