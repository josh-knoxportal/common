var ICON_IMAGE = "http://localhost:8080/v1/mms/resources/test_icon.jpg";
var KEY_ICON = 'icon';
var KEY_OBJECT = 'object';
var KEY_BEACON = 'beacon';
var KEY_VERTEX = 'vertex';
var KEY_FIXED_VERTEX = 'fixed_vertex';

/*
 * Icon 가로, 세로 크기
 */
var ICON_WIDTH = 50;
var ICON_HEIGHT = 50;

var BEACON_GAP_SIZE = 100;

/*
 * Zone Node 반경
 */
var NODE_CIRCLE_RADIUS = 20;
var PATH_STROKE_WIDTH = 20;

/*
 * Color 정의
 */
var NODE_COLOR = '#FFFFFF';
var OBJECT_COLOR = '#FF0000';
var BEACON_COLOR = '#0000FF';		// validity = 2
var BEACON_GRAY_COLOR = '#333333';		// validity = 8
var BEACON_WHITE_COLOR = '#FFFFFF';		// validity = 1
var BEACON_ORANGE_COLOR = '#FFA500';		// validity = 3
var BEACON_RED_COLOR = '#FF5555';		// proximity beacon type
var BEACON_PROX_CIRCLE_COLOR = '#FF5555';
var GRAPH_VERTEX_COLOR = '#FFFF00';
var GRAPH_VERTEX_INIT_COLOR = '#00FFFF';
var GRAPH_END_VERTEX_COLOR = '#FF8822';
var GRAPH_FIXED_VERTEX_COLOR = "#0000FF";
var GRAPH_EDGE_COLOR = '#00FFFF';
var GRAPH_ONE_EDGE_COLOR = '#FF0000';
var GRAPH_CONNECTION_EDGE_COLOR = '#FF6666';
var SELECTED_GRAPH_VERTEX_COLOR = '#FF00FF';
var SELECTED_GRAPH_EDGE_COLOR = '#FF00FF';
var SELECTED_BEACON_COLOR = '#FF00FF';

var oPaper;

/*
 * Mouse Event 저장할 임시 객체
 * startX, startY = Mouse Down 위치 저장
 * mousedown, mousemove = Mouse 움직임 저장
 * dx, dy = Mouse Event Drag 거리 저장
 * oX, oY = ViewBox 처음 위치
 * oWidth, oHeight = ViewBox 가로, 세로 처음 크기
 * viewBox = Image를 보여주고있는 박스 정보
 * zoom = Zoom 레벨
 */
var startX,startY;
var mousedown = false;
var mousemove = false;
var dX,dY;
var oX = 0, oY = 0;
var oWidth;
var oHeight;
var viewBox;
var zoom = 0;

/*
 * Object Data 리스트
 */
var object01 = {coord_x : 125, coord_y : 100};
var object02 = {coord_x : 500, coord_y : 50};
var object03 = {coord_x : 875, coord_y : 50};
var object04 = {coord_x : 115, coord_y : 425};
var object05 = {coord_x : 365, coord_y : 560};
var object06 = {coord_x : 850, coord_y : 250};
var object07 = {coord_x : 760, coord_y : 450};
var object08 = {coord_x : 500, coord_y : 325};

/*
 * Beacon Data 리스트
 */
var beacon_list = [];

/*
 * Graph Data 정
 */
var graph_data;

/*
 * Vertex Data 리스트
 */
var vertex_list;
var fixed_vertex_list;

/*
 * Edge Data 리스트
 */
var edge_list;

 /*
  * Layer 컨트롤 Flag 
  */
var enableObjectLayer = true;
var enableBeaconLayer = true;
var enableGraphLayer = true;
var enableModObjectLayer = false;
var enableModBeaconLayer = false;
var enableModGraphLayer = false;
var selectedVertex = false;
var createZoneMode = false;

/*
 * Edge 생성시 필요한 임시 저장 객체 
 */
var graph_count = 0;
var selected_v1;
var selected_v2;

var onVertexClickCallback;
var onEdgeClickCallback;
var onBeaconClickCallback;

var tmp_update_vertex = [];
var tmp_update_edge = [];
var tmp_update_beacon = {};

var selectedEdge;
var selectedBeacon;

/*
 * Map에 그리는 paper 객체 저장
 */
function setPaper(paper) {
	oPaper = paper;
}

/*
 * 여러개의 Object 그리기 
 */
function drawObjects() {
	drawObject(object01);
	drawObject(object02);
	drawObject(object03);
	drawObject(object04);
	drawObject(object05);
	drawObject(object06);
	drawObject(object07);
	drawObject(object08);
}

/*
 * 여러개의 Beacon 그리기 
 */
function drawBeacons(type) {
	var beaconInfo;
	for (var i = 0; i < beacon_list.length; i++) {
		beaconInfo = beacon_list[i];
		if (type == 2 && beaconInfo.type != type) {
			continue;
		}
		var edit = true;
		if (type == 2) {
			edit = false;
		}
		drawBeacon(beaconInfo, edit);
	}
}

function initBeacon(company, branch, floor, beacon_type, callback) {
	tmp_update_beacon = {};
	getBeaconData(company, branch, floor, beacon_type, callback);
	if (beacon_type != 2) {
		$(canvasID).append('<div id="info_txt" style="position: absolute; top: 15px; left: 30px;"><input id="txt_beacon_info" type="text" readonly="readonly" style="bottom: ' + (oHeight + 20) + 'px; width: auto"></div>');
		
		setOnBeaconHideEvent(beaconHideEvent);
		setOnBeaconDeleteEvent(beaconDeleteEvent);
	}
}


function initGraph(map_id, callback) {
	graph_count = 0;
	selectedVertex = false;
	selected_v1 = null;
	selected_v2 = null;
	enableModGraphLayer = false;

	tmp_update_vertex = [];
	tmp_update_edge = [];
	
	getGraphData(map_id, callback);
	
	$(canvasID).append('<div id="editCtrlBtn" style="position: absolute; top: 15px; right: 30px;"><img class="edgeEditbtn" src="/admin/jslib/bootstrap/img/admin/btn_map_edit.png"></div>');
	
	$(canvasID).append('<div id="info_txt" style="position: absolute; width: auto; top: 15px; left: 30px;"><input id="txt_vertex_info" type="text" readonly="readonly" style="width: auto; left: 20px; top: 5px;" ></div>');
	
	$('.edgeEditbtn').click(function() {
		if (!enableModGraphLayer) {
			ctrlModGraphLayer();
			$('.edgeEditbtn').attr('src', '/admin/jslib/bootstrap/img/admin/btn_map_edit_select.png');
		} else {
			offModGraphLayer();
			$('.edgeEditbtn').attr('src', '/admin/jslib/bootstrap/img/admin/btn_map_edit.png');
		}
	});
	
	$('.edgeEditbtn').hover(function(event) {
		if (!enableModGraphLayer) {
			$(this).attr('src', '/admin/jslib/bootstrap/img/admin/btn_map_edit_over.png');
		} else {
			$(this).attr('src', '/admin/jslib/bootstrap/img/admin/btn_map_edit_select_over.png');
		}
	}, function(){
		if (!enableModGraphLayer) {
			$(this).attr('src', '/admin/jslib/bootstrap/img/admin/btn_map_edit.png');
		} else {
			$(this).attr('src', '/admin/jslib/bootstrap/img/admin/btn_map_edit_select.png');
		}
		
	});
	
	setOnVertexHideEvent(vertexHideEvent);
	setOnEdgeHideEvent(edgeHideEvent);
	setOnVertexDeleteEvent(vertexDeleteEvent);
	setOnEdgeDeleteEvent(edgeDeleteEvent);
}

