package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {

	// DB 연결
	public static Connection getConnection() {
		Properties properties = new Properties();
		Connection con = null;

		try {
			FileInputStream fis = new FileInputStream("src/config/db.properties");
			properties.load(fis);
			String driver = properties.getProperty("driver");
			String url = properties.getProperty("url");
			String id = properties.getProperty("id");
			String pwd = properties.getProperty("pwd");

			Class.forName(driver);
			con = DriverManager.getConnection(url, id, pwd);
		} catch (IOException e) {
			System.out.println("[파일 로딩 오류: " + e.toString() + "]");
		} catch (ClassNotFoundException e) {
			System.out.println("[JDBC 드라이버 오류: " + e.toString() + "]");
		} catch (SQLException e) {
			System.out.println("[DB 연결 오류: " + e.toString() + "]");
		}
		return con;
	}

	// 연결 상태 출력
	public static boolean connectionStatus(Connection con) {
		if (con != null) {
			return true;
		} else {
			return false;
		}
	}

	// 자원 반납 (Connection, PreparedStatement, ResultSet)
	public static void dbClose(PreparedStatement pstmt, ResultSet rs) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 자원 반납 (Connection, PreparedStatement)
	public static void dbClose(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}