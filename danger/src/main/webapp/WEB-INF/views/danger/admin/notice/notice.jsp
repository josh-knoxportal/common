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

<script type="text/javascript">
var orderId = 'worker_name';
var orderBy = 'asc';
var listCnt = 10;
var curPage = 1;
$(document).ready(function() {
	$('#searchBtn').click(function() {
		initSearch();
	});
	$('#all_checkBox').change(function() {
		allCheckBox("all_checkBox", "checked_device_no");
	});
	
	$('#worker_select').click(function() {
		selectWorker();
	});
	
	$('#all_worker_select').click(function() {
// 		selectWorker('all');
		selectWorkerAll();
	});
	
	$('#all_worker_delete').click(function() {
		allDeleteWorker();
	});
	
	$("#worker_name").keydown(function (event) {
        if (event.which === 13) {    //enter
        	initSearch();
        }
    });
	
	$("#device_no").keydown(function (event) {
        if (event.which === 13) {    //enter
        	initSearch();
        }
    });
	$('#send_notice').click(function() {
		//TODO 알림 전송
		noticePushSend();
	});
	
	$('#send_alarm_msg').keyup(function() {
		if($('#send_alarm_msg').val().length>80){
			alertMsg('허용 글자 수를 초과하였습니다.');
			$("#send_alarm_msg").val($('#send_alarm_msg').val().substring(0,80));
		}
		$("#text_count").html('('+$('#send_alarm_msg').val().length+'/80자)');
	});
	$('.numbersOnly').keyup(function () { 
		if (this.value != this.value.replace(/[^0-9\.]/g, '')) {
			this.value = this.value.replace(/[^0-9\.]/g, '');
		}
	});	
	init();
});//jquery 끝

//초기화
function init() {
	initSearch();
}

function allDeleteWorker(){
	$('#worker_list').empty();
	$("#work_count").text("(총 0명)");
}

function initSearch(pageObj) {
	var paramObj={};
	
	if(pageObj){
		paramObj = pageObj;
	}
	
	paramObj["worker_name"] = $('#worker_name').val().trim();
	paramObj["device_no"] = $('#device_no').val().trim();
	paramObj['limit_count'] = listCnt;
	paramObj['order_type'] = orderId;
	paramObj['order_desc_asc'] = orderBy;
	
	if(!check_searchword(paramObj.worker_name)) {
		alertMsg('작업자 입력은 기호,특수문자는<br>입력할 수 없습니다.');
		return false;
	}
	
	document.getElementById('all_checkBox').checked = false;
	search(paramObj);
}


function checkBoxReset(checkName) {
	var checkBoxList = $("input[name="+checkName+"]");
	document.getElementById("all_checkBox").checked=false;
	for(i=0; i<checkBoxList.length; i++){
		checkBoxList[i].checked= false;
	}
}


function selectWorker(allYn){
	var checkedList;
	var filterList = [];
	if(allYn=='all'){
		$('#worker_list').empty();
		checkedList = settingDeviceNo($("input[name=checked_device_no]"));
	}else{
		checkedList = settingDeviceNo($("input[name=checked_device_no]:checked"));
		filterList = filterDeviceNo(checkedList);
		checkBoxReset("checked_device_no");
	}
	if(checkedList.length==0){
		alertMsg("긴급 알림을 전송할 작업자를 선택해주세요.");
		return;
	}
	if(allYn!='all'){
		if(filterList.length==0){
			alertMsg("동일한 단말 번호가 전송 대상자에 있습니다.");
			return;
		}else{
			checkedList = filterList;
		}
	}
	if(checkedList.length!=0){
		var html = '';
		for(i=0; i<checkedList.length; i++){			
			html += '<div class="send_item" id="wokr_li_'+checkedList[i].value+'">';
			html += '	<span>'+$(checkedList[i]).data("name")+'</span>';
			html += '	<span class="work_phone_no">'+checkedList[i].value+'</span>';
			html += '	<span class="btn"><a href="#;" onclick="deleteWorker(\'wokr_li_'+checkedList[i].value+'\')"><img src="<c:url value="/resources/img"/>/btn_userlist_del.gif" alt="삭제"></a></span></div>';
			html += '</div>';
		}
		
		$('#worker_list').append(html);
		$("#work_count").text("(총 "+$('#worker_list').children("div.send_item").length +"명)");
	}
}

function deleteWorker(indexId){
	$("#"+indexId).remove();
	$("#work_count").text("(총 "+$('#worker_list').children("div.send_item").length +"명)");
}

