import java.util.*;

public class LogicalValueGenerator {
    private static Set<Character> variables = new HashSet<>(); // use to maintain the total number
    // of variables in expression

    public static void main(String[] args) throws InvalidSymbolException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the logical expression:");
        String expression = sc.nextLine();
        String cleanExpression = expression.replaceAll("\\s", "");
        cleanExpression = cleanExpression.toLowerCase();
        Queue<Object> rpnQueue = postfixConversion(cleanExpression);
        /*while (!rpnQueue.isEmpty()) {
            System.out.print(rpnQueue.remove());
        }*/

        Node root = convertToTree(rpnQueue);

    }

//    P ∧ q ∧ q → R

    public static Queue<Object> postfixConversion(String original) throws InvalidSymbolException {
        Queue<Object> rpnConverted = new LinkedList<>();
        Stack<LogicalSymbol> operators = new Stack<>();

        for (int i = 0; i < original.length(); i++) {
            char currentChar = original.charAt(i);
            if (Character.isLetter(original.charAt(i))) {
                rpnConverted.add(currentChar);
                variables.add(currentChar);
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
        Node left;
        Node right;

        public Node(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    public static Node convertToTree(Queue<Object> rpnExpression) throws InvalidSymbolException {
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

}
