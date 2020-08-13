package UIWindows;
import Tools.Files.FileHandler;
import Tools.Files.FileSearch;
import Tools.Files.TextFileHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseSelectionWindows {

    private static FileHandler fileHandler;

    public  static void display(){
        try {
            fileHandler = new TextFileHandler(FileSearch.SearchForFile("CoursesDB.txt"));
        }
        catch (Exception e){AlarmBox.display("Can't access the database and create a new file handler try again");}

        Stage windows = new Stage();
        windows.setTitle("Courses information system by E.Y");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(8);
        grid.setHgap(10);
        Label label = new Label("שם קורס");
        GridPane.setConstraints(label,1,0);

        // User input box
        final ComboBox course_combobox = new ComboBox();
        ArrayList<String> courses = getAllCourses();
        for (int i = 0; i < courses.size(); i++) {
            course_combobox.getItems().add(courses.get(i));
        }
        course_combobox.setValue(courses.get(0));
        GridPane.setConstraints(course_combobox,1,1);

        Button button = new Button("חיפוש");
        button.setOnAction( e -> displayCourse(course_combobox.getValue().toString()));
        GridPane.setConstraints(button,1,2);

        Button backup_button = new Button("השתמש בגיבוי");
        backup_button.setOnAction( e -> useBackup());
        backup_button.setStyle("-fx-background-color: tomato;");
        GridPane.setConstraints(backup_button,1,10);

        grid.getChildren().addAll(label,course_combobox,button,backup_button);

        Scene scene = new Scene(grid, 400,220);
        windows.setScene(scene);
        windows.show();
    }

    /**
     * When the backup button is pressed this function will read all the data from the CourseDBBackup file
     * and load it to the main courseDB file.
     * It check with the user before executing the backup restore.
     * Only used when there is an exception in the main file.
     */
    private static void useBackup() {
        try {
            String warning = "לחיצה על הכפתור תפעיל את קובץ הגיבוי. \n" +
                    "כל המידע מקבוץ הגיבוי יועבר לקובץ הראשי.\n" +
                    "יש להשתמש באופציה זו רק במקרה שהנתונים מהקובץ הראשי גורמים לבעיות.\n" +
                    "האם אתה בטוח שזה מה שאת/ה רוצה לעשות?";
            Boolean answer = ConfirmBox.display("Confirmation windows",warning);
            if(answer) {
                FileHandler backup = new TextFileHandler(FileSearch.SearchForFile("CoursesDBBackup.txt"));
                List<String> backup_data = backup.Read();
                fileHandler.WriteToFile(new String[]{backup_data.get(0)}, false);
                for (int i = 1; i < backup_data.size(); i++)
                    fileHandler.WriteToFile(new String[]{backup_data.get(i)}, true);
                MessageBox.display("הגיבוי הועבר בהצלחה לקובץ הראשי.\n" +
                        "נסה להפעיל שוב את החיפוש על הקורס ולבדוק שהכל הסתדר");
            }
        }
        catch(IOException i){AlarmBox.display("לא הייה ניתן לגשת לקובץ הגיבוי ב-\n" +
                "Data\\Backups\\CoursesDBBackup.txt \n" + "יש צורך בבידקה שהקובץ תקין ולאחר מכן נסה שוב.");}

    }

    /**
     * This function will parse the courses db and search for the user's input course.
     * If the mentioned course was found, all the course relevant data will be stored and sent
     * to the CourseWindow class for further action
     * @param course_name -> name of a course in the db
     */
    private static void displayCourse(String course_name) {
        try{
            ArrayList<String> course  = fileHandler.ReadBlock(course_name, "==");
            CourseWindow.start(course);
        }
        catch (NullPointerException e){AlarmBox.display("The course does not exist in the database, try again");}
        catch (Exception e) {AlarmBox.display("Something went wrong, try again");}
    }

    /**
     * Used by the ComboBox to show all the available courses in the system
     * @return Array of all courses names
     */
    private static ArrayList<String> getAllCourses(){
        try {
            List<String> data = fileHandler.Read();
            ArrayList<String> courses = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if(data.get(i).contains("Course name:"))
                    courses.add(data.get(i).replace("Course name: ",""));
            }
            return courses;
        }
        catch (Exception e){AlarmBox.display("Can't read the database, something went wrong in the Read function");}

        return null;
    }

}
