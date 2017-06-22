if (typeof d3 === 'undefined') {
    throw new Error('JavaScript requires d3')
}

if (typeof atbsvg === 'undefined') {
    throw new Error('JavaScript requires d3')
}

/* */
atbsvg.animation = (function() {
    /* */
    function translateAlong(canvas, startx, starty, path) {
        var l = path.getTotalLength();

        return function(d, i, a) {
            return function(t) {
                var p = path.getPointAtLength(t * l);

                /* */
                var base = canvas.translationPoint(startx, starty);
                var transp = canvas.translationPoint(p.x, p.y);
                var translate = [transp.x-base.x,transp.y-base.y];

                //console.log("p:"+ p.x+","+ p.y+" --> tp:"+JSON.stringify(translate));
                
                return "translate( " + translate + ")";
            };
        };
    }

    return {
        ripple : function (canvas, options) {
            if (atbsvg.util.isNull(canvas) || atbsvg.util.isNull(options || atbsvg.util.isNull(canvas.d3svg()))) {
                return ;
            }

            /* */
            if (atbsvg.util.isNull(options.cx)) options.cx = 0;
            if (atbsvg.util.isNull(options.cy)) options.cy = 0;
            if (atbsvg.util.isNull(options.stroke)) options.stroke = 'red'; //'hsl(0, 100%, 70%)';
            if (atbsvg.util.isNull(options['stroke-width'])) options['stroke-width'] = '20px';
            if (atbsvg.util.isNull(options.duration)) options.duration = 2000;
            if (atbsvg.util.isNull(options.delay)) options.delay = 0;
            if (atbsvg.util.isNull(options.radius)) options.radius = 100;
            
            var point = canvas.transformedPoint(options.cx,options.cy);
            /* */
            canvas.d3svg().append('circle')
                .attr({
                    cx: point.x,
                    cy: point.y,
                    stroke: options.stroke,
                    fill: 'none',
                    'stroke-width': options['stroke-width'],
                    r: 1e-6
                })
                .style('stroke-opacity', 1)
                .transition().duration(options.duration)
                .delay(options.delay)
                .ease('cubic-out')
                .style('stroke-opacity', 1e-6)
                .attr('r', options.radius)
                .remove();
        },
        expand : function (d3target, options) {
            if (atbsvg.util.isNull(d3target) || atbsvg.util.isNull(options)) {
                return ;
            }

            if (atbsvg.util.isNull(options.duration)) options.duration = 1000;
            if (atbsvg.util.isNull(options.delay)) options.delay = 0;
            if (atbsvg.util.isNull(options.currentScale)) options.currentScale = 1;
            if (atbsvg.util.isNull(options.currentTranslation)) options.currentTranslation = [0,0];

            var startScale = options.currentScale;
            var endScale = options.currentScale * 1.2;

            /* */
            d3target.transition().duration(options.duration)
                .delay(options.delay)
                .tween("projection", function() {
                    var scale = d3.interpolate(startScale, endScale);
                    return function(t) {
                        d3target.attr('transform','translate('+options.currentTranslation+') scale('+scale(t)+')');
                    };
                })
                .each("end", function() {
                    d3target.attr('transform','translate('+options.currentTranslation+') scale('+options.currentScale+')');
                });
        },
        rubberStrokeWidth : function (d3target, options) {
            if (atbsvg.util.isNull(d3target) || atbsvg.util.isNull(options)) {
                return ;
            }

            if (atbsvg.util.isNull(options.duration)) options.duration = 2000;
            if (atbsvg.util.isNull(options.delay)) options.delay = 0;

            /* */
            var currentWidth = parseInt(d3target.selectAll('rect,circle,line,path,polygon,polyline,ellipse').attr('stroke-width'), 10);

            /* */
            d3target.selectAll('rect,circle,line,path,polygon,polyline,ellipse').transition()
                .duration(options.duration/3)
                .delay(options.delay)
                .style('stroke-width', ''+(currentWidth*5)+'px')
                .transition()
                .duration(options.duration/3)
                .style('stroke-width', ''+(currentWidth)+'px')
                .transition()
                .duration(options.duration/3)
                .style('stroke-width', ''+(currentWidth*5)+'px')
                .transition()
                .duration(options.duration/3)
                .style('stroke-width', ''+(currentWidth)+'px')
        },
        pointAlongPath : function(canvas, d3path, d3primitive, options) {
            if (atbsvg.util.isNull(canvas) || atbsvg.util.isNull(d3path) || atbsvg.util.isNull(d3primitive)) {
                return ;
            }

            if (atbsvg.util.isNull(options.duration)) options.duration = 2000;
            if (atbsvg.util.isNull(options.delay)) options.delay = 0;
            if (atbsvg.util.isNull(options.startX)) options.startX = 0;
            if (atbsvg.util.isNull(options.startY)) options.startY = 0;

            //console.log("d3path:"+JSON.stringify(d3path.node()));

            d3primitive.transition()
                .duration(options.duration)
                .delay(options.delay)
                .ease("linear") /* [2016/08/30] 이정래차장 script 변경요청 작용 .ease("linear") 추가 */
                .attrTween("transform", translateAlong(canvas, options.startX, options.startY, d3path.node()));
                /*
                .each("end", this.pointAlongPathForever(d3path,d3primitive,options));
                */
        },
        appendPointAlongPath : function(d3parent, points, options) {
            if (atbsvg.util.isNull(d3parent) || atbsvg.util.isNull(points) || atbsvg.util.isNull(options)) return ;

            if (atbsvg.util.isNull(options.stroke)) options.stroke = 'red';
            if (atbsvg.util.isNull(options['stroke-width'])) options['stroke-width'] = '5px';
            if (atbsvg.util.isNull(options['circle-stroke'])) options['circle-stroke'] = 'black';
            if (atbsvg.util.isNull(options['circle-stroke-width'])) options['circle-stroke-width'] = '2px';
            if (atbsvg.util.isNull(options['circle-r'])) options['circle-r'] = 4;
            if (atbsvg.util.isNull(options['interpolate'])) options['interpolate'] = 'linear';

            /*
             linear – Normal line (jagged).
             step-before – a stepping graph alternating between vertical and horizontal segments.
             step-after - a stepping graph alternating between horizontal and vertical segments.
             basis - a B-spline, with control point duplication on the ends (that's the one above).
             basis-open - an open B-spline; may not intersect the start or end.
             basis-closed - a closed B-spline, with the start and the end closed in a loop.
             bundle - equivalent to basis, except a separate tension parameter is used to straighten the spline. This could be really cool with varying tension.
             cardinal - a Cardinal spline, with control point duplication on the ends. It looks slightly more 'jagged' than basis.
             cardinal-open - an open Cardinal spline; may not intersect the start or end, but will intersect other control points. So kind of shorter than 'cardinal'.
             cardinal-closed - a closed Cardinal spline, looped back on itself.
             monotone - cubic interpolation that makes the graph only slightly smoother.
             */
            var d3path = d3parent.append('path')
                .data([points])
                .attr({
                	'stroke-linecap':'round',
                    stroke: options.stroke,
                    fill: 'none',
                    'stroke-width': options['stroke-width'],
                })              
                .attr('d',d3.svg.line()
                    .tension(0) // Catmull–Rom
                    .interpolate(options['interpolate']));

            d3parent.selectAll(".point")
                .data(points)
                .enter()
                .append("circle")
                .attr({
                    stroke: options['circle-stroke'],
                    fill: options['circle-stroke'],
                    'stroke-width': options['circle-stroke-width'],
                })
                .attr('cx', function(d) {
                    return d[0];
                })
                .attr('cy', function(d) {
                    return d[1];
                })
                .attr("r", options['circle-r']);

            /* */
            var path = atbsvg.primitive.newPath();
            path.d3element = d3path;

            return path;
        }
    };
})();



/* */
atbsvg.primitive.protoExtend('echo',function() {
    return "echo";
});

