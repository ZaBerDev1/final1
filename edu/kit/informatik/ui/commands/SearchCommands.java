package edu.kit.informatik.ui.commands;

import java.util.Arrays;

import edu.kit.informatik.exceptions.InputException;
import edu.kit.informatik.ui.commands.Commands;
import edu.kit.informatik.gamecontroll.Game;

/**
 * searches the right command and triggers it
 */
public class SearchCommands {
    /** array that contains all commands in a sorted order */
    public Commands[] arrCommands = Commands.values();

    /**
     * constructor that sorts the commands in the command array
     */
    public SearchCommands() {
        sort();
    }

    /**
     * constructs a String that contains a list of all command idWords. This method
     * is only for tests.
     * 
     * @return the list of commands
     */
    public String printCommandArray() {
        String output = "";
        for (int i = 0; i < arrCommands.length; i++) {
            output += arrCommands[i].getIdWord() + "\n";
        }
        return output;
    }

    /**
     * sorts the arrCommands array for the idWord
     */
    public void sort() {
        String[] idCommands = new String[arrCommands.length];
        for (int i = 0; i < idCommands.length; i++) {
            idCommands[i] = arrCommands[i].getIdWord();
        }
        Arrays.sort(idCommands);
        Commands[] reference = duplicate(arrCommands);
        for (int i = 0; i < idCommands.length; i++) {
            // searches for the equality to the idWord of the idCommands array in the
            // arrCommands array
            for (int counter = 0; counter < arrCommands.length; counter++) {
                if (arrCommands[counter].getIdWord().equals(idCommands[i])) {
                    reference[i] = arrCommands[counter];
                    break;
                }
            }
        }
        arrCommands = duplicate(reference);
    }

    /**
     * duplicates the arrCommand
     * 
     * @return returns the new mirrored array
     */
    private static Commands[] duplicate(Commands[] orignal) {
        Commands[] reference = new Commands[orignal.length];
        for (int i = 0; i < reference.length; i++) {
            reference[i] = orignal[i];
        }
        return reference;
    }

    /**
     * finds the suitable commands
     * 
     * @param searchedIdWord thd idWord of the searched commands
     * @return the position of the command in the command array
     */
    private int find(String searchedIdWord) throws InputException {

        int lowerBorder = 0;
        int higherBorder = arrCommands.length;
        int half = 0;
        int halfOld = 0; //to identify when there is the same cycle
        int delta = 0;
        do {
            half = (lowerBorder +  higherBorder) / 2;
            if (half == halfOld) {
                throw new InputException();
            }
            delta = searchedIdWord.compareTo(arrCommands[half].getIdWord());
            if (delta < 0) {
                //searchedIdWord is before the idWord of half in the alphabet
                higherBorder = half;
            } else {
                // searchedIdWord is after the idWord of half in the alphabet
                lowerBorder = half;
            }
            halfOld = half;
        } while(delta != 0);
        return half;
    }

    /**
     * calls the suitable execute method
     * @param wholeInput the whole input
     * @param commandIdWord the idWord of the special command
     * @param parameters    the paramter that will be used by the execute method
     * @param game the current game
     * @throws InputException if the command is not vaild
     * @return returns the output of the executed command
     */
    public String call(String wholeInput, String commandIdWord, String parameters, Game game) throws InputException {
        Commands curr = arrCommands[find(commandIdWord)];
        if (!wholeInput.matches(curr.getSignature())) {
            throw new InputException();
        }
        String output = curr.execute(parameters, game);
        return output;
    }
}