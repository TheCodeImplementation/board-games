/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package gui.boards;

import gui.Controller;

public class GUICheckers extends GUIBoard {
    public GUICheckers(Controller controller) {
        super(8, 8, controller);
        getStylesheets().add("/gui/resources/checkers/checkers.css");
        pieceImages.put("○" , "lightMan");
        pieceImages.put("●" , "darkMan");
        pieceImages.put("◔" , "lightKing");
        pieceImages.put("◕" , "darkKing");

        //disable all dark squares
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if((x + y) % 2 == 0) {
                    getSquareAt(x, y).setDisable(true);
                }
            }
        }
    }
}