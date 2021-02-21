package battleShipApp;

import java.util.LinkedList;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import battleShipApp.Ocean.Cell;



public class BattleshipGame extends Application{
    private Ocean enemyOcean, playerOcean;
    private boolean enemyTurn,smart,horiz,vert,random_smart, running,loaded;
    private int row, column, prevr, prevc,left, right, up, down, prevleft,prevright, prevup, prevdown;
    private Random random = new Random();
    Stage window;
    LinkedList<String> playerHistory = new LinkedList<String>();//to store the 5 last coordinates,with their results
    LinkedList<String> enemyHistory = new LinkedList<String>();
    Label playerInfo = new Label();
    Label enemyInfo = new Label();

    private MenuBar createMenu(){

        Menu applicationMenu = new Menu("_Application");
        //-------Start--------//
        MenuItem start = new MenuItem("Start");
        start.setOnAction(event -> {
            if(loaded) { //push the button only if the files have been loaded
                if (running) { //if already running
                    enemyOcean.cleanOcean();
                    playerOcean.cleanOcean();
                }
                startGame();
                loaded = false; // so the button start cant be pushed again while playing
            }
        });
        applicationMenu.getItems().add(start);

        //-------------Load------------//
        MenuItem load = new MenuItem("Load...");
        load.setOnAction(event -> {
            PopupBox popupBox = new PopupBox(playerOcean,enemyOcean,running);
            popupBox.display("Scenario-ID", "Define your scenarios");
            enemyOcean = popupBox.getEnemyOcean();
            playerOcean = popupBox.getPlayerOcean();
            loaded = true; // the files are loaded so the game can start
            if(running) running = false; //so the game can restart when start button is pressed
        } );
        applicationMenu.getItems().add(load);

        applicationMenu.getItems().add(new SeparatorMenuItem());

        //------------------Exit-----------//
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event ->closeProgram());
        applicationMenu.getItems().add(exit);

        //Menu “Details”
        Menu detailsMenu = new Menu("Details");

        //---------------Enemy Ships----------//
        MenuItem enemyShips = new MenuItem("Enemy Ships...");
        enemyShips.setOnAction(event -> {
            AlertBox alertBox = new AlertBox();
            int enemyShipsSunk = enemyOcean.shipSunk;
            int enemyShipsShot = enemyOcean.shipShot;
            int enemyShipsHealthy = enemyOcean.shipsAlive - enemyShipsShot;
            alertBox.display("Enemy Ships","Alive: " + enemyShipsHealthy+"\nShot: "+ enemyShipsShot+"\nSunk: "+enemyShipsSunk );
        });
        detailsMenu.getItems().add(enemyShips);

        //----------------Player Shots--------------//
        MenuItem playerShots = new MenuItem("Player Shots...");
        playerShots.setOnAction(event ->{
            HistoryBox pHistory = new HistoryBox(playerHistory);
            pHistory.display("Player Shots", "The last 5 moves of the player ");
        });
        detailsMenu.getItems().add(playerShots);


        //---------------Enemy Shots-------------//
        MenuItem enemyShots = new MenuItem("Enemy Shots...");
        enemyShots.setOnAction(event ->{
            HistoryBox eHistory = new HistoryBox(enemyHistory);
            eHistory.display("Enemy Shots", "The last 5 moves of the enemy ");
        });
        detailsMenu.getItems().add(enemyShots);

