package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.DBConnector;
import model.EmployeeVO;
import model.LeaveRequestVO;
import model.PageVO;
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
	 * 사원번호로 사원 존재 여부를 확인
	 * 
	 * @param empNo 사원번호
	 * @return 사원이 존재하면 1, 없으면 0 반환
	 */
	public int selectEmployee(int empNo) {
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
	 * 사원 목록을 페이징 및 정렬 조건에 따라 조회
	 * 
	 * @param pageRange 페이지 시작/끝 번호를 포함한 객체
	 * @param sort      정렬 필드 및 정렬 순서를 포함한 객체
	 * @return 사원 리스트 (List<EmployeeVO>), 실패 시 null 반환
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
	 * 사원 정보 등록
	 * 
	 * @param empNo      사원번호 (String)
	 * @param employeeVO 사원 정보 객체
	 * @return 등록 성공 시 1, 실패 시 0 반환
	 */
	public int insertEmployee(EmployeeVO employeeVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int generatedEmpNo = 0;

		// 1. EMP_SEQ.NEXTVAL을 미리 조회
		String seqSql = "SELECT EMP_SEQ.NEXTVAL FROM DUAL";
		String insertSql = "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			// 시퀀스 값 먼저 가져오기
			pstmt = con.prepareStatement(seqSql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				generatedEmpNo = rs.getInt(1); // 이 값을 저장
			}

			// 가져온 시퀀스 값을 사용해 INSERT
			pstmt = con.prepareStatement(insertSql);
			pstmt.setInt(1, generatedEmpNo);
			pstmt.setString(2, employeeVO.getPassword());
			pstmt.setString(3, employeeVO.getEmpName());
			pstmt.setString(4, employeeVO.getPosition());
			pstmt.setDate(5, java.sql.Date.valueOf(employeeVO.getBirthDate()));
			pstmt.setDate(6, java.sql.Date.valueOf(employeeVO.getHireDate()));
			pstmt.setString(7, employeeVO.getPhoneNumber());
			pstmt.setInt(8, employeeVO.getRole());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return generatedEmpNo;
	}

	/**
	 * 사원의 연차 정보를 등록 연차는 기본적으로 12일로 설정, 연도는 현재 연도로 자동 적용
	 * 
	 * @param employeeVO 사원 정보 객체
	 * @return 등록 성공 시 1, 실패 시 0 반환
	 */
	public int insertLeave(EmployeeVO employeeVO, int generatedEmpNo) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = "INSERT INTO EMPLOYEE_LEAVE VALUES(EXTRACT(YEAR FROM SYSDATE),12,?)";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, generatedEmpNo);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}

	/**
	 * 사원의 특정 컬럼의 정보 수정
	 * 
	 * @param column      수정할 값 컬럼명
	 * @param updateInput 수정할 값
	 * @param updateEmpNo 사원번호
	 * @return 수정 성공 시 1, 실패 시 0 반환
	 */
	public int updateEmployee(EmployeeVO employeeVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;

		StringBuilder query = new StringBuilder("UPDATE EMPLOYEE SET ");
		List<String> setClauses = new ArrayList<>();

		if (employeeVO.getEmpName() != null && !employeeVO.getEmpName().isEmpty()) {
			setClauses.add("EMP_NAME = " + "'" + employeeVO.getEmpName() + "'");
		}

		if (employeeVO.getPosition() != null && !employeeVO.getPosition().isEmpty()) {
			setClauses.add("POSITION = " + "'" + employeeVO.getPosition() + "'");
		}

		if (employeeVO.getPhoneNumber() != null && !employeeVO.getPhoneNumber().isEmpty()) {
			setClauses.add("PHONE_NUMBER = " + "'" + employeeVO.getPhoneNumber() + "'");
		}

		if (setClauses.isEmpty()) {
			// 수정할 내용이 없다면 업데이트 하지 않음
			return 0;
		}

		query.append(String.join(", ", setClauses));
		query.append(" WHERE EMP_NO = " + employeeVO.getEmpNo());

		try {
			pstmt = con.prepareStatement(query.toString());
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}

	/**
	 * 사원 정보를 삭제
	 * 
	 * @param deleteEmpNo 삭제할 사원의 사번
	 * @return 삭제 성공 시 1, 실패 시 0 반환
	 */
	public int deleteEmployee(EmployeeVO employeeVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = "DELETE FROM EMPLOYEE WHERE EMP_NO =?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, employeeVO.getEmpNo());
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}

	/**
	 * 해당 사원의 연차 정보를 삭제
	 * 
	 * @param employeeId 사원번호
	 * @return 삭제 성공 시 1, 실패 시 0 반환
	 */
	public int deleteLeave(EmployeeVO employeeVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = "DELETE FROM EMPLOYEE_LEAVE WHERE EMP_NO =?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, employeeVO.getEmpNo());
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}

	/**
	 * 해당 사원의 연차 신청 내역 삭제
	 * 
	 * @param employeeId 사원번호
	 * @return 삭제 성공 시 1, 실패 시 0 반환
	 */
	public int deleteRequest(EmployeeVO employeeVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = "DELETE FROM LEAVE_REQUEST WHERE EMP_NO=?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, employeeVO.getEmpNo());
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}
	
	/**
	 * 휴가 신청 등록
	 *
	 * @param leaveRequestVO 휴가 신청 객체
	 * @return 삭제 성공 시 1, 실패 시 0 반환
	 */
	public int insertLeaveRequest(LeaveRequestVO leaveRequestVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = """
				INSERT INTO LEAVE_REQUEST (REQ_NO, LEAVE_TYPE, START_DATE, END_DATE, REASON, STATUS, EMP_NO) VALUES
				(REQ_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)
				""";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, leaveRequestVO.getLeaveType());
			pstmt.setDate(2, java.sql.Date.valueOf(leaveRequestVO.getStartDate()));
			pstmt.setDate(3, java.sql.Date.valueOf(leaveRequestVO.getEndDate()));
			pstmt.setString(4, leaveRequestVO.getReason());
			pstmt.setString(5, leaveRequestVO.getStatus());
			pstmt.setInt(6, leaveRequestVO.getEmpNo());
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return count;
	}
}
