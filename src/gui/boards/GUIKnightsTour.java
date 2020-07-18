/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package gui.boards;

import boardgame.Colour;
import boardgame.Move;
import boardgame.Position;
import gui.Controller;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import knightstour.KnightsTourState;

public class GUIKnightsTour extends GUIBoard {
    //CONSTRUCTORS
    public GUIKnightsTour(Controller controller) {
        super(8, 8, controller);
    }
    //METHODS
    public Button           createSquare(int x, int y) {
        Button square = new Button();
        square.setPickOnBounds(false); //The pick bounds 'wander' upwards is this isn't set. Don't know why though?
        square.getStyleClass().setAll("square", "greenFocusHighlight");
        square.getStyleClass().add( (x + y) % 2 == 1 ? "lightSquare" : "darkSquare");

        square.setOnAction(e -> {
            Move move = controller.getGame().getCurrentGameState().getLastMove();
            Position p1;
            if(move == null)
                p1 = new Position(0,7);
            else
                p1 = move.getDestination();
            requestFocus();
            controller.getGame().makeMove(new Move(p1, new Position(x,y), this));
            repaint();
            Platform.runLater( () -> {
                if(controller.currentPlayerIsAI())
                    controller.makeAIMove();
            });
        });
        return square;
    }
    @Override public void   repaint() {
        super.repaint();
        Platform.runLater( () -> {

            for(Node n : getChildren())
                n.getStyleClass().removeAll("knightsTourHighlight");
            KnightsTourState state = (KnightsTourState)controller.getGame().getCurrentGameState();

            for(Position position : state.traversed)
                getSquareAt(position.getX(), position.getY()).getStyleClass().add("knightsTourHighlight");
        });
    }
    @Override public void   notifyGameOver(Colour winner) {
        KnightsTourState state = ((KnightsTourState) controller.getGame().getCurrentGameState());
        promptPlayer("Game Over! You did it in " + state.traversed.size() + " moves.");
    }
}
