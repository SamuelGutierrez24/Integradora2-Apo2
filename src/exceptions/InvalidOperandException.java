package exceptions;

public class InvalidOperandException extends RuntimeException{
    public InvalidOperandException(String message){
        super(message);
    }
}
