/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame.bots;

import boardgame.Colour;
import boardgame.GameState;

import java.util.ArrayList;
import java.util.Collections;

public class OptimalMiniMax implements MoveBot {

    private final String    BOT_NAME = "Optimax";
    private int             maxDepth;
    private Colour          maximizingPlayer;
    //METHODS
    @Override public GameState getMove(GameState gameState, int maxDepth) {
        this.maxDepth = maxDepth;
        this.maximizingPlayer = gameState.getPlayerTurn();
        GameState chosen = null;
        if(!gameState.isTerminal()) {
            MinimaxNode max = max(gameState, 0);
            chosen = max.gameState;
            //max() returns the leaf board and its depth, so we must backtrack to find the actual next move/board.
            for (int i = 1; i < max.depth; i++) {
                chosen = chosen.getPrev();
            }
        }
        return chosen;
    }
    private MinimaxNode max(GameState gameState, int currentDepth){
        if(currentDepth == maxDepth || gameState.isTerminal())
            return new MinimaxNode(gameState, gameState.evaluate(maximizingPlayer), currentDepth);
        MinimaxNode v = new MinimaxNode(Double.NEGATIVE_INFINITY);
        for(GameState successor : shuffle(gameState.getSuccessors())) {
            v = biggest(v, min(successor, currentDepth+1));
        }
        return v;
    }
    private MinimaxNode min(GameState gameState, int currentDepth){
        if(currentDepth == maxDepth || gameState.isTerminal())
            return new MinimaxNode(gameState, gameState.evaluate(maximizingPlayer), currentDepth);
        MinimaxNode v = new MinimaxNode(Double.POSITIVE_INFINITY);
        for(GameState successor : shuffle(gameState.getSuccessors())) {
            v = smallest(v, max(successor, currentDepth+1));
        }
        return v;
    }
    private static MinimaxNode smallest(MinimaxNode n1, MinimaxNode n2) {
        if(n1.score < n2.score)
            return n1;
        if(n1.score > n2.score)
            return n2;
        //if both have the same score, return whichever is reachable in fewer moves
        if(n1.depth <= n2.depth)
            return n1;
        else
            return n2;
    }
    private static MinimaxNode biggest(MinimaxNode n1, MinimaxNode n2) {
        if(n1.score > n2.score)
            return n1;
        if(n1.score < n2.score)
            return n2;
        //if both have the same score, return whichever is reachable in fewer moves
        if(n1.depth <= n2.depth)
            return n1;
        else
            return n2;
    }
    @Override public String toString() {
        return BOT_NAME;
    }
    private ArrayList<GameState> shuffle(ArrayList<GameState> gameStates) {
        Collections.shuffle(gameStates);
        return gameStates;
    }
    //INNER CLASS
    private class MinimaxNode {
        GameState gameState;
        double score;
        int depth;

        public MinimaxNode(Double score) {
            this.score = score;
        }

        public MinimaxNode(GameState gameState, double score, int depth) {
            this.gameState = gameState;
            this.score = score;
            this.depth = depth;
        }
    }
}
