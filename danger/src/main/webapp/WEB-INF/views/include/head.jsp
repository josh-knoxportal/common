<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
	<!--[if lt IE 9]>
	<script src="c:url value="/resources/js"/>/html5shiv.min.js" type="text/javascript"></script>
	<![endif]-->
	<link href="<c:url value="/resources/css"/>/base.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources/css"/>/main.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources/css"/>/ui-style.css" rel="stylesheet" type="text/css">
	<link href="<c:url value="/resources/css"/>/bp-style.css" rel="stylesheet" type="text/css">
	
	<script src="<c:url value="/resources/js"/>/jquery-1.11.3.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		var contextPath 	= "${pageContext.request.contextPath}";
		var resourcePath	= contextPath +'/' + 'resources';
		var language 		= '<c:out value="${pageContext.request.locale.language}" />';
		var ngmsUrl 		= contextPath + '/v1/ngms';
		var wwmsUrl 		= contextPath + '/v1/wwms';
		var vehicleAdminUrl = contextPath +'/vehicle/admin';
		var g_accessToken = "${access_token}";
		var g_requestType = "2";
		var g_config_type = "danger";
		var g_config_value = JSON.parse('${g_server_config.danger_config}');
		
		$(document).ready(function(){
			var pathname = location.pathname;
			$('#gnb li').each(function(){
				var menu_url = $(this).children().attr('href');
				if(pathname == menu_url){
				    $(this).addClass("target");
				}
				
				// 통합로그인인 경우 admin / sadmin 
				if(pathname === "/danger/login/portalLogin.do") {
					if(menu_url === "/danger/sadmin/restrict_area.do" || menu_url === "/danger/admin/worker_status_map.do") {
						$(this).addClass("target");
					}		
				}
			});
			
			$('#logout-btn').click(function(){
				event.preventDefault();
				
				$(location).attr("href", contextPath + "/1/logout.do");
			});
			
			$('#info-btn').click(function(){
				event.preventDefault();
				var admin_type
				if(${sessionScope.account.is_admin} == '0'){
					admin_type = "admin";
				}else{
					admin_type = "sadmin";
				}
				$(location).attr("href",contextPath + "/" + admin_type+"/accountInfo.do");
			});
			
			$('#config-btn').click(function(){
				event.preventDefault();
				var admin_type
				if(${sessionScope.account.is_admin} == '0'){
					admin_type = "admin";
				}else{
					admin_type = "sadmin";
				}
				$(location).attr("href",contextPath + "/" + admin_type+"/serverConfig.do");
			});
			
			var admin_type;
			if(${sessionScope.account.is_admin} == '0'){
				admin_type = "admin";
			}else{
				admin_type = "sadmin";
			}
// 			console.log("/danger/"+admin_type+"/accountInfo.do");
			
			$('#dialog_alert_modal').css({position:'absolute'}).css({
		         left: ($(window).width() - $('#dialog_alert_modal').outerWidth())/2,
		         top: ($(window).height() - $('#dialog_alert_modal').outerHeight())/2
		     });
			
			$("#dialog_alert_modal .modal_close").click(function(e){
				e.preventDefault();
				$("#dialog_alert_msg").html('');
				$("#dialog_alert_modal").hide();
			});
		});
		
		function loading_process(type) {
			var height = screen.availHeight;             //해상도 높이
            var scrollHeight = height - document.body.scrollHeight;      //해상도 높이 – 스크롤이 포함된 문서 높이 = 스크롤 높이
           height = height - scrollHeight;   
           document.getElementById("processBar").style.top = height/2+"px";
			var process_table_length =  $('#atb_processing_data').length ;
			if(process_table_length > 0 ){
				if(type =='hide')
					$('#atb_processing_data').hide();
				else
					$('#atb_processing_data').show();
			}
		}
	</script>
	
	<script src="<c:url value="/resources/js"/>/common/atb.action.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js"/>/common/atb.ui.js" type="text/javascript"></script>
	<script src="<c:url value="/resources/js"/>/common/atb.datatable.js" type="text/javascript"></script>
	<!-- [2016/06/02] added by capsy -->
	<script src="<c:url value="/resources/js"/>/common/atb.util.js" type="text/javascript"></script>
	