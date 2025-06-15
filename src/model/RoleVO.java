package model;

public class RoleVO {
	private int number;
	private String displayName;

	public RoleVO(int number, String displayName) {
		super();
		this.number = number;
		this.displayName = displayName;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
