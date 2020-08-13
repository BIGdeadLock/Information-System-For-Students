package Class;

public class Lecturer extends StaffMember{

    public Lecturer(String first_name, String last_name, String office_hours, String email) {
        super(first_name, last_name, office_hours, email);
    }

    public String toString() {
        return "Lecturer Details: " + '\'' +
                "first_name is " + first_name + '\'' +
                ", last_name is " + last_name + '\'' +
                ", office_hours are: " + office_hours + '\'' +
                ", email is " + email;
    }
}