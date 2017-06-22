<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>


<!--  -->
<link href="<c:url value="/resources/css"/>/jquery-ui.css" rel="stylesheet" type="text/css">
<script src="<c:url value="/resources/js"/>/jquery-ui.min.js" type="text/javascript"></script>


<script src="<c:url value="/resources/js"/>/d3/d3.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/transformation-matrix-js/matrix.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/mathjs/math.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_core.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_extend.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_properties.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_for_ngms.js" type="text/javascript"></script>

<script>
/* [2016/08/19] UPGRADE */
	var canvasHistoryVehicleMap;
	
	$(document).ready(function(){
		canvasHistoryVehicleMap = new LGSmartMap("#canvasHistoryVehicleMap", true, true, true);
		canvasHistoryVehicleMap.setTraceDebugging();
		canvasHistoryVehicleMap.showableRoadMap(false);
		canvasHistoryVehicleMap.initViewScale([-370,-350], 1.15);
		canvasHistoryVehicleMap.setPageSetting(18,5);
		
		/* */
		canvasHistoryVehicleMap.setOnStartZoomListener(function() {
			canvasHistoryVehicleMap.clearMapOnly();
		});
		canvasHistoryVehicleMap.setOnEndZoomListener(function() {
			canvasHistoryVehicleMap.redrawLastDrivingHistory();
		});
	
		/* 이력조회 */
		canvasHistoryVehicleMap.setOnCompletedDrivingListener(function(currentVehicleArray) {
			/* [2016/08/18] added */
			$('#modalVehicleCount').html(''+currentVehicleArray.length);
			
			/* */
			var $body   = $('#tablebody_history_vehicle');
			var $paging = $('#paging_history_vehicle');
			
			canvasHistoryVehicleMap.redrawDrivingTable($body,$paging,'gotoNextTablePage');			
		});
		
		canvasHistoryVehicleMap.setOnDrivingTableBodyEntryListener(function(no,entry) {
			return makeVehicleTableRow(no,entry);
		});
		
		canvasHistoryVehicleMap.setOnCompletedViolationHistoryListener(function(vehicle_uid, violationHistoryList) {
			var html = makeViolationTableRow(violationHistoryList);
			
			if (atbsvg.util.isNullEmpty(html)) {
				$("#car_info .modal_body .cont2").hide();
			} else {
				$("#car_info .modal_body .cont2").show();
			}
			
			$('#modalViolationHistoryTable').html(html);
		});
		canvasHistoryVehicleMap.setOnHideTooltipListner(function() {
			$('#tooltip_vehicle').hide();
		});
		
		canvasHistoryVehicleMap.setOnShowTooltipListner(function(entry,x,y) {
			var $tooltip = $("#tooltip_vehicle");
			$tooltip.css({ 'top':y, 'left':x });
			var is_violated = entry.had_over_speed || entry.had_on_restrict_area;
			
			$tooltip.removeClass(makeRemoveClassHandler(/^type/));
			
			var index = VEHICLE_TYPE.toIndex(entry.vehicle_type);
			$tooltip.addClass('type'+index);
			
			if (is_violated) {
				$tooltip.find('.n_infobox_title').addClass('caution');
			} else {
				$tooltip.find('.n_infobox_title').removeClass('caution');
			}
			
			if (is_violated) {
				$("#tooltup_vehicle_title").html('[ 이상운행 ]');
			} else {
				$("#tooltup_vehicle_title").html("[ 정상운행 ]");
			}
			
			$("#tooltip_vehicle_no").html(""+entry.vehicle_no);
			$("#tooltip_device_no").html(""+entry.device_no);
			
			$tooltip.show();
		});
		
		
		/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
		canvasHistoryVehicleMap.setOnCompletedFactoryCoordListener(function() {
			/* */
			canvasHistoryVehicleMap.showFactoryElements();
		});
	
		/* */
		canvasHistoryVehicleMap.requestFactoryCoord();	
		
		
		
		/* */
		var date = new Date();
		var today = date.format('yyyy-MM-dd');
        $('#datepicker1').val(today);
        $('#datepicker2').val(today);
        
        /* [2016/08/25] modified : 2 --> 365 */
        var daysToAdd = 365;
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
		    onSelect: function (selected) {
				var dtMax = new Date(selected);
				/* */
				dtMax.setDate(dtMax.getDate() + daysToAdd); 
				var dd = dtMax.getDate();
				var mm = dtMax.getMonth() + 1;
				var y = dtMax.getFullYear();
				var dtFormatted = y + '-' + mm + '-'+ dd;
				$("#datepicker2").datepicker("option", "minDate", selected);
				$("#datepicker2").datepicker("option", "maxDate", dtFormatted);
		    }
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
		    onSelect: function (selected) {
				/* from을 제약할 필요는 없다.
				 
	 				var dtMax = new Date(selected);	
					dtMax.setDate(dtMax.getDate() - daysToAdd); 
					var dd = dtMax.getDate();
					var mm = dtMax.getMonth() + 1;
					var y = dtMax.getFullYear();
					var dtFormatted = y + '-' + mm + '-'+ dd;
					$("#datepicker1").datepicker("option", "minDate", dtFormatted);
					$("#datepicker1").datepicker("option", "maxDate", selected); 
				*/
		    }
		   });		
		
		/* END OF 이력조회 */	
	});

	
	/* */
	function clickedTableRow(vehicle_uid, $this) {
		canvasHistoryVehicleMap.requestDrivingSection(vehicle_uid);
		
		/* */
		var vehicle = canvasHistoryVehicleMap.getVehicleEntry(vehicle_uid);
		if (atbsvg.util.isNullEmpty(vehicle)) {
			return ;
		}		
		/* */
		$('#modalVehicleNo').html(''+vehicle.vehicle_no);
		$('#modalPhoneNo').html('단말기 번호 : '+vehicle.device_no);
		$('#modalVehicleComeInDate').html('입차시간 : '+vehicle.come_in_date);
		
		if (vehicle.last_is_over_speed > 0 || vehicle.last_on_restrict_area > 0) {
			$("#car_info .modal_body .cont2").show();
		} else {
			$("#car_info .modal_body .cont2").hide();
		}
		
		/* 
		 * [2016/08/18] 퍼블에서 제공한 함수
		 * function carinfo_modal_open(id, $this) { 
		 */
		var $obj = $("#car_info");	 
		/*
		 * $(".n_map_right .table-list tr").removeClass("active");	
		 */
		var list_top = $(".n_map_right .table-list").offset().top,
			top = $($this).offset().top,
			cal_top = (top + $obj.outerHeight()) - (list_top + $(".n_map_right .table-list").outerHeight());

		if(cal_top > 1){
			$obj.css({'top':cal_top + ($obj.outerHeight()/2) + 40 }).show();
		}else{
			$obj.css({'top':top - ($obj.outerHeight()/2) - 60 }).show();
		}		
		$obj.find(".modal_close").click(function(e){
			e.preventDefault();
			/* */
			lastClickedVehicleId = null;
			/*
			 * $(".n_map_right .table-list tr").removeClass("active");
			 */
			$obj.hide();
		});
		
		/*
		 * $("tr", $obj).removeClass("active");
		 * $($this).parent().parent().addClass("active");
		 */
		 
		/* END OF [2016/08/18] */	
		canvasHistoryVehicleMap.requestViolationList(vehicle_uid);
	}
	
	/* */
	function makeVehicleTableRow(no, entry) {
		if (atbsvg.util.isNull(entry)) return "<tr></tr>";
		
		var html = "";		
		var clickEvent = ' onclick="javascript:clickedTableRow(\''+entry.vehicle_uid+'\',this);"';
		
		if (entry.had_over_speed > 0 || entry.had_on_restrict_area > 0) {
			html += "<tr class='active'"+clickEvent+">";
		} else {
			html += "<tr"+clickEvent+">";
		}
		html += "<td>"+no+"</td>";
		html += "<td>"+entry.vehicle_no+"</td>";
		
		return html;
	}
	
	/* */
	function makeViolationTableRow(violationHistoryList) {
		if (atbsvg.util.isNull(violationHistoryList) || violationHistoryList.length == 0) {	
			return '';
		}
		
		var html = '';
		/* */
		html += '<colgroup>';
		html += '<col style="width:50px">';
		html += '<col style="width:auto">';
		html += '</colgroup>';
		
		var size = violationHistoryList.length;
		for(var idx = 0; idx < size; idx++) {
			var entry = violationHistoryList[idx];
			
			/* [2016/08/26] avg_speed --> peak_speed */
			html += '<tr>';
			if (entry.violation_type == VIOLATION_OVER_SPEED) {
				var anchor = '<a class="hovergray"  href="javascript:twinkleViolationSections(\''+entry.begin_section_uid+'\',\''+entry.end_section_uid+'\')">과속</a>';
				html += '<td><strong>'+anchor+'</strong></td>';
				var speed = ""+entry.peak_speed+" km/h";
				html += '<td>'+speed+'<br>';
				
			
			} else if (entry.violation_type == VIOLATION_ON_RESTRICT) {
				var anchor = '<a class="hovergray" href="javascript:twinkleViolationSections(\''+entry.begin_section_uid+'\',\''+entry.end_section_uid+'\')">출입제한</a>';
				html += '<td><strong>'+anchor+'</strong></td>';
				html += '<td>';
			}		
			html += ''+entry.come_in_date+"<br>~ "+entry.go_out_date+"</td></tr>";	
		}
		
		return html;
	}
	
	/* */
	function gotoNextTablePage(page) {
		canvasHistoryVehicleMap.refreshDrivingTablePage(page);
	}
	
	
	/* [2016/05/13] added */
	function refreshVehicleMapUI() {
		canvasHistoryVehicleMap.viewReset();
		requestVehicleHistory();
	}
	
	/* [2016/05/13] added */
	function requestVehicleHistory() {
		var from = $('#datepicker1').val();
		if (atbsvg.util.isNullEmpty(from)) {
			alert('검색일 조건을 입력하십시오.');
			return ;
		}
		var to = $('#datepicker2').val();
		if (atbsvg.util.isNullEmpty(to)) {
			alert('검색일 조건을 입력하십시오.');
			return ;
		}	
		var violation_type = $('#select_violation_type').val();
		var	vehicle_no = $('#input_vehicle_no').val().trim();
		
		canvasHistoryVehicleMap.requestHistoryDrivingList(from,to,violation_type,vehicle_no);
	}
	
	/* [2016/05/13] added */
	function setDatePicker1() {
		$('#datepicker1').datepicker('show');
	}
	
	/* [2016/05/13] added */
	function setDatePicker2() {
		$('#datepicker2').datepicker('show');
	}
		
	/* [2016/05/31] added */
	function twinkleViolationSections(startUid, endUid) {
		canvasHistoryVehicleMap.twinkleRoadSection(startUid);
	}
	
	/* */
	function excelDownLoad(){
		var paramObj={};
		
		var from = $('#datepicker1').val();
		if (atbsvg.util.isNullEmpty(from)) {
			alert('검색일 조건을 입력하십시오.');
			return ;
		}
		var to = $('#datepicker2').val();
		if (atbsvg.util.isNullEmpty(to)) {
			alert('검색일 조건을 입력하십시오.');
			return ;
		}	
		var violation_type = $('#select_violation_type').val();
		var	vehicle_no = $('#input_vehicle_no').val().trim();
		
		paramObj['from'] = from;
		paramObj['to'] = to;
		paramObj['target'] = 'vehicle_hist';
		paramObj['violation_type'] = violation_type;
		paramObj['vehicle_no'] = vehicle_no;
		
		var param 	= getJsonObjToGetParam(paramObj);
	    
	    $(location).attr("href", '/vehicle/excel/vehicleHist_excel.do?'+param);
	}
	
