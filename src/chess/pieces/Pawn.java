/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package chess.pieces;

import boardgame.*;
import chess.ChessPiece;
import chess.ChessUI;
import chess.ChessState;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Pawn extends ChessPiece implements Promotable{

    private Piece[] promotionOptions = {
            new Knight(getColour()),
            new Bishop(getColour()),
            new Rook(getColour()),
            new Queen(getColour())
    };

    //CONSTRUCTOR
    public Pawn(Colour colour) {
        super(colour,"♙","♟",1);
    }
    //METHODS
    @Override public boolean                isMoveLegal(GameState gameState, Move move) {
        return super.isMoveLegal(gameState, move)
            && ( isMoveForward(gameState, move)
            || isMoveAdvanceTwo(gameState, move)
            || isMoveCapture(gameState, move)
            || isMoveEnPassant(gameState, move) );
    }
    @Override public void                   makeMove(GameState gameState, Move move) {
        super.makeMove(gameState, move);

        if(isMoveEnPassant(gameState, move)) {
            //remove opponent piece
            int modifier = getColour() == Colour.LIGHT ? -1 : 1;
            gameState.setPiece(new Position(move.getX2(), move.getY2()+modifier), null);
        }else if(isPromotionNeeded(move)){
            if(move.getSender() != null)
                gameState.setPiece(move.getDestination(), ((ChessUI) move.getSender()).getPromotionType(promotionOptions));
            else
                gameState.setPiece(move.getDestination(), promotionOptions[3]);
        }
    }
    private boolean                         isMoveForward(GameState gameState, Move move) {
        int y = getColour() == Colour.LIGHT ? 1 : -1;
        return move.distance() == 1
                && gameState.getPiece(move.getDestination()) == null
                && move.getX2() == move.getX1()
                && move.getY2() == move.getY1() + y;
    }
    private boolean                         isMoveCapture(GameState gameState, Move move) {
        Piece piece = gameState.getPiece(move.getDestination());
        int forwardY = getColour() == Colour.LIGHT ? 1 : -1;

        return move.isDiagonal()
            && move.distance() == 1
            && move.getY2() == move.getY1() + forwardY
            && piece != null
            && piece.getColour() == getColour().opponent();
    }
    private boolean                         isMoveAdvanceTwo(GameState gameState, Move move) {
        int[] startlines = getColour() == Colour.LIGHT ? new int[]{0,1} : new int[]{6,7};

        return move.distance() == 2
            && !((ChessState) gameState).isPathObstructed(move)
            && gameState.getPiece(move.getDestination()) == null
            && move.getX1() == move.getX2()
            && IntStream.of(startlines).anyMatch (y -> y == move.getY1()); //https://stackoverflow.com/a/1128728
    }
    public boolean                          isMoveEnPassant(GameState gameState, Move move) {
        Move lastMove = gameState.getLastMove();
        int enemiesStartingLine = getColour() == Colour.LIGHT ? 6 : 1;
        int forwardY            = getColour() == Colour.LIGHT ? 1 : -1;

        return lastMove != null
            && gameState.getPiece(lastMove.getDestination()) instanceof Pawn
            && lastMove.distance() == 2
            && lastMove.getY1() == enemiesStartingLine
            && move.getY1() == lastMove.getY2()
            && move.getX2() == lastMove.getX2()
            && move.getY2() == lastMove.getY2() + forwardY
            && move.isDiagonal()
            && move.distance() == 1;
    }
    @Override public boolean                isPromotionNeeded(Move move) {
        int finishLine = getColour() == Colour.LIGHT ? 7 : 0; //which row the player's pieces promote on.
        return move.getY2() == finishLine;
    }
    @Override public Piece[]                getPromotionOptions() {
        return promotionOptions;
    }
    @Override public void                   promote(GameState gameState, Position position, Piece promotion) {
        gameState.setPiece(position, promotion);
    }
    @Override public ArrayList<GameState>   getSuccessors(GameState gameState, Position myPosition) {
        return Promotable.getPromotedSuccessors(this, super.getSuccessors(gameState,myPosition));
    }
}
