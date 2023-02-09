import java.util.Scanner;

/**
 * In Main, we get all the input, then distribute it by calculators according to the type of data.
 * We report error messages if input data is invalid, otherwise we output the answers
 */
public class Main {
    /**
     * We create a variable to get input data.
     * Made it final as there is no inheritance with it
     */
    private final Scanner sc = new Scanner(System.in);

    /**
     * In this method we call readCalculator(), readCommandsNumber(), reportFatalError(), parseOperation().
     * We also decide which calculator will perform our operations
     * Then we get arithmetic operation and arguments line by line, execute them and output the answer
     *
     * @param args contains the command-line arguments passed to the Java program upon invocation
     */
    public static void main(String[] args) {
        Main main = new Main();
        CalculatorType typeOfCalculator = main.readCalculator();
        Calculator pointerToCalculator = null;
        switch (typeOfCalculator) {
            case INTEGER:
                pointerToCalculator = new IntegerCalculator();
                break;
            case DOUBLE:
                pointerToCalculator = new DoubleCalculator();
                break;
            case STRING:
                pointerToCalculator = new StringCalculator();
                break;
            default:
                main.reportFatalError("Wrong calculator type");
                break;
        }
        int n = main.readCommandsNumber();
        for (int i = 0; i < n; i++) {
            String firstPart = main.sc.next();
            OperationType typeOfOperator = main.parseOperation(firstPart);
            String secondPart = main.sc.next();
            String thirdPart = main.sc.next();
            String answer;
            switch (typeOfOperator) {
                case ADDITION:
                    answer = pointerToCalculator.add(secondPart, thirdPart);
                    break;
                case SUBTRACTION:
                    answer = pointerToCalculator.subtract(secondPart, thirdPart);
                    break;
                case MULTIPLICATION:
                    answer = pointerToCalculator.multiply(secondPart, thirdPart);
                    break;
                case DIVISION:
                    answer = pointerToCalculator.divide(secondPart, thirdPart);
                    break;
                default:
                    answer = "Wrong operation type";
                    break;
            }
            System.out.println(answer);
        }
    }

    /**
     * In this method we get the input type of calculator, then switch it on CalculatorType enum.
     *
     * @return the result of our input data switch on CalculatorType
     */
    private CalculatorType readCalculator() {
        String givenOperator = sc.nextLine();
        CalculatorType givenCalculator;
        switch (givenOperator) {
            case "INTEGER":
                givenCalculator = CalculatorType.INTEGER;
                break;
            case "DOUBLE":
                givenCalculator = CalculatorType.DOUBLE;
                break;
            case "STRING":
                givenCalculator = CalculatorType.STRING;
                break;
            default:
                givenCalculator = CalculatorType.INCORRECT;
                break;
        }
        return givenCalculator;
    }

    /**
     * In readCommandsNUmber() we get the input of lines with operations.
     * In case of invalid input we call reportFatalError with an error message
     *
     * @return is the number of lines with operations and arguments
     */
    private int readCommandsNumber() {
        String amountOfCommands = sc.nextLine();
        if (amountOfCommands.length() > 2) {
            reportFatalError("Amount of commands is Not a Number");
        }
        for (int i = 0; i < amountOfCommands.length(); i++) {
            if (!Character.isDigit(amountOfCommands.charAt(i))) {
                reportFatalError("Amount of commands is Not a Number");
            }
        }
        return Integer.parseInt(amountOfCommands);
    }

    /**
     * This method outputs the reason of a fatal error and stops the program.
     *
     * @param error is a string with the fatal error message
     */
    private void reportFatalError(String error) {
        System.out.println(error);
        System.exit(0);
    }

    /**
     * In this method we get a string with the operator and switch it on OperationType enum.
     *
     * @param a is a string with the operator
     * @return is our operation switch on the enum
     */
    private OperationType parseOperation(String a) {
        OperationType operator;
        switch (a) {
            case "+":
                operator = OperationType.ADDITION;
                break;
            case "-":
                operator = OperationType.SUBTRACTION;
                break;
            case "*":
                operator = OperationType.MULTIPLICATION;
                break;
            case "/":
                operator = OperationType.DIVISION;
                break;
            default:
                operator = OperationType.INCORRECT;
                break;
        }
        return operator;
    }
}

