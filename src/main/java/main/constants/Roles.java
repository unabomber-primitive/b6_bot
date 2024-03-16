package main.constants;

public enum Roles {
    DORM_DIRECTOR("Заведующий общежитием"),
    MAIN_ENGINEER("Главный инженер"),
    STUDENT("Студент"),
    EMPLOYEE("Работник");

    private String role;
    Roles(String s) {
        role = s;
    }

    public String getRole() {
        return role;
    }
}
