package edu.kit.informatik.ui;

import edu.kit.informatik.ui.commands.*;
import edu.kit.informatik.Constant;
import edu.kit.informatik.exceptions.*;
import edu.kit.informatik.gamecontroll.Game;
import edu.kit.informatik.Terminal;

/**
 * The point were the program starts and ends with the output of a command
 */
class Main {
    /**
     * The main method read the input from the terminal and outputs the processed data
     * @param args Console input
     */
    public static void main(String[] args) {
        SearchCommands search = new SearchCommands();
        final int boardX = 15;
        final int boardY = 11;
        Game game = new Game(boardY, boardX);
        boolean programIsRunning = true;
        do {
            try {
                String line = Terminal.readLine();
                String[] splitedInput = splitInput(line);
                String output = search.call(line, splitedInput[0], splitedInput[1], game);
                if (output.equals("exit")) {
                    programIsRunning = false;
                } else {
                    // filter out previous errors
                    if (output.matches(Constant.getExceptionBeginningRegex())) {
                        output = output.substring(4);
                        Terminal.printError(output);
                    } else {
                        Terminal.printLine(output);
                    }
                }
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while(programIsRunning);
    }

    /**
     * splits the row input into parameters and instruction
     * @param input is the raw input of the terminal
     * @return a String array which contains the instruction in 0 and the parameters 1
     */
    private static String[] splitInput(String input) throws InputException {
        String[] splitedInput = input.split(" ");
        if (!(0 < splitedInput.length && splitedInput.length <= 2)) {
            throw new InputException();
        }
        if (splitedInput.length == 1) {
            String instruction = splitedInput[0];
            splitedInput = new String[2];
            splitedInput[0] = instruction;
            splitedInput[1] = "";
        }
        return splitedInput;
    }
}