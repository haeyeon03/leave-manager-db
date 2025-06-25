package controller;

import java.util.List;

import model.EmployeeVO;
import model.LeaveRequestVO;
import service.LeaveService;

public class LeaveController {
	private final LeaveService leaveService;

	public LeaveController(LeaveService leaveService) {
		this.leaveService = leaveService;
	}

	/**
	 * 휴가 신청 등록 API
	 * 
	 * @param LeaveRequestVO 휴가 신청 정보 객체
	 */
	public void insertLeaveRequest(LeaveRequestVO leaveRequestVO) {
		leaveService.insertLeaveRequest(leaveRequestVO);
	}

	/**
	 * 로그인 한 사원의 휴가 신청 내역을 조회 API
	 * 
	 * @param employee 로그인 한 사원
	 * @return 성공시 휴가 신청 목록 , 실패시 null 반환
	 */
	public int getRemainingDays(EmployeeVO employee) {
		return leaveService.getRemainingDays(employee);
	}

	/**
	 * 로그인 한 사원의 휴가 신청 내역 목록 조회 API
	 * 
	 * @param employee 로그인 한 사원
	 * @return 성공시 휴가 신청 목록 , 실패시 null 반환
	 */
	public List<LeaveRequestVO> getLeaveRequestByEmpNo(EmployeeVO employee) {
		List<LeaveRequestVO> leaveRequesList = leaveService.getLeaveRequestByEmpNo(employee);
		return leaveRequesList;
	}

	/**
	 * 전체 연차 신청 목록 API
	 * 
	 * @return 연차 신청 리스트
	 */
	public List<LeaveRequestVO> getLeaveRequestList() {
		List<LeaveRequestVO> leaveRequesList = leaveService.getLeaveRequestList();
		return leaveRequesList;
	}

	/**
	 * 연차 신청 처리 API (예: 승인 또는 반려)
	 * 
	 * @param leaveRequestVO 처리할 연차 신청 객체
	 */
	public void processLeaveRequest(LeaveRequestVO leaveRequestVO) {
		leaveService.processLeaveRequest(leaveRequestVO);
	}
}
