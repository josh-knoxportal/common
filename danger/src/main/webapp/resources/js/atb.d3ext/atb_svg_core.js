if (typeof d3 === 'undefined') {
    throw new Error('JavaScript requires d3')
}
if (typeof Matrix === 'undefined') {
    throw new Error('JavaScript requires Matrix')
}

/*
 * d3 extenstion
 * ex) d3.select(this).moveToFront();
 */
if (!d3.selection.prototype.moveToFront) {
    d3.selection.prototype.moveToFront = function () {
        return this.each(function () {
            this.parentNode.appendChild(this);
        });
    };
}


if (!d3.selection.prototype.moveToBack) {
    d3.selection.prototype.moveToBack = function () {
        return this.each(function () {
            var firstChild = this.parentNode.firstChild;
            if (firstChild) {
                this.parentNode.insertBefore(this, firstChild);
            }
        });
    };
}



/* */
atbsvg = {
    extend : function(destination, source) {
        for (var property in source) {
            if (source[property] && source[property].constructor &&
                source[property].constructor === Object) {
                destination[property] = destination[property] || {};
                arguments.callee(destination[property], source[property]);
            } else {
                destination[property] = source[property];
            }
        }
        return destination;
    }
};


/* */
atbsvg.sound = function(src) {
    "use strict";
    var audio = new Audio();
    audio.src = src;

    return {
        play : function() {
            audio.play();
        }
    }
};


/* */
atbsvg.util = (function() {
    return {
        isNull : function(value) {
            if (value == null || typeof value == "undefined") {
                return true;
            }

            return false;
        },
        isNullEmpty : function(value) {
            if (value == null || typeof value == "undefined" || value == "") {
                return true;
            }

            return false;
        },
        isFunction : function(value) {
            if (typeof value == 'function') return true;
            return false;
        },
        toJsonString : function(value) {
            return JSON.stringify(value);
        },
        distance : function(x1,y1,x2,y2) {
            return Math.sqrt( Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2) );
        },
        getXYFromTranslate : function(translateString) {
            var split = translateString.split(",");
            var x = split[0] ? ~~split[0].split("(")[1] : 0;
            var y = split[1] ? ~~split[1].split(")")[0] : 0;
            return [x, y];
        }
    };
})();


atbsvg.map = function() {
    var map = {};

    return {
        put: function (key, value) {
            map[key] = value;
        },
        get: function (key) {
            return map[key];
        },
        containsKey: function (key) {
            return key in map;
        },
        containsValue: function (value) {
            for (var prop in map) {
                if (map[prop] == value) return true;
            }
            return false;
        },
        isEmpty: function (key) {
            return (this.size() == 0);
        },
        clear: function () {
            for (var prop in map) {
                delete map[prop];
            }
        },
        remove: function (key) {
            delete map[key];
        },
        keys: function () {
            var keys = new Array();
            for (var prop in map) {
                keys.push(prop);
            }
            return keys;
        },
        values: function () {
            var values = new Array();
            for (var prop in map) {
                values.push(map[prop]);
            }
            return values;
        },
        size: function () {
            var count = 0;
            for (var prop in map) {
                count++;
            }
            return count;
        }
    }
};

