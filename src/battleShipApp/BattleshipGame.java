package battleShipApp;
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
    private Random random = new Random();


    private Parent createContent(){
        BorderPane root = new BorderPane();
        root.setPrefSize(600,800);

        enemyOcean = new Ocean(true, event -> { //handler defined for that type of event
            Cell cell = (Cell) event.getSource(); //the cell that the user clicked to shoot
            //human plays
            if (cell.wasShot)
                return;
            cell.shoot();

            //enemyTurn = !cell.shoot();

            if (enemyOcean.shipsAlive == 0) {
                System.out.println("YOU WIN");
                System.exit(0);
            }
            enemyTurn = true;
            /*if (enemyTurn)
                enemyMove();

             */
            enemyMove();
        });

        playerOcean = new Ocean(false, event -> {
            //Cell cell = (Cell) event.getSource();
            //startGame();
        });

        //Set the Board with the Oceans
        VBox vbox = new VBox(50, enemyOcean, playerOcean);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);
        //To start the game click the button
        Button btn = new Button("Place ships");
        btn.setOnMouseClicked(event -> startGame());
        btn.setAlignment(Pos.CENTER);
        root.setRight(btn);

        return root;

    }
    public  Ocean getEnemyOcean(){
        return enemyOcean;
    }
    public Ocean getPlayerOcean(){
        return playerOcean;
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
        int turn = random.nextInt(2);
        if(turn == 1) {//enemy's turn
            enemyTurn = true;
            enemyMove();
        }
    }

    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerOcean.getCell(x, y);
            if (cell.wasShot) // the enemy has already shot that shell of mine
                continue;
            cell.shoot();
            enemyTurn = false;

            //enemyTurn = !cell.shoot(); // shoot the cell and end of enemy's turn

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
    public static void main(String[] args)  {
        launch(args);

        /*//readInput
        int [][] user_placements =  ReadInput.inputPlacement("C:\\Users\\Χριστίνα-Μαρία\\IdeaProjects\\BattleShip\\medialab\\player_default.txt");
        //int [][] enemy_placements = ReadInput.inputPlacement("C:\\Users\\Χριστίνα-Μαρία\\IdeaProjects\\Test\\src\\enemy_default.txt");
        try{
            ReadInput.validate(user_placements);
            //ReadInput.validate(enemy_placements);
            Ocean ocean = new Ocean();
            ocean.shipPlacement(user_placements);


        }
        catch(Exception m){
            System.out.println(m);
        }

         */

    }


}
