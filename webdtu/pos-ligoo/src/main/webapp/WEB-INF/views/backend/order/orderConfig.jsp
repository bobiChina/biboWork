<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_config"/>
	<title>订单管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/doc.css" />

	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>

<script type="text/javascript">
var d;
$(function(){
	//====== 用户信息tip
	$(".user-login-arrow").tipsbox({
		"tipsLeft":"-20px",
		"tipsTop":"20px"
	});

	$(".showTip").inputTipsText();

	$(".manage-page-left").mCustomScrollbar({
		//axis:"y",
		theme:"dark-3",
		advanced:{
			autoExpandHorizontalScroll:true
		},
	 	autoDraggerLength: true
	 });

	 d = dialog();
});

function showDelIcon(obj, type){
	$("#orderdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-lefthover")
	});

	if(type == 1){
		$(obj).find('.handle').show();
		$(obj).addClass("manage-lefthover");
	}else{
		$(obj).find('.handle').hide();
	}
}

function showEditIcon(obj, type){
	if(type == 1){
		$(obj).find('.handle').show();
	}else{
		$(obj).find('.handle').hide();
	}
}

function orderNoChange(id){
	$("#selOrderNo").val(id);
	//修改样式
	$("#orderdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-leftactive");
		if($(this).attr("orderLIData") == id){
			$(this).addClass("manage-leftactive");
		}
    });

	$.post("${ctx}/order/getOrderSNList", {orderno:id},function(data){
		var tr = "";
		var result = eval(data);
		if(result.length == 0){
			$("#dtu_body_sn").html("<tr><td colspan='6'>无绑定设备</td></tr>");
			return;
		}
		$.each(result, function(index, items){
			tr += "<tr style='position:relative;' onmouseover='showEditIcon(this, 1)' onmouseout='showEditIcon(this, 0)'>";
			tr += "<td style='width:32px;'>" + (index + 1) + "</td>";
			tr += "<td style='width:125px;' nowrap='nowrap'>" + items.sn + "</td>";
			tr += "<td style='width:295px;' nowrap='nowrap'>" + items.uuid + "</td>";
			tr += "<td style='width:90px;' nowrap='nowrap'>" + items.dtuUser.userName + "</td>";
			tr += "<td style='width:90px;' nowrap='nowrap'>" + items.hwVersion + "</td>";
			tr += "<td style='width:122px;' nowrap='nowrap'>" + items.swVersion ;
			tr += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			tr += "<img onclick='delSNFun(\"" + items.id + "\",\"" + items.uuid + "\",\"" + items.sn + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			tr += "</div>";
			tr += "</td>";
			tr += "</tr>";
		});
		$("#dtu_body_sn").html(tr);

	});
	toBackPage();
}

function findOrderLi(){
	var selId = $("#selOrderNo").val();
	$("#orderdata").html("");
	$.post("${ctx}/order/getOrderList", {type:1},function(data){
		var li = "";
		var result = eval(data);
		$.each(result, function(index, items){
			li += "<li orderLIData='" + items[1] + "' style='position:relative;'";
			li += " onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' ";
			if(selId == ""){
				if(index == 0){
					li += " class='manage-leftactive' ";
					orderNoChange(items[1]);
				}
			}else{
				if(selId == items[1]){
					li += " class='manage-leftactive' ";
                    orderNoChange(items[1]);
				}
			}
			li += " >";
			li += "<a href='javascript:;' onclick='orderNoChange(\"" + items[1] + "\")'>" + items[1] ;

			li += "</a>";
			li += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			li += "<img onclick='toScanPage(" + items[0] + ",\"" + items[1] + "\", " + items[2] + ")' src='${ctx}/static/images/update.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			li += "</div>";
			li += "</li>";
		});
		$("#orderdata").html(li);
	});
}

