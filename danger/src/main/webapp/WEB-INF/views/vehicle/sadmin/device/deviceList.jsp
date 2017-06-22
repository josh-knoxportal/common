
<%@ page language="java" contentType="text/html;charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript">
	var orderId = 'reg_date';
	var orderBy = 'desc';
	var listCnt = 15;
	var curPage = 1;

	$(document).ready(function() {
		$('#search').click(function() {
			search_init();
		});
		
		$('#search_popup').click(function() {
			search_init_block();
		});

		init();
	});//jquery 끝

	//초기화
	function init() {
		search_init();

		$('.close-btn').click(function() {
			$(this).data('key', '');
			resetSearchData($('#popup_form input'));
			$(this).closest(".pop-wrap").hide();
		});

		$('#list').click(function() {
			$(this).data('key', '');
			resetSearchData($('#popup_form input'));
			$(this).closest(".pop-wrap").hide();
		});

		$('#registerPopup').click(function() {
			$(this).data('key', '');
			resetSearchData($('#popup_form input'));
			$('.reg-pop').show();
		});

		$('#blockPopup').click(function() {
			$(this).data('key', '');
			resetSearchData($('#popup_form input'));
			$('.block-pop').show();
			search_init_block();
		});

		$('.mclose-btn').click(function() {
			$(this).data('key', '');
			resetSearchData($('#mpopup_form input'));
			$(this).closest(".pop-wrap").hide();
			search_init();
		});

		$('#mlist').click(function() {
			$(this).data('key', '');
			resetSearchData($('#mpopup_form input'));
			$(this).closest(".pop-wrap").hide();
		});

		$('#release').click(function() {
			release();
		});

		$('#block').click(function() {
			block();
		});

		$('#unblock').click(function() {
			unblock();
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

		$('#all_checkBox').change(function() {
			allCheckBox("all_checkBox", "checked_uid_str");
		});
		
		$('#all_checkBox_block').change(function() {
			allCheckBox("all_checkBox_block", "checked_uid_str_block");
		});

		$('.enterSearchClass').keydown(function(event) {
			if (event.which === 13) { //enter
				search_init();
				return false;
			}
		});

		$('.numbersOnly').keyup(function() {
			if (this.value != this.value.replace(/[^0-9\.]/g, '')) {
				this.value = this.value.replace(/[^0-9\.]/g, '');
			}
		});
	}

	// paging
	function page_list(page) {
		var paramObj = {};
		curPage = page;

		$.extend(paramObj, getSearchParam($('#search_form')));
		paramObj['page_index'] = (page - 1) * listCnt;
		paramObj['record_count_per_page'] = listCnt;
		paramObj['order_type'] = orderId;
		paramObj['order_desc_asc'] = orderBy;

		search(paramObj);
	}

	// column ordering 
	function orderbyEvent(img, orderName) {
		var $img = $(img).children("a").children("img");
		orderId = orderName;

		$src = $img.prop('src');
		if ($src.indexOf('_asc.png') > 0) {
			orderBy = "desc";
			$img.prop("src", $src.replace("_asc.png", "_desc.png"));
		} else {
			orderBy = "asc";
			$img.prop("src", $src.replace("_desc.png", "_asc.png"));
		}

		page_list(1);
	}

	function search_init() {
		var paramObj = {};
		curPage = 1;

		$.extend(paramObj, getSearchParam($('#search_form')));
		var search_filter = $('#select_search_type').val();
		paramObj[search_filter] = $('#search_filter').val().trim();
		paramObj['page_index'] = 0;
		paramObj['record_count_per_page'] = listCnt;
		paramObj['order_type'] = orderId;
		paramObj['order_desc_asc'] = orderBy;
		
		search(paramObj);
	}

	//검색메소드
	function search(paramObj) {
		var param = getJsonObjToGetParam(paramObj);
		var url = ngmsUrl + '/device.do?' + param;
		var method = methodType.GET;
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		
		$('#device_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable);
	}

	function bindTable(result) {
		var is_entered = {
			0 : '',
			1 : '입차 중',
			2 : '출차 중'			
		};

		var is_company_cd = {
			0 : '개인',
			1 : '법인'
		};
		
		assigned_append(result);

		var list = $('#device_tbody').appendTable(
				{
					data : result.naviDeiviceList,
					totalCnt : result.totalCnt,
					curPage : curPage,
					listCnt : listCnt,
					column : [ 'totalIndex', 'device_no', 'is_company', 'assigned_vehicle_no', 'assigned_vehicle_type', 'assigned_vehicle_regdate', 'is_entered', 'check_box' ],
					link : {
						1 : {
							'class' : 'reg-pop-btn hoverred',
							'key' : 'device_uid'
						}
					},
					cdType : {
						2 : is_company_cd, 
						6 : is_entered
					} // index를 지정함
					,
					defaultVal : {
						3 : '',
						4 : '',
						5 : '',
						6 : ''
					} // index를 지정함
					,
					editCols : {
						7 : {
							'name' : 'checked_uid_str',
							'key' : 'device_uid',
							'param' : [ 'device_uid', 'device_no' ]
// 							,'data' : [ 'check', 'is_assigned' ]
						}
					}
				});
		list.event('modify', function() {
			modifyPopupWindow()
		});
		var page_ret = list.paging('#page_navi', {
			'totalCnt' : $.fn.appendTable.defaults['totalCnt'],
			'serverSideEvent' : 'page_list',
			'curPage' : curPage,
			'lstCnt' : listCnt
		});
		filterColum();
	}
	
	// paging
	function page_list_block(page) {
		var paramObj = {};
		curPage = page;

		$.extend(paramObj, getSearchParam($('#popup_search_form')));
		paramObj['page_index'] = (page - 1) * listCnt;
		paramObj['record_count_per_page'] = listCnt;
		paramObj['order_type'] = orderId;
		paramObj['order_desc_asc'] = orderBy;

		searchBlockDevice(paramObj);
	}

	// column ordering 
	function orderbyEvent_block(img, orderName) {
		var $img = $(img).children("a").children("img");
		orderId = orderName;

		$src = $img.prop('src');
		if ($src.indexOf('_asc.png') > 0) {
			orderBy = "desc";
			$img.prop("src", $src.replace("_asc.png", "_desc.png"));
		} else {
			orderBy = "asc";
			$img.prop("src", $src.replace("_desc.png", "_asc.png"));
		}

		page_list_block(1);
	}

	function search_init_block() {
		var paramObj = {};
		curPage = 1;

		$.extend(paramObj, getSearchParam($('#popup_search_form')));
		paramObj['search_filter_popup'] = $('#search_filter_popup').val().trim();
		paramObj['page_index'] = 0;
		paramObj['record_count_per_page'] = listCnt;
		paramObj['order_type'] = orderId;
		paramObj['order_desc_asc'] = orderBy;
		searchBlockDevice(paramObj);
	}
	
	function searchBlockDevice(paramObj) {
		var param = getJsonObjToGetParam(paramObj);
		var url = ngmsUrl + '/blockdevice.do?' + param;
		var method = methodType.GET;
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
		var paramMap = null;
		
		$('#block_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindBlockTable);
	}
	
	function bindBlockTable(result) {
		
		var list = $('#block_tbody').appendTable(
				{
					data : result.naviDeiviceList,
					totalCnt : result.totalCnt,
					curPage : curPage,
					listCnt : listCnt,
					column : [ 'device_no', 'upd_date', 'check_box' ],
					defaultVal : {
						0 : '-',
						1 : '-'
					} // index를 지정함
					,
					editCols : {
						2 : {
							'name' : 'checked_uid_str_block',
							'key' : 'device_uid',
							'param' : [ 'device_uid', 'device_no' ],
							'data' : [ 'check', 'is_block' ]
						}
					}
				});
		var page_ret = list.paging('#page_navi', {
			'totalCnt' : $.fn.appendTable.defaults['totalCnt'],
			'serverSideEvent' : 'page_list',
			'curPage' : curPage,
			'lstCnt' : listCnt
		});
	}

	function filterColum() {
		$("input[name=checked_uid_str]").each(function() {
			if ($(this).data("check") == 1) {
				$(this).attr("disabled", "disabled");
			}
		});
	}

	function assigned_append(result) {
		var tdHtml = '';
		var tbody = $('#assigned_tbody');
		tbody.children().remove();
		tdHtml = '<tr>';
		tdHtml += '<td>' + result.naviDeviceAssigned.device_total_count
				+ '</td>';
		tdHtml += '<td>' + result.naviDeviceAssigned.assigned + '</td>';
		tdHtml += '<td>' + result.naviDeviceAssigned.not_assigned + '</td>';
		tbody.append(tdHtml + '</tr>');
	}

	function modifyPopupWindow() {
		$('.reg-pop-btn').click(function() {
			var url = ngmsUrl + '/device/' + $(this).data("key") + '.do';
			var method = methodType.GET;
			var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
			var paramMap = null;
			doRestFulApi(url, headerMap, method, paramMap, function(result) {
				formSetting(result.naviDevice, 'mpopup_form');
				$('.modify-pop').show();
			});
		});
	}

	function register() {
		var data = getFormData($('#popup_form'));

		if (!checkInputValue(data)) {
			return false;
		}

		var url = ngmsUrl + '/device.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType, {
			"opcode" : "create"
		});
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('단말기 등록 완료');
				$(".reg-pop").hide();
				search_init();
			} else if (result.msg === 'EXISTDEVICENO') {
				alertMsg('동일한 단말기 번호가 이미 등록되어 있습니다. 단말기 번호를 확인해 주세요.');
				return false;
			}
		});
	}

	function update() {
		var data = getFormData($('#mpopup_form'));

		if (!checkInputValue(data)) {
			return false;
		}

		if (!confirm('단말기 정보를 수정하시겠습니까?')) {
			return false;
		}

		var url = ngmsUrl + '/device.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType, {
			"opcode" : "update"
		});
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('단말기 정보를 수정하였습니다.');
				$(".modify-pop").hide();
				search_init();
			} else if (result.msg === 'EXISTDEVICENO') {
				alertMsg('입력하신 단말기 번호와 동일한 단말기 번호가 존재합니다.');
				return false;
			}
		});
	}

	function checkInputValue(data) {
		if (data.device_no === '') {
			alertMsg('단말기 번호를 입력해주세요');
			return false;
		}

		if (data.device_no.length < 10) {
			alertMsg('단말기 번호를 자리수를 확인해주세요');
			return false;
		}

		if (data.model === '') {
			alertMsg('기종를 입력해주세요');
			return false;
		}
		
		if(!check_searchword(data.model)) {
			alertMsg('기종 입력은<br>공백, 기호, 특수문자는<br>입력할 수 없습니다.');
			return false;
		}
		
		return true;
	}

	function remove() {
		var data = getFormData($('#mpopup_form'));
		if (data.is_assigned == 1) {
			alertMsg('사용중인 단말은 삭제할 수 없습니다.');
			return false;
		}
		if (!confirm('선택하신 단말기를 삭제하시겠습니까?')) {
			return false;
		}

		var url = ngmsUrl + '/device.do';
		var method = 'POST';
		var headerMap = new requestHeaderObject(g_accessToken, g_requestType, {
			"opcode" : "release"
		});
		var paramMap = null;
		doRestFulApi(url, headerMap, method, data, function(result) {
			if (result.msg === 'SUCCESS') {
				alertMsg('선택하신 단말를 삭제하였습니다.');
				$(".modify-pop").hide();
				search_init();
			}
		});
	}

	function release() {
		var data = getFormData($('#form_list'));

		if (data.checked_uid_str) {
			if (!confirm('선택하신 단말기를 등록해제 하시겠습니까?')) {
				return false;
			}

			var url = ngmsUrl + '/device.do';
			var method = 'POST';
			var headerMap = new requestHeaderObject(g_accessToken,
					g_requestType, {
						"opcode" : "release"
					});
			var paramMap = null;
			doRestFulApi(url, headerMap, method, data, function(result) {
				if (result.msg === 'SUCCESS') {
					alertMsg('선택하신 단말기를 등록해제 하였습니다.');
					$("input:checkbox[id='all_checkBox']").attr("checked",false);
					search_init();
				}
			});
		} else {
			alertMsg('해제할 단말기를 선택하세요.');
			return false;
		}

	}

	function block() {
		var data = getFormData($('#form_list'));

		if (data.checked_uid_str) {
			if (!confirm('선택하신 단말기를 차단 하시겠습니까?')) {
				return false;
			}

			var url = ngmsUrl + '/device.do';
			var method = 'POST';
			var headerMap = new requestHeaderObject(g_accessToken,
					g_requestType, {
						"opcode" : "block"
					});
			var paramMap = null;
			doRestFulApi(url, headerMap, method, data, function(result) {
				if (result.msg === 'SUCCESS') {
					alertMsg('선택하신 단말기를 차단 하였습니다.');
					search_init();
				}
			});
		} else {
			alertMsg('차단할 단말기를 선택하세요.');
			return false;
		}

	}
	
	function unblock() {
		var data = getFormData($('#popup_form_list'));
		
		if (data.checked_uid_str_block) {
			if (!confirm('선택하신 단말기를 차단 해제 하시겠습니까?')) {
				return false;
			}

			var url = ngmsUrl + '/device.do';
			var method = 'POST';
			var headerMap = new requestHeaderObject(g_accessToken,
					g_requestType, {
						"opcode" : "block"
					});
			var paramMap = null;
			doRestFulApi(url, headerMap, method, data, function(result) {
				if (result.msg === 'SUCCESS') {
					alertMsg('선택하신 단말기를 차단 해제 하였습니다.');
					search_init_block();
				}
			});
		} else {
			alertMsg('차단 해제할 단말기를 선택하세요.');
			return false;
		}

	}
