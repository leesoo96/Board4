'use strict';

function chkEmptyEle(ele, eleNm){
	if(ele.value == ''){
		alert(eleNm + "(를)을 입력해주세요!");
		ele.focus();
		return true;
	}
	return false;
}