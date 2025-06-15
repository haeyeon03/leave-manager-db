package view;

import java.time.LocalDate;
import java.util.List;

import controller.LeaveController;
import helper.InputHelper;
import model.EmployeeVO;
import model.LeaveRequestVO;

public class GeneralView {
	private final LeaveController leaveController;

	public GeneralView(LeaveController leaveController) {
		this.leaveController = leaveController;
	}

	/**
	 * 사용자 메뉴 실행 메서드
	 * 
	 * @return true: 프로그램 종료 / false: 로그아웃 상태
	 */
	public boolean run(EmployeeVO employee) {
		boolean stops = false;
		userInfo(employee);
		while (!stops) {
			int selectedNumber = displayGeneralMenu();
			switch (selectedNumber) {
			// 1. 휴가신청
			case 1:
				int empNo = employee.getEmpNo();
				String leaveType = InputHelper.inputLeaveType();
				LocalDate startDate = InputHelper.inputStartDate();
				LocalDate endDate = InputHelper.inputEndDate();
				String reason = InputHelper.inputLeaveReason();
				String status = "대기";
				LeaveRequestVO leaveRequestVO = new LeaveRequestVO(leaveType, startDate, endDate, reason, status,
						empNo);
				leaveController.insertLeaveRequest(leaveRequestVO);
				break;
			// 2. 신청내역 확인
			case 2:
				List<LeaveRequestVO> leaveRequesList = leaveController.getLeaveRequest(employee);
				printLeaveRequestList(leaveRequesList);
				break;
			// 3. 로그아웃
			case 3:
				stops = true;
				break;
			// 4. 종료
			case 4:
				return true;
			default:
				System.out.println("[API ERROR] 존재 하지 않는번호입니다. 다시 선택해주세요.");
			}
		}
		return false;
	}

	/**
	 * 사용자 메뉴를 콘솔에 출력
	 *
	 * @return 사용자가 선택한 메뉴 번호
	 */
	private int displayGeneralMenu() {
		System.out.println("+=======================================+");
		System.out.println("           	 메뉴      	       			");
		System.out.println("+=======================================+");
		System.out.println(" 1. 휴가신청                          		");
		System.out.println(" 2. 신청내역 확인                          	");
		System.out.println(" 3. 로그아웃                        		");
		System.out.println(" 4. 종료                         			");
		System.out.println("+=======================================+");
		System.out.println("번호를 입력해주세요:");
		int selectNumber = InputHelper.getInt();
		System.out.println("+---------------------------------------+");
		return selectNumber;
	}

	/**
	 * 로그인한 사용자 정보를 콘솔에 출력
	 *
	 * @param employee 로그인한 직원의 정보
	 */
	private void userInfo(EmployeeVO employee) {
		int remainingDays = leaveController.getRemainingDays(employee);
		System.out.println("=========================================");
		System.out.printf("%s님 로그인 되었습니다. \n", employee.getEmpName());
		System.out.printf("사번 : %d \n", employee.getEmpNo());
		System.out.printf("직급 : %s \n", employee.getPosition());
		System.out.printf("전화번호 : %s \n", employee.getPhoneNumber());
		System.out.printf("생년월일 : %s \n", employee.getBirthDate());
		System.out.printf("입사일자 : %s \n", employee.getHireDate());
		System.out.printf("잔여연차 : %d \n", remainingDays);
		System.out.println("=========================================");
	}
	
	/**
	 * 휴가 신청 내역 목록 콘솔에 출력
	 *
	 * @param LeaveRequestList 휴가 신청 내역 목록
	 */
	private void printLeaveRequestList(List<LeaveRequestVO> list) {
		for (LeaveRequestVO leave : list) {
			System.out.printf("|휴가유형:%-6s | 시작일:%-10s | 종료일:%-10s | 사유:%-12s | 상태:%-6s |\n",
					leave.getLeaveType(), leave.getStartDate(), leave.getEndDate(),
					leave.getReason(), leave.getStatus());
		}
	}
}
