package view;

import java.util.List;

import controller.EmployeeController;
import model.EmployeeVO;
import model.PageVO;
import model.SortVO;
import util.InputUtil;
import util.PageUtil;

public class AdminView {

	private final EmployeeController employeeController;

	public AdminView(EmployeeController employeeController) {
		this.employeeController = employeeController;
	}

	/**
	 * 관리자 요청 메서드
	 * 
	 */
	public boolean run() {
		boolean stops = false;

		while (!stops) {
			int selectedNumber = displayAdminMenu();
			switch (selectedNumber) {
			// 1. 사원조회
			case 1:
				System.out.println("1. 사번 오름차순");
				System.out.println("2. 입사일 오름차순");
				System.out.println("3. 입사일 내림차순");
				int orderByNumber = InputUtil.getInt();
				switch (orderByNumber) {
				case 1:
					// 1. 사번 오름차순
					showEmployeeList(new SortVO("EMP_NO", "ASC"));
					break;
				case 2:
					// 2. 입사일 오름차순
					showEmployeeList(new SortVO("HIRE_DATE", "ASC"));
					break;
				case 3:
					// 3. 입사일 내림차순
					showEmployeeList(new SortVO("HIRE_DATE", "DESC"));
					break;
				}
				break;
			// 4. 사원정보 삭제
			case 4:
				System.out.printf("삭제할 사원의 사번을 입력해주세요:");
				int employeeId = InputUtil.getInt();
				employeeController.deleteEmployee(employeeId);
				break;
			// 6. 로그아웃
			case 6:
				stops = true;
				break;
			// 7. 종료
			case 7:
				return true;
			default:
				System.out.println("존재 하지 않는번호입니다. 다시 선택해주세요.");
			}
		}
		return false;
	}

	/**
	 * 사원정보 리스트 출력 메서드
	 * 
	 */
	private void showEmployeeList(SortVO sort) {
		// 1. 페이지당 항목 수 입력
		System.out.printf("페이지당 표시할 항목 수를 입력해 주세요:");
		int pageSize = InputUtil.getInt();
		System.out.println("+---------------------------------------+");
		
		// 2. 전체 페이지 조회
		int totalPage = employeeController.getTotalPage(pageSize);
		System.out.printf("전체 페이지 수: p.%d\n", totalPage);
		System.out.println("페이지를 선택해주세요:");
		int currentPage = InputUtil.getInt();
		System.out.println("+---------------------------------------+");
		if (1 > currentPage || currentPage > totalPage) {
			System.out.println("존재 하지 않는 페이지입니다. 다시 입력해주세요.");
			return;
		}

		// 3. 사원 조회
		List<EmployeeVO> employeeList = employeeController.getEmployeeList(currentPage, pageSize, sort);
		printUserList(employeeList);
	}

	/**
	 * 사원정보 값 출력 메서드
	 * 
	 */
	private void printUserList(List<EmployeeVO> list) {
		for (EmployeeVO user : list) {
			String remainingDaysStr;
			if (user.getRemainingDays() == 0) {
				remainingDaysStr = "-";
			} else {
				remainingDaysStr = String.valueOf(user.getRemainingDays());
			}

			String roleStr;
			if (user.isRole() == 0) {
				roleStr = "관리자";
			} else {
				roleStr = "사용자";
			}

			System.out.printf(
					"| 사번:%-6s | 비밀번호:%-10s | 이름:%-5s | 직급:%-4s | 생년월일:%-10s | 입사일자:%-10s | 전화번호:%-13s | 관리자 여부:%-6s | 잔여연차:%-5s |\n",
					user.getEmpNo(), user.getPassword(), user.getEmpName(), user.getPosition(), user.getBirthDate(),
					user.getHireDate(), user.getPhoneNumber(), roleStr, remainingDaysStr);
		}
	}

	/**
	 * 콘솔에 관리자 메뉴 표시
	 * 
	 * @return selectNumber
	 */
	public int displayAdminMenu() {
		System.out.println("+=======================================+");
		System.out.println("              관리자 메뉴      				");
		System.out.println("+=======================================+");
		System.out.println(" 1. 사원조회                          		");
		System.out.println(" 2. 사원등록                          		");
		System.out.println(" 3. 사원정보 수정                       	");
		System.out.println(" 4. 사원정보 삭제                       	");
		System.out.println(" 5. 휴가신청 리스트 확인                 		");
		System.out.println(" 6. 로그아웃      	               	 		");
		System.out.println(" 7. 종료                         			");
		System.out.println("+=======================================+");
		System.out.println("번호를 입력해주세요:");
		int selectNumber = InputUtil.getInt();
		System.out.println("+---------------------------------------+");
		return selectNumber;
	}
}
