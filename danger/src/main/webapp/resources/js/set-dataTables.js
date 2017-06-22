$(document).ready(function(){
			
	/* 데이터 테이블 초기화 -- 페이지에 있는 전체 테이블 속성이 같게 설정됩니다. */
	$.extend( true, $.fn.dataTable.defaults, {
		"sDom": "<'length-wrap clearfix'><'table-wrap'tr><'form-actions'><'pn-wrap'p l>",
		"paging": true,
		"bInfo": false,
		"bFilter": false,
		"bSort": false, // 오름,내림 정렬
		"bProcessing":true,
		"pagingType": "simple_numbers_no_ellipses",
		"iDisplayLength" : 10,
		"aLengthMenu": [
			[10, 20, 30, -1],
			[10, 20, 30, "All"]
		],
		"oLanguage": {
			"sLengthMenu": "_MENU_ ",
			"sProcessing": "<div class='process-in'><div class='process'><img src='../../resources/img/ajax-loading.gif'><p>Loading...</p></div></div>",
			"oPaginate": {
				"sPrevious":"prev",
				"sNext":"next"
			}
		}
	});

});