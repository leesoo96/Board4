<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<h2>${data == null ? '글등록' : '글수정' }</h2>
<form action="bRegmod" method="post" id="frm">
	<input type="hidden" name="i_board" value="${param.i_board }">
	<input type="hidden" name="typ" value="${typ }">
	<div>
		제목 <input type="text" name="title" value="${data.title }" required>
	</div>
	<div>
		내용 <textarea name="ctnt" required>${data.ctnt }</textarea>
	</div>
	<div>
		<input type="submit" value="확인">
	</div>
</form>

<script>
	/* /bRegmod의 doPost() -> err  */
	<c:if test="${err != null}">
		alert('${err}');
	</c:if>
</script>