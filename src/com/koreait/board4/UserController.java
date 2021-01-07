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
import com.mysql.cj.Session;

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
			login(request, response);
			return;
		}
		
		String dbPw = loginUser.getUser_pw();
		String dbSalt = loginUser.getSalt();
		String securityPw = SecurityUtils.getSecurePassword(user_pw, dbSalt);
		
		if(!securityPw.equals(dbPw)) {
//			이상이 생김
			login(request, response);
		}else{
			HttpSession session = request.getSession();
			model.setUser_pw(null);
			model.setSalt(null);
			
//			이상 없음
			response.sendRedirect("/board/list.korea");
		}
	}
}
