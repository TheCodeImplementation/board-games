/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package chess.pieces;

import boardgame.Colour;
import boardgame.GameState;
import boardgame.Move;
import chess.ChessPiece;

public class Knight extends ChessPiece {
    //CONSTRUCTOR
    public Knight(Colour colour) {
        super(colour, "♘", "♞", 3);
    }
    //METHODS
    @Override public boolean isMoveLegal(GameState gameState, Move move) {
        int xDiff = Math.abs(move.getX2() - move.getX1());
        int yDiff = Math.abs(move.getY2() - move.getY1());

        return super.isMoveLegal(gameState, move)
            && ( (xDiff == 1 && yDiff == 2) || (xDiff == 2 && yDiff == 1) );
    }
}
