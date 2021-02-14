package battleShipApp;

public class OversizeException extends  Exception{
    public OversizeException() {}
    public OversizeException(String message){
        super(message);
    }
}
