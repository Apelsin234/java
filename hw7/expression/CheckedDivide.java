package hw7.expression;

import hw7.expression.exceptions.DivisionByZeroException;
import hw7.expression.exceptions.OverflowException;

/**
 * Created by Александр on 19.03.2017.
 */
public class CheckedDivide extends BinaryAbstract implements TripleExpression {
    public CheckedDivide(TripleExpression a, TripleExpression b) {
        super(a, b);
    }

    private void check(int x, int y) throws Exception {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException();
        }
        if (y == 0) {
            throw new DivisionByZeroException();
        }
    }

    protected int op(int x, int y) throws Exception {
        check(x, y);
        return x / y;
    }
}
