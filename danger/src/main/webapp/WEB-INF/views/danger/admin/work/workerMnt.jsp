<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<link href="<c:url value="/resources/css"/>/jquery-ui.css" rel="stylesheet">
<script src="<c:url value="/resources/js"/>/jquery-ui.js" type="text/javascript"></script>

<script src="<c:url value="/resources/js"/>/d3/d3.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/transformation-matrix-js/matrix.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/mathjs/math.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_core.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_extend.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_properties.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_for_ngms.js" type="text/javascript"></script>

<script type="text/javascript">
	var orderId = 'starting_date';
	var orderBy = 'desc';
	var listCnt = 15;
	var curPage = 1;
	$(document).ready(function() {
		$('#searchBtn').click(function() {
			page_list(1);
		});
		
		$('.close-btn').click(function(){
			$(this).data('key','');
			$(this).closest(".pop-wrap").hide();
		});
		
		$(".searchClass").keydown(function (event) {
            if (event.which === 13) {    //enter
            	initSearch();
            }
        });

		$('.numbersOnly').keyup(function () { 
			if (this.value != this.value.replace(/[^0-9\.]/g, '')) {
				this.value = this.value.replace(/[^0-9\.]/g, '');
			}
		});		
       
		$('#delete_workBtn').click(function(){
			workDeleteInit();
		});
		
		$('#modify_workBtn').click(function(){
			updateWork();
		});
		$('#excel_downloadBtn').click(function() {
			excelDownLoad();
		});
		$('#modify_completedWorkBtn').click(function(){
			modifyCompletedWork();
		});
		$('#delete_completedWorkBtn').click(function(){
			deleteCompletedWork();
		});
		
		init();
	});//jquery 끝
	
	//초기화
	function init() {
		initSearch();
	}
	
	function setDatepicker(datepicker1, datepicker2){
		/* */
		$("#"+datepicker1).datepicker({
		    changeMonth: true, 
		    changeYear: true,
		    dateFormat: "yy-mm-dd",
		    showMonthAfterYear:true,
		    monthNames: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		    monthNamesShort: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		    dayNames: ["일", "월", "화", "수", "목", "금", "토"], // For formatting
		    dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"], // For formatting
		    dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
		    beforeShowDay: noBefore
		   });
		
		$("#"+datepicker2).datepicker({
		    changeMonth: true, 
		    changeYear: true,
		    dateFormat: "yy-mm-dd",
		    showMonthAfterYear:true,
		    monthNames: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		    monthNamesShort: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		    dayNames: ["일", "월", "화", "수", "목", "금", "토"], // For formatting
		    dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"], // For formatting
		    dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
		    beforeShowDay: function noStartingDayBefore(date){
				var tdate = new Date(Date.parse($("#"+datepicker1).val())-1*1000*60*60*24);
				if (date < tdate)
					return [false]; 
				return [true];
			}
		   });
	}
	
	function changeStartingDateTime(datepicker1, datepicker2, time1, time2, min1, min2){
		var startingDate =$("#"+datepicker1).val()+$("#"+time1).val()+$("#"+min1).val();
		var completeDate =$("#"+datepicker2).val()+$("#"+time2).val()+$("#"+min2).val();
		if(startingDate > completeDate){
			$("#"+datepicker2).val($("#"+datepicker1).val());
			$("#"+time2).val($("#"+time1).val());
			$("#"+min2).val($("#"+min1).val())
		}
	}
	
	function setTimeSelectbox(timeId, minId, unit){
		var optionHtml  ="";
		for(i=0; i<24; i++){
			if(i<10){
				optionHtml += '<option value="0'+i+'">0'+i+'</option>';
			}else{
				optionHtml += '<option value="'+i+'">'+i+'</option>';
			}
		}
		$("#"+timeId).html(optionHtml);
		
		optionHtml ="";
		for(i=0; i<60; i++){
			if(i%unit==0){
				if(i<10){
					optionHtml += '<option value="0'+i+'">0'+i+'</option>';
				}else{
					optionHtml += '<option value="'+i+'">'+i+'</option>';
				}
			}
		}
		$("#"+minId).html(optionHtml);
	}
	
	function workRegistorPopup(){
		var date = new Date();
		var tdate = new Date(Date.parse(date)+1*1000*60*60*24);
		var today = date.format('yyyy-MM-dd');
		var tomorrow = tdate.format('yyyy-MM-dd');
		$('.work_registorPop').show();
		setTimeSelectbox("reg_starting_time","reg_starting_min", 5);
		setTimeSelectbox("reg_complete_time","reg_complete_min", 5);
		resetSearchData($('.work_registorPop input'));
		setDatepicker("reg_datepicker1","reg_datepicker2");
		$(".last_modi_time").text("");
		getFactoryList('select_factory_list');
		getWorkTypeCheckBoxList('select_work_type');
		
		$("#reg_datepicker1").val(today);
		$("#reg_datepicker2").val(tomorrow);
		$("#reg_starting_time").val("09");
		$("#reg_starting_min").val("00");
		$("#reg_complete_time").val("09");
		$("#reg_complete_min").val("00");
		
		getNewWorkNo();
	}
	
	function workModifyPopup(workUid){
		resetSearchData($('.work_registorPop input'));	
		var url 		= wwmsUrl + '/worker.do?work_uid='+workUid; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap 	= null;			
		doRestFulApi(url, headerMap, method, paramMap, function(result){
			var dataObject = dateSetting(result.workerMntList[0], "reg");
			
			formSetting(dataObject, 'workRegist_popup_form');
			$(".last_modi_time").text("(최종변경시간 : " + result.workerMntList[0].upd_date + ")");
			if(result.workerMntList[0].factory_uid!=null && result.workerMntList[0].factory_uid!='')
				getFactoryList('select_factory_list', result.workerMntList[0].factory_uid);
			else
				getFactoryList('select_factory_list', result.workerMntList[0].zone_uid);
			if(result.workerMntList[0].work_type!=null){
				var wortList = result.workerMntList[0].work_type.split(",");
				getWorkTypeCheckBoxList('select_work_type', wortList, "workRegist_popup_form");
			}
		}) ;
	}
	
	function dateSetting(object, type){
		var returnObject = object;
		var startingDate = object.starting_date;
		var completeDate = object.complete_date;
		
		if(startingDate!=null){
			var temp1 = startingDate.split(" ");
			returnObject["starting_date"] = temp1[0];
			var temp3 = temp1[1].split(":");
			$("#"+type+"_starting_time").val(temp3[0]);
			$("#"+type+"_starting_min").val(temp3[1]);

		}else{
			$("#"+type+"_starting_time").val("00");
			$("#"+type+"_starting_min").val("00");
		}
		if(completeDate!=null){
			var temp2 = completeDate.split(" ");
			returnObject["complete_date"] = temp2[0];
			var temp4 = temp2[1].split(":");
			$("#"+type+"_complete_time").val(temp4[0]);
			$("#"+type+"_complete_min").val(temp4[1]);
		}else{
			$("#"+type+"_complete_time").val("00");
			$("#"+type+"_complete_min").val("00");
		}
		return returnObject;
	}
	
	function changWorkNo(){
		if($("#select_work_no").children('option:selected').attr("type")=="auto"){
			workRegistorPopup();
		}else{
			workModifyPopup($("#select_work_no").children('option:selected').val());
		}
	}
	
	function changeFactory(){
		if($("#select_work_no").children('option:selected').attr("type")=="auto"){
			getNewWorkNo();
		}
	}
	
	function getNewWorkNo(){
		var name = $("#select_factory_list").children('option:selected').text();
		var url =  wwmsUrl + '/newWorkNo.do?name='+encodeURIComponent(name); ; 
		var method = 'GET';
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		doRestFulApi(url, headerMap, method, paramMap, 
			function (result) {
				getWorkNoList(result.workCud.work_no);
			}) ;
	}
	
	function getWorkNoList(work_no){
		var url =  wwmsUrl + '/code_workno.do' ; 
		var method = 'GET';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		
		doRestFulApi(url, headerMap, method, paramMap, 
			function (result) {
				if (!atbsvg.util.isNull(result) && !atbsvg.util.isNull(result.codeWorkNoList)) {				
					var $select = $("#select_work_no");
					var codeWorkNoList = result.codeWorkNoList;
					var html = '<option value="'+work_no+'" checked type="auto">'+work_no+'</option>';		
					
					if (!atbsvg.util.isNull(codeWorkNoList) && codeWorkNoList.length > 0) {
						var size = codeWorkNoList.length;
						for(var index = 0; index < size; index++) {
							var item = codeWorkNoList[index];							
							html += '<option value="'+item.work_uid+'" type="select">'+item.work_no+'</option>';
						}
					}
					
					$select.html(html);
				}
			}) ;
	}
	
	function getWorkTypeCheckBoxList(selectId, settingValList, formId){
		var url =  wwmsUrl + '/code_worktype.do' ; 
		var method = 'GET';
		var headerMap = {};
		var paramMap = null;
		
		doRestFulApi(url, headerMap, method, paramMap, 
			function (result) {
				if (!atbsvg.util.isNull(result) && !atbsvg.util.isNull(result.codeWorkTypeList)) {				
					var $select = $("#"+selectId);
					var workTypeList = result.codeWorkTypeList;
					var html = '';			
					
					if (!atbsvg.util.isNull(workTypeList) && workTypeList.length > 0) {
						var size = workTypeList.length;
						for(var index = 0; index < size; index++) {
							var item = workTypeList[index];
							html += '<li style="list-style:none; padding: 3px 0 3px 0; float:left; width:33%; box-sizing:border-box;"><input type="checkbox" name="work_type_check" value="'+item.code+'"/>'+item.name+'</li>';
						}
					}
					
					$select.html(html);
					
					if(settingValList){
						for(i=0; i<settingValList.length; i++){
							$("input:checkbox[name=work_type_check]", $("#"+formId)).filter("input[value="+settingValList[i]+"]").attr("checked",true);
						}
						
					}
				}
			}) ;
	}
	
	function getFactoryList(selectId, settingVal) {
		var url =  wwmsUrl + '/code_factoryzone/all.do' ; 
		var method = 'GET';
		var headerMap = {};
		var paramMap = null;

		doRestFulApi(url, headerMap, method, paramMap, 
			function (result) {
    			if (!atbsvg.util.isNull(result) && !atbsvg.util.isNull(result.codeFactoryZoneList)) {		
    				var $select = $("#"+selectId);
    				$select.empty();
					var factoryZoneList = result.codeFactoryZoneList;
					var html = '<option value="" checked>단위 공장 선택</option>';			
					
					if (!atbsvg.util.isNull(factoryZoneList) && factoryZoneList.length > 0) {
						var size = factoryZoneList.length;
						for(var index = 0; index < size; index++) {
							var item = factoryZoneList[index];
							html += '<option type="'+item.type+'" value="'+item.uid+'">'+item.name+'</option>';
						}
					}
					$select.html(html);
					
					if(settingVal){
						$("#"+selectId).val(settingVal);
					}
    			}
			}) ;
	}
	
	function initSearch(pageObj) {
		var paramObj={};
		
		if(pageObj){
			paramObj = pageObj;
		}
		
		paramObj["worker_name"] = $('#worker_name').val().trim();
		paramObj["device_no"] = $('#device_no').val().trim();
		paramObj["parter_name"] = $('#search_parter_name').val().trim();
		paramObj["name"] = $('#search_name').val().trim();
		paramObj["work_no"] = $('#search_work_no').val().trim();
		paramObj['limit_count'] = listCnt;
		paramObj['order_type'] = orderId;
		paramObj['order_desc_asc'] = orderBy;
		
		if(!check_searchword(paramObj.worker_name)) {
			alertMsg('작업자 입력은 기호,특수문자는<br>입력할 수 없습니다.');
			return false;
		}
		search(paramObj);
	}
	
	//검색메소드
	function search(paramObj) {
		var param 		= getJsonObjToGetParam(paramObj);
		var url 		= wwmsUrl + '/worker.do?'+param ; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType); // new requestHeaderObject("1234test","2", {추가적인 파람});
		var paramMap 	= null;		
		
		$('#workerMnt_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable) ;
	}
	
	function bindTable(result) {
		var totalCnt = 0;
		var list = $('#workerMnt_tbody').appendTable({
				 data		:	result.workerMntList
				,totalCnt 	:	result.totalcnt
				,curPage : curPage
				,listCnt : listCnt
				,column		:	['totalIndex', 'worker_name', 'device_no', 'parter_name' , 'factory_name', 'zone_name'
				       		 	 , 'last_factory_name', 'last_zone_name', 'work_type_img', 'work_no', 'name','starting_date', 'complete_date'
				       		 	 , 'worker_count'
				       		 	 ]
				,link 		:  	{
					 2:{'class' : 'info-pop-btn hoverred', 'key' : 'work_uid'} // index를 지정함
				}
				,defaultVal : 	{1 : '-', 2 : '-', 3 : '-', 4 : '-', 5 : '위치미확인', 6 : '위치미확인', 7 : '위치미확인', 8 : '-', 10 : '-', 11 : '-', 12 : '-', 13 : '-'}
		});	
		list.event('modify', function(){infoWorkPopup()});
		var page_ret = list.paging('#page_navi',{'totalCnt':$.fn.appendTable.defaults['totalCnt'], 'serverSideEvent' : 'page_list', 'curPage':curPage, 'lstCnt' : listCnt});
		filterColum();
	}
	
	function filterColum() {
		$("[name=complete_date_name]").each( function () {
			if($(this).data("check")==1){
				$(this).css("font-weight","Bold");
			}
		});		
		$("[name=starting_date_name]").each( function () {
			if($(this).data("check")!=""){
				$(this).css("font-weight","Bold");
			}
		});
	}
	
	function infoWorkPopup() {
		$('#workerMnt_tbody tr').click(function() {
			
			// case : alive_reporting, is_completed === 2
			var workUid = $(this).find('.info-pop-btn').data("key");
			if( workUid === null ) {
				return false;
			}
			
			// case 0, 1
			var url 		= wwmsUrl + '/worker.do?work_uid='+workUid; 
			var method 		= methodType.GET;
			var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
			var paramMap 	= null;			
			doRestFulApi(url, headerMap, method, paramMap, function(result) {				
				if( result.workerMntList.length > 0 ) {
					var device_no = result.workerMntList[0].device_no;
					if( result.workerMntList[0].is_completed === 1 ) {
						$('.work_modifyPop').show();
						setTimeSelectbox("mod_starting_time","mod_starting_min", 1);
						setTimeSelectbox("mod_complete_time","mod_complete_min", 1);
						setDatepicker("mod_datepicker1","mod_datepicker2");
						var dataObject = dateSetting(result.workerMntList[0], "mod");
						formSetting(dataObject, 'modify_popup_form');
						document.getElementById("mod_work_no").setAttribute('data-key', workUid);
						$(".last_modi_time").text(" 최종변경시간 : " + result.workerMntList[0].upd_date + ")");
						
						$('#mod_datepicker1').val(result.workerMntList[0].info_starting_date.substring(0,10));
						$('#mod_datepicker2').val(result.workerMntList[0].info_complete_date.substring(0,10));
						
						if(result.workerMntList[0].work_type){
							var wortList = result.workerMntList[0].work_type.split(",");
							getWorkTypeCheckBoxList('mod_select_work_type', wortList, "modify_popup_form");
						}
						
// 						var parter_code;
// 						if(result.workerMntList[0].parter_name != null && result.workerMntList[0].parter_code == "") {
// 							parter_code = "insert";
// 							$('#modi_parter_name').show();
// 	 						$('#modi_parter_name').val(result.workerMntList[0].parter_name);
// 						} else {
// 							parter_code = result.workerMntList[0].parter_code;
// 							$('#modi_parter_name').hide();
// 						}
// 						getParterNameList('modi_parter_list', parter_code);
					} else if( result.workerMntList[0].is_completed === 0 ) {
						$('.work_infoPop').show();
						$(".last_modi_time").text("(최종변경시간 : "+result.workerMntList[0].upd_date+")");
						var dataObject = dateSetting(result.workerMntList[0], "mod");
						formSetting(dataObject, 'info_popup_form');
					}
				}
			});
		});
	}
	
	//server side page
	function page_list(page){
		var paramObj = {}
		curPage = page;
		paramObj['limit_offset'] = ( page -1 ) * listCnt;
		
		initSearch(paramObj);
	}
	
	function setDatePicker(id) {
		$('#'+id).datepicker('show');
	}
	
	function orderbyEvent(orderName, img) {
		var $img = $(img).children("a").children("img");
		orderId = orderName;
		
		$src = $img.prop('src');
		if($src.indexOf('_asc.png') > 0 ){
			orderBy="desc";
			$img.prop("src", $src.replace("_asc.png","_desc.png"));
		}
		else{
			orderBy="asc";
			$img.prop("src", $src.replace("_desc.png","_asc.png"));
		}
		
		initSearch();
	}
	
	function workRegistInit() {
		var data = getFormData($('#workRegist_popup_form'));
		var startingDay = data.starting_date+data.reg_starting_time+data.reg_starting_min;
		var completeDay = data.complete_date+data.reg_complete_time+data.reg_complete_min;
		if($("#select_factory_list option:selected").val()==""){
			alertMsg('단위 공장을 선택하세요.');
			return;
		}
		if(!check_searchword(data.name)) {
			alertMsg('작업자 입력은<br>공백, 기호, 특수문자는<br>입력할 수 없습니다.');
			return false;
		}
		if($("#worker_count").val()==""){
			alertMsg('작업자수를 입력하세요.');
			return;
		}
		if($("#worker_count").val()==0){
			alertMsg('작업자수는 1명 이상 입력해야 합니다.');
			return;
		}
		if(startingDay> completeDay){
			alertMsg('작업종료시간이 작업시작시간보다 빠를수 없습니다.');
			return;
		}
		if($("#select_work_no").children('option:selected').attr("type")=="auto"){
			registWork();
		}else{
			updateWork();
		}
	}
	
	function workDeleteInit() {
		if($("#select_work_no").children('option:selected').attr("type")=="auto"){
			$(".work_registorPop").hide();
		}else{
			deleteWork();
		}
	}
	
	function registWork(){
		var type_list = [];
		var data = getFormData($('#workRegist_popup_form'));
		var type = $("#select_factory_list option:selected").attr('type');
		
		if(data.work_type_check){
			type_list = data.work_type_check.split(",");
		}
		
		data["zone_type"] =type;
		data["type_list"] =type_list;
		data["starting_date"] = data.starting_date+" "+data.reg_starting_time+":"+data.reg_starting_min;
		data["complete_date"] = data.complete_date+" "+data.reg_complete_time+":"+data.reg_complete_min;
		var url = wwmsUrl + '/workInsert.do' ; 
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alertMsg('작업이 등록되었습니다.');			
				$(".work_registorPop").hide();
				initSearch();				
			}			
		}) ;	
	}
	
	function deleteWork(){
		var data ={};
		data["work_uid"] =$("#select_work_no").children('option:selected').val();
		var url = wwmsUrl + '/workDelete.do' ; 
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){		
			if(result.msg==='SUCCESS') {
				alertMsg('작업이 삭제 되었습니다.');			
				$(".work_registorPop").hide();
				initSearch();
			}			
		}) ;	
	}
	
	function updateWork() {
		var type_list = [];
		var data = getFormData($('#workRegist_popup_form'));
		var type = $("#select_factory_list option:selected").attr('type');
		
		if(data.work_type_check){
			type_list = data.work_type_check.split(",");
		}
		data["zone_type"] =type;
		data["type_list"] =type_list;
		data["work_uid"] =$("#select_work_no").children('option:selected').val();
		data["work_no"] =$("#select_work_no").children('option:selected').text();
		data["starting_date"] = data.starting_date+ " "+data.reg_starting_time+":"+data.reg_starting_min;
		data["complete_date"] = data.complete_date+ " "+data.reg_complete_time+":"+data.reg_complete_min;
		
		var url = wwmsUrl + '/workUpdate.do' ; 
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alertMsg('작업을 수정하였습니다.');			
				$(".work_registorPop").hide();
				initSearch();				
			}			
		}) ;	
	}
	
	function modifyCompletedWork() {
		var data = getFormData($('#modify_popup_form'));
		var startingDay = data.info_starting_date+data.mod_starting_time+data.mod_starting_min;
		var completeDay = data.info_complete_date+data.mod_complete_time+data.mod_complete_min;
		
		if($("#mod_worker_count").val()==""){
			alertMsg('작업자수를 입력하세요.');
			return;
		}
		
		if($("#mod_worker_count").val()==0){
			alertMsg('작업자수는 1명 이상 입력해야 합니다.');
			return;
		}

		if($("#mod_worker_count").val()>100){
			alertMsg('작업자수는 100명 이하로<br>입력해야 합니다.');
			return;
		}

		if(startingDay> completeDay){
			alertMsg('작업종료시간이 작업시작시간보다 빠를수 없습니다.');
			return;
		}

		var type_list = [];		
		var url = wwmsUrl + '/workerUpdate.do' ; 
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;	
		data["starting_date"] = data.info_starting_date + " " + data.mod_starting_time + ":" + data.mod_starting_min;
		data["complete_date"] = data.info_complete_date + " " + data.mod_complete_time + ":" + data.mod_complete_min;

		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('작업을 수정하였습니다.');
				$(".work_modifyPop").hide();
				initSearch();
			}
		});
	}

	function deleteCompletedWork() {
		var data = getFormData($('#modify_popup_form'));
		var url = wwmsUrl + '/complatedWorkerDelete.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('작업이 삭제 되었습니다.');
				$(".work_modifyPop").hide();
				initSearch();
			}
		});
	}

	function excelDownLoad() {
		var paramObj = {};

		paramObj["worker_name"] = $('#worker_name').val().trim();
		paramObj["device_no"] = $('#device_no').val().trim();
		paramObj["parter_name"] = $('#search_parter_name').val().trim();
		paramObj["name"] = $('#search_name').val().trim();
		paramObj["work_no"] = $('#search_work_no').val().trim();
		paramObj['target'] = 'worker_mnt';

		var param = getJsonObjToGetParam(paramObj);

		$(location).attr("href", '/danger/excel/workerMnt_excel.do?' + param);
	}

	function getParterNameList(selectId, value) {
		var url =  wwmsUrl + '/get_partername.do';
		var method = 'GET';
		var headerMap = {};
		var paramMap = null;
		doRestFulApi(
				url,
				headerMap,
				method,
				paramMap,
				function(result) {
					if (!atbsvg.util.isNull(result)
							&& !atbsvg.util.isNull(result.parterNameList)) {
						var $select = $("#" + selectId);
						var parterNameList = result.parterNameList;
						var html = '<option value="">부서/업체명 선택</option>';
						if (!atbsvg.util.isNull(parterNameList)
								&& parterNameList.length > 0) {
							var size = parterNameList.length;
							for (var index = 0; index < size; index++) {
								var item = parterNameList[index];
								if (item.parter_code == value) {
									html += '<option value="'+item.parter_code+'" type="select" selected>'
											+ item.parter_name + '</option>';
								} else {
									html += '<option value="'+item.parter_code+'" type="select">'
											+ item.parter_name + '</option>';
								}
							}
							if (value == "insert") {
								html += '<option value="insert" selected>직접입력</option>';
							} else {
								html += '<option value="insert">직접입력</option>';
							}
						}
						$select.html(html);
					}
				});
	}

	function changParter() {
		if ($("#select_parter_list").children('option:selected').attr("value") == "insert") {
			$('#input_parter_name').show();
		} else {
			$('#input_parter_name').hide();
		}
		if ($("#modi_parter_list").children('option:selected').attr("value") == "insert") {
			$('#modi_parter_name').show();
		} else {
			$('#modi_parter_name').hide();
		}
	}
