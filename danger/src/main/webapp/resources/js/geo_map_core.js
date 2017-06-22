var MAP_ID = 2;

var ATTR_DISTRICT = 6;
var ATTR_MARKET = 5;
var ATTR_BRANCH = 7;

var INIT_ADDRESS = "서울특별시";
var INIT_ZOOM_LEVEL = 16;
var MIN_ZOOM_LEVEL = 13;

var DEFAULT_OPACITY = 0.35;
var ZERO_OPACITY = 0.0;

var DONG_COLOR = '#0000FF';
var DISTRICT_COLOR = '#0000FF';
var MARKET_COLOR = '#FF0000';
var BRANCH_COLOR = '#0000FF';
var CLICK_COLOR = '#FFFF00';

var BRANCH_STROKE_WEIGHT = 10;
var DONG_STROKE_WEIGHT = 5;

var DISTRICT_OPTIONS = {
	strokeColor:DISTRICT_COLOR,
	fillColor:DISTRICT_COLOR,
	fillOpacity:ZERO_OPACITY,
	clickable:true
};
var MARKET_OPTIONS = {
	strokeColor:MARKET_COLOR,
	fillColor:MARKET_COLOR,
	fillOpacity:DEFAULT_OPACITY,
	clickable:true
};
var BRANCH_OPTIONS = {
	strokeColor:BRANCH_COLOR,
	fillColor:BRANCH_COLOR,
	fillOpacity:DEFAULT_OPACITY,
	strokeWeight : BRANCH_STROKE_WEIGHT,
	clickable:true
};

var CLICK_OPTIONS = {
	strokeColor:CLICK_COLOR,
	fillColor:CLICK_COLOR,
	fillOpacity:DEFAULT_OPACITY,
	clickable:true
};

var drawingManager;
var map;
var rect;
var latlng;
var mapOptions;
var infoWindow;
var geocoder;


var total_geozone_list = [];
var total_search_geozone_list = [];

var districtzone_list = new Array();
var districtzone_search_list = new Array();
var districtzone_card_list = [];
var districtzone_search_card_list = [];
var distrcit_num = 0;

var marketzone_list = new Array();
var marketzone_search_list = new Array();
var marketzone_card_list = [];
var marketzone_search_card_list = [];

var branchzone_list = new Array();
var branchzone_search_list = new Array();
var branchzone_card_list = [];
var branchzone_search_card_list = [];

var editzone_list = new Array();

var drawGeoZonesMode = false;

var createSearchMode = false;
var createDistrictZoneMode = false;
var createMarketZoneMode = false;
var createBranchZoneMode = false;

var editMode = false;

var searchMode = false;

var dragflag = false;

var pageNum = 1;

var onGeoZoneClickCallback;

/*
 * Zone 생성시 필요한 임시 저장 객체 
 */
var tmp_create_district_zone = new Array();
var tmp_create_market_zone;
var tmp_create_branch_zone;

var temp_drawing_zone;

/*
 * Zone 편집시 필요한 임시 저장 객체 
 */
var tmp_geo_update_zone;

var selectGeoZoneId;


var markers = [];
var markerGroup = new Object();

//var zone_list;


/*
 * Reset Object
 */
function resetObject(){
	
	total_geozone_list = [];
	
	districtzone_list = new Array();
	districtzone_card_list = [];
	distrcit_num = 0;

	marketzone_list = new Array();
	marketzone_card_list = [];

	branchzone_list = new Array();
	branchzone_card_list = [];

	editzone_list = new Array();
}

/*
 * Reset Search Card list Object
 */
function resetSearchCardObject(){
	total_search_geozone_list = [];
	districtzone_search_card_list = [];
	marketzone_search_card_list = [];
	branchzone_search_card_list = [];
}

/*
 * Reset Search list Object
 */
function resetSearchObject(){
	//total_search_geozone_list = [];
	districtzone_search_list = [];
	marketzone_search_list = [];
	branchzone_search_list = [];
}

/*
 * Default District Data생성
 */
function createDefaultDistrictZoneData(id,attr) {
	var dZone  = {
			id: id,
			validity: 1,
			map_id: -1,
			name: "NewDistrictZone",
			owner_group: getOwnerGroup(),
			owner_type: getOwnerType(),
			positioning: 1,
			type: 1,
			zone_level: 4,
			attribute:attr,
			company_id: -1,
			company_brand_id: -1,
			branch_id: -1,
			floor_id: -1,
			tenant_corner_id: -1,
			store_type: -1,
			title_center_x : 0,
			title_center_y : 0,
			title_left_top_x : 0,
			title_left_top_y : 0,
			title_right_bottom_x : 0,
			title_right_bottom_y : 0,
			title_angle : 0,
			coords_array: [],
			dong_array: []
	};
	return dZone;
}


/*
 * Default Market Data생성
 */
function createDefaultMarketZoneData(id,attr) {
	var dZone  = {
			id: id,
			validity: 1,
			map_id: -1,
			name: "NewMarketZone",
			owner_group: getOwnerGroup(),
			owner_type: getOwnerType(),
			positioning: 1,
			type: 1,
			zone_level: 4,
			attribute:attr,
			company_id: -1,
			company_brand_id: -1,
			branch_id: -1,
			floor_id: -1,
			tenant_corner_id: -1,
			store_type: -1,
			title_center_x : 0,
			title_center_y : 0,
			title_left_top_x : 0,
			title_left_top_y : 0,
			title_right_bottom_x : 0,
			title_right_bottom_y : 0,
			title_angle : 0,
			coords_array: []
	};
	return dZone;
}

/*
 * Default Branch Data생성
 */
function createDefaultBranchZoneData(id,attr) {
	var dZone  = {
			id: id,
			validity: 1,
			map_id: -1,
			name: "NewBranchZone",
			owner_group: getOwnerGroup(),
			owner_type: getOwnerType(),
			positioning: 1,
			type: 1,
			zone_level: 4,
			attribute:attr,
			company_id: -1,
			company_brand_id: -1,
			branch_id: -1,
			floor_id: -1,
			tenant_corner_id: -1,
			store_type: -1,
			title_center_x : 0,
			title_center_y : 0,
			title_left_top_x : 0,
			title_left_top_y : 0,
			title_right_bottom_x : 0,
			title_right_bottom_y : 0,
			title_angle : 0,
			coords_array: []
	};
	return dZone;
}

/*
 * 생성중인 행정 Zone 정보 반환
 */
function getCreateDistrictZoneData() {
	return tmp_create_district_zone;
}

/*
 * 생성중인 상권 Zone 정보 반환
 */
function getCreateMarketZoneData() {
	return tmp_create_market_zone;
}

/*
 * 생성중인 지점 Zone 정보 반환
 */
function getCreateBranchZoneData() {
	return tmp_create_branch_zone;
}

/*
 * 편집중인 Zone 정보 반환
 */
function getUpdateZoneData() {
	return tmp_geo_update_zone;
}


/*
 * 행정 Zone 정보 reset
 */
function resetDistrictZoneData() {
	tmp_create_district_zone = [];
}

/*
 * 상권 Zone 정보 reset
 */
function resetMarketZoneData() {
	tmp_create_market_zone = '';
}

/*
 * 지점 Zone 정보 reset
 */
function resetBranchZoneData() {
	tmp_create_branch_zone = '';
}

