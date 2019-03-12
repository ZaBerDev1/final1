package edu.kit.informatik.exceptions;

import edu.kit.informatik.Constant;

public class ResultException extends Exception {
    private static final long serialVersionUID = 3;

    /**
     * constructor
     * 
     * @param errorMessage the speciefied error message
     */
    public ResultException(String errorMessage) {
        super(Constant.EXCEPTIONBEGINNING + errorMessage);
    }
}