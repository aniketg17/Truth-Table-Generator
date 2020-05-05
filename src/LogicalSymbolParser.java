public class LogicalSymbolParser {

    /*
      Converts the passed in parameter character 'symbol' into
      its equivalent enum form based on unicode values
     */

    public static LogicalSymbolType getParsedSymbol(char symbol) {
        if (symbol == '\u21d2' || symbol == '\u2192') {
            return LogicalSymbolType.IMPLICATION;
        } else if (symbol == '\u21d4' || symbol == '\u2261' || symbol == '\u2194') {
            return LogicalSymbolType.BICONDITIONAL;
        } else if (symbol == '\u00ac' || symbol == '\u02dc' || symbol == '\u0021') {
            return LogicalSymbolType.NEGATION;
        } else if (symbol == '\u2227' || symbol == '\u0026' || symbol == '\u00b7') {
            return LogicalSymbolType.CONJUNCTION;
        } else if (symbol == '\u2228' || symbol == '\u002b' || symbol == '\u2225') {
            return LogicalSymbolType.DISJUNCTION;
        } else if (symbol == '\u2295' || symbol == '\u22bb') {
            return LogicalSymbolType.XOR;
        }
        return null;
    }
}