/*
 * 편집중인 Zone 정보 reset
 */
function resetUpdateZoneData() {
	tmp_geo_update_zone = '';
}

/*
 * GeoZone 목록   
 */
function drawGeoZones() {
	
	drawGeoZonesMode = true;
	
	var size = zone_list.length;
	//console.log(zone_list);
	
	var id = 0;
	for (var i = 0; i < size; i++) {
		if (zone_list[i].attribute == 6) {
			
			if(zone_list[i].group_dong_name != null  ){
				if(id != zone_list[i].id){
					id = zone_list[i].id;
					total_geozone_list.push(zone_list[i]);
					districtzone_card_list.push(zone_list[i]);
				}else{
				}
			}
			drawDistrictZone(zone_list[i]);
		} else if (zone_list[i].attribute == 5) {
			total_geozone_list.push(zone_list[i]);
			marketzone_card_list.push(zone_list[i]);
			drawMarketZone(zone_list[i]);
		} else if (zone_list[i].attribute == 7) {
			total_geozone_list.push(zone_list[i]);
			branchzone_card_list.push(zone_list[i]);
			drawBranchZone(zone_list[i]);
		}
	}	
	
	//console.log("[total_geozone_list ] >>>");
	//console.log(total_geozone_list);
	
	drawGeoZonesMode = false;
}

/*
 *  모든 Geo click존 비활성화    
 */
function setDefaultAllClickZone() {
	setDefaultClickZoneList(districtzone_list );
	setDefaultClickZoneList(marketzone_list );
	setDefaultClickZoneList(branchzone_list );
	
	if(districtzone_search_list.length > 0){
		setDefaultClickZoneList(districtzone_search_list);
	}else if(marketzone_search_list.length > 0 ){
		setDefaultClickZoneList(marketzone_search_list);
	}else if(branchzone_search_list.length > 0 ){
		setDefaultClickZoneList(branchzone_search_list);
	}else if(editzone_list.length > 0 ){
		setDefaultClickZoneList(editzone_list);
	}
}

/*
 *  Geo click존 리스트 비활성화    
 */
function setDefaultClickZoneList(zone_list) {
	
	var size = zone_list.length;
	//console.log(zone_list);
	
	for (var i = 0; i < size; i++) {
		if (zone_list[i].attribute == 6) {
			zone_list[i].setOptions({fillColor : DISTRICT_COLOR , strokeColor : DISTRICT_COLOR });
		} else if (zone_list[i].attribute == 5) {
			zone_list[i].setOptions({fillColor : MARKET_COLOR  , strokeColor : MARKET_COLOR  });
		} else if (zone_list[i].attribute == 7) {
			zone_list[i].setOptions({fillColor : BRANCH_COLOR   , strokeColor : BRANCH_COLOR   });
		}  
	}	
}

/*
 * GeoZone 이벤트 설정
 */
function setGeoZoneEvent(z, zone) {
	////console.log(z);
	var conent;
	
	google.maps.event.addListener(z, "click", function(event) {
		//console.log('click');
		
		//getZoneData(zone.id);
		//setZoneClickCallback(zoneClickCallback);
		
		if (onGeoZoneClickCallback != null) {
			onGeoZoneClickCallback(z, zone);
		}
	});
	
	google.maps.event.addListener(z, "rightclick", function(event) {
		//console.log('rightclick');
		//console.log(event);
		//console.log(event.vertex);
		
		/*
		var r = confirm('존을 삭제하시겠습니까? ');
	    if (r == true) {
	    	z.getPath().clear();
			//console.log(zone.id);
			
			drawingManager.setOptions({
				drawingControl: true
			});
	    } else {
	    	
	    }
	    */
		/*
		if (event.vertex != null) {
			alert(1);
			z.getPath().removeAt(event.vertex);
			//console.log(z.getPath().getLength());
			zone.coords_array[event.vertex].validity = 9;
			//updateZoneCoords(zone);
		} else {
			//deleteZoneData(""+zone.id); // zone 삭제
		}
		*/
	    
	});
	
	google.maps.event.addListener(z, "dragstart", function(event){
		dragflag = true;
		//console.log('dragstart');
		/* for(var i=0; i<z.getPath().length;i++){
			//console.log(z.getPath().getAt(i).lat());
			//console.log(z.getPath().getAt(i).lng());	
		} */
		
	});
	
	google.maps.event.addListener(z, "dragend", function(event){
		dragflag = false;
		//console.log('dragend');
		//console.log(z.getPath().getAt(0).lat());
		for (var i = 0; i < zone.coords_array.length; i++) {
			zone.coords_array[i].coord_x = z.getPath().getAt(i).lat();
			zone.coords_array[i].coord_y = z.getPath().getAt(i).lng();
			//console.log("order("+ i +") lat  : " +  z.getPath().getAt(i).lat());
			//console.log("order("+ i +") lng  : "+ z.getPath().getAt(i).lng());
		}
		
		tmp_geo_update_zone = zone; 
		
	});
	
	google.maps.event.addListener(z.getPath(), "insert_at", function(event) {
		//console.log('insert_at');
		//console.log(event);
		//console.log(z.getPath().getLength());
		var coord = {
				id: -1,
				coord_x: z.getPath().getAt(event).lat(),
				coord_y: z.getPath().getAt(event).lng()
		};
		
		//console.log("zone.coords_array.length(before) : " +zone.coords_array.length);
		coord.zone_id = zone.id;
		coord.validity = 1;
		
		zone.coords_array.splice(event, 0, coord);
		
		//console.log("zone.coords_array.length(after) : " +zone.coords_array.length);
		reorderZoneCoords(zone);
		updateZoneCoords(zone);
	});
	
	google.maps.event.addListener(z.getPath(), "set_at", function(event) {
		if (!dragflag) {
			//console.log('set_at');
			//console.log(event);
			//console.log(z.getPath().getAt(event));
			
			zone.coords_array[event].coord_x = z.getPath().getAt(event).lat();
			zone.coords_array[event].coord_y = z.getPath().getAt(event).lng();
			
			for (var i = 0; i < zone.coords_array.length; i++) {
				zone.coords_array[i].coord_x = z.getPath().getAt(i).lat();
				zone.coords_array[i].coord_y = z.getPath().getAt(i).lng();
				//console.log("order("+ i +") lat  : " +  z.getPath().getAt(i).lat());
				//console.log("order("+ i +") lng  : "+ z.getPath().getAt(i).lng());
			}
			//console.log(zone);
			
			updateZoneCoords(zone);
		}
	});
	
	/*
	google.maps.event.addListener(z.getPath(), "remove_at", function(event) {
		//console.log('remove_at');
		//console.log(event);
		//console.log("Here is remove_at event");
		//console.log(z.getPath().getLength());
		zone.coords_array[event].validity = 9;
		updateZoneCoords(zone);
	});
	*/
}

/*
 * GeoZone Info 이벤트 설정
 */
function setGeoZoneInfoEvent(z, zone) {
	google.maps.event.addListener(z, "mouseover", function(event) {
		var contentString = '<p>'+ zone.name +'</p><p>'+ zone.address +'</p>';  
		infoWindow = new google.maps.InfoWindow({
			  content: contentString,
			  position : {lat: zone.center_x , lng: zone.center_y}
		});
		
		infoWindow.open(map);
	});
	
	google.maps.event.addListener(z, "mouseout", function(event) {
		infoWindow.close();
	});
}



