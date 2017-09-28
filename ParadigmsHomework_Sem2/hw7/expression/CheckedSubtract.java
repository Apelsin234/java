package hw7.expression;

import hw7.expression.exceptions.OverflowException;

/**
 * Created by Александр on 19.03.2017.
 */
public class CheckedSubtract extends BinaryAbstract implements TripleExpression {
    public CheckedSubtract(TripleExpression a, TripleExpression b) {
        super(a, b);
    }

    private void check(int x, int y) throws Exception {
        if ((y > 0 && x < Integer.MIN_VALUE + y) || (y < 0 && x > Integer.MAX_VALUE + y)) {
            throw new OverflowException();
        }
    }

    protected int op(int x, int y) throws Exception {
        check(x,y);
        return x - y;
    }
}
