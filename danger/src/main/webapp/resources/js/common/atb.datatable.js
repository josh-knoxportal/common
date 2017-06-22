/* 
 * $('#tbody_id').appendTable({data:array, key :['a','b','c'], link :{'a':'link','b':'link'}});
 * 
 * */

;(function($){    
	var table_event = 1;
	var edit_type = ['check_box','select_box','radio_btn','link_text','html_id','edit_text'];
	var process_html ='';
	process_html +='<div class="process-in">';
	process_html +='<div class="process">';
	process_html +='	<img src="../resources/img/ajax-loading.gif"><p>Loading...</p>'
	process_html +='	</div>';
	process_html +='</div>';
		
	
	$.fn.tableLoading = function(options) {
		var opts = $.extend({}, $.fn.appendTable.defaults, options);
		var table_id= $(this).attr('id');
		var tbody = $(this);
		//loading bar 만들기
		var process_table_length =  $('#atb_tables_processing_'+table_id).length
		if(process_table_length == 0 ){
			var p_html = data_process(table_id, opts.process_html);
			tbody.parents('table').after(p_html);
		
		}else{
			$('#atb_tables_processing_'+table_id).show();
		}
		
	}
	
	$.fn.appendTable = function(options) {
		
		var opts = $.extend({}, $.fn.appendTable.defaults, options);
		
		// return을 해서 체인으로 사용가능하도록 한다.
		var column = opts.column;
		var link = opts.link;
		var totalCnt = opts.totalCnt;
		var listNo = opts.listCnt*(opts.curPage-1);
		var cdType = opts.cdType;
		var defaultVal = opts.defaultVal;
		var editCols = opts.editCols;
		var table_id= $(this).attr('id');

		var maxTdLength = this.parent().children("thead").children("tr").children("th").length;
		var tdHtml = '';
		var tbody = $(this);
		tbody.children().remove();
		
		/*
		//loading bar 만들기
		var process_table_length =  $('#atb_tables_processing_'+table_id).length
		if(process_table_length == 0 ){
			var p_html = data_process(table_id, opts.process_html);
			tbody.parents('table').after(p_html);
		
		}else{
			$('#atb_tables_processing_'+table_id).show();
		}
		*/
		
		//return;
		
		$.fn.appendTable.defaults['totalCnt'] = totalCnt;
		
		
		if(totalCnt==0){
			tdHtml = '<tr><td colspan="'+column.length+'">검색 결과가 없습니다.</td></tr>';
			tbody.append(tdHtml);
		}else{
			$.each(opts.data, function(i) {
				tdHtml = '<tr>';
				for(var index =0; index < column.length;index++){
					//break
					if(maxTdLength <= index) return;
					
					var link_data = link[index];
					var alinkHtmlS = '';
					var alinkHtmlE = '';
					if(link_data != null){
						
						alinkHtmlS = link_make(link_data, this);
						alinkHtmlE +='</a>'
					}
					if(column[index] == 'totalIndex')
						tdHtml += '<td>'+((totalCnt-listNo)-i)+'</td>';
					else if($.inArray(column[index], edit_type) != -1){	
						var edit = column[index];
						
											
						if(column[index] == 'html_id'){
							//console.log(editCols[index]['html_id']);
							tdHtml += '<td id="td_'+this[editCols[index]['html_id']]+'" data-key="'+this[editCols[index]['key']]+'"></td>';
						}else if(column[index] == 'edit_text'){
							tdHtml += edit_make(column[index], editCols[index], this);
						}else					
							tdHtml += '<td>'+edit_make(column[index], editCols[index], this)+"</td>";
					}
					else{
						//var value = cdTypeByIndex(index,this[column[index]], cdType);
						var value = cdTypeByIndex(this[column[index]], cdType[index]);
						//tdHtml += '<td data-index="'+column[index]+'">'+alinkHtmlS+setDefaultVal(index, value, defaultVal)+alinkHtmlE+'</td>';
						tdHtml += '<td data-index="'+column[index]+'">'+alinkHtmlS+setDefaultVal(value, defaultVal[index])+alinkHtmlE+'</td>';
					}
					//setDefaultVal(column[index],this[column[index]], defaultVal);
				}
				tbody.append(tdHtml+'</tr>');
			});
		}
		
		
		/*
		if(table_event > 1)
			return this;
		*/
		
		this.event = function(opcode,fn){			
			fn.call();
		};
		
		this.paging = function(page_navi, option){
			
			var retMap = {};
			
			var page_opt = $.extend({}, $.fn.appendTable.pageDefaults, option);
			
			var page = '';
			var startTag = page_opt.startTag;
			var endTag = page_opt.endTag;
			
			var first 	= page_opt.first;
			var last	= page_opt.last;
			var pre 	= page_opt.pre;
			var next	= page_opt.next;
			var curPage = page_opt.curPage;
			var totalCnt= page_opt.totalCnt;
			var lstCnt  = page_opt.lstCnt;
			var pageCnt = page_opt.pageCnt;
			var sEvent	= page_opt.serverSideEvent;
			
			var sPage =  Math.ceil(curPage/pageCnt) *  (pageCnt)  - (pageCnt -1)  ;
			var tPage =  Math.ceil(totalCnt/lstCnt);

			if(sPage > pageCnt){
				//구현해야함
				if(first > ''){
					
				}
				if(pre > ''){
					pre = replaceAll(pre, 'href="#;"', 'href="#;" onclick='+sEvent+'('+(sPage-pageCnt)+')');
					//console.log(pre);
				}
			}else {
				pre = '';
			}
			
			if(sPage <= (tPage - pageCnt) ){
				//구현해야함
				if(last > ''){
					
				}
				if(next > ''){
					next = replaceAll(next, 'href="#;"', 'href="#;" onclick='+sEvent+'('+(sPage+pageCnt)+')');
				}
			}else{
				next ='';
			}
			
			var onClass = ""; 
			for(i= sPage ; i < sPage + pageCnt  ; i++){
				if(i > tPage  ){
					break;
				}

				if(i==curPage)
					onClass ='active';
				else
					onClass ='';
				
				page += ' <li class="'+onClass+'"><a href="#;" onclick="'+sEvent+'('+i+')">'+i+'</a></li>';
			}

			$(page_navi).html(startTag+first+pre+page+next+last+endTag);
			
			retMap['cur_page'] = curPage;
			retMap['limit_offset'] = sPage * lstCnt;
			retMap['limit_count'] =  lstCnt;
			
			return retMap;
			
		};
		
		table_event++;
		$('#atb_tables_processing_'+table_id).hide();
		return this;

	}
	
	//function cdTypeByIndex(index, value,  cdType){
	function cdTypeByIndex(value,  cdText){	
		//var cdText = cdType[index.toString()];
		if(cdText != null)
			return cdText[value];
		else
			return value;
	}
	
	
	//function setDefaultVal(index, value, defaultVal){
	function setDefaultVal(value, textVal) {		
		if (value != null && value != '') {
			return value;
		}
			
		if (textVal != null) {
			return textVal;
		}
		
		return "";
	}

	
	function init(){
		
	}
	
	function replaceAll(str, searchStr, replaceStr) {

	    return str.split(searchStr).join(replaceStr);
	}
	
	function eventEttr(event_data, cur_data){
		var attr = ''
		if(event_data['event'] != null){
			var param = event_data['param'];
			var event_param = '' ; 
			$.each( param, function( index, value ) {
				if(index > 0)
					event_param +=',';
				
				event_param += '\''+cur_data[value]+'\'';
			});
			
			attr += ' onclick="'+event_data['event'] +'('+event_param+')"';	
		
		}
		
		return attr;
		
	}
	function link_make(link_data, cur_data){
		
		var alinkHtmlS = '<a href="#;"';
		
		if(link_data['id'] != null)
			alinkHtmlS +=' id="'+link_data['id']+'_'+i+'"';
		if(link_data['class'] != null)
			alinkHtmlS +=' class="'+link_data['class']+'"';
		if(link_data['key'] != null)
			alinkHtmlS +=' data-key="'+cur_data[link_data['key']]+'"';
		
		var attr = eventEttr(link_data, cur_data);
		alinkHtmlS += attr;
		
		alinkHtmlS +='>';
		return alinkHtmlS;
		
	}
	
	function edit_make(type, edit_data, cur_data){
		var html = '';
		var attr = '';
		
		if(edit_data == null || edit_data == undefined){
			alert('edit 컬럼에 값을 잘모 입려하셨습니다.');
			return false;
		}
		switch(type){
			case 'check_box' :
				if(edit_data['key'] != null)
					attr += ' value="'+cur_data[edit_data['key']]+'"';
				
				if(edit_data['name'] != null)
					attr += ' name="'+edit_data['name']+'"';
				
				if(edit_data['data'] != null){
					attr += ' data-'+edit_data['data'][0]+'="'+cur_data[edit_data['data'][1]]+'"';
				}
				
				attr += eventEttr(edit_data, cur_data);
		
				html = '<input type="checkbox" '+attr+'/>';
				
				break;
			case 'select_box' :
				break;
			
			case 'btn_img' :
				break;
				
			case 'text_img' :
				break;
				
			case 'link_text' :
				html = link_make(edit_data,cur_data );
				html += edit_data['text'] + '</a>';
				break;
				
			case 'html_id' :				
				html = '<td';
				if(edit_data['html_id'] != null)					
					html += ' id="td_'+cur_data[edit_data['html_id']]+'" ';
				
				if(edit_data['key'] != null)					
					html += ' data-key="td_'+cur_data[edit_data['key']]+'" ';
				
				if(edit_data['key'] != null)					
					html += ' class="td_'+cur_data[edit_data['class']]+'" ';
				
				
				html = '><td>';				
				
				break;
			case 'edit_text' :				
				html = '<td';
				if(edit_data['name'] != null)					
					html += ' name="'+edit_data['name']+'" ';
				
				if(edit_data['data'] != null)					
					html += ' data-'+edit_data['data'][0]+'="'+cur_data[edit_data['data'][1]]+'"';
				
				html += '>';
				
				if(edit_data['value'] != null){
					if(cur_data[edit_data['value']] !=null)
						html += cur_data[edit_data['value']];
					else
						html += "-";
				}	
					
				
				html +=	'</td>';
				
				break;
			default : 
				break;	
		}
		
		//console.log(html);
		return html;
		
	}
	
	function data_process(id, add_html){
		var html = '' ;
		html +='<div id="atb_tables_processing_'+id+'" class="dataTables_processing" style="display: block;">';
		html +=add_html;
		html +='</div>';		
		return html;
	
	}
	
	
	/*
	 td 에는 cursor 적용이 안되네요. 
	function cursor(options){
		var cursor = options.cursor;
		for(var index =0; index < cursor.length;index++){
			$(".css_"+cursor[index]).css( "cursor","hand" );
		}
	
	} 
	*/
	
	
	//  appendTable 플러그인의 기본 옵션들이다.
	$.fn.appendTable.defaults = {
		  data :[]	
		 ,totalCnt : 0
		 ,listCnt : 20
		 ,curPage : 1
		 ,column :[]
		 ,link   : {} 
		, cdType : {}
		,defaultVal : {}
		,editCols : {}
		,process_html : process_html
	};
	
	//  appendTable 플러그인의 페이징 기본 옵션들이다.
	$.fn.appendTable.pageDefaults = {
			 startTag : '<ul class="pagination">'
			,endTag : '</ul>'
			,first : ''
			,last  : ''
			,pre  : '<li class="previous"><a href="#;">이전</a></li>'
			,next : '<li class="next"><a href="#;">다음</a></li>'
			,onClass : 'active'
			,curPage : 1
			,totalCnt: 0
			,lstCnt : 20
			,pageCnt : 10
			,serverSideEvent : 'list'
	};
 
})(jQuery);