</script>
<div class="tab-style">
	<ul class="tab-box member-tab clearfix">
	<c:choose>
	<c:when test="${sessionScope.account.is_admin == '0'}">
		<c:set var="isAdmin" value="admin"/>
		<li class="active">
			<a href="<spring:url value="/admin/worker.do"/>" class="tab-btn"><span class="">작업자 관리</span></a>
		</li>
		<li>
			<a href="<spring:url value="/admin/work.do"/>" class="tab-btn"><span class="">작업 관리</span></a>
		</li>
	</c:when>
	<c:when test="${sessionScope.account.is_admin == '1'}">
	<li class="active"><!-- sadmin -->
		<a href="<spring:url value="/sadmin/worker.do"/>" class="tab-btn"><span class="">작업자 관리</span></a>
	</li>
	<li>
		<a href="<spring:url value="/sadmin/work.do"/>" class="tab-btn"><span class="">작업 관리</span></a>
	</li>		
	</c:when>
	</c:choose>
	</ul>

	<div class="tab-wrap">
		<div class="tab-contents target">
			<!-- search-wrap -->
			<div class="search-wrap display-table">
				<div class="table-row">
					<div class="table-cell">
						<div class="form-wrap clearfix">
							<h5 class="blind_position">검색어를 입력해 주세요</h5>
							<form action="#" class="clearfix">
								<div class="form-group clearfix">
									<span class="search-text label">작업자</span>
									<div class="input-box field-box">
										<input class="form-input searchClass" style="width:68px;" type="text" name="" id="worker_name">
									</div>
									
									<span class="search-text label">폰번호</span>
									<div class="input-box mr5" style="width:98px;">
										<input class="form-input searchClass numbersOnly" type="text" name="" id="device_no">
									</div>
									
									<span class="search-text label">공장명</span>
									<div class="input-box field-box">
										<input class="form-input searchClass" style="width: 90px;" type = "text" name="" id = "search_parter_name">
									</div>

									<span class="search-text label">작업명</span>
									<div class="input-box field-box">
										<input class="form-input searchClass" style="width: 68px;" type="text" name="" id="search_name">
									</div>

									<span class="search-text label">작업번호</span>
									<div class="input-box field-box">
										<input class="form-input searchClass" style="width: 108px;" type="text" name="" id="search_work_no">
									</div>

									<button type="button" class="btn" id="searchBtn">검색</button> 
									<button type="button" class="btn" id="excel_downloadBtn">엑셀 저장</button> 
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<div class="search_tbl_gap" style="padding:20px 0 0;"></div>
			<div class="table-list">
				<table class="ntype_tbl work_tbl">
					<colgroup>
						<col style="width:50px;">
						
						<col style="width:80px;">
						<col style="width:80px;">
						<col style="width:80px;">
						
						<col style="width:80px;">
						<col style="width:80px;">
						
						<col style="width:80px;">
						<col style="width:80px;">
						
						<col style="width:80px;">
						<col style="width:80px;">
						<col style="width:80px;">
						<col style="width:80px;">
						<col style="width:80px;">
						<col style="width:80px;">
					</colgroup>
					<thead>
						<tr class="first_tr">
							<th rowspan="2">No</th>
							<th colspan="3">작업자정보</th>
							<th colspan="2">인가구역설정</th>
							<th colspan="2">최종위치</th>
							<th colspan="6">작업정보</th>
						</tr>
						<tr>
							<th onclick="javascript:orderbyEvent('worker_name', this)">작업자 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('device_no', this)">폰번호 <a href="#;" class="ord"><img  src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('parter_name', this)">업체명 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('factory_name', this)">공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('zone_name', this)">Zone <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('last_factory_name', this)">공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('last_zone_name', this)">Zone <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
			
							<th onclick="javascript:orderbyEvent('work_type_name', this)">작업유형 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('work_no', this)">작업번호 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('name', this)">작업명 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('starting_date', this)">작업시작 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('complete_date', this)">작업종료 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('worker_count', this)">작업자수 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						</tr>
					</thead>
					<tbody id="workerMnt_tbody"></tbody>
				</table>
				<div class="work_type">
					<img src="<c:url value="/resources/img"/>/img_work_type.gif" alt="">
				</div>
				<div class="pn-wrap pn-single" id="page_navi"></div>
			</div>
		</div>
	</div>
