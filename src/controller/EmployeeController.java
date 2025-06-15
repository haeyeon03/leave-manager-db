package controller;

import java.util.List;

import model.EmployeeVO;
import model.PageVO;
import model.SortVO;
import service.EmployeeService;

public class EmployeeController {

	private final EmployeeService adminService;

	public EmployeeController(EmployeeService adminService) {
		this.adminService = adminService;
	}

	// 총 페이지 수
	public int getTotalPage(int pageSize) {
		int totalPage = adminService.getTotalPage(pageSize);
		return totalPage;
	}

	// 사원조회
	public List<EmployeeVO> getEmployeeList(int currentPage, int pageSize, SortVO sort) {
		List<EmployeeVO> employeeList = adminService.getEmployeeList(currentPage, pageSize, sort);
		return employeeList;
	}

	// 사원삭제
	public void deleteEmployee(int employeeId) {
		adminService.deleteEmployee(employeeId);
	}

}
