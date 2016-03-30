<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Jsonp test with jquery</title>

<script src="//code.jquery.com/jquery-1.12.0.min.js"></script>
<script>
	$(document).ready(function() {
// 		$("#testBtn").click(function() {
// 			$.getJSON("http://127.0.0.1:8080/server/data.jsp?callback=?", function(d) {
// 				$.each(d, function(k, v) {
// 					$("#getjson").append("<div>" + k + " : " + v + "</div>");
// 				});
// 				$("#getjson").show();
// 			});
				
// 				- JSON
// 				test04.jsp
// 				{"value1":"값1","value2":"값2"}
// 				No 'Access-Control-Allow-Origin' header is present on the requested resource.
// 				Origin 'http://localhost:8091' is therefore not allowed access.

// 				- JSONP
// 				test04.jsp?callback=jQuery1120007468581548891962_1452847492857&_=1452847492864
// 				jQuery1120007468581548891962_1452847492857({"value1":"값1","value2":"값2"});
			$.ajax({
        'beforeSend':function(request){
        	request.setRequestHeader('abc', 3);
        },
// 				url : "http://localhost:8080/test04.jsp",
// 				dataType : "json",
				url : "http://127.0.0.1:8080/test04.jsp",
				dataType : "jsonp",
				jsonp : "callback",
				type : "POST",
				data : "[{a:1, b:2},{c:3}]",
				success : function(d) {
					console.log(d);
// 					$.each(d, function(k, v) {
// 						$("#ajax").append("<div>" + k + " : " + v + "</div>");
// 					});
// 					$("#ajax").show();
				},
				error : function(request, status, error) {
					console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
				}
			});
// 		});
	});
</script>
</head>

<body>
<!-- 	<button id="testBtn">테스트</button> -->
<!-- 	<div id="getjson"></div> -->
<!-- 	<div id="ajax"></div> -->
</body>
</html>