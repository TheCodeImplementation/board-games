/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package checkers.pieces;

import boardgame.*;
import checkers.CheckerPiece;
import checkers.CheckersState;

public class Man extends CheckerPiece{

    //CONSTRUCTOR
    public Man(Colour colour) {
        super(colour, "○", "●", 1);
    }
    //METHODS
    @Override public boolean    isMoveLegal(GameState gameState, Move move) {
        return super.isMoveLegal(gameState, move)
            && ( (getColour() == Colour.LIGHT && move.isSouth())
            || (getColour() == Colour.DARK && move.isNorth()) );
    }
    @Override public void       makeMove(GameState gameState, Move move) {
        super.makeMove(gameState, move);
        int promotionY = getColour() == Colour.LIGHT ? 0 : 7;
        if(move.getY2() == promotionY)
            ((CheckersState) gameState).setToPromote(move.getDestination());
    }
    public void                 promote(GameState gameState, Position position) {
        gameState.setPiece(position, new King(getColour()));
    }
}
