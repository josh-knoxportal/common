if (typeof atbsvg === 'undefined') {
    throw new Error('JavaScript requires AndTheBee D3 Extension')
}
if (typeof math === 'undefined') {
    throw new Error('JavaScript requires mathjs')
}
if (typeof math.intersect === 'undefined') {
    throw new Error('JavaScript requires mathjs')
}
if (typeof LGSmartMapAttr === 'undefined') {
    throw new Error('JavaScript requires LGSmartMapAttr')
}


/* */
var/*const*/  VIOLATION_UNKNOWN			= 0;
var/*const*/  VIOLATION_OVER_SPEED		= 1;
var/*const*/  VIOLATION_ON_RESTRICT		= 2;

/*
 * [2016/08/18]
 */
var VEHICLE_TYPE = (function() {
	var unknownCode = {"index":-1, "code":'unknown', "name":"미확인","color":"white", "border":"black"};

	/* index는 bp-style.css의 type 번호 */
	var codeList =[
			{"index":1, "code":'delivery',		"name":"정기 납품",		"color":"#3a8c0e", "border":"#466c05"},
			{"index":3, "code":'logistics',		"name":"정기 물류",		"color":"#0175ae", "border":"#065576"},
			{"index":5, "code":'work',			"name":"정기 작업/공사",	"color":"#f16600", "border":"#af4b00"},
			{"index":7, "code":'normal',		"name":"정기 일반/방문",	"color":"#5b67ef", "border":"#384089"},
			{"index":2, "code":'dailydelivery',	"name":"일일 납품",		"color":"#aedd15", "border":"#43760d"},
			{"index":4, "code":'dailylogistics',"name":"일일 물류",		"color":"#46b9ef", "border":"#0b7eb5"},
			{"index":6, "code":'dailywork',		"name":"일일 작업/공사",	"color":"#ffa121", "border":"#ad7425"},
			{"index":8, "code":'dailynormal',	"name":"일일 일반/방문",	"color":"#808dff", "border":"#5661df"},
			{"index":9, "code":'etc',			"name":"기타",			"color":"#8d47f5", "border":"#392258"}
	];

	var codeMap = new atbsvg.map();
	for (var idx = 0; idx < codeList.length; idx++) {
		var items = codeList[idx];
		codeMap.put(items.code,items);
	}

	return {
		toName : function(code) {
			var items = codeMap.get(code);
			if (atbsvg.util.isNull(items)) return "";

			return items.name;
		},
		toIndex : function(code) {
			var items = codeMap.get(code);
			if (atbsvg.util.isNull(items)) return 1;

			return items.index;
		},
		toColor : function(code) {
			var items = codeMap.get(code);
			if (atbsvg.util.isNull(items)) return "black";

			return items.color;
		},
		toBorderColor : function(code) {
			var items = codeMap.get(code);
			if (atbsvg.util.isNull(items)) return "black";

			return items.border;
		},
		size : function() {
			return codeList.length;
		},
		indexAt : function(index) {
			try {
				return codeList[index];
			} catch(e) {
				return unknownCode;
			}
		},
		getEntry : function(code) {
			var items = codeMap.get(code);
			if (atbsvg.util.isNull(items)) {
				return unknownCode;
			}

			return items;
		}
	}
})();


