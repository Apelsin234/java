package hw7.expression;

import hw7.expression.exceptions.OverflowException;

/**
 * Created by Александр on 19.03.2017.
 */
public class CheckedAdd extends BinaryAbstract implements TripleExpression {

    public CheckedAdd(TripleExpression a, TripleExpression b) {
        super(a, b);
    }

    private void check(int x, int y) throws Exception {
        if ((x > 0 && y > Integer.MAX_VALUE - x) || (x < 0 && y < Integer.MIN_VALUE - x)) {
            throw new OverflowException();
        }
    }

    protected int op(int x, int y) throws Exception {
        check(x, y);
        return x + y;
    }
}
