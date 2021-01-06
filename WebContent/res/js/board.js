// 글 번호 클릭 시  해당 url로 이동
function clickArticle(i_board){
		var url = `bDetail?i_board=${i_board}`;
		location.href = url; 
}

function clickDel(i_board, typ){
	if(confirm('정말 삭제하시겠습니까?')){
		location.href = `bDel?i_board=${i_board}&typ=${typ}`;
	}
}

// 댓글 수정버튼 클릭 
function cmtMod(i_cmt){
	var modFrm = document.querySelector('#mod_'+i_cmt);
	modFrm.classList.remove('cmt_mod_form');	
}
function cmtModClose(i_cmt){
	var modFrm = document.querySelector('#mod_'+i_cmt);
	modFrm.classList.add('cmt_mod_form');
}

// 좋아요 기능 처리 1
function toggleFavorite(i_board){
	var heart = document.querySelector('#favoriteFunc');
	var state = heart.getAttribute('is_favorite'); // 문자열상태임!
	console.log(state); // 좋아요 안누른 상태 -> 0이 나온다(기본값)
	state = 1 - state; 
	
	// get 방식으로 통신
	axios.get('/board/ajax_favorite', {
		params: {
			// state 값이 1이면 좋아요안누름 0이면 좋아요 누름 
			state : state,
			// t_board의 i_board => 게시물 번호(PK)
			i_board : i_board
		}
	}).then(function(res){ 
		console.log(res); // 통신 성공
		
	/*	data:
	      result: 1 - 콘솔창에서 확인완료
	*/	if(res.data.result == 1){
			var iconClass = state == 1 ? 'fas' : 'far';
			heart.innerHTML = `<i class="${iconClass} fa-heart"></i>`;
			heart.setAttribute('is_favorite', state);
		}else {
			alert('에러가 발생하였습니다.');
		}
	}).catch(function(err){
		console.err('에러 발생!!' + err); // 통신 실패 
	});
}

// 제목 혹은 내용이 빈 공란일 경우 알람 표시 - 현재 사용X 비속어확인용도로 사용
function chk(){
	var frm = document.querySelector('#frm');
	if(chkEmptyEle(frm.title, '제목') || chkEmptyEle(frm.ctnt, '내용')){
		return false;
	}
}