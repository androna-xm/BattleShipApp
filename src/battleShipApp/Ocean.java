package battleShipApp;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class Ocean extends Parent {

    int shotsFired; //the total number of shots fired by the user
    int hitCount; //the number of times a shot hit a ship
    int shipSunk;//the number of sunk ships
    int shipsAlive;
    int shipShot;
    int points;
    private VBox rows = new VBox();
    public boolean enemy = false;

    //constructor
    public Ocean(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        shotsFired = 0;
        hitCount = 0;
        shipSunk = 0;
        points = 0;
        shipShot = 0;
        shipsAlive = 5;


        for (int r = 0; r < 10; r++) {
            HBox row = new HBox();
            for (int col = 0; col < 10; col++) {
                Cell c = new Cell(r,col, this);
                if(this.enemy)
                    c.setOnMouseClicked(handler); // when we click the mouse on a cell o handler is defined
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    private void checkNeighbors(Ship ship, int r, int c) throws AdjacentTilesException {
        //above
        if(r>0 && getCell(r-1,c).ship!= null &&  getCell(r-1,c).ship.getSeats()!=ship.getSeats()){
            throw new AdjacentTilesException(ship.getType()+" has neighbor ship above");
        }
        //below
        if (r < 9 && getCell(r + 1,c).ship != null  && getCell(r + 1,c).ship.getSeats() != ship.getSeats()) {
            throw new AdjacentTilesException(ship.getType()+" has neighbor ship below");
        }
        //left
        if (c > 0 && getCell(r,c - 1).ship != null && getCell(r,c - 1).ship.getSeats() != ship.getSeats()) {
            throw new AdjacentTilesException(ship.getType()+" has neighbor ship left");
        }
        //right
        if (c < 9 && getCell(r,c + 1).ship != null && getCell(r,c + 1).ship.getSeats() != ship.getSeats()) {
            throw new AdjacentTilesException(ship.getType()+" has neighbor ship right");
        }
    }

    private void placeShipAt(Ship ship) throws OverlapTilesException, AdjacentTilesException{  //putting a reference to the ship in each of 1 or more locations (up to 5) in the ships
        try {
            if (ship.isVertical()) {
                for (int r = ship.getRow(); r < ship.getRow() + ship.getSeats(); r++) {
                    //Overlap
                    if (getCell(r, ship.getColumn()).ship != null)
                        throw new OverlapTilesException("another ship already in this shell");
                    //Adjacent
                    checkNeighbors(ship, r, ship.getColumn());

                    //Put the new cell of the ship
                    Cell cell = getCell(r, ship.getColumn());
                    cell.ship = ship;
                }
            }
            else {
                for (int c = ship.getColumn(); c < ship.getColumn() + ship.getSeats(); c++) {
                    //Overlap
                    if(getCell(ship.getRow(),c).ship != null)
                        throw new OverlapTilesException("another ship already in this shell");
                    //Adjacent
                    checkNeighbors(ship, ship.getRow(), c);

                    Cell cell = getCell(ship.getRow(), c);
                    cell.ship = ship;
                }
            }
        }
        catch(OverlapTilesException e){
            cleanOcean();
            //System.out.println("cleaned ocean");
            throw new OverlapTilesException(e.getMessage());
        }
        catch(AdjacentTilesException e){
            cleanOcean();
            throw new AdjacentTilesException(e.getMessage());
        }

    }
    private void paintShips(){
        for (int i=0; i<10; i++){
            for(int j =0; j<10; j++){
                Cell cell =getCell(i,j);
                if(cell.ship != null ){
                    cell.setFill(Color.WHITE);
                    cell.setStroke(Color.GREEN);
                }
            }
        }
    }

    public void shipPlacement(int[][] placement) throws OversizeException,OverlapTilesException,AdjacentTilesException{
        Carrier carrier = new Carrier();
        Battleship battleship = new Battleship();
        Cruiser cruiser = new Cruiser();
        Submarine submarine = new Submarine();
        Destroyer destroyer = new Destroyer();

        for (int ship = 0; ship < 5; ship++) {
            switch (placement[ship][0]) {
                case 1 -> {
                    carrier.setRow(placement[ship][1]);
                    carrier.setColumn(placement[ship][2]);
                    if (placement[ship][3] == 2) carrier.setVertical(true);
                }
                case 2 -> {
                    battleship.setRow(placement[ship][1]);
                    battleship.setColumn(placement[ship][2]);
                    if (placement[ship][3] == 2) battleship.setVertical(true);
                }
                case 3 -> {
                    cruiser.setRow(placement[ship][1]);
                    cruiser.setColumn(placement[ship][2]);
                    if (placement[ship][3] == 2) cruiser.setVertical(true);
                }
                case 4 -> {
                    submarine.setRow(placement[ship][1]);
                    submarine.setColumn(placement[ship][2]);
                    if (placement[ship][3] == 2) submarine.setVertical(true);
                }
                case 5 -> {
                    destroyer.setRow(placement[ship][1]);
                    destroyer.setColumn(placement[ship][2]);
                    if (placement[ship][3] == 2) destroyer.setVertical(true);
                }
            }
        }
        try{
            //Oversize
            carrier.okToPlaceShipAt();
            battleship.okToPlaceShipAt();
            cruiser.okToPlaceShipAt();
            submarine.okToPlaceShipAt();
            destroyer.okToPlaceShipAt();

            placeShipAt(carrier);
            placeShipAt(battleship);
            placeShipAt(cruiser);
            placeShipAt(submarine);
            placeShipAt(destroyer);

            if(!enemy) paintShips();
        }
        catch(OversizeException e){
            throw new OversizeException(e.getMessage());
        }
        catch(OverlapTilesException e){
            throw new OverlapTilesException(e.getMessage());
        }
        catch(AdjacentTilesException e){
            throw new AdjacentTilesException(e.getMessage());
        }
    }

    public void cleanOcean(){
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(getCell(i,j).ship != null)
                    getCell(i,j).ship = null;
            }
        }
    }

    public void restartOcean(){
        shotsFired = 0;
        hitCount = 0;
        shipSunk = 0;
        points = 0;
        shipShot = 0;
        shipsAlive = 5;
        for(int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                if(getCell(i,j).ship != null)
                    getCell(i,j).ship = null;
                getCell(i,j).wasShot = false;
                getCell(i,j).setFill(Color.LIGHTGRAY);
                getCell(i,j).setStroke(Color.BLACK);
            }
        }
    }

    public class Cell extends Rectangle{
        public int row, column ;
        public boolean wasShot ;
        public Ship ship;

        private Ocean ocean;

        public Cell(int row, int column, Ocean ocean) {
            super(30, 30);
            this.row = row;
            this.column = column;
            this.ocean = ocean;
            wasShot = false;
            ship = null;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        public boolean shoot() {
            wasShot = true;
            setFill(Color.BLACK);

            if (ship != null) {
                ship.health --;
                hitCount++;
                if(ship.firstShot()) shipShot++;
                points+= ship.getPoints();
                setFill(Color.RED);
                if (!ship.isAlive()) {
                    points += ship.getBonus();
                    shipsAlive --;
                    shipSunk ++;
                    shipShot --;
                }
                return true; //we hit the ship
            }
            return false; //miss
        }
    }

    public Cell getCell(int row, int column) {
        return (Cell)((HBox)rows.getChildren().get(row)).getChildren().get(column);
    }
}