function resizefield(field) {
	var field = document.getElementById(field);
	if (!arguments.callee.storeOrigWidthOnFirstTime) arguments.callee.storeOrigWidthOnFirstTime = field.offsetWidth;
	var newWidth = field.value.length * 7;
	field.style.width = newWidth + "px";
}

function vertexHideEvent(el,fixed) {
	undoVertexData();
	var vertex_color;
	selectedVertex = false;
	select_v1 = null;
	select_v2 = null;
	var v = findVertexData(el.obj_id,fixed);
	//console.log(el.obj_id);
	//console.log(v);
	if (fixed) {
		vertex_color = GRAPH_FIXED_VERTEX_COLOR;
	} else if (v.name.indexOf('NewVertex') > -1 || v.name.length == 0) {
		vertex_color = GRAPH_VERTEX_INIT_COLOR;
	} else if (v.type == 2) {
		vertex_color = GRAPH_END_VERTEX_COLOR;
	} else {
		vertex_color = GRAPH_VERTEX_COLOR;
	}
	el.attr({fill:vertex_color});
}

/*
 * Object 그리기
 */
function drawObject(obj) {
	var r1 = oPaper.circle(obj.coord_x, obj.coord_y, NODE_CIRCLE_RADIUS).attr({fill:OBJECT_COLOR});
	r1.key=KEY_OBJECT;
	
	var obj_icon = oPaper.image(ICON_IMAGE, (obj.coord_x - ICON_WIDTH * 0.5), (obj.coord_y - (ICON_HEIGHT + NODE_CIRCLE_RADIUS * 2)), ICON_WIDTH,ICON_HEIGHT);
	obj_icon.key=KEY_ICON;
	
	var obj_set = paper.set(r1, obj_icon);
	
	setObjectEvent(obj_set);
}

function setObjectEvent(o) {
	var start = function () {
		o.forEach(function(el) {
			if (el.key==KEY_ICON) {
				el.ox = el.attr("x");
				el.oy = el.attr("y");
			} else{
				el.ox = el.attr("cx");
				el.oy = el.attr("cy");
			}
		}, 1);
	},
	move = function (dx, dy) {
		if (enableModObjectLayer) {
			var scale = viewBoxHeight / oHeight;
//			var scaleX = viewBoxWidth / oWidth;
//			var scaleY = viewBoxHeight / oHeight;
			o.forEach(function(el) {
				if (el.key==KEY_ICON) {
					el.attr({x: el.ox + dx * scale, y: el.oy + dy * scale});
				} else {
					el.attr({cx: el.ox + dx * scale, cy: el.oy + dy * scale});
				}
			},1);
		}
	},
	up = function () {};
	
	o.drag(move, start, up);
}

function createBeacon(beacon) {
	beacon_list.push(beacon);
	
	drawBeacon(beacon, true);
}

/*
 * Beacon 그리기
 */
function drawBeacon(obj, edit) {
	var loc_data = obj.beacon_loc;
	
//	var c_y = loc_data.coord_y * 1.75;
//	var c_x = loc_data.coord_x; * 1.75;
//	var coord_x = map_data.width_pixel / 2 + (c_y - map_data.width_pixel / 2);
//	var coord_y = map_data.height_pixel / 2 - (c_x - map_data.height_pixel / 2);
//	loc_data.coord_x = coord_x;
//	loc_data.coord_y = coord_y;
//	
	var color = BEACON_WHITE_COLOR;		// validity = 1
	if( obj.validity == 8 ) {
		color = BEACON_GRAY_COLOR;		// validity = 8
	}
	else if( obj.validity == 3 ) {
		color = BEACON_ORANGE_COLOR;		// validity = 3
	}
	else if( obj.validity == 2 ) {
		color = BEACON_COLOR;			// validity = 2
	}
	if( obj.type == 2 ) {
		color = BEACON_RED_COLOR;		// proximity beacon type
	}
	var r1 = oPaper.circle(loc_data.coord_x, loc_data.coord_y, NODE_CIRCLE_RADIUS).attr({fill:color});
	r1.key=KEY_BEACON;
	r1.obj_id = obj.id;
	r1.beacon_type = obj.type;
	if (edit) {
		setBeaconEvent(r1);
	} else {
		var circle_radius_pixel = calcDistanceToPixel(obj.proximity_cutoff_level);
		var prox_circle = oPaper.circle(loc_data.coord_x, loc_data.coord_y, circle_radius_pixel).attr({fill:BEACON_PROX_CIRCLE_COLOR, opacity: .5});
		r1.prox_circle = prox_circle;
		r1.dblclick(function(e) {
			imgDblClickEvent(e);
		});
		prox_circle.dblclick(function(e) {
			imgDblClickEvent(e);
		});
	}
}

function calcDistanceToPixel(level) {
	var dis = 0; // mm
	if (level == 1) {
		dis = 3000;
	} else if (level == 2) {
		dis = 7000;
	} else if (level == 3){
		dis = 15000;
	} else {
		dis = 1000;
	}
	
	var map = getMap_data();
	var scale = map.scale;
	if (scale == 0) {
		scale = 23.57; // default scale value
	}
	var pixel = dis / scale / 2; // radius
	
	return pixel;
}

function setBeaconEvent(b) {
	var start = function () {
		this.ox = this.attr("cx");
		this.oy = this.attr("cy");
		mousemove = false;
	},
	move = function (dx, dy) {
		if (enableModBeaconLayer) {
			var scale = viewBoxHeight / oHeight;
//			var scaleX = viewBoxWidth / oWidth;
//			var scaleY = viewBoxHeight / oHeight;
			//console.log(scale);
			this.attr({cx: this.ox + dx * scale, cy: this.oy + dy * scale});
			mousemove = true;
		}
	},
	up = function () {
		if (enableModBeaconLayer && mousemove) {
			setBeaconState(this);
		}
	},
	click = function() {
		if (!mousemove) {
			//console.log("beacon click");
			selectedBeacon = this;
			hideAllNode();
			this.attr({fill:SELECTED_BEACON_COLOR});
			if (onBeaconClickCallback != null) {
				var beacon_data = findBeaconData(b.obj_id);
				tmp_update_beacon = cloneBeacon(beacon_data);
				onBeaconClickCallback(tmp_update_beacon);
			}
		}
	},
	dblclick = function () {
//		if (enableModBeaconLayer && !mouseMove) {
//			removeBeacon(this);
//		}
	};
	
	b.drag(move, start, up);
	b.click(click);
	b.mouseover(function(){
		var beacon_info = findBeaconData(this.obj_id);
		
		$('#txt_beacon_info').val("minor : " + beacon_info.minor);
		resizefield('txt_beacon_info');
	});
	b.mouseout(function(){
		$('#txt_beacon_info').val('');
		resizefield('txt_beacon_info');
	});
	//b.dblclick(dblclick);
}

function setBeaconState(b) {
//	var size = beacon_list.length;
//	var beacon;
	
	if (tmp_update_beacon == null || tmp_update_beacon.id != b.obj_id) {
		tmp_update_beacon = cloneBeacon(findBeaconData(b.obj_id));
	}
	
//	for (var i = 0; i < size; i++) {
//		beacon = beacon_list[i];
		if (tmp_update_beacon.id == b.obj_id) {
			tmp_update_beacon.beacon_loc.coord_x = b.attr('cx');
			tmp_update_beacon.beacon_loc.coord_y = b.attr('cy');
			updateBeaconData();
//			return;
		}
//	}
}

