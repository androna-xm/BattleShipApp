package battleShipApp;

public class InvalidCountException extends  Exception{
    public InvalidCountException() {}
    public InvalidCountException(String message){
        super(message);
    }
}
