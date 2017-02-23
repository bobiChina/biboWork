<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_product"/>
	<title>产品管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />

	<link rel="stylesheet" href="${ctx}/static/styles/easyui.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/icon.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/css4easyui.css" />

	<script src="${ctx}/static/js/jquery-1.11.1.min.js"></script>
	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>

	<script src="${ctx}/static/js/jquery.easyui.min.js"></script>
	<script src="${ctx}/static/js/easyui-lang-zh_CN.js"></script>
	<script src="${ctx}/static/js/jquery.jdirk.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.all.min.js"></script>
	<style>
		.combo .combo-text{
			background:#E0E0E0;
			font-size: 14px;
			color:#000;
			padding-top: 0px;
		}
		.combo-arrow{
			background-color:#E0E0E0;
		}
		.combo,
		.combo-panel {
			background-color: #E0E0E0;
			color:#000;
			margin-top:0px;
		}

		.combobox-item,.combobox-group {
			font-size: 14px;
			padding: 3px;
			padding-right: 0px;
		}
		.combo{border-width:0px;}

		.combobg{
			background-color: #FFF !important;;
		}

		.manage-input-text {
			background: #E0E0E0;
			padding-left: 5px;
			width: 206px;
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

	$(".showTip").inputTipsText();

	$(".manage-page-left").mCustomScrollbar({
		//axis:"y",
		theme:"dark-3",
		advanced:{
			autoExpandHorizontalScroll:true
		},
	 	autoDraggerLength: true
	 });

	var productArr = [];
	<c:forEach items="${productList}" var="item" varStatus="status">
		productArr.push({"prodid":"${item[0]}", "prodname": "${item[1]}"});
	</c:forEach>
    $('#searchproductKey_li').combobox({
		valueField: 'prodid',
		textField: 'prodname',
		data: productArr,
    	onSelect: function(record){
			productIdChange(record.prodid, record.prodname);
    	},
    	onLoadSuccess: function(){
    		$("#searchorder_dev").find(".combo").addClass("combobg");
            $("#searchorder_dev").find(".combo-text").addClass("combobg");
			//默认显示value的内容
			var p = $("input[name='searchproductKey_li']").closest("span.combo,div.combo-p,div.menu");//向上查找
			var tipObj = p.find(".combo-text");
			var defaultValue = tipObj.val();
			$(tipObj).on({
				focus:function(){
					if($(this).val() == defaultValue ){
						$(this).val("");
					}
				},
				blur:function(){
					if($(this).val() == ''){
						$(this).val(defaultValue);
					}
				}
			});
		 }
    });


});

function addProduct(){
	$("#prodValue").val("");
	$.easyui.showDialog({
		title: "添加产品型号",
		width: 500, height: 280,
		content:$("#addProdInfo").html(),
		topMost: true,
		enableApplyButton: false,
		onSave: function (d) {
			var dataForm = d.form("getData");
			var prodValue = dataForm.prodValue ;
			if( prodValue == "" || prodValue == "输入产品型号"){
				$.messager.alert("系统提示", "产品型号不能为空", "warning");
				return false;
			}else{
				//判断字符串是否包含小括号
				var reg = /^[A-Z]+[\( | A-Z0-9]*[\)|A-Z0-9]*$/g ;
				if(reg.test(prodValue) ){


				}else{
					$.messager.alert("系统提示", "产品型号只能为大写字母或大写字母加数字", "warning");
					//alert("产品型号只能为大写字母或大写字母加数字");
					return false;
				}

				$.post("${ctx}/product/saveProd?rmd=" + new Date().getTime(), {prodname: prodValue, prodType : dataForm.prodType },function(data){
					 if(data == "-1"){
					 	$.messager.alert("系统提示", "产品型号<font color='red'>" + prodValue + "</font>重复", "info");
					 	return false;
					 }else{
					 	$.messager.alert("系统提示", "操作成功", "info");
					 	$("#selProductId").val(data);
					 	$('#searchproductKey_li').combobox({
							url: '${ctx}/product/getBoxvalue?rmd=' + new Date().getTime(),
							valueField: 'prodid',
							textField: 'prodname',
							onSelect: function(record){
								productIdChange(record.prodid, record.prodname);
							}
					 	});
					 }
					 findPrdLi();
				 });
			}
		}
	});
}

