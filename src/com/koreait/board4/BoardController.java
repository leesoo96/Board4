package com.koreait.board4;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreait.board4.common.SecurityUtils;
import com.koreait.board4.common.Utils;
import com.koreait.board4.db.BoardDAO;
import com.koreait.board4.db.CommonDAO;
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
		
//		ip주소로 구분 -> application 객체 이용해서 새로고침해도 조회수 증가 제어 
//		ip주소값 얻어오기 , application에서 특정값 만들기- 어떤글인지 구분해야한다 
//		특정값으로 값있는지 확인 , 없으면 그 특정값으로 아이피주소 set 
//		있으면 application에 저장된 아이피주소가 나랑  같은지 확인  - 같으면 무시, 다르면 조호수 증가 
		
		String myIp = request.getRemoteAddr();
		final String KEY = String.format("_%d", i_board);

//		메모리에 저장되기때문에 용량을 많이 잡아먹는다 -> 메모리 낭비!!!!!!!!!
//		테이블만들어서 거기에 조회수를 저장하는 것이 가장 좋다
		ServletContext application = request.getServletContext();
		String compareIp = (String)application.getAttribute(KEY);
		
		if(!myIp.equals(compareIp) || compareIp == null) {
			application.setAttribute(myIp, KEY);
			
			String sql = " UPDATE t_board"
						 + " SET hits = hits + 1"
						 + " WHERE i_board = ?";
			
			BoardDAO.executeUpdate(sql, new SQLInterUpdate() {
				
				@Override
				public void proc(PreparedStatement pstmt) throws SQLException {
					pstmt.setInt(1, i_board);
				}
			});
		}
		
		BoardSEL sel = BoardDAO.selBoard(param);
		request.setAttribute("data", sel);
		request.setAttribute("jsList", new String[] {"board", "axios.min"});
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
					 + " SELECT ?, "
					 + " IFNULL(MAX(seq), 0) + 1 ,"
					 + " ?, ?, ? "
					 + " FROM t_board "
					 + " WHERE typ = ? ";
		
		int result = BoardDAO.executeUpdate(sql, new SQLInterUpdate() {
			
			@Override
			public void proc(PreparedStatement pstmt) throws SQLException {
				pstmt.setInt(1, typ);
				pstmt.setNString(2, title);
				pstmt.setNString(3, ctnt);
				pstmt.setInt(4, i_user);
				pstmt.setInt(5, typ);
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
	
	public void fav(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int state = Utils.getIntParam(request, "state");
		int i_board = Utils.getIntParam(request, "i_board");
		
		String sql = " INSERT INTO t_board_favorite"
					 + " (i_board, i_user)"
					 + " VALUES (?, ?)";
		
		if(state == 0) {
			sql = " DELETE FROM t_board_favorite"
				  + " WHERE i_board = ? AND i_user = ?";
		}
		
		int result = BoardDAO.executeUpdate(sql, new SQLInterUpdate() {
			
			@Override
			public void proc(PreparedStatement pstmt) throws SQLException {
				pstmt.setInt(1, i_board);
				pstmt.setInt(2, SecurityUtils.getLoginI_UserPK(request));
			}
		});
		
		response.setContentType("application/json");
		response.getWriter().print(String.format("{\"result\":%d}", result));
//			                                  {"result":1} or {"result":0}
	}
}
