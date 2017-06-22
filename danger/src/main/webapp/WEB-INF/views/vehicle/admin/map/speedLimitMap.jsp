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
	var canvasSpeedMap;
	
	/* */
	$(document).ready(function(){
		canvasSpeedMap = new LGSmartMap("#canvasSpeedMap", true, true, false);
		canvasSpeedMap.showSpeedLimitOnMap(true);
		canvasSpeedMap.enabledRoadDragDrop(true);

		canvasSpeedMap.initViewScale([-420,-385], 1.275);
		
		canvasSpeedMap.zoomEnabled(false);
		canvasSpeedMap.dragEnabled(false);
		
		/* */
		canvasSpeedMap.setOnCompletedLoadMapListener(function(roadSectionList) {
			/*
			 * no action
			 */ 
		});
		
		canvasSpeedMap.setOnClickedRoadSectionListener(function(x,y) {
			canvasSpeedMap.unselectAll();
			var position = canvasSpeedMap.relocationDialogPosition(x,y);
			selectedLinesToChangeSpeed(position[0],position[1]);
			return true;
		});
		
		canvasSpeedMap.setOnMultiSelectedRoadSectionListener(function(x,y) {
			var position = canvasSpeedMap.relocationDialogPosition(x,y);
			selectedLinesToChangeSpeed(position[0],position[1]);
		});
		
		canvasSpeedMap.setOnSuccessUpdateListener(function() {
			canvasSpeedMap.showAlert("성공적으로 요청이 처리되었습니다.");
			$('#in_current_speed_limit').val('');
			canvasSpeedMap.redrawRoadMap();
		});
		
		/* */
		canvasSpeedMap.redrawRoadMap();
	});

	
	/* */	
	function selectedLinesToChangeSpeed(left, top) {
		/* left, top : reserved */
		if (canvasSpeedMap.isSingleRoadSelection()) {		
			var entry = canvasSpeedMap.getSelectdSingleRoadSection();
			$('#in_current_speed_limit').val(entry.speed_limit);
		} else {
			$('#in_current_speed_limit').val('');
		}		
		$('#in_new_speed_limit').val($("#in_new_speed_limit option:first").val());
	}
	
	
	/* */
	function updateSpeedLimitAll(){
		var newspeed = $("#in_all_speed_limit").val();
		if (atbsvg.util.isNullEmpty(newspeed)) {
			canvasSpeedMap.showAlert('제한속도 변경값이 유효하지 않습니다.');
			return ;
		}
	
		canvasSpeedMap.requestUpdateSpeedLimitAll(newspeed);
	}
	
	/* */
	function updateSpeedLimit() {
		var newspeed = $('#in_new_speed_limit').val();
		if (atbsvg.util.isNullEmpty(newspeed)) {
			canvasSpeedMap.showAlert('제한속도 변경값이 유효하지 않습니다.');
			return ;
		}
		
		canvasSpeedMap.unselectAll();	
		/* */
		if (canvasSpeedMap.isSingleRoadSelection()) {
			canvasSpeedMap.requestUpdateSpeedLimitSingleSelected(newspeed);
		} else {
			canvasSpeedMap.requestUpdateSpeedLimitMultiSelected(newspeed);
		}		
	}
	
</script>

<!--  HTML CONTENTS -->
				<div class="n_map_wrap">
					<div class="n_map_left">
						<div class="n_map_box_2" id="canvasSpeedMap" style="overflow: hidden;">
							<!-- map -->
						</div>
						<div class="n_map_box_desc">
							<img src="<c:url value="/resources/img"/>/n_map_box_2_desc.gif" alt="">
						</div>
					</div>
					<div class="n_map_right">
						<div class="n_map_search n_cartype_search">
							<p class="car_limitkm_srch">제한속도 설정</p>
							
							<div class="input-box car_limitkm_setting">
								<strong>도로 전체 제한속도 설정</strong>
								<div class="input-box">
									<!--  
										<input class="form-input" type="number" name="name" id='in_all_speed_limit'>
									-->
									<select id='in_all_speed_limit' class="sel3">
										<option value='15' selected>15Km</option>
										<option value='25'>25Km</option>
										<option value='40'>40Km</option>
									</select>
								</div>
								<div class="text-center">
									<a href="javascript:updateSpeedLimitAll();""><img src="<c:url value="/resources/img"/>/btn_confirm.png" alt="확인"></a>
								</div>
							</div>
							
							<div class="input_gap"></div>
			
							<div class="input-box car_limitkm_setting">
								<strong>규칙설정</strong>
								<ul>
									<li>
										<p>현재 제한 속도</p>
										<div class="input-box">
											<input class="form-input" style="width:100%;" readonly type="number" name="name" id="in_current_speed_limit">
										</div>
									</li>
									<li>
										<p>제한속도 변경</p>
										<div class="input-box">
										<select id='in_new_speed_limit' class="sel3">
											<option value='15' selected>15Km</option>
											<option value='25'>25Km</option>
											<option value='40'>40Km</option>
										</select>
										<!-- 
											<input class="form-input" type="number" name="name" id="in_new_speed_limit">
										 -->
										</div>
									</li>
								</ul>
							</div>
			
							<div class="text-center">
								<a href="javascript:updateSpeedLimit();"><img src="<c:url value="/resources/img"/>/btn_confirm.png" alt="확인"></a>
							</div>
						</div>
			
					</div>
				</div>
	
	
