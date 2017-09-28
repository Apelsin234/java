package hw7.expression;

import hw7.expression.exceptions.ParsingException;

/**
 * Created by Александр on 02.04.2017.
 */
public class CheckedSqrt extends UnaryAbstract implements TripleExpression {

    public CheckedSqrt(TripleExpression val) {
        super(val);
    }

    private void check(int a) throws ParsingException {
        if (a < 0) {
            throw new ParsingException("sqrt negative number");
        }
    }

    public int op(int a) throws Exception {
        check(a);
        int l = -1;
        int r = 46341 ;
        while (r - l > 1) {
            int m = (l + r) / 2;
            if (m * m <= a) {
                l = m;
            } else {
                r = m;
            }
        }
        return l;
    }
}
