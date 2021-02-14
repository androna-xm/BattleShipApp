package battleShipApp;

public class OverlapTilesException extends Exception{
    public OverlapTilesException(){}
    public OverlapTilesException(String message ){
        super(message);
    }
}