atbsvg.primitive = (function() {
    /*
     * svg 공통 속성을 정의한다.
     */
    function element() {
        this.tag = '';
        this.options = {};
        this.eventCallbacks = {};
        this.d3element = null;
        this.text = null;
        this.userdata = null;
    };

    /* */
    element.prototype.attr = function(name, value) {
        if (atbsvg.util.isNull(name) || atbsvg.util.isNull(value)) {
            return this;
        }

        if (!atbsvg.util.isNull(this.d3element)) {
            this.d3element.attr(name, value);
        }

        if (atbsvg.util.isNull(this.options)) this.options = {};
        /* */
        this.options[name] = value;

        return this;
    }

    /* */
    element.prototype.getCenter = function() {
        if (!atbsvg.util.isNull(this.d3element)) {
            // use the native SVG interface to get the bounding box
            var bbox = this.d3element.node().getBBox();
            // return the center of the bounding box
            return [bbox.x + bbox.width / 2, bbox.y + bbox.height / 2];
        }

        return [0,0];
    }

    /* */
    element.prototype.getBoundaryBox = function() {
        if (!atbsvg.util.isNull(this.d3element)) {
            var bbox = this.d3element.node().getBBox();
            // return the center of the bounding box
            /*
             * console.log(JSON.stringify(bbox));
             */

            return bbox;
        }

        return {x:0,y:0,width:0,heigth:0};
    }

    /* */
    element.prototype.isInRectangle = function(x,y,width,height) {
        var box = this.getBoundaryBox();

        /* */
        var bwidth = box.width;
        var bheight = box.height;
        
        if (atbsvg.util.isNull(bwidth)) bwidth = 0;
        if (atbsvg.util.isNull(bheight)) bheight = 0;
        
        /* */
        if (box.x < x || box.y < y) {
            return false;
        }
        
        var x2 = x + width;
        var y2 = y + height;
        
        var bx2 = box.x + bwidth;
        var by2 = box.y + bheight;
        
        if (bx2 > x2 || by2 > y2) {
            return false;
        }
        
//        var center = this.getCenter();
//        if (x > center[0] || y > center[1]) return false;
//
//        var x2 = x + width;
//        var y2 = y + height;
//        if (x2 < center[0] || y2 < center[1]) return false;

        return true;
    }

    /* */
    element.prototype.setOption = function(name, value) {
        if (atbsvg.util.isNull(name) || atbsvg.util.isNull(value)) {
            return this;
        }

        if (atbsvg.util.isNull(this.options)) this.options = {};
        /* */
        this.options[name] = value;

        return this;
    }


    /* */
    element.prototype.concatOption = function(name, value) {
        if (atbsvg.util.isNull(name) || atbsvg.util.isNull(value)) {
            return this;
        }

        if (atbsvg.util.isNull(this.options)) this.options = {};
        /* */
        if (atbsvg.util.isNull(this.options[name])) {
            this.options[name] = '';
        }
        this.options[name] += value;

        return this;
    }

    /* */
    element.prototype.initTag = function(value) {
        this.tag = value;
        return this;
    }

    /* */
    element.prototype.addCallback = function(event, cbfunc) {
        if (!atbsvg.util.isNull(event) && !atbsvg.util.isNull(cbfunc)) {
            if (atbsvg.util.isNull(this.eventCallbacks)) this.eventCallbacks = {};

            this.eventCallbacks[event] = cbfunc;

            if (!atbsvg.util.isNull(this.d3element)) {
                this.d3element.on(event,cbfunc);
            }
        }

        return this;
    };


    /* */
    element.prototype.remove = function() {
        if (!atbsvg.util.isNull(this.d3element)) {
            this.d3element.remove();
            this.d3element = null;
        }
        return this;
    }

    /* */
    element.prototype.clear = function() {
        this.remove();
        /* */
        this.options({});
        this.eventCallbacks({});
        this.text(null);
    }

    /* */
    element.prototype.style = function(name, value) {
        if (!atbsvg.util.isNull(this.d3element)) {
            if (atbsvg.util.isNull(name) || atbsvg.util.isNull(value)) {
                return this;
            }
            this.d3element.style(name, value);
        }

        return this;
    }

    element.prototype.moveToFront = function(name, value) {
        if (!atbsvg.util.isNull(this.d3element)) {
            this.d3element.moveToFront();
        }

        return this;
    }

    element.prototype.moveToBack = function(name, value) {
        if (!atbsvg.util.isNull(this.d3element)) {
            this.d3element.moveToBack();
        }

        return this;
    }
    
    /* */
    element.prototype.hide = function() {
    	return this.style('opacity',0);
    }
    /* */
    element.prototype.show = function(opacity) {
    	if (!arguments.length) { return this.style('opacity',1); }
    	return this.style('opacity',opacity);
    }

    /* */
    element.prototype.transform = function(translation, scale) {
        return this.redrawWithAttr("transform", "translate(" + translation + ")" + " scale(" + scale + ")");
    }

    element.prototype.translation = function(translation) {
        return this.redrawWithAttr("transform", "translate(" + translation + ")");
    }

    element.prototype.rotate = function(rotate) {
        return this.redrawWithAttr("transform", "rotate(" + rotate + ")");
    }


    element.prototype.scale = function(scale) {
        return this.redrawWithAttr("transform", "scale(" + scale + ")");
    }

    element.prototype.skew = function(x, y) {
        return this.redrawWithAttr("transform", "skewX(" + x + ") skewY(" + y + ")");
    }

    element.prototype.skewX = function(x) {
        return this.redrawWithAttr("transform", "skewX(" + x + ")");
    }

    element.prototype.skewY = function(y) {
        return this.redrawWithAttr("transform", "skewY(" + y + ")");
    }

    /* */
    element.prototype.redrawWithAttr = function(name, value) {
        if (atbsvg.util.isNull(name) || atbsvg.util.isNull(value)) {
            return this;
        }

        if (!atbsvg.util.isNull(this.d3element)) {
            this.d3element.attr(name, value);
        }
        return this;
    }


    element.prototype.onClick = function(cbfunc) {
        return this.addCallback('click',cbfunc);
    };
    element.prototype.onFocusIn = function(cbfunc) {
        return this.addCallback('focusin',cbfunc);
    };
    element.prototype.onFocusOut = function(cbfunc) {
        return this.addCallback('focusout',cbfunc);
    };
    element.prototype.onActivate = function(cbfunc) {
        return this.addCallback('activate',cbfunc);
    };
    element.prototype.onMousedown = function(cbfunc) {
        return this.addCallback('mousedown',cbfunc);
    };
    element.prototype.onMouseup = function(cbfunc) {
        return this.addCallback('mouseup',cbfunc);
    };
    element.prototype.onMouseover = function(cbfunc) {
        return this.addCallback('mouseover',cbfunc);
    };
    element.prototype.onMousemove = function(cbfunc) {
        return this.addCallback('mousemove',cbfunc);
    };
    element.prototype.onMouseout = function(cbfunc) {
        return this.addCallback('mouseout',cbfunc);
    }

    /* */
    element.prototype.on = function(event, cbfunc) {
        return this.addCallback(event,cbfunc);
    }

    /* attributes */
    element.prototype.id = function(value) {
        if (!arguments.length) { return this.options.id; }
        return this.attr('id',value);
    };
    element.prototype.x = function(value) {
        if (!arguments.length) { return this.options.x; }
        return this.attr('x',value);
    };
    element.prototype.y = function(value) {
        if (!arguments.length) { return this.options.y; }
        return this.attr('y',value);
    };
    element.prototype.width = function(value) {
        if (!arguments.length) { return this.options.width; }
        return this.attr('width',value);
    };
    element.prototype.height = function(value) {
        if (!arguments.length) { return this.options.height; }
        return this.attr('height',value);
    };
    element.prototype.stroke = function(value) {
        if (!arguments.length) { return this.options.stroke; }
        return this.attr('stroke',value);
    };
    element.prototype.strokeWidth = function(value) {
        if (!arguments.length) { return this.options['stroke-width']; }
        return this.attr('stroke-width',value);
    };
    element.prototype.fill = function(value) {
        if (!arguments.length) { return this.options.fill; }
        return this.attr('fill',value);
    };
    element.prototype.fillOpacity = function(value) {
        if (!arguments.length) { return this.options.fill; }
        return this.attr('fill-opacity',value);
    };
    element.prototype.strokeOpacity = function(value) {
        if (!arguments.length) { return this.options['stroke-opacity']; }
        return this.attr('stroke-opacity',value);
    };
    element.prototype.strokeDasharray = function(value) {
        if (!arguments.length) { return this.options['stroke-dasharray']; }
        return this.attr('stroke-dasharray',value);
    };
    element.prototype.strokeLinecap = function(value) {
        if (!arguments.length) { return this.options['stroke-linecap']; }
        return this.attr('stroke-linecap',value);
    };
    element.prototype.opacity = function(value) {
        if (!arguments.length) { return this.options['opacity']; }
        return this.attr('opacity',value);
    };


    /*
     * svg image의 속성을 정의한다.
     */
    function image() {
        element.call(this);
        this.initTag('image');
    }

    image.prototype = Object.create(element.prototype);
    image.prototype.constructor = image;
    image.prototype.href = function(value) {
        if (!arguments.length) { return this.options.href; }
        return this.attr('href',value);;
    };

    /*
     * svg text의 속성을 정의한다.
     */
    function text() {
        element.call(this);
        this.initTag('text');
    }

    text.prototype = Object.create(element.prototype);
    text.prototype.constructor = text;

    text.prototype.fontSize = function(value) {
        if (!arguments.length) { return this.options['font-size']; }
        return this.attr('font-size',value);
    };
    text.prototype.fontFamily = function(value) {
        if (!arguments.length) { return this.options['font-family']; }
        return this.attr('font-family',value);
    };
    text.prototype.fontStyle = function(value) {
        if (!arguments.length) { return this.options['font-style']; }
        return this.attr('font-style',value);
    };
    text.prototype.textDecoration = function(value) {
        if (!arguments.length) { return this.options['text-decoration']; }
        return this.attr('text-decoration',value);
    };
    text.prototype.fontWeight = function(value) {
        if (!arguments.length) { return this.options['font-weight']; }
        return this.attr('font-weight',value);
    };
    text.prototype.wordSpacing = function(value) {
        if (!arguments.length) { return this.options['word-spacing']; }
        return this.attr('word-spacing',value);
    };
    text.prototype.letterSpacing = function(value) {
        if (!arguments.length) { return this.options['letter-spacing']; }
        return this.attr('letter-spacing',value);
    };
    text.prototype.textAnchor = function(value) {
        if (!arguments.length) { return this.options['text-anchor']; }
        return this.attr('text-anchor',value);
    };
    text.prototype.textLength = function(value) {
        if (!arguments.length) { return this.options['textLength']; }
        return this.attr('textLength',value);
    };
    text.prototype.lengthAdjust = function(value) {
        if (!arguments.length) { return this.options['lengthAdjust']; }
        return this.attr('lengthAdjust',value);
    };
    text.prototype.direction = function(value) {
        if (!arguments.length) { return this.options['direction']; }
        return this.attr('direction',value);
    };
    text.prototype.writingMode = function(value) {
        if (!arguments.length) { return this.options['writing-mode']; }
        return this.attr('writing-mode',value);
    };



    /*
     * svg line의 속성을 정의한다.
     */
    function line() {
        element.call(this);
        this.initTag('line');
    }

    line.prototype = Object.create(element.prototype);
    line.prototype.constructor = line;
    line.prototype.x1 = function(value) {
        if (!arguments.length) { return this.options.x1; }
        return this.attr('x1',value);
    };
    line.prototype.y1 = function(value) {
        if (!arguments.length) { return this.options.y1; }
        return this.attr('y1',value);
    };
    line.prototype.x2 = function(value) {
        if (!arguments.length) { return this.options.x2; }
        return this.attr('x2',value);
    };
    line.prototype.y2 = function(value) {
        if (!arguments.length) { return this.options.y2; }
        return this.attr('y2',value);
    };


    /*
     * svg rect의 속성을 정의한다.
     */
    function rect() {
        element.call(this);
        this.initTag('rect');
    }

    rect.prototype = Object.create(element.prototype);
    rect.prototype.constructor = rect;
    rect.prototype.rx = function(value) {
        if (!arguments.length) { return this.options.rx; }
        return this.attr('rx',value);
    };

    /*
     * svg anchor의 속성을 정의한다.
     */
    function anchor() {
        element.call(this);
        this.initTag('a');
    }
    anchor.prototype = Object.create(element.prototype);
    anchor.prototype.constructor = anchor;
    anchor.prototype.href = function(value) {
        if (!arguments.length) { return this.options['href']; }
        return this.attr('href',value);
    };


    /*
     * svg circle의 속성을 정의한다.
     */
    function circle() {
        element.call(this);
        this.initTag('circle');
    }
    circle.prototype = Object.create(element.prototype);
    circle.prototype.constructor = circle;
    circle.prototype.cx = function(value) {
        if (!arguments.length) { return this.options.cx; }
        return this.attr('cx',value);
    };
    circle.prototype.cy = function(value) {
        if (!arguments.length) { return this.options.cy; }
        return this.attr('cy',value);
    };
    circle.prototype.r = function(value) {
        if (!arguments.length) { return this.options.r; }
        return this.attr('r',value);
    };


    /*
     * svg ellipse의 속성을 정의한다.
     */
    function ellipse() {
        element.call(this);
        this.initTag('ellipse');
    }
    ellipse.prototype = Object.create(element.prototype);
    ellipse.prototype.constructor = ellipse;
    ellipse.prototype.cx = function(value) {
        if (!arguments.length) { return this.options.cx; }
        return this.attr('cx',value);
    };
    ellipse.prototype.cy = function(value) {
        if (!arguments.length) { return this.options.cy; }
        return this.attr('cy',value);
    };
    ellipse.prototype.rx = function(value) {
        if (!arguments.length) { return this.options.rx; }
        return this.attr('rx',value);
    };
    ellipse.prototype.ry = function(value) {
        if (!arguments.length) { return this.options.ry; }
        return this.attr('ry',value);
    };


    /*
     * svg polyline의 속성을 정의한다.
     */
    function polyline() {
        element.call(this);
        this.initTag('polyline');
    }
    polyline.prototype = Object.create(element.prototype);
    polyline.prototype.constructor = polyline;
    polyline.prototype.points = function(value) {
        if (!arguments.length) { return this.options.points; }
        return this.attr('points',value);
    };
    polyline.prototype.startPoint = function(x,y) {
        return this.setOption('points'," "+x+","+y);
    };
    polyline.prototype.nextPoint = function(x,y) {
        return this.concatOption('points'," "+x+","+y);
    };
    polyline.prototype.draw = function() {
        return this.attr('points',this.options.points);;
    };


    /*
     * svg polygon의 속성을 정의한다.
     */
    function polygon() {
        element.call(this);
        this.initTag('polygon');
    }
    polygon.prototype = Object.create(element.prototype);
    polygon.prototype.constructor = polygon;
    polygon.prototype.points = function(value) {
        if (!arguments.length) { return this.options.points; }
        return this.attr('points',value);;
    };
    polygon.prototype.startPoint = function(x,y) {
        return this.setOption('points'," "+x+","+y);
    };
    polygon.prototype.nextPoint = function(x,y) {
        return this.concatOption('points'," "+x+","+y);
    };
    polygon.prototype.fillRule = function(x,y) {
        if (!arguments.length) { return this.options['fill-rule']; }
        return this.attr('fill-rule',value);;
    };
    polygon.prototype.draw = function() {
        return this.attr('points',this.options.points);
    };

    /*
     * svg path의 속성을 정의한다.
     */
    function path() {
        element.call(this);
        this.initTag('path');
    }
    path.prototype = Object.create(element.prototype);
    path.prototype.constructor = path;
    path.prototype.d = function(value) {
        if (!arguments.length) { return this.options.d; }
        return this.attr('d',value);
    };
    path.prototype.draw = function() {
        return this.attr('d',this.options.d);
    };

    path.prototype.moveTo = function(x,y) {
        return this.concatOption('d'," M "+x+","+y);
    };
    path.prototype.lineTo = function(x,y) {
        return this.concatOption('d'," L "+x+","+y);
    };
    path.prototype.closePath = function() {
        return this.concatOption('d'," Z ");
    };
    path.prototype.vertical = function(y) {
        return this.concatOption('d'," V "+y);
    };
    path.prototype.horizontal = function(x) {
        return this.concatOption('d'," H "+x);
    };
    path.prototype.curveTo = function(x1,y1,x2,y2,x,y) {
        return this.concatOption('d'," C "+x1+","+y1+" "+x2+","+y2+" "+x+","+y);
    };
    path.prototype.smoothCurveTo = function(x2,y2,x,y) {
        return this.concatOption('d'," S "+x2+","+y2+" "+x+","+y);
    };
    path.prototype.bezierCurveTo = function(x1,y1,x,y) {
        return this.concatOption('d'," Q "+x1+","+y1+" "+x+","+y);
    };
    path.prototype.smoothBezierCurveTo = function(x,y) {
        return this.concatOption('d'," T "+x+","+y);
    };
    /* a25,100 -30 0,1 50,-25 */
    path.prototype.ellipticalArc = function(rx,ry,xaxisrotation,largearcflag,sweepflag,x,y) {
        return this.concatOption('d'," A "+rx+","+ry+" "+xaxisrotation+" "+largearcflag+","+sweepflag+" "+x+","+y);
    };
    path.prototype.moveToRelative = function(x,y) {
        return this.concatOption('d'," m "+x+","+y);
    };
    path.prototype.lineToRelative = function(x,y) {
        return this.concatOption('d'," l "+x+","+y);
    };
    path.prototype.closePathRelative = function() {
        return this.concatOption('d'," z ");
    };
    path.prototype.verticalRelative = function(y) {
        return this.concatOption('d'," v "+y);
    };
    path.prototype.horizontalRelative = function(x) {
        return this.concatOption('d'," h "+x);
    };
    path.prototype.curveToRelative = function(x1,y1,x2,y2,x,y) {
        return this.concatOption('d'," c "+x1+","+y1+" "+x2+","+y2+" "+x+","+y);
    };
    path.prototype.smoothCurveToRelative = function(x2,y2,x,y) {
        return this.concatOption('d'," s "+x2+","+y2+" "+x+","+y);
    };
    path.prototype.bezierCurveToRelative = function(x1,y1,x,y) {
        return this.concatOption('d'," q "+x1+","+y1+" "+x+","+y);
    };
    path.prototype.smoothBezierCurveToRelative = function(x,y) {
        return this.concatOption('d'," t "+x+","+y);
    };
    /* a25,100 -30 0,1 50,-25 */
    path.prototype.ellipticalArcRelative = function(rx,ry,xaxisrotation,largearcflag,sweepflag,x,y) {
        return this.concatOption('d'," a "+rx+","+ry+" "+xaxisrotation+" "+largearcflag+","+sweepflag+" "+x+","+y);
    };


    /*
     * svg group의 속성을 정의한다.
     */
    function group() {
        element.call(this);
        this.initTag('g');
    }
    group.prototype = Object.create(element.prototype);
    group.prototype.constructor = group;


    /*
     * svg defs의 속성을 정의한다.
     */
    function defs() {
        element.call(this);
        this.initTag('defs');
    }
    defs.prototype = Object.create(element.prototype);
    defs.prototype.constructor = defs;


    return {
        protoExtend : function(name, fn) {
            if (atbsvg.util.isNull(name) || !atbsvg.util.isFunction(fn)) return ;
            element.prototype[name] = fn;
        },
        appendElement : function(d3parent, primitive) {
            if (atbsvg.util.isNull(d3parent) || atbsvg.util.isNull(primitive)) return null;

            var tag   = primitive.tag,
                attrs = primitive.options,
                text  = primitive.text,
                eventCallbacks = primitive.eventCallbacks;

            var element = d3parent.append(tag);
            primitive.d3element = element;

            /* */
            for(var name in attrs) {
                element.attr(name, attrs[name]);
            }
            if (!atbsvg.util.isNullEmpty(text)) {
                element.text(text)
            }
            for(var name in eventCallbacks) {
                var fn = eventCallbacks[name];
                if (atbsvg.util.isFunction(fn)) {
                    element.on(name, fn);
                }
            }
            return element;
        },
        newImage : function() {
            return new image();
        },
        newText : function() {
            return new text();
        },
        newLine : function() {
            return new line();
        },
        newRect : function() {
            return new rect();
        },
        newAnchor : function() {
            return new anchor();
        },
        newCircle : function() {
            return new circle();
        },
        newEllipse : function() {
            return new ellipse();
        },
        newPolyline : function() {
            return new polyline();
        },
        newPolygon : function() {
            return new polygon();
        },
        newPath : function() {
            return new path();
        },
        newGroup : function() {
            return new group();
        },
        newDefs : function() {
            return new defs();
        }
    }
})();


