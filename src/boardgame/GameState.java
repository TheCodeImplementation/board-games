/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class GameState {
    private GameState   prev, future;
    private Move        lastMove;
    private Colour      playerTurn = Colour.LIGHT;
    private Piece[][]   squares;
    //CONSTRUCTORS
    public GameState(){
        this(8,8);
    }
    public GameState(int boardWidth, int boardHeight){
        squares = new Piece[boardWidth][boardHeight];
    }
    public GameState(int boardWidth, int boardHeight, String arrangement, Piece[] pieces) {
        this(boardWidth, boardHeight);
        //create a map linking pieces and their symbols
        HashMap<String, Piece> symbols = new HashMap<>();
        for(Piece piece : pieces) {
            symbols.put(piece.toString(), piece);
        }
        symbols.put(" ", null); //must add a space for empty squares
        int pointer = 0;
        //use the sequence of chess symbols to place the correct piece to each square
        for (int y = boardHeight-1; y >= 0; y--) {
            for (int x = 0; x < boardWidth; x++) {
                String symbol = arrangement.charAt(pointer++)+"";
                while(!symbols.containsKey(symbol))
                    symbol = arrangement.charAt(pointer++)+"";
                setPiece(new Position(x, y), symbols.get(symbol));
            }
        }
    }
    //METHODS
    public boolean              isMoveLegal(Move move) {
        //is the move within the bounds of the board?
        for (int coordinate : new int[]{move.getX1(), move.getY1(), move.getX2(), move.getY2()})
            if(coordinate < 0 || coordinate >= getBoardWidth())
                return false;
        Piece piece = getPiece(move.getStart());
        //does the chosen piece belong to the current player and can it make the requested move?
        return piece != null
            && piece.getColour() == getPlayerTurn()
            && piece.isMoveLegal(this, move);
    }
    public void                 makeMove(Move move) {
        getPiece(move.getStart()).makeMove(this, move);
        setLastMove(move);
    }
    public abstract void        postMoveUINotifications(UserInterface ui);
    public GameState            copy() {
        GameState copy = null;
        try{
            copy            = this.getClass().getDeclaredConstructor().newInstance();
            copy.prev       = prev;
            copy.future     = future;
            copy.lastMove   = lastMove;
            copy.playerTurn = playerTurn;
            for (int x = 0; x < squares.length; x++) {
                for (int y = 0; y < squares[x].length; y++) {
                    copy.squares[x][y] = squares[x][y];
                }
            }
        }catch(Exception e){
            System.out.println("ERROR: " + this.getClass() + " must have a no-arg constructor for copy() to work.");
            System.exit(1);
        }

        return copy;
    }
    public boolean              isTerminal() {
        //Do both players have material on the board?
        boolean whiteFound = false;
        boolean blackFound = false;
        for (Position position : getAllPossiblePositions()){
            Piece piece = getPiece(position);
            if(piece != null) {
                if (piece.getColour() == Colour.LIGHT)
                    whiteFound = true;
                else
                    blackFound = true;
            }
        }
        return !whiteFound || !blackFound;
    }
    public double               evaluate(Colour maximizingPlayer) {
        //Material balance evaluation function
        int score = 0;
        for(Position position : getAllPossiblePositions()) {
            Piece piece = getPiece(position);
            if (piece != null)
                if (piece.getColour() == maximizingPlayer)
                    score += piece.getValue();
                else
                    score -= piece.getValue();
        }
        return score;
    }
    public ArrayList<GameState> getSuccessors() {
        ArrayList<GameState> successors = new ArrayList<>();

        for(Position position : getPlayersPositions(getPlayerTurn())){
            Piece piece = getPiece(position);
            if(piece != null && piece.getColour() == getPlayerTurn()){
                successors.addAll(piece.getSuccessors(this, position));
            }
        }
        return successors;
    }
    public ArrayList<Position>  getPlayersPositions(Colour playerColour) {
        ArrayList<Position> positions = new ArrayList<>();

        for(Position position : getAllPossiblePositions()){
            Piece piece = getPiece(position);
            if(piece != null && piece.getColour() == playerColour)
                positions.add(position);
        }
        return positions;
    }
    public ArrayList<Position>  getAllPossiblePositions() {
        ArrayList<Position> positions = new ArrayList<>(64);

        for (int x = 0; x < getBoardWidth(); x++) {
            for (int y = 0; y < getBoardHeight(); y++) {
                positions.add(new Position(x, y));
            }
        }
        return positions;
    }
    public boolean              equals(GameState other) {
        return playerTurn == other.playerTurn
            && Arrays.deepEquals(squares, other.squares);
    }
    @Override public String toString() {
        String files = "  a b c d e f g h"; //the column headings
        StringBuilder sb = new StringBuilder(files + "\n");
        for (int y = 7; y >= 0; y--) {
            sb.append( (y+1) +"|"); //add the left-side ranks (row numbers)
            for (int x = 0; x < 8; x++) {
                if(squares[x][y] != null) {
                    sb.append(squares[x][y] + "|");
                }else {
                    sb.append(" |");
                }
            }
            sb.append(y+1 + "\n");  //add the right-side ranks (row numbers)
        }
        sb.append(files);
        return sb.toString();
    }
    //GETTERS & SETTERS
    public GameState            getPrev() {
        return prev;
    }
    public void                 setPrev(GameState prev) {
        this.prev = prev;
    }
    public GameState            getFuture() {
        return future;
    }
    public void                 setFuture(GameState future) {
        this.future = future;
    }
    public Move                 getLastMove() {
        return lastMove;
    }
    public void                 setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }
    public Colour               getPlayerTurn() {
        return playerTurn;
    }
    public void                 setPlayerTurn(Colour playerTurn) {
        this.playerTurn = playerTurn;
    }
    public void                 endPlayerTurn() {
        playerTurn = playerTurn.opponent();
    }
    public Piece                getPiece(Position position) {
        return squares[position.getX()][position.getY()];
    }
    public void                 setPiece(Position position, Piece piece) {
        squares[position.getX()][position.getY()] = piece;
    }
    public int                  getBoardWidth() {
        return squares.length;
    }
    public int                  getBoardHeight() {
        return squares[0].length;
    }
}
