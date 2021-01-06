package com.koreait.board4;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller {

	private static UserController uCont = new UserController();
	
//	에러 발생시 에러 표출 페이지 이동 
	public static void goToErr(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsp = "/WEB-INF/view/err.jsp";
		request.getRequestDispatcher(jsp).forward(request, response);
	}
	
	public static void nav(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
//		split -> / 를 기준으로 조각내어(구분) 배열로 만듦
		String[] urlArr = request.getRequestURI().split("/");
//		System.out.println(urlArr.length);
//		(root)/user/(name).korea -> 총 3개로 조각남
	
		switch (urlArr[1]) {
		case "user":
			
			switch (urlArr[2]) {
			case "login.korea":
				uCont.login(request, response);
				return;
			case "loginProc.korea":
				uCont.loginProc(request, response);
				return;
			}
			
			break;
		case "board":
			break;	
		}
		goToErr(request, response);
	}
}
