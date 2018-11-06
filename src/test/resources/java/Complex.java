import java.util.*;
import java.io.*;
import java.text.*;
import java.util.regex.*;
public class Complex{

    private double real;
    private double imaginary;

    public static void main(String[] args) {

        ArrayList<Equation> memory = new ArrayList<Equation>();
        String line = "";
        String[] lines;
        while (!line.toUpperCase().equals("QUIT")) {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            lines = scanner.nextLine().split("=");
            if (lines.length < 3) {

                line = lines[lines.length - 1];

                for (int i = 0; i < memory.size(); i++) {

                    lines[lines.length-1] = lines[lines.length-1].replaceAll(memory.get(i).getVariable(),memory.get(i).getValue().toString());

                }

                try {

                    if (!line.toUpperCase().equals("QUIT") && !line.toUpperCase().equals("VARIABLES") && !line.toUpperCase().equals("HELP")) {

                        line = lines[lines.length - 1];

                        if (!lines[0].equals(line)) {

                            Pattern p = Pattern.compile("\\s*[A-Za-z]+\\s*");
                            Matcher m = p.matcher(lines[0]);
                            if (m.matches()) {
                                lines[0] = lines[0].replaceAll("\\s","");
                                Equation e = new Equation(lines[0],new Complex(lines[1]));
                                for (int i = 0; i < memory.size(); i++) {

                                    if (memory.get(i).getVariable().equals(lines[0])) {

                                        memory.remove(i);

                                    }

                                }
                                memory.add(e);
                            } else {

                                System.out.println("Syntax error");
                                continue;

                            }


                        }

                        System.out.println(new Complex(line));

                    } else if (line.toUpperCase().equals("VARIABLES")) {

                        System.out.println("Variable, Value");
                        System.out.println("---------------");
                        for (int i = 0; i < memory.size(); i++) {
                            System.out.println(memory.get(i).getVariable()+", "+memory.get(i).getValue());
                        }

                    } else if (line.toUpperCase().equals("HELP")) {

                        System.out.println("commands:");
                        System.out.println("\tHelp - Show this menu");
                        System.out.println("\tVariables - Show all variables and their values");
                        System.out.println("\tQuit - Exit the program");

                    }

                } catch (Exception e) {

                    System.out.println("Syntax error");

                }

            } else {

                System.out.println("Syntax error");

            }
        }

    }

    Complex(double _real, double _imaginary) {

        real = _real;
        imaginary = _imaginary;

    }

