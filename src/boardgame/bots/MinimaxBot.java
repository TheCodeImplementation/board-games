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

public class MinimaxBot implements MoveBot {

    private final String BOT_NAME = "Minimax";
    private Colour maximizingPlayer;
    //METHODS
    @Override public GameState      getMove(GameState gameState, int depth) {
        GameState chosen = null;
        maximizingPlayer = gameState.getPlayerTurn();
        if(!gameState.isTerminal()) {
            double maxValue = Double.NEGATIVE_INFINITY;
            for(GameState successor : shuffle(gameState.getSuccessors()) ) {
                double tmp = Math.max(maxValue, min(successor, depth-1));
                if (tmp >= maxValue) {
                    maxValue = tmp;
                    chosen = successor;
                }
            }
        }
        return chosen;
    }
    private double                  max(GameState gameState, int depth){
        if(depth == 0 || gameState.isTerminal())
            return gameState.evaluate(maximizingPlayer);
        double value = Double.NEGATIVE_INFINITY;
        for(GameState successor : shuffle(gameState.getSuccessors()) ) {
            value = Math.max(value, min(successor, depth-1));
        }
        return value;
    }
    private double                  min(GameState gameState, int depth){
        if(depth == 0 || gameState.isTerminal())
            return gameState.evaluate(maximizingPlayer);
        double value = Double.POSITIVE_INFINITY;
        for(GameState successor : shuffle(gameState.getSuccessors()) ) {
            value = Math.min(value, max(successor, depth-1));
        }
        return value;
    }
    @Override public String         toString() {
        return BOT_NAME;
    }
    private ArrayList<GameState>    shuffle(ArrayList<GameState> boards) {
        Collections.shuffle(boards);
        return boards;
    }
}
