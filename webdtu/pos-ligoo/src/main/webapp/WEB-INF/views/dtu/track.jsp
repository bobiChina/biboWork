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

<script type="text/javascript">
var isPause = false;//暂停标志位
var isRunning = false;//停止标志位
var count = 1;
var interval;
var vehicleType = "${vehicleType}";
var markArr = new Array();
$(function(){
		// 百度地图API功能
		var map = new BMap.Map("allmap");
		map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
		map.enableContinuousZoom(); 
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_LEFT,offset: new BMap.Size(25, 50)}));  //右上角，仅包含平移和缩放按钮
		map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT,offset: new BMap.Size(10, 25)}));
	
		var point = new BMap.Point(107.95000,36.26667);
		map.centerAndZoom(point, 5);
	
		
		$().tipsInitStop();		
		getDate(3);
		
		$("#timeSelect").change(function(){
			if ($("#timeSelect").val() == 0){
				$("#startDiv").css("display","block");
				$("#endDiv").css("display","block");
				getDate(0);
			}else{
				$("#startDiv").css("display","none");
				$("#endDiv").css("display","none");
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
						markArr = [];
						$("#waitTip").hide();
						if(data == "[]"){
							$("#startBtn").val("开始");
							alert("该时间段内无轨迹数据");
							return;
						}
						isRunning = true;
					    var arr = data;
					    json = eval('(' + arr + ')');
					    var pntArr = new Array();
						var startPnt;
						var endPnt;
					    for (var i = 0; i < json.length; i++) {
					    	var point = new BMap.Point(json[i].x,json[i].y);
					    	var insertTime = json[i].insertTime;
					    	
							if (i==0)
								startPnt = point;
							if (i==json.length-1)
								endPnt = point;
								                    
							pntArr.push(point);
							createMarker(json[i]);
					    }
					    //创建开始和结束节点
					    var startIcon = new BMap.Icon("${ctx}/static/images/trackStart.png", new BMap.Size(50,50));
					    var startMark = new BMap.Marker(startPnt,{icon:startIcon});
						map.addOverlay(startMark); 
						var endIcon = new BMap.Icon("${ctx}/static/images/trackEnd.png", new BMap.Size(50,50));
					    var endMark = new BMap.Marker(endPnt,{icon:endIcon});
						map.addOverlay(endMark); 
						count = 1;
					    map.centerAndZoom(startPnt,15); 
						var polyline = new BMap.Polyline(pntArr, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5}); 
						map.addOverlay(polyline);    
						
					    if (vehicleType == '新能源客车')
						  typename = 'bus';
					    else if (vehicleType == '低速微型车'){
						  typename = 'jeep';
					    }
					    else if (vehicleType == '乘用车'){
						  typename = 'car';
					    }
					    else if (vehicleType == '其他'){
				  		  typename = 'train';
					    }
					 	var imagename = typename+"-blue.png"
						var myIcon = new BMap.Icon("${ctx}/static/images/"+imagename, new BMap.Size(42,32));
						var carMk = new BMap.Marker(startPnt,{icon:myIcon});
						map.addOverlay(carMk); 
						
						
						interval = setInterval(resetMkPoint,$("#playRate").val());
						function resetMkPoint(){
							if (isPause == true){
								return;
							}
							map.setCenter(pntArr[count]);
							carMk.setPosition(pntArr[count]);
                            if (document.getElementById("isTrackpoint").checked)
                            {
                            	map.addOverlay(markArr[count]); 
                            }
							
							if(count < pntArr.length){
								count++;
							}else{
								isRunning = false;
								isPause = false;
								$("#startBtn").val("开始");
								clearInterval(interval);
							}
						}

						$("#playRate").change(function(){
							if (isRunning == true){
							    clearInterval(interval);
							    interval = setInterval(resetMkPoint, $("#playRate").val());
							}
						});
						
			});
		});

		$("#endBtn").click(function(){
			clearInterval(interval);
			map.clearOverlays();
			isRunning = false;
			isPause = false;
			$("#startBtn").val("开始");
		});

});


function setMapBg(){
	console.log($(".BMap_pop div").length);
	jQuery(".BMap_pop div").css({
		"backgroundColor":"#4d4c50"
	});


	jQuery(".BMap_pop img").each(function(){
		if($(this).attr("src") == "http://api0.map.bdimg.com/images/iw3.png"){
			$(this).attr("src","${ctx}/static/images/iw3.png");
		}
	});
}

//setMapBg();

