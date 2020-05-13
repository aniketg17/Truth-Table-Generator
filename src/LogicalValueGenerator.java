import java.util.*;

public class LogicalValueGenerator {
    private TreeMap<Character, Boolean> variables = new TreeMap<>(); // use to maintain the total number
                                                                        // of variables in expression
    private String expressionToConvert;
    private String[][] binaryGrid;

    public LogicalValueGenerator(String expressionToConvert) {
        this.expressionToConvert = expressionToConvert;
    }


    public String[][] getBinaryGrid() {
        return binaryGrid;
    }

    public boolean[] truthValuesGenerator() throws InvalidSymbolException {
        if (expressionToConvert == null || expressionToConvert.equals("")) {
            throw new InvalidSymbolException("No expression to parse!");
        }
        Queue<Object> rpnQueue = postfixConversion(expressionToConvert);
        binaryGrid = fillGrid();
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
                variables.replace(character, value);
                iterate++;
            }
            queueClone = new LinkedList<>(rpnQueue);
            truthValues[i] = evaluateExpression(queueClone, variables);
//            System.out.println(i + " " + binaryGrid[i][0] + " " + binaryGrid[i][1] + " " + binaryGrid[i][2] +
//                    " " + binaryGrid[i][3] + " " + binaryGrid[i][4] + " " + binaryGrid[i][5] + " " + evaluateExpression(queueClone, variables));
        }

        return truthValues;
    }

    public TreeMap<Character, Boolean> getVariables() {
        return variables;
    }

    public  String[][] fillGrid() {
        String[][] grid = new String[(int) Math.pow(2, variables.size())][variables.size()];

        for (int i = 0; i < grid.length; i++) {
            String row = convertBinary(i);
            grid[i] = row.split(" ");
        }
        return grid;
    }

    public  String convertBinary(int num) {
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


    public  boolean evaluateExpression(Queue<Object> rpnExpression, TreeMap<Character, Boolean> values) throws InvalidSymbolException {
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

    public  Queue<Object> postfixConversion(String original) throws InvalidSymbolException {
        Queue<Object> rpnConverted = new LinkedList<>();
        Stack<LogicalSymbol> operators = new Stack<>();
        Stack<Character> bracketValidation = new Stack<>();

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
                            bracketValidation.push(')');
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
                rpnConverted.add(operators.pop().getSymbol());
            }
            return rpnConverted;
        } catch (EmptyStackException e) {
            throw new InvalidSymbolException("Invalid Expression Syntax");
        }
    }
}
