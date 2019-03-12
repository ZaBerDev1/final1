package edu.kit.informatik.gamecontroll;

import edu.kit.informatik.Constant;
import edu.kit.informatik.exceptions.BoardException;
import edu.kit.informatik.exceptions.ResultException;

/**
 * an object of this class contains all calculated scores and results of the finished game
 */
class Result {
    /** the actual board of the game */
    private Board board;
    /** the reference board */
    private char[][] refBoard;
    /** the end score of the game */
    private int score = 0;
    /** the end score of Vesta */
    private int scoreVesta = 0;
    /** the end score of Ceres */
    private int scoreCeres = 0;
    private final char marker = 'M';

    /**
     * constructor that gets the current state of the board
     * and creates an reference board that can be used for the calculations
     * 
     * @param board the current state of the board
     */
    public Result(Board board) {
        this.board = board;
        refBoard = new char[board.getBoardYLength()][board.getBoardXLength()];
        for (int x = 0; x < board.getBoardXLength(); x++) {
            for (int y = 0; y < board.getBoardYLength(); y++) {
                refBoard[y][x] = board.getField(y, x);
            }
        }
    }

    /**
     * @param planet choces which score should be calculated
     * @return the planet score
     * @throws BoardException if the find method gets an error
     * @throws ResultExcetption if there is not exactly one planet of each kind
     */
    private int calculatePlanetScoreRecursiveStart(char planet) throws BoardException, ResultException {
        Coordinat[] planetPos = board.findTokenOnBoard(planet);
        if (planetPos.length != 1) {
            throw new ResultException("The result can't be calculated if there is not exactly "
            + "one Ceres and one Vesta on the board.");
        }
        int calculatedScore = 0;
        calculatedScore += calculatePlanetScoreRecursive(new Coordinat(planetPos[0].getY() + 1, planetPos[0].getX()));
        calculatedScore += calculatePlanetScoreRecursive(new Coordinat(planetPos[0].getY() - 1, planetPos[0].getX()));
        calculatedScore += calculatePlanetScoreRecursive(new Coordinat(planetPos[0].getY(), planetPos[0].getX() + 1));
        calculatedScore += calculatePlanetScoreRecursive(new Coordinat(planetPos[0].getY(), planetPos[0].getX() - 1));
        removeMarkers();
        return calculatedScore;
    }

    /**
     * calculates the score of a planet in a recrusice way
     * @param the coordinat of the field which should be checked
     * @return the score of the planet
     */
    private int calculatePlanetScoreRecursive(Coordinat curr) {
        int calculatedScore = 0;
        if (!(0 <= curr.getX() && curr.getX() < board.getBoardXLength() 
            && 0 <= curr.getY() && curr.getY() < board.getBoardYLength())) {
            return calculatedScore;
        }
        if (refBoard[curr.getY()][curr.getX()] == Constant.BoardTokens.EMPTYVALUE) {
            refBoard[curr.getY()][curr.getX()] = marker;
            calculatedScore++;
            calculatedScore += calculatePlanetScoreRecursive(new Coordinat(curr.getY() + 1, curr.getX()));
            calculatedScore += calculatePlanetScoreRecursive(new Coordinat(curr.getY() - 1, curr.getX()));
            calculatedScore += calculatePlanetScoreRecursive(new Coordinat(curr.getY(), curr.getX() + 1));
            calculatedScore += calculatePlanetScoreRecursive(new Coordinat(curr.getY(), curr.getX() - 1));
        }
        return calculatedScore;
    }

    /**
     * calculates the whole score
     * @return the whole score
     * @throws ResultException if the planet exists twice or is not placed yet
     * @throws BoardException form a method of the board
     */
    public int calculate() throws ResultException, BoardException {
        scoreVesta = calculatePlanetScoreRecursiveStart(Constant.BoardTokens.VESTAVALUE);
        scoreCeres = calculatePlanetScoreRecursiveStart(Constant.BoardTokens.CERESVALUE);
        score = Math.max(scoreVesta, scoreCeres)
                + (Math.max(scoreVesta, scoreCeres) - Math.min(scoreVesta, scoreCeres));
        return score;
    }

    /**
     * removes every marker form the refBoard
     */
    private void removeMarkers() {
        for (int x = 0; x < board.getBoardXLength(); x++) {
            for (int y = 0; y < board.getBoardYLength(); y++) {
                if (refBoard[y][x] == marker) {
                    refBoard[y][x] = Constant.BoardTokens.EMPTYVALUE;
                }
            }
        }
    }
}