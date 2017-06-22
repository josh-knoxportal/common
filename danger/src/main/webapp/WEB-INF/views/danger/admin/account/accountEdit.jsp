<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
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
			move_detail();
		});
		
		var url = ngmsUrl + '/adminAccount/${sessionScope.account.account_uid}.do?system_type=${sessionScope.account.system_type}';
		var method = 'GET';
		var headerMap = {"access_token" : "1234test", "request_type" : "2"};
		var paramMap = {'system_type':${sessionScope.account.system_type}};
		doRestFulApi(url, headerMap, method, paramMap, function(result){
			formSetting(result.adminAccount, 'form');
			return false;
		}) ;
	}
	
	function update(){
		var data = getFormData($('#form'));
		
		// check
		if(data.user_id === '') {
			alertMsg('아이디를 입력해주세요');
			return false;
		}
		
		if(data.name === '') {
			alertMsg('이름를 입력해주세요');
			return false;
		}
		
		if(!check_searchword(data.name)) {
			alertMsg('이름은 공백, 기호, 특수문자는 입력할 수 없습니다.');
			return false;
		}
		
		if(data.department === '') {
			alertMsg('소속를 입력해주세요');
			return false;
		}
		
		if(!check_searchword(data.department)) {
			alertMsg('소속은 공백, 기호, 특수문자는 입력할 수 없습니다.');
			return false;
		}
		
		if(data.email === '') {
			alertMsg('이메일주소를 입력해주세요');
			return false;
		}
		
		if(!emailValidation(data['email'])) {
			alertMsg('이메일 주소가 유효하지 않습니다');
			return false;
		}
		
		if(data.current_password === '') {
			alertMsg('현재 비밀번호를 입력해주세요');
			return false;
		}
		
		if(!blank_pattern(data.current_password)) {
			alertMsg("현재 비밀번호는 공백없이 입력해주세요.");
			return false;
		}
		
		if(data.new_password !== '' || data.new_password_confirm !== '') {
			if(!check_password_rule(data.new_password)) {
				return false;
			}
			
			if(data.new_password !== data.new_password_confirm) {
				alertMsg('입력하신 신규 비밀번호가 일치하지 않습니다.');
				return false;
			}
		}	
		
		if(!confirm('입력하신 정보로 수정하시겠습니까?'))
			return false;
		
		var url = ngmsUrl + '/adminAccount.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "update"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				move_detail();
			} else if(result.msg==='NOTPASSWORD') {
				alertMsg('입력하신 비밀번호가 일치하지 않습니다.');
				return false;
			} else if(result.msg==='EXISTEMAIL') {
				alertMsg('입력하신 이메일과 동일한 이메일이 존재합니다.');
			}
		});
	}
	
	function move_detail() {
		$(location).attr("href", contextPath + "/admin/accountInfo.do");
	}
</script>
				<div class="form-wrap clearfix pb60">
					<form id="form" name="form" action="#" class="clearfix">
					<input type="hidden" id="account_uid" name="account_uid" />
						<div class="group-box display-table">
							<div class="form-group first-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">아이디<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="user_id" id="user_id" readonly>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">이름<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="name" id="name">
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">소속<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="department" id="department">
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">이메일<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="email" id="email">
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">현재 비밀번호 입력<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="password" class="form-input" name="current_password" id="current_password"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">신규 비밀번호 입력<span></span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="password" class="form-input" name="new_password" id="new_password" />
									</div> (비밀번호 변경 시에만 입력해주세요.)
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">신규 비밀번호 재입력<span></span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="password" class="form-input" name="new_password_confirm" id="new_password_confirm" />
									</div> (비밀번호 변경 시에만 입력해주세요.)
								</div>
							</div>
						</div>

						<div class="form-actions text-right">
							<button type="button" class="btn mr5" id="list">취소</button>
							<button type="button" class="btn" id="edit">수정</button>  							
						</div>
					</form>
				</div>