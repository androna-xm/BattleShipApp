package battleShipApp;

public class AdjacentTilesException extends Exception{
    public AdjacentTilesException(){};
    public AdjacentTilesException(String message){
        super(message);
    }
}
