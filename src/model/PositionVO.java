package model;

public class PositionVO {
	private int number;
	private String displayName;
	
	public PositionVO(int number, String displayName) {
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
