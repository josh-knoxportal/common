if (typeof atbsvg === 'undefined') {
    throw new Error('JavaScript requires AndTheBee D3 Extension')
}
if (typeof math === 'undefined') {
    throw new Error('JavaScript requires mathjs')
}
if (typeof math.intersect === 'undefined') {
    throw new Error('JavaScript requires mathjs')
}

/* [2016/06/03] added */
if (typeof contextPath === 'undefined') {
	contextPath = '';
}

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};
Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;
    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(searchString, position){
      position = position || 0;
      return this.substr(position, searchString.length) === searchString;
  };
};


/* */
var LGSmartMapAttr = (function() {
	/* [common] view */	
	var width = 1150;
	var height= 980.12857;
	var backgroundPath = contextPath+'/resources/img/bg_map_car.png';
	
	/* real map size */
	var mapWidth  = 3500;
	var mapHeight = 2983;
	
	/* canvas size */
	var canvasWidth = 1150;
	var canvasHeight= 980.12857;	
	
	/* */
	recalcWeight();
	
	
	/* */
	function recalcWeight() {
		if (mapWidth > 0) {
			weightWidth = (width / mapWidth);
		} else {
			weightWidth = 0;
		}
		
		if (mapHeight > 0) {
			weightHeight = (height / mapHeight);
		} else {
			weightHeight = 0;
		}
	}
	
	return {
		width : function(value) {
			if (!arguments.length) { return width }
	        width = value;
	        
	        /* */
	        recalcWeight();
	        return this;
		},
		height : function(value) {
			if (!arguments.length) { return height }
			height = value;
			
			/* */
	        recalcWeight();
	        return this;
		},
		backgroundPath : function(value) {
			if (!arguments.length) { return backgroundPath }
			backgroundPath = value;
	        return this;
		},
		mapWidth : function(value) {
			if (!arguments.length) { return mapWidth }
			mapWidth = value;
			
			/* */
	        recalcWeight();
	        return this;
		},
		mapHeight : function(value) {
			if (!arguments.length) { return mapHeight }
			mapHeight = value;
			
			/* */
	        recalcWeight();
	        return this;
		},
		canvasWidth : function(value) {
			if (!arguments.length) { return canvasWidth }
			canvasWidth = value;
	        return this;
		},
		canvasHeight : function(value) {
			if (!arguments.length) { return canvasHeight }
			canvasHeight = value;
	        return this;
		},
		/*
		 * 화면에 보이는 맵의 크기와 실제 이미지 및 좌표계가 다를 경우, 해당 좌표를 화면 맵에 표시하기 위해
		 * 보정이 필요함
		 * (실제맵의 X, Y) --> (화면맵의 X, Y)로 변환
		 */
		pointOnMapView : function(x,y) {
			/*
			 * [2016/05/27] 
			 * 		웹은 (left, top) 기준점 (0,0)
			 * 		앱에서는 (left, bottom) 기준점 (0,0)
			 * 
			 * 		var adjY = mapHeight - y;
			 * 
			 * [2016/06/01]
			 *      (left,top) 기준점 (0,0)으로 재조
			 *      
			 *      var adjY = y;
			 */
			
			var adjY = y;
			return {
				x : (x * weightWidth),
				y : (adjY * weightHeight)
			};
		}
	}
})();



