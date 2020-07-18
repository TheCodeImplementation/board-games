/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package gui.boards;

import boardgame.*;
import chess.ChessUI;
import gui.Controller;
import gui.PopUp;
import javafx.scene.paint.Color;

public class GUIChess extends GUIBoard implements ChessUI {
    //CONSTRUCTOR
    public GUIChess(Controller controller) {
        super(8, 8, controller);
    }
    //METHODS
    @Override public Piece  getPromotionType(Piece[] options) {
        return PopUp.presentOptions(options, true);
    }
    @Override public void   notifyCheck(Position kingsPosition) {
        flashColour(getSquareAt(kingsPosition.getX(), kingsPosition.getY()), Color.RED);
    }
    @Override public void   notifyCheckmate(Position kingsPosition, Colour winner) {
        flashColour(getSquareAt(kingsPosition.getX(), kingsPosition.getY()), Color.RED);
        promptPlayer("CHECKMATE: " + winner.toString().toLowerCase() + " wins");
    }
    @Override public void   notifyStalemate() {
        promptPlayer("Draw: stalemate");
    }
    @Override public void   notifyInsufficientMaterial() {
        promptPlayer("DRAW: insufficient material to continue");

    }
    @Override public void   notifyThreefoldRepetition() {
        promptPlayer("DRAW: threefold repetition");
    }
}
