<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
					<div class="head-contents clearfix"> <!-- head-contents s --> 
						<div class="left-admin">
							<div class="admin clearfix">
								<h3 class="blind_position">사용자 정보</h3>
								<p class="user-id">안녕하세요. <span>${sessionScope.account.name}(${sessionScope.account.user_id})</span>님은 관리자 입니다.</p>
							</div>
							<div class="last-log">
								<ul class="clearfix">
									<li>최종접속일자 : <span>${sessionScope.account.last_login_date}</span></li>
								</ul>
							</div>
						</div>
						<div class="right-admin clearfix">
							<div class="table-cell">
								<button type="button" class="" id='logout-btn'>로그아웃</button>
								<button type="button" class="info-btn" id='info-btn'>사용자 정보</button>
								<button type="button" class="info-btn" id='config-btn'>설정</button>
							</div>
						</div>
					</div> <!-- head-contents e -->
