/**
 * Created by wly on 2016/3/21.
 */
//系统虚拟目录
var sys_rootpath = document.location.pathname.substring(0,document.location.pathname.substring(1).indexOf('/')+1);

if(sys_rootpath == "/order"){
    sys_rootpath = "";
}
/**
 * 软件上传表单
 */
function submitForm(){
    var rs = true;
    var idsArr = [];
    $(".euploadify-f").each(function(i, e){
        idsArr.push("#" + $(this).attr("id"));
        if("" == $(this).euploadify("getValues") || "," == $(this).euploadify("getValues")){
            rs = false;
        }
    });
    if(!rs || idsArr.length == 0){
        $(idsArr.join(',')).euploadify("validate");
        $.messager.alert("系统提示", "上传项不能为空", "warning");
        return ;
    }else{
        $.messager.confirm("软件上传保存确认", "保存操作会覆盖原先上传的软件<br>是否确认保存订单软件？", function(cr){
            if(cr){
                $('#orderSWUploadForm').form('submit',{
                    url: sys_rootpath + '/order/orderFileUpload',
                    onSubmit:function(param){
                        $.easyui.loading({ msg: "正在保存...", topMost: true });
                        $("#upload_orderno").val($("#selOrderNo").val());
                        /*var rs = true;
                         var idsArr = [];
                         $(".euploadify-f").each(function(i, e){
                         idsArr.push("#" + $(this).attr("id"));
                         if("" == $(this).euploadify("getValues") || "," == $(this).euploadify("getValues")){
                         rs = false;
                         $.easyui.loaded(true);
                         //return false;
                         }
                         });
                         if(!rs){
                         $(idsArr.join(',')).euploadify("validate");
                         //$.messager.alert("系统提示", "上传项不能为空", "warning");
                         }*/
                        return true; // 返回false终止表单提交
                    },
                    success: function(data){
                        $.easyui.loaded(true);
                        orderNoChange($("#selOrderNo").val());
                        $.messager.alert("系统提示", "操作成功", "info");
                        //toBackPage();
                        //orderNoChange($("#selOrderNo").val());
                    }
                });
            }
        });
    }
}
/**
 * 是否使用模版
 * @param obj
 * @param device_class
 * @param device_code
 * @param device_bum_no
 * @param device_type
 */