</div>

<div class="pop-wrap work_infoPop"> <!-- work_infoPop s -->
	<div class="pop-bg"></div>
	<div class="pop-up"> <!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">작업자 정보</h3>
			<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
		</div>
		<div class="pop-contents"> <!-- pop-contents -->

			<div class="form-wrap prev-box clearfix">
				<form id="info_popup_form" action="#" class="clearfix">
				<p class="last_modi_time"></p>
					<div class="group-box display-table">
						<div class="form-group table-row first-group clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업자명 <span></span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="worker_name" id="worker_name" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">부서/업체명 <span></span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="parter_name" id="parter_name" readonly/>
								</div>
							</div>
						</div>
						<!-- TODO 변경 사항 확정 후 수정 필요 -->
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업번호 </label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="work_no" id="work_no" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업명 </label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="name" id="info_name" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">단위공장 <span></span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="factory_name" id="factory_name" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업유형 </label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="work_type_name" id="work_type_name" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업시작일시</label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="info_starting_date" id="" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업자수 <span></span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="worker_count" id="" readonly/>
								</div>
							</div>
						</div>
					</div>
					<div class="form-actions text-center">
						<button type="button" class="btn close-btn">확인</button> 
					</div>
				</form>
			</div>

		</div> <!-- pop-contents e -->
	</div> <!-- pop-up e -->
