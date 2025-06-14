package model;

public class PageVO {

	private int startNumber;
	private int endNumber;

	public PageVO() {
		super();
	}

	public PageVO(int startNumber, int endNumber) {
		this.startNumber = startNumber;
		this.endNumber = endNumber;
	}

	public int getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(int startNumber) {
		this.startNumber = startNumber;
	}

	public int getEndNumber() {
		return endNumber;
	}

	public void setEndNumber(int endNumber) {
		this.endNumber = endNumber;
	}

}
