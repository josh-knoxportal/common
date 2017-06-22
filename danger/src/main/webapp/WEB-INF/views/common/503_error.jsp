<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!doctype html>
<!--[if IE 8]> <html lang="ko" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="ko" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko"> <!--<![endif]-->
<head>
	<meta charset="UTF-8">
	<meta http-equiv="x-ua-compatible" content="IE=edge, chrome=1">
	<meta charset="utf-8">
	<title>LG SmartPlnat 안전관리</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"> 
	<!--[if lt IE 9]>
	<script src="../resources/js/html5shiv.min.js" type="text/javascript"></script>
	<![endif]-->
	<link href="<c:url value="/resources"/>/css/base.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources"/>/css/main.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources"/>/css/ui-style.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources"/>/css/bp-style.css" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources"/>/js/jquery-1.11.3.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function(){
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
				<p class="title">서비스를 사용할 수 없습니다.</p>
				<p class="contents">위와 같은 장애를 일으켜서 죄송합니다.<br />과부하 및 서버 문제로 서비스가 불가능한 상태입니다. 
				<br /><br />문의 : IT팀 02-2222-3333
				</p>	
			</div>
		</div>
	</div>
</body>
</html>