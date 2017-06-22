<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" session="false"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		$('#login').click(function(){
			login();
		});
		
		$('.id-confirm-btn').click(function(){
			search_id();
		});
		
		$('.pw-confirm-btn').click(function(){
			reset_password();
		});
		
		init();
	});//jquery 끝
	
	//초기화
	function init(){

	}

	//검색메소드
	function login(paramObj){
		var data = getFormData($('#form'));
		
		// check
		if(data.user_id === '') {
			alert('아이디를 입력해주세요');
			return false;
		}
		
		if(calByte.getByteLength(data.user_id) > 50) {
			alert('아이디는 50자리 이내로 입력해주세요.');
			return false;
		}
		
		if(data.password === '') {
			alert('비밀번호를 입력해주세요');
			return false;
		}
		
		var url = ngmsUrl + '/danger/login.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				move_page(result.forward_url);
			} else if(result.msg==='NOMATCH'){
				alert('아이디와 비밀번호가 일치하지 않습니다.');
			} else if(result.msg==='POPUPPASSOWORD'){
				alert('입력한 비밀번호가 5회이상 일치하지 않습니다.\n비밀번호 재설정 화면으로 이동합니다.');
				resetSearchData($('#pw_form input'));
				$('.reset-pw-pop').show();
			}
			return false;
		}) ;
	}

	function move_page(forward_url) {
		$(location).attr("href", contextPath + forward_url);
	}

	function search_id() {
		var data = getFormData($('#id_form'));
		
		if(data.name === '') {
			alert('이름를 입력해주세요');
			return false;
		}
		
		if(data.email === '') {
			alert('등록된 이메일주소를 입력해주세요');
			return false;
		}
		
		if(!emailValidation(data['email'])) {
			alert('이메일 주소가 유효하지 않습니다');
			return false;
		}
		
		var url = ngmsUrl + '/danger/searchId.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				$('.prev-box').hide();
				$('.next-box').show();
			} else if( result.msg === 'NOMATCHID' ) {
				alert('아이디와 이메일이 일치하지 않습니다.');
			} else if( result.msg === 'FAILEMAIL') {
				alert('이메일 전송에 오류가 발생하였습니다.');
			}
			return false;
		}) ;			
	}
		
	function reset_password() {
		var data = getFormData($('#pw_form'));
		
		// check
		if(data.user_id === '') {
			alert('로그인 ID를 입력해주세요');
			return false;
		}
		
		if(data.email === '') {
			alert('등록된 이메일주소를 입력해주세요');
			return false;
		}
		
		if(!emailValidation(data['email'])) {
			alert('이메일 주소가 유효하지 않습니다');
			return false;
		}		
		
		var url = ngmsUrl + '/danger/resetPassword.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				$('.prev-box').hide();
				$('.next-box').show();
			} else if( result.msg === 'NOMATCHID' ) {
				alert('등록하지 않은 아이디 입니다.');
			} else if( result.msg === 'NOMATCHEMAIL') {
				alert('아이디와 이메일이 일치하지 않습니다.');
			} else if( result.msg === 'FAILEMAIL') {
				alert('이메일 전송에 오류가 발생하였습니다.');
			}
			return false;
		});			
	}
