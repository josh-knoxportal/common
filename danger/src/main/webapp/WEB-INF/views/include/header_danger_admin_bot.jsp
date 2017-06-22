<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
					<h3 class="blind_position">전체메뉴</h3>
					<ul id="gnb" class="gnb2 clearfix">
					<c:if test="${g_access_config}">
						<li>
							<a href="<spring:url value="/admin/worker_status_map.do"/>">작업자 현장투입현황</a>
						</li>
					</c:if>
						<li>
							<a href="<spring:url value="/admin/work_status_map.do"/>">작업 등록현황</a>
						</li>
						<li>
							<a href="<spring:url value="/admin/workStatus.do"/>">작업자별 현황</a>
						</li>
						<li>
							<a href="<spring:url value="/admin/notice.do"/>">긴급 알림 전송</a>
						</li>
						<li class="last-gnb">
							<a href="<spring:url value="/admin/worker.do"/>">작업자/작업 관리</a>
						</li>
					</ul>
					
<!--  ----------------------------------------------------------------------  -->
<!-- 작업자 연락처 팝업 시작 -->
<!--  ----------------------------------------------------------------------  -->

<div class="worker_contact_box" style="z-index:99999">
	<div class="contact_header">
		<h2>작업자 연락처</h2>
		<p class="last_modi_time"></p>
		<div class="worker_list">
			<table id="table_worker_contact">
			</table>
		</div>
	</div>
	<div class="worker_alarm_box">
		<span class="tit">알람 내용</span>
		<textarea id='worker_alarm_msg'  placeholder="위험지역에 진입하셨습니다.안전을 위해 즉시 벗어나주시기 바랍니다."></textarea>
		<button id="btn_push_message" class="btn full">전송</button>
	</div>
	<a href="#;" class="worker_close"><img src="<c:url value="/resources/img"/>/img_modal_close2.png" alt="닫기"></a>
</div>

<!-- 비인가자 출입 알림 -->
<div id="dialog_restricted_zone" class="modal">
	<div class="modal_header">
		<span>알림</span>
		<a href="#;" class="modal_close"><img src="<c:url value="/resources/img"/>/img_modal_close.png" alt="닫기"></a>
	</div>
	<div class="modal_body">
		<div class="car_loc_info">
			<p id="dialog_restricted_date"></p>
		</div>
		<div class="car_number">
			<span id="dialog_restricted_msg"></span>
			<p>비인가 작업자 진입</p>
		</div>
		<div class="modal_btn">
			<a href="#;" class="modal_confirm"><img src="<c:url value="/resources/img"/>/btn_confirm.png" alt="확인"></a>
			<a href="#;" class="modal_close"><img src="<c:url value="/resources/img"/>/btn_close.png" alt="닫기"></a>
		</div>
	</div>
</div>
<!-- // 알림 -->

<!-- script -->
<script type="text/javascript">
$(document).ready(function() {
	
	$('#dialog_restricted_zone').css({position:'absolute'}).css({
		left: ($(window).width() - $('#dialog_restricted_zone').outerWidth())/2,
		top: ($(window).height() - $('#dialog_restricted_zone').outerHeight())/2 + 100
	});	
	
	$(".worker_close").click(function(e){
		e.preventDefault();
		$(".worker_contact_box").hide();
	});
	
	$("#dialog_restricted_zone .modal_close").click(function(e){
		e.preventDefault();
		$("#dialog_restricted_zone").hide();
	});
	
	$("#dialog_restricted_zone .modal_confirm").click(function(e){
		e.preventDefault();
		popupContactsOnFactoryZone(arr_Contact['type'], arr_Contact['zone_id']);
	});	
	
	$("#btn_push_message").click(function(e) {
		e.preventDefault();
		restricted_push_send();
	});
	
	setTimeout(restrictedRefreshZoneStatus,5000);
});

/* [2016/05/28] */
var arr_Contact = { 'type' : '' , 'uid'  : '', 'time' : 0, 'zone_id' : ''};

function restrictedRefreshZoneStatus() {
	var zone_type = globalFactoryZoneType();
	var zone_uid = globalFactoryZoneUid();
	
	if(zone_uid != null && zone_uid != ""){
		if(arr_Contact['zone_id'] != zone_uid || arr_Contact['type'] != zone_type){
			$('#dialog_restricted_zone').hide();
		}
	}

	/*
	if(arr_Contact['uid'] != zone_uid || arr_Contact['type'] != zone_type){
		$('#dialog_restricted_zone').hide();
	}
	*/
		
	if($('#dialog_restricted_zone').css("display") !== 'none'){
		setTimeout(restrictedRefreshZoneStatus,5000);
		return;
	}
		
	try {
		var url 		= wwmsUrl + '/restricted_check.do'; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken,g_requestType);
		
		var comp_zone_id = '';
		
		doRestFulApi( url, headerMap, methodType.GET, null, 
			function (result) {
				var showCnt = 0 ;
				$.each(result.restrictedList, function(i,val){
					
					if(val.zone_name == null)
						return true;
					
					//존 검색된 것만 보이도록
					if(zone_uid != null && zone_uid != "" && zone_type != "all"){
						comp_zone_id = (val.type == 'gps' ? val.zone_id : val.beacon_zone_id); 
						zone_type = (zone_type == 'factory' ? 'gps' : 'beacon');
						if(zone_uid != comp_zone_id || zone_type != val.type){
							return true;
						}
					}
					
// 					if(arr_Contact['uid'] == val.work_uid && arr_Contact['time'] == val.time){
// 						//pass
// 					} else {
 						showCnt++;				
						
						// arr_restricted_zone.push(key);
// 						아래줄은 주석처리, 5초마다 팝업창이 뜬다. 
//                      주석 해제시 닫기 버튼을 누르면 같은 메세지는 한번만 띄우고 위의 if 조건에 걸려 팝업화면 않뜬다.
						arr_Contact['type'] = (val.type == 'gps' ? 'factory' : 'zone' );
						arr_Contact['uid'] = val.work_uid;
						arr_Contact['time'] = val.time;
						arr_Contact['zone_id'] =  (val.type == 'gps' ? val.zone_id : val.beacon_zone_id); 
						
						$('#dialog_restricted_zone').show();
						
						var date = new Date();
						$("#dialog_restricted_date").html(date.format("yyyy-MM-dd HH:mm:ss"));
						$('#dialog_restricted_msg').html(val.zone_name+' 공정구역에');
						return false;
// 					}					
				});
				
				if(showCnt > 0 ){
					$('#dialog_restricted_zone').show();
					beep();
					beep();
					beep();
					beep();
				}
	
			}, function(result){
				$('#dialog_restricted_zone').hide();
			}
			
		);		
	} catch (e) {}
	
	/* 이 요청을 어떻게 봐야하나. */	
   setTimeout(restrictedRefreshZoneStatus,5000);
}


