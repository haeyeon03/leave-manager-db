package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import config.DBConnector;
import model.EmployeeVO;
import model.PageVO;
import model.Role;
import model.SortVO;

public class EmployeeDAO {

	/**
	 * 총 페이지 수
	 * 
	 * @param totalSize 리스트의 크기
	 * @return 총 페이지
	 */
	public int countAllEmployee() {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String query = "SELECT COUNT(*) TOTALPAGE FROM EMPLOYEE";

		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				int totalPage = rs.getInt("TOTALPAGE");
				return totalPage;
			}

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return 0;
	}

	/**
	 * 사원 정보 조회
	 * 
	 * @param empNo 사원번호
	 * @return 성공 시 1, 실패 시 0 반환
	 */
	public int checkEmployee(int empNo) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT COUNT(*) COUNT FROM EMPLOYEE WHERE EMP_NO = ?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("COUNT");
				return count;
			}

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return 0;
	}

	/**
	 * 사원조회
	 * 
	 * @return employeeList, 실패 시 null 반환
	 */
	public List<EmployeeVO> selectEmployeeList(PageVO pageRange, SortVO sort) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String query = String.format("""
				SELECT * FROM (
				    SELECT
				        E.EMP_NO,
				        E.PASSWORD,
				        E.EMP_NAME,
				        E.POSITION,
				        E.BIRTH_DATE,
				        E.HIRE_DATE,
				        E.PHONE_NUMBER,
				        E.ROLE,
				        EL.REMAINING_DAYS,
				        ROW_NUMBER() OVER (ORDER BY %s %s) AS RN
				    FROM EMPLOYEE E
				    LEFT JOIN EMPLOYEE_LEAVE EL
				        ON E.EMP_NO = EL.EMP_NO
				        AND EL.YEAR = EXTRACT(YEAR FROM SYSDATE)
				)
				WHERE RN BETWEEN ? AND ?
				""", "E." + sort.getField(), sort.getOrderBy());

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, pageRange.getStartNumber());
			pstmt.setInt(2, pageRange.getEndNumber());
			rs = pstmt.executeQuery();

			List<EmployeeVO> employeeList = new ArrayList<>();
			while (rs.next()) {
				int empNo = rs.getInt("EMP_NO");
				String password = rs.getString("PASSWORD");
				String empName = rs.getString("EMP_NAME");
				String position = rs.getString("POSITION");
				LocalDate birthDate = rs.getDate("BIRTH_DATE").toLocalDate();
				LocalDate hireDate = rs.getDate("HIRE_DATE").toLocalDate();
				String phoneNumber = rs.getString("PHONE_NUMBER");
				int role = rs.getInt("ROLE");
				int remainingDays = rs.getInt("REMAINING_DAYS");

				EmployeeVO employeeVO = new EmployeeVO(empNo, password, empName, position, birthDate, hireDate,
						phoneNumber, role, remainingDays);
				employeeList.add(employeeVO);

			}
			return employeeList;

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return null;
	}

	/**
	 * 사원등록
	 * 
	 * @return 성공 시 1, 실패 시 0 반환
	 */
	public int insertEmployee(String empNo, EmployeeVO employeeVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = String.format("INSERT INTO EMPLOYEE VALUES (%s, ?, ?, ?, ?, ?, ?, ?)", empNo);

		try {
			pstmt = con.prepareStatement(query);

			pstmt.setString(1, employeeVO.getPassword());
			pstmt.setString(2, employeeVO.getEmpName());
			pstmt.setString(3, employeeVO.getPosition());
			pstmt.setDate(4, java.sql.Date.valueOf(employeeVO.getBirthDate()));
			pstmt.setDate(5, java.sql.Date.valueOf(employeeVO.getHireDate()));
			pstmt.setString(6, employeeVO.getPhoneNumber());
			pstmt.setInt(7, employeeVO.getRole());
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}

	/**
	 * 사원수정
	 * 
	 * @return 성공 시 1, 실패 시 0 반환
	 */
	public int updateEmployee(String column, Object updateInput, int updateEmpNo) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = String.format("UPDATE EMPLOYEE SET %s = ? WHERE EMP_NO = ?", column);

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setObject(1, updateInput);
			pstmt.setInt(2, updateEmpNo);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}

	/**
	 * 연차 갯수 수정
	 * 
	 * @return 성공 시 1, 실패 시 0 반환
	 */
	public int updateLeaveDays(Object updateInput, int updateEmpNo) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = "UPDATE EMPLOYEE_LEAVE SET REMAINING_DAYS = ? WHERE EMP_NO = ? AND YEAR = EXTRACT(YEAR FROM SYSDATE)";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setObject(1, updateInput);
			pstmt.setInt(2, updateEmpNo);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}

	/**
	 * 사원삭제
	 * 
	 * @return 성공 시 1, 실패 시 0 반환
	 */
	public int deleteEmployee(int deleteEmpNo) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = "DELETE FROM EMPLOYEE WHERE EMP_NO =?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, deleteEmpNo);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}

	/**
	 * 사원 연차 내역 삭제
	 * 
	 * @return 성공 시 1, 실패 시 0 반환
	 */
	public int deleteLeave(int employeeId) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = "DELETE FROM EMPLOYEE_LEAVE WHERE EMP_NO =?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, employeeId);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}
}