/**
 * CalculatorType enum contains prohibited types of data to do operations with and case when data is invalid.
 */
enum CalculatorType {
    /**
     * INTEGER means that all operations in out calculator will be produced only with inputs of integer type.
     */
    INTEGER,
    /**
     * DOUBLE means that all operations in our calculator will be done only with inputs of floating-point type.
     */
    DOUBLE,
    /**
     * STRING means that all operations in our calculator will be produced only with strings.
     */
    STRING,
    /**
     * INCORRECT is made to mark that the input data is invalid and doesn't suit the input terms.
     */
    INCORRECT
}

/**
 * OperationType enum contains types of operations with given data and case for invalid inputs.
 */
enum OperationType {
    /**
     * ADDITION means operation '+' with input values.
     */
    ADDITION,
    /**
     * SUBTRACTION means operation '-' with input values.
     */
    SUBTRACTION,
    /**
     * MULTIPLICATION means operation '*' with input values.
     */
    MULTIPLICATION,
    /**
     * DIVISION means operation '/' with input values.
     */
    DIVISION,
    /**
     * INCORRECT means that input data is invalid.
     */
    INCORRECT
}

/**
 * Calculator abstract class contains methods for arithmetical operations.
 * Among the operations are addition, subtraction, multiplication and division
 * It is a parent to IntegerCalculator, DoubleCalculator and StringCalculator
 */
abstract class Calculator {
    /**
     * Abstract method for addition.
     *
     * @param a is a string with the first addend
     * @param b is a string with the second addend
     * @return is a string containing the result of the summation
     */
    public abstract String add(String a, String b);

    /**
     * Abstract method for subtraction.
     *
     * @param a is a string with the minuend
     * @param b is a string with the subtrahend
     * @return is a string containing the result of the subtraction
     */
    public abstract String subtract(String a, String b);

    /**
     * Abstract method for multiplication.
     *
     * @param a is a string with the first factor
     * @param b is a string with the second factor
     * @return is a string containing the result of multiplication
     */
    public abstract String multiply(String a, String b);

    /**
     * Abstract method for division.
     *
     * @param a is a string with the divisor
     * @param b is a string with the dividend
     * @return is a string containing the result of the division
     */
    public abstract String divide(String a, String b);
}

/**
 * Calculator for integer values.
 * It is a child of abstract class Calculator and performs addition, subtraction, multiplication, division
 * The IntegerCalculator can return an error message in cases of wrong input data and division by zero
 */
