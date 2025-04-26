package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static Map<String, BigInteger> variables = new HashMap<>();
    static String numberRegex = "\\d+";
    static String variableRegex = "[a-zA-Z]+";
    static String operatorRegex = "([-\\+]+)|/|\\*|\\)|\\(";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true){
            String input = scanner.nextLine().trim();
            String assignmentRegex = ".+\\s*=\\s*.+";
            String commandRegex = "/?[a-zA-Z0-9]+";
            String calcRegex = "(([a-zA-Z]+|[0-9]+)(\\s*[-+*/]+\\s*([a-zA-z]+|[0-9])+)+)|(.*\\(.*)|(.*\\).*)";

            if(input.matches(assignmentRegex)){
                tryAssignment(input);
            }else if(input.matches(commandRegex)){
                if(input.equals("/exit")){
                    System.out.println("Bye!");
                    break;
                }else{
                    tryCommand(input);
                }
            }else if(input.matches(calcRegex)){
                List<String> postfixNotationList = convert(input);
                if(postfixNotationList.getFirst().equals("error: invalidExpression")){
                    System.out.println("Invalid expression");
                }else{
                    calculate(postfixNotationList);
                }
            }
        }
    }

    public static void tryAssignment(String input) {
        String[] parts = input.split("\\s*=\\s*", 2);
        String key = null;
        BigInteger value = null;

        // check first part of assignment
        if (parts[0].matches("[a-zA-Z]+")) {
            key = parts[0];
        } else {
            System.out.println("Invalid identifier");
            return;
        }

        // check is second part of assignment
        if (parts[1].matches("-?[0-9]+\\b")) {
            value = new BigInteger(parts[1]);
        } else if (parts[1].matches("[a-zA-Z]+")) {
            if (variables.containsKey(parts[1])) {
                value = variables.get(parts[1]);
            } else {
                System.out.println("Unknown variable");
            }
        } else {
            System.out.println("Invalid assignment");
        }
        if (value != null) {
            variables.put(key, value);
        }
    }

    public static void tryCommand(String input) {
        if (input.matches("[a-zA-Z]+")) {
            if (variables.containsKey(input)) {
                System.out.println(variables.get(input));
            } else {
                System.out.println("Unknown variable");
            }
        } else if (input.equals("/help")) {
            System.out.println("The program calculates the sum of numbers");
        } else if (input.matches("/\\w*")) {
            System.out.println("Unknown command");
        } else {
            System.out.println("Invalid identifier");
        }
    }

    public static List<String> convert(String input) {
        // tokenazing string
        String regex = String.format("%s|%s|%s", numberRegex, variableRegex, operatorRegex);
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        List<String> listOfTokens = new ArrayList<>();
        while (m.find()) {
            if(m.group().matches("\\++")){
                listOfTokens.add("+");
            }else if(m.group().matches("^-(--)*$")){
                listOfTokens.add("-");
            }else if(m.group().matches("^(--)+$")){
                listOfTokens.add("+");
            }else{
                listOfTokens.add(m.group());
            }
        }
        if(listOfTokens.getFirst().equals("-")){
            listOfTokens.addFirst("0");
        }

        // implement algorithm for infix to  postfix conversion
        Deque<String> stack = new ArrayDeque<>();
        List<String> postfixNotation = new ArrayList<>();
        for(String token : listOfTokens){
            if (token.matches(numberRegex) || token.matches(variableRegex)){
                postfixNotation.add(token);
            }else if(token.matches(operatorRegex)){
                if(stack.isEmpty() || stack.peekFirst().equals("(")){
                    stack.push(token);
                }else if(token.equals("(")){
                    stack.push(token);
                }else if(token.equals(")")){
                    do {
                        postfixNotation.add(stack.pop());
                    } while (!stack.isEmpty() && !stack.peekFirst().equals("("));
                    if(!stack.isEmpty()){
                        stack.pop();
                    }else{
                        postfixNotation.clear();
                        postfixNotation.add("error: invalidExpression");
                        break;
                    }
                }else if(isHigherPrecedence(token, stack.peekFirst()) == 1){
                    stack.push(token);
                }else if(isHigherPrecedence(token, stack.peekFirst()) == -1 || isHigherPrecedence(token, stack.peekFirst()) == 0 ){
                    do{
                        postfixNotation.add(stack.pop());
                    }
                    while(!stack.isEmpty() && (isHigherPrecedence(token, stack.peekFirst()) != -1 || !stack.peekFirst().equals("(")));
                    stack.push(token);
                }
            }
        }
        while(!stack.isEmpty()){
            if(stack.peekFirst().equals("(") || stack.peekFirst().equals(")")){
                postfixNotation.clear();
                postfixNotation.add("error: invalidExpression");
                break;
            }else{
                postfixNotation.add(stack.pop());
            }
        }
        return postfixNotation;
    }

    public static int isHigherPrecedence(String token, String topOfStack){
        String higherPrecedence = "[*/]";
        String lowerPrecedence = "[-+]";

        if(token.matches(higherPrecedence) && topOfStack.matches(lowerPrecedence)){
            return 1;
        }else if((token.matches(higherPrecedence) && topOfStack.matches(higherPrecedence)) || (token.matches(lowerPrecedence) && topOfStack.matches(lowerPrecedence))){
            return 0;
        }else {
            return -1;
        }
    }

    public static void calculate(List<String> postfixNotationList){
        Deque<BigInteger> stack = new ArrayDeque<>();
        boolean error = false;
        for(String el : postfixNotationList){
            if(el.matches(numberRegex)){
                stack.push(new BigInteger(el));
            }else if(el.matches(variableRegex)){
                BigInteger value = variables.get(el);
                if(value == null){
                    System.out.println("Unknown variable");
                    break;
                }else{
                    stack.push(value);
                }
            }else if(el.matches("[-+*/]")){
               try{
                   BigInteger b = stack.pop();
                   BigInteger a = stack.pop();
                   BigInteger result = switch (el) {
                       case "+" -> a.add(b);
                       case "-" -> a.subtract(b);
                       case "*" -> a.multiply(b);
                       case "/" -> a.divide(b);
                       default -> BigInteger.ZERO;
                   };
                   stack.push(result);
               } catch (Exception e) {
                   System.out.println("Invalid expression");
                   error = true;
                   break;
               }
            }
        }
        if(!error){
            BigInteger finalResult = stack.pop();
            System.out.println(finalResult);
        }

    }
}
