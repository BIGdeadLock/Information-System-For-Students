package Data.DataStructures;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseUpdate {
    /**
     * This class Wrap the data of an exercise's update
     * Updates include: Number, Text, Date
     */
    private int update_number;
    private String Text;
    private Date update_date;
    private DateFormat df = new SimpleDateFormat("dd/MM/yy");

    public ExerciseUpdate(int update_number, String text) {
        this.update_number = update_number;
        Text = text;
        update_date = new Date();
    }

    public ExerciseUpdate(int update_number, String text,String update_Date) throws ParseException {
        this.update_number = update_number;
        Text = text;
        update_date = new Date();
        this.update_date = df.parse(update_Date);
    }
    /**
     * Get the update date in dd/MM/yy HH:mm:ss format
     * @return String Object in the above format
     */
    public String getUpdate_date() {
        return df.format(update_date);
    }

    public void setUpdate_date() {
        this.update_date = new Date();
    }

    public int getUpdate_number() {
        return update_number;
    }

    public void setUpdate_number(int update_number) {
        this.update_number = update_number;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }


}
