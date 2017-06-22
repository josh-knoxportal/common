<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		$('#modify').click(function(){
			event.preventDefault();
			$(location).attr("href", contextPath + "/admin/accountInfoModify.do?account_uid=${account.account_uid}");
		});
	});
</script>
				<div class="form-wrap clearfix pb60">
					<form id="form" name="form" action="#" class="clearfix">
						<div class="group-box display-table">
							<div class="form-group first-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">아이디<span></span></label>
								</div>
								<div class="table-cell">${account.user_id}</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">이름<span></span></label>
								</div>
								<div class="table-cell">${account.name}</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">소속<span></span></label>
								</div>
								<div class="table-cell">${account.department}</div>
							</div>
							<div class="form-group table-row clearfix">
								<div class="table-cell title-cell align-top">
									<label class="control-label">이메일<span></span></label>
								</div>
								<div class="table-cell">${account.email}</div>
							</div>
						</div>

						<div class="form-actions text-right">
							<button type="button" class="btn" id='modify' >정보수정</button>  							
						</div>
					</form>
				</div>