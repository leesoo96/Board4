package com.koreait.board4.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.koreait.board4.common.SecurityUtils;
import com.koreait.board4.model.UserModel;

public class UserDAO {

	public static int selUser(UserModel p) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = " select i_user, user_id, user_pw, salt "
					 + " from t_user "
					 + " where user_id = ? ";
		
		try {
			conn = DBUtils.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, p.getUser_id());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String dbPw = rs.getString("user_pw");
				String salt = rs.getString("salt");
				String securityPw = SecurityUtils.getSecurePassword(p.getUser_pw(), salt);
				
				if(securityPw.equals(dbPw)) {
					return 1;
				}else {
					return 3;
				}		
			}
			return 2;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, pstmt, rs);
		}
		
		return 0;
	}
}
