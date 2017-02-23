/**
 * Created by wly on 2016/3/21.
 */
//系统虚拟目录
var sys_rootpath = document.location.pathname.substring(0,document.location.pathname.substring(1).indexOf('/')+1);

if(sys_rootpath == "/order"){
    sys_rootpath = "";
}

/********************************************************************************************************************
* 订单日志
 *******************************************************************************************************************/
function del_tracklog(){
    $.messager.confirm("删除确认", "是否确认删除最新一条日志记录？", function(cr){
        if(cr){
            $.easyui.loading({ msg: "正在提交...", topMost: true });
            $.post(sys_rootpath + "/order/delOrderTrack?rmd=" + new Date().getTime(), { orderNo:$("#selOrderNo").val()},function(data){
                $.easyui.loaded(true);
                orderNoChange($("#selOrderNo").val());
                if(data == "ok"){
                    //单独更新日志不能同步更新状态
                    $.messager.alert("系统提示", "操作成功", "warning");
                }else{
                    $.messager.alert("系统提示", "操作失败", "warning");
                }
            });
        }
    });
}

function show_tracklog(){
    //获取已选择
    $.easyui.showDialog({
        title: "订单跟踪日志填写",
        width: 700, height: 516,
        content:$("#order_tracklog_div").html(),
        topMost: true,
        enableApplyButton: false,
        onSave: function (d) {
            var dataForm = d.form("getData");

            var orderStatus = dataForm.orderStatus;
            var actionDate = dataForm.actionDate;

            if(orderStatus == undefined){
                $.messager.alert("系统提示", "请选择事件", "warning");
                return false;
            }

            /*if("" == actionDate){
             $.messager.alert("系统提示", "事件日期不能为空", "warning");
             return false;
             }*/

            var actionMan = dataForm.actionMan;
            if("" == actionMan && orderStatus != "9" && orderStatus != "10"){
                $.messager.alert("系统提示", "相关人员不能为空", "warning");
                return false;
            }

            /* 屏蔽各交期 2016年5月25日 14:11:36
             var reviewDelivery = dataForm.reviewDelivery;
             if("" == reviewDelivery && orderStatus == "2"){
             $.messager.alert("系统提示", "评审交期不能为空", "warning");
             return false;
             }

             var developDelivery = dataForm.developDelivery;
             if("" == developDelivery && orderStatus == "4"){
             $.messager.alert("系统提示", "开发交期不能为空", "warning");
             return false;
             }

             var testDelivery = dataForm.testDelivery;
             if("" == testDelivery && orderStatus == "6"){
             $.messager.alert("系统提示", "测试交期不能为空", "warning");
             return false;
             }*/

            var remarks = dataForm.remarks;
            remarks = $.trim(remarks);
            /*if("" == remarks){
             $.messager.alert("系统提示", "更新内容不能为空", "warning");
             return false;
             }*/

            if(remarks.length > 210){
                $.messager.alert("系统提示", "更新内容不能大于200字", "warning");
                return false;
            }

            /* 屏蔽各交期 2016年5月25日 14:11:36
             var isDiff = dataForm.isDiff;
             if(isDiff){
             var newDelivery = dataForm.newDelivery ;
             if("" == newDelivery){
             $.messager.alert("系统提示", "新交期不能为空", "warning");
             return false;
             }
             }*/
            if(dataForm.reviewDevelopDuration != undefined && dataForm.reviewDevelopDuration == ""){
                $.messager.alert("系统提示", "开发工时不能为空", "warning");
                return false;
            }
            if(dataForm.reviewTestDuration != undefined && dataForm.reviewTestDuration == ""){
                $.messager.alert("系统提示", "测试工时不能为空", "warning");
                return false;
            }
            if(dataForm.developDuration != undefined && dataForm.developDuration == ""){
                $.messager.alert("系统提示", "开发工时不能为空", "warning");
                return false;
            }
            if(dataForm.testDuration != undefined && dataForm.testDuration == ""){
                $.messager.alert("系统提示", "测试工时不能为空", "warning");
                return false;
            }
            d.form('submit',{
                url: sys_rootpath + '/order/saveTrack?rmd=' + new Date().getTime() ,
                onSubmit:function(param){
                    $.easyui.loading({ msg: "正在保存...", topMost: true });
                    return true; // 返回false终止表单提交
                },
                success: function(data){
                    $.easyui.loaded(true);
                    orderNoChange($("#selOrderNo").val()); //单独更新日志不能同步更新状态
                    $.messager.alert("系统提示", "操作成功", "info");
                    //getTrackLog();
                }
            });

            return true;
        }
    });
}

function selAction(obj, actionType){
    var className = $(obj).parent("li").attr('class');
    if("manage-nochange" == className){
        $.messager.alert("系统提示", "当前事件不可操作", "warning");
        return;
    }
    $(obj).parents("ul").find("li").each(function(indexNum, ele){
        if("manage-nochange" == $(this).attr('class')){

        }else{
            $(this).removeClass("manage-leftactive");
        }
    });
    $(obj).parent("li").addClass("manage-leftactive");
    var str = $("#logHtml_" + actionType).html();
    str = str.replace(/td_/g, "td").replace(/tr_/g, "tr").replace(/table_/g, "table");
    str = str.replace(/TD_/g, "td").replace(/TR_/g, "tr").replace(/TABLE_/g, "table");

    $(obj).closest("div.dialog-content").find("#show_logHtml_div").html(str);
}
/********************************************************************************************************************
* 订单日志
 *******************************************************************************************************************/