    Complex(String eval) {

        String ev = eval.replaceAll("cos","c")
                .replaceAll("sin","s")
                .replaceAll("\\t"," ")
                .replaceAll("^\\s+","")
                .replaceAll("\\s+$","");
        ev = ev.replaceAll("^\\([\\+\\-]?[0-9ir]+[ir]?\\)$",ev.replaceAll("[\\(\\)]",""))
                .replaceAll("^\\s*\\-","0 - ")
                .replaceAll("^\\s*\\+","");
        ev = ev.replaceAll("^[0-9ir]+(\\.[0-9]+)?[ir]?$","0 + "+eval);
        String[] firstInput = ev.split("");
        ArrayList<String> inputArrayList = new ArrayList<String>();
        String buffer = "";
        Pattern p = Pattern.compile("^[0-9ir\\.]$");
        for (int i = 0; i < firstInput.length; i++) {

            if (p.matcher(firstInput[i]).matches()) {

                buffer += firstInput[i];

            } else if (!firstInput[i].equals(" ")) {

                if (!buffer.equals("")) {

                    inputArrayList.add(buffer);
                    buffer = "";

                }

                inputArrayList.add(firstInput[i]);

            }

        }

        if (!buffer.equals("")) {

            inputArrayList.add(buffer);

        }

        String[] input = new String[inputArrayList.size()];

        for (int i = 0; i < inputArrayList.size(); i++) {

            input[i] = inputArrayList.get(i);

        }

        for (int i = 0; i < input.length; i++) {

            if (input[i].matches("^[0-9.]+$")) {

                input[i] += "r";

            }

            if (input[i].matches("^i$")) {

                input[i] = "1i";

            }

        }


        String[] output = infixToRPN(input);

        Stack<String> stack = new Stack<String>();

        for (String token : output) {

            stack.push(token);

        }

        Collections.reverse(stack);

        Stack<String> outputBuffer = new Stack<String>();

        while (stack.size() > 0) {

            String temp = stack.pop();
            if (isOperator(temp)) {

                Double doubleValue;
                Complex value1, value2 = new Complex(0,0);
                String value = outputBuffer.pop();
                String[] split;
                if (value.matches("^.+\\,.+$")) {

                    split = value.split(",");
                    value1 = new Complex(Double.parseDouble(split[0]),Double.parseDouble(split[1]));

                } else {

                    doubleValue = Double.parseDouble(value.substring(0, value.length()-1));
                    value1 = (value.matches("^[0-9.]+r$") ? new Complex(doubleValue, 0):new Complex(0, doubleValue));

                }

                if (!temp.equals("c") && !temp.equals("s")) {

                    value = outputBuffer.pop();

                    if (value.matches("^.+\\,.+$")) {

                        split = value.split(",");
                        value2 = new Complex(Double.parseDouble(split[0]),Double.parseDouble(split[1]));

                    } else {

                        doubleValue = Double.parseDouble(value.substring(0, value.length()-1));
                        value2 = (value.matches("^[0-9.]+r$") ? new Complex(doubleValue, 0):new Complex(0, doubleValue));

                    }

                }


                Complex result = new Complex(0,0);

//                switch (temp) {
//
//                    case "+":
//
//                        result = value2.add(value1);
//
//                        break;
//                    case "-":
//
//                        result = value2.subtract(value1);
//
//                        break;
//                    case "*":
//
//                        result = value2.multiply(value1);
//
//                        break;
//
//                    case "/":
//
//                        result = value2.divide(value1);
//
//                        break;
//
//                    case "^":
//
//                        result = value2.pow(value1);
//
//                        break;
//
//                    case "c":
//
//                        result = value1.cos();
//
//                        break;
//
//                    case "s":
//
//                        result = value1.sin();
//
//                        break;
//
//                }

                outputBuffer.add(result.getReal()+","+result.getImaginary());



            } else {

                outputBuffer.add(temp);

            }

        }

        Collections.reverse(outputBuffer);

        String result = "";

        while (outputBuffer.size() > 0) {

            result = outputBuffer.pop();

        }

        real = Double.parseDouble(result.split(",")[0]);
        imaginary = Double.parseDouble(result.split(",")[1]);

    }


    public Complex add(Complex arg) {

        return new Complex(real + arg.getReal(), imaginary+arg.getImaginary());

    }

    public Complex subtract(Complex arg) {

        return new Complex(real - arg.getReal(), imaginary - arg.getImaginary());

    }

    public Complex multiply(Complex arg) {

        return new Complex((real * arg.getReal()) - (imaginary * arg.getImaginary()),
                (real * arg.getImaginary()) + (imaginary * arg.getReal()));

    }

    public Complex divide(Complex arg) {

        Complex numerator = new Complex(real, imaginary);
        Complex conjugate = new Complex(arg.getReal(), -arg.getImaginary());
        Complex denominator = arg;
        numerator = numerator.multiply(conjugate);
        denominator = denominator.multiply(conjugate);
        return new Complex(
                numerator.getReal() / denominator.getReal(),
                numerator.getImaginary() / denominator.getReal()
        );

    }

    public Complex cos() {

        return new Complex(
                Math.cos(real) * Math.cosh(imaginary),
                -Math.sin(real) * Math.sinh(imaginary)
        );

    }

    public Complex sin() {

        return new Complex(
                Math.sin(real) * Math.cosh(imaginary),
                Math.cos(real) * Math.sinh(imaginary)
        );

    }

    public static Complex cos(Complex arg) {

        return arg.cos();

    }

    public static Complex sin(Complex arg) {

        return arg.sin();

    }

