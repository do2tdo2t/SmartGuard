package dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import vo.StreetLight;

public class StreetLightDAO {
	public int doShow(ArrayList<StreetLight> list) {
		int code=200;
		System.out.println("doShow()");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String selectSQL = "select * from term.streetlight";
		
		try {
			con = (Connection) JDBCUtil.getConnection();
			System.out.println("db연결 성공");
			pstmt = (PreparedStatement) con.prepareStatement(selectSQL);
			rst = pstmt.executeQuery();
			StreetLight obj;
			while(rst.next()) {
				obj = new StreetLight();
				obj.setId(rst.getString(1));
				obj.setCode(rst.getInt(2));
				obj.setLat(rst.getDouble(3));
				obj.setLon(rst.getDouble(4));
				obj.setInfo(rst.getString(5));
				obj.setAlarm(rst.getInt(6));
				list.add(obj);
			}
			
		}catch(SQLException e) {
			System.out.println(e.toString());
			code = 400;
		}finally {
			JDBCUtil.close(rst, pstmt, con);
			return code;
		}
	}
	
	public int doUpdate(String id,int code) {
		int c=200;
		System.out.println("doShow()");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String selectSQL = "select * from term.streetlight where id = ?";
		String updateSQL = "update term.streetlight set alarm = 0 where id = ?";
		
		try {
			con = (Connection) JDBCUtil.getConnection();
			System.out.println("db연결 성공");
			pstmt = (PreparedStatement) con.prepareStatement(selectSQL);
			pstmt.setString(1, id);
			rst = pstmt.executeQuery(selectSQL);
			int val = 0;
			if(rst.next()) {
				//update
				pstmt = (PreparedStatement) con.prepareStatement(updateSQL);
				pstmt.setString(2, id);
				val = pstmt.executeUpdate();
			}else {
				System.out.println("id doesn't exist");
				c = 300;
				return c;
			}
			if(val!=0) {
				System.out.println("update success");
			}else {
				System.out.println("update fail");
				c=300;
			}
			return c;
		}catch(SQLException e) {
			System.out.println(e.toString());
			c = 400;
			return c;
		}finally {
			JDBCUtil.close(rst, pstmt, con);
		}
	}
	public int doSelect(StreetLight obj) {
		int code=200;
		System.out.println("doShow()");
		if(obj.getId()==null) {
			System.out.println("parameter error");
			code = 500;
			return code;
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String selectSQL = "select * from term.streetlight where id = ?";
		try {
			con = (Connection) JDBCUtil.getConnection();
			System.out.println("db연결 성공");
			pstmt = (PreparedStatement) con.prepareStatement(selectSQL);
			rst = pstmt.executeQuery();
			if(rst.next()) {
				obj.setCode(rst.getInt(2));
				obj.setLon(rst.getDouble(3));
				obj.setLat(rst.getDouble(4));
				obj.setInfo(rst.getString(5));
			}else {
				System.out.println("id doesn't exist");
				code = 300;
			}
			return code;
		}catch(SQLException e) {
			System.out.println(e.toString());
			code = 400;
			return code;
		}finally {
			JDBCUtil.close(rst, pstmt, con);
		}
		
	}
	

}
