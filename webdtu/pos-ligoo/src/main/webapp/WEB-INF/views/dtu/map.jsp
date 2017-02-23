<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="map"/>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript" charset="UTF-8"></script>
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=CF9649ad8e4abb469672be973645361c"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer.js"></script>
	<title>地图</title>
	<style type="text/css">  
	   .anchorBL{  
	       display:none;  
	   }  
	 </style> 
<script type="text/javascript">
$(function(){
		var markers = [];
		var list = ${dtuList};
		// 百度地图API功能
		var map = new BMap.Map("allmap");
		map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
		map.enableContinuousZoom(); 
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT,offset: new BMap.Size(10, 50)}));  //右上角，仅包含平移和缩放按钮
		map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_RIGHT,offset: new BMap.Size(10, 25)}));
		//map.addControl(new BMap.OverviewMapControl({isOpen:true, anchor: BMAP_ANCHOR_BOTTOM_LEFT}));   //右上角，打开
		var point;
		if (list.length >0)
			point = new BMap.Point(list[0].lontitude,list[0].latitude);
		else
			point = new BMap.Point(116.404, 39.915);
		
		for(var i=0;i<list.length;i++){
			if ((list[i].lontitude !=0)||(list[i].latitude!=0)){
				point = new BMap.Point(list[i].lontitude,list[i].latitude);
				break;
			}
		}
		point = new BMap.Point(107.95000,36.26667);
		map.centerAndZoom(point, 5);
		
		
		//var bounds = map.getBounds();

		for (var i = 0; i <list.length; i ++) {
			
		  if ((list[i].lontitude == 0)&&(list[i].latitude == 0))
			  continue;
		  var point = new BMap.Point(list[i].lontitude,list[i].latitude);
	  
		  var sContent =
				"<div class=\"bus-stop-see-info\">"												+
					"<div class=\"see-stop-info-con\">"											+
						"<ul>"																	+
							"<li><a style='color:#96cdcd;' target='_blank' href='${ctx}/dtu/track/"+list[i].uuid+"'>轨迹查询</a></li>"											+
							"<hr/>"																	+
							"<li><label>UUID：</label><a style='color:#96cdcd;' target='_blank' href='${ctx}/dtu/get/"+list[i].uuid+"'>"+list[i].uuid+"</a></li>"			+
							"<li><label>车架号  :</label><a style='color:#96cdcd;' target='_blank' href='${ctx}/dtu/get/"+list[i].uuid+"'>"+list[i].vehicleNumber+"</a></li>"	+
							"<li><label>SOC : </label><span>"+list[i].soc+"%</span></li>";
							if (list[i].chargeStatus ==0)
								sContent += "<li><label>车辆状态 :</label><span>放电</span></li>";
							else if(list[i].chargeStatus ==1)	
								sContent += "<li><label>车辆状态 :</label><span>充电</span></li>";
							else
								sContent += "<li><label>车辆状态 :</label><span>离线</span></li>";
							if (list[i].alarmStatus ==1)
								sContent += "<li><label>报警状态:</label><span>报警</span></li>";
							else	
								sContent += "<li><label>报警状态 :</label><span>正常</span></li>";
									
							sContent +="<li><label>车辆类型:</label><span>"+list[i].typeName+"</span></li>";
							//sContent +="<li><label>SIM卡号   :</label><span>"+list[i].simCard+"</span></li>";
							sContent +="<li><label>电池型号 :</label><span>"+list[i].facName+"</span></li>";
							sContent +="</ul></div></div>"		
							
	 	  var iopts = {
	 				  width : 220,     // 信息窗口宽度
	 				  height: 185,     // 信息窗口高度
	 				  enableMessage:false,//设置允许信息窗发送短息
	 	  }	 	  
		  var infoWindow = new BMap.InfoWindow(sContent,iopts);	
		  
	 		
		  var opts = {
				  position : point,    // 指定文本标注所在的地理位置
				  offset   : new BMap.Size(33, 25)    //设置文本偏移量
		  }
		  var uuid = list[i].uuid.substr(list[i].uuid.length-6,6);
		  var label = new BMap.Label(uuid, opts);  // 创建文本标注对象
			label.setStyle({
				 color : "#ffffff",
				 fontSize : "12px",
				 height:"20px",
				 maxWidth:"200px",
				 lineHeight : "20px",
				 backgroundColor:"#cf2235",
				 borderColor:"#cf2235",
				 fontFamily:"微软雅黑"
			 });
		  
		  var typename;
		  var vstatus;
		  if (list[i].typeName == '新能源客车')
			  typename = 'bus';
		  else if (list[i].typeName == '低速微型车'){
			  typename = 'jeep';
		  }
		  else if (list[i].typeName == '乘用车'){
			  typename = 'car';
		  }
		  else if (list[i].typeName == '其他'){
	  		  typename = 'train';
		  }else{//添加其他类型车辆的默认图标 wly 2015年9月12日10:24:11
			  typename = 'train';
		  }
		  
		  if (list[i].chargeStatus == 2) {//离线
			  vstatus = 'gray';
		  }else{
			  if (list[i].alarmStatus == 1)
				  vstatus = 'red';
			  else{
				  if (list[i].chargeStatus == 0){
					  vstatus = 'blue';
				  }else if (list[i].chargeStatus == 1)
					  vstatus = 'green';
			  }
		  }
		  var imagename = typename + '-' + vstatus + '.png'
		  //console.log(imagename);
		  addMarkerAndInfoWindow(point,infoWindow,label,imagename);
		  
		  //var markerClusterer = new BMapLib.MarkerClusterer(map, {markers:markers});
		}
		
		function addMarkerAndInfoWindow(point,infoWindow,label,imagename){

			  var myIcon = new BMap.Icon("${ctx}/static/images/"+imagename, new BMap.Size(42,32));
			
			  var marker = new BMap.Marker(point,{icon:myIcon});
			  //map.addOverlay(marker);
			  //marker.setLabel(label);
			  //map.addOverlay(label);
			  marker.addEventListener("click", function(){          
				   setMapBg();
				   marker.openInfoWindow(infoWindow);
			  });
			  markers.push(marker);
		}
		
		var markerClusterer = new BMapLib.MarkerClusterer(map, {markers:markers});
		setMapBg();
		function setMapBg(){
			jQuery(".BMap_pop div").css({
				"backgroundColor":"#4d4c50"
			});


			jQuery(".BMap_pop img").each(function(){
				if($(this).attr("src") == "http://api0.map.bdimg.com/images/iw3.png"){
					$(this).attr("src","${ctx}/static/images/iw3.png");
				}
			});
		}		
		$(".user-login-arrow").tipsbox({
			"tipsLeft":"-20px",
			"tipsTop":"20px"
		});	
		
		$().tipsInitStop();
		
		
});


</script>
</head>
<body>	
<div class="container">

	<div class="body-outer dclear clearfix">
		<div class="map-outer">
			<div class="map-box-head">
				<h3 class="dleft">车辆地图位置 <a href="${ctx}/dtu/list" style="margin-left: 15px; color: #FFF; font-size: 12px; display: none;">返回</a></h3>
				<h3 class="dright map-box-head-screen">全屏</h3>
			</div>
			<div class="map-wrap dclear">
				<div id="allmap" class="map-img" style="height:600px"></div>
				<div class="dianchi-footer dianchi-footer-position" id="dianchi-footer"></div>
			</div>
		</div>
	</div> 
</div>

<style>
	.BMap_pop .BMap_top{ background:#4d4c50; }
	.BMap_pop .BMap_center{background:#4d4c50; }
	.BMap_pop .BMap_bottom{background:#4d4c50;}
</style>
</body>
</html>