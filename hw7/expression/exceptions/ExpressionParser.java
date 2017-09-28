package hw7.expression.exceptions;

import hw7.expression.*;

/**
 * Created by Александр on 25.03.2017.
 */
public class ExpressionParser implements Parser {
    private enum Set {MINUS, PLUS, NUMBER, VARIABLE, STAR, SLASH, LBRACKET, RBRACKET, ABS, MIN, MAX, SQRT, LOG, POW}


    private int index;
    private String expression;
    private int constant;
    private char variable;
    private Set current;
    private boolean erro;

    private boolean checked() {
        if (current == Set.STAR || current == Set.SLASH || current == Set.MIN || current == Set.MAX || current == Set.PLUS) {
            return true;
        } else {
            return false;
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(getNextChar())) {
        }
        index--;
    }

    private char getNextChar() {
        if (index < expression.length()) {
            return expression.charAt(index++);
        } else {
            return '\0';
        }

    }

    private void number(char ch) throws OverflowException {
        StringBuilder str = new StringBuilder();
        do {
            str.append(ch);
            ch = getNextChar();
        }
        while (Character.isDigit(ch));
        try {
            constant = Integer.parseInt(str.toString());
        } catch (Exception e) {
            throw new OverflowException(str.toString());
        }

        index--;
        current = Set.NUMBER;
    }

    private void getNext() throws ParsingException, OverflowException {
        skipWhitespace();
        char c = getNextChar();
        boolean b = true;
        int index_cor = index;
        if (Character.isDigit(c)) {
            number(c);
        } else {
            switch (c) {
                case '-':
                    current = Set.MINUS;
                    break;
                case '+':
                    current = Set.PLUS;
                    break;
                case '/':
                    current = Set.SLASH;
                    break;
                case '*':
                    current = Set.STAR;
                    break;
                case ')':
                    current = Set.RBRACKET;
                    break;
                case '(':
                    current = Set.LBRACKET;
                    break;
                case 'x':
                case 'y':
                case 'z':
                    current = Set.VARIABLE;
                    variable = c;
                    break;

                default:
                    if (index + 4 <= expression.length() && expression.substring(index - 1, index + 3).equals("log2")) {
                        current = Set.LOG;
                        index += 3;
                    } else {
                        if (index + 4 <= expression.length() && expression.substring(index - 1, index + 3).equals("pow2")) {
                            current = Set.POW;
                            index += 3;
                        } else {
                            if (index + 3 <= expression.length() && expression.substring(index - 1, index + 2).equals("abs")) {
                                current = Set.ABS;
                                index += 2;
                            } else {
                                if (index + 4 <= expression.length() && expression.substring(index - 1, index + 3).equals("sqrt")) {
                                    current = Set.SQRT;
                                    index += 3;
                                } else {
                                    if (index + 3 <= expression.length() && expression.substring(index - 1, index + 2).equals("min")) {
                                        current = Set.MIN;
                                        index += 2;
                                    } else {
                                        if (index + 3 <= expression.length() && expression.substring(index - 1, index + 2).equals("max")) {
                                            current = Set.MAX;
                                            index += 2;
                                        } else {
                                            if (!Character.isWhitespace(c)) {
                                                b = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!b || !check_post(index)) {
                        if (!b) {
                            index--;
                        }
                        StringBuilder str = new StringBuilder();
                        StringBuilder str2 = new StringBuilder();
                        int index_inv = index;
                        while (!check_pre(index)) {
                            str.append(expression.charAt(index--));
                        }
                        index_cor = index + 2;
                        index = index_inv + 1;
                        while (!check_post(index)) {
                            str2.append(expression.charAt(index++));
                        }

                        throw new ParsingException("Could not determine command or variable \""
                                + str.reverse().toString() + str2.toString() + "\" in index: " + index_cor);
                    }

            }
        }

        if (checked() && erro) {
            StringBuilder sCor = new StringBuilder();
            int i;
            for (i = 0; i < index_cor - 1; i++) {
                sCor.append(' ');
            }
            sCor.append('^');
            for (++i; i < expression.length(); i++) {
                sCor.append(' ');
            }

            throw new ParsingException("Missing argument at index: " + (index_cor) + '\n' + expression + '\n' + sCor.toString());
        } else {
            erro = checked();
        }
    }

    private boolean check_post(int index) {
        if (index < expression.length()) {
            return Character.isWhitespace(expression.charAt(index)) || expression.charAt(index) == '-'
                    || expression.charAt(index) == '(';
        } else {
            return true;
        }
    }

    private boolean check_pre(int index) {
        if (index >= 0) {
            return Character.isWhitespace(expression.charAt(index)) || expression.charAt(index) == '-'
                    || expression.charAt(index) == ')' || expression.charAt(index) == '(';
        } else {
            return true;
        }
    }

    private TripleExpression fraction() throws ParsingException, OverflowException {
        getNext();
        TripleExpression ans;
        switch (current) {
            case NUMBER:
                ans = new Const(constant);
                getNext();
                break;
            case VARIABLE:
                ans = new Variable(Character.toString(variable));
                getNext();
                break;
            case LOG:
                ans = new CheckedLog(fraction());
                break;
            case POW:
                ans = new CheckedPow(fraction());
                break;
            case ABS:
                ans = new CheckedAbs(fraction());
                break;
            case SQRT:
                ans = new CheckedSqrt(fraction());
                break;
            case MINUS:
                if (Character.isDigit(expression.charAt(index))) {
                    number('-');
                    ans = new Const(constant);
                    getNext();
                } else {
                    ans = new CheckedNegate(fraction());
                }
                break;
            case LBRACKET:
                ans = minAndMax();
                getNext();
                break;

            default:
                throw new ParsingException("incorrect symbol at position: " + index);
        }
        return ans;
    }

    private TripleExpression divAndMul() throws ParsingException, OverflowException {
        TripleExpression a = fraction();
        while (true) {
            switch (current) {
                case SLASH:
                    a = new CheckedDivide(a, fraction());
                    break;
                case STAR:
                    a = new CheckedMultiply(a, fraction());
                    break;
                default:
                    return a;
            }
        }
    }

    private TripleExpression subAndAdd() throws ParsingException, OverflowException {
        TripleExpression a = divAndMul();
        while (true) {
            switch (current) {
                case MINUS:
                    a = new CheckedSubtract(a, divAndMul());
                    break;
                case PLUS:
                    a = new CheckedAdd(a, divAndMul());
                    break;
                default:
                    return a;
            }

        }
    }

    private TripleExpression minAndMax() throws ParsingException, OverflowException {
        TripleExpression a = subAndAdd();
        while (true) {
            switch (current) {
                case MIN:
                    a = new CheckedMin(a, subAndAdd());
                    break;
                case MAX:
                    a = new CheckedMax(a, subAndAdd());
                    break;
                default:
                    return a;
            }

        }
    }

    public TripleExpression parse(String expression) throws ParsingException, OverflowException {
        index = 0;
        int balance = 0;
        erro = true;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                balance++;
            } else if (expression.charAt(i) == ')') {
                balance--;
            }
            if (balance < 0) {
                throw new ParsingException("No opening parenthesis int index:" + (i + 1));
            }
        }
        if (balance != 0) {
            throw new ParsingException("No closing parenthesis");
        }
        this.expression = expression;
        return minAndMax();
    }
}
