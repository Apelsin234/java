package hw7.expression;

import hw7.expression.exceptions.OverflowException;

/**
 * Created by Александр on 02.04.2017.
 */
public class CheckedAbs extends UnaryAbstract implements TripleExpression {

    public CheckedAbs(TripleExpression val) {
        super(val);
    }

    private void check(int x) throws OverflowException {
        if(x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }

    }

    public int op(int a) throws Exception {
        check(a);
        return (a > 0 ? a : -a);
    }
}
