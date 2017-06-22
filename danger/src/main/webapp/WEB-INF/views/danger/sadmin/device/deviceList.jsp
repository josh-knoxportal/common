<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
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
	var orderId = 'last_server_access_time';
	var orderBy = 'desc';
	var listCnt = 15;
	var curPage = 1;
	
	$(document).ready(function() {
		init();
	});

	//초기화
	function init() {
		search_init();

		$('.close-btn').click(function(){
			$(this).data('key','');
			resetSearchData($('#popup_form input'));
			$(this).closest(".pop-wrap").hide();
		});

		$('#registerPopup').click(function(){
			$(this).data('key','');
			resetSearchData($('#popup_form input'));
			$('.reg-pop').show();
			setDatepicker("reg_datepicker");
			getFactoryList('select_factory_list');
		});

		$('.mclose-btn').click(function(){
			$(this).data('key','');
			resetSearchData($('#mpopup_form input'));
			$(this).closest(".pop-wrap").hide();
		});

		$('#register').click(function() {
			register();
		});

		$('#update').click(function() {
			update();
		});

		$('#delete').click(function() {
			remove();
		});
		
		$('#password').click(function() {
			$(location).attr("href", contextPath + "/sadmin/devicePassword.do");
		});
		
		$('#excel_downloadBtn').click(function() {
			excelDownLoad();
		});
		
		$('.numbersOnly').keyup(function () { 
			if (this.value != this.value.replace(/[^0-9\.]/g, '')) {
				this.value = this.value.replace(/[^0-9\.]/g, '');
			}
		});
	}

	function setDatepicker(datepicker1){
		var date = new Date();
		var today = date.format('yyyy-MM-dd');
		$("#"+datepicker1).val(today);
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
		    dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"]
		   });
	}

	function setDatePicker(id) {
		$('#'+id).datepicker('show');
	}

	
	//server side page
	function page_list(page){
		var paramObj = {};
		curPage = page;
		
		$.extend( paramObj, getSearchParam($('#search_form')) );
		paramObj['page_index'] = ( page -1 ) * listCnt;
		paramObj['record_count_per_page'] = listCnt;
		paramObj['order_type'] = orderId;
		paramObj['order_desc_asc'] = orderBy;
		
		search(paramObj);
	}
	
	function orderbyEvent(img, orderName) {
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

		page_list(1);
	}
	
	function search_init() {
		var paramObj = {};
		curPage      = 1;

		$.extend( paramObj, getSearchParam($('#search_form')) );
		paramObj['page_index']            = 0;
		paramObj['record_count_per_page'] = listCnt;
		paramObj['order_type']            = 'last_server_access_time';
		paramObj['order_desc_asc']        = "DESC";
		search(paramObj);
	}
	
	function search(paramObj) {
		var param 		= getJsonObjToGetParam(paramObj);
		var url 		= wwmsUrl + '/device.do?'+param ;
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap 	= null;

		$('#tablelist_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable) ;
	}

	function bindTable(result) {
		var status = {1:'사용중',2:'수리',3:'정지',0:'사용종료'};		
		var list = $('#tablelist_tbody').appendTable({
			 data	 : result.deviceDangerList
			,totalCnt: result.totalCnt
			,curPage : curPage
			,listCnt : listCnt
			,column	 : ['totalIndex', 'device_no', 'network_type', 'factory_name', 'account_name', 'status', 'last_server_access_time', 'link_text' ]
			,link 	 : {1:{'class' : 'reg-pop-btn hoverred', 'key' : 'device_uid'}}
			,cdType	 : {5 : status}
			,defaultVal:{2 : '-', 3 : '-'}
			,editCols  :{7 : {'text' : '수정',  'class' : 'btn_tbl', 'key' : 'device_uid'}}
		});
		list.event('modify', function(){modifyPopupWindow()});
		list.event('modify2', function(){modifyPopupWindow2()});
		var page_ret = list.paging('#page_navi',{'totalCnt':$.fn.appendTable.defaults['totalCnt'], 'serverSideEvent' : 'page_list', 'curPage':curPage, 'lstCnt' : listCnt});
	}

	function modifyPopupWindow2() {
		$('.reg-pop-btn').click(function() {			
			var url 		= wwmsUrl + '/device/'+$(this).data("key")+'.do' ;
			var method 		= methodType.GET;
			var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
			var paramMap 	= null;
			doRestFulApi(url, headerMap, method, paramMap, function(result){
				formSetting(result.deviceDangerVo, 'mpopup_form');
				if(result.deviceDangerVo.network_type=='' || result.deviceDangerVo.network_type==null) {
					$("#mod_select_network_type").val("").prop("selected", true);
				}
				$('.modify-pop').show();				
				$('.last_modi_time').empty().append('(최종변경시간 : ' + result.deviceDangerVo.upd_date + ')');
				getFactoryList('mod_select_factory_list', result.deviceDangerVo.factory_uid);
				$("#mod_status_list").val(result.deviceDangerVo.status);
			});
			return false;
		});
	}

	function modifyPopupWindow() {
		$('.btn_tbl').click(function() {
			var url 		= wwmsUrl + '/device/'+$(this).data("key")+'.do' ;
			var method 		= methodType.GET;
			var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType);
			var paramMap 	= null;
			doRestFulApi(url, headerMap, method, paramMap, function(result){
				formSetting(result.deviceDangerVo, 'mpopup_form');
				if(result.deviceDangerVo.network_type=='' || result.deviceDangerVo.network_type==null) {
					$("#mod_select_network_type").val("").prop("selected", true);
				}
				$('.modify-pop').show();
				$('.last_modi_time').empty().append('(최종변경시간 : ' + result.deviceDangerVo.upd_date + ')');
				getFactoryList('mod_select_factory_list', result.deviceDangerVo.factory_uid);
				$("#mod_status_list").val(result.deviceDangerVo.status);
			}) ;
		});
	}
	
	function checkInputValue(data) {
		if(data.device_no === '') {
			alertMsg('스마트폰 번호를 입력해 주세요');
			return false;
		}
		
		if(data.device_no.length < 10) {
			alertMsg('스마트폰 번호를 자리수를 확인해주세요');
			return false;
		}
		
		if(data.factory_uid === '') {
			alertMsg('관리공장를 선택해 주세요');
			return false;
		}
		
		if(data.account_name === '') {
			alertMsg('관리자를 입력해 주세요');
			return false;
		}		
		return true;
	}

	function register() {
		var data = getFormData($('#popup_form'));
		
		if(!checkInputValue(data)){
			return false;	
		}
		
		if(data.reg_date === '') {
			alertMsg('등록일을 선택해 주세요');
			return false;
		}
		
		var url = wwmsUrl + '/device.do' ;
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType, {"opcode" : "create"});
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alertMsg('입력하신 정보로 단말등록 하였습니다.');
				$(".reg-pop").hide();
				search_init();
			} else if(result.msg==='EXISTDEVICENO') {
				alertMsg('동일한 단말기 번호가 이미 등록되어 있습니다. 단말기 번호를 확인해 주세요.');
				return false;
			}
		}) ;
	}

	function update() {
		var data = getFormData($('#mpopup_form'));
		
		if(!checkInputValue(data)){
			return false;	
		}
		if(data.is_assigned==1){
			alertMsg('현재 작업에 할당되어 수정할 수 없습니다.');
			return false;
		}
		if(!confirm('단말정보를 수정하시겠습니까?')) {
			return false;
		}

		var url = wwmsUrl + '/device.do' ;
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType, {"opcode" : "update"});
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alertMsg('단말정보를 수정하였습니다.');
				$(".modify-pop").hide();
				search_init();
			}
		}) ;
	}

	function remove() {
		var data = getFormData($('#mpopup_form'));
		if(data.is_assigned==1){
			alertMsg('현재 작업에 할당되어 삭제할 수 없습니다.');
			return false;
		}
		if(!confirm('선택하신 단말정보를 삭제하시겠습니까?')) {
			return false;
		}
		
		var url = wwmsUrl + '/device.do' ;
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType, {"opcode" : "deleteByUid"});
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alertMsg('해당 단말 정보를 삭제하였습니다.');
				$(".modify-pop").hide();
				search_init();
			}
		}) ;
	}

	function getFactoryList(selectId, settingVal) {

		var url = wwmsUrl + '/code_factoryzone/factory.do' ;
		var method = 'GET';
		var headerMap = {};
		var paramMap = null;

		doRestFulApi(url, headerMap, method, paramMap,
			function (result) {
    			if (!atbsvg.util.isNull(result) && !atbsvg.util.isNull(result.codeFactoryZoneList)) {
    				var $select = $("#"+selectId);
					var factoryZoneList = result.codeFactoryZoneList;
					var html = '<option value="" checked>관리공장</option>';

					if (!atbsvg.util.isNull(factoryZoneList) && factoryZoneList.length > 0) {
						var size = factoryZoneList.length;
						for(var index = 0; index < size; index++) {
							var item = factoryZoneList[index];
								html += '<option type="'+item.type+'" value="'+item.uid+'">'+item.name+'</option>'; 
						}
					}
					html += '<option value="0">인가없음</option>'; 
					html += '<option value="999">전공장</option>'; 
					$select.html(html);

					if(settingVal){
						$("#"+selectId).val(settingVal);
					}
    			}
			}) ;
	}
	
	function excelDownLoad() {
		var paramObj = {};
		paramObj['target'] = 'danger_device_list';
		var param = getJsonObjToGetParam(paramObj);		
		$(location).attr('href', '/danger/excel/device_danger_excel.do?'+param);
	}	