</script>
<!--  HTML CONTENTS -->
				<div class="n_map_wrap">
					<div class="n_map_left">
						<div class="n_map_item">
							<img src="<c:url value="/resources/img"/>/img_n_map_item2.png" alt="">
						</div>
						<div class="n_map_box" id="canvasHistoryVehicleMap" style="overflow: hidden;"></div>
						
						<div class="map_util">
							<a href="javascript:canvasHistoryVehicleMap.zoomIn();"><img src="<c:url value="/resources/img"/>/img_map_plus.png" alt="+"></a>
							<a href="javascript:canvasHistoryVehicleMap.zoomOut();"><img src="<c:url value="/resources/img"/>/img_map_minus.png" alt="+"></a>
							<a href="javascript:refreshVehicleMapUI()"><img src="<c:url value="/resources/img"/>/img_map_reload.png" alt="reload"></a>
						</div>
					</div>
					<div class="n_map_right">
						<div class="n_map_search">
							<div class="input-box">
								<!-- no : hasDatepicker -->
								<input id="datepicker1" class="form-input" type="text" name="name">
								<button type="button" class="calendar-btn blind" onclick="javascript:setDatePicker1();" title="날짜를 선택해주세요.">날짜선택</button>
							</div>

							<div class="input-box">
								<!-- no : hasDatepicker -->
								<input id="datepicker2" class="form-input" type="text" name="name">
								<button type="button" class="calendar-btn blind"onclick="javascript:setDatePicker2();" title="날짜를 선택해주세요.">날짜선택</button>
							</div>

							<div class="input-box">
								<select id="select_violation_type" class="form-select">
									<option value="0">전체운행차량</option>											
									<option value="1">과속차량</option>
									<option value="2">출입제한위반차량</option>
								</select>
							</div>

							<div class="input-box">
								<input id="input_vehicle_no"  class="form-input carno-input" type="text" name="name" placeholder="차량번호 입력하세요">
							</div>

							<div class="text-center">
								<a href="javascript:requestVehicleHistory()"><img src="<c:url value="/resources/img"/>/btn_search.gif" alt="검색"></a>
								<a href="javascript:excelDownLoad()"><img src="<c:url value="/resources/img"/>/btn_excel.gif" alt="엑셀 저장"></a>
							</div>
						</div>

						<div class="n_map_list">
							<p>조회 차량 : <strong id='modalVehicleCount'>0</strong>대</p>
							<div class="table-list">
								<table class="ntype_tbl work_tbl">
								<thead>
									<tr>
										<th>No</th>
										<th>차량번호</th>
									</tr>
								</thead>
								<tbody id='tablebody_history_vehicle'>
									<!-- empty -->
								</tbody>
							</table>
							</div>
				
							<!--  pagination -->
							<div id='paging_history_vehicle' class="pn-wrap pn-single"></div>
							<!--  /pagination -->
							
							
							<!-- 차량 정보 모달 -->
							<div id="car_info" class="modal">
								<div class="modal_header">
									<span id="modalVehicleNo"></span>
									<a href="#;" class="modal_close"><img src="<c:url value="/resources/img"/>/img_modal_close.png" alt="닫기"></a>
								</div>
								<div class="modal_body">
									<div class="cont1">
										<p><strong>[입차]</strong></p>
										<p id='modalPhoneNo'></p>
										<p id='modalVehicleComeInDate'></p>
									</div>
									
									<div class="cont2">
										<p><strong>[이상운행]</strong></p>
										<table id='modalViolationHistoryTable'>
											<!-- empty -->
										</table>
									</div>
								</div>
							</div>
							<!-- // 차량 정보 모달 -->
						</div>
					</div>
				</div>

	<!-- tooltip -->
	<div id='tooltip_vehicle' class="modal n_infobox type1">
		<div class="n_infobox_title" id="tooltup_vehicle_title"></div>
		<div class="n_infobox_list">
			<ul>
				<li>차량번호 : <span id="tooltip_vehicle_no"></span></li>
				<li>단말기번호 : <span id="tooltip_device_no"></span></li>
			</ul>
		</div>
	</div>