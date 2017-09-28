package hw7.expression;

import hw7.expression.exceptions.OverflowException;

/**
 * Created by Александр on 08.04.2017.
 */
public class CheckedNegate extends UnaryAbstract implements TripleExpression {
    public CheckedNegate(TripleExpression val) {
        super(val);
    }

    private void check(int a) throws OverflowException {
        if(a==Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    public int op(int a) throws Exception {
        check(a);
        return -a;
    }


}
