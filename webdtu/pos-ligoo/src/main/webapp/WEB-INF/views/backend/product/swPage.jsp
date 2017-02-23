<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_swPage"/>
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
	<script src="${ctx}/static/js/uploadify/jquery.uploadify.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.all.min.js"></script>
	<script src="${ctx}/static/js/jquery.euploadify.js"></script>
	<style>

		.table-body select {
			background: #E0E0E0;
			color: #000;
			border: none;
			height: 22px;
			line-height: 28px;
			text-align: right;
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

		.body-outer {
			width: 970px;
			margin: 0 auto;
		}

		.table-box-backend {
			margin-top: 0;
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
			<c:forEach items="${swList}" var="item" varStatus="status">
			productArr.push({"prodid":"${item[0]}", "prodname": "${item[1]}"});
			</c:forEach>
			$('#searchproductKey_li').combobox({
				valueField: 'prodid',
				textField: 'prodname',
				data: productArr,
				onSelect: function(record){
					swIdChange(record.prodid, record.prodname);
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
						$.post("${ctx}/product/saveHwVersion?rmd=" + new Date().getTime(), {hwName: hwValue, prodId:$("#selSwId").val()},function(data){
							if(data == "-1"){
								$.messager.alert("系统提示", "版本号<font color='red'>" + hwValue + "</font>重复", "warning");
								return false;
							}else{
								$.messager.alert("系统提示", "操作成功", "info");
								swIdChange($("#selSwId").val(), "");
							}

						});
					}
				}
			});
		}


		function swIdChange(id, name){
			if(id == "-1"){
				return;
			}
			$("#selSwId").val(id);
			//修改样式
			$("#productdata").find("li").each(function(indexNum, ele){
				$(this).removeClass("manage-leftactive");
				if($(this).attr("prodLIData") == id){
					$(this).addClass("manage-leftactive");
				}
			});
			//清除原信息
			$("#edit_sw_name").val(name);
			$("#show_version_sw_name").html(name);
			$("#edit_id").val(id);

			$("#show_hwList_tab").html("");
			$("#add_hwList_tab").html("");
			$("#edit_hwList_tab").html("");
			$("#show_version_tab").html("");
			$.post("${ctx}/product/findSwInfo?rmd=" + new Date().getTime(), {swId:$("#selSwId").val()},function(data){
				var tr = "";
				var result = eval('(' + data + ')');
				var showStr = "<tr >";
				var updateStr = "<tr >";
				var addStr = "<tr >";
				$.each(result.isUseHw , function(index, items){
					showStr += "<td style=\"text-align:left; border:0px; padding-left: 10px;\" >" + items[1] + "</td>";
					updateStr += "<td style=\"text-align:left; border:0px; \" ><label style=\"margin-right:25px;\"><input type=\"checkbox\" checked=\"checked\" name=\"hwObj\" value=\""
							+  items[0] + "\" />" + items[1] + "</label></td>";

					if((index+1) % 4 == 0 && ((index + 1) <result.isUseHw.length) ){
						showStr += "</tr >";
						showStr += "<tr >";

						updateStr += "</tr >";
						updateStr += "<tr >";
					}
				});
				showStr += "</tr >";
				updateStr += "</tr >";
				$("#show_hwList_tab").html(showStr);

				$.each(result.noUseHw , function(index, items){
					addStr += "<td style=\"text-align:left; border:0px; \" ><label style=\"margin-right:25px;\"><input type=\"checkbox\" name=\"hwObj\" value=\""
							+  items[0] + "\" />" + items[1] + "</label></td>";
					if((index+1) % 4 == 0){
						addStr += "</tr >";
						addStr += "<tr >";
					}
				});
				addStr += "</tr >";
				$("#add_hwList_tab").html(addStr);
				$("#edit_hwList_tab").html(updateStr + addStr);

				if(undefined != result.version && result.version.length > 9){
					$("#swinfo_tab").css("width", 785);
				}else{
					$("#swinfo_tab").css("width", 802);
				}

				var showVersionStr = "<tr style='color:#000; background-color:#ECECF0;'>" +
						" <td style=\"width: 50px;\">&nbsp;</td>" +
						" <td style=\"width: 80px;\">版本号</td>" +
						" <td width=\"*\">软件名称</td>" +
						" <td style=\"width: 90px;\">添加人</td>" +
						" <td style=\"width: 160px;\" >添加日期</td>" +
						" </tr>";
				$.each(result.version , function(index, items){
					showVersionStr += " <tr style='color:#000; background-color:#FFFFFF;' onmouseover='showEditIcon(this, 1)' onmouseout='showEditIcon(this, 0)'>";
					if(index == 0){
						showVersionStr += " <td style=\"width: 50px;\"><input type='checkbox' name='show_status' checked='checked' disabled /></td>";
					}else{
						showVersionStr += " <td style=\"width: 50px;\"><input type='checkbox' name='show_status' disabled /></td>";
					}
					showVersionStr += " <td style=\"width: 90px;\" valign=\"middle\"><a href='javascript:void(0)' onclick='showVersion(\"" + items[0] + "\")'  >" + items[1] + "</a></td>";
					if(null == items[4] || undefined == items[4] ){
						showVersionStr += " <td width=\"*\">-</td>";
					}else{
						showVersionStr += " <td width=\"*\"><a href='${ctx}/fileOperate/downloadById?id=" + items[3] + "' >" + items[4] + "</a></td>";
					}
					showVersionStr += " <td style=\"width: 90px;\" valign=\"middle\">" + items[6] + "</td>" +
							" <td style=\"width: 160px;\" valign=\"middle\"><li style='position:relative; border: 0px;' >" + items[7] ;
					//		" <td style=\"width: 60px;\"><span class=\"icon-search\" style='cursor: pointer;'>&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;<span class=\"icon-cancel\" style='cursor: pointer;' onclick='delSWVersion(\"" + items[0] + "\",\"" + items[1] + "\")'>&nbsp;&nbsp;&nbsp;&nbsp;</span>";
					//showVersionStr += "<img onclick='delSWVersion(\"" + items[0] + "\",\"" + items[1] + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
					//showVersionStr += "</td></tr>";
					showVersionStr += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
					showVersionStr += "<img onclick='delSWVersion(\"" + items[0] + "\",\"" + items[1] + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
					showVersionStr += "</div></td></tr>";
				});
				$("#show_version_tab").html(showVersionStr);
			});

			toBackPage();
		}

		function showVersion(id){
			$.post("${ctx}/product/showVersion?rmd=" + new Date().getTime(), {id:id},function(data){
				var result = eval('(' + data + ')');
				$("#dialog_sw_code").html($("#edit_sw_name").val());
				$("#dialog_sw_version").html(result.sw_version);
				$("#dialog_sw_user").html(result.opt_user_name);
				$("#dialog_sw_time").html(result.add_time);
				$("#dialog_sw_file").html(result.file_name);
				$("#dialog_sw_updatenote").html(result.update_note);

				$.easyui.showDialog({
					title: "软件版本信息",
					width: 600, height: 380,
					content:$("#test_dialog").html(),
					topMost: true,
					enableApplyButton: false,
					enableSaveButton: false
				});

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
			var selId = $("#selSwId").val();
			$("#productdata").html("");
			$.post("${ctx}/product/getSwLi?rmd=" + new Date().getTime(), {},function(data){
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
							swIdChange(items[0], items[1]);
						}
					}else{
						if(selId == items[0]){
							li += " class='manage-leftactive' ";
							swIdChange(items[0], items[1]);
						}
					}
					li += " >";
					li += "<a href='javascript:;' onclick='swIdChange(" + items[0] + ",\"" + items[1] + "\")'>" + items[1] ;
					li += "</a>";
					li += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
					li += "<img onclick='delProdFun(" + items[0] + ",\"" + items[1] + "\")' src='${ctx}/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
					li += "</div>";
					li += "</li>";
				});
				$("#productdata").html(li);
			});
		}

		function delSWVersion(id, version){
			$.messager.confirm("版本号删除确认", '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>版本号:' + version + '</div>', function(cr){
				if(cr){
					$.post("${ctx}/product/delSWVersion?rmd=" + new Date().getTime(), {id:id},function(data){
						$.messager.alert("系统提示", "操作成功", "info");
						//显示右侧信息
						swIdChange($("#selSwId").val(), $("#edit_sw_name").val());
					});
				}
			});
		}

		function delProdFun(id, name){
			$.messager.confirm("产品型号删除确认", '<div style="text-align: left; padding-left:40px;">是否确认删除？<br>产品型号:' + name + '</div>', function(cr){
				if(cr){
					$.post("${ctx}/product/delSWFun?rmd=" + new Date().getTime(), {id:id},function(data){
						if(data == "-1"){
							$.messager.alert("系统提示", "存在关联的版本号<br>删除失败", "info");
						}else if(data == "-2"){
							$.messager.alert("系统提示", "存在关联的扫码信息<br>删除失败", "info");
						}else{
							$.messager.alert("系统提示", "操作成功", "info");
							//显示左侧信息
							$("#selSwId").val("");
							findPrdLi();
							$('#searchproductKey_li').combobox({
								url: '${ctx}/product/findSWBoxvalue?rmd=' + new Date().getTime(),
								valueField: 'prodid',
								textField: 'prodname',
								onSelect: function(record){
									swIdChange(record.prodid, record.prodname);
								}
							});
						}
					});
				}
			});
		}


		function toBackPage(){
			$("#swInfoShow").css("display", "block");
			$("#swClassAddFormDiv").css("display", "none");
			$("#swClassEditFormDiv").css("display", "none");
			$("#swVersionAddForm").css("display", "none");

			/**
			 * 清除的部分信息
			 */
			$("#add_sw_name").val("");
			$("#swVersion").val("");
			$("#file_code").euploadify("cancel", "*");
			$("#file_code").euploadify("reset");
		}

		function toAddSWInfoPage(){
			toBackPage();
			$("#swInfoShow").css("display", "none");
			$("#swClassAddFormDiv").css("display", "block");
			//$("#add_sw_name").val("");
		}

		function toEditSWInfoPage(){
			toBackPage();
			$("#swInfoShow").css("display", "none");
			$("#swClassEditFormDiv").css("display", "block");
		}
		function toAddVersionPage(){
			toBackPage();
			$("#swInfoShow").css("display", "none");
			$("#swVersionAddForm").css("display", "block");
			/*$("#swVersion").val("");
			 $("#file_code").euploadify("cancel", "*");
			 $("#file_code").euploadify("reset");*/
		}

		function saveSwInfo(optType){
			var hwStr = "";
			var formStr = "swClassAddForm";
			if("edit" == optType){
				formStr = "swClassEditForm";
			}
			$("#" + formStr + " [name='hwObj']").each(function(index, ele){
				if($(this).is(':checked')){
					hwStr += $(this).val() + ",";
				}
			});

			var sw_name = $("#" + optType + "_sw_name").val();
			sw_name = $.trim(sw_name);
			if(sw_name == ""){
				$.messager.alert("系统提示", "软件型号不能为空", "warring");
				return;
			}
			var reg = /^[A-Z]+[\( | A-Z0-9]*[\)|A-Z0-9]*$/g ;
			if(reg.test(sw_name) ){

			}else{
				$.messager.alert("系统提示", "软件型号输入不正确", "warning");
				return false;
			}


			$('#' + formStr).form('submit',{
				url: '${ctx}/product/saveSwInfo?ram=' + new Date().getTime() ,
				onSubmit:function(param){
					$.easyui.loading({ msg: "正在保存...", topMost: true });
					$("#" + optType + "_sw_name").val(sw_name);
					$("#" + optType + "_hwStr").val(hwStr);

					return true; // 返回false终止表单提交
				},
				success: function(data){
					$.easyui.loaded(true);
					if(data == "-1"){
						$.messager.alert("系统提示", "软件型号重复", "info");
					}else{
						$.messager.alert("系统提示", "操作成功", "info");
						var obj = eval('(' + data + ')');
						$("#selSwId").val(obj.id);
						findPrdLi();
						swIdChange(obj.id, obj.name);
						$('#searchproductKey_li').combobox({
							url: '${ctx}/product/findSWBoxvalue?rmd=' + new Date().getTime(),
							valueField: 'prodid',
							textField: 'prodname',
							onSelect: function(record){
								swIdChange(record.prodid, record.prodname);
							}
						});
					}
				}
			});
		}

		function saveSwVersion(){
			//var fileStr = $("#file_code").euploadify("getValues");
			var swVersion = $("#swVersion").val();
			swVersion = $.trim(swVersion);
			if(swVersion == ""){
				$.messager.alert("系统提示", "软件版本号不能为空", "warring");
				return;
			}
			var updateNote = $("#updateNote").val();
			updateNote = $.trim(updateNote);
			if(updateNote.length > 150){
				$.messager.alert("系统提示", "更新说明不能大于150字", "warring");
				return;
			}

			$('#swVersionAddForm').form('submit',{
				url: '${ctx}/product/saveSwVersion?ram=' + new Date().getTime() ,
				onSubmit:function(param){
					$.easyui.loading({ msg: "正在保存...", topMost: true });
					$("#add_swid").val($("#selSwId").val());
					$("#swVersion").val(swVersion);
					$("#updateNote").val(updateNote);

					return true; // 返回false终止表单提交
				},
				success: function(data){
					$.easyui.loaded(true);
					if(data == "-1"){
						$.messager.alert("系统提示", "软件版本号重复", "info");
					}else{
						$.messager.alert("系统提示", "操作成功", "info");
						//var obj = eval('(' + data + ')');
						swIdChange($("#selSwId").val(), $("#edit_sw_name").val());
					}
				}
			});
		}

	</script>