function isModuleSel(obj, device_class, device_code, device_bum_no, device_type){

    if($(obj).is(':checked')){
        var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
        $("#" + idStr + "s19").euploadify("destroy");
        $("#" + idStr + "td").html("<font color='red'>已使用模版文件</font><input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" type=\"hidden\" value=\"usetemplate\" />");
    }else{
        var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
        $("#" + idStr + "td").html("<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" />");
        var orderno = $("#selOrderNo").val();
        addEupload(idStr + "s19", "*.s19;", orderno, device_code, device_class, device_type, device_bum_no);
    }
}



/**
 * 初始化软件上传内容
 * @param device_class
 * @param device_code
 * @param device_bum_no
 * @param device_type
 */
function showUploadDeviceTR(device_class, device_code, device_bum_no, device_type){
    var idStrs = [];
    var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
    var add_device_str = "<tr style='color:#000; background-color:#E2E2E5;'>" +
        "<td style=\"text-align:right; width:90px; border:0px; height:33px;\">设备类型：</td>" +
        "<td style=\"text-align:left;border:0px; height:33px;\" width=\"*\" colspan='2'>" + device_class +
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设备型号：" + device_code;
    if("BMU" == device_class){
        add_device_str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID编号：#" + device_bum_no  ;
    }
    add_device_str += "</td></tr>";
    if(device_class == "BCU" || device_class == "BYU"){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
            "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td></tr>";

            //"<td style=\"text-align:center; width:90px; border:0px; height:40px;\">" +
           //"<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "')\" />启用模版</label></td></tr>";
        idStrs.push(idStr + "s19");

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">配置文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
            "<input id=\"" + idStr + "cfg\" name=\"" + idStr + "cfg_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td></tr>";
        idStrs.push(idStr + "cfg");

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">规则文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
            "<input id=\"" + idStr + "csf\" name=\"" + idStr + "csf_devicefile\" class=\"easyui-euploadify\" type=\"text\"" + "</td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td></tr>";
        idStrs.push(idStr + "csf");
    }else if(device_class == "BMU"){
        idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
            "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\"" + "</td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\">" +
            "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "')\" />启用模版</label></td></tr>";
        idStrs.push(idStr + "s19");

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">配置文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
            "<input id=\"" + idStr + "cfg\" name=\"" + idStr + "cfg_devicefile\" class=\"easyui-euploadify\" type=\"text\"" + "</td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td></tr>";
        idStrs.push(idStr + "cfg");
    }else if(device_class == "LDM"){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
            "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\"" + "</td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\">" +
            "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "')\" />启用模版</label></td></tr>";
        idStrs.push(idStr + "s19");

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">配置文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
            "<input id=\"" + idStr + "cfg\" name=\"" + idStr + "cfg_devicefile\" class=\"easyui-euploadify\" type=\"text\"" + "</td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td></tr>";
        idStrs.push(idStr + "cfg");
    }else if(device_class == "DTU" ){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
            "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\"" + "</td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\">" +
            "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "')\" />启用模版</label></td></tr>";
        idStrs.push(idStr + "s19");
    }

    $("#upload_device_tab_" + device_class).append(add_device_str);
    var orderno = $("#selOrderNo").val();
    var suffixStr = "";
    var eId = "";
    //初始化上传控件
    for(var i=0; i<idStrs.length; i++){
        eId = idStrs[i];
        suffixStr = "*." + eId.substring(eId.length -3) + ";";
        addEupload(eId, suffixStr, orderno, device_code, device_class, device_type, device_bum_no);
    }
    //return add_device_str;
}
/**
 * 文件上传验证返回信息
 * @param file
 * @param data
 * @param response
 */
function showonUploadSuccessInfo(file, data, response){
    var dataObj = eval('(' + data + ')');
    if(dataObj.message == "s19_error"){
        var hrefStr = $("#" + file.id).find("a").eq(0).attr("href");
        if(null == hrefStr || undefined == hrefStr){

        }else{
            hrefStr = hrefStr.substring(15, (hrefStr.indexOf(".")-2));
            $("#" + hrefStr).uploadify('cancel', file.id);
            $.messager.alert("系统提示", "订单设备与固件程序不匹配", "warning");
        }
    }
}

/**
 * 添加上传控件
 * @param idStr
 * @param suffixStr
 * @param orderno
 * @param device_code
 * @param device_class
 * @param device_type
 */
function addEupload(idStr, suffixStr, orderno, device_code, device_class, device_type, device_bum_no){
    var bumLimit = '';
    if(undefined == device_bum_no || null == device_bum_no || device_bum_no == "0"){

    }else{
        bumLimit = "_M" + device_bum_no;
    }
    $("#" +idStr).euploadify({
        width: 450,
        //height: 400,
        width:358,
        multi: true,
        multiTemplate: 'uploadify',
        auto: false,
        showStop: false,
        showCancel: false,
        preventCaching: false,
        tooltip: false,
        required: true,
        formData: {orderno:orderno, device_code:device_code, device_class:device_class , device_type:device_type, limit:orderno + '_' + device_code + bumLimit, limit_num:1 },
        fileSizeLimit: '10MB',
        //queueSizeLimit:1, //不能验证上传后的数量或者删除后的数量
        //uploadLimit:1,
        fileTypeExts: suffixStr,
        onUploadSuccess: showonUploadSuccessInfo ,
        swf: sys_rootpath + '/static/js/uploadify/uploadify.swf',
        uploader: sys_rootpath + '/fileOperate/uploadOrderConfigFile'
    });
}

/**
 * 重新选择设备型号
 * @param obj
 */
function changeDeviceSel(obj){
    var optType = $("#addDeviceType").val();
    var selObj = $(obj).parents("table").find("select").eq(1);
    var bmuInput = $(obj).parents("table").find("input").eq(0);
    var bmuSpan = $(obj).parents("table").find("span").eq(0);
    var bmuTR = $(obj).parents("table").find("tr").eq(2);

    var device_code_arr = [];
    var formStr = "orderEditForm";
    if(optType == 'add'){
        formStr = "orderAddForm";
    }
    //已选设备型号
    $("#" + formStr + " input[name='device_code_sel']").each(function(x, ele){
        device_code_arr.push($(this).val());
    });

    var selStr = $(obj).val();
    if("-1" == selStr){
        selObj.empty();
    }
    else if("BCU" == selStr){
        selObj.empty();
        bmuTR.css("display", "none");
        for(var i=0; i<bcuArr.length; i++){
            if(device_code_arr.indexOf(bcuArr[i]) > -1){
            }
            else{
                selObj.append("<option value='" + bcuArr[i] + "'>" + bcuArr[i] + "</option>");
            }
        }
    }
    else if("BYU" == selStr){
        selObj.empty();
        bmuTR.css("display", "none");
        for(var i=0; i<byuArr.length; i++){
            if(device_code_arr.indexOf(byuArr[i]) > -1){
            }
            else{
                selObj.append("<option value='" + byuArr[i] + "'>" + byuArr[i] + "</option>");
            }
        }
    }
    else if("BMU" == selStr){
        selObj.empty();
        bmuTR.css("display", "");
        for(var i=0; i<bmuArr.length; i++){
            selObj.append("<option value='" + bmuArr[i] + "'>" + bmuArr[i] + "</option>");
        }

        var bmu_no = 1;
        $("#" + formStr + " input[name='device_bmu_no']").each(function(index, ele){
            if($(this).val() > 0){
                bmu_no = bmu_no + 1;
            }
        });

        bmuInput.val(bmu_no);
        bmuSpan.html("#" + bmu_no);
    }
    else if("LDM" == selStr){
        selObj.empty();
        bmuTR.css("display", "none");
        for(var i=0; i<ldmArr.length; i++){
            if(device_code_arr.indexOf(ldmArr[i]) > -1){
            }
            else{
                selObj.append("<option value='" + ldmArr[i] + "'>" + ldmArr[i] + "</option>");
            }
        }
    }
    else if("DTU" == selStr){
        selObj.empty();
        bmuTR.css("display", "none");
        for(var i=0; i<dtuArr.length; i++){
            if(device_code_arr.indexOf(dtuArr[i]) > -1){
            }
            else{
                selObj.append("<option value='" + dtuArr[i] + "'>" + dtuArr[i] + "</option>");
            }
        }
    }
}
/**
 * 删除设备型号
 * @param obj
 * @param opt_type
 */
function delDeviceTr(obj, opt_type){
    if("BMU" == $(obj).parents("tr").find("input").eq(0).val()){
        var delnum =  $(obj).parents("tr").find("input").eq(1).val();
        $.messager.confirm("删除确认", "同时删除从机地址大于或等于 " + delnum + " 的设备<br>是否确认删除订单？", function(cr){
            if(cr){
                //$(obj).parents("tr").remove();

                var formStr = "orderEditForm";
                if(opt_type == 'add'){
                    formStr = "orderAddForm";
                }
                //删除选中从机
                $(obj).parents("tr").remove();
                //删除大于选中从机地址的设备
                $("#" + formStr + " input[name='device_bmu_no']").each(function(x, ele){
                    if(parseInt($(this).val()) > window.parseInt(delnum)){
                        $(this).parents("tr").remove();
                    }
                });
            }
        });
    }else{
        $(obj).parents("tr").remove();
    }
}


//判断数组是否是从1开始的连续数字
function isContinuationInteger(array){
    var i=0;
    var isContinuation=true;
    for(var e in array){
        i++;
        if(array[e]!=i){
            isContinuation=false;
            break;
        }
    }
    return isContinuation;
}
/**
 * 选择订单设备
 * @param opt_type
 */
function show_device_add(opt_type){
    //获取已选择
    $.easyui.showDialog({
        title: "选择订单设备",
        width: 500, height: 280,
        content:$("#test_dialog").html(),
        topMost: true,
        enableApplyButton: false,
        onSave: function (d) {
            var dataForm = d.form("getData");
            if(dataForm.add_device_class_p == "-1" || undefined == dataForm.add_device_code_p || null == dataForm.add_device_code_p || dataForm.add_device_code_p == ""){
                $.messager.alert("系统提示", "请选择设备信息", "warning");
                return false;
            }else{
                /*****************************************
                 * 主机型号只有一个(2016年4月8日 11:00:02)
                 *****************************************/
                if(dataForm.add_device_class_p == "BCU" || dataForm.add_device_class_p == "BYU"){
                    $("#" + opt_type + "_device_tab_BCU").html("");
                    $("#" + opt_type + "_device_tab_BYU").html("");
                }
                $("#" + opt_type + "_device_tab_" + dataForm.add_device_class_p).append(addDeviceTR(dataForm.add_device_class_p, dataForm.add_device_code_p , dataForm.add_device_bmu_p, opt_type));
                /*****************************************
                 * 除BCU、BYU，其余都滚动到底部
                 *****************************************/
                if(dataForm.add_device_class_p == "BCU" || dataForm.add_device_class_p == "BYU"){

                }else{
                    $("#" + opt_type + "_device_tab").closest("div.scroll-class").scrollTop($("#" + opt_type + "_device_tab").closest("div.scroll-class")[0].scrollHeight );
                }
            }
        }
    });
}

/**
 * 组装设备添加信息
 * @param device_class
 * @param device_code
 * @param device_bum_no
 * @param optType
 * @returns {string}
 */
function addDeviceTR(device_class, device_code, device_bum_no, optType){
    var add_device_str = "<tr>" +
        "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">设备类型：</td>" +
        "<td style=\"text-align:left; width:60px; border:0px; height:37px;\">" + device_class +
        "<input type=\"hidden\" name=\"device_class_sel\" value=\"" + device_class + "\" /></td>" +
        "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">设备型号：</td>" +
        "<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" >" + device_code ;
    if("BMU" == device_class){
        add_device_str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID编号：#" + device_bum_no + "<input type=\"hidden\" name=\"device_bmu_no\" value=\"" + device_bum_no + "\" />";
    }else{
        add_device_str += "<input type=\"hidden\" name=\"device_bmu_no\" value=\"0\" />";
    }
    add_device_str += "<input type=\"hidden\" name=\"device_code_sel\" value=\"" + device_code + "\" />" +
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img onclick='delDeviceTr(this, \"" + optType + "\")' src='" + sys_rootpath + "/static/images/del.png' style='width: 20px; cursor: pointer; '>" +

        "</td>" +
        "</tr>";
    return add_device_str;
}

/***************************************************************
 * 原JS整理
 ***************************************************************/
/**
 * 订单编号验证
 */
function checkOrderNo(){
    window.setTimeout(function(){
        $.post(sys_rootpath + "/order/checkOrderNo?rmd=" + new Date().getTime(), { orderno:$("#add_orderno").val()},function(data){
            if(data == "isuse"){
                $("#add_checkOrderNo").val("0");
            }else{
                $("#add_checkOrderNo").val("1");
            }
        });
    }, 500);
}

/**
 * 订单删除
 * @param id
 * @param orderno
 * @param status
 */
function delOrderFun(id, orderno, status){
    $.messager.confirm("删除确认", "是否确认删除订单？<br>编号：" + orderno , function(cr){
        if(cr){
            $.post(sys_rootpath + "/order/delorder?rmd=" + new Date().getTime(), {orderid : id, orderno : orderno},function(data){
                var msg = "操作失败";
                if(data == "ok"){
                    msg = "操作成功";
                    //操作成功才会刷新左侧
                    $("#selOrderNo").val("");
                    findOrderLi();
                }else if(data == "-1"){
                    msg = "订单已有设备绑定，不能删除";
                }
                $.messager.alert("系统提示", msg, "info");

            });
        }
    });
}

/**
 * 组装电池名称
 * @param opttype
 */
function autoBatteryName(opttype){
    var batteryTypeStr = $("#" + opttype + "_batteryModel_batteryType").find("option:selected").text() + "-";
    if (($("#" + opttype + "_batteryModel_batteryType").val()== -1)||($("#" + opttype + "_batteryModel_batteryType").val()== null)){
        batteryTypeStr = "";
    }
    var str = $("#" + opttype + "_factoryName").val() + "-" + batteryTypeStr
        + $("#" + opttype + "_batteryNumber").val() + "S-" + $("#" + opttype + "_capacity").val() + "AH";
    $("#" + opttype + "_batteryModel_batteryName").val(str);

}
/**
 * 验证客户信息
 * @param opttype
 * @param keywords
 */
function findUserInfo(opttype, keywords){
    $.post(sys_rootpath + "/order/getUserInfo?rmd=" + new Date().getTime(), {id : keywords},function(data){
        if(data != "-1"){

        }else{
            $.messager.alert("系统提示", "客户不存在", "warning");
        }
    });
}


function formatStatus(val,row){
    if(val == "1"){
        return '<span style="color:blue;">正常</span>';
    }else if(val == "2"){
        return '<span style="color:red;">已删除</span>';
    }else{
        return '<span style="color:red;">未知</span>';
    }
}



function addCartype(opttype){
    if($("#" + opttype + "_vehicleModel").val() == "addVType"){
        $("#" + opttype + "_vehicleModel").val("-1");
    }else{
        return;
    }
    $.easyui.showDialog({
        title: "添加车辆型号",
        width: 300, height: 150,
        content:'<input type="text" class="manage-input-text" style="width:202px;margin-top:20px; margin-left:40px;" id="property_cartype" name="property_cartype" value=""  />',
        topMost: true,
        enableApplyButton: false,
        onSave: function (d) {
            var dataForm = d.form("getData");
            var returnStr = dataForm.property_cartype ;
            if(undefined == returnStr || null == returnStr || returnStr == ""){
                $.messager.alert("系统提示", "车辆型号不能为空", "warning");
                return false;
            }else{
                var rs = false;
                $("#" + opttype + "_vehicleModel option").each(function(index, el){
                    if(returnStr == $(el).text()){
                        rs = true;
                        $(el).attr("selected", true);//默认选中
                    }
                });

                if(rs){
                    //showDialogAlert("车辆型号已存在");
                }
                else{
                    $("#" + opttype + "_vehicleModel").append("<option value='-9999'>"+ returnStr +"</option>");
                    $("#" + opttype + "_vehicleModel").val("-9999");
                }
            }
        }
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

function showDelIcon_dtu(obj, type){
    if(type == 1){
        $(obj).find('.handle').show();
    }else{
        $(obj).find('.handle').hide();
    }
}

function findOrderLi(){
    var selId = $("#selOrderNo").val();
    $("#orderdata").html("");
    $.post(sys_rootpath + "/order/getOrderList?rmd=" + new Date().getTime(), {type:1},function(data){
        var li = "";
        var result = eval(data);
        $.each(result, function(index, items){
            li += "<li orderLIData='" + items[1] + "' style='position:relative;'";
            if( items[2] < 2){
                li += " onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' ";
            }
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
            if( items[2] == 2){
                li += "<a href='javascript:;' onclick='orderNoChange(\"" + items[1] + "\")' title='已删除'><font color='red'>" + items[1] + "</font>" ;
            }else{
                li += "<a href='javascript:;' onclick='orderNoChange(\"" + items[1] + "\")'>" + items[1] ;
            }
            li += "</a>";
            li += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
            li += "<img onclick='delOrderFun(" + items[0] + ",\"" + items[1] + "\", " + items[2] + ")' src='" + sys_rootpath + "/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
            li += "</div>";
            li += "</li>";
        });
        $("#orderdata").html(li);
    });
}


function updateOrder(optType){
    $.easyui.loading({ msg: "正在保存...", topMost: true });

    if(optType == 'add'){
        if ($("#" + optType + "_orderno").val() == ""){
            $.easyui.loaded(true);
            $.messager.alert("系统提示", "订单编号不能为空", "warning");
            return;
        }
        if ($("#" + optType + "_checkOrderNo").val() == "0"){
            $.easyui.loaded(true);
            $.messager.alert("系统提示", "订单编号重复", "warning");
            return;
        }
    }else{
        if ($("#" + optType + "_id").val() == ""){
            $.easyui.loaded(true);
            $.messager.alert("系统提示", "请选择订单", "warning");
            return;
        }
    }
    var formStr = "orderEditForm";
    if(optType == 'add'){
        formStr = "orderAddForm";
    }


    /********************** 设备信息 *******************************/
    //检查设备型号
    var device_code_sels = "";
    $("#" + formStr + " input[name='device_code_sel']").each(function(x, ele){
        device_code_sels += $(this).val() + ",";
    });
    $("#" + optType + "_devices_code").val(device_code_sels);

    var device_class_sels = "";
    $("#" + formStr + " input[name='device_class_sel']").each(function(x, ele){
        device_class_sels += $(this).val() + ",";
    });
    $("#" + optType + "_devices_class").val(device_class_sels);

    var device_bmu_sels = "";
    $("#" + formStr + " input[name='device_bmu_no']").each(function(x, ele){
        device_bmu_sels += $(this).val() + ",";
    });
    $("#" + optType + "_devices_bmu").val(device_bmu_sels);

    /********************** 设备信息 *******************************/

    var khmcStr = $('#searchUserKey_' + optType).combobox('getValue');
    if (khmcStr == undefined|| khmcStr == ""){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "客户不能为空", "warning");
        return;
    }
    if(optType == 'edit'){//修改客户
        $("#edit_corp_id").val(khmcStr);
    }
    if ($("#" + optType + "_quantity").val() == ""){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "订货数量不能为空", "warning");
        return;
    }
    if ($("#" + optType + "_projectNote").val() == ""){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "项目描述不能为空", "warning");
        return;
    }
    /** 2016年1月12日13:49:14 屏蔽，许多订单无DTU信息 **/
    if ($("#" + optType + "_vehicleType_id").val() == "-1"){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "车辆类型不能为空", "warning");
        return;
    }

    if ($("#" + optType + "_vehicleModel").val() == "-1"){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "车辆型号不能为空", "warning");
        return;
    }
    if ($("#" + optType + "_batteryModel_batteryType").val() == "-1"){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "电池类型不能为空", "warning");
        return;
    }
    if ($("#" + optType + "_factoryName").val() == ""){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "电池厂商不能为空", "warning");
        return;
    }

    if ($("#" + optType + "_batteryNumber").val() == ""){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "电池串数不能为空", "warning");
        return;
    }

    if ($("#" + optType + "_capacity").val() == ""){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "电池容量不能为空", "warning");
        return;
    }
    /** **/


        //车辆型号文本信息
    $("#" + optType + "_vehicleModel_modelName").val($("#" + optType + "_vehicleModel").find("option:selected").text());


    $.post(sys_rootpath + "/order/saveOrder?rmd=" + new Date().getTime(), $("#" + formStr).serialize(),function(data){
        $.easyui.loaded(true);
        $.messager.alert("系统提示", "操作成功", "warning");
        if(optType == 'add'){
            $("#selOrderNo").val(data);
            //转到显示页面
            findOrderLi();
            //重新加载下拉框内容
            $('#searchorderKey_li').combobox({
                url: sys_rootpath + '/order/getOrderListBoxvalue?type=1&rmd=' + new Date().getTime(),
                valueField: 'orderno',
                textField: 'ordername',
                onSelect: function(record){
                    orderNoChange(record.orderno);
                }
            });
        }else{
            orderNoChange(data)
        }
    });
}
/**
 * 修改页面
 */
