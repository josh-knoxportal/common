<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
					<h3 class="blind_position">전체메뉴</h3>
					<ul id="gnb" class="clearfix">
						<li>
							<a href="<spring:url value="/vehicle/admin/vehicle_driving.do"/>">차량운행 현황</a>
						</li>
						<li>
							<a href="<spring:url value="/vehicle/admin/history_vehicles.do"/>">차량운행이력 조회</a>
						</li>
						<li>
							<a href="<spring:url value="/vehicle/admin/driving_rule.do"/>">출입제한도로 설정</a>
						</li>
						<li>
							<a href="<spring:url value="/vehicle/admin/speed_limit.do"/>">제한속도 설정</a>
						</li>
						<li>
							<a href="<spring:url value="/vehicle/admin/device.do"/>">등록 차량 관리</a>
						</li>
						<li class="last-gnb">
							<a href="<spring:url value="/vehicle/admin/account.do"/>">계정관리</a>
						</li>
					</ul>
