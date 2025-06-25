import config.DBConnector;
import controller.EmployeeController;
import controller.LeaveController;
import controller.LoginController;
import dao.EmployeeDAO;
import dao.LeaveDAO;
import dao.LoginDAO;
import model.EmployeeVO;
import service.EmployeeService;
import service.LeaveService;
import service.LoginService;
import view.AdminView;
import view.GeneralView;
import view.LoginView;

public class LeaveManagerMain {

	public static void main(String[] args) {
		try {
			// Backend
			// DB 연결
			DBConnector.openConnection();

			// 로그인 API
			LoginDAO loginDAO = new LoginDAO();
			LoginService loginService = new LoginService(loginDAO);
			LoginController loginController = new LoginController(loginService);

			// 사원 API
			EmployeeDAO employeeDAO = new EmployeeDAO();
			EmployeeService employeeService = new EmployeeService(employeeDAO);
			EmployeeController employeeController = new EmployeeController(employeeService);

			// 휴가 신청 API
			LeaveDAO leaveveDAO = new LeaveDAO();
			LeaveService leaveService = new LeaveService(leaveveDAO);
			LeaveController leaveController = new LeaveController(leaveService);

			// View
			LoginView loginView = new LoginView(loginController);
			AdminView adminView = new AdminView(employeeController,leaveController);
			GeneralView generalView = new GeneralView(leaveController);

			boolean stops = false;

			while (!stops) {
				// 로그인
				EmployeeVO employee = loginView.login();

				if (employee == null) {
					continue;
				}
				// 로그인한 사원의 role 에 따라 분기처리
				switch (employee.getRole()) {
				case 0:
					stops = adminView.run();
					break;
				case 1:
					stops = generalView.run(employee);
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
