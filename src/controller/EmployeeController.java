package controller;

import java.util.List;

import model.EmployeeVO;
import model.PageVO;
import service.EmployeeService;

public class EmployeeController {

	private final EmployeeService adminService;
	
	public EmployeeController(EmployeeService adminService){
		this.adminService=adminService;
	}

	public int getTotalPage(int pageSize) {
		int totalPage = adminService.getTotalPage(pageSize);
		return totalPage;
	}

	public List<EmployeeVO> getEmployeeList(int currentPage, int pageSize, int number) {
		List<EmployeeVO> employeeList = adminService.getEmployeeList(currentPage, pageSize, number);
		return employeeList;
	}
	
}
