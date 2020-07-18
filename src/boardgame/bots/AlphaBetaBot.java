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

public class AlphaBetaBot implements MoveBot {

    private final String    BOT_NAME = "Alpha-Beta";
    private int             maxDepth = 3;
    private Colour          maximizingPlayer;

    //METHODS
    @Override public GameState      getMove(GameState gameState, int maxDepth) {
        GameState chosen = null;
        maximizingPlayer = gameState.getPlayerTurn();
        if(!gameState.isTerminal()) {
            this.maxDepth = maxDepth;
            double value = Double.NEGATIVE_INFINITY;
            double alpha = Double.NEGATIVE_INFINITY;
            for(GameState successor : shuffle(gameState.getSuccessors()) ) {
                System.out.println();
                Double tmp = Math.max(value, min(successor, 0, alpha, Double.POSITIVE_INFINITY));
                if(tmp > value) {
                    value = tmp;
                    chosen = successor;
                }
                alpha = Math.max(alpha, value);
            }
        }
        return chosen;
    }
    private double                  min(GameState gameState, int depth, double alpha, double beta){
        if(depth == maxDepth || gameState.isTerminal())
            return gameState.evaluate(maximizingPlayer);
        double value = Double.POSITIVE_INFINITY;
        for(GameState successor : shuffle(gameState.getSuccessors()) ) {
            value = Math.min(value, max(successor, depth+1, alpha, beta));
            if(value <= alpha)
                return value;
            beta = Math.min(beta, value);
        }
        return value;
    }
    private double                  max(GameState gameState, int depth, double alpha, double beta){
        if(depth == maxDepth || gameState.isTerminal())
            return gameState.evaluate(maximizingPlayer);
        double value = Double.NEGATIVE_INFINITY;
        for(GameState successor : shuffle(gameState.getSuccessors()) ) {
            value = Math.max(value, min(successor, depth+1, alpha, beta));
            if(value >= beta)
                return value;
            alpha = Math.max(alpha, value);
        }
        return value;
    }
    @Override public String         toString() {
        return BOT_NAME;
    }
    private ArrayList<GameState>    shuffle(ArrayList<GameState> gameStates) {
        Collections.shuffle(gameStates);
        return gameStates;
    }
}
