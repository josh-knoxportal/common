<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript">
	var vehicleReg = {};
	var listCnt = 15;
	var curPage = 1;
	
	$(document).ready(function(){
		//loading_process();
		
		$('#search').click(function(){
			initSearch();
		});
		$("#search_filter").keydown(function (event) {
            if (event.which === 13) {    //enter
            	initSearch();
            }
        });
		$('.numbersOnly').keyup(function () { 
			if (this.value != this.value.replace(/[^0-9\.]/g, '')) {
				this.value = this.value.replace(/[^0-9\.]/g, '');
			}
		});	
		init();
	});//jquery 끝
	
	//초기화
	function init(){
		initSearch();
		
		
		/* 등록 */
		$('.mod-pop-btn').click(function(){			
			update();
		//	$('body').css('overflow','hidden');
		});
		
		$('.close-btn').click(function(){
			$(this).data('key','');
			resetSearchData($('#reg_form input'));			
			$(this).closest(".pop-wrap").hide();
		});
		
		//loading_process('hide');
		
	}
	
	function update(){
		vehicleReg['vehicle_no'] = $('#vehicle_no').val();
		
		var opcode      = 'insert';
		if(vehicleReg.is_assigned == '1')
			opcode      = 'update';
		
		var param 		= vehicleReg;
		var url 		= ngmsUrl + '/vehicle_reg.do' ; 
		var method 		= methodType.POST;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType,{"opcode": opcode});
		
		doRestFulApi(url, headerMap, method, param, function(result){
			// console.log(result);
			// 성공 or 실패처리 공통모듈 처리			
			if(result.checkCode == 1){
				alertMsg('차량을 정보를 할당하였습니다.');
				page_list(1);
				$(".reg-pop").hide();
			}else{
				//alertMsg(result.msg);
				$('#vehicle_msg').text(result.msg);
				document.getElementById("vehicle_msg").style.color = 'red';
			}			
			
		}) ;
	}
	
	function release(device_no){
		
		var param 		= {};
		var url 		= ngmsUrl + '/unregister.do'; 
		var method 		= methodType.POST;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType,{"device_no": device_no});
		
		doRestFulApi(url, headerMap, method, param, function(result){
			// 성공 or 실패처리 공통모듈 처리			
			if(result.checkCode == 1){
				alertMsg('차량을 해제하였습니다.');
				page_list(1);
			}else{
				if(result.msg=="SUCCESS"){
					alertMsg("차량이 해제되었습니다.");
				}else{
					alertMsg(result.msg);
				}
					
				page_list(1);
				//$('#vehicle_msg').text(result.msg);
			}			
			
		}) ;
	}
	
	function initSearch(paramObj){
		var param = {}; 
		
		if(paramObj){
			param = paramObj;
		}
		var device_no  = $('#search_filter').val();
		param['search_filter'] = device_no;
		param['record_count_per_page'] = listCnt;
		
		search(param);
	}
	
	//검색메소드
	function search(paramObj){
		//var page, limit_offset, limit_count, search_filter 
		var param 		= getJsonObjToGetParam(paramObj);
		var url 		= ngmsUrl + '/vehicle_reg.do?'+param ; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType); // new requestHeaderObject("1234test","2", {추가적인 파람});
		var paramMap 	= null;		
		
		$('#data').tableLoading({});
		doRestFulApi(url, headerMap, method, paramMap, bindTable) ;		
	}
	
	
	//server side page
	function page_list(page){
		var paramObj = {}
		curPage = page;
		/*
		이렇게 해도 되고. 
		paramObj['limit_offset'] = ( page -1 ) * listCnt;
		paramObj['limit_count'] = listCnt;
		$.extend( paramObj, getSearchParam($('#search_form')) );
		*/
		paramObj['page_index'] = ( page -1 ) * listCnt;
		
		initSearch(paramObj);
		
	}
	
	
	function bindTable(result){
		//return;
		
		var is_assing_cd1 = {0:'해제',1:'사용'};
		var is_assing_cd2 = {0:'등록',1:'해제'};
		var is_out_cd 	  = {0:'운행중',1:'미운행'};
		
		var list = $('#data').appendTable({
				 data		:	result.vehicleRegList
				,totalCnt 	:	result.totalCnt
				,curPage : curPage
				,listCnt : listCnt
				 //,column		:	['totalIndex', 'device_no','vehicle_no','is_assigned','is_assigned','is_out']
				 //,column		:	['totalIndex', 'device_no', 'vehicle_no', 'is_assigned','is_assigned', 'link_text']
				,column		:	['totalIndex', 'device_no', 'vehicle_no', 'is_assigned',  'html_id']
				,link 		:  	{
									 //3:{'class' : 'reg-pop-btn', 'key' : 'device_no', 'event' : 'vehicleRegData', 'param' : ['device_no','vehicle_no']} // index를 지정함
									  3:{'class' : 'reg-pop-btn hoverred', 'key' : 'device_uid'} // index를 지정함
									// ,4:{'class' : 'unreg-pop-btn', 'key' : 'device_no'}
								}
				//,cdType		:   {3 : is_assing_cd1, 4 : is_assing_cd2, 5 :is_out_cd}  // index를 지정함
				,cdType		:   {3 : is_assing_cd1}
				,defaultVal : 	{2 : '없음'}              // index를 지정함
				//,editCols	:	{4 : {'name' : 'chk_nm', 'key' : 'is_assign', 'event' : 'test_event', 'param' : ['device_no','vehicle_no']}}
				//,editCols	:	{4 : {'html_id' :  'device_uid', 'key' : 'is_out', 'class' : 'td_btn' }}
				,editCols	:	{4 : {'html_id' :  'device_uid' }}
				});
		
		list.event('add',function(){vehicleRegPop()});
		list.event('add',function(){makeTableBtn(result.vehicleRegList)});
		
		var page_ret = list.paging('#page_navi',{'totalCnt':$.fn.appendTable.defaults['totalCnt'], 'serverSideEvent' : 'page_list', 'curPage':curPage, 'lstCnt' : listCnt});
		
		
	}
	
	function makeTableBtn(vehicleRegList){
				//console.log(vehicleRegList);
			$.each(vehicleRegList, function( index, value ) {
				//console.log(value['device_uid'])
				var html  ='';
				if(value['is_assigned'] == 1)
					html = '<span class="btn01_disabled">등록</span> <a href="#:" onclick="release(\''+value['device_no']+'\')" class="btn01_off"><span>해제</span></a>';
				else
					html = '<a href="#:" onclick="vehicleRegPopBtn(\''+value['device_uid']+'\')" class="btn01_off"><span>등록</span></a> <span class="btn01_disabled">해제</span> ';
					
				if(value["is_assigned"]==0 && value["is_out"]==0){
					html = '<span class="btn01_disabled">등록</span> <a href="#:" onclick="release(\''+value['device_no']+'\')" class="btn01_off"><span>해제</span></a>';
				}
				
				$('#td_'+value['device_uid']).html(html);
				});
	}
	
	
	function vehicleRegData(device_no, vehicle_no){
		
		$('.reg-pop').show();
		$('#device_no').val(device_no);
		$('#vehicle_no').val(vehicle_no);
	}
	
	
	function vehicleRegPop() {		
		$('.reg-pop-btn').click(function(){
			
			//console.log($(this).data("key"));
			
			var url 		= ngmsUrl + '/vehicle_reg/'+$(this).data("key")+'.do' ; 
			var method 		= methodType.GET;
			var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType); // new requestHeaderObject("1234test","2", {추가적인 파람});
			var paramMap 	= null;			
			doRestFulApi(url, headerMap, method, paramMap, function(result){
				//console.log(result);
				vehicleReg = result.vehicleReg;
				if(vehicleReg.is_out ==  0){
					alertMsg('운행중에 수정이 불가능합니다.');
					return ;
				}
				$('#vehicle_msg').text('');
				$('#vehicle_no').val(vehicleReg.vehicle_no);				
				$('.reg-pop').show();
				
			}) ;	
			
			//console.log($(this).data('key'));
		});
		
		$('.unreg-pop-btn').click(function(){
			var txt = $(this).html();
			if(txt =='해제'){
				//alert($(this).data('key'));
				release($(this).data('key'));
			}
		});
	}
	
	function vehicleRegPopBtn(device_uid) {		
		var url 		= ngmsUrl + '/vehicle_reg/'+device_uid+'.do' ; 
		var method 		= methodType.GET;
		var headerMap 	= new requestHeaderObject(g_accessToken, g_requestType); // new requestHeaderObject("1234test","2", {추가적인 파람});
		var paramMap 	= null;			
		doRestFulApi(url, headerMap, method, paramMap, function(result){
			//console.log(result);
			vehicleReg = result.vehicleReg;
			if(vehicleReg.is_out ==  0){
				alertMsg('운행중에 수정이 불가능합니다.');
				return ;
			}
			$('#vehicle_msg').text('');
			$('#vehicle_no').val(vehicleReg.vehicle_no);				
			$('.reg-pop').show();
		});
	}
