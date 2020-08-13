package UIWindows;

import Tools.Commands.OSCommand;
import Tools.Files.BackupFileHandler;
import Tools.Files.CsvFileHandler;
import Tools.Files.FileSearch;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image ;

public class UI extends Application {
/*
This class is used for the user interface.
It will have instances for each course with all of its data.
 */

    Button course_button,secretariat_button,close,exercises_button, backup_button,calender_button;

    Stage window;
    Scene scene1, scene2;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setPrefWidth(10);

        Label label1 = new Label("Welcome!\n" +
                "המערכת נועדה להציג מידע"  +
                " על הקורסים השונים בסמסטר זה" + "\n" +
                "ולעזור לכם במעקב אחר תרגילים, הרצאות ותרגולים." + "\n" +
                "תהנו חברים!");
        label1.setFont(new Font("Arial", 14));
        GridPane.setConstraints(label1,1,0);
        Image image = new Image("File:"+FileSearch.SearchForFile("logo_ise.jpg"));
        ImageView imageView = new ImageView(image);
        GridPane.setConstraints(imageView,0,0);

        // Course db Button configuration
        course_button = new Button();
        course_button.setText("מידע על קורס מסויים");
        course_button.setOnAction(e -> CourseSelectionWindows.display());
        course_button.setMinWidth(grid.getPrefWidth());
        course_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        course_button.setStyle("-fx-background-color: #a6b5c9");
        Label course_label = new Label("לקבלת המידע על קורס \n" +
                "כולל לוח למעקב תרגילים, סילבוס ועוד,\n" +
                "לחץ על קבלת מידע על קורס");
        course_label.setFont(new Font("Arial", 15));
        GridPane.setConstraints(course_label,0,1);
        GridPane.setConstraints(course_button,1,1);

        // Secretariat db Button configuration
        secretariat_button = new Button();
        secretariat_button.setText("מזכירות");
        secretariat_button.setOnAction(e -> SecretariesWindow.display());
        secretariat_button.setMinWidth(grid.getPrefWidth());
        secretariat_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        secretariat_button.setStyle("-fx-background-color: #a6b5c9");
        Label sec_label = new Label("לקבלת מידע על כל המזכירות \n" +
                "כגון: שם, מייל, מיקום וכו' לחץ על כפתור המזכירות");
        sec_label.setFont(new Font("Arial", 15));
        GridPane.setConstraints(sec_label,0,2);
        GridPane.setConstraints(secretariat_button,1,2);

        exercises_button = new Button();
        exercises_button.setText("גיליון תרגילים");
        exercises_button.setOnAction(e -> ExercisesTableWindow.start());
        exercises_button.setMinHeight(grid.getPrefWidth());
        exercises_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        exercises_button.setStyle("-fx-background-color: #a6b5c9");
        Label exercises_label = new Label("לקבלת גיליון המציג את כל התרגילים לחץ על גיליון תרגילים. \n" +
                "שימו לב: התרגילים ממויינים לפי מספר הימים עד ליום ההגשה.");
        exercises_label.setFont(new Font("Arial", 15));
        GridPane.setConstraints(exercises_label,0,3);
        GridPane.setConstraints(exercises_button,1,3);

        // Calender button
        calender_button = new Button();
        calender_button.setText("לוח שנה אקדמאי");
        calender_button.setOnAction(e ->openCalender());
        calender_button.setMinHeight(grid.getPrefWidth());
        calender_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        calender_button.setStyle("-fx-background-color: #a6b5c9");
        Label calender_label = new Label("לצפייה בלוח השנה האקדמאי של 2019-2020");
        calender_label.setFont(new Font("Arial", 15));
        GridPane.setConstraints(calender_label,0,4);
        GridPane.setConstraints(calender_button,1,4);

        // Backup update Button configuration
        backup_button = new Button("לעדכון קובץ גיבוי הנתונים של התרגילים, לחץ פה");
        backup_button.setMinWidth(grid.getPrefWidth());
        backup_button.setStyle("-fx-background-color: #a6b5c9");
        backup_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        backup_button.setOnAction(e->checkUpdateAndMainFiles());
        Label backup_label = new Label("כפתור זה נועד כדי לעדכן את כל הנתונים על תרגילים חדשים שנוספו \n" +
                "למערכת מהקובץ הראשי לקובץ הגיבוי.\n" +
                "כדאי לעשות זאת בסוף עדכון לפני יציאה");
        backup_label.setFont(new Font("Arial", 15));
        GridPane.setConstraints(backup_label,0,5);
        GridPane.setConstraints(backup_button,1,5);


        // Exit Button configuration
        close = new Button("ליציאה מהתוכנה, לחץ פה");
        close.setMinWidth(grid.getPrefWidth());
        close.setStyle("-fx-background-color: #a6b5c9");
        close.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        close.setOnAction(e->closeProgram());
        GridPane.setConstraints(close,1,6);

        // Layout Main configuration
        grid.getChildren().addAll(label1,imageView,course_label,course_button,sec_label,secretariat_button,exercises_label,
                exercises_button,backup_label, backup_button,close, calender_button,calender_label);
        grid.setStyle("-fx-background-color: white");
        scene1 = new Scene(grid, 850,550);
        window.setScene(scene1);
        window.setTitle("Information system by Eden Yavin");
        window.show();
    }

    /**
     * Used to open the calender of the academic year located on
     * src/Data/Calender/Calender.pdf
     */
    private void openCalender() {
        String main = FileSearch.SearchForFile("Calender.pdf");
        OSCommand.Run("cmd /c \""+ main +"\"" );
    }

    /**
     * This function will be used by the user to update the backup file
     * With any new data added.
     */
    private void checkUpdateAndMainFiles() {
        BackupFileHandler backupFileHandler = new BackupFileHandler();
        CsvFileHandler main_file = new CsvFileHandler(FileSearch.SearchForFile("ExercisesDataBase.csv"));
        try {
            if (!backupFileHandler.CompareMainAndBackup(main_file)) {
                backupFileHandler.writeFromMainToBackup("ExercisesDataBase.csv");
                MessageBox.display("עדכון המידע בוצע בהצלחה!");
            }
            else
                MessageBox.display("המידע כרגע בקובץ הגיבוי עדכני, אין צורך בעדכון");
        }
        catch (Exception b){AlarmBox.display("Can't Main file has invalid data. backup failed");}
    }

    private void closeProgram(){
        Boolean answer = ConfirmBox.display("Confirmation windows","Are you sure?");
        if(answer){
            window.close();
            System.exit(0);
        }
    }

    public static void Restrart(){
        String main = FileSearch.SearchForFile("ClickThis.bat");
        OSCommand.Run("cmd /c \""+ main +"\"" );
        System.exit(1);
    }

}
