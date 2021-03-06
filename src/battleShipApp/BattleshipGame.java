package battleShipApp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import battleShipApp.Ocean.Cell;



public class BattleshipGame extends Application{
    private Ocean enemyOcean, playerOcean;
    private boolean enemyTurn,smart,horiz,vert,random_smart, running,loaded;
    private int row, column, prevr, prevc,left, right, up, down, prevleft,prevright, prevup, prevdown;
    private final Random random = new Random();
    Stage window;
    LinkedList<String> playerHistory = new LinkedList<>();//to store the 5 last coordinates,with their results
    LinkedList<String> enemyHistory = new LinkedList<>();
    Label playerInfo = new Label();
    Label enemyInfo = new Label();

    private MenuBar createMenu() throws FileNotFoundException {

        Menu applicationMenu = new Menu("_Application");
        FileInputStream input = new FileInputStream("medialab/images/icon1.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);

        applicationMenu.setGraphic(imageView);

        MenuItem start = new MenuItem("Start");
        start.setOnAction(event -> {
            if(loaded) { //push the button only if the files have been loaded
                try {
                    startGame();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                loaded = false; // so the button start cant be pushed again while playing
            }
        });
        applicationMenu.getItems().add(start);

        MenuItem load = new MenuItem("Load...");
        load.setOnAction(event -> {
            PopupBox popupBox = new PopupBox(playerOcean,enemyOcean,running);
            try {
                loaded = popupBox.display("Scenario-ID", "Define your scenarios");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(loaded){
                enemyOcean = popupBox.getEnemyOcean();
                playerOcean = popupBox.getPlayerOcean();
                if(running) running = false; //so the game can restart when start button is pressed
                playerInfo.setText("Active Ships = "+playerOcean.shipsAlive +"\nPoints = "+enemyOcean.points
                        +"\nTotal shots = "+enemyOcean.shotsFired + "\nSuccessful Shots = "+ enemyOcean.hitCount);
                enemyInfo.setText("Active Ships = "+enemyOcean.shipsAlive +"\nPoints = "
                        +playerOcean.points+"\nTotal shots = "+playerOcean.shotsFired +"\nSuccessful Shots = "+ playerOcean.hitCount);
                playerInfo.getStyleClass().add("label-white");
                enemyInfo.getStyleClass().add("label-white");
            }
        } );
        applicationMenu.getItems().add(load);

        applicationMenu.getItems().add(new SeparatorMenuItem());


        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event ->closeProgram());
        applicationMenu.getItems().add(exit);

        //Menu “Details”
        Menu detailsMenu = new Menu("_Details");
        FileInputStream input2 = new FileInputStream("medialab/images/Cannon.png");
        Image image2 = new Image(input2);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(25);
        imageView2.setFitHeight(25);
        detailsMenu.setGraphic(imageView2);

        MenuItem enemyShips = new MenuItem("Enemy Ships...");
        enemyShips.setOnAction(event -> {
            if(running) {
                AlertBox alertBox = new AlertBox();
                int enemyShipsSunk = enemyOcean.shipSunk;
                int enemyShipsShot = enemyOcean.shipShot;
                int enemyShipsHealthy = enemyOcean.shipsAlive - enemyShipsShot;
                try {
                    alertBox.display("Enemy Ships", "Alive: " + enemyShipsHealthy + "\nShot: " + enemyShipsShot + "\nSunk: " + enemyShipsSunk);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        detailsMenu.getItems().add(enemyShips);

        MenuItem playerShots = new MenuItem("Player Shots...");
        playerShots.setOnAction(event ->{
            if(running) {
                HistoryBox pHistory = new HistoryBox(playerHistory);
                pHistory.display("Player Shots", "The last 5 moves of the player ");
            }
        });
        detailsMenu.getItems().add(playerShots);

        MenuItem enemyShots = new MenuItem("Enemy Shots...");
        enemyShots.setOnAction(event ->{
            if(running) {
                HistoryBox eHistory = new HistoryBox(enemyHistory);
                eHistory.display("Enemy Shots", "The last 5 moves of the enemy ");
            }
        });
        detailsMenu.getItems().add(enemyShots);

        //Main menu bar
        MenuBar menubar = new MenuBar();
        menubar.getMenus().addAll(applicationMenu , detailsMenu);
        return menubar;
    }

    private void startGame() throws FileNotFoundException {
        running = true;
        int turn = random.nextInt(2);
        AlertBox turnBox = new AlertBox();
        if(turn == 1) {//enemy's turn
            enemyTurn = true;
            turnBox.display("Start game","Enemy plays first!");
            enemyMove();
        }
        else{
            turnBox.display("Start game","You play first!");
        }

    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("Exit", "Are you sure you want to exit?");
        if(answer)
            window.close();
    }

    private void restart(){
        running = false;
        playerHistory.clear();
        enemyHistory.clear();
        playerOcean.restartOcean();
        enemyOcean.restartOcean();
        playerInfo.setText("Active Ships = "+playerOcean.shipsAlive +"\nPoints = "+enemyOcean.points
                +"\nTotal shots = "+enemyOcean.shotsFired + "\nSuccessful Shots = "+ enemyOcean.hitCount);
        enemyInfo.setText("Active Ships = "+enemyOcean.shipsAlive +"\nPoints = "
                +playerOcean.points+"\nTotal shots = "+playerOcean.shotsFired +"\nSuccessful Shots = "+ playerOcean.hitCount);
        playerInfo.getStyleClass().add("label-white");
        enemyInfo.getStyleClass().add("label-white");
    }

    private void playerMoves(Cell cell) throws FileNotFoundException {
        if (cell.wasShot)
            return;
        if(cell.shoot())
            addToHistory(playerHistory,"("+cell.row +","+ cell.column +"),hit,"+cell.ship.getType());
        else
            addToHistory(playerHistory,"("+cell.row +","+ cell.column +"),miss");

        enemyOcean.shotsFired++;
        playerInfo.setText("Active Ships = " + playerOcean.shipsAlive + "\nPoints = " + enemyOcean.points
                + "\nTotal shots = " + enemyOcean.shotsFired + "\nSuccessful Shots = " + enemyOcean.hitCount);
        playerInfo.getStyleClass().add("label-white");
        if(enemyOcean.shotsFired ==40){
            AlertBox winBox = new AlertBox();
            if(playerOcean.points > enemyOcean.points ){
                winBox.display("End Game","You completed 40 moves\nEnemy's points:"+playerOcean.points
                        +"\nPlayer points:"+enemyOcean.points+"\nYou lost");
            }
            else if(enemyOcean.points > playerOcean.points){
                winBox.display("End Game", "You completed 40 moves\nEnemy's points:"+playerOcean.points +
                        "\nPlayer's points:"+enemyOcean.points+"\nYou won");
            }
            else
                winBox.display("End Game","You completed 40 moves\nEnemy's point's:"+playerOcean.points+
                        "\nPlayer's points:"+enemyOcean.points+"\nDraw" );
            restart();
        }
        else {

            if (enemyOcean.shipsAlive == 0) {
                AlertBox loseBox = new AlertBox();
                loseBox.display("End Game", "You sunk all the enemy's ships\nYou won !");
                restart();
            } else {
                enemyTurn = true;
                enemyMove();
            }
        }
    }

    private void enemyMove() throws FileNotFoundException {
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


            enemyTurn = false;
            playerOcean.shotsFired++;
            enemyInfo.setText("Active Ships = " + enemyOcean.shipsAlive + "\nPoints = " + playerOcean.points +
                    "\nTotal shots = " + playerOcean.shotsFired + "\nSuccessful Shots = " + playerOcean.hitCount);
            if(playerOcean.shotsFired ==40){
                AlertBox winBox = new AlertBox();
                if(playerOcean.points > enemyOcean.points ){
                    winBox.display("End Game","Enemy completed 40 moves\nEnemy's points:"+playerOcean.points
                            +"\nPlayer points:"+enemyOcean.points+"\nYou lost");
                }
                else if(enemyOcean.points > playerOcean.points){
                    winBox.display("End Game", "Enemy completed 40 moves\nEnemy's points:"+playerOcean.points
                            +"\nPlayer points:"+enemyOcean.points+"\nYou won");
                }
                else
                    winBox.display("End Game","Enemy completed 40 moves\nEnemy's points:"+playerOcean.points
                            +"\nPlayer points:"+enemyOcean.points+"\nDraw");
                restart();
            }

            if (playerOcean.shipsAlive == 0) {
                AlertBox loseBox = new AlertBox();
                loseBox.display("End Game", "ALl your ships are sunk\nYou lost !");
                restart();
            }


        }
    }

    public void addToHistory( LinkedList<String> history , String str){
        if(history.size() == 5)
            history.remove();
        history.add(str);
    }

    public void start(Stage primaryStage) throws FileNotFoundException {
        window = primaryStage;
        window.setTitle("Medialab Battleship");
        FileInputStream input3 = new FileInputStream("medialab/images/icon.jpeg");
        Image image3 = new Image(input3);
        window.getIcons().add(image3);
        window.setOnCloseRequest(e-> {
            e.consume();
            closeProgram();
        });

        BorderPane root = new BorderPane();
        //root.setPrefSize(2000,800);

        enemyOcean = new Ocean(true, event -> {
            if(running) {//cant shoot if the game hasnt started
                Cell cell = (Cell) event.getSource(); //the cell that the user clicked to shoot
                try {
                    playerMoves(cell);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        playerOcean = new Ocean(false, event -> {  });

        GridPane infopane = new GridPane();
        infopane.setHgap(520);
        infopane.setVgap(10);
        Label plLabel = new Label("Player Info:");
        plLabel.getStyleClass().add("label-white");
        infopane.add(plLabel, 1, 0);
        infopane.add(playerInfo, 1, 1);
        Label enLabel = new Label("Enemy Info:");
        enLabel.getStyleClass().add("label-white");
        infopane.add(enLabel, 0, 0);
        infopane.add(enemyInfo, 0, 1);
        infopane.setAlignment(Pos.CENTER);


        //Set the Board with the Oceans
        HBox hBox = new HBox(250, setBoard(playerOcean), setBoard(enemyOcean));
        hBox.setAlignment(Pos.CENTER);

        Label mine = new Label("Player's Ocean");
        mine.getStyleClass().add("label-white");
        Label yours = new Label("Enemy's Ocean");
        yours.getStyleClass().add("label-white");
        HBox hBox2 = new HBox(520, mine,yours);
        hBox2.setAlignment(Pos.CENTER);

        Label title = new Label("Define the row and the column of your next shot");
        title.getStyleClass().add("label-white");
        VBox vbox = new VBox(20,infopane,hBox,hBox2,title,shootArea());
        vbox.setAlignment(Pos.CENTER);

        root.setTop(createMenu());
        root.setCenter(vbox);


        Scene scene = new Scene(root, 1200, 800);  //createContent returns root
        scene.getStylesheets().add(getClass().getResource("styleMe.css").toExternalForm());

        FileInputStream input = new FileInputStream("medialab/images/shoot.jpg");
        Image image = new Image(input);
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0,1.0,true,true,true,true));
        Background background = new Background(backgroundimage);
        vbox.setBackground(background);

        window.setScene(scene);
        window.show();
    }

    public GridPane shootArea()  {
        //Input field for player to shoot
        GridPane gridpane = new GridPane();
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        Label row = new Label("Row:");
        row.getStyleClass().add("label-white");
        gridpane.add(row, 0, 1);
        ChoiceBox<Integer> rowChoice = new ChoiceBox<>();
        rowChoice.getItems().addAll(0,1,2,3,4,5,6,7,8,9);
        gridpane.add(rowChoice,1,1);
        Label col = new Label("Column:");
        col.getStyleClass().add("label-white");
        gridpane.add(col, 2,1);
        ChoiceBox<Integer> colChoice = new ChoiceBox<>();
        colChoice.getItems().addAll(0,1,2,3,4,5,6,7,8,9);
        gridpane.add(colChoice,3,1);
        Button shootBtn = new Button("Shoot");
        gridpane.add(shootBtn,4,1);
        shootBtn.setOnAction(e -> {
            if(running) {
                try {
                    getChoice(rowChoice,colChoice);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });
        gridpane.setAlignment(Pos.CENTER);

        return gridpane;
    }

    private void getChoice(ChoiceBox<Integer> rowChoice, ChoiceBox<Integer> colChoice) throws FileNotFoundException {
        int row = rowChoice.getValue();
        int column = colChoice.getValue();
        Cell cell = enemyOcean.getCell(row,column);
        playerMoves(cell);
    }


    public HBox setBoard(Ocean ocean){
        HBox columns = new HBox();
        for(int c =0; c<10; c++){
            Rectangle rec = new Rectangle(30,30);
            rec.setFill(Color.TRANSPARENT);
            rec.setStroke(Color.BLACK);
            Text num = new Text(String.valueOf(c));
            StackPane stack = new StackPane();
            stack.getChildren().addAll(rec,num);
            columns.getChildren().add(stack);
        }
        VBox rows = new VBox();
        for(int r=0; r<11; r++){
            Rectangle rec = new Rectangle(30,30);
            rec.setFill(Color.TRANSPARENT);
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



    public static void main(String[] args)  { launch(args);}
}
