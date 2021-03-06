package battleShipApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ConfirmBox {
    static boolean answer;

    public static boolean display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(125);
        window.setMinWidth(250);
        FileInputStream input = null;
        try {
            input = new FileInputStream("medialab/images/icon.jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);
        window.getIcons().add(image);

        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e-> {
            answer = false;
            window.close();
        });


        HBox hlayout = new HBox(10);
        hlayout.getChildren().addAll(yesButton,noButton);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,hlayout);
        layout.setAlignment(Pos.CENTER);
        hlayout.setAlignment(Pos.CENTER);


        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        return answer;
    }
}