function toScanPage(id, orderno, status){
	//选中对应操作项，防止订单对应误操作
	$("#selOrderNo").val(orderno);
	//修改样式
	$("#orderdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-leftactive");
		if($(this).attr("orderLIData") == orderno){
			$(this).addClass("manage-leftactive");
		}
	});
	$("#snInfoShow").css("display", "none");
	$("#scanInfoShow").css("display", "block");
	$.post("${ctx}/order/getOrderScanList", {orderno:orderno},function(data){
		var tr = "";
		var result = eval(data);
		if(result.length == 0){
			$("#dtu_body_scan").html("<tr><td colspan='6'>无待确认设备</td></tr>");
			$("#showDtuSetBtu").css("display" ,"none");
			return;
		}
		$.each(result, function(index, items){
			tr += "<tr style='position:relative;' onmouseover='showEditIcon(this, 1)' onmouseout='showEditIcon(this, 0)'>";
			tr += "<td style='width:32px;'><input type='checkbox' name='selScanObj' value='" + items.id + "' /></td>";
			tr += "<td style='width:125px;' nowrap='nowrap'>" + items.sn + "</td>";
			tr += "<td style='width:295px;' nowrap='nowrap'>" + items.uuid + "</td>";
			tr += "<td style='width:90px;' nowrap='nowrap'>" + items.dtuUser.userName + "</td>";
			tr += "<td style='width:90px;' nowrap='nowrap'>" + items.hwVersion + "</td>";
			tr += "<td style='width:122px;' nowrap='nowrap'>" + items.swVersion ;
			tr += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			tr += "<img onclick='delScanFun(\"" + items.id + "\",\"" + items.uuid + "\",\"" + items.sn + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			tr += "</div>";
			tr += "</td>";
			tr += "</tr>";
		});
		$("#dtu_body_scan").html(tr);
		$("#showDtuSetBtu").css("display", "block");
	});

}

function toBackPage(){
	$("#snInfoShow").css("display", "block");
	$("#scanInfoShow").css("display", "none");
}

function delScanFun(id, uuid, sn){
	var d = dialog({
		title: '系统提示',
		content: '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>SN码:' + sn + '<br>UUID:' + uuid + '</div>',
		zIndex: 999999,
		button: [
				{
					value: '确定',
					callback: function () {
						$.post("${ctx}/order/delScanFun", {id:id},function(data){
							//显示右侧信息
							toScanPage(0, $("#selOrderNo").val(), 0);
						});
					}
				},
				{
					value: '取消',
					callback: function () {
					},
					autofocus: true
				}
			]
	});
	d.width(250);
	d.showModal();
}

function delSNFun(id, uuid, sn){
	var d = dialog({
		title: '系统提示',
		content: '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>SN码:' + sn + '<br>UUID:' + uuid + '</div>',
		zIndex: 999999,
		button: [
				{
					value: '确定',
					callback: function () {
						$.post("${ctx}/order/delSNFun", {id:id},function(data){
							//显示右侧信息
							orderNoChange($("#selOrderNo").val());
						});
					}
				},
				{
					value: '取消',
					callback: function () {
					},
					autofocus: true
				}
			]
	});
	d.width(250);
	d.showModal();
}

function selAllScan(obj){
	if($(obj).is(":checked")){
		$("#dtu_body_scan").find("input").each(function(i, em){
			if($(this).attr("id") == "selAll"){

			}else{
				$(this).prop('checked' , true);
			}
		});
	}else{
		$("#dtu_body_scan").find("input").each(function(i, em){
			if($(this).attr("id") == "selAll"){

			}else{
				$(this).prop('checked' , false);
			}
		});
	}


}
//绑定SN
function saveSNScan(){
	var selStr = "";
	$("#dtu_body_scan").find("input").each(function(i, em){
		if($(this).attr("id") == "selAll"){

        }else{
        	if($(this).prop('checked')){
				selStr += $(this).val() + ",";
			}
        }
	});

	if(selStr == ""){
		showDialogAlert("请选择扫码信息");
        return;
	}

	d.showModal();
	$.post("${ctx}/order/saveSNScan", {ids:selStr, orderno:$("#selOrderNo").val()},function(data){
		//显示右侧信息
		toScanPage(0, $("#selOrderNo").val(), 0);
		d.close();
	});
}