/*
 * GeoZone Click시 Callback 설정
 */
function setGeoZoneClickCallback(callback) {
	onGeoZoneClickCallback = callback;
}


/*
 * 존 영역 수정, 드래그 기능 Off
 */
function disableEditDragZone() {
	
	var size =  districtzone_list.length;
	for (var i = 0 ; i < size; i++) {
		districtzone_list[i].setOptions({editable:false,draggable:false});
	}
	
	var size =  marketzone_list.length;
	for (var i = 0 ; i < size; i++) {
		marketzone_list[i].setOptions({editable:false,draggable:false});
	}
	
	size = branchzone_list.length;
	for (var i = 0 ; i < size; i++) {
		branchzone_list[i].setOptions({editable:false,draggable:false});
	}
}

/*
 * 존 영역 수정, 드래그, 클릭 기능 Off
 */
function disableEditDragClickZone() {
	
	var size =  districtzone_list.length;
	for (var i = 0 ; i < size; i++) {
		districtzone_list[i].setOptions({editable:false,draggable:false,clickable:false});
	}
	
	size =  marketzone_list.length;
	for (var i = 0 ; i < size; i++) {
		marketzone_list[i].setOptions({editable:false,draggable:false,clickable:false});
	}
	
	size = branchzone_list.length;
	for (var i = 0 ; i < size; i++) {
		branchzone_list[i].setOptions({editable:false,draggable:false,clickable:false});
	}
}

/*
 * 존 영역 클릭 기능 On
 */
function ableClickZone() {
	
	var size =  districtzone_list.length;
	for (var i = 0 ; i < size; i++) {
		districtzone_list[i].setOptions({editable:false,draggable:false,clickable:true});
	}
	
	size =  marketzone_list.length;
	for (var i = 0 ; i < size; i++) {
		marketzone_list[i].setOptions({editable:false,draggable:false,clickable:true});
	}
	
	size = branchzone_list.length;
	for (var i = 0 ; i < size; i++) {
		branchzone_list[i].setOptions({editable:false,draggable:false,clickable:true});
	}
}

/*
 *  remove TempDrawingZone
 */
function removeTempDrawingZone(){
	//console.log("removeTempDrawingZone");
	if(temp_drawing_zone == null){
		
	} else{
		temp_drawing_zone.setMap(null);
	}
}

/*
 *  remove AllMarker
 */
function removeAllMarker(){
	//console.log("remove AllMarker");
	for (var i = 0; i < markers.length; i++) {
	    markers[i].setMap(null);
	}
}

/*
 *  reset Map UI
 */
function defaultMapUI(){

	drawingManager.setMap(null);
	drawingManager.setOptions({
		drawingControl: false,
		polygonOptions: {
			clickable :true,
			editable : false,
			draggable : false
	    } 
	});
	
	setDefaultCardBorder();
	setDefaultAllClickZone();
	removeEditZone();
	removeAllPolygonZone();
	removeAllSearchPolygonZone();
	createPolygonList();
	
	ableClickZone();
	removeTempDrawingZone();
	removeAllMarker(); 
	
}

/*
 * 행정동 그리기
 */
function drawDongZone(zone) {
	var coords_array = zone.coords_array;
	var district;
	var districtInfo;
	var tmpgps;
	var tmp_coord;
	var districtCoords = [];
	
	for (var i = 0; i < coords_array.length; i++) {
		tmp_coord = coords_array[i];
		tmpgps = {id:tmp_coord.id,lat:tmp_coord.coord_x, lng:tmp_coord.coord_y};
		districtCoords.push(tmpgps);
	}
	district = new google.maps.Polygon({
		path:districtCoords,
		strokeColor:DONG_COLOR,
		strokeWeight : DONG_STROKE_WEIGHT,
		//fillColor:DONG_COLOR,
		fillOpacity:ZERO_OPACITY 
	});
	
	var districtOptions= {
		clickable: true,
		draggable : false,
		editable : false
	};
	
	setGeoZoneEvent(district, zone);
	//setGeoZoneInfoEvent(district, zone);
	
	district.setOptions(districtOptions);
	district.setMap(map);
	
	district.id = zone.id;
	district.name = zone.name;
	district.attribute = zone.attribute;
	
	////console.log("[drawGeoZonesMode] >>>" + drawGeoZonesMode);
	if(drawGeoZonesMode){
		districtzone_list.push(district);
	}
	
	if(searchMode){
		districtzone_search_list.push(district);
	}
	
}


/*
 * 행정 Zone 그리기
 */
function drawDistrictZone(zone) {
	var coords_array = zone.coords_array;
	var district;
	var districtInfo;
	var tmpgps;
	var tmp_coord;
	var districtCoords = [];
	
	for (var i = 0; i < coords_array.length; i++) {
		tmp_coord = coords_array[i];
		tmpgps = {id:tmp_coord.id,lat:tmp_coord.coord_x, lng:tmp_coord.coord_y};
		districtCoords.push(tmpgps);
	}
	district = new google.maps.Polygon({
		path:districtCoords,
		strokeColor:DISTRICT_COLOR,
		strokeWeight : DONG_STROKE_WEIGHT,
		fillColor:DISTRICT_COLOR,
		fillOpacity:ZERO_OPACITY
	});
	
	var districtOptions= {
		clickable: true,
		draggable : false,
		editable : false
	};
	
	setGeoZoneEvent(district, zone);
	//setGeoZoneInfoEvent(district, zone);
	
	//district.setOptions(districtOptions);
	district.setMap(map);
	
	district.id = zone.id;
	district.name = zone.name;
	district.attribute = zone.attribute;
	
	//console.log("[drawGeoZonesMode] >>>" + drawGeoZonesMode);
	if(drawGeoZonesMode){
		districtzone_list.push(district);
	}
	
	if(searchMode){
		districtzone_search_list.push(district);
	}
}

/*
 * 상권 Zone 그리기
 */ 
function drawMarketZone(zone) {
	var marketName = zone.name;
	var coords_array = zone.coords_array;
	var market;
	var tmpgps;
	var tmp_coord;
	var marketCoords = [];
	
	for (var i = 0; i < coords_array.length; i++) {
		tmp_coord = coords_array[i];
		tmpgps = {id:tmp_coord.id,lat:tmp_coord.coord_x, lng:tmp_coord.coord_y};
		marketCoords.push(tmpgps);
	}
	
	//console.log(marketCoords);
	
	market = new google.maps.Polygon({
		paths :marketCoords,
		strokeColor:MARKET_COLOR,
		fillColor:MARKET_COLOR,
		fillOpacity:DEFAULT_OPACITY,
	});
	
	var marketOptions= {
		clickable: true
	};
	
	setGeoZoneEvent(market, zone);
	setGeoZoneInfoEvent(market, zone);
	
	//market.setOptions(marketOptions);
	market.setMap(map);
	
	market.id = zone.id;
	market.name = zone.name;
	market.attribute = zone.attribute;
	
	//console.log("[drawGeoZonesMode] >>>" + drawGeoZonesMode);
	if(drawGeoZonesMode){
		marketzone_list.push(market);
	}
	
	if(searchMode){
		marketzone_search_list.push(market);
	}
}

/*
 * 지점 Zone 그리기
 */
