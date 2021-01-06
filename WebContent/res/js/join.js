function joinConfirm(){
	var joinFrm = document.querySelector('#joinFrm');
	var userId = joinFrm.user_id;
	
	var regExpUserId = /^[A-Za-z0-9+]*$/;
	if(!regExpUserId.test(userId.value)){
		alert('아이디는 영소문자와 숫자만 가능합니다.');
		userId.focus();
		return false;
	}
	
	var pw = joinFrm.user_pw;
	var pw_Confirm = joinFrm.user_pw_chk;
	if(pw.value !== pw_Confirm.value){
		alert('비밀번호가 다릅니다.');
		pw_Confirm.focus();
		return false;
	}
	
	var name = joinFrm.nm;
	var nameConfirm = /^[가-힣]*$/;
	if(!nameConfirm.test(name.value)){
		name.focus();
		alert('이름은 한글만 가능합니다.');
		return false;
	}
}