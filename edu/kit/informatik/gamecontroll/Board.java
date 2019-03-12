package edu.kit.informatik.gamecontroll;

import java.util.*;
import edu.kit.informatik.exceptions.BoardException;
import edu.kit.informatik.exceptions.ResultException;
import edu.kit.informatik.exceptions.TokenSetException;
import edu.kit.informatik.Constant;

/**
 * The class is a model of the physical board.
 */
class Board {
    /** length of the board in the y direction */
    private int boardYLength;
    /** length of the board in the x direction */
    private int boardXLength;
    /** the board is modeled as an 2D array */
    private char arrBoard[][];
    /** the board which is stored throw the storeBoard method */
    private char storedBoard[][];
    /** contains all not used mission controll tokens in a sorted way */
    private TokenSet tokenSet = new TokenSet(Constant.SYMBOLVALUES);
    /** the token which was used by the place-method */
    private byte lastUsedToken = 0;

    /**
     * constructor to declare the new board
     * @param boardY hight of the board
     * @param boardX length of the board
     */
    Board(int boardY, int boardX) {
        boardXLength = boardX;
        boardYLength = boardY;
        arrBoard = new char[boardY][boardX];
        emptyBoard();
        storeBoard();
        resetTokens();
    }

    /**
     * getter for a single field of the board
     * @param y the y coordinat of the field
     * @param x the x coordinat of the field
     * @return the token on the field
     */
    public char getField(int y, int x) {
        return arrBoard[y][x];
    }

    /**
     * getter for the y length of the board
     * @return thie y board length
     */
    public int getBoardYLength() {
        return this.boardYLength;
    }

    /**
     * getter for the x length of the board
     * @return thie x board length
     */
    public int getBoardXLength() {
        return this.boardXLength;
    }

    /**
     * resets the set of tokens
     */
    public void resetTokens() {
        tokenSet = new TokenSet(Constant.SYMBOLVALUES);
    }

    /**
     * checks if the token set is empty
     * @return true if it is empty
     */
    public boolean tokenSetEmpty() {
        return tokenSet.checkEmpty();
    }

    /**
     * getter for the value of the token vesta
     * @return the char which represents the planet on the board
     */
    public char getVestaValue() {
        return Constant.BoardTokens.VESTAVALUE;
    }

    /**
     * getter for the value of the token ceres
     * @return the char which represents the planet on the board
     */
    public char getCeresValue() {
        return Constant.BoardTokens.CERESVALUE;
    }

    /**
     * getter that returns the last used token
     * @return the last used token
     */
    public byte getLastUsedToken() {
        return this.lastUsedToken;
    }

    /** fills all fields with the EMPTYVALUE symbol */
    public void emptyBoard() {
        for (int x = 0; x < boardXLength; x++) {
            for (int y = 0; y < boardYLength; y++) {
                arrBoard[y][x] = Constant.BoardTokens.EMPTYVALUE;
            }
        }
    }

    /** stores the arrBoard in the variable storeBoard */
    public void storeBoard() {
        storedBoard = new char[boardYLength][boardXLength];
        for (int x = 0; x < boardXLength; x++) {
            for (int y = 0; y < boardYLength; y++) {
                storedBoard[y][x] = arrBoard[y][x];
            }
        }
    }

    /** resets the whole board to the stored board */
    public void resetBoardToStored() {
        try {
            setWholeBoard(storedBoard);
        } catch (BoardException e) {
            // can't happen because the stored board was constructed in the rules
        }
    }

