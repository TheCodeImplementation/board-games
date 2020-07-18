/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package gui;

import boardgame.*;
import boardgame.bots.*;
import checkers.CheckersState;
import chess.ChessState;
import gui.boards.GUIBoard;
import gui.boards.GUICheckers;
import gui.boards.GUIChess;
import gui.boards.GUIKnightsTour;
import horde.HordeState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import knightstour.KnightsTourState;

public class Controller {
    @FXML StackPane boardHolder;
    @FXML Label     lblFooter;
    //top level buttons
    @FXML Button btnUndoAll;
    @FXML Button btnUndo;
    @FXML Button btnRedoAll;
    @FXML Button btnRedo;
    @FXML Button btnNewGame;
    @FXML Button btnAIMove;
    @FXML Button btnLightPlayer;
    @FXML Button btnDarkPlayer;
    private final Integer[] depthOptions   = {1,2,3,4,5,6,7};
    private MoveBot         lightBot = new MinimaxBot();
    private MoveBot         darkBot  = new MinimaxBot();
    private final String    humanSymbol = "👨";
    private final String    robotSymbol = "🤖";
    private final MoveBot[] aiOptions = {
            new MinimaxBot(),
            new AlphaBetaBot(),
            new OptimalMiniMax()
    };
    private final Game  game = new Game();
    private Thread      aiThread = null;
    private int         lightDepth = 3;
    private int         darkDepth = 3;
    private GUIBoard    gui;
    //METHODS
    public void initialize() {
        btnLightPlayer.setText(humanSymbol);
        btnDarkPlayer.setText(humanSymbol);
        menuChessClicked(null);
    }
    //Game Selection
    @FXML void menuChessClicked(ActionEvent actionEvent) {
        gui = new GUIChess(this);
        boardHolder.getChildren().setAll(gui);
        game.setCurrentGameState(new ChessState());
        refreshGUI();
    }
    @FXML void menuCheckersClicked(ActionEvent actionEvent) {
        gui = new GUICheckers(this);
        boardHolder.getChildren().setAll(gui);
        game.setCurrentGameState(new CheckersState());
        refreshGUI();
    }
    @FXML void menuHordeClicked(ActionEvent actionEvent) {
        gui = new GUIChess(this);
        boardHolder.getChildren().setAll(gui);
        game.setCurrentGameState(new HordeState());
        refreshGUI();
    }
    @FXML void menuKnightsTourClicked(ActionEvent actionEvent) {
        gui = new GUIKnightsTour(this);
        boardHolder.getChildren().setAll(gui);
        game.setCurrentGameState(new KnightsTourState());
        refreshGUI();
    }
    //General game controls
    @FXML void      btnUndoAllClicked(ActionEvent actionEvent) {
        game.undoAll();
        refreshGUI();
    }
    @FXML void      btnUndoClicked(ActionEvent actionEvent) {
        game.undoMove();
        refreshGUI();
    }
    @FXML void      btnRedoClicked(ActionEvent actionEvent) {
        game.redoMove();
        refreshGUI();
    }
    @FXML void      btnRedoAllClicked(ActionEvent actionEvent) {
        game.redoAll();
        refreshGUI();
    }
    @FXML void      btnNewGameClicked(ActionEvent actionEvent) {
        game.newGame();
        refreshGUI();
    }
    @FXML void      btnRotateClicked(ActionEvent actionEvent) {
        gui.rotateBoard();
    }
    public void     makeAIMove() {
        //disabledButtonList = board modifying buttons. Mustn't allow changes while ai is doing work.
        Node[] disabledButtonList = {gui,btnUndoAll,btnUndo,btnRedo,btnRedoAll,btnNewGame,btnAIMove};
        //if makeAIMove is called while aiThread is already busy, eg user spams play-as-computer button, ignore it.
        if(aiThread == null) {
            for (Node n : disabledButtonList)
                n.setDisable(true);
            aiThread = new Thread( () -> {
                //get move from movebot - must call the appropriate algorithm for the player
                GameState aiMove = game.getCurrentGameState().getPlayerTurn() == Colour.LIGHT ?
                        lightBot.getMove(game.getCurrentGameState(), lightDepth) :
                        darkBot.getMove(game.getCurrentGameState(), darkDepth);
                //make move
                if (aiMove != null) {
                    aiMove.getLastMove().setSender(gui);
                    game.makeMove(aiMove);
                    gui.repaint();
                    aiThread = null;
                    if(currentPlayerIsAI())
                        Platform.runLater(() -> makeAIMove());
                }
                for (Node n : disabledButtonList)
                        n.setDisable(false);
            });
            aiThread.start();
        }
    }
    public boolean  currentPlayerIsAI() {
        if(game.getCurrentGameState().getPlayerTurn() == Colour.LIGHT)
            return btnLightPlayer.getText().equals(robotSymbol);
        else
            return btnDarkPlayer.getText().equals(robotSymbol);
    }
    @FXML void      togglePlayerAI(ActionEvent actionEvent) {
        Button btn = ((Button) actionEvent.getSource());
        btn.setText( btn.getText().equals(humanSymbol) ? robotSymbol :  humanSymbol);
        if(currentPlayerIsAI())
            makeAIMove();
    }
    @FXML void      chooseAIBot(MouseEvent mouseEvent) {
        if(mouseEvent.isSecondaryButtonDown())
            if(mouseEvent.getSource() == btnLightPlayer) {
                lightBot = PopUp.presentOptions(aiOptions, false);
                lightDepth = PopUp.presentOptions(depthOptions, true);
            }
            else {
                darkBot = PopUp.presentOptions(aiOptions, false);
                darkDepth = PopUp.presentOptions(depthOptions, true);
            }
    }
    private void    refreshGUI() {
        gui.repaint();
        aiThread = null;
        btnLightPlayer.setText("👨");
        btnDarkPlayer.setText("👨");
    }
    //GETTERS & SETTERS
    public Game getGame() {
        return game;
    }
}