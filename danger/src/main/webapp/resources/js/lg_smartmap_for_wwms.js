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
var/*const*/ WSC_FACTORY		= 1;
var/*const*/ WSC_ZONE			= 2;


/* [2016/08/19] added */
var/*const*/ REQ_MODE_WORK		= 1;
var/*const*/ REQ_MODE_WORKER	= 2;



/* [2016/05/23] constants for LG Smart Plant MAP */
var LGSmartMap = function(parentId,zoomable,draggable,scalable) {
	/* common */
	var/*const*/ COLOR_TRANSPARENT   		= 'transparent';	
	
	/* font */
	var/*const*/ FONTCOLOR_DEFAULT			= 'black';
    /* 0.25em : 맵 이미지상의 폰트 크기 */
	var/*const*/ FONTSIZE_ON_UNIT			= "0.7em";
	var/*const*/ LETTER_SPACING			= "0.0";
	
	
	/* restrict area */
	var/*const*/ STROKEWIDTH_RESTRICT_BORDER = '1px';
	
	/* factory */
	var/*const*/ FACTORY_MAIN_COLOR 		= '#008be4';
	
	/* zone */
	var/*const*/ ZONE_MAIN_COLOR			= '#ff603b';
 
	/* */
	var/*const*/ RIPPLE_CIRCLE_R			= 20;
	

	/* normal case */
	var/*const*/ WSC_TEXTCOLOR_DEFAULT	 		= 'black';
	 
	var/*const*/ WSC_CIRCLE_BG_R 				= 14;
	var/*const*/ WSC_CIRCLE_R					= 13;
	var/*const*/ WSC_STROKEWIDTH_CIRCLE_OUTER 	= '4px';
	var/*const*/ WSC_STROKEWIDTH_CIRCLE		= '2.5px';
	var/*const*/ WSC_TEXTSIZE	 				= '0.8em';
	var/*const*/ WSC_TEXT_TOPMARGIN			= -3;
    
    
	/* 클릭되어 단독으로 표시될때 : normal case의 두배 */
    //const WSC_FOCUS_CIRCLE_BG_R 			= 28;
    //const WSC_FOCUS_CIRCLE_R				= 26;
    //const WSC_FOCUS_STROKEWIDTH_CIRCLE_OUTER= '6px';
    //const WSC_FOCUS_STROKEWIDTH_CIRCLE	= '4.5px';
    //const WSC_FOCUS_TEXTSIZE	 			= '1.6em';	/* 텍스트 크기 */
    //const WSC_FOCUS_TEXT_TOPMARGIN		= -5;		/* 텍스트의 센터의 위치 조정 */
    
    /* [2016/09/08] 2배로 확대 */
	var/*const*/ WSC_FOCUS_CIRCLE_BG_R 			= 56;
	var/*const*/ WSC_FOCUS_CIRCLE_R				= 54;
	var/*const*/ WSC_FOCUS_STROKEWIDTH_CIRCLE_OUTER= '4px';
	var/*const*/ WSC_FOCUS_STROKEWIDTH_CIRCLE		= '4px';
	var/*const*/ WSC_FOCUS_TEXTSIZE	 			= '3.2em';	/* 텍스트 크기 */
	var/*const*/ WSC_FOCUS_TEXT_TOPMARGIN			= -10;		/* 텍스트의 센터의 위치 조정 */
    
	var/*const*/ WSC_FOCUS_TITLE_TEXTSIZE			= '1.5em';	/* 공장명/존명 */
	var/*const*/ WSC_FOCUS_TITLE_TOPMARGIN			= 25;		/* 공장명/존명의 센터의 위치 조정 */
    
    /* */
	var/*const*/ UNKNOWN_UID				= -1;
    var/*const*/ UNKNOWN_MAIN_COLOR		= FACTORY_MAIN_COLOR;
    var/*const*/ UNKNOWN_CIRCLE_X			= 2900;
    var/*const*/ UNKNOWN_CIRCLE_Y			= 1400;
	
    
	/* [common] */
	var onCompletedFactoryCoordListener;
	var onCompletedZoneCoordListener;
	var onUpdatedFactoryRestrictListener;
	var onUpdatedZoneRestrictListener;
	
	/* [work] */
	var onCompletedCodeFactoryZoneListener;
	var onCompletedCodeWorkTypeListener;
	/* [work] */
	var onCompletedWorkStatusListener;
	var onClickedWorkStatusCircleListener;

	/* */
	var onHideTooltipListner;
	var onShowTooltipListner;
	
	/* [common] */
	var mapFactoryCoord = new atbsvg.map();
	var mapZoneCoord = new atbsvg.map();
	
	var factoryList = null;
	var zoneList = null;
	
	/* [work] */
	var mapStatusElement = new atbsvg.map();
	var mapStatusData = new atbsvg.map();
	
	var statusShowType = null;
	var factoryStatusList = null;
	var zoneStatusList = null;
	var worktypeStatusList = null;
	
	/* work */
	/* [2016/05/26] */
	var showOnlyFactoryZoneBoundary = false;
	var showOnlyRestrictedFactoryZone = false;
	
	/* */
	var initScale = 1.0;
	var initTranslate = [0, 0];
	
	/* */
	var minScale = initScale;
	var maxScale = (initScale+1);
	
	/* [2016/08/19] added */
	var reqStatusMapMode = REQ_MODE_WORK;
	
	/* [common] */
	var width = LGSmartMapAttr.width(),
		height= LGSmartMapAttr.height(),
		backgroundPath = LGSmartMapAttr.backgroundPath(),
		canvasWidth = LGSmartMapAttr.canvasWidth(),
		canvasHeight = LGSmartMapAttr.canvasHeight();
	
	/* [common] */
	var canvas;
		

	
	/* */	
	if (!arguments.length) { 
		parentId = "body";
	}
	
	/* */
    canvas = atbsvg.canvas()
    		.width(canvasWidth)
    		.height(canvasHeight)
    		.zoomEnabled(zoomable)
    		.dragEnabled(draggable)
    		.mouseZoomEnabled(false)
    		.blockedDragAway(true);
    
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
		canvas.removeChildAll("g");	
		/* */
		vehicleMap.clear();
		
		if (showableRoadMap) {
			roadMap.clear();
		}
	}
    
    /* */
    function clearMapOnly() {
    	canvas.removeChildAll("g");	
    	/* [2016/06/22] added for ripple */
    	canvas.removeChildAll("circle");
    }
       
    /* [common] */
    function showAlert(msg) {
		alert(msg);
	}
    
    
    /* [common] */
    function ripple(group) {
    	var center = group.getCenter();
    	atbsvg.animation.ripple(canvas, {cx: center[0], cy: center[1], duration:2000, radius:RIPPLE_CIRCLE_R,'stroke-width':'5px'});
    	atbsvg.animation.ripple(canvas, {cx: center[0], cy: center[1], duration:2000, radius:RIPPLE_CIRCLE_R,'stroke-width':'5px',delay:300});
    	atbsvg.animation.ripple(canvas, {cx: center[0], cy: center[1], duration:2000, radius:RIPPLE_CIRCLE_R,'stroke-width':'5px',delay:600});
    	atbsvg.animation.ripple(canvas, {cx: center[0], cy: center[1], duration:2000, radius:RIPPLE_CIRCLE_R,'stroke-width':'5px',delay:900});
    }
    
    
    /* ----------------------------------------------- */
    /* factory */
    /* ----------------------------------------------- */
 
    /* restrict */
    /* [2016/06/13] @see lg_smartmap_for_ngms */
    function showFactoryElement(factory) {
    	if (atbsvg.util.isNull(factory)) return ;
    	/* [2016/06/27]
		 * 비콘존하고 겹치는 공장은 제거하기 위해 is_factory 항목이용 
		 */
		if (factory.is_factory != 1) return ;
		
    	/* group */
        var group = atbsvg.primitive.newGroup().id(""+factory.factry_uid);
        canvas.appendElement(group);
        
        var points = '';
        for(var index = 0; index < factory.coord.length; index++) {
        	var coord = factory.coord[index];
        	/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
        	var point = LGSmartMapAttr.pointOnMapView(coord.x, coord.y); 	
        	
        	points += ' '+point.x+','+point.y;
        }
        
        /* [2016/06/27] */
        var stokeColor = FACTORY_MAIN_COLOR;
		if (showOnlyRestrictedFactoryZone && factory.is_restricted == 0) {
			stokeColor = COLOR_TRANSPARENT;
		}
		/* end of [2016/06/27] */
		
        var polygon = atbsvg.primitive.newPolygon().points(points)
        		.strokeWidth(STROKEWIDTH_RESTRICT_BORDER)
        		.stroke(stokeColor);
              
        if (factory.is_restricted == 1 && showOnlyFactoryZoneBoundary == false) {		
        	polygon.fill(FACTORY_MAIN_COLOR).fillOpacity(0.5);
        } else {
        	/* [2016/08/20] modified */
        	if (showOnlyRestrictedFactoryZone && factory.is_restricted == 1) {
        		polygon.fill(FACTORY_MAIN_COLOR).fillOpacity(0.5);
        	} else {
        		polygon.fill(COLOR_TRANSPARENT);
        	}
        }
        
        canvas.innerElement(group.d3element, polygon);
        
        var point = LGSmartMapAttr.pointOnMapView(factory.centerX, factory.centerY);
        
        /*
         * [2016/06/13] 비콘존의 이름은 빼는 것으로
         * [2016/08/22] 다시 보여달라네 ㅠ.
         */
        /*
        if (!showOnlyFactoryZoneBoundary) {
        */
	        /* .opacity('0.7') */
	        var text = atbsvg.primitive.newText()
				.x(point.x).y(point.y)
				.stroke(FONTCOLOR_DEFAULT)
				.strokeWidth('0.3px')
				.fontSize(FONTSIZE_ON_UNIT)		
				.letterSpacing(LETTER_SPACING)			
				.fill(FONTCOLOR_DEFAULT)
				.textAnchor('middle');
			text.text = ""+factory.name;
		   
			canvas.innerElement(group.d3element, text);
		/*	
         }
        */
		
		 /* */
        canvas.transformedElement(group.d3element); 
        
        /* [2016/05/26] */
        if (showOnlyFactoryZoneBoundary) {
        	group.opacity('0.7');
        }
        
        /* */
        factory.adbElement = group;
    }
    
    /* */
    function redrawFactoryElement(factory) {
    	if (atbsvg.util.isNull(factory)) return ;
    	
    	if (atbsvg.util.isNull(factory.adbElement)) {
    		factory.addElement.remove();
    		factory.addElement = null;
    	}
    	
    	showFactoryElement(factory);	
    }

    
    /* restrict */
    /* [2016/06/13] @see lg_smartmap_for_ngms */
    function showFactoryElements() {
    	if (atbsvg.util.isNull(factoryList)) return ;
    	
    	var size = factoryList.length;
    	for(var index = 0; index < size; index++) {
    		/*
    		 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
    		 */
    		/*
    		if (showOnlyRestrictedFactoryZone && factoryList[index].is_restricted == 0) {
    			continue;
    		}
    		*/  		
    		/* [2016/06/27]
    		 * 비콘존하고 겹치는 공장은 제거하기 위해 is_factory 항목이용 
    		 */
    		if (factoryList[index].is_factory == 1) {
    			showFactoryElement(factoryList[index]);
    		}
    	}
    }
    
    /* ----------------------------------------------- */
    /* zone */
    /* ----------------------------------------------- */

    /* restrict */
    /* [2016/06/13] @see lg_smartmap_for_ngms */
    function showZoneElement(zone) {
    	if (atbsvg.util.isNull(zone)) return ;
    	
    	/* group */
        var group = atbsvg.primitive.newGroup().id(""+zone.zone_uid);
        canvas.appendElement(group);
        
        
        var points = '';
        for(var index = 0; index < zone.coord.length; index++) {
        	var coord = zone.coord[index];
        	/* [2016/05/23] (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환 */
        	var point = LGSmartMapAttr.pointOnMapView(coord.x, coord.y); 	
        	
        	points += ' '+point.x+','+point.y;
        }
        
        
        /* [2016/06/27] */
        var stokeColor = ZONE_MAIN_COLOR;
		if (showOnlyRestrictedFactoryZone && zone.is_restricted == 0) {
			stokeColor = COLOR_TRANSPARENT;
		}
		/* end of [2016/06/27] */		
		
        var polygon = atbsvg.primitive.newPolygon().points(points)
			.strokeWidth(STROKEWIDTH_RESTRICT_BORDER)
			.stroke(stokeColor);

        if (zone.is_restricted == 1 && showOnlyFactoryZoneBoundary == false) {
			polygon.fill(ZONE_MAIN_COLOR).fillOpacity(0.5);
		} else {
        	/* [2016/08/20] modified */
        	if (showOnlyRestrictedFactoryZone && zone.is_restricted == 1) {
        		polygon.fill(ZONE_MAIN_COLOR).fillOpacity(0.5);
        	} else {
        		polygon.fill(COLOR_TRANSPARENT);
        	}
		}
		canvas.innerElement(group.d3element, polygon);

		var point = LGSmartMapAttr.pointOnMapView(zone.centerX, zone.centerY);
        
        /*
         * [2016/06/13] 비콘존의 이름은 빼는 것으로
         * [2016/08/22] 다시 보여달라네 ㅠ.
         */
		/*
		 * if (!showOnlyFactoryZoneBoundary) {
		 */
	        var text = atbsvg.primitive.newText()
				.x(point.x).y(point.y)
				.stroke(FONTCOLOR_DEFAULT)
				.strokeWidth('0.3px')
				.fontSize(FONTSIZE_ON_UNIT)		
				.letterSpacing(LETTER_SPACING)			
				.fill(FONTCOLOR_DEFAULT)
				.textAnchor('middle');
			text.text = ""+zone.name;
		   
			canvas.innerElement(group.d3element, text);
		/*	
		 }
		 */
		
        /* */
        canvas.transformedElement(group.d3element); 
        
          
        /* [2016/05/26] */
        if (showOnlyFactoryZoneBoundary) {
        	group.opacity('0.7');
        }
        
        /* */
        zone.adbElement = group;
    }
    
    
    /* */
    function redrawZoneElement(zone) {
    	if (atbsvg.util.isNull(zone)) return ;
    	
    	if (atbsvg.util.isNull(zone.adbElement)) {
    		zone.addElement.remove();
        	zone.addElement = null;
    	}
    	
    	zone.addElement.remove();
    	zone.addElement = null;
    	showZoneElement(zone);	
    }   
    
    
    /* restrict */
    /* [2016/06/13] @see lg_smartmap_for_ngms */  
    function showZoneElements() {
    	if (atbsvg.util.isNull(zoneList)) return ;
    	
    	var size = zoneList.length;
    	for(var index = 0; index < size; index++) {
    		/*
    		 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
    		 */
    		/*
    		if (showOnlyRestrictedFactoryZone && zoneList[index].is_restricted == 0) {
    			continue;
    		}
    		*/   		
    		showZoneElement(zoneList[index]);
    	}
    }
    
    /* [2016/08/20] added */
    function relocationTooltipPosition(x, y) {
		return [x-100,y-20];
	}
    
    
    /* ----------------------------------------------- */
    /* work status */
    /* ----------------------------------------------- */
    function makeStatusKey(type, id) {
    	return  ""+type+"_"+id;
    }
    
    
    /*[2016/09/01 requested by LG : 작업대쉬보드, 현재진행+완료 합 --> 현재 진행 작업/작업자수의 합 변경요청] */
    function updateTooltipOptions(type, dataOptions) {
    	/* type : reserved */
    	dataOptions.type = type;
    	
    	/* */
    	if (reqStatusMapMode == REQ_MODE_WORKER) {
    		dataOptions.left_title  = '총인원수';
    		dataOptions.right_title = '비인가자수';
    		
    		dataOptions.left_value  = ''+(dataOptions.people_authorized_cnt+dataOptions.people_unauthorized_cnt);
    		dataOptions.right_value = ''+dataOptions.people_unauthorized_cnt;
    	} else {
    		dataOptions.left_title  = '작업수';
    		dataOptions.right_title = '작업인원수';
/*    		
    		dataOptions.left_value  = ''+(dataOptions.work_completed_cnt+dataOptions.work_working_cnt);
    		dataOptions.right_value = ''+(dataOptions.people_completed_cnt+dataOptions.people_working_cnt);		
 */
    		dataOptions.left_value  = ''+(dataOptions.work_working_cnt);
    		dataOptions.right_value = ''+(dataOptions.people_working_cnt);
    	}   	
    }
    
    /* */
    var lastTooltipInfo = {
    		'key' : null,
    		'x' : 0,
    		'y' : 0
    };
    
    /* internal */
    function drawStatusCircle(type,uid,centerX,centerY,isFocused,titleName) {
    	/* group */
    	var key = makeStatusKey(type,uid);     
        var options = mapStatusData.get(key);
        if (atbsvg.util.isNullEmpty(options)) return;
        
    	
    	/* */
        var group = atbsvg.primitive.newGroup().id(key);
        canvas.appendElement(group);

        /* */
        updateTooltipOptions(type, options);

        var leftValue  = options.left_value;
        var rightValue = options.right_value;
        
        /* */
        var length = rightValue.length;
        if (leftValue.length > rightValue.length) {
        	length = leftValue.length;
        }
        
        /* */
        var circleOuterR = WSC_CIRCLE_BG_R;
        var curcleR = WSC_CIRCLE_R;
        var stwCircleOuter = WSC_STROKEWIDTH_CIRCLE_OUTER;
        var stwCircle = WSC_STROKEWIDTH_CIRCLE;
        var txtSize = WSC_TEXTSIZE;
        var topMargin = WSC_TEXT_TOPMARGIN;
        
        /* focus가 된 경우 */
        if (isFocused == true) {
            circleOuterR = WSC_FOCUS_CIRCLE_BG_R;
            curcleR = WSC_FOCUS_CIRCLE_R;
            stwCircleOuter = WSC_FOCUS_STROKEWIDTH_CIRCLE_OUTER;
            stwCircle = WSC_FOCUS_STROKEWIDTH_CIRCLE;
            txtSize = WSC_FOCUS_TEXTSIZE;  
            topMargin = WSC_FOCUS_TEXT_TOPMARGIN;
        }        
        
        if (length >= 2) {
        	var weight = 1 + (length*2.5)/10;
        	
        	circleOuterR *= weight;
        	curcleR *= weight;
        }
        
        /* */
        var mainColor = '';
        switch (type) {
        case WSC_FACTORY:
        	/* [2016/08/22] added */
        	if (uid >= 0) {
        		mainColor = FACTORY_MAIN_COLOR;
        	} else {
        		mainColor = UNKNOWN_MAIN_COLOR;
        	}
        	break;
        case WSC_ZONE:
        	mainColor = ZONE_MAIN_COLOR;
        	break;
        default:
        	return ;
        }
        
        var point = LGSmartMapAttr.pointOnMapView(centerX, centerY);       
        var circleOuter = atbsvg.primitive.newCircle()
	    	.cx(point.x).cy(point.y)
	    	.r(circleOuterR)
	    	.fill(mainColor)
	    	.stroke(mainColor)
	    	.strokeWidth(stwCircleOuter)
	    	.opacity('0.5');
        canvas.innerElement(group.d3element, circleOuter);
 
        /* */
        var circle = atbsvg.primitive.newCircle()
		    .cx(point.x).cy(point.y)
		    .r(curcleR)
		    .fill('white')
	    	.stroke(mainColor)
	    	.strokeWidth(stwCircle)
		    .opacity('0.8'); 
        
	    canvas.innerElement(group.d3element, circle);
	    circle.moveToFront();
	    
	    /* [2016/09/08] 공장이름을 표시하자 : focus가 된 경우 */
        if (isFocused == true) {
        	console.log("title ok:"+titleName);
        	
        	var txtTitle = atbsvg.primitive.newText()
				.x(point.x).y(point.y-WSC_FOCUS_TITLE_TOPMARGIN)
				.stroke(WSC_TEXTCOLOR_DEFAULT)
				.strokeWidth('0.3px')
				.fill(WSC_TEXTCOLOR_DEFAULT)
				.fontSize(WSC_FOCUS_TITLE_TEXTSIZE)
				.letterSpacing(LETTER_SPACING)
				.textAnchor('middle')
				.opacity('0.7');
        	
        	txtTitle.text = titleName;
        	canvas.innerElement(group.d3element, txtTitle);
        }
	    
	    
	    var txtCenter = atbsvg.primitive.newText()
			.x(point.x).y(point.y-topMargin)
			.stroke(WSC_TEXTCOLOR_DEFAULT)
			.strokeWidth('0.3px')
			.fill(WSC_TEXTCOLOR_DEFAULT)
			.fontSize(txtSize)
			.letterSpacing(LETTER_SPACING)
			.textAnchor('middle');
	    txtCenter.text = "/";
	    canvas.innerElement(group.d3element, txtCenter);
	    
	    var txtLeft = atbsvg.primitive.newText()
			.x(point.x-5).y(point.y-topMargin)
			.stroke(WSC_TEXTCOLOR_DEFAULT)
			.strokeWidth('0.3px')
			.fill(WSC_TEXTCOLOR_DEFAULT)
			.fontSize(txtSize)
			.letterSpacing(LETTER_SPACING)
			.textAnchor('end');
	    txtLeft.text = ''+leftValue;
	    canvas.innerElement(group.d3element, txtLeft);
        
	    var txtRight = atbsvg.primitive.newText()
			.x(point.x+5).y(point.y-topMargin)
			.stroke(mainColor)
			.strokeWidth('0.3px')
			.fill(mainColor)
			.fontSize(txtSize)
			.letterSpacing(LETTER_SPACING)
			.textAnchor('start');
	    txtRight.text = ''+rightValue;
	    canvas.innerElement(group.d3element, txtRight);
        
        /* */
        canvas.transformedElement(group.d3element); 
        
        
        /* [2016/08/28] added */
        if (!atbsvg.util.isNullEmpty(lastTooltipInfo.key) &&
        		lastTooltipInfo.key == key) {
        	onHideTooltipListner();
        	onShowTooltipListner(options, lastTooltipInfo.x, lastTooltipInfo.y);
        }
    	
        
        /* [2016/08/22] modified */
        group.onClick(function() {
			if (atbsvg.util.isFunction(onHideTooltipListner)) {
				onHideTooltipListner();
			}
       	 
			/* */
        	if (atbsvg.util.isFunction(onClickedWorkStatusCircleListener)) {
        		var stype = 'factory';
        		if (type == WSC_ZONE) 
        			stype = 'zone';
        		
        		onClickedWorkStatusCircleListener(stype, uid);
        	}
        });
        
        /* */
        group.on("mouseover", function(){ 
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		var coordinates = d3.mouse(this);     		
        		/* [2016/06/07] no-scale */
        		/*
        		 * var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		 * var rcoord = relocationTooltipPosition(point.x,point.y);
        		 */        		
        		/*
        		 * [2016/08/20] scale
        		 * var rcoord = relocationTooltipPosition(coordinates[0],coordinates[1]);
        		 */
        	
        		var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		var rcoord = relocationTooltipPosition(point.x,point.y);
        		
        		/* */
        		onShowTooltipListner(options, rcoord[0], rcoord[1]);
        		
        		/* [2016/08/29] 값을 계속 갱신 */
        	    lastTooltipInfo.key = key;
        	    lastTooltipInfo.x = rcoord[0];
        	    lastTooltipInfo.y = rcoord[1];
        	}
        }).on("mousemove", function() {
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		var coordinates = d3.mouse(this);   
        		/* [2016/06/07] no-scale */
        		/*
        		 * var point = canvas.transformedPoint(coordinates[0], coordinates[1]);  
        		 * var rcoord = relocationTooltipPosition(point.x,point.y); 
        		 */
        		/*
        		 * [2016/08/20] scale
        		 * var rcoord = relocationTooltipPosition(coordinates[0],coordinates[1]);
        		 */
        		
        		var point = canvas.transformedPoint(coordinates[0], coordinates[1]);
        		var rcoord = relocationTooltipPosition(point.x,point.y);
        		
        		/* */
        		onShowTooltipListner(options, rcoord[0], rcoord[1]);
        		
        		/* [2016/08/29] 값을 계속 갱신 */
        	    lastTooltipInfo.key = key;
        	    lastTooltipInfo.x = rcoord[0];
        	    lastTooltipInfo.y = rcoord[1];
        	}
        }).on("mouseout", function(){ 
        	/* [2016/08/28] added */
        	lastTooltipInfo.key = null;
        	
        	if (atbsvg.util.isFunction(onHideTooltipListner)) {
        		onHideTooltipListner();
        	}        	
        });
               
        /* */
        mapStatusElement.put(key,group);
    }
    
    
    
    /*
     * [2016/08/20] 화면에 표시할지 여부 (값이 없으면 표시하지 않는다)
     */
    function isShowableData(type, data) {
    	if (reqStatusMapMode == REQ_MODE_WORKER) {
    		if (data.people_authorized_cnt == 0 && data.people_unauthorized_cnt == 0) {
        		return false;
        	}
    	} else {
    		if (data.work_completed_cnt == 0 && data.work_working_cnt == 0 &&
    				data.people_completed_cnt == 0 && data.people_working_cnt == 0) {
    			return false;
        	}	
    	}   
    	
    	return true;
    }
    
    /* */
    function showAllWorkStatus() {
    	/* */
    	showFactoryElements();
    	showZoneElements();
		
    	if (!atbsvg.util.isNull(factoryStatusList)) {
	    	var size = factoryStatusList.length;
	    	
	    	for(var index = 0; index < size; index++) {
	    		var factory = factoryStatusList[index];
	    	  	var fcoord = mapFactoryCoord.get(""+factory.factory_uid);
	    	  	
	        	if (atbsvg.util.isNull(fcoord)) {	        		
	        		/* [2016/08/22] added */
	        		continue;
	        	}
	 
	        	/* 둘다 0인 경우에는 표시하지 않는다. */
	            /*
	             * [2016/08/22] 다시 보여달라네 ㅠ.
	             */
	        	/*
	        	if (isShowableData(WSC_FACTORY,factory) != true) {
	        		continue;
	        	}
	        	*/
	        	
	        	
	        	/* [2016/08/20] modified */
	        	var key = makeStatusKey(WSC_FACTORY,factory.factory_uid);
	        	mapStatusData.put(key,factory);

	        	/* */
	        	drawStatusCircle(WSC_FACTORY,factory.factory_uid,fcoord.centerX,fcoord.centerY,false,factory.factory_name);
	    	}
    	}
    	
    	if (!atbsvg.util.isNull(zoneStatusList)) {
    		var size = zoneStatusList.length;
	    	
	    	for(var index = 0; index < size; index++) {
	    		var zone = zoneStatusList[index];
	    	  	var zcoord = mapZoneCoord.get(""+zone.zone_uid);
	        	if (atbsvg.util.isNull(zcoord)) continue;
	 
	        	/* 둘다 0인 경우에는 표시하지 않는다. */
	            /*
	             * [2016/08/22] 다시 보여달라네 ㅠ.
	             */
	        	/*
	        	if (isShowableData(WSC_ZONE,zone) != true) {
	        		continue;
	        	}
	        	*/
	        	
	        	/* [2016/08/20] modified */
	        	var key = makeStatusKey(WSC_ZONE,zone.zone_uid);
	        	mapStatusData.put(key,zone);

	        	/* */
	        	drawStatusCircle(WSC_ZONE,zone.zone_uid,zcoord.centerX,zcoord.centerY,false,zone.zone_name);
	    	}
    	}
    }
    
    function showFactoryWorkStatus() {
    	/*
    	 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
    	 */
    	showFactoryElements();
    	showZoneElements();
    	
    	/* */
    	if (!atbsvg.util.isNull(factoryStatusList) && factoryStatusList.length > 0) {
    		var factory = factoryStatusList[0];
    		if (factory.factory_uid < 0) {
    			if (factoryStatusList.length > 1) {
    				factory = factoryStatusList[1];
    			} else {
    				return ;
    			}
    		} 
 
    		/* */
    		var entry = mapFactoryCoord.get(""+factory.factory_uid);
    		if (!atbsvg.util.isNull(entry)) {
    			showFactoryElement(entry);
    		}
    		
        	/* [2016/08/20] modified */
        	var key = makeStatusKey(WSC_FACTORY,factory.factory_uid);
        	mapStatusData.put(key,factory);

        	/* */
        	drawStatusCircle(WSC_FACTORY,factory.factory_uid,entry.centerX,entry.centerY,true,factory.factory_name);
    	}
    }
    
    function showZoneWorkStatus() {
    	/*
    	 * [2016/06/13] requested by LG : 모든 단위공장과 비콘존은 표시한다.
    	 */
    	showFactoryElements();
    	showZoneElements();
    	
    	
    	/* */
    	if (!atbsvg.util.isNull(zoneStatusList) && zoneStatusList.length > 0) {
    		var zone = zoneStatusList[0];
 
    		/* */
    		var entry = mapZoneCoord.get(""+zone.zone_uid);
    		if (!atbsvg.util.isNull(entry)) {
    			showZoneElement(entry);
    		}
    		
        	/* [2016/08/20] modified */
        	var key = makeStatusKey(WSC_ZONE,zone.zone_uid);
        	mapStatusData.put(key,zone);

        	/* */
        	drawStatusCircle(WSC_ZONE,zone.zone_uid,entry.centerX,entry.centerY,true,zone.zone_name);
    	}
    	
    }
    
    
    /* */
    function redrawFactoryElementWithId(factory_uid, is_restricted) {
    	var factory = mapFactoryCoord.get(factory_uid);
    	if (atbsvg.util.isNull(factory)) return ;
    	/* */
    	factory.is_restricted = is_restricted;	
    	
    	/* */
    	clearMapOnly();
		showFactoryElements();
    }
    
    
    /* */
    function redrawZoneElementWithId(zone_uid,is_restricted) {
    	var zone = mapZoneCoord.get(zone_uid);
    	if (atbsvg.util.isNull(zone)) return ;

    	zone.is_restricted = is_restricted;
    	/* */
    	clearMapOnly();
		showZoneElements();
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
    	showAlert : function(msg) {
    		showAlert(msg);
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
    	/* [common] */
    	/*
    	 * [2016/06/13] @see lg_smartmap_for_ngms  
    	 */
    	setOnCompletedFactoryCoordListener : function(cbfunc) {
    		onCompletedFactoryCoordListener = cbfunc;
    	},
    	/* [common] */
    	/*
    	 * [2016/06/13] @see lg_smartmap_for_ngms  
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
    				
    				/* [2016/08/22] */
    				mapFactoryCoord.put(""+UNKNOWN_UID,
    						{
    							'factory_uid': UNKNOWN_UID,
    							'factory_name': '위치미확인',
    							'centerX' : UNKNOWN_CIRCLE_X,
    		        			'centerY' : UNKNOWN_CIRCLE_Y
    						});
        		  	
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
    	/* [common] */
    	showFactoryCoord : function() {
    		clearMapOnly();
    		showFactoryElements();
    	},
    	/* [common] */
    	currentFactoryList : function() {
    		return factoryList;
    	},
    	/* [common] */
    	/*
    	 * [2016/06/13] @see lg_smartmap_for_ngms  
    	 */
    	setOnCompletedZoneCoordListener : function(cbfunc) {
    		onCompletedZoneCoordListener = cbfunc;
    	},
    	/* [common] */
    	/*
    	 * [2016/06/13] @see lg_smartmap_for_ngms  
    	 */
    	requestZoneCoord : function() {
    		/* wwmsUrl in head.jsp */
    		var url = wwmsUrl+'/zone_coord.do' ; 
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;	
    		
    		
    		doRestFulApi(url, headerMap, method, paramMap, 
    			function (result) {
    				zoneList = null;
    				mapZoneCoord.clear();
    				/* */
    				if (!atbsvg.util.isNull(result) &&
    						!atbsvg.util.isNull(result.zoneList) &&
    						!atbsvg.util.isNull(result.zoneCoordList)) {
    					/* */
    					zoneList = result.zoneList;
    					var size = zoneList.length;
    					for(var index = 0; index < size; index++) {
    						var zone = zoneList[index];
    						zone.coord = [];
    						/* */
    						mapZoneCoord.put(""+zone.zone_uid, zone);
    					}
    					
    					size = result.zoneCoordList.length;
    					for(var index = 0; index < size; index++) {
    						var coord = result.zoneCoordList[index];					
    						var zone = mapZoneCoord.get(""+coord.zone_uid);
    						if (!atbsvg.util.isNull(zone)) {
    							zone.coord.push(coord);
    						}
    					}
    					
    					size = zoneList.length;
    					for(var index = 0; index < size; index++) {
    						var zone = zoneList[index];
    						
    						var csize = zone.coord.length;
    						if (csize > 0) {
	    						var x = 0, y = 0;
	    						for(var cidx = 0; cidx < csize; cidx++) {
	    							var coord = zone.coord[cidx];
	    							
	    							x += coord.x;
	    							y += coord.y;
	    						}
	    						
	    						zone.centerX = x / csize;
	    						zone.centerY = y / csize;
    						} else {
    							zone.centerX = 0;
    							zone.centerY = 0;
    						}
    					}
    					   					
    					/* */    					
    					if (atbsvg.util.isFunction(onCompletedZoneCoordListener)) {
    						onCompletedZoneCoordListener();
    					}
    				}
    			}) ;
    	},
    	/* [common] */
    	showZoneCoord : function() {
    		clearMapOnly();
    		showZoneElements();
    	},
    	/* [common] */
    	currentZoneList : function() {
    		return zoneList;
    	}, 
    	/* [for work status] */
    	showOnlyFactoryZoneBoundary : function(enabled) {
    		if (!arguments.length) { return showOnlyFactoryZoneBoundary; }
    		showOnlyFactoryZoneBoundary = enabled;
    	},
    	showOnlyRestrictedFactoryZone: function(enabled) {
    		if (!arguments.length) { return showOnlyRestrictedFactoryZone; }
    		showOnlyRestrictedFactoryZone = enabled;
    	},
    	/* [restrict] */
    	setOnUpdatedFactoryRestrictListener : function(cbfunc) {
    		onUpdatedFactoryRestrictListener = cbfunc;
    	},  	
    	/* [restrict] */
    	requestFactoryRestrict : function(factory_uids) {
    		/* wwmsUrl in head.jsp */
    		var url = wwmsUrl+'/factory_restrict.do' ; 
    		var method = 'POST';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = {'factory_uids':factory_uids};	
    		
    		
    		doRestFulApi(url, headerMap, method, paramMap, 
    			function (result) {
					if (atbsvg.util.isFunction(onUpdatedFactoryRestrictListener)) {
						onUpdatedFactoryRestrictListener();
					}
    			}) ;
    	},  	
    	/* [restrict] */
    	setOnUpdatedZoneRestrictListener : function(cbfunc) {
    		onUpdatedZoneRestrictListener = cbfunc;
    	},
    	/* [restrict] */
    	requestZoneRestrict : function(zone_uids) {
    		/* wwmsUrl in head.jsp */
    		var url = wwmsUrl+'/zone_restrict.do' ; 
    		var method = 'POST';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = {'zone_uids':zone_uids};	
    		
    		
    		doRestFulApi(url, headerMap, method, paramMap, 
    			function (result) {    				
					if (atbsvg.util.isFunction(onUpdatedZoneRestrictListener)) {
						onUpdatedZoneRestrictListener();
					}
    			}) ;
    	},  	
    	/* [work] */
    	setOnCompletedCodeFactoryZoneListener : function(cbfunc) {
    		onCompletedCodeFactoryZoneListener = cbfunc;
    	},
    	/* [work] */
    	requestCodeFactoryZone : function() {
    		/* wwmsUrl in head.jsp */
    		var url = wwmsUrl+'/code_factoryzone/all.do' ; 
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;
    		
    		
    		doRestFulApi(url, headerMap, method, paramMap, 
    			function (result) {
	    			if (!atbsvg.util.isNull(result) &&
							!atbsvg.util.isNull(result.codeFactoryZoneList)) {		
						if (atbsvg.util.isFunction(onCompletedCodeFactoryZoneListener)) {
							onCompletedCodeFactoryZoneListener(result.codeFactoryZoneList);
						}
	    			}
    			}) ;
    	},
    	/* [work] */
    	setOnCompletedCodeWorkTypeListener : function(cbfunc) {
    		onCompletedCodeWorkTypeListener = cbfunc;
    	},
    	/* [work] */
    	requestCodeWorkType : function() {
    		/* wwmsUrl in head.jsp */
    		var url = wwmsUrl+'/code_worktype.do' ; 
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;	
    		
    		
    		doRestFulApi(url, headerMap, method, paramMap, 
    			function (result) {
    				if (!atbsvg.util.isNull(result) &&
						!atbsvg.util.isNull(result.codeWorkTypeList)) {				
						if (atbsvg.util.isFunction(onCompletedCodeWorkTypeListener)) {
							onCompletedCodeWorkTypeListener(result.codeWorkTypeList);
						}
    				}
    			}) ;
    	},
    	/* [work] */
    	setOnCompletedWorkStatusListener : function(cbfunc) {
    		onCompletedWorkStatusListener = cbfunc;
    	},
    	/* [work] */
    	requestWorkStatusList : function(type, uid, work_type) {
    		var param = 'type='+type;
    		if (!atbsvg.util.isNull(uid) && uid.length > 0) {
    			param += '&uid='+uid;
    		}
    		if (!atbsvg.util.isNull(work_type) && work_type.length > 0) {
    			param += '&work_type='+work_type;
    		}
    		
    		/* wwmsUrl in head.jsp */
    		var url = '';
    		if (reqStatusMapMode == REQ_MODE_WORKER) {
    			url = wwmsUrl+'/map_worker_status.do?'+param ;
    		} else {
    			url = wwmsUrl+'/map_work_status.do?'+param ; 
    		}
    		
    		var method = 'GET';
    		var headerMap = new requestHeaderObject(g_accessToken, g_requestType);
    		var paramMap = null;	
    		
    		/* [2016/06/02] */
    		try { loading_process(); } catch(e) {}
    		
    		doRestFulApi(url, headerMap, method, paramMap, 
    			function (result) {
    				/* [2016/06/02] */
        			try { loading_process('hide'); } catch(e) {}
        		
	    			factoryStatusList = null;
	    			zoneStatusList = null;
	    			worktypeStatusList = null;
	    			statusShowType = null;
    			
    				if (!atbsvg.util.isNull(result)) {	
    					if (!atbsvg.util.isNull(result.show_type)) {
    						statusShowType = result.show_type;
    					}
    					 					
    					if (!atbsvg.util.isNull(result.factoryStatusList)) {
    						factoryStatusList = result.factoryStatusList;
    					}
    					
    					if (!atbsvg.util.isNull(result.zoneStatusList)) {
    						zoneStatusList = result.zoneStatusList;
    					}
    					
    					if (!atbsvg.util.isNull(result.worktypeStatusList)) {
    						worktypeStatusList = result.worktypeStatusList;
    					}
    					
						if (atbsvg.util.isFunction(onCompletedWorkStatusListener)) {
							onCompletedWorkStatusListener();
						}
    				}
    			},
    			function () {
    				/* [2016/06/02] */
    	    		try { loading_process('hide'); } catch(e) {}
    			}) ;
    	},
    	/* [work] */
    	currentFactoryStatusList : function() {
    		return factoryStatusList;
    	},
    	/* [work] */
    	currentZoneStatusList : function() {
    		return zoneStatusList;
    	},
    	/* [work] */
    	currentWorkTypeStatusList : function() {
    		return worktypeStatusList;
    	},
    	/* [work] */
    	currentStatusShowType : function() {
    		return statusShowType;
    	},
    	/* [work] */
    	redrawWorkStatusMap : function() {
    		clearMapOnly();
    		mapStatusElement.clear();
    		mapStatusData.clear();
    		
    		if (statusShowType == 'factory') {
    			showFactoryWorkStatus();
    		} else if (statusShowType == 'zone') {
    			showZoneWorkStatus();
    		} else {
    			showAllWorkStatus();
    		}
    	},
    	/* [work] */
    	setOnClickedWorkStatusCircleListener : function(cbfunc) {
    		onClickedWorkStatusCircleListener = cbfunc;
    	},
    	/* [work] */
    	toFrontWorkStatusCircle : function(type,id) {
            /* */
            var group = mapStatusElement.get(makeStatusKey(type,id));
            if (atbsvg.util.isNull(group)) return ;
            
            group.moveToFront();
            ripple(group);
    	},
    	setupRequestStatusMapMode : function(mode) {
    		reqStatusMapMode = mode;
    	},
    	/* [current driving] */
    	setOnHideTooltipListner : function(cbfunc) {
    		onHideTooltipListner = cbfunc;
    	},
    	/* [current driving] */
    	setOnShowTooltipListner : function(cbfunc) {
    		onShowTooltipListner = cbfunc;
    	},
    	
    	/* [2016/09/08] added */
    	redrawFactoryElementWithId : redrawFactoryElementWithId,
    	redrawZoneElementWithId : redrawZoneElementWithId
    	
    } /* end of return */   
};