function toEditPage(){
    toBackPage();
    $("#orderInfoShow").css("display", "none");
    $("#orderEditForm").css("display", "block");
    $("#userAddType").val("");
    $("#addDeviceType").val("")
}
/**
 * 新增页面
 */
function toAddPage(){
    toBackPage();
    $("#orderInfoShow").css("display", "none");
    $("#orderAddForm").css("display", "block");
    $("#orderAddForm")[0].reset();
    $("#userIdAdd").val("");
    $("#userAddType").val("");

    $("#addDeviceType").val("add");
}

/**
 * 返回浏览页
 */
function toBackPage(){
    $("#orderInfoShow").css("display", "block");
    $("#orderEditForm").css("display", "none");
    $("#orderAddForm").css("display", "none");
    $("#orderSWUploadForm").css("display", "none");
}
/**
 * 软件上传页面
 */
function toUpLoadPage(){
    toBackPage();
    $("#orderInfoShow").css("display", "none");
    $("#orderSWUploadForm").css("display", "block");
}

/**
 * 清除信息
 */
function cleanOtherInfo(){
    $("#add_orderno").val("");
    $("#add_salesman").val("");
    $('#searchUserKey_add').combobox('setValue', '');
    $('#searchUserKey_edit').combobox('setValue', '');
    $("#add_batteryModel_batteryName").val("");
    $("#add_vehicleType_id").val("-1");
    $("#add_vehicleModel").empty();
    $("#add_vehicleModel").append("<option value='-1'>请选择</option>");
    $("#add_batteryModel_batteryType").val("-1");
    $("#add_factoryName").val("");
    $("#add_batteryNumber").val("");
    $("#add_capacity").val("");
    $("#add_checkOrderNo").val("0");

    //清除设备信息
    $("#edit_device_tab_BCU").html("");
    $("#edit_device_tab_BYU").html("");
    $("#edit_device_tab_BMU").html("");
    $("#edit_device_tab_LDM").html("");
    $("#edit_device_tab_DTU").html("");

    $("#add_device_tab_BCU").html("");
    $("#add_device_tab_BYU").html("");
    $("#add_device_tab_BMU").html("");
    $("#add_device_tab_LDM").html("");
    $("#add_device_tab_DTU").html("");

    $("#show_device_tab_BCU").html("");
    $("#show_device_tab_BYU").html("");
    $("#show_device_tab_BMU").html("");
    $("#show_device_tab_LDM").html("");
    $("#show_device_tab_DTU").html("");
}

