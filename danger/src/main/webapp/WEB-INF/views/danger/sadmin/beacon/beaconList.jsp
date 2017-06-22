<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link href="<c:url value="/resources/css"/>/jquery-ui.css" rel="stylesheet">
<script src="<c:url value="/resources/js"/>/jquery-ui.js" type="text/javascript"></script>

<script src="<c:url value="/resources/js"/>/d3/d3.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/transformation-matrix-js/matrix.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/mathjs/math.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_core.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_extend.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_properties.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_for_ngms.js" type="text/javascript"></script>
<script type="text/javascript">

	var orderId = 'name';
	var orderBy = 'asc';
	var listCnt = 15;
	var curPage = 1;
	
	$(document).ready(function() {
		init();
	});

	//초기화
	function init() {
		search_init();

		$('.mclose-btn').click(function(){
			$(this).data('key','');
			$('.last_modi_time').children().remove();
			resetSearchData($('#mpopup_form input'));			
			$(this).closest(".pop-wrap").hide();
		});
		
		$('#update').click(function() {
			update();
		});
				
		$('#delete').click(function() {
			remove();
		});
		
		$('#excel_downloadBtn').click(function() {
			excelDownLoad();
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
		paramObj['order_type']            = 'name';
		paramObj['order_desc_asc']        = "asc";
		search(paramObj);
	}
	
	function search(paramObj) {
// 		console.log(paramObj);
		var param 		= getJsonObjToGetParam(paramObj);
		var url 		= wwmsUrl + '/beacon.do?'+param ; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject("1234test","2");
		var paramMap 	= null;		
		
		$('#tablelist_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable) ;		
	}
	
	function bindTable(result) {
		var is_activated = {0:'비활성', 1:'활성'};
// 		console.log(result);
		
		assigned_append(result);
		
		var list = $('#tablelist_tbody').appendTable({
				 data		:	result.beaconDangerList
				,totalCnt 	:	result.totalCnt
				,curPage : curPage
				,listCnt : listCnt
				,column		:	['totalIndex', 'name', 'factory_name', 'zone_name', 'is_activated', 'battery_ox', 'upd_date', 'link_text' ]
				,cdType		:   {4 : is_activated}
// 				,defaultVal : 	{2 : '-'}
				,editCols	:	{7 : {'text' : '설정',  'class' : 'btn_tbl', 'key' : 'beacon_uid'}}
		
		});
		list.event('modify', function(){ modifyPopupWindow() });
		var page_ret = list.paging('#page_navi',{'totalCnt':$.fn.appendTable.defaults['totalCnt'], 'serverSideEvent' : 'page_list', 'curPage':curPage, 'lstCnt' : listCnt});
	}

	function assigned_append(result) {
		$('#beaconTotalCount').empty().append(result.beaconActivate.total_count); 
		$('#beaconNotActivated').empty().append(result.beaconActivate.not_activated);
		$('#beaconActivated').empty().append(result.beaconActivate.activated);
	}

	function modifyPopupWindow() {
		$('.btn_tbl').click(function() {	
// 			console.log($(this).data("key"));
			
			var url 		= wwmsUrl + '/beacon/'+$(this).data("key")+'.do' ; 
			var method 		= methodType.GET;
			var headerMap 	= new requestHeaderObject("1234test","2");
			var paramMap 	= null;			
			doRestFulApi(url, headerMap, method, paramMap, function(result){
// 				console.log(result);
				if(result.beaconDangerVo.upd_date!==null) {
					$('.last_modi_time').empty().append('(최종변경시간 : ' + result.beaconDangerVo.upd_date + ')');	
				}				
				formSetting(result.beaconDangerVo, 'mpopup_form');
				getFactoryList('mod_select_factory_list', result.beaconDangerVo.zone_uid);
				$('.modify-pop').show();
			}) ;
		});
	}

	function update() {
		var data = getFormData($('#mpopup_form'));
// 		console.log(data);
		
		if(data.zone_uid === '') {
			alertMsg('관리존를 선택해 주세요');
			return false;
		}
		
		if(!confirm('입력하신 정보로 수정하시겠습니까?'))
			return false;
		
		var url = wwmsUrl + '/beacon.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "update"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
// 			console.log(result);			
			if(result.msg==='SUCCESS') {
				alertMsg('입력하신 정보가 수정되었습니다.');	
				$(".modify-pop").hide();
				search_init();				
			}			
		}) ;
	}
	
	function remove() {
		var data = getFormData($('#mpopup_form'));
// 		console.log(data);
		
		if(!confirm('비콘을 삭제하시겠습니까?'))
			return false;
		
		var url = wwmsUrl + '/beacon.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "deleteByUid"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
// 			console.log(result);			
			if(result.msg==='SUCCESS') {
				alertMsg('비콘이 삭제되었습니다.');		
				$(".modify-pop").hide();
				search_init();				
			}			
		}) ;		
	}

	function excelDownLoad() {
		var paramObj = {};
		paramObj['target'] = 'danger_beacon_list';
		var param = getJsonObjToGetParam(paramObj);		
		$(location).attr('href', '/danger/excel/device_beacon_excel.do?'+param);
	}
	
	function getFactoryList(selectId, settingVal) {
		var url = wwmsUrl + '/code_factoryzone/zone.do' ;
		var method = 'GET';
		var headerMap = {};
		var paramMap = null;

		doRestFulApi(url, headerMap, method, paramMap,
			function (result) {
    			if (!atbsvg.util.isNull(result) && !atbsvg.util.isNull(result.codeFactoryZoneList)) {
    				var $select = $("#"+selectId);
					var factoryZoneList = result.codeFactoryZoneList;
					var html = '<option value="" checked>관리 존</option>';

					if (!atbsvg.util.isNull(factoryZoneList) && factoryZoneList.length > 0) {
						var size = factoryZoneList.length;
						for(var index = 0; index < size; index++) {
							var item = factoryZoneList[index];
								html += '<option type="'+item.type+'" value="'+item.uid+'">'+item.name+'</option>'; 
						}
					}
					$select.html(html);

					if(settingVal){
						$("#"+selectId).val(settingVal);
					}
    			}
			}) ;
	}	
