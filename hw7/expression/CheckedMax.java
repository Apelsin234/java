package hw7.expression;

/**
 * Created by Александр on 10.04.2017.
 */
public class CheckedMax extends BinaryAbstract implements TripleExpression {

    public CheckedMax(TripleExpression a, TripleExpression b) {
        super(a, b);
    }

    protected int op(int x, int y) throws Exception {
        return x > y ? x : y;
    }
}
