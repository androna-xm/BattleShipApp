package battleShipApp;
import java.sql.SQLOutput;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import battleShipApp.Ocean.Cell;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;
import static java.lang.Math.min;

/*
public class BattleshipGame extends Application {
    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Medialab Battleship");



        Scene scene = new Scene(root, 300, 275)
        primaryStage.setScene();
        primaryStage.show();
    }
   */

public class BattleshipGame extends Application{
    private Ocean enemyOcean, playerOcean;
    private boolean enemyTurn;
    private boolean smart = false;
    boolean horiz = false;
    boolean vert = false;
    private int row, column, prevr, prevc,left, right, up, down, prevleft,prevright, prevup, prevdown;
    private Random random = new Random();
    boolean random_smart;
    int horizMiss, vertMiss = 0;

    private Parent createContent(){
        BorderPane root = new BorderPane();
        root.setPrefSize(600,800);

        enemyOcean = new Ocean(true, event -> { //handler defined for that type of event
            Cell cell = (Cell) event.getSource(); //the cell that the user clicked to shoot
            //human plays
            if (cell.wasShot)
                return;
            cell.shoot();
            if (enemyOcean.shipsAlive == 0) {
                System.out.println("YOU WIN");
                System.exit(0);
            }
            enemyTurn = true;
            enemyMove();
        });

        playerOcean = new Ocean(false, event -> {  });

        //Set the Board with the Oceans
        VBox vbox = new VBox(50, enemyOcean, playerOcean);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);
        //To start the game click the button
        Button btn = new Button("Place ships");
        btn.setOnMouseClicked(event -> {
            startGame();
            btn.setDisable(true);
        });
        btn.setAlignment(Pos.CENTER);
        root.setRight(btn);

