<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--[if lt IE 9]>
<script src="c:url value="/resources/js"/>/html5shiv.min.js" type="text/javascript"></script>
<![endif]-->
<link href="<c:url value="/resources/css"/>/base.css" rel="stylesheet" type="text/css">
<link href="<c:url value="/resources/css"/>/main.css" rel="stylesheet" type="text/css">
<link href="<c:url value="/resources/css"/>/ui-style.css" rel="stylesheet" type="text/css">
<link href="<c:url value="/resources/css"/>/bp-style.css" rel="stylesheet" type="text/css">
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
</head>
<body>
	<%@ include file="/WEB-INF/views/include/portal_login_fail_body.jsp"%>
</body>
<script src="<c:url value="/resources/js"/>/jquery-1.11.3.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#backBtn').click(function() {
			location.replace("/vehicle/admin/login.do");
		});
	});
</script>
</html>