function disableBeacon(id) {
	//deleteBeaconData(b);
	
//	var tmp;
//	for (var i = 0; i < beacon_list.length; i++) {
//		tmp = beacon_list[i];
//		
//		if (tmp.id == id) {
//			beacon_list.splice(i, 1);
//		}
//	}
	
	var beacon = findBeaconData(id);
	beacon.validity = 8;
	
	var b = findBeacon(id);
	//b.remove();
	b.attr({fill:BEACON_GRAY_COLOR});
	
	tmp_update_beacon = null;
}

function removeBeacon(id) {
	//deleteBeaconData(b);
	
	var tmp;
	for (var i = 0; i < beacon_list.length; i++) {
		tmp = beacon_list[i];
		
		if (tmp.id == id) {
			beacon_list.splice(i, 1);
		}
	}
	
	var b = findBeacon(id);
	b.remove();
	
	tmp_update_beacon = null;
}

/*
 * Graph 그리기 
 */
function drawGraph() {
	var size = vertex_list.length;
	var vertex;
	
	for (var i = 0; i < size; i++) {
		vertex = vertex_list[i];
		drawVertex(vertex, false);
	}
	
	drawPathList(edge_list);
}

/*
 * Vertex 그리기 
 */
function drawVertex(vertex, fixed) {
	var vertex_color;
	if (fixed) {
		vertex_color = GRAPH_FIXED_VERTEX_COLOR;
	} else if (vertex.name.indexOf("NewVertex") > -1 || vertex.name.length == 0) {
		vertex_color = GRAPH_VERTEX_INIT_COLOR;
	} else if (vertex.type == 2) {
		vertex_color = GRAPH_END_VERTEX_COLOR;
	} else {
		vertex_color = GRAPH_VERTEX_COLOR;
	}
//	var coord_x = map_data.width_pixel / 2 + (vertex.coord_y - map_data.width_pixel / 2);
//	var coord_y = map_data.height_pixel / 2 - (vertex.coord_x - map_data.height_pixel / 2);
//	vertex.coord_x = coord_x;
//	vartex.coord_y = coord_y;
//	var r1 = paper.circle(coord_x, coord_y, NODE_CIRCLE_RADIUS).attr({fill:vertex_color});
	var r1 = paper.circle(vertex.coord_x, vertex.coord_y, NODE_CIRCLE_RADIUS).attr({fill:vertex_color});
	if (fixed) {
		r1.key=KEY_FIXED_VERTEX;
	} else {
		r1.key=KEY_VERTEX;
	}
	
	r1.vertex_type = vertex.type;
	r1.vertex_attr = vertex.attr;
	r1.obj_id=vertex.id;
	
	//var text = paper.text(vertex.coord_x, vertex.coord_y+30, vertex.id+":"+vertex.name).attr({"font-size":11, fill: "#0000ff", "stroke-width": 2});
	
	setVertexEvent(r1,fixed);
}

function drawFixedVertex(zone_list) {
	if (fixed_vertex_list == null || fixed_vertex_list.length == 0) {
		return;
	}
	
	var size = fixed_vertex_list.length;
	
	for (var i = 0; i < size; i++) {
		var graph_data = fixed_vertex_list[i];
		var zone = null;
		for (var j = 0; j < zone_list.length; j++) {
			if (zone_list[j].tenant_corner.section_floor_id == graph_data.floor_id) {
				zone = zone_list[j];
			}
		}
		
		if (zone == null) {
			continue;
		}
		
		var sx = zone.sx;
		var sy = zone.sy;
		var ex = zone.ex;
		var ey = zone.ey;
		var width = zone.width;
		var height = zone.height;
		//console.log(sx);
		//console.log(sy);
		//console.log(ex);
		//console.log(ey);
		
		var map_width = graph_data.width_pixel;
		var map_height = graph_data.height_pixel;
		
		var graph_list = graph_data.map_graph_list;
		var graph_size = graph_list.length;
		
		for (var j = 0; j < graph_size; j++) {
			var vertex = graph_list[j];
			
			vertex.coord_x = sx + (vertex.coord_x / map_width) * width;
			vertex.coord_y = sy + (vertex.coord_y / map_height) * height;
			drawVertex(vertex, true);
		}
		
	}
}

/*
 * Vertex Event 설정 
 */
function setVertexEvent(v,fixed) {
	var start = function () {
		this.ox = this.attr("cx");
		this.oy = this.attr("cy");
		mousemove = false;
	},
	move = function (dx, dy) {
		if (enableModGraphLayer && selectedVertex && this.obj_id == selected_v1.obj_id) {
			mousemove = true;
			var scale = viewBoxHeight / oHeight;
//			var scaleX = viewBoxWidth / oWidth;
//			var scaleY = viewBoxHeight / oHeight;
			this.attr({cx: this.ox + dx * scale, cy: this.oy + dy * scale});
			reloadGraph(v.obj_id);
		}
	},
	up = function () {
		if (enableModGraphLayer && mousemove) {
			setEdgeState(this);
			setVertexState(this);
		}
	},
	click = function() {
		if (mousemove) {
			return;
		}
		if (selectedEdge != null) {
			hideAllNode();
		}
		//console.log("vertex click event");
		if (selected_v1 != null && selected_v1.obj_id != this.obj_id) {
			undoVertexData();
		}
		if (onVertexClickCallback != null) {
			onVertexClickCallback(findVertexData(this.obj_id, fixed), false);
		}
		if (enableModGraphLayer) {
			if (!mousemove && !selectedVertex) {
				selectedVertex = true;
				this.attr({fill:SELECTED_GRAPH_VERTEX_COLOR});
				selected_v1 = this;
			} else if (selectedVertex) {
				hideAllNode();
				if (selected_v1.obj_id != this.obj_id) {
					selected_v2 = this;
					//console.log(selected_v1);
					//console.log(selected_v2);
					createDefaultEdge(selected_v1, selected_v2);
				}
				
				selectedVertex=false;
				var color = GRAPH_VERTEX_COLOR;
				if (selected_v1.key == KEY_FIXED_VERTEX) {
					color = GRAPH_FIXED_VERTEX_COLOR;
				} else {
					color = GRAPH_VERTEX_COLOR;
				}
				selected_v1.attr({fill:color});
			}
		} else {
			hideAllNode();
			selectedVertex = true;
			selected_v1 = this;
			this.attr({fill:SELECTED_GRAPH_VERTEX_COLOR});
		}
		$('#txt_vertex_info').val("x:"+this.attr('cx')+",y:"+this.attr('cy'));
		resizefield('txt_vertex_info');
	},
	dblclick = function() {
//		if (enableModGraphLayer && !mouseMove) {
//			var tVertex = paper.getById(findVertex(this.obj_id));
//			
//			removePathByVertex(this.obj_id);
//			
//			removeVertex(tVertex);
//		}
	};
	
	if (fixed) {
		v.click(function() {
			if (selectedEdge != null) {
				hideAllNode();
			}
			//console.log("vertex click event");
			if (selected_v1 != null && selected_v1.obj_id != this.obj_id) {
				undoVertexData();
			}
			if (onVertexClickCallback != null) {
				onVertexClickCallback(findVertexData(this.obj_id, fixed), true);
			}
			if (enableModGraphLayer) {
				if (!selectedVertex) {
					selectedVertex = true;
					this.attr({fill:SELECTED_GRAPH_VERTEX_COLOR});
					selected_v1 = this;
				} else if (selectedVertex) {
					hideAllNode();
					if (selected_v1.obj_id != this.obj_id) {
						selected_v2 = this;
						//console.log(selected_v1);
						//console.log(selected_v2);
						createDefaultEdge(selected_v1, selected_v2);
					}
					
					selectedVertex=false;
					var color = GRAPH_VERTEX_COLOR;
					if (selected_v1.key == KEY_FIXED_VERTEX) {
						color = GRAPH_FIXED_VERTEX_COLOR;
					} else {
						color = GRAPH_VERTEX_COLOR;
					}
					selected_v1.attr({fill:color});
				}
			} else {
				hideAllNode();
				selectedVertex = true;
				selected_v1 = this;
				this.attr({fill:SELECTED_GRAPH_VERTEX_COLOR});
			}
			$('#txt_vertex_info').val("x:"+this.attr('cx')+",y:"+this.attr('cy'));
			resizefield('txt_vertex_info');
		});
	} else {
		v.drag(move, start, up);
		v.click(click);
		v.dblclick(dblclick);
	}
}
/*
 * Vertex 삭제
 */
