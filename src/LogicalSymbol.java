public class LogicalSymbol {
    private LogicalSymbolTypeEnum type;
    private int precedence;

    public LogicalSymbol(LogicalSymbolTypeEnum type) {
        this.type = type;

        if (type == LogicalSymbolTypeEnum.NEGATION) {
            this.precedence = 6;
        } else if (type == LogicalSymbolTypeEnum.CONJUNCTION) {
            this.precedence = 5;
        } else if (type == LogicalSymbolTypeEnum.XOR) {
            this.precedence = 4;
        } else if (type == LogicalSymbolTypeEnum.DISJUNCTION) {
            this.precedence = 3;
        } else if (type == LogicalSymbolTypeEnum.IMPLICATION) {
            this.precedence = 2;
        } else {
            this.precedence = 1;
        }
    }

    public LogicalSymbolTypeEnum getType() {
        return type;
    }

    public int getPrecedence() {
        return precedence;
    }
}