function settingDeviceNo(checkList){
	var deviceListMap = {};
	var returnList = [];
	
	for(i=0; i<checkList.length; i++){
		var flag = true;
		if(i==0){
			returnList.push(checkList[i]);
		}else{
			for(j=0; j<returnList.length; j++){
				if(returnList[j].value==checkList[i].value){
					flag = false;
				}
			}
			if(flag){
				returnList.push(checkList[i]);
			}
		}
	}
	return returnList;
}

function filterDeviceNo(checkList){
	var deviceListMap = {};
	var workList = $("#worker_list .work_phone_no");
	var returnList = [];
	
	for(i=0; i<checkList.length; i++){
		var flag = true;
		for(j=0; j<workList.length; j++){
			if($(workList[j]).text()==checkList[i].value){
				flag = false;
			}
		}
		if(flag){
			returnList.push(checkList[i]);
		}
	}
	return returnList;
}

//검색메소드
function search(paramObj) {
	var param 		= getJsonObjToGetParam(paramObj);
	var url 		= wwmsUrl + '/assignWork.do?'+param ; 
	var method 		= methodType.GET;
	var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
	var paramMap 	= null;		
	
	$('#workerList_tbody').tableLoading({});
	doRestFulApi(url, headerMap, method, paramMap, bindTable) ;
}

function bindTable(result) {
	var totalCnt = 0;
	var is_completed_img = {1:'<img src="<c:url value="/resources/img"/>/icon_work_off.gif" alt="">',0:'<img src="<c:url value="/resources/img"/>/icon_work_on.gif" alt="">'};
	var list = $('#workerList_tbody').appendTable({
			 data		: result.workStatusList
			,totalCnt 	: result.totalcnt
			,curPage    : curPage
			,listCnt    : listCnt
			,column		: ['check_box', 'worker_name', 'device_no', 'parter_name' , 'last_factory_name', 'last_zone_name', 'work_type_name', 'is_completed']
			,cdType		: {7 : is_completed_img}
			,defaultVal : {1:'-',2:'-',3:'-', 4 : '위치미확인', 5 : '위치미확인', 6 : '-', 7 : '-'}
			,editCols	: {0 : {'name' : 'checked_device_no', 'key' : 'device_no', 'data' : ['name','worker_name']}} //data 옵션 : [key, value]
	});	
	var page_ret = list.paging('#page_navi',{'totalCnt':$.fn.appendTable.defaults['totalCnt'], 'serverSideEvent' : 'page_list', 'curPage':curPage, 'lstCnt' : listCnt});
}

//server side page
function page_list(page){
	var paramObj = {}
	curPage = page;
	paramObj['limit_offset'] = ( page -1 ) * listCnt;
	
	initSearch(paramObj);
}

