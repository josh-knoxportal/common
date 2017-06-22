<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
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
									<span class="search-text">기간</span>
									<div class="input-box calendar-box" style="width:150px;">
										<input id="datepicker1" class="form-input" type="text" name="name" onchange="changeStartingDate('datepicker1','datepicker2')">
										<button type="button" class="calendar-btn blind" onclick="javascript:setDatePicker1()" title="날짜를 선택해주세요.">날짜선택</button>
									</div>
									<span class="dash"> - </span>
									<div class="input-box calendar-box" style="width:150px;">
										<input id="datepicker2" class="form-input" type="text" name="name">
										<button type="button" class="calendar-btn blind" onclick="javascript:setDatePicker2()" title="날짜를 선택해주세요.">날짜선택</button>
									</div>									
									<span class="search-text label">공장명</span>
									<div class="input-box field-box">
										<input class="form-input searchClass" style="width:150px;" type = "text" name="" id = "search_parter_name">
									</div>
									<span class="search-text label">작업명</span>
									<div class="input-box field-box">
										<input class="form-input searchClass" style="width:150px;" type="text" name="" id="search_name">
									</div>														
									<button id="searchBtn" type="button" class="btn">검색</button> 
									<button id="excel_downloadBtn" type="button" class="btn">엑셀 저장</button> 
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<!-- // search-wrap -->
			<div class="search_tbl_gap" style="padding:20px 0 0;"></div>
			<div class="table-list">
				<table class="ntype_tbl work_tbl">
					<colgroup>
						<col style="width: 30px;">
						<col style="width: 70px;">
						<col style="width: 70px;">
						<col style="width: 70px;">
						<col style="width: 70px;">
						<col style="width: 80px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
						<col style="width: 69px;">
					</colgroup>
					<thead>
						<tr class="first_tr">
							<th rowspan="2">No</th>
							<th rowspan="2" onclick="javascript:orderbyEvent('work_day', this)">관리일자 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th rowspan="2" onclick="javascript:orderbyEvent('work_no', this)">작업번호 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th rowspan="2" onclick="javascript:orderbyEvent('name', this)">작업명 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th rowspan="2" onclick="javascript:orderbyEvent('parter_name', this)">부서/<br>업체명 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th rowspan="2" onclick="javascript:orderbyEvent('work_type_name', this)">작업유형 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th colspan="5">작업계획정보</th>
							<th colspan="3">작업정보</th>
							<th colspan="2">인가구역설정</th>
						</tr>
						<tr>						
							<th onclick="javascript:orderbyEvent('starting_date', this)">작업시작 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('complete_date', this)">작업종료 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('worker_count', this)">작업자수 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('factory_name', this)">공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('zone_name', this)">Zone<a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('real_starting_date', this)">작업시작 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('real_ending_date', this)">작업종료 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('real_worker_count', this)">작업자수 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('worker_factory_name', this)">공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('worker_zone_name', this)">Zone <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						</tr>
					</thead>
					<tbody id="workerMnt_tbody"></tbody>
				</table>				
				<div class="work_type">
					<img src="<c:url value="/resources/img"/>/img_work_type.gif" alt="">
				</div>			
				<div class="form-actions"><button type="button" class="btn" id="work_registerBtn">작업 등록</button></div>
				<div class="pn-wrap pn-single" id="page_navi"></div>
			</div>
		</div>
	</div>
 <script type="text/javascript">
	var orderId = 'real_starting_date';
	var orderBy = 'desc';
	var listCnt = 15;
	var curPage = 1;

	$(document).ready(function() {
		
		$($("#gnb").children("li")[4]).addClass("target");
		
		var date = new Date();
		var today = date.format('yyyy-MM-dd');		
        $('#datepicker1').val(today);
        $('#datepicker2').val(today);
        setDatepicker("datepicker1", "datepicker2");
        
		$('#searchBtn').click(function() {
			page_list(1);
		});

		$('#work_registerBtn').click(function() {
			workRegistorPopup();
		});
		
		$('.close-btn').click(function(){
			$(this).data('key','');
			$(this).closest(".pop-wrap").hide();
		});
		
		$(".searchClass").keydown(function (event) {
            if (event.which === 13) { //enter
            	initSearch();
            }
        });

		$('.numbersOnly').keyup(function () { 
			if (this.value != this.value.replace(/[^0-9\.]/g, '')) {
				this.value = this.value.replace(/[^0-9\.]/g, '');
			}
		});

		// excel
		$('#excel_downloadBtn').click(function() {
			excelDownLoad();
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
	
	// 등록
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
		getParterNameList('select_parter_list','');
		getWorkTypeCheckBoxList('select_work_type');
		$('#input_parter_name').hide();
		
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
		var url 		= wwmsUrl + '/workDetail.do?work_uid='+workUid; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap 	= null;		
		doRestFulApi(url, headerMap, method, paramMap, function(result){
			var dataObject = dateSetting(result.workMntVo, "reg");
			formSetting(dataObject, 'workRegist_popup_form');
			// partner
			var parter_code;
			if(result.workMntVo.parter_name != null && result.workMntVo.parter_code == ""){
				parter_code = "insert";
				$('#select_parter_name').show();
					$('#select_parter_name').val(result.workMntVo.parter_name);
			} else {
				parter_code = result.workMntVo.parter_code;
				$('#select_parter_name').hide();
			}
			getParterNameList('select_parter_list',parter_code);
			$(".last_modi_time").text("(최종변경시간 : "+result.workMntVo.upd_date+")");
			if(result.workMntVo.factory_uid!=null && result.workMntVo.factory_uid!='')
				getFactoryList('select_factory_list', result.workMntVo.factory_uid);
			else
				getFactoryList('select_factory_list', result.workMntVo.zone_uid);
			if(result.workMntVo.work_type!=null){
				var wortList = result.workMntVo.work_type.split(",");
				getWorkTypeCheckBoxList('select_work_type', wortList, "workRegist_popup_form");
			}
		}) ;
	}
	
	function dateSetting(object, type){
		var returnObject = object;
		var startingDate = object.starting_date;
		var completeDate = object.complete_date;
		
		if(startingDate!=null && startingDate!=''){
			var temp1 = startingDate.split(" ");
			returnObject["starting_date"] = temp1[0];
			var temp3 = temp1[1].split(":");
			$("#"+type+"_starting_time").val(temp3[0]);
			$("#"+type+"_starting_min").val(temp3[1]);

		}else{
			$("#"+type+"_starting_time").val("00");
			$("#"+type+"_starting_min").val("00");
		}
		if(completeDate!=null && completeDate!=''){
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
	
	function changParter(){
		if($("#select_parter_list").children('option:selected').attr("value")=="insert"){
			$('#input_parter_name').show();
		}else{
			$('#input_parter_name').hide();
		}
		if($("#modi_parter_list").children('option:selected').attr("value")=="insert"){
			$('#modi_parter_name').show();
		}else{
			$('#modi_parter_name').hide();
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
		var url =  wwmsUrl + '/code_workno_before.do' ; 
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
	
	function getParterNameList(selectId,value){
		var url =  wwmsUrl + '/get_partername.do'; 
		var method = 'GET';
		var headerMap = {};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, paramMap, 
			function (result) {
		
				if (!atbsvg.util.isNull(result) && !atbsvg.util.isNull(result.parterNameList)) {				
					var $select = $("#"+selectId);
					var parterNameList = result.parterNameList;
					var html = '<option value="">부서/업체명 선택</option>';
					if (!atbsvg.util.isNull(parterNameList) && parterNameList.length > 0) {
						var size = parterNameList.length;
						for(var index = 0; index < size; index++) {
							var item = parterNameList[index];
							if(item.parter_code == value){
								html += '<option value="'+item.parter_code+'" type="select" selected>'+item.parter_name+'</option>';
							}else{
								html += '<option value="'+item.parter_code+'" type="select">'+item.parter_name+'</option>';
							}
						}
						if(value == "insert"){
							html += '<option value="insert" selected>직접입력</option>';
						}else{
							html += '<option value="insert">직접입력</option>';
						}
					}
					$select.html(html);
				}
			}) ;
	}
	
	function getWorkTypeCheckBoxList(selectId, settingValList, formId){
		var url =  wwmsUrl + '/code_worktype.do' ; 
		var method = 'GET';
		var headerMap = {"access_token" : "1234test", "request_type" : "2"};
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
	
	function getFactoryList(selectId, settingVal){
		
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
							
							/* */
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
		var from = $('#datepicker1').val();
		var to = $('#datepicker2').val();
		
		if(pageObj){
			paramObj = pageObj;
		}
		
		paramObj['from'] = from;
		paramObj['to'] = to;
		paramObj["parter_name"] = $('#search_parter_name').val().trim();
		paramObj["name"] = $('#search_name').val().trim();
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
		var url 		= wwmsUrl + '/work.do?'+param ; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap 	= null;
		
		$('#workerMnt_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable) ;
	}
	
	function bindTable(result) {
		var totalCnt = 0;		
		var list = $('#workerMnt_tbody').appendTable({
			 data    : result.workMntList
			,totalCnt: result.totalcnt
			,curPage : curPage
			,listCnt : listCnt
			,column  : ['totalIndex', 'work_day', 'work_no', 'name', 'parter_name', 'work_type_img', 'starting_date', 'complete_date', 'worker_count'
			    , 'factory_name', 'zone_name','real_starting_date','real_ending_date', 'real_worker_count',  'worker_factory_name', 'worker_zone_name']
			,link    : { 2:{'class' : 'info-pop-btn hoverred', 'key' : 'work_uid'}}
			,defaultVal : 	{1 : '-', 2 : '-', 3 : '-', 4 : '-', 5 : '-', 6 : '-', 7 : '-', 8 : '-', 9 : '-', 10 : '-', 11 : '-', 12 : '-', 13 : '-', 14 : '-' , 15 : '-' }
		});	
		list.event('modify', function(){infoWorkPopup()});
		var page_ret = list.paging('#page_navi',{'totalCnt':$.fn.appendTable.defaults['totalCnt'], 'serverSideEvent' : 'page_list', 'curPage':curPage, 'lstCnt' : listCnt});
//		filterColum();
	}
	
// 	function filterColum(){
// 		$("[name=complete_date_name]").each( function () {
// 	      if($(this).data("check")==1){
// 	    	  $(this).css("font-weight","Bold");
// 	      }
// 	   });
// 		$("[name=starting_date_name]").each( function () {
// 		      if($(this).data("check")!=""){
// 		    	  $(this).css("font-weight","Bold");
// 		      }
// 		   });
// 	}

// row click
	function infoWorkPopup() {
		$('#workerMnt_tbody tr').click(function() {
			var workUid = $(this).find('.info-pop-btn').data("key");
			if( workUid === null ) {
				return false;
			}
			
			var url 		= wwmsUrl + '/workDetail.do?work_uid='+workUid; 
			var method 		= methodType.GET;
			var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
			var paramMap 	= null;
			doRestFulApi(url, headerMap, method, paramMap, modifyWorkInfo );
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
	
	function setDatePicker1() {
		$('#datepicker1').datepicker('show');
	}
	
	function setDatePicker2() {
		$('#datepicker2').datepicker('show');
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

	function excelDownLoad(){
		var paramObj={};		
		paramObj['from'] =  $('#datepicker1').val();
		paramObj['to'] = $('#datepicker2').val();
		paramObj["parter_name"] = $('#search_parter_name').val().trim();
		paramObj["name"] = $('#search_name').val().trim();
		paramObj['target'] = 'work_mnt';		
		var param 	= getJsonObjToGetParam(paramObj);	    
	    $(location).attr("href", '/danger/excel/workMnt_excel.do?'+param);
	}

</script>