</script>
				<div class="excel-btn-box">
					<button id="excel_downloadBtn" type="button" class="excel-btn"><img src="<c:url value="/resources/img"/>/excel_down.png" alt=" ">엑셀 다운로드</button>
				</div>

				<form id="search_form" name="search_form"></form>
				<div class="table-list">
					<table class="ntype_tbl">
						<colgroup>
							<col style="width:70px;">
							<col style="width:170px;">
							<col style="width:170px;">
							<col style="width:170px;">
							<col style="width:auto;">
							<col style="width:130px;">
							<col style="width:130px;">
							<col style="width:130px;">
						</colgroup>
						<thead>
							<tr class="first_tr">
								<th>No</th>
								<th onclick="javascript:orderbyEvent(this, 'device_no')">스마트폰 번호 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'network_type')">폰구분 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'factory_name')">관리공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'account_name')">관리자 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'status')">상태 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'last_server_access_time')">업데이트 일자 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
								<th>수정</th>
							</tr>
						</thead>
						<tbody id='tablelist_tbody'></tbody>
					</table>

					<div class="form-actions">
						<button type="button" class="btn mr10" id="registerPopup">등록</button><button type="button" class="btn" id="password">비밀번호설정</button>
					</div>

					<!--  pagination -->
					<div class="pn-wrap pn-single" id="page_navi"></div>
				</div>


	<div class="pop-wrap pop_tmn reg-pop"> <!-- pop_tmn -->
		<div class="pop-bg"></div>
		<div class="pop-up"> <!-- pop-up s -->
			<div class="pop-header clearfix">
				<h3 class="pop-title">단말정보</h3>
				<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
			</div>
			<div class="pop-contents"> <!-- pop-contents -->

				<div class="form-wrap prev-box clearfix">
					<form id="popup_form" action="#" class="clearfix">
						<div class="group-box display-table">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">스마트폰 번호 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input numbersOnly" type="text" id="device_no" name="device_no" maxlength="11"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">폰구분 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="select-box full">
										<select class="sel" id="select_network_type" name="network_type">
											<option value="" checked>폰구분</option>
											<option value="totalcompany">통합(법인)폰</option>
											<option value="totalperson">통합(개인)폰</option>
											<option value="LGU+">LGU+</option>
											<option value="SKT">SKT</option>
											<option value="KT">KT</option>
										</select>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">관리공장 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="select-box full">
										<select class="sel" id="select_factory_list" name="factory_uid">
										</select>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">관리자 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
