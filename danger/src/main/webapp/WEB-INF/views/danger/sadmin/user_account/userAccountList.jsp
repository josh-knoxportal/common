<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	var isAllowUserId = false;
	var orderId = 'reg_date';
	var orderBy = 'desc';	
	var listCnt = 15;
	var curPage = 1;
	
	$(document).ready(function() {
		init();
	});//jquery 끝
	
	//초기화
	function init(){
		search_init();

		$('.close-btn').click(function(){
			$(this).data('key','');
			resetSearchData($('#popup_form input'));			
			$(this).closest(".pop-wrap").hide();
		});
		
// 		$('#list').click(function(){
// 			$(this).data('key','');
// 			resetSearchData($('#popup_form input'));			
// 			$(this).closest(".pop-wrap").hide();
// 		});
		
		$('#registerPopup').click(function(){
			$(this).data('key','');
			resetSearchData($('#popup_form input'));						
			$('.reg-pop').show();
		});
		
		$('.mclose-btn').click(function(){
			$(this).data('key','');
			resetSearchData($('#mpopup_form input'));			
			$(this).closest(".pop-wrap").hide();
		});
		
		$('#registerPopup').click(function(){
			$(this).data('key','');
			resetSearchData($('#popup_form input'));						
			$('.reg-pop').show();
		});
		
		$('#isAllow').click(function(){
			isAllow();
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
		paramObj['order_type']            = 'reg_date';
		paramObj['order_desc_asc']        = "DESC";
		search(paramObj);
	}
	
	//검색메소드
	function search(paramObj){
		//var page, limit_offset, limit_count, search_filter 
		var param 		= getJsonObjToGetParam(paramObj);
		var url 		= ngmsUrl + '/adminAccount.do?'+param ; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject("1234test","2");
		var paramMap 	= null;	
		$('#account_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable) ;
	}
	
	function bindTable(result){	
		var list = $('#account_tbody').appendTable({
				 data		:	result.adminAccountList
				,totalCnt 	:	result.totalCnt
				,curPage 	: curPage
				,listCnt	: listCnt
				,column		:	['totalIndex', 'name', 'department', 'user_id', 'email']
				,link 		:  	{
					 1:{'class' : 'reg-pop-btn', 'key' : 'account_uid'} // index를 지정함
				}
				});
		list.event('modify', function(){
			modifyPopupWindow()
		});
		var page_ret = list.paging('#page_navi',{
			'totalCnt':$.fn.appendTable.defaults['totalCnt'], 
			'serverSideEvent' : 'page_list',
			'curPage' : curPage,
			'lstCnt' : listCnt});	
	}

	function modifyPopupWindow() {
		$('.reg-pop-btn').click(function() {				
			var url 		= ngmsUrl + '/adminAccount/'+$(this).data("key")+'.do' ; 
			var method 		= methodType.GET;
			var headerMap 	= new requestHeaderObject("1234test","2");
			var paramMap 	= null;			
			doRestFulApi(url, headerMap, method, paramMap, function(result){
				formSetting(result.adminAccount, 'mpopup_form');
				if(result.adminAccount.upd_date!==null) {
					$('.notice-essential').empty().append('<p><span>* </span> 표시는 필수 입력항목입니다. (최종변경시간 : ' + result.adminAccount.upd_date + ')</p> ');
				} else {
					$('.notice-essential').empty().append('<p><span>* </span> 표시는 필수 입력항목입니다.</p> ');
				}
				$('.modify-pop').show();
			}) ;
		});
	}
	
	function register() {
		var data = getFormData($('#popup_form'));
		
		// check
		if(!check_inputParameter(data)) {
			return false;
		}
		
		if(data.password === '') {
			alertMsg('비밀번호를 입력해주세요');
			return false;
		}
		
		if(!blank_pattern(data.password)) {
			alertMsg("현재 비밀번호는 공백없이 입력해주세요.");
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
		
		if(!isAllowUserId) {
			alertMsg('아이디 중복체크를 해주세요.');
			return false;
		}

		if(!confirm('입력하신 정보로 등록하시겠습니까?'))
			return false;
		
		var url = ngmsUrl + '/adminAccount.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "create"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alertMsg('입력하신 정보로 계정 등록하였습니다.');
				$(".reg-pop").hide();
				search_init();
			} else if(result.msg==='EXISTUSERID') {
				alertMsg('입력하신 아이디와 동일한 아이디가 존재합니다.');
			} else if(result.msg==='EXISTEMAIL') {
				alertMsg('입력하신 이메일과 동일한 이메일이 존재합니다.');
			}			
		}) ;
	}
	
	function isAllow() {
		var data = getFormData($('#popup_form'));
		
		if(data.user_id === '') {
			alertMsg('아이디를 입력해주세요');
			return false;
		}
		
		var url = ngmsUrl + '/checkUserId.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "update"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alertMsg('사용가능한 아이디입니다.');
				isAllowUserId = true;								
			} else if(result.msg==='EMPTYUSERID') {
				alertMsg('아이디를 입력해주세요.');
				isAllowUserId = false;
			} else if(result.msg==='EXISTUSERID') {
				alertMsg('입력하신 아이디와 동일한 아이디가 존재합니다.');
				isAllowUserId = false;
			}
			return false;
		});
	}
	
	function check_inputParameter(data) {
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
		return true;
	}
	
	function update() {
		var data = getFormData($('#mpopup_form'));
		
		// check
		if(!check_inputParameter(data)) {
			return false;
		}
		
		if(!confirm('입력하신 정보로 수정하시겠습니까?'))
			return false;
		
		var url = ngmsUrl + '/adminAccount.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "updateByAdmin"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alertMsg('입력하신 정보가 수정되었습니다.');	
				$(".modify-pop").hide();
				search_init();				
			} else if(result.msg==='EXISTEMAIL') {
				alertMsg('입력하신 이메일과 동일한 이메일이 존재합니다.');
			}
			return false;
		});
	}
	
	function remove() {
		var data = getFormData($('#mpopup_form'));
		
		if(!confirm('선택하신 사용자 계정을 삭제하시겠습니까?'))
			return false;
		
		var url = ngmsUrl + '/adminAccount.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "deleteByUid"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
			if(result.msg==='SUCCESS') {
				alert('선택하신 사용자 계정이 삭제하였습니다.');			
				$(".modify-pop").hide();
				search_init();				
			}			
		}) ;		
	}
