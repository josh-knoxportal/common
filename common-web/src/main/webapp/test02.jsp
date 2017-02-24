<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="//code.jquery.com/jquery-1.12.0.min.js"></script>
<script>
	$(document)
			.ready(
					function() {
						var url = 'http://www.jquery4u.com/scripts/jquery4u-sites.json?callback=?';

						$.ajax({
						   type: 'GET',
						    url: url,
						    async: false,
						    jsonpCallback: 'jsonCallback',
						    contentType: "application/json",
						    dataType: 'jsonp',
						    success: function(json) {
						       console.dir(json.sites);
						    },
						    error: function(e) {
						       console.log(e.message);
						    }
						});
// 						$.ajax({
// 							type : 'GET',
// 							contentType: "application/json",
// 							beforeSend : function(request) {
// 								request
// 										.setRequestHeader(
// 												'access_token',
// 												'79E5F0BA4352C7D23BAF36618DF605E881E458BB0B803BF49E4DA8DE75741BEC23A28F0E795FCFA36FE7F89E573C6C05AB4D5738EA15D8511C4B4301095E5995B1E12ED670A78BC3AA014DECB98F30B2AD3F26BBBF510E9AF5A39D1D094DD1FDC381A7A05DB1C59D11A9B2AAC0231F30FDE8ACABD0A42DD948E997BDC6CB44C4');
// 								request.setRequestHeader('request_type', '1');
// 							},
// 							url : "http://localhost/v1/mms/rssi.do",
// 							dataType : "jsonp",
// 							jsonpCallback : "callback",
// 							crossDomain : true,
// 							success : function(data) {
// 								alert(data);
// 							},
// 							error : function(request, status, error) {
// 								alert("code:" + request.status + "\n" + "message:" + request.responseText
// 										+ "\n" + "error:" + error);
// 							}
// 						});
					});
</script>
</head>
<body>

</body>
</html>