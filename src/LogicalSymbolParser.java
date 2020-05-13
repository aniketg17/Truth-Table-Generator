/**
 * <h1>Class for Parsing Logical Symbols</h1>
 * This class is essentially used as an intermediary between
 * the LogicalSymbol class and the LogicalValueGenerator. Each
 * character (that is not a letter) is parsed by methods here into
 * the enum types of LogicalSymbolTypeEnum and then used in LogicalSymbol
 * <p>
 *
 * @author  Aniket Kumar Gupta
 */

public class LogicalSymbolParser {

    /**
     * This method is used to assign an EnumType to the character passed in. Error handling
     * is done here in case rogue values are passed in.
     * @param symbol This is the character than needs to be parsed
     * @return LogicalSymbolTypeEnum This returns the EnumType associated with the given Unicode value
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

    /**
     * This method is used to figure out whether the parameter passed in
     * is a valid logical operator
     * @param symbol This is the character which is to be determined as a logical operator
     * @return boolean This returns whether the symbol passed in is a valid operator.
     */

    public static boolean isLogicalSymbol(char symbol) {
        return symbol == '\u21d2' || symbol == '\u2192' || symbol == '\u21d4' ||
                symbol == '\u2261' || symbol == '\u2194' ||
                symbol == '\u00ac' || symbol == '\u02dc' || symbol == '\u0021' ||
                symbol == '\u2227' || symbol == '\u0026' || symbol == '\u00b7' ||
                symbol == '\u2228' || symbol == '\u002b' || symbol == '\u2225' ||
                symbol == '\u2295' || symbol == '\u22bb';
    }
}
