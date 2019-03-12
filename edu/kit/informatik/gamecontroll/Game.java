package edu.kit.informatik.gamecontroll;

import edu.kit.informatik.exceptions.BoardException;
import edu.kit.informatik.exceptions.CommandOrderException;
import edu.kit.informatik.exceptions.TokenSetException;
import edu.kit.informatik.exceptions.WrongPhaseException;
import edu.kit.informatik.Constant;

/**
 * the game which contains all important aspects for example the board and the available tokens
 */
public class Game {
    // private static final String OK = "OK";
    private static final byte MOVEDONE = 0;
    private static final byte ROLLDONE = 1;
    private static final byte PLACEDONE = 2;
    private static final byte STARTPHASE = 1;
    /** the result of the game */
    Result result;
    /** phase of the game (phase 3 is the phase after the game has ended) */
    private byte phase = STARTPHASE;
    /** shows if the set-vc was already used in this phase of the game */
    private boolean setVcUsed = false;
    /** the actual board on which the game is played */
    private Board board;
    /**
     * the number which was either rolled or can be used because the rolled token
     * isn't availble
     */
    private byte[] availableNumbers = {0, 0};
    /** the diffrent steps of a round (roll, place, move) */
    private byte stepMovement = MOVEDONE;

    /**
     * constructor to create a new game
     * 
     * @param boardY y length of the board
     * @param boardX x length of the board
     */
    public Game(int boardY, int boardX) {
        this.board = new Board(boardY, boardX);
        this.phase = STARTPHASE;
        this.setVcUsed = false;
    }

    /**
     * getter for the phase
     * 
     * @return the attribute phase
     */
    public byte getPhase() {
        return phase;
    }

    /**
     * sets the setVcUsed variable to true
     */
    public void setTrueSetVcUsed() {
        setVcUsed = true;
    }

    /**
     * switches the phase of the game to the next one
     */
    public void checkPhaseSwitch() {
        if (board.tokenSetEmpty()) {
            phase++;
            setVcUsed = false;
            board.resetTokens();
        }
    }

    /**
     * converts the board into a String
     * 
     * @return the visualized board as a String
     */
    public String printBoard() {
        String output = board.printBoard();
        return output;
    }

    /**
     * returns the state of the field
     * 
     * @param field the coordinates of the field
     * @return - for empty, + for a blocked field and C or V for Ceres or Vesta
     */
    public String stateOfField(Coordinat field) {
        String output = Character.toString(board.stateOfField(field));
        return output;
    }

    /**
     * moves vesta or ceres
     * 
     * @param path the path at which the token is moved
     * @return OK if everything was successfull
     * @throws WrongPhaseException   if the "move" is not valid at this phase of the
     *                               game
     * @throws CommandOrderException the commands were insert in the wrong order
     */
    public String move(Coordinat[] path) throws WrongPhaseException, CommandOrderException {
        board.storeBoard();
        if (path.length > availableNumbers[0] && path.length > availableNumbers[1]) {
            throw new CommandOrderException("The command should only conatain " + availableNumbers[0] + " or "
                    + availableNumbers[1] + " parameters.");
        }
        if (!setVcUsed) {
            throw new WrongPhaseException("The command set-vc need to be used at first in each phase.");
        }
        if (stepMovement != PLACEDONE) {
            throw new CommandOrderException();
        }
        String output = "";
        if (phase == 1) {
            try {
                output = board.move(path, board.getVestaValue(), board.getLastUsedToken());
            } catch (BoardException e) {
                output = e.getMessage();
            }
        } else if (phase == 2) {
            try {
                output = board.move(path, board.getCeresValue(), board.getLastUsedToken());
            } catch (BoardException e) {
                output = e.getMessage();
            }
        } else {
            throw new WrongPhaseException("In this state of the game no movement is allowed.");
        }
        if (output.matches(Constant.getExceptionBeginningRegex())) {
            stepMovement = PLACEDONE;
            board.resetBoardToStored();
        } else {
            stepMovement = MOVEDONE;
            checkPhaseSwitch();
        }
        return output;
    }

