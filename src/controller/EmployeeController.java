package controller;

import java.util.List;

import model.EmployeeVO;
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

	// 사원 번호 체크
	public int checkEmployee(int empNo) {
		int count = adminService.checkEmployee(empNo);
		return count;
	}

	// 사원조회
	public List<EmployeeVO> getEmployeeList(int currentPage, int pageSize, SortVO sort) {
		List<EmployeeVO> employeeList = adminService.getEmployeeList(currentPage, pageSize, sort);
		return employeeList;
	}

	// 사원등록
	public void insertEmployee(EmployeeVO employeeVO) {
		adminService.insertEmployee(employeeVO);
	}

	// 사원수정
	public void updateEmployee(int editNumber,int updateEmpNo, Object updateInput) {
		adminService.updateEmployee(editNumber, updateEmpNo, updateInput);
	}

	// 사원삭제
	public void deleteEmployee(int deleteEmpNo) {
		adminService.deleteEmployee(deleteEmpNo);
	}

}
