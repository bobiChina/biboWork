<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta name="menu" content="backend_simcard"/>
	<title>SIM卡管理</title>
	<link rel="stylesheet" href="${ctx}/static/styles/css.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/easyui.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/icon.css" />
	<link rel="stylesheet" href="${ctx}/static/styles/css4easyui.css" />

	<script src="${ctx}/static/js/ligao.global.js"></script>
	<script src="${ctx}/static/js/jquery.easyui.min.js"></script>
	<script src="${ctx}/static/js/easyui-lang-zh_CN.js"></script>
	<script src="${ctx}/static/js/jquery.jdirk.js"></script>
	<script src="${ctx}/static/js/jeasyui.extensions.js"></script>

<style>
.datagrid-header-inner {
    background: #AAAAAB;
    float: left;
    width: 1013px;
}
</style>

<script type="text/javascript">
var corpArr = [];
$(function(){
	//====== 用户信息tip
	$(".user-login-arrow").tipsbox({
		"tipsLeft":"-20px",
		"tipsTop":"20px"
	});

	$('#dg').datagrid({
    		rownumbers:true,
    		singleSelect: false,
    		url: '${ctx}/simcard/getTableData',
    		autoRowHeight:false,
    		pagination:true,
    		pageSize:20,
    		width:1013,
    		toolbar: '#tb' ,
    		onLoadSuccess: function(data){
				 if (data.rows.length > 0) {
				 	//循环判断操作为新增的不能选择
				 	for (var i = 0; i < data.rows.length; i++) {
				 		//根据operate让某些行不可选
				 		if (data.rows[i].status == "2") {
				 			$("input[type='checkbox']")[i + 1].disabled = true;
				 		}
				 	}
				 }
    		},
    		onClickRow: function(rowIndex, rowData){
    			//加载完毕后获取所有的checkbox遍历
    			$("input[type='checkbox']").each(function(index, el){
    				//如果当前的复选框不可选，则不让其选中
    				if (el.disabled == true) {
    					$('#dg').datagrid('unselectRow', index - 1);
    				}
    			})
      },
			onCheckAll: function(rows) {
				$("input[type='checkbox']").each(function(index, el) {
					if (el.disabled) {
						$("#dg").datagrid('uncheckRow', index - 1);//此处参考其他人的代码，原代码为unselectRow
					}
				})
			},
    	 });
});

function formatStatus(val,row){
	if(val == "1"){
		return '<span style="color:blue;">正常</span>';
	}else if(val == "2"){
		return '<span style="color:red;">已删除</span>';
	}else{
		return '<span style="color:red;">未知</span>';
	}
}

//查询
function searchInfo(){
	searchNormalFun("dg", {"keywords": $("#keywords").val()});
}

//删除
function delSim(){
	var rows = $('#dg').datagrid('getSelections');
	var ids = "";
	for(var i=0; i<rows.length; i++){
		ids += rows[i].id + ",";
	}
	if("" == ids){
		$.messager.alert("系统提示", "请选择删除项", "warning");
	}else{
		$.messager.confirm("删除确认", "是否确认删除？", function(cr){
			if(cr){
				$.post("${ctx}/simcard/delSimCard", {ids: ids},function(data){
					var msg = "操作成功";
					if(data != "ok"){
						msg = "操作失败";
					}
					$.messager.alert("系统提示", msg, "info", function(){
					});
					searchInfo();
				 });
			}
		});
	}
}

//附件上传开始
var timer1 ;
var filepath = "";
function selectUpload()
{
	$("#ifrobj")[0].contentWindow.document.getElementById("upfile").click();

	timer1 = setInterval(function(){
		//获取上传路径,定时扫描,及时销毁
		try{
			filepath = $("#ifrobj")[0].contentWindow.document.getElementById("upfile").value;
		}catch(e){
		}
		//当文件不等于空时执行的操作
		if (filepath != "")
		{
			//启动遮罩层
			$.easyui.loading({ msg: "正在处理...", topMost: true });
			//执行附件上传
			$("#ifrobj")[0].contentWindow.document.getElementById("submitBtu").click();//提交上传文件
		}
	},200);
}

//附件回调方法
function getFJS(newFileName, backName, errorMsg) {
	//记得一定要加上，还原ifram的定向
    document.getElementById("ifrobj").src='${ctx}/fileOperate/singleUploadFile?fileType=xls&backmethod=getFJS';
	if(errorMsg != ""){
		$.messager.alert("系统提示", errorMsg, "warning");
	}else{
		filepath = ""; //清除原纪录
		if(null != timer1){
			clearInterval(timer1); //终止扫描
		}
		//处理业务
		$.post("${ctx}/simcard/saveSimCard", {filename: newFileName},function(data){
			$.easyui.loaded(true);//清除加载框
			$.messager.alert("系统提示", data, "info");
			searchInfo();
		 });
	}
 }
 // 附件上传结束
</script>
</head>
<body>
<div class="body-outer dclear clearfix" style="width:1013px;">
			<div class="page-table-body" align="center" style="padding-top:9px;width:1013px;">
				<table id="dg" style="width:1013px;height:729px;">
                	<thead>
                		<tr>
                			<th data-options="field:'id',width:70,align:'center', halign:'center', checkbox:true"></th>
                			<th data-options="field:'ccid',width:220,align:'center', halign:'center', sortable:true">CCID</th>
                			<th data-options="field:'sim',width:200,align:'center', halign:'center', sortable:true">SIM</th>
                			<th data-options="field:'status',width:150,align:'center', halign:'center',formatter:formatStatus">状态</th>
                			<th data-options="field:'optname',width:150,align:'center', halign:'center'">操作人</th>
                			<th data-options="field:'opttime',width:180,align:'center', halign:'center', sortable:true">操作时间</th>
                		</tr>
                	</thead>
                </table>
			</div>
			<div id="tb" style="height:20px; padding:17px 10px;background: url('${ctx}/static/images/table_header_sim.gif') no-repeat center top;">
				<table style="width:1000px;" cellspadding="0" colspadding="0" border="0">
					<tr>
						<td style="width:70px;" align="right">
							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="selectUpload()">导入</a>&nbsp;
						</td>
                        <td style="width:65px;" align="right">
                        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-hamburg-busy',plain:true" onclick="delSim()">删除</a>&nbsp;
						</td>
						<td style="width:185px;" >
							<input type="text" name="keywords" style="margin-left:20px;height:23px;" id="keywords" value="" placeholder="CCID或SIM" maxlength="20" />
						</td>
						<td style="width:120px;" >
							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchInfo()">搜索</a>
						</td>
						<td width="*" align="right">
							<a href="${ctx}/static/download/simcardTemplate.xls" style="margin-right:20px;color:#FFF;" >模板下载</a>
						</td>
					</tr>
				</table>
			</div>
</div> <!-- //body outer end -->
<iframe src="${ctx}/fileOperate/singleUploadFile?fileType=xls&backmethod=getFJS" name="ifrobj" id="ifrobj" frameborder="0" scrolling="no" width="0" height="0"></iframe>
</body>
</html>