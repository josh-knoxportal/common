
/**
 * select option 넣기
 * @param paramMap
 * @param isAll
 * @param callback
 * @return
 */
function addOption(url, paramMap, isAll, callback) {
	//doRestFulApi(url, headerMap, method, paramMap, sucessCallback, errorCallback, option)
	//var 
	doRestFulApi(url, methodType.get, paramMap, function(result) {
		for(var responseName in result) {
			var resultMap = result[responseName];
			var object = $("#" + responseName);
			if(isAll) {
				object.html('<option value="" selected="selected">전체</option>');
			}
			else {
				object.html('<option value="" selected="selected">선택하세요</option>');
			}
			$.each(resultMap, function(i, obj) {
				$("#" + responseName).append('<option value="' + obj.value + '">' + obj.text + '</option>');
			});
		}
		if(callback != null) {
			callback.call();
		}
	},'','',null);
}

/**
 * select option 넣기
 * @param paramMap
 * @param msg
 * @param callback
 * @param selected
 * @return
 */
function addOptionMsg(url, paramMap,  msg,  callback, selected) {
	doRestFulApi(url, methodType.get, paramMap, function(result){
	//doAction(paramMap, function(result) {
		
		for(var responseName in result) {
			
			var resultMap = result[responseName];
			var object = $("#" + responseName);
			object.html('');
			if(msg == "" ){
				//object.html('');
			}else if(msg != null ){
				object.html('<option value="" selected="selected">'+msg+'</option>');
			}else{
				object.html('<option value="" selected="selected">선택하세요</option>');
			}
			var cnt = 0 ;
			$.each(resultMap, function(i, obj) {
				cnt++;
				$("#" + responseName).append('<option value="' + obj.value + '">' + obj.text + '</option>');
			});
			
			if(cnt ==0) object.html('<option value="" selected="selected">없음</option>');
	
			if(selected != null || selected != undefined){		
				$("#" + responseName).val(selected);
			}
		}
		
		if(callback != null) {
			callback.call();
		}
	},'','',null);
}



/**
 * 옵션 삭제
 * @param selectObj
 * @return
 */
function removeOptions(selectObj) {
	// option 만큼 반복하면서
	selectObj.find("option").each(function() {
		//optioin을 지운다.   
		$(this).remove();  
	});
}


/**
 * 검색 조건 파라미터 가져오기
 * @return
 */
function getSearchParam(objList, prefix) {	
	var data = {};
	var obja = $("input, select, checkbox", objList);
	$.each(obja, function(i, obj) {
		if(this.type == "checkbox") {
			if(prefix) {
				alert("checkbox prefix 구현해야함");
			}
			if($(this).is(":checked")) {
				data[this.name] = "Y";
			}
			else {
				data[this.name] = "N";
			}			 
		}
		// select option 일 경우
		else if(this.type == "select-one") {
			
			if(this.className == "keyword") {
				data[this.options[this.selectedIndex].value] = $("input.param[name='" + this.name + "']").val();
			}
			else {
				if(prefix) {
					if($(this).val() != null) {
						data[this.name.replace(prefix, "")] = this.options[this.selectedIndex].value;
					}
				}
				else {
					if($(this).val() != null) {
						data[this.name] = this.options[this.selectedIndex].value;
					}
				}
			}
		}
		// 일반 input type 일 경우
		else if(this.type == "text") {
			if(this.className != "param") {
				if(prefix) {
					data[this.name.replace(prefix, "")] = this.value;
				}
				else {
					data[this.name] = this.value;
				}
			}
		}
		else if(this.type == "radio") {
			if(prefix) {
				alert("radio prefix 구현해야함");
			}
			data[this.name] = $("input[name='" + this.name + "']:checked").val();
		}
	});
	return data;	
}

/**
 * Form 객체정보 담기
 * @return
 */
function getFormData(objList){
	var data = {};
	
	//data["ACTION"] = action;	
	var obja = $("input, textarea, select", objList);	
	$.each(obja, function(i, obj) {			
		if(this.type == "checkbox") {
			if($(this).is(":checked")) {
				if(data[this.name] !=null){
					data[this.name] = data[this.name] +','+$(this).val() ;
				}else{
					data[this.name] = $(this).val() ;
				}
			}
		}
		else if(this.type == "radio"){
			data[this.name] = $("input[name='" + this.name + "']:checked", $(objList)).val();
		}else if(this.type == "select-one"){
			data[this.name] = this.options[this.selectedIndex].value;
			
		}else if(this.type == "textarea"){			
			data[this.name] =$("textarea[name="+this.name+"]", $(objList)).val();			
		}else{
			data[this.name] = this.value;
		}
		
	});
	return data;
}

