package com.koreait.board4;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreait.board4.common.SecurityUtils;
import com.koreait.board4.common.Utils;
import com.koreait.board4.db.UserDAO;
import com.koreait.board4.model.UserModel;

public class UserController {
//	로그인 페이지 표출(get)
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utils.forwardTemp("로그인", "temp/basic_temp", "user/login", request, response);
	}
	
//	로그인 처리(post)
	public void loginProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		
		UserModel model = new UserModel();
		model.setUser_id(user_id);

		UserModel loginUser = UserDAO.selUser(model);
		
		if(loginUser == null) { // 아이디 없음
			request.setAttribute("msg",  "아이디가 없습니다. 아이디를 확인해주세요");
			login(request, response);
			return;
		}
		
		String dbPw = loginUser.getUser_pw();
		String dbSalt = loginUser.getSalt();
		String securityPw = SecurityUtils.getSecurePassword(user_pw, dbSalt);
		
		if(!securityPw.equals(dbPw)) {
			request.setAttribute("msg",  "비밀번호가 틀립니다. 비밀번호를 확인해주세요");
//			비밀번호 다름
			login(request, response);
		}else{
//			로그인 성공
			HttpSession session = request.getSession();
			model.setUser_pw(null);
			model.setSalt(null);
			
			session.setAttribute("loginUser", loginUser);
			
			response.sendRedirect("/board/list.korea");
		}
	}
	
//	로그아웃
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		
		response.sendRedirect("/user/login.korea");
	}
	
//	회원가입 페이지 표출(get)
	public void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utils.forwardTemp("회원가입", "temp/basic_temp", "user/join", request, response);
	}
	
//	회원가입
	public void joinProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int result = UserDAO.joinUser(request);

		if(result != 1) {
//			회원가입 오류 발생 시 회원가입 화면으로 이동
			join(request, response);
		}else {
//			회원가입 성공 시 로그인 화면으로 이동
			login(request, response);
		}
	}
}
