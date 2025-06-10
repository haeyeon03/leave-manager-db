package service;

import dao.LoginDAO;
import model.EmployeeVO;

public class LoginService {

	private final LoginDAO loginDAO;

	public LoginService(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

	/**
	 * 사번과 비밀번호를 검증
	 *
	 * @param input 사번, 비밀번호
	 * @return 로그인 성공 시 사원 정보(EmployeeVO), 실패 시 null 반환
	 */
	public EmployeeVO login(EmployeeVO input) {
		EmployeeVO employee = loginDAO.login(input);

		if (employee == null) {
			System.out.println("존재하지 않는 사번입니다.");
			return null;
		}

		// 비밀번호 비교
		if (!input.getPassword().equals(employee.getPassword())) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return null;
		}

		return employee;
	}
}