        //Main menu bar
        MenuBar menubar = new MenuBar();
        menubar.getMenus().addAll(applicationMenu , detailsMenu);
        return menubar;
    }

    private void startGame() {
        //pick turn
        running = true;
        playerInfo.setText("Player Info:\nActive Ships = "+playerOcean.shipsAlive +"\nPoints = "+playerOcean.points+ "\nSuccessful Shoots = "+ playerOcean.hitCount);
        enemyInfo.setText("Enemy Info:\nActive Ships = "+enemyOcean.shipsAlive +"\nPoints = "+enemyOcean.points+ "\nSuccessful Shoots = "+ enemyOcean.hitCount);
        int turn = random.nextInt(2);
        System.out.println(turn);
        if(turn == 1) {//enemy's turn
            enemyTurn = true;
            enemyMove();
        }

    }

    private void enemyMove() {
        while (enemyTurn) {
            if (smart) {
                if (horiz) {
                    random_smart = false;
                    //System.out.println("I am smart and should look horizontal");
                    prevleft = left;
                    prevright = right;
                    if (left == 0 || playerOcean.getCell(row,left -1).wasShot) { // trapped in left
                        if(playerOcean.getCell(row,right+1).wasShot){ // trapped in left and right
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                            smart = horiz =  false; // peritto
                            //System.out.println("i am trapped so Random pick: row "+row+" column "+column);
                        }
                        else{
                            column = right + 1;
                            //System.out.println("Go right : row "+row +" column "+column);
                        }

                    }
                    else if (right == 9 || playerOcean.getCell(row,right+1).wasShot) {//trapped in right
                        if( playerOcean.getCell(row,left -1 ).wasShot){
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                            //System.out.println("i am trapped so Random pick: row "+row+" column "+column);
                            smart = horiz = false;//peritto
                        }
                        else {
                            column = left - 1;
                            //System.out.println("Go left: row "+row +" column "+column);
                        }
                    }
                    else {
                        column = Math.random() < 0.5 ? left - 1 : right + 1;
                        //System.out.println("Go random left or right: row "+row +" column "+column);
                    }
                }
                else if (vert) {
                    random_smart = false;
                    //System.out.println("I am smart and should look vertical");
                    prevup = up;
                    prevdown = down;
                    if (up == 0 || playerOcean.getCell(up-1,column).wasShot ) {
                        if(playerOcean.getCell(down+1,column).wasShot) {
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                            //System.out.println("i am trapped so Random pick : row "+row+" column "+column);
                            smart = vert= false; //peritto

                        }
                        else {
                            //System.out.println("Go down: row "+row+" column "+column);
                            row = down + 1;
                        }

                    }
                    else if (down == 9 || playerOcean.getCell(down+1,column).wasShot) {
                        if(playerOcean.getCell(up-1,column).wasShot){
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                            //System.out.println("i am trapped so Random pick: row "+row+" column "+column);
                            smart = vert = false; //peritto
                        }
                        else{
                            row = up - 1;
                            //System.out.println("Go up: row "+row+" column "+column);
                        }
                    }
                    else {
                        row = Math.random() < 0.5 ? up - 1 : down + 1;
                        //System.out.println("go random up or down: row "+row+" column "+column);
                    }
                }
                else { // random
                    random_smart = true;
                    //System.out.println("i play random smart: row "+ row +"column " + column);
                    prevr = row;
                    prevc = column;
                    double ran = Math.random();
                    if (ran < 0.5) { //horizontal
                        if(column == 0 ) column ++;
                        else if( column == 9 ) column --;
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
                //System.out.println("Random pick : row "+row+" column "+column);
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
                addToHistory(enemyHistory, "("+row +","+ column +"),hit,"+cell.ship.getType());
                smart = true;
                left = right = column;
                up = down = row;
                horiz = vert = random_smart= false;
                while (right < 9 && playerOcean.getCell(row, right + 1).wasShot && playerOcean.getCell(row, right + 1).ship != null) {
                    right++;
                    horiz = true;
                    //System.out.println("Horizontal");

                }
                while (left > 0 && playerOcean.getCell(row, left - 1).wasShot && playerOcean.getCell(row, left - 1).ship != null) {
                    left--;
                    horiz = true;
                    //System.out.println("Horizontal");
                }
                if (!horiz) {
                    while (up > 0 && playerOcean.getCell(up - 1, column).wasShot && playerOcean.getCell(up - 1, column).ship != null) {
                        up--;
                        vert = true;
                        //System.out.println("Vertical");
                    }
                    while (down < 9 && playerOcean.getCell(down + 1, column).wasShot && playerOcean.getCell(down + 1, column).ship != null) {
                        down++;
                        vert = true;
                       // System.out.println("Vertical");
                    }
                }

            }
            else {
                addToHistory(enemyHistory, "("+row +","+ column +"),miss");
                if (random_smart) {
                    //System.out.println("missed but random smart");
                    //smart = true; // peritto
                    row = prevr;
                    column = prevc;
                    //System.out.println("row " + row + " column "+column);
                }
                else if(horiz){
                    //System.out.println("missed but horiz");
                    left = prevleft;
                    right = prevright;

                }
                else if(vert){
                    //System.out.println("missed but vert");
                    up = prevup;
                    down = prevdown;
                }
                else smart = false;
            }
            enemyInfo.setText("Enemy Info:\nActive Ships = "+enemyOcean.shipsAlive +"\nPoints = "+playerOcean.points+ "\nSuccessful Shoots = "+ playerOcean.hitCount);

            enemyTurn = false;

            if (playerOcean.shipsAlive == 0) {
                System.out.println("YOU LOSE");
                System.exit(0);
            }

        }
    }

    public void addToHistory( LinkedList<String> history , String str){
        if(history.size() == 5)
            history.remove();
        history.add(str);
    }
    public void start(Stage primaryStage){
        window = primaryStage;
        window.setTitle("Medialab Battleship");
        window.setOnCloseRequest(e-> {
            e.consume();
            closeProgram();
        });

        BorderPane root = new BorderPane();
        root.setPrefSize(800,800);

        enemyOcean = new Ocean(true, event -> {
            if(running) {//cant shoot if the game hasnt started
                Cell cell = (Cell) event.getSource(); //the cell that the user clicked to shoot
                //human plays
                if (cell.wasShot)
                    return;
                if(cell.shoot())
                    addToHistory(playerHistory,"("+cell.row +","+ cell.column +"),hit,"+cell.ship.getType());
                else
                    addToHistory(playerHistory,"("+cell.row +","+ cell.column +"),miss");
                playerInfo.setText("Player Info: \nActive Ships = "+playerOcean.shipsAlive +"\nPoints = "+enemyOcean.points+ "\nSuccessful Shoots = "+ enemyOcean.hitCount);
                if (enemyOcean.shipsAlive == 0) {
                    System.out.println("YOU WIN");
                    System.exit(0);
                }
                enemyTurn = true;
                enemyMove();
            }
        });

        playerOcean = new Ocean(false, event -> {  });


        HBox infoBox = new HBox(20);


        infoBox.getChildren().addAll(playerInfo,enemyInfo);
        infoBox.setAlignment(Pos.CENTER);



        //Set the Board with the Oceans
        VBox vbox = new VBox(50,infoBox, setBoard(enemyOcean), setBoard(playerOcean));
        vbox.setAlignment(Pos.CENTER);

        root.setTop(createMenu());
        root.setCenter(vbox);

        Scene scene = new Scene(root, 600, 900);  //createContent returns root
        //Scene scene = new Scene(root);

        window.setScene(scene);
        window.show();
    }

    public HBox setBoard(Ocean ocean){
        HBox columns = new HBox();
        for(int c =0; c<10; c++){
            Rectangle rec = new Rectangle(30,30);
            rec.setFill(Color.WHITE);
            rec.setStroke(Color.BLACK);
            Text num = new Text(String.valueOf(c));
            StackPane stack = new StackPane();
            stack.getChildren().addAll(rec,num);
            columns.getChildren().add(stack);
        }
        VBox rows = new VBox();
        for(int r=0; r<11; r++){
            Rectangle rec = new Rectangle(30,30);
            rec.setFill(Color.WHITE);
            rec.setStroke(Color.BLACK);
            if(r == 0) {
                rows.getChildren().add(rec);
            }
            else{
                Text num = new Text(String.valueOf(r-1));
                StackPane stack = new StackPane();
                stack.getChildren().addAll(rec,num);
                rows.getChildren().add(stack);
            }
        }
        VBox box = new VBox (columns,ocean);
        HBox total = new HBox(rows,box);
        total.setAlignment(Pos.CENTER);
        return total;
    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("Exit", "Are you sure you want to exit?");
        if(answer)
            window.close();
    }

    public static void main(String[] args)  { launch(args);}
}
