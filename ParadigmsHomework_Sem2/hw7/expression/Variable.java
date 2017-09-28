package hw7.expression;

/**
 * Created by Александр on 19.03.2017.
 */
public class Variable implements TripleExpression {

    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public int evaluate(int x, int y, int z) {
        switch (name) {
            case "y":
                return y;
            case "x":
                return x;
            case "z":
                return z;
        }
        return 0;
    }
}
