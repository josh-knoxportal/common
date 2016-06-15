<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Jsonp test with jquery</title>

<script src="http://code.jquery.com/jquery-2.1.1.js" integrity="sha256-FA/0OOqu3gRvHOuidXnRbcmAWVcJORhz+pv3TX2+U6w="
	crossorigin="anonymous"></script>
<script>
	$(document).ready(function() {
// 		if (",".indexOf(",") > -1) {
// 			console.log("y");
// 		} else {
// 			console.log("n");
// 		}
		$("#select01 option:eq(1)").prop('selected', true);
	});
</script>
</head>

<body>
<select id="select01">
	<option value=""></option>
	<option value="1">1</option>
	<option value="2">2</option>
</select>
</body>
</html>