</div> <!-- reset-pw-pop e -->

<div class="pop-wrap work_modifyPop"> <!-- work_modifyPop s -->
	<div class="pop-bg"></div>
	<div class="pop-up"> <!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">작업자 정보 수정</h3>
			<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
		</div>
		<div class="pop-contents"> <!-- pop-contents -->
			<div class="form-wrap prev-box clearfix">
				<form id="modify_popup_form" action="#" class="clearfix">
				<input type="hidden" name="mapping_uid">
				<p class="last_modi_time"></p>
					<div class="group-box display-table">
						<div class="form-group table-row first-group clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업자명 <span></span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="worker_name" id="worker_name" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">업체명 <span></span></label>
							</div>
							<div class="table-cell">
<!-- 								<div> -->
<!-- 									<select class="sel" id="modi_parter_list" name="modi_parter_name" onchange="changParter()" > -->
<!-- 										<option></option> -->
<!-- 									</select>  -->
<!-- 									<input class="form-input" type="text" name="modi_parter_name" id="modi_parter_name" maxlength="20" style="width: 30%;height: 26px;padding: 0 8px;border: 1px solid #acacac;font-size: 12px;color: #555;"/> -->
<!-- 								</div> -->
								<div class="input-box">
									<input class="form-input" type="text" name="parter_name" id="parter_name" readonly/>
								</div>	
							</div>
						</div>
						 <div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업번호</label>
							</div>
							<div class="table-cell">							 
								<div class="input-box">
									<input class="form-input" type="text" name="work_no" id="mod_work_no" readonly/>
								</div>							 
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업명</label>
							</div>
							<div class="table-cell"> 
								<div class="input-box">
									<input class="form-input" type="text" name="name" id="mod_name" readonly/>
								</div> 
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">단위공장 <span></span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="factory_name" id="factory_name" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업유형 </label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="work_type_name" id="work_type_name" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업시작일시</label>
							</div>
							<div class="table-cell">
								<div class="input-box calendar-box full">
									<input id="mod_datepicker1" class="form-input calendar_input" type="text" name="info_starting_date" onchange="changeStartingDateTime('mod_datepicker1','mod_datepicker2', 'mod_starting_time','mod_complete_time','mod_starting_min','mod_complete_min')" readonly/>
									<button type="button" class="calendar-btn blind calendar_button" onclick="setDatePicker('mod_datepicker1')" title="날짜를 선택해주세요.">날짜선택</button>
									<select class="sel2 calendar_sel1" id="mod_starting_time" name="mod_starting_time">
									</select> <span class="calendar_span">:</span>
									<select class="sel2 calendar_sel2" id="mod_starting_min" name="mod_starting_min">
									</select>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업종료일시</label>
							</div>
							<div class="table-cell">
								<div class="input-box calendar-box full">
									<input id="mod_datepicker2" class="form-input calendar_input" type="text" name="info_complete_date" readonly/>
									<button type="button" class="calendar-btn blind calendar_button" onclick="setDatePicker('mod_datepicker2')" title="날짜를 선택해주세요." >날짜선택</button>
									<select class="sel2 calendar_sel1" id="mod_complete_time" name="mod_complete_time">										
									</select> <span class="calendar_span">:</span>
									<select class="sel2 calendar_sel2" id="mod_complete_min" name="mod_complete_min">										
									</select>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업자수 <span></span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input numbersOnly" type="text" name="worker_count" id="mod_worker_count" maxlength="3"/>
								</div>
							</div>
						</div>
					</div>
					<div class="form-actions text-center">
						<button type="button" class="btn mr5 confirm-btn" id="delete_completedWorkBtn">삭제</button>
						<button type="button" class="btn" id="modify_completedWorkBtn">수정</button> 
					</div>
				</form>
			</div>

		</div> <!-- pop-contents e -->
	</div> <!-- pop-up e -->
</div> <!-- reset-pw-pop e -->