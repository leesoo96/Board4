package com.koreait.board4;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.board4.common.SecurityUtils;
import com.koreait.board4.common.Utils;
import com.koreait.board4.db.BoardDAO;
import com.koreait.board4.db.SQLInterUpdate;
import com.koreait.board4.model.BoardParam;
import com.koreait.board4.model.BoardSEL;

public class BoardController {

	public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typ = Utils.getIntParam(request, "typ", 1);

		BoardParam param = new BoardParam();
		param.setTyp(typ);

		List<BoardSEL> list = BoardDAO.selBoardList(param);
		request.setAttribute("list", list);
		request.setAttribute("jsList", new String[] {"board"});
		Utils.forwardTemp("list", "temp/basic_temp", "board/bList", request, response);
	}
	
	public void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i_board = Utils.getIntParam(request, "i_board");
		
		BoardParam param = new BoardParam();
		param.setI_board(i_board);
		param.setI_user(SecurityUtils.getLoginI_UserPK(request));
		
		BoardSEL sel = BoardDAO.selBoard(param);
		request.setAttribute("data", sel);
	
		Utils.forwardTemp(sel.getTitle(), "temp/basic_temp", "board/bDetail", request, response);
	}
	
	public void reg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utils.forwardTemp("글쓰기", "temp/basic_temp", "board/bRegMod", request, response);
		
	}
	
	public void regProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typ = Utils.getIntParam(request, "typ");
		String title = request.getParameter("title");
		String ctnt = request.getParameter("ctnt");
		int i_user = SecurityUtils.getLoginI_UserPK(request);
		
//		db처리
		String sql = " INSERT INTO t_board "
					 + " (typ, seq, title, ctnt, i_user) "
					 + " SELECT typ, "
					 + " IFNULL(MAX(seq), 0) + 1 ,"
					 + " ?, ?, ? "
					 + " FROM t_board "
					 + " WHERE typ = ? ";
		
		int result = BoardDAO.executeUpdate(sql, new SQLInterUpdate() {
			
			@Override
			public void proc(PreparedStatement pstmt) throws SQLException {
				pstmt.setNString(1, title);
				pstmt.setNString(2, ctnt);
				pstmt.setInt(3, i_user);
				pstmt.setInt(4, typ);
			}
		});
		
//		redirect와 Dispatcher 차이점 설명참조 
//		https://devbox.tistory.com/entry/Comporison-Dispatcher%EB%B0%A9%EC%8B%9D%EA%B3%BC-Redirect-%EB%B0%A9%EC%8B%9D
		response.sendRedirect("/board/list?typ=" + typ);
//		request.getRequestDispatcher("/board/list?typ=" + typ).forward(request, response);
	}
	
	public void mod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utils.forwardTemp("글쓰기", "temp/basic_temp", "board/bRegMod", request, response);
	}
	
	public void modProc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
