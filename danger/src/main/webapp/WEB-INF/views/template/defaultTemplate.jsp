<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!doctype html>
<!--[if IE 8]> <html lang="ko" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="ko" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko"> <!--<![endif]-->

<head>
	<meta charset="UTF-8">
	<meta http-equiv="x-ua-compatible" content="IE=edge, chrome=1">

	<title>Smart Plant 안전관리::${pageTitle}</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"> 
	<tiles:insertAttribute name="head" />
	<c:if test="${!empty(add_js)}">
		<tiles:insertAttribute name="head_add_js" />
	</c:if>
</head>

<body>
<div id="wrap" class=""><!-- wrap s --> <!-- super-login 클래스 추가시 슈퍼관리자버튼 노출됨-->
		<div class="wrap-in"> <!-- login-wrap s -->
			<div id="header" class="clearfix">
				<div class="header-top clearfix">
					<tiles:insertAttribute name="header_top_logo" />
					<tiles:insertAttribute name="header_top" />
				</div>
				<div class="header-bot">
					<tiles:insertAttribute name="header_bot" />
				</div>
			</div>
			<div id="contents"> <!-- contents s -->
				<div class="target-nav clearfix">
					<h4 class="target-title">${pageTitle}</h4>
					<ul class="clearfix">
						<li class="target-home"><a href="#">HOME</a></li> <!-- 해당페이지 주소(href) 넣어주세요! -->
						<li class="target-arrow"></li>
						<li class="target-active"><a href="#">${pageTitle}</a></li>
					</ul>
				</div>
				
				<tiles:insertAttribute name="contents" />
				
				
			</div> <!-- contents e -->
		</div> <!-- login-wrap e -->
	</div> <!-- wrap e -->
	<div id="footer">
		<tiles:insertAttribute name="footer" />	
	</div>
 
	<div id="atb_processing_data" class="dataTables_processing" style="display:none;">
		<div class="process-in">
			<div class="process" id="processBar">	<img src="<c:url value="/resources/img"/>/ajax-loading.gif"><p>Loading...</p>	</div>
		</div>
	</div>
	
	<!-- 비인가자 출입 알림 -->
	<div id="dialog_alert_modal" class="modal" style="z-index:999999">
		<div class="modal_header">
			<span>알림</span>
			<a href="#;" class="modal_close"><img src="<c:url value="/resources/img"/>/img_modal_close.png" alt="닫기"></a>
		</div>
		<div class="modal_body">
			<div class="car_number">
				<span id="dialog_alert_msg"></span>
			</div>
	
			<div class="modal_btn">
				<a href="#;" class="modal_close"><img src="<c:url value="/resources/img"/>/btn_close.png" alt="닫기"></a>
			</div>
		</div>
	</div>
	<!-- // 알림 -->


</body>
</html>