function drawBranchZone(zone) {
	var coords_array = zone.coords_array;
	var branch;
	var tmpgps;
	var tmp_coord;
	var branchCoords = [];
	
	for (var i = 0; i < coords_array.length; i++) {
		tmp_coord = coords_array[i];
		tmpgps = {id:tmp_coord.id,lat:tmp_coord.coord_x, lng:tmp_coord.coord_y};
		branchCoords.push(tmpgps);
	}
	
	branch = new google.maps.Polygon({
		path:branchCoords,
		strokeColor:BRANCH_COLOR,
		strokeWeight : BRANCH_STROKE_WEIGHT,
		fillColor:BRANCH_COLOR,
		fillOpacity:DEFAULT_OPACITY,
	});
	
	var branchOptions= {
		clickable: true
	};
	
	setGeoZoneEvent(branch, zone);
	setGeoZoneInfoEvent(branch, zone);
	
	//branch.setOptions(branchOptions);
	branch.setMap(map);
	
	branch.id = zone.id;
	branch.name = zone.name;
	branch.attribute = zone.attribute;
	
	//console.log("[drawGeoZonesMode] >>>" + drawGeoZonesMode);
	if(drawGeoZonesMode){
		branchzone_list.push(branch);
	}
	
	if(searchMode){
		branchzone_search_list.push(branch);
	}
}

/*
 * 편집 Zone 그리기
 */ 
function drawEditZone(zone) {
	var marketName = zone.name;
	//console.log(zone.name);
	var coords_array = zone.coords_array;
	var edit;
	var tmpgps;
	var tmp_coord;
	var editCoords = [];
	
	for (var i = 0; i < coords_array.length; i++) {
		tmp_coord = coords_array[i];
		tmpgps = {id:tmp_coord.id,lat:tmp_coord.coord_x, lng:tmp_coord.coord_y};
		editCoords.push(tmpgps);
	}
	
	edit = new google.maps.Polygon({
		path:editCoords,
		strokeColor:CLICK_COLOR,
		fillColor:CLICK_COLOR,
		fillOpacity:DEFAULT_OPACITY,
	});
	
	var editOptions= {
		clickable: true,
		draggable: true,
		editable : true
	};
	
	setGeoZoneEvent(edit, zone);
	
	edit.setOptions(editOptions);
	edit.setMap(map);
	
	edit.id = zone.id;
	edit.name = zone.name;
	edit.attribute = zone.attribute;
	
	for (var i = 0; i < coords_array.length; i++) {
		tmp_coord = coords_array[i];
		edit.getPath().getAt(i).id = tmp_coord.id;
	}
	
	//console.log("[editMode] >>>" + editMode);
	if(editMode){
		editzone_list.push(edit);
	}
}


/*
 *  Polygon 리스트 다시 생성하기
 */ 
function createPolygonList(){
	if(createSearchMode){
		drawPolygonList(districtzone_list);
		drawPolygonList(marketzone_list);
		drawPolygonList(branchzone_list);
	}else if (createDistrictZoneMode) {
		drawPolygonList(districtzone_list);
	}else if(createMarketZoneMode){
		drawPolygonList(marketzone_list);
	}else if(createBranchZoneMode){
		drawPolygonList(branchzone_list);
	}
}

/*
 *  Polygon 그리기
 */ 
function drawPolygon(zone) {
	//console.log(zone);
	
	zone.setMap(map);
}

/*
 *  Polygon 지우기
 */ 
function removePolygon(zone) {
	////console.log(zone);
	
	zone.setMap(null);
}

/*
 *  Polygon 리스트 그리기
 */ 
function drawPolygonList(zone_list) {
	//console.log(zone_list);
	
	var size =  zone_list.length;
	
	for (var i = 0 ; i < size; i++) {
		if(zone_list[i].name == 'drawing'){
			
		}else{
			zone_list[i].setMap(map);
		}
		
	}
}

/*
 *  편집중인 Polygon 원래대로 복구하기
 */ 
function resetEditingPolygon(zone_list, id) {
	//console.log(zone_list);
	//console.log(id);
	
	var size =  zone_list.length;
	
	for (var i = 0 ; i < size; i++) {
		if(zone_list[i].id == id){
			zone_list[i].setMap(map);
		}
	}
}

/*
 *  Polygon 리스트 지우기
 */ 
function removePolygonList(zone_list) {
	////console.log(zone_list);
	
	var size =  zone_list.length;
	
	for (var i = 0 ; i < size; i++) {
		zone_list[i].setMap(null);
	}
	
}
/*
 *  Polygon 리스트 모두 지우기
 */ 
function removeAllPolygonZone() {
	removePolygonList(districtzone_list);
	removePolygonList(marketzone_list);
	removePolygonList(branchzone_list);
	removePolygonList(editzone_list);
}

/*
 *  Polygon 검색 리스트 모두 지우기
 */
function removeAllSearchPolygonZone() {
	removePolygonList(districtzone_search_list);
	removePolygonList(marketzone_search_list);
	removePolygonList(branchzone_search_list );
}


/*
 *  편집중 Zone Polygon 원래대로 되돌리기
 */
function removeEditZone() {
	if(editzone_list.length > 0 ){
		var zone  = editzone_list[0];
		var id = zone.id;
		
		//console.log(editzone_list);
		removePolygonList(editzone_list);
		
		resetEditingPolygon(districtzone_list, id);
		resetEditingPolygon(marketzone_list, id);
		resetEditingPolygon(branchzone_list, id);
			
		editzone_list = new Array();
	}
}

/*
 * GeoZone Data를 생성
 * GeoZone Data를 생성하고 DB에 생성 요청
 */
function insertGeoZoneData(zone_data, callback) {
	//console.log(zone_data.coords_array);
	var tmp_zone_list = [zone_data];
	var zone = JSON.stringify(tmp_zone_list);
	//console.log("JSON Stringify zone 데이터 >>>>>");
	//console.log(zone);
	$.post("/admin/zone_data.do", {
		zone:zone
	}, function(result) {
		//console.log(result);
		var zone = result.zone_array[0];
		var coords = zone.coords_array;
		
		zone_data.id = zone.id;
		var coords_array = zone_data.coords_array;
		
		for (var j = 0; j < coords_array.length; j++) {
			coords_array[j].zone_id = coords[j].zone_id;
			coords_array[j].id = coords[j].id;
		}
		//zone_list.push(zone_data);
		
		if (callback != null) {
			//callback(zone_data.id);
			callback(zone_data.name);
		}
		
	});
}

/*
 * GeoZone 행정존 Data를 생성
 * GeoZone 행정존  Data를 생성하고 DB에 생성 요청
 */
function insertGeoDistrictZoneData(zone_data, callback) {
	//console.log(zone_data.coords_array);
	var tmp_zone_list = [zone_data];
	var zone = JSON.stringify(tmp_zone_list);
	//console.log(zone);
	$.post("/admin/geo_district_zone_data.do", {
		zone:zone
	}, function(result) {
		//console.log("행정존 입력 func");
		//console.log(result);
		var zone = result.zone_array[0];
		var coords = zone.coords_array;
		
		zone_data.id = zone.id;                                                                                  
		var coords_array = zone_data.coords_array;
		
		for (var j = 0; j < coords_array.length; j++) {
			coords_array[j].zone_id = coords[j].zone_id;
			coords_array[j].id = coords[j].id;
		}
		//zone_list.push(zone_data);
		
		if (callback != null) {
			//callback(zone_data.id);
			callback(zone_data.name);
		}
		
	});
}


