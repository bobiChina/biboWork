<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head lang="en">
    <meta charset="UTF-8">
    <link rel="stylesheet" href="./public/css/css.css"/>

    <title>site管理</title>
</head>
<body>
    <link type="text/css" rel="stylesheet" href="${ctx}/static/styles/page.css"></link>
    <script src="${ctx}/static/js/jquery.myPagination.js" type="text/javascript" charset="UTF-8"></script>
                <div class="airport-right-content-head">
                    <h2>site 管理</h2>
                </div>

                <div class="eg">
					 <a href="#" id="btnAdd" class="airport-bth airport-bth-m">添加</a>&nbsp;&nbsp;
					 <!-- <a href="#" id="btnDel" class="airport-bth airport-bth-m">删除</a>&nbsp;&nbsp;
					  -->
                </div>

                <div class="eg">
                    
                    <div class="airport-table">
                        <table id="mypage">
                           <thead>
                               <tr>
                                   <td>选择</td>
                                   <td>名称</td>
                                   <td>电话</td>
								   <td>注册日期</td>
                                   <td>操作</td>
                               </tr>
                           </thead>
                            <tbody>

                            </tbody>
                       </table> 
                    </div>
                    <div id="page"></div>  
                </div>
                
                    <input id="delId" type="hidden"/>
                    <div id="divAdd" style="display:none">
                        <form id="addForm" method="post">
                      	   <input type="hidden" id="id" name="id"/>
                      	   <input type="hidden" id="regdate" name="regdate"/>
                      	   <input type="hidden" id="customerId" name="customer.id"/>
                           
                           <div class="airport-from-label">
                                <label for="airport-from-id">名称</label> 
                                <input type="text" id="title" name="title" class="airport-input-text airport-input-text-s" onblur="return checkTitle(this);" />
                                <span id="titleTip"></span>
                            </div>

                            <div class="airport-from-label">
                                <label for="airport-from-id">联系电话</label>
                                <input id="contact"  type="text" name="contact" class="airport-input-text airport-input-text-s" onblur="return checkContact(this);" />
                                <span id="contactTip"></span>
                            </div> 

                            <div class="airport-from-label">
                                <label for="airport-from-id">手机</label>
                                <input id="phone"  type="text" name="phone" class="airport-input-text airport-input-text-s" onblur="return checkPhone(this);" />
                                <span id="phoneTip"></span>
                            </div>

                            <div class="airport-from-label">
                                <label for="airport-from-id">email</label>
                                <input id="email"  type="text" name="email" class="airport-input-text airport-input-text-s" onblur="return checkEmail(this);" />
                                <span id="emailTip"></span>
                            </div>
                            
							<div class="airport-from-label">
                                <label for="airport-from-id">地址</label>
                                <input id="addr"  type="text" name="addr" class="airport-input-text airport-input-text-s" onblur="return checkAddr(this);" />
                                <span id="addrTip"></span>
                            </div>
							<div class="airport-from-label">
                                <label for="airport-from-id">邮编</label>
                                <input id="zip"  type="text" name="zip" class="airport-input-text airport-input-text-s" onblur="return checkZip(this);" />
                                <span id="zipTip"></span>
                            </div>
							<div class="airport-from-label">
                                <label for="airport-from-id">欢迎页</label>
                                <input id="wellcomePage"  type="text" name="wellcomePage" class="airport-input-text airport-input-text-s" />
                                <span id="wellcomePageTip"></span>
                            </div>
							<div class="airport-from-label">
                                <label for="airport-from-id">验证页</label>
                                <input id="authurl"  type="text" name="authurl" class="airport-input-text airport-input-text-s" />
                                <span id="authurlTip"></span>
                            </div>
							<div class="airport-from-label">
                                <label for="airport-from-id">备注</label>
                                <input id="notes"  type="text" name="notes" class="airport-input-text airport-input-text-s" />
                                <span id="notesTip"></span>
                            </div>
                            <input id="btnSave" class="airport-bth airport-bth-s" type="button" value="确定" onclick="return checkData();" />
                            <input class="airport-bth airport-bth-s alert-button-canncel" type="button" value="关闭"/>
                            <input id="resetBtn" type="reset" style="display:none"/>
							
                        </form>
                    </div>

					<div id="divDel" style="display:none">
							<br/>
							<div class="airport-from-label">确定删除吗？
                            </div>
							<br/>
						<input id="btnDelOK" class="airport-bth airport-bth-s alert-button-canncel" type="button" value="确认">	
						<input class="airport-bth airport-bth-s alert-button-canncel" type="button" value="关闭">
					</div>