</script>
				<div class="table-list">
					<form action="#" class="clearfix" name="form_list" id="form_list">
					<table class="ntype_tbl">
						<colgroup>
							<col width="7%">
							<col width="23%">
							<col width="23%">
							<col width="23%">
							<col width="23%">
						</colgroup>
						<thead>
							<tr class="first_tr">
								<th>번호</th>
								<th>이름</th>
								<th>소속</th>
								<th>아이디</th>
								<th>이메일</th>
							</tr>
						</thead>
						<tbody id="account_tbody">
						</tbody>
					</table>
					</form>

					<div class="form-actions">
						<button id="registerPopup" type="button" class="btn">등록</button>
					</div>

					<!--  pagination -->
					<div class="pn-wrap pn-single" id="page_navi">
					</div>
					<!--  // pagination -->
				</div>
				

	<div class="pop-wrap reg-pop"> <!-- reg-pop s -->
		<div class="pop-bg"></div>
		<div class="pop-up"> <!-- pop-up s -->
			<div class="pop-header clearfix">
				<h3 class="pop-title">신규 사용자 등록</h3>
				<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
			</div>
			<div class="pop-contents"> <!-- pop-contents -->
				<div class="pop-info"></div>
				<div class="form-wrap clearfix pb60">
					<h5 class="blind_position">신규 사용자 정보를 입력해주세요.</h5>
					<form id='popup_form' action="#" class="clearfix">
						<div class="group-box display-table">
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">이름<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="name" id="name" />
									</div>
								</div>
							</div>
							
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">소속<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="department" id="department" />
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">아이디<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="user_id" id="user_id" />
									</div>
									<button type="button" class="btn" id='isAllow'>중복확인</button>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">이메일<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="email" id="email" />
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">비밀번호 입력<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="password" class="form-input" name="password" id="password" />
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">비밀번호 재입력<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="password" class="form-input" name="password_confirm" id="password_confirm" />
									</div>
								</div>
							</div>						
						</div>
						<div class="form-actions text-center">
							<button type="button" class="btn" id='register'>등록</button>
						</div>
					</form>
				</div>
			</div> <!-- pop-contents e -->
		</div> <!-- pop-up e -->
	</div> <!-- reg-pop e -->
	
	<div class="pop-wrap modify-pop"> <!-- reg-pop s -->
		<div class="pop-bg"></div>
		<div class="pop-up"> <!-- pop-up s -->
			<div class="pop-header clearfix">
				<h3 class="pop-title">사용자 정보</h3>
				<button type="button" class="pop-close mclose-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
			</div>
			<div class="pop-contents"> <!-- pop-contents -->
				<div class="pop-info"></div>

				<div class="notice-essential"></div>
				<div class="form-wrap clearfix pb60">
					<h5 class="blind_position">사용자 정보를 입력해주세요.</h5>
					<form id='mpopup_form' action="#" class="clearfix">
						<input type="hidden" id="account_uid" name="account_uid" />
						<div class="group-box display-table">
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">이름<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="name" id="name" />
									</div>
								</div>
							</div>
							
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">소속<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="department" id="department"  />
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">아이디<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text"  readonly class="form-input" name="user_id" id="user_id"  readonly/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">이메일<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input type="text" class="form-input" name="email" id="email" />
									</div>
								</div>
							</div>					
						</div>
						<div class="form-actions text-center">
							<button type="button" class="btn" id='update'>수정</button>
							<button type="button" class="btn" id='delete'>삭제</button>
						</div>
					</form>
				</div>
			</div> <!-- pop-contents e -->
		</div> <!-- pop-up e -->
	</div> <!-- reg-pop e -->					