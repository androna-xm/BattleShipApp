package battleShipApp;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.scene.control.TextArea;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;



public class PopupBox {
    boolean placed, player_check, enemy_check, running;
    Ocean playerOcean,enemyOcean;
    static boolean loaded;

    PopupBox(Ocean playerOcean, Ocean enemyOcean, boolean running){
        this.playerOcean = playerOcean;
        this.enemyOcean = enemyOcean;
        this.running = running;
    }


    public Ocean getEnemyOcean(){
        return enemyOcean;
    }

    public Ocean getPlayerOcean() {
        return playerOcean;
    }

    private static void saveToFile(TextArea textArea, String fileName){
        ObservableList<CharSequence> paragraph = textArea.getParagraphs();
        Iterator<CharSequence>  iter = paragraph.iterator();
        try
        {
            BufferedWriter bf = new BufferedWriter(new FileWriter(fileName));
            while(iter.hasNext())
            {
                CharSequence seq = iter.next();
                bf.append(seq);
                bf.newLine();
            }
            bf.flush();
            bf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private boolean prepareGame( boolean player){
        placed = false;
        try{
            if(player){
                int [][] user_placements =  ReadInput.inputPlacement("medialab/player_SCENARIO-ID.txt");
                ReadInput.validate(user_placements);
                playerOcean.shipPlacement(user_placements);
            }
            else{
                int [][] enemy_placements = ReadInput.inputPlacement("medialab/enemy_SCENARIO-ID.txt");
                ReadInput.validate(enemy_placements);
                enemyOcean.shipPlacement(enemy_placements);
            }
            placed = true;
        }
        catch(Exception m){
            AlertBox.display("Error", m.getMessage() + "\n Please correct the placement");
            System.out.println(m);
            System.out.println("The game didnt start");
        }
        return placed ;
    }


    public  boolean display(String title, String message){
        loaded = false;
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(400);
        window.setMinWidth(400);

        Label label = new Label();
        label.setText(message);

        Label playerScenario = new Label("Player Scenario:");
        TextArea playerTextArea = new TextArea();
        playerTextArea.setPrefColumnCount(7);
        playerTextArea.setPrefRowCount(5);

        Label enemyScenario = new Label("Enemy Scenario:");
        TextArea enemyTextArea = new TextArea();
        enemyTextArea.setPrefColumnCount(7);
        enemyTextArea.setPrefRowCount(5);


        VBox vb1 = new VBox();
        vb1.getChildren().addAll( label, playerScenario , playerTextArea);
        vb1.setSpacing(10);

        Button btn = new Button("Complete");
        btn.setOnAction(e -> {
            if(enemy_check && player_check) {
                loaded = true;
                window.close();
            }
        });
        btn.setAlignment(Pos.CENTER);

        VBox vb2 = new VBox();
        vb2.getChildren().addAll(enemyScenario, enemyTextArea,btn);
        vb2.setSpacing(10);

        Button btnPlayer = new Button("Player Check");
        btnPlayer.setOnAction(event -> {
            saveToFile(playerTextArea, "medialab/player_SCENARIO-ID.txt");
            if(running)
                playerOcean.restartOcean();
            player_check = prepareGame(true);
            if(player_check){
                playerTextArea.setEditable(false); //cant change the scenario once selected
                btnPlayer.setDisable(true);
            }

        });

        Button btnEnemy = new Button("Enemy Check");
        btnEnemy.setOnAction(event -> {
            saveToFile(enemyTextArea,"medialab/enemy_SCENARIO-ID.txt");
            if(running)
                enemyOcean.restartOcean();
            enemy_check = prepareGame(false);
            if(enemy_check){
                enemyTextArea.setEditable(false);
                btnEnemy.setDisable(true);
            }

        });

        HBox hb1 = new HBox(10);
        hb1.getChildren().addAll(vb1,btnPlayer);
        hb1.setAlignment(Pos.CENTER);

        HBox hb2 = new HBox(10);
        hb2.getChildren().addAll(vb2,btnEnemy);
        hb2.setAlignment(Pos.CENTER);



        VBox layout = new VBox();
        layout.getChildren().addAll(hb1, hb2);
        layout.setSpacing(20);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return loaded;



    }
}
