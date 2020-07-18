/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package checkers.pieces;

import boardgame.*;
import checkers.CheckerPiece;

/*
NB: This class has no more functionality than the super class. I considered renaming CheckerPiece, King, but then
that would make Checker is a 'instanceof' King, which we can't have.
 */
public class King extends CheckerPiece {
    //CONSTRUCTOR
    public King(Colour colour) {
        super(colour, "◔", "◕", 3);
    } //3's an arbitrary choice.
}