</script>

<div class="search-wrap display-table">
	<div class="table-row">
		<div class="table-cell">
			<div class="form-wrap clearfix">
				<h5 class="blind_position">검색어를 입력해 주세요</h5>
				<form id="search_form" name="search_form" action="#"
					class="clearfix">
					<div class="form-group clearfix">
						<select id="select_search_type" class="sel">
							<option value="device_no">단말기 번호</option>
							<option value="assigned_vehicle_no">차량번호</option>
						</select>
						<div class="input-box mr5" style="width:250px;">
							<input class="form-input enterSearchClass" type="text" id="search_filter" name="search_filter" maxlength="11" />
						</div>
						<button id="search" name="search" type="button" class="btn">검색</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<div class="search_tbl_gap" style="padding: 20px 0 0;"></div>

<div class="table-list">
	<form action="#" class="clearfix" name="form_list" id="form_list">
		<table class="ntype_tbl work_tbl">
			<colgroup>
				<col width="8%">
				<col width="15%">
				<col width="14%">
				<col width="14%">
				<col width="14%">
				<col width="14%">
				<col width="14%">
				<col width="7%">
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th onclick="javascript:orderbyEvent(this, 'device_no')">단말기번호<a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
					<th onclick="javascript:orderbyEvent(this, 'is_company')">단말구분<a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
					<th onclick="javascript:orderbyEvent(this, 'assigned_vehicle_no')">차량번호<a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
					<th onclick="javascript:orderbyEvent(this, 'assigned_vehicle_type')">차량유형<a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
					<th onclick="javascript:orderbyEvent(this, 'assigned_vehicle_regdate')">차량등록일 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
					<th onclick="javascript:orderbyEvent(this, 'is_entered')">출입상태 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
					<th>선택 <input type="checkbox" id="all_checkBox" /></th>
				</tr>
			</thead>
			<tbody id="device_tbody"></tbody>
		</table>
	</form>

	<div class="form-actions">
		<button id="registerPopup" type="button" class="btn mr10">단말 신규 등록</button>
		<button id="release" type="button" class="btn mr10">단말 등록 해제</button>
		<button id="block" type="button" class="btn mr10">이용 차단</button>
		<button id="blockPopup" type="button" class="btn">차단 목록</button>
	</div>
