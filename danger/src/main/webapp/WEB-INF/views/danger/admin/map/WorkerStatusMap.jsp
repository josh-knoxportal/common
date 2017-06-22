<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<style>
.tr_rowspan{height:58px !important;}
</style>


<!--  -->
<script src="<c:url value="/resources/js"/>/d3/d3.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/transformation-matrix-js/matrix.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/mathjs/math.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_core.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_extend.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_properties.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_for_wwms.js" type="text/javascript"></script>

<script type="text/javascript">
	/* [2016/05/23] added */
	var	canvasWorkStatusMap;
	
	/* */
	$(document).ready(function(){
		/* */		
		$("#select_factory_zone").on("change",function() {
			updateGlobalFactoryZone();
			/* */
			requestWorkStatusList();
		});
		
		$("#select_work_type").on("change",function() {
			requestWorkStatusList();
		});
		
		/* [2016/08/22] added */
		LGSmartMapAttr.backgroundPath(contextPath+'/resources/img/bg_map_worker.png');
		
		/* */	
		canvasWorkStatusMap = new LGSmartMap("#canvasWorkStatusMap", true, true, false);
		canvasWorkStatusMap.setupRequestStatusMapMode(REQ_MODE_WORKER);
		//canvasWorkStatusMap.initViewScale([-380,-310], 1.08);
		canvasWorkStatusMap.initViewScale([-615,-390], 1.30);
		canvasWorkStatusMap.zoomEnabled(false);
		canvasWorkStatusMap.dragEnabled(false);
		
		canvasWorkStatusMap.showOnlyFactoryZoneBoundary(true);
		canvasWorkStatusMap.showOnlyRestrictedFactoryZone(true);
		
		/* */
		canvasWorkStatusMap.setOnHideTooltipListner(function() {
			$('#tooltipFactoryBox').hide();
			$('#tooltipBeaconBox').hide();
    	});
		
		/* */
    	canvasWorkStatusMap.setOnShowTooltipListner(function(options,x,y) {
			var $tooltip;
        	if (options.type == WSC_FACTORY) {
        		$tooltip = $("#tooltipFactoryBox");
        	} else {
        		$tooltip = $("#tooltipBeaconBox");
        	}
        	/*
        	 * options.name, options.left_title, options.right_title
        	 */
             switch (options.type) {
             case WSC_FACTORY:
            	 $tooltip.find(".title").html(''+options.factory_name);
             	 break;
             case WSC_ZONE:
            	 $tooltip.find(".title").html(''+options.zone_name);
            	 break;
             default:
            	 $tooltip.find(".title").html('');
            	 break;
             }
        	
        	$tooltip.find(".total_title").html(''+options.left_title);
        	$tooltip.find(".unlicense_title").html(''+options.right_title);
        	$tooltip.find(".total").html(''+options.left_value);
        	$tooltip.find(".unlicense").html(''+options.right_value);
  
        	
			$tooltip.css({ 'top':y, 'left':x });
			$tooltip.show();
    	});
    	
		/* */
		canvasWorkStatusMap.setOnCompletedFactoryCoordListener(function() {
			/* no action */
		});
		
		/* */
		canvasWorkStatusMap.setOnCompletedZoneCoordListener(function() {
			/* no action */
		});
		
		/* */
		canvasWorkStatusMap.setOnCompletedCodeFactoryZoneListener(function(factoryZoneList) {
			buildFactoryZoneSelector(factoryZoneList);
			initFactoryZoneSelectorWithGlobal();
			canvasWorkStatusMap.requestCodeWorkType();
		});
		
		/* */
		canvasWorkStatusMap.setOnCompletedCodeWorkTypeListener(function(workTypeList) {
			buildWorkTypeSelector(workTypeList);
			
			/* only one */
			periodicRefreshCurrentWorkStatus();
		});
		
		
		/* */
		canvasWorkStatusMap.setOnCompletedWorkStatusListener(function() {
			refreshFactoryTableAfterQuery();
			refreshZoneTableAfterQuery();
			refreshWorkTypeTableAfterQuery();		
			refreshMapUI();	
		});
		
		/* */
		canvasWorkStatusMap.setOnClickedWorkStatusCircleListener(function(type,id) {   	 
			popupContactsOnFactoryZone(type,id);
		})
		
		
		canvasWorkStatusMap.requestFactoryCoord();
		canvasWorkStatusMap.requestZoneCoord();
		canvasWorkStatusMap.requestCodeFactoryZone();
	});

	
	
	/* [2016/05/25] */
	function periodicRefreshCurrentWorkStatus() {
		/* */
		try {
			requestWorkStatusList();	
		} catch (e) {
			
		}
		
		/* */
        setTimeout(periodicRefreshCurrentWorkStatus,5000);
	}
	
	/* */
	function buildFactoryZoneSelector(factoryZoneList) {
		var $select = $("#select_factory_zone");
		
		var html = '<option type="all" value="" checked>위험구역 전체</option>';
		if (!atbsvg.util.isNull(factoryZoneList) && factoryZoneList.length > 0) {
			var size = factoryZoneList.length;
			for(var index = 0; index < size; index++) {
				var item = factoryZoneList[index];
				
				/* */
				if (item.is_restricted == 1) {
					html += '<option type="'+item.type+'" value="'+item.uid+'">'+item.name+'</option>';
				}
			}
		}
		
		$select.html(html);
	}

	/* */
	function buildWorkTypeSelector(workTypeList) {
		var $select = $("#select_work_type");
		
		var html = '<option value="" checked>작업유형 전체</option>';			
		if (!atbsvg.util.isNull(workTypeList) && workTypeList.length > 0) {
			var size = workTypeList.length;
			for(var index = 0; index < size; index++) {
				var item = workTypeList[index];
				
				/* */
				html += '<option value="'+item.code+'">'+item.name+'</option>';
			}
		}
		
		$select.html(html);
	}
	
	/* */
	function updateGlobalFactoryZone() {
		var uid = $("#select_factory_zone").val();
		var type = $("#select_factory_zone option:selected").attr('type');
		
		//console.log("[update] uid:"+uid+",type:"+type);				
		globalFactoryZoneUid(uid);
		globalFactoryZoneType(type);
	}
	
	
	
	/* */
	function initFactoryZoneSelectorWithGlobal() {
		var uid  = globalFactoryZoneUid();
		var type = globalFactoryZoneType();
		
		if (!atbsvg.util.isNullEmpty(uid) && !atbsvg.util.isNullEmpty(type)) {
			//console.log("[initial] uid:"+uid+",type:"+type);
			try {
				var $sel = $("#select_factory_zone");		
				if ($sel.find('option[value="'+uid +'"]').length > 0) {
					$sel.val(uid);
				}
			} catch (e) { /* ignore */ }
		}
	}
	
	
	/* */
	function requestWorkStatusList() {
		var uid = $("#select_factory_zone").val();
		var type = $("#select_factory_zone option:selected").attr('type');
		var work_type = $("#select_work_type").val();	
		
		//console.log("type:"+type+",uid:"+uid+",work_type:"+work_type);
		
		canvasWorkStatusMap.requestWorkStatusList(type, uid, work_type);
	}
	
	/* */
	function refreshFactoryTableAfterQuery() {
		var flist = canvasWorkStatusMap.currentFactoryStatusList();
		var $tbody = $('#tbody_factory_status');
		$tbody.html('');

		if (atbsvg.util.isNull(flist)) return ;
		
		var html = '';
		var size = flist.length;
		
		var total_authorized_cnt = 0;
		var total_unauthorized_cnt = 0;
		var total_work_cnt = 0;
		
		var unknownFactory = null;
		for(var index = 0; index < size; index++) {
			var factory = flist[index];
			
			/* [2016/08/22] added */
			if (factory.factory_uid < 0) {
				unknownFactory = factory;
				continue;
			}
			
			html += "<tr onclick='javascript:canvasWorkStatusMap.toFrontWorkStatusCircle("+WSC_FACTORY+",\""+factory.factory_uid+"\");'>";
			html += "<td>"+factory.factory_name+"</td>";
			html += "<td>"+factory.people_authorized_cnt+"</td>";
			html += "<td>"+factory.people_unauthorized_cnt+"</td>";
			html += "<td>"+factory.work_cnt+"</td>";
			html += "</tr>";
			
			/* */		
			total_authorized_cnt += parseInt(factory.people_authorized_cnt);
			total_unauthorized_cnt += parseInt(factory.people_unauthorized_cnt);
			total_work_cnt += parseInt(factory.work_cnt);
		}	
		
		/* [2016/08/26] 순서를 맞추기 위해 */
		if (unknownFactory != null) {
			html += "<tr onclick='javascript:canvasWorkStatusMap.toFrontWorkStatusCircle("+WSC_FACTORY+",\""+unknownFactory.factory_uid+"\");'>";
			html += "<td>"+unknownFactory.factory_name+"</td>";
			html += "<td>"+unknownFactory.people_authorized_cnt+"</td>";
			html += "<td>"+unknownFactory.people_unauthorized_cnt+"</td>";
			html += "<td>"+unknownFactory.work_cnt+"</td>";
			html += "</tr>";
			
			/* */		
			total_authorized_cnt += parseInt(unknownFactory.people_authorized_cnt);
			total_unauthorized_cnt += parseInt(unknownFactory.people_unauthorized_cnt);
			total_work_cnt += parseInt(unknownFactory.work_cnt);			
		}
		
		$tbody.html(html);
		
		
		/* */
		var $tfoot = $("#tfoot_factory_status");
		$tfoot.html('');
		html = '';		
		html += "<tr class='sum'>";
		html += "<td>합계</td>";
		html += "<td>"+total_authorized_cnt+"</td>";
		html += "<td>"+total_unauthorized_cnt+"</td>";
		html += "<td>"+total_work_cnt+"</td>";
		html += "</tr>";	
		$tfoot.html(html);
	}
	
	/* */
	function refreshZoneTableAfterQuery() {
		var zlist = canvasWorkStatusMap.currentZoneStatusList();
		var $tbody = $('#tbody_zone_status');
		$tbody.html('');

		if (atbsvg.util.isNull(zlist)) return ;
		
		var html = '';
		var size = zlist.length;
		
		var total_authorized_cnt = 0;
		var total_unauthorized_cnt = 0;
		var total_work_cnt = 0;
		
		for(var index = 0; index < size; index++) {
			var zone = zlist[index];
			html += "<tr onclick='javascript:canvasWorkStatusMap.toFrontWorkStatusCircle("+WSC_ZONE+",\""+zone.zone_uid+"\")'>";
			html += "<td>"+zone.zone_name+"</td>";
			html += "<td>"+zone.people_authorized_cnt+"</td>";
			html += "<td>"+zone.people_unauthorized_cnt+"</td>";
			html += "<td>"+zone.work_cnt+"</td>";
			html += "</tr>";
			
			/* */		
			total_authorized_cnt += parseInt(zone.people_authorized_cnt);
			total_unauthorized_cnt += parseInt(zone.people_unauthorized_cnt);
			total_work_cnt += parseInt(zone.work_cnt);
		}	
		
		$tbody.html(html);
	
		/* */
		var $tfoot = $("#tfoot_zone_status");
		$tfoot.html('');
		html = '';		
		html += "<tr class='sum'>";
		html += "<td>합계</td>";
		html += "<td>"+total_authorized_cnt+"</td>";
		html += "<td>"+total_unauthorized_cnt+"</td>";
		html += "<td>"+total_work_cnt+"</td>";
		html += "</tr>";	
		$tfoot.html(html);		
	}

	/* */
	function refreshWorkTypeTableAfterQuery() {
		var wlist = canvasWorkStatusMap.currentWorkTypeStatusList();
		var $tbody = $('#tbody_worktype_status');
		$tbody.html('');

		if (atbsvg.util.isNull(wlist)) return ;
		
		var html = '';
		var size = wlist.length;
		
		var total_people_cnt = 0;
		var total_work_cnt = 0;
		
		for(var index = 0; index < size; index++) {
			var work = wlist[index];
			html += "<tr>";
			html += "<td>"+work.work_type_name+"</td>";
			html += "<td>"+work.people_cnt+"</td>";
			html += "<td>"+work.work_cnt+"</td>";
			html += "</tr>";

			/* */		
			total_people_cnt += parseInt(work.people_cnt);
			total_work_cnt += parseInt(work.work_cnt);
		}	
		
		$tbody.html(html);
		
		/* */
		var $tfoot = $("#tfoot_worktype_status");
		$tfoot.html('');
		html = '';		
		html += "<tr class='sum'>";
		html += "<td>합계</td>";
		html += "<td>"+total_people_cnt+"</td>";
		html += "<td>"+total_work_cnt+"</td>";
		html += "</tr>";	
		$tfoot.html(html);			
	}
	
	/* */
	function refreshMapUI() {
		canvasWorkStatusMap.redrawWorkStatusMap();
	}
