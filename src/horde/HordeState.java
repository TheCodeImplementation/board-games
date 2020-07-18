/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package horde;

import boardgame.*;
import chess.ChessState;

public class HordeState extends ChessState {
    //CONSTRUCTORS
    public HordeState() {
        super("a-b-c-d-e-f-g-h\n" +
                        "8|♜|♞|♝|♛|♚|♝|♞|♜|8\n" +
                        "7|♟|♟|♟|♟|♟|♟|♟|♟|7\n" +
                        "6| | | | | | | | |6\n" +
                        "5| |♙|♙| | |♙|♙| |5\n" +
                        "4|♙|♙|♙|♙|♙|♙|♙|♙|4\n" +
                        "3|♙|♙|♙|♙|♙|♙|♙|♙|3\n" +
                        "2|♙|♙|♙|♙|♙|♙|♙|♙|2\n" +
                        "1|♙|♙|♙|♙|♙|♙|♙|♙|1\n" +
                          "a-b-c-d-e-f-g-h"
        );
    }
    //METHODS
    @Override public boolean    isCheck(Colour playerColour) {
        return playerColour == Colour.DARK
            && super.isCheck(playerColour);
    }
    @Override public void       postMoveUINotifications(UserInterface ui) {
        if(noLightPieceLeft())
            ui.notifyGameOver(Colour.DARK);
        else
            super.postMoveUINotifications(ui);
    }
    @Override public boolean    isTerminal() {
        return noLightPieceLeft()
            || super.isTerminal();
    }
    public boolean              noLightPieceLeft() {
        return getPlayersPositions(Colour.LIGHT).size() == 0;
    }
}