function removeVertex(id, callback) {
	removePathByVertex(id);
	deleteVertexData(id, callback);
}

/*
 * Default Edge 생성
 */
function createDefaultEdge(v1,v2) {
	if (v1.key == KEY_FIXED_VERTEX && v2.key == KEY_FIXED_VERTEX) {
		 return null;
	}
	
	var sx = v1.attr('cx');
	var sy = v1.attr('cy');
	var ex = v2.attr('cx');
	var ey = v2.attr('cy');
	var dist = calcDistance(sx,sy,ex,ey)
	var vid = v1.obj_id + "|" + v2.obj_id;
	var type = 2;
	var update_vertex;
	var fixed_vertex;
	if (v1.key != v2.key) {
		//console.log(v1.key);
		//console.log(v2.key);
		if (v1.vertex_attr == 4) {
			type = 4;
		} else {
			type = 3;
		}
		
		if (v1.key == KEY_FIXED_VERTEX) {
			update_vertex = v2;
			fixed_vertex = v1;
		} else {
			update_vertex = v1;
			fixed_vertex = v2;
		}
		//console.log(fixed_vertex.obj_id);
		var f_vertex_data = findVertexData(fixed_vertex.obj_id, true);
		//console.log(f_vertex_data);
		alert("vertex[" + update_vertex.obj_id + "]의 정보가 type=" + f_vertex_data.type + ", attr=" + f_vertex_data.attr + "section=" + f_vertex_data.section + "으로 변경됩니다.");
		
		var u_vertex_data = findVertexData(update_vertex.obj_id);
		u_vertex_data = cloneVertex(u_vertex_data);
		u_vertex_data.type = f_vertex_data.type;
		u_vertex_data.attr = f_vertex_data.attr;
		u_vertex_data.section = f_vertex_data.section;
		u_vertex_data.section_name = f_vertex_data.section_name;
		if (f_vertex_data.attr == 4) {
			if (f_vertex_data.direction == 1) {
				u_vertex_data.direction = 2;
			} else if (f_vertex_data.direction == 2) {
				u_vertex_data.direction = 1;
			} else if (f_vertex_data.direction == 3) {
				u_vertex_data.direction = 4;
			} else if (f_vertex_data.direction == 4) {
				u_vertex_data.direction = 3;
			}
		}
		//console.log(u_vertex_data);
		tmp_update_vertex.push(u_vertex_data);
		updateVertexData();
	}
	
	var edge = {name:"edge_"+vid,start_vertex_id:v1.obj_id , end_vertex_id:v2.obj_id, type:type, validity:1, weight:dist, description:"edge_"+vid, creator_id: g_user_id, editor_id: g_user_id};
	
	insertEdgeData(edge);
	
	return edge;
}

/*
 * Edge 제거 
 */
function removeEdge(id, callback) {
	//console.log("removeEdge");
	//console.log(id);
	var edge_data = findEdgeData(id);
	var vid = edge_data.start_vertex_id + '|' + edge_data.end_vertex_id;
	var edge = paper.getById(findEdge(vid));
	var split_list = edge.vid.split("|");
	
	var v1 = split_list[0];
	var v2 = split_list[1];
	
	var tmp;
	var rEdge;
	
	for (var i = 0; i < edge_list.length; i++) {
		tmp = edge_list[i];
		
		if (tmp.start_vertex_id == v1 && tmp.end_vertex_id == v2) {
			rEdge = tmp;
			edge_list.splice(i, 1);
		}
	}
	
	deleteEdgeData(""+rEdge.id, callback);
	
	edge.remove();
}

/*
 * Edge 제거
 * Vertex가 제거되면 연결된 Edge 삭제
 */
function removePathByVertex(id) {
	var remove_list = new Array();
	var obj_list = new Array();
	var dObj = "";
	
	var pathSize = edge_list.length;
	var path;
	var obj_id;
	var edge;
	
	for (var i = 0; i < pathSize; i++) {
		path = edge_list[i];
		
		if (path.start_vertex_id == id || path.end_vertex_id == id) {
			obj_id = path.start_vertex_id+"|"+path.end_vertex_id;
			remove_list.push(i);
			obj_list.push(obj_id);
			dObj += path.id + ",";
		}
	}
	dObj = dObj.substring(0,dObj.lastIndexOf(","));
	
	if (dObj.length != 0) {
		deleteEdgeData(dObj);
	}
	
	for (var i = remove_list.length - 1; i > -1; i--) {
		edge_list.splice(remove_list[i], 1);
	}
	
	pathSize = obj_list.length;
	
	for (var i = 0; i < pathSize; i++) {
		edge = paper.getById(findEdge(obj_list[i]));
		edge.remove();
	}
}

/*
 * Graph를 다시 그리는 함수
 * Vertex가 변경되었을 때 Edge를 다시 연결해주는 함수
 */
function reloadGraph(id) {
	var size = edge_list.length;
	var pathlist = new Array();
	var count = 0;
	for (var i = 0; i < size; i++) {
		path = edge_list[i];
		if (path.start_vertex_id == Number(id) || path.end_vertex_id == Number(id)) {
			pathlist[count++] = path;
		}
	}
	
	for (var j = 0; j < count; j++) {
		var tmpPath = pathlist[j];
		
		var startV = paper.getById(findVertex(tmpPath.start_vertex_id));
		var endV = paper.getById(findVertex(tmpPath.end_vertex_id));
		
		var sbbox = startV.getBBox();
		var ebbox = endV.getBBox();
		var pathStr = "M " + (sbbox.x + sbbox.width / 2) + " " + (sbbox.y + sbbox.height / 2) + "L " + (ebbox.x + ebbox.width / 2) + " " + (ebbox.y + ebbox.height / 2) + "";
		
		var edge = paper.getById(findEdge(tmpPath.start_vertex_id+"|"+tmpPath.end_vertex_id));
		edge.attr({path:pathStr});
		startV.toFront();
		endV.toFront();
	}
}

/*
 * Edge 그리기
 */
