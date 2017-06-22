<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>


<!--  -->
<script src="<c:url value="/resources/js"/>/d3/d3.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/transformation-matrix-js/matrix.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/mathjs/math.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_core.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_extend.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_properties.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_for_wwms.js" type="text/javascript"></script>

<script>
/* [2016/08/18] UPGRADE */
	var	canvasFactoryZoneMap;
	
	$(document).ready(function(){
		/* tab */
		var tabButton = $(".tab-box > li > a");
		var tabShow = $(".tab-wrap > .tab-contents");
		tabButton.on('click focus',function(){
			var btnIdx = $(this).parent().index(); // 클릭된 버튼의 부모 인덱스
			if(tabButton.target){ // 이전 버튼인지 판단
				$(tabButton.target).parent().removeClass("active"); // 이전 버튼인경우 클래스 제거
				$(this).parent().addClass("active"); // 현제 클릭된 버튼 클래스 추가
				$(tabHide).removeClass("target"); // 이전 클릭된 버튼의 부모 인덱스와 같은 탭영역 클래스 제거(숨기기)
				$(tabShow[btnIdx]).addClass("target"); // 현제 클릭된 버튼의 부모 인덱스와 같은 탭영역 클래스 추가(보이기)
			}else{
				$(tabButton).parent().removeClass("active");
				$(this).parent().addClass("active");
				$(tabShow).removeClass("target");
				$(tabShow[btnIdx]).addClass("target");
			}
			tabButton.target = this; //현제 클릭된 버튼
			tabHide = $(tabShow[btnIdx]); //현제 보여지는 탭영역
			
			refreshMapUI();
			return false;
		});
		
		/* */
		$("#btn_set_factory").on("click",function() {
			clickedFactoryUpdateButton();
		});
		
		/* */
		$("#btn_set_zone").on("click",function() {
			clickedZoneUpdateButton();
		});
		
		
		/* [2016/08/22] added */
		LGSmartMapAttr.backgroundPath(contextPath+'/resources/img/bg_map_worker.png');
		
		/* */
		canvasFactoryZoneMap = new LGSmartMap("#canvasFactoryZoneMap", true, true, false);
		//canvasFactoryZoneMap.initViewScale([-380,-310], 1.08);
		canvasFactoryZoneMap.initViewScale([-615,-390], 1.30);
		canvasFactoryZoneMap.zoomEnabled(false);
		canvasFactoryZoneMap.dragEnabled(false);
		
		/* */
		canvasFactoryZoneMap.setOnCompletedFactoryCoordListener(function() {
			refreshFactoryTable(isAscending($('#img_factory_order')));
			refreshMapUI();
		});
		
		/* */
		canvasFactoryZoneMap.setOnCompletedZoneCoordListener(function() {
			refreshZoneTable(isAscending($('#img_zone_order')));
			refreshMapUI();
		});
		
		/* */
		canvasFactoryZoneMap.setOnUpdatedFactoryRestrictListener(function() {
			canvasFactoryZoneMap.showAlert("성공적으로 요청이 처리되었습니다.");
			canvasFactoryZoneMap.requestFactoryCoord();
		});
		
		/* */
		canvasFactoryZoneMap.setOnUpdatedZoneRestrictListener(function() {
			canvasFactoryZoneMap.showAlert("성공적으로 요청이 처리되었습니다.");
			canvasFactoryZoneMap.requestZoneCoord();
		});
	
		canvasFactoryZoneMap.requestFactoryCoord();
		canvasFactoryZoneMap.requestZoneCoord();
	});
	
	
	/* */
	function isCurrentFactoryTab() {
		if ($('#tabitem_current_factory').hasClass('active')) return true;
		return false;
	}
	
	
	/* */
	function isAscending($img) {
		try {
			$src = $img.prop('src');
			if($src.indexOf('_asc.png') > 0 ){
				return true;
			}
			
			return false;
		} catch (e) {
			return true;
		}
	}
	
	/* */
	function updateSortingImage($img, ascending) {
		try {
			var $src = $img.prop('src');		
			if (ascending) {
				if ($src.indexOf('_desc.png') > 0) {
					$img.prop("src", $src.replace("_desc.png","_asc.png"));
				}
			} else {
				if ($src.indexOf('_asc.png') > 0) {
					$img.prop("src", $src.replace("_asc.png","_desc.png"));
				}
			}
		} catch(e) { /* ignore */ }
	}
	
	function toggleSortedList() {
		var $img;
		
		if (isCurrentFactoryTab()) {
			$img = $('#img_factory_order');
		} else {
			$img = $('#img_zone_order');
		}
		
		/* */
		$src = $img.prop('src');
		if(isAscending($img)) {
			updateSortingImage($img,false);
		}
		else{
			updateSortingImage($img,true);
		}
		
		/* */
		if (isCurrentFactoryTab()) {
			refreshFactoryTable(isAscending($img));
		} else {
			refreshZoneTable(isAscending($img));
		}
	}

	
	/* */
	function refreshMapUI() {
		if (isCurrentFactoryTab()) {
			canvasFactoryZoneMap.showFactoryCoord();
		} else {
			canvasFactoryZoneMap.showZoneCoord();
		}
	}
	
	/* [2016/06/09] sorting..., */
	function makeFactoryTableTd(factory) {
		if (atbsvg.util.isNull(factory)) return '';
		
		/* [2016/06/29] */
		if (factory.is_factory != 1) {
			return '';
		}
		/* end of [2016/06/29] */
		
		var checked = '';
		
		var html = "<tr>";
		html += "<td>"+factory.name+"</td>";
		if (factory.is_restricted == 1) {
			checked = "checked";
		}
		html += "<td><input type='checkbox' "+checked+" value='"+factory.factory_uid+"'/></td>";
		html += "</tr>";	
		
		return html;
	}
	
	/* */
	function refreshFactoryTable(ascending) {
		/* */
		var $tablebody = $('#tbody_factory');
		$tablebody.html('');
		
		var flist = canvasFactoryZoneMap.currentFactoryList();
		if (atbsvg.util.isNull(flist) || flist.length <= 0) return ;
		
		var html = '';
		var size = flist.length;
		
		/* [2016/06/09] sorting..., */
		if (ascending == true) {
			for(var index = 0; index < size; index++) {
				html += makeFactoryTableTd(flist[index]);			
			}
		} else {
			for(var index = (size-1); index >= 0; index--) {
				html += makeFactoryTableTd(flist[index]);			
			}
		}
		
		$tablebody.html(html);
		
		$('#tbody_factory input[type="checkbox"]').change(function() {
			if (this.checked) {
				canvasFactoryZoneMap.redrawFactoryElementWithId($(this).val(),1);
			} else {
				canvasFactoryZoneMap.redrawFactoryElementWithId($(this).val(),0);
			}
		});
	}
	
	
	
	/* [2016/06/09] sorting..., */
	function makeZoneTableTd(zone) {
		if (atbsvg.util.isNull(zone)) return '';
		
		var checked = '';
		
		var html = "<tr>";
		html += "<td>"+zone.name+"</td>";
		if (zone.is_restricted == 1) {
			checked = "checked";
		}
		html += "<td><input type='checkbox' "+checked+" value='"+zone.zone_uid+"'/></td>";
		html += "</tr>";
		
		return html;
	}
	
	/* */
	function refreshZoneTable(ascending) {
		var $tablebody = $('#tbody_zone');
		$tablebody.html('');
		
		var zlist = canvasFactoryZoneMap.currentZoneList();
		if (atbsvg.util.isNull(zlist) || zlist.length <= 0) return ;
		
		var html = '';
		var size = zlist.length;
		
		
		/* [2016/06/09] sorting..., */
		if (ascending == true) {
			for(var index = 0; index < size; index++) {
				html += makeZoneTableTd(zlist[index]);		
			}
		} else {
			for(var index = (size-1); index >= 0; index--) {
				html += makeZoneTableTd(zlist[index]);				
			}
		}
		
		$tablebody.html(html);	
			
		$('#tbody_zone input[type="checkbox"]').change(function() {
			if (this.checked) {
				canvasFactoryZoneMap.redrawZoneElementWithId($(this).val(),1);
			} else {
				canvasFactoryZoneMap.redrawZoneElementWithId($(this).val(),0);
			}
		});
	}
	
	/* */
	function clickedFactoryUpdateButton() {
		var factory_uids = '';
		$('#tbody_factory input:checkbox:checked').each(function() {
			if (factory_uids != '') {
				factory_uids += ',';
			}
			factory_uids += $(this).val();
		});
		
		canvasFactoryZoneMap.requestFactoryRestrict(factory_uids);
	}
	
	/* */
	function clickedZoneUpdateButton() {
		var zone_uids = '';
		$('#tbody_zone input:checkbox:checked').each(function() {
			if (zone_uids != '') {
				zone_uids += ',';
			}
			zone_uids += $(this).val();
		});
		
		canvasFactoryZoneMap.requestZoneRestrict(zone_uids);		
	}	
	