function addHardWare(){
	$("#hwValue").val("");
	$.easyui.showDialog({
		title: "添加版本号",
		width: 500, height: 280,
		content:$("#addHwInfo").html(),
		topMost: true,
		enableApplyButton: false,
		onSave: function (d) {
			var dataForm = d.form("getData");
			var hwValue = dataForm.hwValue;
			if( hwValue == "" || hwValue == "输入版本号"){
				$.messager.alert("系统提示", "版本号不能为空", "warning");
				return false;
			}else{
				/*var reg = /^\d+(\.\d+)?$/g ;
				if(reg.test(hwValue) ){
					$.messager.alert("系统提示", "版本号输入不正确", "warning");
					return false;
				}*/
				$.post("${ctx}/product/saveHwVersion?rmd=" + new Date().getTime(), {hwName: hwValue, prodId:$("#selProductId").val()},function(data){
					if(data == "-1"){
						$.messager.alert("系统提示", "版本号<font color='red'>" + hwValue + "</font>重复", "warning");
						return false;
					}else{
						$.messager.alert("系统提示", "操作成功", "info");
						productIdChange($("#selProductId").val(), "");
					}

				});
			}
		}
	});
}




function productIdChange(id, name){
	if(id == "-1"){
		return;
	}
	$("#selProductId").val(id);
	//修改样式
	$("#productdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-leftactive");
		if($(this).attr("prodLIData") == id){
			$(this).addClass("manage-leftactive");
		}
    });
    //清除原信息
    if(name == null || name == undefined || name == ""){

    }else{
    	$("#prodName_show").html(name);
    }

	$("#hw_body").html("");
	$.post("${ctx}/product/getHWList?rmd=" + new Date().getTime(), {prodId:$("#selProductId").val()},function(data){
		var tr = "";
		var result = eval(data);
		if(result.length == 0){
			$("#hw_body").html("<tr><td colspan='6'>无版本号信息</td></tr>");
			return;
		}
		tr += "<tr style='color:#000; background-color:#ECECF0;'>";
		tr += "<td style='width:50px;'>&nbsp;</td>";
		tr += "<td style='width:180px;' nowrap='nowrap'>产品型号</td>";
		tr += "<td style='width:180px;' nowrap='nowrap'>硬件版本号</td>";
		tr += "<td style='width:180px;' nowrap='nowrap'>添加人</td>";
		tr += "<td style='width:190px;' nowrap='nowrap'>添加日期</td>";
		tr += "</tr>";
		$.each(result, function(index, items){
			tr += "<tr onmouseover='showEditIcon(this, 1)' onmouseout='showEditIcon(this, 0)'>";
			tr += "<td style='width:50px;'>" + (index + 1) + "</td>";
			tr += "<td style='width:180px;' nowrap='nowrap'>" + items.productType.prodTypename + "</td>";
			tr += "<td style='width:180px;' nowrap='nowrap'>" + items.version + "</td>";
			tr += "<td style='width:180px;' nowrap='nowrap'>" + items.optUser.userName + "</td>";
			tr += "<td style='width:190px;' nowrap='nowrap'><li style='position:relative; border: 0px;' >" + (parseInt(items.optTime.year) + 1900) + "-" + (parseInt(items.optTime.month) + 1) + "-" + items.optTime.date ;
			tr += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			tr += "<img onclick='delHWFun(\"" + items.id + "\",\"" + items.version + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			tr += "</div></li>";
			tr += "</td>";
			tr += "</tr>";
		});

		$("#hw_body").html(tr);
});

}


function showEditIcon(obj, type){
	if(type == 1){
		$(obj).find('.handle').show();
	}else{
		$(obj).find('.handle').hide();
	}
}

