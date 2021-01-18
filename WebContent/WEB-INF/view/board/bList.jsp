<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="stylesheet" href="/res/css/bList.css">
<div>
	<c:if test="${loginUser != null }">
		<div>        
			<a href="/board/reg.korea?typ=${param.typ == null ? 1 : param.typ}"><button>WRITE</button></a>	
		</div>
	</c:if>
	
	<c:choose>
		<c:when test="${fn:length(list) != 0 }">
			<table>
			<tr>
				<td>번호</td>
				<td>제목</td>
				<td>작성자</td>
				<td>작성날짜</td>
				<td>조회수</td>
				<td>선호도</td>
			</tr>
		
		<c:forEach items="${list }" var="item">
			<tr class="pointer" onclick="clickArticle(${item.i_board})">
				<td>${item.seq}</td>
				<td>${item.title}</td>
				<td>
					${item.nm }
					<c:if test="${item.profile_img == null}">
						<div class="circular--landscape circular--size50">
							<img id="profileImg" src="/res/img/basic_profile.png">
						</div>
					</c:if>
					<c:if test="${item.profile_img != null}">
						<div class="circular--landscape circular--size50">
							<img id="profileImg" src="/res/img/${item.i_user}/${item.profile_img}">
						</div>
					</c:if>
				</td>
				<td>${item.r_dt}</td>
				<td>${item.hits }</td>
				<td>${item.is_favorite }</td>
			</tr>
		</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<div>아직 게시물이 없습니다</div>
		</c:otherwise>
	</c:choose>
	
	<div class="pageContainer">
		<c:forEach begin="1" end="${pageCnt}" var="i">
			<span class="page">
				<a href="/board/bList?typ=${typ}&page=${i}">${i}</a>
			</span>
		</c:forEach>
	</div>
</div>