<style type="text/css">
    .jianfeitech-alert-inner div.jianfeitech-alert-con{  text-align: left;}
    .airport-bth-s{ margin-left: 40px;}
    .airport-from-label{padding-left: 40px;}
    .airport-from-label{  position: relative;}
    .onError{position:absolute; left: 375px; top:5px; }
</style>

<script>
    $(function(){
    	search();
    	
        $("#btnAdd").on({
            click:function(){
                $_element = $("#divAdd");
                var _alert = new JianfeitechGlobal();
                _alert.formAlert($_element,'新建site',"600");
            }
        });
        $("#btnDel").on({
            click:function(){
                $_element = $("#divDel");
                var _alert = new JianfeitechGlobal();
                _alert.formAlert($_element,'删除确认',"300");
            }
        });
        
		$("body").on("click","#btnSave",function(){
			$.post(${ctx}"/sconfig/sites/save", $(".jianfeitech-alert-con form").serialize(),function(data){
				//$(".jianfeitech-alert-con form input#resetBtn").trigger("click");
				search();
				$("#alert-jianfeitech").remove();
		    });
		});

        
//		$("body").on("click","#btnSave",function(){
//			$.post(${ctx}"/sconfig/sites/save", $(".jianfeitech-alert-con form").serialize(),function(data){
//				$(".jianfeitech-alert-con form input#resetBtn").trigger("click");
//				$("#alert-jianfeitech").remove();
//		    });
//		});
      
		$("body").on("click","#btnDelOK",function(){
			$.get(${ctx}"/sconfig/sites/delete", {id: $("#delId").val()},
					  function(data){
						search();
			});			
		});
    })
    
    function search() {
    	
		$("#page").myPagination({
			currPage : 1,
			pageCount : 10,
			pageSize : 5,
			ajax : {
				on : true, //开启状态
				callback : 'ajaxCallBack', //回调函数，注，此 ajaxCallBack 函数，必须定义在 $(function() {}); 外面
				url : '${ctx}/sconfig/sites/get',//访问服务器地址
				dataType : 'json', //返回类型
				param : {
					on : true,
					page : 1,
					pageCountId : 'pageCount'//后台不需要处理此参数
				}
			   //参数列表，其中on 必须开启，page 参数必须存在，其他的都是自定义参数，如果是多条件查询，可以序列化表单，然后增加 page参数
			}
		});
	}    
    
	//自定义 回调函数
	function ajaxCallBack(data) {
		//alert(data.result);   //显示服务器返回信息
		var result = data.result;
		
		var insetViewData = ""; //视图数据
		if(result != ""){
			$.each(result, function(index, items){
				insetViewData += createTR(items);
			});
		}else{
			insetViewData+= "<tr align='center'><td colspan='6' bgcolor='#FFFFFF' style='text-align:center;'><font color='red'>无记录...</font></td></tr>"
		}
		$("#mypage > tbody").html(insetViewData);
	}

	function createTR(obj) {
		var tr = "<tr align='center'>";
			tr += "<td><input type='checkbox'></td>";
			tr += "<td>" + obj.title + "</td>";
			tr += "<td>" + obj.phone + "</td>";
			tr += "<td>" + (obj.regdate.year-100+2000)+"-"+ (obj.regdate.month+1) +"-"+ obj.regdate.date + "</td>";
			tr += "<td><a href='#' onclick='editSite("+obj.id+")'>修改</a>";
			tr += "<a href='#' onclick='delSite("+obj.id+")'>删除</a></td>"
			tr += "</tr>";
		return tr;
	}  
	
	function editSite(id){
        $_element = $("#divAdd");
        var _alert = new JianfeitechGlobal();
        _alert.formAlert($_element,'修改site',"600");
        
		$.get(${ctx}"/sconfig/sites/find", { id: id },
		  function(data){
			result = eval('(' + data + ')'); 
			 $(".jianfeitech-alert-con form input#id").val(result.id);
			 $(".jianfeitech-alert-con form input#customerId").val(result.customer.id);
			 $(".jianfeitech-alert-con form input#regdate").val((result.regdate.year-100+2000)+"-"+ (result.regdate.month+1) +"-"+ result.regdate.date);
			 $(".jianfeitech-alert-con form input#title").val(result.title);
			 $(".jianfeitech-alert-con form input#contact").val(result.contact);
			 $(".jianfeitech-alert-con form input#phone").val(result.phone);
			 $(".jianfeitech-alert-con form input#email").val(result.email);
			 $(".jianfeitech-alert-con form input#addr").val(result.addr);
			 $(".jianfeitech-alert-con form input#zip").val(result.zip);
			 $(".jianfeitech-alert-con form input#wellcomePage").val(result.wellcomePage);
			 $(".jianfeitech-alert-con form input#authurl").val(result.authurl);
			 $(".jianfeitech-alert-con form input#notes").val(result.notes);
			 
		});
	}
	
	function delSite(id){
		$("#delId").val(id);
        $_element = $("#divDel");
        var _alert = new JianfeitechGlobal();
        _alert.formAlert($_element,'删除确认',"300");
	}
	
	//新增或修改门店操作
	function addOrUpdateSite(){
		$.post(${ctx}"/sconfig/sites/save", $(".jianfeitech-alert-con form").serialize(),function(data){
            $(".jianfeitech-alert-con form input#resetBtn").trigger("click");
            $("#alert-jianfeitech").remove();
            search();
        });
	}
	
	//JS校验    begin
	function checkTitle(obj){
		var titleTip = $(".jianfeitech-alert-con form #titleTip");
        if("" == obj.value){
            obj.focus();
            titleTip.attr("class", "onError");
            titleTip.html("请输入Site名称！");
            return false;
       }else{
    	   titleTip.attr("class", "");
    	   titleTip.html("");
           return true;
       }
	}
	
	function checkContact(obj){
		var contactTip = $(".jianfeitech-alert-con form #contactTip");
		
		var regContact = /^(\d{3,4}\-?)?\d{7,8}$/;
        if("" == obj.value){
          contactTip.attr("class", "onError");
          contactTip.html("请输入联系电话！");
          return false;
        }else if(!regContact.test(obj.value)){
          contactTip.attr("class", "onError");
          contactTip.html("联系电话格式不正确，请核查！");
          return false;
        }else{
        	contactTip.attr("class", "");
        	contactTip.html("");
            return true;
        }
	}
	
	function checkPhone(obj){
		var phoneTip = $(".jianfeitech-alert-con form #phoneTip");
		 
		var regPhone = /^1\d{10}$/;
        if("" == obj.value){
          phoneTip.attr("class", "onError");
          phoneTip.html("请输入手机号码！");
          return false;
        }else if(!regPhone.test(obj.value)){
          phoneTip.attr("class", "onError");
          phoneTip.html("手机号码格式不正确，请核查！");
          return false;
        }else{
          phoneTip.attr("class", "");
          phoneTip.html("");
          return true;
        }
	}
	
	function checkEmail(obj){
		var emailTip = $(".jianfeitech-alert-con form #emailTip");
		
		var regEmail = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if("" == obj.value){
          emailTip.attr("class", "onError");
          emailTip.html("请输入email！");
          return false;
        }else if(!regEmail.test(obj.value)){
          emailTip.attr("class", "onError");
          emailTip.html("email格式不正确，请核查！");
          return false;
        }else{
          emailTip.attr("class", "");
          emailTip.html("");
          return true;
        }
	}
	
	function checkAddr(obj){
		var addrTip = $(".jianfeitech-alert-con form #addrTip");
		
		if("" == obj.value){
          addrTip.attr("class", "onError");
          addrTip.html("请输入地址！");
          return false;
        }else{
          addrTip.attr("class", "");
          addrTip.html("");
          return true;
        }
	}
	
	function checkZip(obj){
		var zipTip = $(".jianfeitech-alert-con form #zipTip");
		
		var regZip =  /^[1-9][0-9]{5}$/; //开头不能为0,共6位
        if("" == obj.value){
          zipTip.attr("class", "onError");
          zipTip.html("请输入邮编！");
          return false;
        }else if(!regZip.test(obj.value)){
          zipTip.attr("class", "onError");
          zipTip.html("邮编格式不正确，请核查！");
          return false;
        }else{
          zipTip.attr("class", "");
          zipTip.html("");
          return true;
        }
	}
	
	function checkData(){
		var title = $(".jianfeitech-alert-con form input[name='title']").val();
        var titleTip = $(".jianfeitech-alert-con form #titleTip");
        
        var contact = $(".jianfeitech-alert-con form input[name='contact']").val();
        var contactTip = $(".jianfeitech-alert-con form #contactTip");
        
        var phone = $(".jianfeitech-alert-con form input[name='phone']").val();
        var phoneTip = $(".jianfeitech-alert-con form #phoneTip");
        
        var email = $(".jianfeitech-alert-con form input[name='email']").val();
        var emailTip = $(".jianfeitech-alert-con form #emailTip");
        
        var addr = $(".jianfeitech-alert-con form input[name='addr']").val();
        var addrTip = $(".jianfeitech-alert-con form #addrTip");
        
        var zip = $(".jianfeitech-alert-con form input[name='zip']").val();
        var zipTip = $(".jianfeitech-alert-con form #zipTip");
        
        //总的提示内容
        var checkResult = "";
          
        if("" == title){
      	   titleTip.attr("class", "onError");
      	   titleTip.html("请输入Site名称！");
           checkResult = checkResult + "请输入Site名称！<br />";
        }
        
        var regContact = /^(\d{3,4}\-?)?\d{7,8}$/;
        if("" == contact){
      	  contactTip.attr("class", "onError");
      	  contactTip.html("请输入联系电话！");
          checkResult = checkResult + "请输入联系电话！<br />";
        }else if(!regContact.test(contact)){
      	  contactTip.attr("class", "onError");
      	  contactTip.html("联系电话格式不正确，请核查！");
      	  checkResult = checkResult + "联系电话格式不正确，请核查！<br />";
        }
        
        var regPhone = /^1\d{10}$/;
        if("" == phone){
          phoneTip.attr("class", "onError");
          phoneTip.html("请输入手机号码！");
          checkResult = checkResult + "请输入手机号码！<br />";
        }else if(!regPhone.test(phone)){
    	  phoneTip.attr("class", "onError");
    	  phoneTip.html("手机号码格式不正确，请核查！");
    	  checkResult = checkResult + "手机号码格式不正确，请核查！<br />";
        }
        
        var regEmail = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if("" == email){
          emailTip.attr("class", "onError");
          emailTip.html("请输入email！");
          checkResult = checkResult + "请输入email！<br />";
        }else if(!regEmail.test(email)){
          emailTip.attr("class", "onError");
          emailTip.html("email格式不正确，请核查！");
          checkResult = checkResult + "email格式不正确，请核查！<br />";
        }
        
        
        if("" == addr){
          addrTip.attr("class", "onError");
          addrTip.html("请输入地址！");
          checkResult = checkResult + "请输入地址！<br />";
        }
        
        var regZip =  /^[1-9][0-9]{5}$/; //开头不能为0,共6位
        if("" == zip){
          zipTip.attr("class", "onError");
          zipTip.html("请输入邮编！");
          checkResult = checkResult + "请输入邮编！<br />";
        }else if(!regZip.test(zip)){
          zipTip.attr("class", "onError");
          zipTip.html("邮编格式不正确，请核查！");
          checkResult = checkResult + "邮编格式不正确，请核查！<br />";
        }
      
        if("" != checkResult){
            return false;
        }
        //调用新增或修改
        addOrUpdateSite();
	}
	
	//JS校验  end
</script>

</body>
</html>