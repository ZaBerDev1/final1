package edu.kit.informatik.exceptions;

import edu.kit.informatik.Constant;

public class InputException extends Exception {
    private static final long serialVersionUID = 0;

    /**
     * constructor
     * 
     * @param errorMessage the speciefied error message
     */
    public InputException(String errorMessage) {
        super(Constant.EXCEPTIONBEGINNING + errorMessage);
    }

    /**
     * constructor which contains a list of all commands as a error message
     */
    public InputException() {
        super("This is not a valid command. Please insert one of the following commands." + "\nstate m;n"
                + "\nprint" + "\nset-vc m;n" + "\nroll symbol" + "\nplace x1;y1:x2;y2" + "\nmove x1;y1:xi;yi"
                + "\nshow-result" + "\nreset" + "\nquit");
    }
}