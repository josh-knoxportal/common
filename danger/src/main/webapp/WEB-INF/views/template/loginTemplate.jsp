<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" session="false"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>


<!doctype html>
<!--[if IE 8]> <html lang="ko" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="ko" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="ko"> <!--<![endif]-->
<head>
	<meta charset="UTF-8">
	<meta http-equiv="x-ua-compatible" content="IE=edge, chrome=1">
	<meta charset="utf-8">
	<title>로그인 &lt; Smart Plant 안전관리</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"> 

	<!--[if lt IE 9]>
	<script src="..<c:url value="/resources"/>/js/html5shiv.min.js" type="text/javascript"></script>
	<![endif]-->

	<link href="<c:url value="/resources"/>/css/base.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources"/>/css/main.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources"/>/css/ui-style.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources"/>/css/login.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources"/>/css/bp-style.css" rel="stylesheet" type="text/css">
	
	<script src="<c:url value="/resources"/>/js/jquery-1.11.3.min.js" type="text/javascript"></script>
	
	<script type="text/javascript">
	var contextPath 	= "${pageContext.request.contextPath}";
	var resourcePath	= contextPath +'/' + 'resources';
	var language 		= '<c:out value="${pageContext.request.locale.language}" />';
	var ngmsUrl 		= contextPath + '/v1/ngms';
	var wwmsUrl 		= contextPath + '/v1/wwms';
	var g_accessToken = "${access_token}1234";
	var g_requestType = "2";
	
		$(document).ready(function(){

			/* 아이디 */
			$('.find-id-btn').click(function(){
				resetSearchData($('#id_form input'));	
				$('.find-id-pop').show();
			});
			
			/* 비밀번호 */
			$('.reset-pw-btn').click(function(){
				resetSearchData($('#pw_form input'));
				$('.reset-pw-pop').show();
			});
			
			/* 닫기(X)버튼 */
			$('.close-btn').click(function(){
				$('.prev-box').show();
				$('.next-box').hide();
				$(this).closest(".pop-wrap").hide();
			});
			
			$('.enterClass').keydown(function (event) {
	            if (event.which === 13) { //enter
	            	login();;
	            }
	        });
			
			$('.idEnterClass').keydown(function (event) {
	            if (event.which === 13) { //enter
	            	search_id();
	            }
	        });
			
			$('.pwEnterClass').keydown(function (event) {
	            if (event.which === 13) { //enter
	            	reset_password();
	            }
	        });
			
		});
		
		function loading_process(type) {
			var process_table_length =  $('#atb_processing_data').length ;
			if(process_table_length > 0 ){
				if(type =='hide')
					$('#atb_processing_data').hide();
				else
					$('#atb_processing_data').show();
			}
		}
	</script>
	<script src="<c:url value="/resources"/>/js/common/atb.action.js" type="text/javascript"></script>
	<script src="<c:url value="/resources"/>/js/common/atb.ui.js" type="text/javascript"></script>	
	<script src="<c:url value="/resources"/>/js/common/atb.util.js" type="text/javascript"></script>
</head>
<body class="login">
	<tiles:insertAttribute name="body" />
	
	<div id="atb_processing_data" class="dataTables_processing" style="display:none;">
		<div class="process-in">
			<div class="process">	<img src="<c:url value="/resources"/>/img/ajax-loading.gif"><p>Loading...</p>	</div>
		</div>
	</div>
</body>
</html>
