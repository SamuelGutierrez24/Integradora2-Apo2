package exceptions;

public class NoSuchCountryException extends RuntimeException{
    
    public NoSuchCountryException(String message){
        super(message);
    }
}
