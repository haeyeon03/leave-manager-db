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

	private static Connection con;

	public static void openConnection() throws Exception {
		Properties properties = new Properties();

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
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		}
	}

	public static void closeResources(PreparedStatement pstmt, ResultSet rs) {
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

	public static void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Connection getCon() {
		return con;
	}
}