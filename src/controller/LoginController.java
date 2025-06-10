package controller;

import model.EmployeeVO;
import service.LoginService;

public class LoginController {

	private final LoginService loginService;

	public LoginController (LoginService loginService){
		this.loginService = loginService;
	}

	/**
	 * 로그인 API
	 *
	 * @param input 사번, 비밀번호
	 * @return 로그인 성공 시 사원 정보(EmployeeVO), 실패 시 null 반환
	 */
	public EmployeeVO login(EmployeeVO input) {
		// TODO. 생성자 주입으로 변경
		EmployeeVO employee = loginService.login(input);
		return employee;
	}
}
