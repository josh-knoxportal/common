var methodType   = {'GET' :'GET', 'POST' : 'POST', 'DELETE' : 'DELETE', 'PUT' :'PUT'};
var headerConst  = ['access_token','request_type'];
var ajaxCache = false;
//var headerParam  = {'access_token' : null, "request_type" : null};

var XSSfilter = function(content) {
	return content.replace(/</g, "&lt;").replace(/>/g, "&gt;");
    
};


var Searchfilter = function(content) {
	//content =  content.replace(/like/, "").replace(/%/g, "\%");
	var front_url = content.substr(0, content.indexOf("?")+1);
	var end_url  =  content.substr(content.indexOf("?")+1,content.length) ; 
	return front_url + end_url.replace(/like/, "\like\\").replace(/%/g, "\%\\");
};



var requestHeaderObject = function(access_token, request_type, opts){
	//opts 추가적인 param으로 사용한다. //join
	var access_token = access_token;
	var request_type = request_type;
	
	var ret = {access_token : access_token, request_type: request_type};
	if(opts != null)
		$.extend( ret, opts );
		
	return ret;
}


function getJsonObjToGetParam(jsonObj){
	var param = '';
	if(jsonObj == null) return param;
	
	$.each(jsonObj, function(key, value){
		if(key == null || value == "") return;
		param += '&'+key+'='+encodeURIComponent(value);
	});
	
	//console.log(param);
	return param;
}
/**
 * RestFul call 방식으로 통신
 * @param url 
 * @param paramMap
 * @param callback
 * @return
 */
function doRestFulApi(url, headerMap, method, paramMap, sucessCallback, errorCallback, option, disableLoading) {
	
	if(methodType.GET != method && disableLoading == null){
		loading_process();
	}
	
	var jsonParam ='';
	
	var cur_url = document.URL;
	headerMap['cur_url'] = cur_url;
	
	if(paramMap != null){
		jsonParam = XSSfilter(JSON.stringify(paramMap));
	}
	
	/*
	if(method  == methodType.GET){
		url = Searchfilter(url);
		console.log(url);
	}
	*/
	
	
	
	$.ajax({
		url: url,
		type: method,
		cache : ajaxCache, 
		data : jsonParam,
		contentType: 'application/json; charset=utf-8',
		dataType: 'json',
		//headers: {"access_token" : g_access_token, "request_type" : "2", "x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		headers: headerMap,
		//headers: {"access_token" : g_accessToken, "request_type" : g_requestType},
		success: function(result) {
			
//			console.log(result);
			if(methodType.GET != method && disableLoading == null){
				loading_process('hide');
			}
			
			if(typeof sucessCallback === "function")
				sucessCallback(result, option);
			else
				commonSuccess();
		
			
		},
        error : function(jqXHR, textStatus, errorThrown) { 
        	
        	
//        	console.log(errorThrown);

        	if(methodType.GET != method && disableLoading == null){
				loading_process('hide');
			}
        	
        	//console.log(errorThrown);
			if(errorCallback != null) 
				errorCallback.call(jqXHR, textStatus, errorThrown);
			else 
				commonError.call(jqXHR, textStatus, errorThrown);
		
		}
    });
}



/**
 * Ready Ajax Form
 * @param formName
 */
function doAjaxFormReady(formName){   
	var frm ;
	frm = $("#" + formName);
	frm.ajaxForm(); 
}


/**
 * Ready Ajax Form
 * @param formName
 */
function doAjaxSubmit(formName, paramObj,sucessCallback,option){
	var frm = $("#"+ formName);
	var options = {};
	options = {
		data : paramObj,
		success : 
		function(result){
			if(typeof sucessCallback === "function")
				sucessCallback(result, option);
			
		},
		clearForm : true,
		resetForm : true
	};
	frm.attr("action",paramObj.url);
	frm.ajaxSubmit(options);
	return false;
}




/**
 * 에러처리 공통화 만들기
 * @param jqXHR
 * @param textStatus
 * @param errorThrown
 * @return
 */
function commonError(jqXHR, textStatus, errorThrown) {
	// 
	//openDialog("errorDialog", null, 300);
	//$("#errorDialog").dialog("open");
	/*
	if(jqXHR == "error" &&  textStatus ==  ""  && errorThrown == undefined){
		//alertMsg(');
		var cur_url = top.location.href;
		//top.href.location = contextPath + '/vehicle/admin/login.do';
	}
	*/
	if(this != null){
		if(this.status == 401 || this.status == 403){
			alert('권한이 없습니다.');
			var cur_url = top.location.href; 
			top.location.href = contextPath + '/admin/login.do';
			
			return;
		}
		if(this.status == 503){
			top.location.href = contextPath + '/auth_503.do';
			return;
		}
	}
	
	alertMsg('오류가 발생하였습니다.');
}



/**
 * 성공처리 공통화 만들기
 * @return
 */
function commonSuccess() {
	alert('처리하였습니다.');
}