</script>
</head>
<body>
<input type="hidden" id="selOrderNo" name="selOrderNo" value="" />
<input type="hidden" id="userDtu_old" name="userDtu_old" value="" />
<div class="body-outer dclear clearfix">
		<div class="table-box page-manage-box-outer dclear clearfix">
			<div class="table-body page-table-body">
				<div class="history-save-table-head">
					<div class="history-save-table-head-outer">
						<div class="manage-page-head-left dleft" onclick='findOrderLi(-999)' style="cursor: pointer;">
							<div style="color:#FFF; font-size:14px; text-decoration: underline;">订单列表</div>
						</div>
						<div class="manage-page-head-right dright">
							设备信息
						</div>
					</div>
				</div>
				<style type="text/css">
					.table-body select{ width: 202px;}
				</style>
				<div class="historty-table-con-outer dclear">
					<div class="history-table-con-inner manage-page-inner clearfix">
						<div class="manage-page-left dleft" style="height:541px;">
							<ul id="orderdata">
								<c:forEach items="${orderList}" var="item" varStatus="status">
									<li orderLIData="${item[1]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;' <c:if test="${item[2] < 2}"> onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' </c:if> >
                                    	<a href="javascript:;" onclick="orderNoChange('${item[1]}')" >${item[1]}</a>
                                    	<div class="handle" style="position: absolute; right: 5px; margin-top: -30px; display: none;">
                                    	<img onclick="toScanPage('${item[0]}','${item[1]}', ${item[2]})" src="${ctx}/static/images/update.png" style="position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; ">
                                     	</div>
                                     	<c:if test="${status.count == 1}">
										 <script type="text/javascript">
											$(function (){
												orderNoChange('${item[1]}');
											});
										 </script>
										 </c:if>
                                     </li>
								</c:forEach>
							</ul>
						</div>
						<div class="manage-page-right dright" id="snInfoShow">
							<div class="manage-page-item">
								<div style="margin-top:0px; height:45px; width:760px;">
									<div class="table-body2" >
										<table class="dclear" >
												<tr class="trHead">
													<th style="width:32px;">&nbsp;</th>
													<th style="width:125px;">SN码</th>
													<th style="width:295px;">UUID</th>
													<th style="width:90px;">产品型号</th>
													<th style="width:90px;">硬件版本号</th>
													<th style="width:122px;">软件版本号</th>
												</tr>
										</table>
									</div>
								</div>
								<div class="manage-page-left dleft" style="margin-top:-5px; height:382px; width:760px; background: #FFF;">
									<div class="table-body2" >
										<table class="dclear" >
											<tbody id="dtu_body_sn" valign="top">
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						<div class="manage-page-right dright" id="scanInfoShow" style="display:none;">
							<div class="manage-page-item">
								<div style="margin-top:0px; height:45px; width:760px;">
									<div class="table-body2" >
										<table class="dclear" >
												<tr class="trHead">
													<th style="width:32px;"><input type="checkbox" value="1" name="selAll" id="selAll" onclick="selAllScan(this)" /> </th>
													<th style="width:125px;">SN码</th>
													<th style="width:295px;">UUID</th>
													<th style="width:90px;">产品型号</th>
													<th style="width:90px;">硬件版本号</th>
													<th style="width:122px;">软件版本号</th>
												</tr>
										</table>
									</div>
								</div>
								<div class="manage-page-left dleft" style="margin-top:-5px; height:452px; width:760px; background: #FFF;">
									<div class="table-body2" >
										<table class="dclear" >
											<tbody id="dtu_body_scan" valign="top">
											</tbody>
										</table>
									</div>
								</div>
								<div style="margin-top:459px; height:45px; width:760px;background: #CCCCCC !important; display:none;" align="right" id="showDtuSetBtu">
									<a href="javascript:void(0)" class="l-btn"  onclick="saveSNScan()" style="margin-right:20px; margin-top:8px;"><span class="l-btn-left"><span class="l-btn-text">&nbsp;保 存&nbsp;</span></span></a>&nbsp;
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="table-footer page-footer dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->
</body>
</html>