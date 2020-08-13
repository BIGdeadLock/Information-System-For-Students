package UIWindows;

import Class.Tirgul;
import Data.DataStructures.TirgulimDS;
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

public class CourseTirgulimWindow {

    static private int next;
    static private String course_name;
    static private TirgulimDS tirgulimDS;

    public static void start(String course,String file) {
        course_name = course;

        TableView<Tirgul> table = new TableView<>();;
        Stage windows = new Stage();
        windows.setTitle(" גיליון מעקב תרגולים של " + course);

        // Title for the table
        final Label title1 = new Label(" תרגולים של קורס " + course_name);
        title1.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 22; -fx-text-fill: darkred");

        //Id column
        TableColumn<Tirgul, Integer> Id_coulmn = new TableColumn<>("מספר תרגול");
        Id_coulmn.setMinWidth(30);
        Id_coulmn.setCellValueFactory(new PropertyValueFactory<>("number"));

        //Topic column
        TableColumn<Tirgul, String> topic_column = new TableColumn<>("נושא תרגול");
        topic_column.setMinWidth(120);
        topic_column.setCellValueFactory(new PropertyValueFactory<>("topic"));

        //Done or not column
        TableColumn<Tirgul, String> done_column = new TableColumn<>("סטטוס מעבר על תרגול");
        done_column.setMinWidth(65);
        done_column.setCellValueFactory(new PropertyValueFactory<>("status"));

        //topic input
        TextField tirgul_topic_input = new TextField();
        tirgul_topic_input.setMinWidth(100);
        tirgul_topic_input.setPromptText("Scheduling");

        //Get user data
        ObservableList<Tirgul> observableList = getCurrentTirgulim(file);
        // index of the next data
        if(observableList.size() == 0)
            next = 0;
        else
            next = observableList.get(observableList.size()-1).getNumber();

        //Buttons
        Button addButton = new Button("הוספת תרגול חדשה");
        addButton.setOnAction(e -> addButtonClicked(table));
        addButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        addButton.setStyle("-fx-background-color: #a6b5c9");
        Button deleteButton = new Button("למחיקת תרגול");
        deleteButton.setOnAction(e -> deleteButtonClicked(table));
        deleteButton.setStyle("-fx-background-color: #a6b5c9");
        deleteButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Button doneUpdateButton = new Button("סימון תרגול כנצפה");
        doneUpdateButton.setOnAction(e -> doneUpdateButtonButtonClicked(table,true));
        doneUpdateButton.setStyle("-fx-background-color: #a6b5c9");
        doneUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Button UndoneUpdateButton = new Button("סימון תרגול כלא נצפה");
        UndoneUpdateButton.setOnAction(e -> doneUpdateButtonButtonClicked(table,false));
        UndoneUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        UndoneUpdateButton.setStyle("-fx-background-color: #a6b5c9");
        Button updateTopic = new Button("עדכון נושא תרגול");
        updateTopic.setOnAction(e -> updateTopic(table,tirgul_topic_input.getText()));
        updateTopic.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        updateTopic.setStyle("-fx-background-color: #a6b5c9");
        Button tirgulFile = new Button("פתח את מצגת התרגול");
        tirgulFile.setOnAction(e -> openPresentation(table,course_name));
        tirgulFile.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        tirgulFile.setStyle("-fx-background-color: #a6b5c9");


        table.setItems(observableList);
        table.getColumns().addAll(Id_coulmn, topic_column,done_column);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setRowFactory(lecture -> new TableRow<Tirgul>(){
            /**
             * Set the row colors to rows with done and not done text
             * @param item - row that represent an exercise
             * @param empty - check if the row is empty
             */
            @Override
            protected void updateItem(Tirgul item, boolean empty) {
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
        hBox1.getChildren().addAll(addButton,deleteButton,doneUpdateButton,UndoneUpdateButton,tirgulFile);
        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10,10,10,10));
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(tirgul_topic_input,updateTopic);

        HBox titleHBOX1=new HBox();
        titleHBOX1.getChildren().add(title1);
        titleHBOX1.setAlignment(Pos.BASELINE_RIGHT);

        Image image = new Image("File:"+ FileSearch.SearchForFile("tirgul.png"));
        ImageView imageView = new ImageView(image);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(titleHBOX1,table,hBox1,hBox2,imageView);
        vBox.setSpacing(10);


        Scene scene = new Scene(vBox,900,800);
        windows.setScene(scene);
        windows.show();

    }

    /**
     * When the delete button is pressed delete the tirgul from the tabse and from the DB
     * @param table - the current table view instnace
     */
    private static void deleteButtonClicked(TableView<Tirgul> table) {
        // Check with the user that the tirgul deletion was not a mistake
        String warning = "שימו לב: כפתור זה נועד רק למחיקת התרגול האחרון ברשימה.\n" +
                "מחיקת תרגול אחר עלול לגרור לבעיות " +
                "באינדקוס מספר התרגול בהמשך.\n" +
                "אם התרגול שברצונך למחוק הוא האחרון לחץ כן.\n" +
                "אם התרגול שברצונך למחוק הוא לא אחרון לחץ לא." ;
        Boolean answer = ConfirmBox.display("Confirmation window",warning);
        if(!answer)
            return;

        next--;
        try{
            ObservableList<Tirgul> allProducts;
            allProducts = table.getItems();
            Tirgul selected_tirgul = table.getSelectionModel().getSelectedItems().get(0);
            allProducts.remove(selected_tirgul);
            TirgulimDS.deleteData(selected_tirgul);
            table.refresh();

        }
        catch (NullPointerException n){AlarmBox.display("Please click on an lecture \n " +
                "before pushing the button Or the exercise does not \n" +
                "exist in the database file");}
        catch (IOException e){AlarmBox.display("Can't add the new data to the database. Please check that the lecture data base is \n" +
                "closed or not been corrupted");}
    }

    /**
     * Used to open the presentation file of the tirgual chosen by the user
     * @param table - the tableview instance
     */
    private static void openPresentation(TableView table,String course_name) {
        String course_format = "";
        if(course_name.contains("מבוא למערכות הפעלה"))
            course_format = "OS";
        else if(course_name.contains("מבוא לכלכלה לתעשייה וניהול"))
            course_format= "Eco";
        else if(course_name.contains("מודלים חישוביים ואלגוריתמים"))
            course_format="MNA";
        else if(course_name.contains("מימוש מערכות בסיסי נתונים"))
            course_format="DB";
        else if(course_name.contains("נושאים מתקדמים בתכנות"))
            course_format = "ATP";
        else if(course_name.contains("שיטות נומריות בתעשייה"))
            course_format = "NUM";

        ObservableList<Tirgul> productSelected;
        productSelected = table.getSelectionModel().getSelectedItems();
        String number_of_tirgul = String.valueOf(productSelected.get(0).getNumber());
        String presentation_file = course_format + "Practice" + number_of_tirgul;
        String presentation_path = FileSearch.SearchForFile(presentation_file);
        String presentation_command = "cmd /c \""+ presentation_path +"\"" ;
        OSCommand.Run(presentation_command);
    }

    /**
     * Will get the input from the user and update the lecture topic
     * @param table - the table instance used by this class
     * @param topic - the updated topic
     */
    private static void updateTopic(TableView<Tirgul> table, String topic) {
        try {
            ObservableList<Tirgul> productSelected;
            productSelected = table.getSelectionModel().getSelectedItems();
            productSelected.get(0).setTopic(topic);
            table.refresh();
        }
        catch (IOException i){ AlarmBox.display("Can't add the new data to the database. Please check that the Tirgulim data base is \n" +
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
            ObservableList<Tirgul> productSelected;
            productSelected = table.getSelectionModel().getSelectedItems();
            productSelected.get(0).setStatus(done);
            table.refresh();
        }
        catch (IOException i){ AlarmBox.display("Can't add the new data to the database. Please check that the Tirgulim data base is \n" +
                "closed or not been corrupted");}
        catch (NullPointerException n){AlarmBox.display("Please click on a tirgul \n " +
                "before pushing the button");}
    }

    /**
     * For each lecture added to the system the function will add it to the table and to the database.
     * The new lecture is marked as Waiting
     * @param table - current running table
     */
    private static void addButtonClicked(TableView table) {
        next++;
        ObservableList<Tirgul> lectureObservableList;
        Tirgul tirgul;
        try {
            // Create a new tirgul object with the next available number
            tirgul = new Tirgul(next);
            if (table.getItems() == null) {
                // Update the observable list
                lectureObservableList = FXCollections.observableArrayList();
                table.setItems(lectureObservableList);
            }
            table.getItems().add(tirgul);
            String[] new_data = new String[3];
            new_data[0] = String.valueOf(next);
            new_data[1] = tirgul.getTopic();
            new_data[2] = tirgul.getStatus();
            tirgulimDS.setTirgulim(tirgul);
            table.refresh();
        }
        catch (IOException i){
            AlarmBox.display("Can't add the new data to the database. Please check that the Tirgulim data base\n" +
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
    private static ObservableList<Tirgul> getCurrentTirgulim(String course) {
        ObservableList<Tirgul> tirgulim = FXCollections.observableArrayList();
        try {
            tirgulimDS = new TirgulimDS(course);
            ArrayList<Tirgul> course_lectures = tirgulimDS.getTirgulim();
            for (int i = 0; i < course_lectures.size(); i++)
                tirgulim.add(course_lectures.get(i));
        }
        catch (NullPointerException n) {
            /*
             * The db file is empty.
             */
            AlarmBox.display("אין תרגולים קיימים כרגע בקובץ ההרצאות של הקורס הנבחר");
            return null;
        }
        catch (IndexOutOfBoundsException e){
            /*
             * The db file is empty.
             */
            AlarmBox.display("אין תרגולים קיימים כרגע בקובץ ההרצאות של הקורס הנבחר");
            return null;
        }
        catch (Exception e){
            AlarmBox.display("Error while reading the csv tirgulim file");
            UI.Restrart();
        }
        return tirgulim;
    }
}
