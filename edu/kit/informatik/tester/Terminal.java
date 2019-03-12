package edu.kit.informatik.tester;

import java.util.Scanner;
import java.io.*;

/**
 * a test class that replaces the real Terminal class to test the program based
 * on a compare to a txt
 */
public final class Terminal {
    private static InputHandler inputHandler;
    private static TestReport testReport;
    private static boolean enableTestMode = false;
    private static final BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));

    /**
     * enables test mode
     * @param newTestMode sets the test mode variable
     */
    public static void setTestMode(boolean newTestMode) {
        enableTestMode = newTestMode;
    }

    /**
     * lets the user set the path creates although a testReport object
     */
    public static void inputPath() {
        boolean validPath = false;
        do {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please insert the path of your test document.");
                String path = scanner.nextLine();
                scanner.close();
                if (path.matches("^default .*$")) {
                    // default file or path
                    inputDefaultPath(path);
                } else {
                    // normal path input
                    inputWholePath(path);
                }
                validPath = true;
            } catch (FileNotFoundException e) {
                System.out.println("This is not a valid path.");
            }
        } while (!validPath);
    }

    /**
     * no input throw the console
     * 
     * @param path the path to the test txt file
     */
    public static void inputPath(String path) {
        try {
            if (path.matches("^default .*$")) {
                // default file or path
                inputDefaultPath(path);
            } else {
                // normal path input
                inputWholePath(path);
            }
        } catch (FileNotFoundException e) {
            System.out.println("This is not a valid path.");
        }
    }

    /**
     * enables the user to input the whole path at once
     * 
     * @param path the path which has been inputed
     * @return the new inputHandler
     * @throws FileNotFoundException if the path is not valid
     */
    protected static InputHandler inputWholePath(String path) throws FileNotFoundException {
        inputHandler = new InputHandler(path);
        testReport = new TestReport();
        return inputHandler;
    }

    /**
     * enables the user to input a file that is lockated at the default path
     * 
     * @param path the path which has been inputed
     * @return the new inputHandler
     * @throws FileNotFoundException if the path is not valid
     */
    protected static InputHandler inputDefaultPath(String path) throws FileNotFoundException {
        String nPath = "C:\\Users\\User\\Desktop\\" + path.substring(8);
        inputHandler = new InputHandler(nPath);
        testReport = new TestReport();
        return inputHandler;
    }

    /**
     * compares to the output line if it is no Error message. There is only one
     * input line in a row posible
     * 
     * @param message the message which should be printed out
     */
    public static void printLine(String message) {
        if (enableTestMode) {
            String line = "";
            String[] splitMessage = message.split("\n");
            String output = "";
            for (int i = 0; i < splitMessage.length; i++) {
                line = inputHandler.getNextLine();
                if (line.equals(splitMessage[i])) {
                    testReport.addSuccess();
                    output = splitMessage[i];
                } else {
                    output = "Expected output: " + line + " | Actual output: " + splitMessage[i];
                    testReport.addError(inputHandler.getCurrentLine(), output);
                }
                System.out.println(output);
            }
        } else {
            System.out.println(message);
        }

    }

    /**
     * compares to the output line if it is a Error message. There is only one input
     * line in a row posible
     * 
     * @param errorMessage the message which should be printed out
     */
    public static void printError(String errorMessage) {
        if (enableTestMode) {
            String line = "";
            String[] splitMessage = errorMessage.split("\n");
            String output = "";
            for (int i = 0; i < splitMessage.length; i++) {
                line = inputHandler.getNextLine();
                String add = "";
                if (i == 0) {
                    add = "Error, ";
                }
                if (line.equals(add + splitMessage[i])) {
                    testReport.addSuccess();
                    output = add + splitMessage[i];
                } else {
                    output = "Expected output: " + line + " | Actual output: " + add + splitMessage[i];
                    testReport.addError(inputHandler.getCurrentLine(), output);
                }
                System.out.println(output);
            }
        } else {
            System.out.println("Error, " + errorMessage);
        }
    }

    /**
     * simulates the input
     * 
     * @return the next line of the input
     */
    public static String readLine() {
        if (enableTestMode) {
            String line = inputHandler.getNextLine();
            String input = "";
            if (inputHandler.checkLineInput(line) == 1) {
                input = line.substring(2, line.length());
            } else {
                testReport.addError(inputHandler.getCurrentLine(), input);
            }
            System.out.println(line);
            return input;
        } else {
            try {
                return IN.readLine();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * prints the test report out
     */
    public static void printTestReport() {
        if (enableTestMode) {
            System.out.println(testReport.getSummary());
        }
    }
}