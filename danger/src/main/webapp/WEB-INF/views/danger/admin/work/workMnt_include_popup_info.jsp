<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<div class="pop-wrap work_infoPop"> <!-- reset-pw-pop s -->
	<div class="pop-bg"></div>
	<div class="pop-up"> <!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">작업 정보</h3>
			<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
		</div>
		<div class="pop-contents"> <!-- pop-contents -->
			
			<div class="form-wrap prev-box clearfix">
				<form id="info_popup_form" action="#" class="clearfix">
				<p class="last_modi_time"></p>
					<div class="group-box display-table">
						<div class="form-group table-row first-group clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업번호 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="work_no" id="work_no" readonly/>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업명</label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="name" id="info_name" readonly/>
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
							 		<ul style="width:350px;" id="info_select_work_type">
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
									<input class="form-input" type="text" name="starting_date" readonly/>
									<button type="button" class="calendar-btn blind" onclick="" title="날짜를 선택해주세요.">날짜선택</button>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업종료일시</label>
							</div>
							<div class="table-cell">
								<div class="input-box calendar-box full">
									<input class="form-input" type="text" name="complete_date" readonly/>
									<button type="button" class="calendar-btn blind" onclick="" title="날짜를 선택해주세요.">날짜선택</button>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">작업자수 <span>*</span></label>
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