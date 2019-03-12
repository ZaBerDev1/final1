package edu.kit.informatik.exceptions;

import edu.kit.informatik.Constant;

public class CommandOrderException extends Exception {
    private static final long serialVersionUID = 3;

    /**
     * constructor
     * 
     * @param errorMessage the speciefied error message
     */
    public CommandOrderException(String errorMessage) {
        super(Constant.EXCEPTIONBEGINNING + errorMessage);
    }

    /**
     * constructor the returns a general error message for that type of exception
     */
    public CommandOrderException() {
        super(Constant.EXCEPTIONBEGINNING + "The commands were insert in the wrong order.");
    }
}