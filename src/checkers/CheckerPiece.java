/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package checkers;

import boardgame.*;

public abstract class CheckerPiece extends Piece {
    //CONSTRUCTOR
    protected CheckerPiece(Colour colour, String whiteSymbol, String blackSymbol, int value) {
        super(colour, whiteSymbol, blackSymbol, value);
    }
    //METHODS
    @Override public boolean isMoveLegal(GameState gameState, Move move) {
        Piece inbetween = gameState.getPiece(getInbetweenSquare(move));

        return move.isDiagonal()
            && gameState.getPiece(move.getDestination()) == null
            && ( move.distance() == 1
            //if distance is 2, the jumped piece must be an enemy and not already captured.
            || (move.distance() == 2 && inbetween != null && inbetween.getColour() == getColour().opponent()
            && !((CheckersState) gameState).capturedPieces.contains(getInbetweenSquare(move))) );
    }
    @Override public void    makeMove(GameState gameState, Move move) {
        gameState.setPiece(move.getDestination(), gameState.getPiece(move.getStart()));
        gameState.setPiece(move.getStart(), null);
        if(move.distance() == 2)
            ((CheckersState) gameState).addCaptured(getInbetweenSquare(move));
    }
    private Position         getInbetweenSquare(Move move) {
        int x = (move.getX2() + move.getX1()) / 2;
        int y = (move.getY2() + move.getY1()) / 2;
        return new Position(x,y);
    }
}
