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
    int shipsAlive = 5;
    int points;
    private VBox rows = new VBox();
    public boolean enemy = false;

    //constructor
    public Ocean(boolean enemy, EventHandler<? super MouseEvent> handler) {
        //create an empty Ocean with EmptyShips
        /*EmptyShip empty = new EmptyShip();
        for (Ship[] row : ships)
            Arrays.fill(row, empty);
         */
        this.enemy = enemy;
        shotsFired = 0;
        hitCount = 0;
        shipSunk = 0;
        points = 0;

        for (int r = 0; r < 10; r++) {
            HBox row = new HBox();
            for (int col = 0; col < 10; col++) {
                Cell c = new Cell(r,col, this);
                if(this.enemy) c.setOnMouseClicked(handler); // when we click the mouse on a cell o handler is defined
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    public static void validateLocation(Ship ship) {
        try {
            ship.okToPlaceShipAt();
        } catch (Exception mes) {
            System.out.println(mes);
        }

    }

    public void checkSeat(int row, int column) throws OverlapTilesException {
        //if (ships[row][column].getSeats() > 1) //not-empty
        if(getCell(row,column).ship != null)
            throw new OverlapTilesException("another ship already in this shell");
    }

    private void checkNeighbors(Ship ship, int r, int c) throws AdjacentTilesException {
        //above
        if(r>0 && getCell(r-1,c).ship!= null &&  getCell(r-1,c).ship.getSeats()!=ship.getSeats()){
            throw new AdjacentTilesException("neighbor ship above");
        }
        //below
        if (r < 9 && getCell(r + 1,c).ship != null  && getCell(r + 1,c).ship.getSeats() != ship.getSeats()) {
            throw new AdjacentTilesException("neighbor ship below");
        }
        //left
        if (c > 0 && getCell(r,c - 1).ship != null && getCell(r,c - 1).ship.getSeats() != ship.getSeats()) {
            throw new AdjacentTilesException("neighbor ship left");
        }
        //right
        if (c < 9 && getCell(r,c + 1).ship != null && getCell(r,c + 1).ship.getSeats() != ship.getSeats()) {
            throw new AdjacentTilesException("neighbor ship right");
        }
    }

    private void placeShipAt(Ship ship, int row, int column, int orientation) {  //putting a reference to the ship in each of 1 or more locations (up to 5) in the ships
        ship.setRow(row);
        ship.setColumn(column);
        if (orientation == 2) {
            ship.setVertical(true);
        }
        validateLocation(ship); //in-border
        try {
            if (ship.isVertical()) {
                for (int r = row; r < row + ship.getSeats(); r++) {
                    checkSeat(r, column);//empty or taken
                    checkNeighbors(ship, r, column);
                    //ships[r][column] = ship;
                    Cell cell = getCell(r, column);
                    cell.ship = ship;
                    if(!enemy){
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            } else {
                for (int c = column; c < column + ship.getSeats(); c++) {
                    checkSeat(row, c);//empty or taken
                    checkNeighbors(ship, row, c);
                    //ships[row][c] = ship;
                    Cell cell = getCell(row, c);
                    cell.ship = ship;
                    if(!enemy){
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
        } catch (Exception mes) {
            System.out.println(mes);
        }
    }
    public Cell getCell(int row, int column) {
        return (Cell)((HBox)rows.getChildren().get(row)).getChildren().get(column);
    }
    public void shipPlacement(int[][] placement) {
        for (int ship = 0; ship < 5; ship++) {
            switch (placement[ship][0]) {
                case 1 -> {
                    Carrier carrier = new Carrier();
                    placeShipAt(carrier, placement[ship][1], placement[ship][2], placement[ship][3]);
                }
                case 2 -> {
                    Battleship battleship = new Battleship();
                    placeShipAt(battleship, placement[ship][1], placement[ship][2], placement[ship][3]);
                }
                case 3 -> {
                    Cruiser cruiser = new Cruiser();
                    placeShipAt(cruiser, placement[ship][1], placement[ship][2], placement[ship][3]);
                }
                case 4 -> {
                    Submarine submarine = new Submarine();
                    placeShipAt(submarine, placement[ship][1], placement[ship][2], placement[ship][3]);
                }
                case 5 -> {
                    Destroyer destroyer = new Destroyer();
                    placeShipAt(destroyer, placement[ship][1], placement[ship][2], placement[ship][3]);
                }
            }
        }
    }

    public class Cell extends Rectangle{
        public int row, column ;
        public boolean wasShot = false;
        public Ship ship =null;

        private Ocean ocean;

        public Cell(int row, int column, Ocean ocean) {
            super(30, 30);
            this.row = row;
            this.column = column;
            this.ocean = ocean;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        public boolean shoot() {
            wasShot = true;
            setFill(Color.BLACK);

            if (ship != null) {
                ship.health --;
                points+= ship.getPoints();
                setFill(Color.RED);
                if (!ship.isAlive()) {
                    points += ship.getBonus();
                    shipsAlive --;
                    shipSunk ++;
                }
                return true; //we hit the ship
            }
            return false; //miss
        }
    }
}

    /*public boolean isOccupied(int row, int column) { //if the given location contains a ship
        System.out.println(ships[row][column].getType());
        System.out.println(ships[row][column].health);
        return ships[row][column].getSeats() != 1;
    }

    public boolean shootAt(int row, int column) {
        if (ships[row][column].isAlive()) {//there is a ship
            ships[row][column].health--;
            if (!ships[row][column].isAlive()) {
                points += ships[row][column].getBonus();
                shipsAlive--;
                shipSunk++;
            } else {
                points += ships[row][column].getPoints();
            }
            return true;
        }
        return false;
    }

     */


    /*
    int getShotsFired()
Returns the number of shots fired (in this game).
int getHitCount()
Returns the number of hits recorded (in this game). All hits are counted, not just the first time a given square is hit.
int getShipsSunk()
Returns the number of ships sunk (in this game).
    boolean isGameOver()
Returns true if all ships have been sunk, otherwise false.
     */