/*
 * GeoZone 행정동 Data를 생성
 * GeoZone 행정동  Data를 생성하고 DB에 생성 요청
 */
function insertGeoDistrictDongData(zone_data, callback) {
	//console.log(zone_data.coords_array);
	var tmp_zone_list = [zone_data];
	var zone = JSON.stringify(tmp_zone_list);
	//console.log(zone);
	$.post("/admin/geo_district_dong_data.do", {
		zone:zone
	}, function(result) {
		//console.log("행정동 입력 func");
		//console.log(result);
		var zone = result.zone_array[0];
		var coords = zone.coords_array;
		
		zone_data.id = zone.id;
		var coords_array = zone_data.coords_array;
		
		for (var j = 0; j < coords_array.length; j++) {
			coords_array[j].zone_id = coords[j].zone_id;
			coords_array[j].id = coords[j].id;
		}
		//zone_list.push(zone_data);
		
		if (callback != null) {
			//callback(zone_data.id);
			callback(zone_data.name);
		}
		
	});
}

/*
 * Zone Data를 조회
 * Zone Data를 DB에 요청하고, 결과를 관리
 */
function getGeoZoneData(callback) {
	$.get("/admin/geo_zone_data.do", {
	}, function(result) {
		//console.log(result);
		zone_list = result.zone_array;
		
		callback();
	});
}


/*
 * GeoZone Data를 조회
 * GeoZone Data를 DB에 요청하고, 결과를 관리
 */
function getDistrictZoneData(zone_id, callback) {
	//console.log(zone_id);
	
	$.ajax({
		type: 'GET',
		url: '/v1/zms/zone/'+zone_id+'.do',
		dataType: "json",
		headers: {"access_token" : g_access_token, "request_type" : "2", "x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data: { zone_id : zone_id },
		success: function(result) {
			//console.log(result);
			callback(result.zone_array[0]);
		},
		error: function (e) {
			bootbox.alert(e);
		} 
	});
	
}

/*
 * GeoZone Data를 조회
 */
function getBranchZoneData(zone_id, callback) {
	var result;
	
	$.ajax({
		type: 'GET',
		url: '/v1/zms/zone/'+zone_id+'.do',
		dataType: "json",
		headers: {"access_token" : g_access_token, "request_type" : "2", "x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data: { zone_id : zone_id },
		success: function(result) {
			//console.log(result);
			//console.log(result.zone_array[0]);
			
			callback(result.zone_array[0]);
			
		},
		error: function (e) {
			bootbox.alert(e);
		} 
	});
	
}

/*
 * GeoZone Data를 업데이트
 * GeoZone Data를 업데이트하고 DB에 업데이트 요청
 */
function updateGeoZoneData(zone_array, zone_obj, callback) {
	if (zone_array == null) {
		return;
	}
	var zone_data = JSON.stringify(zone_array);
	//console.log(zone_data);
	$.ajax({
		url: "/admin/zone_data.do",
		datatype:'json',
		type: 'PUT',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:zone_data,
		success: function(result) {
			//console.log(result);
			
			if(zone_obj != undefined ){
				updateZoneCoords(zone_obj);
			}
			
			if (callback != null)
				callback(zone_array[0]);
		}
	});
}

/*
 * GeoZone 행정존 Data를 업데이트
 * GeoZone 행정존  Data를 업데이트하고 DB에 생성 요청
 */
function updateGeoDistrictZoneData(zone_data, callback) {
	
	var zone = new Array();
	zone.push(zone_data); 
		
	$.ajax({
		url: "/admin/geo_district_zone_data.do",
		datatype:'json',
		type: 'PUT',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:JSON.stringify(zone),
		success: function(result) {
			//console.log(result);
			var zone = result.zone_array[0];
			var coords = zone.coords_array;
			
			zone_data.id = zone.id;                                                                                  
			var coords_array = zone_data.coords_array;
			
			for (var j = 0; j < coords_array.length; j++) {
				coords_array[j].zone_id = coords[j].zone_id;
				coords_array[j].id = coords[j].id;
			}
			
			if (callback != null) {
				callback(zone);
			}
		}
	});
}


/*
 * GeoZone Data를 삭제
 * GeoZone Data를 삭제하고 DB에 삭제 요청
 */
function deleteGeoZoneData(zone, callback) {
	$.ajax({
		url: "/admin/zone_data.do",
		datatype:'text',
		type: 'DELETE',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:zone,
		success: function(result) {
			//console.log(result);
			
			for (var i = 0; i < zone_list.length; i++) {
				if (zone_list[i].id == result.zone_id_array[0].zone_id) {
					zone_list.splice(i, 1);
				}
			}
			callback();
		}
	});
}
   
/*
 *  주소 검색(
 */ 
function setCenterAddress(address) {
	geocoder.geocode( { 'address': address}, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			map.setCenter(results[0].geometry.location);
			//console.log(results[0].geometry.location);
		} else {
			swal({
				 title: "요청 결과를 찾을 수 없습니다. 주소를 다시 입력하여 주시기 바랍니다.", 
			     type: "error",
			     confirmButtonText: "확인"
			})
			/*bootbox.alert("요청 결과를 찾을 수 없습니다. 주소를 다시 입력하여 주시기 바랍니다.");*/
		}
	});
}

/*
 *  주소 값 생성 
 */ 
function getGeoAddress(zone_obj, latlng, callback) {
	//console.log(latlng);
	var address;
	geocoder.geocode({'location': latlng}, function(results, status) {
		if (status === google.maps.GeocoderStatus.OK) {
			//console.log(results);
			address = results[1].formatted_address;
			//console.log(address);
			zone_obj.address = address 
			callback();
		}else{
			swal({
				 title: "적절한 주소 값을 찾을 수 없습니다,  존을 다시 그려주세요.", 
			     type: "error",
			     confirmButtonText: "확인"
			})
			/*bootbox.alert('적절한 주소 값을 찾을 수 없습니다,  존을 다시 그려주세요.')*/
		} 
	});
}

/*
 *  GeoZone Data를 검색 
 */ 
