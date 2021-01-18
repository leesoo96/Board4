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
					 + " B.i_user, B.nm, B.profile_img, "
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
				bModel.setProfile_img(rs.getString("profile_img"));
				
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
	public static BoardSEL selBoard(BoardParam param) {
		BoardSEL sel = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql =  " SELECT A.i_board, A.typ, A.seq, A.title, A.ctnt, A.r_dt, A.hits, "
				 	  + " B.i_user, B.nm, B.profile_img,"
				 	  + " ifnull(C.favorite_cnt, 0) AS favorite_cnt, "
				 	  + " CASE WHEN D.i_board IS NULL THEN 0 ELSE 1 END "
				 	  + " AS is_favorite " // i_board의 컬럼명 바꿈
				 	  + " FROM t_board A "
				 	  + " LEFT JOIN t_user B "
				 	  + " ON A.i_user = B.i_user "
				 	  + " LEFT JOIN ( "
				 	  + " SELECT i_board, COUNT(i_board) AS favorite_cnt "
				 	  + " FROM t_board_favorite "
				 	  + " GROUP BY i_board ) "
				 	  + " C ON A.i_board = C.i_board "
				 	  + " LEFT JOIN t_board_favorite D "
				 	  + " ON A.i_board = D.i_board "
				 	  + " AND D.i_user = ? "
				 	  + " WHERE A.i_board = ? ";

		try {
			conn = DBUtils.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, param.getI_user());
			pstmt.setInt(2, param.getI_board());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				sel = new BoardSEL();
				
				sel.setI_user(param.getI_user());
				sel.setI_board(param.getI_board());
				sel.setTyp(rs.getInt("typ"));
				sel.setSeq(rs.getInt("seq"));
				sel.setTitle(rs.getNString("title"));
				sel.setCtnt(rs.getNString("ctnt"));
				sel.setR_dt(rs.getString("r_dt"));
				sel.setHits(rs.getInt("hits"));
				sel.setNm(rs.getString("nm"));
				sel.setIs_favorite(rs.getInt("is_favorite"));
				sel.setFavorite_cnt(rs.getInt("favorite_cnt"));
				sel.setProfile_img(rs.getString("profile_img"));
				
				return sel;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, pstmt, rs);
		}
		return sel;
	}
}
