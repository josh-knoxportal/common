<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>


<!--  -->
<script src="<c:url value="/resources/js"/>/d3/d3.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/transformation-matrix-js/matrix.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/mathjs/math.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_core.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_extend.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_properties.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_for_ngms.js" type="text/javascript"></script>
    
<script> 
	/* [2016/08/18] UPGRADE */
	var	canvasRestrictMap;
	
	/* */
	$(document).ready(function(){
		canvasRestrictMap = new LGSmartMap("#canvasRestrictMap", true, true, false);
		canvasRestrictMap.enabledRoadDragDrop(true);
		canvasRestrictMap.initViewScale([-420,-385], 1.275);
		canvasRestrictMap.zoomEnabled(false);
		canvasRestrictMap.dragEnabled(false);
		
		/* */
		canvasRestrictMap.setOnCompletedLoadMapListener(function(roadSectionList) {
			/*
			 * no action
			 */
		});
		
		canvasRestrictMap.setOnClickedRoadSectionListener(function(x,y) {
			canvasRestrictMap.unselectAll();
			var position = canvasRestrictMap.relocationDialogPosition(x,y);
			selectedLinesToChangeRestrict(position[0],position[1]);
			return true;
		});
		
		canvasRestrictMap.setOnMultiSelectedRoadSectionListener(function(x,y) {
			var position = canvasRestrictMap.relocationDialogPosition(x,y);
			selectedLinesToChangeRestrict(position[0],position[1]);
		});
		
		canvasRestrictMap.setOnSuccessUpdateListener(function() {
			canvasRestrictMap.showAlert("성공적으로 요청이 처리되었습니다.");
			var vehicleType = canvasRestrictMap.getCurrentVehicleType();
			canvasRestrictMap.redrawRoadMap(vehicleType);
		});
		
		/* select vehicle type */
		var html = '';
		for(var idx = 0; idx < VEHICLE_TYPE.size(); idx++) {
			var items = VEHICLE_TYPE.indexAt(idx);
			var selected = '';
			if (idx == 0) selected = "selected";
			html += '<option value="'+items.code+'" '+selected+'>'+items.name+'</option>';
		}
		$('#selectVehicleType').html(html);
	
		
		/* [2016/08/18] */
		$('#selectVehicleType').change(function() {
			var vehicle_type = $(this).val();
			if (atbsvg.util.isNull(vehicle_type)) return;		
			canvasRestrictMap.redrawRoadMap(vehicle_type);
		});
		
		/* */
		canvasRestrictMap.redrawRoadMap(VEHICLE_TYPE.indexAt(0).code);
	});

	
	/* */
	function changeRestrictPopupCondition(is_restrict_area) {
		$('input:radio[name="radio_restrict_area"]').removeAttr("checked");
		if (is_restrict_area == 1) {
			$('input:radio[name="radio_restrict_area"]:input[value=1]').prop("checked", true);
		} else {
			$('input:radio[name="radio_restrict_area"]:input[value=0]').prop("checked", true);
		}
	}
	
	
	function selectedLinesToChangeRestrict(left, top) {
		/* left, top : reserved */
		if (canvasRestrictMap.isSingleRoadSelection()) {
			var entry = canvasRestrictMap.getSelectdSingleRoadSection();		
			changeRestrictPopupCondition(entry.is_restrict_area);
		} else {
			changeRestrictPopupCondition(0);
		}		
	}
	
	function updateRestictLines() {
		/* */
		canvasRestrictMap.unselectAll();			
		/* */
		var is_restrict_area = $('input[name="radio_restrict_area"]:radio:checked').val();	
		if (canvasRestrictMap.isSingleRoadSelection()) {
			canvasRestrictMap.requestUpdateRestrictLineSingleSelected(is_restrict_area);
		} else {
			canvasRestrictMap.requestUpdateRestrictLineMultiSelected(is_restrict_area);
		}	
	}	
</script>

<!--  HTML CONTENTS -->
				<div class="n_map_wrap">
					<div class="n_map_left">
						<div class="n_map_box_2" id="canvasRestrictMap" style="overflow: hidden;">
							<!-- map -->
						</div>
						<div class="n_map_box_desc">
							<img src="<c:url value="/resources/img"/>/n_map_box_2_desc.gif" alt="">
						</div>
					</div>
					<div class="n_map_right">
						<div class="n_map_search n_cartype_search">
							<p class="car_type_srch">차량유형 선택</p>

							<div class="input-box">
								<select id='selectVehicleType' class="sel3">
									<option value='delivery' selected>정기 납품</option>
									<option value='logistics'>정기 물류</option>
									<option value='work'>정기 작업/공사</option>
									<option value='normal'>정기 일반/방문</option>
									<option value='dailydelivery'>일일 납품</option>
									<option value='dailylogistics'>일일 물류</option>
									<option value='dailywork'>일일 작업/공사</option>
									<option value='dailynormal'>일일 일반/방문</option>
									<option value='etc'>기타</option>
								</select>
							</div>

	


							<div class="input_gap"></div>

							<div class="input-box input_rule_setting">
								<strong>규칙설정</strong>
								<ul>
									<li>
										<label><input type="radio" name="radio_restrict_area" value="1"> <span>출입제한도로 설정</span></label>
									</li>
									<li>
										<label><input type="radio" name="radio_restrict_area" value="0" checked> <span>출입제한도로 해제</span></label>
									</li>
								</ul>
							</div>

							<div class="text-center">
								<a href="javascript:updateRestictLines();"><img src="<c:url value="/resources/img"/>/btn_confirm.png" alt="확인"></a>
							</div>
						</div>

					</div>
				</div>