<!-- 										<input class="form-input" type="text" id="account_uid" name="account_uid"/> -->
										<input class="form-input" type="text" id="account_name" name="account_name"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">등록일 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box calendar-box full">
										<input id="reg_datepicker" class="form-input" type="text" name="reg_date"/>
										<button type="button" class="calendar-btn blind" onclick="setDatePicker('reg_datepicker')" title="날짜를 선택해주세요.">날짜선택</button>
									</div>
								</div>
							</div>
						</div>
						<div class="form-actions text-center">
							<button id='register' type="button" class="btn confirm-btn">등록</button>
						</div>
					</form>
				</div>

			</div> <!-- pop-contents e -->
		</div> <!-- pop-up e -->
	</div> <!-- pop_tmn e -->

	<div class="pop-wrap pop_tmn modify-pop"> <!-- pop_tmn -->
		<div class="pop-bg"></div>
		<div class="pop-up"> <!-- pop-up s -->
			<div class="pop-header clearfix">
				<h3 class="pop-title">단말정보</h2>
				<button type="button" class="pop-close mclose-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
			</div>
			<div class="pop-contents"> <!-- pop-contents -->

				<p class="last_modi_time">(최종변경시간 :)</p>

				<div class="form-wrap prev-box clearfix">
					<form id="mpopup_form" action="#" class="clearfix">
					<input type="hidden" id="device_uid" name="device_uid"/>
					<input type="hidden" id="is_assigned" name="is_assigned"/>
						<div class="group-box display-table">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">스마트폰 번호 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" id="device_no" name="device_no" readonly />
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">폰구분 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" id="network_type" name="network_type" readonly />
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">관리공장 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="select-box full">
										<select class="sel" id="mod_select_factory_list" name="factory_uid">
										</select>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">관리자 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" id="account_name" name="account_name"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">상태 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="select-box full">
										<select class="sel" id="mod_status_list" name="status">
											<option type="status" value=1>사용중</option>
											<option type="status" value=2>수리</option>
											<option type="status" value=3>정지</option>
											<option type="status" value=0>사용종료</option>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="form-actions text-center">
							<button id='delete' type="button" class="btn mr5 confirm-btn">삭제</button>
							<button id='update' type="button" class="btn confirm-btn">수정</button>
						</div>
					</form>
				</div>

			</div> <!-- pop-contents e -->
		</div> <!-- pop-up e -->
	</div> <!-- pop_tmn e -->