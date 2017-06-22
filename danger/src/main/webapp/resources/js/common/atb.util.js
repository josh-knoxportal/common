
/* */
function setSessionStorage(code, data){
	if(!!window['sessionStorage']) {
		window.sessionStorage[code] = data;
	}
}

/* */
function getSessionStorage(code){
	if(!!window['sessionStorage']) {
		return window.sessionStorage[code];
	}
	return null;
}

/* */
function setLocalStorage(code, data){
	if(!!window['localStorage']) {
		window.localStorage.setItem(code,data);
	}
}

/* */
function getLocalStorage(code){
	if(!!window['localStorage']) {
		return window.localStorage.getItem(code);
	}
	return null;
}




/* */
var/*const*/ _GKEY_FACTORY_ZONE_UID  = 'fz_uid';
var/*const*/ _GKEY_FACTORY_ZONE_TYPE = 'fz_type';

/*
 * factory_uid or zone_uid or ''
 */
function globalFactoryZoneUid(uidNullable) {
	 if (!arguments.length) { 
		 return getLocalStorage(_GKEY_FACTORY_ZONE_UID); 
	 }
	 
	 setLocalStorage(_GKEY_FACTORY_ZONE_UID,uidNullable);
}

/*
 * "all", "factory", "zone"
 */
function globalFactoryZoneType(typeAllOrFactoryOrZone) {
	 if (!arguments.length) { 
		 return getLocalStorage(_GKEY_FACTORY_ZONE_TYPE); 
	 }
	 
	 setLocalStorage(_GKEY_FACTORY_ZONE_TYPE,typeAllOrFactoryOrZone);
}

/*
 * device "=" replace All
 */
function deviceNoRelaceAll(deviceNo) {
	var str ="";
	str = deviceNo.replace(/-/gi,"");
	
	return str;
}

/*
 * email validation check
 */
function emailValidation(email) {
	var regExp = /[0-9a-zA-Z][_0-9a-zA-Z-]*@[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+){1,2}$/;
	// 입력을 안했으면
	if (email.lenght == 0) {
		return false;
	}
	// 이메일 형식에 맞지않으면
	if (!email.match(regExp)) {
		return false;
	}
	return true;
}

/*
 * password validation check 
 */
function check_password_rule(pw) {
	var pattern = /^(?=.*[a-zA-Z])(?=.*[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"])(?=.*[0-9]).{8,32}$/;
	var blank_pattern = /[\s]/g;
	
	if (pw.length < 8 || pw.length > 14) {
		alertMsg("새 비밀번호는 8~14자리<br> 이내로 입력해주세요.");
		return false;
	}
	
	if( blank_pattern.test(pw) === true){
		alertMsg("비밀번호는 공백없이 입력해주세요.");
		return false;
	}
	
	if (!pattern.test(pw)) {
		alertMsg("새 비밀번호는<br> 8~14자리 이내로<br>영문,숫자,특수문자를<br>조합하여 입력해주세요.");
		return false;
	}
	return true;
}

function blank_pattern(str) {
	var blank_pattern = /[\s]/g;
	
	if (blank_pattern.test(str) === true) {
		return false;
	}

	return true;
}

/*
 * 전체 체크박스 
 */
function allCheckBox(allcheckId, checkListName) {
	var checkBoxList = $("input[name="+checkListName+"]");
	
	if ($('#'+allcheckId).prop('checked')==false) {
		for(i=0; i<checkBoxList.length; i++){
			checkBoxList[i].checked= false;
		}
   }else{
	   for(i=0; i<checkBoxList.length; i++){
		   if(!$(checkBoxList[i]).prop("disabled")){
			   checkBoxList[i].checked= true;
		   }
		}
   }
}

var calByte = {
		getByteLength : function(s) {

			if (s == null || s.length == 0) {
				return 0;
			}
			var size = 0;

			for ( var i = 0; i < s.length; i++) {
				size += this.charByteSize(s.charAt(i));
			}

			return size;
		},
			
		cutByteLength : function(s, len) {

			if (s == null || s.length == 0) {
				return 0;
			}
			var size = 0;
			var rIndex = s.length;

			for ( var i = 0; i < s.length; i++) {
				size += this.charByteSize(s.charAt(i));
				if( size == len ) {
					rIndex = i + 1;
					break;
				} else if( size > len ) {
					rIndex = i;
					break;
				}
			}

			return s.substring(0, rIndex);
		},

		charByteSize : function(ch) {

			if (ch == null || ch.length == 0) {
				return 0;
			}

			var charCode = ch.charCodeAt(0);

			if (charCode <= 0x00007F) {
				return 1;
			} else if (charCode <= 0x0007FF) {
				return 2;
			} else if (charCode <= 0x00FFFF) {
				return 3;
			} else {
				return 4;
			}
		}
	};

	function viewDisplay(obj) {
		$(".viewByte").html( calByte.getByteLength( obj.value ) );
		$(".view20Byte").html( calByte.cutByteLength( obj.value, 20 ) );
	}
	

function check_searchword(str) {

	var blank_pattern = /[\s]/g;
	if (blank_pattern.test(str) == true) {
//		alert(' 공백은 사용할 수 없습니다. ');
		return false;
	}

//	var special_pattern = /[`~!@<>#$%^&*|\\\'\";:\/?]/gi;
	var special_pattern = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
	if (special_pattern.test(str) == true) {
//		alert('기호, 특수문자는 검색어로 사용할 수 없습니다.');
		return false;
	}
	
	return true;
}	