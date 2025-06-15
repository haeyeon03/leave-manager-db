package controller;

import java.util.List;

import code.Position;
import code.Role;
import model.EmployeeVO;
import model.LeaveRequestVO;
import model.PositionVO;
import model.RoleVO;
import model.SortVO;
import service.EmployeeService;

public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * 총 페이지 수 조회 API
	 *
	 * @param pageSize 한 페이지에 보여줄 데이터 수
	 * @return 총 페이지 수
	 */
	public int getTotalPage(int pageSize) {
		int totalPage = employeeService.getTotalPage(pageSize);
		return totalPage;
	}

	/**
	 * 사원 번호 존재 여부 확인 API
	 *
	 * @param empNo 사원번호
	 * @return 존재하면 1, 존재하지 않으면 0
	 */
	public int checkEmployee(int empNo) {
		int count = employeeService.checkEmployee(empNo);
		return count;
	}

	/**
	 * 사원 목록 조회 API
	 *
	 * @param currentPage 현재 페이지 번호
	 * @param pageSize    페이지당 항목 수
	 * @param sort        정렬 정보 객체
	 * @return 사원 목록 리스트
	 */
	public List<EmployeeVO> getEmployeeList(int currentPage, int pageSize, SortVO sort) {
		List<EmployeeVO> employeeList = employeeService.getEmployeeList(currentPage, pageSize, sort);
		return employeeList;
	}

	/**
	 * 사원 등록 API
	 *
	 * @param employeeVO 등록할 사원 정보
	 */
	public void addEmployee(EmployeeVO employeeVO) {
		employeeService.addEmployee(employeeVO);
	}

	/**
	 * 사원 정보 수정 API
	 *
	 * @param type  수정 유형 (1: 이름, 2: 직급, 3: 전화번호, 4: 연차)
	 * @param empNo 수정할 사원의 사원번호
	 * @param value 수정할 값
	 */
	public int modifyEmployee(EmployeeVO employeeVO) {
		return employeeService.modifyEmployee(employeeVO);
	}

	/**
	 * 사원 삭제
	 *
	 * @param deleteEmpNo 삭제할 사원번호
	 */
	public void removeEmployee(EmployeeVO employeeVO) {
		employeeService.deleteEmployee(employeeVO);
	}

	/**
	 * 직급 리스트 조회 API
	 * 
	 * @return List
	 */
	public List<PositionVO> getPositionList() {
		return Position.toList();
	}

	/**
	 * 역할 리스트 조회 API
	 * 
	 * @return List
	 */
	public List<RoleVO> getRoleList() {
		return Role.toList();
	}
}
