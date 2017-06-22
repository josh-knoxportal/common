<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	var orderId = 'reg_date';
	var orderBy = 'desc';
	var listCnt = 15;
	var curPage = 1;
	
	$(document).ready(function(){
		$('#search').click(function(){
			event.preventDefault();
			search_type();
		});
		
		$('#remove').click(function(){
			event.preventDefault();
			remove();
		});

		$('#register').click(function() {
			event.preventDefault();
			$(location).attr("href", contextPath + "/vehicle/admin/accountRegister.do");
		});

		$('.enterSearchClass').keydown(function(event) {
			if (event.which === 13) { //enter
				page_list(1);
				return false;
			}
		});

		$('#all_checkBox').change(function() {
			allCheckBox("all_checkBox", "checked_uid_str");
		});

		init();
	});
	
	function init() {
		search_init();
	}

	//server side page
	function page_list(page){
		var paramObj = {};
		curPage = page;
		
		$.extend( paramObj, getSearchParam($('#search_form')) );
		paramObj['page_index'] = ( page -1 ) * listCnt;
		paramObj['record_count_per_page']  = listCnt;
// 		paramObj['order_type'] = orderId;
// 		paramObj['order_desc_asc'] = orderBy;
		
		search(paramObj);
	}
	
// 	function orderbyEvent(img, orderName) {
// 		var $img = $(img).children("a").children("img");
// 		orderId = orderName;

// 		$src = $img.prop('src');
// 		if($src.indexOf('_asc.png') > 0 ){
// 			orderBy="desc";
// 			$img.prop("src", $src.replace("_asc.png","_desc.png"));
// 		}
// 		else{
// 			orderBy="asc";
// 			$img.prop("src", $src.replace("_desc.png","_asc.png"));
// 		}

// 		page_list(1);
// 	}
	
	function search_init() {
		var paramObj = {};
		curPage      = 1;

		$.extend( paramObj, getSearchParam($('#search_form')) );
		paramObj['page_index']   = 0;
		paramObj['record_count_per_page']    = listCnt;
// 		paramObj['order_type']     = 'name';
// 		paramObj['order_desc_asc'] = "asc";

		if(!check_searchword(paramObj.search_filter)) {
			alertMsg('검색어는 공백,기호,특수문자를 입력할 수 없습니다.');
			return false;
		}
		
		search(paramObj);
	}
	
	function search(paramObj) {
		var param = getJsonObjToGetParam(paramObj);
		var url = ngmsUrl + '/adminAccount.do?' + param;
		var method = methodType.GET;
		var headerMap = new requestHeaderObject("1234test", "2");
		var paramMap = null;
		$('#account_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable);	
	}	
	
	function search_type() {
		search_init();
	}
	
	function bindTable(result) {
		var system_type = { '1' : '시스템관리자', '2' : '일반관리자' };
		var list = $('#account_tbody').appendTable({
			data : result.adminAccountList,
			totalCnt : result.totalCnt,
			curPage : curPage,
			listCnt : listCnt,
			column : [ 'totalIndex', 'user_id', 'system_type', 'name', 'email', 'reg_date', 'check_box' ],
			cdType : { 2 : system_type },
			editCols : { 6 : { 'name' : 'checked_uid_str', 'key' : 'account_uid', 'param' : [ 'account_uid', 'user_id', '' ], 'data' : [ 'check', 'is_admin' ] } }
		});

		var page_ret = list.paging('#page_navi', {
			'totalCnt' : $.fn.appendTable.defaults['totalCnt'],
			'serverSideEvent' : 'page_list',
			'curPage' : curPage,
			'lstCnt' : listCnt
		});
		filterColum();
	}

	function filterColum() {
		$("input[name=checked_uid_str]").each(function() {
			if ($(this).data("check") == 1) {
				$(this).attr("disabled", "disabled");
			}
		});
	}

	function remove() {
		var data = getFormData($('#form_list'));

		if (data.checked_uid_str === undefined) {
			alertMsg('계정 삭제 대상을 선택하여주세요');
			return false;
		}

		if (!confirm('선택하신 아이디에 대해 계정 삭제를 하시겠습니까?')) {
			return false;
		}

		var url = ngmsUrl + '/adminAccount.do';
		var method = 'POST';
		var headerMap = {
			"access_token" : "1234test",
			"request_type" : "2",
			"opcode" : "vehicledelete"
		};
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result) {
			if(result.msg==='SUCCESS') {
				search_init();								
			} else if(result.msg==='NOAUTH') {
				$(location).attr("href", contextPath + "/vehicle/admin/login.do");
			}			
		});
	}
</script>
				<div class="search-wrap display-table">
					<div class="table-row">
						<div class="table-cell">
							<div class="form-wrap clearfix">
								<h5 class="blind_position">검색어를 입력해 주세요</h5>
								<form action="#" class="clearfix" id="search_form" name="search_form">
									<div class="form-group clearfix">
										<span class="search-text">SEARCH</span><!-- 텍스트로 변경 -->
										<div class="select-box mr25"> 
											<select class="form-select" id="search_type" name="search_type" title="선택하세요">
												<option value="search_all">전체</option>
												<option value="search_id">아이디</option>
												<option value="search_name">이름</option>
											</select>
										</div>
										<div class="input-box">
											<input class="form-input enterSearchClass" type="text" name="search_filter" id="search_filter" max="11" />
										</div>
										<button type="button" class="btn" id="search" name="search">검색</button> 
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>

				<div class="search_tbl_gap" style="padding:20px 0 0;"></div>

				<div class="table-list">
					<form action="#" class="clearfix" name="form_list" id="form_list">
					<table class="table-style data-table">
						<colgroup>
							<col width="7%">
							<col width="18%">
							<col width="19%">
							<col width="19%">
							<col width="20%">
							<col width="10%">
							<col width="7%">
						</colgroup>
						<thead>
							<tr>
								<th>번호</th>
								<th>아이디</th>
								<th>관리권한</th>
								<th>이름</th>
								<th>이메일</th>
								<th>등록일</th>
								<th>선택 <input type="checkbox" id="all_checkBox"/></th>
							</tr>
						</thead>
						<tbody id="account_tbody">
						</tbody>
					</table>
					</form>

					<div class="form-actions">
						<button id="remove"   type="button" class="btn mr10">계정 삭제</button><button id="register" type="button" class="btn">계정 등록</button>
					</div>

					<!--  pagination -->
					<div class="pn-wrap pn-single" id="page_navi">
					</div>
					<!--  // pagination -->
				</div>