package model;

import java.time.LocalDate;

public class LeaveByEmployeeVO {
	private int empNo;
	private String empName;
	private int reqNo;
	private String leaveType;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reason;

	public LeaveByEmployeeVO() {
		super();
	}

	public LeaveByEmployeeVO(int empNo, String empName, int reqNo, String leaveType, LocalDate startDate,
			LocalDate endDate, String reason) {
		super();
		this.empNo = empNo;
		this.empName = empName;
		this.reqNo = reqNo;
		this.leaveType = leaveType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
	}

	public int getEmpNo() {
		return empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public int getReqNo() {
		return reqNo;
	}

	public void setReqNo(int reqNo) {
		this.reqNo = reqNo;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "LeaveByEmployeeVO [empNo=" + empNo + ", empName=" + empName + ", reqNo=" + reqNo + ", leaveType="
				+ leaveType + ", startDate=" + startDate + ", endDate=" + endDate + ", reason=" + reason
				+ ", getEmpNo()=" + getEmpNo() + ", getEmpName()=" + getEmpName() + ", getReqNo()=" + getReqNo()
				+ ", getLeaveType()=" + getLeaveType() + ", getStartDate()=" + getStartDate() + ", getEndDate()="
				+ getEndDate() + ", getReason()=" + getReason() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
}
