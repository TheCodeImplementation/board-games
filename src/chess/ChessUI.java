/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package chess;

import boardgame.Colour;
import boardgame.Piece;
import boardgame.Position;
import boardgame.UserInterface;

public interface ChessUI extends UserInterface {
    Piece getPromotionType(Piece[] options);
    void notifyCheck(Position kingsPosition);
    void notifyCheckmate(Position kingsPosition, Colour winner);
    void notifyStalemate();
    void notifyInsufficientMaterial();
    void notifyThreefoldRepetition();
}
