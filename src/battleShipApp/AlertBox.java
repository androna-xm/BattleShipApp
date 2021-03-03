package battleShipApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class AlertBox {

    public static void display(String title, String message) throws FileNotFoundException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(145);
        window.setMinWidth(250);
        FileInputStream input = new FileInputStream("medialab/images/icon.jpeg");
        Image image = new Image(input);
        window.getIcons().add(image);

        Label label = new Label();
        label.setText(message);

        Button ok = new Button("Ok");
        ok.setOnAction(e-> window.close());


        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,ok);
        layout.setAlignment(Pos.CENTER);



        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

    }
}
