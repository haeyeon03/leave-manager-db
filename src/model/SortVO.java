package model;

public class SortVO {
	private String field;
	private String orderBy;

	public SortVO(String field, String orderBy) {
		this.field = field;
		this.orderBy = orderBy;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
