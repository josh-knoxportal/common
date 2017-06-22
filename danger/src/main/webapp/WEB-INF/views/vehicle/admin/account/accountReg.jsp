<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		init();
	});//jquery 끝
	
	function init() {
		$('#create').click(function() {
			event.preventDefault();
			create();
		});
		
		$('#list').click(function() {
			event.preventDefault();
			move_List();
		});
	}
	
	function create() {
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
		
		if(data.email === '') {
			alertMsg('이메일주소를 입력해주세요');
			return false;
		}
		
		if(!emailValidation(data['email'])) {
			alertMsg('이메일 주소가 유효하지 않습니다');
			return false;
		}
		
		if(data.password === '') {
			alertMsg('비밀번호를 입력해주세요');
			return false;
		}
		
		if(!check_password_rule(data.password)) {
			return false;
		}
		
		if(data.password_confirm === '') {
			alertMsg('비밀번호 확인를 입력해주세요');
			return false;
		}

		if(data.password !== data.password_confirm) {
			alertMsg('입력하신 신규 비밀번호가 일치하지 않습니다.');
			return false;
		}
		
		if(!confirm('입력하신 정보로 계정등록 하시겠습니까?')) {
			return false;
		}
			
		var url = ngmsUrl + '/adminAccount.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "create"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
// 			console.log(result);
			if(result.msg==='SUCCESS') {
				alertMsg('입력하신 정보로 계정 등록하였습니다.');
				move_List();
			} else if(result.msg==='EXISTUSERID') {
				alertMsg('입력하신 아이디와 동일한 아이디가 존재합니다.');
			} else if(result.msg==='EXISTEMAIL') {
				alertMsg('입력하신 이메일과 동일한 이메일이 존재합니다.');
			}
			return false;
		}) ;
	}
	
	function move_List() {
		$(location).attr("href", contextPath + "/vehicle/admin/account.do");
	}	
</script>
				<div class="notice-essential">
					<p><span>* </span> 표시는 필수 입력항목입니다.</p>
				</div>
				<div class="form-wrap clearfix pb60">
					<h5 class="blind_position">계정 등록 정보를 입력해주세요.</h5>
					<form id="form" action="#" class="clearfix">
						<div class="group-box display-table table-4-col">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">아이디 <span>*</span></label>
								</div>
								<div class="table-cell table-2-cell">
									<div class="input-box">
										<input class="form-input" type="text" name="user_id"/>
									</div>
								</div>
								<div class="table-cell title-cell"	>
									<label class="control-label">관리권한 구분 <span>*</span></label>
								</div>
								<div class="table-cell">
									일반 관리자
								</div>
							</div>

							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">이름 <span>*</span></label>
								</div>
								<div class="table-cell table-2-cell">
									<div class="input-box">
										<input class="form-input" type="text" name="name" value=""/>
									</div>
								</div>
								<div class="table-cell title-cell">
									<label class="control-label">이메일 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" name="email" value=""/>
									</div>
								</div>
							</div>

							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">비밀번호 <span>*</span></label>
								</div>
								<div class="table-cell table-2-cell">
									<div class="input-box">
										<input class="form-input" type="password" name="password" maxlength="14"  value=""/>
									</div>
								</div>
								<div class="table-cell title-cell">
									<label class="control-label">비밀번호 확인 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="password" name="password_confirm" maxlength="14" value=""/>
									</div>
								</div>
							</div>
						</div>
						<div class="form-actions text-right">
							<button id="list"   type="button" class="btn mr5">목 록</button>
							<button id="create" type="button" class="btn">확 인</button>
						</div>
					</form>
				</div>