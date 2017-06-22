<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		$("#version").text("프로그램 : 1.0, XML : " + g_config_value.buildtime.package.version["text_"]);
		
		$("#log-level").val(g_config_value.runtime["log-level"]["text_"]);
		
		$('#save').click(function(){
			event.preventDefault();
			var data = getFormData($('#form'));
			
			if(!confirm('입력하신 정보로 저장하시겠습니까?'))
				return false;
			
			data.config_uid = "${g_server_config.config_uid}";
			
			g_config_value.runtime["log-level"]["text_"] = data["log-level"];
			data.danger_config = JSON.stringify(g_config_value);
			
			var url = wwmsUrl + '/server_config/updates.do' ; 
			var method = 'PUT';
			var headerMap = "";
			var paramMap = [ data ];
			doRestFulApi(url, headerMap, method, paramMap, function(result) {
				if(result.msg === 'SUCCESS') {
					alert("저장되었습니다.");
					location.reload();
				}
			});
		});
	});
</script>
				<div class="form-wrap clearfix pb60">
					<form id="form" name="form" action="#" class="clearfix">
						<div class="group-box display-table">
							<div class="form-group first-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">버전<span></span></label>
								</div>
								<div class="table-cell">
									<label id="version" />
								</div>
							</div>
							<div class="form-group first-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">로그 레벨<span></span></label>
								</div>
								<div class="table-cell">
									<select class="sel" id="log-level" name="log-level">
										<option value="warn">WARN</option>
										<option value="info">INFO</option>
										<option value="debug">DEBUG</option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-actions text-right">
							<button type="button" class="btn" id='save' >저장</button>
						</div>
					</form>
				</div>