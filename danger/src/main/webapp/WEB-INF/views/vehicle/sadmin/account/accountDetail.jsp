<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		init();
	});//jquery 끝
	
	function init(){
		
		$('#modify').click(function(){
			move_update();
			return false;
		});
		
		var url = ngmsUrl + '/adminAccount/${account_uid}.do' ; 
		var method = 'GET';
		var headerMap = {"access_token" : "1234test", "request_type" : "2"};
		var paramMap = null;
		doRestFulApi(url, headerMap, method, paramMap, bindTable) ;
		
		function bindTable(result) {
// 			console.log(result);
			var system_type = {'0':'-', '1': '시스템관리자','2':'일반관리자'};
			var tdHtml = '';
			var tbody = $('#account_tbody');
			tbody.children().remove();
			tdHtml = '<tr>';
			tdHtml += '<td>'+result.adminAccount.user_id+'</td>';
			tdHtml += '<td>'+system_type[result.adminAccount.system_type]+'</td>';
			tdHtml += '<td>'+result.adminAccount.name+'</td>';
			tdHtml += '<td>'+result.adminAccount.email+'</td>';
			tdHtml += '<td>'+result.adminAccount.reg_date+'</td>';
			tbody.append(tdHtml+'</tr>');
		}
		
		function move_update() {
			$(location).attr("href", contextPath + "/vehicle/sadmin/accountModify.do?account_uid=${account_uid}");
		}
	}
</script>
				<div class="table-list">
				<form id='form' >
					<table class="table-style data-table">
						<colgroup>
							<col width="20%">
							<col width="20%">
							<col width="20%">
							<col width="30%">
							<col width="10%">
						</colgroup>
						<thead>
							<tr>
								<th>아이디</th>
								<th>관리권한</th>
								<th>이름</th>
								<th>이메일</th>
								<th>등록일</th>
							</tr>
						</thead>
						<tbody id="account_tbody">
						</tbody>
					</table>
					<div class="form-actions">
						<button id="modify" type="button" class="btn">계정 수정</button>
					</div>
				</form>
				</div>