<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
  <meta name="menu" content="task"/>
  <title>任务管理</title>
  <link rel="stylesheet" href="${ctx}/static/styles/css.css" />
  <link rel="stylesheet" href="${ctx}/static/styles/jquery.mCustomScrollbar.min2.css" />
  <link rel="stylesheet" href="${ctx}/static/styles/easyui.css" />
  <link rel="stylesheet" href="${ctx}/static/styles/icon.css" />
  <link rel="stylesheet" href="${ctx}/static/styles/css4easyui.css" />

  <script src="${ctx}/static/js/ligao.global.js"></script>
  <script src="${ctx}/static/js/jquery.mCustomScrollbar.concat.min.js"></script>
  <script src="${ctx}/static/js/jquery.easyui.min.js"></script>
  <script src="${ctx}/static/js/easyui-lang-zh_CN.js"></script>
  <script src="${ctx}/static/js/jquery.jdirk.js"></script>
  <script src="${ctx}/static/js/jeasyui.extensions.js"></script>
  <script src="${ctx}/static/js/jeasyui.extensions.all.min.js"></script>
  <style>
    .panel-body {
      border-width: 1px;
      border-color: #E0E0E0;
      border-right: none;
      border-left: none;
    }
    .datagrid-header-inner {
      background: #AAAAAB;
      float: left;
      width: 1010px;
    }
  </style>

  <script type="text/javascript">
    $(function(){
      //====== 用户信息tip
      $(".user-login-arrow").tipsbox({
        "tipsLeft":"-20px",
        "tipsTop":"20px"
      });
      getOrderForCorp();
    });

    function getOrderForCorp(){
      // EASYUI列表部分
      $('#dg_order').datagrid({
        rownumbers:true,
        toolbar:'#search_dg_order',
        singleSelect: true,
        //url: '${ctx}/order/getOrderDataByCorp?corpid=11&rmd=' + new Date().getTime(),
        autoRowHeight:false,
        enableHeaderContextMenu:false,//表头右键
        enableRowContextMenu:false,//行右键
        onLoadSuccess: function(data){

        },
        pagination:true,
        pageSize:20,
        width:1008,
        height:625
      });
    }
  </script>
</head>
<body>
<div class="body-outer dclear clearfix" style="width:1020px;">
  <div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
    <div class="page-table-body" style="width:1020px;">
      <!-- ---------------------------head start -------------------------- -->
      <div class="history-save-table-head-backend">
        <div class="history-save-table-head-outer">
          <div class="manage-page-head-left dleft">
            <div class="dleft" style="width:950px;color:#FFF; font-size:18px; font-weight: 700;">任务列表</div>
          </div>
          <div class="manage-page-head-right dright" >
            <shiro:hasRole name="admin">
            <div class="dright" style="width:24px;display:block;"><a href="${ctx}/otaTask/addPage" ><img src="${ctx}/static/images/add.png" style="width:24px;margin-top:10px;" title="添加任务" alt="添加任务" /></a></div>
            </shiro:hasRole>
          </div>
        </div>
      </div>
      <!-- ---------------------------head end -------------------------- -->
      <div class="historty-table-con-outer-backend dclear">
        <div class="manage-page-inner clearfix" style="width:1010px;">
          <div class="easyui-panel" id="panel_task" style="width:1010px;font-size:14px;height: 645px;padding: 0; margin-top: 55px;" >
            <table id="dg_order">
              <thead>
              <tr>
                <th data-options="field:'orderno',width:140,align:'center', halign:'center', sortable:true">订单编号</th>
                <th data-options="field:'corp_name',width:200,align:'center', halign:'center', sortable:true">客户名称</th>
                <th data-options="field:'project_note',width:202,align:'left', halign:'center'">项目描述</th>
                <th data-options="field:'update_num',width:100,align:'center', halign:'center', sortable:true">升级数量</th>
                <th data-options="field:'opttime',width:130,halign:'center',align:'center', halign:'center', sortable:true">升级日期</th>
                <th data-options="field:'opt_user_name',width:90,align:'center', halign:'center', sortable:true">状态</th>
                <th data-options="field:'id',width:90,halign:'center',align:'center', halign:'center'">详情</th>
              </tr>
              </thead>
            </table>
            <div id="search_dg_order" style="height:38px; line-height: 38px; padding-left: 10px; font-size: 14px;">
              订单编号：<input type="text" class="manage-input-text" name="dg_order_keyword" id="dg_order_keyword" style="width: 195px;" />
              <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="searchInfo()">查询</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="page-footer-task dclear"></div>
  </div>
</div>

</body>
</html>
