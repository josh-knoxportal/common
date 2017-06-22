<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<div class="pop-wrap work_registorPop"> <!-- reset-pw-pop s -->
	<div class="pop-bg"></div>
	<div class="pop-up"> <!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">작업 등록</h3>
			<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
		</div>
		<div class="pop-contents"> <!-- pop-contents -->
			<div class="form-wrap prev-box clearfix">
				<form id="workRegist_popup_form" action="#" class="clearfix">
				<p class="last_modi_time"></p>
					<div class="group-box display-table">
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업번호<span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="select-box full">
									<select class="sel" id="select_work_no" name="work_no" onchange="changWorkNo()">
										<option></option>
									</select> 
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업명 </label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="name" id="name" maxlength="50"/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">부서/업체명 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div>
									<select class="sel" id="select_parter_list" name="parter_name" onchange="changParter()" >
										<option></option>
									</select> 
									<input class="form-input" type="text" name="input_parter_name" id="input_parter_name" maxlength="20" style="width: 30%;height: 26px;padding: 0 8px;border: 1px solid #acacac;font-size: 12px;color: #555;"/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">단위공장 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="select-box full">
									<select class="sel" id="select_factory_list" name="factory_uid" onchange="changeFactory()">
									</select> 
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업유형</label>
							</div>
							<div class="table-cell">
								<div>
							 		<ul style="width:350px;" id="select_work_type">
							 		</ul>
							 	</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업시작일시</label>
							</div>
							<div class="table-cell">
								<div class="input-box calendar-box full">
									<input id="reg_datepicker1" class="form-input calendar_input" type="text" name="starting_date" onchange="changeStartingDateTime('reg_datepicker1','reg_datepicker2', 'reg_starting_time','reg_complete_time','reg_starting_min','reg_complete_min')" readonly/>
									<button type="button" class="calendar-btn blind calendar_button" onclick="setDatePicker('reg_datepicker1')" title="날짜를 선택해주세요.">날짜선택</button>
									<select class="sel2 calendar_sel1" id="reg_starting_time" name="reg_starting_time">
									</select> <span class="calendar_span">:</span>
									<select class="sel2 calendar_sel2" id="reg_starting_min" name="reg_starting_min">
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
									<input id="reg_datepicker2" class="form-input calendar_input" type="text" name="complete_date" readonly>
									<button type="button" class="calendar-btn blind calendar_button" onclick="setDatePicker('reg_datepicker2')" title="날짜를 선택해주세요.">날짜선택</button>
									<select class="sel2 calendar_sel1" id="reg_complete_time" name="reg_complete_time">
									</select> <span class="calendar_span">:</span>
									<select class="sel2 calendar_sel2" id="reg_complete_min" name="reg_complete_min">
									</select> 
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업자수 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input numbersOnly" type="text" name="worker_count" id="worker_count" maxlength="3"/>
								</div>
							</div>
						</div>
					</div>
					<div class="form-actions text-center">
						<button type="button" class="btn mr5 confirm-btn" id="delete_workBtn">삭제</button>   
						<button type="button" class="btn" id="work_registBtn">확인</button> 
					</div>
				</form>
			</div>
		</div> <!-- pop-contents e -->
	</div> <!-- pop-up e -->