/**
 * 软件上传表单
 */
function submitForm_bak(){
    var rs = true;
    var idsArr = [];
    // var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_" + "s19";
    var noUploadMap = new Map();
    var uploadMap = new Map();

    /**********************************************************************
     * 1）BCU BYU BMU LDM DTU 单类上传
     * 2）BCU (C11/C11B) 上传时可以没有csf文件
     * 3）软件变更，覆盖上传
     **********************************************************************/
    var key_up = "";
    var value_up = "";
    $(".euploadify-f").each(function(i, e){
        key_up = $(this).attr("id");
        value_up = $(this).euploadify("getValues");
        idsArr.push("#" + key_up);
        if("" == value_up || "," == value_up){
            rs = false;
            /*************************************************************
             * 添加绝缘检测配置文件不做限制 sby 2016年5月18日 20:21:33
             *************************************************************/
            if("BCU_C11_0_csf" == key_up || "BCU_C11B_0_csf" == key_up ){
                //可不上传csf文件
            }
            else{
                noUploadMap.put(key_up.substring(0, 3), "1");//存设备类型
            }
        }else{
            uploadMap.put(key_up.substring(0, 3), key_up.substring(0, 3));//存设备类型
        }
    });
    $(idsArr.join(',')).euploadify("validate");

    /**
    * 单独一个LCD时，判断不准确
    * if(idsArr.length == 0){
    *    $.messager.alert("系统提示", "无上传项", "warning");
    *    return ;
    * }
     */

    /****************************************************
     * 查询使用模版的情况 tq 2016年4月25日 17:38:57
     * 发现使用模版时也验证是否有其他文件未上传
     ****************************************************/
    $("input[value='usetemplate'][type='hidden']").each(function(i, ele){
        uploadMap.put($(this).attr("id").substring(0, 3), $(this).attr("id").substring(0, 3));//存设备类型
    });

    if(uploadMap.size() == 0){
        $.messager.alert("系统提示", "无软件上传", "warning");
        return;
    }

    /***************************************
     * 循环上传项，找出未上传的设备类型分类
     ***************************************/
    var checkDeviceClass = true;
    uploadMap.each(function(key,value,index){
        if("1" == noUploadMap.get(key)){
            checkDeviceClass = false;
        }
    });

    if(!checkDeviceClass){
        $.messager.alert("系统提示", "请确保单一设备类型完整上传", "warning");
        return;
    }

    var msgStr = "保存操作会覆盖原先上传的软件<br>是否确认保存订单软件？";
    if(!rs){
        msgStr = "上传项有未上传的文件<br>是否确认继续上传保存操作？";
    }
    $.messager.confirm("软件上传保存确认", msgStr, function(cr){
        if(cr){
            $('#orderSWUploadForm').form('submit',{
                url: sys_rootpath + '/order/orderFileUpload',
                onSubmit:function(param){
                    $.easyui.loading({ msg: "正在保存...", topMost: true });
                    $("#upload_orderno").val($("#selOrderNo").val());
                    return true; // 返回false终止表单提交
                },
                success: function(data){
                    $.easyui.loaded(true);
                    orderNoChange($("#selOrderNo").val());
                    $.messager.alert("系统提示", "操作成功", "info");
                }
            });
        }
    });

    /*if(!rs || idsArr.length == 0){
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
                        /!*var rs = true;
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
                         }*!/
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
    }*/
}
/**
 * 是否使用模版
 * @param obj
 * @param device_class
 * @param device_code
 * @param device_bum_no
 * @param device_type
 */
