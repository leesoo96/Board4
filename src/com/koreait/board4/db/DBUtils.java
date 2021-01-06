package com.koreait.board4.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

	public static Connection getConn() throws ClassNotFoundException, SQLException{
		final String URL = "jdbc:mysql://localhost:3306/board3?serverTimezone=UTC";
		final String USER = "root";
		final String PW = "koreait2020";
		
		Class.forName("com.mysql.cj.jdbc.Driver"); 

		Connection conn = null;

		conn = DriverManager.getConnection(URL, USER, PW);
		
		System.out.println("데이터베이스 연결 성공");
		
		return conn;
	}

//	자원 해제
	public static void close(Connection conn, PreparedStatement pstmt) {
		if(conn != null) {
			try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		if(pstmt != null) {
			try {pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		close(conn, pstmt);
		
		if(rs != null) {
			try {rs.close();} catch (Exception e) {e.printStackTrace();}
		}
	}
}
