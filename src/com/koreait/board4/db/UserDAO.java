package com.koreait.board4.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;

import com.koreait.board4.common.SecurityUtils;
import com.koreait.board4.common.Utils;
import com.koreait.board4.model.UserModel;

public class UserDAO extends CommonDAO{

//	로그인
	public static UserModel selUser(UserModel p) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = " SELECT * "
					 + " FROM t_user "
					 + " WHERE ";
		
		if(p.getUser_id() != null) {
			sql += " user_id = ?";
		}else if(p.getI_user() > 0) {
			sql += " i_user = ?";
		}
		
		try {
			conn = DBUtils.getConn();
			pstmt = conn.prepareStatement(sql);
			
			if(p.getUser_id() != null) {
				pstmt.setString(1, p.getUser_id());
			}else if(p.getI_user() > 0) {
				pstmt.setInt(1, p.getI_user());
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				UserModel um = new UserModel();
				um.setI_user(rs.getInt("i_user"));
				um.setUser_id(rs.getString("user_id"));
				um.setNm(rs.getString("nm"));
				um.setUser_pw(rs.getString("user_pw"));
				um.setSalt(rs.getString("salt"));
				um.setNm(rs.getString("nm"));
				um.setGender(rs.getInt("gender"));
				um.setPhone(rs.getString("phone"));
				um.setProfile_img(rs.getString("profile_img"));
				um.setR_dt(rs.getString("r_dt"));
				
				return um;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, pstmt, rs);
		}
		
		return null;
	}
	
//	회원가입
	public static int joinUser(HttpServletRequest request) {
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String salt = SecurityUtils.getSalt();
		String nm = request.getParameter("nm");
		int gender = Utils.getIntParam(request, "gender");
		String phone = request.getParameter("phone");
		String secPw = SecurityUtils.getSecurePassword(user_pw, salt);
		
		String sql = " INSERT INTO t_user "
					 + " (user_id, user_pw, salt, nm, gender, phone) "
					 + " values (?, ?, ?, ?, ?, ?) ";
		
		return CommonDAO.executeUpdate(sql, new SQLInterUpdate() {
			
			@Override
			public void proc(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, user_id);
				pstmt.setString(2, secPw);
				pstmt.setString(3, salt);
				pstmt.setString(4, nm);
				pstmt.setInt(5, gender);
				pstmt.setString(6, phone);
			}
		});
	}
	
	public static int getCurrent_pw(UserModel p) {
		UserModel model = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = " SELECT user_pw as current_pw"
				+ "FROM t_user WHERE i_user = ?";
		
		try {
			conn = DBUtils.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p.getI_user());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				model = new UserModel();
				model.setUser_pw(rs.getString("user_pw"));
				model.setI_user(p.getI_user());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(conn, pstmt, rs);
		}
		
		return 0;
	}
}
