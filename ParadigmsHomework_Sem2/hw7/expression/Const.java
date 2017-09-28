package hw7.expression;

/**
 * Created by Александр on 19.03.2017.
 */
public class Const implements TripleExpression {
    private int con;

    public Const(int con) {
        this.con = con;
    }

    public int evaluate(int x, int y, int z) {
        return con;
    }
}
