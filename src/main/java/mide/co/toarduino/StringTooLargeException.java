package mide.co.toarduino;

/**
 * Created by Olumide on 3/19/2015.
 */
public class StringTooLargeException extends Exception{
    public StringTooLargeException(){
        super();
    }

    public StringTooLargeException(String message){
        super(message);
    }
}
