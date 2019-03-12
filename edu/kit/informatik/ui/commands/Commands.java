package edu.kit.informatik.ui.commands;

import edu.kit.informatik.exceptions.BoardException;
import edu.kit.informatik.exceptions.CommandOrderException;
import edu.kit.informatik.exceptions.InputException;
import edu.kit.informatik.exceptions.WrongPhaseException;
import edu.kit.informatik.gamecontroll.*;
import edu.kit.informatik.Constant;

/**
 * contains all valid commands as well as there signature and behavior
 */
public enum Commands {
    /**
     * Returns the state of a specified field
     */
    STATE(Constant.CommandRegex.STATEREGEX) {
        @Override
        public String execute(String parameters, Game game) {
            String[] arrParameters = parameters.split(";");
            Coordinat field = new Coordinat();
            field.setY(Integer.parseInt(arrParameters[0]));
            field.setX(Integer.parseInt(arrParameters[1]));
            return game.stateOfField(field);
        }
    },

    /**
     * returns the board
     */
    PRINT(Constant.CommandRegex.PRINTREGEX) {
        @Override
        public String execute(String parameters, Game game) throws InputException {
            if (!parameters.equals("")) {
                throw new InputException("This command shouldn't contain any parameters.");
            }
            return game.printBoard();
        }
    },

    /**
     * sets the position of Vesta or Ceres
     */
    SETVC(Constant.CommandRegex.SETVCREGEX) {
        @Override
        public String execute(String parameters, Game game) {
            String[] singleCoordinat = parameters.split(";");
            Coordinat field = new Coordinat(Integer.parseInt(singleCoordinat[0]), Integer.parseInt(singleCoordinat[1]));
            try {
                return game.setVC(field);
            } catch (WrongPhaseException e) {
                return e.getMessage();
            }
        }
    },

    /**
     * inputs the rolled symbol
     */
    ROLL(Constant.CommandRegex.ROLLREGEX) {
        @Override
        public String execute(String parameters, Game game) {
            String output = "";
            try {
                output = game.roll(parameters);
            } catch (CommandOrderException e) {
                output = e.getMessage();
            }
            return output;
        }
    },

    /**
     * places one of the mission controll tokens
     */
    PLACE(Constant.CommandRegex.PLACEREGEX) {
        @Override
        public String execute(String parameters, Game game) {
            String[] endPoints = parameters.split(":");
            String[] startCoordinat = endPoints[0].split(";");
            String[] endCoordinat = endPoints[1].split(";");
            Coordinat start = new Coordinat(Integer.parseInt(startCoordinat[0]), Integer.parseInt(startCoordinat[1]));
            Coordinat end = new Coordinat(Integer.parseInt(endCoordinat[0]), Integer.parseInt(endCoordinat[1]));
            try {
                return game.place(start, end);
            } catch (CommandOrderException e) {
                return e.getMessage();
            } catch (BoardException e) {
                return e.getMessage();
            }
        }
    },

    /**
     * moves Ceres or Vesta
     */
    MOVE(Constant.CommandRegex.MOVEREGEX) {
        @Override
        public String execute(String parameters, Game game) {
            String[] arrCoordiants = parameters.split(":");
            Coordinat[] path = new Coordinat[arrCoordiants.length];
            for (int i = 0; i < arrCoordiants.length; i++) {
                String[] singleCoordinat  = arrCoordiants[i].split(";");
                if (singleCoordinat.length == 2 && !(singleCoordinat[0].equals("") && singleCoordinat[1].equals("")))
                    path[i] = new Coordinat(Integer.parseInt(singleCoordinat[0]), Integer.parseInt(singleCoordinat[1]));
            }
            try {
                return game.move(path);
            } catch (WrongPhaseException e) {
                return e.getMessage();
            } catch (CommandOrderException e) {
                return e.getMessage();
            }

        }
    },

    /**
     * calculate the final result. The command is only availble after the second phase
     */
    SHOWRESULT(Constant.CommandRegex.SHOWRESULTREGEX) {
        @Override
        public String execute(String parameters, Game game) throws InputException {
            String output = "";
            try {
                output = game.showResult();
            } catch (CommandOrderException e) {
                output = e.getMessage();
            }
            return output;
        }
    },

    /**
     * surrenders the current game and starts a new one
     */
    RESET(Constant.CommandRegex.RESETREGEX) {
        @Override
        public String execute(String parameters, Game game) throws InputException {
            String output = game.reset();
            return output;
        }
    },

    /**
     * closes the program
     */
    QUIT(Constant.CommandRegex.QUITREGEX) {
        @Override
        public String execute(String parameters, Game game) throws InputException {
            return "exit";
        }
    };

    /** the form of the command */
    private String signature;
    /** the id of the command */
    private String idWord;

    /**
     * constructor for the commands
     * @param signature the form of the command
     */
    Commands(String signature) {
            this.signature = signature;
            String signatureWithoutEndSymbol = signature.replaceAll("[$]", "");
            this.idWord = signatureWithoutEndSymbol.split(" ")[0].substring(1);
    }

    /**
     * getter for the signature of a command
     * @return the signature of the command
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * getter for the idWord of a command
     * @return the idWord of the command
     */
    public String getIdWord() {
        return this.idWord;
    }

    @Override
    public String toString() {
        return this.idWord;
    }

    /**
     * executes the command
     * @param parameters the inputed parameters
     * @param game the current game
     * @return the terminal output
     * @throws InputException if the inputed commands are not valid
     */
    public abstract String execute(String parameters, Game game) throws InputException;
}