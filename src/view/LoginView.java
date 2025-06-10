package view;

import controller.LoginController;
import model.EmployeeVO;
import util.InputUtil;

public class LoginView {
	private final LoginController loginController;
	
	public LoginView (LoginController loginController) {
		this.loginController = loginController;
	}
	
	/**
	 * 로그인 요청 메서드
	 * 
	 * @return EmployeeVO 로그인한 사원 정보
	 */
	public EmployeeVO login() {
		EmployeeVO input = displayLoginInput();
		EmployeeVO employee = loginController.login(input);
		return employee;
	}

	/**
	 * 콘솔에 로그인을 위한 사번과 비밀번호를 입력하도록 표시
	 * 
	 * @return EmployeeVO 사번, 비밀번호
	 */
	public EmployeeVO displayLoginInput() {
		System.out.println("+---------------------------------------+");
		System.out.println("           	  로그인      	      		");
		System.out.println("+---------------------------------------+");
		System.out.println("사번을 입력해주세요:");
		int empNo = InputUtil.getInt();
		System.out.println("비밀번호를 입력해주세요:");
		String password = InputUtil.getString();
		System.out.println("+---------------------------------------+");
		return new EmployeeVO(empNo, password);
	}
}
