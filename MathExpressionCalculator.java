import java.util.*;

public class MathExpressionCalculator {
    static class Node {
        String value;
        Node left, right;

        public Node(String value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    static class ExpressionTree {
        private Node root;
        private Map<String, Double> variables;

        public ExpressionTree() {
            root = null;
            variables = new HashMap<>();
        }

        // Build the tree from a fully parenthesized expression
        public void buildTree(String expression) {
            root = buildTreeHelper(expression);
        }

        private Node buildTreeHelper(String expression) {
            expression = expression.trim();

            // If the expression is a number or variable, return a leaf node
            if (expression.matches("[0-9]+")) {
                return new Node(expression);
            } else if (expression.matches("[a-zA-Z]+[0-9]*")) {
                return new Node(expression);
            }

            // Remove the outermost parentheses
            if (expression.startsWith("(") && expression.endsWith(")")) {
                expression = expression.substring(1, expression.length() - 1);
            }

            // Find the main operator in the expression (it is the operator outside of parentheses)
            int depth = 0;
            int operatorPos = -1;
            for (int i = expression.length() - 1; i >= 0; i--) {
                char c = expression.charAt(i);
                if (c == ')') depth++;
                else if (c == '(') depth--;
                else if (depth == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                    operatorPos = i;
                    break;
                }
            }

            if (operatorPos != -1) {
                String operator = String.valueOf(expression.charAt(operatorPos));
                String leftExpr = expression.substring(0, operatorPos).trim();
                String rightExpr = expression.substring(operatorPos + 1).trim();

                Node node = new Node(operator);
                node.left = buildTreeHelper(leftExpr);
                node.right = buildTreeHelper(rightExpr);
                return node;
            }

            return null;
        }

        // Evaluate the expression tree
        public double evaluate() {
            return evaluateTree(root);
        }

        private double evaluateTree(Node node) {
            if (node == null) {
                return 0;
            }

            // If the node is a number, return its value
            if (node.value.matches("[0-9]+")) {
                return Double.parseDouble(node.value);
            }

            // If the node is a variable, get its value from the variables map
            if (variables.containsKey(node.value)) {
                return variables.get(node.value);
            }

            // If the node is an operator, evaluate the left and right children
            double leftVal = evaluateTree(node.left);
            double rightVal = evaluateTree(node.right);

            switch (node.value) {
                case "+":
                    return leftVal + rightVal;
                case "-":
                    return leftVal - rightVal;
                case "*":
                    return leftVal * rightVal;
                case "/":
                    return leftVal / rightVal;
            }

            return 0;
        }

        // Print the expression tree in Prefix notation
        public void printPrefix() {
            System.out.print("Prefix: ");
            printPrefixHelper(root);
            System.out.println();
        }

        private void printPrefixHelper(Node node) {
            if (node == null) {
                return;
            }
            System.out.print(node.value + " ");
            printPrefixHelper(node.left);
            printPrefixHelper(node.right);
        }

        // Print the expression tree in Postfix notation
        public void printPostfix() {
            System.out.print("Postfix: ");
            printPostfixHelper(root);
            System.out.println();
        }

        private void printPostfixHelper(Node node) {
            if (node == null) {
                return;
            }
            printPostfixHelper(node.left);
            printPostfixHelper(node.right);
            System.out.print(node.value + " ");
        }

        // Print the expression tree in Infix notation
        public void printInfix() {
            System.out.print("Infix: ");
            printInfixHelper(root);
            System.out.println();
        }

        private void printInfixHelper(Node node) {
            if (node == null) {
                return;
            }
            if (node.left != null || node.right != null) {
                System.out.print("(");
            }
            printInfixHelper(node.left);
            System.out.print(node.value + " ");
            printInfixHelper(node.right);
            if (node.left != null || node.right != null) {
                System.out.print(")");
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ExpressionTree tree = new ExpressionTree();

        System.out.print("Enter arithmetic expression: ");
        String expression = input.nextLine();
        tree.buildTree(expression);

        System.out.println("\nExpression tree:");
        tree.printInfix();
        tree.printPrefix();
        tree.printPostfix();
        System.out.println("Expression evaluates to: " + tree.evaluate());

        input.close();
    }
}
