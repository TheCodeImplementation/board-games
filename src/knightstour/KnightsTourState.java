/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package knightstour;

import boardgame.*;
import chess.pieces.*;

import java.util.ArrayList;

public class KnightsTourState extends GameState {
    public ArrayList<Position> traversed = new ArrayList<>();
    public ArrayList<Position> visited = new ArrayList<>(64);
    //CONSTRUCTORS
    public KnightsTourState() {
        super(8,8,
      "a-b-c-d-e-f-g-h\n" +
                "8|♘| | | | | | | |8\n" +
                "7| | | | | | | | |7\n" +
                "6| | | | | | | | |6\n" +
                "5| | | | | | | | |5\n" +
                "4| | | | | | | | |4\n" +
                "3| | | | | | | | |3\n" +
                "2| | | | | | | | |2\n" +
                "1| | | | | | | | |1\n" +
                  "a-b-c-d-e-f-g-h",
                new Piece[]{new Knight(Colour.LIGHT)}
                );
        traversed.add(new Position(0,7));
        visited.add(new Position(0,7));
    }
    //METHODS
    @Override public void       makeMove(Move move) {
        super.makeMove(move);
        traversed.add(move.getDestination());
        if(!visited.contains(move.getDestination()))
            visited.add(move.getDestination());
    }
    @Override public void       postMoveUINotifications(UserInterface ui) {
        if(isTerminal())
            ui.notifyGameOver(null);
    }
    @Override public GameState  copy() {
        KnightsTourState copy = (KnightsTourState)super.copy();
        copy.traversed  = (ArrayList<Position>) traversed.clone();
        copy.visited    = (ArrayList<Position>) visited.clone();
        return copy;

    }
    @Override public double     evaluate(Colour maximizingPlayer) {
        return isTerminal() ? 10000 : visited.size() - traversed.size(); //10000 is an arbitrarily large number
    }
    @Override public boolean    isTerminal() {
        return visited.size() == 64;
    }
}