    /**
     * sets the token for a planet
     * 
     * @param field future field of the planet
     * @return OK or an error message
     * @throws WrongPhaseException if the game is in the wrong round or phase
     */
    public String setVC(Coordinat field) throws WrongPhaseException {
        if (setVcUsed) {
            throw new WrongPhaseException("The command set-vc need to be used at first in each phase.");
        }
        final char errorPlanet = 'E';
        char planet = errorPlanet;
        if (phase != 1 && phase != 2) {
            throw new WrongPhaseException("The command set-vc can only be used in phase 1 or 2 at the beginning.");
        }
        if (phase == 1) {
            planet = Constant.BoardTokens.VESTAVALUE;
        }
        if (phase == 2) {
            planet = Constant.BoardTokens.CERESVALUE;
        }
        String output = "";
        try {
            output = board.setVC(field, planet);
        } catch (BoardException e) {
            output = e.getMessage();
        }
        if (output.equals(Constant.OK)) {
            checkPhaseSwitch();
            setTrueSetVcUsed();
        }
        return output;
    }

    /**
     * roll a symobl
     * 
     * @param rolledSymbol rolled symbol
     * @return OK
     * @throws CommandOrderException another command need to be insert in front of
     *                               this one
     */
    public String roll(String rolledSymbol) throws CommandOrderException {
        if (!setVcUsed) {
            throw new CommandOrderException("The command set-vc need to be used at first in each phase.");
        }
        if (stepMovement != MOVEDONE) {
            throw new CommandOrderException();
        }
        byte rolledNumber = 0;
        if (rolledSymbol.matches("([2-6])")) {
            rolledNumber = Byte.parseByte(rolledSymbol);
        }
        if (rolledSymbol.equals("DAWN")) {
            rolledNumber = Constant.DAWN;
        }
        // checks if there are alternative tokens available (if they are 0 there is non
        // available)
        byte[] alternativeTokens = {0, 0};
        try {
            alternativeTokens = board.findToken(rolledNumber);
        } catch (TokenSetException e) {
            return e.getMessage();
        }
        // there are no alternatives so the searched token is available
        if (alternativeTokens[0] == 0 && alternativeTokens[1] == 0) {
            alternativeTokens[0] = rolledNumber;
        }
        this.availableNumbers = alternativeTokens;
        stepMovement = ROLLDONE;
        return Constant.OK;
    }

    /**
     * places the mission controll tokens
     * 
     * @param start on end of the token
     * @param end   the other end of the token
     * @return OK or an error message
     * @throws CommandOrderException if roll wasn't used before
     * @throws BoardException        if the planetSourrounded function gets an error
     */
    public String place(Coordinat start, Coordinat end) throws CommandOrderException, BoardException {
        board.storeBoard();
        if (!setVcUsed) {
            throw new CommandOrderException("The command set-vc need to be used at first in each phase.");
        }
        if (stepMovement != ROLLDONE) {
            throw new CommandOrderException();
        }
        String output = "";
        try {
            output = board.place(start, end, availableNumbers);
        } catch (BoardException e) {
            board.resetBoardToStored();
            return e.getMessage();
        }
        char planet = 0;
        if (phase == 1)
            planet = Constant.BoardTokens.VESTAVALUE;
        if (phase == 2)
            planet = Constant.BoardTokens.CERESVALUE;
        if (board.planetSurrounded(planet)) {
            checkPhaseSwitch();
            stepMovement = MOVEDONE;
        } else {
            stepMovement = PLACEDONE;
        }
        if (output.matches(Constant.getExceptionBeginningRegex())) {
            // if an error occured
            stepMovement = ROLLDONE;
        }
        return output;
    }

    /**
     * calculates the result of the game in the endgame stage
     * 
     * @return an integer that represents the result of the game
     * @throws CommandOrderException if the second phase of the game was not
     *                               finished
     */
    public String showResult() throws CommandOrderException {
        if (phase != 3) {
            throw new CommandOrderException(
                    "The result can only be calculated after the second phase of the game has ended.");
        }
        return board.calculatResult();
    }

    /**
     * resets the whole game
     * 
     * @return OK
     */
    public String reset() {
        board.emptyBoard();
        this.phase = STARTPHASE;
        this.setVcUsed = false;
        this.availableNumbers[0] = 0;
        this.availableNumbers[1] = 0;
        this.stepMovement = MOVEDONE;
        board.resetTokens();
        return Constant.OK;
    }
}