</script>
				<div class="beacon_summary">
					<ul>
						<li>
							<div class="beacon_tit">
								<span>전체 합계</span>
								<span id="beaconTotalCount" class="count"></span>
							</div>
						</li>
						<li>
							<div class="beacon_tit">
								<span>활성 비콘</span>
								<span id="beaconActivated" class="count"></span>
							</div>
						</li>
						<li>
							<div class="beacon_tit">
								<span>비활성 비콘</span>
								<span id="beaconNotActivated" class="count"></span>
							</div>
						</li>
					</ul>
				</div>
				
				<div class="excel-btn-box">
					<button id="excel_downloadBtn" type="button" class="excel-btn"><img src="<c:url value="/resources/img"/>/excel_down.png" alt=" ">엑셀 다운로드</button>
				</div>

				<form id="search_form" name="search_form"></form>
				<div class="table-list">
					<table class="ntype_tbl">
						<colgroup>
							<col style="width:8%;">
							<col style="width:15%" />
							<col style="width:20%" />
							<col style="width:15%" />
							<col style="width:10%" />
							<col style="width:10%" />
							<col style="width:auto" />
							<col style="width:10%" />
						</colgroup>
						<thead>
							<tr class="first_tr">
								<th>No</th>
								<th onclick="javascript:orderbyEvent(this, 'name')">Beacon <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'factory_name')">단위공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'zone_name')">Zone <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'is_activated')">상태 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'battery')">배터리 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
								<th onclick="javascript:orderbyEvent(this, 'upd_date')">업데이트 일시 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
								<th>설정</th>
							</tr>
						</thead>
						<tbody id='tablelist_tbody'></tbody>
					</table>

					<!--  pagination -->
					<div class="pn-wrap pn-single" id="page_navi"></div>
				</div>
				
	<div class="pop-wrap pop_tmn modify-pop"> <!-- pop_tmn -->
		<div class="pop-bg"></div>
		<div class="pop-up"> <!-- pop-up s -->
			<div class="pop-header clearfix">
				<h3 class="pop-title">비콘설정</h2>
				<button type="button" class="pop-close mclose-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
			</div>
			<div class="pop-contents"> <!-- pop-contents -->

				<p class="last_modi_time"></p>

				<div class="form-wrap prev-box clearfix">
					<form id="mpopup_form" action="#" class="clearfix">
					<input type="hidden" id="beacon_uid" name="beacon_uid"/>
						<div class="group-box display-table">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">Minor<span></span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" id="minor" name="minor" readonly="readonly" disabled="disabled"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">비콘명<span></span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" id="name" name="name" readonly="readonly"  disabled="disabled"/>
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">신호주기<span></span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" id="signal_period" name="signal_period"/>
									</div> ms
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">Tx Powers<span></span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" id="txpower" name="txpower"/>
									</div> db
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">설명<span></span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" id="txpower" name="description" maxlength="120"/>
<!-- 										<textarea rows="2" cols="40" id="description" name="description"></textarea> -->
									</div>
								</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">관리존<span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="select-box full">
										<select class="sel" id="mod_select_factory_list" name="zone_uid">
										</select>
									</div>
								</div>
							</div>				
						</div>
						<div class="form-actions text-center">
							<button id='delete' type="button" class="btn mr5 confirm-btn">삭제</button>   
							<button id='update' type="button" class="btn confirm-btn">저장</button> 
						</div>
					</form>
				</div>

			</div> <!-- pop-contents e -->
		</div> <!-- pop-up e -->
	</div> <!-- pop_tmn e -->					