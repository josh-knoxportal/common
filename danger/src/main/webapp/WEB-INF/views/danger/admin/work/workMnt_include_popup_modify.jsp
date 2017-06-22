<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<div class="pop-wrap work_modifyPop">
	<div class="pop-bg"></div>
	<div class="pop-up"> <!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">작업 정보 수정</h3>
			<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
		</div>
		<div class="pop-contents"> <!-- pop-contents -->
			
			<div class="form-wrap prev-box clearfix">
				<form id="modify_popup_form" action="#" class="clearfix">
				<input type="hidden" name="work_uid">
				<input type="hidden" name="work_make_type">
				<p class="last_modi_time"></p>
					<div class="group-box display-table">
						 <div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업번호 <span></span></label>
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
								<label class="control-label">부서/업체명 <span></span></label>
							</div>
							<div class="table-cell">
								<div>
									<select class="sel" id="modi_parter_list" name="modi_parter_name" onchange="changParter()" >
										<option></option>
									</select> 
									<input class="form-input" type="text" name="modi_parter_name" id="modi_parter_name" maxlength="20" style="width: 30%;height: 26px;padding: 0 8px;border: 1px solid #acacac;font-size: 12px;color: #555;"/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">단위공장 <span></span></label>
							</div>
							<div class="table-cell">
<!-- 								<div class="input-box"> -->
<!-- 									<input class="form-input" type="text" name="factory_name" id="factory_name" readonly/> -->
<!-- 								</div> -->
								<div class="select-box full">
									<select class="sel" id="modi_select_factory_list" name="modi_factory_uid" onchange="changeFactory()">
									</select> 
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업유형 </label>
							</div>
							<div class="table-cell">
								<div>
							 		<ul style="width:350px;" id="mod_select_work_type">
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
									<input id="mod_datepicker1" class="form-input calendar_input" type="text" name="starting_date" onchange="changeStartingDateTime('mod_datepicker1','mod_datepicker2', 'mod_starting_time','mod_complete_time','mod_starting_min','mod_complete_min')" readonly/>
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
									<input id="mod_datepicker2" class="form-input calendar_input" type="text" name="complete_date" readonly/>
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
<script type="text/javascript">
	$(document).ready(function() {
		// modify popup update
		$('#modify_completedWorkBtn').click(function() {
			modifyCompletedWork();
		});

		// modify popup delete
		$('#delete_completedWorkBtn').click(function() {
			deleteCompletedWork();
		});
	});

	function modifyWorkInfo(result) {
		if( result.workMntVo === null ) {
			return false;
		}
		
		var is_completed = result.workMntVo.is_completed;
		var real_worker_count = result.workMntVo.real_worker_count;
		
		if( is_completed === 0 && real_worker_count === 0 ) {
			work_before_init(result);
		}
		
		if( is_completed === 0 && real_worker_count > 0) {
			work_doing_init(result);	
		}
		
		if( is_completed === 1 ) {
			work_done_init(result);
			$("#mod_select_work_type li input").attr("disabled", true);
		}
	}
	
	function work_before_init(result) {
		resetSearchData($('.work_modifyPop input'));
		
		// time reset
		setTimeSelectbox("mod_starting_time","mod_starting_min", 1);
		setTimeSelectbox("mod_complete_time","mod_complete_min", 1);
		setDatepicker("mod_datepicker1","mod_datepicker2");					
		
		$('.work_modifyPop').show();
		$(".last_modi_time").text("(최종변경시간 : "+result.workMntVo.upd_date+")");
		
		var dataObject = dateSetting(result.workMntVo, "mod");				
		formSetting(dataObject, 'modify_popup_form');
		$("#mod_name").attr("readonly", false);
		
		// partner
		var parter_code;
		if(result.workMntVo.parter_name != null && result.workMntVo.parter_code == ""){
			parter_code = "insert";
			$('#modi_parter_name').show();
				$('#modi_parter_name').val(result.workMntVo.parter_name);
		} else {
			parter_code = result.workMntVo.parter_code;
			$('#modi_parter_name').hide();
		}
		getParterNameList('modi_parter_list',parter_code); 					

		// factory
		if( result.workMntVo.factory_uid != null && result.workMntVo.factory_uid != '' )
			getFactoryList('modi_select_factory_list', result.workMntVo.factory_uid);
		else
			getFactoryList('modi_select_factory_list', result.workMntVo.zone_uid);

		// work type
		if( result.workMntVo.work_type === null ) {
			getWorkTypeCheckBoxList('mod_select_work_type');
		} else {
			var wortList = result.workMntVo.work_type.split(",");
			getWorkTypeCheckBoxList('mod_select_work_type', wortList, "modify_popup_form");
		}
	}
	
	function work_doing_init(result) {
		resetSearchData($('.work_infoPop input'));
		formSetting(result.workMntVo, 'info_popup_form');
		$(".last_modi_time").text("(최종변경시간 : "+result.workMntVo.upd_date+")");
		if(result.workMntVo.work_type){
			var wortList = result.workMntVo.work_type.split(",");
			getWorkTypeCheckBoxListDisable('info_select_work_type', wortList, "info_popup_form");
		}
		$('.work_infoPop').show();
	}
	
	function modifyCompletedWork() {
		// worker_count
		if ($("#mod_worker_count").val() == "") {
			alertMsg('작업자수를 입력하세요.');
			return;
		}

		if ($("#mod_worker_count").val() == 0) {
			alertMsg('작업자수는 1명 이상 입력해야 합니다.');
			return;
		}

		if ($("#mod_worker_count").val() > 100) {
			alertMsg('작업자수는 100명까지만 입력 가능합니다.');
			return;
		}

		// data
		var data = getFormData($('#modify_popup_form'));		
		
		// start / end date
		var startingDay = data.starting_date + data.mod_starting_time + data.mod_starting_min;
		var completeDay = data.complete_date + data.mod_complete_time + data.mod_complete_min;	
		if(data.complete_date !== '') {
			if (startingDay > completeDay) {
				alertMsg('작업종료시간이 작업시작시간보다 빠를수 없습니다.');
				return;
			}	
		}		
		
		if ($("#modi_parter_list").children('option:selected').attr("value") === "insert") {
			data["parter_name"] = data.modi_parter_name;
			data["parter_code"] = '';
		} else {
			data["parter_name"] = $("#modi_parter_list").children('option:selected').text();
			data["parter_code"] = $("#modi_parter_list").val();
		}
		
		var zone_type = $("#modi_select_factory_list option:selected").attr('type');
		data["zone_type"] = zone_type;
		data["factory_uid"] = data["modi_factory_uid"];
		
		var work_type_list = [];
		if (data.work_type_check) {
			work_type_list = data.work_type_check.split(",");
		}
		data["type_list"] = work_type_list;
		
		if(data.starting_date!=='') {
			data["starting_date"] = data.starting_date + " " + data.mod_starting_time + ":" + data.mod_starting_min;
		}
		
		if(data.complete_date!=='') {
			data["complete_date"] = data.complete_date + " " + data.mod_complete_time + ":" + data.mod_complete_min;
		}

		var url = wwmsUrl + '/workUpdate.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
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
		var url = wwmsUrl + '/complatedWorkDelete.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		data = getFormData($('#modify_popup_form'));
		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('작업이 삭제 되었습니다.');
				$(".work_modifyPop").hide();
				initSearch();
			}
		});
	}
</script>
