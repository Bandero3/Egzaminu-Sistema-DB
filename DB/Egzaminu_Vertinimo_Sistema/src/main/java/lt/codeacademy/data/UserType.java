package lt.codeacademy.data;

public enum UserType {
    STUDENT(0, "STUDENT"),
    TEACHER(1, "TEACHER");

    private final int roleCode;
    private final String roleName;

    UserType(int roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public int getRoleCode() {
        return roleCode;
    }

    public String getRoleName() {
        return roleName;
    }
}

