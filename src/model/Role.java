package model;

public enum Role {
    ADMIN("ADMIN", "관리자", 0),
    GENERAL("GENERAL", "일반 사원", 1);

    private final String code;
    private final String displayName;
    private final int role;

    Role(String code, String displayName, int role) {
        this.code = code;
        this.displayName = displayName;
        this.role = role;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRole() {
        return role;
    }

    public static Role fromChoice(int choice) {
        Role[] values = Role.values();
        if (choice < 1 || choice > values.length) {
            throw new IllegalArgumentException("올바르지 않은 선택입니다.");
        }
        return values[choice - 1];
    }
}

