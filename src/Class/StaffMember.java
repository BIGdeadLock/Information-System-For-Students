package Class;

public abstract class StaffMember {

    protected String first_name;
    protected String last_name;
    protected String office_hours;
    protected String email;

    public StaffMember(String first_name, String last_name, String office_hours, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.office_hours = office_hours;
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFull_name(){
        return first_name + " " + last_name;
    }

    public String getOffice_hours() {
        return office_hours;
    }

    public String getEmail() {
        return email;
    }

    public void setOffice_hours(String office_hours) {
        this.office_hours = office_hours;
    }

}