function restricted_push_send(){

	var phoneList = [] ;
	if($('#unauth_check').is(":checked")){
		$('#table_worker_contact .unauth').each(function() {
			phoneList.push($(this).html());
		});
	}
	
	if($('#auth_check').is(":checked")){
		$('#table_worker_contact .auth').each(function() {
			phoneList.push($(this).html());
		});
	}
	
	if(phoneList.length == 0){
		alertMsg('알림 대상자를 선택하세요.');
		return;
	}
	
	if($('#worker_alarm_msg').val() == ''){
		$('#worker_alarm_msg').val("위험지역에 진입하셨습니다.안전을 위해 즉시 벗어나주시기 바랍니다.");
	}
	
	try {
		var url 		= wwmsUrl + '/restricted_check.do'; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken,g_requestType);
		var paramMap = {};
		paramMap['phoneList'] = phoneList;
		paramMap['msg'] = $('#worker_alarm_msg').val();
		paramMap['user_id'] = 'danger';
		
		doRestFulApi(wwmsUrl + '/restricted_push.do', headerMap, methodType.POST, paramMap, 
			function (result) {
				alertMsg('알림을 성공적으로 <br/>전송했습니다.');
				$('#dialog_restricted_zone').hide();
				$(".worker_contact_box").hide();
			}
		);		
	} catch (e) {}
}


/* 
 * type : factory or zone
 * uid  : factory_uid or zone_uid
 */
function popupContactsOnFactoryZone(type, uid) {
	var url = wwmsUrl + '/contacts_on/'+type+'/'+uid+'.do' ; 
	var method = 'GET';
	var headerMap = {};
	var paramMap = null;

	$(".worker_contact_box").hide();
	
	doRestFulApi(url, headerMap, method, paramMap, 
		function (result) {
		
			$(".last_modi_time").html(" 최종 업데이트 시간 : " + new Date().format("yyyy-MM-dd HH:mm:ss") );

			var $table = $('#table_worker_contact');
			var size = 0;
			
			$table.html('');
			try { size = result.workerContactList.length; } catch(e) {}
			
			if (size > 0) {
				var unauthHtml = '';
				unauthHtml += '<tr>';
				unauthHtml += '<td class="worker_tp1 align_l">비인가 작업자</td>';
				unauthHtml += '<td class="worker_tp1 align_r"><input type="checkbox" id="unauth_check"></td>';
				unauthHtml += '</tr>';
								
				var authHtml = '';
				authHtml += '<tr>';
				authHtml += '<td class="worker_tp2 align_l">인가 작업자</td>';
				authHtml += '<td class="worker_tp2 align_r"><input type="checkbox" id="auth_check"></td>';
				authHtml += '</tr>';
				
				var authCount = 0;
				var unauthCount = 0;
				for(var index = 0; index < size; index++) {
					var contact = result.workerContactList[index];
		
					/* company, name, device_no, push_id, is_authorized */
					if (contact.is_authorized == 1) {
						authCount++;
						/* */
						authHtml += '<tr>';
						authHtml += '<td class="nm">'+contact.name+'</td>';
						authHtml += '<td class="align_c auth">'+contact.device_no+'</td>';
						authHtml += '</tr>';
					} else {
						unauthCount++;
						/* */
						unauthHtml += '<tr>';
						unauthHtml += '<td class="nm">'+contact.name+'</td>';
						unauthHtml += '<td class="align_c unauth">'+contact.device_no+'</td>';
						unauthHtml += '</tr>';
					}
				}
				
				if (authCount > 0 && unauthCount > 0) {				
					$table.html(unauthHtml+authHtml);
				} else if (authCount > 0) {
					$table.html(authHtml);
				} else if (unauthCount > 0) {
					$table.html(unauthHtml);
				} else {
					$(".worker_contact_box").hide();
					alertMsg("요청하신 연락처가 존재하지 않습니다.");
					return ;
				}
			
				$(".worker_contact_box").show();
			} else {
				alertMsg("요청하신 연락처가 존재하지 않습니다.");
			}
		}) ;	
}

</script>

<!--  ----------------------------------------------------------------------  -->
<!-- 작업자 연락처 팝업 종료 -->
<!--  ----------------------------------------------------------------------  -->					