    /**
     * sets the whole board to a board that is given
     * @param givenBoard the given board
     * @throws BoardException if the given board has the wrong shape or a planet
     *                        exists twice
     */
    public void setWholeBoard(char[][] givenBoard) throws BoardException {
        char[][] backUpBoard = new char[boardYLength][boardXLength]; // it's a local back-up-board to let the storeBoard
                                                                     // untouched
        for (int x = 0; x < boardXLength; x++) {
            for (int y = 0; y < boardYLength; y++) {
                backUpBoard[y][x] = arrBoard[y][x];
            }
        }
        int givenBoardYLength = givenBoard.length;
        if (givenBoardYLength != boardYLength) {
            throw new BoardException("The given board has not the same size as the board of the game.");
        }
        int givenBoardXLength = givenBoard[0].length;
        if (givenBoardXLength != boardXLength) {
            throw new BoardException("The given board has not the same size as the board of the game.");
        }
        for (int x = 0; x < boardXLength; x++) {
            for (int y = 0; y < boardYLength; y++) {
                arrBoard[y][x] = givenBoard[y][x];
            }
        }
        if (findTokenOnBoard(Constant.BoardTokens.CERESVALUE).length > 1
                || findTokenOnBoard(Constant.BoardTokens.VESTAVALUE).length > 1) {
            for (int x = 0; x < boardXLength; x++) {
                for (int y = 0; y < boardYLength; y++) {
                    arrBoard[y][x] = backUpBoard[y][x];
                }
            }
            throw new BoardException("There are two planets the same on the given board.");
        }
    }

    /**
     * creats a String that contains the whole board
     * @return a visualization of the board
     */
    public String printBoard() {
        String output = "";
        for (int y = 0; y < boardYLength; y++) {
            for (int x = 0; x < boardXLength; x++) {
                output += arrBoard[y][x];
            }
            if (y < boardYLength - 1)
                output += "\n";
        }
        return output;
    }

    /**
     * moves Vesta or Ceres
     * @param path      the path of the planet
     * @param planet    the char which represents the planet on the board
     * @param maxLength the maximum length of the path
     * @return OK if there are no issues or a error message
     * @throws BoardException if the given path is too long
     */
    public String move(Coordinat[] path, char planet, byte maxLength) throws BoardException {
        if (maxLength < path.length) {
            throw new BoardException("The inputed path is too long.");
        }
        for (int i = 0; i < path.length; i++) {
            Coordinat start;
            Coordinat end = path[i];
            // first point of the path is not inputed so it need to be generated
            if (i == 0) {
                start = findTokenOnBoard(planet)[0];
            } else {
                start = path[i - 1];
            }
            try {
                atomicMove(start, end, planet);
            } catch (BoardException e) {
                return e.getMessage();
            }
        }
        return Constant.OK;
    }

    /**
     * the parts of which every move can be split up
     * @param start  the start field of the move
     * @param end    the end field of the move
     * @param planet the char which represents the planet on the board
     * @throws BoardException
     */
    private void atomicMove(Coordinat start, Coordinat end, char planet) throws BoardException {
        if (stateOfField(start) != planet) {
            throw new BoardException("The token " + planet + " is at the time not at this position.");
        }
        if (stateOfField(end) != Constant.BoardTokens.EMPTYVALUE) {
            throw new BoardException("This path is blocked.");
        }
        if (!(start.horizontalConnection(end) || start.verticalConnection(end))) {
            throw new BoardException("Two fields of the path are not directly near each other.");
        }
        // actual movement
        arrBoard[start.getY()][start.getX()] = Constant.BoardTokens.EMPTYVALUE;
        arrBoard[end.getY()][end.getX()] = planet;
    }

    /**
     * checks if the planet is surrounded by not empty fields
     * @param planet the planet
     * @return true if it is surrounded
     * @throws BoardException if there is more than one or less than one planet on
     *                        the board
     */
    public boolean planetSurrounded(char planet) throws BoardException {
        Coordinat[] planetPos = findTokenOnBoard(planet);
        if (planetPos.length != 1) {
            throw new BoardException("The planet should be only once on the board.");
        }
        int x = 0;
        int y = 0;
        // left
        x = planetPos[0].getX() - 1;
        y = planetPos[0].getY();
        if (!checkFieldSurrounded(x, y)) {
            return false;
        }
        x = planetPos[0].getX() + 1;
        y = planetPos[0].getY();
        if (!checkFieldSurrounded(x, y)) {
            return false;
        }
        // top
        x = planetPos[0].getX();
        y = planetPos[0].getY() - 1;
        if (!checkFieldSurrounded(x, y)) {
            return false;
        }
        // bottom
        x = planetPos[0].getX();
        y = planetPos[0].getY() + 1;
        if (!checkFieldSurrounded(x, y)) {
            return false;
        }
        return true;
    }

