package hw7.expression;

import hw7.expression.*;

/**
 * Created by Александр on 19.03.2017.
 */

abstract class UnaryAbstract implements TripleExpression {
    private TripleExpression first;

    protected UnaryAbstract(TripleExpression a) {
        first = a;
    }

    public int evaluate(int x, int y, int z) throws Exception {
        return op(first.evaluate(x, y, z));
    }

    protected abstract int op(int x) throws Exception;


}
