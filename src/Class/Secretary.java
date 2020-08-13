package Class;

public class Secretary extends StaffMember{

    private int building_number;

    public Secretary(String first_name, String last_name, String email, int building_number) {
        super(first_name, last_name, "09:00-17:00", email);
        this.building_number = building_number;
    }

    @Override
    public String toString() {
        return "Secretary Details: " + '\'' +
                "first_name is " + first_name + '\'' +
                ", last_name is " + last_name + '\'' +
                ", office_hours are: " + office_hours + '\'' +
                ", email is " + email;
    }
}