</script>


<div class="danger_zone_box">	
	<!-- danger_zone_l -->
	<div class="danger_zone_l">
		<div class="danger_map">
			<!-- IE11 :  style="overflow: hidden;" -->
			<div class="map" id="canvasFactoryZoneMap" style="overflow: hidden;"></div>
		</div>
	</div>
	<!-- // danger_zone_l -->


	<!-- http://blog.publisher.name/339 -->

	<!-- danger_zone_r -->
	<div class="danger_zone_r">
		
			<div class="tab-style">
				<ul class="tab-box half-tab clearfix">
				<li id="tabitem_current_factory" class="active">
					<a href="#" class="tab-btn"><span class="">단위공장</span></a>
				</li>
				<li>
					<a href="#" class="tab-btn"><span class="">비콘 Zone</span></a>
				</li>
			</ul>

			<div class="tab-wrap"  style="padding-top:0;">
				<div class="tab-contents target">
					<div class="table-list">
	<div class="danger_tbl_section">
		<div class="fixed_head_tbl">
							<table class="ntype_tbl ntype_tbl_head">
							<colgroup>
								<col style="width:300px;">
								<col style="width:auto">
							</colgroup>
							<thead>
								<tr class="first_tr">
									<th>공장명 <a href="javascript:toggleSortedList()" class="ord"><img id="img_factory_order" src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
									<th>선택</th>
								</tr>
							</thead>
							</table>
												
		<div class="fixed_head_body" style="height:566px;">
		<table class="ntype_tbl ntype_tbl_body">
			<colgroup>
				<col style="width:300px;">
				<col style="width:auto">
			</colgroup>
							<tbody id="tbody_factory">
								<!-- empty -->
							</tbody>
						</table>
		</div>
	</div>
