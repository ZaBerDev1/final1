package edu.kit.informatik.gamecontroll;

import java.util.Arrays;
import edu.kit.informatik.exceptions.TokenSetException;

/**
 * represents the set of tokens which is currently available to the player
 */
class TokenSet {
    /** the value which is added in the array if the value was removed */
    private static final byte USEDTOKENVALUE = 0;
    /** all tokens that were currently not used */
    private byte[] notUsedTokens;

    /**
     * constructor that sets the notUsedTokens array
     * 
     * @param tokens the tokens which should be in the tokens array
     */
    public TokenSet(byte[] tokens) {
        notUsedTokens = new byte[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            notUsedTokens[i] = tokens[i];
        }
    }

    /**
     * returns the token as a byte
     * 
     * @param pos the position of the token in the notUsedToken array
     * @return the token as a byte
     * @throws TokenSetException if the position is not in the array
     */
    public byte getToken(int pos) throws TokenSetException {
        if (pos >= notUsedTokens.length) {
        throw new TokenSetException("This position should be lower than "
        + notUsedTokens.length + ".");
        }
        return notUsedTokens[pos];
    }

    /**
     * finds a token
     * 
     * @param searchedToken the tokens which the method searchs for
     * @return the position of the token in the array
     * @throws TokenSetException if the array doesn't contain the token
     */
    public int find(byte searchedToken) throws TokenSetException {
        sort();
        for (int i = 0; i < notUsedTokens.length - 1; i++) {
            if (notUsedTokens[i] == searchedToken) {
                return i;
            }
            if (notUsedTokens[i + 1] == searchedToken) {
                return i + 1;
            }
            if (notUsedTokens[i] < searchedToken && searchedToken < notUsedTokens[i + 1]) {
                throw new TokenSetException("This token is currently not available.", i, i + 1);
            }
        }
        throw new TokenSetException("This token is currently not available.", 0, notUsedTokens.length - 1);
    }

    /**
     * removes a token
     * 
     * @param pos the position of the token in the notUsedArray (can be calculate
     *            with find-method)
     * @return true: if it was removed, false: if the position is not in the array
     */
    public boolean remove(int pos) {
        try {
            notUsedTokens[pos] = USEDTOKENVALUE;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        sort();
        return true;
    }

    /**
     * checks if the token set is empty
     * @return true if it is empty
     */
    public boolean checkEmpty() {
        sort();
        return notUsedTokens[notUsedTokens.length - 1] == USEDTOKENVALUE;
    }

    @Override
    public String toString() {
        String output = "{";
        for (int i = 0; i < notUsedTokens.length; i++) {
            output += notUsedTokens[i];
            if (i == notUsedTokens.length - 1) {
                //last cycle
                output += "}";
            } else {
                //every other cylce
                output += ",";
            }
        }
        return output;
    }


    /**
     * sorts the notUsedTokens array
     */
    private void sort() {
        Arrays.sort(notUsedTokens);
    }
}