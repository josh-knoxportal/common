<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.net.InetAddress" %>

<HTML>
<HEAD>
    <TITLE>세션 JSP 테스트</TITLE>
</HEAD>
<BODY>
<h1>세션 JSP 테스트</h1>
<%
    Integer ival = (Integer)session.getAttribute("sessiontest.counter");
    if(ival==null) {
        ival = new Integer(1);
    }
    else {
        ival = new Integer(ival.intValue() + 1);
    }
    session.setAttribute("sessiontest.counter", ival);
    out.print(InetAddress.getLocalHost().getHostAddress() + "<br/>");
%>
당신은 이곳을 <b> <%= ival %> </b>번 방문 했습니다.<p>
<p>
<h3>request 객체와 관련된 세션 데이터</h3>
요청된 세션 ID : <%= request.getRequestedSessionId() %><br />
쿠키로 부터 요청된 세션 ID 인가? : <%= request.isRequestedSessionIdFromCookie() %><br />
URL로부터 요청된 세션 ID 인가? : <%= request.isRequestedSessionIdFromURL() %><br />
유효한 세션 ID 인가? : <%= request.isRequestedSessionIdValid() %><br />
</BODY>
</HTML>