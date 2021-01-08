package com.koreait.board4;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.board4.common.Utils;
import com.koreait.board4.db.BoardDAO;
import com.koreait.board4.model.BoardModel;
import com.koreait.board4.model.BoardParam;
import com.koreait.board4.model.BoardSEL;

public class BoardController {

	public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int typ = Utils.getIntParam(request, "typ");

		BoardParam param = new BoardParam();
		param.setTyp(typ);

		List<BoardSEL> list = BoardDAO.selBoardList(param);
		request.setAttribute("list", list);
		
		Utils.forwardTemp("list", "temp/basic_temp", "board/bList", request, response);
	}
	
	public void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i_board = Utils.getIntParam(request, "i_board");
		
	}
	
	public void reg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utils.forwardTemp("글쓰기", "temp/basic_temp", "board/bRegMod", request, response);
		
		int result = BoardDAO.reg(request);
//		request.setAttribute("data", o); 미완
		System.out.println(result);
		if(result == 0) {
			list(request, response);
		}else {
			response.sendRedirect("");
		}
	}
	
	public void mod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
