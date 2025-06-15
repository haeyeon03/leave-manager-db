package service;

import java.util.ArrayList;
import java.util.List;

import dao.EmployeeDAO;
import model.EmployeeVO;
import model.LeaveRequestVO;
import model.PageVO;
import model.SortVO;
import util.PageUtil;

public class EmployeeService {

	private final EmployeeDAO employeeDAO;

	public EmployeeService(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	/**
	 * 전체 사원 수를 기준으로 총 페이지 수를 계산
	 * 
	 * @param pageSize 한 페이지에 표시할 항목 수
	 * @return 총 페이지 수
	 */
	public int getTotalPage(int pageSize) {
		int totalCount = employeeDAO.countAllEmployee();
		int totalPage = PageUtil.calculateTotalPage(totalCount, pageSize);
		return totalPage;
	}

	/**
	 * 사원번호의 중복 여부를 확인
	 * 
	 * @param empNo 확인할 사원번호
	 * @return 존재하면 1, 없으면 0 반환
	 */
	public int checkEmployee(int empNo) {
		int count = employeeDAO.selectEmployee(empNo);
		return count;
	}

	/**
	 * 사원 목록을 페이징 및 정렬 조건에 맞게 조회
	 * 
	 * @param currentPage 현재 페이지 번호
	 * @param pageSize    한 페이지에 표시할 항목 수
	 * @param sort        정렬 조건 객체
	 * @return 조회된 사원 목록
	 */
	public List<EmployeeVO> getEmployeeList(int currentPage, int pageSize, SortVO sort) {
		int totalCount = employeeDAO.countAllEmployee();
		PageVO page = PageUtil.paginate(totalCount, pageSize, currentPage);
		List<EmployeeVO> employeeList = new ArrayList<>();

		employeeList = employeeDAO.selectEmployeeList(page, sort);
		return employeeList;
	}

	/**
	 * 사원을 등록 역할(role)에 따라 시퀀스를 분기 등록 후 연차 정보도 함께 등록
	 * 
	 * @param employeeVO 등록할 사원 정보
	 */
	public int addEmployee(EmployeeVO employeeVO) {
		int inserted = 0;
		int empNo = employeeDAO.insertEmployee(employeeVO);
		if (empNo > 0) {
			inserted = employeeDAO.insertLeave(employeeVO, empNo);
			if (inserted > 0) {
				System.out.println("[API INFO] 사원 및 휴가 등록을 성공하였습니다.");
			} else {
				System.out.println("[API ERROR] 휴가 등록을 실패였습니다.");
			}
		} else {
			System.out.println("[API ERROR] 사원 등록을 실패하였습니다.");
		}
		return inserted;
	}

	/**
	 * 사원의 정보를 수정 수정 항목은 editNumber 값에 따라 분기처리
	 * 
	 * @param type  수정 유형 (1: 이름, 2: 직급, 3: 전화번호, 4: 연차갯수)
	 * @param empNo 수정할 사원번호
	 * @param value 수정할 값
	 */
	public int modifyEmployee(EmployeeVO employeeVO) {
		int updated = employeeDAO.updateEmployee(employeeVO);
		if (updated == 0) {
			System.out.println("[API ERROR] 사원 수정을 실패였습니다.");
		} else {
			System.out.println("[API INFO] 수정을 성공하였습니다.");
		}
		return updated;
	}

	/**
	 * 연차 정보와 사원 정보를 순서대로 삭제
	 * 
	 * @param deleteEmpNo 삭제할 사원번호
	 */
	public void deleteEmployee(EmployeeVO employeeVO) {
		int deleteLeave = employeeDAO.deleteLeave(employeeVO);
		if (deleteLeave == 0) {
			System.out.println("[API ERROR] 휴가 내역 삭제 실패하였습니다.");
			return;
		} else {
			int deleteRequest = employeeDAO.deleteRequest(employeeVO);
			if (deleteRequest == 0) {
				System.out.println("[API ERROR] 휴가 신청 내역 삭제 실패하였습니다.");
				return;
			} else {
				int deleteEmployee = employeeDAO.deleteEmployee(employeeVO);
				if (deleteEmployee == 0) {
					System.out.println("[API ERROR] 사원 삭제 실패하였습니다.");
					return;
				}
			}
			System.out.println("[API INFO] 삭제 성공하였습니다.");
		}
	}
}
