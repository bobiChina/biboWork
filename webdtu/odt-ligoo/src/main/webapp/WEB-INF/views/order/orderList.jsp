<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
  <meta name="menu" content="orderService"/>
  <title>订单列表</title>
  <link rel="stylesheet" href="${ctx}/static/styles/css.css" />
  <link rel="stylesheet" href="${ctx}/static/styles/easyui.css" />
  <link rel="stylesheet" href="${ctx}/static/styles/icon.css" />
  <link rel="stylesheet" href="${ctx}/static/styles/css4easyui.css" />

  <script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
  <script src="${ctx}/static/js/ligao.global.js"></script>
  <link rel="stylesheet" href="${ctx}/static/styles/page.css" />


  <script type="text/javascript">

    $(function(){
      search();
      $("#corp_id").change(
          function (){
            search();
          }
      );

      $("#salesman").change(
          function (){
            search();
          }
      );

      $("#orderstatus").change(
          function (){
            search();
          }
      );
      $("#order_type").change(
          function (){
            search();
          }
      );

      $("#input-box-submit").click(
          function (){
            $("#orderstatus").val(-1);
            $("#salesman").val(-1);
            $("#corp_id").val(-1);
            $("#order_type").val(-1);
            search();
          }
      );

      $(".search-input-text").inputTipsText();

      //用户登陆tips
      $(".user-login-arrow").tipsbox({
        "tipsLeft":"-20px",
        "tipsTop":"20px"
      });
    });

    function search() {
      var orderno = "";
      if ($("#orderno").val() == "请输入订单编号或合同编号直接搜索")
        orderno = "";
      else
        orderno = $("#orderno").val();
      var timestamp = new Date();
      $("#page").myPagination({
        currPage : 1,
        pageCount : 0,
        pageSize : 5,
        cssStyle:'yahoo',
        info: {
          first_on:true,
          last_on:true
        },
        ajax : {
          on : true, //开启状态
          callback : 'ajaxCallBack', //回调函数，注，此 ajaxCallBack 函数，必须定义在 $(function() {}); 外面
          url : '${ctx}/orderService/listQuery',//访问服务器地址
          dataType : 'json', //返回类型
          param : {
            on : true,
            page : 1,
            pageCountId : 'pageCount',
            orderno:orderno,
            salesman: $("#salesman").val(),
            corp_id: $("#corp_id").val(),
            orderstatus: $("#orderstatus").val(),
            order_type: $("#order_type").val()
          }
          //参数列表，其中on 必须开启，page 参数必须存在，其他的都是自定义参数，如果是多条件查询，可以序列化表单，然后增加 page参数
        }
      });
    }

    function ajaxCallBack(data) {
      $("#hidPageNumber").val(data.pageNumber);
      var result = data.result;
      var startNumber = (data.pageNumber-1)*15;
      var insetViewData = ""; //视图数据

      if(result != ""){
        $.each(result, function(index, items){
          insetViewData += createTR(startNumber+index+1,items);
          index += 1;
        });

      }else{
        insetViewData+= "<tr align='center'><td colspan='8' bgcolor='#FFFFFF' style='text-align:center;'><font color='red'>无记录...</font></td></tr>";
      }
      $("#container > tbody").html(insetViewData);
      $(".table-body").fixedHead({
        nums: 10
      });
      $(".table-body").setThead({
        "setColumnsWidth":[50,170,320,110,100,100,100,100]
      });
    }

    function createTR(index,obj) {
      var tr = "<tr>";
      tr += "<td>" + index + "</td>";
      tr += "<td><a target='_blank' href='${ctx}/orderService/detail?orderno="+obj.orderno+"'>" + obj.orderno + "</a></td>";
      tr += "<td style='text-align: left;'>&nbsp;&nbsp;" + obj.corp_name + "</td>";
      tr += "<td>" + obj.salesman + "</td>";
      tr += "<td>" + obj.quantity + "套</td>";

      if(obj.status == 0){
        tr += "<td>新订单</td>";
        tr += "<td>-</td>";
      }
      else if(obj.status == 1){
        tr += "<td>评审中</td>";
        tr += "<td>-</td>";
      }
      else if(obj.status == 2){
        tr += "<td>待接收</td>";
        tr += "<td>" + ( obj.review_delivery == undefined ? "-" : obj.review_delivery ) + "</td>";
      }
      else if(obj.status == 3){
        tr += "<td>待开发</td>";
        tr += "<td>" + ( obj.review_delivery == undefined || obj.review_delivery == null || obj.review_delivery == ""  ? "-" : obj.review_delivery ) + "</td>";
      }
      else if(obj.status == 4){
        tr += "<td>开发中</td>";
        tr += "<td>" + ( obj.review_delivery == undefined || obj.review_delivery == null || obj.review_delivery == ""  ? "-" : obj.review_delivery ) + "</td>";
      }
      else if(obj.status == 5){
        tr += "<td>待测试</td>";
        tr += "<td>" + ( obj.review_delivery == undefined || obj.review_delivery == null || obj.review_delivery == ""  ? "-" : obj.review_delivery ) + "</td>";
      }
      else if(obj.status == 6){
        tr += "<td>测试中</td>";
        tr += "<td>" + ( obj.review_delivery == undefined || obj.review_delivery == null || obj.review_delivery == ""  ? "-" : obj.review_delivery ) + "</td>";
      }
      else if(obj.status == 7){
        tr += "<td>待上传</td>";
        tr += "<td>" + ( obj.review_delivery == undefined || obj.review_delivery == null || obj.review_delivery == ""  ? "-" : obj.review_delivery ) + "</td>";
      }
      else if(obj.status == 8){
        tr += "<td>已齐套</td>";
        tr += "<td>" + ( obj.review_delivery == undefined || obj.review_delivery == null || obj.review_delivery == ""  ? "-" : obj.review_delivery ) + "</td>";
      }
      else if(obj.status == 9){
        tr += "<td><font color='red'><b>暂停</b></font></td>";
        tr += "<td>-</td>";
      }
      else if(obj.status == 10){
        tr += "<td><font color='red'><b>取消</b></font></td>";
        tr += "<td>-</td>";
      }
      tr += "<td><a target='_blank' href='${ctx}/orderService/detail?orderno="+obj.orderno+"'>" + "查看" + "</a></td>";

      tr += "</tr>";
      return tr;
    }

  </script>

  <style>
    .btn-sm {
      margin-top: 22px;
      margin-left: 60px;
      padding: 5px 10px;
      font-size: 16px;
      line-height: 1.5;
      border-radius: 3px;
    }
    .btn-success {
      color: #fff;
      background-color: #5cb85c;
      border-color: #4cae4c;
    }
    .btn {
      display: inline-block;
      padding: 6px 12px;
      margin-bottom: 0;
      font-size: 14px;
      font-weight: 400;
      line-height: 1.42857143;
      text-align: center;
      white-space: nowrap;
      vertical-align: middle;
      cursor: pointer;
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
      background-image: none;
      border: 1px solid transparent;
      border-radius: 4px;
    }
  </style>