</script>
	<div id="wrap"><!-- wrap s -->
		<div id="login-wrap"> <!-- login-wrap s -->
			<div id="header">
				<h2 class="login-logo" style="margin-bottom:18px;"><img height="65" src="<c:url value="/resources"/>/img/lg_logo_dlogin.png" alt="현장출입관리"></h2>
			</div>
			<div id="contents"> <!-- contents s -->
				<div class="login-title">
					<h3>관리자 로그인</h3>
					<p>현장출입관리 사이트에 오신 것을 환영합니다.</p>
				</div>
				<div id="formWrap" class="clearfix"> 
					<div class="left-form"> <!-- left-form s -->
						<div class="login-form">
							<form id="form" name="form" action="#" class="clearfix">
								<div class="group-box">
									<div class="form-group clearfix">
										<label class="control-label">아이디</label>
										<div class="input-box">
											<input class="form-input enterClass" type="text" name="user_id" maxlength="50"/>
										</div>
									</div>
									<div class="form-group clearfix">
										<label class="control-label">비밀번호</label>
										<div class="input-box">
											<input class="form-input enterClass" type="password" name="password" maxlength="14"/>
										</div>
									</div>
								</div>
								<div class="form-actions">
									<button type="button" class="btn login-btn enterClass" id="login" name="login">로그인</button>           
								</div>
							</form>
						</div>
						<div class="user-find clearfix">
							<ul class="clearfix">
								<li>
									<button type="button" class="find-id-btn">아이디 찾기</button>
									<div class="line"></div>
								</li>
								<li>
									<button type="button" class="reset-pw-btn">비밀번호 재발급</button>
								</li>
							</ul>
						</div>
					</div> <!-- left-form e -->
					<div class="right-form-safe"> <!-- right-form s -->
						<p class="blind">현장출입관리</p>
					</div> <!-- right-form e -->
				</div>
			</div> <!-- contents e -->
			<div class="notice">
				<p>※ 불법적인 접근 또는 허가되지 않은 사용자가 접속을 시도할 경우 관계 법령에 의해 처벌을 받을 수 있습니다.</p>
			</div>
		</div> <!-- login-wrap e -->
	</div> <!-- wrap e -->
	<div id="footer">
		<address class="copy">Copyright 2016 LG Uplus Corp. All Rights Reserved.</address>
	</div>

	
	<div class="pop-wrap find-id-pop"> <!-- find-id-pop s -->
		<div class="pop-bg"></div>
		<div class="pop-up"> <!-- pop-up s -->
			<div class="pop-header clearfix">
				<h3 class="pop-title">아이디 찾기</h2>
				<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources"/>/img/pop-close.png" alt="팝업닫기"></button>
			</div>
			<div class="pop-contents"> <!-- pop-contents -->
				<div class="pop-info">
					<p>본인 확인 후 로그인 아이디를 등록된 메일 주소로 보내드립니다.</p>
				</div>

				<div class="form-wrap prev-box clearfix">
					<form id="id_form" action="#" class="clearfix">
						<div class="group-box display-table">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">이름 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input idEnterClass" type="text" name="name" maxlength="50"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">등록된 이메일 주소 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input idEnterClass" type="text" id="email" name="email" maxlength="50"/>
									</div>
								</div>
							</div>
						</div>
						<div class="form-actions text-center">
							<button type="button" class="btn mr5 id-confirm-btn">확 인</button>   
							<button type="button" class="btn close-btn">닫 기</button> 
						</div>
					</form>
				</div>

				<div class="form-wrap next-box clearfix">
					<div class="find-user-id display-table">
						<div class="table-row">
							<div class="table-cell">
								<span class="find-id-info">등록된 메일주소로 아이디 정보 전송하였습니다.</span>
							</div>
						</div>
					</div>
					<div class="form-actions text-center">
						<button type="button" class="btn close-btn">확 인</button> 
					</div>
				</div>

			</div> <!-- pop-contents e -->
		</div> <!-- pop-up e -->
	</div> <!-- find-id-pop e -->

	<div class="pop-wrap reset-pw-pop"> <!-- reset-pw-pop s -->
		<div class="pop-bg"></div>
		<div class="pop-up"> <!-- pop-up s -->
			<div class="pop-header clearfix">
				<h3 class="pop-title">비밀번호 재발급</h3>
				<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources"/>/img/pop-close.png" alt="팝업닫기"></button>
			</div>
			<div class="pop-contents"> <!-- pop-contents -->
				<div class="pop-info">
					<p class="pw-text-info">아이디와 등록된 이메일 주소를 입력해주세요.<br>이메일로 임시 비밀번호가 발송됩니다.</p>
				</div>
				<div class="form-wrap prev-box clearfix">
					<form id="pw_form" action="#" class="clearfix">
						<div class="group-box display-table">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">아이디 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input pwEnterClass" type="text" name="user_id" maxlength="50"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">이메일<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
<!-- 										<input class="form-input" type="text" name="user_id" value="dangertest"/> -->
										<input class="form-input pwEnterClass" type="text" name="email" maxlength="50"/>
									</div>
								</div>
							</div>
						</div>
						<div class="form-actions text-center">
							<button type="button" class="btn mr5 pw-confirm-btn">확 인</button>   
							<button type="button" class="btn close-btn">닫 기</button> 
						</div>
					</form>
				</div>

				<div class="form-wrap next-box clearfix">
					<div class="find-user-id display-table">
						<div class="table-row">
							<div class="table-cell">
								<span class="find-id-info">비밀번호 초기화가 완료 되었습니다.</span>
							</div>
						</div>
					</div>
					<div class="form-actions text-center">
						<button type="button" class="btn close-btn">확 인</button> 
					</div>
				</div>

			</div> <!-- pop-contents e -->
		</div> <!-- pop-up e -->
	</div> <!-- reset-pw-pop e -->