</script>

<style>
.danger_map .map_buttons { position:absolute; top:15px;right:15px;z-index:10; }
</style>

<div class="danger_zone_box">	
	<!-- danger_zone_l -->
	<div class="danger_zone_l">
		<div class="danger_cat">
			<!-- danger_cat_l -->
			<div class="danger_cat_l n_danger_cat_l2">
				<span class="zone1">공장구역</span>
				<span class="zone2">비콘 설치구역</span>
			</div>
			<!-- // danger_cat_l -->

			<!-- danger_cat_r -->
			<div class="danger_cat_r">
				<select id="select_factory_zone" class="sel">
					<!-- empty -->
				</select>
				<select id="select_work_type" class="sel">
					<!-- empty -->
				</select>
			</div>
			<!-- // danger_cat_r -->
		</div>
		<div class="danger_map">
			<!-- IE11 :  style="overflow: hidden;" -->
			<div class="map" id="canvasWorkStatusMap" style="overflow: hidden;"></div>
			
			<div class="map_buttons">
				<a href="javascript:requestWorkStatusList()"><img src="<c:url value="/resources/img"/>/img_map_reload.png" alt="reload"></a>
			</div>
		</div>
	</div>
	<!-- // danger_zone_l -->

	<!-- danger_zone_r -->
	<div class="danger_zone_r">
		<!-- danger_tbl_section -->
		<div class="danger_tbl_section first">
			<h3>공장 구역별 현황</h3>
			<div class="fixed_head_tbl">
				<table class="ntype_tbl ntype_tbl_head">
					<colgroup>
						<col style="width:40%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
					</colgroup>
					<thead>
						<tr class="first_tr">
							<th rowspan="2">GPS기반<br>위험구역</th>
							<th colspan="2">작업자수</th>
							<th rowspan="2">작업 수</th>
						</tr>
						<tr class="sec_tr">
							<th>인가</th>
							<th>비인가</th>
						</tr>
					</thead>
				</table>

				<div class="fixed_head_body">
				<table class="ntype_tbl ntype_tbl_body">
					<colgroup>
						<col style="width:auto;">
						<col style="width:21%;">
						<col style="width:21%;">
						<col style="width:16.8%;">
					</colgroup>
					<tbody id="tbody_factory_status">
						<!-- empty -->
					</tbody>
				</table>
				</div>

				<table class="ntype_tbl ntype_tbl_foot">
					<colgroup>
						<col style="width:40%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
					</colgroup>
					<tfoot id="tfoot_factory_status">
						<!-- empty -->
					</tfoot>
				</table>
			</div>
		</div>
		<!-- danger_tbl_section -->
		
		<!-- danger_tbl_section -->
		<div class="danger_tbl_section">
			<h3>비콘 설치구역별 현황</h3>
			<div class="fixed_head_tbl">
				<table class="ntype_tbl ntype_tbl_head">
					<colgroup>
						<col style="width:40%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
					</colgroup>
					<thead>
						<tr class="first_tr">
							<th rowspan="2">비콘기반<br>위험구역</th>
							<th colspan="2">작업자수</th>
							<th rowspan="2">작업 수</th>
						</tr>
						<tr class="sec_tr">
							<th>인가</th>
							<th>비인가</th>
						</tr>
					</thead>
				</table>

				<div class="fixed_head_body">
					<table class="ntype_tbl ntype_tbl_body">
						<colgroup>
							<col style="width:auto;">
							<col style="width:21%;">
							<col style="width:21%;">
							<col style="width:16.8%;">
						</colgroup>
						<tbody id="tbody_zone_status">
							<!-- empty -->
						</tbody>
					</table>
				</div>

				<table class="ntype_tbl ntype_tbl_foot">
					<colgroup>
						<col style="width:40%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
					</colgroup>
					<tfoot id="tfoot_zone_status">
						<!-- empty -->
					</tfoot>
				</table>
			</div>
		</div>
		<!-- // danger_tbl_section -->

		<!-- danger_tbl_section -->
		<div class="danger_tbl_section">
			<h3>작업 유형별 현황</h3>
			<div class="fixed_head_tbl">
				<table class="ntype_tbl ntype_tbl_head">
					<colgroup>
						<col style="width:40%;">
						<col style="width:30%;">
						<col style="width:30%;">
					</colgroup>
					<thead>
						<tr class="first_tr tr_rowspan">
							<th>작업유형</th>
							<th>작업자수</th>
							<th>작업수</th> 
						</tr>
					</thead>
				</table>
				
				<div class="fixed_head_body">
				<table class="ntype_tbl ntype_tbl_body">
					<colgroup>
							<col style="width:39%;">
							<col style="width:30%;">
							<col style="width:25.25%;">
					</colgroup>
					<tbody id="tbody_worktype_status">
						<!-- empty -->
					</tbody>
				</table>
				</div>

				<table class="ntype_tbl ntype_tbl_foot">
					<colgroup>
						<col style="width:40%;">
						<col style="width:30%;">
						<col style="width:30%;">
					</colgroup>
					<tfoot id="tfoot_worktype_status">
						<!-- empty -->
					</tfoot>
				</table>
			</div>
		</div>
		<!-- // danger_tbl_section -->
	</div>
	<!-- // danger_zone_r -->
</div>


<!-- 2016-08-22 -->
<div id='tooltipFactoryBox' class="n_cir_info n_info_factory2 modal">
	<div class="n_cir_info_box">
		<div class="align_center"><span class="title"></span></div>
		<span class="total_title"></span>
		<span class="unlicense_title"></span>
		<span class="total"></span>
		<span class="unlicense"></span>
	</div>
</div>

<div id='tooltipBeaconBox' class="n_cir_info n_info_beacon2 modal">
	<div class="n_cir_info_box">
		<div class="align_center"><span class="title"></span></div>
		<span class="total_title"></span>
		<span class="unlicense_title"></span>
		<span class="total"></span>
		<span class="unlicense"></span>
	</div>
</div>
<!-- // 2016-08-22 -->
					

