import config.DBConnector;
import controller.EmployeeController;
import controller.LoginController;
import dao.EmployeeDAO;
import dao.LoginDAO;
import model.EmployeeVO;
import service.EmployeeService;
import service.LoginService;
import view.AdminView;
import view.GeneralView;
import view.LoginView;

public class LeaveManagerMain {

	public static void main(String[] args) {
		try {
			// DB 연결
			DBConnector.openConnection();

			// 로그인
			LoginDAO loginDAO = new LoginDAO();
			LoginService loginService = new LoginService(loginDAO);
			LoginController loginController = new LoginController(loginService);
			LoginView loginView = new LoginView(loginController);
			
			// 사원
			EmployeeDAO employeeDAO = new EmployeeDAO();
			EmployeeService employeeService = new EmployeeService(employeeDAO);
			EmployeeController employeeController = new EmployeeController(employeeService);
			AdminView adminView = new AdminView(employeeController);
			
			// 사용자
			GeneralView generalView = new GeneralView();
			
			boolean stops = false;
			
			while(!stops) {
				// 로그인
				EmployeeVO employee = loginView.login();
				
				if (employee == null) {
					continue;
				}
				
				// 로그인한 사원의 role 에 따라 분기처리
				switch(employee.isRole()) {
					case 0:
						stops = adminView.run();
						break;
					case 1:
						stops = generalView.run();
						break;
				}
			}
		} catch (Exception e) {
			System.out.println("[ERROR] " + e.toString());
		} finally {
			DBConnector.closeConnection();
			System.out.println("프로그램을 종료합니다.");
		}
	}
}
