package UIWindows;

import Class.Lecture;
import Data.DataStructures.CoursesToWorkSheetDictionary;
import Data.DataStructures.Exercise;
import Data.DataStructures.LecturesDS;
import Tools.Commands.OSCommand;
import Tools.Files.FileSearch;
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

public class CourseLecturesTableWindow {

        static private int next;
        static private String course_name;
        static private LecturesDS lecturesDS;

        public static void start(String course,String file) {
            course_name = course;

            TableView<Lecture> table = new TableView<>();;
            Stage windows = new Stage();
            windows.setTitle(" גיליון מעקב הרצאות של " + course);

            // Title for the table
            final Label title1 = new Label(" הרצאות של קורס " + course_name);
            title1.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 22; -fx-text-fill: darkred");

            //Id column
            TableColumn<Lecture, Integer> Id_coulmn = new TableColumn<>("מספר הרצאה");
            Id_coulmn.setMinWidth(30);
            Id_coulmn.setCellValueFactory(new PropertyValueFactory<>("number"));

            //Topic column
            TableColumn<Lecture, String> topic_column = new TableColumn<>("נושא ההרצאה");
            topic_column.setMinWidth(120);
            topic_column.setCellValueFactory(new PropertyValueFactory<>("topic"));

            //Done or not column
            TableColumn<Lecture, String> done_column = new TableColumn<>("סטטוס מעבר על ההרצאה");
            done_column.setMinWidth(65);
            done_column.setCellValueFactory(new PropertyValueFactory<>("status"));

            //topic input
            TextField lecture_course_input = new TextField();
            lecture_course_input.setMinWidth(100);
            lecture_course_input.setPromptText("design patterns");

            //Get user data
            ObservableList<Lecture> observableList = getCurrentLectures(file);
            // index of the next data
            if(observableList.size() == 0)
                next = 0;
            else
                next = observableList.get(observableList.size()-1).getNumber();

            //Buttons
            Button addButton = new Button("הוספת הרצאה חדשה");
            addButton.setOnAction(e -> addButtonClicked(table));
            addButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            addButton.setStyle("-fx-background-color: #a6b5c9");
            Button deleteButton = new Button("למחיקת הרצאה");
            deleteButton.setOnAction(e -> deleteButtonClicked(table));
            deleteButton.setStyle("-fx-background-color: #a6b5c9");
           deleteButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            Button doneUpdateButton = new Button("סימון הרצאה כנצפתה");
            doneUpdateButton.setOnAction(e -> doneUpdateButtonButtonClicked(table,true));
            doneUpdateButton.setStyle("-fx-background-color: #a6b5c9");
            doneUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            Button UndoneUpdateButton = new Button("סימון הרצאה כלא נצפתה");
            UndoneUpdateButton.setOnAction(e -> doneUpdateButtonButtonClicked(table,false));
            UndoneUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            UndoneUpdateButton.setStyle("-fx-background-color: #a6b5c9");
            Button updateTopic = new Button("עדכון נושא הרצאה");
            updateTopic.setOnAction(e -> updateTopic(table,lecture_course_input.getText()));
            updateTopic.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            updateTopic.setStyle("-fx-background-color: #a6b5c9");
            Button lectureFile = new Button("פתח את מצגת ההרצאה");
            lectureFile.setOnAction(e -> openPresentation(table,course_name));
            lectureFile.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            lectureFile.setStyle("-fx-background-color: #a6b5c9");

            table.setItems(observableList);
            table.getColumns().addAll(Id_coulmn, topic_column,done_column);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            table.setRowFactory(lecture -> new TableRow<Lecture>(){
                /**
                 * Set the row colors to rows with done and not done text
                 * @param item - row that represent an exercise
                 * @param empty - check if the row is empty
                 */
                @Override
                protected void updateItem(Lecture item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle("");
                    } else if(item.getStatus().equals("Waiting")) {
                        setStyle("-fx-background-color: tomato;");
                    } else if(item.getStatus().equals("Done")) {
                        setStyle("-fx-background-color:SPRINGGREEN;");
                    } else
                        setStyle("");
                }});

            HBox hBox1 = new HBox();
            hBox1.setPadding(new Insets(10,10,10,10));
            hBox1.setSpacing(10);
            hBox1.getChildren().addAll(addButton,deleteButton,doneUpdateButton,UndoneUpdateButton,lectureFile);
            HBox hBox2 = new HBox();
            hBox2.setPadding(new Insets(10,10,10,10));
            hBox2.setSpacing(10);
            hBox2.getChildren().addAll(lecture_course_input,updateTopic);