function isModuleSel(obj, device_class, device_code, device_bum_no, device_type, fileType){
    if($(obj).is(':checked')){
        var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
        idStr = idStr.replace("(", "-L").replace(")", "-R");// 替换左右括号
        $("#" + idStr + fileType).euploadify("destroy");
        $("#" + idStr + "td").html("<font color='red'>已使用模版文件</font><input id=\"" + idStr + fileType + "\" name=\"" + idStr + fileType + "_devicefile\" type=\"hidden\" value=\"usetemplate\" />");
    }else{
        var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
        idStr = idStr.replace("(", "-L").replace(")", "-R");// 替换左右括号
        $("#" + idStr + "td").html("<input id=\"" + idStr + fileType + "\" name=\"" + idStr + fileType + "_devicefile\" class=\"easyui-euploadify\" type=\"text\" />");
        var orderno = $("#selOrderNo").val();
        if(fileType == "zip"){
            addEupload(idStr + "zip", "*.zip;*.rar;", orderno, device_code, device_class, device_type, device_bum_no);
        }else{
            addEupload(idStr + "s19", "*.s19;", orderno, device_code, device_class, device_type, device_bum_no);
        }

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
    //device_code = device_code.replace("(", "\(").replace(")", "\)");
    var idStrs = [];
    var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
    idStr = idStr.replace("(", "-L").replace(")", "-R");// 替换左右括号
    var add_device_str = "<tr style='color:#000; background-color:#E2E2E5; width: 790px;'>" +
        "<td style=\"text-align:right; width:90px; border:0px; height:33px;\">设备类型：</td>" +
        "<td style=\"text-align:left;border:0px; height:33px; width: 700px;\" colspan='2'>" + device_class +
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设备型号：" + device_code;
    if("BMU" == device_class){
        add_device_str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID编号：#" + device_bum_no  ;
    }else{
        add_device_str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" ;
    }
    add_device_str += "</td></tr>";
    if(device_class == "BCU" || device_class == "BYU" || device_class =="OTHER"){
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
            "<input id=\"" + idStr + "csf\" name=\"" + idStr + "csf_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td></tr>";
        idStrs.push(idStr + "csf");
    }else if(device_class == "BMU"){
        idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
            "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
            "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "', 's19')\" />启用模版</label></td></tr>";
        idStrs.push(idStr + "s19");

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">配置文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
            "<input id=\"" + idStr + "cfg\" name=\"" + idStr + "cfg_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td></tr>";
        idStrs.push(idStr + "cfg");
    }else if(device_class == "LDM"){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
            "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
            "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "', 's19')\" />启用模版</label></td></tr>";
        idStrs.push(idStr + "s19");

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">配置文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
            "<input id=\"" + idStr + "cfg\" name=\"" + idStr + "cfg_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td></tr>";
        idStrs.push(idStr + "cfg");
    }
    else if(device_class == "DTU" ){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
            "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
            "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "', 's19')\" />启用模版</label>" +
            "</td></tr>";
        idStrs.push(idStr + "s19");
    }
    else if(device_class == "LCD" ){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">压缩文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
            "<input id=\"" + idStr + "zip\" name=\"" + idStr + "zip_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
            "<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
            "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "', 'zip')\" />启用模版</label>" +
            "</td></tr>";
        idStrs.push(idStr + "zip");
    }
    else if(device_class == "CS" ){
        // CS 无上传文件
    }

    $("#upload_device_tab_" + device_class).append(add_device_str);
    var orderno = $("#selOrderNo").val();
    var suffixStr = "";
    var eId = "";
    //初始化上传控件
    for(var i=0; i<idStrs.length; i++){
        eId = idStrs[i];
        suffixStr = eId.substring(eId.length -3);
        if(suffixStr == "zip"){
            suffixStr = "*.zip;*.rar;";
        }else{
            suffixStr = "*." + suffixStr + ";";
            // suffixStr = "*." + eId.substring(eId.length -3) + ";";
        }
        addEupload(eId, suffixStr, orderno, device_code, device_class, device_type, device_bum_no);
    }
    //return add_device_str;
}

/**
 * 初始化文件上传
 * @param device_class
 * @param device_code
 * @param device_bum_no
 * @param device_type
 * @param file_type
 */
function showUploadDevice4Again(device_class, device_code, device_bum_no, device_type, file_type){
    var idStrs = [];
    var idStr = device_class + "_" + device_code + "_" + device_bum_no + "_";
    var add_device_str = "";
    var trObj = $("#" + idStr + file_type + "_" + device_type).parents("tr");
    trObj.html("");
    device_code = device_code.replace("-L", "(").replace("-R", ")");//设备型号还原
    if(device_class == "BCU" || device_class == "BYU" || device_class == "OTHER"){
        if(file_type == "s19"){
            add_device_str =
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
                "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td>";
            idStrs.push(idStr + "s19");
        }else if(file_type == "cfg"){
            add_device_str =
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">配置文件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
                "<input id=\"" + idStr + "cfg\" name=\"" + idStr + "cfg_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td>";
            idStrs.push(idStr + "cfg");
        }else if(file_type == "csf"){
            add_device_str =
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">规则文件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
                "<input id=\"" + idStr + "csf\" name=\"" + idStr + "csf_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td>";
            idStrs.push(idStr + "csf");
        }
    }else if(device_class == "BMU"){
        if(file_type == "s19"){
            add_device_str =
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
                "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
                "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" " +
                "onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "', 's19')\" />启用模版</label></td>";
            idStrs.push(idStr + "s19");
        }else if(file_type == "cfg"){
            add_device_str +=
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">配置文件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
                "<input id=\"" + idStr + "cfg\" name=\"" + idStr + "cfg_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td>";
            idStrs.push(idStr + "cfg");
        }
    }else if(device_class == "LDM"){
        if(file_type == "s19"){
            add_device_str =
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
                "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
                "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" " +
                "onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "', 's19')\" />启用模版</label></td>";
            idStrs.push(idStr + "s19");
        }else if(file_type == "cfg"){
            add_device_str =
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">配置文件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" >" +
                "<input id=\"" + idStr + "cfg\" name=\"" + idStr + "cfg_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:center; width:120px; border:0px; height:40px;\"></td>";
            idStrs.push(idStr + "cfg");
        }
    }
    else if(device_class == "DTU" ){
        if(file_type == "s19"){
            add_device_str =
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">订单固件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
                "<input id=\"" + idStr + "s19\" name=\"" + idStr + "s19_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
                "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" " +
                "onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "', 's19')\" />启用模版</label></td>";
            idStrs.push(idStr + "s19");
        }
    }
    else if(device_class == "LCD" ){
        if(file_type == "zip"){
            add_device_str =
                "<td style=\"text-align:right; width:90px; border:0px; height:40px;\">压缩文件：</td>" +
                "<td style=\"text-align:left; border:0px; height:40px;\" width=\"*\" id=\"" + idStr + "td\">" +
                "<input id=\"" + idStr + "zip\" name=\"" + idStr + "zip_devicefile\" class=\"easyui-euploadify\" type=\"text\" /></td>" +
                "<td style=\"text-align:right; padding-right: 20px; width:180px; border:0px; height:40px;\">" +
                "<label><input type='checkbox' id=\"" + idStr + "checkbox\" name=\"checkbox\" " +
                "onclick=\"isModuleSel(this,'" + device_class + "','" + device_code + "','" + device_bum_no + "','" + device_type + "', 'zip')\" />启用模版</label></td>";
            idStrs.push(idStr + "zip");
        }
    }
    else if(device_class == "CS" ){
        // CS 无上传文件
    }

    //$("#upload_device_tab_" + device_class).append(add_device_str);
    trObj.html(add_device_str);
    var orderno = $("#selOrderNo").val();
    var suffixStr = "";
    var eId = "";
    //初始化上传控件
    for(var i=0; i<idStrs.length; i++){
        eId = idStrs[i];
        suffixStr = eId.substring(eId.length -3);
        if(suffixStr == "zip"){
            suffixStr = "*.zip;*.rar;";
        }else{
            suffixStr = "*." + suffixStr + ";";
            // suffixStr = "*." + eId.substring(eId.length -3) + ";";
        }
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
    // $(document.getElementById('ID名称'))  idStr.replace("(", "\(").replace(")", "\)")
    $("#" + idStr).euploadify({
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
    var deviceCodeTD = $(obj).parents("table").find("tr").eq(1).find("td").eq(1);

    var device_code_arr = [];
    var formStr = getFormStr();
    var selStr = $(obj).val();
    if("OTHER" == selStr){
        deviceCodeTD.html("");
        deviceCodeTD.html("<input type=\"text\" class=\"manage-input-text\" style=\"width:202px;\" id=\"add_device_code_p\" name=\"add_device_code_p\" value=\"\" maxlength=\"15\" />");
    }else{
        deviceCodeTD.html("");
        var str = "<select class=\"manage-select\" id=\"add_device_code_p\" name=\"add_device_code_p\" class=\"form-control input-sm\"  style=\"height:28px; width:207px;\">" +
            "<option value= \"\">请选择</option>" +
            "</select>";
        deviceCodeTD.html(str);
        selObj = $(obj).parents("table").find("select").eq(1);
    }
    //已选设备型号
    $("#" + formStr + " input[name='device_code_sel']").each(function(x, ele){
        device_code_arr.push($(this).val());
    });


    if("-1" == selStr){
        //selObj.empty();
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
        //一体机ID从 2 开始
        var byuHtmlStr = $("#" + formStr + " #" + optType + "_device_tab_BYU").eq(0).html();
        if(byuHtmlStr != null && byuHtmlStr != undefined && byuHtmlStr.length > 0){
            bmu_no = 2;
        }
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
    else if("LCD" == selStr){
        selObj.empty();
        bmuTR.css("display", "none");
        for(var i=0; i<lcdArr.length; i++){
            if(device_code_arr.indexOf(lcdArr[i]) > -1){
            }
            else{
                selObj.append("<option value='" + lcdArr[i] + "'>" + lcdArr[i] + "</option>");
            }
        }
    }
    else if("CS" == selStr){
        selObj.empty();
        bmuTR.css("display", "none");
        for(var i=0; i<csArr.length; i++){
            if(device_code_arr.indexOf(csArr[i]) > -1){
            }
            else{
                selObj.append("<option value='" + csArr[i] + "'>" + csArr[i] + "</option>");
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

                var formStr = getFormStr();

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
 * 获取对应的form表单ID
 */
function getFormStr(){
    var opt_type = $("#addDeviceType").val();
    if(opt_type == 'add'){
        return "orderAddForm";
    }else if(opt_type == 'edit'){
        return "orderEditForm";
    }
    else if(opt_type == 'add_sw'){
        return "orderAddSWForm";
    }
    else if(opt_type == 'edit_module'){
        return "orderEditModuleForm";
    }
}

/**
 * 选择订单设备
 * @param opt_type
 */
function show_device_add(opt_type){
    //引用的系统配置不能修改
    var shAddType = $("#page_shAddType").val();
    var selOrderNo = $("#selOrderNo").val();
    var orderType = $("#page_orderType").val();
    if("edit_module" == opt_type && shAddType == 1){
        $.messager.alert("系统提示", "引用订单不能增加系统配置", "warning");
        return;
    }

    //获取已选择
    $.easyui.showDialog({
        title: "选择订单设备",
        width: 500, height: 280,
        content:$("#test_dialog").html(),
        topMost: true,
        enableApplyButton: false,
        onSave: function (d) {
            var formStr = getFormStr();
            var dataForm = d.form("getData");
            if(String.isNullOrEmpty(dataForm.add_device_code_p) || dataForm.add_device_class_p == "-1" ){
                $.messager.alert("系统提示", "请选择设备信息", "warning");
                return false;
            }
            else if(String.isNullOrEmpty(dataForm.add_device_count)){
                $.messager.alert("系统提示", "请填写数量", "warning");
                return false;
            }else if(dataForm.add_device_class_p == "OTHER"){
                //检查是否符合书写规则  大写字母加数字
                var reg = /^[0-9A-Z]*$/g;
                var deviceCodeStr = dataForm.add_device_code_p ;

                if(reg.test(deviceCodeStr)){
                    //检查是否已填写该型号

                    var isUse = false;
                    //已选设备型号
                    $("#" + formStr + " input[name='device_code_sel']").each(function(x, ele){
                        if($(this).val() == deviceCodeStr ){
                            isUse = true;
                        }
                    });
                    if(isUse){
                        $.messager.alert("系统提示", "设备型号已存在", "warning");
                        return false;
                    }

                    //检查是否存在于服务已有设备
                    if(Array.contains(bcuArr, deviceCodeStr)){
                        $.messager.alert("系统提示", "设备型号存在于BCU中", "warning");
                        return false;
                    }
                    if(Array.contains(byuArr, deviceCodeStr)){
                        $.messager.alert("系统提示", "设备型号存在于BYU中", "warning");
                        return false;
                    }

                    if(Array.contains(bmuArr, deviceCodeStr)){
                        $.messager.alert("系统提示", "设备型号存在于BMU中", "warning");
                        return false;
                    }
                    if(Array.contains(ldmArr, deviceCodeStr)){
                        $.messager.alert("系统提示", "设备型号存在于LDM中", "warning");
                        return false;
                    }
                    if(Array.contains(dtuArr, deviceCodeStr)){
                        $.messager.alert("系统提示", "设备型号存在于DTU中", "warning");
                        return false;
                    }
                    if(Array.contains(lcdArr, deviceCodeStr)){
                        $.messager.alert("系统提示", "设备型号存在于LCD中", "warning");
                        return false;
                    }
                    if(Array.contains(csArr, deviceCodeStr)){
                        $.messager.alert("系统提示", "设备型号存在于CS中", "warning");
                        return false;
                    }

                    $("#" + opt_type + "_device_tab_" + dataForm.add_device_class_p).append(addDeviceTR(dataForm.add_device_class_p, dataForm.add_device_code_p , dataForm.add_device_bmu_p, opt_type, dataForm.add_device_count));
                    /*****************************************
                     * 滚动到底部
                     *****************************************/
                    $("#" + opt_type + "_device_tab").closest("div.scroll-class").scrollTop($("#" + opt_type + "_device_tab").closest("div.scroll-class")[0].scrollHeight );

                }else{
                    $.messager.alert("系统提示", "设备型号格式为大写字母加数字组合", "warning");
                    return false;
                }
            }
            else{
                var device_tab_BCU = $("#" + opt_type + "_device_tab_BCU").html();
                var device_tab_BYU = $("#" + opt_type + "_device_tab_BYU").html();
                var device_tab_BMU = $("#" + opt_type + "_device_tab_BMU").html();

                /*****************************************
                 * 主机型号只有一个(2016年4月8日 11:00:02)
                 *****************************************/
                if(dataForm.add_device_class_p == "BCU"){
                    $("#" + opt_type + "_device_tab_BCU").html("");
                    if(device_tab_BYU.length > 0){
                        $("#" + opt_type + "_device_tab_BYU").html("");
                        if(device_tab_BMU.length > 0){
                            $("#" + opt_type + "_device_tab_BMU").html("");
                        }
                    }else{
                        var isUse = false;
                        //判断从机是否有值且是从1开始的
                        $("#" + formStr + " input[name='device_bmu_no']").each(function(index, ele){
                            if($(this).val() == 1){
                                isUse = true;
                            }
                        });
                        if(! isUse){//没有从机ID为 1 的，清除所有从机
                            $("#" + opt_type + "_device_tab_BMU").html("");
                        }
                    }
                }else if(dataForm.add_device_class_p == "BYU"){
                    $("#" + opt_type + "_device_tab_BYU").html("");
                    if(device_tab_BCU.length > 0){
                        $("#" + opt_type + "_device_tab_BCU").html("");
                        if(device_tab_BMU.length > 0){
                            $("#" + opt_type + "_device_tab_BMU").html("");
                        }
                    }else{
                        var isUse = false;
                        //判断从机是否有值且是从1开始的
                        $("#" + formStr + " input[name='device_bmu_no']").each(function(index, ele){
                            if($(this).val() == 1){
                                isUse = true;
                            }
                        });
                        if(isUse){//有从机ID为 1 的，清除所有从机
                            $("#" + opt_type + "_device_tab_BMU").html("");
                        }
                    }
                }
                /*****************************************
                 * 一体机不带从机(2016年5月12日 16:54:00)
                 * 一体机带从机 从机编号从2开始
                 if(dataForm.add_device_class_p == "BYU"){
                            $("#" + opt_type + "_device_tab_BMU").html("");
                        }
                 if(dataForm.add_device_class_p == "BMU" && $("#" + opt_type + "_device_tab_BYU").html() != ""){
                            $.messager.alert("系统提示", "一体机不带从机", "warning");
                            return false;
                        }
                 *****************************************/
                /********************************************************
                 * LDM(绝缘检测)型号只能选择一个(2016年4月22日 15:54:25 hgd)
                 ********************************************************/
                if(dataForm.add_device_class_p == "LDM"){
                    $("#" + opt_type + "_device_tab_LDM").html("");
                }

                $("#" + opt_type + "_device_tab_" + dataForm.add_device_class_p).append(addDeviceTR(dataForm.add_device_class_p, dataForm.add_device_code_p , dataForm.add_device_bmu_p, opt_type, dataForm.add_device_count));
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
 * @param device_count
 * @returns {string}
 */
function addDeviceTR(device_class, device_code, device_bum_no, optType, device_count){
    //添加新售后订单，并且是引用软件时，数量默认为0
    if($("#orderSelSHForm").css("display") == "block"){
        device_count = 0;
    }
    var orderType = $("#page_orderType").val();
    var shAddType = $("#page_shAddType").val();
    var add_device_str = "<tr>" +
        "<td style=\"text-align:right; width:90px; border:0px; height:37px; line-height: 37px;\">设备类型：</td>" +
        "<td style=\"text-align:left; width:60px; border:0px; height:37px; line-height: 37px;\">" + device_class +
        "<input type=\"hidden\" name=\"device_class_sel\" value=\"" + device_class + "\" /></td>" +
        "<td style=\"text-align:right; width:80px; border:0px; height:37px; line-height: 37px;\">设备型号：</td>" +
        "<td style=\"text-align:left; width:80px; border:0px; height:37px; line-height: 37px;\">" + device_code + "</td>";
    if("BMU" == device_class){
        add_device_str += "<td style=\"text-align:left; width:140px; border:0px; height:37px; line-height: 37px;\">&nbsp;&nbsp;&nbsp;ID&nbsp;编号：#" + device_bum_no +
            "</td><td style=\"text-align:left; border:0px; height:37px; line-height: 37px; line-height: 37px; \" width=\"*\" >" +
            "<input type=\"hidden\" name=\"device_bmu_no\" value=\"" + device_bum_no + "\" />";
        // add_device_str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数量：" + device_count + "<input type=\"hidden\" name=\"device_count\" value=\"" + device_count + "\" />";
        // 硬件售后显示数量 其他不显示数量 <input type="text"
        if(orderType == 3){
            add_device_str += "&nbsp;&nbsp;数量：<input type=\"text\" class=\"manage-input-text\" style=\"width:50px;\" maxlength='4' name=\"device_count\" value=\"" + device_count + "\" onkeyup=\"this.value=this.value.replace(/\D/g,\'\'); \" onafterpaste=\"this.value=this.value.replace(/\D/g,\'\')\" />";
        }else{
            add_device_str += "<input type=\"hidden\" name=\"device_count\" value=\"" + device_count + "\" />";
        }
    }else{
        add_device_str += "<td style=\"text-align:left; width:120px; border:0px; height:37px; line-height: 37px;\">&nbsp;</td><input type=\"hidden\" name=\"device_bmu_no\" value=\"0\" />";
        //add_device_str += "<td style=\"text-align:left; border:0px; height:37px; line-height: 37px;\" width=\"*\" >" + device_count + "<input type=\"hidden\" name=\"device_count\" value=\"" + device_count + "\" />";
        // 不显示数量
        add_device_str += "<td style=\"text-align:left; border:0px; height:37px; line-height: 37px;\" width=\"*\" >";

        if(orderType == 3){
            add_device_str += "&nbsp;&nbsp;数量：<input type=\"text\" class=\"manage-input-text\" style=\"width:50px;\" maxlength='4' name=\"device_count\" value=\"" + device_count + "\" onkeyup=\"this.value=this.value.replace(/\D/g,\'\'); \" onafterpaste=\"this.value=this.value.replace(/\D/g,\'\')\" />";
        }else{
            add_device_str += "<input type=\"hidden\" name=\"device_count\" value=\"" + device_count + "\" />";
        }
    }
    add_device_str += "<input type=\"hidden\" name=\"device_code_sel\" value=\"" + device_code + "\" /></td>" +
        "<td style=\"text-align:left; width:180px; border:0px; height:37px; line-height: 37px;\">";
    if(optType == "sw"){
        add_device_str += "&nbsp;"
    }else{
        add_device_str += "<img onclick='delDeviceTr(this, \"" + optType + "\")' src='" + sys_rootpath + "/static/images/del.png' style='width: 20px; margin-top:10px; cursor: pointer;'>" ;
    }
    add_device_str += "</td></tr>";
    return add_device_str;
}
/***************************************************************
 * 原JS整理
 ***************************************************************/
/**
 * 订单编号验证
 */
function checkOrderNo(optType){
    window.setTimeout(function(){
        $.post(sys_rootpath + "/order/checkOrderNo?rmd=" + new Date().getTime(), { orderno:$("#" + optType + "_orderno").val()},function(data){
            if(data == "isuse"){
                $("#" + optType + "_checkOrderNo").val("0");
            }else{
                $("#" + optType + "_checkOrderNo").val("1");
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
function delOrderFun(id, orderno, status, list_type){
    $.messager.confirm("删除确认", "是否确认删除订单？<br>编号：" + orderno , function(cr){
        if(cr){
            $.post(sys_rootpath + "/order/delorder?rmd=" + new Date().getTime(), {orderid : id, orderno : orderno},function(data){
                var msg = "操作失败";
                if(data == "ok"){
                    msg = "操作成功";
                    //操作成功才会刷新左侧
                    $("#selOrderNo").val("");
                    findOrderLi(list_type);
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
    if(showDelIconType == 1){
        if(type == 1){
            $(obj).find('.handle').show();
        }else{
            $(obj).find('.handle').hide();
        }
    }
}

function showDelIcon(obj, type){
    $("#orderdata").find("li").each(function(indexNum, ele){
        $(this).removeClass("manage-lefthover")
    });
    if(type == 1){
        $(obj).addClass("manage-lefthover");
    }
    if(showDelIconType == 1){
        if(type == 1){
            $(obj).find('.handle').show();
        }else{
            $(obj).find('.handle').hide();
        }
    }
}

function showDelIcon_dtu(obj, type){
    if(showDelIconType == 1){
        if(type == 1){
            $(obj).find('.handle').show();
        }else{
            $(obj).find('.handle').hide();
        }
    }
}

function findOrderLi(list_type){
    var selId = $("#selOrderNo").val();
    $("#orderdata").html("");
    $.post(sys_rootpath + "/order/getOrderList?list_type=" + list_type + "&rmd=" + new Date().getTime(), {type:1},function(data){
        var li = "";
        var result = eval(data);
        $.each(result, function(index, items){
            li += "<li orderLIData='" + items[1] + "' style='position:relative;'";
            //if( items[2] < 2){
                li += " onmouseover='showDelIcon(this, 1)' onmouseout='showDelIcon(this, 0)' ";
            //}
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
            /*if( items[2] == 2){
                li += "<a href='javascript:;' onclick='orderNoChange(\"" + items[1] + "\")' title='已删除'><font color='red'>" + items[1] + "</font>" ;
            }else{*/
                li += "<a href='javascript:;' onclick='orderNoChange(\"" + items[1] + "\")'>" + items[1] ;
            //}
            li += "</a>";
            li += "<div class='handle' style='position: absolute; right: 5px; margin-top: -30px; display: none;'>";
            li += "<img onclick='delOrderFun(" + items[0] + ",\"" + items[1] + "\", " + items[2] + ", " + list_type + ")' src='" + sys_rootpath + "/static/images/del.png' style='position: absolute; top: 0px; right: 0px; width: 20px; cursor: pointer; '>";
            li += "</div>";
            li += "</li>";
        });
        $("#orderdata").html(li);
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
    $("#addDeviceType").val("edit")
}
/**
 * 新增页面
 */
function toAddPage(){
    toBackPage();
    $("#orderInfoShow").css("display", "none");
    $("#orderAddForm").css("display", "block");
    $("#orderAddForm")[0].reset();
    //$("#userIdAdd").val("");
    //$("#userAddType").val("");

    $("#addDeviceType").val("add");
}

function toAddSHPage(){
    toBackPage();
    $("#orderInfoShow").css("display", "none");
    $("#orderAddSWForm").css("display", "block");
    $("#orderAddSWForm")[0].reset();

    $("#addDeviceType").val("add_sh");
}

/**
 * 返回浏览页
 */
function toBackPage(){
    $("#orderInfoShow").css("display", "block");
    $("#orderEditForm").css("display", "none");
    $("#orderAddForm").css("display", "none");
    $("#orderSWUploadForm").css("display", "none");
    $("#orderAddSWForm").css("display", "none");//售后订单添加页面
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
    //清除上传项数据
    /**
     * eupload需先销毁才能清除
     */
    $(".euploadify-f").each(function(i, e){
        $("#" + $(this).attr("id")).euploadify("destroy");
    });
    $("#upload_device_tab_BCU").html("");
    $("#upload_device_tab_BYU").html("");
    $("#upload_device_tab_BMU").html("");
    $("#upload_device_tab_LDM").html("");
    $("#upload_device_tab_DTU").html("");
    $("#upload_device_tab_LCD").html("");
    $("#upload_device_tab_OTHER").html("");

    $("#add_orderno").val("");
    $("#add_salesman").val("");
    $('#searchUserKey_add').combobox('setValue', '');
    $('#searchUserKey_edit').combobox('setValue', '');
    $("#add_batteryModel_batteryName").val("");
    $("#add_vehicleType_id").val("");
    $("#add_vehicleModel").empty();
    $("#add_vehicleModel").append("<option value=''>请选择</option>");
    $("#add_batteryModel_batteryType").val("");
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
    $("#edit_device_tab_LCD").html("");
    $("#edit_device_tab_CS").html("");
    $("#edit_device_tab_OTHER").html("");

    $("#add_device_tab_BCU").html("");
    $("#add_device_tab_BYU").html("");
    $("#add_device_tab_BMU").html("");
    $("#add_device_tab_LDM").html("");
    $("#add_device_tab_DTU").html("");
    $("#add_device_tab_LCD").html("");
    $("#add_device_tab_CS").html("");
    $("#add_device_tab_OTHER").html("");

    $("#show_device_tab_BCU").html("");
    $("#show_device_tab_BYU").html("");
    $("#show_device_tab_BMU").html("");
    $("#show_device_tab_LDM").html("");
    $("#show_device_tab_DTU").html("");
    $("#show_device_tab_LCD").html("");
    $("#show_device_tab_CS").html("");
    $("#show_device_tab_OTHER").html("");
}


/**
 * （暂时未使用）
 * @param device_class
 * @param device_code
 * @param device_bum_no
 * @param order_deviceFileArr
 * @returns {string}
 */
function showDeviceTR(device_class, device_code, device_bum_no, order_deviceFileArr){
    //var fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_s19"];
    //alert(JSON.stringify(order_deviceFileArr));

    var add_device_str = "<tr style='color:#000; background-color:#E2E2E5;'>" +
        "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">设备类型：</td>" +
        "<td style=\"text-align:left; width:60px; border:0px; height:37px;\">" + device_class + "</td>" +
        "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">设备型号：</td>" +
        "<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" >" + device_code ;
    if("BMU" == device_class){
        add_device_str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID编号：#" + device_bum_no  ;
    }
    add_device_str += "</td></tr>";
    if(device_class == "BCU" || device_class == "BYU"){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" >";
        if(undefined != order_deviceFileArr){
            fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_s19"];
            if(undefined != fileObj){
                add_device_str += "<div style='float: left;margin-left: 10px;'><a href='javascript:alert(\"" + fileObj[1] + "\");' " + fileObj[1] +
                    "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
                    "</div></td></tr>";
            }else{
                add_device_str += "&nbsp;</td></tr>";
            }
        }else{
            add_device_str += "&nbsp;</td></tr>";
        }

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">配置文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" >";
        if(undefined != order_deviceFileArr){
            fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_cfg"];
            if(undefined != fileObj){
                add_device_str += "<div style='float: left;margin-left: 10px;'><a href='javascript:alert(\"" + fileObj[1] + "\");' " + fileObj[1] +
                    "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
                    "</div></td></tr>";
            }else{
                add_device_str += "&nbsp;</td></tr>";
            }
        }else{
            add_device_str += "&nbsp;</td></tr>";
        }

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">规则文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" >";
        if(undefined != order_deviceFileArr){
            fileObj = order_deviceFileArr[device_code + "_" + device_bum_no + "_csf"];
            if(undefined != fileObj){
                add_device_str += "<div style='float: left;margin-left: 10px;'><a href='javascript:alert(\"" + fileObj[1] + "\");' " + fileObj[1] +
                    "</a></div><div style='float: right; margin-right: 20px;'>" + fileObj[2] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + fileObj[3] +
                    "</div></td></tr>";
            }else{
                add_device_str += "&nbsp;</td></tr>";
            }
        }else{
            add_device_str += "&nbsp;</td></tr>";
        }

    }else if(device_class == "BMU" || device_class == "LDM"){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" >" + "" + "</td></tr>";

        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">配置文件：</td>" +
            "<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" >" + "" + "</td></tr>";
    }else if(device_class == "BMU" ){
        add_device_str += "<tr>" +
            "<td style=\"text-align:right; width:90px; border:0px; height:37px;\">订单固件：</td>" +
            "<td style=\"text-align:left; border:0px; height:37px;\" width=\"*\" >" + "" + "</td></tr>";
    }
    return add_device_str;
}




/*********************************************************************
 * 数组相关 方法
 ********************************************************************/
function contains(arr, obj) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] === obj) {
            return true;
        }
    }
    return false;
}

/*********************************************************************
 * map 方法
 ********************************************************************/
Array.prototype.remove = function(s) {
    for (var i = 0; i < this.length; i++) {
        if (s == this[i])
            this.splice(i, 1);
    }
}

/**
 * Simple Map
 *
 *
 * var m = new Map();
 * m.put('key','value');
 * ...
 * var s = "";
 * m.each(function(key,value,index){
 *      s += index+":"+ key+"="+value+"/n";
 * });
 * alert(s);
 *
 * @author dewitt
 * @date 2008-05-24
 */
function Map() {
    /** 存放键的数组(遍历用到) */
    this.keys = new Array();
    /** 存放数据 */
    this.data = new Object();

    /**
     * 放入一个键值对
     * @param {String} key
     * @param {Object} value
     */
    this.put = function(key, value) {
        if(this.data[key] == null){
            this.keys.push(key);
        }
        this.data[key] = value;
    };

    /**
     * 获取某键对应的值
     * @param {String} key
     * @return {Object} value
     */
    this.get = function(key) {
        return this.data[key];
    };

    /**
     * 删除一个键值对
     * @param {String} key
     */
    this.remove = function(key) {
        this.keys.remove(key);
        this.data[key] = null;
    };

    /**
     * 遍历Map,执行处理函数
     *
     * @param {Function} 回调函数 function(key,value,index){..}
     */
    this.each = function(fn){
        if(typeof fn != 'function'){
            return;
        }
        var len = this.keys.length;
        for(var i=0;i<len;i++){
            var k = this.keys[i];
            fn(k,this.data[k],i);
        }
    };

    /**
     * 获取键值数组(类似Java的entrySet())
     * @return 键值对象{key,value}的数组
     */
    this.entrys = function() {
        var len = this.keys.length;
        var entrys = new Array(len);
        for (var i = 0; i < len; i++) {
            entrys[i] = {
                key : this.keys[i],
                value : this.data[i]
            };
        }
        return entrys;
    };

    /**
     * 判断Map是否为空
     */
    this.isEmpty = function() {
        return this.keys.length == 0;
    };

    /**
     * 获取键值对数量
     */
    this.size = function(){
        return this.keys.length;
    };

    /**
     * 重写toString
     */
    this.toString = function(){
        var s = "{";
        for(var i=0;i<this.keys.length;i++,s+=','){
            var k = this.keys[i];
            s += k+"="+this.data[k];
        }
        s+="}";
        return s;
    };
}

function testMap(){
    var m = new Map();
    m.put('key1','Comtop');
    m.put('key2','南方电网');
    m.put('key3','景新花园');
    alert("init:"+m);

    m.put('key1','康拓普');
    alert("set key1:"+m);

    m.remove("key2");
    alert("remove key2: "+m);

    var s ="";
    m.each(function(key,value,index){
        s += index+":"+ key+"="+value+"/n";
    });
    alert(s);
}