package hw7.expression;

/**
 * Created by Александр on 19.03.2017.
 */

abstract public class BinaryAbstract implements TripleExpression {
    private TripleExpression first;
    private TripleExpression second;

    protected BinaryAbstract(TripleExpression a, TripleExpression b) {
        first = a;
        second = b;
    }

    public int evaluate(int x, int y, int z) throws Exception {
        return op(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    protected abstract int op(int x, int y) throws Exception;


}
