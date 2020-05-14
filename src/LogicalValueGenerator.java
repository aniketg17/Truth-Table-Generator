import java.util.*;

/**
 * <h1>Logical Value Generation (Main Class)</h1>
 * This class constructs the binary grid for each of the variables
 * and for each row of binary values
 * calculates the result of the provided expression. This is the class
 * where all of the expression evaluation is done.
 * <p>
 *
 * @author  Aniket Kumar Gupta
 */

public class LogicalValueGenerator {
    private TreeMap<Character, Boolean> variables = new TreeMap<>(); // use to maintain the total number
    // of variables in expression
    private String expressionToConvert;
    private String[][] binaryGrid;

    /**
     * This constructor initialises its class object with the string to be evaluated
     * @param expressionToConvert This is the expression that needs to be evaluated
     */

    public LogicalValueGenerator(String expressionToConvert) {
        this.expressionToConvert = expressionToConvert;
    }


    /**
     * This method is a getter for the binaryGrid generated
     * @return String[][] This returns the 2D String binary grid.
     */

    public String[][] getBinaryGrid() {
        return binaryGrid;
    }

    /**
     * This method is used to assign each variable in the treemap
     * a truth value and then depending on that call the method
     * to evaluate the expression
     * @return boolean[] This returns the boolean array with evaluated truth values.
     */

    public boolean[] truthValuesGenerator() throws InvalidSymbolException {
        if (expressionToConvert == null || expressionToConvert.equals("")) {
            throw new InvalidSymbolException("No expression to parse!");
        }
        Queue<Object> rpnQueue = postfixConversion(expressionToConvert); // converted expression in RPN
        binaryGrid = fillGrid(); // binary grid ready
        Queue<Object> queueClone;
        boolean[] truthValues = new boolean[(int) Math.pow(2, variables.size())];

        for (int i = 0; i < binaryGrid.length; i++) {
            int iterate = 0;
            for (Character character : variables.keySet()) {
                boolean value;
                if (binaryGrid[i][iterate].equals("False")) {
                    value = false;
                } else {
                    value = true;
                }
                variables.replace(character, value); // update treemap with new values
                iterate++;
            }
            queueClone = new LinkedList<>(rpnQueue);
            truthValues[i] = evaluateExpression(queueClone, variables); // based on treemap values,
                                                                        // calculate the expression value
        }

        return truthValues;
    }

    /**
     * This method is a getter for the TreeMap storing variables
     * @return TreeMap This returns TreeMap containing the variables with current truth values.
     */

    public TreeMap<Character, Boolean> getVariables() {
        return variables;
    }

    /**
     * This method is used to fill the 2D grid depending on which, for each set of truth values of
     * variables, the expression is evaluated row-wise.
     * @return String[][] This returns the String 2D array containing all possible combinations
     * of truth values for all variables
     */

    public String[][] fillGrid() {
        String[][] grid = new String[(int) Math.pow(2, variables.size())][variables.size()];

        for (int i = 0; i < grid.length; i++) {
            String row = convertBinary(i);
            grid[i] = row.split(" ");
        }
        return grid;
    }

    /**
     * This method is used to convert a number to binary
     * and then represent those binary values as a String containing
     * "True" and "False". For example, the binary number 10 would mean
     * "True False"
     * @param num This is the number to be represented as String
     * @return String This returns the string as combination of True and False.
     */

    public String convertBinary(int num) {
        StringBuilder convertedBinary = new StringBuilder();
        int stringAdditions = 0;
        while (num > 0) {
            int rem = num % 2;
            num = num / 2;
            if (rem == 0) {
                convertedBinary.insert(0, "False ");
                ++stringAdditions;
            } else {
                convertedBinary.insert(0, "True ");
                ++stringAdditions;
            }
        }

        if (stringAdditions < variables.size()) {
            while (stringAdditions < variables.size()) {
                convertedBinary.insert(0, "False ");
                ++stringAdditions;
            }
        }
        return convertedBinary.toString();
    }


    /**
     * This method uses a variation of Shunting Yard algorithm
     * to evaluate the expression converted already to reverse Polish
     * notation. Based on that, it returns a truth value.
     * @param rpnExpression This is the logical expression in RPN represented as a queue
     * @param values  This TreeMap contains the variables along with their associated truth values
     * @return boolean This returns the truth value of the expression for the variable values
     * passed in
     */

