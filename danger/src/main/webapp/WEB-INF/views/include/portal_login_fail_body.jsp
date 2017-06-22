<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
	<div id="error_wrap">
		<div id="error_container">
			<div id="error_icon">
				<img src="<c:url value="/resources"/>/img/icon_warning.png" alt="">
			</div>
			<div id="error_contents">
				<p class="title">
					통합 로그인이 실패하였습니다.
				</p>
				<p class="contents">
					방문하시려는 페이지의 주소가 잘못 입력되었거나,<br />페이지의 주소가 변경 혹은 삭제되어 요청하신 페이지를 찾을 수
					없습니다. <br />입력하신 주소가 정확한지 다시 한번 확인해 주시기 바랍니다. <br />
					<a id="backBtn" href="#;">[뒤로가기]</a>
				</p>
			</div>
		</div>
	</div>