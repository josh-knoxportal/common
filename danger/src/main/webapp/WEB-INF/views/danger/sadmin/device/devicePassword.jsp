<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		init();
	});//jquery 끝
	
	function init(){		
		$('#edit').click(function(){
			event.preventDefault();
			update();
		});
		
		$('#list').click(function(){
			event.preventDefault();
			$(location).attr("href", contextPath + "/sadmin/device.do");
		});
	}
	
	function update(){
		var data = getFormData($('#form'));
		var blank_pattern = /[\s]/g;
		
		if(data.current_password === '') {
			alertMsg('현재 비밀번호를 입력해 주세요'); 
			return false;
		}
		
		if( blank_pattern.test(data.current_password) === true){
			alertMsg("현재 비밀번호는 공백없이 입력해주세요.");
			return false;
		}
		
		if(data.current_password.length !== 4) {
			alertMsg('현재 비밀번호는 4자리로 입력해 주세요');
			return false;
		}
		
		if(data.new_password === '') {
			alertMsg('신규 비밀번호를 입력해 주세요');
			return false;
		}
		
		if( blank_pattern.test(data.new_password) === true){
			alertMsg("신규 비밀번호는 공백없이 입력해주세요.");
			return false;
		}
		
		if(data.new_password.length !== 4) {
			alertMsg('신규 비밀번호는 4자리로 입력해 주세요');
			return false;
		}
		
		if(data.new_password_confirm === '') {
			alertMsg('신규 비밀번호 재입력 해주세요');
			return false;
		}
		
		if( blank_pattern.test(data.new_password_confirm) === true){
			alertMsg("신규 비밀번호 재입력는 공백없이 입력해주세요.");
			return false;
		}
		
		if(data.new_password_confirm.length !== 4) {
			alertMsg('신규 비밀번호 재입력는 4자리로 입력해 주세요');
			return false;
		}
		
		if(data.new_password !== data.new_password_confirm) {
			alertMsg('입력하신 신규 비밀번호가 일치하지 않습니다.');
			return false;
		}
		
		if(!confirm('입력하신 정보로 수정하시겠습니까?'))
			return false;

		var url = wwmsUrl + '/devicePassword.do' ;
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "resetDevicePassword"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result) {
			if(result.msg==='SUCCESS') {
				$(location).attr("href", contextPath + "/sadmin/device.do");
			} else if(result.msg==='NOTMATCH') {
				alertMsg('입력하신 비밀번호가 일치하지 않습니다.');
				return false;
			}
		});
	}
</script>
				<div class="form-wrap clearfix pb60">
					<form id="form" name="form" action="#" class="clearfix">
					<input type="hidden" id="account_uid" name="account_uid" />
						<div class="group-box display-table">
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">현재 비밀번호 입력<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="password" class="form-input numbersOnly" name="current_password" id="current_password" maxlength="4"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">신규 비밀번호 입력<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="password" class="form-input numbersOnly" name="new_password" id="new_password"  maxlength="4"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">신규 비밀번호 재입력<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="password" class="form-input numbersOnly" name="new_password_confirm" id="new_password_confirm" maxlength="4" />
									</div>
								</div>
							</div>
						</div>
						<div class="form-actions text-right">
							<button type="button" class="btn mr5" id="list">취소</button>
							<button type="button" class="btn" id="edit">확인</button>  							
						</div>
					</form>
				</div>