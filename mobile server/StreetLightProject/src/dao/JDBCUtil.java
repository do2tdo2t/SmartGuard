package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCUtil {
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/term", "", "");
		} catch (Exception e) {
			System.out.print("db연결 실패");
			e.printStackTrace();
		}
		return null;
	}

	public static void close(PreparedStatement stmt, Connection con) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt = null;
		}
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con = null;
		}
	}

	public static void close(ResultSet rst, PreparedStatement stmt, Connection con) {
		try {
			if (rst != null)
				rst.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rst = null;
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt = null;
		}
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con = null;
		}
	}
}
