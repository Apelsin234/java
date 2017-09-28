package hw7.expression;

import hw7.expression.exceptions.OverflowException;

/**
 * Created by Александр on 19.03.2017.
 */
public class CheckedMultiply extends BinaryAbstract implements TripleExpression {
    public CheckedMultiply(TripleExpression a, TripleExpression b) {
        super(a, b);
    }

    private void check(int x, int y) throws Exception {
        if (x > y) {
            check(y, x);
        } else {
            if (x < 0) {
                if (y < 0) {
                    if (x < Integer.MAX_VALUE / y) {
                        throw new OverflowException();
                    }
                } else if (y > 0) {
                    if (Integer.MIN_VALUE / y > x) {
                        throw new OverflowException();
                    }
                }
            } else if (x > 0) {
                if (x > Integer.MAX_VALUE / y) {
                    throw new OverflowException();
                }
            }
        }
    }

    protected int op(int x, int y) throws Exception {
        check(x, y);
        return x * y;
    }
}