function searchGeoZoneData(search_word, callback ) {
	//console.log(search_word);
	$.ajax({
		type: 'GET',
		url: '/v1/zms/geozone.do',
		dataType: "json",
		headers: {"access_token" : g_access_token, "request_type" : "2", "x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data: { search_filter : search_word },
		success: function(result) {
			//console.log(result);
			
			callback(result.zone_array, search_word);
		},
		error: function (e) {
			bootbox.alert(e);
		} 
	});
}

/*
 *  시군구 및 행정동 Data를 검색 
 */ 
function searchDongData(search_word, callback ) {
	//console.log(search_word);
	$.ajax({
		type: 'GET',
		url: '/v1/zms/dong_search.do',
		dataType: "json",
		headers: {"access_token" : g_access_token, "request_type" : "2", "x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data: { search_filter : search_word },
		success: function(result) {
			//console.log(result);
			
			callback(result.zone_array, search_word);
		},
		error: function (e) {
			bootbox.alert(e);
		} 
	});
}

/*
 * 내 위치로 가기 버튼 
 */
function addMyLocationButton(map) {
	var controlDiv = document.createElement('div');
	
	var firstChild = document.createElement('button');
	firstChild.style.backgroundColor = '#fff';
	firstChild.style.border = 'none';
	firstChild.style.outline = 'none';
	firstChild.style.width = '28px';
	firstChild.style.height = '28px';
	firstChild.style.borderRadius = '2px';
	firstChild.style.boxShadow = '0 1px 4px rgba(0,0,0,0.3)';
	firstChild.style.cursor = 'pointer';
	firstChild.style.marginTop = '10px';
	firstChild.style.marginRight = '10px';
	firstChild.style.padding = '0px';
	firstChild.title = 'Your Location';
	controlDiv.appendChild(firstChild);
	
	var secondChild = document.createElement('div');
	secondChild.style.margin = '5px';
	secondChild.style.width = '18px';
	secondChild.style.height = '18px';
	secondChild.style.backgroundImage = 'url(https://maps.gstatic.com/tactile/mylocation/mylocation-sprite-1x.png)';
	secondChild.style.backgroundSize = '180px 18px';
	secondChild.style.backgroundPosition = '0px 0px';
	secondChild.style.backgroundRepeat = 'no-repeat';
	secondChild.id = 'you_location_img';
	firstChild.appendChild(secondChild);
	
	google.maps.event.addListener(map, 'dragend', function() {
		$('#you_location_img').css('background-position', '0px 0px');
	});

	firstChild.addEventListener('click', function() {
		if(navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function(position) {
				var latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
				map.setCenter(latlng);
			});
		}else{
			swal({
				 title: "현재 위치를 찾을 수 없습니다.", 
			     type: "error",
			     confirmButtonText: "확인"
			})
			/*bootbox.alert('현재 위치를 찾을 수 없습니다.');*/
			return;
		}
	});
	
	controlDiv.index = 1;
	map.controls[google.maps.ControlPosition.TOP_RIGHT].push(controlDiv);
}

/*
 *  Geo검색 첫번째 존으로 맵 이동
 */
function geoSearchZoneMove(zone){
	//console.log('[geoSearchZoneMove ] zone >>>>> ' );
	//console.log(zone);
	
	var pos = { lat : null, lng : null };

	pos.lat = zone.center_x;
	pos.lng = zone.center_y;
	
	map.setCenter(pos);
}


function ftGetGeoCompanyList(success_callback, error_callback) {
	var req_url =  "/v1/cbms/company.do?company_id=-1";
	$.ajax({
		method: "GET",
		url: req_url,
		dataType: "json",
		headers: {"access_token" : g_access_token, "request_type" : "2", "x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		async: false, 
		success: function(resp) {
			success_callback(resp);
		},
		error: function (e) {
			error_callback(e);
		} 
	});
}

function ftGetGeoCompanyBrandList(company_id, success_callback, error_callback) {
	var req_url = "/v1/cbms/brand.do?company_id=" + company_id + "&brand_id=-1";
	$.ajax({
		method: "GET",
		url: req_url,
		dataType: "json",
		headers: {"access_token" : g_access_token, "request_type" : "2", "x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		async: false,
		success: function(resp) {
			//console.log(resp);
			success_callback(resp);
		},
		error: function (e) {
			error_callback(e);
		} 
	});
}

function ftGetGeoBranchList(company_id, brand_id, success_callback, error_callback) {
	var req_url = "/v1/cbms/branch.do?company_id=" + company_id + "&brand_id=" + brand_id + "&branch_id=-1";
	$.ajax({
		method: "GET",
		url: req_url,
		dataType: "json",
		headers: {"access_token" : g_access_token, "request_type" : "2", "x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		async: false,
		success: function(resp) {
			//console.log(resp);
			success_callback(resp);
		},
		error: function (e) {
			error_callback(e);
		} 
	});
}



var DEFAULT_RADIUS = 500;
var map_img;

function drawMapImage(src, latlng, angle, radius, ratio, rotate_callback, scale_callback) {
	//console.log(latlng);
	if (radius == 0) {
		radius = DEFAULT_RADIUS
	}
	//console.log("drawMapImage");
	//console.log(ratio);
	var coords = getPolygonCoords(latlng, 0, radius, ratio);
	//console.log(coords);
	var bounds = getBounds(coords);
	//console.log(bounds);
	map_img = new USGSOverlay(bounds, angle, radius, ratio, src, map);
	map_img.setRotationCallback(rotate_callback);
	map_img.setScaleCallback(scale_callback);
	
	map_img.setMap(map);
}

function initOverlay() {
	USGSOverlay.prototype = new google.maps.OverlayView();

	/**
	 * onAdd is called when the map's panes are ready and the overlay has been
	 * added to the map.
	 */
	USGSOverlay.prototype.onAdd = function() {
		var overlay = this;
		
		//console.log(overlay);

		var div = document.createElement('div');
		div.style.border = 'none';
		div.style.borderWidth = '0px';
		div.style.position = 'absolute';

		// Create the img element and attach it to the div.
		var img = document.createElement('img');
		img.src = this.image_;
		img.style.width = '100%';
		img.style.height = '100%';
		div.appendChild(img);

		this.div_ = div;
		this.div_.style.transform = "rotate("+this.angle_+"deg)";

		// Add the element to the "overlayImage" pane.
		var panes = this.getPanes();
		panes.overlayImage.appendChild(this.div_);
		
		var ne = this.bounds_.getNorthEast();
		var sw = this.bounds_.getSouthWest();
		
		var center = getCenterCoords(ne,sw);
		//console.log("onAdd");
		//console.log(this.ratio_);
		var coords = getPolygonCoords(center, this.angle_, this.radius_, this.ratio_);
		
		var polygon = new google.maps.Polygon({
			path:coords,
			strokeColor:BRANCH_COLOR,
			fillColor:BRANCH_COLOR,
			fillOpacity:DEFAULT_OPACITY,
			draggable:true,
		});
		
		this.polygon_ = polygon;
		
		polygon.setMap(this.map_);
		
		
		google.maps.event.addListener(polygon, "drag", function(event) {
			//console.log('polygon drag');
			var cx = polygon.getPath().getAt(1).lat() + (polygon.getPath().getAt(3).lat() - polygon.getPath().getAt(1).lat()) / 2;
			var cy = polygon.getPath().getAt(1).lng() + (polygon.getPath().getAt(3).lng() - polygon.getPath().getAt(1).lng()) / 2
			var center = new google.maps.LatLng(cx,cy);
			//console.log("polygon drag");
			//console.log(overlay.getRatio());
			var coords = getPolygonCoords(center, 0, overlay.getRadius(), overlay.getRatio());
			overlay.setBounds(getBounds(coords));
		});
		
		google.maps.event.addListener(polygon, "dragend", function(event) {
		});
		
		var circle_coords = getRotatePoint(center, this.angle_, this.radius_ * (this.ratio_ * 0.75));
		
		var circle = new google.maps.Circle({
			center:circle_coords,
			radius: 10,
			fillOpacitiy:1,
			fillColor:'#000000',
			draggable:true
		});
		
		this.circle_ = circle;
		
		circle.setMap(this.map_);
		
		var circle_center;
		google.maps.event.addListener(circle, "dragstart", function(event) {
			circle_center = {lat: circle.get('center').lat(), lng:  circle.get('center').lng()};
			circle.ox = circle.get('center').lat();
			circle.oy = circle.get('center').lng();
			circle.ocenter = new google.maps.LatLng(circle.ox,circle.oy);
			//console.log('mousedown');
		});
		google.maps.event.addListener(circle, "drag", function(event) {
			//console.log('drag');
			circle_center = {lat: this.ox, lng:  circle.get('center').lng()};
			circle.setCenter(circle_center);
			var dir = 0;
			if (circle.oy - circle.get('center').lng() < 0) {
				dir = 1;
			} else if (circle.oy - circle.get('center').lng() > 0) {
				dir = -1;
			}
			var angle = overlay.getAngle();
			angle += 1 * dir;
			overlay.setRotation(angle);
			
			this.ox = circle.get('center').lat();
			this.oy = circle.get('center').lng();
		});
		google.maps.event.addListener(circle, "dragend", function(event) {
			var cx = polygon.getPath().getAt(1).lat() + (polygon.getPath().getAt(3).lat() - polygon.getPath().getAt(1).lat()) / 2;
			var cy = polygon.getPath().getAt(1).lng() + (polygon.getPath().getAt(3).lng() - polygon.getPath().getAt(1).lng()) / 2
			var center = new google.maps.LatLng(cx,cy);
			
			var direction5 = parseInt(parseInt(overlay.getAngle()));
			var radius5 = parseInt(overlay.getRadius() * (overlay.getRatio() * 0.75));
			var end_point5 = new google.maps.geometry.spherical.computeOffset(center, radius5, direction5);
			
			circle.setCenter(end_point5);
		});
		
		var scale_circle_coords_sw = new google.maps.LatLng(coords[1].lat(), coords[1].lng());
		var scale_circle_coords_nw = new google.maps.LatLng(coords[2].lat(), coords[2].lng());
		var scale_circle_coords_ne = new google.maps.LatLng(coords[3].lat(), coords[3].lng());
		var scale_circle_coords_se = new google.maps.LatLng(coords[4].lat(), coords[4].lng());
		var scale_circle_sw = new google.maps.Circle({
			center:scale_circle_coords_sw,
			radius: 10,
			fillOpacitiy:1,
			fillColor:'#000000',
			draggable:true
		});
		var scale_circle_nw = new google.maps.Circle({
			center:scale_circle_coords_nw,
			radius: 10,
			fillOpacitiy:1,
			fillColor:'#000000',
			draggable:true
		});
		var scale_circle_ne = new google.maps.Circle({
			center:scale_circle_coords_ne,
			radius: 10,
			fillOpacitiy:1,
			fillColor:'#000000',
			draggable:true
		});
		var scale_circle_se = new google.maps.Circle({
			center:scale_circle_coords_se,
			radius: 10,
			fillOpacitiy:1,
			fillColor:'#000000',
			draggable:true
		});
		
		this.scale_circle_ = [];
		this.scale_circle_[0] = scale_circle_sw;
		this.scale_circle_[1] = scale_circle_nw;
		this.scale_circle_[2] = scale_circle_ne;
		this.scale_circle_[3] = scale_circle_se;
		scale_circle_sw.setMap(this.map_);
		scale_circle_nw.setMap(this.map_);
		scale_circle_ne.setMap(this.map_);
		scale_circle_se.setMap(this.map_);
		
		for (var i = 0; i < this.scale_circle_.length; i ++) {
			var s_circle = this.scale_circle_[i];
			
			google.maps.event.addListener(s_circle, "drag", function(event) {
				var sp = polygon.getPath().getAt(1);
				var ep = polygon.getPath().getAt(3);
				
				//console.log(polygon.getPath());
				var center = getCenterCoords(sp,ep);
				var distance = calcDistance(center.lat(), center.lng(), this.getCenter().lat(), this.getCenter().lng());
				//console.log(distance);
				//console.log(Math.sqrt(Math.pow(distance * 2, 2) * 0.8));
				
				var radius = Math.round(Math.sqrt(Math.pow(distance * 2, 2) * 0.8) + 0.5);
				
				overlay.setRadius(radius);
				
				//console.log("scale_circle drag");
				//console.log(overlay.getRatio());
				var coords = getPolygonCoords(center, 0, radius, overlay.getRatio());
				
				var bounds = getBounds(coords);
				
				overlay.setBounds(bounds);
			});
		}
	};

	USGSOverlay.prototype.draw = function() {

		// We use the south-west and north-east
		// coordinates of the overlay to peg it to the correct position and size.
		// To do this, we need to retrieve the projection from the overlay.
		var overlayProjection = this.getProjection();

		// Retrieve the south-west and north-east coordinates of this overlay
		// in LatLngs and convert them to pixel coordinates.
		// We'll use these coordinates to resize the div.
		var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_.getSouthWest());
		var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast());

		// Resize the image's div to fit the indicated dimensions.
		var div = this.div_;
		div.style.left = sw.x + 'px';
		div.style.top = ne.y + 'px';
		div.style.width = (ne.x - sw.x) + 'px';
		div.style.height = (sw.y - ne.y) + 'px';
	};

	USGSOverlay.prototype.onRemove = function() {
		this.div_.parentNode.removeChild(this.div_);
	};

	USGSOverlay.prototype.setBounds = function(bounds) {
		//console.log('setBounds');
		this.bounds_ = bounds;
		//console.log(this.bounds_.getSouthWest());
		//console.log(this.bounds_.getNorthEast());
		//console.log(this.getProjection());

		// We use the south-west and north-east
		// coordinates of the overlay to peg it to the correct position and size.
		// To do this, we need to retrieve the projection from the overlay.
		var overlayProjection = this.getProjection();

		// Retrieve the south-west and north-east coordinates of this overlay
		// in LatLngs and convert them to pixel coordinates.
		// We'll use these coordinates to resize the div.
		var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_.getSouthWest());
		var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast());

		// Resize the image's div to fit the indicated dimensions.
		var div = this.div_;
		div.style.left = sw.x + 'px';
		div.style.top = ne.y + 'px';
		div.style.width = (ne.x - sw.x) + 'px';
		div.style.height = (sw.y - ne.y) + 'px';
		
		var angle = this.angle_;
		var radius = this.radius_;
		
		var swl = this.bounds_.getSouthWest();
		var nel = this.bounds_.getNorthEast();
		var center = getCenterCoords(nel,swl);
		//console.log("setBounds");
		//console.log(this.ratio_);
		var poly_coords = getPolygonCoords(center, angle, radius, this.ratio_);
		this.polygon_.setPath(poly_coords);
		
		
		
		var circle_point = getRotatePoint(center, angle, radius * (this.ratio_ * 0.75));
		this.circle_.setCenter(circle_point);
		
		this.scale_circle_[0].setCenter(poly_coords[1]);
		this.scale_circle_[1].setCenter(poly_coords[2]);
		this.scale_circle_[2].setCenter(poly_coords[3]);
		this.scale_circle_[3].setCenter(poly_coords[4]);
		
		this.scale_callback_(radius);
	};

	USGSOverlay.prototype.setRotation = function(deg) {
		//var angle = this.getAngle();
		var angle = deg;
		var radius = this.getRadius();
		//angle += deg;
		this.div_.style.transform = "rotate("+angle+"deg)";

		var sw = this.bounds_.getSouthWest();
		var ne = this.bounds_.getNorthEast();
		
		var center = getCenterCoords(ne,sw);

		//console.log(angle % 360);
		this.setAngle(angle % 360);
		//console.log("rotation");
		//console.log(this.ratio_);
		var coords = getPolygonCoords(center, angle, radius, this.ratio_);

		this.polygon_.setPath(coords);
		
		
		var circle_point = getRotatePoint(center, angle, radius * (this.ratio_ * 0.75));
		
		this.circle_.setCenter(circle_point);
		
		this.scale_circle_[0].setCenter(coords[1]);
		this.scale_circle_[1].setCenter(coords[2]);
		this.scale_circle_[2].setCenter(coords[3]);
		this.scale_circle_[3].setCenter(coords[4]);
		
		////console.log(this.getRotationCallback());
		//if (this.getRotationCallback() != null) {
			this.rotation_callback_(deg);
		//}
	};
	USGSOverlay.prototype.getAngle = function() {
		return this.angle_;
	};
	USGSOverlay.prototype.getRadius = function() {
		return this.radius_;
	}
	USGSOverlay.prototype.getRatio = function() {
		return this.ratio_;
	}
	USGSOverlay.prototype.setAngle = function(angle) {
		this.angle_ = angle;
	}
	USGSOverlay.prototype.setRadius = function(radius) {
		this.radius_ = radius;
	}
	USGSOverlay.prototype.getCenter = function() {
		var ne = this.bounds_.getNorthEast();
		var sw = this.bounds_.getSouthWest();
		
		var center = getCenterCoords(ne,sw);
		return center;
	}
	USGSOverlay.prototype.setRotationCallback = function(callback) {
		//console.log("set rotation callback");
		this.rotation_callback_ = callback;
	}
	USGSOverlay.prototype.setScaleCallback = function(callback) {
		//console.log("set scale callback");
		this.scale_callback_ = callback;
	}
	USGSOverlay.prototype.getRotationCallback = function() {
		return this.rotation_callback_;
	}
	USGSOverlay.prototype.getMapParam = function() {
		//console.log("getMapParam()");
		var map_info = [];
		map_info["angle"] = this.angle_;
		map_info["coord_0_x"] = this.scale_circle_[1].getCenter().lat();
		map_info["coord_0_y"] = this.scale_circle_[1].getCenter().lng();
		map_info["coord_1_x"] = this.scale_circle_[2].getCenter().lat();
		map_info["coord_1_y"] = this.scale_circle_[2].getCenter().lng();
		map_info["coord_2_x"] = this.scale_circle_[3].getCenter().lat();
		map_info["coord_2_y"] = this.scale_circle_[3].getCenter().lng();
		map_info["coord_3_x"] = this.scale_circle_[0].getCenter().lat();
		map_info["coord_3_y"] = this.scale_circle_[0].getCenter().lng();
		map_info["center_x"] = (this.scale_circle_[0].getCenter().lat() + this.scale_circle_[1].getCenter().lat() + this.scale_circle_[2].getCenter().lat() + this.scale_circle_[3].getCenter().lat()) / 4;
		map_info["center_y"] = (this.scale_circle_[0].getCenter().lng() + this.scale_circle_[1].getCenter().lng() + this.scale_circle_[2].getCenter().lng() + this.scale_circle_[3].getCenter().lng()) / 4;
		map_info["width"] = calcDistance(
				this.scale_circle_[1].getCenter().lat(),
				this.scale_circle_[1].getCenter().lng(),
				this.scale_circle_[2].getCenter().lat(),
				this.scale_circle_[2].getCenter().lng());
		map_info["height"] = calcDistance(
				this.scale_circle_[1].getCenter().lat(),
				this.scale_circle_[1].getCenter().lng(),
				this.scale_circle_[0].getCenter().lat(),
				this.scale_circle_[0].getCenter().lng());
		return map_info;
	}
}

