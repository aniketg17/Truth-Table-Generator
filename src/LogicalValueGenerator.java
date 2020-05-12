import java.util.*;

public class LogicalValueGenerator {
    private static TreeMap<Character, Boolean> variables = new TreeMap<>(); // use to maintain the total number
    // of variables in expression

    public static void main(String[] args) throws InvalidSymbolException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the logical expression:");
        String expression = sc.nextLine();
        String cleanExpression = expression.replaceAll("\\s", "");
        cleanExpression = cleanExpression.toLowerCase();
        Queue<Object> rpnQueue = postfixConversion(cleanExpression);
       /* while (!rpnQueue.isEmpty()) {
            System.out.print(rpnQueue.remove());
        }*/

        String[][] binaryGrid = fillGrid();

        Queue<Object> queueClone;
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
            System.out.println(evaluateExpression(queueClone, variables));
        }

       /*HashMap<Character, Boolean> s = new HashMap<>();
       s.put('p', false);
       s.put('q', false);
       s.put('r', true);
       s.put('s', true);*/


        //  System.out.println(evaluateExpression(rpnQueue, s));

      /*  Node root = convertToTree(rpnQueue);
        evaluateTraversal(root);*/
    }

//    P ∧ q ∧ q → R

    public static String[][] fillGrid() {
        String[][] grid = new String[(int) Math.pow(2, variables.size())][variables.size()];

        for (int i = 0; i < grid.length; i++) {
            String row = convertBinary(i);
            for (int j = 0; j < row.length(); j++) {
                String truthValue;
                if (row.charAt(row.length() - j - 1) == 0) {
                    truthValue = "False";
                } else {
                    truthValue = "True";
                }
                grid[i][row.length() - j - 1] = truthValue;
            }
        }
        return grid;
    }

    public static String convertBinary(int num) {
        StringBuilder convertedBinary = new StringBuilder();
        while (num > 0) {
            int rem = num % 2;
            num = num / 2;
            convertedBinary.insert(0, rem);
        }

        if (convertedBinary.length() < variables.size()) {
            while (convertedBinary.length() < variables.size()) {
                convertedBinary.insert(0, "0");
            }
        }

        return convertedBinary.toString();
    }


    public static boolean evaluateExpression(Queue<Object> rpnExpression, TreeMap<Character, Boolean> values) throws InvalidSymbolException {
        Stack<Boolean> variables = new Stack<>();

        while (!rpnExpression.isEmpty()) {
            Object removedItem = rpnExpression.remove();
            if (Character.isLetter((Character) removedItem)) {
                variables.add(values.get(removedItem));
            } else {
                LogicalSymbolTypeEnum parsedType = LogicalSymbolParser.getParsedSymbol((Character) removedItem);
                LogicalSymbol currentSymbol = new LogicalSymbol(parsedType);
                if (currentSymbol.getType() != LogicalSymbolTypeEnum.NEGATION) {
                    boolean operandOne = variables.pop();
                    boolean operandTwo = variables.pop();
                    variables.add(currentSymbol.applyOperation(operandOne, operandTwo));
                } else {
                    boolean operandOne = variables.pop();
                    variables.add(currentSymbol.applyOperation(operandOne, false));
                }
            }
        }
        return variables.pop();
    }

    public static Queue<Object> postfixConversion(String original) throws InvalidSymbolException {
        Queue<Object> rpnConverted = new LinkedList<>();
        Stack<LogicalSymbol> operators = new Stack<>();

        for (int i = 0; i < original.length(); i++) {
            char currentChar = original.charAt(i);
            if (Character.isLetter(original.charAt(i))) {
                rpnConverted.add(currentChar);
                variables.put(currentChar, false);
            } else if (currentChar == ')' || currentChar == ']' || currentChar == '}') {
                while (!operators.isEmpty() && operators.peek().getType() != LogicalSymbolTypeEnum.BRACKET) {
                    rpnConverted.add(operators.pop().getSymbol());
                }
                operators.pop();
            } else {
                LogicalSymbolTypeEnum parsedType = LogicalSymbolParser.getParsedSymbol(currentChar);
                LogicalSymbol currentSymbol = new LogicalSymbol(parsedType);
                if (currentSymbol.getType() == LogicalSymbolTypeEnum.BRACKET) {
                    operators.push(currentSymbol);
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
    }

    static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    /*public static Node convertToTree(Queue<Object> rpnExpression) throws InvalidSymbolException {
        Stack<Object> operands = new Stack<>();

        while (!rpnExpression.isEmpty()) {
            Object currElement = rpnExpression.remove();

            if (Character.isLetter((Character) currElement)) {
                operands.push(currElement);
            } else {
                LogicalSymbolTypeEnum parsedType = LogicalSymbolParser.getParsedSymbol((Character) currElement);
                LogicalSymbol currentSymbol = new LogicalSymbol(parsedType);

                Node tree = new Node<>(currentSymbol);
                if ((currentSymbol).getType() == LogicalSymbolTypeEnum.NEGATION) {
                    tree.left = new Node<>(operands.pop());
                    tree.right = null;
                } else {
                    tree.left = new Node<>(operands.pop());
                    tree.right = new Node<>(operands.pop());
                }
                operands.push(tree);
            }

        }

        assert operands.size() == 1; // size should be 1 because the only element in stack should be the root node
        return (Node) operands.pop();
    }

    public static void evaluateTraversal(Node root) {
        if (root == null) return;

        if (root.value instanceof LogicalSymbol) {
            System.out.println(((LogicalSymbol) root.value).getSymbol());
            evaluateTraversal(root.left);
            evaluateTraversal(root.right);
        } else if (root.value instanceof Character){
            System.out.println(root.value);
            evaluateTraversal(root.left);
            evaluateTraversal(root.right);
        } else if (root.value instanceof Node) {
            Node n = (Node) root.value;
            LogicalSymbol s = (LogicalSymbol) n.value;
            System.out.println((s.getSymbol()));
            evaluateTraversal(n.left);
            evaluateTraversal(n.right);
        }


    }*/

}
