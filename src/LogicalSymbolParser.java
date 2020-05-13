public class LogicalSymbolParser {

    /*
      Converts the passed in parameter character 'symbol' into
      its equivalent enum form based on unicode values
     */

    public static LogicalSymbolTypeEnum getParsedSymbol(char symbol) throws InvalidSymbolException {
        if (symbol == '\u21d2' || symbol == '\u2192') {
            return LogicalSymbolTypeEnum.IMPLICATION;
        } else if (symbol == '\u21d4' || symbol == '\u2261' || symbol == '\u2194') {
            return LogicalSymbolTypeEnum.BICONDITIONAL;
        } else if (symbol == '\u00ac' || symbol == '\u02dc' || symbol == '\u0021') {
            return LogicalSymbolTypeEnum.NEGATION;
        } else if (symbol == '\u2227' || symbol == '\u0026' || symbol == '\u00b7') {
            return LogicalSymbolTypeEnum.CONJUNCTION;
        } else if (symbol == '\u2228' || symbol == '\u002b' || symbol == '\u2225') {
            return LogicalSymbolTypeEnum.DISJUNCTION;
        } else if (symbol == '\u2295' || symbol == '\u22bb') {
            return LogicalSymbolTypeEnum.XOR;
        } else if (symbol == '(' || symbol == '[' || symbol == '{') {
            return LogicalSymbolTypeEnum.BRACKET;
        } else {
            throw new InvalidSymbolException("At least one of the input symbols is invalid.");
        }
    }

    public static boolean isLogicalSymbol(char symbol) {
        return symbol == '\u21d2' || symbol == '\u2192' || symbol == '\u21d4' ||
                symbol == '\u2261' || symbol == '\u2194' ||
                symbol == '\u00ac' || symbol == '\u02dc' || symbol == '\u0021' ||
                symbol == '\u2227' || symbol == '\u0026' || symbol == '\u00b7' ||
                symbol == '\u2228' || symbol == '\u002b' || symbol == '\u2225' ||
                symbol == '\u2295' || symbol == '\u22bb';
    }
}
