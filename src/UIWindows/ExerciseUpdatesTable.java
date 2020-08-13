package UIWindows;

import Data.DataStructures.Exercise;
import Data.DataStructures.ExerciseUpdate;
import Data.DataStructures.UpdatesManager;
import Errors.BackupFailureException;
import javafx.beans.binding.DoubleBinding;
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
import org.omg.CORBA.OBJECT_NOT_EXIST;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


public class ExerciseUpdatesTable {

    private static String course;
    private static Exercise exercise;
    static private int next;

    public static void start(String course_name,Exercise ex) {
        TableView<ExerciseUpdate> table = new TableView<>();
        course = course_name;
        exercise = ex;

        Stage windows = new Stage();
        windows.setTitle("גיליון עדכונים");

        // Title for the table
        final Label title1 = new Label(" מעקב עדכונים של  " + course_name + " עבודה מספר " + exercise.getId());
        title1.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 22; -fx-text-fill: darkred");


        // Id column
        TableColumn<ExerciseUpdate, Integer> Id_coulmn = new TableColumn<>("מס' עדכון");
        Id_coulmn.setMinWidth(90);
        Id_coulmn.setMaxWidth(90);
        Id_coulmn.setStyle("-fx-alignment: CENTER");
        Id_coulmn.setCellValueFactory(new PropertyValueFactory<>("update_number"));

        //Update date column
        TableColumn<ExerciseUpdate, String> update_date_column = new TableColumn<>("תאריך עדכון אחרון");
        update_date_column.setMinWidth(90);
        update_date_column.setMaxWidth(90);
        update_date_column.setCellValueFactory(new PropertyValueFactory<>("update_date"));

        // Update Text column
        TableColumn<ExerciseUpdate, String> update_text_column = new TableColumn<>("נושא העדכון");
        update_text_column.setMinWidth(120);
        update_text_column.setPrefWidth(120);
        update_text_column.setCellValueFactory(new PropertyValueFactory<>("Text"));

        // Exercise Update Input
        TextArea update_input = new TextArea();
        update_input.setPromptText("הוספת פונקציות חדשות למחלקת TEST");
        update_input.setPrefWidth(210);
        update_input.setPrefHeight(75);

        // Updates related buttons
        Button AddupdateButton = new Button("הוספת עדכון");
        AddupdateButton.setOnAction(e -> AddupdateButtonClicked(table,update_input));
        AddupdateButton.setStyle("-fx-background-color: #a6b5c9");
        AddupdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Button updateButton = new Button("עדכון הטקסט לעדכון שנבחר");
        updateButton.setOnAction(e -> updateButtonClicked(table,update_input));
        updateButton.setStyle("-fx-background-color: #a6b5c9");
        updateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Button deleteUpdateButton = new Button("למחיקת עדכון שסומן");
        deleteUpdateButton.setOnAction(e -> deleteUpdateButtonClicked(table));
        deleteUpdateButton.setStyle("-fx-background-color: #a6b5c9");
        deleteUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        // Backup Button
        Button backupUpdateButton = new Button("עדכן את קובץ הגיבוי בכל העדכונים החדשים");
        backupUpdateButton.setOnAction(e -> backupUpdateButtonClicked());
        backupUpdateButton.setStyle("-fx-background-color: #a6b5c9");
        backupUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Button useBackupUpdateButton = new Button("העבר את המידע מהגיבוי לקובץ הראשי");
        useBackupUpdateButton.setOnAction(e -> useBackupUpdateButtonClicked(table));
        useBackupUpdateButton.setStyle("-fx-background-color: #a6b5c9");
        useBackupUpdateButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));


        ObservableList<ExerciseUpdate> updates = getAllUpdates();
        if(updates.size() == 0)
            next=1;
        else
            next = updates.size()+1;

        table.setItems(updates);
        table.getColumns().addAll(Id_coulmn,update_date_column,update_text_column);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox hBox_update = new HBox();
        hBox_update.setPadding(new Insets(10,10,10,10));
        hBox_update.setSpacing(10);
        hBox_update.getChildren().addAll(update_input,AddupdateButton, updateButton,deleteUpdateButton);

        HBox titleHBOX1=new HBox();
        titleHBOX1.getChildren().add(title1);
        titleHBOX1.setAlignment(Pos.BASELINE_RIGHT);

        HBox backupHBOX=new HBox();
        backupHBOX.setSpacing(10);
        backupHBOX.getChildren().addAll(backupUpdateButton,useBackupUpdateButton);
        backupHBOX.setAlignment(Pos.BASELINE_RIGHT);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(titleHBOX1,table,hBox_update,backupHBOX);

        Scene scene = new Scene(vBox);
        windows.setScene(scene);
        windows.show();
    }

    /**
     * Move the data from backup to main when the button is clicked.
     */
    private static void useBackupUpdateButtonClicked(TableView<ExerciseUpdate> table) {
        try {
            exercise.getUpdatesManager().updateMainFromBackup();
            AlarmBox.display("העברת המידע הועברה בהצלחה! מעדכן את הטבלה...");
            updateObservableListAndUpdateTable(table,exercise.getUpdatesManager().getUpdates());
        }
        catch (IOException i){AlarmBox.display("אי אפשר לגשת לקובץ:" +
                "Data\\\\Exercises\\\\\" + course + \"Ex_X_UpdatesBackup.txt File. \n" +
                "בבקשה תבדוק שהקובץ לא נפגע במידע לא תקין או שלא נמחק");}
        catch (ParseException p){AlarmBox.display("המידע בקובץ הגיבוי לא תקין. תבדוק בבקשה שאין רווחים מיותרים \n" +
                "או תאריך לא בפורמט וכו'.");}
    }

    /**
     * Update the backup file when the button is clicked.
     */
    private static void backupUpdateButtonClicked() {
        try {
            exercise.getUpdatesManager().updateBackupFromMain();
            AlarmBox.display("העדכון בוצע בהצלחה!");
        }
        catch (IOException i){AlarmBox.display("אי אפשר לגשת לקובץ:" +
                "Data\\\\Exercises\\\\\" + course + \"Ex_X_UpdatesBackup.txt File. \n" +
                "בבקשה תבדוק שהקובץ לא נפגע במידע לא תקין או שלא נמחק");}

    }

    /**
     * Updates the update date of an exercise when the "Update button" is pushed.
     * The new date is then rewritten to the db file.
     * @param table - the table instance being used by the UI
     */
    private static void updateDate(TableView<ExerciseUpdate> table) {
        try {
            ExerciseUpdate update_selected;
            update_selected = table.getSelectionModel().getSelectedItems().get(0);
            exercise.getUpdatesManager().updateExistingUpdateDate(update_selected);
            table.refresh();
        }
        catch (IOException i){ AlarmBox.display("Can't add the new data to the database. Please check that the Exercises data base is \n" +
                "closed or not been corrupted");}
        catch (OBJECT_NOT_EXIST o){ AlarmBox.display("The chosen update doesn't exist the the DS and DB. \n" +
                "Please check the DB file or use the Backup.");}
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}
    }

    /**
     * This function is used to update the observable list with new Array of updates.
     * A new observable list is created. The old one is replaced with the new one
     * Most used when an update is deleted.
     * @param tableView - the current table view
     * @param new_updates - the ArrayList of updates to add to the new obserablelist
     */
    private static void updateObservableListAndUpdateTable(TableView tableView,ArrayList<ExerciseUpdate> new_updates){
        ObservableList<ExerciseUpdate> updates = FXCollections.observableArrayList();
        // Add all the updates to the observablelist
        for (int i = 0; i < new_updates.size(); i++)
            updates.add(new_updates.get(i));

        tableView.setItems(updates);
        tableView.refresh();
    }

    private static void deleteUpdateButtonClicked(TableView<ExerciseUpdate> table) {
        try {
            next--;
            ExerciseUpdate update_selected = table.getSelectionModel().getSelectedItems().get(0);
            // Delete the update from the DS and DB and update the table with a new observable list
            ArrayList<ExerciseUpdate> new_updates = exercise.getUpdatesManager().deleteUpdate(update_selected.getUpdate_number());
            updateObservableListAndUpdateTable(table,new_updates);
        }
        catch (IOException i){AlarmBox.display("Can't access Data\\Exercises\\" + course + "Ex_X_Updates.txt File");}
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}
    }

    private static void updateButtonClicked(TableView<ExerciseUpdate> table, TextArea update_input) {
        try {
            String updated_data = "";
            for (String line : update_input.getText().split("\\n")) updated_data += line + " ";
            ExerciseUpdate update_selected = table.getSelectionModel().getSelectedItems().get(0);
            update_selected.setText(updated_data);
            exercise.getUpdatesManager().updateExistingUpdateText(update_selected, updated_data);
            updateDate(table);
            table.refresh();
        }
        catch (IOException i){AlarmBox.display("Can't access Data\\Exercises\\" + course + "Ex_X_Updates.txt File");}
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}
        catch (OBJECT_NOT_EXIST o){ AlarmBox.display("The chosen update doesn't exist the the DS and DB. \n" +
                "Please check the DB file or use the Backup.");}
    }

    /**
     * Get all the ArrayList of ExerciseUpdates Objects from the UpdatesManager
     * @return ObservableList<ExerciseUpdate> - the current table updates
     */
    private static ObservableList<ExerciseUpdate> getAllUpdates() {
        ArrayList<ExerciseUpdate> temp = exercise.getUpdatesManager().getUpdates();
        ObservableList<ExerciseUpdate> updates = FXCollections.observableArrayList();
        // Add all the exercise's updates to the observablelist for update observation
        for (int i = 0; i < temp.size(); i++)
            updates.add(temp.get(i));

        return updates;
    }

    private static void AddupdateButtonClicked(TableView<ExerciseUpdate> table,TextArea update_input) {
        String updated_data = "";
        for (String line : update_input.getText().split("\\n")) updated_data += line + " ";

        try {
            // Add the new data to the chosen exercise.
            ExerciseUpdate EU = new ExerciseUpdate(next++,updated_data);
            exercise.getUpdatesManager().addNewUpdate(EU);
            table.getItems().add(EU);
        }

        catch (IOException i){AlarmBox.display("Can't access Data\\Exercises\\" + course + "Ex_X_Updates.txt File");}
        catch (NullPointerException n){AlarmBox.display("Please click on an exercise \n " +
                "before pushing the button");}
    }


}