/**
 * 검색조건 리셋
 * @return
 */
function resetSearchData(objList) {
	$.each(objList, function(i, obj) {
		if(this.type == "select-one") {
			if(this.className == "keyword") {
				this.selectedIndex = 0;
				$("input.param[name='" + this.name + "']").val("");
			}
			else {
				this.selectedIndex = 0;
			}
		}
		else if(this.type == "text") {
			if(this.className != "param") {
				this.value = "";
			}
		}
	});
}

/**
 * form에 값 세팅
 * data = 해당object
 * form = form object
 * 
 **/
function formSetting(data, form){
		var obj = $("input, textarea, select", $("#"+form));
		var setting_val, fld_type, check_val, obj_name;

		$.each(obj, function(i, o){
			obj_name = o.name;
			obj_name = obj_name.replace("WE_","");      //webeditor
			setting_val = formSearchVal(data, obj_name);
			//console.log(setting_val);
			fld_type = o.type;
			if(fld_type=="checkbox"){
				//check_val = setting_val.split(",");
				if(setting_val != null && setting_val != undefined){
					check_val = setting_val;
					for(var j = 0 ; j < check_val.length ; j++){
						$("input:checkbox[name="+o.name+"]", $("#"+form)).filter("input[value="+check_val[j]+"]").attr("checked",true);
					}
				}
			}else if(fld_type=="radio"){
				$("input:radio[name="+o.name+"]", $("#"+form)).attr("checked",false);
				$("input:radio[name="+o.name+"]", $("#"+form)).filter("input[value="+setting_val+"]").attr("checked",true);
			}else if(fld_type=="select-one"){
				$(o).find("option").filter(function(){return $(this).val() == setting_val; }).attr("selected",true);
			}else{
				$(o).val(setting_val);
			}
		});
}
/**
 * json 과 form 필드명 비교 후 해당 값 반환
 * @data : json 데이터            
 * @name : 반환받을 이름 (form 필드명)  
 */
function formSearchVal(data, field_name){
	var fieldnm = field_name;
	for(i in data){
		if(i == field_name){
			var field_set = eval("data."+fieldnm);
			return field_set;
		}
	}
}

/**
 * json 값에 따른 table td id 값 
 * @data : json 오브젝트       
 * @form : 값을 셋팅할 테이블 오브젝트 
 */ 
function tblSetting(data, table){
	var obj = $("tbody td", $("#"+table));
	var tdId;
	var prefix = "td_", setting_val;
 
	$.each(obj, function(i, o){
		tdId = $(o).attr("id");
		if(tdId == null || tdId == undefined || tdId == ""){

		}else{
			setting_val = formSearchVal(data, tdId.replace(prefix,""));
			if(setting_val == null || setting_val == "null"){
				setting_val = "-";
			}
			if($("#" + tdId, $("#"+table)).length > 0){
				$("#" + tdId, $("#"+table)).html("<div>" + setting_val + "</div>");
			}
		}
	});
}




/**
 * 해당 폼 리셋              
 * @form : 초기화 시킬 폼 오브젝트  
 */
function formReset(form){
	var obj = $("input, textarea, select", $("#"+form));
	$.each(obj, function(i, o){
		fld_type = o.type;

		if(fld_type=="checkbox"){
			$("input:checkbox[name="+o.name+"]", $("#"+form)).attr("checked",false);
			//$("input:checkbox[name="+o.name+"]").eq(0).attr("checked",true);
		}else if(fld_type=="radio"){
			$("input:radio[name="+o.name+"]", $("#"+form)).attr("checked",false);
			$("input:radio[name="+o.name+"]", $("#"+form)).eq(0).attr("checked",true);
		}else if(fld_type=="select-one"){
			$(o).find("option").eq(0).attr("selected",true);
		}else{
			$(o).val("");
		}
	});
}

/**
 * 공통화된 다이얼로그 창 띄우기
 * @param id 다이얼로그로 띄울 div id
 * @param buttonAction 다이얼로그에서 적용할 버튼 액션 모음
 * @param width 다이얼로그 가로 길이
 * @param alertType 다이얼로그에 입력될 body
 * @param title 다이얼로그 title
 * @return
 */
function openDialog(id, buttonAction, width, title) {
	
}


function confirm_dialog(options){		
	if(confirm(options.txt)){
		options.confirmCallback(options);
	}	
}

function message_dialog(options){
	//레이어 메시지로 구성할 수 있다.
	alert(options.msg);
}



