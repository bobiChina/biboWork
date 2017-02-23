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
	<script src="${ctx}/static/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<title>轨迹</title>
	<style type="text/css">  
	   .anchorBL{  
	       display:none;  
	   }  
	 </style> 
<script type="text/javascript">
var isPause = false;//暂停标志位
var isRunning = false;//停止标志位
$(function(){
		// 百度地图API功能
		var map = new BMap.Map("allmap");
		map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
		map.enableContinuousZoom(); 
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT,offset: new BMap.Size(10, 50)}));  //右上角，仅包含平移和缩放按钮
		map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_RIGHT,offset: new BMap.Size(10, 25)}));
	
		var point = new BMap.Point(107.95000,36.26667);
		map.centerAndZoom(point, 5);
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
		
		$().tipsInitStop();		

		
		$("#timeSelect").change(function(){
			if ($("#timeSelect").val() == 0){
				$("#startDiv").css("display","block");
				$("#endDiv").css("display","block");
				getDate(0);
			}else{
				$("#startDiv").css("display","block");
				$("#endDiv").css("display","block");
				getDate(parseInt($("#timeSelect").val()));
			}
		})
		
		$("#startBtn").click(function(){
			
			if ($("#startBtn").val() == "开始"){
				$("#startBtn").val("暂停");
				isPause = false;
			}else if ($("#startBtn").val() == "暂停"){
				$("#startBtn").val("开始");
				isPause = true;
			}
			
			if (isRunning == true)
				return;
				
			diff = getDateBetween($("#startDT").val(),$("#endDT").val());
			if (diff>3){
				alert("开始时间和结束时间相差不能超过3天");
				return;
			}
			map.clearOverlays();
			$("#waitTip").show();
			$.get("${ctx}/dtu/getTracks", { uuid: "${uuid}",startDT: $("#startDT").val(),endDT:$("#endDT").val() },
					  function(data){
						isRunning = true;
						$("#waitTip").hide();
					    var arr = data;
					    var json = eval('(' + arr + ')');
					    var pntArr = new Array();
						var startPnt;
						var endPnt;
					    for (var i = 0; i < json.length; i++) {
					    	var point = new BMap.Point(json[i].x,json[i].y);
							if (i==0)
								startPnt = point;
							if (i==json.length-1)
								endPnt = point;
							pntArr.push(point);
					    }
					    //创建开始和结束节点
					    var startIcon = new BMap.Icon("${ctx}/static/images/trackStart.png", new BMap.Size(50,50));
					    var startMark = new BMap.Marker(startPnt,{icon:startIcon});
						map.addOverlay(startMark); 
						 var endIcon = new BMap.Icon("${ctx}/static/images/trackEnd.png", new BMap.Size(50,50));
					    var endMark = new BMap.Marker(endPnt,{icon:endIcon});
						map.addOverlay(endMark); 
						
					    map.centerAndZoom(startPnt,15); 
						var polyline = new BMap.Polyline(pntArr, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5}); 
						map.addOverlay(polyline);      
						var carMk = new BMap.Marker(startPnt);
						map.addOverlay(carMk); 
						var isPlay = true;
						function resetMkPoint(i){
							if (isPause == true){
								resetMkPoint(i);
							}
								
							map.setCenter(pntArr[i]);
							carMk.setPosition(pntArr[i]);
							if (isPlay){
								if(i < pntArr.length){
									setTimeout(function(){
										i++;
										
										resetMkPoint(i);
									},$("#playRate").val());
								}else{
									isRunning = false;
								}
						    }
						}
						setTimeout(function(){
							resetMkPoint(5);
						},$("#playRate").val())
			});
		})
});



function getDateBetween(startDate,endDate){
	var d1 = new Date(startDate+":00");
	var d2 = new Date(endDate+":00");
	diff = parseFloat(Math.abs(d2 - d1) / 1000 / 60 / 60 / 24) ;
	return diff;
}

function getDate(hour){
	var dd = new Date();
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    var h = dd.getHours();
    var mm = dd.getMinutes();
    m = m > 9 ? m : "0" + m;
    d = d > 9 ? d : "0" + d;
    mm = mm > 9 ? mm : "0" + mm;
    h = h > 9 ? h : "0" + h;
    var endDate =  y + "-" + m + "-" + d + " "+ h + ":" + mm;
    $("#endDT").val(endDate);
    
	var dd2 = new Date(dd.getTime() - 1000*60*60*hour);
    var y2 = dd2.getFullYear();
    var m2 = dd2.getMonth()+1;//获取当前月份的日期
    var d2 = dd2.getDate();
    var h2 = dd2.getHours();
    var mm2 = dd2.getMinutes();
    m2 = m2 > 9 ? m2 : "0" + m2;
    d2 = d2 > 9 ? d2 : "0" + d2;
    mm2 = mm2 > 9 ? mm2 : "0" + mm2;
    h2 = h2 > 9 ? h2 : "0" + h2;
    var startDate =  y2 + "-" + m2 + "-" + d2 + " "+ h2 + ":" + mm2;
    $("#startDT").val(startDate);
}

</script>
</head>
<body>	
		<div id="titlebar"></div>
		<div id="allmap" class="map-img" style="height:600px"></div>
		<div id="toolbar">
			<div>
				<span>时间范围:</span>
				<select id="timeSelect" style="width: 140px;">
	                                <option value="3">3小时内</option>
	                                <option value="6">6小时内</option>
	                                <option value="12">12小时内</option>
	                                <option value="24">24小时内</option>
	                                <option value="48">2天内</option>
	                                <option value="72">3天内</option>
	                                <option value="0">自定义时间段</option>
	            </select>
            </div>
			<div id="startDiv" style="display:none">
				<span>起始时间:</span>
				<input type="text" id="startDT" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endDT\')}'})" readonly="readonly">
			</div>
			<div id="endDiv" style="display:none">
				<span>结束时间:</span>
				<input type="text" id="endDT" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'startDT\')}',maxDate:'%y-%M-%d %H:%m'})" readonly="readonly">
			</div>	
            <div>
	            <span>播放速度:</span>
	            <select id="playRate" name="playRate"><option value="10">非常快</option>
					<option value="100">默认速度</option>
					<option value="100">快速</option>
					<option value="1000">中速</option>
					<option value="5000">慢速</option>
				</select>
			</div>	
			<div>
				<span id="waitTip" style="color:red;display:none">正在获取数据请稍后...</span>
			</div>
			<div>
				<input id="startBtn" type="button" value="开始"/><input id="endBtn" type="button" value="停止"/>
			</div>				
		</div>
<style>
	.BMap_pop .BMap_top{ background:#4d4c50; }
	.BMap_pop .BMap_center{background:#4d4c50; }
	.BMap_pop .BMap_bottom{background:#4d4c50;}
	#allmap {width:100%;height:100%;position:absolute;top:56px;left:0px;}
	#titlebar {width: 100%;height: 56px;background: url(../images/table-head-2.png) no-repeat right top;z-index:999;position:absolute;top:0px;left:0px;}
	#toolbar {width:240;height:260;position:absolute;top:60px;right:20px;background: #fff;z-index:999;}
</style>
</body>
</html>