package battleShipApp;


public abstract class Ship {

    private int seats;
    private int points;
    private int bonus;
    private int row;
    private int column;
    private boolean vertical;
    public int health;

    //getters to be overriden
    public abstract String getType();

    public abstract int getSeats();

    public abstract int getPoints();

    public abstract int getBonus();


    //setters
    public void setSeats(int seats) {
        this.seats = seats;
        this.health = seats;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    //getters
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isVertical() {
        return vertical;
    }


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


    public boolean isAlive() { return health > 0; }

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

    class EmptyShip extends Ship {
        public EmptyShip() {
            setSeats(1);
            setPoints(0);
            setBonus(0);
        }

        @Override
        public String getType() {
            return "Empty";
        }

        @Override
        public int getSeats() {
            return 1;
        }

        @Override
        public int getPoints() {
            return 0;
        }

        @Override
        public int getBonus() {
            return 0;
        }


    }
