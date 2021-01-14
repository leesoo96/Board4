<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title }</title>
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<link rel="stylesheet" href="/res/css/common.css?ver=2">
<c:forEach items="${jsList }" var="item">
	<script defer src="/res/js/${item }.js?ver=2"></script>
</c:forEach>
<script defer src="/res/js/common.js"></script>
</head>
<body>
	<div id="container">
		<header>
			<ul>
			  <c:if test="${loginUser == null}">
			    	<li><a href="/user/login.korea">로그인</a></li>
			  </c:if>
			  
			  <c:if test="${loginUser != null}">
			  	<li>${loginUser.nm } 님, 환영합니다!</li>
			    <li><a href="/user/logout.korea">로그아웃</a></li>
			  </c:if>
			
			  <!-- 게시판 목록 표출 -->
			  <c:forEach items="${menus}" var="item">
			  	<li class="${item.typ == param.typ ? 'selectedMenu' : ''}">
			  		<a href="/board/list.korea?typ=${item.typ }">
			  			${item.nm }
			  		</a>
			  	</li>
			  </c:forEach>
			  
			  <c:if test="${loginUser != null }">
			  	<li><a href="/user/profile.korea">프로필</a></li>
			  	<li><a href="/user/changePw.korea">비밀번호 변경</a></li>
			  </c:if>
			</ul>
		</header>
		<section>
			<jsp:include page="${page}"/>
		</section>
	</div>
</body>
</html>