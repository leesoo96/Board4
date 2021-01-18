<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="centerCont">
	<div>
		<h2>비밀번호 변경</h2>
		<form action="/user/changePwProc.korea" method="post"> 
			<div>
				<input type="password" name="current_pw" placeholder="현재 비밀번호">
			</div>
			<div>
				<input type="password" name="user_pw" placeholder="변경할 비밀번호">
			</div>
		  	<div>
				<input type="password" name="user_pw_chk" placeholder="비밀번호 확인">
			</div>
			<input type="submit" value="변경">
		</form>
		<div>${msg }</div>
	</div>
</div>