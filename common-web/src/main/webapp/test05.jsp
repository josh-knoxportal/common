<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	request.setAttribute("skoh", 1);

	System.out.println(request.getAttribute("skoh"));
%>

<%-- ${skoh} --%>
<%-- <c:out value="${skoh}" /> --%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jquery</title>

<script src="http://code.jquery.com/jquery-2.1.1.js" integrity="sha256-FA/0OOqu3gRvHOuidXnRbcmAWVcJORhz+pv3TX2+U6w="
	crossorigin="anonymous"></script>
<script>
	$(document).ready(function() {
		// 		$($("#a")[0]).text("First span");
		// 		$($("#a")[1]).text("Second span");
		// 		$($("span[name='a']")[0]).text("First span");
		// 		$($("span[name='a']")[1]).text("Second span");
		// 		$($(".demo")[0]).text("First span");
		// 		$($(".demo")[1]).text("Second span");

		// 		var a = $($("select[name='select01']")[1]);
		// 		console.log(a.prop("title"));
		// 		a.empty();

		// 		var a = $("select[name='select01']");
		// 		$.each (a, function (index, b) {
		// 			$(b).empty();
		// 		});
		// 		$(a[1]).empty();

		// 		if (",".indexOf(",") > -1) {
		// 			console.log("y");
		// 		} else {
		// 			console.log("n");
		// 		}
		// 		$("#select01 option:eq(1)").prop('selected', true);
		// 		$("#select01").empty();

		// 		var a = [];
		// 		a[0] = "0";
		// 		a[1] = "1";
		// 		a[2] = "2";
		// 		console.log(a.length);
		// 		console.log(a);

		// 		a.splice(1, 1);
		// 		console.log(a.length);
		// 		console.log(a);

		// 		for (i = a.length - 1; i >= 0; i--) {
		// 			if (a[i] == null) {
		// 				a.splice(i, 1);
		// 			}
		// 		}
		// 		console.log(a.length);
		// 		console.log(a);

		// 		$("#select02").prop("disabled", true);

		// 		$("#input01").keydown(function() {
		// 			console.log("keydown");
		// 		});

		// 		$("#input01").keyup(function() {
		// // 			console.log("keyup");
		// 	        $(this).data("old", $(this).data("new") || "");
		// 	        $(this).data("new", $(this).val());
		// 	        console.log($(this).data("old"));
		// 	        console.log($(this).data("new"));
		// 		});

		// 		$("#input01").keypress(function() {
		// 			console.log("keypress");

		// 		$("#input01").change(function() {
		// 			console.log("change");
		// 	        $(this).data("old", $(this).data("new") || "");
		// 	        $(this).data("new", $(this).val());
		// 	        console.log($(this).data("old"));
		// 	        console.log($(this).data("new"));
		// 		});

		// 		$("#input01").on("change", function() {
		// 			console.log("on");
		// 		});
	});
</script>
</head>

<body>
	<!-- 	<div> -->
	<!-- 		<span id="a" name="a" class="demo">1</span> <span id="a" name="a" class="demo">2</span> <span>3</span> -->
	<!-- 	</div> -->

	<!-- 	<select id="select01" name="select01" title="select01"> -->
	<!-- 		<option value=""></option> -->
	<!-- 		<option value="1">a</option> -->
	<!-- 		<option value="2">b</option> -->
	<!-- 	</select> -->
	<!-- 	<select id="select02" name="select01" title="select02"> -->
	<!-- 		<option value=""></option> -->
	<!-- 		<option value="3">c</option> -->
	<!-- 		<option value="4">d</option> -->
	<!-- 	</select> -->
	<!-- 	<input id="input01" name="select01" title="select02" value="1" /> -->

	<form method="post" action="http://localhost:8050/sample/insert_json.do" enctype="multipart/form-data">
		<input type="hidden" name="name" value="s">
		<input type="hidden" name="test_id" value="3">
		<input type="hidden" name="reg_id" value="1">
		<input type="hidden" name="mod_id" value="1">
		<input type="file" name="file">
		<input type="file" name="file">
		<input type="file" name="skoh">
		<input type="submit">
	</form>
</body>
</html>