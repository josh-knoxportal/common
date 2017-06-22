<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		init();
	});//jquery 끝
	
	function init(){
		
		$('#edit').click(function(){
			update();
		});
		
		$('#list').click(function(){
			move_detail();
		});
		
		var url = ngmsUrl + '/adminAccount/${account_uid}.do';
		var method = 'GET';
		var headerMap = {"access_token" : "1234test", "request_type" : "2"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, paramMap, function(result){
			$('#readonly_user_id').append(result.adminAccount.user_id);
			//result.adminAccount.email = '';
 			formSetting(result.adminAccount, 'form');
			return false;
		}) ;
	}
	
	function update() {
		var data = getFormData($('#form'));
		
		// check
		if(data.email === '') {
			alertMsg('이메일주소를 입력해주세요');
			return false;
		}
		
		if(!emailValidation(data['email'])) {
			alertMsg('이메일 주소가 유효하지 않습니다');
			return false;
		}
		
		if(data.current_password === '') {
			alertMsg('비밀번호를 입력해주세요');
			return false;
		}
		
		if(!blank_pattern(data.current_password)) {
			alertMsg("비밀번호는 공백없이 입력해주세요.");
			return false;
		}
		
		if(data.current_password === data.new_password){
			alertMsg("동일한 비밀번호 입니다.");
			return false;
		}
	
		if(data.new_password !== '' || data.new_password_confirm !== '') {
			if(!check_password_rule(data.new_password)) {
				return false;
			}
			
			if(check_password()) {
				alertMsg('입력하신 신규 비밀번호가 일치하지 않습니다.');
				return false;
			}
		}
		
		if(!confirm('입력하신 정보로 수정하시겠습니까?')) {
			return false;
		}
		
		var url = ngmsUrl + '/adminAccount.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "update"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result) {
			if(result.msg==='SUCCESS') {
				move_detail();
			} else if(result.msg==='NOTPASSWORD') {
				alertMsg('입력하신 비밀번호가 일치하지 않습니다.');
			} else if(result.msg==='EXISTEMAIL') {
				alertMsg('입력하신 이메일과 동일한 이메일이 존재합니다.');
			}
			return false;
		}) ;
	}
	
	function move_detail() {
		$(location).attr("href", contextPath + "/vehicle/admin/accountDetail.do");
	}
	
	function check_password() {
		var hasError = false;
		var current_password  = $("#current_password").val();
		var confirm_password  = $("#new_password").val();
		var confirm_password2 = $("#new_password_confirm").val();		
        
		if(current_password !== '') {
			if (confirm_password === '') { 
	            hasError = true;
	        } else if (confirm_password2 === '') { 
	            hasError = true;
	        } else if (confirm_password !== confirm_password2 ) { 
	            hasError = true;
	        }
		} else {
			hasError = true;
		}
		return hasError;
	}
</script>
				<div class="notice-essential">
					<p><span>* </span> 표시는 필수 입력항목입니다.</p>
				</div>
				<div class="form-wrap clearfix pb60">
					<h5 class="blind_position">계정 등록 정보를 입력해주세요.</h5>
					<form id="form" name="form" action="#" class="clearfix">
						<input type="hidden" id="account_uid" name="account_uid" />
						<input type="hidden" id="user_id" name="user_id" />
						<div class="group-box display-table">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">아이디 <span>*</span></label>
								</div>
								<div class="table-cell" id="readonly_user_id"></div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">이메일 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" name="email" id="email"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">비밀번호 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="password" name="current_password" id="current_password" />
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">새 비밀번호</label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="password" name="new_password" id="new_password" />
									</div>
									(비밀번호 변경 시에만 입력해주세요.)
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">새 비밀번호 확인</label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="password" name="new_password_confirm" id="new_password_confirm" />
									</div>
									(비밀번호 변경 시에만 입력해주세요.)
								</div>
							</div>
						</div>
						<div class="form-actions text-right">
							<button type="button" class="btn mr5" id="list">목 록</button>
							<button type="button" class="btn" id="edit">수 정</button>
						</div>
					</form>
				</div>