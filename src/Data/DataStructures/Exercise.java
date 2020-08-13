package Data.DataStructures;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Exercise implements Comparable{

    private  SimpleDateFormat myFormat;
    private int id;
    private Date due_date;
    private String due_date_old_format;
    private float days_left;
    private String done;
    private Date update_date;
    private String course;
    private String grade;
    private UpdatesManager updatesManager;

    public Exercise(int id, String due_date, String done,String course) throws ParseException,IOException {

        this.id = id;
        this.course = course;
        this.done = done;
        this.due_date_old_format = due_date;
        this.update_date = new Date(System.currentTimeMillis());
        this.grade = "Not Updated";
        setUpdatesManager();
        setDue_date();
    }

    public Exercise(int id, String due_date, String done, String update_date, String course,String grade) throws IOException,ParseException
    {
        this.id = id;
        this.done = done;
        this.course = course;
        this.due_date_old_format = due_date;
        this.grade = grade;
        setUpdatesManager();
        setDue_date();
        setUpdate_date(update_date);
    }

    @Override
    public int compareTo(Object o) {
        Exercise exercise = (Exercise) o;
        if(this.getDone().equals("Done") && exercise.getDone().equals("Not Done"))
            return 1;
        else if(this.getDone().equals("Not Done") && exercise.getDone().equals("Done"))
            return -1;
        else if(days_left > exercise.getDays_left())
            return 1;
        else if(days_left < exercise.getDays_left())
            return -1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(due_date_old_format, exercise.due_date_old_format) &&
                Objects.equals(course, exercise.course) &&
                Objects.equals(id, exercise.id);
    }

    public String getGrade() {
        return grade;
    }

    /**
     * Set the grade to the exercise.
     * Check the data is valid before updating
     * @param grade - the grade to update - String value
     * @throws IOException - Can't update the DB file
     * @throws ParseException - The input data in invalid
     */
    public void setGrade(String grade) throws IOException,ParseException,NumberFormatException{
        // Check the string is an int
        try {
            double d = Double.parseDouble(grade);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException();
        }

        int value = Integer.valueOf(grade);
        if(value < -2)
            throw new ParseException("Invalid grade" , 0);

        else if(value > 100)
            throw new ParseException("Invalid grade" , 0);

        else if(value == -1)
            this.grade = "Not Needed";
        else
            this.grade = grade;
        // Update the new grade and Update the db with the Update() call function
        Update();
    }

    /**
     * Set a new UpdatesManager object using the Singelton design pattern.
     * Meaning only 1 can be created for each Exercise Instance.
     */
    private void setUpdatesManager() throws IOException,ParseException{
        if (this.updatesManager == null) {
            String[] temp = course.split(" ");
            String new_format_course_name = "";
            for (int i = 0; i < temp.length - 1; i++)
                new_format_course_name += temp[i] + "-";
            new_format_course_name += temp[temp.length - 1];
            updatesManager = new UpdatesManager(id,new_format_course_name);
        }
        else
            return;

    }

    public UpdatesManager getUpdatesManager() {
        return updatesManager;
    }

    private void setUpdate_date(String update_date) throws ParseException {
        myFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        this.update_date = myFormat.parse(update_date);

    }

    public void setUpdate_date(boolean doneUpdate) throws IOException{
        this.update_date = new Date(System.currentTimeMillis());
        // If the new Update was by the DoneUpdate no need to update the db again.
        if(!doneUpdate)
            Update();
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourse(){return course;}

    public String getDue_date() {
        return due_date.toString();
    }

    public float getDays_left() {
        return days_left;
    }

    public void setDue_date() throws ParseException {
        String[] temp = due_date_old_format.split("/");
        validateDays(temp[0]);
        validateMonth(temp[1]);
        validateYear(temp[2]);
        myFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.due_date = myFormat.parse(due_date_old_format);
        setDays_left();
    }

    private void validateDays(String days) throws ParseException{
        int day = Integer.parseInt(days);
        if(day<1 || day > 31)
            throw new ParseException("Days needs to be between 1 to 31",1);
    }

    private void validateMonth(String months) throws ParseException{
        int month = Integer.parseInt(months);
        if(month<1 || month > 12)
            throw new ParseException("Month needs to be between 1 to 12",4);
    }
    private void validateYear(String years) throws ParseException{
        if(years.length() < 4)
            throw new ParseException("Year needs to be 4 numbers",7);
    }

    private void setDays_left() {
        Date now = new Date(System.currentTimeMillis());
        long diff = this.due_date.getTime() - now.getTime();
        this.days_left = (diff / (1000*60*60*24));
        this.days_left = (int)days_left;
    }

    public String getDue_date_old_format() {
        return due_date_old_format;
    }

    public String getDone() {
        return done;
    }

    public void setDone(boolean done) throws IOException{
        if(done)
            this.done = "Done";

        else
            this.done = "Not Done";

        setUpdate_date(true);
        Update();
    }

    public static int compareByGrade(Exercise o1, Exercise o2){
        if(o1.getGrade().equals("Not Needed"))
            return 1;
        else if(o2.getGrade().equals("Not Needed"))
            return -1;
        else if(o1.getGrade().equals("Not Updated"))
            return 1;
        else if(o2.getGrade().equals("Not Updated"))
            return -1;
        else if(Integer.valueOf(o1.getGrade()) < Integer.valueOf(o2.getGrade()))
            return 1;
        else if(Integer.valueOf(o1.getGrade()) > Integer.valueOf(o2.getGrade()))
            return -1;
        else
            return 0;
    }

    public void Update() throws IOException {
        CoursesToWorkSheetDictionary.UpdateDB(this, true);
    }
}