function drawPath(path) {
	//console.log(path);
	var startV = paper.getById(findVertex(path.start_vertex_id));
	
	var endV = paper.getById(findVertex(path.end_vertex_id));
	
	var sbbox = startV.getBBox();
	var ebbox = endV.getBBox();
	var pathStr = "M " + (sbbox.x + sbbox.width / 2) + " " + (sbbox.y + sbbox.height / 2) + "L " + (ebbox.x + ebbox.width / 2) + " " + (ebbox.y + ebbox.height / 2) + "";
	
	var pathObj = paper.path(pathStr);
	var pathColor = GRAPH_EDGE_COLOR;
	var pathAttr;
	if (path.type == 1) {
		pathColor = GRAPH_ONE_EDGE_COLOR;
		pathAttr = {stroke: pathColor, 'stroke-width': PATH_STROKE_WIDTH, 'stroke-opacity': 1, 'arrow-end': 'classic[-wide[-long]]'};
	} else if (path.type == 3 || path.type == 4) {
		pathColor = GRAPH_CONNECTION_EDGE_COLOR;
		pathAttr = {stroke: pathColor, 'stroke-width': PATH_STROKE_WIDTH, 'stroke-opacity': 1};
	} else {
		pathAttr = {stroke: pathColor, 'stroke-width': PATH_STROKE_WIDTH, 'stroke-opacity': 1};
	}
	pathObj.attr(pathAttr);
	pathObj.key = "edge";
	pathObj.edge_type = path.type;
	pathObj.obj_id = path.id;
	pathObj.vid=path.start_vertex_id+"|"+path.end_vertex_id;
	//console.log(pathObj.obj_id);
	//console.log(pathObj.vid);
	
//	var text = paper.text(pathObj.getBBox().x + pathObj.getBBox().width / 2, pathObj.getBBox().y + pathObj.getBBox().height / 2).attr({"font-size":20, fill: "#ffffff", "stroke-width": 2, text:path.id});

	setEdgeEvent(pathObj);
	
	startV.toFront();
	endV.toFront();
}

function edgeHideEvent(el) {
	var edge_color;
	selectedEdge = null;
	var edge = findEdgeData(el.obj_id);
	if (edge.type == 1) {
		edge_color = GRAPH_ONE_EDGE_COLOR;
		el.attr({stroke:edge_color, 'arrow-end':'classic[-wide[-long]]'});
	} else if (edge.type == 3 || edge.type == 4) {
		edge_color = GRAPH_CONNECTION_EDGE_COLOR;
		el.attr({stroke:edge_color});
	} else {
		edge_color = GRAPH_EDGE_COLOR;
		el.attr({stroke:edge_color});
	}
}

function beaconHideEvent(el) {
    var obj = findBeaconData(el.obj_id);
    
    var color = BEACON_WHITE_COLOR;		// validity = 1
	if( obj.validity == 8 ) {
		color = BEACON_GRAY_COLOR;		// validity = 8
	}
	else if( obj.validity == 3 ) {
		color = BEACON_ORANGE_COLOR;		// validity = 3
	}
	else if( obj.validity == 2 ) {
		color = BEACON_COLOR;			// validity = 2
	}
	if( obj.type == 2 ) {
		color = BEACON_RED_COLOR;		// proximity beacon type
	}
    
    el.attr({fill:color});
}

function drawBeaconByFilter(type) {
	hideAllNode();
	
	paper.forEach(function(el) {
		if (el.key == KEY_BEACON) {
			if (el.beacon_type == type || type == 0) {
				el.show();
				
				if (el.prox_circle != null) {
					el.prox_circle.show();
				}
			} else {
				el.hide();
				
				if (el.prox_circle != null) {
					el.prox_circle.hide();
				}
			}
		}
	});
}

/*
 * Edge Event 설정
 */
function setEdgeEvent(path) {
	var dblclick = function() {
		//console.log("path dblclick");
		if (enableModGraphLayer) {
			removeEdge(this.vid);
		}
	}, click = function() {
		if (selectedEdge != null && selectedEdge.id != this.id) {
			resetUpdatedEdgeList();
		}
		//console.log("path click");
		hideAllNode();
		
		selectedEdge = this;
		this.attr({stroke: SELECTED_GRAPH_EDGE_COLOR});
		if (onEdgeClickCallback != null) {
			onEdgeClickCallback(findEdgeData(this.obj_id));
		}
	};
	//path.dblclick(dblclick);
	path.click(click);
}

function setVertexState(v) {
	var size = vertex_list.length;
	var vertex;
	
	for (var i = 0; i < size; i++) {
		vertex = vertex_list[i];
		if (vertex.id == v.obj_id) {
			var cVertex = cloneVertex(vertex);
			cVertex.coord_x = v.attr('cx');
			cVertex.coord_y = v.attr('cy');
			tmp_update_vertex[cVertex.id] = cVertex;
			//updateVertexData(vertex);
			return;
		}
	}
}

function setEdgeState(v) {
	var size = edge_list.length;
	var edge;
	var sv;
	var ev;
	var dist;
	
	var upd_edge_list = new Array();
	for (var i = 0; i < size; i++) {
		edge = edge_list[i];
		
		if (edge.start_vertex_id == v.obj_id || edge.end_vertex_id == v.obj_id) {
			sv = paper.getById(findVertex(edge.start_vertex_id));
			ev = paper.getById(findVertex(edge.end_vertex_id));
			
			edge.weight = calcDistance(sv.attr('cx'), sv.attr('cy'), ev.attr('cx'), ev.attr('cy'));
			
			upd_edge_list.push(cloneEdge(edge));
		}
	}
	
	for (var e in upd_edge_list) {
		if (tmp_update_edge.hasOwnProperty(upd_edge_list[e].id)) {
			tmp_update_edge.splice(upd_edge_list[e].id,1,upd_edge_list[e]);
		} else {
			tmp_update_edge[upd_edge_list[e].id] = upd_edge_list[e];
		}
	}
	
	//updateEdgeData(upd_edge_list);
}

function cloneVertex(v) {
	var tmp_vertex = {};
	for (var i in v) {
		if (v.hasOwnProperty(i)) {
			tmp_vertex[i] = v[i];
		}
	}
	return tmp_vertex;
}

function cloneEdge(e) {
	var tmp_edge = {};
	for (var i in e) {
		if (e.hasOwnProperty(i)) {
			tmp_edge[i] = e[i];
		}
	}
	return tmp_edge;
}

function cloneBeacon(b) {
	var tmp_beacon = {};
	for (var i in b) {
		if (b.hasOwnProperty(i)) {
			if (i == "beacon_loc" || i == "beacon_status") {
				tmp_beacon[i] = {};
				for (var k in b[i]) {
					tmp_beacon[i][k] = b[i][k];
				}
			} else {
				tmp_beacon[i] = b[i];
			}
		}
	}
	return tmp_beacon;
}

/*
 * 여러 Edge 그리기
 */
function drawPathList(pathlist) {
	var size = pathlist.length;
	var path;
	for (var i = 0; i < size; i++) {
		path = pathlist[i];
		if (path.type == 3) {
			continue;
		}
		drawPath(path);
	}
}

/*
 * Vertex 찾기
 */
function findVertex(id) {
	var el_id;
	paper.forEach(function(el){
		if (el.key == "vertex") {
			if (el.obj_id == id) {
				el_id = el.id;
			}
		} else if (el.key == KEY_FIXED_VERTEX) {
			if (el.obj_id == id) {
				el_id = el.id;
			}
		}
	});
	return el_id;
}

function findVertexData(id, fixed) {
	var res;
	
	if (fixed) {
		for (var i in fixed_vertex_list) {
			var v_list = fixed_vertex_list[i].map_graph_list;
			for (var j in v_list) {
				if (v_list[j].id == id) {
					res = v_list[j]
				}
			}
		}
	} else {
		for (var idx in tmp_update_vertex) {
			if (tmp_update_vertex[idx].id == id) {
				return tmp_update_vertex[idx];
			}
		}
		
		for (var i = 0; i < vertex_list.length; i++) {
			if (vertex_list[i].id == id) {
				res = vertex_list[i];
			} 
		}
	}
	
	return res;
}

/*
 * Edge 찾기
 */
function findEdge(id) {
	var el_id;
	paper.forEach(function(el){
		if (el.key == "edge") {
			if (el.vid == id) {
				el_id = el.id;
			}
		}
	});
	return el_id;
}

