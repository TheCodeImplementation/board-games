/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package chess;

import boardgame.*;

public abstract class ChessPiece extends Piece {
    //CONSTRUCTOR
    protected ChessPiece(Colour colour, String whiteSymbol, String blackSymbol, int value) {
        super(colour, whiteSymbol, blackSymbol, value);
    }
    //METHODS
    @Override public boolean    isMoveLegal(GameState gameState, Move move) {
        Piece destination = gameState.getPiece(move.getDestination());
        return destination == null
            || destination.getColour() == getColour().opponent();
    }
    @Override public void       makeMove(GameState gameState, Move move) {
        gameState.setPiece(move.getDestination(), gameState.getPiece(move.getStart()));
        gameState.setPiece(move.getStart(), null);
    }
}
