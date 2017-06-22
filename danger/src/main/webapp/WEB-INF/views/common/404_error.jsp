<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
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
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">

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
				<p class="title">
					서비스 이용에 불편을 드려 죄송합니다.<br />요청하신 페이지를 찾을 수 없습니다.
				</p>
				<p class="contents">
					방문하시려는 페이지의 주소가 잘못 입력되었거나,<br />페이지의 주소가 변경 혹은 삭제되어 요청하신 페이지를 찾을 수
					없습니다. <br />입력하신 주소가 정확한지 다시 한번 확인해 주시기 바랍니다. <br />
					<a id="backBtn" href="#;">[뒤로가기]</a>
				</p>
			</div>
		</div>
	</div>
</body>
</html>