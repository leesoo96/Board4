package com.koreait.board4.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koreait.board4.model.ManageBoardModel;

public class CommonDAO {
//	게시판 메뉴 목록 확인
	public static List<ManageBoardModel> selMenuList(){
		List<ManageBoardModel> list = new ArrayList<ManageBoardModel>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = " SELECT typ, nm "
					 + " FROM t_manage_board "
					 + " ORDER BY orderby ";
		
		try {
			conn = DBUtils.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ManageBoardModel mbm = new ManageBoardModel();
				mbm.setTyp(rs.getInt("typ"));
				mbm.setNm(rs.getString("nm"));
				
				list.add(mbm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, pstmt, rs);
		}
		
		return list;
	}
	
//  insert, update, delete 에서 사용
	public static int executeUpdate(String sql, SQLInterUpdate siu) {
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 
		 try {
			conn = DBUtils.getConn();
			pstmt = conn.prepareStatement(sql);
			siu.proc(pstmt);
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, pstmt);
		}
		 
		 return 0;
	}
}
