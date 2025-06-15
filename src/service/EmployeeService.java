package service;

import java.util.ArrayList;
import java.util.Date;
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

	// 사원등록
	public void insertEmployee(EmployeeVO employeeVO) {
		int role = employeeVO.getMode().getRole();
		employeeVO.setRole(role);
		int count = 0;
		switch (role) {
		case 0: // 관리자
			count = adminDAO.insertEmployee("ADM_SEQ.NEXTVAL", employeeVO);
			break;
		case 1: // 일반 사원
			count = adminDAO.insertEmployee("EMP_SEQ.NEXTVAL", employeeVO);
			break;
		default:
			System.out.println("알 수 없는 역할 입니다");
			return;
		}

		if (count == 0) {
			System.out.println("사원 등록을 실패였습니다.");
			return;
		}
		System.out.println("사원 등록을 성공하였습니다.");
	}

	// 사원삭제
	public void deleteEmployee(int employeeId) {
		int deleteLeave = adminDAO.deleteLeave(employeeId);
		if (deleteLeave == 0) {
			System.out.println("휴가 내역 삭제 실패하였습니다.");
			return;
		} else {
			int deleteEmployee = adminDAO.deleteEmployee(employeeId);
			if (deleteEmployee == 0) {
				System.out.println("사원 삭제 실패하였습니다.");
				return;
			}
			System.out.println("삭제 완료되었습니다.");
		}
	}
}