function findEdgeData(id) {
	var res;
	
	for (var idx in tmp_update_edge) {
		if (tmp_update_edge[idx].id == id) {
			return tmp_update_edge[idx];
		}
	}
	
	for (var i = 0; i < edge_list.length; i++) {
		if (edge_list[i].id == id) {
			res = edge_list[i];
		} 
	}
	return res;
}
/*
 * Vertex 찾기
 */
function findBeacon(id) {
	var tmp_el;
	paper.forEach(function(el){
		if (el.key == "beacon") {
			if (el.obj_id == id) {
				tmp_el = el;
			}
		}
	});
	return tmp_el;
}

function findBeaconData(id) {
	var res;
	for (var idx in beacon_list) {
		if (beacon_list[idx].id == id) {
			res = beacon_list[idx];
		}
	}
	
	return res;
}

/*
 * ObjectLayer 보여주기 On/Off 
 */
function ctrlObjectLayer() {
	enableObjectLayer = true;
	showObjectLayer(enableObjectLayer);
}

/*
 * GraphLayer 보여주기 On/Off 
 */
function ctrlBeaconLayer() {
	enableBeaconLayer = true;
	showBeaconLayer(enableBeaconLayer);
}

/*
 * GraphLayer 보여주기 On/Off 
 */
function ctrlGraphLayer() {
	enableGraphLayer = true;
	showGraphLayer(enableGraphLayer);
}


/*
 * Object 수정할 수 있는 권한 On/Off 
 */
function ctrlModObjectLayer() {
	enableModObjectLayer = true;
}

/*
 * Graph 수정할 수 있는 권한 On/Off 
 */
function ctrlModGraphLayer() {
	enableModGraphLayer = true;
}

function offModGraphLayer() {
	enableModGraphLayer = false;
}

/*
 * Beacon 수정할 수 있는 권한 On/Off 
 */
function ctrlModBeaconLayer() {
	enableModBeaconLayer = true;
}

function offModBeaconLayer() {
	enableModBeaconLayer = false;
}

/*
 * Beacon Layer 아이템들 보여주기 On/Off 
 * show : show flag
 */
function showBeaconLayer(show) {
	oPaper.forEach(function(el) {
		oPaper.forEach(function(el){
			if(el.key==KEY_BEACON){
				show ? el.show() : el.hide();
			}
		});
	});
}

/*
 * Object Layer 아이템들 보여주기 On/Off 
 * show : show flag
 */
function showObjectLayer(show) {
	oPaper.forEach(function(el) {
		oPaper.forEach(function(el){
			if(el.key==KEY_OBJECT || el.key==KEY_ICON){
				show ? el.show() : el.hide();
			}
		});
	});
}

/*
 * Graph Layer 아이템들 보여주기 On/Off 
 * show : show flag
 */
function showGraphLayer(show) {
	paper.forEach(function(el) {
		paper.forEach(function(el){
			if(el.key=="vertex"){
				show ? el.show() : el.hide();
			} else if (el.key=="edge") {
				show ? el.show() : el.hide();
			}
		});
	});
}

/*
 * Object Data를 업데이트
 * Object Data를 업데이트하고 DB에 업데이트 요청
 */
function updateObjectData(zone) {
	$.ajax({
		url: "/admin/zone_data.do",
		type: 'PUT',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:zone,
		success: function(result) {
			//console.log(result);
		}
	});
}

/*
 * Object Data를 생성
 * Object Data를 생성하고 DB에 생성 요청
 */
function insertObjectData(zone) {
	$.post("/admin/zone_data.do", {
		zone:zone
	}, function(result) {
		//console.log(result);
	});
}

/*
 * Object Data를 삭제
 * Object Data를 삭제하고 DB에 삭제 요청
 */
function deleteObjectData(zone) {
	$.ajax({
		url: "/admin/zone_data.do",
		type: 'DELETE',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:zone,
		success: function(result) {
			//console.log(result);
		}
	});
}

/*
 * Object Data를 조회
 * Object Data를 DB에 요청하고, 결과를 관리
 */
function getObjectData(zone) {
	$.post("/admin/zone_data.do", {
		zone:zone
	}, function(result) {
		//console.log(result);
	});
}

/*
 * Beacon Data를 업데이트
 * Beacon Data를 업데이트하고 DB에 업데이트 요청
 */
function updateBeaconData(callback) {
	if (tmp_update_beacon == null) {
		return;
	}
	var beacon = tmp_update_beacon;
	
	var tmp_beacon_list = {
			beacon_list: [beacon]
	}
	var beacon_data = JSON.stringify(tmp_beacon_list);
	
	//console.log(beacon_data);
	$.ajax({
		url: "/admin/beacon_data.do",
		type: 'PUT',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:beacon_data,
		success: function(result) {
			//console.log(result);
			if (callback != null) {
				callback(tmp_beacon_list);
			}
			
			for (var i in beacon_list) {
				if (beacon.id == beacon_list[i].id) {
					beacon_list.splice(i, 1, beacon);
				}
			}
			
			tmp_update_beacon = null;
		}
	});
}

/*
 * Beacon Data를 생성
 * Beacon Data를 생성하고 DB에 생성 요청
 */
function insertBeaconData(beacon) {
	var tmp_beacon_list = {
			beacon_list: [beacon]
	};
	
	var beacon_data = JSON.stringify(tmp_beacon_list);
	
	//console.log(beacon_data);
	$.post("/admin/beacon_data.do", {
		beacon_data:beacon_data
	}, function(result) {
		//console.log(result);
		
		var res_beacon_list = result.beacon_list;
		beacon.id = res_beacon_list[0].id;
		
		beacon_list.push(beacon);
		
		drawBeacon(beacon,true);
	});
}

/*
 * Beacon Data를 삭제
 * Beacon Data를 삭제하고 DB에 삭제 요청
 */
function deleteBeaconData(b) {
	//console.log("removeBeacon");
	//console.log(b.obj_id);
	var id = b.obj_id;
	
	$.ajax({
		url: "/admin/beacon_data.do",
		type: 'DELETE',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:""+id,
		success: function(result) {
			//console.log(result);
			
			var tmp;
			for (var i = 0; i < beacon_list.length; i++) {
				tmp = beacon_list[i];
				
				if (tmp.id == id) {
					beacon_list.splice(i, 1);
				}
			}
			b.remove();
		}
	});
}

/*
 * Beacon Data를 조회
 * Beacon Data를 DB에 요청하고, 결과를 관리
 */
function getBeaconData(company_id,branch_id,floor_id,beacon_type,callback) {
	$.get("/admin/beacon_data.do", {
		company_id:company_id,
		branch_id:branch_id,
		floor_id:floor_id,
		beacon_type:beacon_type
	}, function(result) {
		//console.log(result.beacon_list);
		beacon_list= result.beacon_list;
		callback();
	});
}

/*
 * Graph Data를 조회
 * Graph Data를 DB에 요청하고, 결과를 관리
 */
function getGraphData(map_id,callback) {
	$.get("/admin/graph_data.do", {
		map_id:map_id
	}, function(result) {
		//console.log(result);
		graph_data = result;
		vertex_list = result.map_graph_vertex_list;
		edge_list = result.map_graph_edge_list;
		
		callback();
	});
}

/*
 * Vertex Data를 업데이트
 * Vertex Data를 업데이트하고 DB에 업데이트 요청
 */
function updateVertexData(callback) {
	var vlist = new Array();
	
	if (tmp_update_vertex.length == 0) {
		return;
	}
	
	for (var idx in tmp_update_vertex) {
		tmp_update_vertex[idx].editor_id = g_user_id;
		vlist.push(tmp_update_vertex[idx]);
	}
	
	var vertex_data = JSON.stringify(vlist);
	
	//console.log(vertex_data);
	$.ajax({
		url: "/admin/vertex_data.do",
		type: 'PUT',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:vertex_data,
		success: function(result) {
			//console.log(result);
			
			updateVertexList();
			
			resetUpdatedVertexList();
			
			if (callback != null) {
				callback(vlist);
			}
		}
	});
}

