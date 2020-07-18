/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame;

import java.util.ArrayList;

public abstract class Piece {
    private final   Colour  colour;
    protected       String  lightSymbol;
    protected       String  darkSymbol;
    protected       int     value;
    //CONSTRUCTOR
    protected Piece(Colour colour, String lightSymbol, String darkSymbol, int value) {
        this.colour = colour;
        this.lightSymbol = lightSymbol;
        this.darkSymbol = darkSymbol;
        this.value = value;
    }
    //METHODS
    public abstract boolean     isMoveLegal(GameState gameState, Move move);
    public abstract void        makeMove(GameState gameState, Move move);
    @Override public String     toString() {
        return getColour() == Colour.LIGHT ? lightSymbol : darkSymbol;
    }
    public ArrayList<GameState> getSuccessors(GameState gameState, Position myPosition) {
        ArrayList<GameState> successors = new ArrayList<>();

        for(Position position : gameState.getAllPossiblePositions()) {
            Move move = new Move(myPosition, position);
            if (gameState.isMoveLegal(move)) {
                GameState copy = gameState.copy();
                copy.setPrev(gameState); //connect copy to the chain of boards.
                copy.makeMove(move);
                successors.add(copy);
            }
        }
        return successors;
    }
    //GETTERS
    public int      getValue() {
        return value;
    }
    public Colour   getColour() {
        return colour;
    }
}