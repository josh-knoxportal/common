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

<style>
.n_infoboxXX{position:relative;z-index:100;padding:6px 10px;width:188px;text-align:center;font-family: Dotum,'돋움';}
</style>

<script>
/* [2016/08/18] UPGRADE */
	var canvasCurrentVehicleMap;
	var lastClickedVehicleId = null;

	/* */
	$(document).ready(function(){
		canvasCurrentVehicleMap = new LGSmartMap("#canvasCurrentVehicleMap", true, true, false);
		canvasCurrentVehicleMap.showableRoadMap(false);
		canvasCurrentVehicleMap.initViewScale([-370,-350], 1.15);
		/* */
		canvasCurrentVehicleMap.setOnStartZoomListener(function() {
			canvasCurrentVehicleMap.clearMapOnly();
		});
		/* */
		canvasCurrentVehicleMap.setOnEndZoomListener(function() {
			canvasCurrentVehicleMap.redrawCurrentDrivingMap();
		});
		
		/* 운행현황 */
		canvasCurrentVehicleMap.setOnCompletedDrivingListener(function(currentVehicleArray, unknownVehicleArray) {
			/* [2016/05/27] added : 위치를 알 수 없는 경우 */
// 			if (!atbsvg.util.isNull(unknownVehicleArray) && unknownVehicleArray.length > 0) {
// 				var html = '';
// 				for(var index = 0; index < unknownVehicleArray.length; index++) {
// 					if (index > 0) {
// 						html += '<br>';
// 					}
// 					html += unknownVehicleArray[index];
// 				}			
// 				showUnknownVehicleDialog(html);
// 			} else {
// 				closeUnknownVehicleDialog();
// 			}
			/* end of [2016/05/27] */
			
			/* [2016/06/24] */
			var page = 1;
			if (!atbsvg.util.isNullEmpty(lastClickedVehicleId)) {
				for(var index = 0; index < currentVehicleArray.length; index++) {
					if (lastClickedVehicleId == currentVehicleArray[index].vehicle_uid) {
						page = canvasCurrentVehicleMap.pageOfIndex(index);
						break;
					}
				}	
			}
			/* end of [2016/06/24] */
			
			/* [2016/08/18] added */
			$('#modalVehicleCount').html(''+currentVehicleArray.length);
			
			/* */
			var $body   = $('#tablebody_current_vehicle');
			var $paging = $('#paging_current_vehicle');
			
			canvasCurrentVehicleMap.redrawDrivingTableOnPage($body,$paging,'gotoNextTablePage',page);			
		});
		
		canvasCurrentVehicleMap.setOnDrivingTableBodyEntryListener(function(no,entry) {
			return makeVehicleTableRow(no,entry);
		});
		
		/* */
		canvasCurrentVehicleMap.setOnCompletedViolationHistoryListener(function(vehicle_uid, violationHistoryList) {		
			var html = makeViolationTableRow(violationHistoryList);
			$('#modalViolationHistoryTable').html(html);
		});
		
		/* */
		canvasCurrentVehicleMap.setOnHideTooltipListner(function() {
			$('#tooltip_vehicle').hide();
		});
		
		/* */
		canvasCurrentVehicleMap.setOnShowTooltipListner(function(entry,x,y) {
			console.log("setOnShowTooltipListner");
			
			var $tooltip = $("#tooltip_vehicle");
			$tooltip.css({ 'top':y, 'left':x });
			var is_violated = entry.last_is_over_speed || entry.last_on_restrict_area;
			
			$tooltip.removeClass(makeRemoveClassHandler(/^type/));
			
			var index = VEHICLE_TYPE.toIndex(entry.vehicle_type);
			$tooltip.addClass('type'+index);
			
			if (is_violated) {
				$tooltip.find('.n_infobox_title').addClass('caution');
			} else {
				$tooltip.find('.n_infobox_title').removeClass('caution');
			}
			
			if (is_violated) {
				var title = '[ 출입제한도로 진입 ]'
				if (entry.last_is_over_speed) {
					title = '[ 과속 ]'
				}
				$("#tooltup_vehicle_title").html(title);
			} else {
				$("#tooltup_vehicle_title").html("[ 정상운행 ]");
			}
			
			$("#tooltip_vehicle_no").html(""+entry.vehicle_no);
			$("#tooltip_device_no").html(""+entry.device_no);
			
			$tooltip.show();
		});
		
		// initUnknownVehicleDialog();
		
		/* 한번만 호출된다. */
		canvasCurrentVehicleMap.setOnCompletedLoadMapListener(function(roadSectionList) {
			periodicRefreshCurrentVehicle();
		});
		
		/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
		canvasCurrentVehicleMap.setOnCompletedFactoryCoordListener(function() {
			/* */
			canvasCurrentVehicleMap.redrawRoadMap();
		});
		
		/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
		canvasCurrentVehicleMap.requestFactoryCoord();		
		/*
		 * canvasCurrentVehicleMap.redrawRoadMap();
		 */
		/* end of [2016/05/14] */
	});

	
	/* [2016/05/18] */
	function periodicRefreshCurrentVehicle() {
		/* */
		canvasCurrentVehicleMap.requestCurrentDrivingList();
        setTimeout(periodicRefreshCurrentVehicle,1000);
	}
	
	/* */
	function clickedTableRow(vehicle_uid, $this) {
		canvasCurrentVehicleMap.showRippleOnVehicle(vehicle_uid);
		
		/* */
		var unknownVehicle = false;
		
		/* */
		var vehicle = canvasCurrentVehicleMap.getVehicleEntry(vehicle_uid);
		if (atbsvg.util.isNullEmpty(vehicle)) {
			vehicle = canvasCurrentVehicleMap.getVehicleUnknownEntry(vehicle_uid);
			if (atbsvg.util.isNullEmpty(vehicle)) {
				return ;
			}		
			unknownVehicle = false;
		}
		
		
		/* */
		lastClickedVehicleId = vehicle_uid;
		
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
		
		if (unknownVehicle == false) {
			canvasCurrentVehicleMap.requestViolationList(vehicle_uid);
		}
	}
	
	/* */
	function makeVehicleTableRow(no, entry) {
		if (atbsvg.util.isNull(entry)) return "<tr></tr>";
		
		var html = "";		
		var clickEvent = ' onclick="javascript:clickedTableRow(\''+entry.vehicle_uid+'\',this);"';
		
		if (entry.last_is_over_speed > 0 || entry.last_on_restrict_area > 0) {
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
		canvasCurrentVehicleMap.refreshDrivingTablePage(page);
	}
	
	/* [2016/05/13] added */
	function initUnknownVehicleDialog() {
		var $obj = $("#dialog_unknown_vehicle");
		
		/* [2016/05/27] */
		try {
			$obj.draggable();
		} catch(e) {
			/* ignore */
		}
		
		/* */
		$obj.find(".modal_close").click(function(e){
			e.preventDefault();
			$obj.hide();
		});		
	}
		
	/* [2016/05/13] added */
	function showUnknownVehicleDialog(html){
		var $obj = $("#dialog_unknown_vehicle");
		
		var cdate = new Date();
		$("#dialog_unknown_date").html(cdate.format("yyyy-MM-dd HH:mm:ss"));
		$("#dialog_unknown_vehicle_no").html(''+html);
		
		$obj.show();			
	}
	
	/* [2016/05/27] added */
	function closeUnknownVehicleDialog(){
		$("#dialog_unknown_vehicle").hide();			
	}
	
	
	/* [2016/05/13] added */
	function refreshVehicleMapUI() {
		canvasCurrentVehicleMap.viewReset();
		canvasCurrentVehicleMap.requestCurrentDrivingList();
	}
	
	/* [2016/05/31] added */
	function twinkleViolationSections(startUid, endUid) {
		canvasCurrentVehicleMap.twinkleRoadSection(startUid);
	}
	
</script>
<!--  HTML CONTENTS -->
			<div class="n_map_wrap">
					<div class="n_map_left">
						<div class="n_map_item">
							<img src="<c:url value="/resources/img"/>/img_n_map_item.png" alt="">
						</div>
						<div class="n_map_box" id="canvasCurrentVehicleMap" style="overflow: hidden;"></div>
						
						<!-- <div class="n_map_box_2"></div> -->	
						<div class="map_util">
							<a href="javascript:canvasCurrentVehicleMap.zoomIn();"><img src="<c:url value="/resources/img"/>/img_map_plus.png" alt="+"></a>
							<a href="javascript:canvasCurrentVehicleMap.zoomOut();"><img src="<c:url value="/resources/img"/>/img_map_minus.png" alt="+"></a>
							<a href="javascript:refreshVehicleMapUI()"><img src="<c:url value="/resources/img"/>/img_map_reload.png" alt="reload"></a>
						</div>
					</div>
					<div class="n_map_right">
						<div class="n_map_list">
							<p>운행 중 차량 : <strong id='modalVehicleCount'>0</strong>대</p>
							<div class="table-list">
								<table class="ntype_tbl work_tbl">
								<thead>
									<tr>
										<th>No</th>
										<th>차량번호</th>
									</tr>
								</thead>
								<tbody id='tablebody_current_vehicle'>
									<!-- empty -->
								</tbody>
							</table>
							</div>
				
							<!--  pagination -->
							<div id='paging_current_vehicle' class="pn-wrap pn-single"></div>
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
						
	<!-- 알림 : 퍼블리셔가 제공해주지 않은 모달 -->
	<div id="dialog_unknown_vehicle" class="modal">
		<div class="modal_header">
			<span>알림</span>
			<a href="#;" class="modal_close"><img src="<c:url value="/resources/img"/>/img_modal_close.png" alt="닫기"></a>
		</div>
		<div class="modal_body">
			<div class="car_loc_info">
				<p id="dialog_unknown_date"></p>
				<p>다음 차량의 위치정보를 알 수 없습니다.</p>
			</div>
			
			<div class="car_number">
				<span id="dialog_unknown_vehicle_no"></span>
			</div>

			<div class="modal_btn">
				<a href="#;" class="modal_close"><img src="<c:url value="/resources/img"/>/btn_confirm.png" alt="확인"></a>
			</div>
		</div>
	</div>
	<!-- // 알림 -->
