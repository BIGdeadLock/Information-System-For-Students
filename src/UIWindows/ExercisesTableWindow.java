package UIWindows;

import Data.DataStructures.CourseWorksSheet;
import Data.DataStructures.CoursesToWorkSheetDictionary;
import Data.DataStructures.Exercise;
import Data.Exercises.AssignmentExecuter;
import Errors.UseBackupFailureExecption;
import Tools.Files.BackupFileHandler;
import Tools.Files.CsvFileHandler;
import Tools.Files.FileHandler;
import Tools.Files.FileSearch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ExercisesTableWindow {

    private static BackupFileHandler backupFileHandler = new BackupFileHandler();

    public static void start() {
        TableView<Exercise> table = new TableView<>();;
        Stage windows = new Stage();
        windows.setTitle("גיליון עבודות");

        final Label title1 = new Label("ירוק - התרגיל בוצע");
        final Label title2 = new Label("אדום - נשארו פחות מ-4 ימים להגשה");
        final Label title3 = new Label("כתום - נשארו בין 5 ל-10 ימים עד ההגשה");
        final Label title4 = new Label("כחול - נשארו יותר מ-10 ימים עד ההגשה. אפשר להיות רגוע");
        title1.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 17; -fx-text-fill: darkred");
        title2.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 17; -fx-text-fill: darkred");
        title3.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 17; -fx-text-fill: darkred");
        title4.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 17; -fx-text-fill: darkred");

        // Course column
        TableColumn<Exercise, Integer> Course_coulmn = new TableColumn<>("קורס");
        Course_coulmn.setMinWidth(70);
        Course_coulmn.setPrefWidth(70);
        Course_coulmn.setCellValueFactory(new PropertyValueFactory<>("course"));

        // Id column
        TableColumn<Exercise, Integer> Id_coulmn = new TableColumn<>("מס' עבודה");
        Id_coulmn.setMinWidth(40);
        Id_coulmn.setPrefWidth(40);
        Id_coulmn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Due Date column
        TableColumn<Exercise, String> due_date_column = new TableColumn<>("תאריך הגשה");
        due_date_column.setMinWidth(120);
        due_date_column.setPrefWidth(120);
        due_date_column.setCellValueFactory(new PropertyValueFactory<>("due_date"));

        //Days Left column
        TableColumn<Exercise, String> days_left_column = new TableColumn<>("ימים שנשארו");
        days_left_column.setMinWidth(50);
        days_left_column.setCellValueFactory(new PropertyValueFactory<>("days_left"));

        // Grade column
        TableColumn<Exercise, String> grade_column = new TableColumn<>("ציון עבודה");
        grade_column.setMinWidth(65);
        grade_column.setPrefWidth(65);
        grade_column.setCellValueFactory(new PropertyValueFactory<>("grade"));

        //Done or not column
        TableColumn<Exercise, String> done_column = new TableColumn<>("בוצע");
        done_column.setMinWidth(25);
        done_column.setPrefWidth(25);
        done_column.setCellValueFactory(new PropertyValueFactory<>("done"));

        ObservableList<Exercise> exercises = getSortedWorkSheet();

        //Buttons
        Button doneUpdateButton = new Button("סימון עבודה כבוצעה");
        doneUpdateButton.setOnAction(e -> doneUpdateButtonButtonClicked(table,true));
        doneUpdateButton.setStyle("-fx-background-color: #a6b5c9");
        doneUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Button UndoneUpdateButton = new Button("סימון עבודה כלא בוצעה");
        UndoneUpdateButton.setOnAction(e -> doneUpdateButtonButtonClicked(table,false));
        UndoneUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        UndoneUpdateButton.setStyle("-fx-background-color: #a6b5c9");
        Button openAssignment = new Button("פתח את קובץ העבודה");
        openAssignment.setOnAction(e -> openAssignmentButtonButtonClicked(table));
        openAssignment.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        openAssignment.setStyle("-fx-background-color: #a6b5c9");

        //Buttons
        Button sortByGradeButton = new Button("מיון עבודות לפי ציון");
        sortByGradeButton.setOnAction(e -> sortByGrade(table));
        sortByGradeButton.setStyle("-fx-background-color: #a6b5c9");
        sortByGradeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        //Buttons
        Button sortByGradeDaysLeft = new Button("מיון עבודות לפי מספר ימים שנשארו");
        sortByGradeDaysLeft.setOnAction(e -> sortByDatsLeft(table));
        sortByGradeDaysLeft.setStyle("-fx-background-color: #a6b5c9");
        sortByGradeDaysLeft.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        HBox hBox_button_1 = new HBox();
        hBox_button_1.setPadding(new Insets(10,10,10,10));
        hBox_button_1.setSpacing(10);
        hBox_button_1.getChildren().addAll(doneUpdateButton,UndoneUpdateButton,openAssignment);
        HBox hBox_button_2 = new HBox();
        hBox_button_2.setPadding(new Insets(10,10,10,10));
        hBox_button_2.setSpacing(10);
        hBox_button_2.getChildren().addAll(sortByGradeButton,
                sortByGradeDaysLeft);

        table.setItems(exercises);
        table.getColumns().addAll(Course_coulmn,Id_coulmn, due_date_column,days_left_column, grade_column, done_column);
        // Set the row colors based on lambda expression to check the value of the Done column.
        table.setRowFactory(exercise -> new TableRow<Exercise>(){
            /**
             * Set the row colors to rows with done and not done text
             * @param item - row that represent an exercise
             * @param empty - check if the row is empty
             */
            @Override
            protected void updateItem(Exercise item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if(item.getDone().equals("Not Done") && item.getDays_left() < 4) {
                    setStyle("-fx-background-color: tomato;");
                } else if(item.getDone().equals("Not Done") && item.getDays_left() >= 4 && item.getDays_left() <= 10) {
                    setStyle("-fx-background-color: DARKORANGE;");
                } else if(item.getDone().equals("Not Done") && item.getDays_left() > 10) {
                    setStyle("-fx-background-color: LIGHTSTEELBLUE;");
                } else if(item.getDone().equals("Done")) {
                    setStyle("-fx-background-color:SPRINGGREEN;");
                } else
                    setStyle("");
            }});
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox titleHBOX1=new HBox();
        HBox titleHBOX2=new HBox();
        HBox titleHBOX3=new HBox();
        HBox titleHBOX4=new HBox();
        titleHBOX1.getChildren().add(title1);
        titleHBOX2.getChildren().add(title2);
        titleHBOX3.getChildren().add(title3);
        titleHBOX4.getChildren().add(title4);
        titleHBOX1.setAlignment(Pos.BASELINE_RIGHT);
        titleHBOX2.setAlignment(Pos.BASELINE_RIGHT);
        titleHBOX3.setAlignment(Pos.BASELINE_RIGHT);
        titleHBOX4.setAlignment(Pos.BASELINE_RIGHT);

        Image image = new Image("File:"+FileSearch.SearchForFile("assignment.png"));
        ImageView imageView = new ImageView(image);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(titleHBOX1,titleHBOX2,titleHBOX3,titleHBOX4,table,hBox_button_1,
                hBox_button_2,imageView);
        vBox.setSpacing(10);

        Scene scene = new Scene(vBox,800,800);
        windows.setScene(scene);
        windows.show();
        AlarmAboutDaysLeft();
    }

    private static void sortByDatsLeft(TableView<Exercise> table) {
        FXCollections.sort(table.getItems());
        table.refresh();
    }

    private static void sortByGrade(TableView<Exercise> table) {
        ObservableList<Exercise> exercises = table.getItems();
        exercises.sort(Exercise::compareByGrade);
        table.refresh();
    }

    /**
     * The function will open the Assignment file based on the assignment clicked by the user.
     * It uses function from AssignmentExecuter Class.
     * If this button is pushed with no Exercise chosen a pop up Alarmbox will appear.
     * @param table - The current table instance
     */
    private static void openAssignmentButtonButtonClicked(TableView<Exercise> table) {
        try{
        Exercise exercise_selected;
        exercise_selected = table.getSelectionModel().getSelectedItems().get(0);
        // Setp up the args as - course name and id both strings.
        String[] course_name_space_splitted = exercise_selected.getCourse().split(" ");
        String course_name = "";
        // Make the course name with hyphens, i.e Operating-Systems instead of Operating Systems
        for (int i = 0; i < course_name_space_splitted.length-1; i++) {
            course_name += course_name_space_splitted[i] + "-";
        }
        course_name+= course_name_space_splitted[course_name_space_splitted.length-1];
        AssignmentExecuter.execute(new String[]{course_name,Integer.toString(exercise_selected.getId())});
        }
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}

    }

    /**
     * Will get the input from the user and update the exercise with "Done" or "Not Done"
     * @param table - the table instance used by this class
     * @param done - true: The Done button was pushed, false: The Not Done button was pushed
     */
    private static void doneUpdateButtonButtonClicked(TableView<Exercise> table,boolean done) {
        try {
            ObservableList<Exercise> productSelected;
            productSelected = table.getSelectionModel().getSelectedItems();
            productSelected.get(0).setDone(done);
            table.refresh();
        }
        catch (IOException i){ AlarmBox.display("Can't add the new data to the database. Please check that the Exercises data base is \n" +
                "closed or not been corrupted");}
    }


    private static ObservableList<Exercise> getSortedWorkSheet() {
        ObservableList<Exercise> exercises = FXCollections.observableArrayList();
        try {
            CoursesToWorkSheetDictionary courses_work_sheet = new CoursesToWorkSheetDictionary();
            CourseWorksSheet cw;
            for (int i = 0; i < courses_work_sheet.getSize(); i++) {
                cw = courses_work_sheet.getNode(i);
                for (int j = 1; j <= cw.getSize() ; j++) {
                    exercises.add(cw.getExInfo(j));
                }
            }
        }
        catch (NullPointerException n) {
            /*
            * The db file is empty.
            * First try to check if the backup is not empty and warn the user.
            * If action is required use the backup file.
            * If no action is needed (no exercises) return null and open a new work sheet
            * */
            if(backupFileHandler.getfileSize() != 0) {
                Backup();
                UI.Restrart();
            }
            return null;
        }
        catch (IndexOutOfBoundsException e){
            /*
             * The db file is empty.
             * First try to check if the backup is not empty and warn the user.
             * If action is required use the backup file.
             * If no action is needed (no exercises) return null and open a new work sheet
             * */
            if(backupFileHandler.getfileSize() != 0) {
                Backup();
                UI.Restrart();
            }
        }
        catch (Exception e){
            AlarmBox.display("Error while reading the csv exercise file");
            Backup();
            UI.Restrart();
        }

        /*
         * Check if main file is empty and backup is not empty.
         * If yes there is a possibly an error occurred and the main got deleted.
         * In that case the user will be prompt if that is the case. If the reply is yes,
         * The system will write the backup to the main and restart the system.
         * */
        if(exercises.size() == 0 && backupFileHandler.getfileSize() != 0) {
            String message = "המידע בקובץ הראשי ריק (אין תרגילים קיימים במערכת) " +
                    "אבל קובץ הגיבוי לא ריק. \n" +
                    "לחץ כן כדי להעביר את המידע מהגיבוי לראשי.\n" +
                    "לחץ לא אם זה בסדר וברצונך לפתוח קובץ מידע חדש";
            Boolean answer = ConfirmBox.display("Confirmation windows", message);
            if (answer) {
                Backup();
                UI.Restrart();
            }
        }

        FXCollections.sort(exercises);
        return exercises;
    }

    /**
     * The function will loop over all the exercises and if there is an exercise
     * which deadline is 3 days away it will pop up an alarm box with a warning
     */
    private static void AlarmAboutDaysLeft(){
        ObservableList<Exercise> exercises = getSortedWorkSheet();
        ArrayList<Exercise> alarm = new ArrayList<>();
        Exercise exercise;
        for (int i = 0; i < exercises.size(); i++) {
            exercise = exercises.get(i);
            if(exercise.getDays_left() <= 3 && exercise.getDays_left() >= 0)
                alarm.add(exercise);
        }
        if(alarm.size() > 0){
            String warning = "WARNING!!!!!\n " + "למשימות הבאות נותרו 3 ימים או פחות:\n";
            for (int i = 0; i < alarm.size(); i++)
                warning += "קורס-" + alarm.get(i).getCourse() + " משימה מספר-" + alarm.get(i).getId() + "\n";

            MessageBox.display(warning);
        }

    }

    /** When the function will get called it will try to write all the data
     * from the backup file located on src\Data\Backups\ExercisesDataBaseBackup.csv
     * To the main file - src\Data\ExercisesDataBase.csv file
     */
    public static void Backup() {
        AlarmBox.display("נמצאה בעיה במידע בקובץ הראשי. המידע מקובץ הגיבוי יועבר לקובץ הראשי. \n" +
                "לחץ CLOSE ולאחר מכאן התוכנה תבצע ריסטרט והמידע ייתעדכן. \n");
        try {
            CoursesToWorkSheetDictionary.Backup();
        }
        catch(UseBackupFailureExecption u){
            AlarmBox.display("Can't access the data on the backup data. Try fixing it manually. \n" +
                    "Check te  in src\\Data\\Backups\\ExercisesDataBaseBackup.csv file and update the Backup from the main \n" +
                    "src\\Data\\ExercisesDataBase.csv file.");
        }
    }

}