</div>
						<div class="form-actions text-right" style="padding-bottom:0;">
							<button id="btn_set_factory" type="button" class="btn">선택</button>
						</div>
					</div>
				</div>
				<div class="tab-contents right-tab">
					<div class="table-list">
<!-- danger_tbl_section -->
<div class="danger_tbl_section">
	<div class="fixed_head_tbl">
		<table class="ntype_tbl ntype_tbl_head">
							<colgroup>
								<col style="width:300px;">
								<col style="width:auto">
							</colgroup>
							<thead>
								<tr class="first_tr">
									<th>Zone <a href="javascript:toggleSortedList()" class="ord"><img id="img_zone_order" src="<c:url value="/resources/img"/>/img_ord_asc.png" alt=""></a></th>
									<th>선택</th>
								</tr>
							</thead>
		</table>
												
		<div class="fixed_head_body" style="height:566px;">
					<table class="ntype_tbl ntype_tbl_body">
						<colgroup>
							<col style="width:300px;">
							<col style="width:auto">
						</colgroup>
							<tbody id="tbody_zone">
								<!-- empty -->
							</tbody>
						</table>
		</div>
	</div>
</div>
						<div class="form-actions text-right"  style="padding-bottom:0;">
							<button id="btn_set_zone" type="button" class="btn">선택</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- // danger_zone_r -->
</div>