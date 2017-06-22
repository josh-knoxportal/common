<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

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
	var orderId = 'starting_date';
	var orderBy = 'desc';
	var listCnt = 15;
	var curPage = 1;
	$(document).ready(function() {
		var date = new Date();
		var today = date.format('yyyy-MM-dd');
        $('#datepicker1').val(today);
        $('#datepicker2').val(today);
        
		$("#datepicker1").datepicker({
		    changeMonth: true, 
		    changeYear: true,
		    dateFormat: "yy-mm-dd",
		    showMonthAfterYear:true,
		    monthNames: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		    monthNamesShort: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		    dayNames: ["일", "월", "화", "수", "목", "금", "토"], // For formatting
		    dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"], // For formatting
		    dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
		    beforeShowDay: noBefore
		   });
		
		$("#datepicker2").datepicker({
		    changeMonth: true, 
		    changeYear: true,
		    dateFormat: "yy-mm-dd",
		    showMonthAfterYear:true,
		    monthNames: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		    monthNamesShort: ["1","2","3","4","5","6","7","8","9","10","11","12"],
		    dayNames: ["일", "월", "화", "수", "목", "금", "토"], // For formatting
		    dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"], // For formatting
		    dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
		    beforeShowDay: function noStartingDayBefore(date){
				var tdate = new Date(Date.parse($("#datepicker1").val())-1*1000*60*60*24);
				if (date < tdate)
					return [false]; 
				return [true];
			}
		   });
		
		$('#searchBtn').click(function() {
			page_list(1);
		});
		$('#excel_downloadBtn').click(function() {
			excelDownLoad();
		});
		
		init();
	});//jquery 끝
	
	//초기화
	function init() {
		initSearch();
	}
	
	function initSearch(pageObj) {
		var paramObj={};
		
		var from = $('#datepicker1').val();
		if (atbsvg.util.isNullEmpty(from)) {
			alertMsg('검색일 조건을 입력하십시오.');
			return ;
		}
		var to = $('#datepicker2').val();
		if (atbsvg.util.isNullEmpty(to)) {
			alertMsg('검색일 조건을 입력하십시오.');
			return ;
		}
		
		if(pageObj){
			paramObj = pageObj;
		}
		paramObj['from'] = from;
		paramObj['to'] = to;
		paramObj['limit_count'] = listCnt;
		paramObj['order_type'] = orderId;
		paramObj['order_desc_asc'] = orderBy;
		
		search(paramObj);
	}
	
	//검색메소드
	function search(paramObj) {
		var param 		= getJsonObjToGetParam(paramObj);
		var url 		= wwmsUrl + '/workStatus.do?'+param ; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType); // new requestHeaderObject("1234test","2", {추가적인 파람});
		var paramMap 	= null;		
		
		$('#workStatus_tbody').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable) ;
	}
	
	function bindTable(result) {
		var totalCnt = 0;
		
		var list = $('#workStatus_tbody').appendTable({
				 data		:	result.workStatusList
				,totalCnt 	:	totalCnt = result.totalcnt
				,curPage : curPage
				,listCnt : listCnt
				,column		:	['totalIndex', 'work_day', 'worker_name', 'device_no', 'parter_name' , 'factory_name', 'zone_name'
				       		 	 , 'last_factory_name', 'last_zone_name', 'work_type_img', 'work_no', 'name', 'starting_date', 'complete_date'
				       		 	 , 'worker_count'
				       		 	 ]
				,defaultVal : 	{2 : '-', 3 : '-', 4 : '-', 5 : '-', 6 : '-', 7 : '위치미확인', 8 : '위치미확인', 9 : '-', 11 : '-', 12 : '-', 13 : '-'}
		});	
		var page_ret = list.paging('#page_navi',{'totalCnt':$.fn.appendTable.defaults['totalCnt'], 'serverSideEvent' : 'page_list', 'curPage':curPage, 'lstCnt' : listCnt});
		
	}
	
	//server side page
	function page_list(page){
		var paramObj = {}
		curPage = page;
		paramObj['limit_offset'] = ( page -1 ) * listCnt;
		
		initSearch(paramObj);
	}
	
	function setDatePicker1() {
		$('#datepicker1').datepicker('show');
	}
	
	function setDatePicker2() {
		$('#datepicker2').datepicker('show');
	}
	
	function orderbyEvent(orderName, img) {
		var $img = $(img).children("a").children("img");
		orderId = orderName;
		/*
		var allImgTh = $(img).parent("tr").children("th");
		
		for(i=0; i < allImgTh.length; i++){
			var thElement = allImgTh[i];
			$imgSrc = $(thElement).children("a").children("img").prop('src');
			
			if($(img).text() !=  $(thElement).text()){
				$(thElement).children("a").children("img").prop("src", $imgSrc.replace("_desc.png","_asc.png"));
			}
		}
		*/
		$src = $img.prop('src');
		if($src.indexOf('_asc.png') > 0 ){
			orderBy="desc";
			$img.prop("src", $src.replace("_asc.png","_desc.png"));
		}
		else{
			orderBy="asc";
			$img.prop("src", $src.replace("_desc.png","_asc.png"));
		}
		
		initSearch();
	}
	
	function excelDownLoad(){
		var paramObj={};
		
		var from = $('#datepicker1').val();
		var to = $('#datepicker2').val();
		
		paramObj['from'] = from;
		paramObj['to'] = to;
		paramObj['target'] = 'work_status';
		
		var param 	= getJsonObjToGetParam(paramObj);
	    
	    $(location).attr("href", '/danger/excel/workStatus_excel.do?'+param);
	}
	
