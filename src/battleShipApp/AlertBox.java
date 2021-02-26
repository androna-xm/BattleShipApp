package battleShipApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;


public class AlertBox {

    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(145);
        window.setMinWidth(250);

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