function updateVertexList() {
	for (var i in tmp_update_vertex) {
		for (var j in vertex_list) {
			if (vertex_list[j].id == i) {
				vertex_list[j] = tmp_update_vertex[i];
			}
		}
	}
}

function undoVertexData() {
	for (var i in tmp_update_vertex) {
		for (var j in vertex_list) {
			if (vertex_list[j].id == i) {
				var v = vertex_list[j];
				
				var el = paper.getById(findVertex(v.id));
				
				el.attr({cx: v.coord_x, cy: v.coord_y});
				
				reloadGraph(el.obj_id);
			}
		}
	}
	
	resetUpdatedVertexList();
}

/*
 * Vertex Data를 생성
 * Vertex Data를 생성하고 DB에 생성 요청
 */
function insertVertexData(vertex) {
	var vertex_data = JSON.stringify(vertex);
	$.post("/admin/vertex_data.do", {
		vertex_data:vertex_data
	}, function(result) {
		vertex.id = result.id;
		//console.log(result);
		
		hideAllNode();
		onBackgroundClickCallback();
		
		vertex_list.push(vertex);
		
		drawVertex(vertex, false);
	});
}

/*
 * Vertex Data를 삭제
 * Vertex Data를 삭제하고 DB에 삭제 요청
 */
function deleteVertexData(id, callback) {
	//console.log("removeVertex");
	//console.log(id);
	
	$.ajax({
		url: "/admin/vertex_data.do",
		type: 'DELETE',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:""+id,
		success: function(result) {
			//console.log(result);
			var tmp;
			var v = paper.getById(findVertex(id));
			for (var i = 0; i < vertex_list.length; i++) {
				tmp = vertex_list[i];
				
				if (tmp.id == id) {
					vertex_list.splice(i, 1);
				}
			}
			
			v.remove();
			hideAllNode();
			onBackgroundClickCallback();
			
			if (callback != null) {
				callback(id);
			}
		}
	});
}

/*
 * Vertex Data를 조회
 * Vertex Data를 DB에 요청하고, 결과를 관리
 */
function getVertexData(floor_id_list, callback) {
	$.get("/admin/vertex_data.do", {
		floor_id_list:floor_id_list
	}, function(result) {
		//console.log(result.map_graph_data_list);
		fixed_vertex_list= result.map_graph_data_list;
		callback(fixed_vertex_list);
	});
}


/*
 * Edge Data를 업데이트
 * Edge Data를 업데이트하고 DB에 업데이트 요청
 */
function updateEdgeData(callback) {
	var elist = new Array();
	
	if (tmp_update_edge.length == 0) {
		return;
	}
	
	for (var idx in tmp_update_edge) {
		tmp_update_edge[idx].editor_id = g_user_id;
		elist.push(tmp_update_edge[idx]);
	}
	
	var edge_data = JSON.stringify(elist);
	$.ajax({
		url: "/admin/edge_data.do",
		type: 'PUT',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:edge_data,
		success: function(result) {
			//console.log(result);
			
			if (callback != null) {
				callback(elist);
			}
		}
	});
}

/*
 * Edge Data를 생성
 * Edge Data를 생성하고 DB에 생성 요청
 */
function insertEdgeData(edge) {
	var edge_data = JSON.stringify(edge);
	$.post("/admin/edge_data.do", {
		edge_data:edge_data
	}, function(result) {
		//console.log(result);
		edge.id = result.id;
		
		edge_list.push(edge);
		drawPath(edge);
		
		hideAllNode();
		onBackgroundClickCallback();
	});
}

/*
 * Edge Data를 삭제
 * Edge Data를 삭제하고 DB에 삭제 요청
 */
function deleteEdgeData(edge, callback) {
	$.ajax({
		url: "/admin/edge_data.do",
		type: 'DELETE',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:edge,
		success: function(result) {
			//console.log(result);
			selectedEdge = null;
			hideAllNode();
			onBackgroundClickCallback();
			
			if (callback != null) {
				callback(edge);
			}
		}
	});
}

/*
 * Edge Data를 조회
 * Edge Data를 DB에 요청하고, 결과를 관리
 */
function getEdgeData(edge) {
	$.get("/admin/edge_data.do", {
		edge_id:edge_id
	}, function(result) {
		//console.log(result.beacon_list);
		beacon_list= result.beacon_list;
		callback();
	});
}

function insertSectionData(section, callback, error_callback) {
	var section_data = JSON.stringify(section)
	$.post("/admin/section_data.do", {
		section:section_data
	}, function(result) {
		//console.log(result);
		if (result.id == -1) {
			error_callback();
		} else {
			section.id = result.id;
			section_list.push(section);
			if (callback != null) {
				callback(section);
			}
		}
	});
}

function deleteSectionData(section_id,callback) {
	$.ajax({
		url: "/admin/section_data.do",
		type: 'DELETE',
		headers: {"x-ssg-deviceid" : "admin", "x-ssg-userid" : g_user_id},
		data:""+section_id,
		success: function(result) {
			//console.log(result);
			
			var tmp;
			for (var i = 0; i < section_list.length; i++) {
				tmp = section_list[i];
				
				if (tmp.id == section_id) {
					section_list.splice(i, 1);
				}
			}
			
			var v_list = getVertex_data();
			for (var i in v_list) {
				if (v_list[i].section == section_id) {
					v_list[i].section =-1;
					v_list[i].section_name = "";
				}
			}
			
			if (callback != null) {
				callback(section_id);
			}
		}
	});
}

function getSectionData(branch_id, attr) {
	$.get("/admin/section_data.do", {
		branch_id:branch_id,
		attr:attr
	}, function(result) {
		//console.log(result.section_list);
		section_list= result.section_list;
	});
}

function getConnectionEdgeData(branch_id, vertex_id) {
	$.get("/admin/connection_edge.do", {
		branch_id:branch_id,
		vertex_id:vertex_id
	}, function(result) {
		//console.log("getConnectionEdgeData");
		//console.log(vertex_id);
		//console.log(result);
		var e_list = result.edge_list;
		for (var i in e_list) {
			edge_list.push(e_list[i]);
			drawPath(e_list[i]);
		}
	});
}

function getSection_data() {
	return section_list;
}

/*
 * Graph Edge List 반환
 */
function getEdge_data() {
	return edge_list;
}

/*
 * Graph Vertex List 반환
 */
function getVertex_data() {
	return vertex_list;
}

/*
 * Vertex 추가 이벤트 반환
 */
function getAddVertexEvent() {
	return addVertexEvent;
}

/*
 * Vertex 추가 이벤트
 */
function addVertexEvent(e) {
	//console.log("addVertexEvent");
	if (enableModGraphLayer) {
		var item = convertZoomCoord(-1,getConvertEventX(e), getConvertEventY(e));
		item.id = --count;
		item.type = 1;
		item.attr = 0;
		item.map_graph_id = graph_data.id;
		item.name = '';
		item.validity = 1;
		item.creator_id = g_user_id;
		item.editor_id = g_user_id;
		
		insertVertexData(item);
	}
}

/*
 * Beacon 추가 이벤트 반환
 */
function getAddBeaconEvent() {
	return addBeaconEvent;
}

/*
 * Beacon 추가 이벤트
 */
function addBeaconEvent(e) {
	//console.log("addBeaconEvent");
	if (enableModBeaconLayer) {
		var item = createDefaultBeaconData(e);
		
		insertBeaconData(item);
	}
}