function getMapInfo() {
	//console.log(map_img.getMapParam());
	return map_img.getMapParam();
}

function calcDistance(lat1,lon1,lat2,lon2) {
	var R = 6371; // km
	var dLat = (lat2-lat1) * Math.PI / 180;
	var dLon = (lon2-lon1) * Math.PI / 180;
	var lat1 = lat1 * Math.PI / 180;
	var lat2 = lat2 * Math.PI / 180;

	var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	var d = R * c * 1000;
	
	d += 0.5;
	d = Math.round(d);
	
	return d;
}

function rotateMap(angle) {
	map_img.setRotation(angle);
}

function scaleMap(width) {
	map_img.setRadius(width);
	
	var center = map_img.getCenter();
	var coords = getPolygonCoords(center, 0, width, map_img.getRatio());
	
	var bounds = getBounds(coords);
	
	map_img.setBounds(bounds);
}

function getCenterCoords(ne, sw) {
	var cx = sw.lat() + (ne.lat() - sw.lat()) / 2;
	var cy = sw.lng() + (ne.lng() - sw.lng()) / 2;
	var center = new google.maps.LatLng(cx,cy);
	
	return center;
}

function getRotatePoint(start_point, direction, radius) {
	var end_point = new google.maps.geometry.spherical.computeOffset(start_point, radius, direction);
	return end_point;
}

