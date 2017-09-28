package hw7.expression;

import hw7.expression.exceptions.OverflowException;
import hw7.expression.exceptions.ParsingException;

/**
 * Created by Александр on 02.04.2017.
 */
public class CheckedPow extends UnaryAbstract implements TripleExpression {

    public CheckedPow(TripleExpression val) {
        super(val);
    }

    private void check(int x) throws Exception {
        if(x > 30 ) {
            throw new OverflowException();
        }
        if(x < 0 ) {
            throw new ParsingException("Pow of negative value");
        }

    }

    public int op(int a) throws Exception {
        check(a);
        return 1 << a;
    }
}
