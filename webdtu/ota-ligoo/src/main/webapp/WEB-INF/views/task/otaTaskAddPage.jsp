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
    }
    .datagrid-header-inner {
      background: #AAAAAB;
      float: left;
      width: 936px;
    }
   .datagrid-cell, .datagrid-header-rownumber, .datagrid-cell-rownumber .datagrid-cell-rownumber {
     margin: 0;
     padding: 0 4px;
     white-space: nowrap;
     word-wrap: normal;
     overflow: hidden;
     height: 35px;
     line-height: 35px;
     font-size: 14px;
   }

   .datagrid-cell-rownumber {
     margin: 0;
     padding: 0 4px;
     white-space: nowrap;
     word-wrap: normal;
     overflow: hidden;
     height: 35px;
     line-height: 35px;
     font-size: 14px;
   }
  </style>

  <script type="text/javascript">
    $(function(){
      //====== 用户信息tip
      $(".user-login-arrow").tipsbox({
        "tipsLeft":"-20px",
        "tipsTop":"20px"
      });

    //  alert("111");
      //初始化设备
      getOrderDevice("");

      var orderArr = [];
      var orderStrArr = [];
      <c:forEach items="${orderList}" var="item" varStatus="status">orderArr.push({"orderno":"${item}", "ordername": "${item}"});orderStrArr.push("${item}");</c:forEach>
      $('#searchorderKey_li').combobox({
        valueField: 'orderno',
        textField: 'ordername',
        data: orderArr,
        onSelect: function(record){
          //orderNoChange(record.orderno);
        },
        onChange: function(newValue, oldValue){
          if(contains(orderStrArr, newValue)){
            orderNoChange(newValue);
            getOrderDevice(newValue);
          }
        },
        onLoadSuccess: function(){
          $("#searchorder_dev").find(".combo").addClass("combobg");
          $("#searchorder_dev").find(".combo-text").addClass("combobg");
          //默认显示value的内容
          var p = $("input[name='searchorderKey_li']").closest("span.combo,div.combo-p,div.menu");//向上查找
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

    function orderNoChange(orderno){
      $("#show_order_info").html("");
      $("#show_info_s19").html("");
      $("#show_info_cfg").html("");
      $("#show_info_csf").html("");

      $("#selOrderNo").val("");
      $.post("${ctx}/otaTask/findOrderInfo?rmd=" + new Date().getTime(), {orderno : orderno},function(data){
        if(data == "-1"){
          $.messager.alert("系统提示", "订单信息获取失败", "error");
          return ;
        }
        var obj = eval('(' + data + ')');
        $("#show_order_info").html(obj.corp_name + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + obj.project_note + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + obj.quantity + "套");
        $("#selOrderNo").val(orderno);
        var order_shArr = obj.order_sh ;
        $("#order_sw").empty();
        $("#order_sw").append("<option value=''>请选择</option>");
        if(!String.isNullOrEmpty(order_shArr)){
          for(var i=0; i<order_shArr.length; i++){
            $("#order_sw").append("<option value='" + order_shArr[i] + "'>" + order_shArr[i] + "</option>");
          }
        }
      });
    }

    function orderSHChange(obj){
      $("#show_info_s19").html("");
      $("#show_info_cfg").html("");
      $("#show_info_csf").html("");

      var orderno = obj.value;

      if("" == orderno){
        return ;
      }

      $.post("${ctx}/otaTask/findOrderFile?rmd=" + new Date().getTime(), {orderno : orderno},function(data){
        if(data == ""){
          $.messager.alert("系统提示", "订单软件信息获取失败", "error");
          return ;
        }
        var obj = eval('(' + data + ')');
        $("#show_info_s19").html(obj.s19[1]);
        $("#show_info_cfg").html(obj.cfg[1]);
        $("#show_info_csf").html(obj.csf[1]);

      });
    }

    function getOrderDevice(orderno){
      // EASYUI列表部分
      $('#dg_order').datagrid({
        rownumbers:true,
        singleSelect: false,
        url: '${ctx}/otaTask/findOrderDevice?orderno=' + orderno + '&rmd=' + new Date().getTime(),
        autoRowHeight:false,
        enableHeaderContextMenu:true,//表头右键
        enableRowContextMenu:false,//行右键
        onLoadSuccess: function(data){

        },
  /*      pagination:true,
        pageSize:20,*/
        width:970,
        height:350
      });
    }
    function formatStatus(val,row){
      if(val == 1){
        return "放电";
      }else if(val == 2){
        return "充电";
      }else if(val == 3){
        if(row.hours < 24){
          return "离线(" + row.hours + "H)";
        }
        return "离线(" + window.parseInt(row.hours/24) + "天)";
      }else{
        return "&nbsp;";
      }
    }
    //保存设备选项
    function saveUpdateDevice(){
      //检查设备绑定情况
      var rows = $('#dg_order').datagrid('getSelections');
      var ids = "";
      for(var i=0; i<rows.length; i++){
        ids += rows[i].id + ",";
      }
      if("" == ids){
        $.messager.alert("系统提示", "请选择升级设备", "warning");
        return ;
      }else{
        var order_sw = $("#order_sw").val();
        if(order_sw == ""){
          $.messager.alert("系统提示", "请选择升级软件", "warning");
          return ;
        }
        $.messager.confirm("升级任务确认", "是否确认保存已选中设备？", function(cr){
          if(cr){
            $('#hiddenForm').form('submit',{
              url: '${ctx}/otaTask/saveTaskDevice?ram=' + new Date().getMilliseconds() ,
              onSubmit:function(param){
                $.easyui.loading({ msg: "正在保存...", topMost: true });
                $("#hiddenForm_orderno").val($("#selOrderNo").val());
                $("#hiddenForm_order_sw").val($("#order_sw").val());
                $("#hiddenForm_devices").val(ids);

                return true;
              },
              success: function(data){
                $.easyui.loaded(true);
                alert("OK");
              }
            });
          }else{
            //TODO
          }
        });
      }

    }
  </script>
</head>
<body>
<input type="hidden" id="selOrderNo" name="selOrderNo" value="" />
<div class="body-outer dclear clearfix" style="width:1020px;">
  <div class="table-box-backend page-manage-box-outer dclear clearfix" style="width:1020px;">
    <div class="page-table-body" style="width:1020px;">
      <!-- ---------------------------head start -------------------------- -->
      <div class="history-save-table-head-backend">
        <div class="history-save-table-head-outer">
          <div class="manage-page-head-left dleft">
            <div class="dleft" style="width:950px; color:#FFF; font-size:18px; font-weight: 700;">添加任务</div>
          </div>
          <div class="manage-page-head-right dright" >
            <div class="dright" style="width:24px;display:block;"><a href="${ctx}/otaTask/page" ><img src="${ctx}/static/images/back2.png" style="width:24px;margin-top:10px;" title="返回列表" alt="返回列表" /></a></div>
          </div>
        </div>
      </div>
      <!-- ---------------------------head end -------------------------- -->
      <div class="historty-table-con-outer-backend dclear">
        <div class="manage-page-inner clearfix" style="width:1010px;">
          <div class="easyui-panel" id="panel_order" style="width:1010px;font-size:14px;height: 80px;padding: 0; margin-top: 55px;  border-left: none; border-bottom: none; border-right: none;" >
            <div style="width:900px; color:#000; font-size:18px; font-weight: 700; margin: 10px 0 10px 20px;">选择设备</div>
            <div class="dleft" style="width:270px; color:#000; font-size:14px; margin-left: 20px;">
              订单编号：<input id="searchorderKey_li" name="searchorderKey_li" style="width:160px; height:30px;padding:6px 12px;" value="输入订单编号搜索" />
            </div>
            <div class="dright" style="width:700px; color:#000; font-size:14px; height: 35px; line-height: 35px;" id="show_order_info">
              &nbsp;
            </div>
          </div>
          <div class="easyui-panel" style="width:990px;font-size:14px;height: 350px;padding: 0; margin-left: 20px; border: none;" >
            <table id="dg_order">
              <thead>
              <tr>
                <th data-options="field:'id',width:40,align:'center', halign:'center', checkbox:true"></th>
                <th data-options="field:'device_code',width:90,align:'center', halign:'center', sortable:true">设备型号</th>
                <th data-options="field:'uuid_show',width:145,align:'center', halign:'center', sortable:true">UUID</th>
                <th data-options="field:'p_sn',width:120,align:'center', halign:'center'">产品SN码</th>
                <th data-options="field:'vehicle_number',width:120,align:'center', halign:'center', sortable:true">车架号</th>
                <th data-options="field:'city',width:100,halign:'center',align:'center', halign:'center', sortable:true">城市</th>
                <th data-options="field:'hw_version',width:80,align:'center', halign:'center', sortable:true">硬件版本</th>
                <th data-options="field:'sw_version',width:120,align:'center', halign:'center', sortable:true">软件版本</th>
                <th data-options="field:'status',width:100,halign:'center',align:'center', halign:'center', formatter:formatStatus">状态</th>
              </tr>
              </thead>
            </table>
          </div>
          <div class="easyui-panel" id="panel_software" style="width:990px;font-size:14px;height: 240px;padding-left: 20px; border-left: none; border-top: none; border-right: none;" >
            <div style="width:900px; color:#000; font-size:18px; font-weight: 700; margin: 15px 0 5px 0;">选择软件</div>
            <div style="width:900px; color:#000; font-size:14px; margin-left: 6px;margin-bottom: 8px;">
              订单编号：<select id="order_sw" name="order_sw" onchange="orderSHChange(this)" style="margin-left: 13px;">
              <option value="">请选择</option>
            </select>
            </div>
            <div class="easyui-panel" style="width:970px;font-size:14px;height: 80px; " >
              <table style="margin-left: 5px;margin-top: 5px;">
                <tr>
                  <td style="width: 80px; height: 22px;">订单固件：</td>
                  <td style="width: 770px;" id="show_info_s19"></td>
                </tr>
                <tr>
                  <td style="width: 80px; height: 22px;">配置文件：</td>
                  <td style="width: 770px;" id="show_info_cfg"></td>
                </tr>
                <tr>
                  <td style="width: 80px; height: 22px;">规则文件：</td>
                  <td style="width: 770px;" id="show_info_csf"></td>
                </tr>
              </table>
            </div>
            <div style="width: 1010px; margin-top: 10px; background: #FFF;" align="center" >
              <input type="button" value=" " onclick="return saveUpdateDevice()" class="manage-page-submit-button">
              <%-- manage-page-submit-button-up --%>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="page-footer-task dclear"></div>
  </div>
</div>
<form id="hiddenForm" class="easyui-form" method="post" action="${ctx}/otaTask/saveTaskDevice" data-options="novalidate:true" style="display: none;">
  <input id="hiddenForm_orderno" name="orderno" value="" type="hidden" />
  <input id="hiddenForm_devices" name="devices" value="" type="hidden" />
  <input id="hiddenForm_order_sw" name="order_sw" value="" type="hidden" />
</form>
</body>
</html>
