package service;

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

	public List<EmployeeVO> getEmployeeList(int currentPage, int pageSize) {
		int totalCount = adminDAO.countAllEmployee();
		PageVO pageVO = PageUtil.paginate(totalCount, pageSize, currentPage);
		
		List<EmployeeVO> employeeList = adminDAO.getEmployeeList(pageVO);
		return employeeList;
	}

}
