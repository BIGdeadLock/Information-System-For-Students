package UIWindows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageBox {


    public static void display(String message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("הודעה");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 17; -fx-text-fill: BLUE");

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
