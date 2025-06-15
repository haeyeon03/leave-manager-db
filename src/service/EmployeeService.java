package service;

import java.util.ArrayList;
import java.util.List;

import dao.EmployeeDAO;
import model.EmployeeVO;
import model.PageVO;
import model.SortVO;
import util.PageUtil;

public class EmployeeService {

	private final EmployeeDAO adminDAO;

	public EmployeeService(EmployeeDAO adminDAO) {
		this.adminDAO = adminDAO;
	}

	// 총 페이지 수
	public int getTotalPage(int pageSize) {
		int totalCount = adminDAO.countAllEmployee();
		int totalPage = PageUtil.calculateTotalPage(totalCount, pageSize);
		return totalPage;
	}

	// 사원조회
	public List<EmployeeVO> getEmployeeList(int currentPage, int pageSize, SortVO sort) {
		int totalCount = adminDAO.countAllEmployee();
		PageVO page = PageUtil.paginate(totalCount, pageSize, currentPage);
		List<EmployeeVO> employeeList = new ArrayList<>();
	
		employeeList = adminDAO.selectEmployeeList(page, sort);
		return employeeList;
	}

	// 사원삭제
	public void deleteEmployee(int employeeId) {
		int deleteLeave = adminDAO.deleteLeave(employeeId);
		if (deleteLeave == 0) {
			System.out.println("휴가 내역 삭제 실패하였습니다.");
			return;
		} else {
			int deleteEmployee = adminDAO.deleteEmployee(employeeId);
			if(deleteEmployee == 0){
				System.out.println("사원 삭제 실패하였습니다.");
				return;
			}
			System.out.println("삭제 완료되었습니다.");
		}
	}
}
