package model;

import java.time.LocalDate;

public class EmployeeVO {
	private int empNo;
	private String password;
	private String empName;
	private String position;
	private LocalDate birthDate;
	private LocalDate hiredDate;
	private String phoneNumber;
	private int role;

	public EmployeeVO() {
		super();
	}

	public EmployeeVO(int empNo, String password) {
		super();
		this.empNo = empNo;
		this.password = password;
	}

	public EmployeeVO(int empNo, String password, String empName, String position, LocalDate birthDate,
			LocalDate hiredDate, String phoneNumber, int role) {
		super();
		this.empNo = empNo;
		this.password = password;
		this.empName = empName;
		this.position = position;
		this.birthDate = birthDate;
		this.hiredDate = hiredDate;
		this.phoneNumber = phoneNumber;
		this.role = role;
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

	public LocalDate getHiredDate() {
		return hiredDate;
	}

	public void setHiredDate(LocalDate hiredDate) {
		this.hiredDate = hiredDate;
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

	@Override
	public String toString() {
		return "UserVO [empNo=" + empNo + ", password=" + password + ", empName=" + empName + ", position=" + position
				+ ", birthDate=" + birthDate + ", hiredDate=" + hiredDate + ", phoneNumber=" + phoneNumber + ", role="
				+ role + "]";
	}

}