/* */
function importNode(xml) {
    return document.importNode(xml.documentElement, true);
}

atbsvg.complex = (function() {
    /*
     * svg 공통 속성을 정의한다.
     */
    function document() {

    };

    /*
     * svg group의 속성을 정의한다.
     */
    function svg() {
        this.node = null;
    }
    svg.prototype = Object.create(document.prototype);
    svg.prototype.constructor = svg;
    svg.prototype.load = function(svgUri) {
        var that = this;
        d3.xml(svgUri, "image/svg+xml", function(xml) {
            that.node = importNode(xml);
        });

        return this;
    };
    svg.prototype.appendInParent = function(d3parent) {
        if (atbsvg.util.isNull(d3parent) || atbsvg.util.isNull(this.node)) return ;

        var child = d3parent.node().appendChild(this.node.cloneNode(true));

        child.attr('x',200);
        return this;
    }


    return {
        protoExtend: function (name, fn) {
            if (atbsvg.util.isNull(name) || !atbsvg.util.isFunction(fn)) return;
            document.prototype[name] = fn;
        },
        newSvg : function() {
            return new svg();
        }
    }
})();

/* drawtype */
var/*const*/ RECTANGLE = 'rectangle';
var/*const*/ CIRCLE    = 'circle';
var/*const*/ POLYGON   = 'polygon';
var/*const*/ LINE      = 'line';

