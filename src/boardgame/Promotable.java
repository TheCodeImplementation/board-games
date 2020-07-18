/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame;

import java.util.ArrayList;

public interface Promotable {

    boolean                     isPromotionNeeded(Move move);
    Piece[]                     getPromotionOptions();
    void                        promote(GameState gameState, Position position, Piece piece);
    static ArrayList<GameState> getPromotedSuccessors(Promotable piece, ArrayList<GameState> successors) {
        ArrayList<GameState> toDiscard = new ArrayList<>();
        ArrayList<GameState> toAdd = new ArrayList<>();

        for(GameState successor : successors) {
            if(piece.isPromotionNeeded(successor.getLastMove())) {
                for(Piece promotion : piece.getPromotionOptions()) {
                    GameState copy = successor.copy();
                    piece.promote(copy, copy.getLastMove().getDestination(), promotion);
                    toAdd.add(copy);
                }
                toDiscard.add(successor);
            }
        }
        successors.removeAll(toDiscard);
        successors.addAll(toAdd);
        return successors;
    }
}
