<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<div class="pop-wrap work_completePop">
	<div class="pop-bg"></div>
	<div class="pop-up"> <!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">작업 정보 수정</h3>
			<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
		</div>
		<div class="pop-contents"> <!-- pop-contents -->
			
			<div class="form-wrap prev-box clearfix">
				<form id="complete_popup_form" action="#" class="clearfix">
				<input type="hidden" name="work_uid">
				<p class="last_modi_time"></p>
					<div class="group-box display-table">
						 <div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업번호 <span></span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="work_no" id="complete_work_no" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업명</label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="name" id="complete_name" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">부서/업체명 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="parter_name" id="parter_name" readonly/>
									<input class="form-input" type="hidden" name="parter_code" id="parter_code" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">단위공장 <span>*</span></label>
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
								<div>
							 		<ul style="width:350px;" id="complete_select_work_type">
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
									<input id="copmlete_datepicker1" class="form-input calendar_input" type="text" name="starting_date" onchange="changeStartingDateTime('copmlete_datepicker1','complete_datepicker2', 'complete_starting_time','complete_complete_time','complete_starting_min','complete_complete_min')" readonly/>
									<button type="button" class="calendar-btn blind calendar_button" onclick="setDatePicker('copmlete_datepicker1')" title="날짜를 선택해주세요.">날짜선택</button>
									<select class="sel2 calendar_sel1" id="complete_starting_time" name="complete_starting_time">
									</select> <span class="calendar_span">:</span>
									<select class="sel2 calendar_sel2" id="complete_starting_min" name="complete_starting_min">
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
									<input id="complete_datepicker2" class="form-input calendar_input" type="text" name="complete_date" readonly/>
									<button type="button" class="calendar-btn blind calendar_button" onclick="setDatePicker('complete_datepicker2')" title="날짜를 선택해주세요." >날짜선택</button>
									<select class="sel2 calendar_sel1" id="complete_complete_time" name="complete_complete_time">										
									</select> <span class="calendar_span">:</span>
									<select class="sel2 calendar_sel2" id="complete_complete_min" name="complete_complete_min">
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
									<input class="form-input numbersOnly" type="text" name="worker_count" id="complete_worker_count" maxlength="3"/>
								</div>
							</div>
						</div>
					</div>
					<div class="form-actions text-center">
						<button type="button" class="btn mr5 confirm-btn" id="cdelete_completedWorkBtn">삭제</button>
						<button type="button" class="btn" id="cmodify_completedWorkBtn">수정</button> 
					</div>
				</form>
			</div>

		</div> <!-- pop-contents e -->
	</div> <!-- pop-up e -->
</div> <!-- reset-pw-pop e -->
<script type="text/javascript">
	$(document).ready(function() {
		// modify popup update
		$('#cmodify_completedWorkBtn').click(function() {
			cmodifyCompletedWork();
		});

		// modify popup delete
		$('#cdelete_completedWorkBtn').click(function() {
			cdeleteCompletedWork();
		});
	});

	function work_done_init(result) {
		resetSearchData($('.work_completePop input'));
		$('.work_completePop').show();
		var parter_code;					

		if(result.workMntVo.parter_name != null && result.workMntVo.parter_code == ""){
			parter_code = "insert";
			$('#complete_parter_name').show();
				$('#complete_parter_name').val(result.workMntVo.parter_name);
		} else {
			parter_code = result.workMntVo.parter_code;
			$('#complete_parter_name').hide();
		}
		
 		setTimeSelectbox("complete_starting_time","complete_starting_min", 1);
		setTimeSelectbox("complete_complete_time","complete_complete_min", 1);
		setDatepicker("copmlete_datepicker1","complete_datepicker2");
		
		var dataObject = dateSetting(result.workMntVo, "complete");
		
		if(result.workMntVo.parter_name != null && result.workMntVo.parter_code == ""){
			dataObject['complete_parter_name']=result.workMntVo.parter_name;
		}
		formSetting(dataObject, 'complete_popup_form');
		$(".last_modi_time").text("(최종변경시간 : "+result.workMntVo.upd_date+")");
		
		// work type
		if( result.workMntVo.work_type ) {
			var wortList = result.workMntVo.work_type.split(",");
			getWorkTypeCheckBoxListDisable('complete_select_work_type', wortList, "complete_popup_form");
		}
	}
	
	function cmodifyCompletedWork() {
		// worker_count
		if ($("#complete_worker_count").val() == "") {
			alertMsg('작업자수를 입력하세요.');
			return;
		}

		if ($("#complete_worker_count").val() == 0) {
			alertMsg('작업자수는 1명 이상 입력해야 합니다.');
			return;
		}

		if ($("#complete_worker_count").val() > 100) {
			alertMsg('작업자수는 100명까지만 입력 가능합니다.');
			return;
		}

		// data
		var data = getFormData($('#complete_popup_form'));		
		
		// start / end date
		var startingDay = data.starting_date + data.complete_starting_time + data.complete_starting_min;
		var completeDay = data.complete_date + data.complete_complete_time + data.complete_complete_min;	
		if(data.complete_date !== '') {
			if (startingDay > completeDay) {
				alertMsg('작업종료시간이 작업시작시간보다 빠를수 없습니다.');
				return;
			}	
		}		
		
		if(data.starting_date!=='') {
			data["starting_date"] = data.starting_date + " " + data.complete_starting_time + ":" + data.complete_starting_min;
		}
		
		if(data.complete_date!=='') {
			data["complete_date"] = data.complete_date + " " + data.complete_complete_time + ":" + data.complete_complete_min;
		}
		
		var cdata = {};
		cdata["starting_date"] = data["starting_date"];
		cdata["complete_date"] = data["complete_date"];
		cdata["worker_count"]  = data["worker_count"];
		cdata["work_uid"]      = data["work_uid"];
		var url = wwmsUrl + '/workUpdate.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		doRestFulApi(url, headerMap, method, cdata, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('작업을 수정하였습니다.');
				$(".work_completePop").hide();
				initSearch();
			}
		});
	}

	function cdeleteCompletedWork() {
		var data = getFormData($('#complete_popup_form'));
		var url = wwmsUrl + '/complatedWorkDelete.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('작업이 삭제 되었습니다.');
				$(".work_completePop").hide();
				initSearch();
			}
		});
	}

	function getWorkTypeCheckBoxListDisable(selectId, settingValList, formId){
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
							html += '<li style="list-style:none; padding: 3px 0 3px 0; float:left; width:33%; box-sizing:border-box;"><input type="checkbox" onclick="return false;" name="work_type_check" value="'+item.code+'"/>'+item.name+'</li>';
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
</script>