</head>
<body>
<input type="hidden" id="selSwId" name="selSwId" value="" />
<div class="body-outer dclear clearfix" style="width:1020px; margin-top: 15px;">
	<div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
		<div class="table-body page-table-body" style="width:1020px;">
			<!-- ---------------------------head start -------------------------- -->
			<div class="history-save-table-head-backend">
				<div class="history-save-table-head-outer">
					<div class="manage-page-head-left dleft">
						<div class="dleft" style="width:150px;" onclick='findPrdLi()'><a href="javascript:void(0)" style="color:#FFF; font-size:14px;">软件名称</a></div>
					</div>
					<div class="manage-page-head-right dright" >
						<div class="dleft" style="width:670px;">	软件版本信息</div>

					</div>
				</div>
			</div>
			<!-- ---------------------------head end -------------------------- -->
			<div class="historty-table-con-outer-backend dclear">
				<div class="manage-page-inner clearfix" style="width:1010px;">
					<div class="dleft" style="background:#E0E0E0;">
						<div style="background:#E0E0E0; width:197px; height:37px;text-align:center; padding-top:8px; margin-left:3px;border-bottom: 1px solid #cccccc;" id="searchorder_dev">
							<input id="searchproductKey_li" name="searchproductKey_li" style="width:160px; height:30px;padding:6px 12px;" value="输入产品型号搜索" />
						</div>
						<div class="manage-page-left" style="height:538px; margin-bottom: 0px !important;">
							<ul id="productdata">
								<c:forEach items="${swList}" var="item" varStatus="status">
									<li prodLIData="${item[0]}" <c:if test="${status.count == 1}"> class="manage-leftactive" </c:if> style='position:relative;' onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' >
										<a href="javascript:;" onclick="swIdChange('${item[0]}','${item[1]}')" >${item[1]}</a>
										<div class="handle" style="position: absolute; right: 5px; margin-top: -30px; display: none;">
											<img onclick="delProdFun('${item[0]}','${item[1]}')" src="${ctx}/static/images/del.png" style="position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; ">
										</div>
										<c:if test="${status.count == 1}">
											<script type="text/javascript">
												$(function (){
													swIdChange('${item[0]}','${item[1]}');
												});
											</script>
										</c:if>
									</li>
								</c:forEach>
							</ul>
						</div>
						<div style="background:#E0E0E0; width:200px; height:50px;text-align:center;padding-top:5px;">
							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="toAddSWInfoPage()"><strong>&nbsp;&nbsp;创建新型号&nbsp;&nbsp;</strong></a>
						</div>
					</div>
					<div class="easyui-panel" id="swInfoShow" style="display:none; background:#FFF;margin-top:1px; height:650px;width:810px; font-size:14px;" align="right" >
						<div class="easyui-panel" title="模块型号" style="width:805px;height:140px;padding:1px;font-size:14px;" data-options="iconCls:'icon-book-go',tools:'#tt_base'">
							<table class="dclear" style="width:780px;" id="show_hwList_tab">
							</table>
						</div>
						<div id="tt_base">
							<a href="javascript:void(0)" class="icon-edit" onclick="toEditSWInfoPage()"></a>
						</div>
						<div class="easyui-panel" title="软件信息" style="width:805px;padding:0px;font-size:14px;height: 470px;" data-options="iconCls:'icon-book-go',tools:'#tt_module',collapsible:false">
							<table class="dclear" style="width:802px;">
								<tbody id="show_version_tab" valign="top">
								<tr style='color:#000; background-color:#ECECF0;'>
									<td style="width: 90px;" valign="middle">3.0.5.13</td>
									<td width="*"><a href="#" onclick="alert(11)">BM51B_Alpha_HW1.14_SW3.0.5.13(R12337_20160323).s19</a></td>
									<td style="width: 90px;" valign="middle">添加人</td>
									<td style="width: 100px;" valign="middle">2016-03-30</td>
									<td style="width: 40px;"><span class="icon-add">&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
								</tr>

								</tbody>
							</table>
						</div>
						<div id="tt_module">
							<a href="javascript:void(0)" class="icon-add" onclick="javascript:toAddVersionPage()"></a>
						</div>
					</div>
					<div class="easyui-panel" id="swClassAddFormDiv" style="display:none; background:#FFF;margin-top:1px; height:655px;width:810px; font-size:14px;" align="right" >
						<form id="swClassAddForm" action="javascript:;" >
							<input type="hidden" id="add_hwStr" name="hwStr" value="" />
							<input type="hidden" id="add_id" name="id" value="-1" />
							<div class="easyui-panel" title="基本信息" style="width:805px;padding:1px;font-size:14px; height:65px;" data-options="iconCls:'icon-book-go'">
								&nbsp;&nbsp;&nbsp;软件型号：<input type="text"  style="color:#9E9E9E;width:210px;height: 30px;" class="manage-input-text" id="add_sw_name" name="sw_name" value="" maxlength="20" /><font color="red" style="margin-left:6px;">*</font>
							</div>
							<div class="easyui-panel" title="模块型号" style="background:#FFF;margin-top:8px; height:450px;width:805px; font-size:14px; border:0;" data-options="iconCls:'icon-book-go'">
								<table class="dclear" style="width:750px;" id="add_hwList_tab">
								</table>
							</div>
							<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
							<div class="manage-page-right-backend dright" style="margin-top: 20px; width:805px; text-align:center;background:#FFF;">
								<input type="button" value=" " onclick="saveSwInfo('add')"  class="manage-page-submit-button2" />
							</div>
						</form>
					</div>
					<div class="easyui-panel" id="swClassEditFormDiv" style="display:none; background:#FFF;margin-top:1px; height:655px;width:810px; font-size:14px;" align="right" >
						<form id="swClassEditForm" action="javascript:;" >
							<input type="hidden" id="edit_hwStr" name="hwStr" value="" />
							<input type="hidden" id="edit_id" name="id" value="" />
							<div class="easyui-panel" title="基本信息" style="width:805px;padding:1px;font-size:14px; height:65px;" data-options="iconCls:'icon-book-go'">
								&nbsp;&nbsp;&nbsp;软件型号：<input type="text"  style="color:#9E9E9E;width:210px;height: 30px;" class="manage-input-text" id="edit_sw_name" name="sw_name" value="" maxlength="20" /><font color="red" style="margin-left:6px;">*</font>
							</div>
							<div class="easyui-panel" title="模块型号" style="background:#FFF;margin-top:8px; height:450px;width:805px; font-size:14px; border:0;" data-options="iconCls:'icon-book-go'">
								<table class="dclear" style="width:750px;" id="edit_hwList_tab">

								</table>
							</div>
							<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
							<div class="manage-page-right-backend dright" style="margin-top: 20px; width:805px; text-align:center;background:#FFF;">
								<input type="button" value=" " onclick="saveSwInfo('edit')"  class="manage-page-submit-button2" /> <!-- onclick="return updateOrder('add')" -->
							</div>
						</form>
					</div>

					<form id="swVersionAddForm" action="javascript:;" style="display: none;">
						<input type="hidden" id="add_version_id" name="version_id" value="" />
						<input type="hidden" id="add_swid" name="swid" value="" />
						<div class="easyui-panel" style="display:block; background:#FFF;margin-top:1px; height:640px;width:810px; font-size:14px;" align="right" >
							<div class="easyui-panel" title="软件版本信息添加" style="width:805px;height:620px;padding:1px;font-size:14px" align="center"	data-options="iconCls:'icon-book-go'">
								<table class="dclear" style="width:630px; margin-top: 10px;" >
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:45px;">软件型号：</td>
										<td style="text-align:left; border:0px; height:37px; width: 180px;" id="show_version_sw_name">
										</td>
										<td style="text-align:right; width:120px; border:0px; height:45px;">软件版本号：</td>
										<td style="text-align:left; border:0px; height:37px;" width="*">
											<input type="text" id="swVersion" name="swVersion" style="color:#9E9E9E;width:210px;height: 30px;" class="manage-input-text" maxlength="20" value="" onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" /><font color="red">*</font>
										</td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:45px;">软件上传：</td>
										<td style="text-align:left; border:0px; height:45px;" width="*" colspan="3">
											<input id="file_code" name="file_code" class="easyui-euploadify" type="text" style="margin-left:180px;" data-options="
																						   width: 500,
																								//height: 400,
																								multi: true,
																								multiTemplate: 'uploadify',
																								auto: true,
																								showStop: false,
																								showCancel: false,
																								preventCaching: false,
																								tooltip: false,
																								required: true,
																								formData: {orderno:'', device_code:'', device_class:'' , device_type:'', limit_num:1 },
																								fileSizeLimit: '10MB',
																								fileTypeExts: '*.s19;*.zip;*.rar;',
																								swf: '${ctx}/static/js/uploadify/uploadify.swf',
																								uploader: '${ctx}/fileOperate/uploadModuleConfigFile'" />
										</td>
									</tr>
									<tr>
										<td style="text-align:right; width:90px; border:0px; height:45px;">更新说明：</td>
										<td style="text-align:left; border:0px; height:45px;" width="*" colspan="3">
											<textarea id="updateNote" name="updateNote" rows="10" style="width: 500px; margin-top: 2px;"></textarea>
										</td>
									</tr>
								</table>
								<hr style="height:1px;border:none;border-top:1px solid #E0E0E0; margin-top:5px;">
								<div class="manage-page-right-backend dright" style="margin-top: 20px; width:810px; text-align:center; background:#FFF;">
									<input type="button" value=" " onclick="saveSwVersion()"  class="manage-page-submit-button2" />
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="page-footer-backend dclear">
		</div>
	</div>
