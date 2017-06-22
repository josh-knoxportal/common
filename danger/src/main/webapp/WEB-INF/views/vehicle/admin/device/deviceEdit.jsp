<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		
		init();
	});//jquery 끝
	
	function init(){
		var url = '/v1/ngms/device/1.do' ; 
		var method = 'GET';
		var headerMap = {"access_token" : "1234test", "request_type" : "2"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, paramMap, function(result){		
			formSetting(result.naviDevice, 'form');
		}) ;
		
		$('#edit').click(function(){
			update();
		});		
	}
	
	function update(){
		var data = getFormData($('#form'));		
		var url = '/v1/ngms/device.do' ; 
		var method = 'POST';
		var headerMap = {"access_token" : "1234test", "request_type" : "2","opcode" : "update"};
		var paramMap = null;	
		doRestFulApi(url, headerMap, method, data, function(result){
// 			console.log(result);
		}) ;
	}
</script>
				<div class="notice-essential">
					<p><span>* </span> 표시는 필수 입력항목입니다.</p>
				</div>
				<div class="form-wrap clearfix pb60">
					<h5 class="blind_position">단말기 관리 정보를 입력해주세요.</h5>
					<form id='form' action="#" class="clearfix">
						<input type="hidden" id="device_uid" name="device_uid" value="1" />
						<div class="group-box display-table table-4-col">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">단말기 번호 <span>*</span></label>
								</div>
								<div class="table-cell table-2-cell">
									<div class="input-box">
										<input class="form-input" type="text" name="device_no" id="device_no"/>
									</div>
								</div>
								<div class="table-cell title-cell">
									<label class="control-label">기종 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box">
										<input class="form-input" type="text" name="model" id="model"/>
									</div>
								</div>
							</div>
						</div>
						<div class="form-actions text-right">
							<button type="button" class="btn mr5">목 록</button>
							<button type="button" class="btn" id='edit'>확 인</button>
							<span class="modify-btn-box"> <!-- 수정페이지에서 보이게 하세요 -->
								<button type="button" class="btn mr5">수 정</button>
								<button type="button" class="btn">삭 제</button>
							</span>
						</div>
					</form>
				</div>