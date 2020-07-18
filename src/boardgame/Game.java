/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame;

public class Game {
    private GameState currentGameState;
    //METHODS
    public void makeMove(Move move) {
        if(currentGameState.isMoveLegal(move)) {
            GameState copy = currentGameState.copy();
            copy.makeMove(move);
            makeMove(copy);
        }
    }
    public void makeMove(GameState gameState) {
        gameState.setFuture(null); //overwrite future board as it's no longer valid
        currentGameState.setFuture(gameState);
        gameState.setPrev(currentGameState);
        currentGameState = gameState;
        gameState.postMoveUINotifications(gameState.getLastMove().getSender());
    }
    public void undoMove() {
        if(currentGameState.getPrev() != null)
            currentGameState = currentGameState.getPrev();
    }
    public void redoMove() {
        if(currentGameState.getFuture() != null)
            currentGameState = currentGameState.getFuture();
    }
    public void undoAll() {
        while(currentGameState.getPrev() != null)
            currentGameState = currentGameState.getPrev();
    }
    public void redoAll() {
        while(currentGameState.getFuture() != null)
            currentGameState = currentGameState.getFuture();
    }
    public void newGame() {
        undoAll();
        currentGameState.setFuture(null);
    }
    //GETTERS
    public void         setCurrentGameState(GameState gameState) {
        currentGameState = gameState;
    }
    public GameState    getCurrentGameState() {
        return currentGameState;
    }
}