</script>
	
				<div class="search-wrap display-table">
					<div class="table-row">
						<div class="table-cell">
							<div class="form-wrap clearfix">
								<h5 class="blind_position">검색어를 입력해 주세요</h5>
								<form action="#" class="clearfix" id="search_form" name="search_form">
									<div class="form-group clearfix">
										<span class="search-text">단말기 번호 검색 :</span><!-- 텍스트로 변경 -->
										<div class="input-box">
											<input class="form-input numbersOnly" type="text" name="search_filter" id="search_filter"/>
										</div>
										<button id="search" type="button" class="btn">검색</button> 
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>

				
				<div class="search_tbl_gap" style="padding:20px 0 0;"></div>
				
				
				<div class="table-list">
					<table class="table-style data-table">
						<colgroup>
							<col width="7%">
							<col width="25%">
							<col width="20%">
							<col width="16%">
							<col width="32%">
							<!-- <col width="16%"> -->
						</colgroup>
						<thead>
							<tr>
								<th>No</th>
								<th>단말기번호</th>
								<th>등록된 차량번호</th>								
								<th>차량등록</th>
								<th>등록해제 여부</th>
								<!-- <th>운행상태</th> -->
							</tr>
						</thead>
						<tbody id="data">							
					
						</tbody>
					</table>
					
				
				</div>
				
							
				
				<!--  pagination -->
				<div class="pn-wrap pn-single" id="page_navi">
				</div>
				<!--  // pagination -->
				
				
				
				
				
				
	<div class="pop-wrap reg-pop"> <!-- reg-pop s -->
		<div class="pop-bg"></div>
		<div class="pop-up"> <!-- pop-up s -->
			<div class="pop-header clearfix">
				<h3 class="pop-title">차량번호 등록</h3>
				<button type="button" class="pop-close close-btn"><img src="<c:url value="/resources/img"/>/pop-close.png" alt="팝업닫기"></button>
			</div>
			<div class="pop-contents"> <!-- pop-contents -->
				<div class="pop-info"></div>

				<div class="form-wrap prev-box clearfix">
					<form action="#" class="clearfix" id="reg_form">
						<div class="group-box display-table">
							<div class="form-group table-row first-group clearfix">
								<div class="table-cell title-cell">
									<label class="control-label">차량번호 <span>*</span></label>
								</div>
								<div class="table-cell">
									<div class="input-box input-box-250">
										<input class="form-input" type="text" name="vehicle_no" id="vehicle_no" maxlength="20"/>
									</div>
								</div>
								<div class="table-cell"><span id="vehicle_msg"></span></div>
							</div>
						</div>
					</form>
				</div>

				<div class="form-wrap last-box clearfix">
					<div class="form-actions text-center">
						<button type="button" class="btn mod-pop-btn">확 인</button> 
					</div>
				</div>

			</div> <!-- pop-contents e -->
		</div> <!-- pop-up e -->
	</div> <!-- reg-pop e -->
				
				



		
					