</div>

<!--  pagination -->
<div class="pn-wrap pn-single" id="page_navi"></div>
<!--  // pagination -->

<div class="pop-wrap reg-pop">
	<!-- reg-pop s -->
	<div class="pop-bg"></div>
	<div class="pop-up">
		<!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">단말기 신규등록</h3>
			<button type="button" class="pop-close close-btn">
				<img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기">
			</button>
		</div>
		<div class="pop-contents">
			<!-- pop-contents -->
			<div class="form-wrap clearfix pb60">
				<h5 class="blind_position">단말기 관리 정보를 입력해주세요.</h5>
				<form id='popup_form' action="#" class="clearfix">
					<div class="pop-info"></div>

					<div class="notice-essential">
						<p>
							<span>* </span> 표시는 필수 입력항목입니다.
						</p>
					</div>
					<div class="group-box display-table">
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">구분 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="select-box full">
									<select class="sel" id="is_company" name="is_company">
										<option value="0">개인폰</option>
										<option value="1">법인폰</option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">단말기 번호 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input numbersOnly" type="text"
										name="device_no" id="device_no" maxlength="11" />
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">기종 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="model" id="model" />
								</div>
							</div>
						</div>
					</div>
					<div class="form-actions text-right">
						<button type="button" class="btn mr5" id='list'>닫 기</button>
						<button type="button" class="btn" id='register'>확 인</button>
					</div>
				</form>
			</div>
		</div>
		<!-- pop-contents e -->
	</div>
	<!-- pop-up e -->
