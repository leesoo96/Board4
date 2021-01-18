<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="/res/css/bDetail.css?ver=1">

<div id="detailWrap">
	
<a href="/board/list.korea?typ=${data.typ }">목록으로</a>

<form action="bDetail" method="post" onsubmit="return delConfirm();">
	<div>
	번호 : ${data.seq} 조회수 : ${data.hits }<br/>
	작성자 : ${data.nm } 
	<c:if test="${data.profile_img == null}">
		<div class="circular--landscape circular--size50">
			<img id="profileImg" src="/res/img/basic_profile.png">
		</div>
	</c:if>
	<c:if test="${data.profile_img != null}">
		<div class="circular--landscape circular--size50">
			<img id="profileImg" src="/res/img/${data.i_user}/${data.profile_img}">
		</div>
	</c:if> <br/>
	제목 : ${data.title } <br/>
	내용 : ${data.ctnt } <br/>
	작성날짜 : ${data.r_dt } <br/>
	
	<input type="hidden" name="typ" value="${data.typ }">
	<input type="hidden" name="i_board" value="${data.i_board }">
	</div>
</form>

	
	<c:if test="${loginUser != null }">
	
		<c:if test="${data.i_user == loginUser.i_user }">
			<button onclick="clickDel(${data.i_board},${data.typ });">삭제하기</button>
			<a href="bRegmod?typ=${data.typ }&i_board=${data.i_board}">
			<button>수정하기</button>
			</a>
		</c:if>
		<div>
			<form action="cmt/reg" method="post">
				<input type="hidden" name="i_board" value="${data.i_board }">
				댓글 : <input type="text" name="ctnt">
				<input type="submit" value="댓글쓰기">
			</form>
		</div>
		</c:if>
		
		<div>
			<table>
				<tr>
					<th>[댓글목록]</th>
				</tr>
				
				<c:forEach items="${cmtCtnt }" var="item">
					<tr>
						<td>${item.ctnt }</td>
						<td>${item.user_nm }</td>		
						<td>${item.r_dt }</td>
						<td>
					  	<c:if test="${item.i_user == loginUser.i_user }">
								<button onclick="cmtMod(${item.i_cmt });">수정</button>	
								<a href="cmt/del?i_board=${data.i_board }&i_cmt=${item.i_cmt}"><button>삭제</button></a>		
					  	</c:if>	
						</td>	
					</tr>
					
					<c:if test="${item.i_user == loginUser.i_user }">
						<tr id="mod_${item.i_cmt }" class="cmt_mod_form">
							<td colspan="4">
								<form action="cmt/mod" method="post">
									<input type="hidden" name="i_board" value="${data.i_board }" />
									<input type="hidden" name="i_cmt" value="${item.i_cmt }" /> 
									<input type="text" name="ctnt" value="${item.ctnt }" />
									<input type="submit" value="수정하기" />
									<button type="button" onclick="cmtModClose(${item.i_cmt });">닫기</button>
								</form>
							</td>
						</tr>
				    </c:if>	
				</c:forEach>
			</table>
		</div>
	</div>
	
	<c:if test="${loginUser != null}">
	<div id="favoriteFunc" is_favorite="${data.is_favorite}" 
		 onclick="toggleFavorite(${data.i_board});">
		<c:choose>
			<c:when test="${data.is_favorite == 1 }">
				<i class="fas fa-heart"></i>
			</c:when>
			<c:otherwise>
				<i class="far fa-heart"></i>  <!-- == 0 -->
			</c:otherwise>
		</c:choose>	
	</div>
	</c:if>

<a href="/board/list.korea?typ=${data.typ}">돌아가기</a>

<script>
	<c:if test="${msg != null}">
		alert('${msg}');
	</c:if>
</script>