function orderbyEvent(orderName, img) {
	var $img = $(img).children("a").children("img");
	orderId = orderName;
	/*
	var allImgTh = $(img).parent("tr").children("th");
	
	for(i=0; i < allImgTh.length; i++){
		var thElement = allImgTh[i];
		$imgSrc = $(thElement).children("a").children("img").prop('src');
		
		if($(img).text() !=  $(thElement).text()){
			$(thElement).children("a").children("img").prop("src", $imgSrc.replace("_desc.png","_asc.png"));
		}
	}
	*/
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

function noticePushSend(){
	var phoneList = [];
	
	$('.work_phone_no').each(function() {
		phoneList.push($(this).html());
	});
	
	if(phoneList.length == 0){
		alertMsg('알림 대상자를 선택하세요.');
		return;
	}
	
	if($('#send_alarm_msg').val() == ''){
		alertMsg('메시지를 입력하세요.');
		return;
	}
	
	var url 		= wwmsUrl + '/restricted_push.do'; 
	var method 		= methodType.GET;
	var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
	var paramMap = {};
	paramMap['phoneList'] = phoneList;
	paramMap['msg'] = $('#send_alarm_msg').val();
	paramMap['user_id'] = 'danger';

	doRestFulApi(url, headerMap, methodType.POST, paramMap, 
		function (result) {
			alertMsg('알림을 성공적으로 <br/>전송했습니다.');
			allDeleteWorker();
			$('#send_alarm_msg').val("");
		}
	);		
}

function selectWorkerAll() {
	var paramObj={};
	paramObj["worker_name"] = $('#worker_name').val().trim();
	paramObj["device_no"] = $('#device_no').val().trim();
	paramObj['order_type'] = 'worker_name';
	paramObj['order_desc_asc'] = 'asc';

// 	if(!check_searchword(paramObj.worker_name)) {
// 		alertMsg('작업자 입력은 기호,특수문자는<br>입력할 수 없습니다.');
// 		return false;
// 	}
	
	document.getElementById('all_checkBox').checked = false;
	var param 		= getJsonObjToGetParam(paramObj);
	var url 		= wwmsUrl + '/assignWork.do?'+param ; 
	var method 		= methodType.GET;
	var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
	var paramMap 	= null;		
	doRestFulApi(url, headerMap, method, paramMap, selectAllWorker) ;	
}

function selectAllWorker(result){
	var totalCnt = 0;
	var data = [];
	data     = result.workStatusList;
	totalCnt = result.totalcnt;
	
	var checkedList;
	var filterList = [];
 
	$('#worker_list').empty();
	if( totalCnt == 0 ) {
		alertMsg("긴급 알림을 전송할 작업자를 선택해주세요.");
		return;
	} else {
		var html = '';
		for( i = 0 ; i < data.length ; i++ ) {			
			html += '<div class="send_item" id="wokr_li_'+data[i].device_no+'">';
			html += '	<span>'+data[i].worker_name+'</span>';
			html += '	<span class="work_phone_no">'+data[i].device_no+'</span>';
			html += '	<span class="btn"><a href="#;" onclick="deleteWorker(\'wokr_li_'+data[i].device_no+'\')"><img src="<c:url value="/resources/img"/>/btn_userlist_del.gif" alt="삭제"></a></span></div>';
			html += '</div>';
		}
		
		$('#worker_list').append(html);
		$("#work_count").text("(총 "+ totalCnt +"명)");
	}
}
</script>

<div id="contents"> <!-- contents s -->
	<div class="msg_target_box">
		<div class="msg_target_list">
			<div class="msg_target_title"><span class="title">작업자 목록</span></div>
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
										<input class="form-input" type="text" name="" id="worker_name" style="width:147px;">
									</div>
	
									<span class="search-text label">폰번호</span>
									<div class="input-box field-box">
										<input class="form-input numbersOnly" type="text" name="" id="device_no" style="width:147px;">
									</div>
									<button type="button" class="btn" id="searchBtn">검색</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<!-- // search-wrap -->
			
			<div class="table-list">
				<table class="ntype_tbl work_tbl fixed">
					<colgroup>
						<col style="width:8%">
						<col style="width:13.14%">
						<col style="width:13.14%">
						<col style="width:13.14%">
						<col style="width:13.14%">
						<col style="width:13.14%">
						<col style="width:13.14%">
						<col style="width:13.14%">
					</colgroup>
					<thead>
						<tr class="first_tr">
							<th rowspan="2"><input type="checkbox" id="all_checkBox"/></th>
							<th colspan="3">작업자정보</th>
							<th colspan="2">최종위치</th>
							<th colspan="2">작업정보</th>
						</tr>
						<tr>
							<th onclick="javascript:orderbyEvent('worker_name', this)">작업자 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('device_no', this)">스마트폰번호 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('parter_name', this)">업체명 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
		
							<th onclick="javascript:orderbyEvent('last_factory_name', this)">공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('last_zone_name', this)">Zone <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
		
							<th onclick="javascript:orderbyEvent('work_type_name', this)">작업유형 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
							<th onclick="javascript:orderbyEvent('is_completed', this)">작업여부 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
						</tr>
					</thead>
					<tbody id="workerList_tbody">
					
					</tbody>
				</table>
				
				<!--  pagination -->
				<div class="pn-wrap pn-single" id="page_navi">
				</div>
				<!--  // pagination -->
			</div>
	</div>
	
	<div class="msg_target_btn">
		<a href="#;"><img src="<c:url value="/resources/img"/>/btn_msg_target_sel.gif" alt="선택" id="worker_select"></a>
		<a href="#;"><img src="<c:url value="/resources/img"/>/btn_msg_target_all.gif" alt="전체" id="all_worker_select"></a>
	</div>
	
	<div class="msg_target">
		<div class="msg_target_title">
			<span class="title">전송 대상자</span>
			<span class="target_cnt" id="work_count">(총 0명)</span>
			<a href="#;"><img src="<c:url value="/resources/img"/>/btn_del_all.gif" alt="전체삭제" id="all_worker_delete"></a>
		</div>
		<div class="msg_target_sendlist">
			<div class="tbl_sendlist" id="worker_list">
			</div>
		</div>
	</div>
	
	<div class="msg_memo_box">
		<p>알람내용</p>
		<div class="msg_memo">
			<textarea id="send_alarm_msg"></textarea>
			<span class="first">※ 작업자에게 긴급 안내멘트를 발송해 주세요.</span>
			<span id="text_count">(0/80자)</span>
		</div>
		<a href="#;"><img src="<c:url value="/resources/img"/>/btn_msg_send.gif" alt="전송" id="send_notice"></a>
	</div>	
	
</div>