/* [2016/08/19] added */
var/*const*/ COLOR_DRAGGABLE_SHAPE = '#774c87';
var/*const*/ STROKEWIDTH_DRAGGABLE_SHAPE = '2.5px';

/* */
atbsvg.canvas = function() {
    "use strict";

    /* */
    var d3svg           = null,
        zoom            = null,
        base            = null,
        xScale          = null,
        yScale          = null,
        width           = window.innerWidth,
        height          = window.innerHeight,
        zindex          = 0,
        zoomEnabled     = true,
        dragEnabled     = true,
        drawType        = "rectangle",
        scale           = 1,
        translation     = [0,0],
        id              = 'mainsvg',
        
        /* 	const COLOR_SECTION_SELECTED		= '#774c87'; */
        draggableBox    = atbsvg.primitive.newRect().x(0).y(0).width(0).height(0).stroke(COLOR_DRAGGABLE_SHAPE).strokeWidth(STROKEWIDTH_DRAGGABLE_SHAPE).fill("none").strokeDasharray("5, 1"),
        draggableCircle = atbsvg.primitive.newCircle().cx(0).cy(0).r(0).stroke(COLOR_DRAGGABLE_SHAPE).strokeWidth(STROKEWIDTH_DRAGGABLE_SHAPE).fill("none").strokeDasharray("5, 1"),
        draggablePolygon = atbsvg.primitive.newPolygon().points("0,0").stroke(COLOR_DRAGGABLE_SHAPE).strokeWidth(STROKEWIDTH_DRAGGABLE_SHAPE).fill("none").strokeDasharray("5, 1"),
        draggablePolygonSize = 4,
        draggableLine = atbsvg.primitive.newLine().x1(0).y1(0).x2(0).y2(0).stroke(COLOR_DRAGGABLE_SHAPE).strokeWidth(STROKEWIDTH_DRAGGABLE_SHAPE).fill("none").strokeDasharray("5, 1");

    /* */
    var mouseZoomEnabled 			= true,
    	blockedDragAway 			= false;

	/* */
	var onStartTransformListener;
	var onEndTransformListener;
	
    /* */
    var zoomSelectors   = [];

    /* */
    function canvas(selection) {
        base = selection;
        d3svg = base.append("svg")
            .attr("id",id)
            .attr("width", width)
            .attr("height", height)
            .style("z-index", zindex);

        /* */
        xScale = d3.scale.linear()
            .domain([-width / 2, width / 2])
            .range([0, width]);

        yScale = d3.scale.linear()
            .domain([-height / 2, height / 2])
            .range([height, 0]);

        /* */
        function redraw() {
            if (atbsvg.util.isNull(d3svg)) return ;
            
        	/* [2016/06/07] */
        	if (atbsvg.util.isFunction(onStartTransformListener)) onStartTransformListener();	
        	      	
            /* */
            d3svg.attr("transform", "translate(" + translation + ")" + " scale(" + scale + ")");
            /* */
            for (var name in zoomSelectors) {
                d3svg.selectAll(zoomSelectors[name]).attr("transform", "translate(" + translation + ")" + " scale(" + scale + ")");
            }    
            
            /* [2016/06/07] */
        	if (atbsvg.util.isFunction(onEndTransformListener)) onEndTransformListener();
        }
        
        /* */
        function transforming(x, y, scale, translate) {
            var m = new Matrix().translate(translate[0],translate[1]).scaleU(scale);
            return m.applyToPoint(x, y);
        };

        /* [2016/05/16] */
        function keepZoom() {
        	zoom.scale(scale);
        	zoom.translate(translation);
        }
        
        /* */
        function zoomReset() {
            scale = 1;
            translation = [0,0];
            zoom.scale(scale).translate(translation);
            redraw();
        }
        
        /* */
        function fixedScale(scale) {
        	return Math.round(scale * 100) / 100;
        }
        
        /* */
        function isMovingInCanvas(scale, translate) {
        	var topleft = transforming(0,0,scale,translate);
        	if (topleft.x > 0 || topleft.y > 0) {
        		return false;
        	}
        	var topright = transforming(width,0,scale,translate);
        	if (topright.x < width || topright.y > 0) {
        		return false;
        	}
        	
        	var bottomleft = transforming(0,height,scale,translate);
        	if (bottomleft.x > 0 || bottomleft.y < height) {
        		return false;
        	}
        	
        	var bottomright = transforming(0,height,scale,translate);
        	if (topright.x < width || bottomright.y < height) {
        		return false;
        	}	
        	
        	return true;
        }

        zoom = d3.behavior.zoom()
            .center([width / 2, height / 2])
            .scaleExtent([1, 10])
            .on("zoom",function() {
                if (!zoomEnabled) { return; }
                
                /* */
                if (d3.event && mouseZoomEnabled) {
                    scale = fixedScale( d3.event.scale);
                }
            	
                if (!mouseZoomEnabled) {
                	if (d3.event.scale != scale) {
                		keepZoom();
                		return ;
                	}
                }
                
                /* */
                if (blockedDragAway && scale < 1) {
                	scale = 1;
                	keepZoom();
            		return ;
                }
                
                if (dragEnabled) {              	
                    /* */
                	if (blockedDragAway && !isMovingInCanvas(scale,d3.event.translate)) {
                		keepZoom();
                		return ;
                	}
                                	
                    var tbound = -height * scale,
                        bbound = height  * scale,
                        lbound = -width  * scale,
                        rbound = width   * scale;
                    // limit translation to thresholds
                    translation = d3.event ? d3.event.translate : [0, 0];
                    translation = [
                        Math.max(Math.min(translation[0], rbound), lbound),
                        Math.max(Math.min(translation[1], bbound), tbound)
                    ];
                }

                //console.log("scale:"+scale+",translation:"+translation);
                redraw();
            });
        
        /* */
        d3svg.call(zoom);

        /* */
        atbsvg.primitive.appendElement(d3svg, draggableBox);
        atbsvg.primitive.appendElement(d3svg, draggableCircle);
        atbsvg.primitive.appendElement(d3svg, draggablePolygon);
        atbsvg.primitive.appendElement(d3svg, draggableLine);

        /* */
        canvas.d3svg = function() {
            return d3svg;
        }

        /* */
        canvas.d3zoom = function() {
            return
        }

        /* */
        canvas.appendElement = function(primitive) {
            return this.innerElement(d3svg, primitive);
        };
        
        /* */
        canvas.removeChildAll = function(elementName) {
        	d3svg.selectAll(elementName).remove();
        }

        /* [2016/06/08] */
        canvas.fixedScale = function(s) {
        	return fixedScale(s);
        }
        
        /* */
        canvas.zoomScale = function(s) {
        	/* [2016/05/16] */        	
        	s = fixedScale(s);
        	
        	//console.log("s:"+s);
        	if (blockedDragAway && s <= 1.0) {
        		//console.log("reset");
        		zoomReset();
        		return ;
        	}
        	
        	/* [2016/05/16] */
        	if (blockedDragAway && !isMovingInCanvas(s,translation)) {
        		translation[0] = Math.floor(translation[0] * (scale - 1.0));
            	translation[1] = Math.floor(translation[1] * (scale - 1.0));            
        	} else {  		
	        	/* 
	        	 * [2016/05/16] 마우스 휠은 줌이 센터에서 발생하지만 강제 줌인 경우에는 topleft 에서 발생
	        	 * 이를 센터에서 발생하는 것처럼 모의 하는 기능 
	        	 */
	        	var topleftBefore = transforming(width, height, scale, translation);
	        	var topleftAfter  = transforming(width, height, s, translation);
	        	
	        	var marginX = topleftAfter.x - topleftBefore.x;
	        	var marginY = topleftAfter.y - topleftBefore.y;
	        	
	        	translation[0] -= (marginX/2);
	        	translation[1] -= (marginY/2);
        	}
        	
        	/* [2016/05/16] */
        	if (blockedDragAway) {
        		if (translation[0] > 0) translation[0] = 0;
        		if (translation[1] > 0) translation[1] = 0;
        	}
	        	
        	
            /* */
            scale = s;
            keepZoom();
            
            /*
             * zoom.scale(scale);
             */           
            /* */
            redraw();
        };
        
        canvas.zoomScaleExtent = function(min, max) {
        	zoom.scaleExtent([min,max]);
        }
        
        /* */
        canvas.zoomTranslate = function(t) {
            translation = t;
            zoom.translate(translation);
            redraw();
        };

        /* */
        canvas.zoomReset = function() {
        	zoomReset();
        };

        /* init-point to transformed point */
        canvas.transformedPoint = function(x, y) {
            var m = new Matrix().translate(translation[0],translation[1]).scaleU(scale);
            return m.applyToPoint(x, y);
        };

        /* transformed point to init-point */
        canvas.inversePoint = function(x, y) {
            var m = new Matrix().translate(translation[0],translation[1]).scaleU(scale).inverse();
            return m.applyToPoint(x, y);
        };

        /* */
        canvas.translationPoint = function(x, y) {
            var m = new Matrix().translate(translation[0],translation[1]);
            return m.applyToPoint(x, y);
        };

        /* */
        canvas.transformedElement = function(d3selector) {
            d3selector.attr("transform", "translate(" + translation + ")" + " scale(" + scale + ")");
        };

        /* */
        canvas.clearMouseDragDrawing = function() {
            draggableBox.x(0).y(0).width(0).height(0);
            draggableCircle.cx(0).cy(0).r(0);
            draggablePolygon.points("0,0");
            draggableLine.x1(0).y1(0).x2(0).y2(0);
        };

        /* */
        canvas.initMouseDrag = function(mouseEvent) {
            var started = false;                // flag to indicate when shape has been clicked
            var startX = 0, startY = 0;         // stores cursor location upon first click
            var polygons = 0;

            /* */
            d3svg.on('mousedown',function() {
                if (zoomEnabled || dragEnabled) {
                    return;
                }

                /* */
                started = true;

                /* */
                var coordinates = [0, 0];
                coordinates = d3.mouse(this);

                startX = coordinates[0];
                startY = coordinates[1];

                var options = {};
                if (drawType == CIRCLE) {
                    /* */
                    draggableCircle.cx(startX).cy(startY);
                    draggableCircle.d3element.moveToFront();

                    /* */
                    options.cx = startX;
                    options.cy = startY;
                    options.r = 0;
                } else if (drawType == POLYGON) {
                    return ;
                } else if (drawType == LINE) {
                    draggableLine.x1(startX).y1(startY).x2(startX).y2(startY);
                    draggableLine.d3element.moveToFront();

                    /* */
                    options.x1 = startX;
                    options.y1 = startY;
                    options.x2 = startX;
                    options.y2 = startY;
                } else {
                    /* */
                    draggableBox.x(startX).y(startY);
                    draggableBox.d3element.moveToFront();

                    /* */
                    options.x = startX;
                    options.y = startY;
                    options.width = 0;
                    options.height = 0;
                }

                if (mouseEvent) {
                    mouseEvent('mousedown',drawType,options);
                }
            });
            d3svg.on('mousemove',function() {
                if (zoomEnabled || dragEnabled) { return; }

                if (started) {
                    var coordinates = [0, 0];
                    coordinates = d3.mouse(this);

                    var options = {};
                    if (drawType == CIRCLE) {
                        var r = atbsvg.util.distance(startX,startY,coordinates[0],coordinates[1]);
                        draggableCircle.r(r);
                        /* */
                        options.cx = startX;
                        options.cy = startY;
                        options.r = r;
                    } else if (drawType == POLYGON) {
                        return ;
                    } else if (drawType == LINE) {
                        draggableLine.x1(startX).y1(startY).x2(coordinates[0]).y2(coordinates[1]);

                        /* */
                        options.x1 = startX;
                        options.y1 = startY;
                        options.x2 = coordinates[0];
                        options.y2 = coordinates[1];
                    } else {
                        var width  = coordinates[0] - startX;
                        var height = coordinates[1] - startY;
                    
                        /* case 1 : all >= 0 */
                        if (width >= 0 && height >= 0) {
                        	draggableBox.x(startX).y(startY).width(width).height(height);

                            options.x = startX;
                            options.y = startY;
                            options.width = width;
                            options.height = height;
                        } else if (width < 0 && height < 0) {
                        	/* case 2 : all < 0 */
                        	width = Math.abs(width);
                            height = Math.abs(height);
                            
                            draggableBox.x(coordinates[0]).y(coordinates[1]).width(width).height(height);

                            options.x = coordinates[0];
                            options.y = coordinates[1];
                            options.width = width;
                            options.height = height;
                        } else if (width < 0) {
                        	/* case 3 : only width < 0 */
                        	width = Math.abs(width);
                            height = Math.abs(height);
                            
                            draggableBox.x(coordinates[0]).y(startY).width(width).height(height);

                            options.x = coordinates[0];
                            options.y = startY;
                            options.width = width;
                            options.height = height;                     	
                        } else {
                        	/* case 4 : only height < 0 */
                        	
                        	width = Math.abs(width);
                            height = Math.abs(height);
                            
                            draggableBox.x(startX).y(coordinates[1]).width(width).height(height);

                            options.x = startX;
                            options.y = coordinates[1];
                            options.width = width;
                            options.height = height; 
                        }
                    }

                    if (mouseEvent) {
                        mouseEvent('mousemove',drawType,options);
                    }
                }
            });
            
            /* [2016/08/22] added */
            d3svg.on('focusout',function() {
                if (zoomEnabled || dragEnabled) { return; }

                /* */
                if (started) {
                	started = false;
                	if (drawType == CIRCLE) {
                		draggableCircle.cx(0).cy(0).r(0);
                	} else if (drawType == POLYGON) {
                		draggablePolygon.points('0,0');
                	}  else if (drawType == LINE) {
                		draggableLine.x1(0).y1(0).x2(0).y2(0);
                	} else {
                		draggableBox.x(0).y(0).width(0).height(0);   
                	}
                }
            });
            
            
            d3svg.on('mouseleave',function() {
                if (zoomEnabled || dragEnabled) { return; }

                /* */
                if (started) {
                	started = false;
                	if (drawType == CIRCLE) {
                		draggableCircle.cx(0).cy(0).r(0);
                	} else if (drawType == POLYGON) {
                		draggablePolygon.points('0,0');
                	}  else if (drawType == LINE) {
                		draggableLine.x1(0).y1(0).x2(0).y2(0);
                	} else {
                		draggableBox.x(0).y(0).width(0).height(0);   
                	}
                }
            });
            
            d3svg.on('mouseup',function() {
                if (zoomEnabled || dragEnabled) { return; }

                if (started) {
                    /* */
                    var coordinates = [0, 0];
                    coordinates = d3.mouse(this);

                    var options = {};
                    if (drawType == CIRCLE) {
                        started = false;

                        var r = atbsvg.util.distance(startX,startY,coordinates[0],coordinates[1]);
                        draggableCircle.cx(0).cy(0).r(0);
                        /* */
                        options.cx = startX;
                        options.cy = startY;
                        options.r = r;
                    } else if (drawType == POLYGON) {
                        var points = '';
                        if (draggablePolygon.points() == '0,0') {
                            polygons = 0;
                            points = ""+coordinates[0]+","+coordinates[1];
                        } else {
                            polygons++;
                            points = draggablePolygon.points() + " "+coordinates[0]+","+coordinates[1];
                        }

                        /* */
                        options.points = points;

                        draggablePolygon.points(points);
                        draggablePolygon.d3element.moveToFront();

                        if (polygons >= draggablePolygonSize) {
                            started = false;
                            draggablePolygon.points('0,0');
                            if (mouseEvent) {
                                mouseEvent('mouseup',drawType,options);
                            }
                        } else {
                            if (mouseEvent) {
                                mouseEvent('mousemove',drawType,options);
                            }
                        }
                        return ;
                    }  else if (drawType == LINE) {
                        started = false;
                        draggableLine.x1(0).y1(0).x2(0).y2(0);

                        /* */
                        options.x1 = startX;
                        options.y1 = startY;
                        options.x2 = coordinates[0];
                        options.y2 = coordinates[1];
                    } else {
                        started = false;
                        var width = coordinates[0] - startX;
                        var height = coordinates[1] - startY;
                        
                        draggableBox.x(0).y(0).width(0).height(0);
                        /* case 1 : all >= 0 */
                        if (width >= 0 && height >= 0) {
                            options.x = startX;
                            options.y = startY;
                            options.width = width;
                            options.height = height;
                        } else if (width < 0 && height < 0) {
                        	/* case 2 : all < 0 */
                        	width = Math.abs(width);
                            height = Math.abs(height);
                            
                            options.x = coordinates[0];
                            options.y = coordinates[1];
                            options.width = width;
                            options.height = height;
                        } else if (width < 0) {
                        	/* case 3 : only width < 0 */
                        	width = Math.abs(width);
                            height = Math.abs(height);
                            
                             options.x = coordinates[0];
                            options.y = startY;
                            options.width = width;
                            options.height = height;                     	
                        } else {
                        	/* case 4 : only height < 0 */                      	
                        	width = Math.abs(width);
                            height = Math.abs(height);
                            
                            options.x = startX;
                            options.y = coordinates[1];
                            options.width = width;
                            options.height = height; 
                        }
                    }

                    if (mouseEvent) {
                        mouseEvent('mouseup',drawType,options);
                    }
                }

            });
        }
    }

    /* */
    canvas.id = function(value) {
        if (!arguments.length) { return id; }
        id = value;
        return this;
    };


    canvas.width = function(value) {
        if (!arguments.length) return width;
        width = parseInt(value, 10);
        return this;
    };

    canvas.height = function(value) {
        if (!arguments.length) return height;
        height = parseInt(value, 10);
        return this;
    };

    canvas.zindex = function(value) {
        if (!arguments.length) return height;
        zindex = parseInt(value, 10);
        return this;
    }

    canvas.zoomEnabled = function(enabled) {
        if (!arguments.length) { return zoomEnabled; }
        zoomEnabled = enabled;
        return this;
    };

    canvas.mouseZoomEnabled = function(enabled) {
        if (!arguments.length) { return mouseZoomEnabled; }
        mouseZoomEnabled = enabled;
        return this;
    };
    
    canvas.dragEnabled = function(enabled) {
        if (!arguments.length) { return dragEnabled; }
        dragEnabled = enabled;
        return this;
    };

    canvas.blockedDragAway = function(blocked) {
        if (!arguments.length) { return blockedDragAway; }
        blockedDragAway = blocked;
        return this;
    };
  
    canvas.drawType = function(type) {
        if (!arguments.length) { return drawType; }

        if (type == CIRCLE || type == POLYGON || type == LINE) {
            drawType = type;
        } else {
            drawType = RECTANGLE;
        }

        return this;
    };

    canvas.draggablePolygonSize = function(size) {
        if (!arguments.length) { return draggablePolygonSize; }
        draggablePolygonSize = size;

        return this;
    }

    canvas.scale = function(value) {
        if (!arguments.length) { return scale; }
        scale = value;
        return this;
    };

    canvas.translation = function(value) {
        if (!arguments.length) { return translation; }
        translation = value;
        return this;
    };

    canvas.zoomSelectors = function(selectors) {
        if (!arguments.length) { return zoomSelectors; }
        zoomSelectors = selectors;
        return this;
    };
    canvas.addZoomSelector = function(selector) {
        if (!atbsvg.util.isNullEmpty(selector)) {
            if (atbsvg.util.isNull(zoomSelectors)) {
                zoomSelectors = [];
            }
            zoomSelectors.push(selector);
        }
        return this;
    };

    /* */
    canvas.innerElement = function(svgParent, primitive) {
        return atbsvg.primitive.appendElement(svgParent,primitive);
    };


    /* */
    canvas.getDraggablePrimitiveBoxInstance = function() {
        return draggableBox;
    };
    
    
	/* [2016/06/07] */
    canvas.setOnStartTransformListener = function(cbfunc) {
    	onStartTransformListener = cbfunc;
    };
    
    canvas.setOnEndTransformListener = function(cbfunc) {
    	onEndTransformListener = cbfunc;
    };

    return canvas;
}