    public Complex exp() {

        return new Complex(Math.cos(imaginary), Math.sin(imaginary)).multiply(new Complex(Math.pow(Math.E,real),0));

    }

    public double arg() {

        if (real < 0) {

            return Math.PI - Math.atan(imaginary/Math.abs(real));

        } else {

            return Math.atan(imaginary/real);

        }

    }

    public double getReal() {

        return real;

    }

    public double getImaginary() {

        return imaginary;

    }

    public void setReal(double value) {

        real = value;

    }

    public void setImaginary(double value) {

        imaginary = value;

    }

    private Complex pow(Double a, Complex i) {

        Double b = i.getReal();
        Double c = i.getImaginary();
        return new Complex(b*Math.log(a), c*Math.log(a)).exp();

    }

    public Complex pow(Complex arg) {

        double a = real;
        double b = imaginary;
        double c = arg.getReal();
        double d = arg.getImaginary();
        Complex result = pow((Math.pow(a,2)+Math.pow(b,2)), new Complex(c/2,d/2)).multiply(
                new Complex(-d,c).multiply(new Complex(new Complex(a,b).arg(),0) ).exp());

        return new Complex(result.getReal(), result.getImaginary());

    }

    public String toString() {

        DecimalFormat df = new DecimalFormat("#.#############################################");

        if (real != 0 && imaginary != 0) {
            return df.format(real) + (imaginary < 0 ? " - ":" + ") + Math.abs(imaginary) + "i";
        } else if (imaginary != 0) {
            return imaginary+"i";
        } else {
            return df.format(real);
        }

    }

    // Associativity constants for operators
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;

    // Supported operators
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
    static {
        // Map<"token", []{precendence, associativity}>
        OPERATORS.put("+", new int[] { 0, LEFT_ASSOC });
        OPERATORS.put("-", new int[] { 0, LEFT_ASSOC });
        OPERATORS.put("*", new int[] { 5, LEFT_ASSOC });
        OPERATORS.put("/", new int[] { 5, LEFT_ASSOC });
        OPERATORS.put("^", new int[] { 10, RIGHT_ASSOC });
        OPERATORS.put("c", new int[] { 10, RIGHT_ASSOC });
        OPERATORS.put("s", new int[] { 10, RIGHT_ASSOC });
    }

    /**
     * Test if a certain is an operator .
     * @param token The token to be tested .
     * @return True if token is an operator . Otherwise False .
     */
    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    /**
     * Test the associativity of a certain operator token .
     * @param token The token to be tested (needs to operator).
     * @param type LEFT_ASSOC or RIGHT_ASSOC
     * @return True if the tokenType equals the input parameter type .
     */
    private static boolean isAssociative(String token, int type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        if (OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    /**
     * Compare precendece of two operators.
     * @param token1 The first operator .
     * @param token2 The second operator .
     * @return A negative number if token1 has a smaller precedence than token2,
     * 0 if the precendences of the two tokens are equal, a positive number
     * otherwise.
     */
    private static final int cmpPrecedence(String token1, String token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalied tokens: " + token1
                    + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }

    private static String[] infixToRPN(String[] inputTokens) {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        // For all the input tokens [S1] read the next token [S2]
        for (String token : inputTokens) {
            if (isOperator(token)) {
                // If token is an operator (x) [S3]
                while (!stack.empty() && isOperator(stack.peek())) {
                    // [S4]
                    if ((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(
                            token, stack.peek()) <= 0)
                            || (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(
                            token, stack.peek()) < 0)) {
                        out.add(stack.pop()); 	// [S5] [S6]
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack [S7]
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token); 	// [S8]
            } else if (token.equals(")")) {
                // [S9]
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop()); // [S10]
                }
                stack.pop(); // [S11]
            } else {
                out.add(token); // [S12]
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop()); // [S13]
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

}

class Equation{

    private String variable;
    private Complex value;
    Equation(String _variable, Complex _value) {
        variable = _variable;
        value = _value;
    }
    public String getVariable() {

        return variable;

    }
    public Complex getValue() {

        return value;

    }

}