<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	WEB-INF 아래에 JSP 위치 => 보안 상의 이유 
	주소값 응대 무조건 서블릿! ==> MVC 2
	
			-> 개인용
			pageContext 
  ↓ 생명주기 ↑	request       
			session 
	
			-> 공용
			application
			
설명 참조	
https://m.blog.naver.com/PostView.nhn?blogId=milkoon1&logNo=220860106284&proxyReferer=https:%2F%2Fwww.google.com%2F
</body>
</html>