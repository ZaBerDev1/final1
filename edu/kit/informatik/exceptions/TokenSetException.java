package edu.kit.informatik.exceptions;

import edu.kit.informatik.Constant;

public class TokenSetException extends Exception {
    private static final long serialVersionUID = 3;
    /** position of an alternative to the not found token */
    private int alternative1Pos = 0;
    /** position of an alternative to the not found token */
    private int alternative2Pos = 0;

    /**
     * constructor
     * 
     * @param errorMessage the speciefied error message
     */
    public TokenSetException(String errorMessage) {
        super(Constant.EXCEPTIONBEGINNING + errorMessage);
    }

    /**
     * constructor that also sets the position of the alternative tokens
     * 
     * @param errorMessage    the speciefied error message
     * @param alternative1Pos position of an alternative to the not found token
     * @param alternative2Pos position of an alternative to the not found token
     */
    public TokenSetException(String errorMessage, int alternative1Pos, int alternative2Pos) {
        super(Constant.EXCEPTIONBEGINNING + errorMessage);
        this.alternative1Pos = alternative1Pos;
        this.alternative2Pos = alternative2Pos;
    }

    /**
     * returns the postion of the frist alternative token
     * 
     * @return the first alternative token
     */
    public int getAlternative1Pos() {
        return alternative1Pos;
    }

    /**
     * returns the postion of the second alternative token
     * 
     * @return the second alternative token
     */
    public int getAlternative2Pos() {
        return alternative2Pos;
    }
}