            HBox titleHBOX1=new HBox();
            titleHBOX1.getChildren().add(title1);
            titleHBOX1.setAlignment(Pos.BASELINE_RIGHT);

            Image image = new Image("File:"+ FileSearch.SearchForFile("lecture.jpg"));
            ImageView imageView = new ImageView(image);

            VBox vBox = new VBox();
            vBox.getChildren().addAll(titleHBOX1,table,hBox1,hBox2,imageView);
            vBox.setSpacing(10);

            Scene scene = new Scene(vBox,900,800);
            windows.setScene(scene);
            windows.show();
        }

    /**
     * When delete button is pressed this function will delete the correct lecture from the
     * database and refresh the table to not show it.
     * @param table - the tableview instance
     */
    private static void deleteButtonClicked(TableView<Lecture> table) {
        // Check with the user that the lecture deletion was not a mistake
        String warning = "שימו לב: כפתור זה נועד רק למחיקת ההרצאה האחרונה ברשימה.\n" +
                "מחיקת הרצאה אחרת עלול לגרור לבעיות " +
                "באינדקוס מספר ההרצאה בהמשך.\n" +
                "אם ההרצאה שברצונך למחוק היא האחרונה לחץ כן.\n" +
                "אם ההרצאה שברצונך למחוק היא לא אחרונה לחץ לא." ;
        Boolean answer = ConfirmBox.display("Confirmation windows",warning);
        if(!answer)
            return;

        next--;
        try{
            ObservableList<Lecture> allProducts;
            allProducts = table.getItems();
            Lecture selected_lecture = table.getSelectionModel().getSelectedItems().get(0);
            allProducts.remove(selected_lecture);
            lecturesDS.deleteData(selected_lecture);
            table.refresh();

        }
        catch (NullPointerException n){AlarmBox.display("Please click on an lecture \n " +
                "before pushing the button Or the exercise does not \n" +
                "exist in the database file");}
        catch (IOException e){AlarmBox.display("Can't add the new data to the database. Please check that the lecture data base is \n" +
                "closed or not been corrupted");}
    }

    /**
     * The function will get a course name in hebrew and will return the correct file format
     * for the course in english (i.e OS, APT ...)
     * @param course_name - name in hebrew
     * @return Correct course name format in english.
     */
    private static String getCourse_nameFileFormat(String course_name){
        if(course_name.contains("מבוא למערכות הפעלה"))
            return  "OS";
        else if(course_name.contains("מבוא לכלכלה לתעשייה וניהול"))
            return "Eco";
        else if(course_name.contains("מודלים חישוביים ואלגוריתמים"))
            return "MNA";
        else if(course_name.contains("מימוש מערכות בסיסי נתונים"))
            return "DB";
        else if(course_name.contains("נושאים מתקדמים בתכנות"))
            return "ATP";
        else if(course_name.contains("שיטות נומריות בתעשייה"))
            return "NUM";

        return null;
    }

    /**
     * The function will check the existence of a presentation file.
     * If none is found it will notice the user.
     * If one is found and not in the correct name it will change it and open it.
     * @param table - tableview instance
     * @param course_name - the course name of the current tableview
     */
    private static void openPresentation(TableView<Lecture> table, String course_name) {
        String course_format = getCourse_nameFileFormat(course_name);
        ObservableList<Lecture> productSelected;
        productSelected = table.getSelectionModel().getSelectedItems();
        String number_of_lecture = String.valueOf(productSelected.get(0).getNumber());
        String presentation_file = course_format + "Lecture" + number_of_lecture;
        String presentation_path = FileSearch.SearchForFile(presentation_file);
        String temp_name_path = FileSearch.SearchForFile("ABC");

        if(temp_name_path == null && presentation_path == null)
            AlarmBox.display("המערכת לא מוצאת שום קובץ לפתוח.\n" +
                    "אם לא קיים קובץ של מצגת ב- " + course_name+
                    "\\Data\\Lectrues" + "\n" +
                    "ניתן להוריד ידנית מהמודל את הקובץ ולשים אותו בתיקייה המתאימה.\n" +
                    "יש לקרוא לקובץ בשם ABC ואז המערכת תזהה אותו\n" +
                    " ותחליף את השם בשם שהמערכת יודעת לזהות.");

        else if(temp_name_path != null)
        {
            /* Need to get the file format: .docx, .ppt, .pdf ... */
            // Get the file format
            int dot = temp_name_path.indexOf(".");
            String file_format = temp_name_path.substring(dot);
            // Add it to the new name
            presentation_file += file_format;
            // Change the name and let the user know to try again.
            String rename_command = "cmd /c rename \""+ temp_name_path +"\"" + " " + "\""+ presentation_file +"\"";
            OSCommand.Run(rename_command);
            AlarmBox.display("ההחלפה בוצעה. נסה שוב להפעיל את קובץ המצגת עכשיו");
        }
        else {
            /* The fil is in the correct, just need to open it */
            String presentation_command = "cmd /c \"" + presentation_path + "\"";
            OSCommand.Run(presentation_command);


        }


    }


    /**
     * Will get the input from the user and update the lecture topic
     * @param table - the table instance used by this class
     * @param topic - the updated topic
     */
    private static void updateTopic(TableView<Lecture> table, String topic) {
        try {
                ObservableList<Lecture> productSelected;
                productSelected = table.getSelectionModel().getSelectedItems();
                productSelected.get(0).setTopic(topic);
                table.refresh();
            }
            catch (IOException i){ AlarmBox.display("Can't add the new data to the database." +
                    " Please check that the Lectures data base is \n" +
                    "closed or not been corrupted");}
            catch (NullPointerException n){AlarmBox.display("Please click on an lecture \n " +
                    "before pushing the button");}
    }


    /**
     * Will get the input from the user and update the lecture with "Done" or "Waiting"
     * @param table - the table instance used by this class
     * @param done - true: The Done button was pushed, false: The Not Done button was pushed
     */
    private static void doneUpdateButtonButtonClicked(TableView table,boolean done) {
        try {
            ObservableList<Lecture> productSelected;
            productSelected = table.getSelectionModel().getSelectedItems();
            productSelected.get(0).setStatus(done);
            table.refresh();
        }
        catch (IOException i){ AlarmBox.display("Can't add the new data to the database." +
                " Please check that the Lectures data base is \n" +
                "closed or not been corrupted");}
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}
    }

    /**
     * For each lecture added to the system the function will add it to the table and to the database.
     * The new lecture is marked as Waiting
     * @param table - current running table
     */
    private static void addButtonClicked(TableView table) {
        next++;
        ObservableList<Lecture> lectureObservableList;
        Lecture lecture;
        try {
            // Create a new lecture object with the next available number
            lecture = new Lecture(next);
            if (table.getItems() == null) {
                // Update the observable list
                lectureObservableList = FXCollections.observableArrayList();
                table.setItems(lectureObservableList);
            }
            table.getItems().add(lecture);
            String[] new_data = new String[3];
            new_data[0] = String.valueOf(next);
            new_data[1] = lecture.getTopic();
            new_data[2] = lecture.getStatus();
            lecturesDS.setLectures(lecture);
            table.refresh();
        }
        catch (IOException i){
            AlarmBox.display("Can't add the new data to the database. Please check that the Lectures data base\n" +
                    "of the course is closed or not been corrupted");
        }
        catch (Exception e) {
            AlarmBox.display("Something went wrong try again");
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
    private static ObservableList<Lecture> getCurrentLectures(String course) {
        ObservableList<Lecture> lectures = FXCollections.observableArrayList();
        try {
            lecturesDS = new LecturesDS(course);
            ArrayList<Lecture> course_lectures = lecturesDS.getLectures();
            for (int i = 0; i < course_lectures.size(); i++)
                lectures.add(course_lectures.get(i));
        }
        catch (NullPointerException n) {
            /*
             * The db file is empty.
             */
            AlarmBox.display("אין הרצאות קיימות כרגע בקובץ ההרצאות של הקורס הנבחר");
            return null;
        }
        catch (IndexOutOfBoundsException e){
        /*
         * The db file is empty.
         */
        AlarmBox.display("אין הרצאות קיימות כרגע בקובץ ההרצאות של הקורס הנבחר");
        return null;
        }
        catch (Exception e){
            AlarmBox.display("Error while reading the csv lecture file");
            UI.Restrart();
        }
        return lectures;
    }
}