    /**
     * checks if the field is surrounded
     * @param x the x value of the field
     * @param y the y value of the field
     * @return true if the position is occupied
     */
    private boolean checkFieldSurrounded(int x, int y) {
        if (x >= 0 && x < boardXLength && y >= 0 && y < boardYLength
                && arrBoard[y][x] == Constant.BoardTokens.EMPTYVALUE) {
            return false;
        }
        return true;
    }

    /**
     * returns the state of a specified field as a String
     * @param field the coordinates of the field
     * @return the value of the field as a String
     */
    public char stateOfField(Coordinat field) {
        return arrBoard[field.getY()][field.getX()];
    }

    /**
     * sets the position for Vesta or Ceres
     * @param field  the new position of Vesta or Ceres
     * @param planet the planet which should be placed
     * @return OK if the process was successfull
     * @throws BoardException if there is already a token on the field
     */
    public String setVC(Coordinat field, char planet) throws BoardException {
        if (numberOfTokens(planet) != 0) {
            throw new BoardException("There is already a token of the planet on the board.");
        }
        // check if the field is empty
        if (stateOfField(field) != Constant.BoardTokens.EMPTYVALUE) {
            throw new BoardException("There is already a token on this field.");
        }
        arrBoard[field.getY()][field.getX()] = planet;
        return Constant.OK;
    }

