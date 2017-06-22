<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<meta charset="UTF-8">
	<title>서울옥션 통합관리자시스템</title>
	<link rel="stylesheet" href="<c:url value="/resources/css/common.css"/>" />
	<script type="text/javascript" src="https://code.jquery.com/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/common/util.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/common/atb.ui.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/common/validator.js"/>"></script>
</head>

<body>
	<tiles:insertAttribute name="body" />
	<script type="text/javascript" src="<c:url value="/resources/js/ui.min.js"/>"></script>
</body>
</html>