</div>
<!-- reg-pop e -->

<div class="pop-wrap modify-pop">
	<!-- reg-pop s -->
	<div class="pop-bg"></div>
	<div class="pop-up">
		<!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">단말기 정보 수정</h3>
			<button type="button" class="pop-close mclose-btn">
				<img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기">
			</button>
		</div>
		<div class="pop-contents">
			<!-- pop-contents -->
			<div class="form-wrap clearfix pb60">
				<h5 class="blind_position">단말기 관리 정보를 입력해주세요.</h5>
				<form id='mpopup_form' action="#" class="clearfix">
					<div class="pop-info"></div>

					<div class="notice-essential">
						<p>
							<span>* </span> 표시는 필수 입력항목입니다.
						</p>
					</div>
					<input type="hidden" id="device_uid" name="device_uid" value="1" />
					<input type="hidden" id="is_assigned" name=is_assigned value="1" />
					<div class="group-box display-table">
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">구분 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="select-box full">
									<select class="sel" id="is_company" name="is_company">
										<option value="0">개인폰</option>
										<option value="1">법인폰</option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">단말기 번호 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="input-box"> 
									<input class="form-input numbersOnly" type="text" name="device_no" id="device_no" maxlength="11" readonly disabled />
								</div>
							</div>
						</div>
						<div class="form-group table-row clearfix">
							<div class="table-cell title-cell">
								<label class="control-label">기종 <span>*</span></label>
							</div>
							<div class="table-cell">
								<div class="input-box">
									<input class="form-input" type="text" name="model" id="model" />
								</div>
							</div>
						</div>
					</div>
					<div class="form-actions text-right">
						<button type="button" class="btn mr5" id='mlist'>닫 기</button>
						<button type="button" class="btn mr5" id='update'>수 정</button>
						<button type="button" class="btn" id='delete'>삭 제</button>
					</div>
				</form>
			</div>
		</div>
		<!-- pop-contents e -->
	</div>
	<!-- pop-up e -->
