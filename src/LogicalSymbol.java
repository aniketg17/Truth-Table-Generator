public class LogicalSymbol {
    private LogicalSymbolTypeEnum type;
    private int precedence;
    private char symbol;  // symbol used for consistent internal representation while building ADT

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

    public LogicalSymbolTypeEnum getType() {
        return type;
    }

    public int getPrecedence() {
        return precedence;
    }

    public char getSymbol() {
        return symbol;
    }
}
