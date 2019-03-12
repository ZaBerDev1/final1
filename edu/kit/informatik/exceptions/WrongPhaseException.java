package edu.kit.informatik.exceptions;

import edu.kit.informatik.Constant;

public class WrongPhaseException extends Exception {
    private static final long serialVersionUID = 1;

    /**
     * constructor
     * 
     * @param errorMessage the speciefied error message
     */
    public WrongPhaseException(String errorMessage) {
        super(Constant.EXCEPTIONBEGINNING + errorMessage);
    }
}