</div> <!-- reset-pw-pop e -->
<script type="text/javascript">
	$(document).ready(function() {
		// register popup create btn
		$('#work_registBtn').click(function() {
			workRegistInit();
		});

		// register popup delete btn
		$('#delete_workBtn').click(function() {
			workDeleteInit();
		});
	});

	function workRegistInit() {
		var data = getFormData($('#workRegist_popup_form'));
		var startingDay = data.starting_date + data.reg_starting_time + data.reg_starting_min;
		var completeDay = data.complete_date + data.reg_complete_time + data.reg_complete_min;
		
		if ($("#select_parter_list option:selected").val() == "") {
			alertMsg('부서/업체명을 선택하세요.');
			return;
		}
		
		if ($("#select_factory_list option:selected").val() == "") {
			alertMsg('단위 공장을 선택하세요.');
			return;
		}
		
		if (!check_searchword(data.name)) {
			alertMsg('작업명 입력은<br>공백, 기호, 특수문자는<br>입력할 수 없습니다.');
			return false;
		}
		
		if ($("#worker_count").val() == "") {
			alertMsg('작업자수를 입력하세요.');
			return;
		}
		
		if ($("#worker_count").val() == 0) {
			alertMsg('작업자수는 1명 이상 입력해야 합니다.');
			return;
		}
		
		if ($("#worker_count").val() > 100) {
			alertMsg('작업자수는 100명까지만 입력 가능합니다.');
			return;
		}
		
		if (startingDay > completeDay) {
			alertMsg('작업종료시간이 작업시작시간보다 빠를수 없습니다.');
			return;
		}
		
		if ($("#select_work_no").children('option:selected').attr("type") == "auto") {
			registWork();
		} else {
			updateWork();
		}
	}

	function workDeleteInit() {
		if ($("#select_work_no").children('option:selected').attr("type") == "auto") {
			$(".work_registorPop").hide();
		} else {
			deleteWork();
		}
	}

	function registWork() {
		var type_list = [];
		var data = getFormData($('#workRegist_popup_form'));
		var type = $("#select_factory_list option:selected").attr('type');

		if (data.work_type_check) {
			type_list = data.work_type_check.split(",");
		}

		if ($("#select_parter_list").children('option:selected').attr("value") == "insert") {
			data["parter_name"] = data.input_parter_name;
			data["parter_code"] = '';
		} else {
			data["parter_name"] = $("#select_parter_list").children('option:selected').text();
			data["parter_code"] = $("#select_parter_list").val();
		}

		data["zone_type"] = type;
		data["type_list"] = type_list;
		data["work_no"] = $("#select_work_no").children('option:selected').text();
		data["starting_date"] = data.starting_date + " " + data.reg_starting_time + ":" + data.reg_starting_min;
		data["complete_date"] = data.complete_date + " " + data.reg_complete_time + ":" + data.reg_complete_min;
		var url = wwmsUrl + '/workInsert.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('작업이 등록되었습니다.');
				$(".work_registorPop").hide();
				initSearch();
			}
		});
	}

	function deleteWork() {
		var data = {};
		data["work_uid"] = $("#select_work_no").children('option:selected').val();
		var url = wwmsUrl + '/workDelete.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('작업이 삭제 되었습니다.');
				$(".work_registorPop").hide();
				initSearch();
			}
		});
	}

	function updateWork() {
		var type_list = [];
		var data = getFormData($('#workRegist_popup_form'));
		var type = $("#select_factory_list option:selected").attr('type');

		if (data.work_type_check) {
			type_list = data.work_type_check.split(",");
		}
		if ($("#select_parter_list").children('option:selected').attr("value") == "insert") {
			data["parter_name"] = data.modi_parter_name;
			data["parter_code"] = '';
		} else {
			data["parter_name"] = $("#select_parter_list").children('option:selected').text();
			data["parter_code"] = $("#select_parter_list").val();
		}
		data["zone_type"] = type;
		data["type_list"] = type_list;
		data["work_uid"] = $("#select_work_no").children('option:selected').val();
		data["work_no"] = $("#select_work_no").children('option:selected').text();
		data["starting_date"] = data.starting_date + " " + data.reg_starting_time + ":" + data.reg_starting_min;
		data["complete_date"] = data.complete_date + " " + data.reg_complete_time + ":" + data.reg_complete_min;

		var url = wwmsUrl + '/workUpdate.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;

		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('작업을 수정하였습니다.');
				$(".work_registorPop").hide();
				initSearch();
			}
		});
	}
</script>