package Class;

public class Tutor extends StaffMember {

    public Tutor(String first_name, String last_name, String office_hours, String email) {
        super(first_name, last_name, office_hours, email);
    }

    @Override
    public String toString() {
        return "Tutor Details: " +
                "first_name is " + first_name +
                ", last_name is " + last_name  +
                ", office_hours are " + office_hours +
                ", email is " + email;
    }
}
