package hw7.expression;

import hw7.expression.exceptions.ParsingException;

/**
 * Created by Александр on 02.04.2017.
 */
public class CheckedLog extends UnaryAbstract implements TripleExpression {

    public CheckedLog(TripleExpression val) {
        super(val);
    }

    private void check(int x) throws Exception {
        if (x <= 0) {
            throw new ParsingException("Logarithm of negative value");
        }

    }

    public int op(int a) throws Exception {
        check(a);
        int k = 0;
        while (a >= 2) {
            a = a >> 1;
            k++;
        }
        return k;
    }
}
