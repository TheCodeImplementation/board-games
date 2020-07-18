/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package chess.pieces;

import boardgame.*;
import chess.ChessPiece;
import chess.ChessState;

public class King extends ChessPiece {

    //CONSTRUCTOR
    public King(Colour colour) {
        super(colour,"♔","♚",0);
        //value = 0 b/c both kings will always be on the board and so will cancel each other out any way.
    }
    //METHODS
    @Override public boolean    isMoveLegal(GameState gameState, Move move) {
        return super.isMoveLegal(gameState, move)
            && ( move.distance() == 1 || isCastling(gameState, move) );
    }
    @Override public void       makeMove(GameState gameState, Move move) {
        if(move.distance() == 2) { //if distance is 2, it must be castling.
            int startline = getColour() == Colour.LIGHT ? 0 : 7; //players first line/rank
            Position queensideDestination       = new Position(2,startline);

            //if castling queenside
            if (move.getDestination().equals(queensideDestination)) {
                Position queensideRook = new Position(0, startline);
                //move rook
                gameState.setPiece(new Position(3,startline), gameState.getPiece(queensideRook));
                gameState.setPiece(queensideRook, null);
            } else { //castling kingside
                Position kingsideRook = new Position(7,startline);
                //move rook
                gameState.setPiece(new Position(5,startline), gameState.getPiece(kingsideRook));
                gameState.setPiece(kingsideRook, null);
            }
        }
        super.makeMove(gameState, move);
    }
    /**
     * Determine if a move is a legal attempt to castle.
     */
    private boolean             isCastling(GameState gameState, Move move) {
        //can't castle if the path is obstructed
        if(((ChessState) gameState).isPathObstructed(move))
            return false;
        //pieces start line/rank
        int startline = getColour() == Colour.LIGHT ? 0 : 7;
        //need-to-know coordinates
        Position queensideRook              = new Position(0, startline);
        Position queensideDestination       = new Position(2,startline);
        Position queensideTransitorySquare  = new Position(3,startline);
        Position kingsStart                 = new Position(4,startline);
        Position kingsideTransitorySquare   = new Position(5,startline);
        Position kingsideDestination        = new Position(6,startline);
        Position kingsideRook               = new Position(7,startline);
        //if the move doesn't start at the king square and end at the queenside/kingside square...
        if(!move.getStart().equals(kingsStart)
                || ( !move.getDestination().equals(queensideDestination)
                && !move.getDestination().equals(kingsideDestination)) )
            return false;
        //if king's position isn't occupied by a king or the king has moved at some point...
        if(!(gameState.getPiece(kingsStart) instanceof King) || ((ChessState) gameState).hasPieceMoved(kingsStart))
            return false;
        //if queenside move
        if(move.getDestination().equals(queensideDestination)) {
            //if queenside rook's position isn't occupied by a rook or the rook has moved at some point...
            if (!(gameState.getPiece(queensideRook) instanceof Rook)
                    || ((ChessState) gameState).hasPieceMoved(queensideRook))
                return false;
            //are the 3 important squares under attack?
            for (Position position : new Position[]{kingsStart, queensideTransitorySquare, queensideDestination})
                if (((ChessState) gameState).isSquareAttacked(position, getColour().opponent()))
                    return false;
        }
        //if kingside move
        if(move.getDestination().equals(kingsideDestination)) {
            //if kingside rook's position isn't occupied by a rook or the rook has moved at some point...
            if (!(gameState.getPiece(kingsideRook) instanceof Rook)
                    || ((ChessState) gameState).hasPieceMoved(kingsideRook)) {
                return false;
            }
            //are the 3 important squares under attack?
            for (Position position : new Position[]{kingsStart, kingsideTransitorySquare, kingsideDestination})
                if (((ChessState) gameState).isSquareAttacked(position, getColour().opponent())) {
                    return false;
                }
        }
        return true;
    }
}
