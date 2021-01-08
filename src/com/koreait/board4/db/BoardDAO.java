package com.koreait.board4.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.board4.common.Utils;
import com.koreait.board4.model.BoardParam;
import com.koreait.board4.model.BoardSEL;

public class BoardDAO extends CommonDAO{
	
//	각 게시판 게시글 목록 확인
	public static List<BoardSEL> selBoardList(BoardParam param){
		List<BoardSEL> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = " SELECT A.i_board, A.seq, A.title, A.r_dt, A.hits, "
					 + " B.i_user, B.nm, "
					 + " ifnull(C.favorite_cnt, 0) AS favorite_cnt "
					 + " FROM t_board A "
					 + " LEFT JOIN t_user B "
					 + " ON A.i_user = B.i_user "
					 + " LEFT JOIN ( "
					 + " SELECT i_board, COUNT(i_board) AS favorite_cnt "
					 + " FROM t_board_favorite "
					 + " GROUP BY i_board )"
					 + " C ON A.i_board = C.i_board "
					 + " WHERE typ = ? "
					 + " ORDER BY seq DESC ";
		
		try {
			conn = DBUtils.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, param.getTyp());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardSEL bModel = new BoardSEL();
				bModel.setI_board(rs.getInt("i_board"));
				bModel.setTyp(param.getTyp());
				bModel.setSeq(rs.getInt("seq"));
				bModel.setTitle(rs.getNString("title"));
				bModel.setR_dt(rs.getString("r_dt"));
				bModel.setHits(rs.getInt("hits"));
				bModel.setNm(rs.getString("nm"));
				bModel.setIs_favorite(rs.getInt("favorite_cnt"));
				
				list.add(bModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, pstmt, rs);
		}
		
		return list;
	}
	
//	게시글 읽기
	
//	게시글 쓰기
	public static int reg(HttpServletRequest request) {
		int typ = Utils.getIntParam(request, "typ");
		int seq = Utils.getIntParam(request, "seq");
		String title = request.getParameter("title");
		String ctnt = request.getParameter("ctnt");
		int i_user = Utils.getIntParam(request, "i_user");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = " INSERT INTO t_board "
					 + " (typ, seq, title, ctnt, i_user) "
					 + " SELECT ?, IFNULL(MAX(seq), 0) + 1 ,"
					 + " ?, ?, ? "
					 + " FROM t_board "
					 + " WHERE typ = ? ";
		
		return CommonDAO.executeUpdate(sql, new SQLInterUpdate() {
			
			@Override
			public void proc(PreparedStatement pstmt) throws SQLException {
				pstmt.setInt(1, typ);
				pstmt.setNString(2, title);
				pstmt.setNString(3, ctnt);
				pstmt.setInt(4, i_user);
				pstmt.setInt(5, typ);
			}
		});
	}
}
