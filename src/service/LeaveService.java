package service;

import java.util.List;
import java.util.Map;

import dao.LeaveDAO;
import model.EmployeeVO;
import model.LeaveRequestVO;
import util.DateUtil;

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
	public List<LeaveRequestVO> getLeaveRequestByEmpNo(EmployeeVO employee) {
		List<LeaveRequestVO> leaveRequesList = leaveDAO.selectLeaveRequestByEmpNo(employee);
		if (leaveRequesList == null) {
			System.out.println("[API ERROR] 연차 신청 내역 조회를 실패였습니다.");
		} else {
			System.out.println("[API INFO] 휴가 신청을 성공하였습니다.");
		}
		return leaveRequesList;
	}

	/**
	 * 전체 연차 신청 내역을 조회하여 반환
	 *
	 * @return 연차 신청 리스트 (실패 시 null)
	 */
	public List<LeaveRequestVO> getLeaveRequestList() {
		List<LeaveRequestVO> leaveRequesList = leaveDAO.selectLeaveRequest();
		if (leaveRequesList == null) {
			System.out.println("[API ERROR] 연차 신청 내역 조회를 실패였습니다.");
		} else {
			System.out.println("[API INFO] 휴가 신청을 성공하였습니다.");
		}
		return leaveRequesList;
	}

	/**
	 * 연차 신청 건에 대해 상태(승인/반려)를 처리
	 *
	 * @param leaveRequestVO 처리할 연차 신청 정보 (요청번호 + 상태 포함)
	 */
	public void processLeaveRequest(LeaveRequestVO leaveRequestVO) {
		// 요청 번호로 DB에서 휴가 신청 정보 불러오기
		LeaveRequestVO LeaveRequest = leaveDAO.selectLeaveRequestByReqNo(leaveRequestVO.getReqNo());
		if (LeaveRequest == null) {
			System.out.println("[API ERROR] 휴가 신청 정보를 찾을 수 없습니다. REQ_NO=" + leaveRequestVO.getReqNo());
			return;
		}

		String status = leaveRequestVO.getStatus(); // 새 상태 (승인, 반려)
		if (!"승인".equals(status) && !"반려".equals(status)) {
			System.out.println("[API ERROR] 잘못된 상태값입니다: " + status);
			return;
		}

		if ("승인".equals(status)) {
			// 워킹데이 계산
			Map<Integer, Integer> workingDaysMap = DateUtil.calculateWorkingDays(LeaveRequest.getStartDate(),
					LeaveRequest.getEndDate());

			for (Map.Entry<Integer, Integer> entry : workingDaysMap.entrySet()) {
				int year = entry.getKey();
				int days = entry.getValue();

				int updateCount = leaveDAO.deductRemainingDays(LeaveRequest.getEmpNo(), year, days);
				if (updateCount == 0) {
					System.out.println("[API ERROR] " + year + "년도 연차 차감 실패");
					return;
				}
			}
		}

		// 상태 업데이트
		LeaveRequest.setStatus(status);
		int result = leaveDAO.updateLeaveRequestStatus(LeaveRequest);
		if (result > 0) {
			System.out.printf("[API INFO] 휴가 신청이 '%s' 처리되었습니다.%n", status);
		} else {
			System.out.printf("[API ERROR] 휴가 신청 '%s' 처리에 실패했습니다.%n", status);
		}
	}
}