class IntegerCalculator extends Calculator {
    /**
     * Addition for two integers.
     * It also catches invalid inputs as addends and return a string with an error
     *
     * @param a is a string with the first addend
     * @param b is a string with the second addend
     * @return is a string containing the result of addition or an error message in case of invalid inputs
     */
    @Override
    public String add(String a, String b) {
        try {
            return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }

    /**
     * Subtraction for two integers.
     * It also catches invalid inputs as arguments and returns a string with an error
     *
     * @param a is a string with the minuend
     * @param b is a string with the subtrahend
     * @return is a string with the result of subtraction or an error message in case of invalid inputs
     */
    @Override
    public String subtract(String a, String b) {
        try {
            return String.valueOf(Integer.parseInt(a) - Integer.parseInt(b));
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }

    /**
     * Multiplication for two integers.
     * It also catches invalid inputs as arguments and returns a string with an error message
     *
     * @param a is a string with the first factor
     * @param b is a string with the second factor
     * @return is a string containing the result of multiplication or an error message in case of invalid inputs
     */
    @Override
    public String multiply(String a, String b) {
        try {
            return String.valueOf(Integer.parseInt(a) * Integer.parseInt(b));
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }

    /**
     * Division for two integers.
     * In case of division by zero it reports an error message
     * It also catches invalid inputs as arguments and returns a string with an error message
     *
     * @param a is a string with the divisor
     * @param b is a string with the dividend
     * @return is a string with the result of division or an error message because of invalid inputs, division by zero
     */
    @Override
    public String divide(String a, String b) {
        try {
            if (Integer.parseInt(b) == 0) {
                return "Division by zero";
            } else {
                return String.valueOf(Integer.parseInt(a) / Integer.parseInt(b));
            }
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }
}

/**
 * Calculator for double values.
 * It is a child of abstract class Calculator and performs addition, subtraction, multiplication, division
 * The DoubleCalculator can return an error message string because of invalid inputs and division by zero
 */
class DoubleCalculator extends Calculator {
    /**
     * Addition for two arguments of double type.
     * It also catches invalid inputs as addends and returns a string with an error
     *
     * @param a is a string with the first addend
     * @param b is a string with the second addend
     * @return is a string containing the result of addition or an error message in case of invalid inputs
     */
    @Override
    public String add(String a, String b) {
        try {
            return String.valueOf(Double.parseDouble(a) + Double.parseDouble(b));
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }

    /**
     * Subtraction for two arguments of double type.
     * It also catches invalid inputs as arguments and returns a string with an error
     *
     * @param a is a string with the minuend
     * @param b is a string with the subtrahend
     * @return is a string containing the result of subtraction or an error message in case of invalid inputs
     */
    @Override
    public String subtract(String a, String b) {
        try {
            return String.valueOf(Double.parseDouble(a) - Double.parseDouble(b));
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }

    /**
     * Multiplication for two arguments of double type.
     * It also catches invalid inputs as arguments and returns a string with an error
     *
     * @param a is a string with the first factor
     * @param b is a string with the second factor
     * @return is a string containing the result of multiplication or an error message in case of invalid inputs
     */
    @Override
    public String multiply(String a, String b) {
        try {
            return String.valueOf(Double.parseDouble(a) * Double.parseDouble(b));
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }

    /**
     * Division for two arguments of double type.
     * In case of division by zero it reports an error message
     * It also catches invalid inputs as arguments and returns a string with an error message
     *
     * @param a is a string with the divisor
     * @param b is a string with the dividend
     * @return is a string with the result of division or an error message because of invalid inputs, division by zero
     */
    @Override
    public String divide(String a, String b) {
        try {
            if (Double.parseDouble(b) == 0.0) {
                return "Division by zero";
            } else {
                return String.valueOf(Double.parseDouble(a) / Double.parseDouble(b));
            }
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }
}

/**
 * Calculator for string arguments.
 * It is a child of abstract class Calculator and performs addition, subtraction, multiplication, division
 * StringCalculator returns special messages while subtraction and division, the operations are impossible with strings
 */
class StringCalculator extends Calculator {
    /**
     * Addition of string arguments.
     *
     * @param a is a string with the first addend
     * @param b is a string with the second addend
     * @return is a string containing the concatenation of two strings
     */
    @Override
    public String add(String a, String b) {
        return a + b;
    }

    /**
     * Subtraction of strings.
     *
     * @param a is a string with the minuend
     * @param b is a string with the subtrahend
     * @return is the warning message, because subtraction of strings is impossible
     */
    @Override
    public String subtract(String a, String b) {
        return "Unsupported operation for strings";
    }

    /**
     * Multiplication of strings.
     *
     * @param a is a string with the first factor
     * @param b is a string with the second factor(the amount of repetitions of a)
     * @return is a string containing the result of b times concatenation of a
     */
    @Override
    public String multiply(String a, String b) {
        String c = a;
        try {
            int n = Integer.parseInt(b);
            if (n < 0) {
                return "Wrong argument type";
            }
            for (int i = 0; i < n - 1; i++) {
                c = c + a;
            }
            return c;
        } catch (NumberFormatException e) {
            return "Wrong argument type";
        }
    }

    /**
     * Division for strings.
     *
     * @param a is a string with the divisor
     * @param b is a string with the dividend
     * @return is a string containing a warning message, because division of strings is impossible
     */
    @Override
    public String divide(String a, String b) {
        return "Unsupported operation for strings";
    }
}
