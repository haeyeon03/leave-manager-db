package service;

import java.util.List;

import dao.LeaveDAO;
import model.EmployeeVO;
import model.LeaveRequestVO;

public class LeaveService {
	private final LeaveDAO leaveDAO;

	public LeaveService(LeaveDAO leaveDAO) {
		this.leaveDAO = leaveDAO;
	}

	/**
	 * 휴가 신청을 등록
	 * 
	 * @param leaveRequestVO 신청할 휴가 정보
	 */
	public void insertLeaveRequest(LeaveRequestVO leaveRequestVO) {
		int count = leaveDAO.insertLeaveRequest(leaveRequestVO);
		if (count == 0) {
			System.out.println("[API ERROR] 휴가 신청 등록을 실패였습니다.");
		} else {
			System.out.println("[API INFO] 휴가 신청을 성공하였습니다.");
		}
	}

	/**
	 * 로그인 한 사원의 남은 연차 일수를 조회
	 * 
	 * @param employee 로그인 한 사원
	 * @return 성공시 남은 연차 일수 , 실패 시 -1 반환
	 */
	public int getRemainingDays(EmployeeVO employee) {
		int remainingDays = leaveDAO.selectRemainingDays(employee);
		if (remainingDays == -1) {
			System.out.println("[API ERROR] 남은 연차 조회를 실패였습니다.");
		} else {
			System.out.println("[API INFO] 남은 연차 조회를 성공하였습니다.");
		}
		return remainingDays;
	}

	/**
	 * 로그인 한 사원의 휴가 신청 내역 목록 조회
	 * 
	 * @param employee 로그인 한 사원
	 * @return 성공시 휴가 신청 목록 , 실패시 null 반환
	 */
	public List<LeaveRequestVO> getLeaveRequest(EmployeeVO employee) {
		List<LeaveRequestVO> leaveRequesList = leaveDAO.getLeaveRequest(employee);
		if (leaveRequesList == null) {
			System.out.println("[API ERROR] 연차 신청 내역 조회를 실패였습니다.");
		} else {
			System.out.println("[API INFO] 휴가 신청을 성공하였습니다.");
		}
		return leaveRequesList;
	}
}
