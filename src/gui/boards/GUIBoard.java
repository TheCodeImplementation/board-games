/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package gui.boards;

import boardgame.*;
import gui.Controller;
import gui.PopUp;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public abstract class GUIBoard extends GridPane implements UserInterface{
    protected Controller                controller;
    protected int                       width, height;
    protected HashMap<String, String>   pieceImages = new HashMap<>();//<symbol, styleclass> (styleclass has image url)
    protected Queue<Position>           positions = new ArrayDeque<>();
    //CONSTRUCTOR
    public GUIBoard(int width, int height, Controller controller) {
        getStyleClass().setAll("board");
        this.width = width;
        this.height = height;
        this.controller = controller;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                add(createSquare(x,y), x, height-y);
            }
        }
    }
    //METHODS
    public Button           createSquare(int x, int y) {
        Button square = new Button();
        square.setPickOnBounds(false); //The pick bounds 'wander' upwards is this isn't set. Don't know why though?
        square.getStyleClass().setAll("square", "greenFocusHighlight");
        square.getStyleClass().add( (x + y) % 2 == 1 ? "lightSquare" : "darkSquare");

        square.setOnAction(e -> {
            if(!isSelectionValid(x,y))
                requestFocus(); //remove green highlight
            else {
                positions.add(new Position(x, y));
                if (positions.size() == 2) {
                    requestFocus(); //remove green highlight
                    Move move = new Move(positions.remove(), positions.remove(), this);
                    controller.getGame().makeMove(move);
                    repaint();
                    Platform.runLater(() -> {
                        //if next player turn is the computer, call the AI.
                        if (controller.currentPlayerIsAI())
                            controller.makeAIMove();
                    });
                }
            }
        });
        return square;
    }
    public void             repaint() {
        Platform.runLater( () -> {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Piece piece = controller.getGame().getCurrentGameState().getPiece(new Position(x,y));
                    Button square = getSquareAt(x,y);
                    if(piece == null) {
                        square.setText(null);
                        square.getStyleClass().removeAll(pieceImages.values());
                    }else {
                        //if there is an image file associated with this piece...
                        if (pieceImages.containsKey(piece.toString())) {
                            square.getStyleClass().removeAll(pieceImages.values());
                            square.getStyleClass().add(pieceImages.get(piece.toString()));
                        }else { //else just use the internal symbol, ie. ♝, ◔, etc
                            square.setText(piece.toString());
                        }
                    }
                }
            }
            highlightLastMove();
        });
    }
    public void             rotateBoard() {
        setRotate(getRotate() + 180);
        for (Node square : getChildren())
            square.setRotate(square.getRotate() + 180);
    }
    public void             promptPlayer(String message) {
        Platform.runLater( () -> PopUp.presentMessage(message));
    }
    @Override public void   notifyGameOver(Colour winner) {
        promptPlayer("Game Over: " + winner.toString().toLowerCase() + " wins.");
    }
    protected void          highlightLastMove() {
        Move move = controller.getGame().getCurrentGameState().getLastMove();
        if(move != null) {
            flashColour(getSquareAt(move.getX1(), move.getY1()), Color.GREEN);
            flashColour(getSquareAt(move.getX2(), move.getY2()), Color.GREEN);
        }
    }
    protected void          flashColour(Button button, Color colour){
        //Thanks to Stack Overflow user negste, for help with this code: https://stackoverflow.com/a/41672541
        InnerShadow s = new InnerShadow();
        s.setWidth(250);
        button.setEffect(s);

        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(Interpolator.EASE_OUT);
            }
            @Override
            protected void interpolate(double frac) {
                s.setColor(colour.deriveColor(0,255,255,1-frac));
            }
        };
        animation.play();
        animation.setOnFinished(e -> button.setEffect(null));
    }
    protected boolean       isSelectionValid(int x, int y) {
        //Simple validation check.
        GameState gameState = controller.getGame().getCurrentGameState();
        Piece chosen = gameState.getPiece(new Position(x, y));
        //The first selection, position.size = 0, can't be an empty square nor enemy piece.
        //The second selection, position.size = 1, can be whatever.
        return ( chosen != null && chosen.getColour() != gameState.getPlayerTurn().opponent() )
            || positions.size() == 1;
    }
    //GETTERS
    public Button getSquareAt(int x, int y) {
        return (Button)getChildren().get( (y * 8) + x );
    }
}