        return root;

    }

    private void startGame() {
        // place ships
        int [][] user_placements =  ReadInput.inputPlacement("C:\\Users\\Χριστίνα-Μαρία\\IdeaProjects\\BattleShip\\medialab\\player_default.txt");
        int [][] enemy_placements = ReadInput.inputPlacement("C:\\Users\\Χριστίνα-Μαρία\\IdeaProjects\\BattleShip\\medialab\\enemy_default.txt");
        try{
            ReadInput.validate(user_placements);
            playerOcean.shipPlacement(user_placements);
            ReadInput.validate(enemy_placements);
            enemyOcean.shipPlacement(enemy_placements);
        }
        catch(Exception m){
            System.out.println(m);
        }
        //pick turn
        int turn = random.nextInt(1);
        System.out.println(turn);
        if(turn == 1) {//enemy's turn
            enemyTurn = true;
            enemyMove();
        }
    }

    private void myNeighbors ( Ocean ocean, int row, int column ){
        //right
        int left = column;
        int right = column;
        while( right < 9 && ocean.getCell(row,right+1).wasShot){
            right++;
        }
        while( left > 0  && ocean.getCell(row, left -1).wasShot){
            left--;
        }
    }
    private int goVertical(int row){

    }
    private void enemyMove() {
        while (enemyTurn) {
            if (smart) {
                if (horiz) {
                    random_smart = false;
                    System.out.println("I am smart and should look horizontal");
                    prevleft = left;
                    prevright = right;
                    if (left == 0 || playerOcean.getCell(row,left -1).wasShot) { // trapped in left
                        if(playerOcean.getCell(row,right+1).wasShot){ // trapped in left and right
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                            horiz = false; // peritto
                            System.out.println("i am trapped so Random pick: row "+row+" column "+column);
                        }
                        else{
                            column = right + 1;
                            System.out.println("Go right : row "+row +" column "+column);
                        }

                    }
                    else if (right == 9 || playerOcean.getCell(row,right+1).wasShot) {//trapped in right
                        if( playerOcean.getCell(row,left -1 ).wasShot){
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                            System.out.println("i am trapped so Random pick: row "+row+" column "+column);
                            horiz = false;//peritto
                        }
                        else {
                            column = left - 1;
                            System.out.println("Go left: row "+row +" column "+column);
                        }
                    }
                    else {
                        column = Math.random() < 0.5 ? left - 1 : right + 1;
                        System.out.println("Go random left or right: row "+row +" column "+column);
                    }
                }
                else if (vert) {
                    random_smart = false;
                    System.out.println("I am smart and should look vertical");
                    prevup = up;
                    prevdown = down;
                    if (up == 0 || playerOcean.getCell(up-1,column).wasShot ) {
                        if(playerOcean.getCell(down+1,column).wasShot) {
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                            System.out.println("i am trapped so Random pick : row "+row+" column "+column);
                            vert = false; //peritto
                        }
                        else {
                            System.out.println("Go down: row "+row+" column "+column);
                            row = down + 1;
                        }

                    }
                    else if (down == 9 || playerOcean.getCell(down+1,column).wasShot) {
                        if(playerOcean.getCell(up-1,column).wasShot){
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                            System.out.println("i am trapped so Random pick: row "+row+" column "+column);
                            vert = false; //peritto
                        }
                        else{
                            row = up - 1;
                            System.out.println("Go up: row "+row+" column "+column);
                        }
                    }
                    else {
                        row = Math.random() < 0.5 ? up - 1 : down + 1;
                        System.out.println("go random up or down: row "+row+" column "+column);
                    }
                }
                else { // random
                    random_smart = true;
                    System.out.println("i play random smart: row "+ row +"column " + column);
                    prevr = row;
                    prevc = column;
                    double ran = Math.random();
                    if (ran < 0.5) { //horizontal
                        if(column == 0 ) {
                            if(playerOcean.getCell(row,column+1).wasShot) row = goVertical(row);
                            else column ++;
                        }
                        else if( column == 9 ) {
                            if(playerOcean.getCell(row,column-1).wasShot) row = goVertical(row);
                            else column --;
                        }
                        else column = Math.random() < 0.5 ? column - 1 : column + 1;
                    }
                    else { //vertical
                        if(row == 0 ) row++;
                        else if (row ==9) row --;
                        else row = Math.random() < 0.5 ? row - 1 : row + 1;
                    }
                }
            }
            else {
                row = random.nextInt(10);
                column = random.nextInt(10);
                System.out.println("Random pick : row "+row+" column "+column);
            }

            //System.out.println("smart = " + smart + "\nrow " + row + " prevr " + prevr + "\ncolumn " + column + " prevc " + prevc);
            Cell cell = playerOcean.getCell(row, column);

            if (cell.wasShot) {// the enemy has already shot that shell of mine
                System.out.println("wasShot");
                row = prevr;
                column = prevc;
                continue;
            }

            if (cell.shoot()) { // shoot and hit a ship
                smart = true;
                //prevr = row;
                //prevc = column;
                left = right = column;
                //left = min(prevleft, column);
                //right = max(prevright, column);
                up = down = row;
                horiz = vert = random_smart= false;
                while (right < 9 && playerOcean.getCell(row, right + 1).wasShot && playerOcean.getCell(row, right + 1).ship != null) {
                    right++;
                    horiz = true;
                    System.out.println("Horizontal");

                }
                while (left > 0 && playerOcean.getCell(row, left - 1).wasShot && playerOcean.getCell(row, left - 1).ship != null) {
                    left--;
                    horiz = true;
                    System.out.println("Horizontal");
                }
                //prevleft = left;
                //prevright = right;
                if (!horiz) {
                    while (up > 0 && playerOcean.getCell(up - 1, column).wasShot && playerOcean.getCell(up - 1, column).ship != null) {
                        up--;
                        vert = true;
                        System.out.println("Vertical");
                    }
                    while (down < 9 && playerOcean.getCell(down + 1, column).wasShot && playerOcean.getCell(down + 1, column).ship != null) {
                        down++;
                        vert = true;
                        System.out.println("Vertical");
                    }
                }

            }
            else {
                if (random_smart) {
                    System.out.println("missed but random smart");
                    smart = true; // peritto
                    row = prevr;
                    column = prevc;
                    System.out.println("row " + row + " column "+column);
                }
                else if(horiz){
                    System.out.println("missed but horiz");
                    horizMiss++;
                    if(horizMiss == 2){
                        System.out.println("horiz miss 2 ");
                        smart = false;
                        horiz = false;
                    }
                    else {
                        smart = true;
                        left = prevleft;
                        right = prevright;
                    }
                }
                else if(vert){
                    System.out.println("missed but vert");
                    vertMiss++;
                    if(vertMiss == 2){
                        System.out.println("vert miss 2 ");
                        smart = false;
                        vert = false;
                    }
                    else {
                        smart = true;
                        up = prevup;
                        down = prevdown;
                    }
                }
                else smart = false;
            }

            enemyTurn = false;

            if (playerOcean.shipsAlive == 0) {
                System.out.println("YOU LOSE");
                System.exit(0);
            }

        }
    }

    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("battle.fxml"));
        primaryStage.setTitle("Medialab Battleship");
        Scene scene = new Scene(createContent(), 600, 900);  //createContent returns root
        //Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args)  { launch(args);}
}