function createMarker(item){
	var point = new BMap.Point(item.x,item.y);
	var marker;
    markerImageName = 'node';
    myIcon = new BMap.Icon("${ctx}/static/images/trackNode.png", new BMap.Size(20, 20));
    marker = new BMap.Marker(point, { icon: myIcon });
    //marker.setOffset(new BMap.Size(0, 0));
   
    marker.addEventListener("click", function () {
			
    	
        var pt = new BMap.Point(item.x, item.y);
        var gc = new BMap.Geocoder();
        gc.getLocation(pt, function (rs) {
            var addComp = rs.addressComponents;
            var addr = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
            var opts = {
                width: 240,     // 信息窗口宽度
                height: 120,     // 信息窗口高度
                enableMessage:false//设置允许信息窗发送短息
            }
            var lntlat = item.x+"&nbsp;&nbsp;&nbsp;"+item.y;
            
  	        var sContent =
				"<div class=\"bus-stop-see-info\">"												+
					"<div class=\"see-stop-info-con\">"											+
						"<ul>"																	+
							"<li><label>&nbsp; UUID&nbsp;:</label><span>${uuid}</span></li>"	+
							"<hr/>"																	+
							"<li><label>经纬度  :</label><span>"+lntlat+"</span></li>"	+
							"<li><label>&nbsp;&nbsp;&nbsp;时间 :</label><span>"+getDateStr(item.insertTime)+"</span></li>";
							if(addr.length >12){
								sContent +="<li><label>&nbsp;&nbsp;&nbsp;位置 :</label><span>"+addr.substr(0,12)+"</span></li>";
								sContent +="<li><label>&nbsp;&nbsp;&nbsp;</label><span>"+addr.substr(12,12)+"</span></li>";
							}
							else
								sContent +="<li><label>&nbsp;&nbsp;&nbsp;位置 :</label><span>"+addr+"</span></li>";
							sContent +="</ul></div></div>"	
            
            var infoWindow = new BMap.InfoWindow(sContent, opts);
			//setMapBg();
            marker.openInfoWindow(infoWindow);
    		

        });
    });
    markArr.push(marker);
}

function getDateStr(obj){
	var year = obj.year-100+2000;
	var month = obj.month+1;
	if (month <10)
		month = "0"+month;
	var date = obj.date;
	if (date <10)
		date = "0"+date;
	var hours = obj.hours;
	if (hours <10)
		hours = "0"+hours;
	var minutes = obj.minutes;
	if (minutes<10)
		minutes = "0"+minutes;
	var seconds = obj.seconds;
	if (seconds<10)
		seconds = "0"+seconds;
	return year+"-"+month+"-"+date+" &nbsp;"+hours+":"+minutes+":"+seconds;
}

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
		<div id="allmap" class="map-img"></div>
		<div id="toolbar">
			<div class="addbaidu-rows">轨迹回放</div>
			<div class="addbaidu-rows">UUID:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${uuid}</div>
			<div class="addbaidu-rows">车架号:&nbsp;&nbsp;&nbsp;&nbsp;${vehicleNumber}</div>
			<div class="addbaidu-rows">
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
			<div class="addbaidu-rows" id="startDiv" style="display:none">
				<span>开始时间:</span>
				<input type="text" id="startDT" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endDT\')}'})" readonly="readonly">
			</div>
			<div class="addbaidu-rows" id="endDiv" style="display:none">
				<span>结束时间:</span>
				<input type="text" id="endDT" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'startDT\')}',maxDate:'%y-%M-%d %H:%m'})" readonly="readonly">
			</div>	
            <div class="addbaidu-rows">
	            <span>播放速度:</span>
	            <select id="playRate" name="playRate">
	            	<option value="1">非常快</option>
					<option value="10" selected="selected">快速</option>
					<option value="500">中速</option>
					<option value="2000">慢速</option>
				</select>
			</div>	
			<div class="addbaidu-rows"><span>是否显示轨迹点</span>&nbsp;&nbsp;<input id="isTrackpoint" name="isTrackpoint" type="checkbox" checked></div>
			<div class="addbaidu-rows">
				<span id="waitTip" style="color:red;display:none">正在获取数据请稍后...</span>
			</div>
			<div class="addbaidu-rows">
				<input id="startBtn" type="button" value="开始"/><input id="endBtn" type="button" value="停止"/>
			</div>				
		</div>
	<style type="text/css">  
	    .anchorBL{display:none;}  
		/* .BMap_pop .BMap_top{ background:#4d4c50; }
		.BMap_pop .BMap_center{background:#4d4c50; }
		.BMap_pop .BMap_bottom{background:#4d4c50;} */
		#allmap {width:100%;height:100%;}
		.see-stop-info-con{ background:#fff; color:#333;}
		
		#toolbar {width:220px; padding: 10px; position:absolute;top:60px;right:20px;background: #fff;z-index:999; background: #4d4c50; color: #FFF; font-size: 13px;}
		.addbaidu-title{ text-align: center;}
		.addbaidu-rows{ height: 30px; line-height: 30px;}
		#startBtn{padding: 5px 10px; width: 80px; display: block; float: left; margin-right: 5px;margin-left:30px}
		#endBtn{ padding: 5px 10px; width: 80px;display: block;float: left;}
		#timeSelect{ height: 22px; line-height: 22px;}	   
	 </style> 
</body>
</html>