<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <meta name="menu" content="backend_sn"/>
    <title>SN码录入</title>
    <link rel="stylesheet" href="${ctx}/static/styles/css.css"/>
    <link rel="stylesheet" href="${ctx}/static/style/jquery.mCustomScrollbar.min.css"/>

    <link rel="stylesheet" href="${ctx}/static/styles/ui-dialog.css"/>
    <link rel="stylesheet" href="${ctx}/static/styles/doc.css"/>
    <script src="${ctx}/static/js/jquery-1.11.1.min.js"></script>
    <script src="${ctx}/static/js/ligao.global.js"></script>
    <script src="${ctx}/static/js/charts/echarts-plain.js"></script>
    <script src="${ctx}/static/js/highcharts.js" type="text/javascript" charset="UTF-8"></script>
    <script src="${ctx}/static/js/dialog-plus.js"></script>
    <style>
        .btn-sm {
            padding: 5px 10px;
            font-size: 12px;
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
    <script type="text/javascript">

        function initUser() {
            var param = {};
            $("#corpid").empty();
            param.username = $("#keywords_username").val();
            $.post("${ctx}/sn/getuser", param, function (data) {
                var result = eval(data);
                $.each(result, function (index, item) {
                    $("#corpid").append("<option value='" + item[0] + "'>" + item[1] + "</option>");
                });
            });
        }

        function initOrder() {
            $("#username_show").html("");
            $("#corpid").val("");
            $.post("${ctx}/sn/getOrder", {orderno: $("#orderno").val()}, function (data) {
                if (data != "-1") {
                    var result = eval('(' + data + ')');
                    $("#username_show").html(result.corp.corpName);
                    $("#project_note").html(result.projectNote);
                    $("#corpid").val(result.corp.id);
                }
            });
        }

        function sendEmailFun() {
            $.post("${ctx}/sn/sendEmail", {msg: $("#emailNote").val()}, function (data) {
                alert(JSON.stringify(data));
            });
        }

        function getModelBySN() {
            //$.post("${ctx}/sn/getModelBySN", {orderno : "LG201601120001", modelsn : "2115CN1A2200008" },function(data){
            $.post("${ctx}/sn/getModelBySN", {orderno: "LG201601120001", modelsn: "211201601200993"}, function (data) {
                showDialogAlert(data);
            });
        }

        function getOrder4SystemJoint() {
            $.post("${ctx}/sn/getOrder4SystemJoint", {orderno: "LG201603300101"}, function (data) {
                showDialogAlert(data);
            });
        }
        function checkOrderProductSn() {
            $.post("${ctx}/sn/checkOrderProductSn", {
                orderNo: "LG201604070901",
                productSn: "10M54U23V0134"
            }, function (data) {
                showDialogAlert(data);
            });
        }
        function checkProductSnFromTool() {
            $.post("${ctx}/sn/checkProductSnFromTool", {
                orderNo: "LG201604070901",
                productSn: "10M54U23V0134"
            }, function (data) {
                showDialogAlert(data);
            });
        }

        function initOrderNo() {
            if ($("#startnum").val() == "") {
                showDialogAlert("起始值不能为空");
                return;
            }
            if ($("#endnum").val() == "") {
                showDialogAlert("截止值不能为空");
                return;
            }
            $("#ddbhresultStr").html("");
            $.post("${ctx}/sn/getOrderNo", {
                orderno: $("#orderno").val(),
                startnum: $("#startnum").val(),
                endnum: $("#endnum").val()
            }, function (data) {
                if (data != "-1") {
                    var result = eval('(' + data + ')');
                    var str = "";
                    var rsArr = result.result;
                    for (var i = 0; i < rsArr.length; i++) {
                        str += rsArr[i] + ",";
                    }
                    $("#ddbhresultStr").html("查询限制：" + result.startnum + " 至 " + result.endnum + " 实际结果数量：" + result.realnum + " 编号: " + str);
                }
            });
        }

        function initHware() {
            if ($("#prod_type").val() == "-1") {
                showDialogAlert("产品类型不能为空");
                return;
            }
            $("#hardwareStr").html("");
            $.post("${ctx}/sn/getHardwareVersion", {prodtype: $("#prod_type").val()}, function (data) {
                if (data != "-1") {
                    var result = eval('(' + data + ')');
                    var str = "";
                    for (var i = 0; i < result.length; i++) {
                        str += result[i] + ",  ";
                    }
                    $("#hardwareStr").html(str);
                }
            });
        }

        function getPcbSn() {
            $("#pcbsnStr").html("");
            $.post("${ctx}/sn/getPcbSn", {main_sn: $("#temp_sn").val()}, function (data) {
                $("#pcbsnStr").html(data);
            });
        }

        function save() {
            if ($("#corpid").val() == "") {
                showDialogAlert("订单信息未录入");
                return;
            }
            var param = $("#snForm").serializeObject();
            //alert(JSON.stringify(param));
            $("#tjbw").html(JSON.stringify(param));
            $.post("${ctx}/sn/savesn", param, function (data) {
                var rsObj = eval("(" + data + ")");
                //alert("rscode=" + rsObj.rscode + "---rsmsg=" + rsObj.rsmsg );
                showDialogAlert(rsObj.rsmsg);
            });
        }
        function saveMiSN() {
            /*if($("#corpid").val() == ""){
             showDialogAlert("订单信息未录入");
             return ;
             }*/
            //var param = $("#snForm").serializeObject();
            //alert(JSON.stringify(param));
            //$("#tjbw").html(JSON.stringify(param));

            /*var param = {
                user_id: $("#optuserid").val(),
                uuid: "b8b6b31a-6b6b-4093-8564-53b316bec890",
                orderno: "LG201603120205",
                m_sn: "21C5CN3A2200001",
                device_type: "BY5424B",
                hw_version: "1.11",
                sw_version: "1.21",
                submit_type: "0"
            };*/

            var param = {
                user_id: $("#optuserid").val(),
                uuid: $("#uuid").val(),
                orderno: "LG201603120205",
                m_sn: $("#temp_sn").val(),
                device_type:  $("#device_code").val(),
                hw_version: "1.11",
                sw_version: "1.21",
                submit_type: "0"
            };

            $.post("${ctx}/sn/saveMiScan", param, function (data) {
                //var rsObj = eval("(" + data + ")");
                //alert("rscode=" + rsObj.rscode + "---rsmsg=" + rsObj.rsmsg );
                showDialogAlert(data);
            });
        }

        function saveGoods() {
            if ($("#goods_code").val() == "") {
                alert("不能为空");
                return;
            }

            var param = {goods_code: $("#goods_code").val(), p_sn: "11C68A100002"};

            $.post("${ctx}/sn/saveGoodsCode", param, function (data) {
                //var rsObj = eval("(" + data + ")");
                //alert("rscode=" + rsObj.rscode + "---rsmsg=" + rsObj.rsmsg );
                showDialogAlert(data);
            });
        }

        function findPSN() {
            var param = {p_sn: "11C68A100002"};

            $.post("${ctx}/sn/findMiInfoByPSN", param, function (data) {
                showDialogAlert(data);
            });
        }

        function findMSNforMi() {
            var param = {m_sn: "21C5CN3A2200001", orderno: "LG201603120205", device_type: "BY5424B"};
            param = {m_sn: "2115CN3A2200204", orderno: "LG201608230901", device_type: ""};
            param = {m_sn: "20S5CN3A2202305", orderno: "LG201608230901", device_type: ""};
            param = {m_sn: "2135CN3A2209010", orderno: "LG201608230901", device_type: ""};

            $.post("${ctx}/sn/findMSNforMi", param, function (data) {
                showDialogAlert(data);
            });
        }

        function getUuidSn() {
            // {uuids : "123456789-1234567890,7a859828-1c7d-4918-929c-ee1e1ab9e830,test111111", sns : "10A55S23K6525,10A55S23K6526,10A55S23K6527"}
            $.post("${ctx}/sn/getUuidSn", {uuids: "ED42C13B-0A30-4499-BD48-80686F52116D", sns: ""}, function (data) {
                var rsObj = eval("(" + data + ")");
                if (rsObj.rscode == "-1") {
                    showDialogAlert("UUID和SN不能都为空");
                } else if (rsObj.rscode == "-2") {
                    showDialogAlert("未找到匹配数据：" + rsObj.rsmsg.uuids + "---" + rsObj.rsmsg.sns);
                } else {
                    showDialogAlert("返回结果：" + data);
                }
            });
        }


        function savePCBSN() {
            // 21F6461A1500363   pcb_sns = 30G6463A0600249,30G6463A0600247,30G6463A0600246
            $.post("${ctx}/sn/savePcbSn", {
                optuserid: $("#optuserid").val(),
                main_sn: "21F6461A1500363",
                pcb_sns: "30G6463A0600249,30G6463A0600247,30G6463A0600246"
            }, function (data) {
                var rsObj = eval("(" + data + ")");

                showDialogAlert(rsObj.rsmsg);
            });
        }

        function savePCBSN_B() {
            // 21F6461A1500363   pcb_sns = 30G6463A0600249,30G6463A0600247,30G6463A0600246
            $.post("${ctx}/sn/savePcbaSn_B", {
                optuserid: $("#optuserid").val(),
                main_sn: "21C5CN3A2201241",
                //pcb_sns: "30G5CN3A0500005,30H5CN3A0500001,30L5CN3A0500006,30L5CN3A0500007",
                pcb_sns: "",
                hw_version: "1.24",
                orderno: "LG201603120205"
            }, function (data) {
                //var rsObj = eval("(" + data + ")");

                showDialogAlert(data);
            });
        }


        function saveSystemJoint() {
            var jsonData = "{\"lists\":[{\"detail\":{\"csf\":\"success\",\"cfg\":\"success\",\"firm_version\":\"success\"},\"name\":\"BC52B\",\"single_result\":\"success\",\"sn\":\"123456789\",\"type\":\"bcu\",\"uuid\":\"123456789\"},{\"detail\":{\"csf\":\"uncheck\",\"cfg\":\"success\",\"firm_version\":\"uncheck\"},\"name\":\"BC52B\",\"single_result\":\"success\",\"sn\":\"123456780\",\"type\":\"BM51A\",\"uuid\":\"123456780\"}],\"total_result\":\"ok\",\"user_id\":" + $("#optuserid").val() + ",\"orderno\":\"LG201604050101\"}";

            $.post("${ctx}/sn/saveSystemJoint", {json_data: jsonData}, function (data) {
                var rsObj = eval("(" + data + ")");
                showDialogAlert(rsObj.rsmsg);
            });
        }
        function findPcbaInfo() {
            var ordernoStr = $("#orderno").val();
            $.post("${ctx}/sn/findPcbaInfo", {orderno: ordernoStr, m_sn: $("#temp_sn").val()}, function (data) {
                //var rsObj = eval("(" + data + ")");d
                showDialogAlert(data);
            });
        }

        function testPSN() {
            $.post("${ctx}/sn/testPSN", {p_code: $("#temp_sn").val()}, function (data) {
                showDialogAlert(data);
            });
        }


        function saveToolResult_B() {
            alert("B型工装数据保存");
            var jsonData = "{" +
                    "\"user_id\": " + $("#optuserid").val() + "," +
                    "\"order_no\": \"LG201603120205\"," +
                    "\"tool_id\": \"1\"," +
                    "\"total_result\": \"true\"," +
                    "\"uuid\": \"6C0BC2FD-960C-45C9-BD10-7DD705B1A569\"," +
                    "\"name\": \"BC52B\"," +
                    "\"devicetype\": \"1\"," +
                    "\"bmu_id\": \"0\"," +
                    "\"sn\": \"21C5CN3A2200001\"," +
                    "\"soft_version\": \"1.0.2.0/2001.3.2.3\"," +
                    "\"report_type\": \"0\"," +
                    "\"lists\": [{" +
                    "\"check_item\": \"Supply_24\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"open_24V_DET\": \"\"," +
                    "\"open_24V_1\": \"\"," +
                    "\"open_24V_2\": \"\"," +
                    "\"open_24V_3\": \"\"," +
                    "\"open_5V_HALL\": \"\"," +
                    "\"close_24V_DET\": \"\"," +
                    "\"close_24V_1\": \"\"," +
                    "\"close_24V_2\": \"\"," +
                    "\"close_24V_3\": \"\"," +
                    "\"close_5V_HALL\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Supply_BAT\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BAT_OUT_DET\": \"12165\"," +
                    "\"BAT_24V_1_DET\": \"12165\"," +
                    "\"BAT_24V_2_DET\": \"12165\"," +
                    "\"BAT_24V_3_DET\": \"12165\"," +
                    "\"BAT_5V_HALL\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Supply_KEY\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                    "\"OPEN_24V_1_DET\": \"12165\"," +
                    "\"OPEN_24V_2_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"OPEN_5V_HALL\": \"12165\"," +
                    "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                    "\"CLOSE_24V_1_DET\": \"12165\"," +
                    "\"CLOSE_24V_2_DET\": \"12165\"," +
                    "\"CLOSE_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_5V_HALL\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Supply_CHR\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                    "\"OPEN_24V_1_DET\": \"12165\"," +
                    "\"OPEN_24V_2_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"OPEN_5V_HALL\": \"12165\"," +
                    "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                    "\"CLOSE_24V_1_DET\": \"12165\"," +
                    "\"CLOSE_24V_2_DET\": \"12165\"," +
                    "\"CLOSE_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_5V_HALL\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Supply_SIG1\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                    "\"OPEN_24V_1_DET\": \"12165\"," +
                    "\"OPEN_24V_2_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"OPEN_5V_HALL\": \"12165\"," +
                    "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                    "\"CLOSE_24V_1_DET\": \"12165\"," +
                    "\"CLOSE_24V_2_DET\": \"12165\"," +
                    "\"CLOSE_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_5V_HALL\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Supply_CP\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                    "\"OPEN_24V_1_DET\": \"12165\"," +
                    "\"OPEN_24V_2_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"OPEN_5V_HALL\": \"12165\"," +
                    "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                    "\"CLOSE_24V_1_DET\": \"12165\"," +
                    "\"CLOSE_24V_2_DET\": \"12165\"," +
                    "\"CLOSE_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_5V_HALL\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Supply_LATCH\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_BAT_OUT_DET\": \"12165\"," +
                    "\"OPEN_24V_1_DET\": \"12165\"," +
                    "\"OPEN_24V_2_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"OPEN_5V_HALL\": \"12165\"," +
                    "\"CLOSE_BAT_OUT_DET\": \"12165\"," +
                    "\"CLOSE_24V_1_DET\": \"12165\"," +
                    "\"CLOSE_24V_2_DET\": \"12165\"," +
                    "\"CLOSE_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_5V_HALL\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Consum_9V\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CHAR_24V_OUT_DET\": \"12165\"," +
                    "\"CHAR_24V_DET\": \"12165\"," +
                    "\"CHAR_BATTERY_V\": \"12165\"," +
                    "\"CHAR_CURRENT\": \"12165\"," +
                    "\"CHAR_CONSUM\": \"12165\"," +
                    "\"BAT_BAT_OUT_DET\": \"12165\"," +
                    "\"BAT_24V_DET\": \"12165\"," +
                    "\"BAT_BATTERY_V\": \"12165\"," +
                    "\"BAT_DUT_CURRENT\": \"12165\"," +
                    "\"BAT_CONSUM\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Consum_12V\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CHAR_24V_OUT_DET\": \"12165\"," +
                    "\"CHAR_24V_DET\": \"12165\"," +
                    "\"CHAR_BATTERY_V\": \"12165\"," +
                    "\"CHAR_CURRENT\": \"12165\"," +
                    "\"CHAR_CONSUM\": \"12165\"," +
                    "\"BAT_BAT_OUT_DET\": \"12165\"," +
                    "\"BAT_24V_DET\": \"12165\"," +
                    "\"BAT_BATTERY_V\": \"12165\"," +
                    "\"BAT_DUT_CURRENT\": \"12165\"," +
                    "\"BAT_CONSUM\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Consum_36V\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CHAR_24V_OUT_DET\": \"12165\"," +
                    "\"CHAR_24V_DET\": \"12165\"," +
                    "\"CHAR_BATTERY_V\": \"12165\"," +
                    "\"CHAR_CURRENT\": \"12165\"," +
                    "\"CHAR_CONSUM\": \"12165\"," +
                    "\"BAT_BAT_OUT_DET\": \"12165\"," +
                    "\"BAT_24V_DET\": \"12165\"," +
                    "\"BAT_BATTERY_V\": \"12165\"," +
                    "\"BAT_DUT_CURRENT\": \"12165\"," +
                    "\"BAT_CONSUM\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"Consum_48V\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CHAR_24V_OUT_DET\": \"12165\"," +
                    "\"CHAR_24V_DET\": \"12165\"," +
                    "\"CHAR_BATTERY_V\": \"12165\"," +
                    "\"CHAR_CURRENT\": \"12165\"," +
                    "\"CHAR_CONSUM\": \"12165\"," +
                    "\"BAT_BAT_OUT_DET\": \"12165\"," +
                    "\"BAT_24V_DET\": \"12165\"," +
                    "\"BAT_BATTERY_V\": \"12165\"," +
                    "\"BAT_DUT_CURRENT\": \"12165\"," +
                    "\"BAT_CONSUM\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"USB\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"RS485\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"CAN1\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"CAN2\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"CAN3\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"NTC\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"NTC1\": \"12165\"," +
                    "\"NTC2\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"CC\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"NTC1\": \"12165\"," +
                    "\"NTC2\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_1000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_1500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_2000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_2500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_3000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_3500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_4000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_A_4500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_1000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_1500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_2000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_2500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_3000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_3500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_4000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HALL_B_4500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CURRENT_1\": \"12165\"," +
                    "\"HALL_C1\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_50\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_100\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_200\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_300\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_400\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_500\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_600\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_700\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"HV_800\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"HV_TOOLING\": \"12165\"," +
                    "\"PCH\": \"12165\"," +
                    "\"PRE\": \"12165\"," +
                    "\"BAT\": \"12165\"," +
                    "\"INSUHV_ON_ON\": \"12165\"," +
                    "\"INSUHV_OFF_ON\": \"12165\"," +
                    "\"INSUHV_ON_OFF\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RELAY_1\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_RLY_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_RLY_DET\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RELAY_2\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_RLY_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_RLY_DET\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RELAY_3\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_RLY_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_RLY_DET\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RELAY_4\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_RLY_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_RLY_DET\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RELAY_5\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_RLY_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_RLY_DET\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RELAY_6\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_RLY_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_RLY_DET\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RELAY_7\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_RLY_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_RLY_DET\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RELAY_8\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_RLY_DET\": \"12165\"," +
                    "\"OPEN_24V_3_DET\": \"12165\"," +
                    "\"CLOSE_RLY_DET\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"IO\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"LOSE_POWER\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"SD_CARD\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"CP PWM\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"DUTY\": \"12165\"," +
                    "\"CLOSE_CHR_EN_CP_V\": \"12165\"," +
                    "\"OPEN_CHR_EN_CP_V\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RTC_TIME\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"RTC\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"RTC_ALARM\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"DTU\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"SIGNAL\": \"12165\"," +
                    "\"CCID\": \"12165\"," +
                    "\"IMEI\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"DO\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": null" +
                    "}, {" +
                    "\"check_item\": \"BMU_CON_9V\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"TOOL_BM51_DET\": \"12165\"," +
                    "\"DUT_CURRENT\": \"12165\"," +
                    "\"CONSUM\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_CON_12V\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"TOOL_BM51_DET\": \"12165\"," +
                    "\"DUT_CURRENT\": \"12165\"," +
                    "\"CONSUM\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_CON_36V\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"TOOL_BM51_DET\": \"12165\"," +
                    "\"DUT_CURRENT\": \"12165\"," +
                    "\"CONSUM\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_CON_48V\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"TOOL_BM51_DET\": \"12165\"," +
                    "\"DUT_CURRENT\": \"12165\"," +
                    "\"CONSUM\": \"12165\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_VOL_1000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_VOL_2000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_VOL_3000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_VOL_4000\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_VOL_4200\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(-25)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(-5)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(0)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(20)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(30)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(40)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(50)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(65)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(75)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_T(80)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_EXTEND_T(-40)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CHANNEL_1\": \"118460\"," +
                    "\"CHANNEL_2\": \"118460\"," +
                    "\"CHANNEL_3\": \"118460\"," +
                    "\"CHANNEL_4\": \"118460\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_EXTEND_T(25)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CHANNEL_1\": \"118460\"," +
                    "\"CHANNEL_2\": \"118460\"," +
                    "\"CHANNEL_3\": \"118460\"," +
                    "\"CHANNEL_4\": \"118460\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_EXTEND_T(55)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CHANNEL_1\": \"118460\"," +
                    "\"CHANNEL_2\": \"118460\"," +
                    "\"CHANNEL_3\": \"118460\"," +
                    "\"CHANNEL_4\": \"118460\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_EXTEND_T(80)\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"CHANNEL_1\": \"118460\"," +
                    "\"CHANNEL_2\": \"118460\"," +
                    "\"CHANNEL_3\": \"118460\"," +
                    "\"CHANNEL_4\": \"118460\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_1\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_2\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_3\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_4\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_5\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_6\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_7\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_8\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_9\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_10\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_11\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_OFF_12\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_1\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_2\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_3\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_4\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_5\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_6\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_7\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_8\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_9\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_10\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_11\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_BAL_ON_12\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"BOARD1\": \"12106\"," +
                    "\"BOARD2\": \"12106\"," +
                    "\"BOARD3\": \"12106\"," +
                    "\"BOARD4\": \"12106\"," +
                    "\"BOARD5\": \"12106\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_RELAY1\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_PTC_DET\": \"118460\"," +
                    "\"OPEN_24V_BM51_DET\": \"118460\"," +
                    "\"CLOSE_PTC_DET\": \"118460\"" +
                    "}" +
                    "}, {" +
                    "\"check_item\": \"BMU_RELAY2\"," +
                    "\"res\": \"unchecked\"," +
                    "\"detail\": {" +
                    "\"OPEN_FAN_DET\": \"118460\"," +
                    "\"OPEN_24V_BM51_DET\": \"118460\"," +
                    "\"CLOSE_FAN_DET\": \"118460\"" +
                    "}" +
                    "}]," +
                    "\"files\": {" +
                    "\"s19_test\": \"2015090211111\"," +
                    "\"s19_order\": \"2015090211111\"," +
                    "\"cfg\": \"2015090211111\"," +
                    "\"csf\": \"2015090211111\"" +
                    "}" +
                    "}";
            $.post("${ctx}/sn/saveToolResult_B", {json_data: jsonData}, function (data) {
                //var rsObj = eval("(" + data + ")");
                showDialogAlert(data);
            });
        }

        function saveTollInfo() {
            var jsonData = "{" +
                    "    \"user_id\": " + $("#optuserid").val() + "," +
                    "    \"order_no\": \"13567903\"," +
                    "    \"total_result\": \"error\"," +
                    "    \"uuid\": \"123456789\"," +
                    "    \"name\": \"BC52B\"," +
                    "    \"bmu_id\": \"1\"," +
                    "    \"sn\": \"211201601200993\"," +
                    "    \"detail\": {" +
                    "        \"24VSupply\": \"success\"," +
                    "        \"BATTERY\": \"failure\"," +
                    "        \"24VCtl\": \"unchecked\"," +
                    "        \"KEY_ON\": \"unchecked\"," +
                    "        \"CHARGER\": \"unchecked\"" +
                    "    }," +
                    "    \"files\": {" +
                    "        \"s19_test\": \"2015090211111\"," +
                    "        \"s19_order\": \"20152365\"," +
                    "        \"cfg\": \"20156923\"," +
                    "        \"csf\": \"20156989\"" +
                    "    }" +
                    "}";

            $.post("${ctx}/sn/saveToolResult", {json_data: jsonData}, function (data) {
                var rsObj = eval("(" + data + ")");
                showDialogAlert(rsObj.rsmsg);
            });
        }


        function loginFun() {
            var param = {};
            param.username = $("#username").val();
            param.userpass = $("#userpass").val();
            param.logintype = "t";
            $.post("${ctx}/sn/userlogin", param, function (data) {
                var rsObj = eval("(" + data + ")");
                if (rsObj.rscode < 0) {
                    showDialogAlert(rsObj.rsmsg);
                } else {
                    //showDialogAlert(data);
                    $("#showLoginDiv").css("display", "none");
                    $("#showSaveDiv").css("display", "");

                    var userObj = rsObj.rsmsg;
                    $("#optuserid").val(userObj.id);
                    //showDialogAlert($("#optuserid").val());
                }
            });
        }
        function goBackDiv() {
            $("#showLoginDiv").css("display", "");
            $("#showSaveDiv").css("display", "none");
        }
        $.fn.serializeObject = function () {
            var o = {};
            var a = this.serializeArray();
            $.each(a, function () {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        };

    </script>
</head>
<body>
<div class="table-box page-manage-box-outer dclear clearfix" id="showLoginDiv">
    <div class="table-body page-table-body">
        <div class="history-save-table-head">
            <div class="history-save-table-head-outer">
                <div class="profile-head-top">
                    用户登录
                </div>
            </div>
        </div>

        <div class="historty-table-con-outer dclear">
            <div class="history-table-con-inner manage-page-inner clearfix">
                <form action="javascript:;" id="loginForm" name="loginForm">
                    <div class="profile-list-item">
                        <div class="profile-list-item-inner">
                            <div class="profile-list-item-list">
                                <span>账号:</span> <input type="text" id="username" name="username"/>
                            </div>

                            <div class="profile-list-item-list">
                                <span>密码:</span> <input type="password" id="userpass" name="userpass"/>
                            </div>
                        </div>
                    </div>

                    <div align="center" style="padding-bottom:50px; margin-top:15px;">
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="loginFun()">登 录</a>
                    </div>
                    <div align="center" style="padding-bottom:50px; margin-top:15px; display: none;">
                        <a href="http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201603260120&fileType=s19&deviceCode=BC52A&fileName=3&bmuNo=0&isTestS19=1">测试固件BC52A</a>
                        <a href="http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201603260120&fileType=s19&deviceCode=BY31A&fileName=3&bmuNo=0&isTestS19=1">测试固件BY31A</a>
                        <a href="http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201603260120&fileType=s19&deviceCode=C11&fileName=3&bmuNo=0&isTestS19=1">测试固件C11</a>
                        <a href="http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201603260120&fileType=s19&deviceCode=C11B&fileName=3&bmuNo=0&isTestS19=1">测试固件C11B</a>
                        <a href="http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201603260120&fileType=s19&deviceCode=BY31B&fileName=3&bmuNo=0&isTestS19=1">测试固件BY31B</a>
                        <a href="http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201603260120&fileType=s19&deviceCode=BMU51XX&fileName=3&bmuNo=0&isTestS19=1">测试固件BMU51XX</a>
                        <a href="http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201603260120&fileType=s19&deviceCode=BL51B&fileName=3&bmuNo=0&isTestS19=1">测试固件BL51B</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="table-footer page-footer dclear">
    </div>
</div>

<div class="table-box page-manage-box-outer dclear clearfix" id="showSaveDiv" style="display: none;">
    <div class="table-body page-table-body">
        <div class="history-save-table-head">
            <div class="history-save-table-head-outer">
                <div class="profile-head-top">
                    设备SN码绑定
                </div>
            </div>
        </div>

        <div class="historty-table-con-outer dclear">
            <div class="history-table-con-inner manage-page-inner clearfix">
                <form action="javascript:;" id="snForm" name="snForm">
                    <input type="hidden" id="optuserid" name="optuserid" value=""/>
                    <input type="hidden" id="submit_type" name="submit_type" value="1"/>

                    <div class="profile-list-item">
                        <div class="profile-list-item-inner">
                            <div class="profile-list-item-list">
                                <span>UUID:</span> <input type="text" id="uuid" name="uuid"/>
                            </div>

                            <div class="profile-list-item-list">
                                <span>SN码:</span> <input type="text" id="sn" name="sn"/>
                            </div>

                            <div class="profile-list-item-list">
                                <span>模块SN码:</span> <input type="text" id="temp_sn" name="temp_sn"/>
                            </div>

                            <div class="profile-list-item-list">
                                <span>订单号:</span> <input type="text" id="orderno" name="orderno"/>
                            </div>
                            <div class="profile-list-item-list">
                                <span>订单起始数:</span> <input type="text" id="startnum" name="startnum"/>
                            </div>
                            <div class="profile-list-item-list">
                                <span>订单截止数:</span> <input type="text" id="endnum" name="endnum"/>
                            </div>

                            <div class="profile-list-item-list" style="display:none;">
                                <span>客户搜索:</span> <input type="text" id="keywords_username" name="keywords_username"
                                                          value=""/>
                            </div>

                            <div class="profile-list-item-list">
                                <input type="hidden" name="corpid" id="corpid" value=""/>
                                <span>客户名称:</span> <span id="username_show" style="width:200px;"></span>
                            </div>
                            <div class="profile-list-item-list">
                                <span>项目描述:</span> <span id="project_note" style="width:200px;"></span>
                            </div>

                            <div class="profile-list-item-list">
                                <span>产品类型:</span>
                                <select id="prod_type" name="prod_type">
                                    <option value="-1">请选择</option>
                                    <c:forEach items="${prodList}" var="item">
                                        <option value="${item.prodTypename}">${item.prodTypename}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="profile-list-item-list">
                                <span>硬件版本号:</span> <input type="text" id="hw_version" name="hw_version"/>
                            </div>
                            <div class="profile-list-item-list">
                                <span>软件版本号:</span> <input type="text" id="sw_version" name="sw_version"/>
                            </div>
                            <div class="profile-list-item-list">
                                <span>主板版本:</span> <input type="text" id="pcba_type" name="pcba_type"/>
                            </div>
                            <div class="profile-list-item-list" style="display:none;">
                                <span>提交报文:</span> <span id="tjbw"></span>
                            </div>
                            <div class="profile-list-item-list">
                                <span>邮件内容:</span> <input type="text" id="emailNote" name="emailNote"/>
                            </div>
                            <div class="profile-list-item-list">
                                <span>客户料号:</span> <input type="text" id="goods_code" name="goods_code" value=""/>
                            </div>

                            <div class="profile-list-item-list">
                                <span>产品型号:</span> <input type="text" id="device_code" name="device_code" value=""/>
                            </div>
                        </div>
                    </div>

                    <div align="center" style="padding-bottom:50px; margin-top:15px;">
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="initOrder()">订单信息</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="initOrderNo()">订单编号</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="initHware()">硬件版本号</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="save()">保存模块扫码</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="saveMiSN()">保存模块扫码_B</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="saveGoods()">保存客户料号</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="findPSN()">获取产品SN信息</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="findMSNforMi()">获取模块SN信息</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="savePCBSN()">保存PCB板SN</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="getPcbSn()">获取pcb板SN信息</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="getUuidSn()">获取UUID SN信息</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="saveSystemJoint()">保存联调数据</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="saveTollInfo()">保存工装数据</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="saveToolResult_B()">保存B型工装</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="sendEmailFun()">发送邮件</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="findPcbaInfo()">获取PCBA_B扫码信息</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="savePCBSN_B()">PCBA扫码</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="testPSN()">测试PSN生成</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="goBackDiv()">返 回</a>
                    </div>
                    <div align="center" style="padding-bottom:50px; margin-top:15px;">
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="getModelBySN()">工装订单模块SN验证</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="getOrder4SystemJoint()">系统联调获取订单信息</a>
                        <a href="javascript:;" class="btn btn-success btn-sm"
                           onclick="checkOrderProductSn()">产品SN订单校验</a>
                        <a href="javascript:;" class="btn btn-success btn-sm" onclick="checkProductSnFromTool()">产品SN工装测试校验</a>
                    </div>
                    <div style="margin-bottom:20px;padding-left:10px;">
                        <span>订单编号返回信息:</span> <span id="ddbhresultStr"></span>
                    </div>
                    <div style="margin-bottom:20px;padding-left:10px;">
                        <span>硬件版本号返回信息:</span> <span id="hardwareStr"></span>
                    </div>
                    <div style="margin-bottom:20px;padding-left:10px;">
                        <span><a
                                href="http://192.168.1.22:8080/pos-ligoo/fileOperate/downloadConfigFile?orderno=LG201603260120&fileType=s19&deviceCode=BC52A&fileName=3&bmuNo=0&isTestS19=0">PCB板返回信息:</a></span>
                        <span id="pcbsnStr"></span>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="table-footer page-footer dclear">
    </div>
</div>
</body>
</html>