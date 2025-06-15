package code;

import java.util.ArrayList;
import java.util.List;

import model.RoleVO;

public enum Role {
	ADMIN(0, "관리자"), GENERAL(1, "사용자");
	
	private final int number;
	private final String displayName;
	
	private Role(int number, String displayName) {
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
		for (Role r : values()) {
			if (selected == r.getNumber()) {
				return r.getDisplayName();
			}
		}
		return null;
	}
	
	public static List<RoleVO> toList() {
		List<RoleVO> list = new ArrayList<>();
		for (Role r : values()) {
			list.add(new RoleVO(r.getNumber(), r.getDisplayName()));
		}
		return list;
	}
}
