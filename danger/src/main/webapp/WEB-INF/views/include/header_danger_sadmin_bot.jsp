<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
					<h3 class="blind_position">전체메뉴</h3>
					<ul id="gnb" class="gnb2 clearfix">
						<li>
							<a href="<spring:url value="/sadmin/worker.do"/>">작업자/작업 관리</a>
						</li>
						<li>
							<a href="<spring:url value="/sadmin/restrict_area.do"/>">위험지역 관리</a>
						</li>
						<li>
							<a href="<spring:url value="/sadmin/device.do"/>">단말 관리</a>
						</li>
						<li>
							<a href="<spring:url value="/sadmin/beacon.do"/>">비콘 관리</a>
						</li>
						<li class="last-gnb">
							<a href="<spring:url value="/sadmin/userAccount.do"/>">사용자계정 관리</a>
						</li>
					</ul>