function getBeaconCoord() {
	var lastBeaconInfo;
	var max_beacon_x = -1;
	var max_beacon_y = -1;
	
	if (beacon_list.length == 0) {
		max_beacon_x = BEACON_GAP_SIZE;
		max_beacon_y = BEACON_GAP_SIZE;
	} else {
		for (var i in beacon_list) {
			var bInfo = beacon_list[i];
			
			if (lastBeaconInfo == null) {
				lastBeaconInfo = bInfo;
			} else {
				if (lastBeaconInfo.minor < bInfo.minor) {
					lastBeaconInfo = bInfo;
				}
			}
//			if (bInfo.beacon_loc.coord_x > max_beacon_x) {
//				max_beacon_x = bInfo.beacon_loc.coord_x;
//			}
//			if (bInfo.beacon_loc.coord_y > max_beacon_y) {
//				max_beacon_y = bInfo.beacon_loc.coord_y;
//			}
			
		}
	}
	
	var res;
	if (lastBeaconInfo == null) {
		res = {id: -1, coord_x:100, coord_y:100};
	} else {
		res = {id: -1, coord_x:lastBeaconInfo.beacon_loc.coord_x, coord_y:lastBeaconInfo.beacon_loc.coord_y};
	}
	
	return res;
}

/*
 * 기본 Beacon 생성
 */
function createDefaultBeaconData() {
	//var coords = convertZoomCoord(-1,e.offsetX, e.offsetY);
	var coords = getBeaconCoord();
	
	var beacon_loc = {
			beacon_id: -1,
			branch_id: getBranchId(),
			company_id: getCompanyId(),
			floor_id: getFloorId(),
			brand_id: getCompanyBrandId(),
			map_id: getMapId(),
			coord_x: coords.coord_x,
			coord_y: coords.coord_y,
			description: "NewBeacon",
			validity: 1
	};
	var beacon_status = {
			validity: 1
	};
	
	var beacon = {
			major: getMajor(),
			minor: getMinor(),
			name: "NewBeacon",
			serial_number:"",
			type: 1,
			validity: 1,
			uuid: getUUID(),
			broadcast_freq: 300,
			beacon_loc: beacon_loc,
			beacon_status: beacon_status,
			group_id: -1,
	};
	
	return beacon;
}

function getUpdateBeaconData() {
	return tmp_update_beacon;
}

function getUUID() {
	return $('#beacon_uuid_data').val();
	//return "89F9DA42-BA67-4DB3-9231-B0F98C8D6F5B";
}

function getMajor() {
	return $('#beacon_major_data').val();
	//return 1;
}

function getMinor() {
	return 0;
}

function getGroupId() {
	return $('#beacon_groupid_data').val();
	//return 1;
}

function autoConnectNode() {
	
}

function setVertexClickCallback(callback) {
	onVertexClickCallback = callback;
}

function setEdgeClickCallback(callback) {
	onEdgeClickCallback = callback;
}

function setBeaconClickCallback(callback) {
	onBeaconClickCallback = callback;
}

function setUpdatedVertexData(vertex) {
	var res;
	for (var idx in tmp_update_vertex) {
		if (tmp_update_vertex[idx].id == vertex.id) {
			res = tmp_update_vertex[idx];
			tmp_update_vertex.splice(idx,1,vertex);
		}
	}
	
	if (res == null) {
		res = cloneVertex(vertex);
		tmp_update_vertex[res.id] = res;
	}
	
	var el = oPaper.getById(findVertex(res.id));
	//console.log(el);
	
	var vertex_color;
	if (res.name.indexOf("NewVertex") > -1 || res.name.length == 0) {
		vertex_color = GRAPH_VERTEX_INIT_COLOR;
	} else if (vertex.type == 2) {
		vertex_color = GRAPH_END_VERTEX_COLOR;
	} else {
		vertex_color = GRAPH_VERTEX_COLOR;
	}
	el.attr({'fill':vertex_color});
}

function setUpdatedEdgeData(edge) {
	var res;
	for (var idx in tmp_update_edge) {
		if (tmp_update_edge[idx].id == edge.id) {
			res = tmp_update_edge[idx];
			tmp_update_edge.splice(idx,1,edge);
		}
	}
	
	if (res == null) {
		res = cloneEdge(edge);
		tmp_update_edge[res.id] = res;
	}
	//console.log(res.id);
	var vid = res.start_vertex_id + "|" + res.end_vertex_id;
	//console.log(vid);
	var el = oPaper.getById(findEdge(vid));
	//console.log(el);
	var pathColor = (res.type == 1 ? GRAPH_ONE_EDGE_COLOR : GRAPH_EDGE_COLOR);
	var pathAttr;
	if (res.type == 1) {
		el.attr({stroke: pathColor, 'arrow-end':'classic[-wide[-long]]'});
	} else {
		//el.attr({stroke: pathColor});
		el.remove();
		drawPath(edge);
	}
}

/*
 * Beacon 선택시 Beacon 활성화와 Callback 호출
 */
function selectBeacon(id) {
	//console.log(id);
	paper.forEach(function(el) {
		if (el.key == KEY_BEACON && el.obj_id == id) {
			//console.log('clicked beacon');
			//console.log(el.obj_id);
			hideAllNode();
			
			el.attr({fill:SELECTED_BEACON_COLOR});
			
			var beacon_data = findBeaconData(id);
			tmp_update_beacon = cloneBeacon(beacon_data);
			onBeaconClickCallback(tmp_update_beacon);
		}
	});
}

function createDefaultSection(root_branch_id,parent_branch_id,name,attr) {
	var section_item = {
			id: -1,
			branch_id: getBranchId(),
			company_id: getCompanyId(),
			floor_id: getFloorId(),
			brand_id: getCompanyBrandId(),
			root_branch_id: root_branch_id,
			parent_branch_id: parent_branch_id,
			name:name,
			attr:attr,
			validity: 1
	};
	
	return section_item;
}

function updateBeaconState() {
	for (var i in beacon_list) {
		if (beacon_list[i].id == tmp_update_beacon.id) {
			beacon_list.splice(i,1,tmp_update_beacon);
		}
	}
	tmp_update_beacon = null;
}

function resetIndoor() {
	beacon_list = [];

	graph_data = null;

	vertex_list = null;
	fixed_vertex_list = null;

	edge_list = null;

	enableObjectLayer = false;
	enableBeaconLayer = false;
	enableGraphLayer = false;
	enableModObjectLayer = false;
	enableModBeaconLayer = false;
	enableModGraphLayer = false;
	selectedVertex = false;
	createZoneMode = false;

	graph_count = 0;
	selected_v1 = null;
	selected_v2 = null;

	tmp_update_vertex = [];
	tmp_update_edge = [];
	tmp_update_beacon = {};
	
	selectedEdge = null;
	selectedBeacon = null;
}


function resetUpdatedVertexList() {
	tmp_update_vertex = [];
}

function resetUpdatedEdgeList() {
	tmp_update_edge = [];
}

function beaconDeleteEvent() {
	if (selectedBeacon != null && enableModBeaconLayer) {
		deleteBeaconData(selectedBeacon);
	}
}

function vertexDeleteEvent() {
	if (selectedEdge == null && selectedVertex && selected_v1 != null) {
		removeVertex(""+selected_v1.obj_id);
	}
}

function edgeDeleteEvent() {
	if (selectedEdge != null) {
		removeEdge(""+selectedEdge.obj_id);
	}
}

function setUpdateBeaconData(id) {
	var beacon_data = findBeaconData(id);
	tmp_update_beacon = cloneBeacon(beacon_data);
}