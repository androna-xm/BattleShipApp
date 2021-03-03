package battleShipApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;


public class HistoryBox {
    static LinkedList<String> historyList;

    public HistoryBox(LinkedList<String> historyList){
        this.historyList = historyList;
    }



    public static void display(String title, String message){
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

        StringBuilder pHistory = new StringBuilder();
        for (String str : historyList) {
            //System.out.print(str + "\n");
            pHistory.append(str).append("\n");
        }
        Label label2 = new Label();
        label2.setText(pHistory.toString());


        Button ok = new Button("Ok");
        ok.setOnAction(e-> window.close());


        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,label2,ok);
        layout.setAlignment(Pos.CENTER);



        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

    }
}
