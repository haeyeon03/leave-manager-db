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
import model.LeaveRequestVO;

public class LeaveDAO {

	/**
	 * 휴가 신청
	 * 
	 * @param leaveRequestVO 휴가 신청 정보 객체
	 * @return 성공 시 1, 실패 시 0 반환
	 */
	public int insertLeaveRequest(LeaveRequestVO leaveRequestVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		String query = "INSERT INTO LEAVE_REQUEST (REQ_NO, LEAVE_TYPE, START_DATE, END_DATE, REASON, STATUS, EMP_NO) VALUES\r\n"
				+ "(REQ_SEQ.NEXTVAL, ? ,?, ?, ?, ?, ?)";

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

	/**
	 * 로그인 한 사원의 해당 연도 잔여 연차 조회
	 * 
	 * @param employee 로그인 한 사원
	 * @return 성공시 남은 연차 일수 , 실패 시 -1 반환
	 */
	public int selectRemainingDays(EmployeeVO employee) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT REMAINING_DAYS FROM EMPLOYEE_LEAVE WHERE EMP_NO = ? AND YEAR = EXTRACT(YEAR FROM SYSDATE)";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, employee.getEmpNo());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int remainingDays = rs.getInt("REMAINING_DAYS");
				return remainingDays;
			}

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return -1;
	}

	/**
	 * 로그인 한 사원의 휴가 신청 내역 목록 조회
	 * 
	 * @param employee 로그인 한 사원
	 * @return 성공시 휴가 신청 목록 , 실패시 null 반환
	 */
	public List<LeaveRequestVO> selectLeaveRequestByEmpNo(EmployeeVO employee) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT LEAVE_TYPE, START_DATE, END_DATE, REASON, STATUS FROM LEAVE_REQUEST WHERE EMP_NO = ?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, employee.getEmpNo());
			rs = pstmt.executeQuery();
			List<LeaveRequestVO> leaveRequesList = new ArrayList<>();
			while (rs.next()) {
				String leaveType = rs.getString("LEAVE_TYPE");
				LocalDate startDate = rs.getDate("START_DATE").toLocalDate();
				LocalDate endDate = rs.getDate("END_DATE").toLocalDate();
				String reason = rs.getString("REASON");
				String status = rs.getString("STATUS");

				LeaveRequestVO leaveRequestVO = new LeaveRequestVO(leaveType, startDate, endDate, reason, status);
				leaveRequesList.add(leaveRequestVO);

			}
			return leaveRequesList;

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return null;
	}

	/**
	 * 휴가 신청 내역 목록 조회
	 * 
	 * @return 성공시 휴가 신청 목록 , 실패시 null 반환
	 */
	public List<LeaveRequestVO> selectLeaveRequest() {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM LEAVE_REQUEST";

		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			List<LeaveRequestVO> leaveRequesList = new ArrayList<>();
			while (rs.next()) {
				int reqNo = rs.getInt("REQ_NO");
				String leaveType = rs.getString("LEAVE_TYPE");
				LocalDate startDate = rs.getDate("START_DATE").toLocalDate();
				LocalDate endDate = rs.getDate("END_DATE").toLocalDate();
				String reason = rs.getString("REASON");
				String status = rs.getString("STATUS");
				int empNo = rs.getInt("EMP_NO");

				LeaveRequestVO leaveRequestVO = new LeaveRequestVO(reqNo, leaveType, startDate, endDate, reason, status,
						empNo);
				leaveRequesList.add(leaveRequestVO);

			}
			return leaveRequesList;

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return null;
	}

	/**
	 * 특정 사원의 특정 연도 연차 잔여 일수 조회
	 * 
	 * @param empNo 사번
	 * @param year  연도
	 * @return 잔여 일수, 실패 시 -1
	 */
	public int selectRemainingDaysByYear(int empNo, int year) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int remainingDays = -1;
		String query = "SELECT REMAINING_DAYS FROM EMPLOYEE_LEAVE WHERE EMP_NO = ? AND YEAR = ?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, empNo);
			pstmt.setInt(2, year);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				remainingDays = rs.getInt("REMAINING_DAYS");
			}
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return remainingDays;
	}

	/**
	 * 특정 사원의 특정 연도 연차 차감
	 * 
	 * @param empNo 사번
	 * @param year  연도
	 * @param days  차감할 연차 일수
	 * @return 업데이트 성공 시 1 이상, 실패 시 0
	 */
	public int deductRemainingDays(int empNo, int year, int days) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		int count = 0;
		String query = "UPDATE EMPLOYEE_LEAVE SET REMAINING_DAYS = REMAINING_DAYS - ? WHERE EMP_NO = ? AND YEAR = ?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, days);
			pstmt.setInt(2, empNo);
			pstmt.setInt(3, year);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, null);
		}
		return count;
	}

	/**
	 * 연차 신청 상태 업데이트
	 * 
	 * @param leaveRequestVO 연차 신청 객체 (REQ_NO와 상태값 포함)
	 * @return 업데이트 성공 시 1 이상, 실패 시 0
	 */
	public int updateLeaveRequestStatus(LeaveRequestVO leaveRequestVO) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		int count = 0;
		String query = "UPDATE LEAVE_REQUEST SET STATUS = ? WHERE REQ_NO = ?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, leaveRequestVO.getStatus());
			pstmt.setInt(2, leaveRequestVO.getReqNo());
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, null);
		}
		return count;
	}

	/**
	 * 신청 번호(REQ_NO)로 단일 연차 신청 조회
	 * 
	 * @param reqNo 연차 신청 번호
	 * @return 해당 연차 신청 객체, 없으면 null
	 */
	public LeaveRequestVO selectLeaveRequestByReqNo(int reqNo) {
		Connection con = DBConnector.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT REQ_NO, LEAVE_TYPE, START_DATE, END_DATE, REASON, STATUS, EMP_NO FROM LEAVE_REQUEST WHERE REQ_NO = ?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, reqNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int reqNoDb = rs.getInt("REQ_NO");
				String leaveType = rs.getString("LEAVE_TYPE");
				LocalDate startDate = rs.getDate("START_DATE").toLocalDate();
				LocalDate endDate = rs.getDate("END_DATE").toLocalDate();
				String reason = rs.getString("REASON");
				String status = rs.getString("STATUS");
				int empNo = rs.getInt("EMP_NO");

				return new LeaveRequestVO(reqNoDb, leaveType, startDate, endDate, reason, status, empNo);
			}
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		} finally {
			DBConnector.closeResources(pstmt, rs);
		}
		return null;
	}
}