</head>
<body>
<div class="body-outer dclear clearfix">
  <div class="form-box clearfix">
    <div class="input-box dleft" >
      <input type="text"  name="orderno" id ="orderno" class="search-input-text" value="请输入订单编号或合同编号直接搜索"/>
    </div>
    <input type="button" class="dleft" id="input-box-submit" value="搜&nbsp;&nbsp;索" />
  </div> <!-- //form end -->
  <input type="hidden" id="hidPageNumber" value="1"/>

  <div class="table-box dclear clearfix" style="display: block;">
    <div class="table-body" style="min-height:487px;">
      <table id="container" class="dclear" width="100%">
        <thead class="displavisibility">
        <tr>
          <th style="width: 50px; text-align: center;">序号</th>
          <th style="width: 170px; text-align: center;">
            <select id="order_type" name="order_type">
              <option value="-1">订单编号</option>
              <option value="LG2">新订单</option>
              <option value="LG-SH-">售后订单</option>
            </select>
          </th>
          <th style="width: 260px; text-align: center;">
            <select id="corp_id" name="corp_id">
              <option value="-1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;客户名称</option>
              <c:forEach items="${corpList}" var="corpItem">
                <c:if test="${fn:length(corpItem[1])>'9'}"><option value="${corpItem[0]}">${fn:substring(corpItem[1], 0, 9)}...</option></c:if>
                <c:if test="${fn:length(corpItem[1])<'10'}"><option value="${corpItem[0]}">${corpItem[1]}</option></c:if>
              </c:forEach>
            </select>
          </th>
          <th style="width: 90px; text-align: center;">
            <select id="salesman" name="salesman">
              <option value="-1">销售代表</option>
              <c:forEach items="${salesmanList}" var="salesmanItem">
                <c:if test="${fn:length(salesmanItem[1])>'4'}"><option value="${salesmanItem[0]}">${fn:substring(salesmanItem[1], 0, 4)}...</option></c:if>
                <c:if test="${fn:length(salesmanItem[1])<'5'}"><option value="${salesmanItem[0]}">${salesmanItem[1]}</option></c:if>
              </c:forEach>
            </select>
          </th>
          <th style="width: 90px; text-align: center;">数量</th>
          <th style="width: 90px; text-align: center;">
            <select id="orderstatus" name="orderstatus">
              <option value="-1">订单状态</option>
              <option value="0">新订单</option>
              <option value="1">评审中</option>
              <option value="2">待接收</option>
              <option value="3">待开发</option>
              <option value="4">开发中</option>
              <option value="5">待测试</option>
              <option value="6">测试中</option>
              <option value="7">待上传</option>
              <option value="8">已齐套</option>
              <option value="9">暂停</option>
              <option value="10">取消</option>
            </select>
          </th>
          <th style="width: 90px; text-align: center;">预计交期</th>
          <th style="width: 90px; text-align: center;">订单详情</th>
         </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
    </div>
    <div class="table-footer dclear">
      <div id="page" style="float:right;margin-right:20px" class="yahoo">
      </div>
    </div>
  </div>
  <div align="center" style="width: 970px; margin-top: 15px; display: none;">
    <table id="dg">
      <thead>
      <tr>
        <th data-options="field:'action_date',width:114,align:'center', halign:'center', sortable:true">事件日期</th>
        <th data-options="field:'action_name',width:70,align:'center', halign:'center', sortable:true">事件</th>
        <th data-options="field:'order_status',width:70,align:'center', halign:'center', sortable:true">订单状态</th>
        <th data-options="field:'review_delivery',width:85,align:'center', halign:'center', sortable:true">预计交期</th>
        <th data-options="field:'remarks',width:400,halign:'center',align:'left', halign:'center', tooltip: true">更新内容</th>
      </tr>
      </thead>
    </table>
  </div>
</div> <!-- //body outer end -->

<script>
  /*$(function(){
    $('#dg').datagrid({
      rownumbers:true,
      singleSelect: true,
      url: '${ctx}/order/getTrackData?orderno='+ $("#selOrderNo").val() + '&rmd=' + new Date().getTime(),
      autoRowHeight:false,
      enableHeaderContextMenu:false,//表头右键
      enableRowContextMenu:false,//行右键
      pagination:true,
      pageSize:20,
      width:960,
      height:565
    });
  })*/
</script>


</body>

</html>