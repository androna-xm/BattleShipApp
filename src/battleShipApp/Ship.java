package battleShipApp;

/**
 * Abstract class, constructor of the Object Ship. A Ship object encapsulates the information needed to
 * identify a ship type and its location on the table  given by the user.
 */

public abstract class Ship {

    private int seats;
    private int points;
    private int bonus;
    private int row;
    private int column;
    private boolean vertical;
    public int health;

    //getters to be overriden

    /**
     * Returns the type of the ship Object as a String.
     * @return String
     */
    public abstract String getType();

    /**
     * Returns the number of seats that the ship has.
     * @return  number of seats
     */
    public abstract int getSeats();

    /**
     * Returns the points that the user gets ,when hitting a part of that ship.
     * @return points
     */
    public abstract int getPoints();

    /**
     * Returns the bonus points that the user gets if the ship is sunk (all of its seats are hit)
     * @return bonus points
     */
    public abstract int getBonus();


    //setters

    /**
     * Defines the number of seats a ship has according to its type.
     * Ship's health is initialized to the number of seats, showing that none of the seats has been shot yet.
     * @param seats the number of seats
     */
    public void setSeats(int seats) {
        this.seats = seats;
        this.health = seats;
    }

    /**
     * Defines the points gained from a shot at a ship's seat, according to its type.
     * @param points the points
     */

    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Defines the bonus points gained if the ship is sunk,according to its type.
     * Bonus points are gained when all seats are hit successfully.
     * @param bonus the bonus points
     */
    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    /**
     * The number of the row that the first seat of the ship has.
     * The number of the row is between 1 and 9 .
     * @param row the number of the row
     */

    public void setRow(int row) {
        this.row = row;
    }

    /**
     * The number of the column that the first seat of the ship has.
     * The number of the column is between 1 and 9 .
     * @param column the number of the column
     */

    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Defines if the ship is placed vertically or horizontally on the table.
     * @param vertical boolean set true if the ship is places vertically
     */
    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    //getters

    /**
     * Returns the row of the first seat of the ship, a number between 1 and 9.
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Return the column of the first seat of the seat, a number between 1 and 9.
     * @return column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns true if the ship's orientation os vertical, and false if horizontal.
     * @return true of false
     */

    public boolean isVertical() {
        return vertical;
    }

    /**
     * Checks if the ship is placed inside the borders of the table ,given the user input.
     * @throws OversizeException with message that tells which ship is placed out of border.
     */
    public void okToPlaceShipAt() throws OversizeException {
        if (vertical) {
            if (row < 0 || row > 10 - seats || column < 0 || column > 9) {
                throw new OversizeException(" Oversize,Ship "+getType()+" placed out of border");
            }
        } else {
            if (row < 0 || row > 9 || column < 0 || column > 10 - seats) {
                throw new OversizeException(" Oversize,Ship "+getType()+" placed out of border");
            }
        }
    }

    /**
     * Returns if a ship has not been sunk yet, if all his seats haven't been shot.
     * @return true if it is alive, false if it is sunk
     */
    public boolean isAlive() { return health > 0; }

    /**
     * Shows if a ship got its first shot,checking the difference between the seats and the health of the ship
     * @return true or false
     */
    public boolean firstShot() {return (seats - health) ==1;}

}

    class Carrier extends Ship {
        //constructor
        public Carrier() {
            setSeats(5);
            setPoints(350);
            setBonus(1000);
        }

        @Override
        public String getType() {
            return "Carrier";
        }

        @Override
        public int getSeats() {
            return 5;
        }

        @Override
        public int getPoints() {
            return 350;
        }

        @Override
        public int getBonus() {
            return 1000;
        }
    }

    class Battleship extends Ship {
        public Battleship() {
            setSeats(4);
            setPoints(250);
            setBonus(500);
        }

        @Override
        public String getType() {
            return "Battleship";
        }

        @Override
        public int getSeats() {
            return 4;
        }

        @Override
        public int getPoints() {
            return 250;
        }

        @Override
        public int getBonus() {
            return 500;
        }
    }

    class Cruiser extends Ship {

        public Cruiser() {
            setSeats(3);
            setPoints(100);
            setBonus(250);
        }

        @Override
        public String getType() {
            return "Cruiser";
        }

        @Override
        public int getSeats() {
            return 3;
        }

        @Override
        public int getPoints() {
            return 100;
        }

        @Override
        public int getBonus() {
            return 250;
        }
    }

    class Submarine extends Ship {

        public Submarine() {
            setSeats(3);
            setPoints(100);
            setBonus(0);
        }

        @Override
        public String getType() {
            return "Submarine";
        }

        @Override
        public int getSeats() {
            return 3;
        }

        @Override
        public int getPoints() {
            return 100;
        }

        @Override
        public int getBonus() {
            return 0;
        }
    }

    class Destroyer extends Ship {

        public Destroyer() {
            setSeats(2);
            setPoints(50);
            setBonus(0);
        }

        @Override
        public String getType() {
            return "Destroyer";
        }

        @Override
        public int getSeats() {
            return 2;
        }

        @Override
        public int getPoints() {
            return 50;
        }

        @Override
        public int getBonus() {
            return 0;
        }
    }