    /**
     * returns the number of tokens of a speciefied kind on the board
     * @param kindOfToken the kind of tokens as a char
     * @return the number of tokens as an int
     */
    private int numberOfTokens(char kindOfToken) {
        int count = 0;
        for (int y = 0; y < boardYLength; y++) {
            for (int x = 0; x < boardXLength; x++) {
                if (arrBoard[y][x] == kindOfToken) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * if the token was not used the alternative output is both 0
     * @param token the token which is checked
     * @return the alternative tokens
     * @throws TokenSetException if in the getToken-method a wrong position was used
     */
    public byte[] findToken(byte token) throws TokenSetException {
        byte[] alternativeTokens = {0, 0};
        try {
            tokenSet.find(token);
        } catch (TokenSetException e) {
            alternativeTokens[0] = tokenSet.getToken(e.getAlternative1Pos());
            alternativeTokens[1] = tokenSet.getToken(e.getAlternative2Pos());
        }
        return alternativeTokens;
    }

    /**
     * places a token from mission controll
     * @param start            the start point
     * @param end              the end point
     * @param availableNumbers the length of the token
     * @return OK or an error message
     * @throws BoardException if the path is not valid
     */
    public String place(Coordinat start, Coordinat end, byte[] availableNumbers) throws BoardException {
        int length = 0;
        char direction = 0;
        final char horizontalDirection = 'x';
        final char verticalDirection = 'y';
        if (start.verticalInfinty(end)) {
            int requiredLength = start.distanceVertical(end);
            length = selectLength(requiredLength, availableNumbers);
            direction = verticalDirection;
        } else if (start.horizontalInfinity(end)) {
            int requiredLength = start.distanceHorizontal(end);
            length = selectLength(requiredLength, availableNumbers);
            direction = horizontalDirection;
        } else {
            throw new BoardException("The token can only be placed vertical or horizontal.");
        }
        if (start.distanceHorizontal(end) > length || start.distanceVertical(end) > length) {
            throw new BoardException("The inputed coordinats are not " + length + " fields away form each other.");
        }
        boolean buildDirectionNormal = true;
        if (start.getX() >= end.getX() && start.getY() >= end.getY()) {
            buildDirectionNormal = false;
        }
        // initalize build variables
        int x = 0;
        int y = 0;
        int loopMax = length;
        if (buildDirectionNormal) {
            x = start.getX();
            y = start.getY();
        } else {
            x = end.getX();
            y = end.getY();
        }
        if (direction == horizontalDirection)
            loopMax += x;
        if (direction == verticalDirection)
            loopMax += y;
        boolean loopCondition = true;
        int countNotPlaceable = 0;
        do {
            // actual placement
            if (x >= 0 && x < boardXLength && y >= 0 && y < boardYLength) {
                if (arrBoard[y][x] != Constant.BoardTokens.EMPTYVALUE)
                    throw new BoardException("You can't place a token on top of another.");
                arrBoard[y][x] = Constant.BoardTokens.MISSIONCONTROLLVALUE;
            } else if (length + 1 == Constant.DAWN) {
                countNotPlaceable++;
                if (countNotPlaceable == Constant.DAWN)
                    throw new BoardException("Even one end of the DAWN token should touch the board.");
            } else {
                throw new BoardException("The token should be placed on the board.");
            }
            if (direction == horizontalDirection) {
                loopCondition = x < loopMax;
                x++;
            }
            if (direction == verticalDirection) {
                loopCondition = y < loopMax;
                y++;
            }
        } while (loopCondition); // remove token
        byte token = (byte) (length + 1);
        try {
            if (!tokenSet.remove(tokenSet.find(token))) {
                throw new TokenSetException("The position of the remove method is not valid.");
            }
        } catch (TokenSetException e) {
            return e.getMessage();
        }
        this.lastUsedToken = token;
        return Constant.OK;
    }

    /** selects the most suitable length */
    private int selectLength(int requiredLength, byte[] availableTokens) throws BoardException {
        if (requiredLength == availableTokens[0] - 1) {
            return availableTokens[0] - 1;
        }
        if (requiredLength == availableTokens[1] - 1) {
            return availableTokens[1] - 1;
        }
        throw new BoardException("The given coordinates aren't matching any valid tokens.");
    }

    /**
     * finds a token on the board and returns every position of it
     * @param searchedToken the token which we are looking for
     * @return an array of the coordinats of the token
     * @throws BoardException if the searched token doesn't exists in the whole game
     */
    public Coordinat[] findTokenOnBoard(char searchedToken) throws BoardException {
        // check if inputed searchedToken is valid
        char[] allPosibleTokens = Constant.getAllBoardTokens();
        boolean tokenExists = false;
        for (int i = 0; i < allPosibleTokens.length; i++) {
            if (allPosibleTokens[i] == searchedToken) {
                tokenExists = true;
            }
        }
        if (!tokenExists) {
            throw new BoardException("This token doesn't exist in the game.");
        }
        // search for the token
        List<Coordinat> coorList = new ArrayList<Coordinat>();
        for (int x = 0; x < boardXLength; x++) {
            for (int y = 0; y < boardYLength; y++) {
                if (searchedToken == arrBoard[y][x]) {
                    coorList.add(new Coordinat(y, x));
                }
            }
        }
        // convert the list to an array
        Coordinat[] coorArray = new Coordinat[coorList.size()];
        for (int i = 0; i < coorList.size(); i++) {
            coorArray[i] = coorList.get(i);
        }
        return coorArray;
    }

    /**
     * calculates the whole result of the game
     * @return the complete score
     */
    public String calculatResult() {
        Result result = new Result(this);
        String output = "";
        try {
            output = Integer.toString(result.calculate());
        } catch (ResultException e) {
            output = e.getMessage();
        } catch (BoardException e) {
            output = e.getMessage();
        }
        return output;
    }
}