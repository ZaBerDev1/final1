package edu.kit.informatik.tester;

import java.util.ArrayList;

public class TestReport {
    private ArrayList<String> errorList = new ArrayList<String>();
    private int totalSuccesses = 0;

    /**
     * getter for the error count
     * @return the total amount of failed tests
     */
    public int getTotalErrors() {
        return errorList.size();
    }

    /**
     * getter for the success count
     * 
     * @return the total amount of successive tests
     */
    public int getTotalSuccesses() {
        return totalSuccesses;
    }

    /**
     * adds the line to the list of errors
     * @param errorLine the current line of the file
     * @param lineValue the actual value of the error line
     */
    public void addError(int errorLine, String lineValue) {
        errorList.add(errorList.size(), "line " + errorLine + " || " + lineValue);
    }

    /**
     * counts up the total success count
     */
    public void addSuccess() {
        totalSuccesses++;
    }

    /**
     * prints out a summary of the test
     * @return the report as a String
     */
    public String getSummary() {
        String output = "\nAmount of failed tests:     " + errorList.size()
        + "\nAmount of successive tests: " + totalSuccesses
        + "\n                          ________"
        + "\nTotal amount of tests:      " + (totalSuccesses + errorList.size())
        + "\n";
        if (errorList.size() > 0) {
            output += "\nList of fails:";
            for (int i = 0; i < errorList.size(); i++) {
                output += "\n" + errorList.get(i);
            }
        }
        return output;
    }
}