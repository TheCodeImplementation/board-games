/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package checkers;

import boardgame.*;
import checkers.pieces.*;

import java.util.ArrayDeque;

public class CheckersState extends GameState {
    Position                toPromote;
    ArrayDeque<Position>    capturedPieces = new ArrayDeque<>();
    //CONSTRUCTORS
    public CheckersState() {
        this( "a-b-c-d-e-f-g-h\n" +
                        "8|○| |○| |○| |○| |8\n" +
                        "7| |○| |○| |○| |○|7\n" +
                        "6|○| |○| |○| |○| |6\n" +
                        "5| | | | | | | | |5\n" +
                        "4| | | | | | | | |4\n" +
                        "3| |●| |●| |●| |●|3\n" +
                        "2|●| |●| |●| |●| |2\n" +
                        "1| |●| |●| |●| |●|1\n" +
                          "a-b-c-d-e-f-g-h"
        );
    }
    public CheckersState(String arrangement) {
        super(8,8, arrangement,
                new Piece[]{
                        new Man(Colour.LIGHT),
                        new Man(Colour.DARK),
                        new King(Colour.LIGHT),
                        new King(Colour.DARK)
                });
        setPlayerTurn(Colour.DARK);
    }
    //METHODS
    @Override public boolean        isMoveLegal(Move move) {
        return super.isMoveLegal(move)
            //must make a jump move if one is available.
            && ( move.distance() == 2
            || !playerHasJump(getPlayerTurn()) );
    }
    @Override public void           makeMove(Move move) {
        super.makeMove(move);
        //if last move was not a jump or it was but there are no more jumps available to the piece that jumped...
        if(move.distance() == 1 || !hasJumps(move.getDestination()) ) {
            endPlayerTurn();
            while(!capturedPieces.isEmpty())
                setPiece(capturedPieces.pop(), null);
            if(toPromote != null) {
                Man promoted = (Man)getPiece(toPromote);
                promoted.promote(this, toPromote);
                toPromote = null;
            }
        }
    }
    @Override public void           postMoveUINotifications(UserInterface ui) {
        if(isTerminal())
            ui.notifyGameOver(getPlayerTurn().opponent());
    }
    public boolean                  playerHasJump(Colour playerColour) {
        for (int x = 0; x < getBoardWidth(); x++) {
            for (int y = 0; y < getBoardHeight(); y++) {
                Position piecePosition = new Position(x,y);
                Piece piece = getPiece(piecePosition);
                if(piece != null && piece.getColour() == playerColour && hasJumps(piecePosition))
                    return true;
            }
        }
        return false;
    }
    public boolean                  hasJumps(Position piecePosition) {
        Piece piece = getPiece(piecePosition);
        int[] Xs = {piecePosition.getX() - 2, piecePosition.getX() + 2};
        int[] Ys;
        if(piece instanceof King)
            Ys = new int[]{piecePosition.getY() - 2, piecePosition.getY() + 2};
        else
            Ys = new int[]{piece.getColour() == Colour.LIGHT ? piecePosition.getY() - 2 : piecePosition.getY() + 2};
        for (int x : Xs) {
            for(int y : Ys) {
                if (isMoveLegal(new Move(piecePosition, new Position(x, y))))
                    return true;
            }
        }
        return false;
    }
    @Override public CheckersState  copy() {
        CheckersState copy = (CheckersState)super.copy();
        copy.toPromote = toPromote;
        copy.capturedPieces = capturedPieces.clone();
        return copy;
    }
    @Override public boolean        isTerminal() {
        return super.isTerminal()
            || getSuccessors().isEmpty();
    }
    @Override public double         evaluate(Colour maximizingPlayer) {
        if(isTerminal())
            return getPlayerTurn() != maximizingPlayer ? 2000 : -2000; //arbitrarily large number
        //super.evaluation performs simple material balance (allies - enemies), but, in checkers,
        //some pieces on the board have been captured - making it even more favourable to the current player.
        return super.evaluate(maximizingPlayer)
            + (getPlayerTurn() == maximizingPlayer ? capturedPieces.size() : -capturedPieces.size());
    }
    //GETTERS & SETTERS
    public void addCaptured(Position position) {
        capturedPieces.add(position);
    }
    public void setToPromote(Position position) {
        toPromote = position;
    }
}
