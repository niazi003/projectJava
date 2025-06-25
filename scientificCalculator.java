package projectSCD;

import java.util.Scanner;

public class scientificCalculator {

    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static double divide(double a, double b) {
        if (b == 0) {
            System.out.println("Error: Cannot divide by zero.");
            return 0;
        }
        return a / b;
    }

    public static double squareRoot(double a) {
        return Math.sqrt(a);
    }

    public static double power(double a, double b) {
        return Math.pow(a, b);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        double num1, num2;

        do {
            System.out.println("\n--- Scientific Calculator ---");
            System.out.println("1. Add\n2. Subtract\n3. Multiply\n4. Divide");
            System.out.println("5. Square Root\n6. Power");
            System.out.println("0. Exit");
            System.out.print("Choose an operation: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter two numbers: ");
                    num1 = scanner.nextDouble();
                    num2 = scanner.nextDouble();
                    System.out.println("Result: " + add(num1, num2));
                    break;
                case 2:
                    System.out.print("Enter two numbers: ");
                    num1 = scanner.nextDouble();
                    num2 = scanner.nextDouble();
                    System.out.println("Result: " + subtract(num1, num2));
                    break;
                case 3:
                    System.out.print("Enter two numbers: ");
                    num1 = scanner.nextDouble();
                    num2 = scanner.nextDouble();
                    System.out.println("Result: " + multiply(num1, num2));
                    break;
                case 4:
                    System.out.print("Enter two numbers: ");
                    num1 = scanner.nextDouble();
                    num2 = scanner.nextDouble();
                    System.out.println("Result: " + divide(num1, num2));
                    break;
                case 5:
                    System.out.print("Enter a number: ");
                    num1 = scanner.nextDouble();
                    System.out.println("Square Root: " + squareRoot(num1));
                    break;
                case 6:
                    System.out.print("Enter base and exponent: ");
                    num1 = scanner.nextDouble();
                    num2 = scanner.nextDouble();
                    System.out.println("Result: " + power(num1, num2));
                    break;
                case 0:
                    System.out.println("Exiting calculator. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }
}
