package com.koreait.board4.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.koreait.board4.common.SecurityUtils;
import com.koreait.board4.model.UserModel;

public class UserDAO {

	public static UserModel selUser(UserModel p) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = " select i_user, nm, user_pw, salt "
					 + " from t_user "
					 + " where user_id = ? ";
		
		try {
			conn = DBUtils.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, p.getUser_id());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				UserModel um = new UserModel();
				um.setI_user(rs.getInt("i_user"));
				um.setNm(rs.getString("nm"));
				um.setUser_pw(rs.getString("user_pw"));
				um.setSalt(rs.getString("salt"));
				
				return um;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, pstmt, rs);
		}
		
		return null;
	}
}
