package service;

import java.util.ArrayList;
import java.util.List;

import dao.EmployeeDAO;
import model.EmployeeVO;
import model.PageVO;
import util.PageUtil;

public class EmployeeService {

	private final EmployeeDAO adminDAO;

	public EmployeeService(EmployeeDAO adminDAO) {
		this.adminDAO = adminDAO;
	}

	public int getTotalPage(int pageSize) {
		int totalCount = adminDAO.countAllEmployee();
		int totalPage = PageUtil.calculateTotalPage(totalCount, pageSize);
		return totalPage;
	}

	public List<EmployeeVO> getEmployeeList(int currentPage, int pageSize, int number) {
		int totalCount = adminDAO.countAllEmployee();
		PageVO pageVO = PageUtil.paginate(totalCount, pageSize, currentPage);
		List<EmployeeVO> employeeList = new ArrayList<>();
		switch (number) {
		case 1:
			employeeList = adminDAO.getEmployeeList(pageVO,"E.EMP_NO ASC");
			break;
		case 3:
			employeeList = adminDAO.getEmployeeList(pageVO,"E.HIRE_DATE ASC");
			break;
		case 4:
			employeeList = adminDAO.getEmployeeList(pageVO,"E.HIRE_DATE DESC");
			break;
		}
		return employeeList;
	}

}