</script>

<div class="tab-style">
	<ul class="tab-box member-tab clearfix">
		<li class="active">
			<a href="<spring:url value="/admin/workStatus.do"/>" class="tab-btn"><span class="">작업자별 현황</span></a>
		</li>
		<li>
			<a href="<spring:url value="/admin/workIssue.do"/>" class="tab-btn"><span class="">출입감지 현황</span></a>
		</li>
	</ul>

	<div class="tab-wrap">
		<div class="tab-contents target">
			<!-- search-wrap -->
			<div class="search-wrap display-table">
				<div class="table-row">
					<div class="table-cell">
						<div class="form-wrap clearfix">
							<h5 class="blind_position">검색어를 입력해 주세요</h5>
							<form action="#" class="clearfix">
								<div class="form-group clearfix">
									<span class="search-text">관리일자 </span>
									<div class="input-box calendar-box">
										<input id="datepicker1" class="form-input" type="text" name="name" onchange="changeStartingDate('datepicker1','datepicker2')">
										<button type="button" class="calendar-btn blind" onclick="javascript:setDatePicker1()" title="날짜를 선택해주세요.">날짜선택</button>
									</div>
									<span class="dash"> - </span>
									<div class="input-box calendar-box">
										<input id="datepicker2" class="form-input" type="text" name="name">
										<button type="button" class="calendar-btn blind" onclick="javascript:setDatePicker2()" title="날짜를 선택해주세요.">날짜선택</button>
									</div>
									<button id="searchBtn" type="button" class="btn">검색</button> 
									<button id="excel_downloadBtn" type="button" class="btn">엑셀 저장</button> 
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<!-- // search-wrap -->
			<!-- 
			<div class="excel-btn-box">
				<button type="button" class="excel-btn" id="excel_downloadBtn"><img src="<c:url value="/resources/img"/>/excel_down.png" alt=" ">엑셀 다운로드</button>
			</div>
			 -->
			<div class="search_tbl_gap" style="padding:20px 0 0;"></div>

			<table class="ntype_tbl work_tbl">
				<colgroup>
					<col style="width:40px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:80px;">
					<col style="width:70px;">
				</colgroup>
				<thead>
					<tr class="first_tr">
						<th rowspan="2">No</th>
						<th rowspan="2">관리일자</th>
						<th colspan="3">작업자정보</th>
						<th colspan="2">인가구역설정</th>
						<th colspan="2">최종위치</th>
						<th colspan="6">작업정보</th>
					</tr>
					<tr>
						<th onclick="javascript:orderbyEvent('worker_name', this)">작업자 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('device_no', this)">폰번호 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('parter_name', this)">업체명 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('factory_name', this)">공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('zone_name', this)">Zone <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('last_factory_name', this)">공장 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('last_zone_name', this)">Zone <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>

						<th onclick="javascript:orderbyEvent('work_type_name', this)">작업유형 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('work_no', this)">작업번호 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('name', this)">작업명 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('starting_date', this)">작업시작 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('complete_date', this)">작업종료 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_desc.png" alt=""></a></th>
						<th onclick="javascript:orderbyEvent('worker_count', this)">작업자수 <a href="#;" class="ord"><img src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
					</tr>
				</thead>
				<tbody id="workStatus_tbody">
					
				</tbody>
			</table>
			
			<div class="work_type">
				<img src="<c:url value="/resources/img"/>/img_work_type.gif" alt="">
			</div>

			<!--  pagination -->
			<div class="pn-wrap pn-single" id="page_navi"></div>
			<!--  // pagination -->
		</div>
	</div>
</div>