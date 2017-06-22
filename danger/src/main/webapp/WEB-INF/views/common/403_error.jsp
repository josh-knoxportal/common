<%@ page language="java" contentType="text/html;charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>

<!doctype html>
<!--[if IE 8]> <html lang="ko" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="ko" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="ko">
<!--<![endif]-->
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=edge, chrome=1">
<meta charset="utf-8">
<title>LG SmartPlnat 안전관리</title>
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<script type="text/javascript">
	$(document).ready(function() {
		$('#backBtn').click(function() {
			location.replace("/danger/admin/login.do");
		});
	});
</script>
</head>
<body>
	<div id="error_wrap">
		<div id="error_container">
			<div id="error_icon">
				<img src="<c:url value="/resources"/>/img/icon_warning.png" alt="">
			</div>
			<div id="error_contents">
				<p class="title">허용되지 않은 접근 입니다.</p>
				<p class="contents">
					허용되지 않은 페이지로 접근하여 접속이 차단되었습니다.<br />접근권한이 필요하신 분은 승인을 받아 접근하시기
					바랍니다. <br />
					<br />문의 : IT팀 02-2222-3333 <br />
					<a id="backBtn" href="#;">[뒤로가기]</a>
				</p>

			</div>
		</div>
	</div>
</body>
</html>