function getPolygonCoords(start_point, dangle, radius, ratio) {
	//console.log(start_point.lat());
	//console.log(start_point.lng());
	//console.log(dangle);
	//console.log(radius);
	var coords = [];

	var direction = dangle;
	var radis = radius / 2;

	var end_point = getRotatePoint(start_point, parseInt(direction)+180, parseInt(parseFloat(radius)*(ratio)/2));
	//console.log(end_point.lat());
	//console.log(end_point.lng());
	coords.push(end_point);

	var end_point1 = getRotatePoint(end_point, parseInt(direction - 90), parseInt(parseFloat(radius)/2));
	coords.push(end_point1);

	var end_point3 = getRotatePoint(end_point1, parseInt(direction), parseInt(parseFloat(radius)*ratio));
	coords.push(end_point3);

	var end_point2 = getRotatePoint(end_point3, parseInt(direction+90), parseFloat(radius));
	coords.push(end_point2);

	var end_point4 = getRotatePoint(end_point2, parseInt(direction+180), parseInt(parseFloat(radius)*ratio));
	coords.push(end_point4);

	return coords;
}

function getBounds(coords) {
	var bounds = new google.maps.LatLngBounds(
			new google.maps.LatLng(coords[1].lat(), coords[1].lng()),
			new google.maps.LatLng(coords[3].lat(), coords[3].lng())
	);
	return bounds;
}

/** @constructor */
function USGSOverlay(bounds, angle, radius, ratio, image, map) {

	// Now initialize all properties.
	this.bounds_ = bounds;
	this.image_ = image;
	this.map_ = map;
	this.ratio_ = ratio;
	this.angle_ = angle;
	this.polygon_ = null;
	this.radius_ = radius;
	this.circle_ = null;
	this.scale_circle_ = [];
	this.rotation_callback_ = null;
	this.scale_callback_ = null;

	// Define a property to hold the image's div. We'll
	// actually create this div upon receipt of the onAdd()
	// method so we'll leave it null for now.
	this.div_ = null;

	// Explicitly call setMap on this overlay
	this.setMap(map);
}