function showDelIcon(obj, type){
	$("#productdata").find("li").each(function(indexNum, ele){
		$(this).removeClass("manage-lefthover")
	});

	if(type == 1){
		$(obj).find('.handle').show();
		$(obj).addClass("manage-lefthover");
	}else{
		$(obj).find('.handle').hide();
	}
}

function showDelIcon_dtu(obj, type){
	if(type == 1){
		$(obj).find('.handle').show();
	}else{
		$(obj).find('.handle').hide();
	}
}

function findPrdLi(){
	var selId = $("#selProductId").val();
	$("#productdata").html("");
	$.post("${ctx}/product/getProdLi?rmd=" + new Date().getTime(), {},function(data){
		var li = "";
		var result = eval(data);
		$.each(result, function(index, items){
			li += "<li prodLIData='" + items[0] + "' style='position:relative;'";
			//if( items[2] < 1){
			 	li += " onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' ";
			//}
			if(selId == ""){
				if(index == 0){
					li += " class='manage-leftactive' ";
					productIdChange(items[0], items[1]);
				}
			}else{
				if(selId == items[0]){
					li += " class='manage-leftactive' ";
                    productIdChange(items[0], items[1]);
				}
			}
			li += " >";
			li += "<a href='javascript:;' onclick='productIdChange(" + items[0] + ",\"" + items[1] + "\")'>" + items[1] ;
			if( items[3] == 1){
				//li += "<font color='red'>主</font>";
			}
			li += "</a>";
			li += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
			li += "<img onclick='delProdFun(" + items[0] + ",\"" + items[1] + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
			li += "</div>";
			li += "</li>";
		});
		$("#productdata").html(li);
	});
}

function delHWFun(id, version){
	$.messager.confirm("版本号删除确认", '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>版本号:' + version + '</div>', function(cr){
		if(cr){
			$.post("${ctx}/product/delHWFun?rmd=" + new Date().getTime(), {id:id},function(data){
				//显示右侧信息
				productIdChange($("#selProductId").val(), "");
			});
		}
	});
}

function delProdFun(id, name){
	$.messager.confirm("产品型号删除确认", '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>产品型号:' + name + '</div>', function(cr){
		if(cr){
			$.post("${ctx}/product/delProdFun?rmd=" + new Date().getTime(), {id:id},function(data){
				if(data == "-1"){
					$.messager.alert("系统提示", "存在关联的版本号<br>删除失败", "info");
				}else if(data == "-2"){
					$.messager.alert("系统提示", "存在关联的扫码信息<br>删除失败", "info");
				}else{
					//显示左侧信息
					$("#selProductId").val("");
					findPrdLi();
					$('#searchproductKey_li').combobox({
						url: '${ctx}/product/getBoxvalue?rmd=' + new Date().getTime(),
						valueField: 'prodid',
						textField: 'prodname',
						onSelect: function(record){
							productIdChange(record.prodid, record.prodname);
						}
					});
				}
			});
		}
	});
}


