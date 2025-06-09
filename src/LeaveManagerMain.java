import model.EmployeeVO;
import view.AdminView;
import view.GeneralView;
import view.LoginView;

public class LeaveManagerMain {

	public static void main(String[] args) {
		LoginView loginView = new LoginView();
		AdminView adminView = new AdminView();
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
		System.out.println("프로그램을 종료합니다.");
//		DBConnector.dbClose(con);
	}
}
