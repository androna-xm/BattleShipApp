package battleShipApp;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.scene.control.TextArea;

import java.io.*;
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

    private boolean prepareGame( boolean player) throws FileNotFoundException {
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
            System.out.println("The game didn't start");
        }
        return placed ;
    }


    public  boolean display(String title, String message) throws FileNotFoundException {
        loaded = false;
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinHeight(400);
        window.setMinWidth(400);
        FileInputStream input = new FileInputStream("medialab/images/icon.jpeg");
        Image image = new Image(input);
        window.getIcons().add(image);

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

        Button btnPlayer = new Button("Player Check");
        btnPlayer.setOnAction(event -> {
            saveToFile(playerTextArea, "medialab/player_SCENARIO-ID.txt");
            if(running)
                playerOcean.restartOcean();
            try {
                player_check = prepareGame(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
            try {
                enemy_check = prepareGame(false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(enemy_check){
                enemyTextArea.setEditable(false);
                btnEnemy.setDisable(true);
            }

        });
        Button btn = new Button("Complete");
        btn.setOnAction(e -> {
            if(enemy_check && player_check) {
                loaded = true;
                window.close();
            }
        });


        GridPane gridpane = new GridPane();
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        gridpane.add(label,1,0);
        gridpane.add(playerScenario,0,1);
        gridpane.add(playerTextArea,1,1);
        gridpane.add(btnPlayer,2,1);
        gridpane.add(enemyScenario,0,2);
        gridpane.add(enemyTextArea,1,2);
        gridpane.add(btnEnemy,2,2);
        gridpane.add(btn, 1,3);
        gridpane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridpane);
        window.setScene(scene);
        window.showAndWait();

        return loaded;



    }
}