</script>
</head>
<body>
<input type="hidden" id="selProductId" name="selProductId" value="" />
<div class="body-outer dclear clearfix" style="width:1020px;">
		<div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
			<div class="table-body page-table-body" style="width:1020px;">
				<!-- ---------------------------head start -------------------------- -->
				<div class="history-save-table-head-backend">
					<div class="history-save-table-head-outer">
						<div class="manage-page-head-left dleft">
							<div class="dleft" style="width:150px;" onclick='findPrdLi(-999)'><a href="javascript:void(0)" style="color:#FFF; font-size:14px;">模块类型</a></div>
						</div>
						<div class="manage-page-head-right dright" >
							<div class="dleft" style="width:670px;">	硬件版本信息</div>
						</div>
					</div>
				</div>
				<!-- ---------------------------head end -------------------------- -->
				<div class="historty-table-con-outer-backend dclear">
					<div class="manage-page-inner clearfix" style="width:1010px;">
						<div class="dleft" style="background:#E0E0E0;">
							<div style="background:#E0E0E0; width:197px; height:37px;text-align:center; margin-top:8px; margin-left:3px;border-bottom: 1px solid #cccccc;" id="searchorder_dev">
								<input id="searchproductKey_li" name="searchproductKey_li" style="width:160px; height:30px;padding:6px 12px;" value="输入产品型号搜索" />
							</div>
							<div class="manage-page-left" style="height:538px; margin-bottom: 0px !important;">
								<ul id="productdata">
									<c:forEach items="${productList}" var="item" varStatus="status">
										<li prodLIData="${item[0]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;' onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' >
											<a href="javascript:;" onclick="productIdChange('${item[0]}','${item[1]}')" >${item[1]}</a><%--<c:if test="${item[3] == 1}">&nbsp;&nbsp;<font color='red' title='主机型号'>主</font></c:if>--%>
											<div class="handle" style="position: absolute; right: 5px; margin-top: -30px; display: none;">
											<img onclick="delProdFun('${item[0]}','${item[1]}')" src="${ctx}/static/images/del.png" style="position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; ">
											</div>
											<c:if test="${status.count == 1}">
											 <script type="text/javascript">
												$(function (){
													productIdChange('${item[0]}','${item[1]}');
												});
											 </script>
											 </c:if>
										 </li>
									</c:forEach>

								</ul>
							</div>
							<div style="background:#E0E0E0; width:200px; height:50px;text-align:center;padding-top:5px;">
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="addProduct()"><strong>&nbsp;&nbsp;创建新设备&nbsp;&nbsp;</strong></a>
							</div>
						</div>
						<div class="easyui-panel" id="orderInfoShow" style="background:#FFF;margin-top:1px; height:650px;width:810px; font-size:14px; overflow: hidden;" align="right"  >
							<div class="easyui-panel" title="版本号" style="width:805px;height:620px;padding:1px;font-size:14px;"	 data-options="iconCls:'icon-book-go',tools:'#tt_base'">
								<table class="dclear" style=" width:802px;">
									<tbody id="hw_body" valign="top">
									</tbody>
								</table>
							</div>
							<div id="tt_base">
								<a href="javascript:void(0)" class="icon-add" onclick="addHardWare()"></a>
							</div>
						</div>
						<div id="addProdInfo" style="display:none;">
							<table class="dclear" style="width:450px; margin-top:40px;" >
								<tr>
									<td style="text-align:right; width:150px; height:45px; border:0px;">产品型号：</td>
									<td style="text-align:left; border:0px;" ><input type="text" id="prodValue" name="prodValue" style="color:#9E9E9E;width:178px;" class="manage-input-text-m" maxlength="12" value="" /><font color="red">*</font></td>
								</tr>
								<tr>
									<td style="text-align:right; width:150px; height:45px; border:0px;">型号类别：</td>
									<td style="text-align:left; border:0px;" >
										<select id="prodType" name="prodType" style="width:180px;" >
											<option value="1">主机型号</option>
											<option value="0">从机型号</option>
										</select>
									</td>
								</tr>
							</table>
						</div>
						<div id="addHwInfo" style="display:none;">
							<table class="dclear" style="width:450px; margin-top:40px;font-size: 14px;" >
								<tr >
									<td style="text-align:right; width:150px; height:45px; border:0px;">产&nbsp;品&nbsp;型&nbsp;号：</td>
									<td style="text-align:left; border:0px;" id="prodName_show">BC52A-2</td>
								</tr>
								<tr>
									<td style="text-align:right; width:150px; height:45px; border:0px;">硬件版本号：</td>
									<td style="text-align:left; border:0px;" ><input type="text" id="hwValue" name="hwValue" style="color:#9E9E9E;width:178px;" class="manage-input-text-m" maxlength="20" value="" onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" /><font color="red">*</font></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="page-footer-backend dclear">
			</div>
		</div>
	</div> <!-- //body outer end -->
</body>
</html>