<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<link href="<c:url value="/resources/css"/>/jquery-ui.css" rel="stylesheet">
<script src="<c:url value="/resources/js"/>/jquery-ui.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/d3/d3.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/transformation-matrix-js/matrix.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/mathjs/math.min.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_core.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/atb.d3ext/atb_svg_extend.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_properties.js" type="text/javascript"></script>
<script src="<c:url value="/resources/js"/>/lg_smartmap_for_ngms.js" type="text/javascript"></script>
<div class="tab-style">
	<ul class="tab-box member-tab clearfix">
	<c:choose>
	<c:when test="${sessionScope.account.is_admin == '0'}">	
		<li>
			<a href="<spring:url value="/admin/worker.do"/>" class="tab-btn"><span class="">작업자 관리</span></a>
		</li>
		<li class="active">
			<a href="<spring:url value="/admin/work.do"/>" class="tab-btn"><span class="">작업 관리</span></a>
		</li>
	</c:when>
	<c:when test="${sessionScope.account.is_admin == '1'}">
		<li>
			<a href="<spring:url value="/sadmin/worker.do"/>" class="tab-btn"><span class="">작업자 관리</span></a>
		</li>
		<li class="active">
			<a href="<spring:url value="/sadmin/work.do"/>" class="tab-btn"><span class="">작업 관리</span></a>
		</li>	
	</c:when>
	</c:choose>
	</ul>
	<%@ include file="/WEB-INF/views/danger/admin/work/workMnt_include.jsp" %>
</div>
<%@ include file="/WEB-INF/views/danger/admin/work/workMnt_include_popup_register.jsp" %>
<%@ include file="/WEB-INF/views/danger/admin/work/workMnt_include_popup_info.jsp" %>
<%@ include file="/WEB-INF/views/danger/admin/work/workMnt_include_popup_modify.jsp" %>
<%@ include file="/WEB-INF/views/danger/admin/work/workMnt_include_popup_complete.jsp" %>