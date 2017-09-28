package hw7.expression.exceptions;

/**
 * Created by Александр on 08.04.2017.
 */

public class OverflowException extends Exception {
    public OverflowException() {
        super("overflow in execution");
    }

    public OverflowException(String s) {
        super("overflow number " + s);
    }
}
