<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title}</title>
<script defer src="/res/js/join.js"></script>
</head>
<body>
	<div>
		<div> 
			<form id="joinFrm" action="/join" method="post" onsubmit="return joinConfirm();">
				<div><input type="text" name="user_id" placeholder="아이디" required></div>
				<div><input type="password" name="user_pw" placeholder="비밀번호" required></div>
				<div><input type="password" name="user_pw_chk" placeholder="비밀번호 확인"></div>
				<div><input type="text" name="nm" placeholder="성함" required></div>
				<div>
					성별 : 
					<label>여자<input type="radio" name="gender" value="0" checked></label>
					<label>남자<input type="radio" name="gender" value="1"></label>
				</div>
				<div>
					<input type="text" name="phone" placeholder="휴대폰 번호를 입력해주세요.">
				</div>
				<div><input type="submit" value="회원가입"></div>
			</form>
			<a href="/login">로그인</a>
		</div>
	</div>
</body>
</html>