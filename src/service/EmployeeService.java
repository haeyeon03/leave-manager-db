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

	// 사원번호 체크
	public int checkEmployee(int empNo) {
		int count = adminDAO.checkEmployee(empNo);
		return count;
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
		int role = employeeVO.getRole();
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
			System.out.println("[API ERROR] 알 수 없는 역할 입니다");
			return;
		}

		if (count == 0) {
			System.out.println("[API ERROR] 사원 등록을 실패였습니다.");
			return;
		}
		System.out.println("[API SUCCESS] 사원 등록을 성공하였습니다.");
	}

	// 사원수정
	public void updateEmployee(int editNumber, int updateEmpNo, Object updateInput) {
		int count = 0;
		EmployeeVO employeeVO = new EmployeeVO();
		switch (editNumber) {
		// 이름 수정
		case 1:
			count = adminDAO.updateEmployee("EMP_NAME", updateInput, updateEmpNo);
			break;
		// 직급 수정
		case 2:
			count = adminDAO.updateEmployee("POSITION", updateInput,updateEmpNo);
			break;
		// 전화번호 수정
		case 3:
			count = adminDAO.updateEmployee("PHONE_NUMBER", updateInput,updateEmpNo);
			break;
		// 연차갯수 수정
		case 4:
			count = adminDAO.updateLeaveDays(updateInput,updateEmpNo);
			break;
		default:
			System.out.println("[API ERROR] 존재하지 않는 번호 입니다");
			return;
		}

		if (count == 0) {
			System.out.println("[API ERROR] 사원 수정을 실패였습니다.");
			return;
		}
		System.out.println("[API SUCCESS] 사원 수정을 성공하였습니다.");
	}

	// 사원삭제
	public void deleteEmployee(int deleteEmpNo) {
		int deleteLeave = adminDAO.deleteLeave(deleteEmpNo);
		if (deleteLeave == 0) {
			System.out.println("[API ERROR] 휴가 내역 삭제 실패하였습니다.");
			return;
		} else {
			int deleteEmployee = adminDAO.deleteEmployee(deleteEmpNo);
			if (deleteEmployee == 0) {
				System.out.println("[API ERROR] 사원 삭제 실패하였습니다.");
				return;
			}
			System.out.println("[API SUCCESS] 삭제 성공하였습니다.");
		}
	}
}