/* [2016/05/13] constants for LG Smart Plant MAP */
var LGSmartMap = function(parentId,zoomable,draggable,scalable) {
	/* constants */
	var/*const*/ STROKE_WIDTH_OF_SECTION_LINE 	= '3px';
	var/*const*/ SPEED_CIRCLE_RADIUS 			= 10;
	var/*const*/ SPEED_FONT_SIZE 				= "1em";

	var/*const*/ COLOR_TRANSPARENT   			= 'transparent';

	var/*const*/ TRACE_SMALL_CIRCLE_R = 0;
	var/*const*/ TRACE_SMALL_CIRCLE_STROKE 	= 'transparent';

	var/*const*/ COLOR_START_POINT				= '#fc64a3';
	var/*const*/ CIRCLE_POINT_R				= 4;


    /* [2016/08/19] modified */
	var/*const*/ COLOR_SECTION_RESTRICT_ON 	= '#fe812f';
	var/*const*/ COLOR_SECTION_NORMAL	 		= '#67b5f5';
	var/*const*/ COLOR_SECTION_SELECTED		= '#774c87';

	/* */
	var/*const*/ STROKEWIDTH_DRIVING_LINE 		= '2px';  /* [2016/08/30] 이정래차장 script 변경요청 작용 3->2px */
	var/*const*/ STROKEWIDTH_SPEEDLIMIT_CIRCLE = '1.25px';

	/* [2016/08/19] added */
	var/*const*/ VEHICLE_CIRCLE__R				= 6;
	var/*const*/ VEHICLE_STROKEWIDTH			= '1.5px';

	/* */
	var/*const*/ RIPPLE_CIRCLE_R				= 20;


	/*
	 * const COLOR_OVER_SPEED 			= '#fc64a3';
	 * const COLOR_ON_RESTRICT_AREA		= '#fc64a3';
	 * const COLOR_NORMAL_DRIVING 		= '#999999';
	 */
	var/*const*/ COLOR_OVER_SPEED 				= '#c60353';
	var/*const*/ COLOR_ON_RESTRICT_AREA		= '#c60353';
	var/*const*/ COLOR_NORMAL_DRIVING 			= '#707070';

	/* */
	var/*const*/ COLOR_BORDER_CAR				= '#999999';

    /* */
	var/*const*/ FACTORY_FONT_COLOR			= 'black';
    /* 맵의 글씨 크기 FACTORY_FONTSIZE(0.45em) */
	var/*const*/ FACTORY_FONTSIZE				= "0.85em";
	var/*const*/ FACTORY_LETTER_SPACING		= "0.0";



	/* [2016/08/18] modified
	 * 현황과 이력이 차이가 있음
	 */
	/* 상수지만 조정필요 */
	var PAGING_SIZE						= 28;
	var PAGING_NAVI_SIZE				= 5;

	/* 상수지만 조정필요 */
	var TRACE_HISTORY_ON 				= false;



	/* [common] */
	var width = LGSmartMapAttr.width(),
		height= LGSmartMapAttr.height(),
		backgroundPath = LGSmartMapAttr.backgroundPath(),
		canvasWidth = LGSmartMapAttr.canvasWidth(),
		canvasHeight = LGSmartMapAttr.canvasHeight();

	/* [rule] */
	var dialogWidth   = 350,
		dialogHeight  = 150;

	/* [driving] */
	var normalCarPath   = contextPath+'/resources/img/ico_car_normal.png',
		violatedCarPath = contextPath+'/resources/img/ico_car_caution.png';

	/* 맵 이미지상의 주차장 크기 : carCurrentSize(23), carHistorySize(13) */
	var carCurrentSize = 36;
	var carHistorySize = 22;

	/* [common] */
	var canvas;

	/* [rule/driving] */
	var tooltip;
	var roadMap = new atbsvg.map();
	var crossMap = new atbsvg.map();
	var vehicleMap = new atbsvg.map();
	var vehicleUnknownMap = new atbsvg.map();

	/* [common] */
	var onSuccessUpdateListener;

	/* [rule/driving] */
	var onCompletedLoadMapListener;
	var showableRoadMap = true;

	/* [rule] */
	var onClickedRoadSectionListener;
	var onMultiSelectedRoadSectionListener;

	var isSingleRoadSelection = true;
	var selectedSingleRoadSection = null;
	var selectedMultiEntry = null;

	var showSpeedLimitOnMap = false;

	/* [current driving] */
	var onCompletedDrivingListener;
	var onHideTooltipListner;
	var onShowTooltipListner;

	var currentVehicleArray;

	var onDrivingTableBodyEntryListener;
	var	$tablebodyDrivingTable;
	var $pagingDrivingTable;
	var gotoDrivingTableFuncName;
	var onCompletedViolationHistoryListener;
	var onCompletedDrivingEventListener;
	var onCompletedDrivingSectionListener;

	/* */
	var lastHistoryEntry;
	var lastHistoryPoints;


	/* */
	var	currentSelectedVehicleType;

	/* */
	var initScale = 1.0;
	var initTranslate = [0, 0];

	/* */
	var minScale = initScale;
	var maxScale = (initScale+1);


	/*
	 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
	 * [2016/06/13] @see lg_smartmap_for_wwms
	 */
	var onCompletedFactoryCoordListener;
	var factoryList = null;
	var mapFactoryCoord = new atbsvg.map();


	/* */
	if (!arguments.length) {
		parentId = "body";
	}

    canvas = atbsvg.canvas()
    		.width(canvasWidth)
    		.height(canvasHeight)
    		.zoomEnabled(zoomable)
    		.dragEnabled(draggable)
    		.mouseZoomEnabled(false);

//    canvas.blockedDragAway(true);

    d3.select(parentId).call(canvas);

    /* background */
    var background = atbsvg.primitive.newImage().width(width).height(height).href(backgroundPath).id('_bg_map');
    canvas.appendElement(background);
    if (zoomable) {
    	 /*
    	  * canvas.addZoomSelector(background.tag);
    	  */
    	 canvas.addZoomSelector('#_bg_map');
    	 if (scalable) {
    		 canvas.addZoomSelector('g');
    	 }
    }

    /* ----------------------------------------------- */
    /* private */
    /* ----------------------------------------------- */

    /* [common] */
    function clear() {
    	clearMapOnly();

		/* */
		vehicleMap.clear();
		/* */
		vehicleUnknownMap.clear();
		if (showableRoadMap) {
			roadMap.clear();
			crossMap.clear();
		}
	}

    /* */
    function clearMapOnly() {
    	canvas.removeChildAll("g");
    	/* [2016/06/22] added for ripple */
    	canvas.removeChildAll("circle");
    }


    /* [2016/09/22] added */
    function clearOverlayAll() {
    	if (atbsvg.util.isFunction(onHideTooltipListner)) {
    		onHideTooltipListner();
    	}
    }



    /* [common] */
    function showPopupAlert(msg) {
		alert(msg);
	}


    /* [common] */
    function ripple(group) {
    	var center = group.getCenter();
    	rippleWith(center[0],center[1]);
    }

    /* [common] */
    function rippleWith(x, y) {
    	atbsvg.animation.ripple(canvas, {cx: x, cy: y, duration:2000, radius:RIPPLE_CIRCLE_R,'stroke-width':'5px'});
    	atbsvg.animation.ripple(canvas, {cx: x, cy: y, duration:2000, radius:RIPPLE_CIRCLE_R,'stroke-width':'5px',delay:300});
    	atbsvg.animation.ripple(canvas, {cx: x, cy: y, duration:2000, radius:RIPPLE_CIRCLE_R,'stroke-width':'5px',delay:600});
    	atbsvg.animation.ripple(canvas, {cx: x, cy: y, duration:2000, radius:RIPPLE_CIRCLE_R,'stroke-width':'5px',delay:900});
    }

    /* [rule] */
    function relocationDialogPosition(x, y) {
		var nx = x + 10;
		var ny = y + 10;

		if ( (x+dialogWidth) > width) {
			nx = width - dialogWidth;
		}

		if ((y+dialogHeight) > height) {
			ny = height/2;
		}

		return [nx,ny];
	}



    /* [rule] */
    function toggleSelection(group) {
        if (group.userdata.selected) {
            setUnselected(group);
        } else {
            setSelected(group);
        }
    }

    /* [rule] */
    function setSelected(group) {
        group.userdata.selected = true;
        group.d3element.selectAll('line').attr('stroke', COLOR_SECTION_SELECTED);
    }

    /* [rule] */
    function setUnselected(group) {
        group.userdata.selected = false;
        group.d3element.selectAll('line').attr('stroke',group.userdata.orig_stroke);
    }

    /* [rule] */
    function unselectedAll() {
		if (!roadMap.isEmpty()) {
            var keys = roadMap.keys();
            var len = keys.length;
            for (var i = 0; i < len; i++) {
                var group = roadMap.get(keys[i]);
                if (group.userdata.selected) {
                	setUnselected(group);
                }
            }
        }
    }

    /* [rule] */
    function drawRoadSectionLine(entry) {
    	/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
    	var startp = LGSmartMapAttr.pointOnMapView(entry.x1,entry.y1);
    	var endp   = LGSmartMapAttr.pointOnMapView(entry.x2,entry.y2);

        var line = atbsvg.primitive.newLine()
	        .x1(startp.x).y1(startp.y)
	        .x2(endp.x).y2(endp.y)
	        .strokeLinecap('round')
	        .strokeWidth(STROKE_WIDTH_OF_SECTION_LINE)

	    var lineColor = COLOR_SECTION_NORMAL;
        if (entry.is_restrict_area) {
        	lineColor = COLOR_SECTION_RESTRICT_ON;
        }
        line.stroke(lineColor);

        /* group */
        var group = atbsvg.primitive.newGroup().id(""+entry.section_uid);
        group.userdata = entry;
        group.userdata.selected = false;
        group.userdata.orig_stroke = line.stroke();

        canvas.appendElement(group);

        canvas.innerElement(group.d3element, line);
        line.moveToFront();


        /* */
        if (showSpeedLimitOnMap) {
        	var centerX = (startp.x + endp.x)/2;
            var centerY = (startp.y + endp.y)/2;

            var circle = atbsvg.primitive.newCircle()
			    .cx(centerX).cy(centerY)
			    .r(SPEED_CIRCLE_RADIUS)
			    .fill('white')
			    .stroke(lineColor)
			    .strokeWidth(STROKEWIDTH_SPEEDLIMIT_CIRCLE)
			    .opacity('0.7');
            canvas.innerElement(group.d3element, circle);
            circle.moveToFront();

            /* */
            var text = atbsvg.primitive.newText()
		    			.x(centerX).y(centerY+4)
		    			.stroke('black')
		    			.fontSize(SPEED_FONT_SIZE)
		    			.opacity('0.7')
		    			.strokeWidth('0.3px')
		    			.fill('black')
		    			.textAnchor('middle');

		    text.text = ""+entry.speed_limit;

            canvas.innerElement(group.d3element, text);
            text.moveToFront();
        }


        /* */
        canvas.transformedElement(group.d3element);

        /* */
        group.onMousedown(function() {
        	d3.select(this).moveToFront();
        	if (atbsvg.util.isFunction(onClickedRoadSectionListener)) {
        		/* [GROUP PROCESS] */
        		isSingleRoadSelection = true;
        		selectedSingleRoadSection = group.userdata;
        		selectedMultiEntry = null;
        		/* END OF [GROUP PROCESS] */


        		var coordinates = d3.mouse(this);
        		onClickedRoadSectionListener(coordinates[0], coordinates[1]);
        		setSelected(group);
        	}
        });

        roadMap.put(""+entry.section_uid, group);
    }

    /* [rule] */
    function refreshRoadMapWith(roadSectionList) {
		clear();
		if (!atbsvg.util.isNull(roadSectionList)) {
    		for(var idx = 0; idx < roadSectionList.length; idx++) {
				var entry = roadSectionList[idx];
				roadMap.put(entry.section_uid,entry);

				/* */
				if (showableRoadMap) {
					drawRoadSectionLine(entry);
				}
			}
		}

        // TODO: 이력 페이지 이외에서도 실행됨. 만약 차량운행 현황에서 계속 불린다면, 조정 필요.
        /* execute this function at history page only */
        if (!showableRoadMap) {
            generateCrossMap();
        }
	}

    /* [2016/10/13] 연결 정보 생성 */
    function generateCrossMap() {
        /* tostring
        console.log("keys: " + roadMap.keys());
        var k2 = roadMap.keys();
        var l2 = k2.length;
        for (var i = 0; i < l2; i++) {
            var road = roadMap.get(k2[i]);

            console.log("road[" + k2[i] + "] => " + JSON.stringify(road));
        }
        */


        var keys = roadMap.keys();
        var len = keys.length;
        for (var i = 0; i < len; i++) {
            var r1 = roadMap.get(keys[i]);

            var connected = [];

            // TODO: maybe removable?
            // ignoring wrong element of (road[0] => undefined)
            if (r1 == null) {
                continue;
            }

            //console.log("road " + keys[i] + ": started");
            //console.log("road[" + keys[i] + "] => " + JSON.stringify(r1));


            for (var j = 0; j < len; j++) {
                if (i == j) {
                    continue;
                }

                var r2 = roadMap.get(keys[j]);

                if ((r1.x1 == r2.x1 && r1.y1 == r2.y1) || (r1.x1 == r2.x2 && r1.y1 == r2.y2)) {
                    //console.log("road " + keys[i] + ": matching " + keys[j]);
                    connected.push(keys[j]);
                }

                if ((r1.x2 == r2.x1 && r1.y2 == r2.y1) || (r1.x2 == r2.x2 && r1.y2 == r2.y2)) {
                    //console.log("road " + keys[i] + ": matching " + keys[j]);
                    connected.push(keys[j]);
                }
            }

            if (connected.length > 0) {
                crossMap.put(keys[i], connected);
                //console.log("road " + keys[i] + ": inserting " + connected);
            }

            //console.log("road " + keys[i] + ": ended");
        }

        /*
        // tostring
        var k = crossMap.keys();
        var l = k.length;
        for (var i = 0; i < l; i++) {
            var cross = crossMap.get(k[i]);

            console.log("cross[" + k[i] + "] => " + cross);
        }
        */
    }

    /* [rule] */
    function eventDragRoadSections(event, type, options) {
        if (event == 'mouseup') {
            var element = null;

            if (type == RECTANGLE) {
                var x = options.x;
                var y = options.y;
                var width = options.width;
                var height = options.height;

                /* adjust points */
                var pointTopLeft = canvas.inversePoint(x,y);
                var pointBottomRight = canvas.inversePoint(x+width,y+height);

                x = pointTopLeft.x;
                y = pointTopLeft.y;
                width = pointBottomRight.x - x;
                height = pointBottomRight.y - y;

                /* 무의미한 범위 */
                if (width < 20 && height < 20) {
                	return ;
                }

                /* [GROUP PROCESS] */
                isSingleRoadSelection = false;
        		selectedSingleRoadSection = null;
        		selectedMultiRoadSections  = [];
        		/* END OF [GROUP PROCESS] */

                if (!roadMap.isEmpty()) {
                	/* clear */
                	unselectedAll();

                	/* */
                    var keys = roadMap.keys();
                    var len = keys.length;
                    for (var i = 0; i < len; i++) {
                        var group = roadMap.get(keys[i]);
                        if (group.isInRectangle(x, y, width, height)) {
                        	group.d3element.moveToFront();
                        	setSelected(group);
                        	selectedMultiRoadSections.push(group.userdata);
                        }
                    }
                }
                if (atbsvg.util.isFunction(onMultiSelectedRoadSectionListener) && selectedMultiRoadSections.length > 0) {
                	onMultiSelectedRoadSectionListener(x+width, y+height);
                }
            }
        }
	}


    /* [current driving] */
    function drawVehicle(entry) {
       	/* */
    	var is_violated = entry.last_is_over_speed || entry.last_on_restrict_area;

    	/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
    	var point = LGSmartMapAttr.pointOnMapView(entry.last_x, entry.last_y);
    	/* [2016/06/07] no-scale */
    	point = canvas.transformedPoint(point.x, point.y);

    	/* group */
        var group = atbsvg.primitive.newGroup().id(""+entry.vehicle_uid);
        canvas.appendElement(group);

        /* [2016/06/07] 이미지로 통일: 스케일차단으로 가능 */
        var circle = atbsvg.primitive.newCircle().cx(point.x).cy(point.y).r(VEHICLE_CIRCLE__R).strokeWidth(VEHICLE_STROKEWIDTH);

        var vehicleType = VEHICLE_TYPE.getEntry(entry.vehicle_type);

        /* */
        if (is_violated) {
        	circle.stroke(vehicleType.border).fill(vehicleType.color);
        } else {
        	circle.stroke(vehicleType.border).fill(vehicleType.color);
        }
        canvas.innerElement(group.d3element, circle);
        circle.moveToFront();

        /* */
        group.onClick(function() {
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		onHideTooltipListner();
        	}
        });
        group.on("mouseover", function(){
        	if (atbsvg.util.isFunction(onShowTooltipListner)) {
        		var coordinates = d3.mouse(this);
        		/* [2016/06/07] no-scale */
        		/*
        		 * var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		 * var rcoord = relocationTooltipPosition(point.x,point.y);
        		 */
        		var rcoord = relocationTooltipPosition(coordinates[0],coordinates[1]);
        		onShowTooltipListner(entry,rcoord[0], rcoord[1]);
        	}
        }).on("mousemove", function() {
        	if (atbsvg.util.isFunction(onShowTooltipListner)) {
        		var coordinates = d3.mouse(this);
        		/* [2016/06/07] no-scale */
        		/* */
        		/*
        		 * var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		 * var rcoord = relocationTooltipPosition(point.x,point.y);
        		 */
        		var rcoord = relocationTooltipPosition(coordinates[0], coordinates[1]);
        		onShowTooltipListner(entry,rcoord[0], rcoord[1]);
        	}
        }).on("mouseout", function(){
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		onHideTooltipListner();
        	}
        });

        /* [2016/06/07] no-scale */
        /*
         * canvas.transformedElement(group.d3element);
         */

        /* */
        if (is_violated) {
        	var inverse = canvas.inversePoint(point.x, point.y);
    		rippleWith(inverse.x,inverse.y);
	    }

        /* */
        entry.adbElement = group;

        /* */
        vehicleMap.put(""+entry.vehicle_uid,entry);
    }


    /* [driving] */
    function relocationTooltipPosition(x, y) {
		return [x - 100,y + 35];
	}


    /* [2016/05/31] Deprecated : 중복 경로에 대해 모두 표시하지 않고 중요도 순서에 따라 표시하는 것으로 시나리오 변경
     * 본 소스는 모두 표시하는 함수
     */
    var/*const*/ BEZIER_MARGIN_X = 20;
    var/*const*/ BEZIER_MARGIN_Y = 20;

    /* [driving] [2016/05/31] Deprecated : 사용하지 말것 */
    function drawVehicleDrivingTraceForTesting(entry, points) {
		if (points.length == 0) return ;

		/* */
        var group = atbsvg.primitive.newGroup();
        canvas.appendElement(group);

        /* [2016/05/31] added */
        var lineMap = new atbsvg.map();

        /* */
        var sequence = 0;
        for(var idx = 0; idx < (points.length-1); idx++) {
        	var from = points[idx];
        	var to   = points[idx+1];

        	/* [2016/05/31] added */
        	if (from[0] == to[0] && from[1] == to[1]) {
        		continue;
        	}

        	/* [2016/05/31] added */
        	var key = ''+from[0]+'_'+from[1]+'_'+to[0]+'_'+to[1];
        	var dupCnt = lineMap.get(key);

        	//console.log("key:"+key);
        	if (atbsvg.util.isNullEmpty(dupCnt)) {
        		key = ''+to[0]+'_'+to[1]+'_'+from[0]+'_'+from[1];
        		dupCnt = lineMap.get(key);
        	}

        	/* */
        	var line;
        	var centerX;
        	var centerY;
        	if (atbsvg.util.isNullEmpty(dupCnt)) {
        		line = atbsvg.primitive.newLine()
					.strokeWidth(STROKEWIDTH_DRIVING_LINE)
					.x1(from[0]).y1(from[1])
					.x2(to[0]).y2(to[1]);

        		/* */
        		centerX = (from[0]+to[0])/2;
        		centerY = (from[1]+to[1])/2;
        		/* */
        		lineMap.put(key,1);
        	} else {
        	    var marginX = BEZIER_MARGIN_X * dupCnt;
        	    var marginY = BEZIER_MARGIN_Y * dupCnt;

        		var qx = (from[0]+to[0])/2+marginX;
        		var qy = (from[1]+to[1])/2+marginY;

        		var d = 'M '+ from[0] + ' ' + from[1];
        		d += ' Q '+qx+' '+qy+' '+ to[0] + ' ' + to[1];

        		line = atbsvg.primitive.newPath()
        			.d(d)
        			.strokeWidth(STROKEWIDTH_DRIVING_LINE)
        			.fill('none');

        		/* */
        		centerX = qx;
        		centerY = qy;

        		/* */
        		lineMap.put(key,dupCnt+1);
        	}


        	var count_over_speed = from[2];
        	var count_on_restrict_area = from[3];

        	/* */
        	if (count_over_speed > 0) {
        		line.stroke(COLOR_OVER_SPEED);
        	} else if (count_on_restrict_area > 0) {
        		line.stroke(COLOR_ON_RESTRICT_AREA);
        	} else {
        		line.stroke(COLOR_NORMAL_DRIVING);
        	}

        	if (count_on_restrict_area > 0) {
        		line.strokeDasharray('5,5');
        	}

        	canvas.innerElement(group.d3element, line);

        	/* */
            var text = atbsvg.primitive.newText()
			.x(centerX).y(centerY)
			.stroke('black')
			.fontSize("0.7em")
			.opacity('0.7')
			.textAnchor('middle');

            sequence += 1;
            text.text = ""+sequence;
            canvas.innerElement(group.d3element, text);
        }


        /* */
        var path = atbsvg.animation.appendPointAlongPath(group.d3element, points,
                {
                    'interpolate':'linear',
                    'stroke':'transparent',
                    'stroke-width':'0px',
                    'circle-stroke':TRACE_SMALL_CIRCLE_STROKE,
                    'circle-stroke-width':'0px',
                    'circle-r':TRACE_SMALL_CIRCLE_R
                });

        var circle = atbsvg.primitive.newCircle()
		    .cx(points[points.length-1][0]).cy(points[points.length-1][1])
		    .r(CIRCLE_POINT_R)
		    .fill(COLOR_BORDER_CAR)
		    .stroke(COLOR_BORDER_CAR)
		    .strokeWidth('2px');
		    //.opacity('0.7');
	    canvas.innerElement(group.d3element, circle);
	    circle.moveToFront();

        var carImage = atbsvg.primitive.newImage()
    	.x(points[0][0]-(carHistorySize/2)).y(points[0][1]-(carHistorySize/2))
    	.width(carHistorySize).height(carHistorySize).href(normalCarPath);
        canvas.innerElement(group.d3element, carImage);

        canvas.transformedElement(group.d3element);

		/*
		 * [클릭해야 움직이는 것으로 조정]
         * atbsvg.animation.pointAlongPath(canvas, path.d3element, carImage.d3element, {duration:15000, startX:points[0][0], startY:points[0][1]});
		 */

        /* */
        group.onClick(function() {
        	var lpath = path.d3element.node(); /* [2016/08/30] 이정래차장 script 변경요청 작용 lpath 적용 */
        	atbsvg.animation.pointAlongPath(canvas, path.d3element, carImage.d3element, {duration:lpath.__data__.length*200, startX:points[0][0], startY:points[0][1]});
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		onHideTooltipListner();
        	}
        });
        group.on("mouseover", function(){
        	if (atbsvg.util.isFunction(onShowTooltipListner)) {
        		var coordinates = d3.mouse(this);
        		/* */
        		var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		var rcoord = relocationTooltipPosition(point.x,point.y);
        		onShowTooltipListner(entry,rcoord[0], rcoord[1]);
        	}
        }).on("mousemove", function() {
        	if (atbsvg.util.isFunction(onShowTooltipListner)) {
        		var coordinates = d3.mouse(this);
        		/* */
        		var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		var rcoord = relocationTooltipPosition(point.x,point.y);
        		onShowTooltipListner(entry,rcoord[0], rcoord[1]);
        	}
        }).on("mouseout", function(){
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		onHideTooltipListner();
        	}
        });
    }


    /* [2016/05/31] 중복 경로에 대해 모두 표시하지 않고 중요도 순서에 따라 표시하는 것으로 시나리오 변경
     * 본 소스는 중요도 순서로 표시하는 함수
     */
    function drawVehicleDrivingTrace(entry, points) {
		if (points.length == 0) return ;

		/* */
		try {
			if (TRACE_HISTORY_ON == true) {
				drawVehicleDrivingTraceForTesting(entry, points);
				return ;
			}
		} catch(e) {}

		/* */
        var group = atbsvg.primitive.newGroup();
        canvas.appendElement(group);

        /* */
        var showMap = new atbsvg.map();

        /* */
        var vehicleType = VEHICLE_TYPE.getEntry(entry.vehicle_type);

        /* */
        for(var idx = 0; idx < (points.length-1); idx++) {
        	var from = points[idx];
        	var to   = points[idx+1];

        	//console.log("from:("+from[0]+","+from[1]+") to:"+to[0]+","+to[1]+") uid:"+from[4]);
        	if (from[0] == to[0] && from[1] == to[1]) {
        		continue;
        	}

        	/* [2016/05/31] added */
        	var key = ''+from[0]+'_'+from[1]+'_'+to[0]+'_'+to[1];
        	var options = showMap.get(key);

        	if (atbsvg.util.isNullEmpty(options)) {
        		key = ''+to[0]+'_'+to[1]+'_'+from[0]+'_'+from[1];
        		options = showMap.get(key);
        	}

        	if (atbsvg.util.isNullEmpty(options)) {
        		/* new */
        		options = {
        			x1 : from[0],
        			y1 : from[1],
        			x2 : to[0],
        			y2 : to[1],
        			count_over_speed : from[2],
        			count_on_restrict_area : from[3],
        			centerX : (from[0]+to[0])/2,
        			centerY : (from[1]+to[1])/2,
        			section_uid : from[4],
        			speed : from[5]
        		};

        		showMap.put(key,options);
        	} else {
        		/* */
            	var count_over_speed = from[2];
            	var count_on_restrict_area = from[3];

            	/* 기존것이 이미 제한지역이라면 No Update */
            	if (options.count_on_restrict_area > 0) {
            		continue;
            	}
        		if (count_on_restrict_area > 0) {
        			options.count_on_restrict_area = count_on_restrict_area;
        			continue;
        		}

        		/* 기존것이 이미 속도위반이라면 No Update */
            	if (options.count_over_speed > 0) {
            		continue;
            	}
        		if (count_over_speed > 0) {
        			options.count_over_speed = count_over_speed;
        			continue;
        		}
        	}
        }


        var lineArray = showMap.values();
        for(var idx = 0; idx < lineArray.length; idx++) {
        	var options = lineArray[idx];

        	/* */
        	var line = atbsvg.primitive.newLine()
					.strokeWidth(STROKEWIDTH_DRIVING_LINE)
					.x1(options.x1).y1(options.y1)
					.x2(options.x2).y2(options.y2)
					.strokeLinecap('round');

          	/* [2016/08/30] 이정래차장 script 변경요청 작용 .opacity('0.7') */
        	/* [2016/09/08] 교차점을 찾지 못하는 경우에는 투명하게 표현 */
        	/* [2016/09/22] 속도가 10 미만인 것만 투명하게 표시 */

        	if (options.count_on_restrict_area > 0) {
        		line.stroke(COLOR_ON_RESTRICT_AREA)
        			.strokeDasharray('5,5')
        			.opacity('0.7');
        	} else if  (options.count_over_speed > 0) {
        		line.stroke(COLOR_OVER_SPEED)
        		.opacity('0.7');;
        	} else if (options.speed < 10) {
        		/* [2016/09/22] added */
        		line.stroke(COLOR_TRANSPARENT);
        	} else {
        		/* [2016/09/08] gray로 통일 */
        		/* line.stroke(vehicleType.color) */
        		line.stroke(COLOR_NORMAL_DRIVING)
        		.opacity('0.7');;
        	}

        	canvas.innerElement(group.d3element, line);
        }


        /* */
        var path = atbsvg.animation.appendPointAlongPath(group.d3element, points,
                {
		            'interpolate':'linear',
		            'stroke':'transparent',
		            'stroke-width':'0px',
		            'circle-stroke':TRACE_SMALL_CIRCLE_STROKE,
		            'circle-stroke-width':'0px',
		            'circle-r':TRACE_SMALL_CIRCLE_R
                });

        var circleEnd = atbsvg.primitive.newCircle()
		    .cx(points[points.length-1][0]).cy(points[points.length-1][1])
		    .r(CIRCLE_POINT_R)
		    .fill(vehicleType.color)
		    .stroke(vehicleType.border)
		    .strokeWidth('2px');
		    //.opacity('0.7');
	    canvas.innerElement(group.d3element, circleEnd);
	    circleEnd.moveToFront();


	    var circleStart = atbsvg.primitive.newCircle()
		    .cx(points[0][0]).cy(points[0][1])
		    .r(CIRCLE_POINT_R)
		    .fill(vehicleType.color)
		    .stroke(vehicleType.border)
		    .strokeWidth('2px');
		    //.opacity('0.7');
	    canvas.innerElement(group.d3element, circleStart);
	    circleStart.moveToFront();

        var carImage = atbsvg.primitive.newImage()
        	.x(points[0][0]-(carHistorySize/2)).y(points[0][1]-(carHistorySize/2))
        	.width(carHistorySize).height(carHistorySize).href(normalCarPath);
        canvas.innerElement(group.d3element, carImage);
        carImage.moveToFront();



        /* */
        canvas.transformedElement(group.d3element);

        /* [클릭해야 움직이는 것으로 조정]
         * atbsvg.animation.pointAlongPath(canvas, path.d3element, carImage.d3element, {duration:17000, startX:points[0][0], startY:points[0][1]});
    	 */
        /* */
        group.onClick(function() {
        	var lpath = path.d3element.node(); /* [2016/08/30] 이정래차장 script 변경요청 작용 lpath 추가 */
			atbsvg.animation.pointAlongPath(canvas, path.d3element, carImage.d3element, {duration:lpath.__data__.length*200, startX:points[0][0], startY:points[0][1]});
			if (atbsvg.util.isFunction(onHideTooltipListner)) {
				 onHideTooltipListner();
			}
        });
        group.on("mouseover", function(){
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		var coordinates = d3.mouse(this);
        		/* */
        		var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		var rcoord = relocationTooltipPosition(point.x,point.y);
        		onShowTooltipListner(entry,rcoord[0], rcoord[1]);
        	}
        }).on("mousemove", function() {
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		var coordinates = d3.mouse(this);
        		/* */
        		var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		var rcoord = relocationTooltipPosition(point.x,point.y);
        		onShowTooltipListner(entry,rcoord[0], rcoord[1]);
        	}
        }).on("mouseout", function(){
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		onHideTooltipListner();
        	}
        });
    }



    /* [driving] */
    function drawVehicleDrivingEventTrace(entry, list) {
    	if (atbsvg.util.isNull(list)) return ;

		var points = [];
		for(var idx = 0; idx < list.length; idx++) {
			var item = list[idx];

			/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
	    	var repoint = LGSmartMapAttr.pointOnMapView(item.x, item.y);

			/* */
			var point = [repoint.x,repoint.y,item.is_over_speed,item.is_on_restrict_area, null, item.speed];
			points.push(point);
		}

		/* */
		lastHistoryEntry = entry;
		lastHistoryPoints = points;

		/* */
		drawVehicleDrivingTrace(entry,points);
    }

    /* [driving] */
    function geOrthoIntersect(x1, y1, x2, y2, px, py) {
    	if (x1 == x2) {
    		return {
    			x : x1,
    			y : py
    		}
    	}
    	if (y1 == y2) {
    		return {
    			x : px,
    			y : y1
    		}
    	}

    	var slope = (y2-y1)/(x2-x1);
    	var orthoslope = -1 / slope;
    	if (slope == orthoslope) {
    		return {
    			x : px,
    			y : py
    		}
    	}

	    // x = (m1x1 - m2x2 + y2 - y1) / (m1 - m2)
	    var intersectX = ((slope * x1) - (orthoslope * px) + py - y1) / (slope - orthoslope);
	    // y = m1(x - x1) + y1
	    var intersectY = slope * (intersectX - x1) + y1;

	    return {
	        x: intersectX,
	        y: intersectY
	    };
	}

    /* [2016/06/07] added */
    function isValidCoord(x1, y1) {
		if (x1 <= 0
			|| y1 <= 0
			|| x1 >= LGSmartMapAttr.mapWidth()
			|| y1 >= LGSmartMapAttr.mapHeight()) {
			return false;
		}

		return true;
    }

	function isOufOfIntersectRange(intersect,fromSection,toSection) {
		var sx = LGSmartMapAttr.mapWidth();
		var sy = LGSmartMapAttr.mapHeight();
		var ex = 0;
		var ey = 0;

		if (sx > fromSection.x1) sx = fromSection.x1;
		if (sx > fromSection.x2) sx = fromSection.x2;
		if (sx > toSection.x1) sx = toSection.x1;
		if (sx > toSection.x2) sx = toSection.x2;

		if (sy > fromSection.y1) sy = fromSection.y1;
		if (sy > fromSection.y2) sy = fromSection.y2;
		if (sy > toSection.y1) sy = toSection.y1;
		if (sy > toSection.y2) sy = toSection.y2;

		if (ex < fromSection.x1) ex = fromSection.x1;
		if (ex < fromSection.x2) ex = fromSection.x2;
		if (ex < toSection.x1) ex = toSection.x1;
		if (ex < toSection.x2) ex = toSection.x2;

		if (ey < fromSection.y1) ey = fromSection.y1;
		if (ey < fromSection.y2) ey = fromSection.y2;
		if (ey < toSection.y1) ey = toSection.y1;
		if (ey < toSection.y2) ey = toSection.y2;


		if (sx > intersect[0] || ex < intersect[0]) return true;
		if (sy > intersect[1] || ey < intersect[1]) return true;

		return false;
	}

    /* [2016/10/13] 두 도로가 인접한 도로인지 확인 */
    function checkAdjacency(from_uid, to_uid) {
        //console.log("from_uid: " + from_uid + " -> to_uid:" + to_uid);
        //console.log(crossMap.get(from_uid));

        return crossMap.get(from_uid).indexOf(to_uid) != -1;
    }

    /* [2016/10/13] 두 도로 사이 최단 경로 계산. 두 도로를 자연스럽게 이을 수 있는 point[]를 리턴
       param: from {x:x, y:y, r:section_uid},
       param: to {x:x, y:y, r:section_uid}
       return from 과 to 사이 들어갈 point[], ex) [{x, y}, {x, y}]
    */
    function findShortestPath(from, to) {
        //console.log("from: " + atbsvg.util.toJsonString(from) + ", to: " + atbsvg.util.toJsonString(to));

        var ret = [];
        var roadBackTrack = new atbsvg.map();   // r -> r
        var pointBackTrack = new atbsvg.map();  // p -> p
        var pointCostMap = new atbsvg.map();    // p -> cost

        // tuple {cost, road, point[x, y]}
        var queue = new PriorityQueue({ comparator: function(a, b) { return a.cost - b.cost; }});

        var costFunction = function(p1, p2) {
            return math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2));
        };
        var estimateFunction = function (p1, p2) {
            return costFunction(p1, p2); // euclidean
        };
        var pointKey = function(p) {
            return "" + p.x + "_" + p.y;
        };
        var keyPoint = function(key) {
            var p = key.split("_");
            return {x:p[0], y:p[1]};
        };
        var expandRoads = function(point, road) {
            var ret = new atbsvg.map();
            var connected = crossMap.get(road);

            for (var i = 0; i < connected.length; i++) {
                var c = connected[i];
                var candi = roadMap.get(c);
                if (candi == null) {
                    continue;
                }

                if (candi.x1 == point.x && candi.y1 == point.y) {
                    ret.put(c, {x: candi.x2, y: candi.y2});
                } else if (candi.x2 == point.x && candi.y2 == point.y) {
                    ret.put(c, {x: candi.x1, y: candi.y1});
                } else {
                    // do nothing
                }
            }

            //console.log("[" + road + "] " + ret.size() + "/" + connected.length + " candidates");

            return ret; // road -> p
        };

        var sroad = roadMap.get(from.r);
        var eroad = roadMap.get(to.r);

        var p1 = {x:sroad.x1, y:sroad.y1};
        var p2 = {x:sroad.x2, y:sroad.y2};
        var cost1 = costFunction(from, p1);
        var cost2 = costFunction(from, p2);

        pointBackTrack.put(pointKey(p1), from);
        pointBackTrack.put(pointKey(p2), from);

        pointCostMap.put(pointKey(p1), cost1);
        pointCostMap.put(pointKey(p2), cost2);

        queue.queue({cost: cost1 + estimateFunction(p1, to), road: from.r, point: p1});
        queue.queue({cost: cost2 + estimateFunction(p2, to), road: from.r, point: p2});


        var iteration = 0;

        while (queue.length > 0) {
            var curr = queue.dequeue(); // {cost, road, point[x, y]}

            var cost = pointCostMap.get(pointKey(curr.point));

            iteration++;
            //console.log("i:" + iteration + " | road " + curr.road + ", p" + atbsvg.util.toJsonString(curr.point));

            if (to.r == curr.road) {
                queue.queue(curr);
                break;
            }

            var candidates = expandRoads(curr.point, curr.road); // road -> p

            var keys = candidates.keys();
            var length = keys.length;
            for (var i = 0; i < length; i++) {
                var next = candidates.get(keys[i]);

                //console.log("i:" + iteration + "-" + (i+1) + " - curr " + curr.road + " => next " + keys[i] + ", p" + atbsvg.util.toJsonString(next));

                var nextCost = cost + costFunction(curr.point, next);
                var estimate = estimateFunction(next, to);

                if (to.r != keys[i] && pointCostMap.get(pointKey(next)) != null) {
                    continue;
                }

                if (to.r == keys[i]) {
                    queue.clear();
                }

                pointCostMap.put(pointKey(next), nextCost);

                queue.queue({cost: nextCost + estimate, road: keys[i], point: next});

                pointBackTrack.put(pointKey(next), curr.point);

                //console.log("i:" + iteration + "-" + (i+1) + " => cost: " + nextCost + ", expected: " + (nextCost + estimate));

                if (to.r == keys[i]) {
                    break;
                }
            }

            //console.log("i:" + iteration + " ended. go next round w/ queue size of " + queue.length);
        }

        if (queue.length == 0) {
            console.log("queue becomes empty: no path exists");
            return ret;
        }

        // TODO: if one-way attribute introduced
        var result = queue.dequeue();
        if (to.r != result.road) {
            console.log("not end road"); // it can't happen at this moment
            return ret;
        }

        //console.log("path found, s:" + JSON.stringify(from) + ", e:" + JSON.stringify(to) + ", cost: " + pointCostMap.get(pointKey(result.point)));

        var backTrackPoint = result.point;
        ret.push(to);
        while (true) {
            if (backTrackPoint.x == from.x && backTrackPoint.y == from.y) {
                break;
            }

            backTrackPoint = pointBackTrack.get(pointKey(backTrackPoint));
            if (backTrackPoint == null) {
                break;
            }

            ret.push(backTrackPoint);
        }

        return ret.reverse();
    }

    /* [driving] */
    function drawVehicleDrivingSectionTrace(entry, list) {
    	if (atbsvg.util.isNull(list)) return ;

        //console.log("entry: " + JSON.stringify(entry));
        //console.log("list: " + JSON.stringify(list));

		var points = [];
		var interp;
		var repoint;
		var point;

		if (list.length > 0) {
			/* start */
			var item  = list[0];

			/* [2016/06/08] 유효한 좌표일 경우만 */
			if (isValidCoord(item.to_x,item.to_y)) {
				/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
		    	repoint = LGSmartMapAttr.pointOnMapView(item.from_x, item.from_y);

		    	/* */
				point = [repoint.x,repoint.y,item.count_over_speed,item.count_on_restrict_area, null, item.speed];
				points.push(point);
			}

			/* */
			for(var idx = 0; idx < (list.length-1); idx++) {
				from = list[idx];
				to   = list[idx+1];

				var fromSection = roadMap.get(from.section_uid);
				var toSection = roadMap.get(to.section_uid);

				/* */
				if (atbsvg.util.isNull(fromSection) || atbsvg.util.isNull(toSection)) {
					/* to */
					/* [2016/06/08] 유효한 좌표일 경우만 */
					if (isValidCoord(from.to_x, from.to_y)) {
						/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
						repoint = LGSmartMapAttr.pointOnMapView(from.to_x, from.to_y);
						point = [repoint.x,repoint.y,from.count_over_speed,from.count_on_restrict_area, null, from.speed];
						points.push(point);
					}

					/* from */
					/* [2016/06/08] 유효한 좌표일 경우만 */
					if (isValidCoord(to.from_x, to.from_y)) {
						/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
						repoint = LGSmartMapAttr.pointOnMapView(to.from_x, to.from_y);
						point = [repoint.x,repoint.y,to.count_over_speed,to.count_on_restrict_area, null, to.speed];
						points.push(point);
					}

					continue;
				}

                /* [2016/10/13] 연결되지 않는 도로 정보 연결
                    출입제한도로의 경우, 본 record 생성 시점의 출입제한도로 정보를 알 수 없으니
                    출입제한도로 정보는 참조하지 않고 그냥 그림
                */

                if (!checkAdjacency(from.section_uid, to.section_uid)) {
                    //console.log("" + from.section_uid + " -> " + to.section_uid + " not connected");

                    var from = {x:from.to_x, y:from.to_y, r:from.section_uid};
                    var to = {x:to.from_x, y:to.from_y, r:to.section_uid};
                    var path = findShortestPath(from, to);

                    for (var i = 0; i < path.length; i++) {
                        var p = path[i];
                        if (isValidCoord(p.x, p.y)) {
                            repoint = LGSmartMapAttr.pointOnMapView(p.x, p.y);
                            point = [repoint.x,repoint.y, 0, 0, null, 20];
                            points.push(point);
                        }
                    }

                    continue;
                }


				var intersect = math.intersect([fromSection.x1, fromSection.y1],
						[fromSection.x2, fromSection.y2],
						[toSection.x1, toSection.y1],
						[toSection.x2, toSection.y2]);

				/* [2016/06/03] */
				if (atbsvg.util.isNull(intersect)) {
					/* 수평 or 수직 */
					/* to */

					/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
					/*
					 * var repoint = LGSmartMapAttr.pointOnMapView(from.to_x, from.to_y);
					 */
					/* [2016/06/07] 벗어난 점을 도로위의 점으로 조정 */
					var interp = geOrthoIntersect(fromSection.x1, fromSection.y1, fromSection.x2, fromSection.y2, from.to_x, from.to_y);
					/* [2016/06/08] 유효한 좌표일 경우만 */
					if (isValidCoord(interp.x, interp.y)) {
						repoint = LGSmartMapAttr.pointOnMapView(interp.x, interp.y);
						point = [repoint.x,repoint.y,from.count_over_speed,from.count_on_restrict_area, null, from.speed];
						points.push(point);
					}

					/* from */
					/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
					/*
					 * repoint = LGSmartMapAttr.pointOnMapView(to.from_x, to.from_y);
					 */
					/* [2016/06/07] 벗어난 점을 도로위의 점으로 조정 */
					interp = geOrthoIntersect(toSection.x1, toSection.y1, toSection.x2, toSection.y2, to.from_x, to.from_y);
					/* [2016/06/08] 유효한 좌표일 경우만 */
					if (isValidCoord(interp.x, interp.y)) {
						repoint = LGSmartMapAttr.pointOnMapView(interp.x, interp.y);
						point = [repoint.x,repoint.y,to.count_over_speed,to.count_on_restrict_area, null, to.speed];
						points.push(point);
					}

					continue;
				}


				/*
				 * [2016/06/03] append : 유효하지 않은 좌표일 경우
				 * [2016/07/01] 교차점이 유효하지 않거나 두 도로 범위를 벗어날 때.
				 */
				if (!isValidCoord(intersect[0],intersect[1]) ||
					 isOufOfIntersectRange(intersect,fromSection,toSection)) {
					/* 수평 or 수직 */
					/* to */
					/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
					/*
					 * var repoint = LGSmartMapAttr.pointOnMapView(from.to_x, from.to_y);
					 */
					/* [2016/06/07] 벗어난 점을 도로위의 점으로 조정 */
					interp = geOrthoIntersect(fromSection.x1, fromSection.y1, fromSection.x2, fromSection.y2, from.to_x, from.to_y);
					/* [2016/06/08] 유효한 좌표일 경우만 */
					if (isValidCoord(interp.x, interp.y)) {
						repoint = LGSmartMapAttr.pointOnMapView(interp.x, interp.y);

						point = [repoint.x,repoint.y,from.count_over_speed,from.count_on_restrict_area, null, from.speed];
						points.push(point);
					}

					/* from */
					/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
					/*
					 * repoint = LGSmartMapAttr.pointOnMapView(to.from_x, to.from_y);
					 */
					/* [2016/06/07] 벗어난 점을 도로위의 점으로 조정 */
					interp = geOrthoIntersect(toSection.x1, toSection.y1, toSection.x2, toSection.y2, to.from_x, to.from_y);
					/* [2016/06/08] 유효한 좌표일 경우만 */
					if (isValidCoord(interp.x, interp.y)) {
						repoint = LGSmartMapAttr.pointOnMapView(interp.x, interp.y);
						point = [repoint.x,repoint.y,to.count_over_speed,to.count_on_restrict_area, null, to.speed];
						points.push(point);
					}

					continue;
				}

				/* intersect */
				/* [2016/06/08] 유효한 좌표일 경우만 */
				if (isValidCoord(intersect[0],intersect[1])) {
					/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
					repoint = LGSmartMapAttr.pointOnMapView(intersect[0],intersect[1]);
					point = [repoint.x,repoint.y,to.count_over_speed,to.count_on_restrict_area,to.section_uid, to.speed];
					points.push(point);
				}
			}

			/* last */
			item  = list[list.length-1];
			/* [2016/06/03] append : 유효하지 않은 좌표일 경우 */
			if (isValidCoord(item.to_x,item.to_y)) {
				/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
				repoint = LGSmartMapAttr.pointOnMapView(item.to_x,item.to_y);
				point = [repoint.x,repoint.y,0,0,null,item.speed];
				points.push(point);
			}
		}

		/* */
		lastHistoryEntry = entry;
		lastHistoryPoints = points;

		/* */
		drawVehicleDrivingTrace(entry,points);
    }



	/*
	 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
	 * [2016/06/13] @see lg_smartmap_for_wwms
	 */
    function showFactoryElement(factory) {
    	if (atbsvg.util.isNull(factory)) return ;

		/* [2016/06/27]
		 * 비콘존하고 겹치는 공장은 제거하기 위해 is_factory 항목이용
		 */
		if (factory.is_factory != 1) return ;


    	/* group */
        var group = atbsvg.primitive.newGroup().id(""+factory.factry_uid);
        canvas.appendElement(group);


        var point = LGSmartMapAttr.pointOnMapView(factory.centerX, factory.centerY);
        var text = atbsvg.primitive.newText()
			.x(point.x).y(point.y)
			.stroke(FACTORY_FONT_COLOR)
			.strokeWidth('0.3px')
			.fontSize(FACTORY_FONTSIZE)
			.letterSpacing(FACTORY_LETTER_SPACING)
			.fill(FACTORY_FONT_COLOR)
			.textAnchor('middle');
		text.text = ""+factory.name;

		canvas.innerElement(group.d3element, text);

		/* */
        canvas.transformedElement(group.d3element);

        /* */
        factory.adbElement = group;
    }

	/*
	 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
	 * [2016/06/13] @see lg_smartmap_for_wwms
	 */
    function showFactoryElements() {
    	if (atbsvg.util.isNull(factoryList)) return ;

    	var size = factoryList.length;
    	for(var index = 0; index < size; index++) {

    		/* [2016/06/27]
    		 * 비콘존하고 겹치는 공장은 제거하기 위해 is_factory 항목이용
    		 */
    		if (factoryList[index].is_factory == 1) {
    			showFactoryElement(factoryList[index]);
    		}
    	}
    }

    /* ----------------------------------------------- */
    /* public */
    /* ----------------------------------------------- */
    return {
    	/* [common] */
    	clear : function() {
    		clear();
    	},
    	/* [common] */
    	clearMapOnly : function() {
    		clearMapOnly();
    	},
    	/* [common] */
    	showAlert : function(msg) {
    		showPopupAlert(msg);
    	},
    	/* [common] */
    	zoomEnabled : function(zoomable) {
    		canvas.zoomEnabled(zoomable);
    	},
    	/* [common] */
    	dragEnabled : function(draggable) {
    		canvas.dragEnabled(draggable);
    	},
    	/* [common] */
    	zoomScale : function(scale) {
    		canvas.zoomScale(scale);
    	},
    	/* [common] */
    	zoomTranslate : function(translate) {
    		canvas.zoomTranslate(translate);
    	},
    	/* [common] */
    	zoomIn : function() {
    		 var scale =  canvas.scale() + 0.2;
            if (scale <= maxScale) {
            	canvas.zoomScale(scale);
            }
    	},
    	/* [common] */
    	zoomOut : function() {
            var scale = canvas.scale() - 0.2;
            if (scale >= minScale) {
            	canvas.zoomScale(scale);
            }
        },
        /* [common] */
        zoomReset : function() {
            canvas.zoomReset();
        },
    	/* [common] */
    	center : function() {
    		return 	[width/2,height/2];
    	},
    	/* [common] */
    	width : function() {
    		return width;
    	},
    	/* [common] */
    	height : function() {
    		return height;
    	},
    	/* [rule/driving] */
    	relocationDialogPosition : function (x, y) {
    		return relocationDialogPosition(x,y);
    	},
        /* [rule/driving] */
    	redrawRoadMap : function(vehicleType) {
    		currentSelectedVehicleType = '';
    		if (arguments.length > 0) {
    			currentSelectedVehicleType = vehicleType;
    		}

    		clear();

    		/* ngmsUrl in head.jsp */
    		var url = ngmsUrl+'/road_map.do' ;
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;

    		/* [2016/08/18] added */
    		if (!atbsvg.util.isNullEmpty(currentSelectedVehicleType))
    		{
    			url += '?vtype='+currentSelectedVehicleType;
    		}

    		/* */
    		doRestFulApi(url, headerMap, method, paramMap,
    			function (result) {
    				if (!atbsvg.util.isNull(result) &&
    						!atbsvg.util.isNull(result.roadSectionList)) {
    					refreshRoadMapWith(result.roadSectionList);
    					if (atbsvg.util.isFunction(onCompletedLoadMapListener)) {
    						onCompletedLoadMapListener(result.roadSectionList);
    					}
    				}
    			});
    	},
    	getCurrentVehicleType : function() {
    		return currentSelectedVehicleType;
    	},
    	/* [rule/driving] */
    	showableRoadMap : function(showable) {
    		showableRoadMap = showable;
    	},
    	/* [common] */
    	setOnSuccessUpdateListener : function(cbfunc) {
    		onSuccessUpdateListener = cbfunc;
    	},
    	/* [rule] */
    	showSpeedLimitOnMap : function(enabled) {
    		showSpeedLimitOnMap = enabled;
    	},
    	/* [rule] */
    	refreshRoadMapWith  : function(roadSectionList) {
    		refreshRoadMapWith(roadSectionList);
    	},
    	/* [rule] */
    	setOnCompletedLoadMapListener : function(cbfunc) {
    		onCompletedLoadMapListener = cbfunc;
    	},
    	/* [rule] */
    	setOnClickedRoadSectionListener : function(cbfunc) {
    		onClickedRoadSectionListener = cbfunc;
    	},
    	/* [rule] */
    	unselectAll : function() {
    		unselectedAll();
    	},
    	/* [rule] */
    	enabledRoadDragDrop: function() {
    		canvas.initMouseDrag(eventDragRoadSections);
    	},
    	/* [rule] */
    	setOnMultiSelectedRoadSectionListener : function(cbfunc) {
    		onMultiSelectedRoadSectionListener = cbfunc;
    	},
    	/* [rule] */
    	isSingleRoadSelection : function() {
    		return isSingleRoadSelection;
    	},
    	/* [rule] */
    	getSelectdSingleRoadSection : function() {
    		return selectedSingleRoadSection;
    	},
    	/* [rule] */
    	getSelectedMultiRoadSections : function() {
    		return selectedMultiRoadSections;
    	},
    	/* [rule] */
    	getSelectedMultiRoadSectionString : function() {
    		if (this.isSingleRoadSelection() ||
    				atbsvg.util.isNull(selectedMultiRoadSections) ||
    				selectedMultiRoadSections.length == 0) {
    			return '';
    		}

			/* */
			var tokens = '';
			var len = selectedMultiRoadSections.length;
            for (var i = 0; i < len; i++) {
            	var entry = selectedMultiRoadSections[i];

            	if (tokens != '') { tokens += ",";}
            	tokens += ""+entry.section_uid;
            }

            return tokens;
    	},
    	/* [rule] */
    	getAllRoadSectionString : function() {
			/* */
			var tokens = '';
			var keys = roadMap.keys();
            var len = keys.length;
            for (var i = 0; i < len; i++) {
                var group = roadMap.get(keys[i]);
                if (tokens != '') { tokens += ",";}
            	tokens += ""+group.userdata.section_uid;
            }

            return tokens;
    	},

    	/* [rule] UPDATE RESTRICT LINE */
    	/* [rule] */
    	requestUpdateRestrictLineSectionList : function(uids, vehicle_type, is_restrict_area) {
    		if (atbsvg.util.isNullEmpty(uids) || atbsvg.util.isNullEmpty(vehicle_type) || atbsvg.util.isNullEmpty(is_restrict_area)) {
    			showPopupAlert('도로를 선택하신 후 수정이 가능합니다.');
    			return ;
    		}

    		/* ngmsUrl in head.jsp */
    		var url = ngmsUrl+'/restrict_area.do' ;
			var method = 'POST';
			var headerMap = new requestHeaderObject(g_accessToken, g_requestType);

			var paramMap = {'section_uids':uids, 'vehicle_type':vehicle_type, 'is_restrict_area':is_restrict_area };
			/* */
			doRestFulApi(url, headerMap, method, paramMap,
				function (result) {
					if (atbsvg.util.isFunction(onSuccessUpdateListener)) {
						onSuccessUpdateListener();
					}
				});
    	},
       	/* [rule] */
    	requestUpdateRestrictLineMultiSelected : function(is_restrict_area) {
    		var uids = this.getSelectedMultiRoadSectionString();
    		this.requestUpdateRestrictLineSectionList(uids, currentSelectedVehicleType, is_restrict_area);
    		selectedMultiRoadSections = null;
    	},
    	/* [speed] */
    	requestUpdateRestrictLineSingleSelected : function(is_restrict_area) {
    		if (!this.isSingleRoadSelection() || atbsvg.util.isNull(selectedSingleRoadSection)) {
    			showPopupAlert('도로를 선택하신 후 수정이 가능합니다.');
    			return ;
    		}
    		this.requestUpdateRestrictLineSectionList(selectedSingleRoadSection.section_uid, currentSelectedVehicleType, is_restrict_area);
    		selectedSingleRoadSection = null;
    	},
    	/* [rule] UPDATE SPEED LIMIT */
    	/* [rule] */
    	requestUpdateSpeedLimitSectionList : function(uids, speed_limit) {
    		if (atbsvg.util.isNullEmpty(uids) ||atbsvg.util.isNullEmpty(speed_limit) || speed_limit < 0) {
    			showPopupAlert('도로를 선택하신 후 수정이 가능합니다.');
    			return ;
    		}

    		/* ngmsUrl in head.jsp */
    		var url = ngmsUrl+'/speed_limit.do' ;
			var method = 'POST';
			var headerMap = new requestHeaderObject(g_accessToken, g_requestType);


            var paramMap = {'section_uids':uids, 'speed_limit':speed_limit};
			/* */
			doRestFulApi(url, headerMap, method, paramMap,
				function (result) {
					if (atbsvg.util.isFunction(onSuccessUpdateListener)) {
						onSuccessUpdateListener();
					}
				});
    	},
    	/* [rule] */
    	requestUpdateSpeedLimitMultiSelected : function(speed_limit) {
    		var uids = this.getSelectedMultiRoadSectionString();
    		this.requestUpdateSpeedLimitSectionList(uids,speed_limit);
    		selectedMultiRoadSections = null;
    	},
    	/* [rule] */
    	requestUpdateSpeedLimitAll : function(speed_limit) {
    		/* ngmsUrl in head.jsp */
    		this.requestUpdateSpeedLimitSectionList("*",speed_limit);
    	},
    	/* [speed] */
    	requestUpdateSpeedLimitSingleSelected : function(speed_limit) {
    		if (!this.isSingleRoadSelection() || atbsvg.util.isNull(selectedSingleRoadSection)) {
    			showPopupAlert('도로를 선택하신 후 수정이 가능합니다.');
    			return ;
    		}
    		this.requestUpdateSpeedLimitSectionList(selectedSingleRoadSection.section_uid, speed_limit);
    		selectedSingleRoadSection = null;
    	},
    	/* end of speed limit */

    	/* [driving] */
    	setOnCompletedDrivingListener : function(cbfunc) {
    		onCompletedDrivingListener = cbfunc;
    	},
    	/* [current driving] */
    	redrawCurrentDrivingMap : function() {
    		/* */
    		clearMapOnly();
    		/* [2016/09/22] added */
    		clearOverlayAll();

    		/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    		showFactoryElements();

     		if (!atbsvg.util.isNull(currentVehicleArray)) {
	    		for(var idx = 0; idx < currentVehicleArray.length; idx++) {
	    			var entry = currentVehicleArray[idx];
	    			/* */
	    			entry.adbElement = null;
	    			drawVehicle(entry);
	    		}
	    	}
    	},
    	/* [current driving] */
    	requestCurrentDrivingList : function () {
    		var getparams = "is_current=1";

    		/* */
    		/* ngmsUrl in head.jsp */
    		var url = encodeURI(ngmsUrl+'/vehicle_status.do?'+getparams) ;
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;

    		doRestFulApi(url, headerMap, method, paramMap,
    			function (result) {
    				if (!atbsvg.util.isNull(result) &&
    						!atbsvg.util.isNull(result.vehicleStatusList)) {

    					/* */
    					clear();

    					/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    		    		showFactoryElements();

    					/* [2016/05/27] added */
    					var unknownVehicleArray = [];

    					currentVehicleArray = result.vehicleStatusList;
    					if (!atbsvg.util.isNull(currentVehicleArray)) {
    			    		for(var idx = 0; idx < currentVehicleArray.length; idx++) {
    			    			var entry = currentVehicleArray[idx];

    			    			/* [2016/05/27] added : 위치를 알 수 없는 경우 */
    			    			if (atbsvg.util.isNull(entry.last_x) ||
    			    					atbsvg.util.isNull(entry.last_y) ||
    			    					(entry.last_x <= 0 && entry.last_y <= 0) ) {
    			    				/* */
    			    				unknownVehicleArray.push(''+entry.vehicle_no);
    			    				vehicleUnknownMap.put(""+entry.vehicle_uid,entry);

    			    				continue;
    			    			}

    			    			drawVehicle(entry);
    			    		}
    			    	}

    					if (atbsvg.util.isFunction(onCompletedDrivingListener)) {
    						onCompletedDrivingListener(currentVehicleArray,unknownVehicleArray);
    					}
    				}
    			},
    			function () {
    				/* ignore */
    			});
    	},
    	/* [history driving] */
    	requestHistoryDrivingList : function (from, to, violation_type, vehicle_no) {
    		var getparams = "from="+from+"&to="+to+"&violation_type="+violation_type+"&vehicle_no="+vehicle_no;
    		/* ngmsUrl in head.jsp */
    		var url = encodeURI(ngmsUrl+'/vehicle_status.do?'+getparams) ;
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;

    		doRestFulApi(url, headerMap, method, paramMap,
    			function (result) {
    				if (!atbsvg.util.isNull(result) &&
    						!atbsvg.util.isNull(result.vehicleStatusList)) {

    					/* */
    					clear();

    					/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    		    		showFactoryElements();

    					/* */
    					currentVehicleArray = result.vehicleStatusList;
    					if (!atbsvg.util.isNull(currentVehicleArray)) {
    			    		for(var idx = 0; idx < currentVehicleArray.length; idx++) {
    			    			vehicleMap.put(""+currentVehicleArray[idx].vehicle_uid, currentVehicleArray[idx]);
    			    		}
    			    	}

    					if (atbsvg.util.isFunction(onCompletedDrivingListener)) {
    						onCompletedDrivingListener(currentVehicleArray);
    					}
    				}
    			});
    	},
    	/* [current driving] */
    	setOnHideTooltipListner : function(cbfunc) {
    		onHideTooltipListner = cbfunc;
    	},
    	/* [current driving] */
    	setOnShowTooltipListner : function(cbfunc) {
    		onShowTooltipListner = cbfunc;
    	},
    	/* [current driving] */
    	getCurrentVehicleArray : function() {
    		return currentVehicleArray;
    	},
    	/* [current driving] */
    	getCurrentVehicleSize : function() {
    		if (atbsvg.util.isNull(currentVehicleArray)) return 0;
    		return currentVehicleArray.length;
    	},
    	/* [current driving] */
    	getCurrentVehicle : function(index) {
    		if (atbsvg.util.isNull(currentVehicleArray)) return null;
    		return currentVehicleArray[index];
    	},
    	/* [driving] */
    	redrawDrivingTable : function($body, $paging, funcname) {
    		this.redrawDrivingTableOnPage($body, $paging, funcname, 1);
    	},
    	/* [driving] */
    	redrawDrivingTableOnPage : function($body, $paging, funcname, page) {
    		$tablebodyDrivingTable = $body;
    		$pagingDrivingTable = $paging;
    		gotoDrivingTableFuncName = funcname;

    		this.refreshDrivingTablePage(page);
    	},
    	/* [2016/06/24] */
    	pageOfIndex : function(index) {
    		return Math.floor(index/PAGING_SIZE)+1;
    	},
    	/* [driving] */
    	refreshDrivingTablePage : function(activepage) {
    		$tablebodyDrivingTable.html('');
    		$pagingDrivingTable.html('');

    		if (atbsvg.util.isNull(currentVehicleArray)) return ;
    		var start = (activepage-1) * PAGING_SIZE;
    		var end = start+PAGING_SIZE;

    		/* table */
    		var html = '';
    		for(var index = start; index < end && index < currentVehicleArray.length; index++) {
    			if (atbsvg.util.isFunction(onDrivingTableBodyEntryListener)) {
    				var no = currentVehicleArray.length - index;
    				html += onDrivingTableBodyEntryListener(no,currentVehicleArray[index]);
    			}
    		}
    		if(currentVehicleArray.length==0){
    			html += '<tr><td colspan="5">검색 결과가 없습니다.</td></tr>';
    		}
    		$tablebodyDrivingTable.html(html);


    		/* paging */
    		var totalPages =  Math.ceil(currentVehicleArray.length/PAGING_SIZE);
    		if (totalPages <= 1) return ;

    		var startPage = Math.ceil(activepage/PAGING_NAVI_SIZE) * PAGING_NAVI_SIZE  - (PAGING_NAVI_SIZE - 1)  ;
    		var pagehtml = '<ul class="pagination">';
    		if (startPage > PAGING_NAVI_SIZE) {
    			var prepage = startPage - PAGING_NAVI_SIZE;
    			pagehtml += '<li class="previous"><a href="javascript:'+gotoDrivingTableFuncName+'('+prepage+')">이전</a></li>';
    		}
    		var nextStartPage = startPage+PAGING_NAVI_SIZE;

    		for(var pageno = startPage; pageno < nextStartPage && pageno <= totalPages; pageno++) {
    			var classes = '';
    			if (pageno == activepage) classes = ' class="active"';
    			pagehtml += '<li'+classes+'><a href="javascript:'+gotoDrivingTableFuncName+'('+pageno+')">'+pageno+'</a></li>';
    		}

    		if (nextStartPage <= totalPages) {
    			pagehtml += '<li class="next"><a href="javascript:'+gotoDrivingTableFuncName+'('+nextStartPage+')">다음</a></li>';
    		}

    		pagehtml += "</ul>";

    		$pagingDrivingTable.html(pagehtml);
     	},
    	/* [driving] */
    	setOnDrivingTableBodyEntryListener : function(cbfunc) {
    		onDrivingTableBodyEntryListener = cbfunc;
    	},
    	/* [driving] */
    	requestViolationList : function (vehicle_uid) {
    		/* ngmsUrl in head.jsp */
    		var url = ngmsUrl+'/violation/'+vehicle_uid+'.do' ;
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;

    		doRestFulApi(url, headerMap, method, paramMap,
    			function (result) {
    				if (!atbsvg.util.isNull(result) &&
    						!atbsvg.util.isNull(result.violationHistoryList)) {

    					var vehicleEntry = vehicleMap.get(""+vehicle_uid);
    					if (!atbsvg.util.isNull(vehicleEntry)) {
    						vehicleEntry.violationHistoryList = result.violationHistoryList;
    					}

    					if (!atbsvg.util.isNull(onCompletedViolationHistoryListener)) {
    						onCompletedViolationHistoryListener(vehicle_uid,result.violationHistoryList)
    					}
    				}
    			});
    	},
    	/* [driving] */
    	setOnCompletedViolationHistoryListener : function(cbfunc) {
    		onCompletedViolationHistoryListener = cbfunc;
    	},
    	/* [driving] */
    	showRippleOnVehicle : function(vehicle_uid) {
    		var entry = vehicleMap.get(""+vehicle_uid);
    		if (atbsvg.util.isNull(entry) || atbsvg.util.isNull(entry.adbElement)) return ;

    		var center = entry.adbElement.getCenter();
    		var inverse = canvas.inversePoint(center[0], center[1]);
    		rippleWith(inverse.x,inverse.y);
    	},
    	/* [driving] */
    	setOnCompletedDrivingEventListener : function(cbfunc) {
    		onCompletedDrivingEventListener = cbfunc;
    	},
    	/* [driving] */
    	requestDrivingEvent : function(vehicle_uid) {
    		/* ngmsUrl in head.jsp */
    		var url = ngmsUrl+'/driving_event/'+vehicle_uid+'.do' ;
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;


    		doRestFulApi(url, headerMap, method, paramMap,
    			function (result) {
    				if (!atbsvg.util.isNull(result) &&
    						!atbsvg.util.isNull(result.drivingEventList)) {
    					/* */
    					clearMapOnly();

    					/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    					showFactoryElements();

    					/* */
    					lastHistoryEntry = null;
    					lastHistoryPoints = null;

    					/* */
    					var entry = vehicleMap.get(""+vehicle_uid);

    					drawVehicleDrivingEventTrace(entry,result.drivingEventList);

    					/* */
    					if (atbsvg.util.isFunction(onCompletedDrivingEventListener)) {
    						onCompletedDrivingEventListener(result.drivingEventList);
    					}
    				}
    			}) ;
    	},
    	/* [driving] */
    	setOnCompletedDrivingSectionListener : function(cbfunc) {
    		onCompletedDrivingSectionListener = cbfunc;
    	},
    	/* [driving] */
    	requestDrivingSection : function(vehicle_uid) {
    		/* ngmsUrl in head.jsp */
    		var url = ngmsUrl+'/driving_section/'+vehicle_uid+'.do' ;
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;


    		doRestFulApi(url, headerMap, method, paramMap,
    			function (result) {
    				if (!atbsvg.util.isNull(result) &&
    						!atbsvg.util.isNull(result.drivingSectionList)) {
    					/* */
    					clearMapOnly();

    					/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    					showFactoryElements();

    					lastHistoryEntry = null;
    					lastHistoryPoints = null;

    					/* */
    					var entry = vehicleMap.get(""+vehicle_uid);
    					//console.log("entry:"+JSON.stringify(entry));

    					drawVehicleDrivingSectionTrace(entry,result.drivingSectionList);

    					/* */
    					if (atbsvg.util.isFunction(onCompletedDrivingSectionListener)) {
    						onCompletedDrivingSectionListener(result.drivingSectionList);
    					}
    				}
    			}) ;
    	},
    	/* [history] */
    	redrawLastDrivingHistory : function() {
    		/* */
    		clearMapOnly();

    		/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    		showFactoryElements();

    		if (atbsvg.util.isNull(lastHistoryEntry) || atbsvg.util.isNull(lastHistoryPoints)) {
    			return ;
    		}
    		/* */
    		drawVehicleDrivingTrace(lastHistoryEntry,lastHistoryPoints);
    	},
    	/* [driving] */
    	twinkleRoadSection : function(section_uid) {
    		/* */
    	    var section = roadMap.get(""+section_uid);

    	    if (!atbsvg.util.isNull(section)) {
    	    	var centerX = (section.x1+section.x2)/2;
    	    	var centerY = (section.y1+section.y2)/2;

    	    	/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
				var repoint = LGSmartMapAttr.pointOnMapView(centerX,centerY);
				rippleWith(repoint.x,repoint.y);
    	    }
    	},
    	/* [2016/06/07] */
    	setOnStartZoomListener : function(cbfunc) {
    		canvas.setOnStartTransformListener(cbfunc);
    	},
    	/* [2016/06/07] */
    	setOnEndZoomListener : function(cbfunc) {
    		canvas.setOnEndTransformListener(cbfunc);
    	},
    	/* [2016/06/08] */
    	initViewScale : function(translate, scale) {
    		initTranslate = translate;
    		initScale = scale;

    		minScale = initScale;
    		maxScale = (initScale+1.0);

    		this.viewReset();
    	},
    	/* [2016/06/08] */
    	viewReset : function() {
    		/* */
    		canvas.zoomScale(initScale);
    		/* [주의/중요] 오브젝로 값을 넘기면 값이 어떻게 변경될 지 모른다. 따라서 ,값으로 전달 */
    		canvas.zoomTranslate([initTranslate[0],initTranslate[1]]);
    	},
    	/*
    	 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
    	 * [2016/06/13] @see lg_smartmap_for_wwms
    	 */
    	setOnCompletedFactoryCoordListener : function(cbfunc) {
    		onCompletedFactoryCoordListener = cbfunc;
    	},
    	/*
    	 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
    	 * [2016/06/13] @see lg_smartmap_for_wwms
    	 */
    	requestFactoryCoord : function() {
    		/* wwmsUrl in head.jsp */
    		var url = wwmsUrl+'/factory_coord.do' ;
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;


    		doRestFulApi(url, headerMap, method, paramMap,
    			function (result) {
    				factoryList = null;
    				mapFactoryCoord.clear();
    				/* */
    				if (!atbsvg.util.isNull(result) &&
    						!atbsvg.util.isNull(result.factoryList) &&
    						!atbsvg.util.isNull(result.factoryCoordList)) {

    					/* */
    					factoryList = result.factoryList;
    					var size = factoryList.length;
    					for(var index = 0; index < size; index++) {
    						var factory = factoryList[index];
    						factory.coord = [];
    						/* */
    						mapFactoryCoord.put(""+factory.factory_uid, factory);
    					}

    					size = result.factoryCoordList.length;
    					for(var index = 0; index < size; index++) {
    						var coord = result.factoryCoordList[index];
    						var factory = mapFactoryCoord.get(""+coord.factory_uid);
    						if (!atbsvg.util.isNull(factory)) {
    							factory.coord.push(coord);
    						}
    					}

    					size = factoryList.length;
    					for(var index = 0; index < size; index++) {
    						var factory = factoryList[index];

    						var csize = factory.coord.length;
    						if (csize > 0) {
	    						var x = 0, y = 0;
	    						for(var cidx = 0; cidx < csize; cidx++) {
	    							var coord = factory.coord[cidx];

	    							x += coord.x;
	    							y += coord.y;
	    						}

	    						factory.centerX = x / csize;
	    						factory.centerY = y / csize;
    						} else {
    							factory.centerX = 0;
	    						factory.centerY = 0;
    						}
    					}

    					/* */
    					if (atbsvg.util.isFunction(onCompletedFactoryCoordListener)) {
    						onCompletedFactoryCoordListener();
    					}
    				}
    			}) ;
    	},
    	/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    	factoryList : function(list) {
            if (!arguments.length) { return factoryList; }
            factoryList = list;
    	},
    	/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    	mapFactoryCoord : function(map) {
            if (!arguments.length) { return mapFactoryCoord; }
            mapFactoryCoord = map;
    	},
    	/* [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다. */
    	showFactoryElements : function() {
    		showFactoryElements();
    	},
    	getVehicleEntry : function(vehicleUid) {
    		return vehicleMap.get(vehicleUid);
    	},
    	getVehicleUnknownEntry : function(vehicleUid) {
    		return vehicleUnknownMap.get(vehicleUid);
    	},
    	setPageSetting : function(pageSize, pageNaviSize) {
    		if (typeof pageSize === 'number' && pageSize > 1)
    			PAGING_SIZE	= pageSize;

    		if (typeof pageNaviSize === 'number' && pageNaviSize > 1)
    			PAGING_NAVI_SIZE	= pageNaviSize;
    	},
    	setTraceDebugging : function() {
    		TRACE_HISTORY_ON = true;
    	}
    } /* end of return */
};



/* */
function makeRemoveClassHandler(regex) {
	return function (index, classes) {
		return classes.split(/\s+/).filter(function (el) {return regex.test(el);}).join(' ');
	}
}




