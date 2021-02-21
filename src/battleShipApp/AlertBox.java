package battleShipApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;


public class AlertBox {

    public static void display(String title, String message){
        Stage awindow = new Stage();

        awindow.initModality(Modality.APPLICATION_MODAL);
        awindow.setTitle(title);
        awindow.setMinHeight(125);
        awindow.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        Button ok = new Button("Ok");
        ok.setOnAction(e-> awindow.close());


        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,ok);
        layout.setAlignment(Pos.CENTER);



        Scene scene = new Scene(layout);
        awindow.setScene(scene);
        awindow.setResizable(false);
        awindow.showAndWait();

    }
}