//doPopUp('','target_name',100,100,'scrollbars=yes,toolbar=yes,resizable=yes');
//scrollbars = yes  :: 스크롤바 사용 (미사용 no) 
//resizeable = yes :: 좌우스크롤바 사용 (미사용 no) 
//menubar = yes    :: 메뉴바 사용 (미사용 no) 
//toolbar = yes       :: 툴바사용 (미사용 no) 
//width = 100          :: 팝업창의 가로사이즈 
//height = 100         :: 팝업창의 세로사이즈 
//left = 10               :: 좌측에서 10픽셀을 띄운다. 
//top = 10               :: 상단에서 10픽셀을 띄운다. 

var win = null;

function doPopUp(url,target,w,h,features) {
  var winl = (screen.width-w)/2;
  var wint = (screen.height-h)/2;
  if (winl < 0) winl = 0;
  if (wint < 0) wint = 0;
  var settings = 'height=' + h + ',';
  settings += 'width=' + w + ',';
  settings += 'top=' + wint + ',';
  settings += 'left=' + winl + ',';
  settings += features;
  win = window.open(url,target,settings);
  win.window.focus();
}


function alertMsg(msg){
	//layer msg 추후 변경
	//alert(msg);
	$('#dialog_alert_msg').html(msg);
	$('#dialog_alert_modal').show();
}



function getHtml(url){
    
    var result = null;
    var tplURL = url ; 
    $.ajax({
       url: tplURL,
       type: 'get',
       dataType: 'text',
       async: false,
       success: function(data) {
    	   //console.log(data);
           result = data;
       } ,
		error: function(XMLHttpRequest, textStatus, errorThrown){	
//			console.log(XMLHttpRequest);				
		}
    });
    return result;	
}


function addOptionMonth(appendObjectId){
	var html =''; 
	$('#'+appendObjectId).html('');
	for(var i=1 ; i < 13 ; i++){
		html+='<option value="' + zeroFill(i,2) + '">' + i+ '</option>';
	}
	$('#'+appendObjectId).append(html);
	
}


function addOptionHp(appendObjectId){
	var html =''; 
	$('#'+appendObjectId).html('');
	$('#'+appendObjectId).append('<option value="010">010</option>');
	$('#'+appendObjectId).append('<option value="011">011</option>');
	$('#'+appendObjectId).append('<option value="016">016</option>');
	$('#'+appendObjectId).append('<option value="017">017</option>');
	$('#'+appendObjectId).append('<option value="018">018</option>');
	$('#'+appendObjectId).append('<option value="019">019</option>');
	
}


function addOptionTel(appendObjectId){
	var html =''; 
	$('#'+appendObjectId).html('');
	$('#'+appendObjectId).append('<option value="02">02</option>');
	$('#'+appendObjectId).append('<option value="051">051</option>');
	$('#'+appendObjectId).append('<option value="016">016</option>');
	$('#'+appendObjectId).append('<option value="017">017</option>');
	$('#'+appendObjectId).append('<option value="018">018</option>');
	$('#'+appendObjectId).append('<option value="019">019</option>');
	
}

function isBlank(str) {
	if (str == null || str == "") return true;
	else return false;
}

function alertEmpty(sel_id,alert_msg){
	var str = $('#'+sel_id).val();
	if(isBlank(str)){
		$('#'+sel_id).focus();
		alert(alert_msg +"는 필수항목입니다.");
		return true;
	}
	return false;
}

function alertDate(sel_id){
	var str = $('#'+sel_id).val();
	if(isDate(str)){
		$('#'+sel_id).focus();
		alert("잘못된 날짜 형삭입니다.");
		return true;
	}
	return false;
}

function onlyNumber(obj){
	$(obj).val($(obj).val().replace(/[^0-9]/g,""));
}

function bindCheckBoxAll(allCheckBoxId, checkboxName){
	$("#"+allCheckBoxId).on( "click", function() {
		$("input[name="+checkboxName+"]").not(this).prop('checked', this.checked);
	});
	
	$("input[name="+checkboxName+"]").on( "click", function() {
		//var index  = $("input[name="+checkboxName+"]").index($(this));
		var id = $(this).prop('id');
		if(id != allCheckBoxId){
			if(!$(this).is(":checked")){
				$("#"+allCheckBoxId).prop('checked', this.checked);
			}
			
		}
	});
}

// [2016/10/26] modified : 90-> 365*5 변경요청
function noBefore(date){
	var tdate = new Date(Date.parse(new Date())-365*5*1000*60*60*24);
	if (date <tdate)
		return [false]; 
	return [true];
}

function changeStartingDate(datepicker1, datepicker2){
	if($("#"+datepicker1).val() > $("#"+datepicker2).val()){
		$("#"+datepicker2).val($("#"+datepicker1).val());
	}
}


function beep() {

	var snd = new Audio("/resources/sound/beep.mp3");
	snd.play();
	
}