    public boolean evaluateExpression(Queue<Object> rpnExpression, TreeMap<Character, Boolean> values) throws InvalidSymbolException {
        Stack<Boolean> variables = new Stack<>();

        try {
            while (!rpnExpression.isEmpty()) {
                Object removedItem = rpnExpression.remove();
                if (Character.isLetter((Character) removedItem)) {
                    variables.add(values.get(removedItem));
                } else {
                    LogicalSymbolTypeEnum parsedType = LogicalSymbolParser.getParsedSymbol((Character) removedItem);
                    LogicalSymbol currentSymbol = new LogicalSymbol(parsedType);
                    if (currentSymbol.getType() != LogicalSymbolTypeEnum.NEGATION) {
                        boolean operandTwo = variables.pop(); // assigning operandTwo first because for implication order matters
                        boolean operandOne = variables.pop();
                        variables.add(currentSymbol.applyOperation(operandOne, operandTwo));
                    } else {
                        boolean operandOne = variables.pop();
                        variables.add(currentSymbol.applyOperation(operandOne, false));
                    }
                }
            }
            return variables.pop();
        } catch (EmptyStackException e) {
            throw new InvalidSymbolException("Invalid Expression Syntax");
        }
    }

    /**
     * This method is used to convert an expression into RPN
     * using Stacks and Queues. Validation for brackets is also included
     * to ensure that the number of brackets and their forms are correct.
     * @param original This is the string in its original form to be converted to RPN
     * @return Queue This returns the string in RPN as a queue of Objects
     */

    public Queue<Object> postfixConversion(String original) throws InvalidSymbolException {
        Queue<Object> rpnConverted = new LinkedList<>();
        Stack<LogicalSymbol> operators = new Stack<>();
        Stack<Character> bracketValidation = new Stack<>(); // used to ensure consistent use of brackets in string

        try {
            for (int i = 0; i < original.length(); i++) {
                char currentChar = original.charAt(i);
                if (Character.isLetter(original.charAt(i))) {
                    rpnConverted.add(currentChar);
                    variables.put(currentChar, false);
                } else if (currentChar == ')' || currentChar == ']' || currentChar == '}') {
                    if (bracketValidation.isEmpty() || bracketValidation.pop() != currentChar) {
                        throw new InvalidSymbolException("Brackets are malformed!");
                    }
                    while (!operators.isEmpty() && operators.peek().getType() != LogicalSymbolTypeEnum.BRACKET) {
                        rpnConverted.add(operators.pop().getSymbol());
                    }
                    operators.pop();
                } else {
                    LogicalSymbolTypeEnum parsedType = LogicalSymbolParser.getParsedSymbol(currentChar);
                    LogicalSymbol currentSymbol = new LogicalSymbol(parsedType);
                    if (currentSymbol.getType() == LogicalSymbolTypeEnum.BRACKET) {
                        operators.push(currentSymbol);
                        if (currentChar == '(') {
                            bracketValidation.push(')'); // add the required closing bracket to match opening one
                        } else if (currentChar == '[') {
                            bracketValidation.push(']');
                        } else {
                            bracketValidation.push('}');
                        }
                    } else {
                        if (!operators.isEmpty() && operators.peek().getPrecedence() > currentSymbol.getPrecedence()) {
                            while (!operators.isEmpty() &&
                                    operators.peek().getPrecedence() > currentSymbol.getPrecedence() &&
                                    operators.peek().getType() != LogicalSymbolTypeEnum.BRACKET) {
                                LogicalSymbol poppedSymbol = operators.pop();
                                rpnConverted.add(poppedSymbol.getSymbol());
                            }
                            operators.push(currentSymbol);
                        } else {
                            operators.push(currentSymbol);
                        }
                    }
                }
            }
            while (!operators.isEmpty()) {
                rpnConverted.add(operators.pop().getSymbol()); // add remaining operators in stack
            }
            return rpnConverted;
        } catch (EmptyStackException e) {
            throw new InvalidSymbolException("Invalid Expression Syntax");
        }
    }
}
