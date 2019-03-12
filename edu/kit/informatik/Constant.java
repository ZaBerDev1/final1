package edu.kit.informatik;

/**
 * a class that contains all globally used constants
 */
public final class Constant {
    /** used to output OK */
    public static final String OK = "OK";
    /** the start of every Exception */
    public static final String EXCEPTIONBEGINNING = "404 ";
    /** value of the DAWN token */
    public static final byte DAWN = 7;
    /** conatins all symbols of the cube */
    public static final byte[] SYMBOLVALUES = {2, 3, 4, 5, 6, DAWN};

    /**
     * returns all posible cahr in an array
     * 
     * @return an array of all psoible characters
     */
    public static char[] getAllBoardTokens() {
        char[] all = new char[] {BoardTokens.VESTAVALUE, BoardTokens.CERESVALUE, BoardTokens.EMPTYVALUE,
                BoardTokens.MISSIONCONTROLLVALUE};
        return all;
    }

    /**
     * returns the regex for any Exception
     * 
     * @return the regex as a String
     */
    public static String getExceptionBeginningRegex() {
        return "^" + EXCEPTIONBEGINNING + ".*";
    }

    /**
     * class that contains all posible symbols form the board
     * 
     * if a new one gets added it should also be added to the getAll method
     */
    public final class BoardTokens {
        // token variables
        /** value of the token Vesta */
        public static final char VESTAVALUE = 'V';
        /** value of the token Ceres */
        public static final char CERESVALUE = 'C';
        /** value of an empty field */
        public static final char EMPTYVALUE = '-';
        /** value of the token mission controll */
        public static final char MISSIONCONTROLLVALUE = '+';

    }

    /**
     * class that contains all regex of the commands
     */
    public final class CommandRegex {
        /** regex for the standard numbers of the x coordinat */
        public static final String STANDARDNUMBERX = "[1][0]|[0-9]";
        /** regex for the standard numbers of the y coordinat */
        public static final String STANDARDNUMBERY = "[1][0-4]|[0-9]";
        /** regex for the x coordinat of the place method */
        public static final String PLACENUMBERX = "[1][0-6]|[0-9]|-[1-6]";
        /** regex for the y coordinat of the place method */
        public static final String PLACENUMBERY = "[1][0-9]|20|[0-9]|-[1-6]";
        /** regex of the commnad print */
        public static final String PRINTREGEX = "^print$";
        /** regex of the command state */
        public static final String STATEREGEX = "^state (" + STANDARDNUMBERX + ");(" + STANDARDNUMBERY + ")$";
        /** regex of the commnad set-vc */
        public static final String SETVCREGEX = "^set-vc (" + STANDARDNUMBERX + ");(" + STANDARDNUMBERY + ")$";
        /** regex of the commnad roll */
        public static final String ROLLREGEX = "^roll ([2-6]|DAWN)$";
        /** regex of the commnad place */
        public static final String PLACEREGEX = "^place (" + PLACENUMBERX + ");(" + PLACENUMBERY + "):(" + PLACENUMBERX
                + ");(" + PLACENUMBERY + ")$";
        /** regex of the commnad move */
        public static final String MOVEREGEX = "^move (" + STANDARDNUMBERX + ");(" + STANDARDNUMBERY + ")(:("
                + STANDARDNUMBERX + ");(" + STANDARDNUMBERY + "))*$";
        /** regex of the commanad show-result */
        public static final String SHOWRESULTREGEX = "^show-result$";
        /** regex of the commanad reset */
        public static final String RESETREGEX = "^reset$";
        /** regex of the commanad quit */
        public static final String QUITREGEX = "^quit$";
    }
}