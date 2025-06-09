package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import config.DBConnector;
import model.EmployeeVO;

public class LoginDAO {

	/**
	 * 사번과 일치하는 사원 정보 조회
	 *
	 * @param input 사번, 비밀번호
	 * @return 로그인 성공 시 사원 정보(EmployeeVO), 실패 시 null 반환
	 */
	public EmployeeVO login(EmployeeVO input) {
		Connection con = DBConnector.getConnection();
		boolean exit = DBConnector.connectionStatus(con);
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String query = "SELECT * FROM EMPLOYEE WHERE EMP_NO = ?";

		try {
			if (exit) {
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, input.getEmpNo());

				rs = pstmt.executeQuery();

				while (rs.next()) {
					int empNo = rs.getInt("EMP_NO");
					String password = rs.getString("PASSWORD");
					String empName = rs.getString("EMP_NAME");
					String position = rs.getString("POSITION");
					LocalDate birthDate = rs.getDate("BIRTH_DATE").toLocalDate();
					LocalDate hiredDate = rs.getDate("HIRE_DATE").toLocalDate();
					String phoneNumber = rs.getString("PHONE_NUMBER");
					int role = rs.getInt("ROLE");

					return new EmployeeVO(empNo, password, empName, position, birthDate, hiredDate, phoneNumber, role);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.dbClose(pstmt, rs);
		}
		return null;
	}
}
