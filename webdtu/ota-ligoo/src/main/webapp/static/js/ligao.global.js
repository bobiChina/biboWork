(function($){

	/**输入框提示**/
	$.fn.inputTipsText= function(options){
		var defaults = {

        }
	    var options = $.extend(defaults, options);

	    $(this).on({
	    	focus:function(){
	    		if($(this).val() == this.defaultValue){
	    			$(this).val("");
	    		}
	    	},

	    	blur:function(){
	    		if($(this).val() == ''){
	    			$(this).val(this.defaultValue);
	    		}
	    	}
	    });
	};

	/**提示 Tips**/
	$.fn.tipsbox= function(options){
		var defaults = {
			"tipsLeft":"-10px",
			"tipsTop":"40px"
        }
	    var options = $.extend(options, defaults);

	    var _tipsElement = $(this);
	    var _tipsElementBox = _tipsElement.find(".tips-box-js-outer");
	    var _timeId;
	    if(_tipsElementBox.length > 0){
	    	
	    	_tipsElement.on({
	    		/*"click":function(){
	    			_tipsElementBox.css({
	    				"left":options.tipsLeft,
	    				"top":options.tipsTop
	    			});
	    			_tipsElementBox.fadeIn();
	    			$(".tips-box-js-outer").not(_tipsElementBox).css({
	    				"display":"none"
	    			});
	    		},*/


	    	    "mouseenter": function(e){
					_tipsElementBox.css({
	    				"left":options.tipsLeft,
	    				"top":options.tipsTop
	    			});
	    			_tipsElementBox.fadeIn();
	    			$(".tips-box-js-outer").not(_tipsElementBox).css({
	    				"display":"none"
	    			});

	    			//e.preventDefault();
	    		},
	    		
	    		"mouseleave":function(e){
	    				_tipsElementBox.fadeOut();
	    				//e.preventDefault();
	    		}

	    	});

	    	_tipsElementBox.on({
	    		"mouseleave":function(e){
	    			//_tipsElementBox.fadeOut();
	    			//e.preventDefault();
	    		}
	    	});
	    }
	};


	//地图标记操作
	$.fn.tipsInitStop= function(options){
		var defaults = {
		
        }
	    var options = $.extend(defaults, options);

	    $(".bus-stop-con").each(function(){
	    	var _w = $(this).width()+14;
	    	//alert(_w);
	    	$(this).parent().parent().css({
	    		"width": _w+"px"
	    	});
	    });

	    $(".see-stop-info-close").on({
	    	click:function(e){
	    		$(this).parent().parent().fadeOut('fast');
	    		e.stopPropagation();

	    	}
	    });

	   	$(".bus-stop").on({
	   		click:function(e){
	   			var _busStopConWidth = $(this).children(".bus-stop-info").width();
	   			//console.log(_busStopConWidth);
	   			var _busStopConWidth = (203 - _busStopConWidth)/2 ;
	   			$(this).children(".bus-stop-see-info").css({
	   				"left": -_busStopConWidth+"px"
	   			}).fadeIn('fast');
	   			
	   		}
	   	});

	   	
	   	if($(".map-box-head-screen").length > 0){
	   		$(".map-box-head-screen").on({
		   		click:function(){
		   			$(".head-outer").css({
		   				display:"none"
		   			});
		   			$(".footer-outer").css({
		   				display:"none"
		   			});

		   			$(".map-box-head").css({
		   				display:"none"
		   			});


		   			$(".body-outer").css({
		   				"marginTop":"0",
		   				"width":"100%"
		   			});

		   			$(".map-outer").css({
		   				"width":"100%"
		   			});

		   			$(".map-wrap").css({
		   				"width":"100%"
		   			});

		   			$("#allmap").css({
		   				"width":$(window).width()+"px",
		   				"height":$(window).height()+"px"
		   			});

		   			var _colse_span_html = "<div class=\"amplify-screen-wrap-box\"><div class=\"amplify-screen-inner-box\"><span class=\"amplify-screen-colse\">退出全屏<\/span></div></div>";
		   			$(".map-img").append($(_colse_span_html));
		   		}
		   	});
	   	}

	   	


	   	$("body").on('click','.amplify-screen-colse',function(){
	   				$(".head-outer").css({
		   				display:"block"
		   			});
		   			$(".footer-outer").css({
		   				display:"block"
		   			});

		   			$(".map-box-head").css({
		   				display:"block"
		   			});


		   			$(".body-outer").css({
		   				"marginTop":"15px",
		   				"width":"970px"
		   			});

		   			$(".map-outer").css({
		   				"width":"970px"
		   			});

		   			$(".map-wrap").css({
		   				"width":"970px"
		   			});

		   			$("#allmap").css({
		   				"width":"960px",
		   				"height":"500px"
		   			});

		   			$("div.amplify-screen-wrap-box").remove();
	   	});


	};


	//fixed table head
	$.fn.fixedHead = function(options){
		var defaults = {
			headElement : "thead",
			setTableWidth:"1400",
			cutTb:"0"
        };
	    var options = $.extend(defaults, options);
	    var _this = $(this);
	    var _tr = $(this).find("tbody tr").eq(0).height();
	    var _tableBodyHeight = _tr * options.nums-1;
	    //console.log($(this).height());
	    //console.log(_tableBodyHeight);
	    
	    if($(window).scrollTop() > _this.position().top){
			_this.find(options.headElement).css({
				"position":"fixed",
				"top":0,
				"left":"50%",
				"marginLeft":"-485px"
			})
		}else{
			_this.find(options.headElement).css({
				"position":"absolute"
			})
		}
	    
		if($(this).height() > _tableBodyHeight){
			$(window).on({
				"scroll":function(){
					//console.log("Window Top:"+ $(window).scrollTop());
					//console.log("This Scroll Top:"+_this.position().top);
	
					if($(window).scrollTop() > _this.position().top){
						_this.find(options.headElement).css({
							"position":"fixed",
							"top":0,
							"left":"50%",
							"marginLeft":"-485px"
						})
					}else{
						_this.find(options.headElement).css({
							"position":"absolute"
						})
					}
				}
			});
			
		}


		var _tableFixWidth = 0; //动态设计fixed表格宽度

		


		//设置表格宽度
		if(!$(this).find(".history-save-table-head-inner").hasClass("complete-data")){

			//关联上下表体与表头
		if(options.cutTb.length > 0){
			var _mTbWith = $(this).find("tr").eq(0).find("td").map(function(){
				return $(this).width();
			});

			for(var _b=0; _b<_mTbWith.length; _b++){
				_tableFixWidth += parseInt(_mTbWith[_b]);
			}
			
			//console.log(_tableFixWidth);

			var a = 0; //数组起始标记
			var _mTableHeadWidth = []; //头部宽度标记
			var _mt = 0;
			for(var i = 0; i< options.cutTb.length; i++){
				var n = a + options.cutTb[i]; //数组结束标记
				//console.log("xh"+ a + "****** "+ n);
				//console.log("分切间隔:"+a+","+n);
				var p = _mTbWith.slice(a,n);
				a = n;
				//console.log("分切数据:"+ p.length);
				for(var m = 0; m < p.length; m++){
					_mt =_mt+p[m]+2;
					//console.log(m+":"+p[m]);
				}
				_mTableHeadWidth.push(_mt);
				_mt = 0;
				_p = 0;
				
			}

			//console.log(_mTableHeadWidth);

			$(".history-save-table-head-inner > span").each(function(_s){
				$(this).css({
					width: _mTableHeadWidth[_s] + "px"
				})
			});

			$(".history-save-table-head-inner > span").eq(0).css({
				//"marginLeft":"-2px"
			}).end().eq($(".history-save-table-head-inner > span").length -1).css({
				//"marginLeft":"-2px"
			});
		}

		_this.find(".history-save-table-head-inner").css({
			"marginLeft":"-5px"
		})

			$(this).find(".history-save-table-head-inner").css({
					"width":parseInt(_tableFixWidth + 60)  + "px"
			}).addClass("complete-data");

			$(this).find(".history-table-con-inner").css({
				"width":parseInt(_tableFixWidth + 70)  + "px"
			}).addClass("complete-data");
		}
		
		
	};

	//set Head 
	$.fn.setThead = function(options){
		var defaults = {
			"setColumnsWidth"			:[40,80,156,102,119,121,119,48,103,72],
			"setHeadElement"			: "thead tr",
			"setHeadElementChildren" 	: "th"
        };
	    var options = $.extend(defaults, options);
	    var _tdWidth = options.setColumnsWidth;

	    var i = 0;
	    $(this).find(options.setHeadElement).eq(0).find(options.setHeadElementChildren).each(function(){
	    	if(0 == i){
	    		$(this).width(_tdWidth[i] + 0 + "px");
	    		$(this).css({"paddingLeft":"8px"})
	    	}else{
	    		$(this).width(_tdWidth[i] + "px");
	    	}
	    	
	    	i++;
	    });

	    var a = 0;
	    $(this).find("tbody tr").eq(0).find("td").each(function(){
	    	
	    	$(this).width(_tdWidth[a] + "px");
	    	a++;
	    });
	};


	//Tab 切换
	$.fn.navTab = function(options){
		var defaults = {
		
        };
	    var options = $.extend(defaults, options);

	    $(this).each(function(){
	    	$(this).children("span").on({
	    		"mouseenter":function(){
	    			$(this).addClass("readtime-hover");
	    		},
	    		"mouseleave":function(){
	    			$(this).removeClass("readtime-hover");
	    		},

	    		"click":function(){
	    			var _index = $(this).index();
	    			$(this).addClass("readtime-active").siblings().removeClass("readtime-active");
	    			$(options.toItem).find(options.itemList).eq(_index).css({"display":"block"}).siblings().css({"display":"none"});
	    		}

	    	});
	    });
	};


	//设置滚动Table
	$.fn.setScrollTable = function(options){
		var defaults 	= {};
		var options 	= $.extend(defaults,options);

		var _totalTr 	= $(this).find("table tbody tr").length;
		var _trHeight	= $(this).find("table tr").height();
		if(options.trHeight > 0){
			_trHeight = options.trHeight;
		}
		var _tbodyHeight= _totalTr*_trHeight;

		$(this).find(".historty-table-con-outer").css({height:_tbodyHeight+1+"px"});

		var _tableHeadHeight	= $(".history-save-table-head").height();
		var _tableFooterHeight 	= $(".table-footer").height();

		$(this).find(".table-body").height(_tbodyHeight + "px");
	};


	//Manage Tab
	$.fn.manageTab = function(options){
		var defaults = {};
		var options = $.extend(defaults,options);
		var _tabElement = options.tabElement;
		var _this = $(this);

		$(this).on({
			mouseenter:function(){
				$(this).addClass("manage-lefthover");
			},
			mouseleave:function(){
				$(this).removeClass("manage-lefthover");
			},
			click:function(){
				var _thisIndex = $(this).index();
				$(this).addClass("manage-leftactive").siblings().removeClass("manage-leftactive");
				$(_tabElement).eq(_thisIndex).css({"display":"block"}).siblings().css({display:"none"});
			}
		});

	};


	$.fn.createTable = function(options){

		var defaults = {
			headElement : "thead",
			//setTableWidth:"1400",
			cutTb:"0",
			data:""
	    };

		var options = $.extend(defaults, options);

			var _tdStr = "";					//拼接表格
			var _mTdStr = "";	
												//临时生成表格
			var _bmu = options.data;
			//console.log(_bmu);

			var _voltageNums = 1;    //定义电压表格个数
			if(_bmu[0].bmu[options.index] != undefined && _bmu[0].bmu[options.index].hasOwnProperty("singleVoltageList")){
				_voltageNums = _bmu[0].bmu[options.index].singleVoltageList.split(",").length;    //定义电压表格个数
			}
			var _temperNums = 1;			//定义温度表格个数
			if( _bmu[0].bmu[options.index] != undefined && _bmu[0].bmu[options.index].hasOwnProperty("boxTemperList")){
				_temperNums = _bmu[0].bmu[options.index].boxTemperList.split(",").length;			//定义温度表格个数
			}
			var _ampNums = 1;			//定义电流表格个数
			if(_bmu[0].bmu[options.index] != undefined && _bmu[0].bmu[options.index].hasOwnProperty("balanceAmpList")){
				_ampNums = _bmu[0].bmu[options.index].balanceAmpList.split(",").length;			//定义电流表格个数
			}
			
			//========================生成表头======================================//

			options.cutTb = [1,_voltageNums, _temperNums, 1, _ampNums, 1, 1];

			var _mHeadStrTd =  "<tr class=\"trfirst\">";
			_mHeadStrTd += "<td><\/td>";

			for(var _mi = 0; _mi < options.cutTb[1]; _mi++){
				_mHeadStrTd += "<td>"+ (_mi + 1) +"</td>"
			}
			
			for(var _mi = 0; _mi < options.cutTb[2]; _mi++){
				_mHeadStrTd += "<td>"+ (_mi + 1) +"</td>"
			}


			_mHeadStrTd +=  "<td></td>";


			for(var _mi = 0; _mi < options.cutTb[4]; _mi++){
				_mHeadStrTd += "<td>"+ (_mi + 1) +"</td>"
			}

			_mHeadStrTd  +=  "<td></td>";
			_mHeadStrTd  +=  "<td></td>";
			_mHeadStrTd  +=  "</tr>";

			_tdStr += _mHeadStrTd;
			

			//=============================动态生成表格==================================//

			for(var i = 0; i < _bmu.length; i++){

				var _dateObject = _bmu[i].insertTime;
				var _fromatDate = getDateStr(_dateObject);

				_tdStr += "<tr>";

				_tdStr += "<td>"+ _fromatDate + "<\/td>";

				var singleVoltageList = [];    //定义电压表格个数

				if(_bmu[0].bmu[options.index] != undefined && _bmu[0].bmu[options.index].hasOwnProperty("singleVoltageList")){
					singleVoltageList = _bmu[0].bmu[options.index].singleVoltageList.split(",");    //定义电压表格个数
				}else{
					singleVoltageList.push("00");
				}
				_eachCreateVoltageTD(singleVoltageList, _voltageNums);    	//生成电压表格

				var boxTemperList = [];			//定义温度表格个数

				if(_bmu[0].bmu[options.index] != undefined && _bmu[0].bmu[options.index].hasOwnProperty("boxTemperList")){
					boxTemperList = _bmu[0].bmu[options.index].boxTemperList.split(",");			//定义温度表格个数
				}else{
					boxTemperList.push("00");
				}
				_eachCreateTemperTD(boxTemperList, _temperNums);			//生成温度表格
				
				//console.log(_bmu[0].bmu[options.index].singleVoltageList.split(","));

				//生成均衡状态
				var _balanceStatus = options.data[i].balanceStatus;

				if(1 == _balanceStatus){
					_tdStr += "<td>正在均衡<\/td>";	
				}else{
					_tdStr += "<td>关闭均衡<\/td>";	
				}

				var balanceAmpList = [];			//定义电流表格个数

				if(_bmu[0].bmu[options.index] != undefined && _bmu[0].bmu[options.index].hasOwnProperty("balanceAmpList")){
					balanceAmpList = _bmu[0].bmu[options.index].balanceAmpList.split(",");			//定义电流表格个数
				}else{
					boxTemperList.push("00");
				}
				_eachCreateAmpTD(balanceAmpList, _ampNums);					//生成电流表格

				//加热状态
				if( _bmu[i].bmu[options.index] != undefined && 1 == _bmu[i].bmu[options.index].hotStatus){
					_tdStr += "<td>开启<\/td>";
				}else{
					_tdStr += "<td>关闭<\/td>";
				}

				//风扇状态
				if(_bmu[i].bmu[options.index] != undefined && 1 == _bmu[i].bmu[options.index].fanStatus){
					_tdStr += "<td>开启<\/td>";
				}else{
					_tdStr += "<td>关闭<\/td>";
				}

				_tdStr += "<\/tr>";
				
			}

			//================================== 生成电压表格 ============================ //

			function _eachCreateVoltageTD(_tableData, nums){
				//var _voltageNums = 
				_mTdStr = "";
				_createTD(_tableData,nums);

				_tdStr += _mTdStr;
			}

			//=================================== 生成温度表格 ========================== //

			function _eachCreateTemperTD(_tableData, nums){
				_mTdStr = "";
				_createTD(_tableData,nums);
				_tdStr += _mTdStr;
			}

			//==================================  生成电流表格 =========================== //

			function _eachCreateAmpTD(_tableData, nums){
				_mTdStr = "";
				_createTD(_tableData, nums);
				_tdStr += _mTdStr;
			}

			 
			//================================== 生成TD ================================ //

			function _createTD(_tableData, nums){
				//console.log("<<<<<<<<<<"+_tableData);
				if(1 == nums){
					if(_tableData != null && _tableData != undefined && _tableData != ""){
						var _mTmpStr = _tableData[0].substr(0,(_tableData[0].length-1));
						_mTmpStr = _mTmpStr.substr(1);
						_mTdStr += "<td>"+_mTmpStr+"</td>";
					}else{
						_mTdStr += "<td></td>";
					}
				}else{
					for(var _i = 0; _i < nums; _i++){
						if(_tableData[_i] != undefined){
							if(_tableData[_i].indexOf('[') < 0 && _tableData[_i].indexOf(']') < 0){
								_mTdStr += "<td>"+ _tableData[_i]+"</td>";
							}else{
								if(_tableData[_i].indexOf('[') < 0){
									_mTdStr += "<td>"+ _tableData[_i].substr(0,_tableData[_i].length-1)+"</td>";
								}else if(_tableData[_i].indexOf(']') < 0){
									_mTdStr += "<td>"+ _tableData[_i].substr(1)+"</td>";
								}
							}
							
						}else{
							_mTdStr += "<td>"+ "..." +"</td>"
						}
						
					}
				}

				return _mTdStr;

			}

			//================================ 生成日期 =============================== //

			function getDateStr(obj){
				var year = obj.year-100+2000;
				var month = obj.month+1;
				if (month <10)
					month = "0"+month;
				var date = obj.date;
				if (date <10)
					date = "0"+date;
				var hours = obj.hours;
				if (hours <10)
					hours = "0"+hours;
				var minutes = obj.minutes;
				if (minutes<10)
					minutes = "0"+minutes;
				var seconds = obj.seconds;
				if (seconds<10)
					seconds = "0"+seconds;
				return year+"-"+month+"-"+date+" "+hours+":"+minutes+":"+seconds;
			}



			$(".history-table-con-inner tbody").append($(_tdStr));
			//console.log(_tdStr);		



		var _this = $(this);
		var _tr = $(this).find("tbody tr").eq(0).height();
		var _mTableBodyHeight = (_tr * (_bmu.length) + 42); 				//动态生成的表格的高度
		var _tableBodyHeight = _tr * options.nums-1;

		
		//console.log(_tableBodyHeight);

		$(this).css({
			height: _mTableBodyHeight + "px"
		});

		$(this).find(".historty-table-con-outer").css({
			height: _mTableBodyHeight + "px"
		});


		if($(this).height() > _tableBodyHeight){
			$(window).on({
				"scroll":function(){
					//console.log("Window Top:"+ $(window).scrollTop());
					//console.log("This Scroll Top:"+_this.position().top);
					if($(window).scrollTop() > _this.position().top){
						_this.find(options.headElement).css({
							"position":"fixed",
							"top":0,
							"left":"50%",
							"marginLeft":"-485px"
						})
					}else{
						_this.find(options.headElement).css({
							"position":"absolute"
						})
					}
				}
			});	
		}


		var _tableFixWidth = 0; //动态设计fixed表格宽度

			


		//设置表格宽度
		if(!$(this).find(".history-save-table-head-inner").hasClass("complete-data")){

				//关联上下表体与表头
			if(options.cutTb.length > 0){
				var _mTbWith = $(this).find("tr").eq(0).find("td").map(function(){
					return $(this).width();
				});
				//console.log(_mTbWith);
				for(var _b=0; _b<_mTbWith.length; _b++){
					_tableFixWidth += parseInt(_mTbWith[_b]);
				}
				//console.log(_tableFixWidth);
				var a = 0; //数组起始标记
				var _mTableHeadWidth = []; //头部宽度标记
				var _mt = 0;
				for(var i = 0; i< options.cutTb.length; i++){
					var n = a + options.cutTb[i]; //数组结束标记
					//console.log("xh"+ a + "****** "+ n);
					//console.log("分切间隔:"+a+","+n);
					var p = _mTbWith.slice(a,n);
					a = n;
					//console.log("分切数据:"+ p.length);
					//console.log(p);
					for(var m = 0; m < p.length; m++){
						_mt =_mt+p[m]+ 2;
						//console.log(m+":"+p[m]);
					}

					//console.log(_mt);
					_mTableHeadWidth.push(_mt);
					_mt = 0;
					_p = 0;
				}
				//console.log(_mTableHeadWidth);
				$(".history-save-table-head-inner > span").each(function(_s){
					$(this).css({
						width: _mTableHeadWidth[_s] +  "px"
					})
				});

				//console.log($(".history-save-table-head-inner > span:last").width());
				

				$(".history-save-table-head-inner > span").eq(0).css({
					//"marginLeft":"-2px"
					}).end().eq($(".history-save-table-head-inner > span").length -1).css({
					//"marginLeft":"-2px"
					});
				}

				_this.find(".history-save-table-head-inner").css({
					"marginLeft":"-5px"
				})

				$(this).find(".history-save-table-head-inner").css({// wly 2015年11月23日16:14:22   50 >> 10
						"width":(parseInt(_tableFixWidth) + 40)  + "px"
				}).addClass("complete-data");

				//var _lastSpan = $(".history-save-table-head-inner > span:last").width() - (_voltageNums + _temperNums + _ampNums + 1);
				
				var _notLastSpan = 0;
				$(".history-save-table-head-inner > span").not(":last").each(function(_s){
					_notLastSpan += $(this).width();
				});

				var _lastSpan = (parseInt(_tableFixWidth) + 10) - parseInt(_notLastSpan);//wly 2015年11月23日16:14:22   50 >> 10

				if(_lastSpan < 140){
					_lastSpan = 140;
				}
				$(".history-save-table-head-inner > span:last").css({
					width: _lastSpan + "px"
					//console.log($(this).width());
				});

				$(this).find(".history-table-con-inner").css({  //wly 2015年11月23日16:14:22  60>>50
					"width":(parseInt(_tableFixWidth) + 50)  + "px"
				}).addClass("complete-data");
			}


			
	};

	//日历生成
	$.fn.calendar = function(options){

			var defaults 	= {};
			var options 	= $.extend(defaults, options);

			//-----------------------------------------------------
			
			var _staticYear = new Date().getFullYear();

			//-----------------------------------------------------

			//-----------------------------------------------------


			// Url ======: ../static/images/


			var _defaultHtml = "<style type=\"text/css\">"
								+	".js-calendar-dleft{ float: left; background: url(../static/images/js-calendar-dleft.gif) repeat-y center top; width: 324px; position: relative;}"
								+	".js-calendar-dleft-head{ background: url(../static/images/js-calendar-dleft-head.gif) no-repeat center top; width: 324px; height: 55px; line-height: 55px;}"
								+	".js-calendar-dleft-footer{ position: absolute; background: url(../static/images/js-calendar-dleft-footer.png) no-repeat 1px top; width: 324px; height: 15px; left: 0; bottom: 0;}"
								+	".js-calendar-head-dleft{  background: url(../static/images/js-calendar-head-dleft.gif) repeat-y center top; border-bottom: 1px solid #cccccc; width: 315px; width: 318px\0;width: 318px\9; margin: 0 auto; height: 40px; line-height: 40px;}"
								+	".js-calendar-dleft-con ul li{ width: 314px; margin: 0 auto; height: 40px; line-height: 40px; border-bottom: 1px solid #ccc; text-align: center; font-weight: bold; cursor: pointer;}"
								+	".js-calendar-up-year{ cursor: pointer; width: 60px; text-align: center; float: left; display: block; color: #000000;}"
								+	".js-calendar-down-year{ cursor: pointer; width: 60px; text-align: center; float: right; display: block; color: #000000;}"
								+	".js-calendar-year{ width: 195px; text-align: center; float: left; display: block; font-size: 16px; font-weight: bold;}"
								+	".js-calendar-dright{ float: right; position: relative; background: url(../static/images/js-calendar-dright.gif) repeat-y center top; width: 635px;}"
								+	".js-calendar-dright-head{ background: url(../static/images/js-calendar-dright-head.gif) no-repeat center top; height: 55px; line-height: 55px; width: 635px;}"
								+	".js-calendar-dright-footer{ background: url(../static/images/js-calendar-dright-footer.png) no-repeat 0px -1px; height: 11px; position: absolute; bottom: 0; left: 0px; width: 635px;}"
								+	".js-calendar-head-dright{ background: url(../static/images/js-calendar-head-dright.gif) repeat-y center top; height: 40px; line-height: 40px; border-bottom: 1px solid #ccc; width: 625px; margin: 0 auto;}"
								+	".js-calendar-up-month{ cursor: pointer; width: 100px; text-align: center; float: left; display: block; color: #000000;}"
								+	".js-calendar-down-month{ cursor: pointer; width: 100px; text-align: center; float: right; display: block; color: #000000;}"
								+	".js-calendar-month{ width: 425px; text-align: center; float: left; display: block; font-size: 16px; font-weight: bold;}"
								+	".js-calendar-con-head{ height: 40px; line-height: 40px; border-bottom: 1px solid #cccccc;}"
								+   ".js-calendar-td a{ display:block; width:100%; height:100%}"
								+	".js-calendar-dright-con{ width: 625px; margin: 0 auto; overflow: hidden; zoom: 1;}"
								+	".js-calendar-con-head span{ display: block; width: 88px; float: left; text-align: center; color: #787878; font-weight: bold;}"
								+	".js-calendar-td{ font-weight: bold; font-size: 14px; float: left; border-bottom: 1px solid #cbcaca; border-right: 1px solid #cbcaca; width: 88px; height: 74px; line-height: 74px; text-align: center;}"
								+	".js-calendar-dright-head{ font-size: 14px; font-weight: bold; text-align: center; color: #FFF;}"
								+	".js-calendar-dleft-head{ font-size: 14px; font-weight: bold; text-align: center; color: #FFF;}"
								+	".js-calendar-td-active{ background: #cbcaca;}"
								+   ".js-calendar-td-hover{ background: #cbcaca;}"
								+	".js-calendar-td-no{ font-weight: normal; color: #cbcaca;}"
								+	".js-calendar-ul-active{ background: #cbcaca;}"
								+	".js-calendar-ul-hover{ background: #cbcaca;}"
								+"<\/style>"
								+"<div class=\"js-calendar-dleft\">"
								+	"<div class=\"js-calendar-dleft-head\">月报表<\/div>"
								+	"<div class=\"js-calendar-dleft-con\">"
								+		"<p class=\"js-calendar-head-dleft\">"
								+			"<span class=\"js-calendar-up-year\">上一年<\/span>"
								+			"<span class=\"js-calendar-year\">" + _staticYear + "<\/span>"
								+			"<span class=\"js-calendar-down-year\">下一年<\/span>"
								+		"</p>"
								+		"<ul class=\"dclear\">"
								+			"<li>一月<\/li>"
								+			"<li>二月<\/li>"
								+			"<li>三月<\/li>"
								+			"<li>四月<\/li>"
								+			"<li>五月<\/li>"
								+			"<li>六月<\/li>"
								+			"<li>七月<\/li>"
								+			"<li>八月<\/li>"
								+			"<li>九月<\/li>"
								+			"<li>十月<\/li>"
								+			"<li>十一月<\/li>"
								+			"<li>十二月<\/li>"
								+		"<\/ul>"
								+	"<\/div>"
								+	"<div class=\"js-calendar-dleft-footer\"><\/div>"
								+"<\/div>"
								+"<div class=\"js-calendar-dright\">"
								+	"<div class=\"js-calendar-dright-head\">日报表<\/div>"
								+	"<div class=\"js-calendar-dright-con\">"
								+		"<p class=\"js-calendar-head-dright\">"
								+			"<span class=\"js-calendar-up-month\">上一月<\/span>"
								+			"<span class=\"js-calendar-month\">" + _staticYear + "<\/span>"
								+			"<span class=\"js-calendar-down-month\">下一月<\/span>"
								+		"<\/p>"
								+		"<div class=\"js-calendar-con-head\">"
								+			"<span>星期天<\/span>"
								+			"<span>星期一<\/span>"
								+			"<span>星期二<\/span>"
								+			"<span>星期三<\/span>"
								+			"<span>星期四<\/span>"
								+			"<span>星期五<\/span>"
								+			"<span>星期六<\/span>"
								+		"<\/div>"
								+		"<div class=\"js-calendar-con-list\">"
								+		"<\/div>"
								+	"<\/div>"
								+	"<div class=\"js-calendar-dright-footer\"><\/div>"
								+"<\/div>";
			
			$(this).append(_defaultHtml);	

			var _Date  		= new Date();
			var _Date2 		= new Date();
			var _year 		= _Date.getFullYear();
			var _month 		= _Date.getMonth();
			var _month2 	= _Date.getMonth();
			var _today 		= _Date.getDate();
			var _this  		= $(this).find(".js-calendar-con-list");
			
			function _getSetYear(_year){
				 _Date = new Date().setFullYear(_year);
			}

			function _getDates(_year,_month){     		//当前月的天数
				var _mMdate = new Date();
				_mMdate.setFullYear(_year);
				_mMdate.setMonth(_month+1);
				_mMdate.setDate(0);
				return _mMdate.getDate();
			}


			function _getWeekDay(_year,_month){   		//当前月周一是哪一天
				var _mWeekMonth = _Date2;
				var _mWeekMonth2 = new Date(_year,_month,1);
				//console.log(_mWeekMonth.getFullYear());
				return _mWeekMonth2.getDay();
			}


			function _downMonth(){       	//下一月
				if(_month >= 11){
					alert("这是本年最后一个月");
				}else{
					if(_month >= new Date().getMonth() && _year >= new Date().getFullYear()){
						//alert("本月之后的报表未生成");
					}else{
						_month ++;
						_getSetYear(_year)
						_this.empty();
						_initHtml();
					}
				}
				
			}
			

			function _upMonth(mnth){        	//上一月
				if(_month <= 0){
					alert("这是本年第一个月");
				}else{
					_month --;
					_getSetYear(_year)
					_this.empty();
					_initHtml();
				}
				
			}



			function _downYear(){			//下一年
				if(_year >= new Date().getFullYear()){
					alert("下年度报表未生成");
				}else{
					_year ++;
					_getSetYear(_year);
					_this.empty();
					_month = 0;
					$(".js-calendar-year").html(_year);
					_initHtml();
				}
				

			}

			function _upYear(){				//上一年
				 _year --;
				 _getSetYear(_year);
				 _this.empty();
				 _month = 0;
				 //console.log(0);
				 $(".js-calendar-year").html(_year);
				 _initHtml();
			}



			function _clickYearMonth(_index){		//根据左边月份选择

				_getSetYear(_year);
				_month = _index;
				_this.empty();
				_initHtml();
				if(_month < 9){
					_monthb = "0"+(_month+1);
				}else{
					_monthb = (_month+1);
				}
				_url = options.monthreport + _year + "-" + _monthb;

				window.open(_url,"_blank");
			}

			

			//--------------------生成日历---------------------------------

			function _initHtml(){

				var _liHtml = "";

				ws = _getWeekDay(_year,_month); 

				s = _getDates(_year,_month);
		
				
				//console.log(">>>>>>ws<<<<<<<" + ws);
				//console.log(">>>>>>s<<<<<<<" + s);

				for(var i = 0; i < ws; i++){
					_liHtml += "<div class=\"js-calendar-td\"></div>";
				}
				
				//$(".js-calendar-dleft-con ul li").eq(_month).addClass("js-calendar-ul-active").siblings().removeClass("js-calendar-ul-active");

				if(_year == new Date().getFullYear() && _month == _month2){
					$(".js-calendar-dleft-con").find("li").removeClass("js-calendar-ul-active");
				}else{
					$(".js-calendar-dleft-con ul li").eq(_month).addClass("js-calendar-ul-active").siblings().removeClass("js-calendar-ul-active");
				}

				for(var i = 1; i < s+1; i++){
					if(i < 10){
						_mday = "0"+i;
					}else{
						_mday = i;
					}
					
					var _monthTmp = parseInt(_month+1);       //设置左边日历格式
					if(_monthTmp < 10){
						_montha = "0" + _monthTmp;
					}else{
						_montha = _monthTmp;
					}
					
					if(i > (_today-1) && _month == new Date().getMonth() && _year == new Date().getFullYear()){
						_liHtml += "<div class=\"js-calendar-td js-calendar-td-no\">"+ i +"</div>";
					}else if(i == (_today-1) && _month == new Date().getMonth() && _year == new Date().getFullYear()){
						_liHtml += "<div class=\"js-calendar-td\"><a href="+ options.dayreport + _year + "-" + _montha + "-" + _mday  + " target=\"_blank\">"+ i +"</a></div>";
					}else{
						_liHtml += "<div class=\"js-calendar-td\"><a href="+ options.dayreport + _year + "-" + _montha + "-" + _mday + " target=\"_blank\">"+ i +"</a></div>";
					}

				}

				

			
				_this.append(_liHtml);

				var _jsCalendarTdLength = _this.find(".js-calendar-td").length;

				//console.log(_jsCalendarTdLength);
				var _mLiHtml = "";
			 	if(_jsCalendarTdLength < 42){
			 		var _mTotal = 42 - _jsCalendarTdLength;
			 		for(var i = 0; i< _mTotal; i++){
			 			_mLiHtml += "<div class=\"js-calendar-td\"></div>";
			 		}
			 	}

			 	_this.append(_mLiHtml);



			 	

			 	$(".js-calendar-month").html(_year+"年"+(_month+1)+"月");



			 	$(".js-calendar-td").each(function(index){
			 		if(( index+1) % 7 === 0){
			 			$(this).css({
			 				"borderRight":"none",
			 				"width":"90px"
			 			})
			 		}
			 	});


			 	//日期大于当前 则不生成报表

			 	if(_year >= new Date().getFullYear()){
			 		
					//console.log(">>>>>>>>");

			 		var _mTmpIndex;
			 		if(parseInt(_month2-1) >= 0){
			 			_mTmpIndex = parseInt(_month2-1) + 1;
			 		}else{
			 			_mTmpIndex = 0;
			 		}
			 		//console.log("......" + _mTmpIndex);
			 		if( 0 == _mTmpIndex){
			 			//console.log("|||||||>>>>>>>>");
			 			$(".js-calendar-dleft-con ul li").eq(_mTmpIndex).css({
					 			"color":"#cbcaca"
					 	}).addClass("no-click-month").css({"cursor":"text"});

					 	$(".js-calendar-dleft-con ul li:gt("+(_mTmpIndex)+")").css({
					 			"color":"#cbcaca"
					 	}).addClass("no-click-month").css({"cursor":"text"});
			 		
			 		}else{
			 			//console.log("////>>>>>>>>");
			 			$(".js-calendar-dleft-con ul li:gt("+(_mTmpIndex)+")").css({
					 			"color":"#cbcaca"
					 	}).addClass("no-click-month").css({"cursor":"text"});
			 		}
			 		

			 	}else{

			 		/*if(_mTmpIndex == 0){

			 			$(".js-calendar-dleft-con ul li").eq(0).css({
				 			"color":"#000"
				 		}).removeClass("no-click-month").css({"cursor":"pointer"});

			 			$(".js-calendar-dleft-con ul li:gt("+(_mTmpIndex)+")").css({
				 			"color":"#000"
				 		}).removeClass("no-click-month").css({"cursor":"pointer"});
			 		}else{

			 			$(".js-calendar-dleft-con ul li:gt("+(_mTmpIndex)+")").css({
				 			"color":"#000"
				 		}).removeClass("no-click-month").css({"cursor":"pointer"});
			 		}*/
			 		
			 		$(".js-calendar-dleft-con ul li").css({
				 			"color":"#000"
				 	}).removeClass("no-click-month").css({"cursor":"pointer"});
			 		
			 		//console.log("<<<<<<<<<<<");
			 	}

				
				
			}

			$(this).css({
				overflow:"hidden",
				zoom:"1"
				
			});

			//--------------------------上一月，下一月，上一年，下一年 操作

			$(".js-calendar-down-month").on("click",function(){
					_downMonth();
			});

			$(".js-calendar-up-month").on("click",function(){
					_upMonth();
			});


			$(".js-calendar-down-year").on("click",function(){
				_downYear();
			});

			$(".js-calendar-up-year").on("click",function(){
				_upYear();
			});


			/*

			_this.on("click",".js-calendar-down-month",function(){
					_downMonth();
			});

			_this.on("click",".js-calendar-up-month",function(){
					_upMonth();
			});


			_this.on("click",".js-calendar-down-year",function(){
				_downYear();
			});

			_this.on("click",".js-calendar-up-year",function(){
				_upYear();
			});
			
			*/

			
			//--------------------------- 左侧年份操作--------------------------------

			$(".js-calendar-dleft-con ul li").on({
				mouseenter:function(){
					if(false == $(this).hasClass("no-click-month")){
						$(this).addClass("js-calendar-ul-hover");
					}
				},
				mouseleave:function(){
					if(false == $(this).hasClass("no-click-month")){
						$(this).removeClass("js-calendar-ul-hover");
					}
				},
				click:function(){
					if($(this).hasClass("no-click-month")){
						//alert("当前月份报表未生成");
						return false;
					}else{
						var _monthIndex = $(this).index();
						_clickYearMonth(_monthIndex);
					}
					
				}
			});
			


			//--------------------------右侧 日历表格 操作-------------------------------

			_this.on("mouseenter",".js-calendar-td",function(){
			
				if($(this).hasClass("js-calendar-td-no") || $(this).find("a").length <= 0){
					return false;
				}else{
					$(this).addClass("js-calendar-td-hover");
				}
			});

			_this.on("mouseleave",".js-calendar-td",function(){
				if($(this).hasClass("js-calendar-td-no") || $(this).find("a").length <= 0){
					return false;
				}else{
					$(this).removeClass("js-calendar-td-hover");
				}
			});


			_initHtml();
			
	}

	
})(jQuery);