package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeVO {
	private int empNo;
	private String password;
	private String empName;
	private String position;
	private LocalDate birthDate;
	private LocalDate hireDate;
	private String phoneNumber;
	private int role;
	private int remainingDays;
	private List<LeaveRequestVO> leaveRequests = new ArrayList<>();
	
	public void addLeaveReques(LeaveRequestVO leaveRequest) {
		leaveRequests.add(leaveRequest);
	}

	public EmployeeVO() {
		super();
	}

	public EmployeeVO(int empNo, String password) {
		super();
		this.empNo = empNo;
		this.password = password;
	}

	public EmployeeVO(int empNo, String password, String empName, String position, LocalDate birthDate,
			LocalDate hireDate, String phoneNumber, int role) {
		super();
		this.empNo = empNo;
		this.password = password;
		this.empName = empName;
		this.position = position;
		this.birthDate = birthDate;
		this.hireDate = hireDate;
		this.phoneNumber = phoneNumber;
	}

	public EmployeeVO(int empNo, String password, String empName, String position, LocalDate birthDate,
			LocalDate hireDate, String phoneNumber, int role, int remainingDays) {
		super();
		this.empNo = empNo;
		this.password = password;
		this.empName = empName;
		this.position = position;
		this.birthDate = birthDate;
		this.hireDate = hireDate;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.remainingDays = remainingDays;
	}
	public int getEmpNo() {
		return empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int isRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
	
	
	public int getRemainingDays() {
		return remainingDays;
	}

	public void setRemainingDays(int remainingDays) {
		this.remainingDays = remainingDays;
	}

	public List<LeaveRequestVO> getLeaveRequests() {
		return leaveRequests;
	}

	public void setLeaveRequests(List<LeaveRequestVO> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}


	@Override
	public String toString() {
		return "EmployeeVO [empNo=" + empNo + ", password=" + password + ", empName=" + empName + ", position="
				+ position + ", birthDate=" + birthDate + ", hireDate=" + hireDate + ", phoneNumber=" + phoneNumber
				+ ", role=" + role + "]";
	}

}
