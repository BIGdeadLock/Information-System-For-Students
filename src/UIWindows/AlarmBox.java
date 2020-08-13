package UIWindows;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlarmBox {

    public static void display(String message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Error");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 17; -fx-text-fill: darkred");

        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> window.close());
        closeButton.setStyle("-fx-background-color: #a6b5c9");
        closeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white");

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
