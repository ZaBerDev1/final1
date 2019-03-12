package edu.kit.informatik.tester;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class InputHandler {
    private Scanner scannerFile;
    private int currentLine = 0;

    /**
     * sets although the scanner object
     * 
     * @param path path of the file
     * @throws FileNotFoundException if the path was wrong
     */
    InputHandler(String path) throws FileNotFoundException {
        setScannerFile(path);
    }

    /**
     * tests if the path is valid the file should contain
     * 
     * @param path the path to the txt document
     * @throws FileNotFoundException if the path is wrong
     */
    void setScannerFile(String path) throws FileNotFoundException {
        File file = new File(path);
        this.scannerFile = new Scanner(file);
    }

    /**
     * returns the next line of the file
     * 
     * @return the line of the file
     */
    String getNextLine() {
        currentLine++;
        if (scannerFile.hasNext())
            return scannerFile.nextLine();
        else
            return "";
    }

    /**
     * checks what type of line the given line is
     * @param line the line which should be checked by the method
     * @return 0: empty, 1: input, 2: printLine output, 3: printError output
     */
    byte checkLineInput(String line) {
        final byte empty = 0;
        final byte inputValue = 1;
        final byte outputLineValue = 2;
        final byte outputErrorValue = 3;
        if (line.matches("^> .*$")) {
            //input
            return inputValue;
        } else if (line.matches("^Error, .*$")) {
            //output printError
            return outputErrorValue;
        } else if (line.equals("")) {
            //empty
            return empty;
        } else {
            //ouput printLine
            return outputLineValue;
        }
    }

    /**
     * getter for the currentLine attribute
     * 
     * @return returns the current line of the reading
     */
    int getCurrentLine() {
        return currentLine;
    }
}