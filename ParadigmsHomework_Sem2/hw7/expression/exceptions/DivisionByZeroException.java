package hw7.expression.exceptions;

/**
 * Created by Александр on 08.04.2017.
 */
public class DivisionByZeroException extends RuntimeException {
    public DivisionByZeroException() {
        super("division by zero");
    }
}