</div>
<!-- reg-pop e -->

<div class="pop-wrap block-pop">
	<!-- block-pop s -->
	<div class="pop-bg"></div>
	<div class="pop-up">
		<!-- pop-up s -->
		<div class="pop-header clearfix">
			<h3 class="pop-title">차단 사용자</h3>
			<button type="button" class="pop-close mclose-btn"><img src="/resources/img/pop-close.png" alt="팝업닫기"></button>
		</div>
		<div class="pop-contents">
			<!-- pop-contents -->
			<div class="blank"></div>
			<div class="search-wrap display-table">
				<div class="table-row">
					<div class="table-cell">
						<div class="form-wrap clearfix">
							<h5 class="blind_position">검색어를 입력해 주세요</h5>
							<form id="popup_search_form" name="popup_search_form" action="#" class="clearfix">
								<div class="form-group clearfix">
									<span class="search-text">단말기 번호 검색</span>
									<div class="input-box mr5" style="width:250px;">
										<input class="form-input enterSearchClass numbersOnly" type="text" id="search_filter_popup" name="search_filter_popup" maxlength="11" />
									</div>
									<button id="search_popup" name="search_popup" type="button" class="btn">검색</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<div class="search_tbl_gap" style="padding:20px 0 0;"></div>
			<!-- // search-wrap -->

			<div class="form-wrap prev-box clearfix">
				<div class="fixed_head_tbl">
					<form action="#" name="popup_form_list" id="popup_form_list">
						<table class="ntype_tbl ntype_tbl_head">
							<colgroup>
								<col style="width:40%;">
								<col style="width:40%;">
								<col style="width:20%;">
							</colgroup>
							<thead>
								<tr class="first_tr">
									<th onclick="javascript:orderbyEvent_block(this, 'device_no')">차단번호 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
									<th onclick="javascript:orderbyEvent_block(this, 'upd_date')">차단일 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
									<th>선택 <input type="checkbox" id="all_checkBox_block" /></th>
								</tr>
							</thead>
						</table>
						<div class="fixed_head_body">
							<table class="ntype_tbl ntype_tbl_body">
								<colgroup>
									<col style="width:40%;">
									<col style="width:42.3%;">
									<col style="width:auto">
								</colgroup>
								<thead style="display: none;">
									<tr class="first_tr">
										<th onclick="javascript:orderbyEvent_block(this, 'device_no')">차단번호 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
										<th onclick="javascript:orderbyEvent_block(this, 'upd_date')">차단일 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
										<th>선택 <input type="checkbox" id="all_checkBox_block" /></th>
									</tr>
								</thead>
								<tbody id="block_tbody"></tbody>
							</table>
						</div>
					</form>

				</div>
				<div class="form-actions text-right">
					<button type="button" class="btn mr5" id='unblock'>차단 해제</button>
				</div>

			</div>
		</div>
		<!-- pop-contents e -->
	</div>
	<!-- pop-up e -->
</div>
<!-- block-pop e -->