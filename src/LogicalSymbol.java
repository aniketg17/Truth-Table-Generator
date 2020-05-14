/**
 * <h1>Class for Logical Symbols</h1>
 * This is a class to ensure uniformity between all logical operators
 * equivalent to NOT, AND, OR etc. in English
 * in terms of the associated data as well as
 * the methods that each of them can be used with.
 * <p>
 *
 * @author  Aniket Kumar Gupta
 */

public class LogicalSymbol {
    private LogicalSymbolTypeEnum type; // assigned logical symbol enum type to differentiate each operation
    private int precedence; // for operation's precedence level
    private char symbol;  // symbol used for consistent internal representation

    /**
     * This constructor instantiates each object with the data
     * dependent upon the TypeEnum received. Each logical symbol
     * has its own unique properties, hence the need for enum.
     * @param type This is the enum based on which other fields are initialized
     */

    public LogicalSymbol(LogicalSymbolTypeEnum type) {
        this.type = type;

        if (type == LogicalSymbolTypeEnum.NEGATION) {
            this.precedence = 6;
            this.symbol = '\u00ac';
        } else if (type == LogicalSymbolTypeEnum.CONJUNCTION) {
            this.precedence = 5;
            this.symbol = '\u2227';
        } else if (type == LogicalSymbolTypeEnum.XOR) {
            this.precedence = 4;
            this.symbol = '\u2295';
        } else if (type == LogicalSymbolTypeEnum.DISJUNCTION) {
            this.precedence = 3;
            this.symbol = '\u2228';
        } else if (type == LogicalSymbolTypeEnum.IMPLICATION) {
            this.precedence = 2;
            this.symbol = '\u21d2';
        } else if (type == LogicalSymbolTypeEnum.BICONDITIONAL) {
            this.precedence = 1;
            this.symbol = '\u21d4';
        } else if (type == LogicalSymbolTypeEnum.BRACKET) {
            this.precedence = 7;
            this.symbol = '(';
        }
    }

    /**
     * This method is a getter for the
     * enum type of a logical symbol
     * @return the enum type of a symbol
     */

    public LogicalSymbolTypeEnum getType() {
        return type;
    }

    /**
     * This method is a getter for the
     * precedence level of the logical symbol
     * @return the integer precedence of a symbol
     */

    public int getPrecedence() {
        return precedence;
    }

    /**
     * This method is a getter for the
     * symbol  of the logical symbol
     * @return the (character) symbol associated with the Logical Operator
     */

    public char getSymbol() {
        return symbol;
    }

    /**
     * This method is used to apply the LogicalSymbol's associated operation
     * on the two operands passed in.
     * @param operand1 This is the first paramter to addNum method
     * @param operand2  This is the second parameter to addNum method
     * @return boolean This returns the result of the operation.
     */

    public boolean applyOperation(boolean operand1, boolean operand2) {
        if (type == LogicalSymbolTypeEnum.NEGATION) {
            return !operand1;
        } else if (type == LogicalSymbolTypeEnum.CONJUNCTION) {
            return operand1 & operand2;
        } else if (type == LogicalSymbolTypeEnum.DISJUNCTION) {
            return operand1 | operand2;
        } else if (type == LogicalSymbolTypeEnum.IMPLICATION) {
            return (operand2 | !operand1);
        } else if (type == LogicalSymbolTypeEnum.BICONDITIONAL) {
            return operand1 == operand2;
        } else if (type == LogicalSymbolTypeEnum.XOR) {
            return operand1 ^ operand2;
        }
        return false;
    }
}
