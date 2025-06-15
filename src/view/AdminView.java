package view;

import java.time.LocalDate;
import java.util.List;

import controller.EmployeeController;
import helper.InputHelper;
import model.EmployeeVO;
import model.PositionVO;
import model.RoleVO;
import model.SortVO;

public class AdminView {

	private final EmployeeController employeeController;

	public AdminView(EmployeeController employeeController) {
		this.employeeController = employeeController;
	}

	/**
	 * 관리자 메뉴 실행 메서드
	 * 
	 * @return true: 프로그램 종료 / false: 로그아웃 상태
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
				int orderByNumber = InputHelper.getInt();
				switch (orderByNumber) {
				case 1:
					showEmployeeList(new SortVO("EMP_NO", "ASC"));
					break;
				case 2:
					showEmployeeList(new SortVO("HIRE_DATE", "ASC"));
					break;
				case 3:
					showEmployeeList(new SortVO("HIRE_DATE", "DESC"));
					break;
				}
				break;

			// 2. 사원 등록
			case 2: {
				System.out.println("+---------- 등록할 사원의 정보를 입력 ----------+");
				String password = InputHelper.inputPassword();
				String empName = InputHelper.inputEmpName();
				String position = InputHelper.inputPosition(employeeController.getPositionList());
				LocalDate birthDate = InputHelper.inputBirthDate();
				LocalDate hireDate = InputHelper.inputJoinDate();
				String phoneNumber = InputHelper.inputPhoneNumber();
				int role = InputHelper.inputRole(employeeController.getRoleList());

				EmployeeVO employeeVO = new EmployeeVO(password, empName, position, birthDate, hireDate, phoneNumber,
						role);
				employeeController.addEmployee(employeeVO);
				System.out.println("+---------------------------------------+");
				break;
			}
			// 3. 사원정보 수정
			case 3:
				System.out.printf("수정할 사원의 사번을 입력해주세요:");
				int updateEmpNo = InputHelper.getInt();
				int count = employeeController.checkEmployee(updateEmpNo);
				if (count == 0) {
					System.out.println("[API ERROR] 존재 하지 않는 사원번호 입니다.");
				} else {
					displayEditMenu(updateEmpNo);
				}
				break;

			// 4. 사원정보 삭제
			case 4: {
				System.out.printf("삭제할 사원의 사번을 입력해주세요:");
				int deleteEmpNo = InputHelper.getInt();
				EmployeeVO employeeVO = new EmployeeVO();
				employeeVO.setEmpNo(deleteEmpNo);
				employeeController.removeEmployee(employeeVO);
				break;
			}
			// 5. 휴가 신청 리스트 확인 (미구현)
			case 5:
				break;

			// 6. 로그아웃
			case 6:
				stops = true;
				break;

			// 7. 종료
			case 7:
				return true;

			default:
				System.out.println("[API ERROR] 존재 하지 않는번호입니다. 다시 선택해주세요.");
			}
		}
		return false;
	}

	/**
	 * 사원 목록 출력 메서드 (정렬 포함)
	 *
	 * @param sort 정렬 정보 (SortVO 객체)
	 */
	private void showEmployeeList(SortVO sort) {
		System.out.printf("페이지당 표시할 항목 수를 입력해 주세요:");
		int pageSize = InputHelper.getInt();
		System.out.println("+---------------------------------------+");

		int totalPage = employeeController.getTotalPage(pageSize);
		System.out.printf("전체 페이지 수: p.%d\n", totalPage);
		System.out.println("페이지를 선택해주세요:");
		int currentPage = InputHelper.getInt();
		System.out.println("+---------------------------------------+");

		if (1 > currentPage || currentPage > totalPage) {
			System.out.println("[API ERROR] 존재 하지 않는 페이지입니다. 다시 입력해주세요.");
			return;
		}

		List<EmployeeVO> employeeList = employeeController.getEmployeeList(currentPage, pageSize, sort);
		printUserList(employeeList);
	}

	/**
	 * 사원 목록 상세 정보 출력
	 *
	 * @param list 출력할 사원 리스트
	 */
	private void printUserList(List<EmployeeVO> list) {
		for (EmployeeVO employee : list) {
			System.out.printf(
					"| 사번:%-6s | 비밀번호:%-10s | 이름:%-5s | 직급:%-4s | 생년월일:%-10s | 입사일자:%-10s | 전화번호:%-13s | 관리자 여부:%-6s | 잔여연차:%-5s |\n",
					employee.getEmpNo(), employee.getPassword(), employee.getEmpName(), employee.getPosition(),
					employee.getBirthDate(), employee.getHireDate(), employee.getPhoneNumber(), employee.getRole(),
					employee.getRemainingDays());
		}
	}

	/**
	 * 관리자 메뉴 출력
	 *
	 * @return 선택한 메뉴 번호
	 */
	public int displayAdminMenu() {
		System.out.println("+=======================================+");
		System.out.println("              관리자 메뉴      				");
		System.out.println("+=======================================+");
		System.out.println(" 1. 사원조회                          		");
		System.out.println(" 2. 사원등록                          		");
		System.out.println(" 3. 사원정보 수정                       	");
		System.out.println(" 4. 사원정보 삭제                       	");
		System.out.println(" 5. 휴가신청 리스트 확인                 	");
		System.out.println(" 6. 로그아웃      	               	 		");
		System.out.println(" 7. 종료                         			");
		System.out.println("+=======================================+");
		System.out.println("번호를 입력해주세요:");
		int selectNumber = InputHelper.getInt();
		System.out.println("+---------------------------------------+");
		return selectNumber;
	}

	/**
	 * 사원정보 수정 항목 출력 및 수정 처리
	 *
	 * @param updateEmpNo 수정할 사원의 사원번호
	 */
	private void displayEditMenu(int empNo) {
		boolean stops = false;
		while (!stops) {
			int selectedNumber = showUpdateMenu();
			EmployeeVO employeeVO = getUpdateValue(selectedNumber);
			employeeVO.setEmpNo(empNo);
			int updated = employeeController.modifyEmployee(employeeVO);
			if (updated > 0) {
				stops = true;
			}
		}
		System.out.println("+---------------------------------------+");
	}

	private int showUpdateMenu() {
		System.out.println("+=======================================+");
		System.out.println("             수정목록      	       	");
		System.out.println("+=======================================+");
		System.out.println(" 1. 이름                          		");
		System.out.println(" 2. 직급                         			");
		System.out.println(" 3. 전화번호                 				");
		System.out.println("+=======================================+");
		System.out.println("번호를 입력해주세요:");
		return InputHelper.getInt();
	}

	private EmployeeVO getUpdateValue(int number) {
		EmployeeVO employeeVO = new EmployeeVO();
		switch (number) {
		case 1:
			System.out.println("새 이름을 입력해주세요.");
			employeeVO.setEmpName(InputHelper.getString());
			break;
		case 2:
			System.out.println("새 직급 입력해주세요.");
			employeeVO.setPosition(InputHelper.getString());
			break;
		case 3:
			System.out.println("새 전화번호를 입력해주세요.");
			employeeVO.setPhoneNumber(InputHelper.getString());
			break;
		}
		return employeeVO;
	}
}
