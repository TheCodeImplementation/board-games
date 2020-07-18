/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package chess;

import boardgame.*;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ChessState extends GameState {
    //CONSTRUCTORS
    public ChessState() {
        this("a-b-c-d-e-f-g-h\n" +
                       "8|♜|♞|♝|♛|♚|♝|♞|♜|8\n" +
                       "7|♟|♟|♟|♟|♟|♟|♟|♟|7\n" +
                       "6| | | | | | | | |6\n" +
                       "5| | | | | | | | |5\n" +
                       "4| | | | | | | | |4\n" +
                       "3| | | | | | | | |3\n" +
                       "2|♙|♙|♙|♙|♙|♙|♙|♙|2\n" +
                       "1|♖|♘|♗|♕|♔|♗|♘|♖|1\n" +
                         "a-b-c-d-e-f-g-h"
        );
    }
    public ChessState(String arrangement) {
        super(8,8,
            arrangement,
            new Piece[]{
                new Pawn(Colour.LIGHT),
                new Pawn(Colour.DARK),
                new Rook(Colour.LIGHT),
                new Rook(Colour.DARK),
                new Knight(Colour.LIGHT),
                new Knight(Colour.DARK),
                new Bishop(Colour.LIGHT),
                new Bishop(Colour.DARK),
                new Queen(Colour.LIGHT),
                new Queen(Colour.DARK),
                new King(Colour.LIGHT),
                new King(Colour.DARK)
            });
    }
    //METHODS
    @Override public boolean    isMoveLegal(Move move) {
        if(!super.isMoveLegal(move))
            return false;
        //We will make the move on a dummy board then test if it puts the current player in check.
        ChessState copy = (ChessState)copy();
        //Remove sender temporarily. If the move is promotable for pawn, we don't want it to contact the sender (gui) while only checking move legality.
        UserInterface senderCopy = move.getSender();
        move.setSender(null);
        copy.makeMove(move);
        move.setSender(senderCopy); //put sender back in the move
        return !copy.isCheck(getPlayerTurn());
    }
    @Override public void       makeMove(Move move) {
        super.makeMove(move);
        endPlayerTurn();
    }
    @Override public void       postMoveUINotifications(UserInterface ui) {
        if (isCheck(getPlayerTurn()))
            if (isCheckUnbreakable())
                ((ChessUI) ui).notifyCheckmate(getPositionsOf(King.class, getPlayerTurn()).get(0), getPlayerTurn().opponent());
            else
                ((ChessUI) ui).notifyCheck(getPositionsOf(King.class, getPlayerTurn()).get(0));
        else if (isStalemate())
            ((ChessUI) ui).notifyStalemate();
        else if (insufficientMaterial())
            ((ChessUI) ui).notifyInsufficientMaterial();
        else if (isThreefoldRepetition())
            ((ChessUI) ui).notifyThreefoldRepetition();
    }
    public boolean              isCheck(Colour playerColour) {
        return isSquareAttacked(getPositionsOf(King.class, playerColour).get(0), playerColour.opponent());
    }
    public boolean              isCheckUnbreakable() {
        //test if the player has any check-breaking move.
        for(Position start : getPlayersPositions(getPlayerTurn())) {
            for(Position destination : getAllPossiblePositions()){
                Move move = new Move(start, destination);
                if (isMoveLegal(move)) {
                    ChessState copy = (ChessState)copy();
                    copy.makeMove(move);
                    if (!copy.isCheck(copy.getPlayerTurn())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public boolean              isCheckmate() {
        return isCheck(getPlayerTurn()) && isCheckUnbreakable();
    }
    public boolean              insufficientMaterial() {
        //if either player has 3+ piece...
        for(Colour colour : new Colour[]{Colour.LIGHT, Colour.DARK}) {
            if(getPlayersPositions(colour).size() >= 3)
                return false;
        }
        //if there's 1+ queen...
        if(!getPositionsOf(Queen.class, null).isEmpty())
            return false;
        //or there's 1+ rook
        if(!getPositionsOf(Rook.class, null).isEmpty())
            return false;
        //or there's 1+ pawn
        if(!getPositionsOf(Pawn.class, null).isEmpty())
            return false;
        //BISHOPS
        for(Colour colour : new Colour[]{Colour.LIGHT, Colour.DARK}) {
            ArrayList<Position> bishops = getPositionsOf(Bishop.class, colour);
            //or there's 2+ bishops of the same colour
            if(bishops.size() >= 2) {
                //bishops must be on different colour squares
                boolean whiteSquare, blackSquare;
                whiteSquare = blackSquare = false;
                for(Position pos : bishops) {
                    if((pos.getX() + pos.getY()) % 2 == 1)
                        whiteSquare = true;
                    if((pos.getX() + pos.getY()) % 2 == 0)
                        blackSquare = true;
                }
                if(whiteSquare && blackSquare)
                    return false;
            }
            //or there's 1 bishop and 1+ knight
            else if(bishops.size() == 1 && !getPositionsOf(Knight.class, colour).isEmpty())
                return false;
        }
        //or there's 2+ knights of the same colour
        for(Colour colour : new Colour[]{Colour.LIGHT, Colour.DARK}) {
            ArrayList<Position> knights = getPositionsOf(Knight.class, colour);
            if (knights.size() >= 2) {
                return false;
            }
        }
        return true;
    }
    public boolean              isStalemate() {
        return getSuccessors().size() == 0;
    }
    public boolean              isThreefoldRepetition() {
        ChessState tmpChessState = this;
        int counter = 0;
        while(tmpChessState != null) {
            //if the pieces are arranged similarly...
            if(tmpChessState.equals(this)) {
                //if en passant is available, no previous board could possibly match the current board - so stop.
                if (tmpChessState.isEnPassantEligible())
                    return false;
                //if castling eligibility is not the same at this stage, it won't be at an earlier stage either, so stop.
                if (!Arrays.equals(castlingEligibility(), tmpChessState.castlingEligibility()))
                    return false;
                if(++counter == 3) //once we have three, we can stop looping.
                    return true;
            }
            tmpChessState = (ChessState)tmpChessState.getPrev();
        }
        return false;
    }
    /**
     * Returns a boolean array indicating the castling eligibility for a board.
     * [0] = White Queen-side
     * [1] = White King-side
     * [2] = Black Queen-side
     * [3] = Black King-side
     */
    public boolean[]            castlingEligibility() {
        boolean[] results = new boolean[4];
        int counter = 0;
        for (int y : new int[]{0, 7}) { //rank 0 (white pieces) then rank 7 (black pieces).
            if (!hasPieceMoved(new Position(4, y))) { //King
                results[counter++] = !hasPieceMoved(new Position(0, y)); //Q.S. Rook
                results[counter++] = !hasPieceMoved(new Position(7, y)); //K.S. Rook
            }else
                counter += 2;
        }
        return results;
    }
    @Override public boolean    isTerminal() {
        return isCheckmate()
            || isStalemate()
            || insufficientMaterial()
            || isThreefoldRepetition();
    }
    /**
     * Returns the positions of the pieces matching the given type (and colour?).
     * @param type The type of the piece to be found.
     * @param colour The colour of the piece to be searched. If null, will find piece type of either colour
     */
    public ArrayList<Position>  getPositionsOf(Class type, Colour colour) {
        ArrayList<Position> results = new ArrayList<>();

        for(Position position : getAllPossiblePositions()) {
            Piece piece = getPiece(position);
            if(type.isInstance(piece))
                if (colour == null || colour == piece.getColour())
                    results.add(position);
        }
        return results;
    }
    public boolean              isPathObstructed(Move move) {
        //This method only works on straight (up/down/left/right) or diagonal paths. if neither, just return true
        if(!move.isStraight() && !move.isDiagonal())
            return true;
        //x and y will end up -1, 0 or 1
        int x = move.getX2() - move.getX1();
        int y = move.getY2() - move.getY1();
        if(x != 0)
            x /= Math.abs(x);//reduce positive/negative x to 1/-1
        if(y != 0)
            y /= Math.abs(y);//reduce positive/negative y to 1/-1

        Position testSquare = new Position(move.getX1() + x, move.getY1() + y);

        while(!testSquare.equals(move.getDestination())) {
            if(getPiece(testSquare) != null) {
                return true;
            }
            testSquare.setX(testSquare.getX() + x);
            testSquare.setY(testSquare.getY() + y);
        }
        return false;
    }
    public boolean              hasPieceMoved(Position position) {
        Piece piece = getPiece(position);
        //if the piece isn't there at present, then obviously it's moved.
        if(piece == null)
            return true;
        GameState tmpGameState = this.getPrev();
        while(tmpGameState != null) {
            if(piece != tmpGameState.getPiece(position))
                return true;
            tmpGameState = tmpGameState.getPrev();
        }
        return false;
    }
    public boolean              isSquareAttacked(Position coordinates, Colour attackerColour) {
        for(Position position : getPlayersPositions(attackerColour))
            if(getPiece(position).isMoveLegal(this, new Move(position, coordinates)))
                return true;
        return false;
    }
    public boolean              isEnPassantEligible() {
        Move lastMove = getLastMove();
        if(lastMove == null)
            return false;
        //get positions on either side
        Position leftSide   = new Position(lastMove.getX2()-1, lastMove.getY2());
        Position rightSide  = new Position(lastMove.getX2()+1, lastMove.getY2());

        return lastMove.distance() == 2
            && getPiece(lastMove.getDestination()) instanceof Pawn
            && ( (leftSide.getX() >= 0 && getPiece(leftSide) instanceof Pawn)
            || (rightSide.getX() <= 7 && getPiece(rightSide) instanceof Pawn) );
    }
    @Override public double     evaluate(Colour maximizingPlayer) {
        if(isCheckmate())
            return getPlayerTurn() == maximizingPlayer ? -2000 : 2000; //arbitrarily large number
        return super.evaluate(maximizingPlayer);
    }
}