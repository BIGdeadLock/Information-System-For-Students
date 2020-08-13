package UIWindows;
import Tools.Files.AFileHandler;
import Tools.Files.FileHandler;
import Tools.Files.FileSearch;
import Tools.Files.TextFileHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SecretariesWindow {

    public static void display() {

        ArrayList<String> data = getSecData();

        Stage windows = new Stage();
        windows.setTitle("Courses information system by E.Y");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(10);
        grid.setHgap(20);

        Label first_name1_label = new Label("First name: ");
        first_name1_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(first_name1_label,0,0);

        Label first_name1_val = new Label(data.get(0).replace("First name: ",""));
        first_name1_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(first_name1_val,1,0);

        Label last_name1_label = new Label("Last name: ");
        last_name1_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(last_name1_label,0,1);

        Label last_name1_val = new Label(data.get(1).replace("Last name: ",""));
        last_name1_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(last_name1_val,1,1);

        Label email1_label = new Label("Email: ");
        email1_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(email1_label,0,2);

        Label email1_val = new Label(data.get(2).replace("Email: ",""));
        email1_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));;
        GridPane.setConstraints(email1_val,1,2);

        Label office1_label = new Label("Office hours: ");
        office1_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(office1_label,0,3);

        Label office1_val = new Label(data.get(3).replace("Office hours: ",""));
        office1_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(office1_val,1,3);

        grid.getChildren().addAll(first_name1_label,first_name1_val,last_name1_label,last_name1_val,
                email1_label,email1_val,office1_label,office1_val);

        Label first_name2_label = new Label("First name: ");
        first_name2_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(first_name2_label,0,5);

        Label first_name2_val = new Label(data.get(5).replace("First name: ",""));
        first_name2_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(first_name2_val,1,5);

        Label last_name2_label = new Label("Last name: ");
        last_name2_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(last_name2_label,0,6);

        Label last_name2_val = new Label(data.get(6).replace("Last name: ",""));
        last_name2_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(last_name2_val,1,6);

        Label email2_label = new Label("Email: ");
        email2_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(email2_label,0,7);

        Label email2_val = new Label(data.get(7).replace("Email: ",""));
        email2_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(email2_val,1,7);

        Label office2_label = new Label("Office hours: ");
        office2_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(office2_label,0,8);

        Label office2_val = new Label(data.get(8).replace("Office hours: ",""));
        office2_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(office2_val,1,8);

        grid.getChildren().addAll(first_name2_label,first_name2_val,last_name2_label,last_name2_val,
                email2_label,email2_val,office2_label,office2_val);

        Label first_name3_label = new Label("First name: ");
        first_name3_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(first_name3_label,0,10);

        Label first_name3_val = new Label(data.get(10).replace("First name: ",""));
        first_name3_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(first_name3_val,1,10);

        Label last_name3_label = new Label("Last name: ");
        last_name3_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(last_name3_label,0,11);

        Label last_name3_val = new Label(data.get(11).replace("Last name: ",""));
        last_name3_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(last_name3_val,1,11);

        Label email3_label = new Label("Email: ");
        email3_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(email3_label,0,12);

        Label email3_val = new Label(data.get(12).replace("Email: ",""));
        email3_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(email3_val,1,12);

        Label office3_label = new Label("Office hours: ");
        office3_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(office3_label,0,13);

        Label office3_val = new Label(data.get(13).replace("Office hours: ",""));
        office3_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(office3_val,1,13);

        grid.getChildren().addAll(first_name3_label,first_name3_val,last_name3_label,last_name3_val,
                email3_label,email3_val,office3_label,office3_val);

        Label first_name4_label = new Label("First name: ");
        first_name4_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(first_name4_label,0,15);

        Label first_name4_val = new Label(data.get(15).replace("First name: ",""));
        first_name4_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(first_name4_val,1,15);

        Label last_name4_label = new Label("Last name: ");
        last_name4_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(last_name4_label,0,16);

        Label last_name4_val = new Label(data.get(16).replace("Last name: ",""));
        last_name4_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(last_name4_val,1,16);

        Label email4_label = new Label("Email: ");
        email4_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(email4_label,0,17);

        Label email4_val = new Label(data.get(17).replace("Email: ",""));
        email4_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(email4_val,1,17);

        Label office4_label = new Label("Office hours: ");
        office4_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(office4_label,0,18);

        Label office4_val = new Label(data.get(18).replace("Office hours: ",""));
        office4_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        GridPane.setConstraints(office4_val,1,18);

        Label phone4_label = new Label("Phone: ");
        phone4_label.setFont(new Font("Arial", 16));
        GridPane.setConstraints(phone4_label,0,19);

        Label phone4_val = new Label(data.get(19).replace("Phone: ",""));
        phone4_val.setFont(Font.font("Verdana", FontWeight.BOLD, 16));;
        GridPane.setConstraints(phone4_val,1,19);

        grid.getChildren().addAll(first_name4_label,first_name4_val,last_name4_label,last_name4_val,
                email4_label,email4_val,office4_label,office4_val,phone4_label,phone4_val);


        Label row_space1 = new Label("*****************");
        row_space1.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred");
        GridPane.setConstraints(row_space1,0,4);

        Label col_space1 = new Label("*****************");
        col_space1.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred");
        GridPane.setConstraints(col_space1,1,4);

        Label row_space2 = new Label("*****************");
        row_space2.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred");
        GridPane.setConstraints(row_space2,0,9);

        Label col_space2 = new Label("*****************");
        col_space2.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred");
        GridPane.setConstraints(col_space2,1,9);

        Label row_space3 = new Label("*****************");
        row_space3.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred");
        GridPane.setConstraints(row_space3,0,14);

        Label col_space3 = new Label("*****************");
        col_space3.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred");
        GridPane.setConstraints(col_space3,1,14);

        grid.getChildren().addAll(row_space1,row_space2,row_space3,
                col_space1,col_space2,col_space3);
        grid.setStyle("-fx-background-color: white");
        Scene scene = new Scene(grid, 450,655);
        windows.setScene(scene);
        windows.show();
    }

    private static ArrayList<String> getSecData() {
        ArrayList<String> data;
        try {
            AFileHandler fileHandler = new TextFileHandler(FileSearch.SearchForFile("SecretariatDB.txt"));
            data = fileHandler.ReadBlock("First name", "==");
            return data;
        } catch (Exception e) {
            AlarmBox.display("Can't access the secretariat database and create a new file handler. Try again");
        }
        return null;
    }

}
