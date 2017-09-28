package hw7.expression.exceptions;

import java.security.InvalidParameterException;

/**
 * Created by Александр on 08.04.2017.
 */
public class ParsingException extends Exception {
    public ParsingException(String s) {
        super(s);
    }
    public ParsingException() {
    }
}
