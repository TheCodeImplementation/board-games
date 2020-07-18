/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package chess.pieces;

import boardgame.Colour;
import boardgame.GameState;
import chess.ChessPiece;
import boardgame.Move;
import chess.ChessState;

public class Queen extends ChessPiece {
    //CONSRUCTOR
    public Queen(Colour colour) {
        super(colour,"♕","♛",9);
    }
    //METHODS
    @Override public boolean isMoveLegal(GameState gameState, Move move) {
        return super.isMoveLegal(gameState, move)
            && !((ChessState) gameState).isPathObstructed(move)
            && ( move.isDiagonal() || move.isStraight() );
    }
}