</div> <!-- //body outer end -->
<div id="test_dialog" style="display:none;">
	<table class="dclear" style="width:530px;margin-top:5px;font-size:14px; margin-left:35px;" >
		<tr>
			<td style="text-align:right; width:90px; border:0px; height:37px;">软件类型：</td>
			<td style="text-align:left; border:0px; height:37px;" width="*" id="dialog_sw_code"></td>
		</tr>
		<tr>
			<td style="text-align:right; width:90px; border:0px; height:37px;">版&nbsp;本&nbsp;&nbsp;号：</td>
			<td style="text-align:left; border:0px; height:37px;" width="*" id="dialog_sw_version"></td>
		</tr>
		<tr>
			<td style="text-align:right; width:90px; border:0px; height:37px;">添&nbsp;加&nbsp;&nbsp;人：</td>
			<td style="text-align:left; border:0px; height:37px;" width="*" id="dialog_sw_user"></td>
		</tr>
		<tr>
			<td style="text-align:right; width:90px; border:0px; height:37px;">添加时间：</td>
			<td style="text-align:left; border:0px; height:37px;" width="*" id="dialog_sw_time"></td>
		</tr>
		<tr>
			<td style="text-align:right; width:90px; border:0px; height:37px;">文件名称：</td>
			<td style="text-align:left; border:0px; height:37px;" width="*" id="dialog_sw_file"></td>
		</tr>
		<tr>
			<td style="text-align:right; width:90px; border:0px; height:37px;" valign="middle">更新说明：</td>
			<td style="text-align:left; border:0px; height:37px;" width="*" id="dialog_sw_updatenote" ></td>
		</tr>
	</table>
</div>
</body>
</html>