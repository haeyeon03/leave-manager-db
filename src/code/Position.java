package code;

import java.util.ArrayList;
import java.util.List;

import model.PositionVO;

public enum Position {
	STAFF(1, "사원"),
	ASSISTANT_MANAGER(2, "대리"),
	MANAGER(3, "과장"),
	SENIOR_MANAGER(4, "차장"),
	GENERAL_MANAGER(5, "부장")
	;

	private final int number;
	private final String displayName;

	Position(int number, String displayName) {
		this.number = number;
		this.displayName = displayName;
	}

	public int getNumber() {
		return number;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String ByNumber(int selected) {
		for (Position p : values()) {
			if (selected == p.getNumber()) {
				return p.getDisplayName();
			}
		}
		return null;
	}
	
	public static List<PositionVO> toList() {
		List<PositionVO> list = new ArrayList<>();
		for (Position p : values()) {
			list.add(new PositionVO(p.getNumber(), p.getDisplayName()));
		}
		return list;
	}
}
