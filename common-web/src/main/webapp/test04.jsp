<%@ page language="java" contentType="text/javascript; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.io.StringWriter"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>

<%@ page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@ page import="org.oh.common.util.Utils"%>

<%
	System.out.println(Utils.toString(request));
	System.out.println(request.getParameter("a"));

	Map<String, String> dummyData = new HashMap<String, String>();
	dummyData.put("value1", "값1");
	dummyData.put("value2", "값2");

	StringWriter sw = new StringWriter();

	// Jackson JSON Mapper 를 사용해서 Map 을 JSON 문자열로 변환
	ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(sw, dummyData);

// 	request.setAttribute("sw", sw);

	if (request.getParameter("callback") == null)
		out.print(sw);
	else
		out.print(request.getParameter("callback") + "(" + sw + ")");
%>

<%-- ajax 에서 넘겨준 callback 함수 파라메터 가져오기 --%>
<%-- ${sw} --%>
<%-- ${param.callback}(${sw}); --%>
