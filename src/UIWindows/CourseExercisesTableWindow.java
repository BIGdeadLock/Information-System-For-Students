package UIWindows;

import Data.DataStructures.CoursesToWorkSheetDictionary;
import Data.DataStructures.Exercise;
import Data.DataStructures.CourseWorksSheet;
import Data.DataStructures.UpdatesManager;
import Data.Exercises.AssignmentExecuter;
import Tools.Files.BackupFileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseExercisesTableWindow {
    static private int next;
    static private String course_name;
    static private CoursesToWorkSheetDictionary courses_work_sheet;
    private static BackupFileHandler backupFileHandler = new BackupFileHandler();
    static private double weight;

    public static void start(String course,String Weight) {
        course_name = course;
        weight = Double.valueOf(Weight);


        TableView<Exercise> table = new TableView<>();;
        Stage windows = new Stage();
        windows.setTitle("גיליון עבודות של " + course);

        // Title for the table
        final Label title1 = new Label(" משקל העבודות בקורס  " + course_name + " הוא " + Weight + "%");
        title1.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 22; -fx-text-fill: darkred");
        Label grade_label = new Label("לעדכון עבודה ללא ציון יש לעדכן את הציון של העבודה ל: 1-");
        grade_label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 16; -fx-text-fill: darkred");

        //Id column
        TableColumn<Exercise, Integer> Id_coulmn = new TableColumn<>("מספר עבודה");
        Id_coulmn.setMinWidth(30);
        Id_coulmn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Due Date column
        TableColumn<Exercise, String> due_date_column = new TableColumn<>("תאריך הגשה");
        due_date_column.setMinWidth(120);
        due_date_column.setCellValueFactory(new PropertyValueFactory<>("due_date"));

        //Days Left column
        TableColumn<Exercise, String> days_left_column = new TableColumn<>("ימים שנשארו");
        days_left_column.setMinWidth(80);
        days_left_column.setCellValueFactory(new PropertyValueFactory<>("days_left"));

        //Done or not column
        TableColumn<Exercise, String> done_column = new TableColumn<>("בוצע");
        done_column.setMinWidth(65);
        done_column.setCellValueFactory(new PropertyValueFactory<>("done"));

        // Grade column
        TableColumn<Exercise, String> grade_column = new TableColumn<>("ציון");
        grade_column.setMinWidth(65);
        grade_column.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Due_date input
        TextField due_date_input = new TextField();
        due_date_input.setMinWidth(100);
        due_date_input.setPromptText("1/1/2020");
        // Grade Input
        TextField grade_input = new TextField();
        grade_input.setMinWidth(100);
        grade_input.setPromptText("100");

        // Get user data
        ObservableList<Exercise> exercises = getWorkSheet(course);
        // index of the next data
        if(exercises == null)
            next = 0;
        else
            next = exercises.get(exercises.size()-1).getId();

        // Exercise Add/Delete/Done related buttons
        Button addButton = new Button("הוספת עבודה חדשה");
        addButton.setOnAction(e -> addButtonClicked(due_date_input.getText(),table,due_date_input));
        addButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        addButton.setStyle("-fx-background-color: #a6b5c9");
        Button deleteButton = new Button("למחיקת תרגיל");
        deleteButton.setOnAction(e -> deleteButtonClicked(table));
        deleteButton.setStyle("-fx-background-color: #a6b5c9");
        deleteButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
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
        // Grade related button
        openAssignment.setStyle("-fx-background-color: #a6b5c9");
        Button updateGradeButton = new Button("עדכן את ציון העבודה");
        updateGradeButton.setOnAction(e -> updateGradeButtonClicked(table,grade_input.getText()));
        updateGradeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        updateGradeButton.setStyle("-fx-background-color: #a6b5c9");
        Button calculateAverageButton = new Button("חשב את ממוצע העבודות של הקורס");
        calculateAverageButton.setOnAction(e -> calculateAverageButtonClicked(table));
        calculateAverageButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        calculateAverageButton.setStyle("-fx-background-color: #a6b5c9");
        // Updates related buttons
        Button showAllUpdateButton = new Button("הראה את כל העדכונים שנעשו בעבודה");
        showAllUpdateButton.setOnAction(e -> showLastUpdateButtonClicked(table));
        showAllUpdateButton.setStyle("-fx-background-color: #a6b5c9");
        showAllUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));


        table.setItems(exercises);
        table.getColumns().addAll(Id_coulmn, due_date_column,days_left_column, grade_column,done_column);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
                } else if(item.getDone().equals("Not Done")) {
                    setStyle("-fx-background-color: tomato;");
                } else if(item.getDone().equals("Done")) {
                    setStyle("-fx-background-color:SPRINGGREEN;");
                } else
                    setStyle("");
            }});

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(due_date_input,addButton,doneUpdateButton,UndoneUpdateButton
                ,openAssignment,deleteButton);

        HBox hBox_grade = new HBox();
        hBox_grade.setPadding(new Insets(10,10,10,10));
        hBox_grade.setSpacing(10);
        hBox_grade.getChildren().addAll(grade_input,updateGradeButton,calculateAverageButton);
        HBox hBox_grade_label = new HBox();
        hBox_grade_label.setPadding(new Insets(10,10,10,10));
        hBox_grade_label.setSpacing(10);
        hBox_grade_label.getChildren().addAll(grade_label);

        HBox hBox_update = new HBox();
        hBox_update.setPadding(new Insets(10,10,10,10));
        hBox_update.setSpacing(10);
        hBox_update.getChildren().addAll(showAllUpdateButton);

        HBox titleHBOX1=new HBox();
        titleHBOX1.getChildren().add(title1);
        titleHBOX1.setAlignment(Pos.BASELINE_RIGHT);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(titleHBOX1,table,hBox,hBox_grade,hBox_grade_label,hBox_update);

        Scene scene = new Scene(vBox);
        windows.setScene(scene);
        windows.show();

    }

    /**
     * Open a new ExerciseTableWindow instance for the chosen exercise.
     * Will show all the updates of the exercise.
     * @param table the current table view instance
     */
    private static void showLastUpdateButtonClicked(TableView<Exercise> table) {
        try {
            Exercise exerciseSelected = table.getSelectionModel().getSelectedItems().get(0);
            ExerciseUpdatesTable.start(course_name, exerciseSelected);
        }
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button Or the exercise does not \n" +
                "exist in the database file");}
    }


    /**
     * This function will go over all the objects in the table and calculate the average grade
     * for all the exercises.
     * Not Needed and Not Updated are treated as 0.
     * @param table - the current tableview instance
     */
    private static void calculateAverageButtonClicked(TableView<Exercise> table) {
        ObservableList<Exercise> exercises = table.getItems();
        // For each exercise that is not Not Updated or Not Needed we increase the grade
        // With the exercise's grade and increment the updated_exercises counter.
        int score = 0;
        int updated_exercises = 0;
        for (int i = 0; i < exercises.size(); i++) {
            if(!exercises.get(i).getGrade().equals("Not Needed") || !exercises.get(i).getGrade().equals("Not Updated")) {
                score += Integer.valueOf(exercises.get(i).getGrade());
                updated_exercises++;
            }
        }
        double average = score / updated_exercises;
        AlarmBox.display("רק עבודות עם ציון מעודכן נכנסו לחישוב. \n" +
                "הממוצע הוא: " +
                average);

        /* Try asking the user for calculating the final score */
        boolean answer = ConfirmBox.display("חישוב ציון עבודות","האם תרצה לחשב את תוספת ממוצע העבודות לממוצע הסופי?");
        if(answer) {
            double finaL_grade = (weight / 100) * average;
            AlarmBox.display("תוספצת הציון של העבודות לציון הסופי הוא: " +
                    finaL_grade);
        }
    }

    /**
     * Will update the grade of an exercise through exercise instance setGrade function.
     * Will refresh the table to see the change.
     * @param table - TableView instance
     * @param grade_input - the input the user gave in String format
     */
    private static void updateGradeButtonClicked(TableView<Exercise> table,String grade_input) {
        try {
            Exercise exerciseSelected = table.getSelectionModel().getSelectedItems().get(0);
            exerciseSelected.setGrade(grade_input);
            table.refresh();
        }
        catch (IOException i){
            String warning = "הייתה בעיה בניסיון לכתוב לקובץ הנתונים.\n" +
                    "האם אתה רוצה לנסות להעביר את הגיבוי לקובץ הראשי?\n" +
                    "דבר זה עלול לגרור באיבוד מידע אחר שעדכנת אם לא לחצת\n" +
                    "על כפתור הגיבוי במסך הראשי.";
            Boolean answer = ConfirmBox.display("Error writing to file",warning);
            if(answer)
                Backup();
        }
        catch (ParseException p){AlarmBox.display("נא להזין ציון חוקי בין 0 ל-100");}
        catch (NumberFormatException n){AlarmBox.display("נא להזין ציון חוקי בין 0 ל-100");}
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}
    }

    /**
     * Delete an exercise from the table.
     * The exercise that the user clicked is the one which be deleted.
     * @param table - the table instance being used by the UI
     */
    private static void deleteButtonClicked(TableView<Exercise> table) {
        next--;
        try{
        ObservableList<Exercise> allProducts;
        allProducts = table.getItems();
        Exercise exerciseSelected = table.getSelectionModel().getSelectedItems().get(0);
        allProducts.remove(exerciseSelected);
        courses_work_sheet.deleteData(exerciseSelected);
        CoursesToWorkSheetDictionary.UpdateDB(exerciseSelected,false);
        // Destroy all the updates db files with the exercise deletion
        UpdatesManager.destroyDB(exerciseSelected.getId());
        }
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button Or the exercise does not \n" +
                "exist in the database file");}
        catch (IOException e){AlarmBox.display("Can't add the new data to the database. Please check that the Exercises data base is \n" +
                "closed or not been corrupted");}
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
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}
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

            String[] assignment = new String[]{course_name,Integer.toString(exercise_selected.getId())};
            // Use the AssigmentExecuter class to open the assignment
            AssignmentExecuter.execute(assignment);
        }
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}

    }

    /**
     * For each exercise added to the system the function will add it to the table and to the database.
     * The new exercise is marked as Not Done
     * @param due_date - new due date stated by the user
     * @param table - current running table
     * @param due_date_button - the button instance
     */
    private static void addButtonClicked(String due_date,TableView table, TextField due_date_button) {
        next++;
        ObservableList<Exercise> exercises;
        Exercise ex;
        try {
            ex = new Exercise(next, due_date, "Not Done",course_name);
            if (table.getItems() == null) {
                exercises = FXCollections.observableArrayList();
                table.setItems(exercises);
            }
            table.getItems().add(ex);
            due_date_button.clear();
            String[] new_data = new String[5];
            new_data[0] = course_name;
            new_data[1] = due_date;
            new_data[2] = "Not Done";
            new_data[3] = ex.getUpdate_date().toString();
            new_data[4] = "Not Updated";
            courses_work_sheet = getWorksheetHandler();
            courses_work_sheet.addNewCourseWorksheet(course_name);
            courses_work_sheet.addExerciseToDS(course_name,new_data);
            courses_work_sheet.addExerciseToDBFile(course_name,new_data);

        }
        catch (ParseException n) {
            AlarmBox.display("1)Please enter a valid date format -> DD/MM/YYYY\n"+
                    "2)Please enter a valid day between 1 to 31\n"+
                    "3)Please enter a valid month between 1 to 12\n"+
                    "4)Please enter a valid year format of 4 numbers");
        }
        catch (IOException i){
            AlarmBox.display("Can't add the new data to the database. Please check that the Exercises data base is \n" +
                    "closed or not been corrupted");
        }
        catch (Exception e) {
            AlarmBox.display("1)Please enter a valid date format -> DD/MM/YYYY\n"+
                    "2)Please enter a valid day between 1 to 31\n"+
                    "3)Please enter a valid month between 1 to 12\n"+
                    "4)Please enter a valid year format of 4 numbers");
        }
    }

    /**
     * Used to get all the exercise data from the main file.
     * The data is used to create exercise objects which are put into the tableview instance.
     * In case of an empty file null will be returned.
     * In case of exception the data from the backup will be written to the main file and the program
     * will exit.
     * @param course - the course name to get the exercise from
     * @return ObservableList<Exercise> - a list containing all the exercises in the data base of the course selected.
     */
    private static ObservableList<Exercise> getWorkSheet(String course) {
        ObservableList<Exercise> exercises = FXCollections.observableArrayList();
        try {
            courses_work_sheet = getWorksheetHandler();
            CourseWorksSheet corse_works = courses_work_sheet.getNode(course);
            for (int i = 0; i < corse_works.getSize(); i++) {
                exercises.add(corse_works.getExInfo(i + 1));
            }
        }
        catch (NullPointerException n) {
            /*
             * The db file is empty.
             * First try to check if the backup is not empty and warn the user.
             * If action is required use the backup file.
             * If no action is needed (no exercises) return null and open a new work sheet
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
            return null;
        }
        catch (IndexOutOfBoundsException e){
            /*
             * The db file is empty.
             * First try to check if the backup is not empty and warn the user.
             * If action is required use the backup file.
             * If no action is needed (no exercises) return null and open a new work sheet
             * */

            if(exercises.size() == 0 && backupFileHandler.getfileSize() != 0) {
                String message = "המידע בקובץ הראשי ריק (אין תרגילים קיימים במערכת)" +
                        "אבל קובץ הגיבוי לא ריק. \n" +
                        "לחץ כן כדי להעביר את המידע מהגיבוי לראשי.\n" +
                        "לחץ לא אם זה בסדר וברצונך לפתוח קובץ מידע חדש";
                Boolean answer = ConfirmBox.display("Confirmation windows", message);
                if (answer) {
                    Backup();
                    UI.Restrart();
                 }
            }
            return null;
        }
        catch (Exception e){
            AlarmBox.display("Error while reading the csv exercise file");
            Backup();
            UI.Restrart();
        }

        return exercises;
    }

    /**
     * Using the singelton design pattern create only one instance of the data structure
     * {@link CoursesToWorkSheetDictionary } type.
     * If one exists already then returned that instance.
     * @return CoursesToWorkSheetDictionary instance
     */
    private static CoursesToWorkSheetDictionary getWorksheetHandler(){
        if (courses_work_sheet == null){
                try {
                    courses_work_sheet = new CoursesToWorkSheetDictionary();
                    next = 1;
                }
                catch (FileNotFoundException f) {AlarmBox.display("File ExercisesDataBase not found");}
                catch(ParseException p){Backup();}
                catch (IOException i){Backup();}
                catch (SyntaxException s){AlarmBox.display("There is a syntax error in the data. Invalid date," +
                        " two commas (,,)\n" +
                        "and so on");}
            }
        return courses_work_sheet;
    }

    public static void Backup() {
        ExercisesTableWindow.Backup();
    }
}
