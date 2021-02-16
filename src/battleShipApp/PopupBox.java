package battleShipApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;


public class PopupBox {
    static boolean complete;

    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(250);

        Label label = new Label();
        label.setText(message);

        Button button = new Button("Complete");
        button.setOnAction(event -> {
            complete = true;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,button);
        label.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();



    }
}
